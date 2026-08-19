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
                    Drive/journal/ + Drive/reponses/
                            │
                       npm run psy:sync
                            ▼
     psy/outputs/dossier/ + companion/outputs/  ──lit── Claude Psy · Claude Superviseur
```

**Le dépôt reste la source de vérité. Drive n'est qu'un tuyau.**

⭐ **La documentation et les bilans se publient à tout moment** — une fiche est à portée dès qu'elle est écrite et supervisée. **Xavier n'attend pas la séance suivante pour comprendre ce qui lui arrive.**
🔴 **Le reste du programme se publie à la clôture d'une séance** — `ecran`, `exercice`, `questionnaire`, `demarche`, `seance-duo` : ce qui fait agir se décide avec lui.
**Dans les deux cas : supervision bloquante, et annonce à Xavier au moment de la publication.** La prévisibilité tient à l'annonce, pas au calendrier.

---

## 2. Le fichier

```json
{
  "version": 4,
  "publie_le": "2026-08-13",
  "supervision": "2026-08-13-programme-v4",
  "etapes": [ … ]
}
```

| Champ | Règle |
|---|---|
| `version` | Entier, **s'incrémente à chaque publication**. Kokoro compare avec la version qu'il a : s'il y a du nouveau, il affiche **une ligne discrète en haut** — **jamais une notification** |
| `publie_le` | `AAAA-MM-JJ` |
| `supervision` | 🔴 **Obligatoire.** Nom du fichier de `superviseur/outputs/` (sans extension) qui vise **cette version**. Sans lui, `npm run psy:publish` refuse *(voir [`../superviseur/README.md`](../superviseur/README.md))* |

---

## 3. Une étape

Champs communs, tous obligatoires sauf `duree_minutes` :

| Champ | Valeurs |
|---|---|
| `id` | identifiant stable, `kebab-case`. ⚠️ **Ne change jamais** — c'est lui qui relie une réponse à son étape |
| `titre` | ce qui s'affiche dans la liste |
| `type` | `ecran` · `exercice` · `questionnaire` · `demarche` · `fiche` · `seance-duo` · `bilan` |
| `rubrique` | `crise` · `therapie` · `bilan` · `documentation` — **c'est le groupement principal de l'écran d'accueil**. 🔴 **`bilan` est réservée au type `bilan`** |
| `quand` | `aujourdhui` · `au_besoin` · `sans_date` — **absent sur un `bilan`, et sur lui seul** |
| `duree_minutes` | entier, ou absent si la durée n'est pas connue d'avance |

### `ecran` — ouvre une fonction déjà construite dans Kokoro

```json
{ "id": "check-in", "titre": "Check-in du jour", "type": "ecran", "rubrique": "therapie",
  "quand": "aujourdhui", "duree_minutes": 2, "ecran": "check-in" }
```

Valeurs de `ecran` : `check-in` · `mot-code` · `tension-appliquee` · `phrase-soignant`.
**Kokoro refuse un nom d'écran qu'il ne connaît pas** plutôt que d'afficher une ligne morte.

### `exercice` — un déroulé guidé au minuteur

```json
{ "id": "ppc-p1", "titre": "Masque tenu à la main", "type": "exercice", "rubrique": "therapie",
  "quand": "aujourdhui", "duree_minutes": 5,
  "consigne": "Masque contre le visage, sans sangles, machine éteinte, pendant une activité neutre.",
  "minuteur_secondes": 300,
  "sortie_libre": true }
```

`sortie_libre: true` affiche « je peux arrêter avant la fin, sans avoir à le justifier ».
⭐ **C'est toujours `true`.** Le champ existe pour que ce soit écrit, pas pour être mis à `false`.

🔴 **Le minuteur vit dans le panneau, et Kokoro le dit avant de commencer** — quitter l'app l'arrête sans rien écrire. **Aucun son, aucune vibration ne marque le terme** : à zéro, l'issue `termine` part au dossier, et l'écran ne commente pas.

### `questionnaire` — des questions fermées, une par écran

```json
{ "id": "gad7", "titre": "Questionnaire GAD-7", "type": "questionnaire", "rubrique": "therapie",
  "quand": "sans_date", "duree_minutes": 5,
  "questions": [
    { "id": "q1", "enonce": "…", "choix": [
        { "valeur": 0, "libelle": "Jamais" },
        { "valeur": 3, "libelle": "Presque tous les jours" } ] }
  ] }
