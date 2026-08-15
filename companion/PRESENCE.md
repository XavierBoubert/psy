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
| **Posture** | Ce que font le corps et les bras | Les cinq existantes **+ `pensif`, `lecture`, `notes`, `attente`, `sommeil`** |

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
| **Crise** | Derrière le bouton *Mot code* | `attente` | Affaissé, bras posés sur le bouton, **panneau éteint**, 🔴 **immobile** |
| **Liste vide**, tout écran | Inchangée | `sommeil` | Yeux fermés au repos, lévitation ralentie, Zzz en fondu. **Le cadre vide textuel reste affiché** |
| **Panneau ouvert** | Bas à gauche de la bulle | *(régime locuteur)* | Expression liée au contenu affiché |

**Aucune de ces poses ne porte d'information.** Ne pas les reconnaître ne fait rien perdre : aucune action n'est attendue, aucun état du dossier n'y est encodé. 🔴 **C'est aussi ce qui interdit d'ajouter un accessoire pour les rendre plus lisibles** — livre, lunettes et calepin n'existent pas.

---

## 3. Les constantes de départ

**À itérer sur l'appareil. Aucune n'est acquise.**

| | Valeur | Note |
|---|---|---|
| Lévitation | Période **4 500 ms**, amplitude **3 %** de la hauteur, déphasage ¼ | 🔴 **La même horloge que la respiration** — deux périodes distinctes produisent un battement, donc une information involontaire |
| Transit entre écrans | **380–450 ms**, arc, easing continu, **120 ms de retard** sur le décor | Aucun changement de direction net, aucune apparition, aucune disparition |
| Clignement | Intervalle aléatoire **2 800–6 500 ms**, morphing **80 ms**, **yeux seuls** | La borne basse évite le papillonnement |
| Balayage de lecture | Une ligne ≈ **3 000 ms**, retour ≤ **200 ms** | Seul mouvement de la posture `lecture` |
| Geste d'écriture | **4 000 ms** de geste, **10 000–20 000 ms** d'arrêt | 🔴 Intermittent, jamais continu |
| Sommeil | Lévitation à **½ vitesse**, amplitude **½** | |
| Semi-sourire | **La moitié de la flèche du sourire** | §6 E1 |

---

## 4. Les invariants qui bornent l'ensemble

1. 🔴 **Rien ne bouge pendant qu'un texte se lit** : panneau ouvert = habitant hors champ, locuteur immobile hors expression.
2. 🔴 **Il ne se déplace jamais vers le lecteur.** Entrées et sorties **latérales** ; rien ne grandit vers l'avant.
3. 🔴 **Aucun mouvement continu dans le champ** : tout geste répété est intermittent ou borné.
4. 🔴 **`chaleureux` réagit à un fait accompli et n'a pas de contraire.** Quand une étape n'est pas faite, la posture, la place, le texte et le geste sont **inchangés** — seule l'expression reste celle de tous les jours.
5. 🔴 **Le personnage reste vectoriel** ; aucun bitmap, aucun accessoire, aucun symbole porteur d'information. Les Zzz sont des ornements, au sens de `INTERFACE.md` §4.2 : ils n'informent de rien que le cadre vide ne dise déjà en toutes lettres.
6. 🔴 **La respiration ne change jamais de rythme.**

---

## 5. Ce que ça change dans `CORPS.md`

