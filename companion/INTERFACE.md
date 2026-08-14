# Kokoro — l'interface du monde

**v1 — 14/08/2026.** Le monde devient **l'interface principale** de l'app, et jusqu'à l'écran de veille.

> 📐 **Onze décisions tranchées par Xavier le 14/08/2026** — §5. Le décor est spécifié dans [`DECOR.md`](./DECOR.md), le personnage dans [`CORPS.md`](./CORPS.md), le contrat de contenu dans [`../../../PLAN.md` §8](../PLAN.md#8-le-programme--format) — **ce document ne fait que ranger** ce que ces trois-là ont décidé.
>
> 📌 **Il ne décide jamais quel contenu clinique existe.** Ça se décide en séance.
>
> ⏳ **Ce qui reste ouvert** — les bornes des nouveautés (§6.1), l'essai du fond d'écran vivant (§6.3), la cadence de l'entraînement en solo (§6.4), et le comportement du personnage, renvoyé à son propre brainstorm.

---

## 1. Ce qu'il y a à ranger

### 1.1 Ce qui est déjà construit et tourne sur le téléphone

| Surface | Jalon | Ce que c'est | Taille à l'écran |
|---|---|---|---|
| **Accès crise** | K2 ✅ | Notification sur l'écran verrouillé — **une porte, pas un rappel** | hors monde |
| **Mot-code à Chourouk** | K2 ✅ | SMS composé et prêt, téléphone verrouillé, sans réseau data | 1 écran plein |
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

Aucun score · aucune progression · aucun historique · aucun palier atteint · aucun streak · aucune notification ni relance · aucun numéro d'urgence, **3114 compris** · le PHQ-9 · rien de `psy/outputs/dossier/` (profil, état, séances, mesures, briefs) · aucune police, aucun service, aucune image venus d'ailleurs.

---

## 2. Les points durs

| # | Le point | Ce qu'il impose |
|---|---|---|
| 🔴 **P1** | **Un écran du haut ou du bas ne peut pas contenir de liste qui défile verticalement** — le glissement vertical est déjà pris par la traversée du monde. Sur la gauche et la droite, le vertical est libre : la butée y est franche, donc le geste n'y sert à rien d'autre | **Les deux contenus longs vont à gauche et à droite. Le haut et le bas ne portent que ce qui tient en un écran.** C'est ce qui a décidé le rangement du §3 |
| **P2** | Les gestes système de One UI mordent les bords : volet de notifications en haut, retour à l'accueil en bas | Un geste de traversée verticale part **du milieu de l'écran**. À mesurer sur l'appareil |
| **P3** | Le décor ne porte jamais de texte ([`DECOR.md`](./DECOR.md) §7) | Le texte est sur une **surface posée sur le décor**, jamais peint dessus |
| 🔴 **P4** | ⭐ **Le flou du verre dépoli ne doit jamais venir du système.** `Window.setBackgroundBlurRadius` dépend d'un réglage Android que l'économiseur de batterie coupe — l'apparence changerait parce qu'un réglage étranger a bougé, **exactement ce que la doctrine interdit au thème sombre** | **Le flou se calcule dans notre propre rendu** (capture du décor en `GraphicsLayer` + `RenderEffect`), jamais par l'API de fenêtre. Voir §4 |
| 🔴 **P5** | ⭐ **La lisibilité ne doit jamais dépendre de ce qui est derrière.** Un texte posé sur un décor flouté passe du feuillage sombre au ciel clair en glissant | **Une teinte constante par-dessus le flou**, opaque à ~60 %. Le flou fait la profondeur, la teinte fait le contraste. Les deux ne se remplacent pas |
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
- **Les trois `quand` sont des sous-titres écrits** — *Aujourd'hui* · *Quand j'en ai besoin* · *Sans date*. 🔴 **Aucun code couleur, aucune pastille, aucun badge.**
- **Une carte par étape** : le titre, la durée si elle est connue, rien d'autre. Pas de chevron, pas d'aperçu, pas de compteur.
- **Le décor reste visible** autour et entre les cartes — on est toujours dans le même monde, jamais dans une autre application.
- **Une étape ouverte prend l'écran entier** et se ferme d'un bouton écrit *Fermer*, jamais d'un geste : rien ne doit concurrencer la traversée.

---

## 4. Le thème — verre dépoli *(D6)*

**Les surfaces sont du verre dépoli posé sur le paysage** : le décor se devine derrière, flouté, et continue de glisser en parallaxe pendant qu'on traverse. C'est la seule matière qui rende le parallaxe *visible à travers l'interface* au lieu de le masquer.

### 4.1 La recette

| | Jour | Nuit |
|---|---|---|
| Teinte du verre | `#F4F1EA` à **60 %** | `#16222C` à **62 %** |
| Encre | `#20262B` | `#E6EBEE` |
| Encre douce | `#59636B` | `#9AA8B2` |
| Accent | ⭐ **la couleur de la plaque de poitrine de Kokoro**, et elle seule | idem |
| Flou | **24 dp**, gaussien, sur la capture du décor uniquement | idem |
| Liseré | 1 dp de blanc à 18 % sur le bord supérieur | 1 dp de blanc à 10 % |

- 🔴 **Flou et teinte sont deux choses différentes, et aucune ne remplace l'autre** (**P5**) : le flou donne la profondeur, la teinte garantit le contraste **quel que soit ce qui passe derrière**. Sans la teinte, un titre lisible sur le feuillage devient illisible sur le ciel trois centimètres plus loin.
- 🔴 **Le flou est calculé par l'app, jamais par la fenêtre** (**P4**) — sinon l'économiseur de batterie change l'apparence sans prévenir.
- **Repli prévu et écrit** : si le flou ne tient pas les 60 images/seconde pendant la traversée, on tombe sur **la teinte seule à 78 %**. C'est le même dessin, moins la profondeur — et c'est un choix qu'on fait une fois, pas un basculement à l'exécution.
- **La nuit suit la plage horaire du décor** ([`DECOR.md`](./DECOR.md) §5) — lue à l'arrivée, jamais sous les yeux, jamais le thème système.
- **Rayon 20 dp, aucune ombre portée dure.**
- **Typographie : la famille du système** (aucune police distante). Corps **18 sp**, titres **22 sp**, interligne large — lisible en shutdown, c'est-à-dire lisible quand on ne peut plus faire d'effort.
- **Boutons pleine largeur, ≥ 64 dp, un par ligne**, libellé en toutes lettres.
- **Le retour au toucher est immédiat mais sourd** : le verre s'assombrit d'un cran. Aucune onde, aucun rebond. *(La règle des ≥ 800 ms vaut pour les expressions du visage, pas pour l'accusé de réception d'un appui — un appui qui ne répond pas tout de suite se re-tape.)*
- 🔴 **Aucun rouge, nulle part — écran de crise compris.** Le rouge est une alarme, et l'écran de crise doit faire l'inverse : **il se distingue en étant plus grand et plus vide, pas plus vif.**
- **Aucune couleur d'état** : ni vert *fait*, ni orange *en retard*. Il n'y a pas de retard dans ce dispositif.

### 4.2 Ce que ça coûte, dit franchement

Le flou se recalcule **à chaque image pendant la traversée**, puisque le décor bouge derrière. Sur le S22 c'est jouable ; ce n'est pas gratuit, et **la traversée est ce qu'il ne faut surtout pas rendre saccadée** — la fluidité du geste a coûté deux corrections le 14/08. **Ordre de construction imposé : le verre s'ajoute après que la traversée est fluide, et se mesure, pas s'estime.**

### 4.3 L'écran de crise *(D7)*

**Même matière, structure inchangée.** Il garde ses trois boutons, son minuteur, ses critères d'arrêt, ses repères externes — on ne touche qu'à la peau.

🔴 **Deux écarts assumés, et ils vont dans le même sens :** le verre y est **plus opaque** (≥ 85 %) et **sans flou**. La lisibilité y prime sur la profondeur, et rien de ce qui s'affiche au pire moment ne doit dépendre d'un calcul qui peut ramer.

---

## 5. Les décisions — tranchées le 14/08/2026

| # | Décision | Ce qui est acté |
|---|---|---|
| **D1** | Le rangement | ✅ **Une rubrique par écran** (§3) |
| **D2** | Le bord de la crise | ✅ **BAS** |
| **D3** | Kokoro suit-il ? | ⏭️ **Il suivra l'interface — hors sujet ici, renvoyé à son propre brainstorm.** En attendant il reste au centre |
| **D4** | L'écran de contrôle | ✅ **Une roue dentée en haut à droite de l'écran central.** ⚠️ **Exception assumée à « aucune icône seule »** : c'est le seul pictogramme universel du lot, et le centre n'a pas de place pour un mot. 🔴 **Jamais de pastille dessus** |
| **D5** | Les nouveautés | ✅ **Sur l'écran central.** Bornes au §6.1 |
| **D6** | Le thème | ✅ **Verre dépoli** (§4) |
| **D7** | L'écran de crise | ✅ **Adapté à la matière, structure inchangée** (§4.3) |
| **D8** | L'overlay de veille | ✅ **À essayer — fond d'écran vivant** (§6.3) |
| **D9** | Ce qu'on voit en veille | ✅ **Le décor et Kokoro qui respire, rien d'autre** |
| **D10** | L'icône du lanceur, et la notification | ✅ **L'icône ouvre le monde.** 🔴 **La notification est muette : plus de boutons, et plus un mot de son contenu** — elle ouvre le monde **directement posé sur l'écran de crise, sans animation** (§6.2) |
| **D11** | Le titre de l'écran | ✅ **Il ne défile pas.** *(La question posée était : quand on fait défiler la liste des étapes, le mot « Thérapie » part-il vers le haut avec elle ? Non — il reste posé sur une bande de verre en haut. Savoir où l'on est ne doit pas dépendre d'où l'on en est dans la liste.)* |

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

