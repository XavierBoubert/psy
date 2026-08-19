import { mkdir, readFile, readdir, rm, stat, writeFile } from 'node:fs/promises';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { convertirEnPdf } from './md2pdf.ts';
import { ProgrammeSchema, TYPES } from './schemas/programme.ts';
import { decrire } from './schemas/problemes.ts';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const SOURCE = resolve(PROJECT_ROOT, 'companion/inputs/programme.json');
const BIBLIOTHEQUE = resolve(PROJECT_ROOT, 'companion/inputs/bibliotheque');
const BILANS = resolve(PROJECT_ROOT, 'companion/inputs/bilans');
const SUPERVISIONS = resolve(PROJECT_ROOT, 'superviseur/outputs');

const USAGE = 'Usage: psy-publish <dossier-de-transit-drive> [--seance] [--refaire]';

const CLES_NON_TEXTUELLES = ['id', 'type', 'quand', 'rubrique', 'ecran', 'document', 'pour', 'date'] as const;

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

const idDeLetape = (parsed: unknown, rang: number): string => {
  const etapes = isRecord(parsed) ? parsed['etapes'] : null;
  const etape = Array.isArray(etapes) ? etapes[rang] : null;

  return isRecord(etape) && typeof etape['id'] === 'string' ? etape['id'] : `etape ${rang + 1}`;
};

const relireProgramme = (parsed: unknown): ReadonlyArray<string> => {
  const relu = ProgrammeSchema.safeParse(parsed);

  if (relu.success) return [];

  return relu.error.issues.map((issue) =>
    issue.path[0] === 'etapes' && typeof issue.path[1] === 'number'
      ? `${idDeLetape(parsed, issue.path[1])} — ${decrire(issue, 2)}`
      : decrire(issue),
  );
};

