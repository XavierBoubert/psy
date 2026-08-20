import { z } from 'zod';

const KEBAB = /^[a-z0-9-]+$/;
const JOUR = /^\d{4}-\d{2}-\d{2}$/;
const HORODATAGE = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d+)?(?:Z|[+-]\d{2}:\d{2})$/;

const NI_ZERO = 'ou null quand Xavier n\'a pas repondu — et null ne veut pas dire 0';

// 🔴 Le socle longitudinal : ces sept champs tombent ensemble ou pas du tout, et l'ordre est celui du dossier.
export const NOYAU = [
  'shutdowns',
  'exposition_sociale',
  'retrait_sensoriel',
  'renoncements',
  'activites_investies',
  'sommeil_heures',
  'missions_actives',
] as const;

// ⭐ L'id de la carte fait le lien entre Kokoro et Claude Psy : c'est lui qui dit comment lire ses reponses.
export const CARTE_DU_JOURNAL = 'check-in';

export const QUESTION_DES_NOTES = 'notes';

const source = z.enum(['claude-code', 'android'], {
  error: 'source inconnue — deux valeurs, et il n\'y en a pas d\'autre : claude-code ou android',
});

const entierOuNull = z
  .int({ error: `entier attendu, ${NI_ZERO}` })
  .min(0, { error: 'on compte des occurrences, jamais un nombre negatif' })
  .nullable();

const echelleFermee = z
  .int({ error: `choix ferme de 0 a 3, ${NI_ZERO}` })
  .min(0, { error: 'choix ferme de 0 a 3' })
  .max(3, { error: 'choix ferme de 0 a 3' })
  .nullable();

const nombreOuNull = z
  .number({ error: `nombre attendu, ${NI_ZERO}` })
  .min(0, { error: 'jamais negatif' })
  .nullable();

export const JournalSchema = z.object(
  {
    date: z
      .string({ error: 'champ absent' })
      .regex(JOUR, { error: 'hors AAAA-MM-JJ — le tri lexicographique est le tri chronologique (R4)' }),
    source,
    noyau: z.object(
      {
        shutdowns: entierOuNull,
        exposition_sociale: echelleFermee,
        retrait_sensoriel: entierOuNull,
        renoncements: entierOuNull,
        activites_investies: echelleFermee,
        sommeil_heures: nombreOuNull,
        missions_actives: entierOuNull,
      },
      { error: 'le noyau est absent — les sept champs sont le socle longitudinal, ils tombent ensemble ou pas du tout' },
    ),
    campagne: z.record(z.string(), nombreOuNull, {
      error: 'le bloc « campagne » est absent — vide si aucun chantier ne l\'alimente, mais present',
    }),
    notes: z.union([z.string(), z.null()], { error: 'ni null ni du texte — et toujours facultatif (R5)' }),
  },
  { error: 'la racine n\'est pas un objet' },
);

// Un item porte une valeur chiffree, ou le texte d'une note — jamais les deux, jamais aucun des deux.
const item = z
  .object(
    {
      question: z
        .string({ error: 'champ absent' })
        .regex(KEBAB, { error: 'hors kebab-case — c\'est lui qui relie un item a sa reponse' }),
      valeur: z
        .number({ error: 'nombre attendu, ou null quand l\'item a ete passe — et null n\'est pas 0' })
        .nullable()
        .optional(),
      texte: z.union([z.string(), z.null()], { error: 'ni null ni du texte' }).optional(),
    },
    { error: 'ce n\'est pas un objet' },
  )
  .superRefine((rendu, ctx) => {
    const portees = [rendu.valeur, rendu.texte].filter((portee) => portee !== undefined).length;

    if (portees !== 1) {
      ctx.addIssue({
        code: 'custom',
        path: ['valeur'],
        message: 'un item porte « valeur » OU « texte », jamais les deux ni aucun des deux',
      });
    }
  });

export const ReponseSchema = z.object(
  {
    carte: z
      .string({ error: 'champ absent' })
      .regex(KEBAB, { error: 'hors kebab-case — c\'est lui qui relie une reponse a sa carte' }),
    horodatage: z
      .string({ error: 'champ absent' })
      .regex(HORODATAGE, { error: 'hors ISO 8601 avec decalage horaire' }),
    issue: z.enum(['termine', 'arrete_avant_la_fin', 'fait', 'entrainement'], {
      error: 'issue inconnue — attendu : termine, arrete_avant_la_fin, fait ou entrainement',
    }),
    reponses: z
      .array(item, { error: 'une liste ordonnee d\'items, ou null quand la carte ne pose aucune question' })
      .nullable(),
    source,
  },
  { error: 'la racine n\'est pas un objet' },
);

export type Journal = z.infer<typeof JournalSchema>;
export type Reponse = z.infer<typeof ReponseSchema>;
