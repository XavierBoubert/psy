import { mkdir, readFile, readdir, writeFile } from 'node:fs/promises';
import { dirname, extname, isAbsolute, join, relative, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '..');
const DESIGN = resolve(PROJECT_ROOT, 'psy/android/design');
const PROMPTS = join(DESIGN, 'prompts');
const SORTIES = join(DESIGN, 'sorties');

const BASE_PAR_DEFAUT = '_base';
const MODELE_PAR_DEFAUT = 'gemini-3.1-flash-image';
const FORMAT_PAR_DEFAUT = '1:1';
const TAILLE_PAR_DEFAUT = '1K';
const LETTRES = 'abcdefgh';
const MAX_CANDIDATS = LETTRES.length;

const MIMES: Readonly<Record<string, string>> = {
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.webp': 'image/webp',
};

const USAGE = `Usage: kokoro-image <variante> [--n=<1-${MAX_CANDIDATS}>] [--ref=<image>[,<image>]] [--format=<1:1|4:3|3:4|16:9>] [--taille=<1K|2K|4K>] [--base=<charte>] [--modele=<id>] [--sans-base] [--sans-planche]

  <variante>      nom d'un fichier de psy/android/design/prompts/ (sans .md)
  --ref           images de départ — chemin projet, ou raccourci « <variante>/03-b.png » sous sorties/
  --base          charte de style préfixée au prompt (défaut ${BASE_PAR_DEFAUT}, le personnage)
  --sans-base     n'ajoute aucune charte de style
  --sans-planche  n'assemble pas la planche contact`;

type Options = {
  readonly variante: string;
  readonly n: number;
  readonly refs: ReadonlyArray<string>;
  readonly format: string;
  readonly taille: string;
  readonly modele: string;
  readonly base: string | null;
  readonly avecPlanche: boolean;
};

type Reference = {
  readonly chemin: string;
  readonly mime: string;
  readonly base64: string;
};

type Resultat =
  | { readonly kind: 'image'; readonly lettre: string; readonly image: Buffer }
  | { readonly kind: 'echec'; readonly lettre: string; readonly raison: string };

const isRecord = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null && !Array.isArray(value);

const lireDrapeau = (args: ReadonlyArray<string>, nom: string): string | undefined =>
  args.find((arg) => arg.startsWith(`--${nom}=`))?.slice(nom.length + 3);

const lireOptions = (args: ReadonlyArray<string>): Options => {
  const variante = args.find((arg) => !arg.startsWith('--'));
  if (variante === undefined) throw new Error(USAGE);

  const brutN = lireDrapeau(args, 'n');
  const n = brutN === undefined ? 1 : Number(brutN);
  if (!Number.isInteger(n) || n < 1 || n > MAX_CANDIDATS) throw new Error(`--n doit être un entier de 1 à ${MAX_CANDIDATS}`);

  const brutRefs = lireDrapeau(args, 'ref');
  const refs = brutRefs === undefined ? [] : brutRefs.split(',').filter((chemin) => chemin.length > 0);

  return {
    variante,
    n,
    refs,
    format: lireDrapeau(args, 'format') ?? FORMAT_PAR_DEFAUT,
    taille: lireDrapeau(args, 'taille') ?? TAILLE_PAR_DEFAUT,
    modele: lireDrapeau(args, 'modele') ?? MODELE_PAR_DEFAUT,
    base: args.includes('--sans-base') ? null : lireDrapeau(args, 'base') ?? BASE_PAR_DEFAUT,
    avecPlanche: !args.includes('--sans-planche'),
  };
};

const lirePrompt = async (nom: string): Promise<string> => {
  const contenu = await readFile(join(PROMPTS, `${nom}.md`), 'utf8').catch(() => {
    throw new Error(`prompt introuvable : psy/android/design/prompts/${nom}.md`);
  });

  return contenu.trim();
};

const composerPrompt = async (options: Options): Promise<string> => {
  const variante = await lirePrompt(options.variante);

  if (options.base === null) return variante;

  return `${await lirePrompt(options.base)}\n\n---\n\n${variante}`;
};