**Oui, « thérapie » est bien la liste des prochaines actions de la thérapie en cours**, et **oui, une séance à deux s'y trouve pour être jouée** — le contrat lui donne déjà `rubrique: therapie` ([§8.3](../PLAN.md#83-une-étape)).

🔴 **Mais « une catégorie *Séances à deux* dans la documentation » n'existe pas dans le contrat.** La bibliothèque n'a aucun champ de catégorie : les fiches se groupent par `quand`, comme tout le reste. **L'ajouter, c'est modifier `PLAN.md` §8, qui est normatif — donc un acte de séance, supervisé, pas un choix d'écran.**

**Et sur l'entraînement, il y a un point de fond :** l'entraînement **n'est pas un contenu séparé**, c'est **la même étape jouée à blanc**, qui renvoie `issue: "entrainement"`. Le mettre dans la documentation ferait apparaître une même étape à deux endroits — ce qui casse la propriété qui a fait choisir ce rangement.

| Ce qui va où | |
|---|---|
| **Thérapie** | **La séance à deux elle-même, une seule entrée.** ⭐ **Ce n'est pas un bouton de plus dans la liste : c'est le premier écran de l'étape** — voir ci-dessous |
| **Documentation** | Ce qui se **lit** : *Ce qu'est une séance à deux* · *La fiche pour Chourouk* (`montrable`) · *Le signal d'arrêt, convenu à froid*. Des textes, y compris lisibles par l'aide-au-patient. ✅ **Validé le 14/08/2026** |

