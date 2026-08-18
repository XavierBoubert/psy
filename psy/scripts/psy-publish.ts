import { mkdir, readFile, readdir, rm, stat, writeFile } from 'node:fs/promises';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { convertirEnPdf } from './md2pdf.ts';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const SOURCE = resolve(PROJECT_ROOT, 'companion/inputs/programme.json');
const BIBLIOTHEQUE = resolve(PROJECT_ROOT, 'companion/inputs/bibliotheque');
const SUPERVISIONS = resolve(PROJECT_ROOT, 'superviseur/outputs');

const USAGE = 'Usage: psy-publish <dossier-de-transit-drive> [--seance] [--refaire]';

const TYPES = ['ecran', 'exercice', 'questionnaire', 'demarche', 'fiche', 'seance-duo'] as const;
const POUR = ['aide', 'patient'] as const;
const QUAND = ['aujourdhui', 'au_besoin', 'sans_date'] as const;
const RUBRIQUES = ['crise', 'therapie', 'bilan', 'documentation'] as const;
const ECRANS = ['check-in', 'mot-code', 'tension-appliquee', 'phrase-soignant'] as const;

const CLES_NON_TEXTUELLES = ['id', 'type', 'quand', 'rubrique', 'ecran', 'document', 'pour'] as const;

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

const estTexteNonVide = (value: unknown): boolean => typeof value === 'string' && value.trim().length > 0;

