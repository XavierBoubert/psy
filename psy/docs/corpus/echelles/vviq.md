# VVIQ — Vividness of Visual Imagery Questionnaire

**Source :** Marks, D. F. (1973). *Visual imagery differences in the recall of pictures.* British Journal of Psychology, 64(1), 17-24.
**Version retenue :** **VVIQ 16 items**, cotation moderne dite « de Zeman » (1 = aucune image → 5 = aussi vif que la vision réelle).
**Norme d'aphantasie :** Zeman, A., Dewar, M., Della Sala, S. (2015). *Lives without imagery — congenital aphantasia.* Cortex, 73, 378-380.
**Durée :** 5 minutes. **Identifiant `mesures/` :** `vviq`.

---

## 1. Pourquoi cette échelle passe en premier

C'est **la seule échelle du corpus dont le résultat modifie la conception du dispositif**, et pas seulement la compréhension du patient.

L'aphantasie est déclarée par Xavier et intégrée au rapport (§6.4, §9.15) mais **n'a jamais été objectivée par un instrument**. Elle est déjà traitée comme acquise : le PLAN écarte toute technique de visualisation, l'EMDR a été réduit à son instrument, le « lieu sûr » a été déclaré structurellement inopérant. **Une contrainte de conception aussi lourde doit reposer sur une mesure, pas sur une déclaration.**

Trois issues, trois conséquences distinctes :

| Score | Lecture | Conséquence pour le dispositif |
|---|---|---|
| **≤ 32/80** | Aphantasie | Les contraintes actuelles sont confirmées et **définitivement figées**. Toute la famille « imagerie mentale » est écartée pour de bon : lieu sûr, exposition en imagination, relaxation guidée par images, EMDR en imagination. |
| **33-55** | Imagerie faible | Contrainte à **assouplir** : certaines techniques redeviennent envisageables, avec vérification au cas par cas. Ce serait une correction importante du rapport. |
| **≥ 56** | Imagerie normale ou vive | ⚠️ **Discordance majeure** entre la déclaration et la mesure. Ne pas conclure seul : à porter en séance puis, si confirmé, au Dr Isorni. |

---

## 2. Passation

**Consigne littérale à lire, sans reformulation :**

> « Pour chacune des scènes qui suivent, tu vas répondre à quatre questions. À chaque fois, la question est : **à quel point l'image que tu formes est-elle vive et nette ?** Il n'y a pas de bonne réponse. "Aucune image" est une réponse aussi valable que les autres — c'est même une réponse que le questionnaire attend explicitement. »

**⚠️ Piège de passation, à éviter absolument :** ne jamais dire « essaie de mieux voir », « concentre-toi », « prends ton temps pour visualiser ». La consigne standard invite à l'effort ; ici elle transformerait la mesure en test de volonté. Si Xavier répond 1 seize fois de suite, **on ne relance pas.**

**Échelle de réponse, identique aux 16 items :**

| | Libellé |
|---|---|
| **1** | Aucune image. Je « sais » seulement que je pense à l'objet. |
| **2** | Image vague et confuse. |
| **3** | Image moyennement claire et vive. |
| **4** | Image claire et raisonnablement vive. |
| **5** | Parfaitement claire et aussi vive que la vision réelle. |

---

## 3. Items

### Scène 1 — « Pense à un proche, un ami ou une connaissance que tu vois souvent. »

| # | Item |
|---|---|
| 1 | Le contour exact du visage, de la tête, des épaules et du corps |
| 2 | Les mouvements caractéristiques de la tête, la position du corps |
| 3 | La posture précise, la longueur du pas, la démarche |
| 4 | Les différentes couleurs de ses vêtements habituels |

### Scène 2 — « Pense à un soleil levant. »

| # | Item |
|---|---|
| 5 | Le soleil se lève à l'horizon dans un ciel brumeux |
| 6 | Le ciel s'éclaircit et devient bleu, entouré de nuages |
| 7 | Des nuages apparaissent, un orage éclate avec des éclairs |
| 8 | Un arc-en-ciel apparaît |

### Scène 3 — « Pense à la devanture d'un magasin où tu vas souvent. »

| # | Item |
|---|---|
| 9 | L'aspect général du magasin, vu de l'autre côté de la rue |
| 10 | Une vitrine avec ses couleurs, sa forme et le détail des articles exposés |
| 11 | Tu es près de l'entrée : la couleur, la forme et les détails de la porte |
| 12 | Tu entres et vas au comptoir, le vendeur te sert, l'argent change de main |

