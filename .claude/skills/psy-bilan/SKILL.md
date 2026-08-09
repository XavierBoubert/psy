---
name: psy-bilan
description: Passation et cotation d'une échelle validée avec Xavier — VVIQ, TAS-20, CAT-Q, GAD-7, PHQ-9, BES (grille comportementale), DIVA-5. Lit les items dans psy/corpus/echelles/, écrit une passation par fichier dans psy/dossier/mesures/AAAA-MM-JJ-<echelle>.json, réponses item par item. Utiliser quand Xavier dit « on passe le VVIQ », « une échelle », « le bilan », « le questionnaire », ou quand une séance prévoit une passation au plan de `psy/corpus/echelles/README.md` §3.
---

# psy-bilan — passation et cotation d'une échelle

**Une passation = un fichier. Les réponses item par item sont conservées, toujours.**
Un score seul n'est pas une mesure, c'est un résumé (`SCHEMA.md` §6).

---

## 0. Avant la première question

1. **Charger** `psy/dossier/profil.md` et `psy/dossier/etat.md` — ensemble, jamais l'un sans l'autre.
2. **Lire `etat.md` §6** — quelle échelle est prévue, à quelle date, avec quelles réserves.
3. **Lire la fiche de l'échelle** dans `psy/corpus/echelles/` — **intégralement**, items, cotation, seuils, rubrique « ce qu'elle ne dit pas ».
4. **Vérifier** que `psy/dossier/mesures/<date>-<echelle>.json` n'existe pas déjà. Si oui : le dire, ne pas écraser (R2 — append-only).

> ⛔ **Ne jamais faire passer une échelle de mémoire.** Si les items ne sont pas dans `corpus/echelles/`, la passation n'a pas lieu. C'est la règle qui a bloqué le BES le 09/08/2026, et elle est juste : un item mal restitué produit un score faux, donc **faussement rassurant** — le pire résultat possible dans ce dossier.

---

## 1. Trois règles de cadrage, énoncées avant de commencer

| Règle | Application |
|---|---|
| **Une échelle n'est jamais la cible d'une séance** | Elle s'ajoute à la cible du jour et se note dans `mesures_passees` du compte-rendu. |
| **Plafond : 20 minutes d'échelles par séance** | Au-delà, la fatigue de passation dégrade la réponse plus qu'elle n'informe. |
| **« Stop » s'obéit immédiatement** | Réponses obtenues écrites, le reste en `null`, mention dans `notes`. Une passation interrompue **n'est pas invalide, elle est partielle**. Aucune relance, aucune justification demandée. |

**Annonce d'ouverture — format invariable :**

> « On passe la [nom]. [N] items, environ [durée]. Chaque réponse est un chiffre sur une échelle que je te donne avant. Tu peux dire "stop" à tout moment — ça n'a aucune conséquence et ce qui est fait reste enregistré. »

---

## 2. Conduite de la passation

- **Un item à la fois.** Jamais de bloc de dix items à lire d'un coup.
- **Rappeler l'échelle de réponse** aussi souvent que nécessaire, sans le signaler comme une aide.
- **Ne jamais reformuler un item** au-delà de ce que la fiche autorise : la formulation porte la validité.
- **Ne jamais commenter une réponse en cours de passation** — ni « intéressant », ni « c'est cohérent avec », ni « tu es sûr ? ».
- **Pas de réponse = `null`, et on passe.** `null` ≠ `0`.

⚠️ **Ces échelles interrogent des ressentis, et ce n'est pas une violation de R6** — la validation psychométrique remplace l'ancre comportementale (`corpus/echelles/README.md` §1). **Mais aucun score d'échelle n'entre jamais dans le journal quotidien.** Les échelles vivent dans `mesures/`, et nulle part ailleurs.

---

## 3. 🔴 Sécurité — l'item 9 du PHQ-9

**L'item 9 du PHQ-9 interroge l'idéation suicidaire.** Conduite câblée, non contournable :

1. **Il se pose en dernier**, après les huit autres et après le GAD-7 si les deux sont au programme.
2. **Toute réponse ≥ 1 interrompt la passation immédiatement.** On ne finit pas, on ne demande pas de préciser, on ne cote pas.
3. **Déclencher [`psy/protocoles/crise-escalade.md`](../../../psy/protocoles/crise-escalade.md) §2** → skill `psy-crise` : **3114** (gratuit, 24 h/24), contact d'urgence, Dr Isorni, **15** si urgence vitale.
4. **Le fichier `mesures/` s'écrit après**, jamais avant.

> ⭐ **Si la parole est coupée à ce moment-là**, le 3114 et le 15 sont inutilisables : ce sont des numéros de téléphone. Voies sans parole — **114** par SMS, mot-code « shutdown », canal écrit (`crise-escalade.md` §4). ✈️ Ces numéros ne fonctionnent pas depuis la Tunisie du 07/09 au ≈ 28/09.

---

## 4. Cotation — la fiche fait foi, jamais la mémoire

**Recopier la cotation depuis la fiche à chaque passation.** Les items inversés et les sous-scores sont l'endroit où les erreurs se produisent.