```

| Champ | Règle |
|---|---|
| `questions` | Au moins une. `id` en `kebab-case`, **unique dans le questionnaire** — c'est lui qui relie un item à sa réponse |
| `enonce` | Le texte de l'item, **recopié du corpus** |
| `choix` | 🔴 **Au moins deux**, chacun avec `valeur` *(entier)* et `libelle`. **Une question est toujours un choix fermé — aucune saisie de texte, jamais** |

🔴 **Un questionnaire tombe entier ou pas du tout** — `psy:publish` refuse, Kokoro écarte l'étape. **Un item perdu produit un score faux, donc faussement rassurant :** c'est le pire résultat possible ici.
« Passer » écrit `null` — **qui n'est pas `0`**. Un item **jamais atteint** est **absent**, ce qui ne veut pas dire la même chose.

**Trois règles de publication des échelles, non négociables :**

1. 🔴 **Le PHQ-9 ne se publie jamais.** C'est le seul instrument porteur d'un déclencheur d'escalade *(son item 9 interroge l'idéation suicidaire)*, et Kokoro s'interdit tout numéro d'urgence par construction. Il se passe **en conversation**, avec `psy-bilan`.
2. ⛔ **Les items se recopient depuis `psy/docs/corpus/echelles/`, jamais de mémoire.** Un item mal restitué produit un score faux — donc **faussement rassurant**, le pire résultat possible ici.
3. **La cotation n'est pas dans Kokoro.** L'app renvoie les réponses item par item ; **le score se calcule en séance**, et son interprétation aussi. ⭐ **Kokoro n'affiche jamais un score, un seuil ni une interprétation** — ce serait une progression à l'écran, et un score mal lu est pire qu'un score absent.

### `demarche` — une chose à faire dans le monde réel

```json
{ "id": "ppc-releve", "titre": "Demander le relevé de télésuivi", "type": "demarche",
  "rubrique": "therapie", "quand": "sans_date",
  "detail": "Link Sommeil — heures par nuit, nombre de nuits, fuites, IAH résiduel." }
```

Renvoie `fait` ou rien. ⭐ **Pas encore fait n'est pas une donnée** : rien ne s'affiche, rien ne se compte.

🔴 **Une démarche rendue reste à l'écran, en retrait, jusqu'à ce que le psy la retire du programme** *(tranché par Xavier le 18/08/2026)*. **Kokoro montre l'état, il ne décide pas de la sortie** — et il n'affiche jamais combien de démarches restent.

### `fiche` — un texte à lire ou à montrer

```json
{ "id": "panique-13", "titre": "Les 13 symptômes", "type": "fiche", "rubrique": "documentation",
  "quand": "au_besoin", "document": "panique-13-symptomes", "montrable": false }
```

Deux formes, exclusives l'une de l'autre :

- **`texte`** — le contenu est dans le programme. Pour ce qui tient en quelques lignes.
- **`document`** — l'identifiant nu d'un document de la **bibliothèque** *(§6)*. Kokoro résout `bibliotheque/<document>.pdf` et **le confie au lecteur PDF du téléphone** — le **picto « dehors »** de la carte annonce la sortie de l'app. Pour les fiches longues.

`montrable: true` affiche le texte en plein écran, lisible par quelqu'un d'autre — la phrase pour le soignant, la fiche pour Chourouk.

🔴 **Une fiche s'affiche sur l'écran *Documentation*, quelle que soit sa `rubrique`** *(tranché par Xavier le 18/08/2026)*. La bibliothèque entière y vit, groupée par `quand` : c'est ce qui garde à l'écran de crise ses **trois boutons sans défilement**. La `rubrique` d'une fiche ne place donc rien — elle ne range que les étapes qui font agir.

### `bilan` — un compte rendu que Xavier possède déjà

```json
{ "id": "vviq-2026-08", "titre": "VVIQ — imagerie mentale", "type": "bilan",
  "rubrique": "bilan", "date": "2026-08-09", "document": "vviq-2026-08" }
