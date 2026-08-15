# Kokoro — l'interface du monde

**v2 — 14/08/2026.** Le monde devient **l'interface principale** de l'app, et jusqu'à l'écran de veille.

> 🔄 **v2 — le thème change, et lui seul.** Le verre dépoli de la v1 est abandonné au profit de **panneaux extrudés** (§4), après maquette. **Le rangement, les points durs, les onze décisions et tout le contenu sont inchangés** — seule la peau est réécrite, plus D6 qui la nommait et P4/P5 qui n'existaient qu'à cause du flou.

> 📐 **Onze décisions tranchées par Xavier le 14/08/2026** — §5. Le décor est spécifié dans [`DECOR.md`](./DECOR.md), le personnage dans [`CORPS.md`](./CORPS.md), le contrat de contenu dans [`companion/PROGRAMME.md`](PROGRAMME.md) — **ce document ne fait que ranger** ce que ces trois-là ont décidé.
>
> 📌 **Il ne décide jamais quel contenu clinique existe.** Ça se décide en séance.
>
> ⏳ **Ce qui reste ouvert** — les bornes des nouveautés (§6.1), l'essai du fond d'écran vivant (§6.3), la cadence de l'entraînement en solo (§6.4), et le comportement du personnage, renvoyé à son propre brainstorm.
>
> 🏗️ **Écrit en Compose le 15/08/2026** — la matière (§4.1, §4.2) et les cinq écrans (§3) ; détail en **§7**. **Deux points ouverts ont été tranchés par Xavier pour que ça puisse être codé** : les pancartes gardent leurs deux couleurs *(§6.5, option B)*, et la police arrondie est **Varela Round** *(§4.3)*.
>
> ✅ **Le même jour, les quatre surfaces déjà éprouvées y sont passées aussi** — crise, check-in, réglages *(§7.1)*, **à la demande de Xavier**. **Il n'y a plus deux thèmes**, et le mot-code part désormais d'un seul appui.

---

## 1. Ce qu'il y a à ranger

### 1.1 Ce qui est déjà construit et tourne sur le téléphone

| Surface | Jalon | Ce que c'est | Taille à l'écran |
|---|---|---|---|
| **Accès crise** | K2 ✅ | Notification sur l'écran verrouillé — **une porte, pas un rappel** | hors monde |
| **Mot-code à Chourouk** | K2 ✅ | SMS **envoyé en un appui** depuis l'écran **Crise** *(§7.1)*, téléphone verrouillé, sans réseau data. L'écran plein reste le chemin de la notification et le repli en cas d'échec | 1 écran plein |
| **Tension appliquée** | K3 ✅ | 4 repères externes enchaînés, minuteur, critères d'arrêt à un tap | 1 écran plein, guidé |
| **Phrase pour le soignant** | K3 ✅ | Texte montrable en plein écran | 1 écran plein |
| **Check-in du jour** | K4 ✅ | 11 champs fermés, aucune saisie de texte, écrit `journal/` | 1 écran, questions enchaînées |
| **Le monde** | — ✅ | 5 écrans en croix, décor 4 couches, plage de nuit | le contenant |
| **Le corps** | K7 ⏸️ | 6 expressions · 5 postures + `allonge` · respiration · désignation | posé dans le décor |
| **Écran de contrôle** | support | Contact, autorisations, dossier SAF, plage de nuit, test d'alerte | liste qui défile |
| **Atelier du corps** | support | Outil de mise au point du rig | liste qui défile |

### 1.2 Ce que le contrat §8 apporte, et que Kokoro ne lit pas encore *(K5, K6)*

**Six types d'étape × quatre rubriques × trois `quand`.** C'est tout le vocabulaire — il n'y en aura pas d'autre.

| Type | Ce que ça demande à l'interface |
|---|---|
| `ecran` | Ouvrir une fonction déjà construite (les cinq du 1.1). **Refuser un nom inconnu**, plutôt qu'une ligne morte |
| `exercice` | Une consigne + un minuteur + **« je peux arrêter avant la fin »** toujours affiché |
| `questionnaire` | Une question par écran, choix fermés ou compteur, « passer » écrit `null` |
| `demarche` | Un détail à lire, un seul bouton *fait*. **« Pas encore fait » n'existe pas** |
| `fiche` | Un texte court, ou un document Markdown de la bibliothèque. `montrable` = plein écran pour un tiers |
| `seance-duo` | 🔴 Le plus exigeant : **signal d'arrêt rappelé en permanence** · critères d'arrêt à un tap · mode entraînement · séquence chronométrée alternant `aide` / `patient` |

**Plus deux choses qui ne sont pas des étapes :** la **bibliothèque** (Markdown, fiches longues), et la **ligne discrète** qui dit qu'une nouvelle version est arrivée — jamais une notification.

### 1.3 Le contenu qui existe aujourd'hui dans le dépôt

