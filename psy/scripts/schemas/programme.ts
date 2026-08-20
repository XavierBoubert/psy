import { z } from 'zod';

export const TYPES = ['panneau', 'pdf'] as const;

export const ETAPES = ['info', 'question', 'note', 'minuteur', 'checklist', 'confirmation'] as const;

// Une carte qui porte au moins une de ces etapes rend une reponse : c'est elle qui fait agir.
export const ETAPES_QUI_RENDENT = ['question', 'note', 'minuteur', 'confirmation'] as const;

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
    "rubrique attendue : crise, therapie ou documentation — la rubrique bilan est reservee aux cartes du dossier Bilan, rangee la une autre carte n'aurait pas de place a l'ecran",
});

const porteur = z.enum(['patient', 'aidant'], { error: 'porteur attendu : patient ou aidant' });

const info = z.object(
  {
    type: z.literal('info'),
    texte: texteNonVide(),
    montrable: z.boolean({ error: "montrable n'est pas un booleen" }).optional(),
  },
  { error: "ce n'est pas un objet" },
);

const choix = z.object(
  {
    valeur: z.number({ error: '« valeur » absente ou non numerique' }),
    libelle: texteNonVide(),
  },
  { error: "ce n'est pas un objet" },
);

const compteur = z
  .object(
    {
      depart: z.number({ error: '« depart » absent ou non numerique' }),
      pas: z.number({ error: '« pas » absent ou non numerique' }).positive({ error: '« pas » doit etre positif' }),
      grand_pas: z.number({ error: '« grand_pas » absent ou non numerique' }),
      minimum: z.number({ error: '« minimum » absent ou non numerique' }),
      unite: z.enum(['brute', 'minutes', 'heures', 'kilos'], {
        error: 'unite attendue : brute, minutes, heures ou kilos',
      }),
    },
    { error: "ce n'est pas un objet" },
  )
  .superRefine((regle, ctx) => {
    if (regle.grand_pas < regle.pas) {
      ctx.addIssue({ code: 'custom', path: ['grand_pas'], message: 'le grand pas est plus petit que le pas' });
    }

    if (regle.depart < regle.minimum) {
      ctx.addIssue({ code: 'custom', path: ['depart'], message: 'le depart est sous le minimum' });
    }
  });

// 🔴 Une reponse est toujours fermee — un choix ou un compteur, jamais une saisie de texte.
const question = z
  .object(
    {
      type: z.literal('question'),
      id: identifiant(' — un id relie un item a sa reponse'),
      enonce: texteNonVide(" — le texte de l'item, recopie du corpus, jamais de memoire"),
      precision: texteNonVide().optional(),
      choix: z.array(choix).min(2, { error: 'moins de deux choix — une question est toujours un choix ferme' }).optional(),
      compteur: compteur.optional(),
      reprise: z.boolean({ error: "reprise n'est pas un booleen" }).optional(),
    },
    { error: "ce n'est pas un objet" },
  )
  .superRefine((etape, ctx) => {
    const saisies = [etape.choix, etape.compteur].filter((saisie) => saisie !== undefined).length;

    if (saisies !== 1) {
      ctx.addIssue({
        code: 'custom',
        path: ['choix'],
        message: 'une question porte « choix » OU « compteur », jamais les deux ni aucun des deux',
      });
    }

    if (etape.reprise && !etape.compteur) {
      ctx.addIssue({
        code: 'custom',
        path: ['reprise'],
        message: 'seule une question au compteur se reprend — un choix ferme repart toujours de sa premiere option',
      });
    }
  });

// ⭐ Le seul endroit du dispositif ou une saisie libre est proposee, et elle peut rester vide (R5).
const note = z.object(
  {
    type: z.literal('note'),
    id: identifiant(' — un id relie une note a sa reponse'),
    enonce: texteNonVide(),
    precision: texteNonVide().optional(),
  },
  { error: "ce n'est pas un objet" },
);

const minuteur = z.object(
  {
    type: z.literal('minuteur'),
    secondes: z
      .int({ error: '« secondes » absent ou non entier' })
      .positive({ error: '« secondes » doit etre positif' }),
    consigne: texteNonVide().optional(),
    pour: porteur.optional(),
  },
  { error: "ce n'est pas un objet" },
);

