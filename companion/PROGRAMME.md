# `PROGRAMME.md` — le format du programme et de la bibliothèque

> 🔴 **NORMATIF.** Contrat partagé entre **Claude Psy**, qui l'écrit, et **Kokoro**, qui l'affiche.
>
> **Règle unique : Kokoro n'invente rien et ne décide rien.** Il affiche ce qu'on lui donne, et renvoie ce que Xavier a fait.

---

## 1. Le circuit

```
Claude Psy ──écrit── companion/inputs/programme.json + bibliotheque/ + bilans/
                            │
                    Claude Superviseur ── verdict: publiable   (bloquant)
                            │
                     npm run psy:publish
                            ▼
        Drive/programme.json + Drive/bibliotheque/ + Drive/bilans/
                            ▼
                         Kokoro
                            │
                       Drive/reponses/
                            │
                       npm run psy:sync
                            ▼
     companion/outputs/reponses/  ─┬─ versé tel quel
                                   └─ la carte « check-in » reconstruit companion/outputs/journal/
                            ▼
     psy/outputs/dossier/ + companion/outputs/  ──lit── Claude Psy · Claude Superviseur
```

**Le dépôt reste la source de vérité. Drive n'est qu'un tuyau.**

⭐ **La documentation et les bilans se publient à tout moment** — une fiche est à portée dès qu'elle est écrite et supervisée. **Xavier n'attend pas la séance suivante pour comprendre ce qui lui arrive.**
🔴 **Une carte qui fait agir se publie à la clôture d'une séance** — c'est-à-dire toute carte portant au moins une étape qui renvoie une réponse : `question`, `note`, `minuteur`, `confirmation`. **Ce qui fait agir se décide avec lui.**
**Dans les deux cas : supervision bloquante, et annonce à Xavier au moment de la publication.** La prévisibilité tient à l'annonce, pas au calendrier.

---

## 2. Le fichier

```json
{
  "version": 12,
  "publie_le": "2026-08-20",
  "supervision": "2026-08-20-programme-v12",
  "cartes": [ … ]
}
```

| Champ | Règle |
|---|---|
| `version` | Entier, **s'incrémente à chaque publication**. Il sert au dépôt : c'est lui que vise la supervision. 🔴 **Kokoro ne l'affiche pas et ne signale rien de nouveau** *(tranché par Xavier le 20/08/2026)* — **ce qui change s'annonce à Xavier dans la conversation, au moment de la publication**, jamais par l'application |
| `publie_le` | `AAAA-MM-JJ` |
| `supervision` | 🔴 **Obligatoire.** Nom du fichier de `superviseur/outputs/` (sans extension) qui vise **cette version**. Sans lui, `npm run psy:publish` refuse *(voir [`../superviseur/README.md`](../superviseur/README.md))* |

---

## 3. Une carte

**Une carte est une ligne de l'écran d'accueil. Il n'y en a que deux sortes**, et rien d'autre n'existe.

| Champ | Valeurs |
|---|---|
| `id` | identifiant stable, `kebab-case`. ⚠️ **Ne change jamais** — c'est lui qui relie une réponse à sa carte, et **c'est par lui que Claude Psy sait comment la lire** |
| `titre` | ce qui s'affiche dans la liste |
| `type` | `panneau` · `pdf` |
| `rubrique` | `crise` · `therapie` · `documentation` — **c'est le groupement principal de l'écran d'accueil**. 🔴 **`bilan` est réservée aux cartes du dossier Bilan** *(§3.2)* |
| `quand` | `aujourdhui` · `au_besoin` · `sans_date` — **absent sur un bilan, et sur lui seul** |
| `duree_minutes` | entier, ou absent si la durée n'est pas connue d'avance |

### 3.1 `panneau` — la carte ouvre son panneau et le déroule

```json
{ "id": "ppc-palier-1", "titre": "Masque tenu à la main", "type": "panneau", "rubrique": "therapie",
  "quand": "aujourdhui", "duree_minutes": 5, "sortie_libre": true,
  "etapes": [
    { "type": "info", "texte": "Masque contre le visage, sans sangles, machine éteinte." },
    { "type": "minuteur", "secondes": 300 } ] }
```

