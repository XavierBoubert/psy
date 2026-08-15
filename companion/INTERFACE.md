# Kokoro — l'interface du monde

**v2 — 14/08/2026**, *rangement refait le 15/08/2026 (§7.7)*. Le monde devient **l'interface principale** de l'app, et jusqu'à l'écran de veille.

> 🔄 **v2 — le thème change, et lui seul.** Le verre dépoli de la v1 est abandonné au profit de **panneaux extrudés** (§4), après maquette. **Le rangement, les points durs, les onze décisions et tout le contenu sont inchangés** — seule la peau est réécrite, plus D6 qui la nommait et P4/P5 qui n'existaient qu'à cause du flou.

> 📐 **Onze décisions tranchées par Xavier le 14/08/2026** — §5. Le décor est spécifié dans [`DECOR.md`](./DECOR.md), le personnage dans [`CORPS.md`](./CORPS.md), le contrat de contenu dans [`companion/PROGRAMME.md`](PROGRAMME.md) — **ce document ne fait que ranger** ce que ces trois-là ont décidé.
>
> 📌 **Il ne décide jamais quel contenu clinique existe.** Ça se décide en séance.
>
> ⏳ **Ce qui reste ouvert** — les bornes des nouveautés (§6.1), l'essai du fond d'écran vivant (§6.3), la cadence de l'entraînement en solo (§6.4), et le comportement du personnage, renvoyé à son propre brainstorm.
>
> 🏗️ **Écrit en Compose le 15/08/2026** — la matière (§4.1, §4.2) et les écrans (§3) ; détail en **§7**. **Deux points ouverts ont été tranchés par Xavier pour que ça puisse être codé** : les pancartes gardent leurs deux couleurs *(§6.5, option B)*, et la police arrondie est **Varela Round** *(§4.3)*.
>
> ✅ **Le même jour, les quatre surfaces déjà éprouvées y sont passées aussi** — crise, check-in, réglages *(§7.1)*, **à la demande de Xavier**. **Il n'y a plus deux thèmes**, et le mot-code part désormais d'un seul appui.
>
> ✅ **Puis une deuxième passe le même jour** *(§7.2)* : **la notification devient muette** — §6.2 est câblé, il ne restait plus qu'elle — **la croix remplace le bouton *Fermer* sur tous les panneaux**, **la phrase pour le soignant devient une porte indépendante**, et **les deux entrées de la crise affichent littéralement le même écran**.
>
> ⭐ **Et une troisième, après le premier essai sur le téléphone** *(§7.3)* : **la notification garde `CriseActivity`** — le repli écrit d'avance au §6.2, **emprunté parce que le monde demandait de déverrouiller** — et **elle se republie à l'ouverture du monde**, ce qu'aucun écran ne faisait plus depuis **D10**. ⭐ **Puis l'écran central apprend à dire qu'une porte est tombée** *(§7.4)* — **en toutes lettres, pas par une pastille.**
>
> 🔄 **Puis le rangement lui-même est refait, à la demande de Xavier** *(§7.7)* : **la croix de cinq écrans devient un anneau de quatre, entièrement horizontal et sans bout.** On arrive sur la **thérapie** ; **P1 est levé**, donc tout écran peut défiler ; **la crise reste à un seul geste de l'entrée**, désormais dans les deux sens. **D1 garde sa règle et change de forme ; D2, D3, D4 et D5 sont amendés.**

---

## 1. Ce qu'il y a à ranger

### 1.1 Ce qui est déjà construit et tourne sur le téléphone

| Surface | Jalon | Ce que c'est | Taille à l'écran |
|---|---|---|---|
| **Accès crise** | K2 ✅ | Notification **muette** sur l'écran verrouillé *(§6.2, §7.2)* — **une porte, pas un rappel**. Icône et mot *Kokoro*, rien d'autre à lire. Elle ouvre l'écran de crise, **jamais le monde** *(§7.3)* | hors monde |
| **Mot-code à Chourouk** | K2 ✅ | SMS **envoyé en un appui** depuis l'écran **Crise** *(§7.1)*, téléphone verrouillé, sans réseau data. Le bouton se grise le temps de l'envoi ; l'écran plein reste le repli en cas d'échec | 1 écran plein |
| **Tension appliquée** | K3 ✅ | 4 repères externes enchaînés, minuteur, critères d'arrêt à un tap | 1 écran plein, guidé |
| **Phrase pour le soignant** | K3 ✅ | Texte montrable en plein écran. ⭐ **Porte indépendante** *(§7.2)* — elle n'est plus rangée sous la tension appliquée | 1 écran plein |
| **Check-in du jour** | K4 ✅ | 11 champs fermés, aucune saisie de texte, écrit `journal/` | 1 écran, questions enchaînées |
| **Le monde** | — ✅ | **4 écrans en anneau horizontal, sans bout** *(§7.7)*, décor 4 couches, plage de nuit | le contenant |
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
| ✅ **P1** | **Un écran du haut ou du bas ne peut pas contenir de liste qui défile verticalement** — le glissement vertical était pris par la traversée du monde. C'est ce qui avait décidé le rangement en croix : les deux contenus longs à gauche et à droite, le haut et le bas réduits à ce qui tient en un écran | ✅ **Levé le 15/08/2026** *(§7.7)* : **il n'y a plus de haut ni de bas**, la traversée est entièrement horizontale, et **le glissement vertical est rendu au contenu**. 🔴 **Le point reste écrit parce qu'il explique la forme précédente** — et parce qu'il condamne d'avance toute réintroduction d'un écran au-dessus ou au-dessous |
| ✅ **P2** | Les gestes système de One UI mordent les bords : volet de notifications en haut, retour à l'accueil en bas | ✅ **Sans objet depuis §7.7** : plus aucun geste de traversée ne part du haut ni du bas. 🔴 **Le point reste écrit** — il vaut pour tout geste vertical qu'on serait tenté de câbler un jour |
| **P3** | Le décor ne porte jamais de texte ([`DECOR.md`](./DECOR.md) §7) | Le texte est sur une **surface posée sur le décor**, jamais peint dessus |
| ✅ **P4** | ⭐ **Le flou ne doit jamais venir du système.** `Window.setBackgroundBlurRadius` dépend d'un réglage Android que l'économiseur de batterie coupe — l'apparence changerait parce qu'un réglage étranger a bougé, **exactement ce que la doctrine interdit au thème sombre** | ✅ **Sans objet depuis §4 : il n'y a plus de flou du tout.** 🔴 **Le point reste écrit parce qu'il juge d'avance toute réintroduction** — une surface qui floute retombe sous P4, et devra calculer son flou elle-même |
| ✅ **P5** | ⭐ **La lisibilité ne doit jamais dépendre de ce qui est derrière.** Un texte posé sur un décor flouté passe du feuillage sombre au ciel clair en glissant | ✅ **Réglé par l'opacité** : les panneaux de §4 sont pleins, le décor passe **entre** eux et jamais dessous. 🔴 **Le point reste écrit parce qu'il interdit toute surface semi-transparente portant du texte** |
| **P6** | L'overlay de veille : `TYPE_APPLICATION_OVERLAY` ne passe pas au-dessus du keyguard sur Android récent | Piste retenue à essayer : **fond d'écran vivant** — §5, D8 |
| 🔴 **P7** | ⭐ **La notification de crise est lue toute la journée** — elle est permanente sur l'écran verrouillé, et elle porte aujourd'hui les mots *mot-code* et *tension appliquée* trois fois : dans son texte et sur ses deux boutons. **Xavier, 14/08/2026 : « je lis toute la journée mot code et tension appliquée. Ça n'aide pas mes angoisses. »** | ⭐ **Quatrième instance du motif du 10/08** *(retrait des numéros d'urgence)* : **un secours affiché en permanence devient un rappel permanent du danger.** Voir **§6.2** |
| **P8** | En face, la notification donne le mot-code **en un tap** depuis l'écran verrouillé, sur une activité éprouvée pour de vrai le 10/08 | Tout chemin qui passe par le monde ajoute un tap et un chargement **au pire moment**. Les deux exigences se concilient — **§6.2** |

