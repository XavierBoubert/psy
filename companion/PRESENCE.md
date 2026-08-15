# Kokoro — la présence

**Comment le personnage habite le monde, et le plan pour le construire.**

> 📐 Le corps qui fait foi reste `retenus/kokoro-corps-v2.svg` et [`CORPS.md`](CORPS.md) ; le monde, [`DECOR.md`](DECOR.md) ; le rangement, [`INTERFACE.md`](INTERFACE.md). **Ce document ne décrit que le comportement** — ce qu'il ajoute au corps est listé au §5 et remonte dans `CORPS.md` à l'étape finale.
>
> ⏱️ Jalon **K7**.

---

## 1. Le modèle

### 1.1 Deux régimes, jamais les deux à la fois

| | **L'habitant** | **Le locuteur** |
|---|---|---|
| Où | Dans le décor, à sa place sur l'écran courant | En bas à gauche d'un panneau plein écran |
| Cadrage | Corps entier, **60 dp** | À partir du thorax, **≈ 110 dp visibles** |
| Ce qu'il porte | Une **posture** | Une **expression** |
| Ce qui le déclenche | L'écran courant et l'état de sa liste | L'ouverture d'un panneau plein écran |

- **Un panneau plein écran est une bulle de discussion** — l'étape ouverte, une fiche, le check-in, les réglages, la crise. 🔴 **Une carte de liste n'en est pas une** ; rien ne lui attache de personnage.
- **À l'ouverture d'un panneau, l'habitant quitte le champ en vol** ; il revient à la fermeture.
- 🔴 **Une seule instance à l'écran** (`CORPS.md` §8.8) — c'est ce qui impose l'alternance.

### 1.2 Trois axes indépendants

| Axe | Ce que c'est | État |
|---|---|---|
| **Expression** | La forme des trois tracés du panneau | Les six existantes **+ `serein`** (semi-sourire). `sommeil` réutilise `veille` |
| **Regard** | Une translation des deux yeux dans le panneau | ⭐ **Cesse d'être une expression** (`de-cote`) pour devenir un réglage continu |
| **Posture** | Ce que font le corps et les bras | Les cinq existantes **+ `pensif`, `lecture`, `notes`, `accoude`, `sommeil`** |

🔴 **Chaque axe reste un jeu fermé ; leur produit n'a pas à l'être.** `pensif` = *serein × regard vers la liste × repos*.

### 1.3 L'ordre de peinture

```
ciel · nuages loin · nuages près · prairie · feuillage │ OMBRE · KOKORO │ panneaux · rubans · cartes
```

- **L'ombre est peinte dans la couche du personnage**, juste sous lui : un panneau la recouvre mécaniquement. 🔴 **Aucune découpe, aucun test — « pas d'ombre sur l'interface » est une conséquence de l'ordre.**
- **Seule exception** : sur l'écran de crise, le corps passe **sous** le bouton et les bras **dessus** — deux passes de peinture.
- **L'ombre est solidaire du personnage en `x`**, sinon elle dérive au parallaxe. Ellipse floue, très aplatie, posée **bas** — c'est elle qui dit la hauteur de vol.

### 1.4 Les tailles

| | Hauteur | Contour rendu (×3) | Ce qui se lit |
|---|---|---|---|
| Habitant | **60 dp** | ≈ 2,0 px | Une silhouette, une posture, une direction de regard |
| Locuteur | **≈ 110 dp visibles**, tête ≈ 60 dp | ≈ 3,5 px | **Un visage** |

⚠️ **48 dp est exclu** : le contour vaut 1,1 % de la hauteur (`CORPS.md` §4), il y tomberait à 1,6 px et le trait se délave. **60 dp est la plus petite taille où le cerne reste à 2 px pleins.** À revérifier sur l'appareil.

---

## 2. Le comportement par écran

