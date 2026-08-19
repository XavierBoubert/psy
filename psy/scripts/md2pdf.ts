import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { basename, dirname, extname } from 'node:path';
import { marked } from 'marked';
import puppeteer, { type Browser } from 'puppeteer';

export type Conversion = {
  readonly sourcePath: string;
  readonly destinationPath: string;
};

// Page taillee pour un ecran de telephone en liseuse : ajustee a la largeur, une page tient d'un seul tenant.
const PDF_STYLESHEET = `
  @page { size: 90mm 155mm; margin: 9mm 7mm; }
  html { hyphens: auto; }
  body {
    font-family: Georgia, 'Iowan Old Style', 'Times New Roman', serif;
    font-size: 12pt;
    line-height: 1.55;
    color: #1a1a1a;
    text-align: left;
    orphans: 2;
    widows: 2;
    overflow-wrap: break-word;
  }
  p { margin: 0 0 0.7em; }
  h1, h2, h3, h4 {
    font-family: 'Segoe UI', -apple-system, Helvetica, Arial, sans-serif;
    line-height: 1.25;
    hyphens: none;
    page-break-after: avoid;
  }
  h1 { font-size: 17pt; margin: 0 0 0.6em; padding-bottom: 4pt; border-bottom: 1px solid #999; }
  h2 { font-size: 13.5pt; margin: 1.4em 0 0.4em; color: #333; }
  h3 { font-size: 12pt; margin: 1.1em 0 0.3em; }
  h4 { font-size: 11pt; margin: 1em 0 0.3em; }
  ul, ol { margin: 0 0 0.7em; padding-left: 1.15em; }
  li { margin-bottom: 0.35em; }
  table { width: 100%; border-collapse: collapse; margin: 0.7em 0; font-size: 9pt; hyphens: none; }
  th, td { border: 1px solid #ccc; padding: 3pt 4pt; text-align: left; vertical-align: top; }
  th { background: #f0f0f0; }
  tr { page-break-inside: avoid; }
  blockquote {
    margin: 0.8em 0;
    padding: 5pt 8pt;
    border-left: 2px solid #999;
    background: #f4f4f4;
    page-break-inside: avoid;
  }
  blockquote p:last-child { margin-bottom: 0; }
  code, pre { font-family: Consolas, 'Cascadia Code', monospace; font-size: 10pt; hyphens: none; }
  pre { background: #f4f4f4; padding: 6pt; white-space: pre-wrap; word-break: break-word; }
  hr { border: none; border-top: 1px solid #ccc; margin: 1.2em 0; }
  img { max-width: 100%; }
  a { color: #1a1a1a; }
`;

const documentHtml = (bodyHtml: string, titre: string): string => `<!doctype html>
<html lang="fr">
<head>
<meta charset="utf-8">
<title>${titre}</title>
<style>${PDF_STYLESHEET}</style>
</head>
<body>${bodyHtml}</body>
</html>`;

const rendre = async (navigateur: Browser, { sourcePath, destinationPath }: Conversion): Promise<void> => {
  const markdown = await readFile(sourcePath, 'utf8');
  const corps = await marked.parse(markdown, { gfm: true, breaks: false });
  const html = documentHtml(corps, basename(sourcePath, extname(sourcePath)));

  const page = await navigateur.newPage();

  try {
    await page.setContent(html, { waitUntil: 'load' });
    await mkdir(dirname(destinationPath), { recursive: true });
    await writeFile(destinationPath, await page.pdf({ printBackground: true, preferCSSPageSize: true }));
  } finally {
    await page.close();
  }
};

export const convertirEnPdf = async (conversions: ReadonlyArray<Conversion>): Promise<void> => {
  if (conversions.length === 0) return;

  const navigateur = await puppeteer.launch();

  try {
    await Promise.all(conversions.map((conversion) => rendre(navigateur, conversion)));
  } finally {
    await navigateur.close();
  }
};
