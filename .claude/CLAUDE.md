# Psy

## Objectif principal

Créer un **psychologue/psychiatre virtuel basé sur Claude, spécifiquement conçu pour Xavier** — un dispositif complet, pas un chatbot :

1. **Expertise** — une compétence clinique supérieure à celle d'un psy français généraliste sur le profil exact de Xavier (TSA niveau 1, agoraphobie, phobie sang-injection-accident, TAG, aphantasie, camouflage, shutdowns).
2. **Suivi** — un accompagnement psychologique et psychiatrique continu, **en complément du Dr Isorni et jamais en substitution** (aucun conseil de modification de traitement ; protocole de crise câblé, 3114).
3. **Thérapies** — un programme thérapeutique structuré, outillé par des applications sur mesure (EMDR à stimulations bilatérales, tension appliquée, exposition graduée, etc.).
4. **Hygiène de vie** — un programme d'activité physique, d'alimentation, de sommeil et de récupération adapté à ses contraintes.
5. **Présence** — **Kokoro (心)**, un visage numérique sur Android, en surimpression permanente, pour un suivi en temps réel.

**Phase 1 (terminée)** : diagnostic établi à partir de `ressources/xavier/` et `ressources/spécialisées/` → `ressources/xavier/Rapport psychiatrique et psychologique.md` (v2.0), document de référence du profil.
**Phase 2 (en cours)** : conception et construction du dispositif → voir `PLAN.md` à la racine.

### Contraintes de conception non négociables (issues du rapport v2.0)

- **Aphantasie** → aucune technique de visualisation ; verbal, corporel, exposition in vivo uniquement.
- **Shutdowns** → toute interface doit rester utilisable sans parler ni écrire.
- **Empathie cognitive déficitaire** → communication explicite, littérale, sans sous-entendu ni attente implicite.
- **Camouflage = moteur de l'anxiété** → zéro exigence de performance sociale, zéro jugement.
- **Réduire les charges, pas « motiver »** → pas de gamification culpabilisante ni de streaks punitifs.
- **Hypersensibilités (4 canaux)** → UI sobre : pas de son surprise, pas de flash, pas d'animation brusque.
- **Rigidité / routines** → la prévisibilité est une fonctionnalité : aucun changement d'interface non annoncé.
- **Deux mécanismes de crise distincts** → panique (exposition/respiration) ≠ vasovagal (tension appliquée) ; ne jamais les confondre.

Compétences (skills) : `ay-typescript` (patterns de typage), `ay-functional` (immutabilité, composition), `ay-refactor` (méthodologie de refactoring), `ay-api` (design REST / interfaces), `ay-12factor` (services déployables), `/ay-teach [topic]` (apprentissage structuré).

## Langue

Toujours communiquer avec l'utilisateur en français.

## Plan du projet (carte de l'espace de travail)

| Répertoire | Objet |
|-----------|---------|
| `PLAN.md` (racine) | **Plan de conception du dispositif** (v1.0, brainstorming clos en 7 tours) : 5 axes + axe transversal sécurité/éthique, contraintes de conception, architecture à trois surfaces, feuille de route en 7 étapes, journal des décisions |
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
| `Biopsie hépatique - Dr Bouarioua.md` | Résultats de la biopsie hépatique de juillet 2026 (courriel de la Dr Leila Bouarioua, hépato-gastro-entérologue, versé au dossier le 08/08/2026) : **stéatose hépatique liée au surpoids, sans fibrose**, aucune surveillance particulière, perte de poids impérative ; traitement psychotrope non imputable. Inclut l'analyse d'articulation avec le dossier psychiatrique — apport alimentaire doublé et **absence de perception de la satiété** (élément clinique nouveau), hypothèse du **déficit intéroceptif** comme extension corporelle de l'alexithymie, et la règle de conception « signal interne absent → structure externe ». À intégrer au rapport en v2.1 (liste des points en fin de fichier) |
| `Rapport psychiatrique et psychologique.md` | **Document de référence du profil de Xavier.** Rapport de synthèse complet généré par Claude (**v2.1, 08/08/2026**) à partir de toutes les ressources ci-dessus + compléments anamnestiques directs de Xavier + DSM-5 + littérature en ligne : fiche patient détaillée, chronologie 1986-2026, anamnèse, re-cotation indépendante AQ (39/50) et EQ (9/80), analyse critérielle DSM-5 (TSA niveau 1 confirmé ; agoraphobie avec attaques de panique attendues ; phobie sang-injection-accident avec syncopes vasovagales fortement probable ; TAG probable ; trouble panique écarté), aphantasie et shutdowns intégrés au profil, confirmation du diagnostic d'Emeline Saley, **22 enseignements** et recommandations.<br>**v2.1** ajoute le versant somatique : stéatose hépatique (MASLD) sans fibrose confirmée par biopsie, obésité de classe II (IMC 35,1), **§6.5 conduite alimentaire et déficit intéroceptif**, hyperphagie boulimique non retenue (BES à passer), **4e hypothèse SAOS** au différentiel de la distractibilité (à éliminer avant le DIVA-5), **§10.7 versant somatique** (alimentation et activité physique requalifiées en prescription médicale), et la règle de conception **« signal interne absent → structure externe »** (§9.19) |

Les questionnaires notés « réponses cochées uniquement » ont été convertis manuellement (lecture visuelle des pages du PDF source, `pdf-to-markdown` étant incapable de détecter les cases cochées en couleur) : seule la réponse retenue est conservée, les autres choix possibles ont été omis.

## Scripts

Exécuter depuis le répertoire racine (`npm run <name> -- <args>`). Les arguments source/destination sont résolus par rapport à la racine du projet, et non au répertoire de travail courant.
**Implémentation** en suivant `ay-typescript` / `ay-functional`.

| Script | Description |
|--------|-------------|
| `pdf-to-markdown` | Convertit un fichier PDF en Markdown. Usage : `npm run pdf-to-markdown -- <source.pdf> <destination.md>` |
| `docx-to-markdown` | Convertit un fichier DOCX en Markdown. Usage : `npm run docx-to-markdown -- <source.docx> <destination.md>` |
| `markdown-to-pdf` | Convertit un fichier Markdown en PDF (rendu via Puppeteer/Chromium headless). Usage : `npm run markdown-to-pdf -- <source.md> <destination.pdf>` |

## Contraintes

- **Toujours utiliser bash, jamais PowerShell**
- **Toujours charger la skill `ay-typescript`** avant de toucher ou d'évaluer du TypeScript / JavaScript : écriture de code, revue de PR (`inside-pr-review`), ou application de corrections de revue (`pr-fix`). Aucune exception pour les « petits » diffs ou les passes de revue seule.
