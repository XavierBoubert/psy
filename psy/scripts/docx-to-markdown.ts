import { mkdir, readFile, writeFile } from 'node:fs/promises';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';
import JSZip from 'jszip';
import { XMLParser } from 'fast-xml-parser';

const PROJECT_ROOT = resolve(dirname(fileURLToPath(import.meta.url)), '../..');

type ConvertArgs = {
  readonly sourcePath: string;
  readonly destinationPath: string;
};

const USAGE = 'Usage: docx-to-markdown <source.docx> <destination.md>';

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

type XmlElement = {
  readonly tag: string;
  readonly attrs: Readonly<Record<string, string>>;
  readonly children: readonly XmlElement[];
  readonly text: string;
};

const isRecord = (value: unknown): value is Readonly<Record<string, unknown>> =>
  typeof value === 'object' && value !== null && !Array.isArray(value);

const normalize = (nodes: unknown): readonly XmlElement[] => {
  if (!Array.isArray(nodes)) return [];
  return nodes.filter(isRecord).map((raw): XmlElement => {
    const tag = Object.keys(raw).find((key) => key !== ':@') ?? '#unknown';
    const rawAttrs = raw[':@'];
    const attrs = isRecord(rawAttrs)
      ? Object.fromEntries(Object.entries(rawAttrs).filter((entry): entry is [string, string] => typeof entry[1] === 'string'))
      : {};
    if (tag === '#text') {
      const value = raw['#text'];
      return { tag, attrs, children: [], text: typeof value === 'string' ? value : '' };
    }
    return { tag, attrs, children: normalize(raw[tag]), text: '' };
  });
};

const findAll = (element: XmlElement, tag: string): readonly XmlElement[] =>
  element.children.filter((child) => child.tag === tag);

const findFirst = (element: XmlElement, tag: string): XmlElement | undefined =>
  element.children.find((child) => child.tag === tag);

const GREEN_DOMINANCE_THRESHOLD = 15;

const isGreenFill = (hex: string): boolean => {
  if (!/^[0-9a-fA-F]{6}$/.test(hex)) return false;
  const r = Number.parseInt(hex.slice(0, 2), 16);
  const g = Number.parseInt(hex.slice(2, 4), 16);
  const b = Number.parseInt(hex.slice(4, 6), 16);
  return g > r + GREEN_DOMINANCE_THRESHOLD && g > b + GREEN_DOMINANCE_THRESHOLD;
};

const isSelectedCell = (cell: XmlElement): boolean => {
  const cellProps = findFirst(cell, 'w:tcPr');
  const shading = cellProps && findFirst(cellProps, 'w:shd');
  const fill = shading?.attrs['w:fill'];
  return fill !== undefined && isGreenFill(fill);
};

const isTruthyFlag = (element: XmlElement | undefined): boolean =>
  element !== undefined && element.attrs['w:val'] !== '0' && element.attrs['w:val'] !== 'false';

const runText = (run: XmlElement): string => {
  const rawText = run.children
    .map((child) => {
      if (child.tag === 'w:t') return child.children.map((t) => t.text).join('');
      if (child.tag === 'w:tab') return '\t';
      if (child.tag === 'w:br' || child.tag === 'w:cr') return '\n';
      return '';
    })
    .join('');
  if (!rawText) return '';
  const runProps = findFirst(run, 'w:rPr');
  const bold = isTruthyFlag(runProps && findFirst(runProps, 'w:b'));
  const italic = isTruthyFlag(runProps && findFirst(runProps, 'w:i'));
  const wrapped = italic ? `*${rawText}*` : rawText;
  return bold ? `**${wrapped}**` : wrapped;
};

const collectRuns = (element: XmlElement): readonly XmlElement[] =>
  element.children.flatMap((child) => (child.tag === 'w:r' ? [child] : child.tag === 'w:hyperlink' ? collectRuns(child) : []));

const HEADING_STYLES: Readonly<Record<string, string>> = {
  Title: '#',
  Heading1: '#',
  Heading2: '##',
  Heading3: '###',
  Heading4: '####',
  Heading5: '#####',
  Heading6: '######',
};

const paragraphToMarkdown = (paragraph: XmlElement): string => {
  const text = collectRuns(paragraph).map(runText).join('').trim();
  if (!text) return '';
  const paragraphProps = findFirst(paragraph, 'w:pPr');
  const styleName = paragraphProps && findFirst(paragraphProps, 'w:pStyle')?.attrs['w:val'];
  const headingPrefix = styleName ? HEADING_STYLES[styleName] : undefined;
  return headingPrefix ? `${headingPrefix} ${text}` : text;
};

const collapseWhitespace = (value: string): string => value.replace(/\s+/g, ' ').trim();

const escapeCell = (value: string): string => value.replace(/\|/g, '\\|').replace(/\n/g, '<br>');

const cellToMarkdown = (cell: XmlElement): string => {
  const text = collapseWhitespace(findAll(cell, 'w:p').map((p) => collectRuns(p).map(runText).join('')).join(' '));
  const escaped = escapeCell(text);
  return isSelectedCell(cell) ? `**✅ ${escaped}**` : escaped;
};

const tableToMarkdown = (table: XmlElement): string => {
  const rows = findAll(table, 'w:tr').map((row) => findAll(row, 'w:tc').map(cellToMarkdown));
  const columnCount = rows.reduce((max, row) => Math.max(max, row.length), 0);
  if (columnCount === 0) return '';
  const toLine = (cells: readonly string[]): string =>
    `| ${Array.from({ length: columnCount }, (_, i) => cells[i] ?? '').join(' | ')} |`;
  const header = toLine(Array.from({ length: columnCount }, () => ''));
  const separator = `|${Array.from({ length: columnCount }, () => ' --- ').join('|')}|`;
  return [header, separator, ...rows.map(toLine)].join('\n');
};

const bodyToMarkdown = (body: XmlElement): string =>
  body.children
    .map((child) => {
      if (child.tag === 'w:p') return paragraphToMarkdown(child);
      if (child.tag === 'w:tbl') return tableToMarkdown(child);
      return '';
    })
    .filter((block) => block.length > 0)
    .join('\n\n');

const convertDocxToMarkdown = async (docxBuffer: Buffer): Promise<string> => {
  const zip = await JSZip.loadAsync(docxBuffer);
  const documentFile = zip.file('word/document.xml');
  if (!documentFile) {
    throw new Error('word/document.xml not found in .docx archive');
  }
  const xml = await documentFile.async('string');
  const parser = new XMLParser({ ignoreAttributes: false, attributeNamePrefix: '@_', preserveOrder: true });
  const root = normalize(parser.parse(xml));
  const documentElement = root.find((el) => el.tag === 'w:document');
  const body = documentElement && findFirst(documentElement, 'w:body');
  if (!body) {
    throw new Error('w:body not found in document.xml');
  }
  return bodyToMarkdown(body);
};

const convertDocxFileToMarkdown = async ({ sourcePath, destinationPath }: ConvertArgs): Promise<void> => {
  const docxBuffer = await readFile(sourcePath);
  const markdown = await convertDocxToMarkdown(docxBuffer);
  await mkdir(dirname(destinationPath), { recursive: true });
  await writeFile(destinationPath, markdown, 'utf8');
};

const main = async (): Promise<void> => {
  const args = parseArgs(process.argv.slice(2));
  await convertDocxFileToMarkdown(args);
  console.log(`Converted ${args.sourcePath} -> ${args.destinationPath}`);
};

main().catch((error: unknown) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
