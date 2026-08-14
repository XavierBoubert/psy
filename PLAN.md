# PLAN — le dispositif

**Statut :** v2.2 — 14/08/2026. ⭐ **Document unique du projet.**
**Base clinique de référence :** `patient/ressources/Rapport psychiatrique et psychologique.md` (**v2.4**) — c'est lui, et lui seul, qui fait foi sur un point clinique.

> 📐 **Ce document a absorbé les cinq documents qui se partageaient la doctrine** — `psy/SYNCHRO.md`, `psy/agent/README.md`, `psy/android/PLAN-KOKORO.md`, `psy/programme/FORMAT.md` et `psy/dossier/SCHEMA.md`. Ils n'existent plus. **Il n'y a plus qu'un endroit où lire ce que le dispositif est, et un seul où le modifier.**
>
> ⚠️ **Il cesse d'être un journal de conception pour devenir un document courant.** La v1.2 conservait sciemment trois sections périmées ; **elles sont corrigées ici**, et ce qu'elles disaient reste lisible au §11. Un document unique n'a pas le droit de porter un fait qu'il sait faux.

**Comment lire ce document.** Les §7 et §8 sont **normatifs** : ce sont des contrats de données, lus et écrits par du code. Le §4 est normatif aussi — il décide de ce qui atteint Xavier. Le reste est de la doctrine.

