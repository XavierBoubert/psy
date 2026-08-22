# `KOKORO-GENERIQUE.md` — rendre Kokoro paramétrable sans rien perdre

> **Objet : une seule application Android, plusieurs dossiers patients.** Ce que Kokoro sait faire ne change pas ; **ce qu'il affiche cesse d'être écrit dans son code** et devient du contenu, comme les cartes le sont déjà.
>
> 🔴 **Aucune régression.** Le dispositif de Xavier doit sortir de ce chantier **identique à l'écran** — mêmes quatre pages, même personnage, mêmes gestes, mêmes protections. La grille §8 en est le contrat.

---

## 1. Le verdict — l'idée tient, et elle simplifie plus qu'elle ne complique

**Ce qui est aujourd'hui codé en dur et n'a aucune raison de l'être** : le nombre d'écrans *(`enum Ecran`)*, leur titre, leur couleur, leur ordre dans l'anneau, la rubrique qui décide de l'écran d'une carte, les trois `quand`, les six perchoirs, les trois boutons de crise, la cible de la notification. **Tout cela devient `kokoro.yaml`.**

**Ce qui est structurel et reste dans l'app** — et ce n'est pas négociable :

| Reste dans l'app | Pourquoi |
|---|---|
| Le personnage : 15 pièces, 10 postures, 6 expressions, respiration, clignement, vol | 🔴 **Jeu fermé.** Un projet ne dessine pas une posture, il en **choisit** une |
| Les 5 couleurs, la matière, la typographie, l'enfoncement 90 ms | 🔴 **Aucun rouge n'est possible parce que la palette n'en contient pas.** Ouvrir la couleur rouvrirait la question à chaque projet |
| Les 7 familles d'interdits *(`Interdits.kt` + `psy-publish.ts`)* | 🔴 **Le plancher du dispositif, pas un réglage de projet** |
| Les 6 types d'étape, `sortie_libre`, la règle « la carte tombe entière » | 🔴 **Un item perdu produit un score faux, donc faussement rassurant** |
| La tension appliquée, protocole minuté *(§2, Q1)* | Boucle à cycles : hors grammaire des étapes |
| Zéro réseau, zéro son, zéro vibration, zéro streak | 🔴 Invariants testés, valables pour tout patient |

⭐ **Le gain net de code est positif** : la perche fixe *(APP4.b)* supprime à elle seule `Perchoir.PLAFOND`, `horsCadre()`, `auRepos()`, `MONTEE_DU_PERCHOIR`, `BandeDeTete`, `Modifier.poser()` et l'écrêtage vertical de `Habitant.kt`. **Une page ne mesure plus qu'un rectangle : sa perche.**

**Le transit reste en JSON.** `kokoro.yaml` est ce que Claude Psy écrit dans le dépôt ; `psy:publish` le valide et **émet `kokoro.json`** vers Drive. `Json.kt` — 149 lignes, zéro dépendance sur l'appareil — ne bouge pas. ⚠️ **Un lecteur YAML écrit à la main écarterait une carte en silence**, exactement ce que « la carte tombe entière » refuse.

---

## 2. Les deux questions de contenu

### Q1 — La tension appliquée devient un type de carte à part entière

**Elle n'est pas exprimable en étapes, et elle ne doit pas l'être.** `ContenuTension.kt` (286 l.) et `SequenceSoins.kt` portent une **boucle à cycles avec états** *(séquence, bloc, assis-après, arrêt)* ; les six types d'étape n'ont ni répétition, ni branchement, ni horloge partagée. La réécrire en étapes demanderait une grammaire de boucle — **et ferait ré-authorer un protocole clinique par chaque projet.**

🔴 **`type: tension-appliquee`** — nommé, pas générique. Le protocole reste dans l'app ; le YAML décide s'il existe, où il se pose, et **quels sont ses critères d'arrêt** *(§4, Q3)*.

```yaml
- id: tension-appliquee
  titre: La tension appliquée
  type: tension-appliquee
  arret:
    - "Céphalée ou douleur dans la poitrine pendant les cycles : arrêter. …"
    - "Perte de connaissance : s'allonger, jambes surélevées, …"
    - "S'il n'y a ni aiguille, ni geste médical, ni sang, ce n'est pas la bonne parade. …"
```

