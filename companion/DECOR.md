# Kokoro — le décor

**Spécification graphique du monde.** ⭐ **v1 — 14/08/2026.** Le décor est un **paysage peint en quatre couches**, glissé en parallaxe sous **quatre écrans en anneau horizontal** *(rangement refait le 15/08/2026 — [`INTERFACE.md`](INTERFACE.md) §7.7)*.

> 📐 **Ce document ne décide rien de clinique.** Il décrit le monde dans lequel le personnage est posé. Le personnage, lui, est spécifié dans [`CORPS.md`](./CORPS.md) et la doctrine dans [`README.md`](README.md).
>
> ⏱️ **Il s'applique au jalon K7 (« la présence »), comme `CORPS.md`.** K5 et K6 passent d'abord. Ce qui existe aujourd'hui est le monde presque vide : le décor, la navigation, et Kokoro **en tête de l'écran d'entrée** *(§6)*.

---

## 1. Ce qui fait foi

🔴 **Les quatre fichiers détourés font foi** — `app/src/main/res/drawable-nodpi/decor_*.webp`. Ce sont eux que l'application dessine.

Les **planches magenta d'origine** sont versionnées dans [`retenus/decor/`](./ressources/retenus/decor/) : elles permettent de **re-détourer** sans rien régénérer, donc de revenir sur un seuil sans rejouer le modèle. Les prompts qui les ont produites sont dans [`prompts/`](./ressources/prompts/) — `_decor.md` (la charte) et `decor-*.md` (une couche par fichier).

| Couche | Fichier | Profondeur | Ancrage | Largeur |
|---|---|---|---|---|
| Nuages lointains | `decor_nuages_loin.webp` | 0,14 | haut | 1,40 |
| Nuages proches | `decor_nuages_pres.webp` | 0,30 | haut | 1,90 |
| Prairie du fond | `decor_collines.webp` | 0,52 | bas | **3,60** |
| Feuillage de premier plan | `decor_feuillage.webp` | 0,78 | bas | **1,50** |

⭐ **La largeur, en écrans, est aussi l'échelle** *(revue le 14/08/2026)* : une tuile plus large agrandit ce qu'elle contient. La prairie du fond est passée de 1,60 à **3,60** — sous 2,50 elle se lisait comme une bande au ras du bord et non comme une plaine — et le feuillage de 2,10 à **1,50**, parce qu'au premier plan des feuilles trop grandes mangent l'écran. **Le prix est écrit au §8** : une tuile étroite se répète plus souvent.

**Le ciel n'est pas une image** : c'est un dégradé vertical peint sous la pile. Il ne coûte rien à décliner, il ne pixellise jamais, et il évite d'embarquer un aplat de 2 Mo.

---

## 2. 🔴 L'arbitrage à confirmer : un bitmap entre dans l'APK

⚠️ **`CORPS.md` §9 écrit : « Aucun bitmap du personnage. Un PNG sorti d'un modèle n'entre pas dans l'APK. »** Le décor y déroge, et c'est un arbitrage neuf, pas une continuation.

| | Le personnage | Le décor |
|---|---|---|
| **Auteur** | ⭐ **Xavier**, au trait, dans un éditeur vectoriel | Un modèle d'image |
| **Format** | SVG, transcrit en Kotlin pièce par pièce | 4 WebP avec alpha, **≈ 310 ko au total** |
| **Ce qui l'anime** | Un rig — quinze pièces, trois pivots | Rien. Il glisse, il ne se déforme pas |
| **Ce qu'un bitmap coûterait** | 🔴 **Le rig entier** : on ne fait pas pivoter un bras dans un PNG | Rien : une couche n'a aucune articulation |

⭐ **La règle « aucun bitmap » protégeait l'animabilité du personnage, pas une propriété du fichier.** Un décor qui ne bouge pas d'articulation ne perd rien à être peint. Redessiner au vectoriel des nuages gouachés et des feuilles peintes coûterait très cher pour un résultat plus pauvre que l'image de référence qu'a apportée Xavier.