| | Combien | Détail |
|---|---|---|
| **`programme.json` v1** | **11 étapes** | 1 `aujourdhui` (check-in) · 3 `au_besoin` (les trois écrans de crise) · **7 `sans_date`** — les 6 démarches du palier 0 PPC + l'email au Dr Isorni |
| **Bibliothèque** | **0 fiche** | Le README seul. 🔴 Les 8 protocoles de `psy/docs/protocoles/` **ne se copient pas** (C9) — ils se réécrivent pour Xavier |
| **Échelles** | 8 au corpus | Publiables en `questionnaire`, **sauf le PHQ-9, jamais** |
| **Bilans** | 0 | Un bilan est un **texte daté écrit en séance**, pas un calcul de l'app |
| **Séances à deux** | 0 | K6 |

> ⭐ **L'écran de la thérapie sera long avant d'être riche :** 7 démarches administratives PPC sans date. C'est ce qui commande le point dur **P1**.

### 1.4 Ce qui n'entre dans aucun écran

Aucun score · aucune progression · aucun historique · aucun palier atteint · aucun streak · aucune notification ni relance · aucun numéro d'urgence, **3114 compris** · le PHQ-9 · rien de `psy/outputs/dossier/` (profil, état, séances, mesures, briefs) · aucune police **distante**, aucun service, aucune image venus d'ailleurs **à l'exécution**.

> ⭐ **Précision ouverte par la v2 :** la police arrondie de §4.3 est **embarquée dans l'APK**, donc hors ligne et figée à la compilation. **C'est exactement ce que la règle ci-dessus vise à garantir** — elle interdit qu'une ressource soit *allée chercher ailleurs pendant que Xavier s'en sert*, pas qu'elle vienne d'un tiers.

---

## 2. Les points durs

| # | Le point | Ce qu'il impose |
|---|---|---|
| 🔴 **P1** | **Un écran du haut ou du bas ne peut pas contenir de liste qui défile verticalement** — le glissement vertical est déjà pris par la traversée du monde. Sur la gauche et la droite, le vertical est libre : la butée y est franche, donc le geste n'y sert à rien d'autre | **Les deux contenus longs vont à gauche et à droite. Le haut et le bas ne portent que ce qui tient en un écran.** C'est ce qui a décidé le rangement du §3 |
| **P2** | Les gestes système de One UI mordent les bords : volet de notifications en haut, retour à l'accueil en bas | Un geste de traversée verticale part **du milieu de l'écran**. À mesurer sur l'appareil |
| **P3** | Le décor ne porte jamais de texte ([`DECOR.md`](./DECOR.md) §7) | Le texte est sur une **surface posée sur le décor**, jamais peint dessus |
| ✅ **P4** | ⭐ **Le flou ne doit jamais venir du système.** `Window.setBackgroundBlurRadius` dépend d'un réglage Android que l'économiseur de batterie coupe — l'apparence changerait parce qu'un réglage étranger a bougé, **exactement ce que la doctrine interdit au thème sombre** | ✅ **Sans objet depuis §4 : il n'y a plus de flou du tout.** 🔴 **Le point reste écrit parce qu'il juge d'avance toute réintroduction** — une surface qui floute retombe sous P4, et devra calculer son flou elle-même |
| ✅ **P5** | ⭐ **La lisibilité ne doit jamais dépendre de ce qui est derrière.** Un texte posé sur un décor flouté passe du feuillage sombre au ciel clair en glissant | ✅ **Réglé par l'opacité** : les panneaux de §4 sont pleins, le décor passe **entre** eux et jamais dessous. 🔴 **Le point reste écrit parce qu'il interdit toute surface semi-transparente portant du texte** |
| **P6** | L'overlay de veille : `TYPE_APPLICATION_OVERLAY` ne passe pas au-dessus du keyguard sur Android récent | Piste retenue à essayer : **fond d'écran vivant** — §5, D8 |
| 🔴 **P7** | ⭐ **La notification de crise est lue toute la journée** — elle est permanente sur l'écran verrouillé, et elle porte aujourd'hui les mots *mot-code* et *tension appliquée* trois fois : dans son texte et sur ses deux boutons. **Xavier, 14/08/2026 : « je lis toute la journée mot code et tension appliquée. Ça n'aide pas mes angoisses. »** | ⭐ **Quatrième instance du motif du 10/08** *(retrait des numéros d'urgence)* : **un secours affiché en permanence devient un rappel permanent du danger.** Voir **§6.2** |
| **P8** | En face, la notification donne le mot-code **en un tap** depuis l'écran verrouillé, sur une activité éprouvée pour de vrai le 10/08 | Tout chemin qui passe par le monde ajoute un tap et un chargement **au pire moment**. Les deux exigences se concilient — **§6.2** |

---

## 3. Le rangement — acté (D1, D2)

