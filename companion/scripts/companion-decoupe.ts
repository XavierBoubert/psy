import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, extname, isAbsolute, relative, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const SORTIES = resolve(PROJECT_ROOT, 'companion/ressources/sorties');

const LARGEUR_PAR_DEFAUT = 1600;
const QUALITE_WEBP = 0.92;

const MIMES: Readonly<Record<string, string>> = {
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.webp': 'image/webp',
};

const USAGE = `Usage: companion-decoupe <source> <destination.webp|.png> [--largeur=<px>] [--seuil=<0-1>] [--plein=<0-1>] [--marge=<0-0.4>]

  <source>        image générée — chemin projet, ou raccourci « decor-feuillage/01-b.png » sous sorties/
  <destination>   chemin projet du fichier écrit ; l'extension décide du format
  --largeur       largeur de sortie en pixels (défaut ${LARGEUR_PAR_DEFAUT}) ; 0 garde la taille d'origine
  --seuil         magenta au-delà duquel le pixel est totalement transparent (défaut 0.90)
  --plein         magenta en deçà duquel le pixel est totalement opaque (défaut 0.45)
  --marge         fraction de la largeur effacée à gauche comme à droite (défaut 0)

Le fond magenta #FF00FF posé par le modèle devient le canal alpha : la couleur du sujet est
« démultipliée » pour retrouver sa teinte réelle sous les bords fondus, et le reste de frange
magenta est neutralisé.

--marge sert aux couches qui se répètent SANS miroir : la charte leur demande des marges latérales
vides, le modèle les respecte à peu près, et il laisse régulièrement un éclat de nuage collé à un
bord. Effacer une bande étroite garantit ce que le modèle promet — un bord réellement vide, donc un
raccord invisible. À ne pas utiliser sur une couche bord à bord (la prairie) : elle y perdrait son
pied.`;

type Options = {
  readonly source: string;
  readonly destination: string;
  readonly largeur: number;
  readonly seuil: number;
  readonly plein: number;
  readonly marge: number;
};

const lireDrapeau = (args: ReadonlyArray<string>, nom: string): string | undefined =>
  args.find((arg) => arg.startsWith(`--${nom}=`))?.slice(nom.length + 3);

const lireNombre = (args: ReadonlyArray<string>, nom: string, defaut: number): number => {
  const brut = lireDrapeau(args, nom);
  if (brut === undefined) return defaut;

  const valeur = Number(brut);
  if (!Number.isFinite(valeur)) throw new Error(`--${nom} doit être un nombre`);

  return valeur;
};

const lireOptions = (args: ReadonlyArray<string>): Options => {
  const [source, destination] = args.filter((arg) => !arg.startsWith('--'));
  if (source === undefined || destination === undefined) throw new Error(USAGE);

  const seuil = lireNombre(args, 'seuil', 0.9);
  const plein = lireNombre(args, 'plein', 0.45);
  if (!(plein < seuil)) throw new Error('--plein doit être strictement inférieur à --seuil');

  const marge = lireNombre(args, 'marge', 0);
  if (marge < 0 || marge > 0.4) throw new Error('--marge doit être entre 0 et 0.4');

  return {
    source,
    destination,
    largeur: lireNombre(args, 'largeur', LARGEUR_PAR_DEFAUT),
    seuil,
    plein,
    marge,
  };
};

const resoudreSource = async (chemin: string): Promise<string> => {
  const mime = MIMES[extname(chemin).toLowerCase()];
  if (mime === undefined) throw new Error(`format de source non géré : ${chemin}`);

  const candidats = isAbsolute(chemin) ? [chemin] : [resolve(PROJECT_ROOT, chemin), resolve(SORTIES, chemin)];

  for (const candidat of candidats) {
    const contenu = await readFile(candidat).catch(() => null);
    if (contenu !== null) return `data:${mime};base64,${contenu.toString('base64')}`;
  }

  throw new Error(`source introuvable : ${chemin}`);
};