🔴 **Ce que ça n'autorise pas :** aucun bitmap ne représente **Kokoro**, ni une partie de lui, ni un visage, ni une pose. **Le personnage reste vectoriel, sans exception** — `CorpsInvariantsTest` continue de relire le SVG à chaque build.

> ⏳ **À confirmer par Xavier.** Tant que ce n'est pas fait, `CORPS.md` §9 et ce document se contredisent sur la lettre.

---

## 3. Le pipeline — du prompt au fichier

```bash
# 1. générer des candidats (charte du décor, pas celle du personnage)
npm run companion:image -- decor-feuillage --base=_decor --format=16:9 --taille=2K --n=2

# 2. détourer le candidat retenu vers l'APK
npm run companion:decoupe -- decor-feuillage/01-b.png \
  companion/android/app/src/main/res/drawable-nodpi/decor_feuillage.webp \
  --seuil=0.72 --plein=0.30
```

### ⭐ Pourquoi le fond est magenta et pas transparent

**Le modèle ne rend pas d'alpha.** Il renvoie du JPEG, et à qui lui demande « fond transparent » il **peint un damier gris et blanc** — la représentation visuelle de la transparence, prise au pied de la lettre. Deux essais l'ont montré le 14/08/2026.

**La charte lui fait donc peindre un aplat `#FF00FF`**, et le script en fait l'alpha :

| Étape | Ce qu'elle fait |
|---|---|
| **Mesure** | `magenta = min(R,B) − V`. Le fond sature le rouge et le bleu et n'a **aucun** vert ; aucune couleur de la charte ne fait ça. Un bord fondu à moitié mesure la moitié |
| **Alpha** | Rampe entre `--plein` (opaque en deçà) et `--seuil` (transparent au-delà). Le seuil est **serré à 0,72** : au-delà, ce sont les artefacts de compression du JPEG, qui laissaient sinon des poussières roses dans le ciel |
| **Démultiplication** | La couleur observée est un mélange sujet + fond. On retire le fond au prorata de l'alpha pour retrouver la teinte réelle |
| ⭐ **Frange** | Ce qui reste est un contour légèrement lilas. **Il se distingue d'un vrai rose par sa symétrie** — le magenta a exactement autant de rouge que de bleu, un pétale n'en a jamais autant. C'est ce test qui neutralise l'un **sans décolorer l'autre** |

---

## 4. Le parallaxe

La caméra est en **écrans**, et **sur le seul axe horizontal** *(15/08/2026 — [`INTERFACE.md`](INTERFACE.md) §7.7)* : `0` sur l'écran d'entrée, `1` sur son voisin de droite, `-1` sur celui de gauche. Une couche de profondeur *p* se déplace de *p* × un écran ; le contenu est à 1, donc il colle au doigt.

🔴 **Elle n'est bornée d'aucun côté**, et c'est ce qui rend l'anneau gratuit : pour le décor, revenir sur le premier écran n'est **qu'un écran de plus dans le même sens**.

| Point | Ce qui est fait, et pourquoi |
|---|---|
| ⭐ **Répétition en miroir** | Une tuile sur deux est retournée horizontalement. **Deux tuiles voisines se touchent par le même bord : il n'y a pas de raccord à faire coïncider, il n'y a pas de raccord du tout.** C'est ce qui permet de glisser indéfiniment sans trouver la fin du dessin |
| 🔴 **Aucun débattement vertical** *(15/08/2026)* | Il y en avait un, court (0,10), qui disait la profondeur quand la traversée était une croix. **La caméra n'a plus de composante verticale** — et un décor qui bougerait avec une liste qui défile lui donnerait une profondeur qu'il n'a pas. ⭐ **La tranche prolongée disparaît avec lui** : plus rien ne remonte, donc rien ne découvre le ciel |
| **Décalage des couches basses** | 🔴 **Il ne descend jamais sous zéro** : une couche ancrée en bas doit garder son pied hors champ. Il ne paie plus une montée — il n'y en a plus — il cale la composition |
| **Placement de Kokoro** | ⏳ **Rouvert par §7.7** : l'écran central où il se tenait n'existe plus, et il est provisoirement en tête de la liste de la thérapie. **Le biais 0,62 qui le faisait recouvrir par le feuillage attend son propre brainstorm** |

