import { z } from 'zod';

export const TYPES = ['ecran', 'exercice', 'questionnaire', 'demarche', 'fiche', 'seance-duo', 'bilan'] as const;

const KEBAB = /^[a-z0-9-]+$/;
const JOUR = /^\d{4}-\d{2}-\d{2}$/;
const PHQ9 = /phq[\s-]?9/i;
const DERNIER_CRITERE = /ne sais pas quoi faire/i;

const identifiant = (precision = '') =>
  z.string({ error: `champ absent${precision}` }).regex(KEBAB, { error: `hors kebab-case${precision}` });

const texteNonVide = (precision = '') =>
  z.string({ error: `champ absent${precision}` }).regex(/\S/, { error: `champ vide${precision}` });

const jour = (precision = '') =>
  z.string({ error: `champ absent${precision}` }).regex(JOUR, { error: `hors AAAA-MM-JJ${precision}` });

const commun = {
  id: identifiant(),
  titre: texteNonVide(),
  duree_minutes: z.number({ error: "duree_minutes n'est pas un nombre" }).optional(),
};

const quand = z.enum(['aujourdhui', 'au_besoin', 'sans_date'], {
  error: 'quand attendu : aujourdhui, au_besoin ou sans_date',
});

const rubrique = z.enum(['crise', 'therapie', 'documentation'], {
  error:
    "rubrique attendue : crise, therapie ou documentation — la rubrique bilan est reservee au type bilan, rangee la une autre etape n'aurait pas de place a l'ecran",
});

const sortieLibre = z.literal(true, {
  error: 'sortie_libre doit valoir true — sortir avant la fin est toujours permis, sans avoir a le justifier',
});

const ecran = z.object({
  ...commun,
  type: z.literal('ecran'),
  rubrique,
  quand,
  ecran: z.enum(['check-in', 'mot-code', 'tension-appliquee', 'phrase-soignant'], {
    error: "ecran inconnu — Kokoro refuse un nom d'ecran qu'il ne connait pas plutot que d'afficher une ligne morte",
  }),
});

const exercice = z.object({
  ...commun,
  type: z.literal('exercice'),
  rubrique,
  quand,
  consigne: texteNonVide(),
  minuteur_secondes: z
    .int({ error: 'minuteur_secondes absent ou non entier' })
    .positive({ error: 'minuteur_secondes doit etre positif' }),
  sortie_libre: sortieLibre,
});

const choix = z.object(
  {
    valeur: z.int({ error: '« valeur » absente ou non entiere' }),
    libelle: texteNonVide(),
  },
  { error: "ce n'est pas un objet" },
);

const question = z.object(
  {
    id: identifiant(' — un id relie un item a sa reponse'),
    enonce: texteNonVide(" — le texte de l'item, recopie du corpus, jamais de memoire"),
    choix: z
      .array(choix, { error: 'choix absents' })
      .min(2, { error: 'moins de deux choix — une question est toujours un choix ferme, jamais une saisie de texte' }),
  },
  { error: "ce n'est pas un objet" },
);

const questionnaire = z
  .object({
    ...commun,
    type: z.literal('questionnaire'),
    rubrique,
    quand,
    questions: z
      .array(question, { error: 'questions absentes' })
      .min(1, { error: 'aucune question — un questionnaire tombe entier ou pas du tout' }),
  })
  .superRefine((etape, ctx) => {
    const identifiants = etape.questions.map((une) => une.id);

    identifiants
      .filter((id, rang) => identifiants.indexOf(id) !== rang)
      .forEach((id) =>
        ctx.addIssue({
          code: 'custom',
          path: ['questions'],
          message: `question en double : ${id} — un id relie un item a sa reponse`,
        }),
      );

    if (PHQ9.test(etape.id) || PHQ9.test(etape.titre)) {
      ctx.addIssue({
        code: 'custom',
        path: ['titre'],
        message:
          "le PHQ-9 ne se publie jamais — son item 9 interroge l'ideation suicidaire et Kokoro s'interdit tout numero d'urgence par construction ; il se passe en conversation, avec psy-bilan",
      });
    }
  });

const demarche = z.object({
  ...commun,
  type: z.literal('demarche'),
  rubrique,
  quand,
  detail: texteNonVide(),
});

