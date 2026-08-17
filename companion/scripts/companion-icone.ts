import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, extname, isAbsolute, relative, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const RETENUS = resolve(PROJECT_ROOT, 'companion/ressources/retenus');
const RES = 'companion/android/app/src/main/res';

const QUALITE_WEBP = 0.92;

// 108 dp de toile, 72 dp garantis visibles — la géométrie imposée par l'icône adaptative.
const TOILE_LANCEUR = 108;

// 24 dp de statut, 22 dp de dessin — la marge d'une icône de notification, et elle est mince.
const TOILE_NOTIF = 24;
const MARGE_NOTIF = 1;

const DENSITES: ReadonlyArray<{ readonly nom: string; readonly facteur: number }> = [
  { nom: 'mdpi', facteur: 1 },
  { nom: 'hdpi', facteur: 1.5 },
  { nom: 'xhdpi', facteur: 2 },
  { nom: 'xxhdpi', facteur: 3 },
  { nom: 'xxxhdpi', facteur: 4 },
];

const MIMES: Readonly<Record<string, string>> = {
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.webp': 'image/webp',
};

const USAGE = `Usage: companion-icone [<source>] [--res=<dossier>] [--marge=<0-0.5>]
                             [--seuil=<0-255>] [--plein=<0-255>] [--sombre=<0-255>] [--clair=<0-255>]

  <source>   le logo — chemin projet, ou nom de fichier sous ressources/retenus/ (défaut logo.jpg)
  --res      dossier res/ où écrire (défaut ${RES})
  --marge    part de la toile réservée sur chaque côté (défaut 1/6 = les 18 dp de l'icône adaptative)
  --plein    « froideur » (bleu − rouge) en deçà de laquelle le pixel est du personnage (défaut 20)
  --seuil    froideur au-delà de laquelle le pixel est du fond (défaut 45)
  --sombre   luminance en deçà de laquelle un pixel du personnage est un trait (défaut 120)
  --clair    luminance au-delà de laquelle il est un aplat (défaut 185)

Produit les trois tailles d'icône de Kokoro à partir du logo :

  mipmap-*/ic_lanceur_avant.webp   la couche avant de l'icône adaptative — le logo entier
  mipmap-*/ic_lanceur_mono.png     la couche monochrome (icônes thématiques d'Android 13)
  drawable-*/ic_kokoro.png         l'icône de notification, 24 dp, le personnage en aplat

🔴 Le logo déborde de son cadre — la main et le corps touchent les bords. Le réduire aux 72 dp
garantis et **prolonger le pourtour en étirant les pixels de bord** est ce qui le fait survivre à
tous les masques : rien n'est recadré dans la zone sûre, et aucune couture carrée n'apparaît là où
le masque montre plus.

⭐ Les deux icônes en aplat ne sont pas un détourage : une icône de notification est **repeinte
d'une seule couleur** par le système. On garde donc les aplats clairs du personnage et on **perce**
ses traits — yeux, sourire, contours — qui redeviennent des trous.`;

type Options = {
  readonly source: string;
  readonly res: string;
  readonly marge: number;
  readonly plein: number;
  readonly seuil: number;
  readonly sombre: number;
  readonly clair: number;
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
  if (args.includes('--aide')) throw new Error(USAGE);

  const [source] = args.filter((arg) => !arg.startsWith('--'));

  const marge = lireNombre(args, 'marge', 1 / 6);
  if (marge < 0 || marge >= 0.5) throw new Error('--marge doit tomber entre 0 et 0,5');

  const plein = lireNombre(args, 'plein', 20);
  const seuil = lireNombre(args, 'seuil', 45);
  if (!(plein < seuil)) throw new Error('--plein doit être strictement inférieur à --seuil');

  const sombre = lireNombre(args, 'sombre', 120);
  const clair = lireNombre(args, 'clair', 185);
  if (!(sombre < clair)) throw new Error('--sombre doit être strictement inférieur à --clair');

  return {
    source: source || 'logo.jpg',
    res: lireDrapeau(args, 'res') || RES,
    marge,
    plein,
    seuil,
    sombre,
    clair,
  };
};

const resoudreSource = async (chemin: string): Promise<string> => {
  const mime = MIMES[extname(chemin).toLowerCase()];
  if (mime === undefined) throw new Error(`format de source non géré : ${chemin}`);

  const candidats = isAbsolute(chemin) ? [chemin] : [resolve(PROJECT_ROOT, chemin), resolve(RETENUS, chemin)];

  for (const candidat of candidats) {
    const contenu = await readFile(candidat).catch(() => null);
    if (contenu !== null) return `data:${mime};base64,${contenu.toString('base64')}`;
  }

  throw new Error(`source introuvable : ${chemin}`);
};

