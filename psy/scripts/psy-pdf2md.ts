import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import pdf2md from '@opendocsg/pdf2md';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');

type ConvertArgs = {
  readonly sourcePath: string;
  readonly destinationPath: string;
};

const USAGE = 'Usage: psy-pdf2md <source.pdf> <destination.md>';

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

const convertPdfToMarkdown = async ({ sourcePath, destinationPath }: ConvertArgs): Promise<void> => {
  const pdfBuffer = await readFile(sourcePath);
  const markdown = await pdf2md(pdfBuffer);
  await mkdir(dirname(destinationPath), { recursive: true });
  await writeFile(destinationPath, markdown, 'utf8');
};

const main = async (): Promise<void> => {
  const args = parseArgs(process.argv.slice(2));
  await convertPdfToMarkdown(args);
  console.log(`Converted ${args.sourcePath} -> ${args.destinationPath}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