```
                 HAUT
              ┌─────────┐
              │  Bilan  │
              └─────────┘
┌──────────┐  ┌─────────┐  ┌───────────────┐
│ Thérapie │  │ Kokoro  │  │ Documentation │
└──────────┘  └─────────┘  └───────────────┘
   GAUCHE       CENTRE          DROITE
              ┌─────────┐
              │  Crise  │
              └─────────┘
                  BAS
```

**Une rubrique par écran, celles du contrat §8.3.** Un contenu ne change donc jamais de place — la rubrique est écrite dans le JSON et ne bouge pas, alors que `quand` bouge tous les jours. L'interface n'arbitre rien : toute étape publiée sait déjà où elle va.

| Écran | Contenu | Pourquoi là |
|---|---|---|
| **CENTRE** | **Kokoro qui respire** · la **roue dentée** en haut à droite (D4) · **les nouveautés**, quand il y en a (D5) | C'est le lieu où l'on revient |
| **GAUCHE** | **Thérapie** — les prochaines actions de la thérapie en cours, groupées par *aujourd'hui* / *quand j'en ai besoin* / *sans date* | La liste la plus longue : elle défile, donc axe horizontal (**P1**) |
| **DROITE** | **Documentation** — la bibliothèque, une fiche par ligne | Deuxième liste appelée à grandir. Même motif |
| **HAUT** | **Bilan** — les questionnaires à passer, les comptes rendus écrits en séance | Rare, court, tient en un écran. Aucun chiffre, aucune courbe |
| **BAS** | **Crise** — mot-code · tension appliquée · phrase pour le soignant | Trois grands boutons, **jamais de défilement** — c'est une exigence en soi, pas une conséquence |

### 3.1 À quoi ressemble un écran de bord

- **Un titre écrit en toutes lettres**, posé en haut et qui ne défile pas (**D11**).
- **Les trois `quand` sont des sous-titres écrits** — *Aujourd'hui* · *Quand j'en ai besoin* · *Sans date*. 🔴 **Aucune pastille, aucun badge.** ⭐ **La couleur distingue les sections, elle ne les classe jamais** *(§6.5, tranché le 14/08/2026)* — 🔴 **et il reste interdit d'aligner la palette sur une échelle d'urgence** : aucune teinte ne doit pouvoir se lire comme *urgent*, *en retard* ou *important*.
- **Une carte par étape** : le titre, la durée si elle est connue, rien d'autre. Pas de chevron, pas d'aperçu, pas de compteur.
- **Le décor reste visible** autour et entre les cartes — on est toujours dans le même monde, jamais dans une autre application.
- **Une étape ouverte prend l'écran entier** et se ferme d'un bouton écrit *Fermer*, jamais d'un geste : rien ne doit concurrencer la traversée.

---

## 4. Le thème — panneaux extrudés *(D6)*

**Les surfaces sont des panneaux opaques à gros contour posés sur le paysage** — le registre des interfaces de jeu kawaii. Le décor ne se voit plus *à travers* l'interface : **il se voit entre les panneaux.** C'est ce qui commande les écarts généreux entre les cartes — l'espace vide n'est pas de la respiration graphique, c'est là que passe le monde.

> 📌 **Ce thème remplace le verre dépoli**, retenu le 14/08/2026 puis abandonné le même jour après maquette. **Le verre ne se réintroduit pas par morceaux** : une surface qui floute a repris l'ancien thème. La maquette du verre reste consultable en archive — [`ressources/maquette/index.html`](ressources/maquette/index.html) — et la maquette qui fait foi est [`ressources/maquette/kawaii.html`](ressources/maquette/kawaii.html).

### 4.1 La recette

**Une seule recette de matière**, déclinée en couleur. Un panneau, c'est six couches empilées dans cet ordre :

| | Jour | Nuit |
|---|---|---|
| Fond, dégradé haut → bas | `#FFF9F1` → `#FFE8D5` | `#473E5C` → `#37304A` |
| Contour | `#6E5A54` — **brun, jamais noir** | `#2C2438` |
| Épaisseur du trait de contour | **4 dp** | idem |
| Reflet, `inset` sur le bord haut | blanc à **95 %**, 4 dp | blanc à **16 %** |
| Creux, `inset` sur le bord bas | `#CEA082` à **32 %**, 7 dp | noir à **30 %** |
| Épaisseur portée, sous le panneau | **7 dp**, couleur du contour | idem |
| Ombre portée | 16 dp de flou, `#5C3E2E` à 38 % | noir à 52 % |
| Rayon | **26 dp** | idem |
| Encre | `#5C463E` | `#FBF4EC` |
| Encre douce | `#9C8378` | `#B4A8C4` |

**Cinq couleurs, et il n'y en aura pas d'autres.** Chacune est un couple *(clair, sombre)* pour le dégradé :