### Scène 4 — « Pense à un paysage de campagne, avec des arbres, des montagnes et un lac. »

| # | Item |
|---|---|
| 13 | Les contours du paysage |
| 14 | La couleur et la forme des arbres |
| 15 | La couleur et la forme du lac |
| 16 | Un vent fort souffle sur les arbres et sur le lac, provoquant des vagues |

---

## 4. Cotation et seuils

- **Score = somme des 16 items.** Aucun item inversé. **Étendue : 16 à 80.**
- **Aphantasie : ≤ 32/80** (Zeman et al., 2015 — l'aphantasie congénitale « pure » correspond typiquement au plancher, 16/80).
- **Hyperphantasie : ≥ 75/80.**

> 🔴 **Piège de cotation, à vérifier à chaque passation.** La version **originale de Marks (1973) cote à l'envers** : chez lui, **1 = parfaitement clair** et 5 = aucune image, si bien qu'un score *bas* signifie une imagerie *vive*. La littérature moderne sur l'aphantasie a inversé la convention. **La version retenue ici est la moderne : 1 = aucune image.** Un score comparé à la mauvaise convention inverse complètement la conclusion. Le champ `version` du fichier `mesures/` doit porter `"VVIQ-16-Zeman"` pour rendre l'ambiguïté impossible.

---

## 5. ⚠️ Ce que cette échelle ne dit pas

- **Elle ne mesure que l'imagerie visuelle.** L'aphantasie peut être visuelle et laisser intactes l'imagerie auditive, motrice ou spatiale — ce qui est cliniquement décisif ici : **si l'imagerie motrice ou corporelle est préservée, elle est une voie de travail** (ancrage corporel, répétition motrice), et c'est exactement le genre de substitution que le dispositif cherche. Le VVIQ ne répond pas à cette question. Il faudrait un instrument séparé (p. ex. questionnaires d'imagerie auditive ou motrice) — non versé à ce jour, à considérer si le VVIQ confirme l'aphantasie visuelle.
- **C'est une échelle strictement introspective.** Elle demande de rapporter une expérience interne — précisément la fonction déficitaire. La réserve générale du corpus s'applique (`README.md` §1, règle 2 : un score élevé est informatif, un score bas ne clôt aucune question). Ici toutefois le risque est moindre : la question n'est pas « qu'est-ce que tu ressens » mais « y a-t-il une image, oui ou non », ce qui est plus proche d'un constat que d'une introspection.
- **Elle ne dit rien de la mémoire ni de la capacité de description.** Un aphantasique décrit parfaitement un visage sans en former l'image. Un score plancher ne documente aucun déficit cognitif.
- **Aucune norme française validée n'est utilisée ici** : les seuils sont ceux de la littérature anglophone. Suffisant pour un usage clinique individuel, à mentionner si le chiffre est transmis à un praticien.

---

## 6. Ce qu'on écrit dans `mesures/`

```json
{
  "date": "2026-08-09",
  "echelle": "vviq",
  "version": "VVIQ-16-Zeman",
  "score": 16,
  "score_max": 80,
  "seuil": { "valeur": 32, "sens": "en_dessous", "interpretation": "aphantasie" },
  "sous_scores": null,
  "reponses": [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  "passation": "claude-code",
  "notes": null
}
```

> ✅ **Écart avec `SCHEMA.md` §6 — résolu le 09/08/2026** *(audit de cohérence ; annoncé puis appliqué, conformément à `SCHEMA.md` §9)*. L'exemple du schéma portait `"version": "VVIQ-2"` avec un `score_max` de 80, alors que le **VVIQ-2** (Marks, 1995) compte **32 items** — chaque scène évaluée les yeux ouverts puis fermés — et plafonne à **160** : l'exemple était incohérent avec lui-même. Il listait par ailleurs 4 réponses pour une échelle qui en compte 16. **Le schéma est désormais aligné sur la version retenue ici — VVIQ 16 items /80, identifiant `"VVIQ-16-Zeman"`** ; `SCHEMA.md` est passé en v1.1. Aucune règle n'a changé : seul l'exemple était faux.
> *(Renvoi mis à jour le 13/08/2026 : le schéma du dossier vit désormais au `psy/DOSSIER.md` §7 — `SCHEMA.md` a été absorbé. **L'entrée ci-dessus n'est pas réécrite** : c'est un enregistrement daté, R2.)*
