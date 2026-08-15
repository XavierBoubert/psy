# Kokoro — le corps

**Spécification graphique du personnage.** ⭐ **v2.3 — 16/08/2026 — retenue.** Le corps de Kokoro est un **petit robot kawaii en 2D**, dessiné **en pièces séparées pour être riggé et animé**.

> ⭐ **v2.3 — *« Kokoro veille sur toi »*** *(arbitrage de Xavier, 16/08/2026)*. **L'écran de crise du monde porte un personnage** : `attente` devient **`accoude`**, panneau **allumé**, accoudé au bouton *Mot code*, la tête penchée de 6°. **Détail, motifs et six bornes au [§10.2](#102--kokoro-veille-sur-toi--la-dérogation-de-lécran-de-crise-arbitrée-le-16082026).** 🔴 **La borne qui compte : `CriseActivity`, la tension appliquée et la phrase pour le soignant n'en portent aucun**, et un test de sources l'interdit.
>
> ⭐ **v2.2 — la présence est redescendue ici** *(étape E14 de [`PRESENCE.md`](PRESENCE.md))*. Six sections changent : le visage garde **six** expressions mais **`serein` entre et `de-cote` sort** (§3) · le clignement n'agit plus que sur les yeux (§5) · les postures passent de cinq à **dix** (§7) · les interdits de mouvement sont **réécrits, pas contournés** (§8 points 1, 7 et 8) · les tailles deviennent celles des **deux régimes** (§10) · le point ouvert §11.6 est tranché.
>
> ⭐ **v2.1 :** le visage passe au **morphing** — une forme se déforme vers la suivante au lieu de s'échanger en fondu croisé. **Ce que la v2.0 écrivait au §9 (« échange de forme, pas de déformation ») est corrigé, pas nuancé.** Motif et mécanisme : [§9](#-le-morphing-du-visage-14082026).

> 📐 **Ce document n'est pas de la doctrine.** La doctrine du personnage tient dans [`companion/README.md` §4](README.md) et les invariants dans [`companion/README.md` §5](README.md) et [`companion/README.md` §6](README.md). **Ici on ne décide rien de clinique : on dessine ce qui a déjà été décidé.**
>
> ⏱️ **Ce document s'applique au jalon K7 (« la présence »), pas avant.** K5 et K6 passent d'abord.
>
> 🔴 **Le passage au robot est un changement d'apparence — donc il s'annonce avant installation** (règle §5.6, dernière ligne). Comme Kokoro n'a encore **aucun** visage installé, l'annonce sera la première, pas un changement. **La prochaine, elle, en sera un.**

---

## 1. Ce qui fait foi

🔴 **Le dessin est [`retenus/kokoro-corps-v2.svg`](./ressources/retenus/kokoro-corps-v2.svg), et il fait foi.** Ce document le décrit ; il ne le décide pas. En cas de désaccord entre une phrase d'ici et un chiffre de là-bas, **c'est le SVG qui a raison**, et c'est cette phrase-ci qu'il faut corriger.

⭐ **Ce que la v2 change d'abord, c'est l'auteur.** La v1 retenait une **image de modèle** promue dans `retenus/`, et le code en était une interprétation. **La v2 est dessinée par Xavier**, au trait, dans un éditeur vectoriel. Le code n'interprète plus rien : `app/src/main/kotlin/io/allonsy/kokoro/corps/Geometrie.kt` est une **transcription littérale** du fichier — mêmes tracés, mêmes matrices, mêmes épaisseurs, caractère pour caractère — et `CorpsInvariantsTest` relit le SVG à chaque build pour refuser la moindre dérive.

| Ce que Xavier a apporté le 14/08/2026 | Ce que ça change |
|---|---|
| ⭐ **Les pivots des bras sont dans les épaules** | Le bras tourne autour du centre du bouchon arrondi qui le termine en haut. Il **pend déjà écarté de 19,5°** dans le dessin : toute ouverture part de là, et l'horizontale n'est plus à 90° mais à **70,5°** |
| ⭐ **Les pivots des pieds sont au centre du ventre** | Un pied n'a pas d'articulation à lui : il **orbite** autour d'un point situé dans le corps. Ce point n'a pas été choisi ici — **il est écrit dans le dessin** (§9) |
| ⭐ **Le kanji 心 est réalisé** | Il était le seul manque graphique de la v1. **Il remplace la plaque de poitrine**, qui disparaît — et avec elle la seule couleur du personnage (§4) |

**La v0.1 décrivait un ovoïde continu, sans expressions et sans geste. Elle est abandonnée.** Trois demandes de Xavier l'ont fait tomber, dans cet ordre :

| Demande | Ce qu'elle change |
|---|---|
| **Un robot kawaii, animable en 2D** | Le personnage cesse d'être une forme unique et devient un **assemblage de pièces séparées** — c'est la condition d'un rig simple |
| **Plusieurs expressions de visage** | Le visage cesse d'être fixe. ⭐ **Le jeu reste fermé — six, listées au §3** — et **aucune des six ne demande à être interprétée** |
| 🔴 **La possibilité de montrer des parties de l'écran** | Un geste de désignation entre dans le personnage. **C'était interdit par la v0.1.** L'arbitrage, la réserve et les trois garde-fous sont au **§6** |

> ⭐ **Ce que le robot rend possible et que l'ovoïde ne pouvait pas :** l'ovoïde portait ses yeux à même le corps — pour ne rien donner à lire, il fallait le **retourner de dos**. **Le robot porte son visage sur un panneau**, et un panneau **s'éteint**. Un écran vide est la façon la plus économique du monde de dire « il n'y a rien à décoder ici », et elle ne coûte aucun dessin supplémentaire.

> 📎 **Les planches de recherche restent, et ne font plus foi** : [`retenus/kokoro-planche-01.png`](./ressources/retenus/kokoro-planche-01.png) et [`retenus/kokoro-corps.png`](./ressources/retenus/kokoro-corps.png) sont l'étape qui a mené au dessin, pas le dessin. **`retenus/kokoro-corps.svg` (v1) a été supprimé** — deux références qui se contredisent valent moins qu'une seule qui fait foi.

---

## 2. La morphologie

Le personnage tient dans une vue de **240 × 200**, et occupe **125 × 178** — c'est le repère de tous les chiffres de ce document.

