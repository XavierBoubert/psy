# GAD-7 et PHQ-9 — anxiété et dépression, routine mensuelle

**Sources :**
- **GAD-7** — Spitzer, R. L., Kroenke, K., Williams, J. B. W., Löwe, B. (2006). *A brief measure for assessing generalized anxiety disorder.* Archives of Internal Medicine, 166(10), 1092-1097.
- **PHQ-9** — Kroenke, K., Spitzer, R. L., Williams, J. B. W. (2001). *The PHQ-9: validity of a brief depression severity measure.* Journal of General Internal Medicine, 16(9), 606-613.

**Durée :** 5 minutes pour les deux. **Identifiants `mesures/` :** `gad7`, `phq9`.

---

## 1. Pourquoi ces deux-là, et pourquoi maintenant

Ce sont les **seules échelles du corpus que le Dr Isorni lira sans traduction**. Le GAD-7 et le PHQ-9 sont le langage courant du suivi psychiatrique français ; un brief qui les porte est un brief exploitable en consultation.

⏱️ **Le calendrier commande la date de passation.** La venlafaxine a été reprise le **07/08/2026**. Une passation au **22-23/08** tombe à **deux semaines** de la reprise : elle date le point de départ de la titration au moment où l'effet commence à peine à être interprétable, et fournit au 03/09 un **chiffre de départ** au lieu d'un souvenir. Sans elle, la consultation se fera sur impression.

**Répétition prévue :** mensuelle. Ce sont les deux seules échelles du corpus destinées à être repassées en routine ; les autres sont des passations uniques.

---

## 2. Passation

**Consigne littérale, commune aux deux :**

> « Au cours des **deux dernières semaines**, à quelle fréquence as-tu été gêné par les problèmes suivants ? 0 = jamais · 1 = plusieurs jours · 2 = plus de la moitié des jours · 3 = presque tous les jours. »

⚠️ **La fenêtre est de 14 jours et pas « en général ».** Si Xavier répond en généralisant, reposer la question en rappelant la fenêtre — c'est la seule relance autorisée, et elle porte sur la période, jamais sur la réponse.

---

## 3. 🔴 Sécurité — l'item 9 du PHQ-9

**PHQ-9, item 9 : « Penser qu'il vaudrait mieux mourir, ou envisager de te faire du mal d'une manière ou d'une autre. »**

Le rapport documente une **idéation suicidaire à l'adolescence** et pose une vigilance suicidaire au long cours (§6.4). Cet item n'est donc pas une formalité.

**Conduite câblée, non contournable :**

1. **Toute réponse ≥ 1 interrompt la passation immédiatement.** On ne finit pas le questionnaire. On ne calcule pas le score.
2. On applique [`protocoles/crise-escalade.md`](../../protocoles/crise-escalade.md) §2 : **3114** (gratuit, 24 h/24, 7 j/7), contact d'urgence, Dr Isorni. Si l'urgence est vitale : recours médical immédiat, par le moyen le plus rapide sur place. ⭐ **Si la parole est coupée** — voies sans parole au §4 de la fiche : mot-code « shutdown », canal écrit. *(Les numéros d'appel d'urgence ont été retirés du dispositif le 10/08/2026 ; le 3114 est le seul conservé, et c'est ici qu'il sert.)*
3. Aucune tentative de gérer seul, de relativiser, de rationaliser ou de « replacer dans le contexte de la titration ».
4. La trace au dossier vient **après** : un fichier `crises/` puis, seulement ensuite, le fichier `mesures/` partiel.

**Cet item se pose en dernier, toujours**, pour qu'une interruption ne perde pas les autres réponses.

---

## 4. GAD-7 — items et cotation

| # | Item |
|---|---|
| 1 | Te sentir nerveux, anxieux ou tendu |
| 2 | Ne pas être capable d'arrêter de t'inquiéter ou de contrôler tes inquiétudes |
| 3 | T'inquiéter trop à propos de différentes choses |
| 4 | Avoir de la difficulté à te détendre |
| 5 | Être si agité qu'il est difficile de rester tranquille |
| 6 | Devenir facilement contrarié ou irritable |
| 7 | Avoir peur que quelque chose d'épouvantable puisse arriver |

**Score = somme des 7 items. Étendue : 0 à 21.**

