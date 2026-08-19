# `companion/` — Kokoro (心), le compagnon du patient

**Le compagnon de Xavier, sur son téléphone. C'est la seule surface tournée vers lui** : bilans, questionnaires, thérapie, protocoles — tout ce qui lui est accessible passe par ici. Il suit le contenu de Claude Psy : **il n'invente rien et ne décide rien.**

*Kokoro (心) : le mot japonais qui désigne indissociablement le cœur et l'esprit, racine de 心理学* (shinrigaku), *« psychologie ». Il nomme l'objet du soin, pas une promesse de résultat.*

> 📖 Vue d'ensemble : [`../README.md`](../README.md) · Vocabulaire : [`../THESAURUS.md`](../THESAURUS.md) · Ce qui reste à construire : [`../PLAN.md`](../PLAN.md) §5.
>
> 🔴 **Le format du contenu est [`PROGRAMME.md`](PROGRAMME.md)** *(normatif)* — les six types d'étape, les rubriques, les interdits. **Aucune surface n'a le droit d'en inventer un autre.**

---

## 1. Ses cinq rôles, et ce qu'il ne fait jamais

| Rôle | Concrètement |
|---|---|
| **Protéger** | Écran de crise depuis l'écran verrouillé : mot-code à Chourouk, tension appliquée guidée, phrase pour le soignant. **En un geste, sans parler, sans déverrouiller** |
| **Accompagner** | Le programme du jour — ce que Claude Psy a décidé en séance |
| **Éduquer** | La bibliothèque — les fiches **écrites pour être lues par Xavier**, jamais les documents cliniques bruts |
| **Réconforter** | La présence — un personnage qui respire, qui n'attend rien, qui ne reproche rien |
| **Faire à deux** | Les thérapies impossibles en solo : Kokoro passe dans les mains de l'aidant, qui suit un déroulé chronométré. **Mode entraînement obligatoire avant la première fois** |

| Il ne fait jamais | |
|---|---|
| **Décider** | Le contenu vient de Claude Psy, publié en séance |
| **Interpréter** | Aucune lecture, aucune hypothèse, aucun conseil de son cru |
| **Calculer une progression** | Aucun historique, aucune courbe, aucun score. **Un bilan est un texte daté écrit en séance**, jamais un graphique que l'app calcule |
| ⭐ **Venir vers Xavier** | **Aucune notification, aucune relance, aucun reproche.** Xavier vient à lui, et y trouve tout |

> ⭐ **Seule exception à la dernière ligne : l'accès crise sur l'écran verrouillé.** C'est une **porte**, pas un rappel : elle ne dit rien, ne demande rien, et n'apparaît pas parce qu'il s'est passé quelque chose.

**Ce qui n'entrera jamais dans Kokoro** — un conseil touchant au **traitement**, même interrogatif *(ça part au brief)* · un **streak**, un compteur, un historique, une progression calculée · un **son** ou une **vibration** non demandés · une consigne de **visualisation** · une expression de tristesse ou de reproche · un **service tiers** *(cloud, analytics, crash reporting, police distante)* · une **notification** ou une relance · 🔴 un **numéro d'urgence sous quelque forme que ce soit, 3114 compris** — il appartient à une conduite d'escalade, pas à une interface · 🔴 **le PHQ-9**, seul instrument porteur d'un déclencheur d'escalade.

---

## 2. Ce qui tourne sur le téléphone

