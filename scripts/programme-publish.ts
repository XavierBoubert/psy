import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const SOURCE = resolve(PROJECT_ROOT, 'psy/programme/programme.json');

const USAGE = 'Usage: programme-publish <dossier-de-transit-drive>';

const TYPES = ['ecran', 'exercice', 'questionnaire', 'demarche', 'fiche'] as const;
const QUAND = ['aujourdhui', 'au_besoin', 'sans_date'] as const;
const ECRANS = ['check-in', 'mot-code', 'tension-appliquee', 'phrase-soignant'] as const;

const CLES_NON_TEXTUELLES = ['id', 'type', 'quand', 'ecran'] as const;

type Interdit = {
  readonly motif: RegExp;
  readonly raison: string;
};

const INTERDITS: ReadonlyArray<Interdit> = [
  {
    motif: /\b(imagine|imaginer|imaginez|visualise|visualiser|represente-toi|image mentale|lieu sur)\b/,
    raison: 'consigne de visualisation — aphantasie mesuree a 18/80',
  },
  {
    motif: /\bsur 10\b|\bnote (ton|ta)\b|\bton niveau de\b|\bevalue (ton|ta)\b|\ba combien tu te sens\b/,
    raison: 'cotation d\'un ressenti — R6, on cote des comportements observables',
  },
  {
    motif: /\bd'affilee\b|\bconsecutif|\bserie\b|\bregularite\b|\bstreak\b|jour \d+ sur|% de l'objectif/,
    raison: 'compteur de regularite ou de serie — zero streak',
  },
  {
    motif: /\b3114\b|\bsamu\b|\bpompiers\b|(appel\w*|composer|numero)[^.]{0,24}\b(15|112|114)\b/,
    raison: 'numero d\'appel d\'urgence — retires du dispositif le 10/08/2026',
  },
  {
    motif: /\bas-tu besoin\b|\bquand tu sens\b|\baux premiers signes\b|\bsi tu sens\b/,
    raison: 'declenchement sur un prodrome — les reperes sont exterieurs, jamais une sensation',
  },
  {
    motif: /\bvenlafaxine\b|\balprazolam\b|\bparoxetine\b|\bdose\b|\bposologie\b|\bcachet\b|\bcomprime\b|\bton traitement\b/,
    raison: 'contenu touchant au traitement — non-substitution, ca part au brief Dr Isorni',
  },
  {
    motif: /\bdetends-toi\b|\bdetente\b|\brespire lentement\b|\brespiration lente\b|\brelaxation\b/,
    raison: 'consigne de relaxation — deletere sur un episode vasovagal',
  },
];

const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null && !Array.isArray(value);

const normalise = (texte: string): string =>
  texte
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .replace(/[‘’]/g, "'")
    .toLowerCase();

const textesDe = (value: unknown, cle: string): ReadonlyArray<string> => {
  if (CLES_NON_TEXTUELLES.some((exclue) => exclue === cle)) return [];

  if (typeof value === 'string') return [value];

  if (Array.isArray(value)) return value.flatMap((item) => textesDe(item, ''));

  if (isRecord(value)) return Object.entries(value).flatMap(([sousCle, sousValeur]) => textesDe(sousValeur, sousCle));

  return [];
};

const problemesInterdits = (etape: Record<string, unknown>): ReadonlyArray<string> => {
  const textes = textesDe(etape, '').map(normalise);

  return INTERDITS.flatMap((interdit) =>
    textes.some((texte) => interdit.motif.test(texte)) ? [interdit.raison] : [],
  );
};