Un projet qui ne déclare pas cette carte ne l'a pas. **Le code de la tension ne bouge pas d'une ligne.** ⭐ **Le type de carte est un nom de protocole, jamais un `module: …` à décoder** — le contrat se lit sans table de correspondance.

### Q2 — La phrase pour le soignant

**Elle est déjà exprimable, sans rien ajouter au contrat.** C'est un `panneau` d'une étape `info` avec `montrable: true` — exactement ce que le contrat prévoit pour le texte qu'on montre à quelqu'un d'autre.

```yaml
- id: phrase-soignant
  titre: La phrase pour le soignant
  type: panneau
  dialog: false
  sortie_libre: true
  etapes:
    - { type: info, montrable: true, texte: "…" }
    - { type: info, texte: "…" }
```

### `dialog` — la bulle et le personnage sous le panneau

**Aujourd'hui c'est `LocalPanneauPorte`, posé globalement par `CriseActivity`.** Il devient **un champ de la carte** : `dialog: true` ouvre le panneau **avec la queue de bulle et Kokoro dessous**, `dialog: false` l'ouvre nu.

| | Règle |
|---|---|
| Portée | 🔴 **`panneau` seul.** Un `pdf` sort de l'app, un `sms` n'ouvre rien, la tension a sa propre mise en scène |
| Défaut | **`true`** — c'est le comportement actuel dans le monde, donc aucune régression sur les cartes existantes |
| Page `boutons` | 🔴 **Forcé à `false`, et `dialog: true` y est refusé à la publication.** *« Les panneaux de crise ne portent aucun personnage »* est une des six bornes testées de la dérogation de crise — **elle doit rester mécanique, pas confiée à la vigilance de qui écrit le YAML** |

---

## 3. P1 — Le cache : viable, mais pas une copie toutes les 5 secondes

**L'intention est juste** *(sans elle, rien de ce chantier ne passe : §6)*. **Le « copier-coller avant chaque lecture » ne l'est pas** : 39 Ko réécrits toutes les 5 s font ≈ 670 Mo de gravure par jour, pour un fichier qui change **quelques fois par semaine**.

🔴 **Quatre règles, et le mécanisme tient :**

1. **Écrire seulement quand le contenu change.** La lecture Drive rapporte déjà le texte en mémoire : `si texteLu != texteEnCache → écrire`. Comparaison de chaînes, **aucune lecture supplémentaire**, une écriture par publication.
2. **N'écrire qu'après analyse réussie.** ⚠️ Une lecture Drive tronquée ou en cours de synchronisation **ne doit jamais devenir la source de l'écran de crise**. Le cache ne reçoit que ce qui a produit une `Config` valide.
3. **Écriture atomique** — fichier temporaire puis `rename`. Une écriture interrompue laisserait un demi-fichier là où la crise va lire.
4. ⭐ **Inverser l'ordre au démarrage** : lire le cache **d'abord** *(local, instantané)*, peindre, puis laisser Drive corriger. **Bénéfice de bord : le plafond d'attente de 6 s ne se paie plus qu'à la toute première ouverture.**

**Le rythme de relecture Drive, lui, ne change pas** — 5 s, hors fil principal, exactement comme aujourd'hui.

---

## 4. Q3 — Les noms propres dans l'interface

| Nom | Où | Verdict |
|---|---|---|
| **Xavier** | Uniquement dans les **commentaires** Kotlin et XML *(11 occurrences)* | ✅ **Jamais rendu.** Rien à faire — les commentaires disent *pourquoi* une contrainte existe, ils suivront la doc |
| **Chourouk** | `NOM_PAR_DEFAUT` dans `Reglages.kt` — **rendu** sur le bouton *Mot-code à Chourouk*, dans le panneau mot-code, dans l'accusé d'envoi, dans le champ des réglages | 🔴 **Devient le nom de l'aidant aux réglages** *(ci-dessous)*, avec un défaut porté par le YAML |
| **Dr Isorni** | `arret_douleur` — **rendu**, dans les critères d'arrêt de la tension appliquée | 🔴 **Devient le nom du psychiatre aux réglages**, cité par le jeton `{psychiatre}` dans le `arret:` de la carte *(§2, Q1)*. Le champ `arret:` **existe déjà** au contrat pour les cartes portées par l'aidant — on le réutilise, on n'en invente pas |