| Élément | Forme | Motif |
|---|---|---|
| **Tête** | Carré arrondi, **plus large que haut** *(110 × 92)*, dessiné **deux fois** : une coque, et le même tracé réduit en incrustation qui fait le **panneau-visage** | Le panneau est la **seule** surface où quelque chose s'écrit ou s'affiche. En dehors, rien ne bouge. Le redessiner à l'identique plutôt que d'inventer une seconde forme garde le cerne parallèle partout |
| **Corps** | Poire arrondie sous la tête — **étroite aux épaules, large au ventre** *(71 × 74)*, **nettement plus petite que la tête, mais pas minuscule** | Ni chibi (proportions de nourrisson → infantilisant pour un homme de 40 ans), ni humanoïde (proportions d'adulte → lecture sociale) |
| ⭐ **Ligne du ventre** | Un seul trait, courbe, en travers du bas du corps | Elle donne au corps son assise sans ajouter de pièce. **Elle ne sépare rien** : ce n'est pas une ceinture, pas une trappe, pas un cadran |
| **Bras** | Deux fuseaux arrondis, **flottants**, détachés du corps, **sans main ni doigt**, écartés de **19,5°** de la verticale | Une main fait signe, un doigt accuse. **Ni l'une ni l'autre n'existent.** Flottants = un pivot, aucune déformation |
| **Pieds** | Deux formes arrondies flottantes, **aucune jambe tracée** entre le corps et elles | Idem : rien à plier, rien à animer de compliqué |
| ⭐ **心** | Quatre traits, à l'encre, **posés à même le corps, du côté du cœur** *(donc à droite de l'écran, puisqu'il nous fait face)* | Il **remplace** la plaque de poitrine de la v1. Le seul signe du dessin, et il ne veut rien dire d'autre que le nom du compagnon |
| **Absents** | Pas de cou, pas d'oreilles, pas de cheveux, pas de vêtement, pas d'antenne, pas de rivet, pas d'engrenage, pas de câble, pas de cadran, **et plus de plaque** | Oreille = écoute attentive = attente. Vêtement = identité sociale. **Antenne = indicateur d'humeur**, donc information à décoder |

> ⭐ **La plaque de poitrine est supprimée, et le 心 est fait.** La v1 laissait la plaque **vide** parce que le modèle d'images ne savait pas tracer le caractère (série `corps/01`, quatre essais, quatre versions fausses) et remettait le 心 à un tracé vectoriel ultérieur. **Ce tracé existe** — Xavier l'a dessiné à la main dans la v2. La plaque n'avait plus de raison d'être, elle disparaît. **C'est aussi la disparition de la seule couleur du personnage** : voir §4 et §11.

**Vue de face, aucune perspective.** Pas d'axe incliné, pas de trois-quarts.

> ⭐ **Une seule dérogation, arbitrée par Xavier le 16/08/2026, et elle ne porte que sur la tête** *(§7 n° 9, `accoude`)* **:** sur l'écran de crise, **la tête penche de 6°** autour du milieu de la ligne des épaules. 🔴 **Le corps, lui, reste de face** — aucun trois-quarts, aucune perspective, aucun raccourci. **C'est ce qui distingue *veiller sur quelqu'un* de *le fixer***, et l'angle est borné à 10° dans le code pour qu'une nuance ne devienne pas une pose.

> 📌 **La symétrie est celle d'un dessin à la main, pas celle d'un gabarit.** Les axes des bras, des pieds, des yeux et de la bouche tombent entre 119,96 et 120,33, et le pied de gauche porte une rotation de 3,6° que celui de droite n'a pas. **Ces écarts sont dans le dessin, donc ils sont dans l'application** — les corriger reviendrait à redresser ce que Xavier a tracé.

---

## 3. Le visage — six expressions, le jeu est fermé

Tout se joue sur le panneau : **deux yeux, une bouche.** Rien d'autre n'y entre jamais.

> 📌 **Xavier n'a dessiné que `neutre`** — deux ellipses pleines de 6,77 × 8,92, **plus hautes que larges**, et un trait de bouche de 20,8 de long, 3,4 d'épais, à bouts ronds. **Les cinq autres expressions en sont dérivées** : mêmes demi-largeurs, même épaisseur. Elles tiennent donc de ce document, pas du dessin, et **une passe de validation à l'écran leur reste due** — c'est le §11.

| # | Nom | Yeux | Bouche | Quand |
|---|---|---|---|---|
| 1 | ⭐ **`neutre`** *(dessinée)* | Deux ovales pleins | Trait horizontal court | Le dessin de Xavier, tel quel. **Reste dans le jeu ; plus aucune posture ne l'appelle** |
| 2 | ⭐ **`serein`** | Deux ovales pleins | **Semi-sourire** — exactement **la moitié de la flèche** du sourire, 1,75 contre 3,5 | **Par défaut.** Écran d'accueil, veille, overlay. **Il ne demande rien et ne dit rien** |
| 3 | **`attentif`** | Deux ovales pleins | Barre courte arrondie | Une étape est ouverte, un contenu est affiché |
| 4 | **`chaleureux`** | Deux arcs vers le haut *(yeux fermés souriants)* | Petit arc vers le haut | Une étape est faite. ⚠️ **Jamais en réaction à une étape non faite** — voir §8 |
| 5 | **`clignement`** | Deux traits horizontaux courts | Trait court | Transition uniquement, jamais un état stable |
| 6 | **`veille`** | Deux arcs vers le bas *(yeux fermés au repos)* | Trait court | Mode shutdown, écran en veille, **et le sommeil d'une liste vide** *(`PRESENCE.md` §2 — `sommeil` réutilise `veille`, il n'ouvre pas de septième forme)* |

🔴 **Aucune septième expression ne s'ajoute sans passer par ce document et par une annonce préalable.**

> ⭐ **Le jeu reste à six, et le compte a été refait** *(E14, 15/08/2026)*. `PRESENCE.md` §5 annonçait **sept** : c'était une erreur d'arithmétique, pas une décision — **`serein` est entré et `de-cote` est sorti**, donc le jeu n'a pas grandi. Il n'y a **jamais** eu de septième expression, et l'annonce due à Xavier porte un échange, pas un ajout.
>
> 🔄 **`de-cote` a quitté le jeu, et ce n'est pas une suppression : c'est un changement de nature.** Elle n'était que `serein` plus un décalage des deux yeux — **une expression qui portait une direction de regard.** Le regard est devenu un **axe indépendant**, réglé par la posture (§7) et non par le visage : c'est ce qui permet de regarder une liste, un bras ou un élément désigné **sans inventer une forme de visage par direction**. Le décalage lui-même n'a pas bougé d'une unité.

