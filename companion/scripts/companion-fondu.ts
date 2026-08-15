import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, extname, isAbsolute, relative, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const RETENUS = resolve(PROJECT_ROOT, 'companion/ressources/retenus');

const LARGEUR_PAR_DEFAUT = 1080;
const QUALITE_WEBP = 0.92;

const MIMES: Readonly<Record<string, string>> = {
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.webp': 'image/webp',
};

const USAGE = `Usage: companion-fondu <source> <destination.webp|.png> [--ratio=<n>] [--ancrage=<0-1>]
                              [--opacite=<0-1>] [--gauche=<0-1>] [--droite=<0-1>] [--vertical=<0-1>]
                              [--largeur=<px>]

  <source>        illustration de départ — chemin projet, ou nom de fichier sous ressources/retenus/
  <destination>   chemin projet du fichier écrit ; l'extension décide du format
  --ratio         largeur / hauteur du bandeau découpé (défaut 4.6, la zone de contenu mesurée)
  --ancrage       où prendre la bande dans la hauteur : 0 en haut, 1 en bas (défaut 0.35)
  --opacite       opacité du bandeau au centre (défaut 0.6)
  --gauche        largeur du fondu à gauche, en fraction de la largeur (défaut 0.38)
  --droite        idem à droite (défaut 0.12) — court exprès, le personnage y vit
  --vertical      idem en haut et en bas, en fraction de la hauteur (défaut 0.09)

Produit le fond des notifications de Kokoro : une bande recadrée dans l'illustration, ramenée à
l'opacité voulue et fondue vers le transparent sur ses quatre côtés.

🔴 La transparence est cuite dans le fichier, et ce n'est pas un raccourci : un masque alpha
n'existe pas en drawable XML, et une RemoteViews ne sait pas composer deux couches.

⚠️ Le layout qui affiche le résultat doit rester en « fitXY ». « centerCrop » recadre, donc il
jetterait hors du cadre précisément les bords qui adoucissent — c'est pour ça que --ratio existe.`;

