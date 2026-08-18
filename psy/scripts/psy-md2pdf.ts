import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import { convertirEnPdf, type Conversion } from './md2pdf.ts';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');

const USAGE = 'Usage: psy-md2pdf <source.md> <destination.pdf>';

const lireArguments = (argv: readonly string[]): Conversion => {
  const [source, destination] = argv;
  if (!source || !destination) throw new Error(USAGE);

  return {
    sourcePath: resolve(PROJECT_ROOT, source),
    destinationPath: resolve(PROJECT_ROOT, destination),
  };
};

const main = async (): Promise<void> => {
  const conversion = lireArguments(process.argv.slice(2));
  await convertirEnPdf([conversion]);
  console.log(`converti ${conversion.sourcePath} -> ${conversion.destinationPath}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
