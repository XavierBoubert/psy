declare module '@opendocsg/pdf2md' {
  const pdf2md: (pdfBuffer: Uint8Array) => Promise<string>;
  export = pdf2md;
}
