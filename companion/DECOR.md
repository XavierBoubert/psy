# Kokoro — le décor

**Spécification graphique du monde.** ⭐ **v1 — 14/08/2026.** Le décor est un **paysage peint en quatre couches**, glissé en parallaxe sous cinq écrans disposés en croix.

> 📐 **Ce document ne décide rien de clinique.** Il décrit le monde dans lequel le personnage est posé. Le personnage, lui, est spécifié dans [`CORPS.md`](./CORPS.md) et la doctrine dans [`../../../PLAN.md` §5](../PLAN.md#5-kokoro--le-compagnon).
>
> ⏱️ **Il s'applique au jalon K7 (« la présence »), comme `CORPS.md`.** K5 et K6 passent d'abord. Ce qui existe aujourd'hui est le monde vide : le décor, la navigation, et Kokoro au centre.

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
npm run image -- decor-feuillage --base=_decor --format=16:9 --taille=2K --n=2

# 2. détourer le candidat retenu vers l'APK
npm run decoupe -- decor-feuillage/01-b.png \
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

La caméra est en **écrans** : `(0,0)` au centre, `(-1,0)` à gauche, `(0,1)` en bas. Une couche de profondeur *p* se déplace de *p* × un écran ; le contenu est à 1, donc il colle au doigt.

| Point | Ce qui est fait, et pourquoi |
|---|---|
| ⭐ **Répétition en miroir** | Une tuile sur deux est retournée horizontalement. **Deux tuiles voisines se touchent par le même bord : il n'y a pas de raccord à faire coïncider, il n'y a pas de raccord du tout.** C'est ce qui permet de glisser indéfiniment sans trouver la fin du dessin |
| ⭐ **Débattement vertical court** (0,10 contre 1 écran à l'horizontale) | Latéralement la tuile se répète ; verticalement, non — le ciel est en haut et le sol en bas, les échanger n'a aucun sens. Le mouvement vertical dit la profondeur sans défaire la composition |
| **Décalage des couches basses** | Une couche ancrée en bas qui remonte de plus que son décalage **découvre le ciel sous elle**. En la posant un peu plus bas que le bord, la montée reste dans ce qu'on a déjà donné. ⭐ **Le seuil se calcule** : une couche remonte au plus `0,10 × profondeur` d'écran, soit 0,052 pour la prairie et 0,078 pour le feuillage ; les deux décalages sont au-dessus (0,055 et 0,080), donc **la tranche prolongée ne sert plus jamais**. Elle reste en dernier recours : chaque colonne se continue par sa propre couleur, donc sans raccord |
| **Placement de Kokoro** | Biais vertical 0,62 : **les feuilles du premier plan lui passent devant les pieds.** Sans ce recouvrement il flotte au-dessus du décor, et le parallaxe perd ce qu'il venait chercher |

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

## 6. La navigation — une croix, pas une grille

```
        HAUT
GAUCHE  CENTRE  DROITE
        BAS
```

⭐ **Du centre on va aux quatre bords ; d'un bord on ne peut que revenir au centre.** Aucune diagonale, donc **aucun écran n'est à deux gestes**, et depuis n'importe où le centre est à un seul geste. Il n'y a rien à mémoriser d'autre que « on revient toujours ». `EcranTest` le vérifie.

| Règle | Motif |
|---|---|
| **Le décor suit le doigt** pendant le geste, au lieu d'attendre qu'il se lève | Le geste montre son effet pendant qu'on le fait : rien à apprendre, rien à deviner, et quatre écrans deviennent découvrables **sans rien afficher pour les annoncer** |
| ⭐ **L'axe se verrouille au premier mouvement** et ne se relâche qu'au lever du doigt | Sans verrou, un geste un peu oblique — et ils le sont tous — ferait hésiter le monde entre deux écrans. Ce serait le seul endroit du dispositif où le résultat dépendrait de la précision du geste |
| 🔴 **Butée franche, sans élastique** | Quand il n'y a pas de voisin, rien ne bouge, plutôt que de céder puis revenir. Un mouvement qui part et se rétracte demande à être interprété (« est-ce que ça a marché ? ») |
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
| 🔴 **Bouger tout seul.** Aucune dérive, aucun nuage qui file, aucune animation d'ambiance | Hors la respiration de Kokoro, **rien ne se déplace sans que le doigt le déplace** — jamais de mouvement à interpréter (`PLAN.md` §5.6) |
| 🔴 **Changer avec le thème sombre du système** | Il changerait d'apparence selon un réglage étranger à la thérapie, sans que Xavier l'ait décidé. **La plage horaire du §5 est l'inverse** : fixée d'avance, visible dans les réglages, et coupée d'un geste |
| 🔴 **Basculer sous les yeux** | L'heure est lue **à l'arrivée dans le monde**, puis tenue tant qu'il est ouvert. Voir le décor virer tout seul à 21 h serait exactement le mouvement à interpréter que le dispositif ne provoque jamais |
| **Porter du texte, un chiffre, une pastille, un indicateur de page** | Kokoro ne vient jamais vers Xavier, et un décor qui informe est une interpellation |
| **Un être vivant** — animal, oiseau, insecte, visage | Un regard dans le décor est une présence de plus à décoder |
| **Météo, nuit, flétrissure, épine, ombre portée dure** | Zéro valence morale dans l'environnement, comme dans l'expression |
| **Un son, une vibration, un flash, une saturation vive** | Hypersensibilités quatre canaux |

---

## 8. Ce qui reste ouvert

- ⏳ **L'arbitrage du §2** attend la confirmation de Xavier.
- ⏳ **Le contenu des quatre écrans du bord** — programme, bibliothèque, bilans, crise. Il se décide en séance (`PLAN.md` §8), pas ici.
- ⏳ **L'axe de symétrie des tuiles en miroir**, sur le feuillage. ⚠️ **Le rétrécir à 1,50 l'a rapproché** : il se croise désormais une fois par écran traversé au lieu d'une fois toutes les 1,4. La prairie, elle, l'a beaucoup éloigné. Le supprimer demanderait un dessin bouclé, que le modèle ne sait pas produire de façon fiable. **À regarder à l'écran** — si c'est gênant, la sortie est d'élargir le feuillage et de compenser l'échelle en régénérant une planche aux feuilles plus petites.
- ⏳ **Le débattement vertical est modeste** (0,10). Monter à 0,16 rend le haut et le bas plus distincts ; c'est maintenant abordable, les deux décalages du bas ayant de la marge — mais il faudrait les remonter d'autant. À regarder à l'écran, pas à trancher sur le papier.
- ⏳ **La palette de nuit est un premier jet** : ciel `#08202E → #1A4A63`, teinte `#4C7691` multipliée. Elle tient sur les planches actuelles ; elle n'a pas été regardée longtemps.
