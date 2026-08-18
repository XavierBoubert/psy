---
date: 2026-08-18
porte_sur: programme
version: 2
verdict: publiable
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v2

**Objet :** la version **2** de [`companion/inputs/programme.json`](../../companion/inputs/programme.json) — 12 étapes, dont **une nouvelle** : la fiche `fiche-chourouk` *(`montrable: true`)* — et l'unique fichier de la bibliothèque, [`companion/inputs/bibliotheque/fiche-chourouk.md`](../../companion/inputs/bibliotheque/fiche-chourouk.md), écrit ce jour.

**Les deux ont été lus intégralement**, contre [`etat.md`](../../psy/outputs/dossier/etat.md), [`PLAN.md`](../../PLAN.md) §2 *(Étape 6 et Transverse)*, [`crise-escalade.md`](../../psy/docs/protocoles/crise-escalade.md) §2 et §4, et la fiche source [`aidant/ressources/fiche-chourouk.md`](../../aidant/ressources/fiche-chourouk.md).

> **Ce que cette passe ne refait pas :** les sept familles d'interdits. `npm run psy:publish` les a déjà appliquées à la v2 et à la fiche — **sans faute**. Il ne fatigue pas ; ce document regarde ce qu'il ne peut pas voir.

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | **C8** | 🔴 **Le programme n'a jamais atteint Kokoro.** `supervision` valait `null` depuis la création du fichier *(commit `1e8fbcc`, seul commit qui le touche)*, et `H:/Mon Drive/kokoro/` ne contient que `journal/` — **pas de `programme.json`**. Les **onze étapes qui font agir** — check-in, les trois écrans de crise, les sept démarches — sont décidées au dossier et invisibles sur le téléphone. Conséquence directe : la publication **hors séance est mécaniquement refusée**, puisque toute étape qui fait agir y est « nouvelle » | `companion/inputs/programme.json` · transit Drive | **Haute** — ne porte pas sur le contenu de la v2 |
| **2** | **C2** | Le cadre de la fiche source est **déclaré périmé par le dépôt lui-même** : « Chourouk ayant accepté, ce cadre est périmé et la fiche est à réécrire, pas à compléter ». La version publiée hérite de ce cadre — elle explique le shutdown et **n'attribue aucun rôle** | `PLAN.md` §2, Transverse | **Moyenne** — assumé, cf. objection n° 1 |
| **3** | **C3** | Le **« non » de la main** n'existe que dans le format *(`signal_arret`, `PROGRAMME.md` §3)*. `PLAN.md` Étape 6 le dit en toutes lettres : « il lui est dit oralement tant que la fiche n'est pas étendue ». **Rien de ce que l'aidant peut lire ne le porte**, la fiche publiée aujourd'hui comprise | `PLAN.md` §2, Étape 6 · `companion/PROGRAMME.md` §3 | **Moyenne** — sans effet tant qu'aucune `seance-duo` n'existe |
| **4** | **C7** | **1 fiche écrite sur les 15 planifiées**, pour un programme jamais publié. Le rapport production / exécution reste celui signalé le 09/08 | `PLAN-DOCUMENTATION.md` §1 | **Basse** — la fiche du jour est celle qui sert le plus tôt *(départ le 07/09)* |
| **5** | **C10** | `montrable: true` **élargit le lectorat** : la fiche devient lisible par quiconque tient le téléphone, pas seulement par Chourouk. Ce qu'elle apprendrait d'un lecteur non prévu : qu'il perd la parole, qu'il est suivi médicalement, qu'il peut exprimer des idées noires | `programme.json`, étape `fiche-chourouk` | **Basse** — arbitrage A2 |
| **6** | **C5** | La v2 **n'ajoute rien que Xavier n'ait demandé**. C'est la surface où l'effet miroir se voit le mieux, et elle est ici muette — pas contredite, simplement pas éprouvée | `programme.json` | **Basse** — à recompter à la prochaine passe |

**Aucun constat ne bloque le contenu de la v2.** Le n° 1 bloque la **portée** de la publication, pas ce qu'elle contient.

---

## Ce qui tient

**Lu ligne à ligne, et vérifié :**