const checklist = z.object(
  {
    type: z.literal('checklist'),
    enonce: texteNonVide(),
    lignes: z.array(texteNonVide(), { error: 'lignes absentes' }).min(1, { error: 'aucune ligne a cocher' }),
  },
  { error: "ce n'est pas un objet" },
);

const confirmation = z.object(
  {
    type: z.literal('confirmation'),
    libelle: texteNonVide(),
  },
  { error: "ce n'est pas un objet" },
);

const etape = z.discriminatedUnion('type', [info, question, note, minuteur, checklist, confirmation], {
  error: `type d'etape inconnu — attendu : ${ETAPES.join(', ')}`,
});

const sortieLibre = z.literal(true, {
  error: 'sortie_libre doit valoir true — sortir avant la fin est toujours permis, sans avoir a le justifier',
});

const relireIdentifiants = (
  etapes: ReadonlyArray<z.infer<typeof etape>>,
  ctx: z.RefinementCtx,
): void => {
  const identifiants = etapes.flatMap((une) => (une.type === 'question' || une.type === 'note' ? [une.id] : []));

  identifiants
    .filter((id, rang) => identifiants.indexOf(id) !== rang)
    .forEach((id) =>
      ctx.addIssue({
        code: 'custom',
        path: ['etapes'],
        message: `etape en double : ${id} — un id relie un item a sa reponse`,
      }),
    );
};

// L'aidant ne peut ni corriger ni improviser : ce qui manque ne s'invente pas, la carte entiere tombe.
const relireCarteTenueParLAidant = (
  carte: { readonly signal_arret?: string; readonly arret?: ReadonlyArray<string>; readonly etapes: ReadonlyArray<z.infer<typeof etape>> },
  ctx: z.RefinementCtx,
): void => {
  const arret = carte.arret || [];
  const dernier = arret[arret.length - 1];
  const minuteurs = carte.etapes.filter((une) => une.type === 'minuteur');

  if (!carte.signal_arret) {
    ctx.addIssue({
      code: 'custom',
      path: ['signal_arret'],
      message:
        "signal_arret absent — Xavier doit pouvoir arreter SANS PARLER, c'est exactement ce qui tombe en premier ; le geste convenu est le « non » de la main, et il se recopie tel quel",
    });
  }

  if (arret.length < 2) {
    ctx.addIssue({
      code: 'custom',
      path: ['arret'],
      message: "au moins deux criteres d'arret, portes par la case a cocher qui ouvre le deroule",
    });
  }

  if (!dernier || !DERNIER_CRITERE.test(dernier)) {
    ctx.addIssue({
      code: 'custom',
      path: ['arret'],
      message:
        "le dernier critere d'arret est toujours « tu ne sais pas quoi faire, on s'arrete » — l'aidant n'improvise jamais",
    });
  }

  if (carte.etapes[0]?.type !== 'checklist') {
    ctx.addIssue({
      code: 'custom',
      path: ['etapes'],
      message:
        "la premiere etape d'une carte tenue par l'aidant est la checklist d'avant — cocher est la seule preuve que les criteres d'arret ont ete lus",
    });
  }

  if (!minuteurs.length) {
    ctx.addIssue({ code: 'custom', path: ['etapes'], message: 'aucune consigne chronometree — le deroule est vide' });
  }

  minuteurs
    .filter((une) => une.pour === undefined || une.consigne === undefined)
    .forEach(() =>
      ctx.addIssue({
        code: 'custom',
        path: ['etapes'],
        message:
          "une consigne tenue par l'aidant porte toujours « pour » et « consigne » — la consigne dit elle-meme a qui elle s'adresse",
      }),
    );
};

const relireCarteTenueParXavier = (
  carte: { readonly signal_arret?: string; readonly arret?: ReadonlyArray<string>; readonly etapes: ReadonlyArray<z.infer<typeof etape>> },
  ctx: z.RefinementCtx,
): void => {
  if (carte.signal_arret !== undefined || carte.arret !== undefined) {
    ctx.addIssue({
      code: 'custom',
      path: ['signal_arret'],
      message: "signal_arret et arret n'ont de sens que sur une carte tenue par l'aidant (porteur: aidant)",
    });
  }

  carte.etapes
    .filter((une) => une.type === 'minuteur' && une.pour !== undefined)
    .forEach(() =>
      ctx.addIssue({
        code: 'custom',
        path: ['etapes'],
        message: "« pour » n'a de sens que sur une carte tenue par l'aidant — Xavier tient la sienne lui-meme",
      }),
    );
};