---

## 5. ⭐ La nuit — une plage horaire, et rien d'autre *(14/08/2026)*

**Par défaut : 21 h → 6 h.** Les deux bornes se règlent, et l'ensemble **se coupe d'un geste**, dans l'écran de contrôle, section « La nuit du décor ».

| | |
|---|---|
| **Ce qui la déclenche** | L'heure, comparée à deux bornes que Xavier a posées lui-même |
| **Ce qui ne la déclenche jamais** | 🔴 Le thème sombre du système · la luminosité ambiante · l'usage · l'humeur relevée au check-in · quoi que ce soit qui viendrait du dispositif |
| **Quand elle est lue** | ⭐ **À l'arrivée dans le monde, et là seulement** |
| **Ce qui change** | Le ciel et une teinte multipliée sur les quatre couches. **Un seul jeu d'images** : deux jeux dériveraient l'un de l'autre à la première retouche |
| **Ce qui ne change pas** | 🔴 **Kokoro.** Il garde les couleurs du SVG, jour et nuit — lui donner une seconde apparence serait une chose de plus à décoder |

**Pourquoi ce n'est pas un changement d'apparence non annoncé.** L'invariant interdit la surprise, pas la prévision : un décor qui suit le thème d'Android change parce qu'un réglage étranger a bougé, alors qu'une plage horaire est **écrite, visible et désactivable avant de servir**. La prévisibilité est ici une fonctionnalité, exactement comme au reste du dispositif.

**Deux garde-fous s'ajoutent à la case « désactiver » :** un réglage où les deux bornes sont égales est traité comme **vide** — donc jour — et non comme plein ; et une heure qui ne se lit pas **ne s'enregistre pas**, plutôt que d'être corrigée en silence.

---

## 6. La navigation — **un anneau horizontal, sans bout** *(15/08/2026)*

```
  ← … │ Thérapie │ Documentation │ Bilan │ Crise │ Thérapie │ … →
        entrée                                      (le même)
```

⭐ **Une seule direction, quatre écrans, aucun bout.** Après le dernier vient le premier, **et le décor continue dans le même sens** : ce n'est pas un retour en arrière, c'est un tour de plus. La crise est donc à **un seul geste de l'entrée**, dans le sens qu'on veut. `EcranTest` le vérifie.

> 🔄 **C'était une croix jusqu'au 15/08/2026** — centre, haut, bas, gauche, droite. Voir [`INTERFACE.md`](INTERFACE.md) §7.7 pour ce que le changement coûte et ce qu'il rend.

| Règle | Motif |
|---|---|
| **Le décor suit le doigt** pendant le geste, au lieu d'attendre qu'il se lève | Le geste montre son effet pendant qu'on le fait : rien à apprendre, rien à deviner, et quatre écrans deviennent découvrables **sans rien afficher pour les annoncer** |
| ⭐ **Il n'y a plus d'axe à verrouiller** *(15/08/2026)* | Le monde n'écoute que le glissement **horizontal** ; le vertical ne lui parvient jamais et va à la liste de l'écran. **Deux gestes, deux destinataires, aucun arbitrage** — et plus aucun geste oblique dont le résultat dépendrait de sa précision |
| ✅ **La butée franche disparaît sans rien laisser derrière** | Elle existait pour ne pas avoir d'élastique aux bords du monde. **Il n'y a plus de bord** : rien ne part et ne se rétracte, donc rien ne demande à être interprété |
| ⭐ **On ne saute jamais deux écrans**, si lancé soit le geste | Un monde qui défile de trois écrans sur un coup de pouce demanderait de retrouver où l'on est ; d'un écran, on le sait sans regarder |
| **Deux façons d'arriver au bout** : la distance (18 % d'un écran) **ou l'élan** (0,7 écran/s) | Un geste franc mais bref doit suffire. ⭐ **Sur la distance seule, un geste vif échouait** : le doigt part plus vite qu'il ne va loin, et le monde revenait en arrière alors que le geste était sans ambiguïté |
| ⭐ **Un ressort, pas une durée fixe** *(14/08/2026)* | Une interpolation à durée fixe redémarre à vitesse nulle : **le monde s'arrêtait une fraction de seconde au lever du doigt**, puis repartait. Le ressort reprend la caméra **à la vitesse qu'elle avait** — il n'y a plus deux mouvements séparés par un arrêt, mais un seul, celui du doigt, prolongé |
| **Amorti critique, ~600 ms** | Il rejoint sa cible **sans jamais osciller** — un rebond serait un mouvement à interpréter. Même tempo que les transitions du corps (`CORPS.md` §5) |