const FABRIQUER = `(source, options, plans) => new Promise((resolve, reject) => {
  const image = new Image();
  image.onerror = () => reject(new Error('image illisible'));
  image.onload = () => {
    const toile = (largeur, hauteur) => {
      const canvas = document.createElement('canvas');
      canvas.width = largeur;
      canvas.height = hauteur;
      const contexte = canvas.getContext('2d', { willReadFrequently: true });
      contexte.imageSmoothingQuality = 'high';
      return { canvas, contexte };
    };

    const percer = () => {
      const { canvas, contexte } = toile(image.width, image.height);
      contexte.drawImage(image, 0, 0);

      const pixels = contexte.getImageData(0, 0, canvas.width, canvas.height);
      const donnees = pixels.data;

      for (let i = 0; i < donnees.length; i += 4) {
        const r = donnees[i];
        const v = donnees[i + 1];
        const b = donnees[i + 2];

        const froideur = b - r;
        const sujet = 1 - Math.max(0, Math.min(1, (froideur - options.plein) / (options.seuil - options.plein)));
        const luminance = (r * 299 + v * 587 + b * 114) / 1000;
        const aplat = Math.max(0, Math.min(1, (luminance - options.sombre) / (options.clair - options.sombre)));

        donnees[i] = 255;
        donnees[i + 1] = 255;
        donnees[i + 2] = 255;
        donnees[i + 3] = Math.round(sujet * aplat * 255);
      }

      contexte.putImageData(pixels, 0, 0);
      return canvas;
    };

    const poser = (contexte, dessin, taille, marge) => {
      const dedans = taille - 2 * marge;
      const l = dessin.width;
      const h = dessin.height;

      contexte.drawImage(dessin, 0, 0, l, h, marge, marge, dedans, dedans);
      if (!marge) return;

      contexte.drawImage(dessin, 0, 0, 1, h, 0, marge, marge, dedans);
      contexte.drawImage(dessin, l - 1, 0, 1, h, taille - marge, marge, marge, dedans);
      contexte.drawImage(dessin, 0, 0, l, 1, marge, 0, dedans, marge);
      contexte.drawImage(dessin, 0, h - 1, l, 1, marge, taille - marge, dedans, marge);

      contexte.drawImage(dessin, 0, 0, 1, 1, 0, 0, marge, marge);
      contexte.drawImage(dessin, l - 1, 0, 1, 1, taille - marge, 0, marge, marge);
      contexte.drawImage(dessin, 0, h - 1, 1, 1, 0, taille - marge, marge, marge);
      contexte.drawImage(dessin, l - 1, h - 1, 1, 1, taille - marge, taille - marge, marge, marge);
    };

    const aplats = percer();

    const rendus = {};
    for (const plan of plans) {
      const marge = Math.round(plan.taille * plan.marge);
      const { canvas, contexte } = toile(plan.taille, plan.taille);

      if (plan.matiere === 'logo') {
        poser(contexte, image, plan.taille, marge);
      } else {
        contexte.drawImage(aplats, marge, marge, plan.taille - 2 * marge, plan.taille - 2 * marge);
      }

      rendus[plan.chemin] = canvas.toDataURL(plan.type, plan.qualite).split(',')[1];
    }

    resolve(rendus);
  };
  image.src = source;
})`;

type Plan = {
  readonly chemin: string;
  readonly taille: number;
  readonly marge: number;
  readonly matiere: 'logo' | 'aplats';
  readonly type: string;
  readonly qualite: number;
};

const plans = (options: Options): ReadonlyArray<Plan> =>
  DENSITES.flatMap(({ nom, facteur }) => [
    {
      chemin: `mipmap-${nom}/ic_lanceur_avant.webp`,
      taille: Math.round(TOILE_LANCEUR * facteur),
      marge: options.marge,
      matiere: 'logo' as const,
      type: 'image/webp',
      qualite: QUALITE_WEBP,
    },
    {
      chemin: `mipmap-${nom}/ic_lanceur_mono.png`,
      taille: Math.round(TOILE_LANCEUR * facteur),
      marge: options.marge,
      matiere: 'aplats' as const,
      type: 'image/png',
      qualite: 1,
    },
    {
      chemin: `drawable-${nom}/ic_kokoro.png`,
      taille: Math.round(TOILE_NOTIF * facteur),
      marge: MARGE_NOTIF / TOILE_NOTIF,
      matiere: 'aplats' as const,
      type: 'image/png',
      qualite: 1,
    },
  ]);

const fabriquer = async (options: Options, source: string): Promise<ReadonlyMap<string, Buffer>> => {
  const { default: puppeteer } = await import('puppeteer');

  const navigateur = await puppeteer.launch({ headless: true });

  try {
    const page = await navigateur.newPage();
    await page.setContent('<!doctype html><meta charset="utf-8">', { waitUntil: 'load' });

    const arguments_ = [source, options, plans(options)].map((valeur) => JSON.stringify(valeur)).join(', ');
    const rendus: unknown = await page.evaluate(`(${FABRIQUER})(${arguments_})`);

    if (typeof rendus !== 'object' || rendus === null) throw new Error('la fabrique n\'a rien renvoyé');

    return new Map(
      Object.entries(rendus).map(([chemin, base64]) => {
        if (typeof base64 !== 'string') throw new Error(`rendu illisible : ${chemin}`);

        return [chemin, Buffer.from(base64, 'base64')];
      }),
    );
  } finally {
    await navigateur.close();
  }
};

const main = async (): Promise<void> => {
  const options = lireOptions(process.argv.slice(2));
  const source = await resoudreSource(options.source);
  const rendus = await fabriquer(options, source);

  const racine = isAbsolute(options.res) ? options.res : resolve(PROJECT_ROOT, options.res);

  for (const [chemin, image] of rendus) {
    const destination = resolve(racine, chemin);

    await mkdir(dirname(destination), { recursive: true });
    await writeFile(destination, image);

    const taille = (image.length / 1024).toFixed(1);
    console.log(`${relative(PROJECT_ROOT, destination).replaceAll('\\', '/')}  ${taille} ko`);
  }
};

await main().catch((err: unknown) => {
  console.error(err instanceof Error ? err.message : String(err));
  process.exitCode = 1;
});