| Champ | Règle |
|---|---|
| `sortie_libre` | 🔴 **Obligatoire, toujours `true`.** Le champ existe pour que ce soit **écrit**, pas pour être mis à `false`. Kokoro affiche « je peux arrêter avant la fin, sans avoir à le justifier » |
| `etapes` | Au moins une. **Une étape par écran, dans l'ordre, sans retour en arrière** *(§4)* |
| `porteur` | `patient` *(par défaut)* · `aidant` — **qui tient le téléphone** *(§5)* |

🔴 **La carte tombe entière ou pas du tout.** Une étape hors contrat emporte la carte : `psy:publish` refuse la publication entière, Kokoro écarte cette carte-là et affiche le reste. **Un item perdu produit un score faux, donc faussement rassurant :** c'est le pire résultat possible ici.

### 3.2 `pdf` — la carte confie un document au lecteur du téléphone

```json
{ "id": "panique-13-symptomes", "titre": "Les 13 symptômes", "type": "pdf",
  "rubrique": "documentation", "quand": "au_besoin", "document": "panique-13-symptomes" }
```

`document` est l'identifiant nu d'un fichier ; Kokoro résout `bibliotheque/<document>.pdf` et **le confie au lecteur PDF du téléphone**. Le **picto « dehors »** de la carte annonce la sortie de l'app. **Elle ne renvoie rien.**

🔴 **Une carte `pdf` s'affiche sur l'écran *Documentation*, quelle que soit sa `rubrique`** *(18/08/2026)* : c'est ce qui garde à l'écran de crise ses **trois boutons sans défilement**. **La bibliothèque entière y vit, groupée par `rubrique`** — *crise* · *thérapie* · *le dispositif* *(20/08/2026)*. ⭐ **La `rubrique` ne décide donc pas de l'écran où la carte vit, mais de la section où elle se range** ; `quand` n'ordonne rien sur cet écran.

**Le bilan est la seule exception, et il se reconnaît à sa rubrique :**

```json
{ "id": "vviq-2026-08", "titre": "VVIQ — imagerie mentale", "type": "pdf",
  "rubrique": "bilan", "date": "2026-08-09", "document": "vviq-2026-08" }
```

| Champ | Règle |
|---|---|
| `rubrique` | 🔴 **`bilan`** — et aucune autre carte ne la porte. L'écran *Bilan* ne montre que des bilans ; **une carte rangée là sans place à l'écran disparaîtrait en silence** |
| `date` | 🔴 **Obligatoire ici, interdite ailleurs.** `AAAA-MM-JJ`, celle du bilan, jamais celle de la publication. L'écran groupe **par mois décroissant** |
| `quand` | 🔴 **Absent** — ⭐ **la date appartient au document, pas à l'assiduité de Xavier** |
| `document` | Fichier de **`companion/inputs/bilans/<id>.md`**, converti en `bilans/<id>.pdf` par `psy:publish` |

🔴 **Un bilan ne passe pas par la bibliothèque** — canal distinct, contrôles distincts *(§8)*.

---

## 4. Les étapes d'un panneau

**Six types, et rien d'autre.** Une étape par écran ; l'écran suivant s'ouvre quand celui-ci est rendu.

### `info` — un texte à lire, ou à montrer

```json
{ "type": "info", "texte": "Masque contre le visage, sans sangles.", "montrable": false }
```

`montrable: true` affiche le texte en grand, lisible par quelqu'un d'autre — la phrase pour le soignant, la fiche pour l'aidant. **Une `info` ne renvoie rien.**

### `question` — une réponse fermée

Deux formes, **exclusives l'une de l'autre**. 🔴 **Une question est toujours fermée — aucune saisie de texte, jamais.**

```json
{ "type": "question", "id": "q1", "enonce": "…", "choix": [
    { "valeur": 0, "libelle": "Jamais" },
    { "valeur": 3, "libelle": "Presque tous les jours" } ] }

{ "type": "question", "id": "sommeil-heures", "enonce": "Combien d'heures de sommeil ?",
  "compteur": { "depart": 7, "pas": 0.5, "grand_pas": 1, "minimum": 0, "unite": "heures" } }
```

