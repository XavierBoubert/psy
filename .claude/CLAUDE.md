# Psy

## Objectif principal

Créer un **psychologue/psychiatre virtuel basé sur Claude, spécifiquement conçu pour Xavier** — un dispositif complet, pas un chatbot :

1. **Expertise** — une compétence clinique supérieure à celle d'un psy français généraliste sur le profil exact de Xavier (TSA niveau 1, agoraphobie, phobie sang-injection-accident, TAG, aphantasie, camouflage, shutdowns).
2. **Suivi** — un accompagnement psychologique et psychiatrique continu, **en complément du Dr Isorni et jamais en substitution** (aucun conseil de modification de traitement ; protocole de crise câblé, 3114).
3. **Thérapies** — un programme thérapeutique structuré, outillé par des applications sur mesure (EMDR à stimulations bilatérales, tension appliquée, exposition graduée, etc.).
4. **Hygiène de vie** — un programme d'activité physique, d'alimentation, de sommeil et de récupération adapté à ses contraintes.
5. **Présence** — **Kokoro (心)**, un visage numérique sur Android, en surimpression permanente, pour un suivi en temps réel.

**Phase 1 (terminée)** : diagnostic établi à partir de `ressources/xavier/` et `ressources/spécialisées/` → `ressources/xavier/Rapport psychiatrique et psychologique.md` (**v2.4**), document de référence du profil.
**Phase 2 (en cours)** : conception et construction du dispositif → `PLAN.md` (plan) et `psy/` (réalisation). **Étape 0 — socle minimal : close (09/08/2026). Étape 1 — Axe D (prescription médicale) : ouverte, les trois protocoles sont écrits dans `psy/protocoles/`. Étape 2 — instrumentation du suivi : ouverte (09/08/2026), les instruments de mesure sont versés dans `psy/corpus/echelles/`, le check-in quotidien démarre.**

> **Chantier n° 1 en cours : la reprise de la PPC par désensibilisation** (SAOS sévère **insuffisamment traité** — usage très irrégulier, IAH résiduel < 6/h sous appareil). Fiche : `psy/protocoles/ppc-desensibilisation.md`. Les deux autres — alimentation à structure externe, activité physique sans impact — sont écrites et démarrent après. **À partir du palier 1, un seul chantier progresse à la fois.**
>
> ⏱️ **Deux échéances structurent le trimestre** : **consultation Dr Isorni le 03/09/2026 à 12h30** (la dernière avant fin septembre) et **départ en Tunisie le 07/09/2026 pour 3 semaines ou plus**. Détail et conséquences : `psy/dossier/etat.md` §1.

> **Avant toute intervention clinique** (séance, check-in, brief, protocole, outil), charger **`psy/dossier/profil.md`** (contexte permanent) **et `psy/dossier/etat.md`** (état courant), ensemble et jamais l'un sans l'autre. En cas de doute clinique, la source qui fait foi est le rapport v2.4, pas la fiche.
> **Avant d'écrire quoi que ce soit dans `psy/dossier/`**, lire **`psy/dossier/SCHEMA.md`** — il est normatif ; aucune surface n'a le droit d'inventer un format.

### Contraintes de conception non négociables (issues du rapport v2.4)