| | Jour | Nuit | Où |
|---|---|---|---|
| **Menthe** | `#7FD3B4` · `#52AE8D` | `#5FB79A` · `#3E8A72` | ruban *Thérapie*, bouton *Fait* |
| **Pêche** | `#FFAB8E` · `#E8836A` | `#E08C74` · `#B96852` | pancartes de section |
| **Lavande** | `#BFA8E6` · `#9A80C7` | `#A38CCB` · `#7E67A6` | ruban *Documentation* |
| **Azur** | `#8CC6EF` · `#66A3D0` | `#6FA6CD` · `#4F82A9` | ruban *Crise* |
| **Beurre** | `#FFD98F` · `#E8B75F` | `#E0BC77` · `#BC9750` | ruban *Bilan*, roue dentée, ornements |

### 4.2 Les pièces

- **Le titre d'un écran est un ruban** à bouts crantés, une couleur par écran, texte blanc gravé d'une ombre. Il ne défile pas (**D11**).
- **Les sous-titres de `quand` sont des pancartes** pleines, texte blanc gravé.
- **Une carte est un panneau**, dans la couleur neutre du fond. **Toutes les cartes sont identiques** — aucune n'est plus grande, plus vive ni marquée.
- **Un bouton est le même panneau**, plein quand il agit *(menthe, azur, lavande)*, neutre quand il ferme.
- **Les ornements** — étincelles, cœurs, rivets — sont **du décor pur**. 🔴 **Aucun n'est jamais porteur d'information**, et aucun ne se pose sur une carte de la liste : ils vivent sur les bandes de titre, les états vides et l'écran central.

### 4.3 Les règles qui ne bougent pas

- **La nuit suit la plage horaire du décor** ([`DECOR.md`](./DECOR.md) §5) — lue à l'arrivée, jamais sous les yeux, jamais le thème système.
- **Typographie : ✅ Varela Round, embarquée dans l'APK** *(tranché le 14/08/2026)*. ⚠️ **Android n'en garantit aucune** — c'est la seule police du dispositif. Licence SIL OFL 1.1, texte complet dans `android/app/licences/varela-round-OFL.txt`. ⚠️ **Elle n'a qu'une graisse** : le gras des rubans, pancartes et boutons pleins est **synthétisé par Android** — à regarder à l'œil sur l'appareil. Corps **18 sp**, titres **23 sp**, rubans **25 sp**, interligne large — lisible en shutdown, c'est-à-dire lisible quand on ne peut plus faire d'effort.
- **Le gras est autorisé** sur les rubans, les pancartes et les boutons pleins — il fait partie de la matière. **Jamais dans un corps de texte.**
- **Boutons pleine largeur, ≥ 66 dp, un par ligne**, libellé en toutes lettres.
- ⭐ **Le retour au toucher est l'enfoncement du panneau** : il descend de ses 7 dp d'épaisseur, en **90 ms**, et s'arrête net. 🔴 **Aucun rebond, aucun dépassement, aucune onde** — un ressort qui repart au-delà de sa position est exactement l'animation brusque que les hypersensibilités interdisent. *(La règle des ≥ 800 ms vaut pour les expressions du visage, pas pour l'accusé de réception d'un appui — un appui qui ne répond pas tout de suite se re-tape.)*
- 🔴 **Aucun rouge, nulle part — écran de crise compris.** Le rouge est une alarme, et l'écran de crise doit faire l'inverse : **il se distingue en étant plus grand et plus vide, pas plus vif.** ⭐ **La palette n'en contient pas** : c'est ce qui rend la règle tenable au lieu de la laisser à la vigilance.
- ⭐ **Le vert de *Fait* confirme une action ; il n'a pas de contraire.** Il n'existe ni orange *en retard*, ni gris *pas fait*, ni rouge *raté* — **il n'y a pas de retard dans ce dispositif**, donc pas de couleur pour en parler.
- 🔴 **Rien de ce que le style de jeu apporte d'habitude n'entre ici** : pas de barre de progression, pas de jauge, pas d'étoiles gagnées, pas de niveau, pas de série, pas de score, pas de pièce, pas de coffre. **C'est le seul rayon du registre où l'on ne prend rien** — et c'est délibéré : §1.4 ne s'assouplit pas parce que la peau change.

### 4.4 Ce que ça coûte, dit franchement

**Le gain :** plus aucun flou, donc **P4 et P5 tombent** et la traversée ne porte plus de calcul par image. Le risque qui inquiétait le plus — rendre le geste saccadé — disparaît avec la matière qui le causait.

**Le prix :** ce style **ne se pose pas sur les composants Material.** Contours épais, épaisseur portée, rubans crantés et creux internes demandent un petit jeu de composables maison — `PanneauExtrude`, `Ruban`, `BoutonEpais`, `Pancarte` — écrits en `Modifier.drawBehind` avec des `Shape` personnalisées. **C'est du travail d'écriture, pas du travail de réglage** : à faire une fois, proprement, avant d'habiller le premier écran. Le verre, lui, tenait en trois `Modifier`.

