# Kokoro — le décor

**Spécification graphique du monde.** Le décor est un **paysage peint en quatre couches**, glissé en parallaxe sous **quatre écrans en anneau horizontal** *(rangement — [`INTERFACE.md`](INTERFACE.md) §7.7)*.

> 📐 **Ce document ne décide rien de clinique.** Il décrit le monde dans lequel le personnage est posé. Le personnage, lui, est spécifié dans [`CORPS.md`](./CORPS.md) et la doctrine dans [`README.md`](README.md).
>
> ⏱️ **Il s'applique au jalon K7 (« la présence »), comme `CORPS.md`.** K5 et K6 passent d'abord. Ce qui existe aujourd'hui est le monde presque vide : le décor, la navigation, et Kokoro **en tête de l'écran d'entrée** *(§6)*.

---

## 1. Ce qui fait foi

🔴 **Les quatre fichiers détourés font foi** — `app/src/main/res/drawable-nodpi/decor_*.webp`. Ce sont eux que l'application dessine.

Les **planches magenta d'origine** sont versionnées dans [`retenus/decor/`](./ressources/retenus/decor/) : elles permettent de **re-détourer** sans rien régénérer, donc de revenir sur un seuil sans rejouer le modèle. Les prompts qui les ont produites sont dans [`prompts/`](./ressources/prompts/) — `_decor.md` (la charte) et `decor-*.md` (une couche par fichier).

| Couche | Fichier | Profondeur | Ancrage | Largeur | Répétition |
|---|---|---|---|---|---|
| Nuages lointains | `decor_nuages_loin.webp` | 0,14 | haut | 1,40 | miroir ⏳ |
| Nuages proches | `decor_nuages_pres.webp` | 0,30 | haut | **2,40** | ⭐ **simple**, marge 0,16 |
| Prairie du fond | `decor_collines.webp` | 0,52 | bas | **3,60** | 🔴 miroir *(couche de sol)* |
| Feuillage de premier plan | `decor_feuillage.webp` | 0,78 | bas | **1,90** | ⭐ **simple**, marge 0,16 |

⭐ **La largeur, en écrans, est aussi l'échelle** *(revue le 14/08/2026)* : une tuile plus large agrandit ce qu'elle contient. La prairie du fond est passée de 1,60 à **3,60** — sous 2,50 elle se lisait comme une bande au ras du bord et non comme une plaine.

🔄 **Les deux couches sans miroir ont été redessinées en 21:9 et élargies** *(15/08/2026, §4.3)* : nuages proches 1,90 → **2,40**, feuillage 1,50 → **1,90**. ⭐ **L'échelle du dessin n'a pas bougé pour autant** — la planche est plus large *et* la tuile est plus large, dans le même rapport.

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
npm run companion:image -- decor-feuillage --base=_decor --format=21:9 --taille=2K --n=2

# 2. détourer le candidat retenu vers l'APK
npm run companion:decoupe -- decor-feuillage/02-b.png \
  companion/android/app/src/main/res/drawable-nodpi/decor_feuillage.webp \
  --seuil=0.72 --plein=0.30 --marge=0.04
