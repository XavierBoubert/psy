# Psy — le dispositif

**Un psychologue/psychiatre virtuel basé sur Claude, conçu pour Xavier.** Un dispositif, pas un chatbot.

> 📖 **Ce README décrit ce que le dispositif *est*.** Ce qui n'est pas encore fait est dans [`psy/PLAN.md`](psy/PLAN.md) — **le plan appartient à Claude Psy** ; le vocabulaire fait foi dans [`THESAURUS.md`](THESAURUS.md) — *un mot, une chose*.

---

## 1. Les cinq rôles

**Le dépôt s'organise par rôle, pas par nature de fichier.** Chaque rôle a **un** répertoire, et ce qu'il produit, reçoit et documente y tient. Un rôle doit rester lisible — et un jour extractible — sans démêler les autres.

| Rôle | Répertoire | Nom | Ce qu'il est |
|---|---|---|---|
| **psy** | [`psy/`](psy/README.md) | **Claude Psy** | **Le psychiatre et le psychologue.** Une séance de fond par semaine. **Il construit tout le contenu** et le donne à Kokoro |
| **superviseur** | [`superviseur/`](superviseur/README.md) | **Claude Superviseur** | **Le superviseur du psy.** Il supervise **Claude, jamais Xavier**. 🔴 **Sa passe est bloquante avant toute publication** |
| **companion** | [`companion/`](companion/README.md) | **Kokoro (心)** | **Le compagnon du patient**, sur son téléphone. La **seule** surface tournée vers Xavier |
| **patient** | [`patient/`](patient/README.md) | **Xavier** | **Le patient.** Ses documents source — pas un utilisateur à engager |
| **aidant** | [`aidant/`](aidant/README.md) | **Chourouk** | **La personne qui tient le téléphone** pendant une séance à deux. 🔴 **Elle n'est pas thérapeute** |

> ⭐ **Le rôle et la personne ne se confondent pas.** `aidant` est une fonction ; **Chourouk** la tient aujourd'hui. Ce qui est écrit pour l'aidant vaut pour quiconque la tiendra.
>
> ⭐ **Kokoro ne vient jamais vers Xavier.** Aucune notification, aucune relance, aucun reproche. **Xavier vient à lui, et y trouve tout.** *(Seule exception : l'accès crise sur l'écran verrouillé — une porte, pas un rappel.)*

### Ce que le dispositif n'est pas

- **Il ne remplace pas le Dr Isorni.** Non-substitution absolue : aucun conseil de modification de traitement, jamais, même sous forme interrogative. Toute question pharmacologique part au brief.
- **Il n'a ni le corps, ni la prescription, ni la responsabilité légale, ni l'alliance thérapeutique humaine.** Un psychologue en présentiel reste structurellement irremplaçable sur l'exposition in vivo accompagnée, l'EMDR encadré et la validation de l'acquisition de la tension appliquée. **C'est une dette assumée, pas un oubli.**
- **Il ne motive pas.** Les renforçateurs de Xavier fonctionnent normalement. **Il n'y a rien à motiver ; il y a des charges à réduire et des repères à fournir.**

---

## 2. Le circuit du contenu

```
Claude Psy ── programme · bibliothèque · bilans ──► Kokoro ── journal + réponses ──► dossier
     ▲               (companion/inputs/)                     (companion/outputs/)       │
     │           🔴 supervision bloquante                                                │
     └────────────────────────── Claude Superviseur ◄───────────────────────────────────┘
                                 (superviseur/outputs/)
```

**Claude Psy écrit tout le contenu, Claude Superviseur le vise, Kokoro l'affiche, Xavier l'utilise, et ce que Kokoro recueille revient au dossier.** L'aidant n'entre dans la boucle que sur une étape `seance-duo`, et n'y lit que des consignes.

**Les deux mouvements sont scriptés, jamais faits à la main :**

| Sens | Commande | Ce qui passe |
|---|---|---|
| PC → Kokoro | **`npm run psy:publish`** | `companion/inputs/programme.json` + `companion/inputs/bibliotheque/` + `companion/inputs/bilans/` |
| Kokoro → PC | **`npm run psy:sync`** | `companion/outputs/journal/` + `companion/outputs/reponses/` |

🔴 **`psy:publish` refuse la publication entière** si un invariant est enfreint ou si la supervision de la version qui sort manque *(voir [`superviseur/README.md`](superviseur/README.md))*. **Un refus se corrige, il ne se contourne pas** — aucune option de forçage n'existe, et il ne doit jamais en exister une.