**Et une dépendance nouvelle :** la police arrondie doit être embarquée. Sans elle, il manque la moitié de l'effet.

### 4.5 L'écran de crise *(D7)*

**Même matière, structure inchangée.** Il garde ses trois boutons, son minuteur, ses critères d'arrêt, ses repères externes — on ne touche qu'à la peau.

🔴 **L'écart assumé : c'est l'écran le moins décoré du monde.** Aucun ornement, aucune étincelle, aucun cœur, une seule couleur de bouton par fonction, texte à **21 sp** et boutons à **88 dp**. **En crise, la mignonnerie est du bruit** — et le principe de §4.3 vaut encore : il se distingue en étant plus grand et plus vide, pas plus vif.

---

## 5. Les décisions — tranchées le 14/08/2026

| # | Décision | Ce qui est acté |
|---|---|---|
| **D1** | Le rangement | ✅ **Une rubrique par écran** (§3) |
| **D2** | Le bord de la crise | ✅ **BAS** |
| **D3** | Kokoro suit-il ? | ⏭️ **Il suivra l'interface — hors sujet ici, renvoyé à son propre brainstorm.** En attendant il reste au centre |
| **D4** | L'écran de contrôle | ✅ **Une roue dentée en haut à droite de l'écran central.** ⚠️ **Exception assumée à « aucune icône seule »** : c'est le seul pictogramme universel du lot, et le centre n'a pas de place pour un mot. 🔴 **Jamais de pastille dessus** |
| **D5** | Les nouveautés | ✅ **Sur l'écran central.** Bornes au §6.1 |
| **D6** | Le thème | ✅ **Panneaux extrudés, registre du GUI de jeu kawaii** (§4). ⚠️ **Corrige la décision du matin** : le verre dépoli avait été retenu, puis abandonné après maquette le même jour. **Le verre est archivé, il ne revient pas par morceaux** |
| **D7** | L'écran de crise | ✅ **Adapté à la matière, structure inchangée** (§4.5) |
| **D8** | L'overlay de veille | ✅ **À essayer — fond d'écran vivant** (§6.3) |
| **D9** | Ce qu'on voit en veille | ✅ **Le décor et Kokoro qui respire, rien d'autre** |
| **D10** | L'icône du lanceur, et la notification | ✅ **L'icône ouvre le monde** — 🏗️ **câblé le 15/08/2026**, l'écran de contrôle est passé au bout de la roue dentée. 🔴 **La notification est muette : plus de boutons, et plus un mot de son contenu** — elle ouvre le monde **directement posé sur l'écran de crise, sans animation** (§6.2). ⏳ **Cette moitié-là n'est pas faite** |
| **D11** | Le titre de l'écran | ✅ **Il ne défile pas.** *(La question posée était : quand on fait défiler la liste des étapes, le mot « Thérapie » part-il vers le haut avec elle ? Non — il reste posé sur sa bande en haut, sur son ruban. Savoir où l'on est ne doit pas dépendre d'où l'on en est dans la liste.)* |

---

## 6. Ce qui reste à trancher

### 6.1 Les bornes des nouveautés *(D5, à confirmer)*

L'écran central est le seul qui soit vide par doctrine. Y poser les nouveautés est tenable **à quatre conditions**, sans quoi il devient un tableau de bord :

1. **Rien quand il n'y a rien.** Par défaut le centre est vide. C'est l'état des six jours sur sept.
2. **Une carte, une phrase, un fait** : « Ta séance de dimanche a ajouté trois choses. » Pas de liste, pas de détail, pas de pastille.
3. **Elle disparaît** quand les écrans concernés ont été ouverts. Elle ne revient pas, elle ne compte pas les jours.
4. 🔴 **Elle n'apparaît qu'après une publication** — donc une fois par semaine, à la clôture d'une séance, une chose que Xavier a lui-même décidée. **Ce n'est pas Kokoro qui vient vers lui : c'est le programme qui a changé parce qu'ils l'ont changé ensemble.**

### 6.2 🔴 La notification devient muette — acté le 14/08/2026

**Le motif est clinique, et il est de Xavier :** *« avec les boutons sur la notification, je lis toute la journée mot code et tension appliquée. Ça n'aide pas mes angoisses. »*

⭐ **C'est le motif du 10/08 appliqué une quatrième fois** — celui qui a fait retirer les numéros d'urgence de tout le dispositif : **un secours affiché en permanence cesse d'être une porte et devient un rappel permanent du danger.** La notification est visible sur l'écran verrouillé du matin au soir ; ce qu'elle écrit, Xavier le relit cent fois sans jamais en avoir besoin.

**Ce qui est acté :**