| Surface | État | Détail |
|---|---|---|
| **Accès crise** | ✅ | Notification permanente et **muette** sur l'écran verrouillé : l'icône et le mot *Kokoro*, **rien à lire**. Elle ouvre `CriseActivity`, jamais le monde. **Republiée à chaque venue dans le monde** |
| **Écran de crise** | ✅ | Trois boutons — mot-code, tension appliquée, phrase pour le soignant. **Deux portes, un seul contenu** : l'écran *Crise* du monde et `CriseActivity` affichent le **même composable** |
| **Mot-code à Chourouk** | ✅ | SMS **envoyé en un appui**, téléphone verrouillé, sans réseau data. Aucun écran de confirmation. Le bouton se grise le temps de l'envoi ; un accusé paraît en bas |
| **Tension appliquée** | ✅ | Quatre repères externes enchaînés, minuteur, critères d'arrêt à un tap |
| **Check-in du jour** | ✅ | Panneau interne, questions fermées enchaînées, aucune saisie de texte → écrit `journal/AAAA-MM-JJ.json` |
| **Le monde** | ✅ | Quatre écrans en anneau horizontal, décor en quatre couches, plage de nuit, Kokoro habitant |
| **Le corps et la présence** | ✅ | Six expressions, dix postures, respiration, morphing du visage, lévitation, transit, ombre |
| **Réglages** | ✅ | Panneau interne au bout de la roue dentée — **plus une Activity** |
| **Documentation** | ✅ | Lit `programme.json` du dossier synchronisé, filtre les fiches, **confie le PDF au lecteur du téléphone** |
| **Thérapie** | ✅ | Lit les étapes `therapie` de `programme.json`, groupées par `quand` — `ecran`, `exercice` *(minuteur dans le panneau)*, `demarche` *(bouton « c'est fait »)*. **Rien n'est écrit en dur dans l'app** |
| **`reponses/`** | ✅ | Un fichier par étape faite, `AAAA-MM-JJ-HHMM-<id>.json`. 🔴 **Kokoro se souvient localement de ce qu'il a écrit** — l'état d'une étape ne dépend jamais d'un aller-retour par Drive |
| **Bilan** | ✅ | Lit les étapes `bilan` de `programme.json`, **groupées par mois décroissant** sur la `date` du document, et **confie le PDF au lecteur du téléphone**. ⭐ **Aucun score, aucun seuil, aucune interprétation à l'écran, et aucune fonction de partage** |
| **Questionnaire** | ✅ | Type porté et disponible — une question par écran, choix fermés, « passer » et arrêt libres → écrit `reponses/` **item par item**. ⭐ **Aucune échelle validée ne part dans Kokoro** *(tranché le 19/08/2026)* : elles se passent avec Claude Psy |
| **Séance à deux** | 🏗️ | K6. Type porté : accueil, entraînement à blanc, cases à cocher avant d'entrer — dont **une case qui porte le signal d'arrêt et les critères, et sans laquelle le déroulé ne s'ouvre pas** —, puis la séquence chronométrée qui passe seule d'une consigne à l'autre. ⭐ **Rien ne flotte autour de la consigne** : elle dit elle-même à qui elle s'adresse. **L'entraînement mené se retient localement** — l'issue n'est pas dans le nom du fichier. **Il reste à le jouer avec Chourouk**, c'est le critère de fin |

⭐ **Le circuit complet est constaté, pas seulement écrit** *(19/08/2026)* : une étape publiée depuis le PC est apparue sur le téléphone, a été faite, et sa réponse est revenue au dossier par `psy:sync` — **valide au format, sans intervention manuelle**. Un bilan s'est ouvert dans le lecteur PDF du téléphone et s'est partagé depuis lui. **Le partage est celui du lecteur, jamais une fonction de Kokoro.**

---

## 3. Le contenu — ce qu'il lit, ce qu'il écrit

**Kokoro n'accède jamais à `psy/outputs/dossier/`.** Il lit et écrit **un seul dossier de transit**, désigné une fois par Xavier *(SAF, URI d'arbre persistant — **aucune permission au manifeste**)*, et c'est le PC qui fait la jonction avec le dossier clinique.

| Sens | Fichiers | Format *(normatif)* | Acheminé par |
|---|---|---|---|
| Kokoro **lit** | `programme.json` · `bibliotheque/*.pdf` · `bilans/*.pdf` | [`PROGRAMME.md`](PROGRAMME.md) | `npm run psy:publish` |
| Kokoro **écrit** | `journal/AAAA-MM-JJ.json` · `reponses/AAAA-MM-JJ-HHMM-<id>.json` | [`../psy/DOSSIER.md`](../psy/DOSSIER.md) | `npm run psy:sync` |

⭐ **Ni profil, ni état, ni séances, ni crises, ni mesures, ni briefs, ni supervisions ne quittent le PC.** **Le contenu publié est *dérivé*, jamais *extrait*** : il porte ce qu'il y a à faire, jamais ce qui a été constaté, mesuré ou diagnostiqué.

⚠️ **Le bilan est la seule exception, et elle est écrite** : ce n'est ni un dérivé ni un extrait du dossier, **c'est un compte rendu que Xavier possède déjà**, mis en forme et versé tel quel. Canal distinct `bilans/`, contrôles propres *([`PROGRAMME.md` §6](PROGRAMME.md))*.

🔴 **`outputs/` est append-only** *(règle R2)*. `psy:sync` n'écrase jamais un fichier existant, et **un doublon Drive ne se supprime jamais sans être lu : c'est une donnée clinique.**