⭐ **Les deux surfaces vérifient les mêmes interdits et ne réagissent pas pareil, volontairement.** Le PC **refuse tout** — ici on peut corriger, donc on corrige. **Kokoro écarte la seule ligne fautive et affiche le reste** — sur le téléphone on ne peut rien corriger, et perdre tout le programme pour une ligne serait pire.

⭐ **La documentation se publie à tout moment ; le reste du programme se publie à la clôture d'une séance.** Une fiche est à portée dès qu'elle est écrite et supervisée — **Xavier n'attend pas la séance suivante pour comprendre ce qui lui arrive**. Les étapes qui font agir — `ecran`, `exercice`, `questionnaire`, `demarche`, `seance-duo` — se décident avec lui, en séance. **La supervision est bloquante dans les deux cas, et toute publication s'annonce à Xavier au moment où elle se fait.**

### 🔴 Les cinq points où une erreur sort du dispositif

Une erreur interne se corrige. Une erreur qui **sort** atteint quelqu'un.

| Sortie | Vers qui | Contrôle |
|---|---|---|
| **Le programme publié** | Xavier, **sans intermédiaire pour objecter** | `npm run psy:publish` *(mécanique)* **+ supervision bloquante** |
| **La bibliothèque publiée** | Xavier, idem | Identique — **C9 s'applique ici en premier** |
| **Un bilan publié** | Xavier, idem | Identique, **mais C9 ne s'y applique pas** — la question est : **ce document ne contient rien que Xavier ne sache déjà** |
| **Les consignes de séance à deux** | **L'aidant** | Identique, **plus C10** |
| **Le brief** | Le Dr Isorni | **Supervision bloquante** + Xavier relit et décide de transmettre |

**Rien d'autre ne sort.** Ni le dossier, ni les séances, ni les crises, ni les mesures.

---

## 3. 🔴 Le transit — Google Drive *(normatif)*

**Deux mécanismes, deux rôles qui ne se confondent pas :**

| Mécanisme | Rôle | Périmètre |
|---|---|---|
| **Dépôt git privé** `github.com/XavierBoubert/psy` | ⭐ **Historique, archive, source de vérité.** Traçabilité clinique : qui a écrit quoi, quand, avec retour arrière | **Tout le dépôt**, y compris tout ce qui transite par Drive |
| **Google Drive** | ⭐ **Le contenu vivant, dans les deux sens** | La table ci-dessous, et rien d'autre |

**Pourquoi pas git comme canal Android :** il n'existe pas de client git confortable sur Android, et une app qui doit écrire en un geste ne peut pas dépendre d'un `commit`/`push`. **Pourquoi pas Drive comme archive :** il synchronise, il n'archive pas — une erreur d'écriture s'y propage.

### Le périmètre

> **Le critère n'est pas clinique, il est fonctionnel : transite ce dont Kokoro a besoin, et ce que Kokoro produit et dont les Claude ont besoin. Rien d'autre.**

| Sens | Ce qui transite | Auteur unique | Versé dans |
|---|---|---|---|
| **PC → Kokoro** | `programme.json` — la thérapie du moment | Claude Psy | *(source : `companion/inputs/`)* |
| **PC → Kokoro** | `bibliotheque/*.pdf` — la documentation accessible à Xavier. ⭐ **Le Markdown ne part pas** : `psy:publish` le convertit en PDF, et Kokoro le confie au lecteur du téléphone | Claude Psy | *(source : `companion/inputs/bibliotheque/*.md`)* |
| **PC → Kokoro** | `bilans/*.pdf` — les comptes rendus que Xavier possède déjà. ⭐ **Canal distinct de la bibliothèque** : un bilan n'est ni écrit pour lui, ni lisible par l'aidant | Claude Psy | *(source : `companion/inputs/bilans/*.md`)* |
| **Kokoro → PC** | `journal/AAAA-MM-JJ.json` — les check-ins | Kokoro | `companion/outputs/journal/` |
| **Kokoro → PC** | `reponses/AAAA-MM-JJ-HHMM-<id>.json` — ce qui a été fait | Kokoro | `companion/outputs/reponses/` |

