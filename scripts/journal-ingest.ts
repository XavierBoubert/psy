import { copyFile, mkdir, readFile, readdir } from 'node:fs/promises';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const JOURNAL_DIR = resolve(PROJECT_ROOT, 'psy/dossier/journal');

const USAGE = 'Usage: journal-ingest <dossier-de-transit-drive>';

const FILE_NAME_PATTERN = /^\d{4}-\d{2}-\d{2}\.json$/;

const CORE_FIELDS = [
  'shutdowns',
  'exposition_sociale',
  'retrait_sensoriel',
  'renoncements',
  'activites_investies',
  'sommeil_heures',
  'missions_actives',
] as const;

const KNOWN_SOURCES = ['claude-code', 'android', 'web'] as const;

type Outcome =
  | { readonly kind: 'copied'; readonly name: string }
  | { readonly kind: 'already-there'; readonly name: string }
  | { readonly kind: 'needs-hand'; readonly name: string; readonly reason: string };

const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null && !Array.isArray(value);

const isNumberOrNull = (value: unknown): boolean => value === null || typeof value === 'number';

const validate = (raw: string, name: string): string | null => {
  const parsed: unknown = JSON.parse(raw);

  if (!isRecord(parsed)) return 'la racine n\'est pas un objet';
  if (parsed['date'] !== name.replace('.json', '')) return 'le champ « date » ne correspond pas au nom du fichier';

  const source = parsed['source'];
  if (typeof source !== 'string' || !KNOWN_SOURCES.some((known) => known === source)) {
    return `source inconnue : ${String(source)}`;
  }

  const core = parsed['noyau'];
  if (!isRecord(core)) return 'le noyau est absent';

  const missing = CORE_FIELDS.filter((field) => !(field in core));
  if (missing.length > 0) return `champs de noyau manquants : ${missing.join(', ')}`;

  const wrongType = CORE_FIELDS.filter((field) => !isNumberOrNull(core[field]));
  if (wrongType.length > 0) return `champs de noyau qui ne sont ni un nombre ni null : ${wrongType.join(', ')}`;

  if (!isRecord(parsed['campagne'])) return 'le bloc « campagne » est absent';

  const notes = parsed['notes'];
  if (notes !== null && typeof notes !== 'string') return 'le champ « notes » n\'est ni null ni du texte';

  return null;
};

const alreadyThere = async (name: string): Promise<boolean> => {
  const existing = await readdir(JOURNAL_DIR).catch(() => [] as readonly string[]);

  return existing.includes(name);
};

const ingestOne = async (transitDir: string, name: string): Promise<Outcome> => {
  if (!FILE_NAME_PATTERN.test(name)) {
    return { kind: 'needs-hand', name, reason: 'nom hors convention AAAA-MM-JJ.json — doublon Drive probable' };
  }

  const raw = await readFile(join(transitDir, name), 'utf8');

  const problem = ((): string | null => {
    try {
      return validate(raw, name);
    } catch (error: unknown) {
      return `JSON illisible : ${error instanceof Error ? error.message : String(error)}`;
    }
  })();

  if (problem !== null) return { kind: 'needs-hand', name, reason: problem };

  if (await alreadyThere(name)) return { kind: 'already-there', name };

  await copyFile(join(transitDir, name), join(JOURNAL_DIR, name));

  return { kind: 'copied', name };
};

const report = (outcomes: readonly Outcome[]): void => {
  const copied = outcomes.filter((outcome) => outcome.kind === 'copied');
  const kept = outcomes.filter((outcome) => outcome.kind === 'already-there');
  const manual = outcomes.filter((outcome) => outcome.kind === 'needs-hand');

  copied.forEach((outcome) => console.log(`versé   ${outcome.name}`));
  kept.forEach((outcome) => console.log(`inchangé ${outcome.name} — déjà au dossier, jamais écrasé`));
  manual.forEach((outcome) => {
    if (outcome.kind === 'needs-hand') console.log(`à la main ${outcome.name} — ${outcome.reason}`);
  });

  console.log(`\n${copied.length} versé(s), ${kept.length} inchangé(s), ${manual.length} à traiter à la main.`);

  if (manual.length > 0) {
    console.log('Un fichier à traiter à la main ne se supprime jamais sans être lu : c\'est une donnée clinique.');
  }
};

const main = async (): Promise<void> => {
  const [transit] = process.argv.slice(2);
  if (!transit) throw new Error(USAGE);

  const transitDir = resolve(PROJECT_ROOT, transit);
  await mkdir(JOURNAL_DIR, { recursive: true });

  const names = await readdir(transitDir);
  const outcomes = await Promise.all(names.map((name) => ingestOne(transitDir, name)));

  report(outcomes);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