```

⭐ **`--format=21:9` depuis le 15/08/2026** pour les couches sans miroir : plus la planche est large, plus la répétition est rare. *(Le script ne validait pas le format ; la ligne d'aide, elle, ne citait que 16:9.)*

🔴 **`--marge` n'est pas un cadrage, c'est un garde-fou.** La charte demande aux couches sans miroir des marges latérales vides ; le modèle les respecte **à peu près**, et il laisse régulièrement un éclat de nuage collé à un bord — qui réapparaîtrait au milieu du ciel à chaque tuile. Effacer 4 % de chaque côté garantit ce que le modèle promet. ⚠️ **Jamais sur la prairie** : elle y perdrait son pied.

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
| 🔄 **La répétition est devenue une propriété de la couche** *(15/08/2026 — §4.3)* | **Une tuile à marges vides se répète simplement**, en avançant de sa seule partie peinte : les marges se recouvrent, le dessin reprend là où il s'arrête, **aucun axe de symétrie**. **Une tuile bord à bord se répète en miroir** : pas de raccord à faire coïncider, mais elle paie sa continuité en symétrie. Dans les deux cas, on glisse indéfiniment sans trouver la fin du dessin |
| 🔴 **Aucun débattement vertical** *(15/08/2026)* | Il y en avait un, court (0,10), qui disait la profondeur quand la traversée était une croix. **La caméra n'a plus de composante verticale** — et un décor qui bougerait avec une liste qui défile lui donnerait une profondeur qu'il n'a pas. ⭐ **La tranche prolongée disparaît avec lui** : plus rien ne remonte, donc rien ne découvre le ciel |
| **Décalage des couches basses** | 🔴 **Il ne descend jamais sous zéro** : une couche ancrée en bas doit garder son pied hors champ. Il ne paie plus une montée — il n'y en a plus — il cale la composition |
| **Placement de Kokoro** | ⏳ **Rouvert par §7.7** : l'écran central où il se tenait n'existe plus, et il est provisoirement en tête de la liste de la thérapie. **Le biais 0,62 qui le faisait recouvrir par le feuillage attend son propre brainstorm** |
| ⭐ **L'inclinaison du téléphone s'ajoute au doigt** *(15/08/2026)* | Deux mains, une seule caméra de décor — **et elle ne touche que le décor** : le contenu des écrans reste à sa place. Voir §4.1 |

### 4.1 ⭐ L'inclinaison du téléphone — *une position, pas un mouvement* *(15/08/2026, demande de Xavier)*

**Bouger le téléphone déplace le décor**, en plus du glissement gauche ↔ droite qui, lui, ne change pas d'une virgule. `decor/Inclinaison.kt`, `decor/CapteurInclinaison.kt`.

⚠️ **C'est une dérogation au §7, et elle s'écrit** : le décor bouge désormais sans que le doigt le déplace. **Ce qui la rend acceptable est entièrement dans la grandeur mesurée.**

| | |
|---|---|
| **Ce qui est lu** | ⭐ **La direction du bas** — `TYPE_GRAVITY`, repli sur l'accéléromètre. 🔴 **Pas le gyroscope**, dont la grandeur est une vitesse de rotation |
| **Pourquoi pas le gyroscope** | Une vitesse s'intègre pour donner un angle, et **une intégration dérive** : le décor glisserait tout seul, téléphone posé — exactement l'interdit du §7 — et il faudrait un recentrage périodique, c'est-à-dire un second mouvement que personne n'a demandé |
| ⭐ **Ce que la position donne en échange** | **Aucune dérive · aucun recentrage · réversibilité exacte.** Reposer le téléphone comme il était remet le décor où il était, au pixel près. **Le décor est une fonction de la main, pas une mémoire du geste** |
| **Le sens** | ⭐ **Une fenêtre, pas un niveau à bulle** : pencher le bord droit vers le bas découvre ce qui est à droite — le même sens que le doigt qui pousse le monde vers la gauche |
| **La course** | **±18°**, puis butée. 🔴 **Une butée est indispensable ici** : le doigt a un bout — il se lève — une position n'en a pas, et un téléphone retourné emmènerait le décor à l'infini. 🔄 **26° → 18° le 15/08/2026** *(§4.2)* |
| **Le débattement** | **0,40 écran de caméra** en bout de course : le feuillage se déplace de 31 % de la largeur de l'écran, les nuages lointains de 5,6 %. 🔄 **0,18 → 0,40 le 15/08/2026** *(§4.2)*. ⭐ **Ça ne peut pas faire changer d'écran, et pas parce que le nombre est petit** : l'inclinaison n'entre jamais dans la caméra du contenu — **elle n'a aucun chemin vers la traversée** |
| **Le lissage** | Constante de temps ≈ ⅓ s. **Ce n'est pas un confort, c'est l'invariant d'hypersensibilité** : la main tremble, et un décor collé à la mesure brute vibrerait en permanence |
| **L'axe** | **Horizontal seul**, comme le doigt. Un débattement vertical découvrirait le ciel sous les couches basses — c'est ce que le §4 a retiré le 15/08/2026 |
| 🔴 **Le capteur** | Branché **seulement** tant que le monde est à l'écran **et** que le réglage le demande. **Rien n'est enregistré ni transmis** : la valeur vit le temps d'une image, et l'app n'a pas la permission INTERNET |

**Les deux interrupteurs, écran de contrôle → « Le mouvement du décor » :**

| Réglage | Ce qu'il coupe | Défaut |
|---|---|---|
| **Décor en parallaxe** | **Tout** mouvement du décor. Il devient une image fixe derrière des écrans qui glissent — 🔴 **figée à zéro, pas là où elle était** : une image fixe doit être la même à chaque venue | activé |
| **Suivre l'inclinaison** | La seule part venue de la main qui tient le téléphone. Le décor continue de suivre le doigt | activé |

⭐ **Deux interrupteurs et pas un**, parce que ce sont deux gênes différentes : un seul aurait obligé à perdre la traversée pour se débarrasser de l'inclinaison. 🔴 **La ligne de l'inclinaison disparaît** quand la parallaxe est coupée — elle ne ferait rien — **et quand le téléphone n'a pas le capteur**, où une phrase dit pourquoi. *Un interrupteur inerte est un réglage qu'on croit posé.*

### 4.2 🔄 Le réglage vu en main — *le déplacement était trop court* *(15/08/2026, retour de Xavier)*

**Verdict après essai : le glissement au doigt est juste, l'inclinaison ne se voit pas assez.** Deux constantes ont bougé, et **il fallait les deux** :

| | Avant | Après | Pourquoi |
|---|---|---|---|
| **La course** | 26° | **18°** | 🔴 **La moitié du problème, et la moins visible.** Un mouvement de poignet ordinaire vaut **huit à dix degrés** : la course n'était jamais parcourue, on n'en voyait qu'un tiers. **Augmenter le seul débattement n'aurait rallongé qu'un terrain qu'on n'atteignait pas** |
| **Le débattement** | 0,18 écran | **0,40 écran** | Le feuillage passe de 14 % à **31 %** de la largeur de l'écran en bout de course |

⭐ **Ce que ça fait à l'usage :** à 8° d'inclinaison — un poignet posé — le décor se déplace maintenant d'autant qu'il fallait 26° pour obtenir avant. **La sensibilité aux petits angles est multipliée par 3,2**, et c'est elle, pas l'amplitude maximale, que la main rencontre.

⚠️ **Ce qui n'a pas bougé, et ne doit pas :** le lissage *(≈ ⅓ s — l'invariant d'hypersensibilité)* · le sens *(fenêtre, pas niveau à bulle)* · l'axe horizontal seul · le fait que **l'inclinaison ne touche que le décor**. 🔴 **Rien de tout cela ne se règle par un nombre** : ce sont des décisions, pas des dosages.

### 4.3 🔄 Le miroir tombe sur deux couches — *« la jointure n'est pas très jolie »* *(15/08/2026, relevé par Xavier)*

**Le diagnostic n'était pas celui qu'on croyait.** §8 attendait un axe de symétrie *trop fréquent*, et proposait d'élargir les tuiles. En regardant les planches, la cause est ailleurs : **le dessin allait bord à bord**, donc un nuage et des feuilles étaient **coupés par le bord du cadre** — et le miroir leur recollait leur propre reflet. ⭐ **Le miroir ne faisait pas un raccord, il faisait un papillon** : continu, mais parfaitement reconnaissable.

**Élargir n'aurait donc rien réparé** — seulement espacé le défaut d'environ un tiers.

| Étape | Ce qui a été fait |
|---|---|
| **La charte** | Le dessin **flotte au milieu du cadre** : le premier et le dernier sixième de la largeur sont vides. ⚠️ **Formulé en positif** — « l'image commence à un sixième du bord » — parce que la consigne négative *(« rien ne touche le bord »)* a été ignorée deux fois sur deux |
| ⭐ **Un ton unique de magenta** | Une fois les marges demandées, le modèle s'est mis à les peindre **dans un magenta plus clair**, que le détourage ne coupe pas : il restait un **rectangle fantôme** en plein ciel. La charte l'interdit maintenant nommément |
| **Les planches** | Regénérées en **21:9**, retenues après lecture : `decor-feuillage/02-b`, `decor-nuages-pres/05-a` |
| **Le détourage** | `--marge=0.04` — le garde-fou du §3 |
| **Le rendu** | `Couche.marge` décide de tout : marge ⇒ répétition simple au pas de la partie peinte ; pas de marge ⇒ miroir. **Aucun second réglage**, donc pas de combinaison impossible |
| **Vérification** | Le tuilage a été **rendu et regardé hors du téléphone** *(3 écrans de large, ciel de jour)* avant d'entrer dans l'APK. ⭐ **Le raccord a disparu sur les deux couches** |

⏳ **Les nuages lointains gardent leur miroir.** Quatre planches essayées, aucune d'un seul ton — et abaisser le seuil du détourage à 0,45 pour rattraper le magenta clair n'a pas suffi. **C'est la couche la plus pâle et la plus lente du lot** *(profondeur 0,14)* : son axe est le moins visible. À reprendre quand le modèle sera d'humeur.

🔴 **La prairie garde le sien pour une raison qui ne changera jamais** : une couche de sol va bord à bord, sinon elle découvre le ciel sous elle.

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
| 🔴 **Bouger tout seul.** Aucune dérive, aucun nuage qui file, aucune animation d'ambiance | Hors la respiration de Kokoro, **rien ne se déplace sans que la main le déplace** — jamais de mouvement à interpréter (`README.md` §5). 🔄 **« Le doigt » est devenu « la main » le 15/08/2026** *(§4.1)* : l'inclinaison du téléphone déplace le décor. ⭐ **L'interdit ne bouge pas d'un pouce** — c'est justement pourquoi on lit une **position** et non une vitesse à intégrer : **rien ne dérive, rien ne se recentre, et téléphone posé le décor est immobile.** Et il se coupe d'un geste |
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
- ✅ **L'axe de symétrie est supprimé sur le feuillage et les nuages proches** *(§4.3)*. ⚠️ **La sortie envisagée ici — élargir la tuile — était la mauvaise** : le défaut ne venait pas de la fréquence de l'axe mais du dessin coupé par le bord. **Vider les bords l'a supprimé, pas espacé.** ⏳ **Il reste sur les nuages lointains**, la couche la plus pâle et la plus lente.
- ✅ **La course et le débattement de l'inclinaison ont été revus en main** *(§4.2 — 18°, 0,40 écran)*. ⏳ **Le nouveau dosage, lui, n'a pas encore été essayé** — ce sont toujours deux constantes en tête de `decor/Inclinaison.kt`.
- ⏳ **La palette de nuit est un premier jet** : ciel `#08202E → #1A4A63`, teinte `#4C7691` multipliée. Elle tient sur les planches actuelles ; elle n'a pas été regardée longtemps.