| Écran | Place | Posture | Détail |
|---|---|---|---|
| **Thérapie**, avant 18 h | À côté de *Sans date* | `pensif` | Regard qui glisse d'une carte à l'autre. Aucun geste |
| **Thérapie**, à partir de 18 h | À côté du check-in | `montre` | Bras tendu vers le check-in — **il n'a pas de doigt**. Le geste est identique que le check-in soit fait ou non ; **l'expression passe à `chaleureux` s'il est fait** |
| **Documentation** | Au-dessus de la liste | `lecture` | Balayage des yeux, gauche → droite, retour bref, un peu plus bas. Bras avancés vers le bas |
| **Bilan** | Au-dessus de la liste | `notes` | Un bras en bas, petits allers-retours horizontaux, yeux baissés vers ce bras |
| **Crise** *(du monde)* | ⭐ **Accoudé au bouton *Mot code*** | `accoude` | Bras posés sur l'arête, corps derrière, tête un peu penchée, **panneau allumé** — *« Kokoro veille sur toi »*. 🔴 **Immobile hors respiration et clignement**, et **jamais sur les autres surfaces de crise** *(§6 E13)* |
| **Liste vide**, tout écran | Inchangée | `sommeil` | Yeux fermés au repos, lévitation ralentie, Zzz en fondu. **Le cadre vide textuel reste affiché** |
| **Panneau ouvert** | Bas à gauche de la bulle | *(régime locuteur)* | Expression liée au contenu affiché |

**Aucune de ces poses ne porte d'information.** Ne pas les reconnaître ne fait rien perdre : aucune action n'est attendue, aucun état du dossier n'y est encodé. 🔴 **C'est aussi ce qui interdit d'ajouter un accessoire pour les rendre plus lisibles** — livre, lunettes et calepin n'existent pas.

> ⭐ **Une seule pose fait exception, et elle a été arbitrée pour ça** *(16/08/2026)* **: `accoude`, sur l'écran de crise, dit *je suis là*.** C'est une **présence**, pas une information : il n'y a rien à décoder, rien à faire, et **rien de ce qu'elle dit ne dépend du dossier** — ni de l'heure, ni du check-in, ni de ce qui a été fait. **Ne pas la remarquer ne fait toujours rien perdre.** ⚠️ **C'est exactement la ligne que la supervision du 15/08 refusait de franchir sans motif écrit** : sur l'écran de crise, une pose qui n'apporte rien est du bruit. **Le motif existe maintenant, et il vient de Xavier.**

---

## 3. Les constantes de départ

**À itérer sur l'appareil. Aucune n'est acquise.**