---

## 3. Le rangement — **un anneau horizontal** *(D1, D2, refaits le 15/08/2026 — §7.7)*

```
  ← … ┌──────────┐ ┌───────────────┐ ┌────────┐ ┌───────┐ ┌──────────┐ … →
      │ Thérapie │ │ Documentation │ │ Bilan  │ │ Crise │ │ Thérapie │
      └──────────┘ └───────────────┘ └────────┘ └───────┘ └──────────┘
         entrée                                              (le même)
```

**Quatre écrans, une seule direction, aucun bout.** On arrive sur la **thérapie** ; en glissant vers la gauche on va vers la documentation, le bilan, la crise, **puis la thérapie de nouveau** — 🔴 **le décor continue dans le même sens, il ne revient pas en arrière et ne saute pas.** ⭐ **La crise est donc aussi la voisine de droite de l'entrée** : elle est à **un seul geste** de l'ouverture de l'app, dans le sens qu'on veut.

**Une rubrique par écran, celles du contrat §8.3.** Un contenu ne change donc jamais de place — la rubrique est écrite dans le JSON et ne bouge pas, alors que `quand` bouge tous les jours. L'interface n'arbitre rien : toute étape publiée sait déjà où elle va.

| Écran | Contenu | Pourquoi là |
|---|---|---|
| **Thérapie** *(entrée)* | Les prochaines actions de la thérapie en cours, groupées par *aujourd'hui* / *quand j'en ai besoin* / *sans date* · **Kokoro** en tête de liste · la **roue dentée** sur la bande de titre (D4) · **les nouveautés**, quand il y en a (D5) | C'est là qu'on arrive, donc c'est là que vit ce qui n'appartient à aucune rubrique |
| **Documentation** | La bibliothèque, une fiche par ligne | Deuxième liste appelée à grandir |
| **Bilan** | Les questionnaires à passer, les comptes rendus écrits en séance. Aucun chiffre, aucune courbe | ⭐ **Il défile maintenant comme la documentation** — il ne le pouvait pas quand il était en haut |
| **Crise** | Mot-code · tension appliquée · phrase pour le soignant | Trois grands boutons, **jamais de défilement** — 🔴 **c'est une exigence en soi, et elle survit à la levée de P1** |

> ⏳ **La place de Kokoro est provisoire.** Il occupe le haut de la liste de la thérapie et s'en va avec elle quand on défile. **Comment il habite vraiment l'écran se décide à part** — ça touche au corps, pas au rangement.

### 3.1 À quoi ressemble un écran de bord

