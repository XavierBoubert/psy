import { copyFile, mkdir, readFile, readdir, writeFile } from 'node:fs/promises';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import type { ZodType } from 'zod';
import type { Journal, Reponse } from './schemas/dossier.ts';
import { CARTE_DU_JOURNAL, JournalSchema, NOYAU, QUESTION_DES_NOTES, ReponseSchema } from './schemas/dossier.ts';
import { decrire } from './schemas/problemes.ts';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const SORTIES = resolve(PROJECT_ROOT, 'companion/outputs');

const USAGE = 'Usage: psy-sync <dossier-de-transit-drive>';

type Flux = {
  readonly nom: string;
  readonly motif: RegExp;
  readonly convention: string;
  readonly valider: (parsed: Record<string, unknown>, nom: string) => string | null;
};

type Issue =
  | { readonly kind: 'verse'; readonly flux: string; readonly nom: string }
  | { readonly kind: 'deja-la'; readonly flux: string; readonly nom: string }
  | { readonly kind: 'doublon'; readonly flux: string; readonly nom: string }
  | { readonly kind: 'a-la-main'; readonly flux: string; readonly nom: string; readonly raison: string }
  | { readonly kind: 'journal'; readonly flux: string; readonly nom: string };

const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null && !Array.isArray(value);

const relireAvec = (schema: ZodType, parsed: Record<string, unknown>): string | null => {
  const relu = schema.safeParse(parsed);

  return relu.success ? null : relu.error.issues.map((issue) => decrire(issue)).join(' · ');
};

const rassembler = (problemes: ReadonlyArray<string | null>): string | null =>
  problemes.filter((probleme): probleme is string => probleme !== null).join(' · ') || null;

const validerJournal = (parsed: Record<string, unknown>, nom: string): string | null =>
  rassembler([
    parsed['date'] === nom.replace('.json', '') ? null : 'le champ « date » ne correspond pas au nom du fichier',
    relireAvec(JournalSchema, parsed),
  ]);

