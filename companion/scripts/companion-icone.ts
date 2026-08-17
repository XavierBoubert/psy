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
  '.svg': 'image/svg+xml',
};

// Un SVG est rastérisé à sa taille intrinsèque : on la gonfle avant de le charger.
const AGRANDIR_SVG = 8;

const USAGE = `Usage: companion-icone [<source>] [--face=<fichier>] [--res=<dossier>] [--marge=<0-0.5>]
                             [--seuil=<0-255>] [--plein=<0-255>] [--sombre=<0-255>] [--clair=<0-255>]

  <source>   le logo — chemin projet, ou nom de fichier sous ressources/retenus/ (défaut logo.jpg)
  --face     le visage de l'icône de notification (défaut kokoro-face.svg)
  --res      dossier res/ où écrire (défaut ${RES})
  --marge    part de la toile réservée sur chaque côté (défaut 1/6 = les 18 dp de l'icône adaptative)
  --plein    « froideur » (bleu − rouge) en deçà de laquelle le pixel est du personnage (défaut 20)
  --seuil    froideur au-delà de laquelle le pixel est du fond (défaut 45)
  --sombre   luminance en deçà de laquelle un pixel du personnage est un trait (défaut 120)
  --clair    luminance au-delà de laquelle il est un aplat (défaut 185)

Produit les trois tailles d'icône de Kokoro :

  mipmap-*/ic_lanceur_avant.webp   la couche avant de l'icône adaptative — le logo entier
  mipmap-*/ic_lanceur_mono.png     la couche monochrome (icônes thématiques d'Android 13)
  drawable-*/ic_kokoro.png         l'icône de notification, 24 dp, le visage en aplat

🔴 Le logo déborde de son cadre — la main et le corps touchent les bords. Le réduire aux 72 dp
garantis et **prolonger le pourtour en étirant les pixels de bord** est ce qui le fait survivre à
tous les masques : rien n'est recadré dans la zone sûre, et aucune couture carrée n'apparaît là où
le masque montre plus.

⭐ Les deux icônes en aplat ne sont pas un détourage : une icône de notification est **repeinte
d'une seule couleur** par le système. On garde donc les aplats clairs et on **perce** les traits —
yeux, sourire, contours — qui redeviennent des trous.

⭐ L'icône de notification ne vient pas du logo mais du visage dessiné à part : à 24 dp le
personnage entier n'est plus lisible. Le visage est rogné sur ses pixels opaques puis centré, pour
occuper toute la toile utile.`;

type Options = {
  readonly source: string;
  readonly face: string;
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
    face: lireDrapeau(args, 'face') || 'kokoro-face.svg',
    res: lireDrapeau(args, 'res') || RES,
    marge,
    plein,
    seuil,
    sombre,
    clair,
  };
};

const grossirSvg = (contenu: Buffer): Buffer =>
  Buffer.from(
    contenu
      .toString('utf8')
      .replace(/<svg\b[^>]*>/, (balise) =>
        balise.replace(
          /\b(width|height)="([\d.]+)"/g,
          (_, attribut: string, valeur: string) => `${attribut}="${Number(valeur) * AGRANDIR_SVG}"`,
        ),
      ),
    'utf8',
  );

const resoudreSource = async (chemin: string): Promise<string> => {
  const extension = extname(chemin).toLowerCase();
  const mime = MIMES[extension];
  if (mime === undefined) throw new Error(`format de source non géré : ${chemin}`);

  const candidats = isAbsolute(chemin) ? [chemin] : [resolve(PROJECT_ROOT, chemin), resolve(RETENUS, chemin)];

  for (const candidat of candidats) {
    const contenu = await readFile(candidat).catch(() => null);
    if (contenu === null) continue;

    const image = extension === '.svg' ? grossirSvg(contenu) : contenu;

    return `data:${mime};base64,${image.toString('base64')}`;
  }

  throw new Error(`source introuvable : ${chemin}`);
};