- **Un titre écrit en toutes lettres**, posé en haut et qui ne défile pas (**D11**).
- **Les trois `quand` sont des sous-titres écrits** — *Aujourd'hui* · *Quand j'en ai besoin* · *Sans date*. 🔴 **Aucune pastille, aucun badge.** ⭐ **La couleur distingue les sections, elle ne les classe jamais** *(§6.5, tranché le 14/08/2026)* — 🔴 **et il reste interdit d'aligner la palette sur une échelle d'urgence** : aucune teinte ne doit pouvoir se lire comme *urgent*, *en retard* ou *important*.
- **Une carte par étape** : le titre, la durée si elle est connue, rien d'autre. Pas de chevron, pas d'aperçu, pas de compteur.
- **Le décor reste visible** autour et entre les cartes — on est toujours dans le même monde, jamais dans une autre application.
- **Une étape ouverte prend l'écran entier** et se ferme **d'une croix en haut à droite** *(amendé le 15/08/2026, §7.2 — c'était un bouton écrit *Fermer* en pied de page)*, jamais d'un geste : rien ne doit concurrencer la traversée.

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
- **Les ornements** — étincelles, cœurs, rivets — sont **du décor pur**. 🔴 **Aucun n'est jamais porteur d'information**, et aucun ne se pose sur une carte de la liste : ils vivent sur les bandes de titre, les états vides et autour de Kokoro.

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
| **D1** | Le rangement | ✅ **Une rubrique par écran** (§3). 🔄 **La forme a changé le 15/08/2026** — la croix est devenue un **anneau horizontal** *(§7.7)* — **la règle, non** : c'est toujours une rubrique par écran, et un contenu ne change jamais de place |
| **D2** | La place de la crise | 🔄 **Dernière de l'anneau** *(15/08/2026, §7.7)* — c'était **BAS**. ⭐ **La propriété qu'on cherchait est conservée et même renforcée** : elle reste **à un seul geste de l'entrée**, et maintenant dans les deux sens |
| **D3** | Kokoro suit-il ? | ⏭️ **Il suivra l'interface — hors sujet ici, renvoyé à son propre brainstorm.** ⏳ **En attendant il est en tête de la liste de la thérapie** *(§7.7)* — l'écran central où il se tenait n'existe plus |
| **D4** | L'écran de contrôle | ✅ **Une roue dentée**, 🔄 **désormais sur la bande de titre de l'écran d'entrée** *(15/08/2026, §7.7)* — c'était le coin haut droit de l'écran central. ⚠️ **Exception assumée à « aucune icône seule »** : c'est le seul pictogramme universel du lot, et la bande n'a pas la place d'un mot de plus. 🔴 **Jamais de pastille dessus** — ⭐ **réaffirmé le 15/08/2026 par Xavier**, à qui la question s'est posée en vrai *(§7.4)* : **un défaut se dit en toutes lettres sur l'écran d'entrée**, il ne se signale pas par un point |
| **D5** | Les nouveautés | ✅ **Sur l'écran d'entrée — la thérapie** *(15/08/2026)*. Bornes au §6.1 |
| **D6** | Le thème | ✅ **Panneaux extrudés, registre du GUI de jeu kawaii** (§4). ⚠️ **Corrige la décision du matin** : le verre dépoli avait été retenu, puis abandonné après maquette le même jour. **Le verre est archivé, il ne revient pas par morceaux** |
| **D7** | L'écran de crise | ✅ **Adapté à la matière, structure inchangée** (§4.5) |
| **D8** | L'overlay de veille | ✅ **À essayer — fond d'écran vivant** (§6.3) |
| **D9** | Ce qu'on voit en veille | ✅ **Le décor et Kokoro qui respire, rien d'autre** |
| **D10** | L'icône du lanceur, et la notification | ✅ **Les deux sont câblées, le 15/08/2026.** L'icône ouvre le monde, l'écran de contrôle est passé au bout de la roue dentée. 🔴 **La notification est muette : plus de boutons, et plus un mot de son contenu** (§6.2, §7.2). ⚠️ **Elle ouvre `CriseActivity`, pas le monde** — le monde a été essayé et **il demandait de déverrouiller** *(§7.3)*. ⭐ **Et c'est le monde qui la republie** : elle était accrochée à l'écran de contrôle, que cette décision-ci a justement cessé de faire ouvrir |
| **D11** | Le titre de l'écran | ✅ **Il ne défile pas.** *(La question posée était : quand on fait défiler la liste des étapes, le mot « Thérapie » part-il vers le haut avec elle ? Non — il reste posé sur sa bande en haut, sur son ruban. Savoir où l'on est ne doit pas dépendre d'où l'on en est dans la liste.)* |

---

## 6. Ce qui reste à trancher

### 6.1 Les bornes des nouveautés *(D5, à confirmer)*

🔄 **L'emplacement a changé le 15/08/2026** *(§7.7)* : l'écran central n'existe plus, **c'est la tête de l'écran d'entrée — la thérapie**. **Les quatre conditions, elles, ne changent pas d'une virgule**, sans quoi cet emplacement devient un tableau de bord :

1. **Rien quand il n'y a rien.** Par défaut il n'y a **rien au-dessus de la liste**. C'est l'état des six jours sur sept.
2. **Une carte, une phrase, un fait** : « Ta séance de dimanche a ajouté trois choses. » Pas de liste, pas de détail, pas de pastille.
3. **Elle disparaît** quand les écrans concernés ont été ouverts. Elle ne revient pas, elle ne compte pas les jours.
4. 🔴 **Elle n'apparaît qu'après une publication** — donc une fois par semaine, à la clôture d'une séance, une chose que Xavier a lui-même décidée. **Ce n'est pas Kokoro qui vient vers lui : c'est le programme qui a changé parce qu'ils l'ont changé ensemble.**

> ⭐ **Un deuxième usage de cet emplacement est ouvert le 15/08/2026 : l'avis de défaut** *(§7.4)*. Il tient les quatre conditions à la lettre — **rien quand il n'y a rien**, une carte, une phrase, un fait, et il disparaît quand le défaut est réparé. 🔴 **La quatrième condition est amendée, et il faut le dire :** celui-là n'attend pas une publication, il paraît quand **une porte du dispositif a cessé de fonctionner**. **C'est le seul motif qui l'autorise** — un défaut, jamais une suggestion, jamais un rappel, jamais un contenu.

### 6.2 ✅ La notification devient muette — acté le 14/08/2026, **câblé le 15/08/2026** *(§7.2)*

**Le motif est clinique, et il est de Xavier :** *« avec les boutons sur la notification, je lis toute la journée mot code et tension appliquée. Ça n'aide pas mes angoisses. »*