### Ce qui garantit qu'aucune expression ne devient un reproche

| Règle | Pourquoi elle tient |
|---|---|
| 🔴 **Aucun sourcil. Jamais.** | Le sourcil est le porteur principal du reproche, de l'inquiétude et de la déception. **En ne le dessinant pas, on rend le reproche littéralement indessinable** — c'est un invariant tenu par la géométrie, pas par la discipline |
| 🔴 **Les commissures de la bouche ne tombent jamais** | Droite, en barre, ou en sourire — **milieu plus bas que les extrémités**, `⌣`. **Les autres formes n'existent pas dans le jeu de pièces**, et `CorpsInvariantsTest` mesure le milieu de l'arc pour le vérifier. *(La v1 disait « jamais concave vers le bas » : la formulation est ambiguë et elle a déjà produit une moue une fois. On décrit désormais la forme, pas son interdit.)* |
| ⚠️ **Aucun tracé de visage n'est posé dans le repère de la bouche** | Ce repère porte un **demi-tour** : un arc convexe vers le haut y sortirait **concave vers le bas** — précisément la forme interdite. Les expressions sont donc placées par une simple translation, jamais par la matrice du dessin |
| **Les yeux ne s'écarquillent pas** | Taille constante. Aucun reflet, aucun éclat, aucun brillant |
| ⭐ **Il ne suit jamais Xavier du regard** | 🔄 **Réécrit en E14** : le regard n'est plus porté par une expression, c'est un **axe réglé par la posture** (§7) — il va vers ce que les bras font, vers une liste, ou nulle part. **Rien dans le code ne connaît la position de Xavier**, donc aucun regard ne peut le chercher, le suivre ni le tenir. Le personnage ne réagit ni au toucher, ni au doigt, ni au capteur |
| **Aucun symbole** | Pas de goutte de sueur, pas de larme, pas de rougissement, pas de cœur, pas d'étoile, pas d'icône flottante, pas de bulle |

⭐ **Et le panneau s'éteint.** Panneau vide = présence sans visage. C'est l'état des postures `cote-a-cote` et `retrait` (§7) : **zéro trait à lire au moment le plus chargé**, sans avoir à retourner le personnage.

---

## 4. Le trait et la palette

**Ligne claire.** Aucun aplat dégradé, aucune ombre portée, aucune hachure, aucune texture, aucun reflet métallique.

⭐ **La v2 n'a plus d'accent, et donc plus de couleur du tout.** La plaque céladon était le seul endroit coloré du personnage ; le 心 qui la remplace est tracé **à l'encre**. Il reste trois valeurs, et **aucune n'est un signal**.

| Rôle | Valeur | Motif |
|---|---|---|
| Fond | `#F4F1EA` *(papier)* | ❌ **Jamais `#FFFFFF` ni `#000000`** — le contraste maximal est agressif (hypersensibilité visuelle). Le fond est peint par l'écran, pas par le dessin |
| Trait | `#383838` *(encre douce)* | Idem |
| Coque (remplissage) | `#FAF7F0` | À peine détachée du fond |
| Panneau-visage | `#E5DFD4` *(gris chaud)* | Assez pour se distinguer, pas assez pour trancher |
| ❌ Accent | *(supprimé)* | Il n'y a plus une seule couleur dans le personnage |

**Thème sombre :** fond `#14171A`, trait `#D8D4CC`, coque `#1C2024`, panneau `#22262B`.

### L'épaisseur — une déclaration, quatre rendus

⚠️ **La v1 annonçait « épaisseur unique » ; c'est faux de la v2, et il vaut mieux l'écrire que le maintenir.** Le dessin ne déclare que deux valeurs, mais les échelles des pièces en font sortir quatre :

| Où | Déclaré | Rendu | D'où vient l'écart |
|---|---|---|---|
| Corps, ligne du ventre, panneau, pieds, bras | 2 | **2,00** | — |
| Coque de la tête | 2 | **2,61** | La coque est le tracé du panneau **étiré** de 1,28 × 1,33 ; le trait suit, règle SVG |
| 心 | 5 | **2,14** | Il est écrit dans un repère réduit à 0,43 |
| Bouche | 3,5 | **3,38** | Son repère est réduit à 0,97 |

- **Le cerne plus épais de la tête n'est donc pas un choix graphique, c'est une conséquence** — et c'est celle que Xavier a validée en dessinant.
- **Le contour fait ≈ 1,1 % de la hauteur du personnage**, constante à toutes les tailles. *(La v1 annonçait 2,5 %, puis mesurait 1,6 % sur sa planche. Le chiffre suit le dessin, pas l'inverse.)*
- ⭐ **Les terminaisons sont plates partout, sauf le visage, qui est arrondi.** C'est ce qui donne au 心 et à la ligne du ventre leur allure de trait posé plutôt que de tube. *(La v1 disait « terminaisons arrondies » partout — la v2 la contredit, et c'est la v2 qui a raison.)*

---

## 5. La respiration

**Elle et le clignement sont tout ce que le corps fait de lui-même** — le reste du mouvement appartient à la présence (`PRESENCE.md`), pas au corps.