const validerReponse = (parsed: Record<string, unknown>, nom: string): string | null => {
  const carte = parsed['carte'];

  return rassembler([
    typeof carte === 'string' && nom.endsWith(`-${carte}.json`)
      ? null
      : 'le nom du fichier ne se termine pas par la carte qu\'il porte',
    relireAvec(ReponseSchema, parsed),
  ]);
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

// 🔴 Google Drive accepte deux dossiers du même nom. Un « reponses (1) » n'est pas un rebut : il porte
// des données cliniques que personne ne verrait jamais s'il restait ignoré. On le lit, et on le signale.
const dossiersDuFlux = async (transit: string, flux: Flux): Promise<ReadonlyArray<string>> => {
  const entrees = await readdir(transit, { withFileTypes: true }).catch(() => []);
  const doublon = new RegExp(`^${flux.nom} \\(\\d+\\)$`);

  return entrees
    .filter((entree) => entree.isDirectory() && (entree.name === flux.nom || doublon.test(entree.name)))
    .map((entree) => entree.name);
};

const verserUnDossier = async (transit: string, flux: Flux, dossier: string): Promise<ReadonlyArray<Issue>> => {
  const source = join(transit, dossier);

  const noms = await readdir(source).catch(() => [] as ReadonlyArray<string>);
  const versements = await Promise.all(noms.map((nom) => verserUn(source, flux, nom)));

  return dossier === flux.nom
    ? versements
    : [{ kind: 'doublon', flux: flux.nom, nom: dossier }, ...versements];
};

const verserUnFlux = async (transit: string, flux: Flux): Promise<ReadonlyArray<Issue>> => {
  await mkdir(join(SORTIES, flux.nom), { recursive: true });

  const dossiers = await dossiersDuFlux(transit, flux);
  const lots = await Promise.all(dossiers.map((dossier) => verserUnDossier(transit, flux, dossier)));

  return lots.flat();
};

// 🔴 Kokoro n'écrit plus le journal : il rend une réponse comme pour n'importe quelle carte, et c'est ici
// qu'elle redevient un journal. L'id « check-in » est le seul lien — Kokoro n'interprète rien, et le format
// du dossier ne bouge pas d'un champ. Les identifiants de question sont en kebab-case, les clés en snake_case.
const cleDuJournal = (question: string): string => question.replaceAll('-', '_');

const chiffresDe = (reponse: Reponse): ReadonlyMap<string, number | null> =>
  new Map(
    (reponse.reponses || []).flatMap((item): ReadonlyArray<readonly [string, number | null]> =>
      item.valeur === undefined ? [] : [[cleDuJournal(item.question), item.valeur]],
    ),
  );

const journalDeLaReponse = (reponse: Reponse): Journal => {
  const chiffres = chiffresDe(reponse);
  const note = (reponse.reponses || []).find((item) => item.question === QUESTION_DES_NOTES);
  const horsNoyau = [...chiffres].filter(([cle]) => !NOYAU.some((champ) => champ === cle));

  return {
    date: reponse.horodatage.slice(0, 10),
    source: reponse.source,
    noyau: {
      shutdowns: chiffres.get('shutdowns') ?? null,
      exposition_sociale: chiffres.get('exposition_sociale') ?? null,
      retrait_sensoriel: chiffres.get('retrait_sensoriel') ?? null,
      renoncements: chiffres.get('renoncements') ?? null,
      activites_investies: chiffres.get('activites_investies') ?? null,
      sommeil_heures: chiffres.get('sommeil_heures') ?? null,
      missions_actives: chiffres.get('missions_actives') ?? null,
    },
    campagne: Object.fromEntries(horsNoyau),
    notes: note?.texte || null,
  };
};

const reponsesDuJournal = async (): Promise<ReadonlyArray<Reponse>> => {
  const noms = await readdir(join(SORTIES, 'reponses')).catch(() => [] as ReadonlyArray<string>);
  const retenus = noms.filter((nom) => nom.endsWith(`-${CARTE_DU_JOURNAL}.json`));

  const lus = await Promise.all(
    retenus.map(async (nom) => {
      const relu = ReponseSchema.safeParse(JSON.parse(await readFile(join(SORTIES, 'reponses', nom), 'utf8')));

      return relu.success ? [relu.data] : [];
    }),
  );

  return lus.flat();
};

// ⭐ Un jour déjà au journal n'est jamais réécrit : le dépôt garde ce qui y est arrivé en premier.
const reconstruireLeJournal = async (): Promise<ReadonlyArray<Issue>> => {
  const reponses = await reponsesDuJournal();
  const existants = await readdir(join(SORTIES, 'journal')).catch(() => [] as ReadonlyArray<string>);

  const ecrits = await Promise.all(
    reponses.map(async (reponse): Promise<ReadonlyArray<Issue>> => {
      const journal = journalDeLaReponse(reponse);
      const nom = `${journal.date}.json`;
      if (existants.includes(nom)) return [];

      const probleme = relireAvec(JournalSchema, journal);
      if (probleme !== null) return [{ kind: 'a-la-main', flux: 'journal', nom, raison: probleme }];

      await writeFile(join(SORTIES, 'journal', nom), `${JSON.stringify(journal, null, 2)}\n`, 'utf8');

      return [{ kind: 'journal', flux: 'journal', nom }];
    }),
  );

  return ecrits.flat();
};

const rapporter = (issues: ReadonlyArray<Issue>): void => {
  const verses = issues.filter((issue) => issue.kind === 'verse');
  const journaux = issues.filter((issue) => issue.kind === 'journal');
  const inchanges = issues.filter((issue) => issue.kind === 'deja-la');
  const doublons = issues.filter((issue) => issue.kind === 'doublon');
  const aLaMain = issues.filter((issue) => issue.kind === 'a-la-main');

  verses.forEach((issue) => console.log(`versé    ${issue.flux}/${issue.nom}`));
  journaux.forEach((issue) => console.log(`journal  ${issue.flux}/${issue.nom} — reconstruit depuis la carte ${CARTE_DU_JOURNAL}`));
  inchanges.forEach((issue) => console.log(`inchangé ${issue.flux}/${issue.nom} — déjà au dossier, jamais écrasé`));
  doublons.forEach((issue) => console.log(`doublon  ${issue.nom} — Drive a créé un second dossier ; son contenu est versé, lui reste à supprimer`));
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
  const journaux = await reconstruireLeJournal();

  rapporter([...issues.flat(), ...journaux]);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