⭐ **C'est le motif du 10/08 appliqué une quatrième fois** — celui qui a fait retirer les numéros d'urgence de tout le dispositif : **un secours affiché en permanence cesse d'être une porte et devient un rappel permanent du danger.** La notification est visible sur l'écran verrouillé du matin au soir ; ce qu'elle écrit, Xavier le relit cent fois sans jamais en avoir besoin.

**Ce qui est acté :**

1. 🔴 **Les deux boutons d'action disparaissent.**
2. 🔴 **Le corps de la notification aussi.** ⚠️ **Il porte aujourd'hui `mot-code · tension appliquée`** — enlever les boutons en le laissant n'aurait réglé que la moitié du problème. **Il ne reste que l'icône et le mot `Kokoro`.** Rien à lire, rien à relire.
3. ⛔ **Elle devait ouvrir le monde directement posé sur l'écran de crise** — caméra déjà en place, aucun glissement, aucune animation. ⚠️ **Essayé le 15/08/2026, et abandonné le jour même : le téléphone demandait de déverrouiller** *(§7.3)*. **C'est le repli ci-dessous qui s'applique** — elle garde `CriseActivity`.
4. **Le nom du canal et sa description changent aussi** (« Accès sans déverrouiller », « …ouvre le mot-code et la tension appliquée… »). ⭐ **Ils sont visibles dans les réglages Android, pas sur l'écran verrouillé** — donc moins urgents, mais ils doivent dire la même chose. **Nom et description se mettent à jour sans changer l'identifiant du canal** : c'est l'importance, le son et la vibration qui sont figés, pas les libellés. `kokoro_acces_v1` reste.
5. **`controle_acces_explication`, dans l'écran de contrôle, décrit les deux boutons** — à réécrire en même temps.

> 🔴 **Ce qui reste non négociable, et qu'il faut vérifier sur l'appareil :** **le mot-code ne recule jamais au-delà de deux taps depuis l'écran verrouillé.** Aujourd'hui c'est un tap ; le nouveau chemin en fait deux (notification → bouton). **Repli écrit d'avance** si le monde ne s'affiche pas aussi vite et aussi sûrement au-dessus du verrouillage que `CriseActivity` : la notification garde `CriseActivity` — muette elle aussi — et l'écran du bas du monde affiche le même contenu pour les fois où Xavier vient de lui-même. **Deux portes, un seul contenu.**
>
> ✅ **Le repli a été emprunté le 15/08/2026, et il a suffi d'une ligne** — parce que depuis §7.2 `CriseActivity` affiche **le même composable** que l'écran du bas du monde, aux mêmes boutons et aux mêmes gestes. 🔴 **La condition prévue s'est réalisée telle quelle** : le monde ne s'affiche pas au-dessus du verrouillage, il **demande de déverrouiller** *(§7.3)*. **Deux portes, un seul contenu — et c'est ce qui a rendu le repli gratuit.**

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

