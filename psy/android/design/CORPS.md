# Kokoro — le corps

**Spécification graphique du personnage.** ⭐ **v1.0 — 13/08/2026 — retenue.** Le corps de Kokoro est un **petit robot kawaii en 2D**, dessiné pour être découpé et animé sur une page web.

> 📐 **Ce document n'est pas de la doctrine.** La doctrine du personnage tient dans [`../../../PLAN.md` §5.3](../../../PLAN.md#53-le-personnage) et les invariants dans [§5.6](../../../PLAN.md#56-les-invariants-traduits-en-règles-vérifiables) et [§5.7](../../../PLAN.md#57-ce-qui-nentrera-jamais-dans-kokoro). **Ici on ne décide rien de clinique : on dessine ce qui a déjà été décidé.**
>
> ⏱️ **Ce document s'applique au jalon K7 (« la présence »), pas avant.** K5 et K6 passent d'abord.
>
> 🔴 **Le passage au robot est un changement d'apparence — donc il s'annonce avant installation** (règle §5.6, dernière ligne). Comme Kokoro n'a encore **aucun** visage installé, l'annonce sera la première, pas un changement. **La prochaine, elle, en sera un.**

---

## 1. Ce qui a été retenu, et ce que ça remplace

**Planche de référence : [`retenus/kokoro-planche-01.png`](retenus/kokoro-planche-01.png)** — corps entier, six expressions, trois poses de désignation.
**Candidat retenu : [`retenus/kokoro-corps.png`](retenus/kokoro-corps.png)** *(série `robot/01-b`)*.

**La v0.1 décrivait un ovoïde continu, sans expressions et sans geste. Elle est abandonnée.** Trois demandes de Xavier l'ont fait tomber, dans cet ordre :

| Demande | Ce qu'elle change |
|---|---|
| **Un robot kawaii, animable en 2D sur une page web** | Le personnage cesse d'être une forme unique et devient un **assemblage de pièces séparées** — c'est la condition d'un rig simple, et ça vaut pour le web **comme** pour Android |
| **Plusieurs expressions de visage** | Le visage cesse d'être fixe. ⭐ **Le jeu reste fermé — six, listées au §3** — et **aucune des six ne demande à être interprétée** |
| 🔴 **La possibilité de montrer des parties de l'écran** | Un geste de désignation entre dans le personnage. **C'était interdit par la v0.1.** L'arbitrage, la réserve et les trois garde-fous sont au **§6** |

> ⭐ **Ce que le robot rend possible et que l'ovoïde ne pouvait pas :** l'ovoïde portait ses yeux à même le corps — pour ne rien donner à lire, il fallait le **retourner de dos**. **Le robot porte son visage sur un panneau**, et un panneau **s'éteint**. Un écran vide est la façon la plus économique du monde de dire « il n'y a rien à décoder ici », et elle ne coûte aucun dessin supplémentaire.

---

## 2. La morphologie