const resoudreRef = async (chemin: string): Promise<Reference> => {
  const candidats = isAbsolute(chemin) ? [chemin] : [resolve(PROJECT_ROOT, chemin), resolve(SORTIES, chemin)];

  const mime = MIMES[extname(chemin).toLowerCase()];
  if (mime === undefined) throw new Error(`format de référence non géré : ${chemin}`);

  for (const candidat of candidats) {
    const contenu = await readFile(candidat).catch(() => null);
    if (contenu !== null) return { chemin: candidat, mime, base64: contenu.toString('base64') };
  }

  throw new Error(`référence introuvable : ${chemin}`);
};

const extraire = (payload: unknown, cle: 'inlineData' | 'text'): ReadonlyArray<string> => {
  if (!isRecord(payload)) return [];

  const candidats = payload['candidates'];
  if (!Array.isArray(candidats)) return [];

  return candidats.flatMap((candidat: unknown) => {
    if (!isRecord(candidat)) return [];

    const content = candidat['content'];
    if (!isRecord(content)) return [];

    const parts = content['parts'];
    if (!Array.isArray(parts)) return [];

    return parts.flatMap((part: unknown) => {
      if (!isRecord(part)) return [];

      if (cle === 'text') return typeof part['text'] === 'string' ? [part['text']] : [];

      const inline = part['inlineData'] ?? part['inline_data'];
      if (!isRecord(inline)) return [];

      return typeof inline['data'] === 'string' ? [inline['data']] : [];
    });
  });
};

const appeler = async (
  cle: string,
  options: Options,
  prompt: string,
  references: ReadonlyArray<Reference>,
): Promise<Buffer> => {
  const parts = [
    { text: prompt },
    ...references.map((reference) => ({ inlineData: { mimeType: reference.mime, data: reference.base64 } })),
  ];

  const reponse = await fetch(
    `https://generativelanguage.googleapis.com/v1beta/models/${options.modele}:generateContent`,
    {
      method: 'POST',
      headers: { 'content-type': 'application/json', 'x-goog-api-key': cle },
      body: JSON.stringify({
        contents: [{ role: 'user', parts }],
        generationConfig: {
          responseModalities: ['TEXT', 'IMAGE'],
          imageConfig: { aspectRatio: options.format, imageSize: options.taille },
        },
      }),
    },
  );

  const payload: unknown = await reponse.json().catch(() => null);

  if (!reponse.ok) throw new Error(`HTTP ${reponse.status} — ${JSON.stringify(payload).slice(0, 400)}`);

  const images = extraire(payload, 'inlineData');
  const premiere = images[0];

  if (premiere === undefined) throw new Error(`aucune image renvoyée — ${extraire(payload, 'text').join(' ').slice(0, 300) || 'réponse vide'}`);

  return Buffer.from(premiere, 'base64');
};

const genererUn = async (
  cle: string,
  options: Options,
  prompt: string,
  references: ReadonlyArray<Reference>,
  lettre: string,
): Promise<Resultat> => {
  try {
    return { kind: 'image', lettre, image: await appeler(cle, options, prompt, references) };
  } catch {
    try {
      return { kind: 'image', lettre, image: await appeler(cle, options, prompt, references) };
    } catch (err) {
      return { kind: 'echec', lettre, raison: err instanceof Error ? err.message : String(err) };
    }
  }
};

const prochainNumero = async (dossier: string): Promise<string> => {
  const existants: ReadonlyArray<string> = await readdir(dossier).catch(() => []);

  const dernier = existants.reduce<number>((acc, nom) => {
    const trouve = /^(\d{2})[-.]/.exec(nom);

    return trouve === null ? acc : Math.max(acc, Number(trouve[1]));
  }, 0);

  return String(dernier + 1).padStart(2, '0');
};