⚠️ **Au-delà des noms, un texte porte le dossier de Xavier mot pour mot** : `phrase_texte` — *« phobie du sang », « évanoui trois fois »*. Il quitte `strings.xml` par Q2, et c'est le bon geste : **ce n'est pas une chaîne d'interface, c'est du contenu clinique.**

✅ **Le reste de `strings.xml` (151 chaînes) est du vocabulaire d'application** — libellés de réglages, autorisations, minuteurs, accusés. Il reste dans l'app.

### Les trois noms aux réglages

**Une section *Les personnes*, trois champs** — `patient`, `aidant`, `psychiatre` — posés maintenant, **utilisés au fur et à mesure**.

| | Réglage | Défaut | Jeton |
|---|---|---|---|
| Patient | `nom_patient` | `app.personnes.patient` | `{patient}` |
| Aidant | `nom_aidant` | `app.personnes.aidant` | `{aidant}` |
| Psychiatre | `nom_psychiatre` | `app.personnes.psychiatre` | `{psychiatre}` |

- 🔴 **`contactNom` disparaît, il ne se dédouble pas.** Le destinataire du mot-code **est** l'aidant : un second champ portant le même nom serait deux vérités pour une personne. La section *Destinataire du mot-code* ne garde que **le numéro et le message**.
- **Les jetons se résolvent à l'affichage, partout où un texte du YAML en porte un** — titre de carte, `info`, `arret`, `enonce`. `{contact}` disparaît au profit de `{aidant}`, qui dit qui c'est.
- ⚠️ **Un jeton cité sans défaut déclaré fait refuser la publication** *(§7)* : sans ça, un réglage vide afficherait *« Mot-code à  »*.
- 🔴 **Aucun numéro de téléphone ne transite, jamais** — le YAML porte des prénoms, l'appareil porte le numéro.

---

## 5. Le contrat — `companion/inputs/kokoro.yaml`

```yaml
version: 17
publie_le: 2026-09-05
supervision: 2026-09-05-programme-v17

app:
  notification:
    active: true
    page: crise          # 🔴 doit exister, et être voisine de l'entrée dans l'anneau
  mot_code:
    actif: true
  personnes:             # défauts des réglages ; 🔴 aucun numéro ici, jamais
    patient: Xavier
    aidant: Chourouk
    psychiatre: Dr Isorni

pages:
  - id: therapie
    titre: Thérapie
    couleur: menthe
    disposition: liste
    reglages: true                      # la roue dentée et l'avis d'accès vivent ici
    vide: "Rien à faire pour l'instant."
    kokoro:
      animation: liste-du-jour
      groupe: aujourdhui                # ce que « tout fait » compte
    groupes:
      - { id: aujourdhui, libelle: "Aujourd'hui",           couleur: peche }
      - { id: au_besoin,  libelle: "Quand j'en ai besoin",  couleur: beurre }
      - { id: sans_date,  libelle: "Sans date",             couleur: azur }
    cartes: [ … ]

  - id: documentation
    titre: Documentation
    couleur: lavande
    disposition: liste
    kokoro: { animation: lecture }
    groupes: [ … ]
    cartes: [ … ]

  - id: bilan
    titre: Bilan
    couleur: beurre
    disposition: liste
    groupement: mois                    # groupes déduits de « date », mois décroissants
    kokoro: { animation: floss }
    cartes: [ … ]

  - id: crise
    titre: Crise
    couleur: azur
    disposition: boutons                # ne défile jamais ; Kokoro accoudé au 1ᵉʳ bouton
    cartes:
      - { id: mot-code, titre: "Mot-code à {aidant}", type: sms }
      - { id: tension-appliquee, titre: "La tension appliquée", type: tension-appliquee, arret: [ … ] }
      - { id: phrase-soignant, titre: "La phrase pour le soignant", type: panneau, dialog: false, … }
```

### Ce qui change dans une carte