1. 🔴 **Les deux boutons d'action disparaissent.**
2. 🔴 **Le corps de la notification aussi.** ⚠️ **Il porte aujourd'hui `mot-code · tension appliquée`** — enlever les boutons en le laissant n'aurait réglé que la moitié du problème. **Il ne reste que l'icône et le mot `Kokoro`.** Rien à lire, rien à relire.
3. ✅ **Elle ouvre le monde directement posé sur l'écran de crise** — caméra déjà en place, **aucun glissement, aucune animation**. Un geste vers le haut ramène à Kokoro.
4. **Le nom du canal et sa description changent aussi** (« Accès sans déverrouiller », « …ouvre le mot-code et la tension appliquée… »). ⭐ **Ils sont visibles dans les réglages Android, pas sur l'écran verrouillé** — donc moins urgents, mais ils doivent dire la même chose. **Nom et description se mettent à jour sans changer l'identifiant du canal** : c'est l'importance, le son et la vibration qui sont figés, pas les libellés. `kokoro_acces_v1` reste.
5. **`controle_acces_explication`, dans l'écran de contrôle, décrit les deux boutons** — à réécrire en même temps.

> 🔴 **Ce qui reste non négociable, et qu'il faut vérifier sur l'appareil :** **le mot-code ne recule jamais au-delà de deux taps depuis l'écran verrouillé.** Aujourd'hui c'est un tap ; le nouveau chemin en fait deux (notification → bouton). **Repli écrit d'avance** si le monde ne s'affiche pas aussi vite et aussi sûrement au-dessus du verrouillage que `CriseActivity` : la notification garde `CriseActivity` — muette elle aussi — et l'écran du bas du monde affiche le même contenu pour les fois où Xavier vient de lui-même. **Deux portes, un seul contenu.**

### 6.3 L'essai du fond d'écran vivant *(D8)*

**La piste :** un `WallpaperService` qui dessine le décor et Kokoro. Visible sur le verrouillage **et** sur l'accueil One UI, **aucune permission**, et rien que One UI puisse tuer comme il tue les services.

| À vérifier | Pourquoi |
|---|---|
| L'écran de verrouillage de One UI accepte-t-il un fond d'écran vivant ? | Samsung le restreint parfois au seul écran d'accueil |
| La batterie | ⭐ **Kokoro respire — donc ça anime en continu.** Il faut ne dessiner **que quand c'est visible**, à cadence basse (la respiration est lente), et **rien du tout écran éteint** |
| Le parallaxe | ⭐ **Bonne nouvelle : le défilement des pages du lanceur pilote déjà le décalage** (`onOffsetsChanged`). Le décor glisserait **sous le doigt de Xavier**, donc sans jamais bouger seul — la doctrine tient |
| La navigation | ❌ **Impossible sur le verrouillage** : le keyguard mange les gestes. **Et c'est très bien** : en veille on regarde, on ne parcourt pas (D9) |

### 6.4 La bibliothèque et les séances à deux — ⚠️ ce n'est pas une décision d'interface

**Oui, « thérapie » est bien la liste des prochaines actions de la thérapie en cours**, et **oui, une séance à deux s'y trouve pour être jouée** — le contrat lui donne déjà `rubrique: therapie` ([`companion/PROGRAMME.md` §3](PROGRAMME.md)).

🔴 **Mais « une catégorie *Séances à deux* dans la documentation » n'existe pas dans le contrat.** La bibliothèque n'a aucun champ de catégorie : les fiches se groupent par `quand`, comme tout le reste. **L'ajouter, c'est modifier `companion/PROGRAMME.md`, qui est normatif — donc un acte de séance, supervisé, pas un choix d'écran.**

**Et sur l'entraînement, il y a un point de fond :** l'entraînement **n'est pas un contenu séparé**, c'est **la même étape jouée à blanc**, qui renvoie `issue: "entrainement"`. Le mettre dans la documentation ferait apparaître une même étape à deux endroits — ce qui casse la propriété qui a fait choisir ce rangement.

| Ce qui va où | |
|---|---|
| **Thérapie** | **La séance à deux elle-même, une seule entrée.** ⭐ **Ce n'est pas un bouton de plus dans la liste : c'est le premier écran de l'étape** — voir ci-dessous |
| **Documentation** | Ce qui se **lit** : *Ce qu'est une séance à deux* · *La fiche pour Chourouk* (`montrable`) · *Le signal d'arrêt, convenu à froid*. Des textes, y compris lisibles par l'aidant. ✅ **Validé le 14/08/2026** |

#### ⭐ L'écran de choix — acté le 14/08/2026

**Ouvrir une séance à deux ne la démarre pas.** Le premier écran de l'étape pose une question, et rien d'autre : **deux boutons pleine largeur**, la vraie séance, ou l'entraînement.

> ⚠️ **Xavier : « le bouton toujours visible, je sens qu'on va manquer de place. »** — **Il a raison, et l'écran de choix règle exactement ça** : la carte dans la liste ne porte que le titre, comme toutes les autres. Le choix vit dans l'étape, pas dans la liste. **Zéro place consommée ailleurs.**

