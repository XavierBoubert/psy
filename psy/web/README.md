# `web/` — outils de séance desktop

**TypeScript strict** (règles projet : `ay-typescript`, `ay-functional`). ⏸️ Construction à partir de l'Étape 5.

> 📐 **Séquençage : [`../android/PLAN-KOKORO.md`](../android/PLAN-KOKORO.md) §7.** ⭐ **Le premier livrable web est la formalisation Zod du schéma du dossier** — le contrat de données partagé entre les surfaces, aujourd'hui validé par rien —, construit avec le jalon K3. La passation d'échelles passe **après le retour de Tunisie** : les échelles urgentes se passent les 16 et 22-23/08, conduites par `psy-bilan` en conversation.

## Pourquoi le desktop n'est pas un confort mais une nécessité clinique

La stimulation bilatérale visuelle exige une **amplitude de mouvement oculaire** suffisante : dérisoire sur un écran de téléphone, correcte sur un écran desktop. De même, les échelles longues (CAT-Q, TAS-20, DIVA-5) et les tableaux de bord d'évolution sont illisibles sur mobile. Le bon choix ergonomique est ici le bon choix thérapeutique.

## Périmètre

| Outil | Objet | Étape |
|---|---|---|
| Passation d'échelles | VVIQ, TAS-20, CAT-Q, BES, GAD-7, PHQ-9 — cotation automatique, **réponses item par item conservées** | 2 |
| Suivi de paliers d'exposition | PPC, transports, alimentation — paliers écrits, critère de passage, anxiété en temps réel | 3-4 |
| Tableaux de bord d'évolution | Tendances issues de `dossier/journal/` — **sans aucun compteur de régularité** | 2+ |
| Stimulation bilatérale | Point mobile, bips alternés — **instrument seul, aucun protocole de retraitement** (PLAN §3.1) | 6 |

## Contraintes d'interface — non négociables

Hypersensibilités sur 4 canaux, intolérance au changement, camouflage :

- **Aucun son surprise, aucun flash, aucune animation brusque.** Transitions lentes et continues.
- **Palette douce, contraste maîtrisé.**
- **L'interface ne change jamais sans annonce** — pas de refonte surprise, pas d'A/B test, pas de « nouveauté ».
- **Zéro streak, zéro compteur de régularité, zéro pourcentage d'objectif, zéro rappel de manquement.**
- **Utilisable sans parler ni écrire** : tout champ obligatoire est un nombre ou un choix fermé.
- **Aucune consigne de visualisation**, y compris dans les textes d'aide.

## Données

Lecture/écriture de `psy/dossier/` selon `psy/dossier/SCHEMA.md`, **strictement**. Formalisation Zod des schémas du dossier à faire au démarrage de cette surface — c'est ici qu'elle aura un consommateur.