// La frange résiduelle se distingue d'un vrai rose par sa symétrie r≈b (le magenta en a toujours autant).
const DETOURER = `(source, largeur, seuil, plein, marge, type, qualite) => new Promise((resolve, reject) => {
  const image = new Image();
  image.onerror = () => reject(new Error('image illisible'));
  image.onload = () => {
    const echelle = largeur > 0 ? largeur / image.width : 1;
    const canvas = document.createElement('canvas');
    canvas.width = Math.round(image.width * echelle);
    canvas.height = Math.round(image.height * echelle);

    const contexte = canvas.getContext('2d', { willReadFrequently: true });
    contexte.imageSmoothingQuality = 'high';
    contexte.drawImage(image, 0, 0, canvas.width, canvas.height);

    const pixels = contexte.getImageData(0, 0, canvas.width, canvas.height);
    const donnees = pixels.data;

    const bande = Math.round(canvas.width * marge);

    for (let i = 0; i < donnees.length; i += 4) {
      const r = donnees[i];
      const v = donnees[i + 1];
      const b = donnees[i + 2];

      const colonne = (i / 4) % canvas.width;
      const dansLaMarge = colonne < bande || colonne >= canvas.width - bande;

      const magenta = Math.max(0, Math.min(r, b) - v) / 255;
      const alpha = dansLaMarge
        ? 0
        : 1 - Math.max(0, Math.min(1, (magenta - plein) / (seuil - plein)));

      if (alpha <= 0) {
        donnees[i] = 0;
        donnees[i + 1] = 0;
        donnees[i + 2] = 0;
        donnees[i + 3] = 0;
        continue;
      }

      const fond = (1 - alpha) * 255;
      const rendu = [
        Math.round(Math.max(0, Math.min(255, (r - fond) / alpha))),
        Math.round(Math.max(0, Math.min(255, v / alpha))),
        Math.round(Math.max(0, Math.min(255, (b - fond) / alpha))),
      ];

      const frange = Math.min(rendu[0], rendu[2]) - rendu[1];
      const symetrie = Math.abs(rendu[0] - rendu[2]);
      const correction = frange > 8 && symetrie < 32 ? rendu[1] + 8 : 255;

      donnees[i] = Math.min(rendu[0], correction);
      donnees[i + 1] = rendu[1];
      donnees[i + 2] = Math.min(rendu[2], correction);
      donnees[i + 3] = Math.round(alpha * 255);
    }

    contexte.putImageData(pixels, 0, 0);
    resolve(canvas.toDataURL(type, qualite).split(',')[1]);
  };
  image.src = source;
})`;

const detourer = async (options: Options, source: string): Promise<Buffer> => {
  const { default: puppeteer } = await import('puppeteer');

  const sortie = MIMES[extname(options.destination).toLowerCase()];
  if (sortie !== 'image/png' && sortie !== 'image/webp') {
    throw new Error('la destination doit être un .png ou un .webp — l\'alpha ne survit pas au JPEG');
  }

  const navigateur = await puppeteer.launch({ headless: true });

  try {
    const page = await navigateur.newPage();
    await page.setContent('<!doctype html><meta charset="utf-8">', { waitUntil: 'load' });

    const arguments_ = [source, options.largeur, options.seuil, options.plein, options.marge, sortie, QUALITE_WEBP]
      .map((valeur) => JSON.stringify(valeur))
      .join(', ');

    const base64: unknown = await page.evaluate(`(${DETOURER})(${arguments_})`);

    if (typeof base64 !== 'string') throw new Error('le détourage n\'a rien renvoyé');

    return Buffer.from(base64, 'base64');
  } finally {
    await navigateur.close();
  }
};

const main = async (): Promise<void> => {
  const options = lireOptions(process.argv.slice(2));
  const source = await resoudreSource(options.source);
  const image = await detourer(options, source);

  const destination = isAbsolute(options.destination)
    ? options.destination
    : resolve(PROJECT_ROOT, options.destination);

  await mkdir(dirname(destination), { recursive: true });
  await writeFile(destination, image);

  const taille = (image.length / 1024).toFixed(0);
  console.log(`${relative(PROJECT_ROOT, destination).replaceAll('\\', '/')}  ${taille} ko`);
};

await main().catch((err: unknown) => {
  console.error(err instanceof Error ? err.message : String(err));
  process.exitCode = 1;
});
