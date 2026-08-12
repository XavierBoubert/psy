# `agent/` — les rôles Claude Code

> ⚠️ **Les skills ne sont pas dans ce répertoire.** Claude Code ne découvre les skills d'un projet que dans **`.claude/skills/<nom>/SKILL.md`**. Les placer dans `psy/agent/` les rendrait invisibles et donc inutilisables.
>
> Le `PLAN.md` §1.2 prévoyait `psy/agent/` ; la réalisation retenue est `.claude/skills/psy-*`. Ce répertoire ne conserve que cette note d'aiguillage.

## Rôles

| Skill | Rôle | Emplacement | État |
|---|---|---|---|
| `psy-seance` | Séance de fond hebdomadaire — ouverture / travail / clôture / compte-rendu | `.claude/skills/psy-seance/` | ✅ v1 |
| `psy-journal` | Check-in quotidien à faible coût cognitif — 7 questions fermées, < 2 min | `.claude/skills/psy-journal/` | ✅ v1 |
| `psy-crise` | **Triage crise** : sécurité d'abord, puis panique ? vasovagal ? shutdown ? → oriente vers la bonne parade | `.claude/skills/psy-crise/` | ✅ v1 |
| `psy-bilan` | Passation et cotation des échelles (VVIQ, TAS-20, CAT-Q, BES, GAD-7, PHQ-9, DIVA-5) | `.claude/skills/psy-bilan/` | ✅ v1 |
| `psy-brief-isorni` | Brief d'une page avant chaque consultation | `.claude/skills/psy-brief-isorni/` | ✅ v1 |
| `psy-hygiene` | Versant somatique (PPC, alimentation, activité) — **vérifie les paliers en comptant le journal** | `.claude/skills/psy-hygiene/` | ✅ v1 |
| `psy-superviseur` | **Contre-expertise du dispositif** — 8 contrôles (source circulaire, fait périmé, invariant non câblé, dérive R6, effet miroir, autorité fabriquée, prolifération, ⭐ **programme désynchronisé**) → `psy/agent/supervisions/` | `.claude/skills/psy-superviseur/` | ✅ v2 |

> ⚠️ **`psy-superviseur` supervise Claude, pas Xavier.** Le risque qu'il traite est structurel : **presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** Les seules sources primaires sont l'évaluation Saley, le certificat Isorni, les questionnaires bruts, les courriers Roisman, la biopsie et le DSM-5 ; tout le reste est dérivé. Il **n'écrit pas dans `psy/dossier/`** — une supervision porte sur le dispositif, pas sur le patient.
>
> **Première passe : [`supervisions/2026-08-09-supervision.md`](supervisions/2026-08-09-supervision.md)** — 4 constats, dont un bloquant trouvé dans `psy-seance` lui-même.

> 🔴 **`psy-crise` porte la seule exception au premier invariant.** Tous les skills chargent `profil.md` + `etat.md` avant d'agir ; en crise, **les numéros s'affichent avant la lecture du dossier**. Lire deux fiches prend du temps, et le temps est ce qui manque. L'exception est écrite dans le skill, elle ne se déduit pas.
>
> **Ce que chaque rôle a apporté au-delà de sa fiche :** `psy-bilan` interdit de restituer un instrument de mémoire (c'est ce qui a bloqué le BES) · `psy-hygiene` **compte** le critère de passage dans le journal au lieu de demander « tu te sens prêt ? » · `psy-brief-isorni` interdit de compter un jour sans check-in comme un zéro et impose la réserve SAOS avec tout score PHQ-9.

## ⭐ Les trois rôles, et qui parle à qui *(12/08/2026)*

| Rôle | Ce qu'il est | Ce qu'il ne fait jamais |
|---|---|---|
| **Claude Psy** — les 6 skills cliniques | **Le psy de Xavier.** Il monte la thérapie, écrit les protocoles, décide des étapes, et les publie dans Kokoro à la clôture de séance. | Prescrire · publier hors séance · notifier |
| **Superviseur** — `psy-superviseur` | **Le superviseur du psy.** Il supervise **Claude, pas Xavier**. | Écrire dans `dossier/` · modifier ou publier le programme · noter Xavier |
| **Kokoro** — `psy/android/` | **Le compagnon aidant.** Il protège (crise, mot-code, tension appliquée) et il **guide** : il affiche la thérapie et renvoie ce qui a été fait. | Décider · interpréter · **venir vers Xavier** |

```
Claude Psy ──programme.json──► Kokoro ──reponses/──► dossier ──► Claude Psy
     ▲                                                   │
     └───────────── Superviseur ◄────────────────────────┘
                (contrôle : avant chaque brief · 1×/mois · sur demande)
```

**Deux moments où une erreur sort du dispositif et atteint quelqu'un** — ce sont les deux points de contrôle : **le brief** (vers le Dr Isorni) et **le programme publié** (vers Xavier, sur son téléphone, **sans intermédiaire pour objecter**). Le second est contrôlé deux fois : mécaniquement par `npm run publish` à chaque publication, humainement par le superviseur à sa cadence.

## Invariants communs à tout skill de ce projet

Tout nouveau skill charge `psy/dossier/profil.md` **et** `psy/dossier/etat.md` avant d'agir, et respecte sans exception :

- **non-substitution** — aucun conseil de modification de traitement, jamais, même sous forme interrogative ;
- **protocole de crise câblé** — 3114, non contournable ;
- **aucune visualisation** — aphantasie ;
- **utilisable sans parler ni écrire** — shutdowns ;
- **zéro streak, zéro compteur de régularité, zéro reproche d'assiduité** ;
- **annoncer avant de faire** — aucun changement de format sans préavis.