🔴 **Un dossier Drive n'est pas un système de fichiers** — il accepte deux fichiers du même nom sans le signaler. Garde **double** : jeton local de date côté Kokoro, refus d'écrasement côté PC.

**Les sept familles d'interdits sont vérifiées des deux côtés**, et les deux réactions diffèrent volontairement :

- **`npm run psy:publish` refuse la publication entière.** Sur le PC on peut corriger — donc on corrige, on ne publie pas à moitié.
- **Kokoro écarte la seule fiche fautive** et affiche le reste *(`programme/Interdits.kt`, mêmes expressions que `psy-publish.ts`)*. Sur le téléphone on ne peut pas corriger, et perdre tout le programme pour une ligne serait pire.

---

## 4. Les invariants, et le test qui les tient

Ils viennent des contraintes de Xavier *(détail : [`../patient/README.md`](../patient/README.md))*, pas d'un goût. **Une contrainte de conception qui reste une phrase se perd à l'implémentation** — d'où la colonne de droite.

| Invariant | Ce qui le tient |
|---|---|
| Jamais de son ni de vibration | **Aucune permission audio**, aucun appel `Vibrator` — `InvariantsSourcesTest` refuse l'API elle-même |
| Aucun service tiers | **La permission INTERNET n'est pas déclarée** ; `InvariantsSourcesTest` refuse tout appel réseau. Police et images sont embarquées |
| Aucune visualisation, aucune cotation de ressenti, aucun streak, aucun numéro d'urgence, aucun déclenchement sur prodrome | `InvariantsTextesTest` lit **`strings.xml` en entier** ; `Interdits.kt` filtre **le contenu publié**, que les tests ne voient pas |
| Transitions lentes et continues | Expressions et postures **≥ 800 ms**, easing continu. Aucune apparition instantanée, aucun *cut*, aucun rebond |
| Utilisable sans parler ni écrire | **Tout champ obligatoire est un nombre ou un choix fermé** (R5). Le texte libre est facultatif et jamais bloquant |
| Aucun personnage sur un panneau de crise | Deux tests de sources : aucun dessin de personnage dans `crise/`, et `CriseActivity` pose `LocalPanneauPorte` à `false` — **le risque n'est pas d'en ajouter un exprès, c'est qu'il arrive par un paramètre par défaut** |
| Le dessin et le code ne divergent pas | `CorpsInvariantsTest` **relit `retenus/kokoro-corps-v2.svg` à chaque build** — tracé, matrice, épaisseur, remplissage. Un chiffre qui bouge d'un côté fait échouer le build |
| L'apparence ne change jamais sans annonce | Tout changement visuel entre deux versions est annoncé **avant** installation |

---

## 5. Le monde

### L'anneau

```
  ← … │ Thérapie │ Documentation │ Bilan │ Crise │ Thérapie │ … →
        entrée                                      (le même)
```

**Quatre écrans, une seule direction, aucun bout.** Après le dernier vient le premier, **et le décor continue dans le même sens** : ce n'est pas un retour en arrière, c'est un tour de plus. ⭐ **La crise est donc à un seul geste de l'ouverture, dans les deux sens.** `EcranTest` le vérifie.