| Champ | Devient |
|---|---|
| `rubrique` | **Disparaît.** Une carte vit dans la page qui la déclare — ⭐ **la règle bancale « un `pdf` s'affiche toujours sur Documentation » disparaît avec elle** |
| `quand` | **Devient `groupe`**, un identifiant déclaré par la page. Obligatoire si la page déclare des `groupes` |
| `date` | Inchangée, mais **exigée par `groupement: mois`** au lieu de par `rubrique: bilan` |
| `type` | `panneau` · `pdf` · **`sms`** *(APP5)* · **`tension-appliquee`** *(Q1)* |
| `dialog` | **Nouveau, sur `panneau` seul** — la bulle et Kokoro sous le panneau *(§2)*. Défaut `true` |
| `id` | 🔴 **Inchangé, et toujours unique sur tout le fichier** — c'est la clé des réponses et de `psy:sync` |

### La carte `sms` — APP5

```yaml
{ id: mot-code, titre: "Mot-code à {aidant}", type: sms }
```

🔴 **Ni numéro ni texte dans le YAML.** Le destinataire et le message sont **des réglages de l'appareil** *(`Reglages.kt`)*, saisis par le patient — un numéro dans le dépôt ou dans Drive serait une donnée personnelle publiée. **La carte est refusée à la publication si `app.mot_code.actif` est faux.**

### Les animations disponibles — jeu fermé

`liste-du-jour` *(pensif avant 18 h · montre la liste · repos chaleureux quand tout est fait)* · `lecture` · `floss` · `pensif` · `repos`. **Une page `boutons` n'en choisit pas** : c'est toujours l'accoudé, deux passes, sans souffle, sans sommeil.

---

## 6. Le chantier côté Kotlin, fichier par fichier

| Fichier | Ce qui s'y passe |
|---|---|
| `programme/Programme.kt` | `Rubrique` et `Quand` supprimés. Nouveaux : `Config`, `Page`, `Groupe`, `Disposition`, `Groupement`, `Animation`, `Personnes`, `Carte.Sms`, `Carte.TensionAppliquee`, `dialog` sur `Carte.Panneau`. `cartesDe()` · `documents()` · `bilans()` remplacés par `page.cartes`. **Résolution des jetons `{patient}` · `{aidant}` · `{psychiatre}`** à l'affichage |
| `programme/Json.kt` | **Inchangé** |
| `monde/Ecran.kt` | `enum Ecran` → **index de page**. `ecranEn()` devient `pageEn(position, pages)`. ⚠️ **`positionsAutour()` doit cesser de supposer ≥ 3 pages** : sous 3, l'anneau ne boucle pas et la `key()` de composition passe du rang de page au rang absolu, sinon deux voisins portent la même clé |
| `monde/Cartes.kt` | `videsDe()` → `page.cartes.isEmpty()`. `sectionsDuProgramme()` → `page.groupes`. `Contexte` gagne `Tension` **portée par une carte**, perd `Phrase` *(qui devient un panneau)* |
| `monde/Bords.kt` | `ContenuTherapie` · `Documentation` · `Bilan` · `CriseDuMonde` fusionnent en **`ContenuPage(page, …)`** — deux dispositions, un rendu. La roue dentée et `AvisAcces` suivent `page.reglages` |
| `monde/Habitant.kt` | `Perchoir` : 6 valeurs → **2 rôles** *(`PERCHE`, `PREMIER_BOUTON`)*, **clés par page** *(deux pages voisines posent chacune la sienne)*. `place()` lit `page.kokoro`. 🔴 **Suppressions : `PLAFOND`, `horsCadre()`, `auRepos()`, `MONTEE_DU_PERCHOIR`, l'écrêtage vertical** — la perche ne défile plus |
| `monde/MondeKokoro.kt` | L'anneau parcourt `config.pages`. `SceneDeCrise` → **`ScenePleinEcran(page)`**, générique |
| `monde/MondeActivity.kt` | Lit `Config` au lieu de `Programme` ; **tient le cache** selon les quatre règles du §3 |
| `crise/CriseActivity.kt` | Devient **`PageActivity`** : reçoit un id de page, la rend figée. `showWhenLocked` conservé |
| `crise/AccesCrise.kt` | La notification est **conditionnée par `app.notification.active`** et vise `app.notification.page` |
| `crise/Elements.kt` | `PorteDeCrise` supprimé — les boutons viennent des cartes de la page |
| `crise/MotCode.kt`, `tension/*`, `ContenuTension.kt` | **Inchangés**, sauf les critères d'arrêt qui viennent de la carte *(§4)*. Branchés par `Carte.Sms` et `Carte.TensionAppliquee` |
| `reglages/Reglages.kt` | 🔴 **`contactNom` → `nomAidant`**, plus deux champs neufs : `nomPatient`, `nomPsychiatre`. Défauts lus dans `app.personnes` |
| `reglages/PanneauReglages.kt` | **Nouvelle section *Les personnes*** — trois champs, toujours visible. La section *Destinataire du mot-code* ne garde que **numéro et message**, et disparaît quand `app.mot_code.actif` est faux |
| `monde/PanneauCarte.kt` · `ui/Pieces.kt` | `LocalPanneauPorte` alimenté par **`carte.dialog`**, forcé à `false` sur une page `boutons` |
| `res/values/strings.xml` | Titres de page, libellés de groupe, textes de liste vide, phrase pour le soignant et critères d'arrêt **sortent** vers le YAML — et passent donc désormais par `estPermis()` |