const composerPlanche = async (
  vignettes: ReadonlyArray<{ readonly etiquette: string; readonly image: Buffer }>,
  destination: string,
): Promise<void> => {
  const { default: puppeteer } = await import('puppeteer');

  const colonnes = Math.min(vignettes.length, 2);
  const cellules = vignettes
    .map(
      ({ etiquette, image }) =>
        `<figure><img src="data:image/png;base64,${image.toString('base64')}"><figcaption>${etiquette}</figcaption></figure>`,
    )
    .join('');

  const html = `<!doctype html><meta charset="utf-8"><style>
    body { margin: 0; padding: 20px; background: #8d9297; font: 16px ui-monospace, monospace; }
    main { display: grid; grid-template-columns: repeat(${colonnes}, 1fr); gap: 20px; }
    figure { margin: 0; background: #F4F1EA; }
    img { display: block; width: 100%; }
    figcaption { padding: 6px 10px; background: #2B2F33; color: #F4F1EA; }
  </style><main>${cellules}</main>`;

  const navigateur = await puppeteer.launch({ headless: true });

  try {
    const page = await navigateur.newPage();

    await page.setViewport({ width: 240 + colonnes * 480, height: 800 });
    await page.setContent(html, { waitUntil: 'load' });
    await page.evaluate(async () => {
      await Promise.all(Array.from(document.images).map((image) => image.decode()));
    });
    await page.screenshot({ path: destination, fullPage: true });
  } finally {
    await navigateur.close();
  }
};

const main = async (): Promise<void> => {
  try {
    process.loadEnvFile(resolve(PROJECT_ROOT, '.env'));
  } catch {
    // .env facultatif : la clé peut venir de l'environnement
  }

  const cle = process.env['GEMINI_API_KEY'];
  if (cle === undefined || cle.length === 0) throw new Error('GEMINI_API_KEY absente de .env et de l\'environnement');

  const options = lireOptions(process.argv.slice(2));
  const prompt = await composerPrompt(options);
  const references = await Promise.all(options.refs.map(resoudreRef));

  const dossier = join(SORTIES, options.variante);
  await mkdir(dossier, { recursive: true });
  const numero = await prochainNumero(dossier);

  const resultats = await Promise.all(
    Array.from({ length: options.n }, (_, index) =>
      genererUn(cle, options, prompt, references, LETTRES[index] ?? String(index)),
    ),
  );

  const reussis = resultats.filter((resultat) => resultat.kind === 'image');

  await Promise.all(
    reussis.map(({ lettre, image }) => writeFile(join(dossier, `${numero}-${lettre}.png`), image)),
  );

  const journal = [
    `# ${options.variante} — série ${numero}`,
    '',
    `- date : ${new Date().toISOString()}`,
    `- modèle : ${options.modele} · format ${options.format} · taille ${options.taille}`,
    `- charte : ${options.base === null ? 'aucune' : `${options.base}.md`}`,
    `- références : ${references.length === 0 ? 'aucune' : references.map((reference) => relative(PROJECT_ROOT, reference.chemin)).join(', ')}`,
    `- candidats : ${reussis.map(({ lettre }) => lettre).join(' ') || 'aucun'}`,
    '',
    '---',
    '',
    prompt,
    '',
  ].join('\n');

  await writeFile(join(dossier, `${numero}.md`), journal, 'utf8');

  if (options.avecPlanche && reussis.length > 1) {
    await composerPlanche(
      reussis.map(({ lettre, image }) => ({ etiquette: `${options.variante} ${numero}-${lettre}`, image })),
      join(dossier, `${numero}-planche.png`),
    );
  }

  const racine = relative(PROJECT_ROOT, dossier).replaceAll('\\', '/');
  console.log(`${racine}/${numero}-{${reussis.map(({ lettre }) => lettre).join(',')}}.png`);
  if (options.avecPlanche && reussis.length > 1) console.log(`${racine}/${numero}-planche.png  ← à lire`);

  resultats
    .filter((resultat) => resultat.kind === 'echec')
    .forEach(({ lettre, raison }) => console.error(`échec ${numero}-${lettre} : ${raison}`));

  if (reussis.length === 0) process.exitCode = 1;
};

await main().catch((err: unknown) => {
  console.error(err instanceof Error ? err.message : String(err));
  process.exitCode = 1;
});