| Champ | Règle |
|---|---|
| `id` | `kebab-case`, **unique dans la carte** — c'est lui qui relie un item à sa réponse |
| `enonce` | Le texte de l'item, **recopié du corpus** |
| `precision` | Facultatif. Une phrase qui dit **où lire le chiffre**, jamais quoi ressentir |
| `choix` | 🔴 **Au moins deux**, chacun avec `valeur` *(un nombre)* et `libelle` |
| `compteur` | `depart` ≥ `minimum`, `pas` > 0, `grand_pas` ≥ `pas`, `unite` ∈ `brute` · `minutes` · `heures` · `kilos` |
| `reprise` | `true` fait repartir le compteur de **la dernière valeur donnée à cette question**. Réservé aux compteurs — ⭐ **Xavier ne redonne pas un chiffre qui n'a pas bougé, et il n'a rien à retrouver de mémoire** |

**« Passer » écrit `null`** — **qui n'est pas `0`**. Un item **jamais atteint** est **absent**, ce qui ne veut pas dire la même chose.

**Trois règles de publication des échelles, non négociables :**

1. 🔴 **Le PHQ-9 ne se publie jamais.** C'est le seul instrument porteur d'un déclencheur d'escalade *(son item 9 interroge l'idéation suicidaire)*, et Kokoro s'interdit tout numéro d'urgence par construction. Il se passe **en conversation**, avec `psy-bilan`.
2. ⛔ **Les items se recopient depuis `psy/docs/corpus/echelles/`, jamais de mémoire.**
3. **La cotation n'est pas dans Kokoro.** L'app renvoie les réponses item par item ; **le score se calcule en séance**. ⭐ **Kokoro n'affiche jamais un score, un seuil ni une interprétation** — ce serait une progression à l'écran, et un score mal lu est pire qu'un score absent.

### `note` — la seule saisie libre du dispositif

```json
{ "type": "note", "id": "notes", "enonce": "Quelque chose à ajouter ?",
  "precision": "Facultatif. Le champ peut rester vide." }
```

⭐ **Elle peut rester vide, et rien ne le relève.** Aucun autre endroit du dispositif ne demande d'écrire.

### `minuteur` — un temps tenu par l'appareil

```json
{ "type": "minuteur", "secondes": 300, "consigne": "Reste assis, sans te relever." }
```

`consigne` est facultative — quand la consigne tient sur l'écran précédent, une `info` la porte mieux.

🔴 **Le minuteur vit dans le panneau, et Kokoro le dit avant de commencer** — quitter l'app l'arrête sans rien écrire. **Aucun son, aucune vibration ne marque le terme** : à zéro, l'écran passe seul à l'étape suivante, et il ne la commente pas.

### `checklist` — des cases à cocher qui ouvrent la suite

```json
{ "type": "checklist", "enonce": "À vérifier avant d'entrer dans le déroulé.",
  "lignes": [ "Pièce calme, lumière baissée, porte fermée." ] }
```

**Tant que tout n'est pas coché, l'étape suivante ne s'ouvre pas.** ⭐ **Cocher est la seule preuve que ça a été lu.** **Une checklist ne renvoie rien.**

### `confirmation` — une chose faite dans le monde réel

```json
{ "type": "confirmation", "libelle": "C'est fait" }
```

Renvoie `fait`. ⭐ **Pas encore fait n'est pas une donnée** : rien ne s'affiche, rien ne se compte, et fermer le panneau n'écrit rien.

🔴 **Une carte rendue reste à l'écran, en retrait, jusqu'à ce que le psy la retire du programme** *(18/08/2026)*. **Kokoro montre l'état, il ne décide pas de la sortie** — et il n'affiche jamais combien il en reste.

---

## 5. `porteur: "aidant"` — la carte passe dans les mains de quelqu'un d'autre

```json
{ "id": "stab-ancrage-1", "titre": "Ancrage corporel — à deux", "type": "panneau",
  "rubrique": "therapie", "quand": "sans_date", "duree_minutes": 22,
  "porteur": "aidant", "sortie_libre": true,
  "signal_arret": "Xavier fait « non » de la main. On s'arrête, sans rien demander.",
  "arret": [
    "Il ne répond plus aux consignes → on s'arrête, c'est un shutdown, pas un refus.",
    "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser."
  ],
  "etapes": [
    { "type": "checklist", "enonce": "À vérifier avant d'entrer dans le déroulé.",
      "lignes": [ "Pièce calme, lumière baissée, porte fermée.",
                  "Le téléphone reste dans tes mains du début à la fin." ] },
    { "type": "minuteur", "pour": "aidant",  "consigne": "Assieds-toi en face de lui, à un mètre.", "secondes": 30 },
    { "type": "minuteur", "pour": "patient", "consigne": "Lis à voix haute, mot pour mot : « Pose les deux pieds à plat. »", "secondes": 60 } ] }
```