const FABRIQUER = `(sources, options, plans) => {
  const charger = (source) => new Promise((ok, echec) => {
    const image = new Image();
    image.onerror = () => echec(new Error('image illisible'));
    image.onload = () => ok(image);
    image.src = source;
  });

  const toile = (largeur, hauteur) => {
    const canvas = document.createElement('canvas');
    canvas.width = largeur;
    canvas.height = hauteur;
    const contexte = canvas.getContext('2d', { willReadFrequently: true });
    contexte.imageSmoothingQuality = 'high';
    return { canvas, contexte };
  };

  const borne = (valeur) => Math.max(0, Math.min(1, valeur));

  const aplat = (luminance) => borne((luminance - options.sombre) / (options.clair - options.sombre));

  const luminance = (r, v, b) => (r * 299 + v * 587 + b * 114) / 1000;

  const repeindre = (image, alpha) => {
    const { canvas, contexte } = toile(image.width, image.height);
    contexte.drawImage(image, 0, 0);

    const pixels = contexte.getImageData(0, 0, canvas.width, canvas.height);
    const donnees = pixels.data;

    for (let i = 0; i < donnees.length; i += 4) {
      const opacite = alpha(donnees[i], donnees[i + 1], donnees[i + 2], donnees[i + 3]);

      donnees[i] = 255;
      donnees[i + 1] = 255;
      donnees[i + 2] = 255;
      donnees[i + 3] = Math.round(opacite * 255);
    }

    contexte.putImageData(pixels, 0, 0);
    return canvas;
  };

  const percer = (image) => repeindre(image, (r, v, b) => {
    const froideur = b - r;
    const sujet = 1 - borne((froideur - options.plein) / (options.seuil - options.plein));
    return sujet * aplat(luminance(r, v, b));
  });

  const masquer = (image) => repeindre(image, (r, v, b, a) => (a / 255) * aplat(luminance(r, v, b)));

  const rogner = (source) => {
    const donnees = source.getContext('2d').getImageData(0, 0, source.width, source.height).data;

    let cadre = { gauche: source.width, haut: source.height, droite: -1, bas: -1 };
    for (let y = 0; y < source.height; y += 1) {
      for (let x = 0; x < source.width; x += 1) {
        cadre = donnees[(y * source.width + x) * 4 + 3]
          ? {
              gauche: Math.min(cadre.gauche, x),
              haut: Math.min(cadre.haut, y),
              droite: Math.max(cadre.droite, x),
              bas: Math.max(cadre.bas, y),
            }
          : cadre;
      }
    }

    if (cadre.droite < 0) return source;

    const l = cadre.droite - cadre.gauche + 1;
    const h = cadre.bas - cadre.haut + 1;
    const { canvas, contexte } = toile(l, h);
    contexte.drawImage(source, cadre.gauche, cadre.haut, l, h, 0, 0, l, h);

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

  const centrer = (contexte, dessin, taille, marge) => {
    const dedans = taille - 2 * marge;
    const facteur = Math.min(dedans / dessin.width, dedans / dessin.height);
    const l = Math.round(dessin.width * facteur);
    const h = Math.round(dessin.height * facteur);

    contexte.drawImage(dessin, Math.round((taille - l) / 2), Math.round((taille - h) / 2), l, h);
  };

  return Promise.all([charger(sources.logo), charger(sources.face)]).then(([logo, face]) => {
    const aplats = percer(logo);
    const visage = rogner(masquer(face));

    const matieres = {
      logo: (contexte, taille, marge) => poser(contexte, logo, taille, marge),
      aplats: (contexte, taille, marge) =>
        contexte.drawImage(aplats, marge, marge, taille - 2 * marge, taille - 2 * marge),
      face: (contexte, taille, marge) => centrer(contexte, visage, taille, marge),
    };

    const rendus = {};
    for (const plan of plans) {
      const marge = Math.round(plan.taille * plan.marge);
      const { canvas, contexte } = toile(plan.taille, plan.taille);

      matieres[plan.matiere](contexte, plan.taille, marge);

      rendus[plan.chemin] = canvas.toDataURL(plan.type, plan.qualite).split(',')[1];
    }

    return rendus;
  });
}`;

type Plan = {
  readonly chemin: string;
  readonly taille: number;
  readonly marge: number;
  readonly matiere: 'logo' | 'aplats' | 'face';
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
      matiere: 'face' as const,
      type: 'image/png',
      qualite: 1,
    },
  ]);

type Sources = {
  readonly logo: string;
  readonly face: string;
};

const fabriquer = async (options: Options, sources: Sources): Promise<ReadonlyMap<string, Buffer>> => {
  const { default: puppeteer } = await import('puppeteer');

  const navigateur = await puppeteer.launch({ headless: true });

  try {
    const page = await navigateur.newPage();
    await page.setContent('<!doctype html><meta charset="utf-8">', { waitUntil: 'load' });

    const arguments_ = [sources, options, plans(options)].map((valeur) => JSON.stringify(valeur)).join(', ');
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

  const [logo, face] = await Promise.all([resoudreSource(options.source), resoudreSource(options.face)]);
  const rendus = await fabriquer(options, { logo, face });

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