**Écrit, compilé, tests au vert. ⏳ Pas encore posé sur le téléphone** *(aucun appareil branché au moment de l'écriture)* : **rien de ce qui suit n'est vérifié à l'œil.**

| | Où | État |
|---|---|---|
| **La matière** (§4.1) — la recette à six couches, déclinée en couleur | `ui/Matiere.kt` | ✅ Une seule fonction, `Modifier.matiere` |
| **Les pièces** (§4.2) — panneau, carte, bouton, ruban, pancarte, bande de titre, cadre vide, **page, champ de saisie, interrupteur, accusé, séparateur, croix** | `ui/Pieces.kt` | ✅ **Aucun composant Material** hors `Text` |
| **Les ornements** — étincelle, cœur, rivet | `ui/Ornements.kt` | ✅ Décor pur, jamais sur une carte de liste |
| **La police** (§4.3) | `res/font/varela_round.ttf` · `ui/Typographie.kt` | ✅ Embarquée. ⭐ **Interligne centré** — sans quoi un libellé court reste collé en haut d'un bouton haut |
| **Le thème** jour / nuit | `ui/ThemeMonde.kt` | ✅ **Le thème de tout ce que Xavier voit.** L'ancien ne sert plus qu'aux deux outils de mise au point |
| **Les quatre écrans** (§3) | `monde/Bords.kt` | ✅ Ruban fixe (D11), **tous défilent sauf la crise** *(§7.7)*, roue dentée sur la bande d'entrée (D4) |
| **L'avis de porte fermée** (§6.1, §7.4) | `monde/Bords.kt` | ✅ En tête de l'écran d'entrée, **et seulement quand la notification d'accès n'a pas pu s'afficher** |
| **Une étape ouverte** (§3.1) | `monde/Etapes.kt` · `monde/MondeKokoro.kt` | ✅ Plein écran, traversée coupée, fermeture **à la croix ou au *retour* du téléphone** — jamais au geste |
| **L'icône du lanceur** (D10) | `AndroidManifest.xml` · `mipmap-anydpi-v26/ic_lanceur.xml` | ✅ Ouvre le monde. ⭐ **C'est Kokoro qui y est depuis le 16/08/2026** *(§7.6)* — icône adaptative, couche monochrome, écran de démarrage et icône de notification tirés du même logo |
| **La notification muette** (D10, §6.2) | `crise/AccesCrise.kt` | ✅ Icône et mot *Kokoro*. Elle ouvre **`CriseActivity`** *(§7.3)*, republiée à chaque ouverture du monde |
| **La phrase pour le soignant** | `crise/ContenuCrise.kt` | ✅ Une porte à part entière, **sortie de la tension appliquée** *(§7.2)* |

### 7.1 ✅ Les quatre surfaces éprouvées sont passées à la matière — 15/08/2026

**Demandé par Xavier, et c'est ce qui l'autorisait :** 🔴 **la prévisibilité est une fonctionnalité**, donc un écran qui sert en situation ne change pas d'apparence sans que Xavier l'ait décidé. Les deux thèmes ne cohabitent plus.

| Surface | Ce qui a changé |
|---|---|
| **Écrans de crise** — mot-code, tension appliquée, séquence de soins, rester assis, phrase pour le soignant, quand arrêter | Panneaux extrudés, ruban azur, **un titre par vue** au lieu d'un titre répété dans le corps. **Structure inchangée** (§4.5) : mêmes boutons, mêmes repères, mêmes critères d'arrêt. *(Le rangement, lui, a bougé le soir même — §7.2)* |
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

### 7.2 ✅ La deuxième passe — 15/08/2026

**Cinq corrections de Xavier, toutes appliquées.** ⏳ **Toujours aucun appareil branché** : rien de ce qui suit n'est vérifié à l'œil non plus.

| | Ce qui a changé, et pourquoi |
|---|---|
| ⭐ **Le mot-code se grise pendant l'envoi** | Le bouton cesse de répondre et passe à l'encre douce le temps que le SMS parte, aux deux entrées. L'accusé disait déjà *envoi en cours* ; **il informait sans empêcher.** 🔴 **Le bouton ne disparaît pas** — un bouton absent fait croire que la fonction n'existe pas. Il se réarme à l'accusé du réseau, et au plus tard quand l'accusé s'efface : **rien ne doit pouvoir rester mort.** |
| 🔴 **Une seule croix, en haut à droite, sur tous les panneaux** | Le bouton *Fermer* était en pied de page sur les écrans de crise et sur une étape ouverte, absent des réglages, et présent une fois sur deux au check-in. **Il fallait donc lire la page jusqu'en bas pour savoir comment en sortir, et parfois ne pas l'y trouver.** La croix prend la place du rivet droit de la bande de titre : même place partout, quelle que soit la longueur de la page. ⚠️ **Deuxième exception assumée à « aucune icône seule »**, après la roue dentée (**D4**) |
| 🔴 **Les deux entrées de la crise affichent le même écran** | `PortesDeCrise` — **un seul composable**, appelé par l'écran **BAS** du monde et par `CriseActivity`. Mêmes trois boutons, même hauteur, même écart, mêmes gestes, **mot-code compris : un appui, le message part.** ⭐ **Deux écrans qui font la même chose et ne se ressemblent pas obligent à vérifier lequel on a sous les yeux** — au moment précis où il n'y a rien à vérifier. Le libellé du mot-code porte maintenant le prénom enregistré des deux côtés, au lieu d'un *Chourouk* écrit en dur |
| ⭐ **La phrase pour le soignant sort de la tension appliquée** | Elle était un lien répété dans quatre vues de la parade vasovagale, **et il fallait traverser une parade pour atteindre un texte à montrer.** Elle est maintenant une porte de l'écran de crise, et elle n'a plus de bouton *Retour* : **on n'y entre plus depuis ailleurs, il n'y a nulle part où revenir** |
| 🔴 **La notification est muette** — §6.2, enfin câblé | Plus de boutons, **plus une ligne de corps** : l'icône et le mot *Kokoro*. ⚠️ **Sa destination a changé le soir même — voir §7.3** |

**Ce que ça a déplacé ailleurs :**

- Le nom et la description du canal `kokoro_acces_v1` disent maintenant *écran de crise* et non plus *le mot-code et la tension appliquée*. 🔴 **L'identifiant du canal ne change pas** : les libellés se mettent à jour, l'importance et le silence sont figés à la création — en changer l'identifiant rendrait à Android le droit de resonner.
- `controle_acces_explication`, dans les réglages, décrivait les deux boutons. Réécrit.
- Le check-in perd ses boutons *Fermer* ; l'écran *C'est enregistré* n'a donc plus de bouton du tout. **C'est un constat, pas une étape** : la croix en sort.

⏳ **Ce qu'il reste à mesurer sur l'appareil, et qui ne se décide pas au clavier :** le mot-code doit rester **à deux taps de l'écran verrouillé** (§6.2). Si le monde met plus de temps à s'afficher au-dessus du verrouillage que `CriseActivity` n'en mettait, **le repli est prêt** — il suffit de renvoyer la notification sur `CriseActivity`, qui affiche désormais le même écran.

> ✅ **Réponse le soir même : le repli a été pris — §7.3.** Ce n'était pas une question de vitesse, mais de keyguard.

### 7.3 ✅ Ce que le téléphone a répondu — 15/08/2026

**Premier essai réel de la passe précédente. Trois choses en sont revenues, et deux étaient des défauts.**

| Ce que Xavier a constaté | Ce que ça voulait dire, et ce qui est fait |
|---|---|
| 🔴 **« La notification demande de déverrouiller le téléphone quand on est sur le lockscreen »** | ⭐ **C'est le point dur du §6.2 qui se réalise, et il ne se contourne pas au réglage.** Le monde vit dans **la tâche du lanceur** ; `setShowWhenLocked` posé à l'exécution dans `onCreate` **arrive après la décision du keyguard**, qui a déjà choisi de demander le code. `CriseActivity`, elle, le **déclare dans le manifeste**, avec sa propre tâche (`taskAffinity=""`, `singleTask`, `excludeFromRecents`) — **c'est cette configuration-là qui a été éprouvée le 10/08**, et elle n'a pas bougé depuis. 🔴 **Une porte de crise qui demande un code n'est pas une porte** : en shutdown, taper un code est exactement ce qui manque |
| 🔴 **« La notification ouvre le monde sur l'écran du bas au lieu du panneau de crise »** | **Même cause, même correction : la notification revient sur `CriseActivity`.** ⭐ **Et le repli n'a rien coûté** — §7.2 avait rendu les deux écrans identiques la veille au soir. **C'est exactement pour ça que « deux portes, un seul contenu » était écrit d'avance** : une décision d'implémentation a pu être annulée sans que rien de ce que Xavier voit ne change. Le monde ne s'affiche donc **plus jamais** par-dessus le verrouillage |
| 🔴 **« La notification ne s'est lancée ni à l'installation, ni à l'ouverture de l'app »** | **Elle n'était publiée que par l'écran de contrôle** — et **D10** a justement fait que cet écran ne s'ouvre plus tout seul. Depuis, elle n'existait que si Xavier allait la chercher dans les réglages. **Le monde la republie maintenant à chaque venue.** ⚠️ **Ce n'est pas une relance** : c'est la même notification permanente et muette, réécrite au même identifiant — rien de neuf ne paraît, rien ne sonne, rien ne compte |

> ⚠️ **« À l'installation » n'existe pas, et il ne faut pas l'attendre.** Une application fraîchement installée est *arrêtée* pour Android : **aucun code ne s'y exécute avant le premier lancement manuel** — pas de `BOOT_COMPLETED`, pas de réveil. **Le premier lancement est le plus tôt possible**, et c'est là que ça se fait désormais.
>
> ⭐ **L'autorisation de notification, elle, reste demandée dans les réglages** — jamais à l'ouverture du monde. 🔴 **Kokoro ne vient jamais vers Xavier** : une application qui réclame une permission dès qu'on l'ouvre est exactement le contraire. Sans l'autorisation, `publierAccesCrise` renonce en silence.
>
> ✅ **Ce trou a été bouché dans la foulée — §7.4.**

### 7.4 ✅ L'avis de porte fermée — 15/08/2026

**Le défaut à couvrir :** si l'autorisation de notification n'est pas accordée — refusée, ou **révoquée par Android tout seul**, ce qu'il fait pour les applications peu utilisées, et Kokoro en est une par construction — **la porte du verrouillage disparaît sans que rien ne le dise.**

**Xavier a proposé une pastille rouge sur la roue dentée. Elle a été écartée, par lui, pour trois raisons — et les trois étaient déjà écrites :**

| | |
|---|---|
| 🔴 **D4 : « jamais de pastille dessus »** | La règle visait les pastilles qui **comptent** et qui **réclament** — *rien n'y compte, rien n'y attend*. Un défaut n'est pas ça, donc l'exception était défendable. **Elle n'a pas été prise** |
| 🔴 **Le rouge n'est pas dans la palette** | Ce n'est pas un goût : c'est le registre de l'alarme, évacué partout (§4.5, retrait des numéros d'urgence). L'admettre ici aurait obligé à décider **où le rouge a le droit de réapparaître ensuite**, et à l'écrire |
| ⭐ **Et surtout : une pastille est un sous-entendu** | Elle dit *va voir* sans dire **quoi**. 🔴 **L'invariant est explicite, littéral, sans sous-entendu ni attente implicite** — c'est celui-là qui a tranché, et il ne vient pas d'une règle d'interface |

**Ce qui est fait :** une carte sur **l'écran central** *(⭐ déplacée en tête de l'écran d'entrée le même jour — §7.7)*, l'emplacement que §6.1 réserve déjà à *une carte, une phrase, un fait*. Elle porte le fait — *la notification d'accès n'est plus affichée* — **ce qu'il coûte** — *l'écran de crise n'est plus atteignable depuis l'écran verrouillé* — et **le geste**, un bouton vers les réglages.

⭐ **Ce n'est pas une relance :** elle ne compte pas les jours, elle n'insiste pas, elle ne revient pas — **elle constate, et elle s'en va d'elle-même dès que la porte est rouverte.** ⭐ **Et ce n'est pas Kokoro qui vient vers Xavier** : elle est sur un écran qu'il a ouvert, pas une notification.

> ⏳ **Ce qui reste non couvert, et qu'il faut savoir :** l'avis ne paraît **que quand Xavier ouvre le monde**. Si l'autorisation tombe et qu'il n'ouvre pas l'application, **rien ne le lui apprend** — et il n'y a pas de bonne réponse à ça : le seul moyen de le prévenir sans qu'il vienne serait une notification, **c'est-à-dire exactement la chose qui ne marche plus.**

---
### 7.5 ⭐ La notification d'accès prend une illustration de fond — 16/08/2026

**Demande de Xavier :** une image de fond sur la notification — le monde de Kokoro, le personnage à droite, penché, clin d'œil, main en V. **Puis, après un premier essai sur l'appareil :** *« il faudrait que l'image soit transparente (60 %) et en dégradé vers le transparent sur les côtés pour qu'elle soit bien intégrée »*, **et plus de vue dépliée.**

> ⚠️ **Première cible visée, et fausse :** l'alerte K1. **Elle ne paraît jamais d'elle-même** — seul le bouton de test de l'écran de contrôle la déclenche. 🔴 **La seule notification que Xavier voit est celle d'accès à la crise**, et c'est donc elle qui porte l'illustration.

🔴 **Ce que ça touche à la décision du 15/08 (§6.2), et ce que ça ne touche pas.** Le motif de Xavier était une **ligne relue toute la journée** — *« je lis toute la journée mot code et tension appliquée »*. **Une image ne se relit pas comme une ligne**, et c'est ce qui autorise l'illustration. ⭐ **Aucune ligne de corps ne revient** : `avecIllustration` est appelée **sans `texte`**, le paramètre vaut `null` par défaut, et le gabarit masque le champ tant qu'un appelant ne le demande pas. **Le jour où une ligne y reparaît, c'est §6.2 qu'on défait** — pas un détail de gabarit.

#### Une seule vue, et pas de chevron

🔴 **Il n'y a pas de `bigContentView`.** La notification d'accès n'a **rien de plus** à montrer en grand — pas une ligne, pas un bouton — donc l'ouvrir ne donnait rien. ⭐ **Ne pas en poser retire aussi le chevron** : elle cesse d'annoncer un contenu qu'elle n'a pas. **Une affordance qui ne mène à rien est un sous-entendu**, et c'est l'invariant qui a déjà écarté la pastille de §7.4.

#### L'intégration passe par le canal alpha, pas par du code

**Le premier essai posait un rectangle net sur la carte du volet, bords francs sur les quatre côtés, et un voile de papier opaque sous le texte qui faisait un bloc blanc au milieu.** Trois corrections, et elles se tiennent :

| | |
|---|---|
| ⭐ **La transparence est cuite dans le WebP** | 60 % d'opacité au centre, fondu vers le transparent sur les quatre côtés. 🔴 **Un masque alpha n'existe pas en drawable XML, et une `RemoteViews` ne sait pas composer deux couches** — il n'y avait pas d'autre voie |
| 🔴 **`fitXY`, jamais `centerCrop`** | `centerCrop` recadre, donc il jetterait hors du cadre **précisément les bords qui adoucissent**. Le ratio du bandeau (**4,6**) est calé sur la zone de contenu mesurée sur l'appareil de Xavier, ce qui rend l'étirement imperceptible |
| 🔴 **Le voile est supprimé** | C'était lui, le bloc blanc. **C'est l'image qui s'efface à gauche** — fondu large de ce côté, court à droite où vit le personnage — et le texte se lit sur la carte du système |

⚠️ **Le texte ne porte plus de couleur en dur.** `#383838` était illisible dès que le volet passait au sombre, **ce qui est le cas sur l'appareil de Xavier**. `TexteNotifTitre` hérite désormais de `TextAppearance.Compat.Notification.Title` *(androidx)*, qui suit le thème de la notification. ⭐ **Varela Round est la seule chose qu'on ajoute** — la couleur ne nous appartient pas.

#### Ce qui reproduit l'image

⭐ **`npm run companion:fondu` est versionné**, et c'est ce qui rend le réglage possible : l'opacité, l'ancrage de la bande et les trois largeurs de fondu sont des drapeaux. **Sans lui, `notif_bandeau.webp` aurait été un binaire que plus personne ne sait refaire.**

```
npm run companion:fondu -- notification-fond.png \
  companion/android/app/src/main/res/drawable-nodpi/notif_bandeau.webp
```

- 🔴 **Le fond d'une notification appartient au système, pas à l'application.** Depuis Android 12, une notification custom est enveloppée dans le gabarit d'Android. **Ce qui est peint, c'est la zone de contenu.** `DecoratedCustomViewStyle` est déclaré exprès : le système décorerait de toute façon, autant que le rendu soit prévisible.
- 🔴 **`setContentTitle` reste posé**, et ce n'est pas un doublon : une `RemoteViews` n'atteint ni Wear, ni Auto, ni certaines surcouches. **Sans lui, la notification y serait vide.**
- ⚠️ **Le poids compte** : une `RemoteViews` traverse un IPC borné. Le bandeau est en WebP, **30 ko**.
- ⭐ **Les deux notifications partagent une seule fabrique** — `ui/VueNotification.kt`. L'alerte K1 y passe aussi, avec sa ligne de texte à elle.

> 🔴 **Le personnage y déroge à quatre règles de [`CORPS.md`](CORPS.md)** — inclinaison, clin d'œil, main à doigts, geste d'évaluation. **L'écart a été présenté à Xavier point par point et il l'a demandé quand même ; il est acté, borné et écrit à [`CORPS.md` §10.1](CORPS.md).** ⭐ **Le rig ne bouge pas** : `kokoro-corps-v2.svg`, `Geometrie.kt` et `CorpsInvariantsTest` sont intacts, et **le corps animé de l'application ne connaît toujours ni main, ni clin d'œil, ni inclinaison.**

---

### 7.6 ⭐ Le logo de Kokoro devient l'icône de l'app — 16/08/2026

**Xavier a dessiné le logo** — `companion/ressources/retenus/logo.jpg` : le personnage de face, clin d'œil, main en V, le 心 sur la poitrine, sur le bleu du ciel. **Il est appliqué partout où Android montre une icône, et ces endroits ne sont pas un seul.**

| Où Android la montre | Ce qui la porte | Ce que ça change |
|---|---|---|
| **Écran d'accueil, tiroir, récents** | `mipmap-anydpi-v26/ic_lanceur.xml` — icône adaptative | Il n'y avait **aucune icône déclarée** : Android affichait son robot vert par défaut |
| **Écran de démarrage** *(Android 12+)* | La **couche avant** de la même icône, sur `windowSplashScreenBackground` | Le fond du démarrage était le gris du thème Material ; il passe au noir de Kokoro |
| **Icônes thématiques** *(Android 13+)* | La couche `monochrome` | Sans elle, le système fabriquerait sa propre pastille |
| **Barre de statut et volet de notifications** | `drawable-*/ic_kokoro.png` | Remplace **le cercle vide** qui tenait la place depuis K2 |
| **Réglages d'Android, gestionnaire d'applications** | `android:icon` sur `<application>` | Une seule déclaration, tous ces endroits |

**Les trois choses qui se décident, et pourquoi elles se décident ainsi :**

| | |
|---|---|
| 🔴 **Le logo est dans la couche *avant*, jamais dans le fond** | L'écran de démarrage d'Android **ne reprend que l'avant** de l'icône adaptative. Un logo posé au fond y disparaîtrait : le lancement s'ouvrirait sur un rond vide |
| 🔴 **Le personnage est calé sur les 72 dp garantis, et son pourtour prolongé jusqu'aux bords** | Une icône adaptative fait 108 dp mais **n'en montre que 72 à coup sûr** — le reste, chaque lanceur en décide. ⭐ **Le logo déborde de son cadre : la main et le corps touchent les bords.** Le poser en pleine toile aurait fait couper la main par le masque rond ; le poser sur 72 dp en laissant le reste vide aurait montré une **couture carrée** dès qu'un masque en découvre plus. **Les pixels de bord sont donc étirés dans la marge** — le dégradé du logo étant vertical, une ligne étirée horizontalement le prolonge exactement |
| ⭐ **Les deux icônes en aplat ne sont pas un détourage** | Une icône de notification est **repeinte d'une seule couleur** par le système : une photo y devient une tache. **On garde les aplats clairs du personnage et on perce ses traits** — yeux, sourire, contours redeviennent des trous. Le tri se fait sur la « froideur » du pixel *(bleu − rouge)* : le fond cyan la pousse au-delà de 45, la crème du personnage la rend négative, le gris des traits reste sous 20 |

**Tout est fabriqué par [`companion/scripts/companion-icone.ts`](scripts/companion-icone.ts)** — `npm run companion:icone` — **en cinq densités**, à partir du seul JPEG. 🔴 **Aucune image d'icône ne se retouche à la main** : le logo change, on relance le script.

> ⚠️ **Ce qui n'a pas bougé, et n'avait pas à bouger :** l'illustration de fond de la notification *(§7.5)*, qui vient d'une autre image et raconte une autre chose ; le corps animé du personnage *(`CORPS.md`)*, qui ne connaît toujours ni main ni clin d'œil ; et **aucun écran de l'app n'affiche le logo** — les deux seules icônes seules restent la roue dentée **(D4)** et la croix *(§7.2)*.

---

### 7.7 🔄 La croix devient un anneau horizontal — 15/08/2026

**Demande de Xavier**, en cinq points : *plus de haut ni de bas · le parallaxe devient entièrement horizontal, ce qui rend le défilement vertical à tous les écrans · la navigation horizontale est infinie — au dernier écran, glisser encore donne le premier · **et ça continue le parallaxe, ce n'est pas un retour au premier écran** · on arrive sur la thérapie, avec Kokoro quelque part.*

**Ordre acté : Thérapie → Documentation → Bilan → Crise → Thérapie…** ⭐ **Ce que ça donne gratuitement : la crise est aussi le voisin de gauche de l'entrée**, donc elle reste à **un seul geste** de l'ouverture de l'app — la propriété que D2 était allée chercher en la mettant en bas, obtenue cette fois **dans les deux sens**.

#### Ce que la forme change, point par point

| | |
|---|---|
| ✅ **P1 est levé** | Le glissement vertical n'appartient plus au monde : **il est rendu au contenu**. Le bilan défile désormais comme la documentation, et **plus aucun contenu n'est logé ailleurs que là où il a du sens** |
| ⭐ **Il n'y a plus d'axe à verrouiller** | Le monde n'écoute que le glissement **horizontal** ; un mouvement vertical ne lui parvient jamais et va à la liste. **Deux gestes, deux destinataires, aucun arbitrage** — et plus aucun geste oblique dont le résultat dépendrait de sa précision |
| 🔴 **La position ne se replie jamais** | La caméra est un nombre qui court **sans borne**, et l'écran montré est sa position **modulo quatre**. Replier la position au passage du dernier écran remettrait le décor à zéro : **c'est exactement le saut que l'anneau existe pour éviter** |
| ⭐ **Quatre positions peintes, quatre écrans distincts** | On peint l'écran à l'image, son voisin, et une marge de chaque côté — soit exactement un exemplaire de chacun. **Aucun écran n'est monté deux fois**, donc l'état de chacun *(le défilement d'une liste, par exemple)* survit aux tours : ce n'est jamais une copie qui revient, c'est le même |
| 🔴 **Le décor perd son débattement vertical** | Il disait la profondeur quand la traversée était une croix. **La caméra n'a plus de composante verticale** — un décor qui bougerait avec une liste qui défile lui donnerait une profondeur qu'il n'a pas. La répétition en miroir, elle, ne change pas : c'est elle qui rend l'anneau gratuit, **pour le décor, revenir sur le premier écran n'est qu'un écran de plus dans le même sens** |
| ⭐ **La butée franche disparaît sans rien laisser derrière** | Elle existait pour ne pas avoir d'élastique aux bords du monde. **Il n'y a plus de bord** : rien ne part et ne se rétracte, rien ne demande à être interprété |

#### Ce qui a dû déménager

L'écran central n'existe plus, et il portait trois choses. **Toutes les trois vont sur l'écran d'entrée — la thérapie.**

| | Où | Pourquoi là |
|---|---|---|
| **Kokoro** | ⏳ **En tête de la liste**, et il s'en va avec elle quand on défile | **Provisoire, et dit comme tel.** Comment il habite vraiment l'écran touche au corps, pas au rangement — ça se décide à part |
| **La roue dentée** *(D4)* | **Sur la bande de titre**, à la place du rivet de droite — exactement où la croix se pose sur les panneaux *(§7.2)* | 🔴 **Le rivet de droite est la seule place où un bouton puisse paraître, et il n'en tient qu'un** : une bande porte **soit** la croix, **soit** la roue dentée, jamais les deux |
| **L'avis de porte fermée** *(§7.4)* | **En tête de l'écran d'entrée**, au-dessus de Kokoro | Il ne paraît que sur défaut, et il disparaît de lui-même quand le défaut est réparé. **Toujours une phrase, jamais une pastille** |

> ⭐ **La crise, elle, ne défile toujours pas** — 🔴 **et c'est une exigence propre, pas une conséquence de P1.** En crise, une liste qui bouge sous le doigt est une chose de plus à maîtriser. **La levée du point dur ne rouvre pas cette question-là.**

> ⚠️ **Ce qui n'a pas bougé :** le seuil de bascule et l'élan *(inchangés à 0,18 écran et 0,7 écran/s)* · le ressort qui pose la caméra à la vitesse du doigt · le fait qu'**on ne saute jamais deux écrans**, si lancé soit le geste · les quatre couches du décor · la matière, les rubans, les cartes · **et toutes les portes de crise, qui n'ont pas été retouchées d'une ligne.**