| Champ | Règle |
|---|---|
| `signal_arret` | 🔴 **Obligatoire et non vide.** ⭐ **C'est le champ le plus important de la carte** : Xavier doit pouvoir arrêter **sans parler**, parce que c'est exactement ce qui tombe en premier. Le geste se convient **à froid**, jamais pendant. 🔴 **C'est le « non » de la main** — il se recopie tel quel, **il ne se réinvente pas d'une carte à l'autre** |
| `arret` | 🔴 **Obligatoire, au moins deux entrées.** ⭐ **La dernière est toujours « tu ne sais pas quoi faire → on s'arrête »** — l'aidant n'improvise jamais. 🔴 **Kokoro les porte, signal en tête, sur la dernière case de la `checklist`** : elles se lisent **avant** d'entrer, et le déroulé ne s'ouvre pas tant qu'elle n'est pas cochée |
| première étape | 🔴 **Toujours une `checklist`** — c'est elle qui porte les critères d'arrêt |
| `pour` | 🔴 **Obligatoire sur chaque `minuteur`** *(`aidant` · `patient`)*, **et interdit ailleurs**. **La consigne dit elle-même à qui elle s'adresse** — « Lis à voix haute, mot pour mot : … » quand elle est destinée à Xavier. **Kokoro n'affiche aucune étiquette autour d'elle** : `pour` est contrôlé au dépôt comme à la lecture, mais il ne se voit pas. 🔴 **Aucune consigne ne demande une réponse gestuelle de la main** — le « non » de la main est réservé à l'arrêt |

> 🔴 **Ce qu'une carte tenue par l'aidant ne porte jamais** *(contrôle **C10**)* : un diagnostic, un score, une hypothèse, un compte rendu — **rien qui apprenne à l'aidant quelque chose sur Xavier qu'il n'a pas décidé de partager**. Et aucune consigne qui **lui demande de juger** : « estime si ça va », « décide s'il faut continuer », « rassure-le ». **Une consigne qui demande un jugement clinique la met en faute quoi qu'elle fasse.**

> ⭐ **L'entraînement est offert d'office, et il compte autant que la séance.** C'est **les mêmes étapes, jouées à blanc**, sans le matériel réel — 🔴 **et sans minuteur** *(19/08/2026)* : **l'aidant passe d'une consigne à l'autre à la main**, et le temps affiché est celui que la consigne **durera en séance**. ⭐ **Deux raisons, et la seconde est la vraie** : un déroulé qui s'enchaîne tout seul ne laisse pas le temps de se préparer, **et voir la durée avant de la vivre est précisément ce qu'un entraînement sert à donner.** Il renvoie `issue: "entrainement"` — **ce n'est pas une donnée clinique et rien ne s'en déduit**. **La première fois que ça compte ne doit pas être la première fois que ça se fait.**

> ⚠️ **Limite connue du format : la suite d'étapes est linéaire, elle ne sait pas exprimer une répétition en séries.** Un déroulé de stimulation bilatérale est fait de séries — *n* allers-retours, une pause, on recommence. 🔴 **Déplier trente étapes identiques serait un contournement, pas une solution** : le format porterait une cadence sans jamais la nommer, et le Superviseur n'aurait rien à contrôler. **L'extension se décide en séance, sous supervision — pas à l'implémentation.**

---

## 6. Les écrans

Quatre écrans, traversés d'un glissement horizontal : **Thérapie · Documentation · Bilan · Crise**.

- **Thérapie** groupe les cartes `panneau` de rubrique `therapie` par **`quand`** : *aujourd'hui* · *quand j'en ai besoin* · *sans date*.
- **Documentation** porte **toutes les cartes `pdf` hors bilans**, groupées par **rubrique**.
- **Bilan** porte les cartes `pdf` de rubrique `bilan`, groupées par **mois décroissant**.
- 🔴 **Crise porte trois boutons bâtis dans l'app** — mot-code, tension appliquée, phrase pour le soignant. **Ils ne viennent pas du programme et ne peuvent pas en venir** : cet écran doit tenir quand Drive est vide, illisible ou pas encore synchronisé. **Le programme ne les touche jamais.**

**Aucun score, aucune progression, aucun historique, aucun palier atteint.**

