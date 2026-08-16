# Psy — instructions projet

**Un psychologue/psychiatre virtuel basé sur Claude, conçu pour Xavier.** Un dispositif, pas un chatbot.

> 📖 **La documentation du dispositif est [`README.md`](../README.md)** — les rôles, le circuit du contenu, le transit, les conventions, les scripts, les invariants. **Ce fichier ne la duplique pas : il dit comment travailler dedans.**
>
> 📐 Ce qui reste à construire : [`PLAN.md`](../PLAN.md). Le vocabulaire fait foi dans [`THESAURUS.md`](../THESAURUS.md) — **un mot, une chose** : `protocole` ≠ `fiche de bibliothèque`, `chantier` ≠ `cible` ≠ `palier`, et « étape » a deux sens.

---

## Langue et conventions de travail

- **Toujours communiquer avec l'utilisateur en français.**
- **Toujours utiliser bash, jamais PowerShell.**
- **Toujours charger la skill `ay-typescript`** avant de toucher ou d'évaluer du TypeScript / JavaScript — écriture, revue de PR, application de corrections. **Aucune exception**, pas même pour un « petit » diff.
- **Le rôle et le nom ne se confondent pas** : les rôles sont `psy` · `superviseur` · `companion` · `patient` · `aidant` ; les noms sont **Claude Psy** · **Claude Superviseur** · **Kokoro** · **Xavier** · **Chourouk**. Un répertoire porte un rôle ; une phrase clinique nomme qui agit.
- 🔴 **Tout fichier de code ne contient pas de commentaire fait pour Xavier mais uniquement pour Claude dans le cas où c'est vraiement necessaire.**
- 🔴 **Tout fichier markdown écrit par le dispositif est concis, actionnable et acté** — ces fichiers sont rechargés en entier à chaque intervention, leur poids se paie en tokens à chaque session. Concrètement :
  - **Pas de version ni de changelog dans le corps du texte.**, pas d'un en-tête `v2.2` ou d'une section « historique des modifications ». Un fichier décrit ce qui est vrai *maintenant*.
  - **Pas de section « reste à trancher » qui s'accumule.** Une question ouverte se tranche avant d'écrire, ou se pose une fois à l'utilisateur — elle ne vit pas indéfiniment comme note en bas de fichier.
  - **Pas de récit du cheminement.** On écrit la conclusion, pas l'historique de la réflexion qui y a mené — sauf quand le *pourquoi* est lui-même l'information utile (ex. un invariant clinique dont l'origine évite une régression).
  - **Ne s'applique pas** aux documents source non écrits par le dispositif (`patient/ressources/`, `psy/docs/references/`) ni aux constats datés (`seances/`, `mesures/`, `supervision/`, `briefs/`) — un compte-rendu d'un jour donné est déjà acté par nature, ce n'est pas lui qu'on flatte.

---

## 🔴 Avant toute intervention clinique

**Charger ensemble, et jamais l'un sans l'autre :**

1. [`psy/outputs/dossier/profil.md`](../psy/outputs/dossier/profil.md) — le contexte **permanent**
2. [`psy/outputs/dossier/etat.md`](../psy/outputs/dossier/etat.md) — l'état **courant**

**En cas de doute clinique, la source qui fait foi est le rapport v2.4** ([`patient/ressources/Rapport psychiatrique et psychologique.md`](../patient/ressources/Rapport%20psychiatrique%20et%20psychologique.md)), **jamais une fiche.**

> ⭐ **Seule exception : la crise.** `psy-crise` applique la question de sécurité et la conduite **avant** la lecture du dossier — le temps est exactement ce qui manque. **L'exception est écrite, elle ne se déduit pas.**

**Avant d'écrire dans `psy/outputs/dossier/` ou `companion/outputs/`** → lire [`psy/DOSSIER.md`](../psy/DOSSIER.md) *(normatif)*.
**Avant d'écrire dans `companion/inputs/`** → lire [`companion/PROGRAMME.md`](../companion/PROGRAMME.md) *(normatif)*.
**Avant de publier quoi que ce soit** → lire [`superviseur/README.md`](../superviseur/README.md) *(normatif)*. **Aucune surface n'a le droit d'inventer un format.**

---

## Les invariants — ils ne se négocient pas

Ils viennent des contraintes de Xavier *(détail : [`patient/README.md`](../patient/README.md))*, pas d'un goût.

- **Aphantasie** → aucune technique de visualisation, nulle part, y compris dans un texte d'aide. **La consigne est inopérante, pas difficile.**
- **Shutdowns** → tout reste utilisable **sans parler ni écrire**.
- **Empathie cognitive déficitaire** → explicite, littéral, sans sous-entendu ni attente implicite.
- **Camouflage = moteur de l'anxiété** → zéro exigence de performance sociale, zéro jugement.
- **Réduire les charges, pas « motiver »** → **zéro streak, zéro compteur de régularité, zéro reproche d'assiduité.**
- **Hypersensibilités (4 canaux)** → UI sobre : pas de son surprise, pas de flash, pas d'animation brusque.
- **Rigidité / routines** → **la prévisibilité est une fonctionnalité** : annoncer avant de faire, aucun changement d'interface ni de format sans annonce.
- **Trois mécanismes de crise distincts** → panique *(exposition, respiration)* ≠ vasovagal *(tension appliquée)* ≠ shutdown *(mot-code, retrait, reprise différée)*. **La mauvaise parade aggrave.**
- ⭐ **Signal interne absent → structure externe** *(la règle centrale)* → ne jamais demander à Xavier de s'appuyer sur une perception qui lui manque : satiété, fatigue, tension, émotion. **Un échec antérieur documente une consigne inadaptée, jamais un manque de volonté.**
- ⭐ **On cote des comportements observables, pas des ressentis** *(R6)* → jamais « note ton anxiété sur 10 » ; toujours une ancre comportementale. **Et poser les questions sur les états internes explicitement et de façon fermée** — l'absence de plainte n'est pas une absence de problème.
- **Non-substitution** → aucun conseil de modification de traitement, jamais, **même sous forme interrogative**. Ça part au brief Dr Isorni.

> 🔴 **La fiche de crise qui fait foi est [`psy/docs/protocoles/crise-escalade.md`](../psy/docs/protocoles/crise-escalade.md)** — `profil.md` §4 n'en est que le résumé.
>
> 🔴 **Les numéros d'appel d'urgence — 15, 112, 114 — ont été retirés de tout le dispositif**, à la demande de Xavier : aucun n'a jamais servi · ⭐ **une syncope vasovagale ne s'appelle pas, elle s'allonge** · l'affichage permanent était anxiogène. **Ne jamais les réintroduire, sous aucune forme, dans aucune surface.**
>
> ⭐ **Le 3114 est le seul numéro conservé** — prévention du suicide, déclenché **uniquement** par une idéation suicidaire ou une détresse aiguë, **jamais affiché en ouverture ni « au cas où », et jamais dans Kokoro.** En shutdown il est inaccessible : c'est un numéro de téléphone. Voies sans parole : mot-code « shutdown », canal écrit.

---

## Les skills du dispositif

Elles vivent dans `.claude/skills/psy-*` — Claude Code ne les découvre que là.

| Skill | Rôle |
|---|---|
| `psy-seance` | **Séance de fond hebdomadaire** — ouverture / une seule cible / clôture obligatoire → `psy/outputs/dossier/seances/`. ⭐ **Battement du dispositif** : `psy:sync` en ouverture, supervision puis `psy:publish` en clôture. **Seule fenêtre d'écriture du programme** |
| `psy-journal` | Check-in quotidien — 7 questions fermées, < 2 min → `companion/outputs/journal/` |
| `psy-crise` | **Triage de crise** — sécurité avant mécanisme, panique / vasovagal / shutdown, escalade 3114, voies sans parole |
| `psy-bilan` | Passation et cotation d'une échelle → `psy/outputs/dossier/mesures/`. Items lus dans `psy/docs/corpus/echelles/`, **jamais restitués de mémoire** |
| `psy-brief-isorni` | Brief d'une page avant consultation → `psy/outputs/dossier/briefs/`, `transmis: false`. **Aucune proposition pharmacologique** |
| `psy-hygiene` | Versant somatique — ⭐ **le passage de palier se compte dans le journal, il ne se demande pas** |
| `psy-superviseur` | **Contre-expertise du dispositif** — 10 contrôles → `superviseur/outputs/`. 🔴 **Bloquant avant publication.** Il constate ; **la correction est un acte séparé** |

**Invariants de tout skill** : charger `profil.md` + `etat.md` avant d'agir · non-substitution · protocole de crise câblé · aucune visualisation · utilisable sans parler ni écrire · zéro streak · annoncer avant de faire.

---

## 🔴 Rien ne sort sans supervision

**Quatre points où une erreur atteint quelqu'un** — le programme publié, la bibliothèque publiée, les consignes de séance à deux *(vers Chourouk)*, le brief *(vers le Dr Isorni)*. **Chacun exige une passe du Superviseur portant explicitement sur la version qui sort**, et `npm run psy:publish` refuse mécaniquement sans elle.

**Un refus se corrige, il ne se contourne pas** — aucune option de forçage n'existe, et il ne doit jamais en exister une.

---

## Les scripts

Depuis la racine, **scopés par rôle** ; les chemins sont résolus par rapport à la racine du projet.

| Commande | Objet |
|---|---|
| `npm run psy:publish` | 🔴 Publie la thérapie et la bibliothèque vers Kokoro. **Ne se lance qu'à la clôture d'une séance** |
| `npm run psy:sync` | Verse au dépôt ce que Kokoro a écrit. **N'écrase jamais un fichier existant** |
| `npm run psy:pdf2md` · `psy:docx2md` · `psy:md2pdf` | Conversion de documents |
| `npm run companion:kokoro` | Compile, teste, installe et ouvre Kokoro. **Jamais `gradlew` ni `adb` à la main** |
| `npm run companion:image` · `companion:decoupe` · `companion:fondu` | Planches de recherche graphique, détourage du fond magenta, et fondu du bandeau de notification |
| `npm run companion:icone` | Fabrique les icônes de Kokoro depuis `ressources/retenus/logo.jpg`. 🔴 **Aucune image d'icône ne se retouche à la main** |
| `npm run typecheck` | `tsc --noEmit` |

⚠️ **`psy:pdf2md` ne détecte pas les cases cochées en couleur** : un questionnaire rempli se transcrit à la main, par lecture visuelle des pages.

---

## Où vivent les choses

| Répertoire | Objet |
|---|---|
| [`psy/`](../psy/README.md) | **Claude Psy** — `docs/` *(protocoles, corpus, références, gabarits)* · `outputs/dossier/` *(la mémoire longitudinale)* · `scripts/` · 🔴 [`DOSSIER.md`](../psy/DOSSIER.md) |
| [`companion/`](../companion/README.md) | **Kokoro** — `android/` *(le code)* · `inputs/` *(ce que le psy lui donne)* · `outputs/` *(ce qu'il produit)* · 🔴 [`companion/PROGRAMME.md`](../companion/PROGRAMME.md) · `CORPS.md` `DECOR.md` `INTERFACE.md` |
| [`superviseur/`](../superviseur/README.md) | **Claude Superviseur** — `outputs/`, **hors `dossier/`** |
| [`patient/`](../patient/README.md) | **Xavier** — `ressources/`, ses documents source. 🔴 **`ressources/originales/` est une archive : jamais une entrée** |
| [`aidant/`](../aidant/README.md) | **Chourouk** — `ressources/fiche-chourouk.md`, la seule chose écrite qu'elle reçoit |

⭐ **Deux archives `patient/ressources/originales/` et `psy/docs/references/originales/` existent et ne se confondent pas**. **Ne jamais lire leurs fichiers comme entrée** — les documents exploitables sont les Markdown à côté.
