import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { basename, dirname, extname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { marked } from 'marked';
import puppeteer from 'puppeteer';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');

type ConvertArgs = {
  readonly sourcePath: string;
  readonly destinationPath: string;
};

const USAGE = 'Usage: psy-md2pdf <source.md> <destination.pdf>';

const parseArgs = (argv: readonly string[]): ConvertArgs => {
  const [source, destination] = argv;
  if (!source || !destination) {
    throw new Error(USAGE);
  }
  return {
    sourcePath: resolve(PROJECT_ROOT, source),
    destinationPath: resolve(PROJECT_ROOT, destination),
  };
};

const PDF_STYLESHEET = `
  @page { margin: 20mm 16mm; }
  body {
    font-family: -apple-system, 'Segoe UI', Helvetica, Arial, sans-serif;
    font-size: 11pt;
    line-height: 1.5;
    color: #1a1a1a;
  }
  h1, h2, h3, h4 { line-height: 1.25; page-break-after: avoid; }
  h1 { font-size: 20pt; border-bottom: 2px solid #333; padding-bottom: 6pt; }
  h2 { font-size: 15pt; margin-top: 22pt; border-bottom: 1px solid #ccc; padding-bottom: 4pt; }
  h3 { font-size: 12.5pt; margin-top: 16pt; }
  h4 { font-size: 11pt; margin-top: 12pt; }
  table { width: 100%; border-collapse: collapse; margin: 10pt 0; font-size: 9.5pt; }
  th, td { border: 1px solid #ccc; padding: 4pt 6pt; text-align: left; vertical-align: top; }
  th { background: #f0f0f0; }
  tr { page-break-inside: avoid; }
  blockquote { margin: 10pt 0; padding: 4pt 10pt; border-left: 3px solid #999; color: #444; background: #f7f7f7; }
  code, pre { font-family: 'Cascadia Code', Consolas, monospace; font-size: 9pt; }
  pre { background: #f4f4f4; padding: 8pt; overflow-x: auto; }
  hr { border: none; border-top: 1px solid #ccc; margin: 16pt 0; }
  a { color: #1a1a1a; }
`;

const wrapAsHtmlDocument = (bodyHtml: string, title: string): string => `<!doctype html>
<html lang="fr">
<head>
<meta charset="utf-8">
<title>${title}</title>
<style>${PDF_STYLESHEET}</style>
</head>
<body>${bodyHtml}</body>
</html>`;

const convertMarkdownToPdf = async ({ sourcePath, destinationPath }: ConvertArgs): Promise<void> => {
  const markdown = await readFile(sourcePath, 'utf8');
  const bodyHtml = await marked.parse(markdown, { gfm: true, breaks: false });
  const title = basename(sourcePath, extname(sourcePath));
  const html = wrapAsHtmlDocument(bodyHtml, title);

  const browser = await puppeteer.launch();

  try {
    const page = await browser.newPage();
    await page.setContent(html, { waitUntil: 'load' });

    await mkdir(dirname(destinationPath), { recursive: true });
    const pdfBuffer = await page.pdf({ format: 'A4', printBackground: true, preferCSSPageSize: true });
    await writeFile(destinationPath, pdfBuffer);
  } finally {
    await browser.close();
  }
};

const main = async (): Promise<void> => {
  const args = parseArgs(process.argv.slice(2));
  await convertMarkdownToPdf(args);
  console.log(`Converted ${args.sourcePath} -> ${args.destinationPath}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