---

## 7. Ce que Kokoro renvoie

Un fichier par carte rendue, dans `reponses/` : `AAAA-MM-JJ-HHMM-<id>.json`

```json
{ "carte": "ppc-palier-1", "horodatage": "2026-08-13T18:04:00+02:00",
  "issue": "termine", "reponses": null, "source": "android" }
```

**`reponses` est une liste ordonnée, un objet par étape rendue** — `valeur` pour une `question`, `texte` pour une `note` :

```json
{ "carte": "check-in", "horodatage": "2026-08-19T21:12:00+02:00", "issue": "termine",
  "reponses": [ { "question": "shutdowns", "valeur": 2 },
                { "question": "sommeil-heures", "valeur": 7.5 },
                { "question": "ppc-minutes", "valeur": null },
                { "question": "notes", "texte": null } ],
  "source": "android" }
```

⭐ **La cotation n'est pas là-dedans.** Kokoro renvoie les items ; le score se calcule en séance, avec `psy-bilan`.

`issue` : `termine` · `arrete_avant_la_fin` · `fait` · `entrainement`.
⭐ **`arrete_avant_la_fin` n'est pas un échec et ne se commente nulle part.**
⭐ **`entrainement` n'est pas une donnée clinique** — il dit seulement que le déroulé a été répété à blanc.
**Une carte dont aucune étape ne rend — que de l'`info`, que de la `checklist` — n'écrit rien du tout.**

🔴 **Ce que Kokoro a écrit, il s'en souvient localement** — l'état d'une carte ne dépend jamais d'un aller-retour par Drive. Sans ça, une carte faite réapparaît *à faire* le temps que le fichier remonte, et Xavier la refait. **Drive dit ce qui est arrivé au dépôt ; il ne dit pas ce que Xavier a fait.**

### Le journal — reconstruit au dépôt, pas écrit par Kokoro

🔴 **La carte d'`id` `check-in` est le journal quotidien.** Kokoro ne le sait pas et n'a pas à le savoir : il rend une réponse comme pour n'importe quelle carte. **C'est `npm run psy:sync` qui, sur cet id, reconstruit `companion/outputs/journal/AAAA-MM-JJ.json`** au format de [`../psy/DOSSIER.md`](../psy/DOSSIER.md) — les `id` de question en `kebab-case` deviennent les clés en `snake_case`, les sept champs du noyau d'un côté, le reste en campagne, la `note` d'`id` `notes` dans `notes`.

⭐ **C'est là tout le rôle de l'`id` : il fait le lien entre Kokoro et Claude Psy sans que Kokoro n'interprète quoi que ce soit.** Un jour déjà au journal n'est jamais réécrit.

---

## 8. La bibliothèque

**`companion/inputs/bibliotheque/<id>.md`** — un fichier Markdown par document. ⭐ **Le Markdown est la version qui se relit et qui passe la supervision ; il ne part pas.** `npm run psy:publish` le convertit en **`bibliotheque/<id>.pdf`** et ne publie que le PDF — et **retire du transit tout document que la bibliothèque n'appelle plus**.

> 🔴 **La règle qui vaut plus que toutes les autres ici : un document de la bibliothèque est *écrit pour Xavier*, il n'est pas *copié depuis* `psy/docs/protocoles/`.**
>
> Un protocole clinique porte des diagnostics, des pronostics, des noms de praticiens, des hypothèses non tranchées et des réserves adressées à un professionnel. **Une fiche de bibliothèque porte ce qu'il y a à faire, et pourquoi.** C'est le contrôle **C9**.

**Ce qu'une fiche de bibliothèque ne contient jamais :** un diagnostic non encore dit à Xavier · un pronostic · un nom de praticien autre que ceux qu'il consulte · une hypothèse formulée comme un fait · une réserve destinée au Dr Isorni · **et tous les interdits du §9**.

> 🔴 **Le partage entre les deux surfaces — il découle de leurs durées de vie, pas d'un goût** *(18/08/2026)*.
>
> **Ce qui change au rythme des séances vit dans le programme** *(le palier en cours, l'étape du jour, ce qui reste à faire)* — il est republié à chaque clôture. **Ce qui ne change pas vit dans la bibliothèque** *(une échelle entière, une règle, des critères d'arrêt, une conduite de voyage)* — elle part en **PDF figé**, et `psy:publish` ne reconvertit que ce qui a changé.
>
> ⚠️ **Une fiche ne dit donc jamais où Xavier en est rendu.** Elle périmerait au premier passage de palier, **sans que rien ne force sa réécriture**, et c'est la version figée qu'il ouvrirait pour comprendre. **C'est le mode de défaillance C8 appliqué à la bibliothèque** — constaté sur `ppc-les-paliers` avant publication.

