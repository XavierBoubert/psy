# Psy

## Objectif principal

Créer un psychologue virtuel sur mesure pour Xavier : établir son diagnostic à partir de ses ressources (`ressources/xavier/`) et des références cliniques (`ressources/spécialisées/`), puis concevoir une thérapie complète et des outils adaptés à son profil.

Compétences (skills) : `ay-typescript` (patterns de typage), `ay-functional` (immutabilité, composition), `ay-refactor` (méthodologie de refactoring), `ay-api` (design REST / interfaces), `ay-12factor` (services déployables), `/ay-teach [topic]` (apprentissage structuré).

## Langue

Toujours communiquer avec l'utilisateur en français.

## Plan du projet (carte de l'espace de travail)

| Répertoire | Objet |
|-----------|---------|
| `ressources/originales/` | Documents source bruts (ex. PDF) |
| `ressources/spécialisées/` | Documents convertis, utilisés comme entrées pour Claude |
| `ressources/xavier/` | Ressources du profil de Xavier (celui qui prompt), utilisées pour établir son diagnostic et créer un psychologue adapté à lui |
| `scripts/` | Scripts Node.js/TypeScript autonomes (exécutés directement via le support TypeScript natif de Node, sans étape de build) |

## Ressources spécialisées

Le dossier `ressources/spécialisées/` contient les documents convertis (au format Markdown) destinés à être utilisés comme entrées par Claude.

| Fichier | Description |
|--------|-------------|
| `DSM-5_Manuel-diagnostique-et-statistique-des-troubles-mentaux.md` | Version Markdown du DSM-5 (Manuel diagnostique et statistique des troubles mentaux, 5e édition), généré à partir du PDF source via le script `pdf-to-markdown` |
| `DSM-5_TSA.md` | Extrait du DSM-5 : trouble du spectre de l'autisme |
| `DSM-5_TDAH.md` | Extrait du DSM-5 : déficit de l'attention/hyperactivité |
| `DSM-5_Anxio-depressif.md` | Extrait du DSM-5 : troubles dépressifs et troubles anxieux (incl. trouble anxieux généralisé) |

## Ressources originales

Le dossier `ressources/originales/` contient les documents source bruts (ex. PDF) ayant servi à générer les fichiers de `ressources/spécialisées/`.

**Ne jamais lire, ouvrir ou utiliser les fichiers de `ressources/originales/` comme entrée.** Ce dossier existe uniquement comme archive source pour la conversion ; les documents exploitables se trouvent dans `ressources/spécialisées/`.

## Ressources Xavier

Le dossier `ressources/xavier/` contient les ressources propres au profil de Xavier (l'utilisateur qui prompt) : réponses à des questionnaires, historique, éléments personnels, etc. Ces ressources servent à établir son diagnostic et à concevoir un psychologue virtuel et une thérapie adaptés à lui.

| Fichier | Description |
|--------|-------------|
| `Evaluation Xavier.md` | Compte-rendu d'évaluation psychologique complet (Emeline Saley, psychologue clinicienne, 5 avril 2024) : anamnèse, tests passés, synthèse des questionnaires, récapitulatif des troubles observés et conclusion diagnostique (Trouble du Spectre Autistique de légère intensité / syndrome d'Asperger, à confirmer par un psychiatre) |
| `Dossier RQTH.md` | Dossier de demande RQTH (formulaire MDPH Cerfa 15692*01) déposé le 05/12/2024, incluant le certificat médical du Dr Jean-Baptiste ISORNI (TSA type Asperger diagnostiqué par le Dr Lamia Kias en 2023, trouble anxieux, agoraphobie, crises d'angoisse aiguë avec dépersonnalisation/déréalisation, traitement par Venlafaxine) et un justificatif de domicile |
| `Quotient du Spectre Autistique QA.md` | Réponses de Xavier au questionnaire AQ (Autism-Spectrum Quotient, Baron-Cohen et al., 2001) — réponses cochées uniquement |
| `Quotient d'Empathie EQ.md` | Réponses de Xavier au questionnaire EQ (Empathy Quotient / Cambridge Behaviour Scale, Baron-Cohen & Wheelwright, 2004) — réponses cochées uniquement |
| `Echelle-syndrome-Asperger.md` | Réponses au questionnaire Échelle du syndrome d'Asperger (selon Attwood), rempli par la mère de Xavier — réponses cochées uniquement |
| `Inventaire-du-stress.md` | Réponses à l'Inventaire du stress (The Groden Center Inc.) avec récapitulatif des scores par catégorie — réponses cochées uniquement |
| `Question-aux-parents.md` | Réponses de la mère de Xavier à un questionnaire sur les antécédents familiaux, la grossesse, le développement et l'enfance |
| `Rapport psychiatrique et psychologique.md` | Rapport de synthèse complet généré par Claude (v2.0, 08/08/2026) à partir de toutes les ressources ci-dessus + compléments anamnestiques directs de Xavier + DSM-5 + littérature en ligne : fiche patient détaillée, chronologie 1986-2026, anamnèse, re-cotation indépendante AQ (39/50) et EQ (9/80), analyse critérielle DSM-5 (TSA niveau 1 confirmé ; agoraphobie avec attaques de panique attendues ; phobie sang-injection-accident avec syncopes vasovagales fortement probable ; TAG probable ; trouble panique écarté), aphantasie et shutdowns intégrés au profil, confirmation du diagnostic d'Emeline Saley, 18 enseignements et recommandations. Sert de document de référence du profil de Xavier |

Les questionnaires notés « réponses cochées uniquement » ont été convertis manuellement (lecture visuelle des pages du PDF source, `pdf-to-markdown` étant incapable de détecter les cases cochées en couleur) : seule la réponse retenue est conservée, les autres choix possibles ont été omis.

## Scripts

Exécuter depuis le répertoire racine (`npm run <name> -- <args>`). Les arguments source/destination sont résolus par rapport à la racine du projet, et non au répertoire de travail courant.
**Implémentation** en suivant `ay-typescript` / `ay-functional`.

| Script | Description |
|--------|-------------|
| `pdf-to-markdown` | Convertit un fichier PDF en Markdown. Usage : `npm run pdf-to-markdown -- <source.pdf> <destination.md>` |
| `docx-to-markdown` | Convertit un fichier DOCX en Markdown. Usage : `npm run docx-to-markdown -- <source.docx> <destination.md>` |

## Contraintes

- **Toujours utiliser bash, jamais PowerShell**
- **Toujours charger la skill `ay-typescript`** avant de toucher ou d'évaluer du TypeScript / JavaScript : écriture de code, revue de PR (`inside-pr-review`), ou application de corrections de revue (`pr-fix`). Aucune exception pour les « petits » diffs ou les passes de revue seule.
