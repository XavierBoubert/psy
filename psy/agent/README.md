# `agent/` — les rôles Claude Code

> ⚠️ **Les skills ne sont pas dans ce répertoire.** Claude Code ne découvre les skills d'un projet que dans **`.claude/skills/<nom>/SKILL.md`**. Les placer dans `psy/agent/` les rendrait invisibles et donc inutilisables.
>
> Le `PLAN.md` §1.2 prévoyait `psy/agent/` ; la réalisation retenue est `.claude/skills/psy-*`. Ce répertoire ne conserve que cette note d'aiguillage.

## Rôles

| Skill | Rôle | Emplacement | État |
|---|---|---|---|
| `psy-seance` | Séance de fond hebdomadaire — ouverture / travail / clôture / compte-rendu | `.claude/skills/psy-seance/` | ✅ v1 |
| `psy-journal` | Check-in quotidien à faible coût cognitif — 7 questions fermées, < 2 min | `.claude/skills/psy-journal/` | ✅ v1 |
| `psy-crise` | **Triage crise** : panique ? vasovagal ? shutdown ? → oriente vers la bonne parade | — | ⏸️ Étape 3 |
| `psy-bilan` | Passation et cotation des échelles (VVIQ, TAS-20, CAT-Q, BES, GAD-7, PHQ-9, DIVA-5) | — | ⏸️ Étape 2 |
| `psy-brief-isorni` | Brief d'une page avant chaque consultation mensuelle | — | ⏸️ Étape 2 |
| `psy-hygiene` | Programme et suivi du versant somatique (alimentation, activité, sommeil) | — | ⏸️ Étape 1 |
| `psy-superviseur` | Contre-expertise — challenge les conclusions, détecte l'effet miroir | — | ⏸️ |

## Invariants communs à tout skill de ce projet

Tout nouveau skill charge `psy/dossier/profil.md` **et** `psy/dossier/etat.md` avant d'agir, et respecte sans exception :

- **non-substitution** — aucun conseil de modification de traitement, jamais, même sous forme interrogative ;
- **protocole de crise câblé** — 3114, non contournable ;
- **aucune visualisation** — aphantasie ;
- **utilisable sans parler ni écrire** — shutdowns ;
- **zéro streak, zéro compteur de régularité, zéro reproche d'assiduité** ;
- **annoncer avant de faire** — aucun changement de format sans préavis.