### 🔴 Le cache — la condition sans laquelle rien de tout ceci ne passe

Aujourd'hui **l'écran de crise tient sans Drive parce qu'il est écrit dans l'app**. Dès qu'il devient du contenu, il dépend d'un fichier distant — **inacceptable pour une surface ouverte par-dessus l'écran verrouillé.**

**`PageActivity` et le monde lisent le cache d'abord, Drive ensuite** *(mécanisme : §3)*. **Une page de crise déjà vue une fois s'ouvre toujours** — hors ligne, dossier retiré, Drive muet. *(Test dédié, §9.)*

---

## 7. Le chantier côté scripts

| Fichier | Ce qui s'y passe |
|---|---|
| `psy/scripts/schemas/programme.ts` → `kokoro.ts` | Schéma restructuré : `app` + `pages[]`, et **le validateur complet** *(ci-dessous)* |
| `psy/scripts/psy-publish.ts` | Lit le YAML, **le valide entièrement**, **écrit `kokoro.json`** dans le transit. `cartesQuiFontAgir()` parcourt les pages. **`--seance`, la supervision bloquante et les 7 interdits : inchangés**, et les interdits s'appliquent désormais aussi aux titres de page, libellés de groupe et critères d'arrêt |
| `psy/scripts/psy-sync.ts` | **Inchangé** — il travaille sur des ids de carte |
| `package.json` | Dépendance `yaml` — **la seule ajoutée par tout le chantier**, et côté PC uniquement |
| `companion/PROGRAMME.md` | Devient **`companion/KOKORO.md`** *(normatif)* : le contrat décrit `app`, `pages`, `groupes`, `disposition`, les 4 types de carte |

### 🔴 Le validateur — `psy:publish` refuse tout ce qui n'est pas dans cette liste

⭐ **C'est ici que se joue le chantier.** Chaque règle qui sortait du code Kotlin *(un `enum` qui ne connaissait que quatre écrans, une `Rubrique` fermée)* **était une garantie de compilation** ; elle redevient une garantie **seulement si le validateur la porte**. **Une règle non écrite ici est une règle perdue.**