- **C9 — la fiche n'est copiée d'aucun protocole.** Aucune phrase de `bibliotheque/fiche-chourouk.md` ne se retrouve dans `psy/docs/protocoles/`. Elle dérive de `aidant/ressources/fiche-chourouk.md`, elle-même écrite pour un lecteur qui n'est ni soignant ni patient. **Cinq retraits vérifiés** par rapport à la source : la note interne · le tableau de version · la mention du diagnostic *(« personnes autistes »)* · le renvoi aux tests *(« les tests le montrent nettement chez lui »)* · le **3114** et le nom du praticien.
- **C10 — la fiche ne demande aucun jugement.** Pas de « estime si », pas de « décide s'il faut », pas de « rassure-le ». Elle porte trois fois l'inverse : « ce n'est pas à vous de trancher si c'est grave », « vous n'avez pas à trouver la bonne chose à dire », « vous n'êtes pas son soignant ». **Aucun score, aucune hypothèse, aucun compte rendu.**
- **C4 — aucune dérive R6.** La fiche **ne pose aucune question** et ne demande aucune cotation. C'est un texte, pas un questionnaire.
- **C6 — les deux gestes cités sont sourcés.** « Allongez-le, jambes surélevées » est la conduite de `tension-appliquee.md` §5 et de `crise-escalade.md` §1. « Un secours médical par le moyen le plus rapide sur place » est la formulation retenue le 10/08/2026 pour remplacer le **15**.
- **Le retrait des numéros n'affaiblit rien ici.** `crise-escalade.md` §4 pose que l'aidant « n'est pas un relais d'urgence — c'est un relais de charge ». Une fiche qui ne lui confie pas d'escalade chiffrée **applique la doctrine, elle ne la contourne pas** ; et le même §4 tient déjà que le canal téléphonique est le premier à tomber.
- **C8 sur le contenu** — les douze étapes correspondent à `etat.md` §1 : palier 0 PPC en logistique, aucune progression de palier écrite, **aucune étape n'affirme un palier atteint**, aucune étape périmée.
- **Forme** — `version: 2`, `publie_le: 2026-08-18`, ids uniques, `document` en kebab-case, la fiche appelée existe. Vérifié mécaniquement.

---

## Objections de fond

**1. La fiche publiée n'est pas la fiche d'aidant, et elle ne doit pas prétendre l'être.**
`PLAN.md` demande une réécriture au rôle d'aidant — **« non » de la main, mode entraînement, critères d'arrêt**. Cette version ne la fait pas, et c'est défendable : **aucune `seance-duo` n'existe, K6 n'est pas construit, le mode entraînement n'a jamais tourné.** Écrire aujourd'hui les critères d'arrêt d'une séance qui n'existe pas produirait exactement le défaut C3 que ce rôle traque — une règle affirmée, câblée nulle part. ⭐ **La réécriture appartient à K6 ; ce qui est publié aujourd'hui est ce qui sert avant le 07/09.**

**2. Le constat n° 1 est plus grave que la fiche du jour.**
Le dispositif écrit une deuxième surface de documentation pendant que la première — onze étapes, dont les trois écrans de crise et les sept démarches PPC **à boucler avant le départ** — n'est jamais arrivée sur le téléphone. **Ce n'est pas un défaut d'outil : le champ `supervision` n'a jamais été renseigné.** La correction ne demande pas d'écrire, elle demande de publier.

---

## Arbitrages demandés

| # | Question | Recommandation |
|---|---|---|
| **A1** | 🔴 **Les onze étapes qui font agir partent-elles avec la fiche, à la clôture d'une séance ?** *(oui / non)* | **Oui, et c'est le point le plus urgent de cette passe.** Elles sont décidées au dossier depuis le 09/08 et n'ont jamais atteint le téléphone. **`--seance` n'est pas un contournement** : c'est le seul chemin prévu, et il demande une clôture de séance. **Sans lui, la fiche seule ne peut pas partir non plus** — le transit est vide |
| **A2** | **`montrable: true` est-il maintenu ?** *(oui / non)* | **Oui.** C'est le mode d'emploi du type : la fiche existe pour être tendue à quelqu'un au moment où tu ne peux pas parler. La refuser en lecture reviendrait à la rendre inutile au moment exact où elle sert |
| **A3** | **La version transmise à Chourouk garde-t-elle le 3114 ?** *(oui / non)* | **Oui.** `aidant/ressources/fiche-chourouk.md` **n'est pas dans Kokoro** : l'interdit n° 4 ne la vise pas, et l'idéation suicidaire est précisément le déclencheur que `crise-escalade.md` §2 conserve. ⚠️ **Les deux versions divergent volontairement — le noter, pour ne pas « corriger » l'une d'après l'autre plus tard** |
| **A4** | **Chourouk dispose-t-elle des coordonnées du Dr Isorni ?** *(oui / non)* | **À vérifier avant le 07/09.** La fiche lui dit « dites-le à son médecin » ; rien au dossier n'établit qu'elle sait comment. `crise-escalade.md` §5 prévoit ces coordonnées **pour toi**, pas pour elle |

> **Ce document constate. Il ne corrige rien.** La correction est un acte séparé — un audit qui répare ce qu'il trouve ne laisse aucune trace de ce qui n'allait pas.

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 18/08/2026 | Première passe de publication. Programme **v2**, bibliothèque à 1 fiche. **6 constats, 0 bloquant sur le contenu**, 2 objections de fond, 4 arbitrages. Verdict **`publiable`** ; la portée hors séance reste refusée par le script tant que A1 n'est pas rendu. |