type Options = {
  readonly source: string;
  readonly destination: string;
  readonly largeur: number;
  readonly ratio: number;
  readonly ancrage: number;
  readonly opacite: number;
  readonly gauche: number;
  readonly droite: number;
  readonly vertical: number;
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

const lireFraction = (args: ReadonlyArray<string>, nom: string, defaut: number): number => {
  const valeur = lireNombre(args, nom, defaut);
  if (valeur < 0 || valeur > 1) throw new Error(`--${nom} doit tomber entre 0 et 1`);

  return valeur;
};

const lireOptions = (args: ReadonlyArray<string>): Options => {
  const [source, destination] = args.filter((arg) => !arg.startsWith('--'));
  if (source === undefined || destination === undefined) throw new Error(USAGE);

  const ratio = lireNombre(args, 'ratio', 4.6);
  if (ratio <= 0) throw new Error('--ratio doit être strictement positif');

  const gauche = lireFraction(args, 'gauche', 0.38);
  const droite = lireFraction(args, 'droite', 0.12);
  if (gauche + droite >= 1) throw new Error('--gauche et --droite se recouvrent : leur somme atteint la largeur');

  return {
    source,
    destination,
    largeur: lireNombre(args, 'largeur', LARGEUR_PAR_DEFAUT),
    ratio,
    ancrage: lireFraction(args, 'ancrage', 0.35),
    opacite: lireFraction(args, 'opacite', 0.6),
    gauche,
    droite,
    vertical: lireFraction(args, 'vertical', 0.09),
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

/**
 * Le fondu, exécuté dans la page — c'est le seul endroit où un canvas existe.
 *
 * `destination-in` garde le pixel déjà dessiné et lui impose l'alpha du masque qu'on peint
 * par-dessus. Les deux passes se **multiplient** : l'horizontale porte l'opacité générale et les
 * bords latéraux, la verticale ne fait qu'adoucir le haut et le bas. C'est ce produit qui arrondit
 * les quatre coins sans qu'on ait à décrire un dégradé radial.
 *
 * ⭐ L'ordre compte : si la verticale portait aussi l'opacité, les deux se multiplieraient et le
 * centre tomberait à 0,36 au lieu de 0,6.
 */
const FONDRE = `(source, largeur, ratio, ancrage, opacite, gauche, droite, vertical, type, qualite) =>
  new Promise((resolve, reject) => {
    const image = new Image();
    image.onerror = () => reject(new Error('image illisible'));
    image.onload = () => {
      const hauteurSource = image.width / ratio;
      if (hauteurSource > image.height) {
        reject(new Error('ratio trop large : la bande demandée dépasse la hauteur de la source'));
        return;
      }

      const y = (image.height - hauteurSource) * ancrage;
      const echelle = largeur > 0 ? largeur : image.width;
      const hauteur = Math.round((hauteurSource / image.width) * echelle);

      const canvas = document.createElement('canvas');
      canvas.width = echelle;
      canvas.height = hauteur;

      const contexte = canvas.getContext('2d');
      contexte.imageSmoothingQuality = 'high';
      contexte.drawImage(image, 0, y, image.width, hauteurSource, 0, 0, echelle, hauteur);

      contexte.globalCompositeOperation = 'destination-in';

      const horizontal = contexte.createLinearGradient(0, 0, echelle, 0);
      horizontal.addColorStop(0, 'rgba(0,0,0,0)');
      horizontal.addColorStop(gauche, 'rgba(0,0,0,' + opacite + ')');
      horizontal.addColorStop(1 - droite, 'rgba(0,0,0,' + opacite + ')');
      horizontal.addColorStop(1, 'rgba(0,0,0,0)');
      contexte.fillStyle = horizontal;
      contexte.fillRect(0, 0, echelle, hauteur);

      if (vertical > 0) {
        const haut = contexte.createLinearGradient(0, 0, 0, hauteur);
        haut.addColorStop(0, 'rgba(0,0,0,0)');
        haut.addColorStop(vertical, 'rgba(0,0,0,1)');
        haut.addColorStop(1 - vertical, 'rgba(0,0,0,1)');
        haut.addColorStop(1, 'rgba(0,0,0,0)');
        contexte.fillStyle = haut;
        contexte.fillRect(0, 0, echelle, hauteur);
      }

      resolve(canvas.toDataURL(type, qualite).split(',')[1]);
    };
    image.src = source;
  })`;

const fondre = async (options: Options, source: string): Promise<Buffer> => {
  const { default: puppeteer } = await import('puppeteer');

  const sortie = MIMES[extname(options.destination).toLowerCase()];
  if (sortie !== 'image/png' && sortie !== 'image/webp') {
    throw new Error('la destination doit être un .png ou un .webp — l\'alpha ne survit pas au JPEG');
  }

  const navigateur = await puppeteer.launch({ headless: true });

  try {
    const page = await navigateur.newPage();
    await page.setContent('<!doctype html><meta charset="utf-8">', { waitUntil: 'load' });

    const arguments_ = [
      source,
      options.largeur,
      options.ratio,
      options.ancrage,
      options.opacite,
      options.gauche,
      options.droite,
      options.vertical,
      sortie,
      QUALITE_WEBP,
    ]
      .map((valeur) => JSON.stringify(valeur))
      .join(', ');

    const base64: unknown = await page.evaluate(`(${FONDRE})(${arguments_})`);

    if (typeof base64 !== 'string') throw new Error('le fondu n\'a rien renvoyé');

    return Buffer.from(base64, 'base64');
  } finally {
    await navigateur.close();
  }
};

const main = async (): Promise<void> => {
  const options = lireOptions(process.argv.slice(2));
  const source = await resoudreSource(options.source);
  const image = await fondre(options, source);

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