- **Aphantasie** → aucune technique de visualisation ; verbal, corporel, exposition in vivo uniquement.
- **Shutdowns** → toute interface doit rester utilisable sans parler ni écrire.
- **Empathie cognitive déficitaire** → communication explicite, littérale, sans sous-entendu ni attente implicite.
- **Camouflage = moteur de l'anxiété** → zéro exigence de performance sociale, zéro jugement.
- **Réduire les charges, pas « motiver »** → pas de gamification culpabilisante ni de streaks punitifs.
- **Hypersensibilités (4 canaux)** → UI sobre : pas de son surprise, pas de flash, pas d'animation brusque.
- **Rigidité / routines** → la prévisibilité est une fonctionnalité : aucun changement d'interface non annoncé.
- **Deux mécanismes de crise distincts** → panique (exposition/respiration) ≠ vasovagal (tension appliquée) ; ne jamais les confondre. *(Un troisième, le shutdown, a sa propre parade : mot-code, retrait, reprise différée.)*
- ⭐ **Signal interne absent → structure externe** (règle centrale, §9.19) → ne jamais demander à Xavier de s'appuyer sur une perception qui lui manque (satiété, fatigue, tension, émotion). Trois échecs documentés — « imaginez un lieu sûr », « écoutez votre satiété », « portez le masque toute la nuit » — relevaient de la conception, pas de la volonté.
- ⭐ **On cote des comportements observables, pas des ressentis** (règle R6 du schéma du dossier) → jamais « note ton anxiété sur 10 » ; toujours une ancre comportementale (« à combien de choses as-tu renoncé ? »).
- **Poser les questions sur les états internes explicitement et de façon fermée** (§9.20) → l'absence de plainte n'est pas une absence de problème.

Compétences (skills) : `ay-typescript` (patterns de typage), `ay-functional` (immutabilité, composition), `ay-refactor` (méthodologie de refactoring), `ay-api` (design REST / interfaces), `ay-12factor` (services déployables), `/ay-teach [topic]` (apprentissage structuré).

### Données de santé — arbitrage acté (09/08/2026)

Le dossier médical et la mémoire longitudinale sont **versionnés dans le dépôt privé `github.com/XavierBoubert/psy`** ; le transport PC↔téléphone passera par **Syncthing P2P** (aucun serveur tiers ne stocke). Décision de Xavier en connaissance de cause : elle assouplit sciemment la règle « rien ne part vers un tiers » du PLAN §6, en échange de la traçabilité clinique et de la sauvegarde hors-machine.

- **Conditions** : dépôt privé · 2FA + clé SSH · aucun fork, collaborateur ni GitHub Action ayant accès au contenu.
- **Hors GitHub et hors appels à Claude, aucune donnée ne part vers un tiers** — pas de cloud santé, pas de service d'analyse externe, pas de télémétrie. Ne jamais proposer d'en ajouter.
- **Porte de sortie** si l'arbitrage est révisé : `git-crypt` / `age` sur `psy/dossier/` et `ressources/xavier/`. Détail : `psy/SYNCHRO.md` §2.

## Langue

Toujours communiquer avec l'utilisateur en français.

## Plan du projet (carte de l'espace de travail)

| Répertoire | Objet |
|-----------|---------|
| `PLAN.md` (racine) | **Plan de conception du dispositif** (v1.0, brainstorming clos en 7 tours) : 5 axes + axe transversal sécurité/éthique, contraintes de conception, architecture à trois surfaces, feuille de route en 7 étapes, journal des décisions |
| **`psy/`** | **Le dispositif lui-même** (réalisation du PLAN). Carte d'entrée : `psy/README.md` |
| **`psy/dossier/`** ⭐ | **Mémoire longitudinale — source de vérité unique**, lue et écrite par les trois surfaces. Contient `SCHEMA.md` (**normatif**), `profil.md` (permanent), `etat.md` (courant), `gabarits/`, et les répertoires `journal/` `seances/` `crises/` `mesures/` `briefs/` |
| `psy/agent/` | Note d'aiguillage + table des rôles. **Les skills vivent dans `.claude/skills/psy-*`** — Claude Code ne les découvre que là |
| `psy/corpus/` | Référentiels cliniques indexés. **`corpus/echelles/` ⭐ — instruments de mesure : VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9 (complets), BES (partiel + grille comportementale de substitution).** Les 4 corpus thérapeutiques prioritaires restent à récupérer |
| `psy/protocoles/` | Protocoles thérapeutiques opérationnels — fiches actionnables |
| `psy/web/` | Outils de séance desktop — TypeScript strict *(⏸️ Étape 3-4)* |
| `psy/android/` | Kokoro (心) — compagnon permanent, Kotlin + Compose *(⏸️ Étape 5)* |
| `psy/SYNCHRO.md` | Synchronisation et sécurité des données : dépôt privé (historique) + Syncthing P2P (transport PC↔Android) |
| `ressources/originales/` | Documents source bruts (ex. PDF) |
| `ressources/spécialisées/` | Documents convertis, utilisés comme entrées pour Claude |
| `ressources/xavier/` | Ressources du profil de Xavier (celui qui prompt), utilisées pour établir son diagnostic et créer un psychologue adapté à lui |
| `scripts/` | Scripts Node.js/TypeScript autonomes (exécutés directement via le support TypeScript natif de Node, sans étape de build) |