| Portée | Règles |
|---|---|
| **Fichier** | `version` entier croissant · `publie_le` en `AAAA-MM-JJ` · `supervision` présente et visant **cette** version · **au moins une page** |
| **Système** | `notification.page` désigne une page existante · ⚠️ **avertissement si elle n'est pas voisine de l'entrée dans l'anneau** *(la crise doit rester à un geste)* · `personnes` : tout jeton cité dans un texte a **un défaut non vide** |
| **Page** | `id` en kebab-case, **unique** · `couleur` dans les cinq · `disposition` ∈ `liste`·`boutons` · `groupes` **ou** `groupement`, jamais les deux · `reglages: true` sur **une seule page** · `kokoro.animation` dans le jeu fermé · `kokoro.groupe` déclaré par la page · `vide` requis sur une page `liste` |
| **Groupe** | `id` unique **dans sa page** · `libelle` non vide · `couleur` dans les cinq |
| **Carte** | 🔴 **`id` unique sur tout le fichier** *(clé des réponses)* · `type` dans les quatre · `groupe` déclaré par sa page si la page en déclare · `date` **exigée par `groupement: mois`, interdite ailleurs** · `sortie_libre: true` sur tout `panneau` · les 6 types d'étape et leurs règles **inchangés** |
| **Page `boutons`** | 🔴 **`dialog: true` refusé** · pas de `groupes` · ⚠️ **avertissement au-delà de 4 cartes** *(l'écran ne défile pas — au-delà, une carte disparaît en silence)* |
| **`sms`** | Refusée si `mot_code.actif` est faux · **aucun numéro, aucun message dans le YAML** |
| **`tension-appliquee`** | Au plus **une par fichier** · `arret` d'au moins deux lignes, la dernière portant le critère « je ne sais pas quoi faire » — **la règle qui existe déjà pour les cartes de l'aidant** |
| **Textes** | 🔴 **Les 7 familles d'interdits**, désormais sur *tous* les textes rendus : titres de page, libellés de groupe, `vide`, `arret`, titres de carte, étapes |

**Migration** : `programme.json` v16 → `kokoro.yaml` v17, **4 pages, 0 carte perdue**. ⭐ **Contrôle mécanique** : le `kokoro.json` généré doit porter **exactement les mêmes 69 ids de carte**, avec les mêmes étapes, que le `programme.json` actuel. **C'est ce diff qui valide la migration, pas une relecture.**

---

## 8. 🔴 La grille de non-régression — ce qui doit rester vrai à l'écran

| Ce qui existe aujourd'hui | Où ça atterrit |
|---|---|
| 4 écrans, anneau sans bout, crise à un geste dans les deux sens | 4 pages déclarées dans cet ordre + avertissement de publication |
| Titres, couleurs, pancartes de section | `titre`, `couleur`, `groupes[].libelle` |
| Un `pdf` s'ouvre dans le lecteur du téléphone, picto « dehors » | Inchangé |
| Bilan groupé par mois décroissants | `groupement: mois` |
| Bibliothèque groupée crise / thérapie / dispositif | 3 `groupes` déclarés |
| 3 boutons de crise, 88 dp, azur, sans défilement | Page `boutons` : hauteur, ton et absence de défilement **portés par la disposition**, pas par un réglage de plus |
| Kokoro accoudé au bouton *Mot-code*, deux passes, sans souffle, sans sommeil | **Premier bouton d'une page `boutons`** — même code, même pose |
| Kokoro endormi + Zzz sur une liste vide | Conservé *(APP4.b.iii)* |
| Pensif avant 18 h · montre la liste · repos chaleureux quand tout est fait | `animation: liste-du-jour` + `groupe: aujourdhui` |
| Transit 700 ms, arc 26 dp, retard 200 ms, poses de vol | Inchangé |
| Notification permanente muette, ouvre la scène figée par-dessus le verrouillage | `app.notification` + `PageActivity` |
| SMS direct, repli vers l'app Messages, accusé d'envoi, double-envoi impossible | `Carte.Sms` → `MotCode.kt` inchangé |
| Tension appliquée : séquence, cycles, assis 5 min, critères d'arrêt | `type: tension-appliquee` — code inchangé, `arret:` porté par la carte |
| La phrase pour le soignant, montrable en grand | `panneau` + `info montrable` |
| Panneau ouvert dans le monde : queue de bulle + Kokoro dessous | `dialog: true`, **le défaut** |
| Panneau de crise sans bulle ni personnage *(borne testée)* | `dialog` forcé à `false` par la disposition `boutons`, et refusé à la publication |
| Réglages : contact, mot-code, plage nuit, parallaxe, inclinaison, dossier | Inchangés ; **le nom du contact devient le nom de l'aidant**, numéro et message masqués si `mot_code.actif: false` |
| Avis « accès perdu » quand les notifications sont refusées | Suit `page.reglages`, **et disparaît si `notification.active: false`** |
| `Faites`, entraînements, reprises, `issue`, écriture des réponses | Inchangés — tout est clé par id de carte |
| Voile de démarrage, relecture 5 s, plafond d'attente 6 s | Inchangés — ⭐ **le plafond ne se paie plus qu'à la première ouverture** *(§3)* |
| 7 familles d'interdits, zéro réseau, zéro son, zéro streak | Inchangés, **et étendus aux nouveaux champs de texte** |

---

## 9. Les tests — ce qu'il faut ajouter, et ce qu'il ne faut pas casser

- `EcranTest` → **`PagesTest`** : anneau modulo *N*, **N = 1 et N = 2** *(cas que le code actuel ne sait pas traiter)*, voisins tous distincts.
- `InvariantsSourcesTest` — 🔴 **le contrôle « aucun personnage dessiné par une surface de crise » cible le répertoire `crise/`.** Il doit être **repointé sur les surfaces plein écran**, jamais supprimé.
- `InvariantsTextesTest` — 🔴 **il scanne `strings.xml`.** Les textes qui partent au YAML **doivent être couverts par les interdits de `psy-publish.ts` avant de quitter l'app**, sinon la garde se lève sans que rien ne le signale.
- **Nouveau — le hors-ligne** : sans dossier Drive, la page de la notification s'ouvre depuis le cache et rend ses boutons.
- **Nouveau — le cache** *(§3)* : deux lectures identiques n'écrivent qu'une fois · une lecture invalide n'écrit pas · une écriture interrompue ne laisse pas de demi-fichier.
- **Nouveau — la migration** : le `kokoro.json` généré porte le même jeu d'ids et d'étapes que `programme.json` v16.
- **Nouveau — le mot-code coupé** : `mot_code.actif: false` ⇒ carte `sms` refusée à la publication, champs masqués aux réglages, aucun envoi possible.
- **Nouveau — la perche** : sur une page `liste`, la position de Kokoro **ne dépend pas du défilement**.
- **Nouveau — `dialog`** : 🔴 une page `boutons` n'ouvre **jamais** un panneau portant bulle ou personnage, quoi que déclare le YAML — **côté validateur *et* côté app**, les deux.
- **Nouveau — le validateur** : un fichier fautif par règle du §7, chacun refusé **avec sa raison**. ⭐ **C'est le test le plus rentable du chantier** — c'est lui qui remplace ce que le compilateur Kotlin garantissait.

---

## 10. Les phases — dans cet ordre, chacune livrable seule

| # | Phase | Sortie |
|---|---|---|
| 1 | **Contrat et validateur** — `kokoro.yaml`, Zod, **toutes les règles du §7**, migration v16, `KOKORO.md` | Le YAML existe et valide ; rien n'est publié |
| 2 | **Publication** — `psy:publish` émet `kokoro.json` | Diff d'ids vert contre v16 |
| 3 | **Modèle Kotlin** — `Config` · `Page` · `Groupe`, 4 types de carte, jetons, **cache** | L'app lit la nouvelle forme, écran inchangé |
| 4 | **Les pages** — anneau sur *N*, `ContenuPage`, groupes, couleurs, `dialog` | Les 4 écrans viennent du YAML |
| 5 | **La perche** *(APP4.b)* — Kokoro fixe en haut à droite, purge de `Habitant.kt` | Le seul changement visible du chantier |
| 6 | **Page `boutons`** *(APP4.a/c)* — cartes `sms` et `tension-appliquee`, perche sur le 1ᵉʳ bouton, phrase soignant en `panneau` | L'écran de crise vient du YAML |
| 7 | **Système** *(APP2)* — notification paramétrable, `PageActivity`, mot-code coupable, **section *Les personnes*** | Un projet sans notification et sans SMS tourne |
| 8 | **Documentation** — `companion/README.md` §5-6, `THESAURUS.md` *(page, groupe, disposition)*, `CLAUDE.md` | Le vocabulaire suit le code |

🔴 **Rien ne se publie vers Kokoro avant la phase 8, et la supervision porte sur la version migrée** — pas sur la v16.

⚠️ **Une seule chose change sous les yeux de Xavier : la perche** *(phase 5)*. **Elle s'annonce avant d'être livrée** — la prévisibilité est une fonctionnalité.