**Les fiches sont soumises aux mêmes vérifications que les cartes** : `npm run psy:publish` lit chaque fichier de la bibliothèque et applique les sept familles d'interdits. 🔴 **Kokoro les réapplique à la lecture, sur ce qu'il affiche lui-même** — le titre d'une carte et le texte de ses étapes ; **le corps d'un PDF, lui, n'est vérifié qu'au dépôt.**

### Les bilans — l'autre canal

**`companion/inputs/bilans/<id>.md`** — un fichier Markdown par bilan, converti en **`bilans/<id>.pdf`** par `psy:publish`, qui retire du transit tout bilan que le programme n'appelle plus. **Aucun bilan ne vit dans `bibliotheque/`, et aucune fiche ne vit dans `bilans/`.**

🔴 **Les sept familles d'interdits du §9 ne s'appliquent pas au corps d'un bilan** : un rapport clinique réel nomme des traitements, des diagnostics et des praticiens, et c'est précisément sa raison d'être. Elles restent appliquées à son `titre`, qui, lui, s'affiche dans Kokoro.

⭐ **Un bilan ne se réécrit pas pour Xavier : il est déjà à lui.** C'est ce qui le sépare d'une fiche, et ce qui remplace **C9** sur ce canal. Il se publie **à tout moment, hors séance**, sous supervision bloquante portant sur une seule question : **ce document ne contient rien que Xavier ne sache déjà.** 🔴 **Kokoro n'offre aucune fonction de partage** — il confie le PDF au lecteur du téléphone. ⭐ **Le partage est un acte de Xavier dans son lecteur, pas une fonction du dispositif.**

---

## 9. 🔴 Les interdits — vérifiés à la publication ET à la lecture

**Les tests de Kokoro lisent les textes de l'app ; ces textes-ci n'y sont pas.** Sans double vérification, tous les garde-fous du dispositif deviennent contournables par du contenu, **en silence**.

**Deux vérifications, deux réactions volontairement différentes :**

- **`npm run psy:publish` refuse la publication entière.** Sur le PC, on peut corriger — donc on corrige, on ne publie pas à moitié.
- **Kokoro écarte la seule carte fautive** et affiche le reste. Sur le téléphone, on ne peut pas corriger, et perdre tout le programme pour une ligne serait pire.

| # | Interdit | Pourquoi |
|---|---|---|
| 1 | « imagine », « visualise », « représente-toi », « lieu sûr » | Aphantasie mesurée — 18/80 |
| 2 | « note … sur 10 », « ton niveau de », « à combien tu te sens » | R6 — on cote des comportements, pas des ressentis |
| 3 | « jour 3 sur », « d'affilée », « série », « régularité », « % de l'objectif » | Zéro streak |
| 4 | Tout numéro d'appel d'urgence, **3114 compris** | Un écran n'est pas un déclencheur d'escalade |
| 5 | « as-tu besoin », « quand tu sens », « aux premiers signes » | Déclenchement sur repère externe, jamais sur un prodrome |
| 6 | Tout ce qui touche à une dose, une molécule, un traitement | Non-substitution — ça part au brief |
| 7 | « détends-toi », « respire lentement » sur une étape vasovagale | Délétère sur un vasovagal |

---

## 10. Ce que le programme ne fait jamais

1. **Notifier.** *(Seule exception : l'accès crise sur l'écran verrouillé — une porte, pas un rappel.)*
2. **Compter d'un jour à l'autre.** Aucun palier atteint, aucun historique, aucune progression à l'écran. **Les paliers se cotent en séance, à partir du journal.**
3. **Reprocher.** Une carte non faite disparaît de l'écran le lendemain **sans laisser de trace**.
4. **Publier une carte qui fait agir hors séance.** Seuls la documentation et les bilans le peuvent, et ils s'annoncent au moment où ils sont publiés.
5. **Toucher à l'écran de crise.** Ses trois portes sont bâties dans l'app et tiennent sans Drive.