| Score | Sévérité |
|---|---|
| 0-4 | Minimale |
| **5-9** | Légère |
| **10-14** | Modérée — **seuil de dépistage du TAG** |
| **15-21** | Sévère |

---

## 5. PHQ-9 — items et cotation

| # | Item |
|---|---|
| 1 | Peu d'intérêt ou de plaisir à faire les choses |
| 2 | Te sentir triste, déprimé ou désespéré |
| 3 | Difficultés à t'endormir ou à rester endormi, ou dormir trop |
| 4 | Te sentir fatigué ou avoir peu d'énergie |
| 5 | Peu d'appétit ou manger trop |
| 6 | Avoir une mauvaise perception de toi-même, ou le sentiment d'être nul, ou d'avoir déçu ta famille |
| 7 | Difficultés à te concentrer, par exemple pour lire ou regarder la télévision |
| 8 | Bouger ou parler si lentement que les autres auraient pu le remarquer — ou au contraire être si agité que tu as eu du mal à tenir en place |
| 9 | 🔴 Penser qu'il vaudrait mieux mourir, ou envisager de te faire du mal d'une manière ou d'une autre — **cf. §3, se pose en dernier** |

**Score = somme des 9 items. Étendue : 0 à 27.**

| Score | Sévérité |
|---|---|
| 0-4 | Minimale |
| **5-9** | Légère |
| **10-14** | Modérée |
| **15-19** | Modérément sévère |
| **20-27** | Sévère |

---

## 6. ⚠️ Ce que ces échelles ne disent pas

- 🔴 **Quatre items du PHQ-9 sont directement produits par un SAOS sévère insuffisamment traité** : item 3 (sommeil), item 4 (fatigue), item 7 (concentration), item 8 (ralentissement). Ils peuvent à eux seuls porter le score dans la zone « modérée » **sans dépression**. ⭐ **Un score PHQ-9 élevé chez Xavier, aujourd'hui, n'est pas interprétable comme une mesure de l'humeur** — c'est un mélange humeur + dette de sommeil respiratoire. **Le brief au Dr Isorni doit porter cette réserve explicitement**, sans quoi le chiffre induira en erreur le seul praticien qui ignore encore le diagnostic de SAOS.
- **Le même recouvrement joue sur le GAD-7** : les items 4, 5 et 6 (tension, agitation, irritabilité) recoupent le critère C du TAG *et* les effets d'une privation de sommeil chronique *et* les effets possibles d'une titration d'IRSN en cours. Trois causes, un chiffre.
- **Ce sont des échelles introspectives sans ancre comportementale** — la réserve du corpus s'applique (`README.md` §1.2) : chez quelqu'un d'alexithymique, **un score bas ne prouve pas l'absence du trouble.** Les ancres comportementales correspondantes sont dans le journal quotidien : `renoncements` pour l'anxiété, `activites_investies` pour l'humeur. **En cas de discordance entre le score et le journal, c'est le journal qui prime.**
- **Le PHQ-9 ne distingue pas dépression et burnout autistique** — dont le rapport fait le risque nommé (§10.5). Le marqueur du second est la **fréquence des shutdowns**, qui ne figure dans aucune de ces deux échelles.
- **Aucun de ces scores n'autorise à toucher au traitement.** Non-substitution : ils partent au brief, point.

---

## 7. Ce qu'on écrit dans `mesures/`

**Deux fichiers distincts**, un par échelle (R1 du schéma : un fichier par événement).

```json
{
  "date": "2026-08-22",
  "echelle": "gad7",
  "version": "GAD-7-FR",
  "score": null,
  "score_max": 21,
  "seuil": { "valeur": 10, "sens": "au_dessus", "interpretation": "depistage_tag_positif" },
  "sous_scores": null,
  "reponses": [],
  "passation": "claude-code",
  "notes": null
}
```

```json
{
  "date": "2026-08-22",
  "echelle": "phq9",
  "version": "PHQ-9-FR",
  "score": null,
  "score_max": 27,
  "seuil": { "valeur": 10, "sens": "au_dessus", "interpretation": "depression_moderee" },
  "sous_scores": { "item9_ideation": null },
  "reponses": [],
  "passation": "claude-code",
  "notes": "Réserve obligatoire au brief : items 3, 4, 7, 8 recouverts par le SAOS sévère insuffisamment traité."
}
```