const panneau = z
  .object({
    ...commun,
    type: z.literal('panneau'),
    rubrique,
    quand,
    porteur: porteur.optional(),
    signal_arret: texteNonVide().optional(),
    arret: z.array(texteNonVide()).optional(),
    sortie_libre: sortieLibre,
    etapes: z
      .array(etape, { error: 'etapes absentes' })
      .min(1, { error: 'aucune etape — une carte tombe entiere ou pas du tout' }),
  })
  .superRefine((carte, ctx) => {
    relireIdentifiants(carte.etapes, ctx);

    if (carte.porteur === 'aidant') {
      relireCarteTenueParLAidant(carte, ctx);
    } else {
      relireCarteTenueParXavier(carte, ctx);
    }

    if (PHQ9.test(carte.id) || PHQ9.test(carte.titre)) {
      ctx.addIssue({
        code: 'custom',
        path: ['titre'],
        message:
          "le PHQ-9 ne se publie jamais — son item 9 interroge l'ideation suicidaire et Kokoro s'interdit tout numero d'urgence par construction ; il se passe en conversation, avec psy-bilan",
      });
    }
  });

// 🔴 Un bilan ne passe pas par la bibliotheque : canal distinct au depot, dossier distinct dans le transit.
// C'est la rubrique bilan qui les separe, et elle va toujours avec une date et sans « quand ».
const pdf = z
  .object({
    ...commun,
    type: z.literal('pdf'),
    rubrique: z.enum(['crise', 'therapie', 'documentation', 'bilan'], {
      error: 'rubrique attendue : crise, therapie, documentation ou bilan',
    }),
    quand: quand.optional(),
    date: jour(' — celle du bilan, jamais celle de la publication').optional(),
    document: identifiant(' — le nom nu du fichier, sans chemin ni extension'),
  })
  .superRefine((carte, ctx) => {
    const bilan = carte.rubrique === 'bilan';

    if (bilan !== (carte.date !== undefined)) {
      ctx.addIssue({
        code: 'custom',
        path: ['date'],
        message: bilan
          ? "un bilan porte toujours sa date — l'ecran Bilan groupe par mois decroissant"
          : "« date » est reservee aux bilans — elle rangerait cette carte sur un ecran qui ne l'affiche pas",
      });
    }

    if (bilan !== (carte.quand === undefined)) {
      ctx.addIssue({
        code: 'custom',
        path: ['quand'],
        message: bilan
          ? "« quand » sur un bilan — sa date appartient au document, pas a l'assiduite de Xavier"
          : '« quand » absent — aujourdhui, au_besoin ou sans_date',
      });
    }
  });

export const CarteSchema = z.discriminatedUnion('type', [panneau, pdf], {
  error: `type inconnu — attendu : ${TYPES.join(', ')}`,
});

export const ProgrammeSchema = z
  .object(
    {
      version: z.int({ error: 'version absente ou non entiere' }),
      publie_le: jour(),
      supervision: texteNonVide(
        ' — rien ne se publie sans une passe du superviseur (superviseur/README.md §4)',
      ),
      cartes: z.array(CarteSchema, { error: 'cartes absentes' }),
    },
    { error: "la racine n'est pas un objet" },
  )
  .superRefine((programme, ctx) => {
    const identifiants = programme.cartes.map((carte) => carte.id);

    identifiants
      .filter((id, rang) => identifiants.indexOf(id) !== rang)
      .forEach((id) =>
        ctx.addIssue({
          code: 'custom',
          path: ['cartes'],
          message: `id en double : ${id} — un id relie une reponse a sa carte, il doit etre unique`,
        }),
      );
  });

export type Carte = z.infer<typeof CarteSchema>;
export type Programme = z.infer<typeof ProgrammeSchema>;