| § | Ce qui est écrit | Ce qui le remplace |
|---|---|---|
| **§8.7** | *« Il ne se déplace pas à l'écran, ne bouge pas tout seul hors respiration et clignement »* | Vol, lévitation, transit, postures par écran. **La règle est réécrite, pas contournée** |
| **§8.1** | *« N'entre pas dans le champ »* | Interdit conservé **sur l'axe de profondeur** ; les entrées latérales sont admises |
| **§3** | Six expressions | **Sept** — `serein` s'ajoute ; `de-cote` en sort et devient un réglage de regard |
| **§7** | Cinq postures | **Neuf**, plus `allonge` toujours due |
| **§5** | Clignement ≤ 1 / 20 s, expression entière | Cadence aléatoire 2,8–6,5 s, **yeux seuls** — tranche le point ouvert §11.6 |
| **§10** | *« L'écran de crise ne porte aucun personnage »* | **Dérogation bornée** : posture `attente`, panneau éteint, immobile, aucun texte ajouté, aucune extension aux autres surfaces de crise |
| **§10** | 96 dp en en-tête | **60 dp** habitant, **≈ 110 dp** locuteur |
| **§8.6** | Changement d'apparence annoncé avant installation | S'applique : l'annonce porte la table ci-dessus |

---

## 6. Le plan

**Chaque étape est livrable seule et vérifiable dans l'atelier du corps.**

> ✅ **E1 → E7 faits** — le semi-sourire, `serein` par défaut, le regard devenu un axe, le clignement des yeux seuls à 2,8–6,5 s, le balayage de lecture, les neuf postures, le geste d'écriture. **Le corps est prêt ; aucune surface de la thérapie ne l'a encore vu.**
>
> ⭐ **Le regard a gagné un second axe** — l'abaissement — parce que `notes` demande des « yeux baissés vers ce bras » et `lecture` des yeux sur la liste. Il se règle par la posture, comme le premier.
>
> ⚠️ **Ce que le §2 décrit et que les postures ne portent pas encore** : le glissement du regard d'une carte à l'autre (`pensif`) et le balayage de lecture (`lecture`). E5 pose que le balayage se règle **indépendamment de la posture** ; c'est **E9 / E10**, en plaçant l'habitant par écran, qui l'attacheront.
>
> ⚠️ **Un point de doctrine reste à trancher à E14** : le §5 annonce **sept** expressions, alors que `serein` entre et `de-cote` sort — le jeu en compte **six**. `CORPS.md` §3 ne se réécrit pas avant ce comptage.

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

- Ajouter `Pensif`, `Lecture`, `Attente`, `Sommeil` à `Posture` et leurs réglages : ouverture des bras, regard, panneau, échelle.
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

### E12 — Le locuteur

- Kokoro cadré au thorax en bas à gauche des panneaux plein écran, expression choisie par le contenu. L'habitant sort du champ à l'ouverture, revient à la fermeture.
- **Fichiers** : `ui/Pieces.kt` (`PageKokoro`), `monde/MondeKokoro.kt` (`EtapeOuverte`), nouveau `corps/Locuteur.kt`.
- **Fin** : jamais deux Kokoro à l'écran, y compris pendant les 320 ms d'ouverture du panneau.

### E13 — L'écran de crise

- Posture `attente` derrière le bouton *Mot code*, panneau éteint, **immobile** : ni lévitation, ni clignement, ni transit. Deux passes de peinture pour les bras.
- **Fichiers** : `crise/CriseActivity.kt`, `crise/Elements.kt`.
- **Fin** : aucun texte ajouté à l'écran ; la tension appliquée et la phrase pour le soignant restent sans personnage.

### E14 — La reprise documentaire

- Répercuter le §5 dans `CORPS.md` (§3, §5, §7, §8, §10) et retirer de ce fichier ce qui y est monté. Mettre à jour `INTERFACE.md` **D3** et **§3** (la place de Kokoro n'est plus provisoire).
- **Fin** : une seule source par règle ; ce document ne garde que le plan et les constantes.

---

## 7. Ce qui reste à faire hors plan

1. **Supervision** — bloquante avant l'étape **E13** : elle porte sur l'entrée du personnage dans une surface de crise.
2. **Annonce avant installation** — la table du §5, en toutes lettres.
3. **Toujours dû** : la pose `allonge` (`CORPS.md` §11.1) et la validation à l'écran des cinq expressions dérivées (`CORPS.md` §11.2).