const relireContenu = (parsed: unknown): ReadonlyArray<string> => {
  const etapes = isRecord(parsed) && Array.isArray(parsed['etapes']) ? parsed['etapes'] : [];

  return etapes.flatMap((etape: unknown, rang: number) => {
    if (!isRecord(etape)) return [];

    const jugements = etape['type'] === 'seance-duo' ? problemesJugement(etape) : [];

    return [...problemesInterdits(etape), ...jugements].map((probleme) => `${idDeLetape(parsed, rang)} — ${probleme}`);
  });
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

type Document = {
  readonly id: string;
  readonly contenu: string;
  readonly source: string;
  readonly modifieLe: number;
};

const lireDocuments = async (dossier: string): Promise<ReadonlyArray<Document>> => {
  const noms = await readdir(dossier).catch(() => null);
  if (noms === null) return [];

  const fichiers = noms.filter((nom) => nom.endsWith('.md') && nom !== 'README.md');

  return Promise.all(
    fichiers.map(async (nom) => {
      const source = join(dossier, nom);

      return {
        id: nom.replace(/\.md$/, ''),
        contenu: await readFile(source, 'utf8'),
        source,
        modifieLe: (await stat(source)).mtimeMs,
      };
    }),
  );
};

const documentsAppeles = (parsed: unknown, type: string): ReadonlyArray<string> => {
  if (!isRecord(parsed) || !Array.isArray(parsed['etapes'])) return [];

  return parsed['etapes'].flatMap((etape: unknown) =>
    isRecord(etape) && etape['type'] === type && typeof etape['document'] === 'string' ? [etape['document']] : [],
  );
};

const manquants = (appeles: ReadonlyArray<string>, documents: ReadonlyArray<Document>, dossier: string): ReadonlyArray<string> =>
  appeles
    .filter((id) => !documents.some((document) => document.id === id))
    .map((id) => `document introuvable : companion/inputs/${dossier}/${id}.md`);

const relireBibliotheque = (parsed: unknown, fiches: ReadonlyArray<Document>): ReadonlyArray<string> => {
  const fautives = fiches.flatMap((fiche) => {
    const texte = normalise(fiche.contenu);

    return INTERDITS.flatMap((interdit) => (interdit.motif.test(texte) ? [`bibliotheque/${fiche.id}.md — ${interdit.raison}`] : []));
  });

  return [...manquants(documentsAppeles(parsed, 'fiche'), fiches, 'bibliotheque'), ...fautives];
};

// 🔴 Les sept familles d'interdits ne s'appliquent pas au corps d'un bilan : un rapport clinique reel nomme
// des traitements et des diagnostics, et c'est sa raison d'etre. Le titre affiche, lui, reste verifie comme le reste.
const relireBilans = (parsed: unknown, bilans: ReadonlyArray<Document>): ReadonlyArray<string> =>
  manquants(documentsAppeles(parsed, 'bilan'), bilans, 'bilans');

// La documentation et les bilans partent a tout moment ; les etapes qui font agir se decident avec Xavier, en seance.
const FONT_AGIR = TYPES.filter((type) => type !== 'fiche' && type !== 'bilan');

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

const estAJour = async (document: Document, destination: string): Promise<boolean> => {
  const pdf = await stat(destination).catch(() => null);

  return pdf !== null && pdf.mtimeMs >= document.modifieLe;
};

// Le Markdown ne part pas : il passe la supervision au depot, et Kokoro ne recoit que le PDF.
const convertir = async (
  cible: string,
  nom: string,
  documents: ReadonlyArray<Document>,
  refaireTout: boolean,
): Promise<Publication> => {
  const dossier = join(cible, nom);
  await mkdir(dossier, { recursive: true });

  const attendus = documents.map((document) => `${document.id}.pdf`);
  const presents = await readdir(dossier).catch(() => []);
  const retirees = presents.filter((present) => !attendus.some((attendu) => attendu === present));
  await Promise.all(retirees.map((present) => rm(join(dossier, present), { force: true })));

  const cibles = await Promise.all(
    documents.map(async (document) => {
      const destination = join(dossier, `${document.id}.pdf`);

      return { document, destination, aJour: !refaireTout && (await estAJour(document, destination)) };
    }),
  );

  const aConvertir = cibles.filter(({ aJour }) => !aJour);
  await convertirEnPdf(
    aConvertir.map(({ document, destination }) => ({ sourcePath: document.source, destinationPath: destination })),
  );

  return {
    converties: aConvertir.map(({ document }) => document.id),
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
  const fiches = await lireDocuments(BIBLIOTHEQUE);
  const bilans = await lireDocuments(BILANS);

  const problemes = [
    ...relireProgramme(parsed),
    ...relireContenu(parsed),
    ...(await relireSupervision(parsed)),
    ...relireBibliotheque(parsed, fiches),
    ...relireBilans(parsed, bilans),
    ...(options.seance ? [] : await relireHorsSeance(parsed, cible)),
  ];

  if (problemes.length > 0) {
    console.error('Publication refusee — le contenu enfreint les invariants du dispositif :\n');
    problemes.forEach((probleme) => console.error(`  ${probleme}`));
    console.error('\nRien n\'a ete publie. Un refus se corrige, il ne se contourne pas.');

    process.exitCode = 1;

    return;
  }

  const publiees = await convertir(cible, 'bibliotheque', fiches, options.refaireTout);
  const verses = await convertir(cible, 'bilans', bilans, options.refaireTout);
  await writeFile(join(cible, 'programme.json'), brut, 'utf8');

  const etapes = isRecord(parsed) && Array.isArray(parsed['etapes']) ? parsed['etapes'].length : 0;
  const version = isRecord(parsed) ? String(parsed['version']) : '?';
  const supervision = isRecord(parsed) ? String(parsed['supervision']) : '?';

  const dire = (dossier: string, total: number, publication: Publication): void => {
    console.log(
      `         ${dossier} — ${total} document(s) en PDF : ${publication.converties.length} convertis, ${publication.reprises} inchanges`,
    );
    publication.converties.forEach((id) => console.log(`         converti ${id}.pdf`));
    publication.retirees.forEach((nom) => console.log(`         retire   ${nom}`));
  };

  console.log(`publie   programme.json — version ${version}, ${etapes} etapes`);
  dire('bibliotheque', fiches.length, publiees);
  dire('bilans', bilans.length, verses);
  console.log(`vise par ${supervision}`);
  console.log(`portee   ${options.seance ? 'seance — tout le programme' : 'hors seance — documentation seule'}`);
  console.log(`vers     ${cible}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