#### ⭐ L'écran de choix — acté le 14/08/2026

**Ouvrir une séance à deux ne la démarre pas.** Le premier écran de l'étape pose une question, et rien d'autre : **deux boutons pleine largeur**, la vraie séance, ou l'entraînement.

> ⚠️ **Xavier : « le bouton toujours visible, je sens qu'on va manquer de place. »** — **Il a raison, et l'écran de choix règle exactement ça** : la carte dans la liste ne porte que le titre, comme toutes les autres. Le choix vit dans l'étape, pas dans la liste. **Zéro place consommée ailleurs.**

| | |
|---|---|
| **Avant le premier entraînement** | 🔴 **Seul l'entraînement est proposé.** `entrainement_requis` vaut toujours `true` ([§8.3](../PLAN.md#83-une-étape)) : la première fois que ça compte ne doit pas être la première fois que ça se fait |
| **Ensuite** | Les deux boutons. ⭐ **On se réentraîne aussi la veille d'une vraie séance** — ce chemin ne se referme jamais |
| ⭐ **L'entraînement est jouable en solo par l'aide** *(précision de Xavier, 14/08/2026)* | Chourouk peut le répéter **seule**, sans Xavier. Rien ne s'y oppose : le contenu d'une séance à deux **ne porte rien sur Xavier** (contrôle **C10**), donc il se répète sans lui |
| **Ce que ça renvoie** | `issue: "entrainement"` — **ce n'est pas une donnée clinique, et rien ne s'en déduit** |

⏳ **Un point reste ouvert : la cadence en entraînement.** Une séquence réelle tient 22 minutes, dont des silences de 60 secondes. Les tenir seule, à blanc, n'apprend rien de plus que de les avoir lus. **Proposition : en entraînement seulement, un bouton *Suite* permet à l'aide d'avancer à son rythme** ; en séance réelle, jamais — le temps y est tenu par l'appareil, c'est le sens du type. À confirmer.

### 6.5 Renvoyé ailleurs

- **Le comportement du personnage** — D3, son propre brainstorm.
- **La séance à deux (K6)** ne ressemble à aucun autre écran : signal d'arrêt permanent, critères d'arrêt à un tap, deux lecteurs, un chronomètre. **Elle mérite son propre passage.**
- **P2** — les gestes système en haut et en bas : à mesurer sur l'appareil, pas sur le papier.