- **Une rubrique par écran** *(celles du contrat)* : un contenu ne change jamais de place, l'interface n'arbitre rien.
- **Le glissement horizontal traverse le monde, le vertical va à la liste** — deux gestes, deux destinataires, aucun arbitrage, aucun axe à verrouiller. **La crise, elle, ne défile jamais.**
- **La caméra colle au doigt** *(une valeur ordinaire, pas une animation)*, et un ressort à **amorti critique ~600 ms** la reprend **à la vitesse qu'elle avait** — un seul mouvement, jamais deux séparés par un arrêt. On ne saute jamais deux écrans. Deux façons d'arriver au bout : la distance *(18 % d'un écran)* ou l'élan *(0,7 écran/s)*.
- **Un titre en toutes lettres sur un ruban qui ne défile pas** ; les trois `quand` sont des **pancartes écrites** — *Aujourd'hui* · *Quand j'en ai besoin* · *Sans date*. 🔴 **Aucune pastille, aucun badge.**
- **Tout ce qui s'ouvre monte du bas en 320 ms** et se ferme **d'une croix en haut à droite ou par le bouton *retour***, jamais d'un geste : rien ne concurrence la traversée.

### Le décor

**Quatre couches peintes** — `app/src/main/res/drawable-nodpi/decor_*.webp`, ≈ 310 ko au total. 🔴 **Seule dérogation à « aucun bitmap » : une couche n'a aucune articulation, donc rien à animer.** Le personnage, lui, reste vectoriel sans exception. **Le ciel n'est pas une image** : c'est un dégradé vertical peint sous la pile.

| Couche | Profondeur | Largeur | Répétition |
|---|---|---|---|
| `decor_nuages_loin` | 0,14 | 1,40 | miroir |
| `decor_nuages_pres` | 0,30 | 2,40 | simple, marge 0,16 |
| `decor_collines` | 0,52 | 3,60 | 🔴 miroir *(couche de sol : elle va bord à bord, sinon elle découvre le ciel)* |
| `decor_feuillage` | 0,78 | 1,90 | simple, marge 0,16 |

- **Parallaxe horizontale seule**, non bornée — c'est ce qui rend l'anneau gratuit. **Aucun débattement vertical.**
- ⭐ **L'inclinaison du téléphone s'ajoute au doigt** — `TYPE_GRAVITY`, **une position, jamais une vitesse** : pas de gyroscope, donc **aucune dérive, aucun recentrage, réversibilité exacte**, et téléphone posé le décor est immobile. Course **±18°**, débattement **0,40 écran**, lissage ≈ ⅓ s *(la main tremble)*. **Elle ne touche que le décor** et n'a aucun chemin vers la traversée. Capteur branché seulement quand le monde est à l'écran.
- **Deux interrupteurs séparés** aux réglages — *décor en parallaxe* et *suivre l'inclinaison* : ce sont deux gênes différentes. La seconde ligne disparaît quand la première est coupée, ou quand le téléphone n'a pas le capteur.
- ⭐ **La nuit est une plage horaire, et rien d'autre** — 21 h → 6 h par défaut, réglable, désactivable. 🔴 **Lue à l'arrivée dans le monde, jamais bascule sous les yeux, et jamais le thème du système** : un décor qui suit Android changerait parce qu'un réglage étranger a bougé. **Kokoro, lui, garde ses couleurs jour et nuit.**
- 🔴 **Le décor ne porte jamais de texte, ne bouge jamais tout seul, ne contient aucun être vivant** — un regard dans le décor est une présence de plus à décoder.

### La matière

**Des panneaux opaques à gros contour posés sur le paysage** — registre du GUI de jeu kawaii. Le décor ne se voit plus *à travers* l'interface : **il se voit entre les panneaux**, et c'est ce qui commande les écarts généreux entre les cartes. **Aucun flou nulle part** : un flou système dépend d'un réglage qu'Android coupe, et un texte sur fond flouté change de lisibilité selon ce qui passe derrière.

- **Une seule recette** *(`ui/Matiere.kt`)* : dégradé, contour **4 dp brun — jamais noir**, reflet haut, creux bas, **épaisseur portée 7 dp**, ombre, rayon 26 dp.
- **Cinq couleurs, et il n'y en aura pas d'autres** — menthe, pêche, lavande, azur, beurre. 🔴 **Aucun rouge, écran de crise compris** : la palette n'en contient pas, ce qui rend la règle tenable au lieu de la laisser à la vigilance. ⭐ **Le vert de *Fait* n'a pas de contraire** — ni orange *en retard*, ni gris *pas fait* : **il n'y a pas de retard dans ce dispositif.**
- **La couleur distingue les sections, elle ne les classe jamais** — 🔴 aucune teinte ne doit pouvoir se lire comme *urgent* ou *en retard*.
- **Typographie : Varela Round, embarquée dans l'APK** *(SIL OFL 1.1, `app/licences/`)*. Corps 18 sp, titres 23 sp, rubans 25 sp, interligne large — **lisible en shutdown, c'est-à-dire quand on ne peut plus faire d'effort.** Boutons pleine largeur ≥ 66 dp, libellé en toutes lettres.
- ⭐ **Le retour au toucher est l'enfoncement du panneau** — il descend de ses 7 dp en **90 ms** et s'arrête net. 🔴 **Aucun rebond, aucun dépassement, aucune onde.**
- **Les ornements** *(étincelles, cœurs, rivets)* sont **du décor pur** : jamais porteurs d'information, jamais sur une carte de liste.
- 🔴 **Rien de ce que le style de jeu apporte d'habitude n'entre ici** : pas de barre de progression, pas de jauge, pas d'étoile, pas de niveau, pas de score. **C'est le seul rayon du registre où l'on ne prend rien.**
- **L'écran de crise garde la matière et perd la décoration** : aucun ornement, texte 21 sp, boutons 88 dp. **En crise, la mignonnerie est du bruit** — il se distingue en étant plus grand et plus vide, pas plus vif.

---

## 6. Le personnage

🔴 **Le dessin fait foi : [`ressources/retenus/kokoro-corps-v2.svg`](ressources/retenus/kokoro-corps-v2.svg).** `corps/Geometrie.kt` en porte le tracé et la matrice **caractère pour caractère** ; il ne redessine rien. Deux variantes le complètent — `-sleep.svg` *(la pose de sommeil)* et `-right.svg` *(la pose de vol, dont le vol vers la gauche est le miroir calculé)*.

**Nommé, expressif, muet.** Il communique **par texte uniquement** : une voix qui surgit est une agression sensorielle, tandis que le texte se relit à froid, ne force pas le tempo, et **reste lisible en shutdown** — précisément quand le canal verbal est coupé.

**Un petit robot kawaii en 2D, ligne claire, vue de face**, découpé en **quinze pièces** riggées sur trois pivots dérivés du dessin. Tête plus large que haute, corps en poire, bras et pieds flottants **sans main ni doigt** — *une main fait signe, un doigt accuse* —, un **心** à l'encre côté cœur. **Ni cou, ni oreille, ni vêtement, ni antenne** : une antenne est un indicateur d'humeur, donc une information à décoder. Trois valeurs, **aucune couleur, aucun signal**.

**Six expressions, le jeu est fermé** — `neutre`, `serein` *(par défaut, semi-sourire)*, `attentif`, `chaleureux`, `clignement`, `veille`. Tout se joue sur le panneau-visage : deux yeux, une bouche.

- 🔴 **Aucun sourcil, jamais.** Le sourcil est le porteur principal du reproche : en ne le dessinant pas, on rend le reproche **littéralement indessinable**.
- 🔴 **Les commissures ne tombent jamais**, morphing compris — une forme intermédiaire est une combinaison convexe des deux silhouettes, et `CorpsInvariantsTest` le vérifie sur les seize couples de bouches, sur toute la durée.
- 🔴 **`chaleureux` ne se déclenche que sur un fait accompli, et son absence n'est pas un message.** Il n'existe aucune expression « déçu », et il n'en existera jamais.
- **Il ne suit jamais Xavier du regard** : le regard est un **axe réglé par la posture**, et **rien dans le code ne connaît la position de Xavier**.
- ⭐ **Le visage se déforme, il ne se fond pas** — un fondu croisé montrerait deux visages à la fois pendant 800 ms, sur un panneau qui n'en porte que trois traits. Chaque tracé est réduit à une silhouette de 24 points découpée toujours pareil : **la correspondance est construite, pas cherchée.**

**Dix postures, le jeu est fermé** — `repos`, `present`, `montre`, `cote-a-cote`, `retrait`, `pensif`, `lecture`, `notes`, `accoude`, `sommeil`. 🔴 **Aucune ne porte d'information** : ne pas la reconnaître ne fait rien perdre, et c'est ce qui interdit d'ajouter un accessoire pour les rendre lisibles — livre, lunettes et calepin n'existent pas. *(`allonge`, pour l'écran vasovagal, reste à dessiner : une silhouette allongée est une **structure externe**, pas une consigne à imaginer.)*

**Deux régimes, jamais les deux à la fois** *(une seule instance à l'écran)* :

| | **L'habitant** | **Le locuteur** |
|---|---|---|
| Où | Dans le décor, à la bande que chaque écran lui réserve | En bas à gauche d'un panneau plein écran |
| Taille | **60 dp** *(48 dp est exclu : le contour y tombe sous 2 px et le trait se délave)* | **≈ 110 dp**, cadré au thorax — ce qui doit se lire est un visage |
| Ce qu'il porte | Une **posture** | Une **expression** |

- **Ordre de peinture** : décor · **ombre · Kokoro** · panneaux. 🔴 **« Il ne passe jamais devant un texte » est une conséquence de l'ordre, pas une découpe** — tout panneau le recouvre mécaniquement.
- **Respiration : cycle 3,8 s**, le ventre grossit **vers le haut** *(×1,18)*, tête et bras suivent, pieds immobiles. 🔴 **Le rythme ne change jamais** — une respiration qui varie devient une information à décoder. 🔴 **Ce n'est pas un guide respiratoire**, et aucun texte n'invite à s'y caler.
- **Clignement : yeux seuls**, intervalle aléatoire **2 800–6 500 ms**, morphing 80 ms — deux intervalles consécutifs ne sont jamais égaux, *un clignement régulier serait un métronome.*
- **Lévitation 3 800 ms**, transit entre écrans **700 ms** en arc de 26 dp avec 200 ms de retard sur le décor. **Il vole dans la posture de départ et prend celle d'arrivée en se posant** ; 🔴 **la pose de vol remplace la posture, elle ne s'y ajoute pas.**
- 🔴 **Il ne se déplace jamais de lui-même** : il lévite sur place et transite **sur commande du doigt**. Ni sursaut, ni réaction au toucher, ni mouvement pendant qu'un texte se lit — panneau ouvert = habitant **hors champ**, locuteur **immobile hors expression**.
- 🔴 **Il ne vient jamais vers Xavier, y compris avec son corps** : l'interdit est entier sur l'axe de profondeur — rien ne grandit vers l'avant. Les entrées et sorties **latérales** sont admises, à hauteur constante.

### ⭐ L'écran de crise — *« Kokoro veille sur toi »*

**La seule dérogation, et son motif vient de Xavier.** Une première version *(présence muette, panneau éteint, pose affaissée)* avait été **refusée par la supervision** : sur l'écran de crise, une pose qui n'apporte rien est du bruit. Ce qui l'a débloquée n'est pas un argument, c'est un changement d'objet — **un visage bienveillant, panneau allumé, accoudé au bouton *Mot code* comme à un muret**, tête penchée de 6°.

🔴 **Six bornes, un test pour chacune :**

1. **Cette scène-là et elle seule.** La notification ouvre **la même scène, figée** — même décor **sans parallaxe**, même personnage **sans transit, sans souffle, sans clignement**. *On ne s'impose pas par-dessus le verrouillage avec quelque chose qui remue.* 🔴 **Les panneaux de crise n'en portent aucun.**
2. **Aucun texte ajouté** : les trois boutons ne bougent ni de place, ni de taille, ni de libellé.
3. **Il ne vole pas et ne respire pas une fois posé** — seule place du dispositif dans ces deux cas. Il est **accoudé, pas posé au sol** : le faire léviter ferait glisser ses bras le long du bord et **son ombre tomberait sur l'interface**.
4. **Il ne s'y endort jamais.** Pas de liste, et veiller est ce qu'il y fait.
5. **Rien n'y dépend du dossier** — ni de l'heure, ni du check-in. Une seule pose, toujours la même.
6. **La tête penche de 6°, bornée à 10°**, et c'est la seule inclinaison du dispositif. 🔴 **Le corps reste de face** : c'est ce qui distingue *veiller sur quelqu'un* de *le fixer*.

> 🔴 **Une dérogation bornée, assumée, qui ne s'étend pas** : le fond de la notification porte une illustration peinte où Kokoro lève la main en V et fait un clin d'œil — quatre écarts avec le rig, demandés par Xavier après qu'ils lui ont été présentés un par un. **Elle n'entre pas dans le rig, ne crée pas de septième expression, ne s'étend à aucun écran et ne ramène aucun texte.** ⭐ **Le pari est qu'une image qu'on ne peut pas rater ne demande rien en retour. Si elle finit par se lire comme une attente, elle se retire.**

---

## 7. Android — ce qui est câblé, et pourquoi

**Kotlin natif + Jetpack Compose.** Cible **Samsung Galaxy S22 / One UI**, app personnelle **sideloadée** — aucune contrainte Google Play. C'est le seul morceau du projet qui sort du TypeScript strict imposé par les règles projet : `showWhenLocked` et full-screen intent sont des APIs natives ; en cross-platform ce sont des ponts fragiles.

**Cinq permissions, et pas une de plus** — `POST_NOTIFICATIONS`, `USE_FULL_SCREEN_INTENT`, `WAKE_LOCK`, `SEND_SMS`, `RECEIVE_BOOT_COMPLETED`. 🔴 **Ni INTERNET, ni audio, ni stockage** *(le dossier de transit passe par SAF)*.

| Point | Ce qui est fait |
|---|---|
| **S'afficher par-dessus le verrouillage** | ✅ `CriseActivity` déclare `showWhenLocked` + `turnScreenOn` **au manifeste**, dans sa propre tâche *(`taskAffinity=""`, `singleTask`, `excludeFromRecents`)*. 🔴 **Posé à l'exécution, c'est trop tard** — le keyguard a déjà décidé de demander le code. **Le monde ne s'affiche donc jamais par-dessus le verrouillage**, et il n'essaie plus |
| **Réveiller l'écran** | ✅ Full-screen intent sur canal `IMPORTANCE_HIGH` **muet** *(`setSound(null, null)`, vibration désactivée)* **+ `WAKE_LOCK`**, sans lequel l'Always On Display s'intercale |
| **Ne jamais saisir l'écran en cours d'usage** | ✅ Garanti par Android : un full-screen intent est rétrogradé en bannière dès que l'écran est allumé |
| **Canaux de notification** | ⚠️ **Un canal est immuable une fois créé.** Identifiants versionnés — `kokoro_acces_v1`, `kokoro_alerte_v1`. **Nom et description se mettent à jour ; l'importance et le silence sont figés** : changer l'identifiant rendrait à Android le droit de resonner |
| **Publication de la notification** | ✅ **Le monde la republie à chaque venue.** ⚠️ Une app fraîchement installée est *arrêtée* pour Android — **rien ne s'exécute avant le premier lancement manuel**, qui est donc le plus tôt possible |
| **Quand la porte est fermée** | ✅ Un **avis en toutes lettres** en tête de l'écran d'entrée quand la notification n'a pas pu s'afficher. 🔴 **Jamais une pastille sur la roue dentée** — un défaut se dit, il ne se signale pas par un point |
| **Autorisations** | ✅ Demandées **depuis les réglages, jamais à l'ouverture** — une app qui réclame une permission dès qu'on l'ouvre vient vers Xavier. Sans l'autorisation, la publication renonce en silence |
| **Accès aux fichiers** | ✅ **SAF, URI d'arbre persistant.** `MANAGE_EXTERNAL_STORAGE` écarté — il ouvre tout le stockage pour un seul dossier |
| **Écran verrouillé permanent** | ⏸️ Pas d'overlay ni de service de premier plan : `TYPE_APPLICATION_OVERLAY` passe **sous** le keyguard, et One UI tue les services. Piste ouverte : un fond d'écran vivant *(`WallpaperService`, aucune permission)* |

**Les réglages** *(panneau interne, roue dentée sur la bande de titre de l'écran d'entrée)* : contact et message du mot-code · état des trois autorisations avec le guidage pour chacune · republier l'accès crise · choisir le dossier de transit · la plage de nuit · les deux interrupteurs du décor · un test d'alerte.

---

## 8. Construire et déployer

```bash
npm run companion:kokoro          # depuis la racine
./kokoro                          # depuis companion/android/ — la même commande
npm run companion:kokoro -- pose  # sans repasser les tests
```

**Jamais `gradlew` ni `adb` à la main.** Le script sort **un verdict par étape**, et en cas d'échec **seulement l'extrait qui l'explique** — la ligne du compilateur, ou le test tombé avec son message d'assertion tiré du XML de résultats. Rien n'est perdu : tout part dans `build/kokoro.log`, que `./kokoro journal` relit.

| Sous-commande | |
|---|---|
| `./kokoro` | Tout : tests, APK, installation, ouverture, contrôle de plantage |
| `test` · `apk` · `pose` | Tests seuls · compilation seule · compilation + installation + ouverture |
| `lien` | Noue ou renoue le lien sans fil *(câble branché une fois)* |
| `appairer <IP:port> <code>` | Appairage sans fil — *Options de développement › Débogage sans fil › Appairer avec un code* |
| `journal` · `plantage` | Les dernières lignes du dernier build · le tampon de plantage du téléphone |
| `-v` | Laisse Gradle parler comme d'habitude |

⭐ **Le câble n'est pas nécessaire** : PC et téléphone sur le même Wi-Fi suffisent, et **le lien se renoue tout seul** quand aucun téléphone ne répond. L'adresse est retenue dans `.kokoro-sansfil` *(non versionné)*, avec repli sur l'annonce mDNS. 🔴 **Le port 5555 ne survit pas à un redémarrage du téléphone** — c'est Android qui l'impose, pas le script : rebrancher le câble une fois, ou repasser par `appairer`. ⚠️ **Câble et Wi-Fi ensemble : c'est le câble qui sert.**

⚠️ **`pose` ouvre `MondeActivity`**, la seule activité exportée — One UI refuse `am start` sur une activité qui ne l'est pas.

**Outillage** : JDK 21, SDK Android platform 36 / build-tools 36 / platform-tools 37.0.1, `sdk.dir` dans `local.properties` *(non versionné)*. **Pas d'Android Studio sur ce poste** — ligne de commande seule.

---

## 9. Le pipeline d'images

```bash
npm run companion:image -- decor-feuillage --base=_decor --format=21:9 --taille=2K --n=2
npm run companion:decoupe -- decor-feuillage/02-b.png \
  companion/android/app/src/main/res/drawable-nodpi/decor_feuillage.webp \
  --seuil=0.72 --plein=0.30 --marge=0.04
npm run companion:icone           # 🔴 aucune image d'icône ne se retouche à la main
```

- **La charte vit dans `prompts/_base.md` *(personnage)* et `_decor.md` *(décor)***, préfixée à chaque appel. Itérer = modifier trois lignes de la variante, pas réécrire la charte.
- ⭐ **Le fond est magenta `#FF00FF`, pas transparent** : le modèle ne rend pas d'alpha, et à qui lui demande « fond transparent » il **peint un damier gris et blanc**. Le script mesure `min(R,B) − V`, rampe l'alpha entre `--plein` et `--seuil`, démultiplie la couleur, et neutralise la frange lilas **par sa symétrie** — le magenta a exactement autant de rouge que de bleu, un pétale n'en a jamais autant.
- 🔴 **`--marge` n'est pas un cadrage, c'est un garde-fou** : la charte demande aux couches sans miroir des bords vides, le modèle les respecte à peu près, et un éclat collé au bord réapparaîtrait au milieu du ciel à chaque tuile. ⚠️ **Jamais sur la prairie** : elle y perdrait son pied.
- **`sorties/` n'est pas versionné.** Ce qui est retenu est **promu à la main dans `retenus/`**, qui l'est. Les planches magenta d'origine y sont conservées : on peut re-détourer sans rejouer le modèle.
- ⚠️ **Rien de ce que le modèle sort n'entre dans le personnage** — ce sont des planches de recherche. Le corps est dessiné à la main par Xavier, au vectoriel.

---

## 10. Carte

| Chemin | Rôle |
|---|---|
| 🔴 [`PROGRAMME.md`](PROGRAMME.md) | **NORMATIF — le format du programme et de la bibliothèque.** Les six types d'étape, les rubriques, les interdits |
| [`android/`](android/) | **Le code** — `app/src/main/kotlin/io/allonsy/kokoro/` : `monde/` *(l'anneau, les écrans, les étapes)* · `corps/` *(le rig et les expressions)* · `decor/` *(les couches et l'inclinaison)* · `crise/` *(les trois portes)* · `journal/` *(le check-in et le dossier SAF)* · `programme/` *(la lecture et les interdits)* · `reglages/` · `ui/` *(la matière)* |
| [`inputs/`](inputs/) | 🔴 **Ce que Claude Psy lui donne** : `programme.json` + [`bibliotheque/`](inputs/bibliotheque/README.md). **Écrit par le psy seul, jamais sans supervision.** La documentation se publie à tout moment ; **les étapes qui font agir, à la clôture d'une séance** |
| [`outputs/`](outputs/) | 🔴 **Ce que Kokoro produit** : `journal/` *(check-ins)* + `reponses/` *(ce qui a été fait)*. **Écrit par Kokoro seul, append-only** |
| [`ressources/retenus/`](ressources/retenus/) | **Ce qui fait foi** — `kokoro-corps-v2.svg` *(+ `-sleep`, `-right`)*, `kokoro-face.svg`, `logo.jpg`, les quatre planches magenta du décor, le fond de la notification |
| [`ressources/prompts/`](ressources/prompts/) · `sorties/` | La recherche graphique — les chartes et les variantes · les candidats *(non versionné)* |
| [`ressources/maquette/`](ressources/maquette/) | `kawaii.html` — la maquette du thème qui fait foi |
| [`scripts/`](scripts/) | `companion-image.ts` · `companion-decoupe.ts` · `companion-fondu.ts` · `companion-icone.ts` |

> ⚠️ **`inputs/` et `outputs/` sont du contenu clinique**, malgré leur nom d'interface. ⭐ **`inputs/` dit à qui la chose est destinée, pas qui l'a tapée** : le programme est écrit par Claude Psy et vit pourtant ici — il est **donné** à Kokoro.