const fiche = z
  .object({
    ...commun,
    type: z.literal('fiche'),
    rubrique,
    quand,
    texte: texteNonVide().optional(),
    document: identifiant().optional(),
    montrable: z.boolean({ error: "montrable n'est pas un booleen" }).optional(),
  })
  .superRefine((etape, ctx) => {
    if (etape.texte !== undefined && etape.document !== undefined) {
      ctx.addIssue({
        code: 'custom',
        path: ['document'],
        message: 'une fiche porte « texte » OU « document », jamais les deux',
      });
    }

    if (etape.texte === undefined && etape.document === undefined) {
      ctx.addIssue({
        code: 'custom',
        path: ['document'],
        message: "ni « texte » ni « document » — une fiche doit porter l'un des deux",
      });
    }
  });

const consigne = z.object(
  {
    pour: z.enum(['aide', 'patient'], { error: '« pour » attendu : aide ou patient' }),
    consigne: texteNonVide(),
    secondes: z
      .int({ error: '« secondes » absent ou non entier — une seance a deux est chronometree' })
      .positive({ error: '« secondes » doit etre positif — une seance a deux est chronometree' }),
  },
  { error: "ce n'est pas un objet" },
);

const seanceDuo = z
  .object({
    ...commun,
    type: z.literal('seance-duo'),
    rubrique,
    quand,
    entrainement_requis: z.literal(true, {
      error:
        "entrainement_requis doit valoir true — la premiere seance reelle ne peut pas etre la premiere fois que l'aidant decouvre le deroule",
    }),
    signal_arret: texteNonVide(
      " — Xavier doit pouvoir arreter SANS PARLER, c'est exactement ce qui tombe en premier ; le geste convenu est le « non » de la main, et il se recopie tel quel",
    ),
    avant: z.array(texteNonVide(), { error: 'champ absent — ce qui doit etre vrai avant de commencer' }),
    sequence: z.array(consigne, { error: 'sequence absente' }).min(1, { error: 'sequence vide' }),
    arret: z
      .array(texteNonVide(), { error: 'champ absent' })
      .min(2, { error: "au moins deux criteres d'arret, accessibles en un tap a tout moment" }),
    sortie_libre: sortieLibre,
  })
  .superRefine((etape, ctx) => {
    const dernier = etape.arret[etape.arret.length - 1];

    if (!dernier || !DERNIER_CRITERE.test(dernier)) {
      ctx.addIssue({
        code: 'custom',
        path: ['arret'],
        message:
          "le dernier critere d'arret est toujours « tu ne sais pas quoi faire, on s'arrete » — l'aidant n'improvise jamais",
      });
    }
  });

const bilan = z.object({
  ...commun,
  type: z.literal('bilan'),
  rubrique: z.literal('bilan', {
    error: 'un bilan porte toujours la rubrique bilan, et aucun autre type ne la porte',
  }),
  quand: z.undefined({
    error: "« quand » sur un bilan — sa date appartient au document, pas a l'assiduite de Xavier",
  }).optional(),
  date: jour(' — celle du bilan, jamais celle de la publication'),
  document: identifiant(' — un bilan est toujours un PDF de companion/inputs/bilans/'),
  texte: z.undefined({ error: 'un bilan ne porte jamais « texte »' }).optional(),
  montrable: z.undefined({
    error:
      "un bilan n'est jamais montrable — le partage est un acte de Xavier dans son lecteur, pas une fonction du dispositif",
  }).optional(),
});

export const EtapeSchema = z.discriminatedUnion(
  'type',
  [ecran, exercice, questionnaire, demarche, fiche, seanceDuo, bilan],
  { error: `type inconnu — attendu : ${TYPES.join(', ')}` },
);

export const ProgrammeSchema = z
  .object(
    {
      version: z.int({ error: 'version absente ou non entiere' }),
      publie_le: jour(),
      supervision: texteNonVide(
        ' — rien ne se publie sans une passe du superviseur (superviseur/README.md §4)',
      ),
      etapes: z.array(EtapeSchema, { error: 'etapes absentes' }),
    },
    { error: "la racine n'est pas un objet" },
  )
  .superRefine((programme, ctx) => {
    const identifiants = programme.etapes.map((etape) => etape.id);

    identifiants
      .filter((id, rang) => identifiants.indexOf(id) !== rang)
      .forEach((id) =>
        ctx.addIssue({
          code: 'custom',
          path: ['etapes'],
          message: `id en double : ${id} — un id relie une reponse a son etape, il doit etre unique`,
        }),
      );
  });

export type Etape = z.infer<typeof EtapeSchema>;
export type Programme = z.infer<typeof ProgrammeSchema>;