- **Cycle :** 4,5 s inspiration + expiration, sinusoïde continue, aucun temps d'arrêt.
- **Amplitude :** le corps s'étire de **2 %** en hauteur, se rétracte de 1 % en largeur, **autour de sa base**. ⭐ **La ligne du ventre et le 心 sont dessus, donc ils suivent** — c'est le 心 qui monte et descend, et c'est l'ancre visuelle du souffle depuis que la plaque a disparu. **La tête ne bouge pas.**
- 🔴 **Le rythme ne change jamais.** Ni plus vite, ni plus lent, ni en fonction de quoi que ce soit. **Une respiration qui varie devient une information à décoder** — et une information non demandée qui prétend parler de l'état de Xavier est exactement ce que §5.7 interdit.
- 🔴 **Ce n'est pas un guide respiratoire.** Aucun texte n'invite jamais à se caler dessus.
- 🔄 **Clignement — réécrit en E14** *(15/08/2026)* **: il n'agit que sur les yeux**, à cadence aléatoire **2 800 à 6 500 ms**, morphing de **80 ms**. La v2.1 en faisait une **expression entière** (`clignement`, n° 5), donc la bouche se raccourcissait et revenait toutes les 20 à 45 s. ⭐ **Ce tressaillement était le point ouvert §11.6 : il est tranché, et c'est le tressaillement qui tombe.** La bouche garde la forme de l'expression courante ; les deux axes du visage se déforment sur leurs propres horloges. **La borne basse évite le papillonnement, la borne haute évite qu'un clignement devienne un événement**, et deux intervalles consécutifs ne sont jamais égaux — un clignement régulier serait un métronome.
- Transitions entre expressions et entre postures : **≥ 800 ms**, easing continu (invariant §5.6). Aucune apparition instantanée, aucun *cut*.
- ⭐ **Le corps n'a qu'une horloge, et un tour vaut deux respirations.** La lévitation de `PRESENCE.md` s'y branche par un déphasage d'un quart, et le vol du sommeil en divise la phase par deux. 🔴 **Aucun second rythme n'existe** : deux périodes distinctes produiraient un battement lent entre elles, donc une information involontaire.

---

## 6. 🔴 La désignation — l'arbitrage du 13/08/2026

**Xavier a demandé que Kokoro puisse montrer des parties de l'écran. C'est accordé, et la réserve reste écrite.**

**L'objection, qui était réelle et qui n'est pas levée :** *pointer est un geste social.* La v0.1 l'interdisait avec le reste des gestes (faire signe, lever le pouce, applaudir) parce qu'un geste dirigé vers quelqu'un appelle une réponse, et qu'une réponse attendue est une charge de camouflage. **Ce que l'arbitrage retient contre l'objection :** montrer un élément d'interface n'est pas un geste **vers Xavier**, c'est un geste **vers l'écran** — et c'est la fonction **éduquer** de [`companion/README.md` §1](../README.md), qui sans ça reste purement textuelle.

> 🔴 **Quatre garde-fous, aucun optionnel :**
> 1. **Le bras ne dépasse jamais la ligne des épaules.** Un bras levé se lit comme un salut ou une main levée — et le salut est interdit. *(Sur la planche `01`, la pose vers le haut a exactement ce défaut : elle ne fait pas partie du jeu retenu.)* ⭐ **Depuis la v2, la borne se calcule au lieu de se choisir** : le bras pend déjà à 19,5° de la verticale dans le dessin, donc l'horizontale est à **+70,5°**, et pas un degré de plus. `CorpsInvariantsTest` vérifie que la somme fait 90
> 2. **Le bras ne pointe jamais le lecteur** — ni de face, ni vers l'avant. Il désigne un élément **de l'écran**, latéralement ou vers le bas.
> 3. **Aucune main, aucun doigt, aucune paume.** Bout arrondi, membre plein. **Un doigt accuse ; un moignon ne peut pas.**
> 4. ⭐ **Une désignation s'accompagne toujours d'un texte qui dit ce qu'elle désigne.** Xavier ne décode jamais un geste : il le lit écrit à côté. Même règle que les postures (§7).
>
> **Et une frontière :** ❌ **la désignation ne sert jamais à réclamer une action.** Elle montre où une chose se trouve. Elle ne dit jamais *« clique ici »*, *« tu as oublié ça »*, *« regarde ce que tu n'as pas fait »*.

---

## 7. Les dix postures — le jeu est fermé

> 🔄 **Cinq postures sont entrées en E6, E7 et E11** *(15/08/2026)* — `pensif`, `lecture`, `notes`, `accoude`, `sommeil`. **Le compte est dix**, et il a été refait ici : `PRESENCE.md` §5 annonçait **neuf**, c'était une erreur d'arithmétique. `montre` reste **une** posture, quel que soit le côté.
>
> 🔴 **Une posture ne porte aucune information** (`PRESENCE.md` §2). Ne pas la reconnaître ne fait rien perdre : aucune action n'est attendue, aucun état du dossier n'y est encodé. **C'est aussi ce qui interdit d'ajouter un accessoire pour les rendre plus lisibles** — livre, lunettes et calepin n'existent pas.

| # | Nom | Ce qu'on voit | Expression | Quand | Ce que le texte dit à côté |
|---|---|---|---|---|---|
| 1 | **`repos`** | Corps entier, de face, ⭐ **exactement le dessin** — bras écartés de 19,5°, pieds tels que tracés | ⭐ `serein` *(et non plus `neutre`)* | **Par défaut.** Accueil, veille, overlay | Rien. **Seul cas sans texte — parce qu'il ne dit rien** |
| 2 | **`present`** | Idem, immobile | `attentif` | Une étape ou une fiche est ouverte | Le libellé de l'étape |
| 3 | **`montre`** | Un bras tendu latéralement ou vers le bas | `serein` **+ regard latéral** *(l'axe, plus l'expression)* | Une fiche explique où se trouve quelque chose | ⭐ **Ce qu'il montre, écrit.** Jamais une injonction (§6) |
| 4 | ⭐ **`cote-a-cote`** | Corps entier, **panneau éteint** | *(aucune)* | Pendant un exercice, une désensibilisation, un bloc de tension appliquée | « Je reste là pendant que tu fais ça. » **Zéro visage à lire au moment le plus chargé** |
| 5 | **`retrait`** | Réduit à ≈ 40 %, en bord d'écran, **panneau éteint** | *(aucune)* | Mode shutdown : sollicitations coupées | « Je me mets de côté. Rien n'attend. » ❌ **Pas de visage = pas de tristesse lisible.** Ce n'est pas une bouderie, et c'est écrit |
| 6 | 🆕 **`pensif`** | Le dessin, **aucun geste** — seuls les yeux parcourent la liste, lentement | `serein` **+ regard baissé** | Écran de thérapie, avant 18 h | Rien |
| 7 | 🆕 **`lecture`** | Bras avancés vers le bas, balayage des yeux gauche → droite, retour bref | `serein` **+ regard baissé** | Au-dessus d'une liste de fiches | Rien |
| 8 | 🆕 **`notes`** | Un bras en bas, **petits allers-retours intermittents**, yeux baissés vers ce bras | `serein` **+ regard baissé** | Au-dessus du bilan | Rien |
| 9 | 🆕 ⭐ **`accoude`** | **Accoudé sur le bouton comme sur un muret** : les deux bras à l'horizontale reposent sur son arête, le corps passe derrière, la tête dépasse au-dessus et **penche de 6°** | `serein`, **panneau allumé**, regard au centre | **Écran de crise du monde**, et lui seul (§10) | Rien. ⭐ **C'est la seule posture qui n'a rien à côté d'elle et qui dit pourtant quelque chose** : *je suis là* |
| 10 | 🆕 **`sommeil`** | Yeux fermés au repos, lévitation à demi-vitesse, Zzz en fondu | `veille` *(réutilisée, pas une septième)* | Une liste est vide | Le cadre vide, **qui reste affiché** |