## Skills du dispositif

| Skill | Rôle | État |
|---|---|---|
| `psy-seance` | Séance de fond hebdomadaire — ouverture / travail (une seule cible) / clôture obligatoire / compte-rendu dans `psy/dossier/seances/` | ✅ |
| `psy-journal` | Check-in quotidien — 7 questions fermées, < 2 min, aucune saisie de texte obligatoire → `psy/dossier/journal/AAAA-MM-JJ.json` | ✅ |
| `psy-crise`, `psy-bilan`, `psy-brief-isorni`, `psy-hygiene`, `psy-superviseur` | Rôles restants | ⏸️ cf. `psy/agent/README.md` |

**Invariants de tout skill du dispositif** : charger `profil.md` + `etat.md` avant d'agir · **non-substitution** (aucun conseil de modification de traitement, jamais, même sous forme interrogative — ça part au brief Dr Isorni) · **protocole de crise câblé** (3114, non contournable) · aucune visualisation · utilisable sans parler ni écrire · zéro streak ni compteur de régularité · annoncer avant de faire.

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
| `Biopsie hépatique.md` | Biopsie hépatique du **15/06/2026** — fusion de deux sources : courriel de la Dr Leila Bouarioua (hépato-gastro-entérologue) et compte rendu anatomopathologique officiel (Dr Naïma Talhi, CH Argenteuil). Conclusion histologique : **stéato-hépatite non alcoolique (NASH)** — stéatose S2, ballonnisation, infiltrat inflammatoire — **sans fibrose**. Contient la réconciliation des deux sources (le courriel décrit une stéatose simple, l'histologie une NASH), la cible de perte de poids révisée à **7-10 % (7,7-11 kg)**, les données anthropométriques (1,77 m / 110 kg / IMC 35,1) et l'analyse d'articulation avec le dossier psychiatrique (**déficit intéroceptif**, absence de perception de la satiété, règle « signal interne absent → structure externe ») |
| `20260119 Gabriel ROISMAN Conclusion Polysomnographie.md` | Polysomnographie du 29/10/2025, conclusion du 19/01/2026 (Dr Gabriel Roisman, pneumologue-somnologue, centre SomnoGalien, adressée au Dr Célia Fournier, généraliste) : **SAOS sévère, IAH 35/h** (48/h avec MELER), **61 micro-éveils/h**, déficit en sommeil lent profond et paradoxal (7,2 % SP), ronflement 80 % du TST, désaturations 29/h (SpO2 min 86 %), charge hypoxique 61 %min/h, **mouvements périodiques des jambes 31/h**. Épworth 14, ISI 20, Beck 7. Poids à l'examen : **104 kg (IMC 33)**. Conclusion : SAOS sévère, perte de poids souhaitable, prescription de PPC |
| `20260119 Gabriel ROISMAN Demande de PPC.md` | Prescription de PPC du 19/01/2026 : ResMed AirSense 11 auto (4-16 cm H2O), masque nasal ou narinaire, prestataire Link Sommeil |
| `20260504 Gabriel ROISMAN PPC.md` | **Consultation de suivi PPC du 04/05/2026**, de nouveau adressée au seul Dr Fournier : « utilisation très irrégulière », intolérance par **fuites au masque et toux sèche**, ⭐ **IAH résiduel < 6/h** (l'appareil est efficace quand il est porté), mise en place d'un **humidificateur**, pression ramenée à **6-12 cm H2O**, **EPR niveau 2** activé, prise en charge renouvelée, « **je remotive le patient** ». Base de la **v2.4** du rapport |
| `20260808 Email au Dr Isorni.md` | Brouillon d'email prêt à envoyer au Dr Isorni (+ notes internes) : transmission des deux diagnostics qui n'ont pas circulé jusqu'au psychiatre (SAOS sévère, NASH) et 5 questions prioritaires — **alprazolam et SAOS**, **venlafaxine et mouvements périodiques des jambes** (+ ferritine), **bilan hépatique de référence** à la reprise de la venlafaxine, part attribuable au SAOS dans la distractibilité, **paroxétine et prise de poids** (+6 kg en 9 mois). Documente la boucle SAOS → privation de sommeil → dérèglement ghréline/leptine → prise de poids → aggravation SAOS et NASH |
| `Rapport psychiatrique et psychologique.md` | **Document de référence du profil de Xavier.** Rapport de synthèse complet généré par Claude (**v2.4, 09/08/2026**) à partir de toutes les ressources ci-dessus + compléments anamnestiques directs de Xavier + DSM-5 + littérature en ligne : fiche patient détaillée, chronologie 1986-2026, anamnèse, re-cotation indépendante AQ (39/50) et EQ (9/80), analyse critérielle DSM-5 (TSA niveau 1 confirmé ; agoraphobie avec attaques de panique attendues ; phobie sang-injection-accident avec syncopes vasovagales fortement probable ; TAG probable ; trouble panique écarté), aphantasie et shutdowns intégrés au profil, confirmation du diagnostic d'Emeline Saley, **23 enseignements** et recommandations.<br>**v2.1** ajoute le versant somatique : atteinte hépatique confirmée par biopsie, obésité de classe II (IMC 35,1), **§6.5 conduite alimentaire et déficit intéroceptif**, hyperphagie boulimique non retenue (BES à passer), **§10.7 versant somatique** (alimentation et activité physique requalifiées en prescription médicale), et la règle de conception **« signal interne absent → structure externe »** (§9.19).<br>**v2.2** corrige le diagnostic hépatique en **stéato-hépatite non alcoolique (NASH), sans fibrose**, la date du geste au **15/06/2026**, et porte la cible de perte de poids de ≥ 5 % à **7-10 % (7,7-11 kg)**.<br>**v2.3** ⚠️ requalifie le SAOS d'hypothèse en **diagnostic constitué et NON TRAITÉ** (IAH 35/h, PPC prescrite non utilisée) : **§6.6** (SAOS sévère — pourquoi la PPC échoue au vu du profil, boucles SAOS↔poids↔NASH, mouvements périodiques des jambes, alprazolam, défaut de coordination entre six praticiens) et **§10.8** (protocole de désensibilisation à la PPC = exposition graduée) ; §6.3 révisé (traiter avant de conclure sur l'attention), §9.17 et §9.21 révisés, **§9.23 ajouté** (3e instance de la règle §9.19 : l'échec de la PPC était prévisible).<br>**v2.4** ⚠️ corrige un fait de la v2.3 à partir de la consultation du 04/05/2026 : la PPC n'est **pas inutilisée**, elle est **utilisée de façon très irrégulière** ; le SAOS est **insuffisamment traité** et non « non traité » ; ⭐ **IAH résiduel < 6/h sous appareil** (l'efficacité est démontrée, seul le port manque) ; causes d'intolérance documentées (**fuites au masque, toux sèche** — d'où la question **fuite au masque ou à la bouche**, qui commande le choix d'interface) ; réglages actualisés (humidificateur, 6-12 cm H₂O, EPR 2) et prise en charge renouvelée ; **le Dr Roisman sait — c'est le Dr Isorni qui ignore tout**, les deux courriers étant partis au seul Dr Fournier ; « je remotive le patient » versé au §9.23 comme démonstration en conditions réelles que la conduite standard ne suffit pas |

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