const problemesDeForme = (etape: Record<string, unknown>): ReadonlyArray<string> => {
  const type = etape['type'];
  const quand = etape['quand'];

  const communs = [
    typeof etape['id'] === 'string' && /^[a-z0-9-]+$/.test(etape['id']) ? null : 'id absent ou hors kebab-case',
    typeof etape['titre'] === 'string' && etape['titre'].length > 0 ? null : 'titre absent',
    TYPES.some((connu) => connu === type) ? null : `type inconnu : ${String(type)}`,
    QUAND.some((connu) => connu === quand) ? null : `quand inconnu : ${String(quand)}`,
    etape['duree_minutes'] === undefined || typeof etape['duree_minutes'] === 'number'
      ? null
      : 'duree_minutes n\'est pas un nombre',
  ];

  const propres = ((): ReadonlyArray<string | null> => {
    if (type === 'ecran') {
      return [ECRANS.some((connu) => connu === etape['ecran']) ? null : `ecran inconnu : ${String(etape['ecran'])}`];
    }

    if (type === 'exercice') {
      return [
        typeof etape['consigne'] === 'string' ? null : 'consigne absente',
        typeof etape['minuteur_secondes'] === 'number' ? null : 'minuteur_secondes absent',
        etape['sortie_libre'] === true ? null : 'sortie_libre doit valoir true — sortir avant la fin est toujours permis',
      ];
    }

    if (type === 'questionnaire') {
      const questions = etape['questions'];

      if (!Array.isArray(questions) || questions.length === 0) return ['questions absentes'];

      return questions.map((question: unknown, rang: number) =>
        isRecord(question) && typeof question['enonce'] === 'string' && Array.isArray(question['choix'])
          ? null
          : `question ${rang + 1} sans enonce ou sans choix ferme`,
      );
    }

    if (type === 'demarche') {
      return [typeof etape['detail'] === 'string' ? null : 'detail absent'];
    }

    return [typeof etape['texte'] === 'string' ? null : 'texte absent'];
  })();

  return [...communs, ...propres].filter((probleme): probleme is string => probleme !== null);
};

const relire = (etape: unknown, rang: number): ReadonlyArray<string> => {
  if (!isRecord(etape)) return [`etape ${rang + 1} : ce n'est pas un objet`];

  const nom = typeof etape['id'] === 'string' ? etape['id'] : `etape ${rang + 1}`;

  return [...problemesDeForme(etape), ...problemesInterdits(etape)].map((probleme) => `${nom} — ${probleme}`);
};

const relireProgramme = (parsed: unknown): ReadonlyArray<string> => {
  if (!isRecord(parsed)) return ['la racine n\'est pas un objet'];

  const etapes = parsed['etapes'];

  const entete = [
    typeof parsed['version'] === 'number' ? null : 'version absente ou non numerique',
    typeof parsed['publie_le'] === 'string' ? null : 'publie_le absent',
    Array.isArray(etapes) ? null : 'etapes absentes',
  ].filter((probleme): probleme is string => probleme !== null);

  if (!Array.isArray(etapes)) return entete;

  const identifiants = etapes.flatMap((etape) => (isRecord(etape) && typeof etape['id'] === 'string' ? [etape['id']] : []));
  const doublons = identifiants.filter((id, rang) => identifiants.indexOf(id) !== rang);

  return [
    ...entete,
    ...doublons.map((id) => `id en double : ${id} — un id relie une reponse a son etape, il doit etre unique`),
    ...etapes.flatMap(relire),
  ];
};

const main = async (): Promise<void> => {
  const [transit] = process.argv.slice(2);
  if (!transit) throw new Error(USAGE);

  const raw = await readFile(SOURCE, 'utf8');
  const parsed: unknown = JSON.parse(raw);

  const problemes = relireProgramme(parsed);

  if (problemes.length > 0) {
    console.error('Publication refusee — le programme enfreint les invariants du dispositif :\n');
    problemes.forEach((probleme) => console.error(`  ${probleme}`));
    console.error('\nRien n\'a ete publie. Corriger psy/programme/programme.json, puis relancer.');

    process.exitCode = 1;

    return;
  }

  const cible = resolve(PROJECT_ROOT, transit);
  await mkdir(cible, { recursive: true });
  await writeFile(join(cible, 'programme.json'), raw, 'utf8');

  const etapes = isRecord(parsed) && Array.isArray(parsed['etapes']) ? parsed['etapes'].length : 0;
  const version = isRecord(parsed) ? String(parsed['version']) : '?';

  console.log(`publie   programme.json — version ${version}, ${etapes} etapes`);
  console.log(`vers     ${cible}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