**Ce qui ne transite jamais — et la liste est fermée :** `profil.md` · `etat.md` · `seances/` · `crises/` · `mesures/` · `briefs/` · `psy/docs/gabarits/` · `superviseur/outputs/` · `psy/docs/corpus/` · `psy/docs/protocoles/` *(à l'état brut)* · `psy/docs/references/` · `patient/ressources/` · `aidant/ressources/` · le code · `.git`.

> ⭐ **La distinction qui tient tout : le contenu publié est *dérivé*, jamais *extrait*.** Le programme porte **ce qu'il y a à faire**, jamais ce qui a été constaté, mesuré ou diagnostiqué. Une fiche de bibliothèque est **réécrite pour Xavier**, jamais copiée d'un protocole. C'est le contrôle **C9**.
>
> ⚠️ **Le bilan est la seule exception, et elle est écrite** : ni dérivé ni extrait, **c'est un compte rendu que Xavier possède déjà**, versé tel quel par le canal `bilans/`. **C9 n'y a pas de prise** — la question devient : *ce document ne contient rien qu'il ne sache déjà*.
>
> ✅ **Aucun fichier n'a deux auteurs.** `programme.json`, `bibliotheque/` et `bilans/` sont écrits par le PC seul ; `journal/` et `reponses/` par Kokoro seul. **C'est une condition de l'arbitrage, pas une observation** — c'est ce qui rend le risque de conflit tolérable.

### L'arborescence du transit

```
H:\Mon Drive\kokoro\               ← hors dépôt, jamais partagé
  programme.json                   ← PC écrit,    Kokoro lit
  bibliotheque/
    <id>.pdf                       ← PC écrit,    Kokoro lit
  bilans/
    <id>.pdf                       ← PC écrit,    Kokoro lit
  journal/
    AAAA-MM-JJ.json                ← Kokoro écrit, PC lit
  reponses/
    AAAA-MM-JJ-HHMM-<id>.json      ← Kokoro écrit, PC lit
```

### Les règles qui ne se relâchent pas

- 🔴 **Tout ce qui passe par Drive est versé au dépôt et versionné.** Drive n'est jamais la seule copie de quoi que ce soit.
- ⚠️ **Ne jamais faire pointer Drive sur `c:\p\psy`.** Le dépôt n'est synchronisé par aucun service ; le dossier de transit est **hors dépôt**.
- **Conditions du dépôt** : privé · 2FA + clé SSH · aucun fork, collaborateur ni GitHub Action ayant accès au contenu.
- **Conditions du Drive** : 2FA sur le compte Google · dossier **jamais partagé**, aucun lien, aucun destinataire · **aucune application tierce** autorisée dessus.
- **Hors GitHub, hors Google Drive et hors appels à Claude, aucune donnée ne part vers un tiers** — pas de cloud santé, pas de service d'analyse externe, pas de télémétrie. **Toute proposition d'ajouter un service tiers est refusée par défaut.**
- ⚠️ **Toute extension du périmètre transporté est un arbitrage neuf**, à instruire et à tracer — jamais une continuation du précédent.
- ⚠️ **Drive accepte deux fichiers du même nom sans rien signaler.** Il crée `2026-08-11 (1).json`. **Un fichier de ce nom ne se supprime jamais sans être lu : c'est une donnée clinique.** Procédure : lire les deux versions, fusionner à la main dans le dépôt, committer la fusion.

> ⚠️ **Le compte de transit est le compte professionnel `xavier@allons-y.io`** — micro-entreprise, la note passe en frais de société. **L'objection du dispositif reste écrite : un espace lié à l'activité n'est pas le meilleur domicile pour des données de santé.** Elle est acceptée, pas levée ; elle redevient un sujet si la structure gagne un associé, un comptable avec accès ou un administrateur Workspace.
>
> **Porte de sortie**, si l'un de ces arbitrages est révisé : chiffrement au repos par `git-crypt` ou `age` sur `psy/outputs/dossier/` et `patient/ressources/`. Contrepartie : Claude Code ne lit plus rien sans déverrouillage.

---

## 4. La convention des répertoires

| Nom | Ce qu'il contient | Qui y écrit |
|---|---|---|
| `<role>/` *(racine)* | **Sa documentation** — ce que le rôle est et comment il travaille | Claude, hors séance |
| `<role>/ressources/` | **Ce dont le rôle a besoin pour travailler** — sources, référentiels, planches | Claude, hors séance |
| `<role>/outputs/` | **Ce que le rôle produit** | Le rôle, et lui seul |
| `<role>/inputs/` | **Ce qu'un autre rôle lui donne** | L'autre rôle |
| `<role>/scripts/` | **Les scripts qui servent ce rôle** | — |

🔴 **Il n'y a pas de `ressources/` ni de `scripts/` à la racine.** Un fichier qui n'appartient à aucun rôle n'a pas de domicile — c'est le signe qu'il faut nommer son rôle, pas créer un fourre-tout.

⭐ **`inputs/` dit à qui la chose est destinée, pas qui l'a tapée.** Le programme est écrit par Claude Psy et vit pourtant dans `companion/inputs/` : il est **donné** à Kokoro.

**Deux exceptions, et elles sont motivées :**

- **`THESAURUS.md` reste à la racine** : il n'est à aucun rôle, il est au dispositif. ⭐ **`PLAN.md`, lui, a rejoint [`psy/`](psy/PLAN.md)** — ce qui reste à faire est du travail de Claude Psy, et **un plan sans propriétaire ne se met à jour nulle part.**
- **Les skills vivent dans `.claude/skills/psy-*`** — Claude Code ne les découvre que là. Ce n'est pas un choix d'organisation, c'est une contrainte de l'outil.

---

## 5. Les documents normatifs

**Aucune surface n'a le droit d'inventer un format.**

| Document | Ce qu'il fixe |
|---|---|
| 🔴 [`psy/DOSSIER.md`](psy/DOSSIER.md) | **Le format du dossier clinique** — les six règles invariables, l'arborescence, et le contrat de chaque type de fichier |
| 🔴 [`companion/PROGRAMME.md`](companion/PROGRAMME.md) | **Le format du programme et de la bibliothèque** — les six types d'étape, les rubriques, les interdits vérifiés à la publication |
| 🔴 [`superviseur/README.md`](superviseur/README.md) | **La supervision** — les dix contrôles, et le câblage qui la rend bloquante |
| 🔴 [`THESAURUS.md`](THESAURUS.md) | **Le vocabulaire** — un mot, une chose |
| 🔴 §3 ci-dessus | **Le transit** — ce qui a le droit de quitter le PC |

---

## 6. Les scripts

Tous s'exécutent depuis la racine, et **les arguments de chemin sont résolus par rapport à la racine du projet**, jamais au répertoire courant. **Ils sont scopés par rôle**, et leur source vit chez le rôle qu'ils servent.

| Commande | Source | Objet |
|---|---|---|
| `npm run psy:publish` | `psy/scripts/psy-publish.ts` | 🔴 **Publie la thérapie, la bibliothèque et les bilans vers Kokoro.** Valide le programme, chaque fiche et chaque bilan, **vérifie la supervision**, **convertit en PDF** *(seuls les documents qui ont changé)*, **retire du transit ce que le programme n'appelle plus**, et **refuse la publication entière** au moindre manquement. ⭐ **Sans `--seance`, une étape qui fait agir nouvelle ou modifiée est refusée** — seules la documentation et les bilans partent hors séance. `--refaire` reconvertit tout |
| `npm run psy:sync` | `psy/scripts/psy-sync.ts` | ⭐ **Verse au dépôt tout ce que Kokoro a écrit** — `journal/` et `reponses/`. **N'écrase jamais un fichier existant**, valide chaque fichier, signale tout nom hors convention |
| `npm run psy:pdf2md` | `psy/scripts/psy-pdf2md.ts` | Convertit un PDF en Markdown. `-- <source.pdf> <destination.md>` |
| `npm run psy:docx2md` | `psy/scripts/psy-docx2md.ts` | Convertit un DOCX en Markdown. `-- <source.docx> <destination.md>` |
| `npm run psy:md2pdf` | `psy/scripts/psy-md2pdf.ts` | Convertit un Markdown en PDF *(Puppeteer/Chromium headless)*. ⭐ **Format liseuse — page 90 × 155 mm, corps 12 pt** : le PDF se lit ajusté à la largeur d'un écran de téléphone, une page par écran. `-- <source.md> <destination.pdf>` |
| `npm run companion:kokoro` | `companion/android/kokoro` | ⭐ **Compile, teste, installe et ouvre Kokoro sur le téléphone**, ⭐ **par le Wi-Fi, sans câble** *(15/08/2026 — le lien se renoue tout seul)*. C'est le même script que `./kokoro` — il existe pour que **Xavier déploie sans passer par Claude** |
| `npm run companion:image` | `companion/scripts/companion-image.ts` | Génère des planches de recherche graphique via Gemini |
| `npm run companion:decoupe` | `companion/scripts/companion-decoupe.ts` | Détoure une planche : le fond magenta `#FF00FF` devient le canal alpha |
| `npm run companion:fondu` | `companion/scripts/companion-fondu.ts` | Fond le bandeau de notification dans le décor |
| `npm run companion:icone` | `companion/scripts/companion-icone.ts` | Fabrique les icônes de Kokoro — le lanceur et la notification. 🔴 **Aucune image d'icône ne se retouche à la main** |
| `npm run typecheck` | — | `tsc --noEmit` sur `psy/scripts/` et `companion/scripts/` |

⭐ **`psy:publish` et `psy:sync` portent le chemin du transit Drive en dur** : c'est la forme de tous les jours. Pour un autre dossier de transit, appeler le script directement — `node psy/scripts/psy-publish.ts <dossier>`.

**Implémentation** : TypeScript strict, exécuté directement par Node sans étape de build. `tsconfig.json` reste à la racine et couvre les deux répertoires de scripts.

⚠️ **`psy:pdf2md` ne détecte pas les cases cochées en couleur** : un questionnaire rempli se transcrit à la main, par lecture visuelle des pages.

---

## 7. Les invariants du dispositif

Ils viennent des contraintes de Xavier *(détail : [`patient/README.md`](patient/README.md))*. **Aucun n'est une préférence.**

- **Aucune visualisation**, nulle part, y compris dans un texte d'aide. Aphantasie mesurée : la consigne est **inopérante**, pas difficile.
- **Tout reste utilisable sans parler ni écrire** — choix fermés, compteurs, mot-code.
- **On cote des comportements observables, pas des ressentis** *(règle R6)*. Jamais « note ton anxiété sur 10 ».
- ⭐ **Signal interne absent → structure externe.** Ne jamais demander à Xavier de s'appuyer sur une perception qui lui manque : satiété, fatigue, tension, émotion.
- **Zéro streak, zéro compteur de régularité, zéro reproche d'assiduité.**
- **Non-substitution** — aucun conseil de modification de traitement, jamais, même sous forme interrogative.
- **Les trois mécanismes de crise ne se confondent jamais** : panique ≠ vasovagal ≠ shutdown. **La mauvaise parade aggrave.**
- **Annoncer avant de faire.** La prévisibilité est une fonctionnalité : aucun changement d'interface ni de format sans annonce préalable.
- **Aucun numéro d'appel d'urgence dans aucune surface.** Le **3114** est le seul conservé, sur le seul déclencheur de l'idéation suicidaire, **et il n'entre jamais dans Kokoro** : c'est une conduite d'escalade, pas un bouton.

---

## 8. Par où on entre

| Je veux… | Fichier |
|---|---|
| 🔴 **Faire face à une crise, maintenant** | [`psy/docs/protocoles/crise-escalade.md`](psy/docs/protocoles/crise-escalade.md) — **prime sur tout le reste** |
| Savoir qui est Xavier avant de lui parler | [`psy/outputs/dossier/profil.md`](psy/outputs/dossier/profil.md) |
| Savoir où on en est aujourd'hui | [`psy/outputs/dossier/etat.md`](psy/outputs/dossier/etat.md) |
| Trancher un point clinique | [`patient/ressources/Rapport psychiatrique et psychologique.md`](patient/ressources/Rapport%20psychiatrique%20et%20psychologique.md) — **c'est lui qui fait foi**, jamais une fiche |
| Écrire ou lire une donnée du dossier | [`psy/DOSSIER.md`](psy/DOSSIER.md) *(normatif)* |
| Écrire ou publier le programme | [`companion/PROGRAMME.md`](companion/PROGRAMME.md) *(normatif)* |
| Employer le bon mot | [`THESAURUS.md`](THESAURUS.md) |
| Savoir ce qui n'est pas encore fait | [`psy/PLAN.md`](psy/PLAN.md) |

> 🔴 **Avant toute intervention clinique**, charger [`psy/outputs/dossier/profil.md`](psy/outputs/dossier/profil.md) *(contexte permanent)* **et** [`psy/outputs/dossier/etat.md`](psy/outputs/dossier/etat.md) *(état courant)*, **ensemble et jamais l'un sans l'autre**. Le premier dit *qui est Xavier*, le second *où on en est*.
>
> ⭐ **Seule exception : la crise.** La question de sécurité et la conduite s'appliquent **avant** la lecture du dossier — le temps est exactement ce qui manque. **L'exception est écrite, elle ne se déduit pas.**