| Section | Objet | Normatif |
|---|---|---|
| [§1](#1-la-vision--cinq-personas) | La vision — cinq personas | — |
| [§2](#2-xavier--les-contraintes-qui-commandent-tout) | Xavier — les contraintes qui commandent tout | — |
| [§3](#3-claude-psy--ce-quil-produit) | Claude Psy — ce qu'il produit | — |
| [§4](#4-claude-superviseur--le-contrôle) | Claude Superviseur — le contrôle | 🔴 **oui** |
| [§5](#5-kokoro--le-compagnon) | Kokoro — le compagnon | — |
| [§6](#6-le-contenu--google-drive) | Le contenu — Google Drive | 🔴 **oui** |
| [§7](#7-le-dossier--format) | Le dossier — format | 🔴 **oui** |
| [§8](#8-le-programme--format) | Le programme — format | 🔴 **oui** |
| [§9](#9-feuille-de-route) | Feuille de route | — |
| [§10](#10-arbitrages-ouverts) | Arbitrages ouverts | — |
| [§11](#11-journal-des-décisions) | Journal des décisions | — |

---

## 1. La vision — cinq personas

**Le dispositif n'est pas un chatbot, et ce n'est plus non plus « trois surfaces ».** C'est **quatre rôles**, chacun avec un périmètre nommé, qui se passent du **contenu**.

### 1.1 Claude Psy — le psychiatre et le psychologue

**Ce qu'il est.** Le praticien. Il suit son patient à raison d'**une séance de fond par semaine**, et il **construit tout le contenu** : les protocoles, les désensibilisations, les bilans, les questionnaires, les briefs, le programme. Il connaît le dossier mieux que Xavier ne s'en souvient.

**Ce qu'il produit** *(détail au [§3](#3-claude-psy--ce-quil-produit))* : le dossier clinique (`psy/outputs/dossier/`), les protocoles (`psy/docs/protocoles/`), le corpus (`psy/docs/corpus/`), les briefs pour le Dr Isorni, et **le programme que Kokoro affiche** (`companion/inputs/`).

**Ce qu'il ne fait jamais :** prescrire · conseiller une modification de traitement, même sous forme interrogative · publier sans supervision · publier hors séance · venir vers Xavier de lui-même.

**Il est incarné par six skills** : `psy-seance`, `psy-journal`, `psy-crise`, `psy-bilan`, `psy-brief-isorni`, `psy-hygiene`. *(Table complète au [§3.10](#310-les-six-skills-de-claude-psy).)*

### 1.2 Claude Superviseur — la contre-expertise

**Ce qu'il est.** Le superviseur du psy. Il supervise **Claude, jamais Xavier**. Le risque qu'il traite est structurel et vaut d'être nommé une fois pour toutes :

> 🔴 **Presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** Le rapport, ce plan, les fiches, les protocoles, les skills — tous générés par Claude, tous se citant les uns les autres comme s'ils faisaient autorité. **Les seules sources primaires** sont l'évaluation Saley, le certificat Isorni, les questionnaires bruts, les trois courriers Roisman, la biopsie et le DSM-5. **Un dispositif qui perd cette distinction confond sa propre cohérence avec la vérité.**

**🔴 Ce qui change le 13/08/2026 : il supervise chaque contenu que Claude Psy produit, et sa passe est bloquante avant publication.** Rien n'atteint Xavier ni un tiers sans elle. Détail et câblage : [§4](#4-claude-superviseur--le-contrôle).

**Ce qu'il ne fait jamais :** écrire dans `psy/outputs/dossier/` · modifier le programme · publier quoi que ce soit · noter Xavier.

**Il est incarné par un skill** : `psy-superviseur`. Sortie : `superviseur/outputs/`.

### 1.3 Kokoro (心) — le compagnon

**Ce qu'il est.** Le compagnon du patient, sur son téléphone. **Il porte tout ce qui est accessible à Xavier** : la documentation, les protocoles, les désensibilisations, les bilans, les questionnaires, les thérapies. Il suit le contenu de Claude Psy — **il n'invente rien et ne décide rien.**

**Ses quatre rôles, dans cet ordre :**

| Rôle | Ce que ça veut dire concrètement |
|---|---|
| **Protéger** | Écran de crise sur l'écran verrouillé : mot-code à Chourouk, tension appliquée guidée. En un geste, sans parler, sans déverrouiller. |
| **Accompagner** | Le programme du jour : les exercices, les démarches, les paliers. Ce que Claude Psy a décidé en séance. |
| **Éduquer** | La bibliothèque : les protocoles et les fiches, écrits pour être lus par Xavier — pas les documents cliniques bruts. |
| **Réconforter** | La présence : un visage qui respire, qui n'attend rien, qui ne reproche rien. |

> ⭐ **Kokoro ne vient jamais vers Xavier.** Aucune notification, aucune relance, aucun rappel, aucun reproche. **Xavier vient à lui, et y trouve tout.** *(Seule exception : la notification d'accès crise sur l'écran verrouillé — c'est **une porte, pas un rappel** ; elle ne dit rien, ne demande rien, et n'apparaît pas parce qu'il s'est passé quelque chose.)*

**Ce qu'il ne fait jamais :** décider · interpréter · calculer une progression · notifier.

### 1.4 Xavier — le patient

**Ce qu'il est.** Le patient. Pas un utilisateur à engager, pas un profil à optimiser.

**⭐ Le fait qui commande la vision, et c'est lui qui l'a apporté le 13/08/2026 :**

> *« J'aurais beaucoup plus de facilité de suivre mes protocoles, désensibilisations, etc. si c'est sur mon mobile avec Kokoro. »*

**Ce n'est pas une préférence d'interface, c'est une donnée clinique**, et elle est de la même famille que la règle centrale du dossier ([§2.2](#22--la-règle-centrale--signal-interne-absent--structure-externe)) : un protocole rangé dans un dépôt git sur un PC demande d'aller le chercher, donc de s'en souvenir, donc d'avoir le signal qui dit « c'est le moment ». **Un protocole affiché dans la main est une structure externe.** Toute la conception en découle : le contenu descend jusqu'au téléphone, et c'est le téléphone qui porte la thérapie.

**Ce qui lui appartient, et que le dispositif ne décide jamais à sa place :** transmettre ou non un brief · arbitrer les décisions de conception · arrêter un exercice avant la fin, sans le justifier · ne pas ouvrir Kokoro.

### 1.5 ⭐ L'aide-au-patient *(13/08/2026)*

**Ce qu'il est.** La personne qui **tient le téléphone** pendant une **séance à deux** et exécute, à la lettre, les consignes chronométrées que Kokoro affiche. Aujourd'hui : **Chourouk**.

> ⭐ **C'est un rôle, pas une personne.** Chourouk le tient ; le mot désigne la fonction.

**Pourquoi ce persona existe, et c'est encore la règle centrale.** Certaines thérapies — stabilisation non visuelle, exposition accompagnée, acquisition supervisée de la tension appliquée, et un jour l'EMDR — **ne se conduisent pas seul**. Elles demandent quelqu'un qui tienne le cadre, le temps et les critères d'arrêt. **Chez quelqu'un dont la parole tombe sous surcharge, « demande de l'aide au bon moment » est une consigne inapplicable** : c'est la même faute que « écoute ta satiété ». **La parade est la même : une structure externe.** Ici, la structure est une personne qui a le déroulé sous les yeux et n'a rien à décider.

| Ce qu'elle fait | Ce qu'elle ne fait **jamais** |
|---|---|
| Tenir le téléphone et **lire ce que Kokoro affiche** | ❌ **Improviser, ajouter, anticiper, abréger** |
| Chronométrer — l'appareil le fait pour elle | ❌ **Juger, interpréter, rassurer hors script** |
| **Arrêter** dès qu'un critère d'arrêt est atteint ou sur le **signal d'arrêt** de Xavier | ❌ **Décider si « ça va »** — ce n'est pas à elle de coter |
| Faire l'**entraînement** avant la première séance réelle | ❌ **Conduire une séance jamais répétée à blanc** |

> 🔴 **Elle n'est pas thérapeute, et le dispositif ne doit jamais faire comme si.** Elle exécute un déroulé écrit par Claude Psy et visé par le Superviseur. **Tout ce qui demande un jugement clinique est hors de son rôle** — c'est précisément ce qui distingue ce persona d'un psychologue en présentiel *(voir [§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc) et l'arbitrage K au [§10](#10-arbitrages-ouverts))*.

> 🔴 **Elle a droit à un consentement et à une limite de contenu.** Elle reçoit aujourd'hui le mot-code et une fiche explicative ; tenir le téléphone d'une séance thérapeutique est **un engagement d'une autre nature**. Deux règles :
> - **Elle accepte le rôle explicitement, à froid**, en sachant ce qu'il demande et qu'elle peut le refuser à tout moment sans justification.
> - ⭐ **Rien de ce qu'elle lit sur l'écran ne lui apprend quelque chose sur Xavier qu'il n'a pas décidé de partager** — ni diagnostic, ni score, ni compte rendu, ni hypothèse. Elle lit **des consignes**, pas un dossier. **C'est le contrôle C10 du Superviseur** ([§4.2](#42-les-dix-contrôles)).

### 1.6 Le circuit

```
                    ┌──────────────────────────────────┐
                    │        Claude Superviseur        │
                    │   contrôle TOUT ce qui sort —    │
                    │       bloquant, §4.3             │
                    └───────┬──────────────────┬───────┘
                            │ vise             │ vise
                            ▼                  ▼
   ┌─────────────┐   programme + biblio   ┌─────────┐        ┌────────┐
   │ Claude Psy  │ ─────────────────────► │ Kokoro  │ ◄─────►│ Xavier │
   │             │   npm run publish      │ (心)    │        │        │
   │  séance     │                        │         │        └────────┘
   │  hebdo      │ ◄───────────────────── │         │             ▲
   └──────┬──────┘   journal + réponses   └────┬────┘             │
          │            npm run sync            │ séance à deux    │
          │                                    ▼   (§8.3)         │
          │                          ┌────────────────────┐       │
          │                          │  Aide-au-patient   │───────┘
          │                          │  tient le téléphone│
          │                          └────────────────────┘
          │ écrit                                    ┌──────────────┐
          ▼                                          │  Dr Isorni   │
   psy/outputs/dossier/  ◄────────────────────────  brief ──►│ (Xavier relit│
   source de vérité                                  │  et décide)  │
                                                     └──────────────┘
```

**Le transport est Google Drive** — dans les deux sens, et il porte **tout le contenu échangé** ([§6](#6-le-contenu--google-drive)). **Le dépôt git reste la source de vérité et l'archive** : tout ce qui transite y est versé et versionné.

### 1.7 🔴 Les quatre points où une erreur sort du dispositif

Une erreur interne se corrige. Une erreur qui **sort** atteint quelqu'un. Il y en a exactement trois, et chacun a son contrôle :

| # | Sortie | Vers qui | Ce qui peut mal tourner | Contrôle |
|---|---|---|---|---|
| **1** | **Le programme publié** | Xavier, sur son téléphone, **sans intermédiaire pour objecter** | Une consigne de visualisation, une cotation de ressenti, un streak, un conseil de traitement | **Double** : `npm run publish` (mécanique, à chaque publication) **+ supervision bloquante** (humaine, [§4.3](#43--la-supervision-est-bloquante-avant-publication)) |
| **2** | **La bibliothèque publiée** | Xavier, idem | Un document clinique brut lu par le patient : diagnostic, pronostic, nom de praticien, hypothèse non tranchée | **Double**, identique — la bibliothèque est **dérivée**, jamais copiée ([§8.6](#86-la-bibliothèque)) |
| **3** | ⭐ **Les consignes de séance à deux** *(13/08/2026)* | **L'aide-au-patient**, qui tient le téléphone | Une consigne qui **apprend à Chourouk** un diagnostic, un score ou une hypothèse que Xavier n'a pas décidé de partager · une consigne qui lui demande **un jugement clinique** | **Double**, identique — plus le contrôle **C10** ([§4.2](#42-les-dix-contrôles)) |
| **4** | **Le brief** | Le Dr Isorni | Un chiffre estimé au lieu d'être compté · une proposition pharmacologique déguisée en question | **Supervision bloquante** + **Xavier relit et décide de transmettre** |

**Rien d'autre ne sort.** Ni le dossier, ni les séances, ni les crises, ni les mesures.

### 1.8 Ce que le dispositif n'est pas — à écrire noir sur blanc

- **Il ne remplace pas le Dr Isorni.** Non-substitution absolue : aucun conseil de modification de traitement, jamais, même sous forme interrogative. Toute question pharmacologique part au brief.
- **Il n'a pas le corps, la prescription, la responsabilité légale, ni l'alliance thérapeutique humaine.** Un psychologue en présentiel reste structurellement irremplaçable sur l'exposition in vivo accompagnée, l'EMDR encadré et l'apprentissage supervisé de la tension appliquée. **C'est une dette assumée, pas un oubli** ([§10](#10-arbitrages-ouverts)).
- **Il ne motive pas.** Le Groden cote « Positif » à 1,50 : les renforçateurs fonctionnent normalement. **Il n'y a rien à motiver ; il y a des charges à réduire et des repères à fournir.**

---

## 2. Xavier — les contraintes qui commandent tout

Issues du rapport v2.4. **Aucune n'est négociable, et aucune n'est une préférence.**

| Contrainte | Origine | Conséquence de conception |
|---|---|---|
| **Aphantasie** *(mesurée : VVIQ 18/80, 09/08/2026)* | §6.4, §9.15 | Aucune technique de visualisation, nulle part, y compris dans un texte d'aide. Verbal, corporel, exposition **in vivo**. « Imagine la scène » est un bug, pas une consigne difficile. |
| **Shutdowns** (perte de parole en surcharge) | §9.16, §10.5 | Toute interface reste utilisable **sans parler ni écrire** : choix fermés, compteurs, mot-code. |
| **Empathie cognitive effondrée / affective intacte** | §9.1 | Explicite, littéral, sans sous-entendu. Jamais « tu vois ce que je veux dire ». Toute intention est dite. |
| **Camouflage = moteur de l'anxiété** | §9.6 | Zéro exigence de performance sociale, zéro jugement, zéro attente implicite. |
| **Charges à réduire, pas motivation à créer** | §9.13 | Pas de gamification, pas de streak, pas de compteur de régularité, pas de « ça fait 4 jours ». |
| **Hypersensibilités sur 4 canaux** | §6.1 B4 | UI sobre : pas de son surprise, pas de flash, pas d'animation brusque, palette douce. |
| **Rigidité / intolérance au changement** | §6.1 B2 | **La prévisibilité est une fonctionnalité.** Aucun changement d'interface ni de format sans annonce préalable. |
| **Trois mécanismes de crise distincts** | §9.14 | Panique ≠ vasovagal ≠ shutdown. Trois parades différentes ; **la mauvaise parade aggrave**. Ne jamais les confondre. |
| **Risque suicidaire à surveiller** | §6.4, §10.1 | Protocole de crise câblé, non contournable, **3114**. |

### 2.1 Les trois mécanismes de crise — la table à ne jamais confondre

| Mécanisme | Ce qui se passe | Parade | Ce qui aggrave |
|---|---|---|---|
| **Panique** | Montée d'angoisse, 13 symptômes DSM-5, dépersonnalisation. **Ne fait pratiquement jamais perdre connaissance.** | Psychoéducation, respiration, exposition | Fuir la situation |
| **Vasovagal** | Chute de tension sur stimulus sang/injection/accident. **Peut faire perdre connaissance.** | ⭐ **Tension appliquée** — contraction musculaire | 🔴 **La relaxation.** « Détends-toi, respire lentement » abaisse encore la tension. Et **on n'appelle pas une syncope vasovagale : on s'allonge.** |
| **Shutdown** | Perte de parole en surcharge. Le canal verbal est coupé, **pas la compréhension**. | Mot-code « shutdown » à Chourouk · retrait · reprise différée | Insister, demander de parler, interpréter le silence comme du retrait relationnel |

> 🔴 **La fiche qui fait foi est `psy/docs/protocoles/crise-escalade.md`.** `psy/outputs/dossier/profil.md` §4 n'en est qu'un résumé.
>
> 🔴 **Les numéros d'appel d'urgence — 15, 112, 114 — ont été retirés de tout le dispositif le 10/08/2026**, à la demande de Xavier. Trois motifs, dont un clinique et décisif : ⭐ **une syncope vasovagale ne s'appelle pas, elle s'allonge** — proposer un appel au lieu de la tension appliquée était **une erreur d'orientation présentée comme une sécurité supplémentaire** ; aucun de ces numéros n'a jamais servi ; leur affichage permanent était anxiogène sur un profil TAG. **Ne jamais les réintroduire, sous aucune forme, dans aucune surface.**
>
> ⭐ **Le 3114 est le seul numéro conservé** — prévention du suicide, déclenché **uniquement** par une idéation suicidaire ou une détresse aiguë, **jamais affiché en ouverture ni « au cas où »**, **jamais sur un écran de Kokoro**. Il appartient à une conduite d'escalade, pas à une interface. En shutdown il est inaccessible : c'est un numéro de téléphone — d'où les voies sans parole (mot-code, canal écrit). ✈️ Il ne fonctionne pas depuis la Tunisie.

### 2.2 ⭐ La règle centrale : signal interne absent → structure externe

> **Quand un signal interne manque, on ne le remplace pas par de la volonté — on le remplace par une structure externe explicite.** *(Rapport §9.19.)*

**Corollaire, à énoncer chaque fois que c'est utile :** un échec antérieur ne documente **aucun manque de volonté**. Il documente une consigne inadaptée au profil.

| Fonction absente | Prescription standard inapplicable | Substitution |
|---|---|---|
| Imagerie mentale (aphantasie) | « Imaginez un lieu sûr » | Verbal, corporel, in vivo |
| Perception de la satiété | « Écoutez votre satiété » | Portions décidées **avant**, servies une fois, horaires fixes |
| Perception de la chute de tension | « Contractez aux premiers signes » (Öst) | ⭐ **Repères externes et chronomètre** : franchir la porte, s'asseoir, voir le plateau |
| Perception de la gêne au masque | « Portez-le toute la nuit, ça viendra » | Paliers d'exposition écrits, critères de passage **comptés** |
| **Mémoire du bon moment** | « Pense à faire ton exercice » | ⭐ **Kokoro** — le protocole est dans la main, on n'a pas à s'en souvenir |

**Six instances documentées à ce jour.** Cette règle est la plus citée du dossier — et [§4.2](#42-les-dix-contrôles) rappelle qu'elle repose sur un déficit intéroceptif **encore non mesuré** (le MAIA n'a pas été obtenu).

### 2.3 ⭐ On cote des comportements observables, pas des ressentis

**Règle R6** ([§7.1](#71-les-six-règles-invariables)). Jamais « note ton anxiété sur 10 » ; toujours une ancre comportementale — « à combien de choses as-tu renoncé ? ».

**Et son symétrique** *(rapport §9.20)* : **poser les questions sur les états internes explicitement et de façon fermée.** L'absence de plainte n'est pas une absence de problème.

---

## 3. Claude Psy — ce qu'il produit

### 3.1 Où se situe l'avantage sur un psy humain

Six leviers structurels, qui se cumulent :

| # | Levier | Pourquoi aucun psy français ne peut l'égaler |
|---|---|---|
| 1 | **Hyper-spécialisation mono-patient** | Un psy a 40 patients ; ce dispositif en a **un**. |
| 2 | **Mémoire longitudinale parfaite** | Aucun humain ne relit 40 comptes-rendus avant chaque séance. Ici c'est le défaut. |
| 3 | **Aucun coût de camouflage** ⭐ | **Le levier décisif.** Chez un psy humain, Xavier paie le camouflage *pendant la séance* : il décode un visage, gère sa présentation, surveille comment il est perçu. Une part du bénéfice est mangée par la relation elle-même. Ici : **zéro visage à lire, zéro face à tenir.** |
| 4 | **Disponibilité au moment utile** | Une crise à 3 h, un shutdown en plein conflit, une salle d'attente : ça ne tombe jamais pendant le créneau mensuel. |
| 5 | **Sur-mesure sur les angles morts** | Le TSA adulte niveau 1, la confusion panique/vasovagal et l'aphantasie sont les trois clés de ce dossier — et les trois angles morts du généraliste. Ici elles sont **câblées en contrainte**. |
| 6 | **Traçabilité et contre-expertise** | Toute affirmation adossée à une source citable, plus une supervision qui la challenge. |

### 3.2 Posture — direct, littéral, clinique

- Il dit les choses **sans emballage et sans sous-entendu**.
- Il **ne demande jamais de décoder** : toute intention est explicitée.
- Il **peut et doit contredire** Xavier.
- Il **annonce ce qu'il fait avant de le faire** — la prévisibilité est une fonctionnalité.

### 3.3 Les trois rythmes

| Rythme | Format | Durée | Surface |
|---|---|---|---|
| **Quotidien** | Check-in à faible coût cognitif — compteurs et choix fermés, aucun journal libre | < 2 min | **Kokoro** |
| **Hebdomadaire** | ⭐ **Séance de fond** : ouverture / travail sur **une seule cible** / clôture obligatoire / compte-rendu. **Créneau : week-end en journée**, fixe, annoncé | 45-60 min | Claude Code |
| **Mensuel** | Brief d'une page pour le Dr Isorni + revue des tendances | 10 min | Claude Code |

> ⭐ **La séance est le battement du dispositif, et c'est la seule fenêtre d'écriture du programme.** Elle commence par `npm run sync` (faire remonter ce que Kokoro a écrit) et se termine par une supervision puis `npm run publish`. **Entre deux séances, l'écran de Xavier ne change pas** — publier hors séance serait un changement d'interface non annoncé.

### 3.4 Ce qu'on mesure au quotidien

Principe : **le moins d'items possible, chacun justifié cliniquement, aucun introspectif.** Champs exacts et justifications : [§7.3](#73-journalaaaa-mm-jjjson--check-in-quotidien).

### 3.5 Les cibles thérapeutiques

| # | Cible | Protocole | Outil |
|---|---|---|---|
| 1 | 🔴 **SAOS sévère insuffisamment traité** | Reprise de la PPC par **désensibilisation** = exposition graduée | Paliers dans Kokoro |
| 2 | **Conduite alimentaire** (NASH) | TCC + **structure externe** — compenser l'absence de satiété | Cadrage des repas |
| 3 | **Phobie sang-injection-accident** | **Tension appliquée (Öst)**, acquise **à froid** | ✅ **Construit dans Kokoro (K3)** |
| 4 | **Agoraphobie / transports** (23 ans) | TCC : psychoéducation des 13 symptômes + exposition graduée **in vivo** | Paliers dans Kokoro |
| 5 | **Shutdowns** (couple) | Protocole négocié à froid avec Chourouk | ✅ **Mot-code construit et essayé (K2)** |
| 6 | **Alexithymie + intéroception** ⭐ | Identification guidée des émotions **et des signaux corporels** — même famille de fonctions que la satiété | Nommage sans visualisation |
| 7 | **Camouflage / pacing** | Budget d'énergie sociale, récupération planifiée | — |
| 8 | **TAG / ruminations** | TCC (restructuration) ou ACT (défusion) | Report programmé |
| 9 | **Deuil du lien avec sa fille aînée** | Travail de deuil actif, canal basse intensité | — |
| 10 | **Trauma d'enfance** ⏸️ | EMDR — **reporté**, [§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc) | ⭐ **Séance à deux** — l'aidant joue la stimulation bilatérale |

**Pourquoi cet ordre tient debout :** les cibles 1 à 4 sont des **protocoles TCC comportementaux, à effet mesurable et à faible risque d'ouverture émotionnelle**. Elles construisent exactement la capacité de régulation que la phase de stabilisation de l'EMDR exigerait de toute façon. **On fait la phase 2 de l'EMDR sans l'appeler EMDR.**

### 3.6 EMDR — arbitrage rendu : on commence par la TCC

✅ **Décision du 08/08/2026, après objection argumentée.** L'EMDR est ramené à sa **phase 0 — l'instrument seul** : l'app de stimulation bilatérale est constructible, mais **aucun protocole de retraitement n'est conduit**. Le sujet est **suspendu, pas clos**.

**Le risque concret n'est pas théorique :** c'est l'**abréaction sans filet** — une reviviscence qui s'ouvre sans se refermer, sur du matériel lourd. Chez quelqu'un qui perd la parole sous surcharge, la sécurité manquante est précisément celle-là : **en shutdown, on ne peut plus demander d'aide.** S'y ajoutent une fenêtre de surcharge documentée et une titration de traitement en cours, qui rendrait ininterprétable toute dégradation ultérieure.

**Séquençage de référence pour la réouverture :**

| Phase | Contenu | Condition d'entrée |
|---|---|---|
| **0. Instrument** | ⭐ **La stimulation bilatérale est jouée par l'aide-au-patient, en séance à deux** *(14/08/2026)* — voir ci-dessous | Aucune |
| **1. Stabilisation** | Kit d'auto-apaisement **non visuel** + tension appliquée + psychoéducation | Immédiat |
| **2. Retraitement léger** | Matériel récent, charge modérée | Kit testé et efficace ≥ 3 fois |
| **3. Retraitement lourd** | Matériel d'enfance | **Critères chiffrés, figés d'avance** — jamais « quand je me sentirai prêt » |

**Critères de déverrouillage de la phase 3** : traitement stabilisé ≥ 6 semaines · shutdowns stables ou en baisse sur 4 semaines · charge professionnelle plafonnée · phase 2 réussie ≥ 3 fois · sujet évoqué avec le Dr Isorni.

**Garde-fous câblés quelle que soit la phase :** critères d'arrêt automatique · clôture obligatoire (jamais de fin sur du matériel ouvert, `matiere_ouverte: false`) · plafond de fréquence · escalade vers le protocole de crise · **aucune séance en période de shutdown**.

#### ⭐ Ce que l'aide-au-patient change à cet arbitrage — et ce qu'elle ne change pas *(13/08/2026)*

**Il faut être précis, parce que la tentation de lire « quelqu'un est là, donc on peut y aller » est forte.**

| Objection du 08/08/2026 | État après l'arrivée de l'aide-au-patient |
|---|---|
| **L'abréaction sans filet** — une reviviscence qui s'ouvre sans se refermer, chez quelqu'un qui, **en shutdown, ne peut plus demander d'aide** | ⭐ **Partiellement levée, et c'est le gain réel.** Une personne présente, qui a le déroulé et les critères d'arrêt sous les yeux, **est** le filet qui manquait. C'est exactement la règle §9.19 : la demande d'aide cesse de dépendre d'une parole qui peut tomber |
| **La fenêtre de surcharge documentée** — mariage, naissance, nuits fragmentées, missions, deuil actif | ❌ **Inchangée.** Un aide présent ne réduit pas la charge de vie |
| **La titration du traitement** — ouvrir du matériel lourd rend ininterprétable ce qui suit | ❌ **Inchangée.** C'est une question de calendrier pharmacologique, pas de présence |

> 🔴 **Conclusion, et elle ne se négocie pas : les critères de déverrouillage de la phase 3 restent entiers.** L'aide-au-patient **ne remplace pas un clinicien** — elle exécute un déroulé, elle ne conduit pas un retraitement et ne gère pas une abréaction. **Ce qu'elle rend possible aujourd'hui, c'est la phase 1** : la stabilisation non visuelle, l'acquisition supervisée de la tension appliquée, l'exposition accompagnée. **C'est déjà la moitié de ce que l'arbitrage K attendait d'un psychologue en présentiel** — l'autre moitié, le jugement clinique en situation, reste dehors.

#### 🔴 L'instrument est un geste, pas un logiciel *(14/08/2026)*

> ⭐ **Décision de Xavier : la stimulation bilatérale est jouée par l'aide-au-patient, dans une séance à deux.** Elle n'est **pas** un écran, pas une app, pas une surface. `seance-duo` la porte comme il porte tout le reste : Kokoro affiche la cadence et le décompte, **la personne fait le geste**.

**Trois choses que cette décision règle d'un coup, et il vaut la peine de les nommer séparément :**

| Ce que ça règle | Pourquoi |
|---|---|
| ⭐ **L'amplitude oculaire** — le seul argument clinique qui restait en faveur d'un écran desktop | **Une main à un mètre donne plus d'amplitude que n'importe quel écran**, desktop compris. Le problème n'est pas contourné, il est **mieux résolu** — et c'est la modalité historique de l'EMDR, pas un pis-aller |
| ⭐ **La modalité** | L'arbitrage « tactile ou auditive ? » ouvert par la suppression du web **tombe** : les trois modalités redeviennent disponibles, y compris la visuelle, **et aucune ne dépend d'un matériel à construire** |
| ⭐ **Le dernier livrable logiciel de l'Étape 6** | Il n'y a plus d'instrument à développer. **Ce qui reste à écrire est du contenu clinique** — un déroulé — et non du code : c'est [K6](#54-les-jalons) qui le porte, pas un jalon de plus |

🔴 **Ce que la décision ne change pas, et il faut le dire avant que quelqu'un le lise de travers : elle ne déverrouille rien.** Tenir l'instrument et conduire un retraitement sont deux actes différents. Les critères de la phase 3 ci-dessus **restent entiers**, les deux objections inchangées du 08/08 **restent inchangées**, et le geste appartient d'abord à la **phase 1** — stabilisation, ancrage —, là où il ne touche aucun matériel traumatique.

> ⚠️ **Une frontière que cette décision rend visible pour la première fois, et qui n'est pas une objection à la décision — elle porte sur la phase 3 seule.** Un retraitement demande au patient de **dire ce qui vient** entre deux séries. **La personne qui tient l'instrument entend donc le matériel.** ⭐ **Ce n'est plus un problème de compétence, c'est le contrôle C10** : l'aide-au-patient est censée lire des consignes, pas apprendre sur Xavier ce qu'il n'a pas décidé de partager. **En phase 1 et 2 la question ne se pose pas** — il n'y a pas de matériel lourd, et ce que Xavier dit, il choisit de le dire. 📌 **En phase 3, elle devient une condition de plus, à trancher avec le Dr Isorni au moment du déverrouillage** : soit le retraitement lourd se conduit avec un clinicien *(arbitrage K, la dette qui ne disparaît pas)*, soit Xavier décide en connaissance de cause ce qu'il accepte de faire entendre. **Ce n'est pas au dispositif de choisir à sa place, mais c'est à lui de poser la question avant, pas pendant.**

📌 **Un point dur de format, nommé maintenant pour ne pas être découvert à K6 :** `seance-duo` décrit une **séquence linéaire** de consignes chronométrées ([§8.3](#83-une-étape)). Une stimulation bilatérale est une **cadence répétée en séries** — vingt à trente allers-retours, une pause, on recommence. **Le format ne sait pas encore exprimer une répétition**, et l'exprimer en dépliant trente consignes identiques serait un contournement, pas une solution. À traiter comme une extension du §8.3, **en séance et sous supervision**, quand l'Étape 6 s'ouvre.

### 3.7 🔴 Le versant somatique — c'est une prescription médicale, pas de l'hygiène de vie

**Trois diagnostics somatiques constitués**, tous postérieurs à la conception initiale du dispositif :

| Diagnostic | Source primaire | État |
|---|---|---|
| 🔴 **SAOS sévère** — IAH 35/h, 61 micro-éveils/h, SP 7,2 %, MPJ 31/h | Polysomnographie Dr Roisman, 19/01/2026 | **Insuffisamment traité.** PPC prescrite, **utilisée de façon très irrégulière**. ⭐ **IAH résiduel < 6/h sous appareil** — l'efficacité est démontrée, seul le port manque |
| 🔴 **Stéato-hépatite non alcoolique (NASH), sans fibrose** | Anatomopathologie Dr Talhi, biopsie du 15/06/2026 | **Cible de perte de poids : 7-10 %, soit 7,7 à 11 kg → 99-102,3 kg** |
| **Obésité de classe II** — 1,77 m · 110 kg · IMC 35,1 | Déclaré, 08/08/2026 | Facteur commun des deux précédents |

> ⚠️ **Corrections portées ici, et elles annulent trois sections de la v1.2 de ce plan** : l'atteinte hépatique n'est **pas** une stéatose simple mais une **NASH** ; la cible n'est **pas** ≥ 5 % / 5,5 kg mais **7-10 % / 7,7-11 kg**, les seuils hauts valant en cas de stéatohépatite ; le SAOS n'est **pas** une hypothèse à dépister mais un **diagnostic sévère constitué**. *(Ce que disaient les anciennes sections reste lisible au [§11](#11-journal-des-décisions).)*

**Les boucles à connaître, parce qu'elles expliquent pourquoi rien ne bouge séparément :**

```
SAOS ──► privation de sommeil ──► dérèglement ghréline/leptine ──► prise de poids
  ▲                                                                      │
  └──────────────────────────────────────────────────────────────────────┘
                              │
                              └──► hypoxie intermittente ──► aggravation NASH
```

**Trois chantiers, un seul à la fois à partir du palier 1 :**

| # | Chantier | Fiche | État |
|---|---|---|---|
| **1** 🔴 | **Reprise de la PPC par désensibilisation** | `psy/docs/protocoles/ppc-desensibilisation.md` | **Chantier en cours** — palier 0 (logistique) non bouclé |
| 2 | **Alimentation à structure externe** | `psy/docs/protocoles/alimentation-structure-externe.md` | Écrit, démarre après |
| 3 | **Activité physique sans impact** | `psy/docs/protocoles/activite-physique-sans-impact.md` | Écrit, **feu vert médical préalable requis** |

**Principes de conception du programme somatique :** zéro streak · zéro jugement calorique · zéro compteur de régularité · structure externe plutôt que volonté · prévisibilité (mêmes horaires, rotation stable, aucune injonction à « varier »).

> ⭐ **Le passage de palier se compte dans le journal, il ne se demande pas.** « Tu te sens prêt ? » est une question intéroceptive posée à quelqu'un dont l'intéroception est déficitaire. « 3 jours sur 3 au bout du minuteur » est un fait vérifiable. **Si la donnée manque, on ne passe pas** — on ne comble jamais par un souvenir.

> 🔴 **Frontière de non-substitution du chantier PPC :** les réglages, le choix d'interface et l'origine de la fuite (masque ou bouche ?) appartiennent au prestataire et au Dr Roisman. Le dispositif conduit l'**exposition**, rien d'autre.

### 3.8 Le corpus

**Règle : toute affirmation clinique est adossée à une source citable.** `psy/docs/corpus/`.

| Priorité | Corpus | État |
|---|---|---|
| **1** | **Tension appliquée (Öst)** — complet | ✅ versé |
| **2** | **TCC alimentaire + intéroception** — ⭐ le corpus où l'avantage est le plus net (TSA × conduite alimentaire × déficit intéroceptif) | ⏸️ |
| **3** | **TCC de l'agoraphobie** — exposition graduée. ⭐ **Sert deux fois** : la désensibilisation à la PPC *est* une exposition graduée | ⏸️ |
| **4** | **Recommandations HAS** — TSA adulte, troubles anxieux | ⏸️ |
| — | **Échelles et instruments** | ✅ `corpus/echelles/` |
| — | DSM-5 intégral + extraits | ✅ `psy/docs/references/` |
| ⏸️ | EMDR — ⭐ **protocole de stimulation bilatérale conduite par un tiers** *(cadence, longueur des séries, critères d'arrêt)* | Reporté avec [§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc). ⚠️ **Le besoin de corpus change avec la décision du 14/08** : ce n'est plus un instrument à spécifier, c'est **un geste à scripter** |
| ❓ | ACT / défusion cognitive — **vérifier la compatibilité aphantasie** | À évaluer |

### 3.9 Les échelles

**Instruments versés** dans `psy/docs/corpus/echelles/` : VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9 complets ; BES partiel + grille comportementale de substitution ; MAIA (items non obtenus, grille de substitution).

**Deux règles non négociables, écrites dans chaque fiche :**

1. ⛔ **Un instrument ne se restitue jamais de mémoire.** Un item mal restitué produit un score faux — donc **faussement rassurant**, le pire résultat possible ici. C'est la règle qui a bloqué le BES.
2. ⭐ **Chez Xavier, un score élevé est informatif ; un score bas ne clôt aucune question.** L'alexithymie et le déficit intéroceptif sont précisément une difficulté à répondre à ce type de question.

> **R6 ne s'applique pas aux échelles validées, et il faut le dire explicitement.** Le journal quotidien reste strictement comportemental ; une échelle est un autre objet — une passation datée, avec un seuil publié, dont la validation psychométrique remplace l'ancre comportementale.

#### ⭐ Les échelles passent par Kokoro *(arbitrage de Xavier, 13/08/2026)*

**Elles se publient comme étapes `questionnaire`, rubrique `bilan`** ([§8.3](#83-une-étape)). Concernées : **VVIQ · TAS-20 · CAT-Q · GAD-7 · BES · MAIA**.

**Pourquoi c'est le bon support, et pas seulement un support commode :** une passation en conversation demande de tenir un fil, de suivre un rythme donné par quelqu'un d'autre, et de répondre à haute voix ou par écrit — **trois charges que le format fermé de Kokoro supprime**. Une question par écran, des choix fermés, « passer » écrit `null`, et **on peut s'arrêter au milieu sans le justifier**.

**Trois règles de publication, non négociables :**

1. 🔴 **Le PHQ-9 ne se publie jamais** — voir ci-dessous.
2. ⛔ **Les items se recopient depuis `psy/docs/corpus/echelles/`, jamais de mémoire.** La règle qui a bloqué le BES vaut à la publication comme à la passation.
3. **La cotation n'est pas dans Kokoro.** L'app renvoie les réponses item par item ; **le score se calcule en séance**, et son interprétation aussi. ⭐ **Kokoro n'affiche jamais un score, un seuil ni une interprétation** — ce serait une progression à l'écran, et un score mal lu est pire qu'un score absent.

> 🔴 **Deux câblages de sécurité.** (1) **L'item 9 du PHQ-9 interroge l'idéation suicidaire** : il se pose **en dernier**, toute réponse ≥ 1 **interrompt la passation** et déclenche le protocole de crise ; le fichier `mesures/` s'écrit après. (2) ⭐ **Le PHQ-9 ne se passe jamais dans Kokoro** — c'est le seul instrument porteur d'un déclencheur d'escalade, et Kokoro s'interdit tout numéro d'urgence par construction. Il se passe **en conversation**, avec `psy-bilan`.

> ⚠️ **Le PHQ-9 n'est pas interprétable comme une mesure de l'humeur chez Xavier aujourd'hui** : quatre de ses neuf items (sommeil, fatigue, concentration, ralentissement) sont **directement produits par un SAOS sévère insuffisamment traité** et peuvent à eux seuls porter le score en zone « modérée » sans dépression. **La réserve figure obligatoirement au brief.**

### 3.10 Les six skills de Claude Psy

Les skills vivent dans **`.claude/skills/psy-*/SKILL.md`** — Claude Code ne les découvre que là.

| Skill | Rôle | Écrit dans |
|---|---|---|
| `psy-seance` | ⭐ **Séance de fond hebdomadaire** — ouverture / une seule cible / clôture obligatoire. `npm run sync` en ouverture, supervision puis `npm run publish` en clôture. **Seule fenêtre d'écriture du programme** | `dossier/seances/` |
| `psy-journal` | Check-in quotidien — questions fermées, < 2 min, aucune saisie de texte obligatoire | `dossier/journal/` |
| `psy-crise` | **Triage de crise** — sécurité avant mécanisme, panique / vasovagal / shutdown, escalade 3114, voies sans parole | `dossier/crises/` |
| `psy-bilan` | Passation et cotation d'une échelle — items lus dans le corpus, **jamais de mémoire** | `dossier/mesures/` |
| `psy-brief-isorni` | Brief avant consultation — chiffres **calculés** depuis le journal, réserves obligatoires, **aucune proposition pharmacologique** | `dossier/briefs/` |
| `psy-hygiene` | Versant somatique (PPC, alimentation, activité) — **le palier se compte, il ne se demande pas** | `dossier/journal/` (lecture) |

**Invariants communs à tout skill du dispositif :**

- charger **`psy/outputs/dossier/profil.md` et `psy/outputs/dossier/etat.md` ensemble, avant d'agir** — jamais l'un sans l'autre ;
- **non-substitution** — aucun conseil de modification de traitement, jamais, même sous forme interrogative ;
- **protocole de crise câblé** — 3114, non contournable ;
- **aucune visualisation** ;
- **utilisable sans parler ni écrire** ;
- **zéro streak, zéro compteur de régularité, zéro reproche d'assiduité** ;
- **annoncer avant de faire.**

> 🔴 **`psy-crise` porte la seule exception au premier invariant.** En crise, la question de sécurité et la conduite s'appliquent **avant** la lecture du dossier. Lire deux fiches prend du temps, et le temps est exactement ce qui manque ; un contexte chargé n'a jamais aidé personne pendant les trente premières secondes. **L'exception est écrite, elle ne se déduit pas.**

> ⭐ **Ce que chaque rôle a apporté au-delà de sa fiche :** `psy-bilan` interdit de restituer un instrument de mémoire · `psy-hygiene` **compte** le critère de passage au lieu de le demander · `psy-brief-isorni` interdit de compter un jour sans check-in comme un zéro, et rappelle que **« ne faudrait-il pas envisager… ? » est une proposition déguisée** · `psy-journal` interdit le rattrapage rétroactif.

---

## 4. Claude Superviseur — le contrôle

> 🔴 **NORMATIF.** Ce paragraphe décide de ce qui atteint Xavier et le Dr Isorni.

### 4.1 Ce qu'il supervise

**🔴 Nouveau le 13/08/2026 : il supervise chaque contenu que Claude Psy produit.** Auparavant il passait à sa cadence (avant chaque brief, une fois par mois, sur demande) ; désormais **aucun contenu ne sort sans sa passe**.

| Contenu produit par Claude Psy | Supervision | Bloquante ? |
|---|---|---|
| **Le programme** (`companion/inputs/programme.json`) | À chaque publication | 🔴 **Oui — câblée dans `npm run publish`** |
| **La bibliothèque** (`companion/inputs/bibliotheque/`) | À chaque publication | 🔴 **Oui — même passe, même refus** |
| **Le brief** (`dossier/briefs/`) | Avant transmission | 🔴 **Oui — le brief ne part pas sans visa** |
| **Les protocoles** (`psy/docs/protocoles/`) | À l'écriture et à toute révision | Oui, avant qu'un protocole entre dans la bibliothèque |
| **Le corpus, les fiches d'échelle** | À l'écriture | Non — constat, correction séparée |
| **Le dossier** (séances, journal, mesures, crises) | Passe périodique | Non — **le superviseur n'écrit jamais dans `dossier/`** |
| **Ce document** | Passe périodique | Non |

### 4.2 Les dix contrôles

| # | Contrôle | Ce qu'il cherche |
|---|---|---|
| **C1** | **Source circulaire** | Une affirmation qui s'appuie sur un document écrit par Claude, présenté comme s'il faisait autorité. Les seules sources primaires sont listées au [§1.2](#12-claude-superviseur--la-contre-expertise). |
| **C2** | **Fait périmé propagé** | Un fait corrigé dans une version, resté vrai ailleurs. *(Exemple historique : « PPC non utilisée » après la v2.4 qui dit « très irrégulièrement ».)* |
| **C3** | **Invariant déclaré non câblé** | Une règle affirmée dans N documents et implémentée nulle part. *(Exemple : le protocole de crise « câblé en dur » qui n'existait que sous forme de huit lignes.)* |
| **C4** | **Dérive R6** | Une cotation de ressenti réintroduite. ⭐ **Le contrôle le plus rentable** : la première passe l'a trouvée **dans `psy-seance` lui-même**, mot pour mot, le jour où le dispositif se félicitait de l'avoir corrigée ailleurs. |
| **C5** | **Effet miroir** | Claude d'accord avec Xavier sur un point où il devrait objecter. |
| **C6** | **Autorité fabriquée** | Un chiffre, un seuil ou une recommandation cité sans source vérifiable. |
| **C7** | **Prolifération** | Des documents doctrinaux produits pendant que les actes ne le sont pas. **Ce n'est pas une critique de rythme, c'est un risque daté.** |
| **C8** | **Programme désynchronisé du dossier** | Le programme publié affirme un palier, une cible ou une démarche que `etat.md` ne porte pas. |
| **C9** | **Contenu non dérivé** | Un document de la bibliothèque qui est une **copie** d'un document clinique au lieu d'une **réécriture pour Xavier** : diagnostic, pronostic, nom de praticien, hypothèse non tranchée. |
| **C10** 🆕 | 🔴 **Contenu adressé à l'aide-au-patient** | Une consigne de `seance-duo` qui **apprend à Chourouk** quelque chose sur Xavier — diagnostic, score, hypothèse, compte rendu — ou qui **lui demande un jugement clinique** : « estime si ça va », « décide s'il faut continuer », « rassure-le ». Chercher aussi une séquence **sans signal d'arrêt rappelé** ou **sans critères d'arrêt accessibles**. ⭐ **Elle lit des consignes, pas un dossier — et elle n'est pas thérapeute.** |

### 4.3 🔴 La supervision est bloquante avant publication

**La règle.** Aucun programme, aucune bibliothèque, aucun brief ne sort sans une supervision qui porte **explicitement** sur la version qui sort.

**Le câblage, parce qu'un invariant non câblé est exactement ce que C3 traque :**

1. Le superviseur écrit `superviseur/outputs/AAAA-MM-JJ-programme-vN.md`, avec en frontmatter :

```yaml
---
date: 2026-08-13
porte_sur: programme
version: 4
verdict: publiable        # publiable | refuse
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---
```

2. `companion/inputs/programme.json` porte le champ **`supervision`**, obligatoire, qui nomme ce fichier.
3. **`npm run publish` refuse la publication** si : le champ manque · le fichier n'existe pas · sa `version` ne correspond pas à celle du programme · son `verdict` n'est pas `publiable`.

> ⭐ **Ce que ce câblage garantit vraiment : on ne peut pas publier une version supervisée hier.** Le numéro de version relie la passe à son objet. Republier après une correction impose une passe nouvelle — c'est le but.

**Un refus se corrige, il ne se contourne pas.** Il n'existe aucune option de forçage, et il ne doit jamais en exister une.

**Pour le brief** — il n'y a pas de script, donc la garde est dans le skill : `psy-brief-isorni` écrit `transmis: false` **et** `supervise: <fichier>` vide ; le brief ne se propose à la transmission qu'une fois ce champ rempli par une supervision de verdict `publiable`. **Xavier relit et décide ensuite** — la supervision ne remplace pas son arbitrage, elle le précède.

### 4.4 Ce que le superviseur ne fait jamais

1. **Écrire dans `psy/outputs/dossier/`.** Une supervision porte sur le dispositif, pas sur le patient. Sortie : `superviseur/outputs/`.
2. **Modifier ou publier le programme.** Il constate ; **la correction est un acte séparé**, fait par Claude Psy.
3. **Noter Xavier.** Il ne supervise pas le patient.
4. **Noter le processus sur le résultat.** Une hypothèse tenue pour acquise avant d'être mesurée reste une faute même si la mesure l'a ensuite confirmée. *(L'aphantasie a été tenue pour acquise deux jours avant que le VVIQ ne la confirme.)*

> ⚠️ **Deux caractéristiques sont aujourd'hui dans ce statut** : l'alexithymie (TAS-20 non passé) et le **déficit intéroceptif — brique de la règle centrale [§2.2](#22--la-règle-centrale--signal-interne-absent--structure-externe), la plus citée du dossier, et la seule sans instrument.**

---

## 5. Kokoro — le compagnon

### 5.1 La doctrine

**Kotlin natif + Jetpack Compose.** Cible : **Samsung Galaxy / One UI**. App personnelle et sideloadée — aucune contrainte Google Play. C'est le seul morceau du projet qui sort du TypeScript strict imposé par les règles projet : overlay système, foreground service, `showWhenLocked` et full-screen intent sont des APIs natives ; en cross-platform ce sont des ponts fragiles.

**Trois décisions de construction, prises et closes :**

1. ⭐ **Kokoro n'a pas commencé par le visage, mais par l'écran de crise.** Le mot-code convenu avec Chourouk n'avait aucun porteur : parole coupée, il fallait déverrouiller, ouvrir une messagerie, trouver un contact et écrire — quatre gestes, dont un impossible.
2. **App unique, multi-modules.** Trois apps seraient trois icônes à retrouver au pire moment.
3. **Aucune base de données.** L'app écrit des fichiers JSON — R1/R2/R3 l'imposent, une base dupliquerait la source de vérité.

**Et depuis le 12/08/2026, la décision qui change la nature du projet :**

> ⭐ **Kokoro n'apprend plus rien : il lit.** Claude Psy écrit la thérapie, Kokoro l'affiche et renvoie ce que Xavier a fait. **Ajouter une désensibilisation, un exercice, une démarche ou un questionnaire cesse d'être un acte de développement pour devenir un acte clinique**, fait en séance.

### 5.2 Ce que Kokoro contient

**Tout ce qui est accessible à Xavier**, groupé en quatre rubriques ([§8.3](#83-une-étape)) :

| Rubrique | Contenu | Exemple |
|---|---|---|
| **`crise`** | Ce qui doit être là au pire moment, accessible **depuis l'écran verrouillé** | Mot-code à Chourouk · tension appliquée guidée · phrase pour le soignant |
| **`therapie`** | Les protocoles en cours, les paliers, les exercices, les démarches — et ⭐ **les séances à deux** | Palier PPC du moment · repas servis une fois · bloc de tension appliquée · **ancrage corporel à deux** |
| **`bilan`** | Ses bilans et ses questionnaires — ⭐ **les échelles se passent ici** *(13/08/2026)* | Passation GAD-7, TAS-20, CAT-Q… · compte rendu de bilan **écrit par Claude Psy**, jamais calculé par l'app. 🔴 **Jamais le PHQ-9** |
| **`documentation`** | La bibliothèque — les fiches écrites pour être lues par lui | Les 13 symptômes de la panique · le kit vol · la fiche pour Chourouk |

> 🔴 **Un bilan dans Kokoro est un texte daté écrit en séance, jamais un graphique que l'app calcule.** Kokoro n'affiche aucune progression, aucun historique, aucun palier atteint — [§5.7](#57-ce-qui-nentrera-jamais-dans-kokoro). Ce qui satisfait la vision (« Xavier a ses bilans dans la main ») sans toucher à l'invariant : **c'est le psy qui interprète, pas l'interface.**

### 5.3 Le personnage

**Kokoro (心)** — le mot japonais qui désigne indissociablement le cœur et l'esprit, racine de 心理学 *(shinrigaku)*, « psychologie ». Deux raisons de le trouver juste : en japonais **cœur et esprit ne sont pas séparés**, ce qui convient à un dossier où l'angoisse passe par le ventre et où la satiété ne se sent pas ; et le nom désigne **l'objet du soin, pas une promesse de résultat** — aucun jour de mauvaise passe ne le fera sonner faux.

**Nommé, expressif, muet.** Il communique **par texte uniquement** : une voix qui surgit est une agression sensorielle, tandis que le texte se relit à froid, ne force pas le tempo, et **reste lisible en shutdown** — précisément quand le canal verbal est coupé.

⭐ **Le corps — retenu le 13/08/2026 : un petit robot kawaii en 2D, à trait minimal et ligne claire.** Tête carrée arrondie portant un **panneau-visage**, corps en capsule, bras et pieds flottants sans main ni doigt, une seule couleur d'accent sur une plaque de poitrine. **Deux jeux fermés, et ils sont fermés au sens strict — rien hors liste ne s'affiche** : **six expressions** (neutre · attentif · chaleureux · clignement · veille · de-côté) et **cinq postures** (repos · présent · montre · côte-à-côte · retrait), plus `allonge` pour le seul écran vasovagal. 🔴 **Aucun sourcil, jamais, et aucune bouche concave vers le bas** — le reproche n'est pas interdit par discipline, il est rendu **indessinable par la géométrie**. ⭐ **Le panneau s'éteint** : présence sans visage, donc rien à décoder, au moment précis où la charge est la plus forte. 🔴 **Kokoro peut désigner un élément de l'écran** *(arbitrage de Xavier, réserve conservée)* — jamais le lecteur, jamais au-dessus de l'épaule, jamais sans un texte qui dit ce qu'il montre, jamais pour réclamer une action. Spécification complète : [`companion/CORPS.md`](./companion/CORPS.md).

**État de repos — 99 % du temps : il respire, c'est tout.** Micro-animation lente et constante, sans information, zéro charge cognitive, zéro interprétation à faire. La charge mesurée reste **consultable en un tap**, jamais affichée d'elle-même, **et jamais avec une valence morale**.

### 5.4 Les jalons

| Jalon | Objet | État |
|---|---|---|
| **K0** | **Le poste de travail** — JDK 21, SDK Android, Gradle, APK installé sur le Galaxy S22 | ✅ **10/08/2026.** Android Studio écarté : outillage CLI seul |
| **K1** | ⚡ **Le full-screen intent** *(spike de faisabilité — le point le plus risqué du projet)* | ✅ **10/08/2026.** Téléphone verrouillé, écran éteint, Kokoro s'affiche par-dessus le verrouillage **sans son ni vibration**, sur trois passages. ⭐ La restriction d'Android 14 ne s'applique pas sur ce Galaxy S22 ; **l'écran de guidage reste dans l'app**, une mise à jour peut changer ça. 🔦 Un `WAKE_LOCK` s'est révélé nécessaire — `setTurnScreenOn` seul laisse l'Always On Display s'intercaler |
| **K2** | 🔴 **Le noyau de crise** | ✅ **10/08/2026, sans réserve.** Notification d'accès sur l'écran verrouillé → écran à deux boutons (**mot-code** · **tension appliquée**) → SMS composé et prêt, **`deviceLocked=1` de bout en bout, sans réseau data**. ⭐ **Le mot-code a été envoyé pour de vrai, téléphone verrouillé, et Chourouk a confirmé** — **à froid, en la prévenant** : la première fois qu'elle recevra ce mot ne sera pas la première fois qu'elle le reçoit |
| **K3** | **Tension appliquée guidée** | ✅ **construit le 10/08/2026** — les **quatre repères externes** de la fiche §2 dans leur ordre (porte · fauteuil · plateau-garrot-aiguille en cycles enchaînés · après-geste puis 5 min assis), **phrase pour le soignant** montrable, critères d'arrêt à un tap. ⭐ **On ne déclenche plus sur une sensation, on déclenche sur un fait extérieur.** ⏳ **Critère de fin ouvert : un bloc en salle d'attente réelle** |
| **K4** | **Check-in quotidien + transport** | ✅ **11/08/2026** — un vrai check-in saisi sur le téléphone est arrivé au dossier (`journal/2026-08-11.json`, `"source": "android"`). 11 champs en compteurs et choix fermés, énoncés **mot pour mot** du skill, **aucune saisie de texte**, format identique au gabarit. Écriture par **SAF** — **aucune permission au manifeste** |
| **K5** | ⭐ **Le programme et la bibliothèque** | 🔴 **en cours.** Kokoro lit `programme.json` + `bibliotheque/`, affiche par rubrique, écrit `reponses/`. ✅ Moitié PC écrite et vérifiée ; **Kokoro ne lit pas encore** |
| **K6** 🆕 | ⭐ **La séance à deux** *(13/08/2026)* | 🔜 **Le jalon qui ouvre les thérapies impossibles en solo.** Type `seance-duo` : déroulé chronométré tenu par l'**aide-au-patient**, **mode entraînement** obligatoire avant la première fois, **signal d'arrêt** rappelé en permanence, **critères d'arrêt à un tap**. **Critère de fin :** un entraînement joué en entier par Chourouk, puis une séance réelle menée à son terme ou **arrêtée sur le signal** — l'un et l'autre valent |
| **K7** | **La présence** *(Kokoro devient Kokoro)* | ⏸️ Foreground service + overlay · ⭐ **le corps — spécifié et retenu le 13/08/2026, reste à tracer en vectoriel** ([`design/CORPS.md`](./companion/CORPS.md)) · **écran de diagnostic One UI**. **Critère de fin :** l'overlay survit 72 h sans être tué par One UI |
| ~~—~~ | ~~**Interpellation**~~ | ❌ **supprimé le 12/08/2026** — incompatible avec « Kokoro ne vient jamais vers Xavier ». 📌 **Ce n'est pas une perte : Android rétrograde déjà un full-screen intent en bannière dès que le téléphone est en usage.** La décision ne fait qu'aligner l'intention sur ce que la plateforme garantissait |

**Critère de fin de K5 :** une étape publiée depuis le PC apparaît sur le téléphone, est faite par Xavier, et sa réponse revient dans `psy/outputs/dossier/` — valide au format, sans intervention manuelle.

#### ⭐ La règle de priorité, révisée le 13/08/2026

**Ce qu'elle disait depuis le 10/08** *(ancien plan applicatif §9-A)* : *le développement passe APRÈS le palier 0 de la PPC et le brief, jamais à leur place.* **Motif** : construire est gratifiant et mesurable, appeler un prestataire ne l'est pas. Au 13/08, K0 → K4 sont franchis **et `ppc_minutes` est toujours à 0** — la règle avait raison de se méfier.

**Ce que Xavier arbitre le 13/08/2026 :** *« On va avancer sur Kokoro. Plus vite on avance sur lui, plus vite je peux avancer dans tous mes sujets. »* — et *« ces points vont apparaître dans Kokoro, ce qui va me permettre de les traiter avec le temps »*.

> ⭐ **L'arbitrage tient debout, et il faut dire pourquoi : depuis K5, Kokoro ne concurrence plus le palier 0 — il le porte.** Les six démarches du palier 0 **sont déjà des étapes du programme**. Construire Kokoro n'est plus une alternative à les faire : c'est la structure externe qui les met sous la main. C'est la même bascule que pour les protocoles.

> 🔴 **Ce qui ne change pas, et qui est la partie utile de l'ancienne règle :**
> - **le brief garde sa date** — écrit au week-end du **29-30/08**, pour la consultation du **03/09**, quoi qu'il arrive côté code ;
> - **les démarches du palier 0 sont des appels et des emails** — Kokoro les affiche, il ne les passe pas ;
> - ⭐ **`ppc_minutes` reste l'indicateur qui tranche.** Tant qu'il est à 0, le contrôle **C7** (prolifération) reste ouvert, et **chaque compte rendu de jalon continue de le reporter, à dessein.**

### 5.5 Points durs Android — à traiter, pas à découvrir

| Point | Réalité | Traitement |
|---|---|---|
| **Full-screen intent** | ✅ Levé le 10/08/2026 sur le Galaxy S22 : permission accordée à l'installation, `canUseFullScreenIntent()` vrai sans manipulation | Écran de guidage conservé — une mise à jour système peut changer ce comportement |
| **Canal de notification** | ⚠️ **Un canal est immuable une fois créé** | Identifiant versionné (`kokoro_alerte_v1`) ; toute modification de réglage impose `_v2` |
| **Foreground service** | Depuis Android 14, un `foregroundServiceType` est obligatoire | `specialUse` avec justification. Aucune review : l'app est sideloadée |
| **Notification persistante** | Une notification de service peut sonner | Canal `IMPORTANCE_LOW`, **aucun son, aucune vibration** — règle, pas préférence |
| **Accès aux fichiers** | Le stockage cloisonné empêche d'écrire librement | ✅ **SAF, URI d'arbre persistant.** `MANAGE_EXTERNAL_STORAGE` écarté — il ouvre tout le stockage pour un seul dossier. **Aucune permission au manifeste**, transport interchangeable |
| **Un dossier Drive n'est pas un système de fichiers** | 🔴 Drive **accepte deux fichiers du même nom** et ne le signale pas | Garde **double** : jeton local de date + interrogation du dossier. Côté PC, l'ingestion refuse d'écraser |
| **One UI tue les services** | Deux réglages batterie obligatoires | *Batterie → Limites d'utilisation en arrière-plan → Applications jamais mises en veille* + désactivation de l'optimisation. **Écran de diagnostic dans l'app** — une mise à jour les réinitialise |

### 5.6 Les invariants, traduits en règles vérifiables

Une contrainte de conception qui reste une phrase se perd à l'implémentation. **Checklist de revue de chaque écran :**

| Invariant | Règle de code vérifiable |
|---|---|
| Jamais de son | L'app ne déclare **aucune** permission audio ; tout canal est `IMPORTANCE_LOW` ou moins |
| Jamais de vibration non sollicitée | Aucun appel `Vibrator` hors d'une action déclenchée par Xavier dans la seconde |
| Transitions lentes et continues | Toute animation d'expression ≥ **800 ms**, easing continu. Aucune apparition instantanée |
| Utilisable sans parler ni écrire | **Tout champ obligatoire est un nombre ou un choix fermé** (R5). Le texte libre est facultatif et jamais bloquant |
| Aucune visualisation | Aucun texte ne contient « imagine », « visualise », « représente-toi ». **Vérifié par test sur les chaînes de l'app et sur le contenu publié** |
| Zéro streak | Aucun compteur de régularité, de série, de pourcentage d'objectif ni de moyenne mobile affichée |
| Aucune cotation de ressenti (R6) | Aucun libellé « note ton X sur 10 ». Toute question a une ancre comportementale |
| Aucun numéro d'urgence | **Vérifié par test**, sur les sources **et** sur le contenu publié |
| Déclenchement sur repère externe | Aucun texte ne dit « aux premiers signes », « quand tu sens », « si tu sens » |
| L'apparence ne change jamais sans annonce | Tout changement visuel entre deux versions est annoncé **avant** installation |

> 🔴 **Le point dur ouvert par K5, et il faut le nommer : les garde-fous câblés en tests devenaient contournables par du contenu, en silence.** Les tests de Kokoro vérifiaient les textes de l'app ; à partir de K5 ces textes ne sont plus dans l'app.
>
> **Double garde, et les deux réactions diffèrent volontairement :**
> - **`npm run publish` refuse la publication entière.** Sur le PC on peut corriger — donc on corrige, on ne publie pas à moitié.
> - **Kokoro écarte la seule étape fautive** et affiche le reste. Sur le téléphone on ne peut pas corriger, et perdre tout le programme pour une ligne serait pire.

### 5.7 Ce qui n'entrera jamais dans Kokoro

1. Un conseil, une suggestion ou un rappel touchant au **traitement** — même sous forme de question. Ça part au brief.
2. Un **streak**, un compteur de régularité, un pourcentage d'objectif, un « ça fait 4 jours », un historique, une progression calculée.
3. Un **son** ou une **vibration** non demandés.
4. Une consigne de **visualisation**, y compris dans un texte d'aide.
5. Une **expression de tristesse, de déception ou de reproche** sur le visage.
6. Un **service tiers** : pas de cloud, pas d'analytics, pas de crash reporting, pas de police distante.
7. 🔴 **Une notification, un rappel, une relance.** Seule exception : la notification d'accès crise sur l'écran verrouillé — **une porte, pas un rappel**.
8. 🔴 **Un numéro d'urgence, sous quelque forme que ce soit** — appel, SMS, lien, texte d'aide. ⭐ **Y compris le 3114** : il appartient à une conduite d'escalade, pas à une interface. Un écran qui l'affiche en permanence le transforme en décor — et c'est précisément ce qui angoissait sans jamais servir.
9. 🔴 **Le PHQ-9** — seul instrument porteur d'un déclencheur d'escalade ([§3.9](#39-les-échelles)).

### 5.8 Il n'y a qu'une surface *(14/08/2026)*

> 🔴 **La surface web desktop est supprimée. Kokoro est la seule surface que Xavier touche.** Ce n'est pas un report : `psy/web/` est supprimé du dépôt, et **le critère de répartition entre surfaces disparaît avec la deuxième surface**.

**Le motif est celui du 13/08, poussé jusqu'au bout.** Un outil qui vit sur le PC demande d'aller le chercher, donc de se souvenir qu'il existe, donc d'avoir le signal qui dit « c'est le moment » — c'est exactement ce que la règle centrale ([§2.2](#22--la-règle-centrale--signal-interne-absent--structure-externe)) interdit de supposer. ⭐ **Trois des quatre livrables desktop avaient déjà migré vers Kokoro avant d'avoir été écrits** : la décision ne fait qu'enregistrer un déplacement qui avait eu lieu. **Le quatrième — la stimulation bilatérale — a suivi le jour même**, et par un chemin que personne n'avait vu venir : il ne va nulle part, **il cesse d'être un logiciel**.

| Livrable desktop prévu | Ce qu'il devient |
|---|---|
| ⭐ **Schémas Zod du dossier et du programme** | ➡️ **`scripts/`, et ça reste à faire.** Ce n'a jamais été un livrable d'interface : c'est le **contrat de données**, et ses consommateurs — `programme-publish` et `contenu-sync` — sont déjà écrits et le valident aujourd'hui à la main |
| Tableau de bord d'évolution | ❌ **Supprimé, et il n'avait pas de destinataire.** Kokoro n'affiche jamais d'historique ni de progression ([§5.7](#57-ce-qui-nentrera-jamais-dans-kokoro) n° 2), et la lecture longitudinale est le travail de la séance — un bilan est **un texte daté écrit en séance**, jamais une courbe |
| Passation d'échelles longues | ➡️ **Déjà dans Kokoro** depuis le 13/08 — rubrique `bilan`, **PHQ-9 excepté**. La cotation reste en séance : l'écran n'affiche jamais un score |
| Stimulation bilatérale | ➡️ ⭐ **Jouée par l'aide-au-patient, en séance à deux** *(14/08/2026)* — ce n'est plus un livrable logiciel du tout. [§3.6](#-linstrument-est-un-geste-pas-un-logiciel-14082026) |

> ⭐ **La suppression ne coûte rien, et ce n'était pas acquis d'avance.** Le seul argument proprement **clinique** en faveur du desktop tenait à la **stimulation bilatérale visuelle** : elle exige une amplitude de mouvement oculaire qu'un écran de téléphone ne donne pas. ✅ **Décision de Xavier le 14/08/2026 : l'EMDR se joue via l'aidant, en séance à deux** — et **une main à un mètre donne plus d'amplitude que n'importe quel écran**, desktop compris. **La réserve n'est pas reportée, elle est levée** ; l'objection portait sur le mauvais axe — on cherchait quel écran, la réponse était qu'il n'en faut aucun.
>
> 🔴 **Ce que ça ne déverrouille pas :** tenir l'instrument et conduire un retraitement sont deux actes différents. **Les critères de la phase 3 restent entiers** ([§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc)), et la frontière que la décision rend visible — **en phase 3, la personne qui tient l'instrument entend le matériel** — est traitée là-bas, pas ici.

---

## 6. Le contenu — Google Drive

> 🔴 **NORMATIF.** ⭐ **Version du 13/08/2026 — c'est un arbitrage neuf, tracé comme les précédents.**

### 6.1 La décision

**Deux mécanismes, deux rôles qui ne se confondent pas :**

| Mécanisme | Rôle | Périmètre |
|---|---|---|
| **Dépôt git privé** `github.com/XavierBoubert/psy` | ⭐ **Historique, archive, source de vérité.** Traçabilité clinique gratuite : qui a écrit quoi, quand, avec retour arrière. | **Tout le dépôt.** Y compris tout ce qui transite par Drive |
| **Google Drive** | ⭐ **Le contenu vivant, dans les deux sens.** Ce dont Kokoro a besoin, et ce que Kokoro produit et dont les Claude ont besoin. | [§6.2](#62-le-périmètre) |

**Pourquoi pas git comme canal Android :** il n'existe pas de client git confortable sur Android, et une app qui doit écrire en un geste ne peut pas dépendre d'un `commit`/`push`. Drive pose des fichiers ; c'est ce qu'il faut.

**Pourquoi pas Drive comme archive :** il synchronise, il n'archive pas. Une erreur d'écriture se propage. **Le git fournit le filet.**

> 🔴 **Règle absolue : tout ce qui passe par Drive est versé au dépôt et versionné.** Le Drive n'est jamais la seule copie de quoi que ce soit. `npm run sync` fait la remontée, `npm run publish` fait la descente, et les deux côtés existent dans le dépôt.
>
> ⚠️ **Ne jamais faire pointer Drive sur `c:\p\psy`.** Le dépôt git n'est synchronisé par aucun service. Le dossier Drive est **hors dépôt**.

### 6.2 Le périmètre

**Le critère, et il n'est pas « clinique » — il est fonctionnel :**

> **Transite ce dont Kokoro a besoin, et ce que Kokoro produit et dont les Claude ont besoin. Rien d'autre.**

| Sens | Ce qui transite | Auteur unique | Versé dans |
|---|---|---|---|
| **PC → Kokoro** | `programme.json` — la thérapie du moment | Claude Psy | *(source : `companion/inputs/`)* |
| **PC → Kokoro** | `bibliotheque/*.md` — la documentation accessible à Xavier | Claude Psy | *(source : `companion/inputs/bibliotheque/`)* |
| **Kokoro → PC** | `journal/AAAA-MM-JJ.json` — les check-ins | Kokoro | `companion/outputs/journal/` |
| **Kokoro → PC** | `reponses/AAAA-MM-JJ-HHMM-<id>.json` — ce qui a été fait | Kokoro | `companion/outputs/reponses/` |

**Ce qui ne transite jamais** — et la liste est fermée : `profil.md` · `etat.md` · `seances/` · `crises/` · `mesures/` · `briefs/` · `gabarits/` · `superviseur/outputs/` · `psy/docs/corpus/` · `psy/docs/protocoles/` *(à l'état brut)* · `psy/docs/references/` · `patient/ressources/` · `aidant/ressources/` · le code · `.git`.

> ⭐ **La distinction qui tient tout : le contenu publié est *dérivé*, jamais *extrait*.** Le programme porte **ce qu'il y a à faire**, jamais ce qui a été constaté, mesuré ou diagnostiqué. La bibliothèque porte **une fiche réécrite pour Xavier**, jamais le protocole clinique brut. C'est le contrôle **C9** du superviseur ([§4.2](#42-les-dix-contrôles)).

> ✅ **Aucun fichier n'a deux auteurs.** `programme.json` et `bibliotheque/` sont écrits par le PC seul ; `journal/` et `reponses/` par Kokoro seul. **C'est une condition de l'arbitrage, pas une observation** — c'est ce qui rend le risque de conflit tolérable.

### 6.3 Les arbitrages assumés — à dire franchement

#### GitHub *(09/08/2026)*

Le plan d'origine posait : « données locales, repo privé. **Rien ne part vers un tiers hors appels à Claude.** » **Versionner le dossier sur GitHub contredit partiellement cette phrase** : GitHub est un tiers, et il héberge des données de santé.

Arbitrage de Xavier, en connaissance de cause : la traçabilité clinique et la sauvegarde hors-machine valent le risque résiduel. `patient/ressources/` — l'intégralité des documents médicaux réels — y était de toute façon versionné depuis l'origine.

**Conditions :** dépôt **privé**, à revérifier périodiquement · **2FA** + clé SSH · **aucun fork, aucun collaborateur, aucune GitHub Action** ayant accès au contenu.

#### Google Drive *(11/08/2026, étendu le 12/08 puis le 13/08/2026)*

**Xavier a écarté Syncthing au profit de Google Drive après objection argumentée du dispositif et maintien de sa décision.** L'objection est conservée ici parce qu'**un arbitrage dont on a effacé le contre-argument n'est plus un arbitrage**.

| Objection opposée | Portée après décision |
|---|---|
| Drive n'expose pas de racine sélectionnable en arbre de documents (SAF) | ✅ **Fausse** — vérifié sur l'appareil le 11/08 : Drive *est* sélectionnable. Le contournement prévu n'a pas servi |
| Drive pour desktop ne doit jamais voir `.git` | ✅ Traité par construction : dossier Drive **hors dépôt**, scripts pour la jonction |
| Les conflits Drive sont silencieux (« fichier (1) ») | 🔴 **Pire que prévu, et non résolu** : Drive accepte **deux fichiers du même nom** sans rien dire. Mitigation : auteur unique par fichier + garde double côté app + refus d'écraser côté PC |
| Drive n'apporte pas la sauvegarde | ✅ Acté : Drive est un **transport**, GitHub est l'archive |
| Un tiers de plus voit des données de santé, sur un compte grand public non HDS | ⚠️ **Réel, et il s'élargit à chaque extension** — voir ci-dessous |

**Ce que Drive apporte, et qui a motivé la décision :** Syncthing exige que les deux appareils soient allumés en même temps, et son installation Android passe par F-Droid. Drive n'a ni l'une ni l'autre de ces frictions.

**🔴 Ce que le périmètre du 13/08/2026 élargit, dit franchement :**

- Le 11/08, seul `journal/` transitait — des **compteurs de comportements**, sans diagnostic ni nom de praticien.
- Le 12/08, `programme.json` s'est ajouté — des **libellés cliniques** : nom des démarches, consignes d'exercice, praticiens sollicités.
- **Le 13/08, la bibliothèque s'ajoute** — des **fiches thérapeutiques entières**, lisibles par un tiers qui accéderait au compte : quels protocoles Xavier suit, sur quelles cibles, avec quels paliers. C'est **plus** que le programme, et **moins** qu'un dossier : ni compte rendu, ni mesure, ni diagnostic, ni idéation ne transitent.
- **Xavier a arbitré en connaissance de cause : « c'est assumé ».** Le contre-argument reste écrit ici, il n'est pas levé — il est accepté.

**Conditions attachées :**

- Le dossier Drive **n'est jamais partagé** : aucun lien, aucun destinataire, aucun « partagé avec moi ».
- **2FA obligatoire** sur le compte Google.
- **Aucune application tierce** autorisée sur ce Drive.
- Le dossier Drive est un **transit** : les fichiers y arrivent, sont versés au dépôt, **et le dépôt fait foi**.
- ⚠️ **Aucune extension du périmètre sans nouvel arbitrage tracé ici.** Le jour où une surface voudra faire transiter `mesures/`, `seances/` ou `crises/`, c'est une décision nouvelle — pas une continuation de celle-ci.

#### La règle qui n'a pas changé

L'assouplissement porte sur **GitHub et Google Drive, et rien d'autre**. Hors de ces deux-là et hors des appels à Claude, **aucune donnée ne part vers un tiers** : pas de cloud santé, pas de service d'analyse externe, pas de télémétrie, pas d'hébergeur de sauvegarde. **Toute proposition d'ajouter un service tiers est refusée par défaut** et doit faire l'objet d'un arbitrage explicite, tracé ici.

**Porte de sortie**, si l'un des arbitrages est révisé : chiffrement au repos par `git-crypt` ou `age` sur `psy/outputs/dossier/` et `patient/ressources/`. Contrepartie : Claude Code ne lit plus rien sans déverrouillage, et chaque surface doit gérer la clé.

### 6.4 L'arborescence du transit

```
H:\Mon Drive\kokoro\               ← hors dépôt, jamais partagé
  programme.json                   ← PC écrit,    Kokoro lit
  bibliotheque/
    <id>.md                        ← PC écrit,    Kokoro lit
  journal/
    AAAA-MM-JJ.json                ← Kokoro écrit, PC lit
  reponses/
    AAAA-MM-JJ-HHMM-<id>.json      ← Kokoro écrit, PC lit
```

### 6.5 Les deux scripts

| Commande | Sens | Ce qu'elle fait |
|---|---|---|
| **`npm run sync`** | Drive → dépôt | Verse `journal/` et `reponses/` dans `psy/outputs/dossier/`. **N'écrase jamais un fichier existant** (R2), valide chaque fichier au [§7](#7-le-dossier--format), et signale tout nom hors convention |
| **`npm run publish`** | dépôt → Drive | Valide le programme et la bibliothèque au [§8](#8-le-programme--format), **vérifie la supervision** ([§4.3](#43--la-supervision-est-bloquante-avant-publication)), et **refuse la publication entière** au moindre manquement |

Formes longues : `npm run contenu-sync -- <transit>` · `npm run programme-publish -- <transit>`.

> ⚠️ **Fichiers en double.** Drive ne marque pas les conflits : il crée `2026-08-11 (1).json`. **Un fichier de ce nom ne se supprime jamais sans être lu — c'est une donnée clinique.** Procédure : lire les deux versions, fusionner à la main dans le dépôt, committer la fusion.

> 🔴 **`npm run publish` ne se lance qu'à la clôture d'une séance** ([§3.3](#33-les-trois-rythmes)). **Un refus se corrige, il ne se contourne pas.**

### 6.6 État des conditions — vérifié le 13/08/2026

- [x] **Le dépôt GitHub est privé.** *(Vérifié par Xavier, 13/08/2026.)*
- [x] **La 2FA est active** — sur GitHub **et** sur le compte Google. *(Idem.)*
- [x] **Le dossier Drive n'est partagé avec personne.** *(Idem.)*
- [x] 🔴 **Compte Google du transit — arbitrage E clos le 13/08/2026 : on reste sur le compte professionnel `xavier@allons-y.io`.** ⭐ **Motif de Xavier, et il est valable : il est en micro-entreprise, ce compte lui permet de passer la note en frais de société.** *(Le dispositif recommandait un compte personnel pour isoler des données de santé d'un espace lié à l'activité ; **l'objection reste écrite, elle est acceptée, pas levée.** Elle redeviendra un sujet si la structure change de forme, gagne un associé, un comptable avec accès, ou un administrateur Workspace.)*
- [x] **Le dossier de transit s'appelle `kokoro`** *(13/08/2026)* — il ne portait plus que du journal quand il s'appelait `psy-journal`. **Un nom qui ment finit par tromper quelqu'un.**
- [ ] **Kokoro lit `programme.json` et `bibliotheque/`, et écrit `reponses/`** — jalon K5

> **Sauvegarde froide hors-ligne : sujet clos le 13/08/2026, à la demande de Xavier.** Il n'est pas rouvert.

---

## 7. Le dossier — format

> 🔴 **NORMATIF.** Ce paragraphe définit le format de `psy/outputs/dossier/`. Claude Code et Kokoro le lisent et l'écrivent. **Aucune surface n'a le droit d'inventer un format.**

**Pourquoi il existe :** le dossier est la **source de vérité unique**. C'est lui — pas les conversations — qui rend le suivi longitudinal possible. Si son format dérive, la mémoire longitudinale se dégrade **en silence** : les tendances deviennent incalculables, les comparaisons faussées, et l'avantage n° 2 du dispositif disparaît.

### 7.1 Les six règles invariables

| # | Règle | Raison |
|---|---|---|
| **R1** | **Un fichier par événement.** Jamais de fichier partagé auquel on ajoute des lignes. | Deux appareils qui appendent au même fichier produisent un conflit. Un fichier par événement le rend **structurellement impossible**. ⚠️ **Renforcée par le transport retenu** : Google Drive accepte deux fichiers du même nom **sans rien signaler**. |
| **R2** | **Append-only.** Un enregistrement daté n'est jamais réécrit ni supprimé. Une correction est un **ajout**. | C'est un dossier clinique. L'historique doit rester lisible, **y compris ce qui s'est révélé faux**. Le git log est l'audit. |
| **R3** | **Le format suit l'auteur.** Ce que **Claude** écrit → Markdown + frontmatter YAML. Ce qu'une **application** écrit → JSON. | Chacun son format fiable. Pas de conversion, pas de format bâtard. |
| **R4** | **Nommage `AAAA-MM-JJ` en préfixe, toujours.** | Le tri lexicographique **est** le tri chronologique. Aucun index à maintenir. |
| **R5** | **Aucun champ obligatoire ne demande d'écrire ou de parler.** Tout ce qui est requis est un nombre ou un choix fermé. | Contrainte shutdown : le dossier doit rester alimentable quand le canal verbal est coupé. |
| **R6** | ⭐ **On cote des comportements observables, pas des ressentis.** | Alexithymie probable + déficit intéroceptif. « Note ton anxiété sur 10 » demande d'utiliser une fonction perceptive déficitaire — même erreur que « écoute ta satiété ». Application directe de [§2.2](#22--la-règle-centrale--signal-interne-absent--structure-externe). |

> **R6 est la règle la plus facile à enfreindre sans s'en apercevoir.** À chaque champ ajouté : *« Xavier peut-il répondre en observant ce qu'il a fait, ou doit-il introspecter ce qu'il a ressenti ? »* Si c'est la seconde, le champ est mal conçu.

### 7.2 Arborescence

⭐ **Le dossier est réparti sur deux rôles depuis la réorganisation du 14/08/2026, et la ligne de partage est celle de l'auteur** *(R3, et « aucun fichier n'a deux auteurs », [§6.2](#62-le-périmètre))* : **ce que Claude Psy écrit vit chez le psy, ce que Kokoro écrit vit chez le compagnon.** C'est un déplacement de fichiers, **pas** un changement de format : les §7.3 à §7.8 sont inchangés, et le dossier reste **une seule mémoire longitudinale**, qui se charge en entier.

```
psy/outputs/dossier/                     ← écrit par Claude Psy
  profil.md        fiche condensée — contexte PERMANENT, rechargé à chaque séance
  etat.md          état COURANT — chantier en cours, traitement, questions ouvertes
  seances/         comptes-rendus de séance ... MD   — AAAA-MM-JJ-seance.md
  crises/          épisodes de crise .......... JSON — AAAA-MM-JJ-HHMM-<type>.json
  mesures/         échelles cotées ............ JSON — AAAA-MM-JJ-<echelle>.json
  briefs/          briefs Dr Isorni ........... MD   — AAAA-MM-JJ-isorni.md

companion/outputs/                       ← écrit par Kokoro, versé par `npm run sync`
  journal/         check-ins quotidiens ....... JSON — AAAA-MM-JJ.json
  reponses/        ce que Xavier a fait ....... JSON — AAAA-MM-JJ-HHMM-<id>.json

psy/docs/gabarits/                       ← ni l'un ni l'autre : des modèles vierges
                   à copier, jamais à remplir sur place
```

> ⚠️ **`companion/outputs/` est du dossier clinique, malgré son emplacement.** Les six règles ci-dessus s'y appliquent entières — **R1 et R2 en particulier**. Un fichier n'y est jamais écrasé ni supprimé, et `npm run sync` refuse de le faire.

**`profil.md` et `etat.md` sont les deux seules exceptions à R2** : ce sont des documents vivants, réécrits. Leur historique est tenu par git, et chacun porte un journal de révisions en pied de page.

**La distinction profil / état — à ne jamais confondre :**

| | `profil.md` | `etat.md` |
|---|---|---|
| Contenu | Ce qui ne change pas | Ce qui change |
| Exemples | TSA niveau 1, aphantasie, les 3 mécanismes de crise, ce qu'on ne dit jamais | Traitement en cours, poids, chantier ouvert, questions en attente |
| Révision | Rare (nouveau diagnostic, nouvelle contrainte) | Hebdomadaire (clôture de séance) |

Les deux se chargent **ensemble**, jamais l'un sans l'autre : le profil dit *qui est Xavier*, l'état dit *où on en est*.

### 7.3 `journal/AAAA-MM-JJ.json` — check-in quotidien

Un fichier par jour. **Cible : moins de 2 minutes, zéro saisie de texte obligatoire.**

```json
{
  "date": "2026-08-13",
  "source": "android",
  "noyau": {
    "shutdowns": 0,
    "exposition_sociale": 1,
    "retrait_sensoriel": 0,
    "renoncements": 0,
    "activites_investies": 2,
    "sommeil_heures": 6.5,
    "missions_actives": 3
  },
  "campagne": {},
  "notes": null
}
```

| Champ | Type | Question fermée | Justification clinique |
|---|---|---|---|
| `shutdowns` | entier ≥ 0 | « Combien de fois as-tu perdu la parole ou été incapable de traiter une demande ? » | ⭐ **Indicateur n° 1** — « la fréquence des pertes de parole est le meilleur indicateur de suivi » du burnout autistique (§10.5) |
| `exposition_sociale` | 0-3 | « Combien d'heures d'interaction sociale non choisie ? » 0 = aucune · 1 = < 1 h · 2 = 1-3 h · 3 = > 3 h | Proxy comportemental du **camouflage**, qui prédit anxiété, dépression et épuisement indépendamment des traits autistiques. Mesure l'exposition, pas l'effort ressenti — R6 |
| `retrait_sensoriel` | entier ≥ 0 | « Combien de fois as-tu dû te retirer, mettre un casque, baisser la lumière, quitter une pièce ? » | Charge sensorielle. Comptage d'actions, pas d'inconfort — R6 |
| `renoncements` | entier ≥ 0 | « À combien de choses as-tu renoncé à cause de l'angoisse ? » | Ancre comportementale de l'anxiété : l'**évitement** est le critère D de l'agoraphobie, et il s'observe |
| `activites_investies` | 0-3 | « Combien d'activités as-tu pu investir hors obligations ? » | Ancre comportementale de l'humeur. La **clinophilie** est le marqueur dépressif documenté chez Xavier — on mesure ce marqueur-là, pas « ton moral sur 10 » |
| `sommeil_heures` | nombre ≥ 0 | « Combien d'heures de sommeil, réveils compris ? » | Critère C du TAG — et référence pour juger l'effet de la PPC |
| `missions_actives` | entier ≥ 0 | « Combien de missions professionnelles en cours ? » | **Seule variable d'ajustement disponible** — pas la famille, pas le sommeil |

**Ce que le noyau ne contient délibérément pas :** aucun champ « anxiété /10 », « humeur /10 », « fatigue /10 », « niveau de stress ». Tous violeraient R6.

**Campagne** — champs temporaires liés au chantier ouvert, et seulement lui. Quand le chantier se ferme, ses champs sortent : **le journal ne grossit jamais indéfiniment.** Les champs actifs sont déclarés dans `etat.md`.

```json
"campagne": { "ppc_minutes": 0, "repas_servis_une_fois": 3, "activite_minutes": 0, "poids_kg": null }
```

| Champ | Type | Justification |
|---|---|---|
| `ppc_minutes` | entier ≥ 0 | ⭐ Donnée **objective, issue du télésuivi de l'appareil**, jamais d'une auto-évaluation — exactement l'instrument qu'appelle un déficit intéroceptif |
| `repas_servis_une_fois` | entier 0-4 | On compte les repas **conformes à la structure**, jamais les calories |
| `activite_minutes` | entier ≥ 0 | Prescription médicale. Sans impact, domicile |
| `poids_kg` | nombre \| null | Hebdomadaire. `null` les autres jours. Cible 99-102,3 kg |

**Règles de remplissage :**

- ⭐ **Un jour sans check-in est un jour sans fichier.** Aucun rattrapage rétroactif, aucune relance, aucune trace de manquement. **L'absence de fichier n'est pas une donnée négative — elle n'est pas une donnée du tout**, et un calcul de médiane ne doit jamais la compter comme un zéro.
- Un champ auquel Xavier ne répond pas est écrit `null`. **`null` ≠ `0`.**
- `notes` est **toujours** facultatif et **toujours** en dernier.
- `source` : `"claude-code"` | `"android"`. ⭐ **Deux valeurs, et il n'y en aura pas d'autre** — la surface web est supprimée depuis le 14/08/2026 *([§5.8](#58-il-ny-a-quune-surface-14082026))*.
- ⚠️ **Une seule surface écrit le journal un jour donné.** Vérifier avant d'écrire ; jamais de rattrapage.

> ⚠️ **Interdit dans le journal, en toute circonstance :** compteur de régularité, série, pourcentage d'objectif, moyenne mobile affichée à Xavier, rappel de manquement, jugement calorique. **Un compteur est une charge.**

### 7.4 `reponses/AAAA-MM-JJ-HHMM-<id>.json` — ce que Xavier a fait

Écrit par Kokoro, un fichier par étape faite. Format défini au [§8.5](#85-ce-que-kokoro-renvoie).

> ⭐ **`arrete_avant_la_fin` n'est pas un échec et ne se commente nulle part.** Une étape non faite ne produit aucun fichier — **et ce n'est pas une donnée**.

### 7.5 `crises/AAAA-MM-JJ-HHMM-<type>.json`

> 🔴 **La règle la plus importante du dossier : les trois mécanismes ne se confondent jamais.** Le champ `type` n'a pas de valeur par défaut et ne peut pas être laissé vide.

```json
{
  "horodatage": "2026-08-13T14:32:00+02:00",
  "type": "vasovagal",
  "contexte": "medical",
  "declencheur": "pose de cathéter",
  "duree_minutes": 8,
  "parade_utilisee": "tension_appliquee",
  "parade_efficace": true,
  "perte_de_connaissance": false,
  "source": "claude-code",
  "notes": null
}
```

| Champ | Valeurs | Notes |
|---|---|---|
| `type` | `panique` \| `vasovagal` \| `shutdown` \| `indetermine` | **Obligatoire.** `indetermine` est légitime — mieux vaut « je ne sais pas » qu'un type inventé. Le tri se fait après, à froid |
| `contexte` | `transport` \| `foule` \| `lieu_clos` \| `medical` \| `social` \| `conflit` \| `domicile` \| `autre` | |
| `parade_utilisee` | `tension_appliquee` \| `respiration` \| `retrait_sensoriel` \| `mot_code` \| `sortie_situation` \| `aucune` | `tension_appliquee` **uniquement** pour le vasovagal. `mot_code` uniquement pour le shutdown |
| `perte_de_connaissance` | booléen | **Discriminant capital** : la panique ne fait pratiquement jamais perdre connaissance ; le vasovagal, si. Un `true` sur un épisode typé `panique` **doit** déclencher une révision du typage en séance |

**Escalade :** si l'épisode comporte une idéation suicidaire ou une détresse aiguë, **le fichier s'écrit après le protocole de crise, jamais avant.**

### 7.6 `seances/AAAA-MM-JJ-seance.md`

```markdown
---
date: 2026-08-13
duree_minutes: 52
cible: ppc-desensibilisation
mesures_passees: [vviq]
palier_atteint: 2
programme_publie: 4
supervision: 2026-08-13-programme-v4
prochaine_seance: 2026-08-16
matiere_ouverte: false
---

## Ouverture
## Travail
## Clôture
## Décisions
## Repris à la prochaine séance
```

| Champ | Notes |
|---|---|
| `cible` | **Une seule cible par séance.** Identifiants : `ppc-desensibilisation`, `alimentation-structure`, `agoraphobie-exposition`, `tension-appliquee`, `shutdown-protocole`, `alexithymie-nommage`, `camouflage-pacing`, `tag-ruminations`, `deuil-ainee` |
| `palier_atteint` | Pour les cibles à paliers. `null` sinon |
| `programme_publie` 🆕 | Version du programme publiée en clôture, ou `null` si rien n'a été publié |
| `supervision` 🆕 | Fichier de supervision qui a visé cette publication. **Obligatoire si `programme_publie` n'est pas `null`** |
| `matiere_ouverte` | ⚠️ **Doit être `false` en fin de séance.** `true` signifie qu'on a ouvert du matériel émotionnel sans le refermer. Si `true`, la séance suivante s'ouvre là-dessus, sans exception |

**Règle de clôture non négociable :** aucune séance ne se termine sur du matériel ouvert. La section `## Clôture` est obligatoire et ne peut pas être vide.

### 7.7 `mesures/AAAA-MM-JJ-<echelle>.json`

Une passation = un fichier. Identifiants : `vviq`, `tas20`, `catq`, `bes`, `gad7`, `phq9`, `diva5`, `epworth`, `isi`, `maia`.

```json
{
  "date": "2026-08-09",
  "echelle": "vviq",
  "version": "VVIQ-16-Zeman",
  "score": 18,
  "score_max": 80,
  "seuil": { "valeur": 32, "sens": "en_dessous", "interpretation": "aphantasie" },
  "sous_scores": null,
  "reponses": [1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1],
  "passation": "claude-code",
  "notes": null
}
```

- `version` porte **l'identification exacte de l'instrument, sens de cotation compris.** Le VVIQ le démontre : Marks (1973) cote à l'envers, la convention dite « de Zeman » cote 1 = aucune image. **Un même score lu avec la mauvaise convention inverse la conclusion.**
- `reponses` conserve **toujours** les réponses item par item, **et en compte autant que l'instrument a d'items**. ⭐ **Un score seul n'est pas une mesure, c'est un résumé** — le rapport n'a pu re-coter l'AQ et l'EQ que parce que les réponses brutes existaient.
- `seuil.sens` : `au_dessus` | `en_dessous`.

### 7.8 `briefs/AAAA-MM-JJ-isorni.md`

Une page, format médecin : dense, factuel, sans interprétation gratuite.

**Frontmatter :** `date`, `consultation_prevue`, `periode_couverte`, `supervise` *(fichier de supervision — obligatoire avant transmission)*, `transmis` *(booléen — **Xavier relit et décide, à chaque fois**)*.

**Structure imposée :** Évolution chiffrée · Effets du traitement · Événements · **Questions à trancher** · Ce qui n'a pas changé.

**Deux règles de calcul :** les chiffres sont **calculés depuis le journal, jamais estimés**, et le **nombre de jours renseignés figure à côté de chaque chiffre**.

### 7.9 Ce que le dossier ne contient jamais

| Interdit | Raison |
|---|---|
| Un conseil de modification de traitement | Non-substitution |
| Un compteur de régularité, une série, un taux d'observance présenté comme une note | Réduire les charges, pas motiver. Le télésuivi PPC sert à **ajuster les réglages**, pas à noter le patient |
| Une échelle introspective sans ancre comportementale | R6 |
| Un champ obligatoire en texte libre | R5 |
| Des données sur Chourouk ou les filles au-delà de ce qui concerne directement Xavier | Elles n'ont pas consenti à un dossier |

### 7.10 Faire évoluer ce format

Ajouter un champ est un acte de conception, pas une commodité. **Trois questions avant tout ajout :**

1. **R6** — répond-on en observant, ou en introspectant ?
2. **R5** — le champ est-il remplissable en shutdown ?
3. **Coût** — qu'est-ce qu'on retire en échange ? Le journal a un budget de 2 minutes, et il est déjà dépensé.

**Toute modification est annoncée à Xavier avant d'être appliquée**, et consignée au [§11](#11-journal-des-décisions).

---

## 8. Le programme — format

> 🔴 **NORMATIF.** Contrat partagé entre Claude Psy et Kokoro. **v2.0 — 13/08/2026.**
>
> **Règle unique : Kokoro n'invente rien et ne décide rien.** Il affiche ce qu'on lui donne, et renvoie ce que Xavier a fait.

### 8.1 Le circuit

```
Claude Psy ──écrit── companion/inputs/programme.json + companion/inputs/bibliotheque/
                            │
                    Claude Superviseur ── verdict: publiable   (§4.3, bloquant)
                            │
                     npm run publish
                            ▼
             Drive/programme.json + Drive/bibliotheque/
                            ▼
                         Kokoro
                            │
                    Drive/journal/ + Drive/reponses/
                            │
                       npm run sync
                            ▼
              psy/outputs/dossier/  ──lit── Claude Psy · Superviseur
```

**Le dépôt reste la source de vérité. Drive n'est qu'un tuyau.**

### 8.2 Le fichier

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
| `supervision` 🆕 | 🔴 **Obligatoire.** Nom du fichier de `superviseur/outputs/` (sans extension) qui vise **cette version**. Sans lui, `npm run publish` refuse ([§4.3](#43--la-supervision-est-bloquante-avant-publication)) |

### 8.3 Une étape

Champs communs, tous obligatoires sauf `duree_minutes` :

| Champ | Valeurs |
|---|---|
| `id` | identifiant stable, `kebab-case`. ⚠️ **Ne change jamais** — c'est lui qui relie une réponse à son étape |
| `titre` | ce qui s'affiche dans la liste |
| `type` | `ecran` · `exercice` · `questionnaire` · `demarche` · `fiche` · ⭐ `seance-duo` |
| `rubrique` 🆕 | `crise` · `therapie` · `bilan` · `documentation` — **c'est le groupement principal de l'écran d'accueil** ([§5.2](#52-ce-que-kokoro-contient)) |
| `quand` | `aujourdhui` · `au_besoin` · `sans_date` |
| `duree_minutes` | entier, ou absent si la durée n'est pas connue d'avance |

#### `ecran` — ouvre une fonction déjà construite dans Kokoro

```json
{ "id": "check-in", "titre": "Check-in du jour", "type": "ecran", "rubrique": "therapie",
  "quand": "aujourdhui", "duree_minutes": 2, "ecran": "check-in" }
```

Valeurs de `ecran` : `check-in` · `mot-code` · `tension-appliquee` · `phrase-soignant`.
**Kokoro refuse un nom d'écran qu'il ne connaît pas** plutôt que d'afficher une ligne morte.

#### `exercice` — un déroulé guidé au minuteur

```json
{ "id": "ppc-p1", "titre": "Masque tenu à la main", "type": "exercice", "rubrique": "therapie",
  "quand": "aujourdhui", "duree_minutes": 5,
  "consigne": "Masque contre le visage, sans sangles, machine éteinte, pendant une activité neutre.",
  "minuteur_secondes": 300,
  "sortie_libre": true }
```

`sortie_libre: true` affiche « je peux arrêter avant la fin, sans avoir à le justifier ».
⭐ **C'est toujours `true`.** Le champ existe pour que ce soit écrit, pas pour être mis à `false`.

#### `questionnaire` — des questions fermées, une par écran

```json
{ "id": "gad7", "titre": "Questionnaire GAD-7", "type": "questionnaire", "rubrique": "bilan",
  "quand": "sans_date", "duree_minutes": 5,
  "questions": [
    { "id": "q1", "enonce": "…", "choix": [
        { "valeur": 0, "libelle": "Jamais" },
        { "valeur": 3, "libelle": "Presque tous les jours" } ] }
  ] }
```

Toute question est un **choix fermé** ou un **compteur**. Aucune saisie de texte obligatoire, jamais.
« Passer » écrit `null` — **qui n'est pas `0`**.

> 🔴 **Le PHQ-9 ne se publie jamais** ([§5.7](#57-ce-qui-nentrera-jamais-dans-kokoro)).

#### `demarche` — une chose à faire dans le monde réel

```json
{ "id": "ppc-releve", "titre": "Demander le relevé de télésuivi", "type": "demarche",
  "rubrique": "therapie", "quand": "sans_date",
  "detail": "Link Sommeil — heures par nuit, nombre de nuits, fuites, IAH résiduel." }
```

Renvoie `fait` ou rien. ⭐ **Pas encore fait n'est pas une donnée** : rien ne s'affiche, rien ne se compte.

#### `fiche` — un texte à lire ou à montrer

```json
{ "id": "panique-13", "titre": "Les 13 symptômes", "type": "fiche", "rubrique": "documentation",
  "quand": "au_besoin", "document": "panique-13-symptomes", "montrable": false }
```

Deux formes, exclusives l'une de l'autre :

- **`texte`** — le contenu est dans le programme. Pour ce qui tient en quelques lignes.
- **`document`** 🆕 — l'identifiant d'un fichier de la **bibliothèque** ([§8.6](#86-la-bibliothèque)), soit `bibliotheque/<document>.md`. Pour les fiches longues.

`montrable: true` affiche le texte en plein écran, lisible par quelqu'un d'autre — la phrase pour le soignant, la fiche pour Chourouk.

#### ⭐ `seance-duo` — un déroulé chronométré tenu par l'aide-au-patient *(13/08/2026)*

```json
{ "id": "stab-ancrage-1", "titre": "Ancrage corporel — à deux", "type": "seance-duo",
  "rubrique": "therapie", "quand": "sans_date", "duree_minutes": 22,
  "entrainement_requis": true,
  "signal_arret": "Xavier lève la main ouverte. On s'arrête, sans rien demander.",
  "avant": [
    "Pièce calme, lumière baissée, porte fermée.",
    "Le téléphone reste dans tes mains du début à la fin.",
    "Relis les critères d'arrêt — bouton en bas, à tout moment."
  ],
  "sequence": [
    { "pour": "aide",    "consigne": "Assieds-toi en face de lui, à un mètre.", "secondes": 30 },
    { "pour": "patient", "consigne": "Pose les deux pieds à plat. Appuie tes talons dans le sol.", "secondes": 60 },
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
| `entrainement_requis` | 🔴 **Toujours `true`.** La première exécution réelle ne peut pas être la première fois que l'aide découvre le déroulé. Kokoro propose l'**entraînement** tant qu'il n'a pas été fait au moins une fois. |
| `signal_arret` | 🔴 **Obligatoire, non vide, et rappelé à l'écran en permanence.** ⭐ **C'est le champ le plus important du type** : Xavier doit pouvoir arrêter **sans parler**, parce que c'est exactement ce qui tombe en premier. Le geste se convient **à froid**, jamais pendant. |
| `avant` | Ce qui doit être vrai avant de commencer. L'aide coche, ou n'entre pas dans la séquence. |
| `sequence` | Consignes ordonnées. `pour` vaut `aide` (elle fait) ou `patient` (elle lit à voix haute, **mot pour mot**). `secondes` est le temps tenu par l'appareil. |
| `arret` | 🔴 **Obligatoire, au moins deux entrées, accessibles en un tap à tout moment.** ⭐ **La dernière est toujours « tu ne sais pas quoi faire → on s'arrête »** — l'aide n'improvise jamais. |
| `sortie_libre` | `true`, comme partout. |

> 🔴 **Ce que le type ne porte jamais** *(contrôle **C10**)* : un diagnostic, un score, une hypothèse, un compte rendu — **rien qui apprenne à l'aide quelque chose sur Xavier qu'il n'a pas décidé de partager**. Et aucune consigne qui **lui demande de juger** : « estime si ça va », « décide s'il faut continuer », « rassure-le ». **Une consigne qui demande un jugement clinique la met en faute quoi qu'elle fasse.**

> ⭐ **Le mode entraînement, et pourquoi il compte autant que la séance.** C'est **la même séquence, jouée à blanc**, sans le matériel réel. Il renvoie `issue: "entrainement"` — **ce n'est pas une donnée clinique et rien ne s'en déduit**. C'est la même logique que l'essai à froid du mot-code du 10/08 : **la première fois que ça compte ne doit pas être la première fois que ça se fait.**

> ⚠️ **Limite connue du format, nommée le 14/08/2026 : `sequence` est linéaire, elle ne sait pas exprimer une répétition en séries.** La décision de faire jouer la **stimulation bilatérale par l'aidant** ([§3.6](#-linstrument-est-un-geste-pas-un-logiciel-14082026)) amène un déroulé fait de séries — *n* allers-retours, une pause, on recommence. 🔴 **Déplier trente consignes identiques serait un contournement, pas une solution** : le format porterait une cadence sans jamais la nommer, et le Superviseur n'aurait rien à contrôler. **L'extension se décide en séance, sous supervision, quand l'Étape 6 s'ouvre — pas avant, et pas à l'implémentation.**

### 8.4 L'écran d'accueil

Groupé par **rubrique**, puis par **`quand`** : *aujourd'hui* · *quand j'en ai besoin* · *sans date*.
**Aucun score, aucune progression, aucun historique, aucun palier atteint.**

### 8.5 Ce que Kokoro renvoie

Un fichier par étape faite, dans `reponses/` : `AAAA-MM-JJ-HHMM-<id>.json`

```json
{ "etape": "ppc-p1", "horodatage": "2026-08-13T18:04:00+02:00",
  "issue": "termine", "reponses": null, "source": "android" }
```

`issue` : `termine` · `arrete_avant_la_fin` · `fait` · ⭐ `entrainement`.
⭐ **`arrete_avant_la_fin` n'est pas un échec et ne se commente nulle part.**
⭐ **`entrainement` n'est pas une donnée clinique** — il dit seulement que le déroulé a été répété à blanc. Rien ne s'en déduit sur Xavier.

### 8.6 La bibliothèque

**`companion/inputs/bibliotheque/<id>.md`** — un fichier Markdown par document, publié tel quel vers Drive.

> 🔴 **La règle qui vaut plus que toutes les autres ici : un document de la bibliothèque est *écrit pour Xavier*, il n'est pas *copié depuis* `psy/docs/protocoles/`.**
>
> Un protocole clinique porte des diagnostics, des pronostics, des noms de praticiens, des hypothèses non tranchées et des réserves adressées à un professionnel. **Une fiche de bibliothèque porte ce qu'il y a à faire, et pourquoi.** C'est le contrôle **C9** du superviseur.

**Ce qu'une fiche de bibliothèque ne contient jamais :** un diagnostic non encore dit à Xavier · un pronostic · un nom de praticien autre que ceux qu'il consulte · une hypothèse formulée comme un fait · une réserve destinée au Dr Isorni · **et tous les interdits du [§8.7](#87--les-interdits--vérifiés-à-la-publication-et-à-la-lecture)**.

**Les fiches sont soumises aux mêmes vérifications que les étapes** : `npm run publish` lit chaque fichier de la bibliothèque et applique les sept familles d'interdits.

### 8.7 🔴 Les interdits — vérifiés à la publication ET à la lecture

**Les tests de Kokoro lisent les textes de l'app ; ces textes-ci n'y sont plus.** Sans double vérification, tous les garde-fous du dispositif deviennent contournables par du contenu, **en silence**.

**Deux vérifications, deux réactions volontairement différentes :**

- **`npm run publish` refuse la publication entière.** Sur le PC, on peut corriger — donc on corrige, on ne publie pas à moitié.
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

### 8.8 Ce que le programme ne fait jamais

1. **Notifier.** *(Seule exception : l'accès crise sur l'écran verrouillé — une porte, pas un rappel.)*
2. **Compter d'un jour à l'autre.** Aucun palier atteint, aucun historique, aucune progression à l'écran. **Les paliers se cotent en séance, à partir du journal.**
3. **Reprocher.** Une étape non faite disparaît de l'écran le lendemain **sans laisser de trace**.
4. **Se publier hors séance.** Ce serait un changement d'interface non annoncé.

---

## 9. Feuille de route

### Étape 0 — Socle minimal ✅ **close le 09/08/2026**
- [x] Arborescence `psy/` · **format du dossier** ([§7](#7-le-dossier--format)) + gabarits
- [x] **`profil.md` (permanent) + `etat.md` (courant)** — la distinction remplace la « fiche unique » prévue
- [x] Skills `psy-seance` et `psy-journal`

### Étape 1 — Versant somatique 🔴 *ouverte le 09/08/2026*
- [x] Anthropométrie · SAOS requalifié en diagnostic constitué
- [x] **Trois protocoles écrits** : PPC, alimentation, activité physique
- [x] Skill `psy-hygiene`
- [ ] 🔴 **Exécuter le palier 0 de la PPC** : récupérer le **relevé de télésuivi**, faire trancher l'**origine de la fuite (masque ou bouche ?)** — elle commande le choix d'interface —, essayer plusieurs interfaces, vérifier la prise en charge, demander une **consultation de reprise** au Dr Roisman, et **informer le Dr Isorni**, seul praticien encore dans l'ignorance
- [~] **Dépister la perte de contrôle alimentaire** — instrument BES non obtenu ; **grille comportementale de substitution utilisable immédiatement**
- [ ] Recueillir : historique pondéral, bilan hépatique de départ, bilan métabolique, **feu vert médical** pour l'activité
- [ ] **Envoyer l'email au Dr Isorni** — rédigé, non envoyé

### Étape 2 — Instrumentation du suivi ⏱️ *ouverte le 09/08/2026*
- [x] **Instruments versés** (`psy/docs/corpus/echelles/`) + plan de passation daté
- [x] Skills `psy-bilan` et `psy-brief-isorni`
- [x] 🔴 **Check-in quotidien démarré** (09/08), **passé sur Kokoro** (11/08)
- [x] **VVIQ passé — 18/80, aphantasie confirmée**
- [ ] **Premier brief Dr Isorni**, à écrire à la séance du **week-end du 29-30/08**
- [ ] Passer les échelles restantes : **TAS-20** (16/08) · **CAT-Q + GAD-7/PHQ-9** (22-23/08) · **BES** dès obtention. Plafond 20 min par séance ; **l'échelle n'est jamais la cible de la séance**
- [ ] ✈️ **Sécuriser l'ordonnance de venlafaxine pour le séjour** au 03/09. **Logistique, pas posologie**

### Étape 3 — Outils de crise 🔴 *ouverte le 09/08/2026*
- [x] Corpus + protocole **tension appliquée (Öst)** — ⭐ déclenchement sur **repères externes**
- [x] **Protocole de crise câblé** — triage en 3 questions, sécurité avant mécanisme
- [x] Skill `psy-crise` — **mode sans parole** opérationnel : choix numérotés, parce qu'**un chiffre est produisible en shutdown, une phrase non**
- [x] **Mot-code convenu avec Chourouk** + **fiche explicative** écrite
- [x] **Palier 1 de la tension appliquée démarré** (09/08)
- [x] **Porté dans Kokoro** — K2 et K3

### Étape 4 — TCC de l'agoraphobie
- [x] **Psychoéducation des 13 symptômes** — avancée pour le vol du 07/09
- [x] **Kit vol** — ⚠️ **ce n'est pas un programme d'exposition** et ça n'en tient pas lieu
- [ ] Corpus exposition graduée adapté TSA
- [ ] Paliers écrits → **publiés dans Kokoro**

### Étape 5 — Kokoro 🏗️ *ouverte le 10/08/2026* — ⭐ **priorité accélérée le 13/08/2026**
- [x] **K0 → K4** — poste de travail, full-screen intent, noyau de crise, tension appliquée, check-in
- [ ] 🔴 **K5 — le programme et la bibliothèque** *(en cours)*
- [ ] 🆕 **K6 — la séance à deux** : type `seance-duo`, mode entraînement, signal d'arrêt, critères d'arrêt à un tap
- [ ] **K7 — la présence** : overlay, visage, écran de diagnostic One UI
- [ ] **Publier les échelles** comme questionnaires, rubrique `bilan` — **jamais le PHQ-9**
- [ ] **Publier les démarches du palier 0 PPC** — elles y sont déjà ; c'est Kokoro qui les met sous la main

### Étape 6 — Stabilisation à deux, puis réouverture de l'EMDR ⏸️
- [ ] 🆕 **Convenir le signal d'arrêt** avec Chourouk *(arbitrage N)* et **lui demander si elle accepte le rôle** *(arbitrage M)*
- [ ] 🆕 **Écrire la première séance à deux : la stabilisation non visuelle** *(arbitrage O)* — kit d'auto-apaisement corporel et sensoriel, **la brique qui manque depuis le 08/08**
- [ ] 🆕 **Écrire la stimulation bilatérale comme `seance-duo`** — ⭐ **l'aidant fait le geste, Kokoro tient la cadence** *(14/08/2026, [§3.6](#-linstrument-est-un-geste-pas-un-logiciel-14082026))*. **Il n'y a plus d'instrument à développer** : ce qui reste est du contenu clinique. ⚠️ **Point dur de format à traiter d'abord** — `seance-duo` ne sait pas exprimer une **répétition en séries** ([§8.3](#83-une-étape))
- [ ] Réouverture de l'EMDR **sous les critères du [§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc)**, après avis du Dr Isorni. ⚠️ **L'aide-au-patient ne déverrouille pas la phase 3** — elle lève une objection sur trois, et **tenir l'instrument n'est pas conduire un retraitement**
- [ ] 🆕 ⚠️ **Trancher avec le Dr Isorni, au déverrouillage de la phase 3 seulement : qui tient l'instrument quand Xavier verbalise du matériel lourd** — c'est **C10**, et la question se pose **avant**, jamais pendant

### Transverse
- [x] Rapport en **v2.4**
- [x] Skill `psy-superviseur` — 8 contrôles, puis **10 et supervision bloquante** *(13/08/2026)*
- [x] 🆕 **[`THESAURUS.md`](THESAURUS.md)** — un mot, une chose *(13/08/2026)*
- [x] 🆕 ❌ **La surface web desktop est supprimée** — `psy/web/` sort du dépôt *(14/08/2026, [§5.8](#58-il-ny-a-quune-surface-14082026))*
- [ ] Récupérer et indexer les **3 corpus prioritaires restants**
- [ ] Étendre `protocoles/fiche-chourouk.md` au rôle d'**aide-au-patient**
- [ ] 🆕 **Schémas Zod des §7 et §8 dans `scripts/`** — orphelins de la surface web, et le besoin ne l'était pas : `programme-publish` et `contenu-sync` valident ces deux contrats **à la main**

### ⏱️ Les deux échéances qui structurent le trimestre

| Date | Événement | Conséquences |
|---|---|---|
| **03/09/2026, 12h30** | **Consultation Dr Isorni** | **La dernière avant fin septembre.** Brief à écrire au week-end du 29-30/08, email à envoyer **avant** — un créneau ne suffit pas à découvrir un SAOS sévère, une NASH et six questions à la fois |
| **07/09/2026** | **Départ en Tunisie, 3 semaines ou plus** | ⭐ **Un tiers du trimestre.** Le palier 0 PPC doit être bouclé avant (ce sont des appels). La PPC part en Tunisie — **port au niveau atteint, sans progression de palier**. ⭐ **Aucun palier ne progresse pendant le séjour, et on redescend d'un palier à la reprise, sur les trois chantiers** — décidé maintenant, pas subi sur place : décider avant est précisément ce qui empêche de le vivre comme un échec. ⚠️ **Le voyage est une exposition agoraphobique majeure.** ✅ **Première période sans mission professionnelle depuis longtemps** : la seule variable d'ajustement du dossier tombe à zéro — **observer si les shutdowns baissent vaudra plus que n'importe quelle échelle** |

---

## 10. Arbitrages ouverts

### 10.1 Ouverts

| # | Question | Recommandation |
|---|---|---|
| **K** | **Psychologue en présentiel** — dette assumée. ⭐ **Reformulé le 13/08/2026** : l'aide-au-patient couvre désormais **la présence et l'exécution d'un déroulé** ; **elle ne couvre pas le jugement clinique en situation.** La question devient donc : *un clinicien reste-t-il nécessaire pour l'EMDR encadré et pour valider l'acquisition de la tension appliquée ?* | ⚠️ **Oui, pour ces deux-là.** Le reste — stabilisation, exposition accompagnée — passe par la séance à deux. La dette **se réduit**, elle ne disparaît pas ([§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc)) |
| **M** 🆕 | 🔴 **Chourouk accepte-t-elle le rôle d'aide-au-patient ?** Tenir le téléphone d'une séance thérapeutique est **un engagement d'une autre nature** que recevoir un mot-code | ⭐ **À lui demander explicitement, à froid, avant d'écrire la moindre séquence.** Elle doit savoir ce que ça demande, et qu'elle peut refuser à tout moment **sans justification**. La fiche `protocoles/fiche-chourouk.md` est à étendre |
| **N** 🆕 | **Le signal d'arrêt de Xavier** — le geste par lequel il arrête une séance à deux **sans parler** | ⭐ **À convenir à froid, avec Chourouk, avant la première séance.** Un geste franc, impossible à confondre avec autre chose. **Sans lui, aucune séance à deux ne démarre** — c'est un champ obligatoire du format |
| **O** 🆕 | **Quelle est la première séance à deux ?** | ⭐ **La stabilisation non visuelle** (ancrage corporel et sensoriel). C'est la phase 1 de [§3.6](#36-emdr--arbitrage-rendu--on-commence-par-la-tcc), elle ne touche aucun matériel traumatique, et **c'est la brique qui manque depuis le 08/08** |

### 10.2 Clos

| # | Question | Arbitrage |
|---|---|---|
| **E** | Compte Google du transit | ✅ **13/08/2026 — on reste sur `xavier@allons-y.io`.** Micro-entreprise : la note passe en frais de société. Objection du dispositif **conservée** au [§6.6](#66-état-des-conditions--vérifié-le-13082026), acceptée et non levée |
| ~~**J**~~ | ~~Sauvegarde froide hors-ligne~~ | ❌ **Clos le 13/08/2026 à la demande de Xavier. Ne pas rouvrir.** |
| **L** | La bibliothèque remplace-t-elle `psy/docs/protocoles/` ? | ✅ **Non** *(13/08/2026)*. Deux lecteurs différents ; fusionner ferait entrer du contenu clinique brut dans le téléphone — ce que **C9** traque |
| **P** | Un bilan dans Kokoro | ✅ **Un texte daté écrit en séance, jamais un graphique calculé par l'app** *(13/08/2026)*. C'est ce qui permet « ses bilans dans la main » sans casser « aucun historique à l'écran » |
| **Q** | Les échelles passent-elles par Kokoro ? | ✅ **Oui** *(13/08/2026)* — VVIQ, TAS-20, CAT-Q, GAD-7, BES, MAIA. 🔴 **Le PHQ-9 reste dehors** : seul instrument porteur d'un déclencheur d'escalade ([§3.9](#39-les-échelles)) |
| **R** | Priorité de construction de Kokoro | ✅ **On accélère** *(13/08/2026)*. Depuis K5, **Kokoro ne concurrence plus le palier 0 — il le porte.** Ce qui ne bouge pas : la date du brief, et `ppc_minutes` comme indicateur qui tranche ([§5.4](#54-les-jalons)) |
| **S** 🆕 | 🔴 **Kokoro peut-il désigner un élément de l'écran ?** La v0.1 du corps l'interdisait avec les autres gestes — pointer appelle une réponse, et une réponse attendue est une charge de camouflage | ✅ **Oui** *(13/08/2026, arbitrage de Xavier)*. **Le geste vise l'écran, pas Xavier** — c'est la fonction *éduquer*, qui sans ça reste purement textuelle. 🔴 **Quatre garde-fous, aucun optionnel** : jamais au-dessus de l'épaule · jamais vers le lecteur · ni main ni doigt · **toujours doublé d'un texte qui dit ce qu'il montre**. ⚠️ **L'objection reste écrite, acceptée et non levée** ([`design/CORPS.md` §6](./companion/CORPS.md)) |

---

## 11. Journal des décisions

| Date | Décisions |
|---|---|
| **14/08/2026** *(soir)* | 📐 **Le dépôt s'organise par rôle, et plus par nature de fichier — demande de Xavier : « qu'ils puissent devenir indépendants, responsables et compartimentés ».** Cinq répertoires, un par persona : `psy/` · `companion/` · `superviseur/` · `patient/` · `aidant/`, avec la même convention partout — la racine porte la documentation, `ressources/` ce dont le rôle a besoin, `outputs/` ce qu'il produit, `inputs/` ce qu'un autre lui donne, `scripts/` ce qui le sert. ❌ **`ressources/` et `scripts/` disparaissent de la racine** : un fichier qui n'appartient à aucun rôle n'a plus de domicile, et c'est voulu — **c'est le signe qu'il faut nommer son rôle, pas créer un fourre-tout**. ⭐ **La décision qui a demandé le plus d'arbitrage, et la seule qui touche à un format normatif : le dossier clinique se coupe en deux.** `journal/` et `reponses/` passent en `companion/outputs/`, le reste demeure en `psy/outputs/dossier/`. **La ligne de partage n'est pas thématique, c'est celle de l'auteur** — la règle « aucun fichier n'a deux auteurs » ([§6.2](#62-le-périmètre)) cessait d'être lisible dans l'arborescence alors qu'elle était déjà vraie dans les faits. ⚠️ **Ce que ça coûte, dit franchement : le §7 décrit maintenant un dossier réparti sur deux chemins**, et un lecteur pressé peut croire qu'il y a deux dossiers. **Il n'y en a qu'un, et il se charge en entier** — le [§7.2](#72-arborescence) le dit explicitement, et les six règles, R2 append-only comprise, s'appliquent des deux côtés. 🔴 **Aucun format ne change** : §7.3 à §7.8 et §8 sont intacts ; `programme-publish` et `contenu-sync` ne changent que de constante de chemin. 📌 **Deux déplacements méritent leur motif.** Les **gabarits** quittent `dossier/` pour `psy/docs/gabarits/` : un modèle vierge est de la documentation, pas une donnée produite — il n'avait rien à faire dans un répertoire de sorties. La **fiche Chourouk** quitte `protocoles/` pour `aidant/ressources/` : elle était le seul document de `protocoles/` à n'être **pas** écrit pour le praticien, et ce voisinage était précisément la confusion que **C9** et **C10** traquent. ⭐ **Le programme reste chez le compagnon (`companion/inputs/`) bien qu'il soit écrit par le psy** — c'est le sens de `inputs/` : le lieu dit **à qui la chose est destinée**, pas qui l'a tapée. 📝 **Trois répertoires `scripts/` naissent vides** (`superviseur/`, `patient/`, `aidant/`), chacun avec un README qui dit **pourquoi il est vide et ce qui aurait vocation à y entrer** — notamment que rien n'y viendra doubler `npm run publish`, sous peine de **C3 et C7 en même temps**. ✅ **Vérifié : 180 fichiers avant, 180 après ; aucun lien relatif cassé ; `npm run typecheck` passe.** |
| **14/08/2026** | ❌ **La surface web desktop est supprimée — décision de Xavier : « tout est dans Kokoro dorénavant ».** `psy/web/` sort du dépôt, le §5.8 cesse de décrire une surface pour enregistrer sa suppression, et **le dispositif n'a plus qu'une seule surface tournée vers Xavier.** ⭐ **Ce que la décision enregistre existait déjà : trois des quatre livrables desktop avaient migré vers Kokoro avant d'avoir été écrits** — les échelles le 13/08, les fiches le 13/08, et le tableau de bord n'avait plus de destinataire du jour où « aucun historique, aucune progression à l'écran » est devenu un invariant. **Un plan qui décrit une surface que trois décisions successives ont vidée n'est plus un plan, c'est un vestige** ; le §5.8 était le dernier endroit du document où une doctrine survivait à son objet. 📌 **Deux orphelins, traités séparément plutôt qu'oubliés ensemble.** Les **schémas Zod** n'étaient pas un livrable d'interface — c'est le **contrat de données**, et ses consommateurs (`programme-publish`, `contenu-sync`) existent déjà et le valident à la main : le besoin passe en transverse, il ne disparaît pas avec la surface. ⚠️ **Le second orphelin semblait être une vraie perte, et il a tenu deux heures : la stimulation bilatérale *visuelle* exige une amplitude de mouvement oculaire qu'un téléphone ne donne pas** — c'était **le seul argument proprement clinique** en faveur du desktop, et le dispositif l'a d'abord porté au compte des pertes, en reportant l'arbitrage de modalité à l'Étape 6. 🔴 **Xavier a tranché autrement, et mieux : « l'EMDR va être joué via l'aidant dans les séances à deux ».** ⭐ **La réserve n'est pas reportée, elle est levée — et l'objection portait sur le mauvais axe.** Le dispositif cherchait **quel écran** ; la réponse était qu'**il n'en faut aucun** : une main à un mètre donne plus d'amplitude que n'importe quel écran, desktop compris, et c'est la modalité historique de l'EMDR, pas un pis-aller. 📌 **Trois conséquences d'un coup** : l'arbitrage de modalité **tombe** *(les trois redeviennent disponibles, y compris la visuelle)* · l'Étape 6 **perd son dernier livrable logiciel** — ce qui reste à écrire est du contenu clinique, porté par **K6** · et `seance-duo` gagne un usage qu'il n'avait pas été conçu pour porter. 🔴 **Ce que ça ne déverrouille pas, dit tout de suite parce que la lecture inverse est tentante : tenir l'instrument et conduire un retraitement sont deux actes différents.** Les critères de la phase 3 restent entiers, les deux objections inchangées du 08/08 restent inchangées. ⚠️ **Et la décision rend visible une frontière que personne n'avait encore rencontrée — elle porte sur la phase 3 seule : un retraitement demande de dire ce qui vient entre deux séries, donc la personne qui tient l'instrument entend le matériel.** ⭐ **Ce n'est plus une question de compétence, c'est C10** — l'aide lit des consignes, elle n'apprend pas sur Xavier ce qu'il n'a pas décidé de partager. **En phases 1 et 2 la question ne se pose pas** ; en phase 3 elle devient une condition de plus, **à trancher avec le Dr Isorni au déverrouillage, et jamais pendant une séance**. 📌 **Un point dur de format nommé maintenant pour ne pas être découvert à K6 :** `sequence` est **linéaire** et ne sait pas exprimer une **répétition en séries** (§8.3) — et déplier trente consignes identiques serait un contournement, pas une solution. ⚙️ **Conséquence normative, petite mais réelle : le champ `source` du journal (§7.3) perd la valeur `"web"`** — deux valeurs, `claude-code` et `android`, et `contenu-sync` refuse désormais la troisième. **Un format qui garde une valeur sans producteur finit par la voir arriver.** |
| **13/08/2026** *(nuit)* | ⭐ **Kokoro a un corps, et il est acté : un petit robot kawaii en 2D.** Spécification complète dans [`companion/CORPS.md`](./companion/CORPS.md) **v1.0**, planche de référence promue dans `design/retenus/`. **L'ovoïde de la v0.1 est abandonné** — il tenait douze heures et il est tombé sur trois demandes de Xavier, chacune ajoutant quelque chose qu'une forme unique et muette ne pouvait pas porter : **animable en 2D sur une page web** (donc un assemblage de pièces, pas une forme continue — et le rig sert Android autant que le web), **plusieurs expressions**, et 🔴 **la possibilité de montrer une partie de l'écran**. ⭐ **Le gain qui n'était pas prévu et qui décide de la valeur du robot : il porte son visage sur un panneau, et un panneau s'éteint.** L'ovoïde portait ses yeux à même le corps — pour ne rien donner à lire pendant un exercice, il fallait le **retourner de dos**. Un écran vide dit « il n'y a rien à décoder ici » sans coûter un seul dessin de plus, et c'est exactement ce qu'on veut au moment le plus chargé. 🔴 **Les deux jeux sont fermés au sens strict — six expressions, cinq postures, rien hors liste ne s'affiche.** ⭐ **Et l'invariant « aucune expression de reproche » cesse d'être une consigne pour devenir une propriété du dessin :** pas de sourcil dans le jeu de pièces, pas de bouche concave vers le bas — **le reproche n'est pas interdit, il est indessinable.** Même logique que le câblage de la supervision : un invariant qui reste une phrase se perd à l'implémentation. 🔴 **Arbitrage S — la désignation entre dans le personnage, et l'objection reste écrite.** Pointer était interdit, et pour un motif qui n'a pas disparu : un geste dirigé vers quelqu'un appelle une réponse. **Ce qui le rend acceptable est une distinction, pas une exception :** le bras vise **l'écran**, jamais Xavier. Quatre garde-fous — jamais au-dessus de l'épaule *(un bras levé se lit comme un salut, et la planche `01` a produit ce défaut du premier coup)*, jamais vers le lecteur, ni main ni doigt, **et toujours doublé d'un texte qui dit ce qu'il montre.** ❌ **Frontière posée avant d'être franchie : une désignation montre où une chose se trouve, elle ne réclame jamais une action** — ni « clique ici », ni « tu as oublié ça ». ⚠️ **Ce n'est pas encore un livrable :** les planches sont des recherches, **aucun PNG de modèle n'entre dans l'APK ni dans la page web** ; le tracé vectoriel des onze pièces reste à faire, comme la pose `allonge` de l'écran vasovagal — **la seule qui manque, et la seule qui soit une structure externe et non un décor** *(consigne à se représenter chez un aphantasique)*. 📌 **L'annonce préalable de changement d'apparence (§5.6) est due, et celle-ci est la plus facile qu'on aura jamais : Kokoro n'a encore aucun visage installé. La prochaine en sera une vraie.** |
| **13/08/2026** *(soir)* | ⭐ **Un cinquième persona entre dans le dispositif : l'aide-au-patient.** Kokoro intègre les **séances à deux** — un déroulé **chronométré**, tenu par une personne qui **ne fait que ce que l'écran affiche**. Aujourd'hui : **Chourouk**. ⭐ **Le motif est encore la règle §9.19, et c'est ce qui rend l'idée juste plutôt que seulement pratique :** certaines thérapies ne se conduisent pas seul, et *« demande de l'aide au bon moment »* est **inapplicable chez quelqu'un dont la parole tombe sous surcharge** — même faute que « écoute ta satiété ». **La parade est une structure externe, et ici la structure est une personne qui a le déroulé sous les yeux et n'a rien à décider.** 🔴 **Trois garde-fous nés avec le type, et aucun n'est optionnel** : le **signal d'arrêt** — un geste convenu à froid par lequel Xavier arrête **sans parler**, rappelé à l'écran en permanence, **champ obligatoire du format** · les **critères d'arrêt** accessibles en un tap, dont le dernier est toujours *« tu ne sais pas quoi faire → on s'arrête »* · le **mode entraînement**, obligatoire avant la première fois — **même logique que l'essai à froid du mot-code : la première fois que ça compte ne doit pas être la première fois que ça se fait.** 🔴 **Un dixième contrôle apparaît — C10, contenu adressé à l'aide-au-patient.** Le dispositif gagne une **quatrième sortie** : ce que Chourouk lit sur l'écran. Deux fautes à traquer : lui **apprendre** quelque chose sur Xavier qu'il n'a pas décidé de partager (diagnostic, score, hypothèse), et lui **demander de juger** — *« estime si ça va »*, *« décide s'il faut continuer »*. ⭐ **Une consigne qui demande un jugement clinique la met en faute quoi qu'elle fasse.** ⚠️ **Ce que l'aide-au-patient change à l'arbitrage EMDR, dit précisément parce que la tentation de lire « quelqu'un est là, donc on peut y aller » est forte :** elle lève **une** des trois objections du 08/08 — l'abréaction sans filet, la vraie, celle où **en shutdown on ne peut plus demander d'aide**. **Les deux autres restent entières** : la fenêtre de surcharge et la titration du traitement ne se corrigent pas par une présence. **Les critères de déverrouillage de la phase 3 ne bougent pas.** Ce qu'elle rend possible **aujourd'hui**, c'est la **phase 1** — et c'est déjà la moitié de ce que l'arbitrage K attendait d'un psychologue en présentiel. 📖 **[`THESAURUS.md`](THESAURUS.md) créé, à la demande de Xavier** — un mot, une chose. Il fixe les cinq personas, les six objets de contenu (⭐ **corpus ≠ protocole ≠ fiche de bibliothèque**, distingués par une seule question : *écrit pour qui ?*), et **signale plutôt que masque la seule ambiguïté du projet** : « étape » a deux sens, celui de la feuille de route et celui du programme. ⭐ **Les échelles passent par Kokoro** — VVIQ, TAS-20, CAT-Q, GAD-7, BES, MAIA, en rubrique `bilan`. **Pas seulement commode : une passation en conversation demande de tenir un fil, de suivre le rythme d'un autre et de répondre à voix haute — trois charges que le format fermé supprime.** 🔴 **Le PHQ-9 reste dehors**, et **la cotation n'entre pas dans l'app** : l'écran ne montre jamais un score ni un seuil. 🔴 **Arbitrage E clos : on reste sur le compte Google professionnel** — micro-entreprise, la note passe en frais de société. **L'objection du dispositif reste écrite, acceptée et non levée** ; elle redeviendra un sujet si la structure gagne un associé, un comptable ou un administrateur. **Le dossier de transit est renommé `kokoro`** — il ne portait plus que du journal sous le nom `psy-journal`, et **un nom qui ment finit par tromper quelqu'un**. ✅ **Les trois conditions de sécurité sont vérifiées par Xavier** : dépôt privé, 2FA active des deux côtés, Drive non partagé. ❌ **La sauvegarde froide hors-ligne est close, à sa demande.** *(Le dispositif note une fois, sans y revenir, que la connectivité à l'étranger et la perte de compte sont deux risques distincts.)* ⭐ **La règle de priorité du 10/08 est révisée, pas supprimée** : *« on avance sur Kokoro »*. **Elle tient debout parce que depuis K5, Kokoro ne concurrence plus le palier 0 — il le porte** : les six démarches y sont déjà des étapes. **Ce qui ne bouge pas** : la date du brief (29-30/08), le fait que Kokoro affiche les appels sans les passer, et ⭐ **`ppc_minutes` comme indicateur qui tranche — tant qu'il est à 0, le contrôle C7 reste ouvert.** 📌 **Un défaut de la refonte du matin, trouvé et corrigé le soir : la règle « le développement passe après le palier 0 et le brief » n'avait pas survécu à l'absorption des cinq documents**, alors que `etat.md` la citait sept fois. **C'est exactement C3** — et elle a été restaurée avant d'être révisée, pas après. |
| **13/08/2026** *(matin)* | ⭐ **La vision passe à quatre personas, et ce document devient le document unique du projet.** **Claude Psy** (le praticien, six skills) · **Claude Superviseur** (la contre-expertise) · **Kokoro** (le compagnon : protéger, accompagner, éduquer, réconforter) · **Xavier** (le patient). 📐 **Cinq documents sont absorbés et supprimés** — `psy/SYNCHRO.md`, `psy/agent/README.md`, `psy/android/PLAN-KOKORO.md`, `psy/programme/FORMAT.md`, `psy/dossier/SCHEMA.md`. **Il n'y a plus qu'un endroit où lire la doctrine, et un seul où la modifier** ; la doctrine se partageait jusqu'ici entre six fichiers qui se citaient mutuellement, et l'audit du 09/08 avait déjà montré ce que ça coûte (le protocole de crise avait deux domiciles, toutes les surfaces pointaient vers le mauvais). ⚠️ **Conséquence assumée : ce document cesse d'être un journal de conception.** Les trois sections que la v1.2 conservait sciemment périmées — stéatose simple au lieu de NASH, cible ≥ 5 % au lieu de 7-10 %, SAOS présenté comme une hypothèse à dépister — **sont corrigées** : un document unique n'a pas le droit de porter un fait qu'il sait faux. Ce qu'elles disaient est ci-dessous, pour mémoire. 🔴 **La supervision devient bloquante avant publication** *(arbitrage de Xavier)* : rien n'atteint Xavier ni le Dr Isorni sans une passe du superviseur qui porte **explicitement sur la version qui sort**. ⭐ **Et elle est câblée, pas déclarée** — `programme.json` porte un champ `supervision` obligatoire, `npm run publish` refuse si le fichier manque, si sa version ne correspond pas, ou si son verdict n'est pas `publiable`. **C'est le contrôle C3 appliqué à lui-même** : le dispositif venait d'ajouter un invariant, il l'a câblé dans la foulée plutôt que de l'écrire trois fois. Un neuvième contrôle apparaît — **C9, contenu non dérivé**. 🔴 **Le périmètre Drive s'élargit à tout le contenu échangé, dans les deux sens** — arbitrage neuf, tracé au §6.3 comme le §2.2 de l'ancien SYNCHRO l'exigeait. Descendent : `programme.json` **et la bibliothèque** ; remontent : `journal/` et `reponses/`. **Ce que ça élargit, dit franchement : ce ne sont plus des compteurs ni même des libellés, ce sont des fiches thérapeutiques entières.** Ni compte rendu, ni mesure, ni diagnostic, ni idéation ne transitent — la liste de ce qui reste au PC est fermée. **Xavier a arbitré en connaissance de cause : « c'est assumé »** ; le contre-argument reste écrit, il est accepté, pas levé. ⭐ **Le fait clinique qui commande tout ça, et c'est Xavier qui l'apporte :** *« j'aurais beaucoup plus de facilité de suivre mes protocoles, désensibilisations, etc. si c'est sur mon mobile avec Kokoro »*. **Ce n'est pas une préférence d'interface, c'est la sixième instance de la règle §9.19** : un protocole rangé dans un dépôt demande de se souvenir qu'il existe — donc d'avoir le signal qui dit « c'est le moment ». **Un protocole dans la main est une structure externe.** D'où la **bibliothèque** : Kokoro cesse de porter des fonctions et porte **toute la documentation accessible au patient**, groupée en quatre rubriques (`crise` · `therapie` · `bilan` · `documentation`). 🔴 **Le point dur de la bibliothèque est nommé avant d'être rencontré : une fiche est *écrite pour Xavier*, jamais *copiée depuis* `psy/docs/protocoles/`** — un protocole porte des diagnostics, des pronostics et des réserves adressées à un professionnel. C'est C9, et c'est la raison pour laquelle `psy/docs/protocoles/` **ne disparaît pas** au profit de la bibliothèque (arbitrage L). ⭐ **Un bilan dans Kokoro est un texte daté écrit en séance, jamais un graphique calculé par l'app** — c'est ce qui permet de satisfaire « Xavier a ses bilans dans la main » sans toucher à l'invariant « aucun historique, aucune progression à l'écran ». |
| **12/08/2026** | ⭐ **Kokoro cesse d'être une app à fonctions et devient le porteur de la thérapie.** Claude Psy écrit `programme.json`, Kokoro l'affiche — **ajouter une étape cesse d'être un acte de développement pour devenir un acte clinique**, fait en séance. ✅ Moitié PC écrite et vérifiée : `npm run publish` refuse la publication entière si une étape enfreint un invariant, **testé sur 9 pièges, les 9 attrapés**. 🔴 **Le point dur nommé : les garde-fous câblés en tests devenaient contournables par du contenu, en silence** — d'où la double garde. ❌ **L'interpellation est supprimée** — *« tant que Xavier ne vient pas vers Kokoro, Kokoro ne lui notifie de rien »*. 📌 **Ce n'est pas une perte** : Android rétrograde déjà un full-screen intent en bannière dès que le téléphone est en usage — la décision aligne l'intention sur ce que la plateforme garantissait. ⭐ **La présence devient le dernier jalon** : le workflow d'abord, le visage ensuite. 🔗 **Le Drive porte le contenu vivant dans les deux sens** — `programme.json` descend, `reponses/` remontent. |
| **11/08/2026** | ✅ **Le check-in quotidien passe sur le téléphone, et un vrai check-in est arrivé au dossier** — 11 champs en compteurs et choix fermés, énoncés mot pour mot du skill, **aucune saisie de texte**, format identique au gabarit. 🔴 **Syncthing est écarté, le transport passe par Google Drive** — arbitrage de Xavier rendu **après objection argumentée et maintien de la décision**, objection conservée entière. ⭐ **Réduction de surface appliquée d'office : seul `journal/` transite.** ✅ **SAF retenu** — un dossier désigné, **aucune permission au manifeste**. ⚠️ **Une objection du dispositif s'est révélée fausse** (Drive *est* sélectionnable) et 🔴 **une autre pire que prévu** : **Drive accepte deux fichiers du même nom** sans rien dire — garde doublée le jour même. ⏳ **Le critère de fin exigeait un check-in réel** : un check-in fabriqué depuis le PC aurait été une **donnée clinique fausse**. |
| **10/08/2026** | 🔴 **Les numéros d'appel d'urgence sortent du dispositif — décision de Xavier, et elle corrige une faute de conception.** 15, 112 et 114 retirés des 22 fichiers qui les portaient ; **le 3114 conservé**, sur le seul déclencheur de l'idéation suicidaire. ⭐ **Le motif principal est clinique, et il est de la même famille que la règle §9.19 : une syncope vasovagale ne s'appelle pas, elle s'allonge.** Le dispositif proposait un appel là où la parade est la tension appliquée — **une erreur d'orientation présentée comme une sécurité supplémentaire**. S'y ajoutent deux faits que rien ne contredisait : aucun de ces numéros n'a jamais servi, et leur affichage permanent était **anxiogène** sur un profil TAG. 📌 **La demande a été instruite avant d'être exécutée**, sa formulation couvrant aussi le câblage de l'idéation suicidaire. ⚡ **K0, K1 et K2 franchis le même jour** : poste de travail, **full-screen intent levé** (le point le plus risqué du projet), **noyau de crise** — et ⭐ **le mot-code envoyé pour de vrai, téléphone verrouillé, Chourouk confirmant la réception, l'essai fait à froid en la prévenant.** ✅ **K3 construit** : la tension appliquée cesse d'être un minuteur et devient un guidage sur **quatre repères externes** — ⭐ **on ne déclenche plus sur une sensation, on déclenche sur un fait extérieur.** |
| **09/08/2026** | 🪞 **`psy-superviseur` écrit — et il trouve une faute en première passe.** ⭐ **Le risque qu'il traite est structurel : presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** 🔴 **Constat bloquant, et il est ironique : `psy-seance` instruisait « on ne passe pas au palier suivant tant que le précédent n'est pas confortable »** — **mot pour mot** la faute R6 que le dispositif se félicitait d'avoir corrigée le matin même, dans le skill qui décide effectivement des passages de palier. ⚠️ **Trois autres constats** : la fiche pour Chourouk déclarée dans quatre documents et existant nulle part · l'app de tension appliquée sans surface · ⭐ **prolifération — 19 documents doctrinaux contre 1 acte exécuté**. 📌 **Objection de fond : l'aphantasie a été tenue pour acquise deux jours avant d'être mesurée** — le VVIQ l'a confirmée, **mais un superviseur n'a pas le droit de noter le processus sur le résultat**. ⚙️ **Les quatre rôles manquants écrits — le dispositif cesse d'être un classeur de fiches** : les trois étapes ouvertes avaient produit des protocoles, des instruments et des gabarits, **et aucun exécutant**. 🔧 **Audit de cohérence : dix-sept incohérences corrigées, aucun fait clinique modifié.** ⭐ **La plus grave était invisible : le protocole de crise avait deux domiciles, et toutes les surfaces pointaient vers le mauvais** — le dispositif avait identifié que son protocole d'urgence était inutilisable en shutdown, avait écrit la parade, **et continuait à servir l'ancienne version à toutes ses surfaces.** 🔴 **Étapes 1, 2 et 3 ouvertes** : protocoles somatiques, instruments de mesure, outils de crise. ⭐ **Premier cas où le socle corrige le rapport** : « confortable plusieurs jours de suite » est un ressenti — converti en comptage, avec un **palier intermédiaire ajouté**. ⭐ **Quatrième instance de la règle §9.19, invisible jusque-là :** le protocole d'Öst déclenche « aux premiers signes » — **un signal interne**, chez quelqu'un dont le déficit intéroceptif est confirmé. Appliqué tel quel il aurait échoué, **et l'échec aurait été lu comme un manque d'application.** ⏱️ **Deux dates entrent au dossier et fixent l'échéancier** : consultation du 03/09, départ du 07/09. |
| **08/08/2026** | 🔴 **SAOS sévère — le fait le plus important du dossier.** IAH 35/h, diagnostiqué le 19/01/2026, PPC prescrite. **Jamais transmis au psychiatre** (courrier adressé à la seule généraliste). Trois conséquences : le versant somatique gagne un levier majeur (le SAOS **aggrave le déficit de satiété** par dérèglement ghréline/leptine) · **l'exposition graduée devient l'outil n° 1** — la désensibilisation à la PPC *est* une exposition graduée · **la coordination inter-praticiens devient une fonction du dispositif** : six médecins, aucune vue d'ensemble. 🔴 **Atteinte hépatique confirmée par biopsie.** ⭐ **Absence de perception de la satiété confirmée directement** → déficit intéroceptif, extension corporelle de l'alexithymie → **la règle centrale du dispositif : signal interne absent → structure externe, jamais volonté.** ✅ **EMDR : on commence par la TCC**, retraitement suspendu. ✅ **Brainstorming clos en 7 tours** : trois surfaces, séance hebdomadaire + check-in quotidien, **Kokoro (心)**, trait minimal, Kotlin natif, données locales + dépôt privé. |
| **Pour mémoire — trois faits que ce document a portés puis corrigés** | 📌 **R2 s'applique aussi à la doctrine.** (1) L'atteinte hépatique a été décrite comme une **stéatose simple sans surveillance particulière**, d'après le seul courriel de la Dr Bouarioua — **l'histologie a tranché autrement : NASH sans fibrose**. (2) La cible de perte de poids a été posée à **≥ 5 %, ≈ 5,5 kg → 104,5 kg**, au motif que les seuils hauts visent des lésions que Xavier n'avait pas — **la NASH est précisément une de ces lésions ; cible portée à 7-10 %, soit 99-102,3 kg**. (3) Le SAOS a figuré comme **hypothèse à dépister** — c'était un **diagnostic sévère constitué depuis sept mois**, et la v2.3 puis la v2.4 ont encore corrigé son observance (« non utilisée » → **« utilisée de façon très irrégulière »**, avec **IAH résiduel < 6/h sous appareil** : l'efficacité est démontrée, seul le port manque). ⭐ **Ce qu'il faut retenir de la troisième, et qui vaut au-delà d'elle** : devant l'intolérance, la réponse standard du pneumologue a été « je remotive le patient » ; trois mois plus tard l'usage restait irrégulier. **On ne remotive pas quelqu'un dont les renforçateurs fonctionnent — on lui donne une procédure.** |