```

| Champ | Règle |
|---|---|
| `rubrique` | 🔴 **Toujours `bilan`** — et aucun autre type ne porte cette rubrique. L'écran *Bilan* ne montre que des bilans ; **une étape rangée là sans place à l'écran disparaîtrait en silence** |
| `quand` | 🔴 **Absent** — ⭐ **la date appartient au document, pas à l'assiduité de Xavier** |
| `date` | `AAAA-MM-JJ`, **celle du bilan, jamais celle de la publication**. L'écran groupe **par mois décroissant** |
| `document` | Obligatoire — l'identifiant nu d'un fichier de **`companion/inputs/bilans/<id>.md`**, converti en `bilans/<id>.pdf` par `psy:publish`. 🔴 **Jamais `texte`** |

🔴 **Un bilan ne passe pas par la bibliothèque** — canal distinct, contrôles distincts. Une fiche est *écrite pour Xavier* et *lisible par l'aidant* ; **un bilan n'est ni l'un ni l'autre : c'est un document qu'il possède déjà, adressé à lui seul.** Les sept familles d'interdits du §7 **ne s'appliquent donc pas au corps d'un bilan** — elles restent appliquées à son `titre`, qui, lui, s'affiche dans Kokoro.

🔴 **`montrable` n'existe pas sur un bilan, et Kokoro n'offre aucune fonction de partage.** Il confie le PDF au lecteur du téléphone — picto « dehors », comme une fiche. ⭐ **Le partage est un acte de Xavier dans son lecteur, pas une fonction du dispositif.**

⭐ **Un bilan se publie à tout moment, hors séance** — comme la documentation, et **sous supervision bloquante portant sur une seule question : ce document ne contient rien que Xavier ne sache déjà.** Il ne renvoie rien : `reponses/` ne le connaît pas.

### `seance-duo` — un déroulé chronométré tenu par l'aidant

```json
{ "id": "stab-ancrage-1", "titre": "Ancrage corporel — à deux", "type": "seance-duo",
  "rubrique": "therapie", "quand": "sans_date", "duree_minutes": 22,
  "entrainement_requis": true,
  "signal_arret": "Xavier fait « non » de la main. On s'arrête, sans rien demander.",
  "avant": [
    "Pièce calme, lumière baissée, porte fermée.",
    "Le téléphone reste dans tes mains du début à la fin.",
    "Xavier t'a montré le « non » de la main avant qu'on commence."
  ],
  "sequence": [
    { "pour": "aide",    "consigne": "Assieds-toi en face de lui, à un mètre.", "secondes": 30 },
    { "pour": "patient", "consigne": "Lis à voix haute, mot pour mot : « Pose les deux pieds à plat. »", "secondes": 60 },
    { "pour": "aide",    "consigne": "Ne parle pas pendant ce temps. Le minuteur t'avertit.", "secondes": 60 }
  ],
  "arret": [
    "Il fait le signal d'arrêt → on s'arrête, on ne demande rien.",
    "Il ne répond plus aux consignes → on s'arrête, c'est un shutdown, pas un refus.",
    "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser."
  ],
  "sortie_libre": true }