⏳ **Une sixième pose reste à dessiner et n'est pas dans la planche `01` : `allonge`** — le robot allongé sur le dos, pieds surélevés, **uniquement** pour l'écran vasovagal. Elle n'est pas décorative : *« allonge-toi, jambes surélevées »* est une consigne à se représenter, et **Xavier est aphantasique** ; une silhouette allongée est une **structure externe** ([règle §9.19](../PLAN.md#22--la-règle-centrale--signal-interne-absent--structure-externe)), pas une consigne à imaginer. **C'est la seule pose du jeu qui n'est pas de face.**

**Aucune autre posture ne s'ajoute sans passer par ce document, par une annonce préalable et par la supervision.**

---

## 8. Ce que le corps ne fait jamais

Miroir corporel de [`companion/README.md` §6](README.md) :

1. 🔄 **Se lever, s'approcher, grandir, marcher vers le lecteur.** Kokoro ne vient jamais vers Xavier — **y compris avec son corps.** ⭐ **Réécrit en E14** : l'interdit est conservé **entier sur l'axe de profondeur** — rien ne grandit vers l'avant, rien ne se rapproche —, et **les entrées et sorties latérales sont admises** depuis E9. Elles ne se dirigent vers personne : il traverse le champ d'un bord à l'autre, à hauteur constante.
2. **Faire signe, lever un bras au-dessus de l'épaule, applaudir, lever le pouce.** Le pouce levé est une **évaluation** ; l'évaluation est interdite. *(La désignation du §6 est la seule exception, et elle est bornée.)*
3. **S'affaisser, baisser la tête, se recroqueviller, laisser tomber les bras.** Aucune posture de découragement, jamais — **y compris après une étape non faite.**
4. 🔴 **Réagir à une étape non faite.** L'expression `chaleureux` ne se déclenche que sur un fait accompli ; **son absence n'est pas un message.** Il n'existe aucune expression « déçu », et il n'en existera jamais.
5. **Fixer le lecteur.**
6. **Changer d'apparence sans annonce** : pas de tenue, pas de saison, pas de variante surprise, pas de thème d'événement. 🔴 **La règle s'applique à la présence elle-même** : le passage de « il ne bouge pas » à « il habite le monde » est un changement d'apparence, **et son annonce est due** *(`PRESENCE.md` §7.2)*.
7. 🔄 **Bouger tout seul — réécrit en E14, et la règle est refaite, pas contournée.** La v2.1 posait qu'il ne se déplace pas à l'écran. Depuis E8 à E11 il **lévite sur place** et **transite** d'un écran à l'autre, sur l'horloge de la respiration et **sur commande du doigt** — jamais de lui-même. Ce qui reste interdit sans changement : **sursauter, suivre le doigt, réagir au toucher, dériver sans qu'une main l'ait déplacé**, et **bouger pendant qu'un texte se lit**. 🔴 **Aucun mouvement continu dans le champ** : tout geste répété est intermittent ou borné.
8. **Se dupliquer, se refléter, se dédoubler** — ⭐ **une seule instance à l'écran, et c'est ce qui impose l'alternance** : quand un panneau plein écran s'ouvre, l'habitant sort du champ **avant** que le locuteur n'entre (`PRESENCE.md` §1.1). Deux moitiés d'une même bascule, vérifiées par un test.

---

## 9. Le découpage et les pivots

⭐ **La demande « facile à animer en 2D » n'est pas une contrainte de confort : elle décide de la façon dont le personnage est dessiné.** **Quinze pièces** — douze de corps, trois de visage — chacune une forme simple, aucune imbrication.

| Pièce *(nom du SVG)* | Pivot | Ce qui l'anime |
|---|---|---|
| `head-out`, `head-in` | ⭐ **Milieu des épaules** — `(120,0 ; 111,1)` | 🔄 **Rien, sauf `accoude`** *(16/08/2026)* : la tête y penche de 6°, **coque, panneau et visage ensemble**. Partout ailleurs elle ne bouge pas — ni au souffle, ni au vol. Le pivot n'est pas choisi : les deux ancres d'épaules sont **le seul point que le dessin désigne entre elles**. `head-in` s'allume et s'éteint |
| `eye-right`, `eye-left`, `mouth` | — | ⭐ **Morphing** : la forme se déforme vers la suivante *(voir ci-dessous)*. **Jamais deux visages superposés** |
| `body-form`, `body-line`, `kanji-1…4` | **Base du corps** — `(120,0 ; 169,4)` | Respiration seule (2 % / 1 %). Les six pièces bougent ensemble |
| `arm-right`, `arm-left` | ⭐ **Épaule** — `(80,6 ; 111,1)` et `(159,3 ; 111,1)` | Désignation (§6), **bornée à +70,5°** |
| `foot-right`, `foot-left` | ⭐ **Centre du ventre** — `(119,8 ; 137,7)`, le même pour les deux | Rien pour l'instant : le pivot existe, aucune posture ne s'en sert |

### ⭐ Le morphing du visage *(14/08/2026)*

⚠️ **La v2 disait « échange de forme, pas de déformation », et le code faisait un fondu croisé : les deux expressions étaient dessinées l'une sur l'autre, en transparence opposée. C'est corrigé, et c'est la version corrigée qui fait foi.** Un fondu montre **deux visages à la fois** pendant 800 ms — quatre yeux, deux bouches, tous à demi effacés. Sur un panneau qui ne porte que trois traits, c'est le moment le plus illisible de l'animation, et il tombe précisément là où Kokoro change ce qu'il dit.

