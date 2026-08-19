import type { $ZodIssue } from 'zod/v4/core';

const lisible = (chemin: ReadonlyArray<PropertyKey>): string =>
  chemin.reduce<string>(
    (accumule, cle) =>
      typeof cle === 'number' ? `${accumule}[${cle}]` : accumule ? `${accumule}.${String(cle)}` : String(cle),
    '',
  );

export const decrire = (issue: $ZodIssue, depuis = 0): string => {
  const chemin = lisible(issue.path.slice(depuis));

  return chemin ? `${chemin} : ${issue.message}` : issue.message;
};