```

| Champ | Règle |
|---|---|
| `entrainement_requis` | 🔴 **Toujours `true`.** La première exécution réelle ne peut pas être la première fois que l'aidant découvre le déroulé. Kokoro propose l'**entraînement** tant qu'il n'a pas été fait au moins une fois |
| `signal_arret` | 🔴 **Obligatoire et non vide.** ⭐ **C'est le champ le plus important du type** : Xavier doit pouvoir arrêter **sans parler**, parce que c'est exactement ce qui tombe en premier. Le geste se convient **à froid**, jamais pendant. 🔴 **Le geste convenu est le « non » de la main** — il se recopie tel quel dans chaque `seance-duo`, **il ne se réinvente pas d'une séance à l'autre**. **Kokoro l'affiche en tête de la case à cocher des critères d'arrêt** *(voir `arret`)* |
| `avant` | Ce qui doit être vrai avant de commencer. L'aidant coche, ou n'entre pas dans la séquence |
| `sequence` | Consignes ordonnées. `secondes` est le temps tenu par l'appareil. 🔴 **La consigne dit elle-même à qui elle s'adresse** — « Lis à voix haute, mot pour mot : … » quand elle est destinée à Xavier. **Kokoro n'affiche aucune étiquette autour d'elle** ; `pour` *(`aide` · `patient`)* reste obligatoire et **contrôlé** au dépôt comme à la lecture, mais **il ne s'affiche pas**. 🔴 **Aucune consigne ne demande une réponse gestuelle de la main** — le « non » de la main est réservé à l'arrêt, et un geste ambigu se lit comme un arrêt manqué |
| `arret` | 🔴 **Obligatoire, au moins deux entrées.** ⭐ **La dernière est toujours « tu ne sais pas quoi faire → on s'arrête »** — l'aidant n'improvise jamais. 🔴 **Kokoro les porte sur une case à cocher de l'écran *avant*, le signal d'arrêt en tête** : elles se lisent **avant** d'entrer, et le déroulé ne s'ouvre pas tant que la case n'est pas cochée. ⭐ **Cocher est la seule preuve qu'elles ont été lues** — un rappel permanent et un bouton « quand s'arrêter » encombraient chaque consigne sans rien garantir |
| `sortie_libre` | `true`, comme partout |

> 🔴 **Ce que le type ne porte jamais** *(contrôle **C10**)* : un diagnostic, un score, une hypothèse, un compte rendu — **rien qui apprenne à l'aidant quelque chose sur Xavier qu'il n'a pas décidé de partager**. Et aucune consigne qui **lui demande de juger** : « estime si ça va », « décide s'il faut continuer », « rassure-le ». **Une consigne qui demande un jugement clinique la met en faute quoi qu'elle fasse.**

> ⭐ **Le mode entraînement compte autant que la séance.** C'est **la même séquence, jouée à blanc**, sans le matériel réel — 🔴 **et sans minuteur** *(19/08/2026)* : **l'aidant passe d'une consigne à l'autre à la main**, et le temps affiché est celui que la consigne **durera en séance**. ⭐ **Deux raisons, et la seconde est la vraie** : un déroulé qui s'enchaîne tout seul ne laisse pas le temps de se préparer, **et voir la durée avant de la vivre est précisément ce qu'un entraînement sert à donner.** Il renvoie `issue: "entrainement"` — **ce n'est pas une donnée clinique et rien ne s'en déduit**. **La première fois que ça compte ne doit pas être la première fois que ça se fait.**

> ⚠️ **Limite connue du format : `sequence` est linéaire, elle ne sait pas exprimer une répétition en séries.** Un déroulé de stimulation bilatérale est fait de séries — *n* allers-retours, une pause, on recommence. 🔴 **Déplier trente consignes identiques serait un contournement, pas une solution** : le format porterait une cadence sans jamais la nommer, et le Superviseur n'aurait rien à contrôler. **L'extension se décide en séance, sous supervision — pas à l'implémentation.**

---

## 4. L'écran d'accueil

Groupé par **rubrique**, puis par **`quand`** : *aujourd'hui* · *quand j'en ai besoin* · *sans date*.
**Seul l'écran *Bilan* fait exception : il groupe par mois décroissant, sur la `date` du document.**
**Aucun score, aucune progression, aucun historique, aucun palier atteint.**

---

## 5. Ce que Kokoro renvoie

Un fichier par étape faite, dans `reponses/` : `AAAA-MM-JJ-HHMM-<id>.json`

```json
{ "etape": "ppc-p1", "horodatage": "2026-08-13T18:04:00+02:00",
  "issue": "termine", "reponses": null, "source": "android" }
```

**Seul le `questionnaire` remplit `reponses`** — une liste ordonnée, un objet par item atteint :

```json
{ "etape": "gad7", "horodatage": "2026-08-19T21:12:00+02:00", "issue": "termine",
  "reponses": [ { "question": "q1", "valeur": 2 }, { "question": "q2", "valeur": null } ],
  "source": "android" }