| Élément | Forme | Motif |
|---|---|---|
| **Tête** | Carré arrondi, un peu plus large que haut, avec un **panneau-visage** rectangulaire arrondi en incrustation | Le panneau est la **seule** surface où quelque chose s'écrit ou s'affiche. En dehors, rien ne bouge |
| **Corps** | Capsule arrondie sous la tête, **nettement plus petite que la tête, mais pas minuscule** | Ni chibi (proportions de nourrisson → infantilisant pour un homme de 40 ans), ni humanoïde (proportions d'adulte → lecture sociale) |
| **Bras** | Deux moignons courts et arrondis, **flottants**, détachés du corps, **sans main ni doigt** | Une main fait signe, un doigt accuse. **Ni l'une ni l'autre n'existent.** Flottants = un pivot, aucune déformation |
| **Pieds** | Deux formes arrondies flottantes, **aucune jambe tracée** entre le corps et elles | Idem : rien à plier, rien à animer de compliqué |
| ⭐ **Plaque de poitrine** | Petite plaque arrondie céladon, **vide**, centrée | Le **seul** endroit coloré du dessin, et l'ancre visuelle de la respiration |
| **Absents** | Pas de cou, pas d'oreilles, pas de cheveux, pas de vêtement, pas d'antenne, pas de rivet, pas d'engrenage, pas de câble, pas de cadran | Oreille = écoute attentive = attente. Vêtement = identité sociale. **Antenne = indicateur d'humeur**, donc information à décoder |

> 📌 **La plaque de poitrine est vide, et c'est un choix technique assumé.** Elle devait porter le caractère **心**. Le modèle d'images ne le trace pas correctement (constat de la série `corps/01`, quatre essais, quatre versions fausses). **Le 心 final sera un tracé vectoriel propre**, posé à la main dans la plaque — les planches ne font pas foi sur ce point et il ne sert à rien d'itérer dessus.

**Symétrie stricte, vue de face, aucune perspective.** Pas d'axe incliné, pas de trois-quarts.

---

## 3. Le visage — six expressions, le jeu est fermé

Tout se joue sur le panneau : **deux yeux, une bouche.** Rien d'autre n'y entre jamais.

| # | Nom | Yeux | Bouche | Quand |
|---|---|---|---|---|
| 1 | **`neutre`** | Deux ovales pleins | Trait horizontal court | **Par défaut.** Écran d'accueil, veille, overlay |
| 2 | **`attentif`** | Deux ovales pleins | Barre courte arrondie | Une étape est ouverte, un contenu est affiché |
| 3 | **`chaleureux`** | Deux arcs vers le haut *(yeux fermés souriants)* | Petit arc vers le haut | Une étape est faite. ⚠️ **Jamais en réaction à une étape non faite** — voir §8 |
| 4 | **`clignement`** | Deux traits horizontaux courts | Trait court | Transition uniquement, jamais un état stable |
| 5 | **`veille`** | Deux arcs vers le bas *(yeux fermés au repos)* | Trait court | Mode shutdown, écran en veille |
| 6 | **`de-cote`** | Deux ovales pleins **décalés du même côté** | Trait court | Accompagne une désignation (§6) : **il regarde ce qu'il montre** |

🔴 **Aucune septième expression ne s'ajoute sans passer par ce document et par une annonce préalable.**

### Ce qui garantit qu'aucune expression ne devient un reproche

| Règle | Pourquoi elle tient |
|---|---|
| 🔴 **Aucun sourcil. Jamais.** | Le sourcil est le porteur principal du reproche, de l'inquiétude et de la déception. **En ne le dessinant pas, on rend le reproche littéralement indessinable** — c'est un invariant tenu par la géométrie, pas par la discipline |
| 🔴 **La bouche n'est jamais concave vers le bas** | Droite, en barre, ou convexe vers le haut. **Les trois autres formes n'existent pas dans le jeu de pièces** |
| **Les yeux ne s'écarquillent pas** | Taille constante. Aucun reflet, aucun éclat, aucun brillant |
| ⭐ **Il ne fixe jamais Xavier** | Aucune des six n'est un regard soutenu vers le lecteur. La 6 regarde **ailleurs, à dessein** |
| **Aucun symbole** | Pas de goutte de sueur, pas de larme, pas de rougissement, pas de cœur, pas d'étoile, pas d'icône flottante, pas de bulle |

⭐ **Et le panneau s'éteint.** Panneau vide = présence sans visage. C'est l'état des postures `cote-a-cote` et `retrait` (§7) : **zéro trait à lire au moment le plus chargé**, sans avoir à retourner le personnage.

---

## 4. Le trait et la palette

**Ligne claire, épaisseur unique, terminaisons arrondies.** Aucun aplat dégradé, aucune ombre portée, aucune hachure, aucune texture, aucun reflet métallique. Le trait ne varie **jamais** d'épaisseur à l'intérieur d'un dessin.

| Rôle | Valeur | Motif |
|---|---|---|
| Fond | `#F4F1EA` *(papier)* | ❌ **Jamais `#FFFFFF` ni `#000000`** — le contraste maximal est agressif (hypersensibilité visuelle) |
| Trait | `#2B2F33` *(encre douce)* | Idem |
| Corps (remplissage) | `#FBF9F5` | À peine détaché du fond |
| Panneau-visage | `#E6E2DA` *(gris chaud)* | Assez pour se distinguer, pas assez pour trancher |
| ⭐ Accent — **plaque de poitrine uniquement** | `#8FA99B` *(céladon)* | **Une seule couleur, un seul endroit.** Ni rouge ni orange (alerte), ni bleu clinique |

**Thème sombre :** fond `#14171A`, trait `#D8D4CC`, corps `#1C2024`, panneau `#22262B`, accent inchangé.
**Épaisseur du trait :** ≈ 2,5 % de la hauteur du personnage, constante à toutes les tailles.

---

## 5. La respiration

**99 % du temps, c'est tout ce qui bouge.**

- **Cycle :** 4,5 s inspiration + expiration, sinusoïde continue, aucun temps d'arrêt.
- **Amplitude :** le corps s'étire de **2 %** en hauteur, se rétracte de 1 % en largeur. La plaque de poitrine suit. **La tête ne bouge pas.**
- 🔴 **Le rythme ne change jamais.** Ni plus vite, ni plus lent, ni en fonction de quoi que ce soit. **Une respiration qui varie devient une information à décoder** — et une information non demandée qui prétend parler de l'état de Xavier est exactement ce que §5.7 interdit.
- 🔴 **Ce n'est pas un guide respiratoire.** Aucun texte n'invite jamais à se caler dessus.
- **Clignement (`4`) :** au maximum une fois toutes les 20 s, durée ≤ 200 ms, **rythme irrégulier mais borné** — un clignement régulier devient un métronome.
- Transitions entre expressions et entre postures : **≥ 800 ms**, easing continu (invariant §5.6). Aucune apparition instantanée, aucun *cut*.

---

## 6. 🔴 La désignation — l'arbitrage du 13/08/2026

**Xavier a demandé que Kokoro puisse montrer des parties de l'écran. C'est accordé, et la réserve reste écrite.**

**L'objection, qui était réelle et qui n'est pas levée :** *pointer est un geste social.* La v0.1 l'interdisait avec le reste des gestes (faire signe, lever le pouce, applaudir) parce qu'un geste dirigé vers quelqu'un appelle une réponse, et qu'une réponse attendue est une charge de camouflage. **Ce que l'arbitrage retient contre l'objection :** montrer un élément d'interface n'est pas un geste **vers Xavier**, c'est un geste **vers l'écran** — et c'est la fonction **éduquer** de [§1.3](../../../PLAN.md#13-kokoro-心--le-compagnon), qui sans ça reste purement textuelle.

> 🔴 **Quatre garde-fous, aucun optionnel :**
> 1. **Le bras ne dépasse jamais la ligne des épaules.** Un bras levé se lit comme un salut ou une main levée — et le salut est interdit. *(Sur la planche `01`, la pose vers le haut a exactement ce défaut : elle ne fait pas partie du jeu retenu.)*
> 2. **Le bras ne pointe jamais le lecteur** — ni de face, ni vers l'avant. Il désigne un élément **de l'écran**, latéralement ou vers le bas.
> 3. **Aucune main, aucun doigt, aucune paume.** Bout arrondi, membre plein. **Un doigt accuse ; un moignon ne peut pas.**
> 4. ⭐ **Une désignation s'accompagne toujours d'un texte qui dit ce qu'elle désigne.** Xavier ne décode jamais un geste : il le lit écrit à côté. Même règle que les postures (§7).
>
> **Et une frontière :** ❌ **la désignation ne sert jamais à réclamer une action.** Elle montre où une chose se trouve. Elle ne dit jamais *« clique ici »*, *« tu as oublié ça »*, *« regarde ce que tu n'as pas fait »*.

---

## 7. Les cinq postures — le jeu est fermé

| # | Nom | Ce qu'on voit | Expression | Quand | Ce que le texte dit à côté |
|---|---|---|---|---|---|
| 1 | **`repos`** | Corps entier, de face, bras le long du corps | `neutre` | **Par défaut. 99 % du temps.** Accueil, veille, overlay | Rien. **Seul cas sans texte — parce qu'il ne dit rien** |
| 2 | **`present`** | Idem, immobile | `attentif` | Une étape ou une fiche est ouverte | Le libellé de l'étape |
| 3 | **`montre`** | Un bras tendu latéralement ou vers le bas | `de-cote` | Une fiche explique où se trouve quelque chose | ⭐ **Ce qu'il montre, écrit.** Jamais une injonction (§6) |
| 4 | ⭐ **`cote-a-cote`** | Corps entier, **panneau éteint** | *(aucune)* | Pendant un exercice, une désensibilisation, un bloc de tension appliquée | « Je reste là pendant que tu fais ça. » **Zéro visage à lire au moment le plus chargé** |
| 5 | **`retrait`** | Réduit à ≈ 40 %, en bord d'écran, **panneau éteint** | *(aucune)* | Mode shutdown : sollicitations coupées | « Je me mets de côté. Rien n'attend. » ❌ **Pas de visage = pas de tristesse lisible.** Ce n'est pas une bouderie, et c'est écrit |

⏳ **Une sixième pose reste à dessiner et n'est pas dans la planche `01` : `allonge`** — le robot allongé sur le dos, pieds surélevés, **uniquement** pour l'écran vasovagal. Elle n'est pas décorative : *« allonge-toi, jambes surélevées »* est une consigne à se représenter, et **Xavier est aphantasique** ; une silhouette allongée est une **structure externe** ([règle §9.19](../../../PLAN.md#22--la-règle-centrale--signal-interne-absent--structure-externe)), pas une consigne à imaginer. **C'est la seule pose du jeu qui n'est pas de face.**

**Aucune autre posture ne s'ajoute sans passer par ce document, par une annonce préalable et par la supervision.**

---

## 8. Ce que le corps ne fait jamais

Miroir corporel de [`PLAN.md` §5.7](../../../PLAN.md#57-ce-qui-nentrera-jamais-dans-kokoro) :

1. **Se lever, s'approcher, grandir, marcher vers le lecteur, entrer dans le champ.** Kokoro ne vient jamais vers Xavier — **y compris avec son corps.**
2. **Faire signe, lever un bras au-dessus de l'épaule, applaudir, lever le pouce.** Le pouce levé est une **évaluation** ; l'évaluation est interdite. *(La désignation du §6 est la seule exception, et elle est bornée.)*
3. **S'affaisser, baisser la tête, se recroqueviller, laisser tomber les bras.** Aucune posture de découragement, jamais — **y compris après une étape non faite.**
4. 🔴 **Réagir à une étape non faite.** L'expression `chaleureux` ne se déclenche que sur un fait accompli ; **son absence n'est pas un message.** Il n'existe aucune expression « déçu », et il n'en existera jamais.
5. **Fixer le lecteur.**
6. **Changer d'apparence sans annonce** : pas de tenue, pas de saison, pas de variante surprise, pas de thème d'événement.
7. **Bouger tout seul** hors de la respiration et du clignement : il ne se déplace pas à l'écran, ne sursaute pas, ne suit pas le doigt, ne réagit pas au toucher.
8. **Se dupliquer, se refléter, se dédoubler** — une seule instance à l'écran, toujours au même endroit.

---

## 9. Le découpage pour l'animation

⭐ **La demande « facile à animer en 2D » n'est pas une contrainte de confort : elle décide de la façon dont le personnage est dessiné.** Onze pièces, chacune une forme fermée simple, aucune imbrication.

| Pièce | Pivot | Ce qui l'anime |
|---|---|---|
| `tete` | — | Rien. **Elle ne bouge pas** |
| `panneau` | — | Allumé / éteint |
| `oeil-g`, `oeil-d`, `bouche` | — | **Échange de forme**, pas de déformation : chaque expression est un jeu de trois formes |
| `corps` | Base | Respiration seule (2 % / 1 %) |
| `plaque` | Centre du corps | Suit le corps |
| `bras-g`, `bras-d` | Épaule, **un seul pivot, rotation bornée à la ligne d'épaule** | Désignation (§6) |
| `pied-g`, `pied-d` | — | Rien |

- **Aucune pièce n'a de coude ni de genou** — un membre est une forme, une rotation, aucune déformation.
- **Aucune pièce n'en recouvre une autre** : elles se touchent ou flottent.
- **Format de production : SVG à trait vectoriel**, une pièce par groupe nommé, **plus `VectorDrawable` pour Android** — l'app ne doit dépendre d'aucune ressource distante ni d'aucune police externe ([§5.7 point 6](../../../PLAN.md#57-ce-qui-nentrera-jamais-dans-kokoro)).
- 🔴 **Aucun bitmap.** ⚠️ **Les images générées par le script ne sont pas des livrables** — ce sont des planches de recherche. **Un PNG sorti d'un modèle n'entre ni dans l'APK ni dans la page web.**
- **Animation web : CSS/SVG uniquement**, aucune bibliothèque d'animation, aucun canvas, aucun runtime tiers.

---

## 10. Déclinaisons

| Usage | Taille | Note |
|---|---|---|
| Overlay K7 | 48 dp de haut, coin bas-droit, position fixe | `repos` seul, respiration active |
| En-tête d'écran | 96 dp | `repos`, `present` ou `montre` |
| Écran de crise | ❌ **absent** | 🔴 **L'écran de crise ne porte aucun personnage.** Deux boutons, rien d'autre — un compagnon décoratif au pire moment est du bruit |
| Écran vasovagal | 160 dp | `allonge` uniquement, à côté de la consigne |
| Séance à deux (K6) | ❌ **absent de l'écran de l'aide** | L'aide-au-patient lit un déroulé, pas un personnage. **C10** |
| Surface web desktop ([§5.8](../../../PLAN.md#58-la-surface-web-desktop)) | Identique | ⭐ **Même jeu de pièces, même palette, même jeu fermé.** Une seule apparence sur les deux surfaces |

---

## 11. Ce qui reste à trancher

1. **La pose `allonge`** — à dessiner (§7). C'est le seul manque fonctionnel de la planche `01`.
2. **Le 心 dans la plaque** : tracé vectoriel à poser à la main. Signature juste, ou marquage de trop ? Sans lui, la plaque reste un simple carré céladon — ce qui est tenable.
3. **La couleur d'accent** : céladon `#8FA99B` retenu par défaut. Alternatives tenables si Xavier veut changer : terre cuite sourde `#B08968`, ardoise `#7C8B99`. *(Un accent unique, quel qu'il soit — le nombre n'est pas négociable, la teinte oui.)*
4. **La posture `retrait`** reste la plus risquée du jeu : à valider en la voyant animée, pas sur le papier.

---

## 12. Le pipeline d'images

```bash
npm run image -- robot --n=4 --format=4:3          # 4 candidats + une planche contact
npm run image -- robot --ref=robot/01-b.png        # itère à partir du candidat retenu
```

- **La charte vit dans [`prompts/_base.md`](prompts/_base.md)**, écrite une fois et préfixée à chaque appel : morphologie, palette, six expressions, règles de visage, désignation, interdits. **Itérer = modifier trois lignes du prompt de variante, pas réécrire la charte.**
- [`prompts/robot.md`](prompts/robot.md) ne décrit plus que **la mise en page de la planche**. Une pose isolée se demande dans un nouveau fichier de variante (ex. `allonge.md`).
- Chaque exécution écrit `sorties/<variante>/NN-a.png`, `NN-b.png`… **plus `NN-planche.png`** — une seule image qui contient tous les candidats étiquetés, **pour n'avoir à en lire qu'une**. `NN.md` conserve le prompt exact, le modèle et les références.
- `sorties/` n'est **pas** versionné *(`.gitignore`)*. **Ce qui est retenu est promu à la main dans [`retenus/`](retenus/), qui l'est.**