| | Valeur | Note |
|---|---|---|
| Lévitation | Période **4 500 ms**, amplitude **3 %** de la hauteur, déphasage ¼ | 🔴 **La même horloge que la respiration** — deux périodes distinctes produisent un battement, donc une information involontaire |
| Transit entre écrans | **420 ms**, arc de **26 dp**, easing continu, **120 ms de retard** sur le décor | Aucun changement de direction net, aucune apparition, aucune disparition |
| Clignement | Intervalle aléatoire **2 800–6 500 ms**, morphing **80 ms**, **yeux seuls** | La borne basse évite le papillonnement |
| Balayage de lecture | Une ligne ≈ **3 000 ms**, retour ≤ **200 ms** | Seul mouvement de la posture `lecture` |
| Geste d'écriture | **4 000 ms** de geste, **10 000–20 000 ms** d'arrêt | 🔴 Intermittent, jamais continu |
| Sommeil | Lévitation à **½ vitesse**, amplitude **½** | 🔴 **Obtenue en divisant la phase par deux, pas en ouvrant une horloge** — c'est pourquoi le tour d'horloge vaut **deux** respirations *(9 000 ms)* |
| Semi-sourire | **La moitié de la flèche du sourire** | §6 E1 |
| Ombre | Demi-largeur **39,3** *(le demi-écart d'épaules du dessin)*, aplatissement **0,16**, opacité **0,18**, noyau plein **45 %** du rayon | 🔴 **Aucune de ces quatre valeurs ne varie dans le temps** — c'est ce qui interdit à l'ombre de pulser. Seule la demi-largeur est dérivée du dessin ; les trois autres sont à régler à l'œil |

---

## 4. Les invariants qui bornent l'ensemble

🔄 **Cinq des six sont redescendus dans [`CORPS.md`](CORPS.md) à l'étape E14** — c'est là qu'ils vivent désormais, et ils ne se relisent plus ici : *jamais vers le lecteur* et *aucun mouvement continu* au **§8 point 1 et point 7** · *`chaleureux` sans contraire* au **§8 point 4** · *le personnage reste vectoriel* aux **§2 et §4** · *la respiration ne change jamais de rythme* au **§5**.

**Un seul appartient en propre à la présence, parce qu'il ne parle pas du corps mais de l'alternance des deux régimes :**

1. 🔴 **Rien ne bouge pendant qu'un texte se lit** : panneau ouvert = habitant **hors champ**, locuteur **immobile hors expression**.

---

## 5. ✅ Ce que ça a changé dans `CORPS.md` — **fait le 15/08/2026 (E14)**

**`CORPS.md` v2.2 porte tout ce qui suit ; ce document ne le répète plus.** Il ne reste ici que **le plan et les constantes**.

| § de `CORPS.md` | Ce qui a été réécrit |
|---|---|
| **§3** | Le visage garde **six** expressions — ⭐ **le compte annoncé « sept » était une erreur d'arithmétique** : `serein` est entré, `de-cote` est sorti, le jeu n'a pas grandi. Le regard cesse d'être une expression pour devenir un **axe** |
| **§5** | Le clignement n'agit plus que sur **les yeux**, cadence **2 800–6 500 ms** — **tranche le point ouvert §11.6** |
| **§7** | Cinq postures → ⭐ **dix** *(« neuf » était la seconde erreur de comptage)*, plus `allonge` toujours due |
| **§8 pt 1** | *« N'entre pas dans le champ »* → interdit conservé **entier sur l'axe de profondeur**, entrées et sorties **latérales** admises |
| **§8 pt 7** | *« Il ne se déplace pas, ne bouge pas tout seul »* → vol, lévitation, transit. **La règle est réécrite, pas contournée** |
| **§8 pt 8** | Une seule instance → **l'alternance des deux régimes**, et ce qu'elle impose à l'ouverture d'un panneau |
| **§10** | 48 dp d'overlay et 96 dp d'en-tête → **60 dp** habitant, **≈ 110 dp** locuteur, mesurés **sur le personnage et non sur sa vue** |
| **§10.2** 🆕 | ⭐ **La dérogation de l'écran de crise** — *« Kokoro veille sur toi »*, ses quatre écarts avec ce qui avait été refusé, et ses **six bornes** |
| **§2, §9** 🆕 | **La tête peut pencher** — 6°, bornés à 10°, autour du milieu de la ligne des épaules. **Une seule posture, et le corps reste de face** |
| **§11.6** | Point ouvert → **tranché** |

> 🔴 **§8 point 6 — le changement d'apparence s'annonce avant installation.** L'annonce porte la table ci-dessus, **et elle est due** : voir §7.2.

---

## 6. Le plan

**Chaque étape est livrable seule et vérifiable dans l'atelier du corps.**

> ✅ **E1 → E14 faits** — le semi-sourire, `serein` par défaut, le regard devenu un axe, le clignement des yeux seuls à 2,8–6,5 s, le balayage de lecture, les **dix** postures, le geste d'écriture, le vol et l'ombre, l'habitant dans le monde, la règle horaire, l'état vide, le locuteur, **la veille sur l'écran de crise** et la reprise documentaire. ⭐ **Kokoro habite enfin une surface que Xavier voit** — il était encore dans l'atelier à E8.
>
> ⭐ **E13 a été refusée le 15/08 et arbitrée le 16/08 — et c'est le seul endroit du plan où ça s'est produit.** Ce n'est pas un contretemps : **le dispositif s'était autorisé à lui-même une dérogation à sa propre règle, sans motif et sans que personne ne l'ait demandée.** La supervision l'a bloquée ; **Xavier a débloqué en changeant l'objet** — un visage bienveillant au lieu d'une présence muette, une porte au lieu de deux.
>
> ⭐ **Deux erreurs de comptage trouvées à E14, et elles allaient dans le même sens** : le §5 annonçait **sept** expressions pour six, et **neuf** postures pour dix. **Aucune des deux n'était une décision** — l'une comptait un ajout là où il y avait un échange, l'autre oubliait une posture. **Elles sont corrigées dans `CORPS.md` v2.2**, qui fait foi.
>
> ⭐ **Le regard a gagné un second axe** — l'abaissement — parce que `notes` demande des « yeux baissés vers ce bras » et `lecture` des yeux sur la liste. Il se règle par la posture, comme le premier.
>
> ⭐ **Le flottement de la v1 est mort à E8.** Il battait sur trois périodes distinctes — 3,2 s de hauteur, 5,1 s de dérive latérale, 4,3 s de bascule — donc exactement l'information involontaire que §3 interdit. La lévitation ne garde qu'un axe, sur l'horloge de la respiration. **Et le bras entrouvert « en vol » de l'atelier a disparu avec lui** : la posture est seule maîtresse des bras.
>
> ✅ **Le balayage est attaché depuis E9.** `lecture` porte celui du §3 ; `pensif` en porte un **plus lent et plus court** — il parcourt une liste, pas une ligne. Les deux restent des réglages passés à la posture, jamais des propriétés d'elle.
>
> ⭐ **L'horloge du corps fait maintenant deux respirations par tour** *(E11)*. 🔴 **Ce n'est pas une seconde horloge, c'est la même vue plus loin** : le sommeil demande une lévitation à ½ vitesse, et une phase divisée par deux ne se referme que si le tour est double. **La respiration, elle, n'a pas changé de rythme** — elle en fait simplement deux par tour, et §4.6 tient à la lettre.
>
> ⭐ **La place de l'habitant est ancrée au contenu, pas à la dalle** *(E9)*. Chaque écran déclare une **bande** — à côté de la pancarte d'une section, ou réservée au-dessus d'une liste — et Kokoro se pose dedans. 🔴 **Il en sort avec elle quand la liste défile, et il ne se replace pas.** C'est aussi ce qui rend le transit gratuit : les deux places glissent déjà avec le monde, il n'a qu'à voler de l'une à l'autre, avec ses 120 ms de retard.
>
> 🔴 **« Il ne passe jamais devant un texte » n'est vérifié par aucun test, et ce n'est pas un oubli** : la couche est peinte entre le décor et les écrans, donc **tout panneau le recouvre mécaniquement**. C'est une conséquence de l'ordre de peinture (§1.3) — un test le constaterait sans rien garantir de plus.
>
> 🔄 **Le verrou de l'écran de crise a changé de nature, il n'a pas disparu** *(E13)*. Il tenait l'**absence** de personnage tant que la supervision n'avait pas eu lieu ; il tient maintenant **ce que la dérogation admet, et rien de plus** — le panneau allumé, l'absence de vol, l'impossibilité de s'y endormir, et **le fait que rien n'y dépende du dossier**. ⭐ **Et un test de sources refuse tout personnage dans `crise/`** : le risque n'était pas d'en ajouter un exprès, c'était qu'il y arrive par la valeur par défaut d'un paramètre.
>
> ⭐ **La hauteur de l'habitant se mesurait sur sa vue, pas sur lui** *(corrigé à E12)*. Les deux diffèrent de 11 % — le dessin a des marges — et **le contour sortait donc à 1,8 px**, c'est-à-dire **sous le seuil de 2 px qui sert précisément à écarter les 48 dp** au §1.4. La correction le grandit d'autant : ⏳ **c'est la première chose à regarder sur l'appareil.**
>
> ⚠️ **À regarder sur l'appareil, ce que le code ne peut pas trancher** : les 60 dp de l'habitant *(§1.4, à revérifier — et il vient de changer)*, **le cadrage du locuteur** — la coupe au thorax et sa marge de gauche —, la flèche de l'arc de transit, et **s'il traîne d'une image derrière la liste pendant un défilement lancé** — sa place est lue après la mise en page du contenu.
>
> ✅ **Le point de doctrine de E14 est tranché : six expressions, dix postures.** Le §5 en annonçait sept et neuf — **deux erreurs d'arithmétique, aucune décision.** `CORPS.md` v2.2 fait foi.

### E1 — Le semi-sourire

- Extraire les trois paramètres de `BOUCHE_ARC` en constantes, puis construire `BOUCHE_SEMI` par **division par deux** : `arcSymetrique((DEMI_BOUCHE + DEMI_SOURIRE) / 2, extremites / 2, controle / 2)`. 🔴 **La moitié est dans le code, pas dans un commentaire.**
- Ajouter `BOUCHE_SEMI` à `TRACES` et l'expression `SEREIN(OEIL_OVALE, BOUCHE_SEMI)`.
- **Fichiers** : `corps/Geometrie.kt`, `corps/Expression.kt`, `test/…/CorpsInvariantsTest.kt`.
- **Fin** : un test vérifie que la flèche de `BOUCHE_SEMI` vaut la moitié de celle de `BOUCHE_ARC` (1,75 contre 3,5) et que ses commissures ne tombent pas.

### E2 — `serein` devient l'expression par défaut

- `Posture.Repos` et tout ce qui retombe sur `NEUTRE` passent à `SEREIN`. `NEUTRE` reste dans le jeu.
- **Fichiers** : `corps/Posture.kt`.
- **Fin** : l'atelier montre le semi-sourire au repos ; le morphing `serein ↔ chaleureux` reste conforme.

### E3 — Le regard devient un axe

- Supprimer `Expression.regardParDefaut` et `DE_COTE` ; `regard` ne vient plus que de `ReglagePosture`. `Montre` combine `SEREIN` et un regard latéral.
- **Fichiers** : `corps/Expression.kt`, `corps/Posture.kt`, `corps/RigKokoro.kt`, `corps/Apercus.kt`.
- **Fin** : aucune expression ne porte de regard ; l'atelier règle le regard indépendamment.

### E4 — Le clignement

- Le clignement ne remplace plus l'expression entière : il n'agit **que sur les yeux**, la bouche reste celle de l'expression courante. Cadence aléatoire 2 800–6 500 ms.
- **Fichiers** : `corps/AnimationCorps.kt`, `corps/CorpsKokoro.kt` (morphing dissocié yeux / bouche).
- **Fin** : la bouche ne tressaille plus ; deux intervalles consécutifs ne sont jamais égaux.

### E5 — Le balayage du regard

- Une animation de regard en boucle : parcours lent, retour bref. Paramètres : durée de ligne, amplitude, pause.
- **Fichiers** : `corps/AnimationCorps.kt`.
- **Fin** : réglable et observable dans l'atelier, indépendamment de la posture.

### E6 — Les postures immobiles

- Ajouter `Pensif`, `Lecture`, `Attente` *(devenue `Accoude` le 16/08/2026, E13)*, `Sommeil` à `Posture` et leurs réglages : ouverture des bras, regard, panneau, échelle.
- **Fichiers** : `corps/Posture.kt`, `corps/Apercus.kt`, `corps/AtelierActivity.kt`.
- **Fin** : les quatre apparaissent dans l'atelier et respectent la borne de +70,5° des bras.

### E7 — Le geste d'écriture

- Posture `Notes` : un bras animé en allers-retours courts, **intermittent**, yeux baissés vers ce bras.
- **Fichiers** : `corps/Posture.kt`, `corps/AnimationCorps.kt`.
- **Fin** : le geste s'arrête complètement entre deux séries ; l'amplitude reste sous l'ouverture horizontale.

### E8 — Le vol et l'ombre

- Lévitation idle sur l'horloge de la respiration. Ombre elliptique floue, solidaire en `x`, opacité constante, peinte **avant** le personnage dans la même couche.
- **Fichiers** : nouveau `corps/Vol.kt`, `corps/CorpsKokoro.kt`.
- **Fin** : l'ombre ne pulse pas ; hauteur de vol lisible ; `Vol.FLOTTEMENT` existant remplacé ou aligné.

### E9 — L'habitant dans le monde

- Sortir Kokoro de la liste (`KokoroPose` dans `monde/Bords.kt`) et le poser dans une **couche propre entre le décor et le contenu**. Une place par écran, ancrée au contenu — il sort du champ avec lui quand la liste défile, sans se replacer. Transit sur changement d'écran, en retard sur le décor.
- **Fichiers** : `monde/MondeKokoro.kt`, `monde/Bords.kt`, nouveau `monde/Habitant.kt`.
- **Fin** : la traversée de l'anneau ne fait jamais passer le personnage devant un texte ; aucune ombre sur un panneau.

### E10 — La règle horaire de la thérapie

- Avant 18 h `pensif` près de *Sans date* ; à partir de 18 h `montre` vers le check-in. Expression `chaleureux` si le check-in du jour est écrit, expression ordinaire sinon. 🔴 **Le geste et la place ne dépendent jamais de cet état.**
- **Fichiers** : `monde/Habitant.kt`, `journal/DossierSynchronise.kt` (lecture de l'existence du fichier du jour).
- **Fin** : bascule à 18 h vérifiée aux deux états du check-in ; aucun autre écart observable entre les deux.

### E11 — L'état vide

- Liste vide → posture `sommeil`, lévitation ralentie, Zzz en fondu. Le `CadreVide` textuel reste affiché.
- **Fichiers** : `ui/Pieces.kt`, `monde/Habitant.kt`.
- **Fin** : documentation et bilan, aujourd'hui vides, montrent le sommeil **et** leur texte.

### ✅ E12 — Le locuteur *(fait le 15/08/2026)*

- Kokoro cadré au thorax en bas à gauche des panneaux plein écran, expression choisie par le contenu. L'habitant sort du champ à l'ouverture, revient à la fermeture.
- **Fichiers** : `ui/Pieces.kt` (`PageKokoro`), `monde/MondeKokoro.kt` (`EtapeOuverte`), `monde/Etapes.kt` (`PanneauEtape`), `monde/Habitant.kt`, nouveau `corps/Locuteur.kt`.
- **Fin** : jamais deux Kokoro à l'écran, y compris pendant les 320 ms d'ouverture du panneau.

> ✅ **L'alternance est une bascule unique, et un test la parcourt** : `locuteurEnScene` et `habitantEnScene` ne peuvent être vraies ensemble — ni fausses ensemble, **le personnage n'a pas le droit de disparaître entre les deux**. L'habitant **n'est plus composé du tout** une fois dehors.
>
> ⭐ **Les deux sens ne sont pas symétriques, et c'est ce qui tient l'invariant.** À l'ouverture il part **tout de suite** et le locuteur n'entre qu'une fois qu'il est sorti — le panneau monte du bas, donc **le coin du locuteur est le premier visible**, et entrer là ferait deux Kokoro. À la fermeture, **le panneau l'emporte en redescendant** et l'habitant attend ces 320 ms avant de revenir.
>
> ⭐ **La bande du locuteur est réservée en permanence, occupée ou non** : sans ça le texte se remettrait en page à son arrivée, **sous les yeux de qui lit**.
>
> 🔴 **Les trois pages de crise n'en ont pas reçu**, alors qu'elles partagent le composable des pages : `locuteur` vaut `null` par défaut, et c'est le paramètre — pas la vigilance — qui tient *« aucune extension aux autres surfaces de crise »*.
>
> ⚠️ **Deux surfaces nouvelles portent le personnage** : le **check-in** et les **réglages**. 🔴 **Aucune expression n'y dit qu'une chose n'est pas faite** — `chaleureux` ne paraît que sur le check-in **écrit**, un échec d'écriture ne change pas le visage, et un test le vérifie état par état.

### ✅ E13 — L'écran de crise — **refusée le 15/08, arbitrée et faite le 16/08/2026**

- ⭐ **Posture `accoude` sur le bouton *Mot code*, panneau allumé, tête un peu penchée** — *« Kokoro veille sur toi »*. **Immobile** : ni lévitation, ni ombre, ni transit sur place. Deux passes de peinture pour les bras.
- **Fichiers** : `monde/Habitant.kt`, `monde/Bords.kt`, `monde/MondeKokoro.kt`, `crise/Elements.kt`, `corps/Posture.kt`, `corps/CorpsKokoro.kt`, `corps/Geometrie.kt`.
- **Fin** : aucun texte ajouté à l'écran ; `CriseActivity`, la tension appliquée et la phrase pour le soignant restent sans personnage.

> ⭐ **Ce que Xavier a arbitré, et pourquoi c'est un autre objet que ce qui avait été refusé** *(16/08/2026)* **:**
>
> | | Refusé le 15/08 | ⭐ Arbitré le 16/08 |
> |---|---|---|
> | **Le motif** | aucun — un plan d'animation | *« Kokoro veille sur toi »* |
> | **Le visage** | panneau **éteint** | 🔴 **allumé, `serein`, regard au centre** |
> | **La pose** | *« affaissé »* — mot interdit par `CORPS.md` §8.3 | **accoudé au bouton comme sur un muret** |
> | **Où** | ambigu, deux surfaces possibles | 🔴 **l'écran du monde, et lui seul** |
>
> ⭐ **La borne de Xavier est plus fine que celle que la supervision recommandait.** Elle demandait *« les deux portes, sinon `INTERFACE.md` §6.2 tombe »* ; il en a choisi **une**, et le partage est clinique : **la présence se pose sur l'écran qu'on atteint en traversant — calmement —, jamais sur celui qui s'impose par-dessus le verrouillage quand ça va déjà mal.** ⚠️ **Le contre-argument reste écrit** *(`CORPS.md` §10.2)* : les deux portes ne se ressemblent plus tout à fait. **Ce que §6.2 protégeait tient** — mêmes boutons, même place, même libellé, donc **rien de ce qu'on fait ne dépend de la porte** —, mais c'est à vérifier à l'usage.
>
> 🔴 **Six bornes, un test chacune** : cet écran seul *(test de sources sur `crise/`)* · aucun texte ajouté · il ne vole pas *(accoudé, pas posé — et pas d'ombre sur l'interface)* · il ne s'endort jamais ici · **rien n'y dépend du dossier** *(une seule pose, quelles que soient l'heure et le check-in)* · la tête penche de 6°, bornée à 10°.
>
> ⭐ **Les deux passes de peinture sont une seule instance, et le code l'impose** : le rig est calculé **une fois**, publié, et relu par la couche du haut. Deux rigs animés séparément dériveraient au premier clignement ; deux transits parallèles feraient voler les bras à côté du corps.
>
> ⛔ **Ce qui avait été refusé, et qu'il faut garder en mémoire** — [`superviseur/outputs/2026-08-15-supervision.md`](../superviseur/outputs/2026-08-15-supervision.md). **Trois constats, et aucun n'était « un personnage en crise est impensable » :**
>
> 1. 🔴 **La règle a été réaffirmée la veille et E13 ne lui répond pas.** `CORPS.md` §10 — *« un compagnon décoratif au pire moment est du bruit »* — et §10.1 point 3, écrit à l'occasion de la dérogation de la notification : *« ce qui est admis sur une porte ne l'est pas derrière »*. **E13 ne cite ni l'un ni l'autre.** ⭐ **Et le §2 de ce document arme l'objection lui-même** : *« aucune de ces poses ne porte d'information »* — sur cet écran-là, c'est la définition de la décoration.
> 2. 🔴 **La posture est décrite « affaissé »**, mot que `CORPS.md` §8 point 3 interdit nommément *(« s'affaisser, baisser la tête… aucune posture de découragement, jamais »)*. **Le code, lui, ne s'affaisse pas** : `attente` ne pose que 12° aux deux bras. C'est donc le mot qui est faux, ou l'intention.
> 3. ⚠️ **La cible n'est pas nommée** : le §2 vise l'écran de crise **du monde**, la liste de fichiers vise `CriseActivity`. Or `INTERFACE.md` §6.2 impose depuis le 15/08, **à la demande de Xavier**, que les deux portes affichent la même chose à la lettre. **Une porte avec personnage et une sans, c'est le *« lequel ai-je sous les yeux ? »* qu'on venait de supprimer.**
>
> 🔴 **Le point 1 ne se réparait pas par une correction d'écriture : il appartenait à Xavier** *(arbitrage A1)*, et il l'a rendu le 16/08 **en donnant le motif qui manquait et en changeant la pose**. Les points 2 et 3 sont corrigés avec : le mot *« affaissé »* a disparu du dispositif, et la cible est nommée. ⭐ **Un refus se corrige, il ne se contourne pas — et c'est ce qui vient de se passer.**

### ✅ E14 — La reprise documentaire *(faite le 15/08/2026)*

- Répercuter le §5 dans `CORPS.md` (§3, §5, §7, §8, §10) et retirer de ce fichier ce qui y est monté. Mettre à jour `INTERFACE.md` **D3** et **§3** (la place de Kokoro n'est plus provisoire).
- **Fin** : une seule source par règle ; ce document ne garde que le plan et les constantes.

> ✅ **`CORPS.md` passe en v2.2** : §3, §5, §7, §8 *(points 1, 7, 8)*, §10 *(les tailles)* et §11.6. **`INTERFACE.md` D3 est tranchée** et le *« la place de Kokoro est provisoire »* du §3 est levé.
>
> ⛔ **Une seule ligne n'a pas été écrite : la dérogation de l'écran de crise au §10**, qui dépend de l'arbitrage A1. **`CORPS.md` §10 reste donc inchangé sur ce point**, et il porte désormais le renvoi à la supervision.
>
> ⭐ **Deux erreurs de comptage corrigées au passage** — sept expressions pour six, neuf postures pour dix.

---

## 7. Ce qui reste à faire hors plan

1. ✅ **Supervision faite le 15/08/2026 — verdict : refus — puis arbitrage de Xavier le 16/08.** [`superviseur/outputs/2026-08-15-supervision.md`](../superviseur/outputs/2026-08-15-supervision.md) : **6 constats, 2 bloquants**. **A1 : oui, avec un visage.** **A2 : l'écran du monde seulement** — *contre la recommandation, et le contre-argument reste écrit* (`CORPS.md` §10.2). ⭐ **Le circuit a fonctionné comme il devait** : le dispositif s'était autorisé une dérogation sans motif, la supervision l'a bloquée, **et c'est Xavier qui a débloqué — en changeant l'objet, pas en levant la règle.**
2. 🔴 **Annonce avant installation — c'est le constat le plus urgent de la supervision, et il ne dépend pas de E13** *(constat 4, bloquant)*. La table du §5, en toutes lettres. Elle est due **depuis E9**, et **E12 vient d'étendre le changement au check-in et aux réglages** — trois surfaces que Xavier ouvre, dont deux sans passer par le monde. **La prévisibilité est une fonctionnalité**, et *« annoncer avant de faire »* est l'une des huit contraintes non négociables du profil. **Aucune nouvelle pose sur le téléphone avant cette annonce.**
3. **Toujours dû** : la pose `allonge` (`CORPS.md` §11.1) et la validation à l'écran des cinq expressions dérivées (`CORPS.md` §11.2).
4. ⚠️ **Rappel de calendrier, versé par la supervision** *(constat 6)* : `PLAN-KOKORO.md` §9-A pose que **le développement passe après le palier 0 PPC et le brief**. Le brief du Dr Isorni est dû au **week-end du 29-30/08** pour la consultation du **03/09**. **E1 → E14 ont été faites en une journée** ; c'est le deuxième signalement de C7, après celui du 09/08.