```

⭐ **La cotation n'est pas là-dedans.** Kokoro renvoie les items ; le score se calcule en séance, avec `psy-bilan`.

`issue` : `termine` · `arrete_avant_la_fin` · `fait` · `entrainement`.
⭐ **`arrete_avant_la_fin` n'est pas un échec et ne se commente nulle part.**
⭐ **`entrainement` n'est pas une donnée clinique** — il dit seulement que le déroulé a été répété à blanc.

🔴 **Ce que Kokoro a écrit, il s'en souvient localement** — l'état d'une étape ne dépend jamais d'un aller-retour par Drive. Sans ça, une étape faite réapparaît *à faire* le temps que le fichier remonte, et Xavier la refait. **Drive dit ce qui est arrivé au dépôt ; il ne dit pas ce que Xavier a fait.**

---

## 6. La bibliothèque

**`companion/inputs/bibliotheque/<id>.md`** — un fichier Markdown par document. ⭐ **Le Markdown est la version qui se relit et qui passe la supervision ; il ne part pas.** `npm run psy:publish` le convertit en **`bibliotheque/<id>.pdf`** et ne publie que le PDF — et **retire du transit tout document que la bibliothèque n'appelle plus**.

> 🔴 **La règle qui vaut plus que toutes les autres ici : un document de la bibliothèque est *écrit pour Xavier*, il n'est pas *copié depuis* `psy/docs/protocoles/`.**
>
> Un protocole clinique porte des diagnostics, des pronostics, des noms de praticiens, des hypothèses non tranchées et des réserves adressées à un professionnel. **Une fiche de bibliothèque porte ce qu'il y a à faire, et pourquoi.** C'est le contrôle **C9**.

**Ce qu'une fiche de bibliothèque ne contient jamais :** un diagnostic non encore dit à Xavier · un pronostic · un nom de praticien autre que ceux qu'il consulte · une hypothèse formulée comme un fait · une réserve destinée au Dr Isorni · **et tous les interdits du §7**.

> 🔴 **Le partage entre les deux surfaces — il découle de leurs durées de vie, pas d'un goût** *(18/08/2026)*.
>
> **Ce qui change au rythme des séances vit dans le programme** *(le palier en cours, l'étape du jour, ce qui reste à faire)* — il est republié à chaque clôture. **Ce qui ne change pas vit dans la bibliothèque** *(une échelle entière, une règle, des critères d'arrêt, une conduite de voyage)* — elle part en **PDF figé**, et `psy:publish` ne reconvertit que ce qui a changé.
>
> ⚠️ **Une fiche ne dit donc jamais où Xavier en est rendu.** Elle périmerait au premier passage de palier, **sans que rien ne force sa réécriture**, et c'est la version figée qu'il ouvrirait pour comprendre. **C'est le mode de défaillance C8 appliqué à la bibliothèque** — constaté sur `ppc-les-paliers` avant publication.

**Les fiches sont soumises aux mêmes vérifications que les étapes** : `npm run psy:publish` lit chaque fichier de la bibliothèque et applique les sept familles d'interdits. 🔴 **Kokoro les réapplique à la lecture, sur ce qu'il affiche lui-même** — le titre d'une fiche et le `texte` d'une étape ; **le corps d'un PDF, lui, n'est vérifié qu'au dépôt.**

### Les bilans — l'autre canal

**`companion/inputs/bilans/<id>.md`** — un fichier Markdown par bilan, converti en **`bilans/<id>.pdf`** par `psy:publish`, qui retire du transit tout bilan que le programme n'appelle plus. **Aucun bilan ne vit dans `bibliotheque/`, et aucune fiche ne vit dans `bilans/`.**

🔴 **Les sept familles d'interdits du §7 ne s'appliquent pas au corps d'un bilan** : un rapport clinique réel nomme des traitements, des diagnostics et des praticiens, et c'est précisément sa raison d'être. `psy:publish` y applique ses propres contrôles — le `document` appelé existe, le titre affiché est permis, `texte` et `montrable` sont absents.

⭐ **Un bilan ne se réécrit pas pour Xavier : il est déjà à lui.** C'est ce qui le sépare d'une fiche, et ce qui remplace **C9** sur ce canal.

---

## 7. 🔴 Les interdits — vérifiés à la publication ET à la lecture

**Les tests de Kokoro lisent les textes de l'app ; ces textes-ci n'y sont pas.** Sans double vérification, tous les garde-fous du dispositif deviennent contournables par du contenu, **en silence**.

**Deux vérifications, deux réactions volontairement différentes :**

- **`npm run psy:publish` refuse la publication entière.** Sur le PC, on peut corriger — donc on corrige, on ne publie pas à moitié.
- **Kokoro écarte la seule étape fautive** et affiche le reste. Sur le téléphone, on ne peut pas corriger, et perdre tout le programme pour une ligne serait pire.

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

## 8. Ce que le programme ne fait jamais

1. **Notifier.** *(Seule exception : l'accès crise sur l'écran verrouillé — une porte, pas un rappel.)*
2. **Compter d'un jour à l'autre.** Aucun palier atteint, aucun historique, aucune progression à l'écran. **Les paliers se cotent en séance, à partir du journal.**
3. **Reprocher.** Une étape non faite disparaît de l'écran le lendemain **sans laisser de trace**.
4. **Publier une étape qui fait agir hors séance.** Seule la documentation le peut, et elle s'annonce au moment où elle est publiée.
