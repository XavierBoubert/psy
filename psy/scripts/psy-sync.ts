import { copyFile, mkdir, readFile, readdir } from 'node:fs/promises';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const SORTIES = resolve(PROJECT_ROOT, 'companion/outputs');

const USAGE = 'Usage: psy-sync <dossier-de-transit-drive>';

const CORE_FIELDS = [
  'shutdowns',
  'exposition_sociale',
  'retrait_sensoriel',
  'renoncements',
  'activites_investies',
  'sommeil_heures',
  'missions_actives',
] as const;

const KNOWN_SOURCES = ['claude-code', 'android'] as const;
const KNOWN_ISSUES = ['termine', 'arrete_avant_la_fin', 'fait', 'entrainement'] as const;

type Flux = {
  readonly nom: string;
  readonly motif: RegExp;
  readonly convention: string;
  readonly valider: (parsed: Record<string, unknown>, nom: string) => string | null;
};

type Issue =
  | { readonly kind: 'verse'; readonly flux: string; readonly nom: string }
  | { readonly kind: 'deja-la'; readonly flux: string; readonly nom: string }
  | { readonly kind: 'a-la-main'; readonly flux: string; readonly nom: string; readonly raison: string };

const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null && !Array.isArray(value);

const estNombreOuNull = (value: unknown): boolean => value === null || typeof value === 'number';

const validerJournal = (parsed: Record<string, unknown>, nom: string): string | null => {
  if (parsed['date'] !== nom.replace('.json', '')) return 'le champ « date » ne correspond pas au nom du fichier';

  const source = parsed['source'];
  if (typeof source !== 'string' || !KNOWN_SOURCES.some((connue) => connue === source)) {
    return `source inconnue : ${String(source)}`;
  }

  const noyau = parsed['noyau'];
  if (!isRecord(noyau)) return 'le noyau est absent';

  const manquants = CORE_FIELDS.filter((champ) => !(champ in noyau));
  if (manquants.length > 0) return `champs de noyau manquants : ${manquants.join(', ')}`;

  const mauvaisType = CORE_FIELDS.filter((champ) => !estNombreOuNull(noyau[champ]));
  if (mauvaisType.length > 0) return `champs de noyau qui ne sont ni un nombre ni null : ${mauvaisType.join(', ')}`;

  if (!isRecord(parsed['campagne'])) return 'le bloc « campagne » est absent';

  const notes = parsed['notes'];
  if (notes !== null && typeof notes !== 'string') return 'le champ « notes » n\'est ni null ni du texte';

  return null;
};

const validerReponse = (parsed: Record<string, unknown>, nom: string): string | null => {
  const etape = parsed['etape'];
  if (typeof etape !== 'string' || !/^[a-z0-9-]+$/.test(etape)) return 'le champ « etape » est absent ou hors kebab-case';

  if (!nom.endsWith(`-${etape}.json`)) return `le nom du fichier ne se termine pas par l'étape « ${etape} »`;

  if (typeof parsed['horodatage'] !== 'string') return 'le champ « horodatage » est absent';

  const issue = parsed['issue'];
  if (typeof issue !== 'string' || !KNOWN_ISSUES.some((connue) => connue === issue)) {
    return `issue inconnue : ${String(issue)}`;
  }

  const source = parsed['source'];
  if (typeof source !== 'string' || !KNOWN_SOURCES.some((connue) => connue === source)) {
    return `source inconnue : ${String(source)}`;
  }

  const reponses = parsed['reponses'];
  if (reponses !== null && !isRecord(reponses) && !Array.isArray(reponses)) {
    return 'le champ « reponses » n\'est ni null, ni un objet, ni une liste';
  }

  return null;
};

const FLUX: ReadonlyArray<Flux> = [
  {
    nom: 'journal',
    motif: /^\d{4}-\d{2}-\d{2}\.json$/,
    convention: 'AAAA-MM-JJ.json',
    valider: validerJournal,
  },
  {
    nom: 'reponses',
    motif: /^\d{4}-\d{2}-\d{2}-\d{4}-[a-z0-9-]+\.json$/,
    convention: 'AAAA-MM-JJ-HHMM-<id>.json',
    valider: validerReponse,
  },
];

const dejaLa = async (flux: Flux, nom: string): Promise<boolean> => {
  const existants = await readdir(join(SORTIES, flux.nom)).catch(() => [] as ReadonlyArray<string>);

  return existants.includes(nom);
};

const relire = (flux: Flux, brut: string, nom: string): string | null => {
  try {
    const parsed: unknown = JSON.parse(brut);

    return isRecord(parsed) ? flux.valider(parsed, nom) : 'la racine n\'est pas un objet';
  } catch (error: unknown) {
    return `JSON illisible : ${error instanceof Error ? error.message : String(error)}`;
  }
};

const verserUn = async (source: string, flux: Flux, nom: string): Promise<Issue> => {
  if (!flux.motif.test(nom)) {
    return { kind: 'a-la-main', flux: flux.nom, nom, raison: `nom hors convention ${flux.convention} — doublon Drive probable` };
  }

  const probleme = relire(flux, await readFile(join(source, nom), 'utf8'), nom);
  if (probleme !== null) return { kind: 'a-la-main', flux: flux.nom, nom, raison: probleme };

  if (await dejaLa(flux, nom)) return { kind: 'deja-la', flux: flux.nom, nom };

  await copyFile(join(source, nom), join(SORTIES, flux.nom, nom));

  return { kind: 'verse', flux: flux.nom, nom };
};

const verserUnFlux = async (transit: string, flux: Flux): Promise<ReadonlyArray<Issue>> => {
  const source = join(transit, flux.nom);
  await mkdir(join(SORTIES, flux.nom), { recursive: true });

  const noms = await readdir(source).catch(() => null);
  if (noms === null) return [];

  return Promise.all(noms.map((nom) => verserUn(source, flux, nom)));
};

const rapporter = (issues: ReadonlyArray<Issue>): void => {
  const verses = issues.filter((issue) => issue.kind === 'verse');
  const inchanges = issues.filter((issue) => issue.kind === 'deja-la');
  const aLaMain = issues.filter((issue) => issue.kind === 'a-la-main');

  verses.forEach((issue) => console.log(`versé    ${issue.flux}/${issue.nom}`));
  inchanges.forEach((issue) => console.log(`inchangé ${issue.flux}/${issue.nom} — déjà au dossier, jamais écrasé`));
  aLaMain.forEach((issue) => {
    if (issue.kind === 'a-la-main') console.log(`à la main ${issue.flux}/${issue.nom} — ${issue.raison}`);
  });

  console.log(`\n${verses.length} versé(s), ${inchanges.length} inchangé(s), ${aLaMain.length} à traiter à la main.`);

  if (aLaMain.length > 0) {
    console.log('Un fichier à traiter à la main ne se supprime jamais sans être lu : c\'est une donnée clinique.');
  }
};

const main = async (): Promise<void> => {
  const [transit] = process.argv.slice(2);
  if (!transit) throw new Error(USAGE);

  const racine = resolve(PROJECT_ROOT, transit);

  const issues = await Promise.all(FLUX.map((flux) => verserUnFlux(racine, flux)));

  rapporter(issues.flat());
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