### ⭐ Pourquoi la caméra n'est pas une animation

La caméra est une **valeur ordinaire**, écrite directement par le doigt ; l'animation ne sert qu'à la poser après le lever. La faire piloter par une animation pendant le geste coûtait **une image de retard par doigt posé** : chaque déplacement partait faire un aller-retour par le moteur d'animation avant d'atteindre l'écran. C'était la moitié de la saccade ; le démarrage à vitesse nulle était l'autre.

**Le geste repart aussi d'où la caméra est**, et non du centre de l'écran visé : reprendre le monde pendant qu'il se pose ne le fait donc jamais sauter. Un simple appui, lui, ne déclenche rien — il n'y a de geste qu'au-delà du seuil de glissement d'Android.

---

## 7. Ce que le décor ne fait jamais

| Interdit | Origine |
|---|---|
| 🔴 **Bouger tout seul.** Aucune dérive, aucun nuage qui file, aucune animation d'ambiance | Hors la respiration de Kokoro, **rien ne se déplace sans que le doigt le déplace** — jamais de mouvement à interpréter (`README.md` §5) |
| 🔴 **Changer avec le thème sombre du système** | Il changerait d'apparence selon un réglage étranger à la thérapie, sans que Xavier l'ait décidé. **La plage horaire du §5 est l'inverse** : fixée d'avance, visible dans les réglages, et coupée d'un geste |
| 🔴 **Basculer sous les yeux** | L'heure est lue **à l'arrivée dans le monde**, puis tenue tant qu'il est ouvert. Voir le décor virer tout seul à 21 h serait exactement le mouvement à interpréter que le dispositif ne provoque jamais |
| **Porter du texte, un chiffre, une pastille, un indicateur de page** | Kokoro ne vient jamais vers Xavier, et un décor qui informe est une interpellation |
| **Un être vivant** — animal, oiseau, insecte, visage | Un regard dans le décor est une présence de plus à décoder |
| **Météo, nuit, flétrissure, épine, ombre portée dure** | Zéro valence morale dans l'environnement, comme dans l'expression |
| **Un son, une vibration, un flash, une saturation vive** | Hypersensibilités quatre canaux |

---

## 8. Ce qui reste ouvert

- ⏳ **L'arbitrage du §2** attend la confirmation de Xavier.
- ⏳ **Le contenu des quatre écrans** — programme, bibliothèque, bilans, crise. Il se décide en séance (`companion/PROGRAMME.md`), pas ici.
- ⏳ **L'axe de symétrie des tuiles en miroir**, sur le feuillage. ⚠️ **Le rétrécir à 1,50 l'a rapproché** : il se croise désormais une fois par écran traversé au lieu d'une fois toutes les 1,4. La prairie, elle, l'a beaucoup éloigné. Le supprimer demanderait un dessin bouclé, que le modèle ne sait pas produire de façon fiable. **À regarder à l'écran** — si c'est gênant, la sortie est d'élargir le feuillage et de compenser l'échelle en régénérant une planche aux feuilles plus petites.
- ⏳ **La palette de nuit est un premier jet** : ciel `#08202E → #1A4A63`, teinte `#4C7691` multipliée. Elle tient sur les planches actuelles ; elle n'a pas été regardée longtemps.