| | |
|---|---|
| **Avant le premier entraînement** | 🔴 **Seul l'entraînement est proposé.** `entrainement_requis` vaut toujours `true` ([`companion/PROGRAMME.md` §3](PROGRAMME.md)) : la première fois que ça compte ne doit pas être la première fois que ça se fait |
| **Ensuite** | Les deux boutons. ⭐ **On se réentraîne aussi la veille d'une vraie séance** — ce chemin ne se referme jamais |
| ⭐ **L'entraînement est jouable en solo par l'aide** *(précision de Xavier, 14/08/2026)* | Chourouk peut le répéter **seule**, sans Xavier. Rien ne s'y oppose : le contenu d'une séance à deux **ne porte rien sur Xavier** (contrôle **C10**), donc il se répète sans lui |
| **Ce que ça renvoie** | `issue: "entrainement"` — **ce n'est pas une donnée clinique, et rien ne s'en déduit** |

⏳ **Un point reste ouvert : la cadence en entraînement.** Une séquence réelle tient 22 minutes, dont des silences de 60 secondes. Les tenir seule, à blanc, n'apprend rien de plus que de les avoir lus. **Proposition : en entraînement seulement, un bouton *Suite* permet à l'aide d'avancer à son rythme** ; en séance réelle, jamais — le temps y est tenu par l'appareil, c'est le sens du type. À confirmer.

### 6.5 ✅ Les pancartes de section — **tranché le 14/08/2026 : option B**

> ✅ **Décision de Xavier : les deux couleurs restent.** §3.1 est amendé — *« la couleur distingue les sections, elle ne les classe jamais »* — et l'**interdiction d'aligner la palette sur une échelle d'urgence** y est écrite explicitement. Le raisonnement qui a mené là est conservé ci-dessous, parce qu'il juge d'avance toute couleur qu'on voudrait ajouter.

**§3.1 disait : « Aucun code couleur, aucune pastille, aucun badge » sur les sous-titres de `quand`.** La maquette validée le 14/08/2026 leur donne pourtant deux couleurs — *Aujourd'hui* en pêche, *Sans date* en azur.

**Ce n'est pas forcément une infraction, et c'est ça qu'il faut trancher.** La règle vise le **classement** : elle interdit qu'une couleur dise *urgent*, *en retard* ou *important*. Deux couleurs qui ne font que **séparer** deux sections ne classent rien. Mais *Aujourd'hui* et *Sans date* sont ordonnés dans le temps par nature — **une couleur posée dessus peut se relire comme une urgence**, même si personne ne l'a voulu.

| Option | Ce que ça donne |
|---|---|
| **A — une seule couleur** *(le repli sûr)* | Toutes les pancartes en pêche. La règle de §3.1 tient à la lettre, on ne perd que de la variété |
| ✅ **B — les couleurs restent** | §3.1 s'amende : *« la couleur distingue les sections, elle ne les classe jamais »*, plus l'interdiction explicite d'aligner la palette sur une échelle d'urgence |

### 6.6 Renvoyé ailleurs

- **Le comportement du personnage** — D3, son propre brainstorm.
- **La séance à deux (K6)** ne ressemble à aucun autre écran : signal d'arrêt permanent, critères d'arrêt à un tap, deux lecteurs, un chronomètre. **Elle mérite son propre passage.**
- **P2** — les gestes système en haut et en bas : à mesurer sur l'appareil, pas sur le papier.

---

## 7. 🏗️ Où en est l'implémentation — 15/08/2026