| Échelle | `echelle` | `version` | Étendue | Seuil | Pièges |
|---|---|---|---|---|---|
| VVIQ | `vviq` | `VVIQ-16-Zeman` | 16-80 | **< 32** → aphantasie | ⚠️ **Le sens de cotation est inversé selon les versions.** Ici, **1 = aucune image**. Marks (1973) cotait à l'envers ; le VVIQ-2 est un autre instrument (32 items, /160). Un même score lu avec la mauvaise convention **inverse la conclusion**. |
| TAS-20 | `tas20` | `TAS-20-FR-Loas1996` | 20-100 | **> 61** → alexithymie | Items inversés **4, 5, 10, 18, 19**. **Sous-scores DIF / DDF / EOT obligatoires** — le facteur EOT est contesté chez les personnes autistes, un total tiré par le seul EOT se lit avec prudence. |
| CAT-Q | `catq` | `CAT-Q-FR-traduction-locale` | 25-175 | **> 100** | Items inversés **3, 12, 19, 22, 24**. Sous-scores Compensation / Masking / Assimilation. Chez les hommes, le seuil discrimine mal (109,6 vs 96,9) — **prudence sur un score proche de 100**. |
| GAD-7 | `gad7` | `GAD-7-FR` | 0-21 | **≥ 10** → dépistage TAG positif | Items 4-6 recoupent le TAG, la privation de sommeil **et** la titration d'IRSN. Trois causes, un chiffre. |
| PHQ-9 | `phq9` | `PHQ-9-FR` | 0-27 | cf. fiche | 🔴 **item 9 → §3.** Et voir la réserve SAOS ci-dessous. |
| BES | `bes` | ⚠️ `grille-comportementale-locale` | — | — | **Items formels non disponibles.** On passe la **grille comportementale de 5 questions** (`bes.md` §4) : `score: null`, comptages dans `sous_scores`, `notes` porte que le BES formel n'a pas été passé. |
| DIVA-5 | `diva5` | — | — | — | 🔴 **Bloqué.** Ne pas passer avant traitement effectif du SAOS — un psychostimulant prescrit sur un SAOS non traité masque le trouble au lieu de le corriger. |

> 🔴 **Réserve obligatoire sur le PHQ-9, à écrire dans `notes` et à reporter au brief.** Quatre de ses items — sommeil (3), fatigue (4), concentration (7), ralentissement (8) — sont **directement produits par le SAOS sévère insuffisamment traité**. Ils peuvent porter le score en zone « modérée » **sans dépression**. Aujourd'hui, **un PHQ-9 élevé chez Xavier n'est pas une mesure de l'humeur** : c'est un mélange humeur + dette de sommeil respiratoire. **La réserve part au brief avec le chiffre, jamais le chiffre seul.**

---

## 5. Écriture du fichier

Copier `psy/dossier/gabarits/mesure.json` → `psy/dossier/mesures/AAAA-MM-JJ-<echelle>.json`, `"passation": "claude-code"`.

- **`reponses` contient autant d'entrées que l'instrument a d'items** — 16 pour le VVIQ, 20 pour la TAS-20, 25 pour le CAT-Q. Un item sans réponse est `null`, pas absent.
- **`version` porte l'identification exacte, sens de cotation compris.**
- **`sous_scores`** est obligatoire pour la TAS-20 et le CAT-Q.
- **GAD-7 et PHQ-9 = deux fichiers**, jamais un seul.

---

## 6. Restitution à Xavier — ce qui se dit, et comment

**Toujours dans cet ordre :** le chiffre brut · le seuil publié · **ce que l'échelle ne dit pas** · ce que ça change concrètement au dispositif.

> ⭐ **La règle qui prime sur toute interprétation : chez Xavier, un score élevé est informatif, un score bas ne clôt aucune question.** L'alexithymie et le déficit intéroceptif sont précisément une **difficulté à répondre à ce type de question**. Un score qui minimise peut refléter le déficit de perception, pas l'absence du phénomène.
>
> **En cas de discordance entre un score et le journal quotidien, c'est le journal qui prime** (`corpus/echelles/README.md` §1).

**Ce qu'on ne fait jamais à la restitution :** annoncer un diagnostic · comparer à une passation antérieure comme à une performance · dire « c'est bon » ou « c'est mauvais » · enchaîner sur une deuxième échelle non prévue.

---

## 7. Interdits absolus

- **Restituer des items de mémoire** ou approximer un instrument non versé.
- **Poser l'item 9 du PHQ-9 ailleurs qu'en dernier**, ou continuer après une réponse ≥ 1.
- **Passer le DIVA-5** avant traitement effectif du SAOS.
- **Faire entrer un score dans le journal quotidien** (`journal/*.json` reste strictement comportemental).
- **Conseiller une modification de traitement** à partir d'un score — jamais, même sous forme interrogative. Ça part au brief (`etat.md` §5).
- **Écraser un fichier `mesures/` existant.** Une correction est un ajout daté (R2).
- Transmettre un chiffre à un praticien **sans sa réserve** — traduction non validée pour le CAT-Q, normes anglophones pour le VVIQ, recouvrement SAOS pour le PHQ-9.