**Ce qui le remplace : la forme se déforme vers la suivante.** L'ovale de l'œil s'écrase en trait quand Kokoro cligne, le trait de la bouche se creuse en sourire. **Il n'y a jamais qu'un seul visage à l'écran**, et il est opaque du début à la fin.

| Ce que ça demande | Comment c'est tenu |
|---|---|
| Savoir **quel point de la première forme va sur quel point de la seconde** | Chaque tracé est réduit à sa **silhouette pleine**, découpée **toujours pareil** : bord haut de gauche à droite, bout droit, bord bas de droite à gauche, bout gauche, **24 points chacun**. Deux tracés quelconques ont donc le même nombre de points, dans le même ordre. ⭐ **La correspondance est construite, pas cherchée** — aucun appariement à l'exécution, donc le même couple de formes donne toujours exactement la même image |
| Que l'œil ovale, qui n'a pas de ligne centrale, entre dans le même moule | Il y entre comme un **point** épaissi de son rayon, puis étiré à la verticale — ce qui redonne **exactement** l'ellipse du SVG, `6,771 × 8,915` |
| 🔴 **Que les commissures ne tombent pas non plus pendant la déformation** | Une forme intermédiaire est une **combinaison convexe** des deux silhouettes : `milieu plus bas que les extrémités` est une inégalité linéaire, donc elle se transporte aux images du milieu. `CorpsInvariantsTest` le vérifie quand même, **sur les seize couples de bouches et sur toute la durée** |
| Que la forme validée par Xavier reste la forme validée par Xavier | ⭐ **La silhouette est un polygone — une approche.** Elle ne sert **que pendant le mouvement** : à l'arrêt, et dès qu'un tracé ne change pas d'une expression à l'autre *(l'œil reste ovale de `neutre` à `attentif`)*, le rendu trace la forme du dessin elle-même |

**Durées inchangées :** 800 ms pour un changement d'expression, 80 ms pour un clignement.

⚠️ **Effet de bord repéré, non traité :** `clignement` est une expression **entière** (§3), bouche comprise. Un clignement déforme donc aussi la bouche, `trait` → `trait court` → `trait`, en 80 ms — un tressaillement toutes les 20 à 45 s. Le fondu le faisait déjà ; le morphing le rend plus lisible. **À trancher à la passe de validation à l'écran (§11).**

### D'où sortent les pivots

Le SVG place les pièces ; il ne dit pas autour de quoi elles tournent. **Les trois pivots sont donc dérivés du dessin, jamais choisis** — et la dérivation est écrite dans `Geometrie.kt` à côté de chaque valeur.

- **L'épaule** est le milieu de la corde du bouchon arrondi qui termine le bras en haut. C'est le seul point autour duquel une rotation ne déforme rien. Les deux valeurs se répondent autour de l'axe **119,959** au millième près, ce qui vaut vérification du calcul.
- 🔴 **Le centre du ventre, lui, est littéralement écrit dans le fichier.** `foot-right` porte `matrix(0.998007 0.063107 -0.063107 0.998007 9.056586 -7.210987)`, qui est exactement une **rotation de +3,618°**. Le point fixe de cette rotation est `(119,78 ; 137,71)` : **en faisant pivoter ce pied autour du ventre, Xavier a laissé le pivot dans la matrice.** Les deux pieds en sont à 44,3 et 45,1 — même rayon, à la main-levée près. *(Le test refait le calcul à chaque build.)*
- **La base du corps** est le bas du tracé du torse : le souffle pousse vers le haut, sous la tête, qui ne bouge pas.

### Ce qui tient le dessin et le code ensemble

- 🔴 **`Geometrie.kt` ne redessine rien.** Chaque pièce y porte le tracé et la matrice **du SVG, caractère pour caractère**, et le placement passe par la matrice, jamais par une réécriture du tracé.
- 🔴 **`CorpsInvariantsTest` relit `retenus/kokoro-corps-v2.svg` à chaque build** et compare tracé, matrice, épaisseur et remplissage des douze pièces de corps, plus les rayons des yeux, la longueur de la bouche et la composition des groupes. **Un chiffre qui bouge d'un côté fait échouer le build.**
- **Aucune pièce n'a de coude ni de genou** — un membre est une forme, une rotation, aucune déformation. ⭐ **Le morphing est réservé au visage** : seuls les trois tracés du panneau se déforment, le corps ne se déforme jamais.
- **Aucune pièce n'en recouvre une autre** : elles se touchent ou flottent. **L'ordre de peinture est celui du SVG** — corps, tête, pieds, bras.
- **Format de production : le SVG lui-même**, une pièce par groupe nommé. Android le transcrit en Kotlin et le trace au vectoriel ; l'app ne dépend d'aucune ressource distante ni d'aucune police externe ([§5.7 point 6](../PLAN.md#57-ce-qui-nentrera-jamais-dans-kokoro)).
- 🔴 **Aucun bitmap du personnage.** ⚠️ **Les images générées par le script ne sont pas des livrables** — ce sont des planches de recherche. **Un PNG de Kokoro sorti d'un modèle n'entre pas dans l'APK.** ⏳ **Portée précisée le 14/08/2026, à confirmer par Xavier** : la règle protégeait l'**animabilité** du personnage — on ne fait pas pivoter un bras dans un PNG — et non une propriété du fichier. **Le décor, qui n'a aucune articulation, est peint** ; l'arbitrage et ce qu'il n'autorise pas sont au [`DECOR.md` §2](./DECOR.md).
- **Aucune bibliothèque d'animation, aucun canvas, aucun runtime tiers** — le tracé vectoriel et les animations de Compose, rien d'autre.

---

## 10. Déclinaisons

> 🔄 **Les deux premières lignes sont réécrites en E14** *(15/08/2026)*. Il n'y a plus d'overlay ni d'en-tête : il y a **deux régimes**, l'habitant et le locuteur, et jamais les deux à la fois (`PRESENCE.md` §1.1). ⚠️ **Les hauteurs se mesurent sur le personnage, pas sur sa vue** — les deux diffèrent de 11 %, et mesurer la vue rendait le contour à 1,8 px, soit **sous le seuil qui sert précisément à écarter les 48 dp**.

| Usage | Taille | Note |
|---|---|---|
| 🆕 **L'habitant** — dans le décor | **60 dp** de personnage, contour rendu ≈ 2,0 px | Une posture, une place par écran. ⚠️ **48 dp est exclu** : le contour vaut 1,1 % de la hauteur, il y tomberait à 1,6 px et le trait se délaverait |
| 🆕 **Le locuteur** — en bas à gauche d'un panneau plein écran | **≈ 110 dp** de personnage, **cadré au thorax**, tête ≈ 60 dp, contour ≈ 3,7 px | Une expression, **immobile hors expression**. Ce qui doit se lire est **un visage** |
| 🔄 **Écran de crise — celui du monde** | ⭐ **`accoude`**, à l'échelle du locuteur | Voir **§10.2** — dérogation arbitrée par Xavier le 16/08/2026 |
| 🔴 **Écran de crise — hors du monde** *(`CriseActivity`)*, tension appliquée, phrase pour le soignant | ❌ **absent** | 🔴 **Aucun personnage, et un test de sources l'interdit.** Ces écrans-là s'imposent par-dessus le verrouillage **quand ça va déjà mal** — un compagnon y serait exactement le bruit que §10 refuse |
| Écran vasovagal | 160 dp | `allonge` uniquement, à côté de la consigne |
| Séance à deux (K6) | ❌ **absent de l'écran de l'aide** | L'aidant lit un déroulé, pas un personnage. **C10** |

> ❌ **La surface web desktop est supprimée le 14/08/2026** *([`companion/README.md` §1](README.md))* — cette table n'a plus qu'une seule surface à décliner. ⭐ **Le rig en pièces séparées ne perd rien à la décision** : il n'a jamais été motivé par le web, mais par les **six expressions** et la **désignation d'un élément de l'écran**, qu'une forme unique et muette ne pouvait pas porter.

### 10.2 ⭐ *« Kokoro veille sur toi »* — la dérogation de l'écran de crise, arbitrée le 16/08/2026

**Xavier a tranché : le personnage entre sur l'écran de crise du monde, et il y montre un visage.** La supervision du 15/08/2026 avait **refusé** la version précédente ; ce qui la faisait tomber est ce qu'elle proposait, pas ce qu'elle demandait.

| | **Ce que E13 proposait** *(refusé)* | ⭐ **Ce que Xavier a arbitré** |
|---|---|---|
| **Le motif** | Aucun. Un plan d'animation, et `PRESENCE.md` §2 déclarant lui-même la pose **sans information** — donc une décoration | *« Kokoro veille sur toi »*. **C'est le motif qui manquait, et seul Xavier pouvait le donner** |
| **Le visage** | **Panneau éteint** — une présence muette | 🔴 **Panneau allumé, `serein`, regard au centre.** *« Un visage bienveillant qui te regarde comme un ami »* |
| **La pose** | *« Affaissé »* — **le mot même que §8 point 3 interdit** | **Accoudé sur le bouton comme sur un muret**, tête un peu penchée |
| **Où** | Ambigu — le §2 disait l'écran du monde, la liste de fichiers disait `CriseActivity` | 🔴 **L'écran du monde, et lui seul** |

> ⭐ **L'arbitrage est plus fin que la recommandation qu'il écarte, et il faut le dire :** la supervision demandait *« les deux portes, sinon `INTERFACE.md` §6.2 tombe »*. Xavier a choisi **une seule**, et le partage retenu est clinique : **la présence se pose sur l'écran qu'on atteint en traversant — calmement —, jamais sur celui qui s'impose par-dessus le verrouillage quand ça va déjà mal.**
>
> ⚠️ **Le contre-argument reste écrit, parce qu'un arbitrage dont on efface le contre-argument n'est plus un arbitrage** : les deux portes ne se ressemblent plus tout à fait. **Ce que §6.2 protégeait est préservé** — les trois boutons sont identiques de place, de taille, de libellé et de couleur, donc **rien de ce qu'on fait ne dépend de la porte où l'on est**. Ce qui diffère est un fond. ⏳ **À vérifier à l'usage, et à défaire si jamais il faut regarder lequel des deux écrans on a sous les yeux.**

🔴 **Les six bornes de la dérogation, et un test tient chacune :**

1. **Cet écran-ci, et lui seul.** `CriseActivity`, la tension appliquée et la phrase pour le soignant n'en portent aucun — **un test de sources refuse tout personnage dans `crise/`**, parce que le risque n'est pas d'en ajouter un exprès : c'est qu'il y arrive par la valeur par défaut d'un paramètre.
2. **Aucun texte ajouté.** Les trois boutons ne bougent ni de place, ni de taille, ni de libellé, ni de couleur. Le bouton *Mot code* lui sert d'**arête**, rien de plus.
3. **Il ne vole pas** — seule place du dispositif dans ce cas. **Il est accoudé, pas posé au sol** : le faire léviter ferait glisser ses bras le long du bord, et **son ombre tomberait sur l'interface**.
4. **Il ne s'endort jamais ici.** Pas de liste, et veiller est ce qu'il y fait.
5. **Rien n'y dépend du dossier** — ni de l'heure, ni du check-in. **Une seule pose, toujours la même.**
6. **La tête penche de 6°, bornée à 10°**, et c'est la seule inclinaison du dispositif (§2).

### 10.1 🔴 L'illustration de la notification — une dérogation, demandée et assumée le 16/08/2026

**Le fond de la notification d'accès à la crise porte une illustration peinte où Kokoro sort de ce document sur quatre points.** Xavier l'a demandée telle quelle après que l'écart lui a été présenté point par point. **Ce n'est donc pas une dérive, c'est une décision — et elle est bornée à cette seule image.**

| Ce qui déroge | Ce que dit ce document | Ce que porte l'illustration |
|---|---|---|
| **L'inclinaison** | §2 — *« Vue de face, aucune perspective. Pas d'axe incliné »* | Le corps penche légèrement |
| **Le clin d'œil** | §3 — le jeu des six est **fermé** ; `clignement` ferme **les deux** yeux | Un œil ouvert, un œil fermé souriant |
| **La main et les doigts** | §2 et §6 garde-fou 3 — *« Aucune main, aucun doigt, aucune paume. Un doigt accuse ; un moignon ne peut pas »* | Une main à deux doigts, en V |
| **Le geste** | §8 point 2 — faire signe, lever un bras au-dessus de l'épaule, **lever le pouce**, sont interdits comme **évaluations** | Un V adressé au lecteur, bras levé |

🔴 **Ce que la dérogation ne fait pas, et ne fera pas :**

1. **Elle n'entre pas dans le rig.** `kokoro-corps-v2.svg`, `Geometrie.kt` et `CorpsInvariantsTest` sont inchangés — **le corps animé de l'application ne connaît ni la main, ni le clin d'œil, ni l'inclinaison.** Les quatre règles ci-dessus restent tenues par la géométrie partout ailleurs.
2. **Elle ne crée pas une septième expression.** §3 le dit : *« aucune septième expression ne s'ajoute sans passer par ce document et par une annonce préalable »*. Le clin d'œil est **peint dans une image**, il n'est pas une pièce du visage, et **rien ne peut le déclencher**.
3. **Elle ne s'étend à aucun écran.** L'illustration vit dans le volet de notifications et **nulle part ailleurs**. 🔄 **Ce point disait « l'écran de crise ne porte toujours aucun personnage » ; Xavier l'a rouvert le 16/08/2026** *(§10.2)*, et **la règle qu'il portait tient toujours là où elle comptait** : ce qui entre sur l'écran du monde **n'entre pas** dans `CriseActivity`, ni dans la tension appliquée, ni dans la phrase pour le soignant. **Ce qui est admis sur une porte ne l'est pas derrière** — et ce n'est plus une phrase, c'est un test de sources.
4. **Elle ne ramène pas de texte.** La notification d'accès a été vidée le 15/08/2026 sur un motif de Xavier — *« je lis toute la journée mot code et tension appliquée »* — et **l'illustration s'y ajoute sans reprendre une seule ligne de corps** : `Kokoro` reste seul. ⭐ **C'est ce qui distingue une image d'une ligne** : elle ne se relit pas. 🔴 **Le jour où une ligne y revient, c'est [`INTERFACE.md` §6.2](INTERFACE.md) qu'on défait.**

> ⚠️ **Ce qui reste ouvert, et qu'il faut regarder à l'usage plutôt que trancher ici** : le V et le clin d'œil sont des **signes sociaux à décoder**, et la règle centrale du dispositif est l'explicite et le littéral. **Une image qu'on ne peut pas rater ne demande rien en retour** — c'est ce qui la distingue d'un geste du personnage animé, et c'est le pari de cette dérogation. **Si l'image finit par se lire comme une attente, elle se retire ; elle ne se discute pas.**

---

## 11. Ce qui reste à trancher

1. **La pose `allonge`** — à dessiner (§7). C'est le seul manque fonctionnel du jeu.
2. ⭐ **Les cinq expressions dérivées** (§3) : Xavier n'a dessiné que `neutre`. Les yeux fermés, les arcs et les bouches courtes sont calculés sur les proportions du dessin — **à regarder à l'écran avant de les considérer acquis**, ou à redessiner si le trait ne va pas.
3. ⭐ **Le personnage est devenu monochrome** — la plaque céladon a disparu avec la v1, le 心 est à l'encre. **Est-ce l'intention, ou une conséquence non voulue ?** Si Xavier veut une couleur, elle n'a plus d'endroit évident où se poser : le 心 lui-même est le seul candidat, et le colorer en ferait un signal à décoder. *(Alternatives si besoin : céladon `#8FA99B`, terre cuite sourde `#B08968`, ardoise `#7C8B99`. Un accent unique, quel qu'il soit — le nombre n'est pas négociable, la teinte oui.)*
4. ⭐ **Les pieds ont un pivot et aucun usage.** Xavier a placé leur centre de rotation dans le ventre ; rien ne s'en sert encore, et **§8 point 7 interdit qu'ils bougent tout seuls**. À décider : quelle posture le mérite — probablement `allonge`.
5. **La posture `retrait`** reste la plus risquée du jeu : à valider en la voyant animée, pas sur le papier.
6. ✅ ~~**Le clignement déforme aussi la bouche**~~ — **tranché le 15/08/2026 (E4)** : le clignement n'agit plus que sur **les yeux**, la bouche garde la forme de l'expression courante, et la cadence passe à 2 800–6 500 ms. **Le tressaillement était bien un tressaillement.** Détail au §5.
7. ✅ ~~**L'écran de crise**~~ — **arbitré par Xavier le 16/08/2026** *(§10.2)* : `accoude` remplace `attente`, **panneau allumé**, sur l'écran de crise **du monde** uniquement. ⏳ **Ce qui reste à juger à l'écran, et qui ne se tranche pas sur le papier** : les bras exactement à l'horizontale sur l'arête du bouton *(le §6 garde-fou 1 est **atteint**, pas dépassé — et symétriquement, un seul bras à cette hauteur serait un salut)*, l'inclinaison de 6°, et **si le regard au centre se lit comme bienveillant ou comme fixe.**

---

## 12. Le pipeline d'images

> ⚠️ **Il n'a pas produit le corps retenu.** La v2 est dessinée à la main par Xavier. Ce qui suit sert encore aux **recherches** — une pose à explorer, une variante à voir — mais **plus rien de ce qu'il sort n'entre dans le personnage sans passer par un tracé vectoriel**.

```bash
npm run companion:image -- robot --n=4 --format=4:3          # 4 candidats + une planche contact
npm run companion:image -- robot --ref=robot/01-b.png        # itère à partir du candidat retenu
```

- **La charte vit dans [`prompts/_base.md`](./ressources/prompts/_base.md)**, écrite une fois et préfixée à chaque appel : morphologie, palette, six expressions, règles de visage, désignation, interdits. **Itérer = modifier trois lignes du prompt de variante, pas réécrire la charte.**
- [`prompts/robot.md`](./ressources/prompts/robot.md) ne décrit plus que **la mise en page de la planche**. Une pose isolée se demande dans un nouveau fichier de variante (ex. `allonge.md`).
- Chaque exécution écrit `sorties/<variante>/NN-a.png`, `NN-b.png`… **plus `NN-planche.png`** — une seule image qui contient tous les candidats étiquetés, **pour n'avoir à en lire qu'une**. `NN.md` conserve le prompt exact, le modèle et les références.
- `sorties/` n'est **pas** versionné *(`.gitignore`)*. **Ce qui est retenu est promu à la main dans [`retenus/`](./ressources/retenus/), qui l'est.**