**Écrit, compilé, 78 tests au vert. ⏳ Pas encore posé sur le téléphone** *(aucun appareil branché au moment de l'écriture)* : **rien de ce qui suit n'est vérifié à l'œil.**

| | Où | État |
|---|---|---|
| **La matière** (§4.1) — la recette à six couches, déclinée en couleur | `ui/Matiere.kt` | ✅ Une seule fonction, `Modifier.matiere` |
| **Les pièces** (§4.2) — panneau, carte, bouton, ruban, pancarte, bande de titre, cadre vide, **page, champ de saisie, interrupteur, accusé, séparateur** | `ui/Pieces.kt` | ✅ **Aucun composant Material** hors `Text` |
| **Les ornements** — étincelle, cœur, rivet | `ui/Ornements.kt` | ✅ Décor pur, jamais sur une carte de liste |
| **La police** (§4.3) | `res/font/varela_round.ttf` · `ui/Typographie.kt` | ✅ Embarquée. ⭐ **Interligne centré** — sans quoi un libellé court reste collé en haut d'un bouton haut |
| **Le thème** jour / nuit | `ui/ThemeMonde.kt` | ✅ **Le thème de tout ce que Xavier voit.** L'ancien ne sert plus qu'aux deux outils de mise au point |
| **Les cinq écrans** (§3) | `monde/Bords.kt` | ✅ Ruban fixe (D11), gauche et droite défilent, haut et bas non (**P1**), roue dentée (D4) |
| **Une étape ouverte** (§3.1) | `monde/Etapes.kt` · `monde/MondeKokoro.kt` | ✅ Plein écran, traversée coupée, fermeture au bouton **ou au *retour* du téléphone** — jamais au geste |
| **L'icône du lanceur** (D10) | `AndroidManifest.xml` | ✅ Ouvre le monde |
| **La phrase pour le soignant** | `crise/CriseActivity.kt` | ✅ Une porte à part entière, pour que les trois boutons de l'écran **Crise** mènent quelque part |

### 7.1 ✅ Les quatre surfaces éprouvées sont passées à la matière — 15/08/2026

**Demandé par Xavier, et c'est ce qui l'autorisait :** 🔴 **la prévisibilité est une fonctionnalité**, donc un écran qui sert en situation ne change pas d'apparence sans que Xavier l'ait décidé. Les deux thèmes ne cohabitent plus.

| Surface | Ce qui a changé |
|---|---|
| **Écrans de crise** — mot-code, tension appliquée, séquence de soins, rester assis, phrase pour le soignant, quand arrêter | Panneaux extrudés, ruban azur, **un titre par vue** au lieu d'un titre répété dans le corps. **Structure inchangée** (§4.5) : mêmes boutons, mêmes repères, mêmes critères d'arrêt |
| **Check-in du jour** | Panneaux, ruban pêche. ⭐ **Le vert reste réservé à ce qui avance d'un pas** — *passer* et *arrêter* sont neutres, **jamais gris-triste ni barrés** |
| **Écran de contrôle** → **Réglages** | Panneaux, ruban beurre, sections en pancartes, lignes groupées dans un panneau et séparées d'un trait, interrupteur maison |
| **L'alerte K1 et l'atelier du corps** | ⚠️ **Gardent l'ancien thème.** Ce sont des outils de mise au point, pas des surfaces de soin |

**Trois changements de fond sont venus avec, tous demandés par Xavier :**

1. ⭐ **Le mot-code part d'un seul appui** depuis l'écran **Crise** du monde — **plus d'écran de confirmation entre le bouton et l'envoi.** Demander *es-tu sûr ?* à quelqu'un qui vient de perdre la parole, c'est lui demander un tap de plus au moment précis où il n'en a plus. Un **accusé** paraît en bas de l'écran : *envoi en cours*, puis *mot-code envoyé à …*. 🔴 **Les deux cas d'impossibilité gardent l'ancien écran** — pas de numéro, autorisation SMS refusée, échec du réseau : il explique et propose l'application Messages.
2. ⭐ **Le message du mot-code se règle dans l'application.** Ça ne le rend pas moins convenu : **le changer dans Kokoro ne prévient pas celle qui le reçoit**, et c'est écrit sous le champ.
3. **Les réglages perdent deux boutons** — *Ouvrir le monde* et *Ouvrir le check-in du jour*. L'icône du lanceur ouvre le monde (D10), le check-in est une étape de la thérapie. **Une porte par chose.**

**Et deux défauts de la traversée sont corrigés :**

- ⭐ **Le rattrapage d'un ou deux pixels en fin de glissement.** Le ressort n'avait pas de seuil de visibilité : il prenait celui de Compose, `0.01`, appliqué à des unités qui valent **un écran entier**. L'animation s'arrêtait donc à un centième d'écran de sa cible — une dizaine de pixels — et la valeur y **sautait**. Le seuil est maintenant **un demi-pixel**, exprimé en fraction de la largeur mesurée.
- ⭐ **Le bouton *retour* ferme le panneau** au lieu de quitter l'application.

**⚠️ Ce qui est provisoire, et qu'il ne faut pas prendre pour acquis :**

- 🔴 **Le contenu des écrans est écrit en dur** — les 11 étapes de `inputs/programme.json` v1, recopiées dans `strings.xml` *(et pas dans du Kotlin : c'est `strings.xml` que lisent les tests d'invariants)*. **C'est K5 qui le remplacera par une lecture du dossier synchronisé**, avec le filtrage des sept interdits de [`PROGRAMME.md`](PROGRAMME.md) §7.
- ⏳ **Le bouton *Fait* n'existe pas encore** sur une démarche ouverte : il écrirait dans `reponses/`, ce que Kokoro ne sait pas faire. ⭐ **Un bouton qui n'écrit rien mentirait** — mieux vaut qu'il manque et que ça se voie.
- ⏳ **Les nouveautés** (D5, §6.1) ne sont pas là : elles supposent de comparer deux versions du programme, donc K5.
- ⏳ **§6.2 — la notification muette n'est pas faite.** Elle porte toujours ses deux boutons et son corps de texte.