// C10 — l'aidant execute un deroule, elle ne juge jamais (elle n'est pas therapeute).
const JUGEMENTS: ReadonlyArray<Interdit> = [
  {
    motif: /\b(estime|evalue|juge|apprecie|decide)\b[^.]{0,40}\b(si|s'il|comment|quand)\b|\ba toi de voir\b|\bsi tu penses que\b/,
    raison: 'consigne qui demande un jugement a l\'aidant — elle execute un deroule, elle ne decide pas (C10)',
  },
  {
    motif: /\brassure(-le| le)?\b|\bcalme-le\b|\bdis-lui que ca va\b|\bimprovise\b|\badapte selon\b/,
    raison: 'consigne hors script confiee a l\'aidant — elle ne fait que ce que l\'ecran affiche (C10)',
  },
  {
    motif: /\bdiagnostic\b|\bdiagnostique\b|\bson score\b|\btrouble du spectre\b|\bagoraphobie\b|\balexithymie\b|\bapnee\b|\bnash\b/,
    raison: 'contenu clinique adresse a l\'aidant — elle lit des consignes, pas un dossier (C10)',
  },
];

const problemesJugement = (etape: Record<string, unknown>): ReadonlyArray<string> => {
  const textes = textesDe(etape, '').map(normalise);

  return JUGEMENTS.flatMap((interdit) => (textes.some((texte) => interdit.motif.test(texte)) ? [interdit.raison] : []));
};

const problemeConsigne = (consigne: unknown, rang: number): string | null => {
  if (!isRecord(consigne)) return `consigne ${rang + 1} : ce n'est pas un objet`;

  if (!POUR.some((connu) => connu === consigne['pour'])) return `consigne ${rang + 1} : « pour » vaut ${String(consigne['pour'])}, attendu aide ou patient`;

  if (!estTexteNonVide(consigne['consigne'])) return `consigne ${rang + 1} : texte absent`;

  return typeof consigne['secondes'] === 'number' && consigne['secondes'] > 0
    ? null
    : `consigne ${rang + 1} : « secondes » absent ou nul — une seance a deux est chronometree`;
};

// Une seance a deux implique une tierce personne : ces gardes sont les garde-fous eux-memes, pas de simples validations de forme.
const problemesDuo = (etape: Record<string, unknown>): ReadonlyArray<string> => {
  const sequence = etape['sequence'];
  const arret = etape['arret'];
  const avant = etape['avant'];

  const gardes = [
    etape['entrainement_requis'] === true
      ? null
      : 'entrainement_requis doit valoir true — la premiere seance reelle ne peut pas etre la premiere fois que l\'aide decouvre le deroule',
    estTexteNonVide(etape['signal_arret'])
      ? null
      : 'signal_arret absent — Xavier doit pouvoir arreter SANS PARLER, c\'est exactement ce qui tombe en premier',
    Array.isArray(arret) && arret.length >= 2 && arret.every(estTexteNonVide)
      ? null
      : 'arret : au moins deux criteres d\'arret, non vides',
    Array.isArray(avant) && avant.every(estTexteNonVide) ? null : 'avant : liste de textes non vides',
    etape['sortie_libre'] === true ? null : 'sortie_libre doit valoir true',
  ];

  if (!Array.isArray(sequence) || sequence.length === 0) return [...gardes.filter((p): p is string => p !== null), 'sequence absente'];

  return [...gardes, ...sequence.map(problemeConsigne), ...problemesJugement(etape)].filter(
    (probleme): probleme is string => probleme !== null,
  );
};

// 🔴 Un item perdu produit un score faux, donc faussement rassurant : Kokoro ecarte le questionnaire entier,
// et le PC refuse la publication plutot que de laisser partir une echelle amputee en silence.
const problemeChoix = (choix: unknown, question: string, rang: number): string | null => {
  if (!isRecord(choix)) return `${question}, choix ${rang + 1} : ce n'est pas un objet`;

  if (!Number.isInteger(choix['valeur'])) return `${question}, choix ${rang + 1} : « valeur » absente ou non entiere`;

  return estTexteNonVide(choix['libelle']) ? null : `${question}, choix ${rang + 1} : « libelle » absent`;
};

const problemeQuestion = (question: unknown, rang: number): ReadonlyArray<string> => {
  if (!isRecord(question)) return [`question ${rang + 1} : ce n'est pas un objet`];

  const nom = typeof question['id'] === 'string' ? question['id'] : `question ${rang + 1}`;
  const choix = question['choix'];

  const communs = [
    typeof question['id'] === 'string' && /^[a-z0-9-]+$/.test(question['id'])
      ? null
      : `${nom} : id absent ou hors kebab-case`,
    estTexteNonVide(question['enonce']) ? null : `${nom} : enonce absent`,
    Array.isArray(choix) && choix.length >= 2
      ? null
      : `${nom} : moins de deux choix — une question est toujours un choix ferme, jamais une saisie de texte`,
  ];

  const propres = Array.isArray(choix) ? choix.map((un: unknown, place: number) => problemeChoix(un, nom, place)) : [];

  return [...communs, ...propres].filter((probleme): probleme is string => probleme !== null);
};

const problemesDeForme = (etape: Record<string, unknown>): ReadonlyArray<string> => {
  const type = etape['type'];
  const quand = etape['quand'];

  const rubrique = etape['rubrique'];

  const communs = [
    typeof etape['id'] === 'string' && /^[a-z0-9-]+$/.test(etape['id']) ? null : 'id absent ou hors kebab-case',
    typeof etape['titre'] === 'string' && etape['titre'].length > 0 ? null : 'titre absent',
    TYPES.some((connu) => connu === type) ? null : `type inconnu : ${String(type)}`,
    RUBRIQUES.some((connue) => connue === rubrique) ? null : `rubrique inconnue : ${String(rubrique)}`,
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

      const identifiants = questions.flatMap((question: unknown) =>
        isRecord(question) && typeof question['id'] === 'string' ? [question['id']] : [],
      );
      const doublons = identifiants.filter((id, rang) => identifiants.indexOf(id) !== rang);

      return [
        ...doublons.map((id) => `question en double : ${id} — un id relie un item a sa reponse`),
        ...questions.flatMap(problemeQuestion),
      ];
    }

    if (type === 'demarche') {
      return [typeof etape['detail'] === 'string' ? null : 'detail absent'];
    }

    if (type === 'seance-duo') return problemesDuo(etape);

    const texte = etape['texte'];
    const document = etape['document'];

    if (typeof document === 'string') {
      return [
        /^[a-z0-9-]+$/.test(document) ? null : `document hors kebab-case : ${document}`,
        texte === undefined ? null : 'une fiche porte « texte » OU « document », jamais les deux',
      ];
    }

    return [typeof texte === 'string' ? null : 'ni « texte » ni « document » — une fiche doit porter l\'un des deux'];
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
    typeof parsed['supervision'] === 'string' && parsed['supervision'].length > 0
      ? null
      : 'supervision absente — rien ne se publie sans une passe du superviseur (superviseur/README.md §4)',
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

const champFrontmatter = (contenu: string, champ: string): string | null => {
  const trouve = new RegExp(`^${champ}\\s*:\\s*(.+?)\\s*$`, 'm').exec(contenu);

  return trouve?.[1] ?? null;
};

const relireSupervision = async (parsed: unknown): Promise<ReadonlyArray<string>> => {
  if (!isRecord(parsed) || typeof parsed['supervision'] !== 'string') return [];

  const nom = parsed['supervision'];
  const chemin = join(SUPERVISIONS, `${nom}.md`);

  const contenu = await readFile(chemin, 'utf8').catch(() => null);
  if (contenu === null) return [`supervision introuvable : superviseur/outputs/${nom}.md`];

  const version = String(parsed['version']);

  return [
    champFrontmatter(contenu, 'porte_sur') === 'programme' ? null : `${nom} : « porte_sur » n'est pas « programme »`,
    champFrontmatter(contenu, 'version') === version
      ? null
      : `${nom} : la supervision porte sur la version ${String(champFrontmatter(contenu, 'version'))}, le programme est en version ${version} — une version supervisee hier ne publie pas aujourd'hui`,
    champFrontmatter(contenu, 'verdict') === 'publiable'
      ? null
      : `${nom} : verdict « ${String(champFrontmatter(contenu, 'verdict'))} » — seul « publiable » autorise la publication`,
  ].filter((probleme): probleme is string => probleme !== null);
};

type Fiche = {
  readonly id: string;
  readonly contenu: string;
  readonly source: string;
  readonly modifieLe: number;
};

const lireBibliotheque = async (): Promise<ReadonlyArray<Fiche>> => {
  const noms = await readdir(BIBLIOTHEQUE).catch(() => null);
  if (noms === null) return [];

  const fichiers = noms.filter((nom) => nom.endsWith('.md') && nom !== 'README.md');

  return Promise.all(
    fichiers.map(async (nom) => {
      const source = join(BIBLIOTHEQUE, nom);

      return {
        id: nom.replace(/\.md$/, ''),
        contenu: await readFile(source, 'utf8'),
        source,
        modifieLe: (await stat(source)).mtimeMs,
      };
    }),
  );
};

const documentsAppeles = (parsed: unknown): ReadonlyArray<string> => {
  if (!isRecord(parsed) || !Array.isArray(parsed['etapes'])) return [];

  return parsed['etapes'].flatMap((etape: unknown) =>
    isRecord(etape) && typeof etape['document'] === 'string' ? [etape['document']] : [],
  );
};

const relireBibliotheque = (parsed: unknown, fiches: ReadonlyArray<Fiche>): ReadonlyArray<string> => {
  const appeles = documentsAppeles(parsed);

  const manquants = appeles
    .filter((id) => !fiches.some((fiche) => fiche.id === id))
    .map((id) => `document introuvable : companion/inputs/bibliotheque/${id}.md`);

  const fautives = fiches.flatMap((fiche) => {
    const texte = normalise(fiche.contenu);

    return INTERDITS.flatMap((interdit) => (interdit.motif.test(texte) ? [`bibliotheque/${fiche.id}.md — ${interdit.raison}`] : []));
  });

  return [...manquants, ...fautives];
};

// La documentation part a tout moment ; les etapes qui font agir se decident avec Xavier, en seance.
const FONT_AGIR = TYPES.filter((type) => type !== 'fiche');

const canonique = (valeur: unknown): string => {
  if (Array.isArray(valeur)) return `[${valeur.map(canonique).join(',')}]`;

  if (isRecord(valeur)) {
    return `{${Object.keys(valeur)
      .sort()
      .map((cle) => `${JSON.stringify(cle)}:${canonique(valeur[cle])}`)
      .join(',')}}`;
  }

  return JSON.stringify(valeur) ?? 'null';
};

const etapesQuiFontAgir = (parsed: unknown): ReadonlyMap<string, string> => {
  if (!isRecord(parsed) || !Array.isArray(parsed['etapes'])) return new Map();

  const retenues = parsed['etapes'].filter(
    (etape: unknown) => isRecord(etape) && FONT_AGIR.some((type) => type === etape['type']) && typeof etape['id'] === 'string',
  );

  return new Map(retenues.map((etape: Record<string, unknown>) => [String(etape['id']), canonique(etape)]));
};

const lireJson = async (chemin: string): Promise<unknown> => {
  const brut = await readFile(chemin, 'utf8').catch(() => null);
  if (brut === null) return null;

  try {
    return JSON.parse(brut);
  } catch {
    return null;
  }
};

const relireHorsSeance = async (parsed: unknown, cible: string): Promise<ReadonlyArray<string>> => {
  const publiees = etapesQuiFontAgir(await lireJson(join(cible, 'programme.json')));
  const aPublier = etapesQuiFontAgir(parsed);

  const touchees = [
    ...[...aPublier].filter(([id, forme]) => publiees.get(id) !== forme).map(([id]) => id),
    ...[...publiees.keys()].filter((id) => !aPublier.has(id)),
  ];

  return touchees.map(
    (id) => `${id} — etape qui fait agir, nouvelle ou modifiee : elle se decide avec Xavier, a la cloture d'une seance (--seance)`,
  );
};

type Publication = {
  readonly converties: ReadonlyArray<string>;
  readonly reprises: number;
  readonly retirees: ReadonlyArray<string>;
};

const estAJour = async (fiche: Fiche, destination: string): Promise<boolean> => {
  const pdf = await stat(destination).catch(() => null);

  return pdf !== null && pdf.mtimeMs >= fiche.modifieLe;
};

// Le Markdown ne part pas : il passe la supervision au depot, et Kokoro ne recoit que le PDF.
const publier = async (
  cible: string,
  brut: string,
  fiches: ReadonlyArray<Fiche>,
  refaireTout: boolean,
): Promise<Publication> => {
  const dossier = join(cible, 'bibliotheque');
  await mkdir(dossier, { recursive: true });

  const attendus = fiches.map((fiche) => `${fiche.id}.pdf`);
  const presents = await readdir(dossier).catch(() => []);
  const retirees = presents.filter((nom) => !attendus.some((attendu) => attendu === nom));
  await Promise.all(retirees.map((nom) => rm(join(dossier, nom), { force: true })));

  const cibles = await Promise.all(
    fiches.map(async (fiche) => {
      const destination = join(dossier, `${fiche.id}.pdf`);

      return { fiche, destination, aJour: !refaireTout && (await estAJour(fiche, destination)) };
    }),
  );

  const aConvertir = cibles.filter(({ aJour }) => !aJour);
  await convertirEnPdf(aConvertir.map(({ fiche, destination }) => ({ sourcePath: fiche.source, destinationPath: destination })));

  await writeFile(join(cible, 'programme.json'), brut, 'utf8');

  return {
    converties: aConvertir.map(({ fiche }) => fiche.id),
    reprises: cibles.length - aConvertir.length,
    retirees,
  };
};

type Options = {
  readonly transit: string;
  readonly seance: boolean;
  readonly refaireTout: boolean;
};

const lireOptions = (argv: readonly string[]): Options => {
  const transit = argv.find((argument) => !argument.startsWith('--'));
  if (!transit) throw new Error(USAGE);

  return {
    transit,
    seance: argv.some((argument) => argument === '--seance'),
    refaireTout: argv.some((argument) => argument === '--refaire'),
  };
};

const main = async (): Promise<void> => {
  const options = lireOptions(process.argv.slice(2));
  const cible = resolve(PROJECT_ROOT, options.transit);

  const brut = await readFile(SOURCE, 'utf8');
  const parsed: unknown = JSON.parse(brut);
  const fiches = await lireBibliotheque();

  const problemes = [
    ...relireProgramme(parsed),
    ...(await relireSupervision(parsed)),
    ...relireBibliotheque(parsed, fiches),
    ...(options.seance ? [] : await relireHorsSeance(parsed, cible)),
  ];

  if (problemes.length > 0) {
    console.error('Publication refusee — le contenu enfreint les invariants du dispositif :\n');
    problemes.forEach((probleme) => console.error(`  ${probleme}`));
    console.error('\nRien n\'a ete publie. Un refus se corrige, il ne se contourne pas.');

    process.exitCode = 1;

    return;
  }

  const publication = await publier(cible, brut, fiches, options.refaireTout);

  const etapes = isRecord(parsed) && Array.isArray(parsed['etapes']) ? parsed['etapes'].length : 0;
  const version = isRecord(parsed) ? String(parsed['version']) : '?';
  const supervision = isRecord(parsed) ? String(parsed['supervision']) : '?';

  console.log(`publie   programme.json — version ${version}, ${etapes} etapes`);
  console.log(
    `         bibliotheque — ${fiches.length} fiche(s) en PDF : ${publication.converties.length} converties, ${publication.reprises} inchangees`,
  );
  publication.converties.forEach((id) => console.log(`         converti ${id}.pdf`));
  publication.retirees.forEach((nom) => console.log(`         retire   ${nom}`));
  console.log(`vise par ${supervision}`);
  console.log(`portee   ${options.seance ? 'seance — tout le programme' : 'hors seance — documentation seule'}`);
  console.log(`vers     ${cible}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
