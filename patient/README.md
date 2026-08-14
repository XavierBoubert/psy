# `patient/` — Xavier

**Le patient.** Pas un utilisateur à engager, pas une cible d'activation. Le dispositif entier est construit pour lui, et **ses documents source vivent ici**.

> 🔴 **Ce répertoire ne contient rien que le dispositif ait écrit sur Xavier.** Ce que Claude Psy constate, mesure et conclut vit dans [`../psy/outputs/dossier/`](../psy/outputs/dossier/) ; ce que Kokoro recueille vit dans [`../companion/outputs/`](../companion/outputs/). **Ici, ce sont les sources** — évaluations, courriers de praticiens, questionnaires remplis, examens.
>
> ⚠️ **Une seule pièce fait exception et elle est signalée comme telle :** le *Rapport psychiatrique et psychologique* est écrit par Claude. Il fait foi sur un point clinique, mais **il est dérivé**, pas primaire.

---

## Les contraintes qui commandent tout le dispositif

Elles viennent du rapport v2.4. **Aucune n'est une préférence** *(les invariants qu'elles imposent : [`../README.md`](../README.md) §7)* :

| Contrainte | Ce qu'elle interdit |
|---|---|
| **Aphantasie** *(VVIQ 18/80)* | Aucune technique de visualisation. **La consigne est inopérante, pas difficile** — verbal, corporel, exposition in vivo uniquement |
| **Shutdowns** | Toute interface doit rester utilisable **sans parler ni écrire** |
| **Empathie cognitive déficitaire** | Communication explicite, littérale, **sans sous-entendu ni attente implicite** |
| **Camouflage = moteur de l'anxiété** | Zéro exigence de performance sociale, zéro jugement |
| **Réduire les charges, pas « motiver »** | Pas de gamification culpabilisante, **pas de streak punitif** |
| **Hypersensibilités (4 canaux)** | UI sobre : pas de son surprise, pas de flash, pas d'animation brusque |
| **Rigidité / routines** | **La prévisibilité est une fonctionnalité** : aucun changement d'interface non annoncé |
| **Trois mécanismes de crise distincts** | Panique *(exposition/respiration)* ≠ vasovagal *(tension appliquée)* ≠ shutdown *(mot-code, retrait, reprise différée)*. **La mauvaise parade aggrave.** |

> ⭐ **La règle centrale — signal interne absent → structure externe** *(rapport v2.4 §9.19)*. Ne jamais demander à Xavier de s'appuyer sur une perception qui lui manque : satiété, fatigue, tension, émotion. **Trois échecs documentés** — « imaginez un lieu sûr », « écoutez votre satiété », « portez le masque toute la nuit » — **relevaient de la conception, pas de la volonté.**
>
> ⭐ **On cote des comportements observables, pas des ressentis** *(règle R6)*. Jamais « note ton anxiété sur 10 » ; toujours une ancre comportementale — « à combien de choses as-tu renoncé ? ».

---

## `ressources/` — les documents source

Les ressources propres au profil de Xavier (l'utilisateur qui prompt) : réponses à des questionnaires, historique, éléments personnels, examens. Elles servent à établir son diagnostic et à concevoir un psychologue virtuel et une thérapie adaptés à lui.

| Fichier | Description |
|--------|-------------|
| [`Evaluation Xavier.md`](ressources/Evaluation%20Xavier.md) | Compte-rendu d'évaluation psychologique complet (Emeline Saley, psychologue clinicienne, 5 avril 2024) : anamnèse, tests passés, synthèse des questionnaires, récapitulatif des troubles observés et conclusion diagnostique (Trouble du Spectre Autistique de légère intensité / syndrome d'Asperger, à confirmer par un psychiatre) |
| [`Dossier RQTH.md`](ressources/Dossier%20RQTH.md) | Dossier de demande RQTH (formulaire MDPH Cerfa 15692*01) déposé le 05/12/2024, incluant le certificat médical du Dr Jean-Baptiste ISORNI (TSA type Asperger diagnostiqué par le Dr Lamia Kias en 2023, trouble anxieux, agoraphobie, crises d'angoisse aiguë avec dépersonnalisation/déréalisation, traitement par Venlafaxine) et un justificatif de domicile |
| [`Quotient du Spectre Autistique QA.md`](ressources/Quotient%20du%20Spectre%20Autistique%20QA.md) | Réponses de Xavier au questionnaire AQ (Autism-Spectrum Quotient, Baron-Cohen et al., 2001) — réponses cochées uniquement |
| [`Quotient d'Empathie EQ.md`](ressources/Quotient%20d'Empathie%20EQ.md) | Réponses de Xavier au questionnaire EQ (Empathy Quotient / Cambridge Behaviour Scale, Baron-Cohen & Wheelwright, 2004) — réponses cochées uniquement |
| [`Echelle-syndrome-Asperger.md`](ressources/Echelle-syndrome-Asperger.md) | Réponses au questionnaire Échelle du syndrome d'Asperger (selon Attwood), rempli par la mère de Xavier — réponses cochées uniquement |
| [`Inventaire-du-stress.md`](ressources/Inventaire-du-stress.md) | Réponses à l'Inventaire du stress (The Groden Center Inc.) avec récapitulatif des scores par catégorie — réponses cochées uniquement |
| [`Question-aux-parents.md`](ressources/Question-aux-parents.md) | Réponses de la mère de Xavier à un questionnaire sur les antécédents familiaux, la grossesse, le développement et l'enfance |
| [`Biopsie hépatique.md`](ressources/Biopsie%20hépatique.md) | Biopsie hépatique du **15/06/2026** — fusion de deux sources : courriel de la Dr Leila Bouarioua (hépato-gastro-entérologue) et compte rendu anatomopathologique officiel (Dr Naïma Talhi, CH Argenteuil). Conclusion histologique : **stéato-hépatite non alcoolique (NASH)** — stéatose S2, ballonnisation, infiltrat inflammatoire — **sans fibrose**. Contient la réconciliation des deux sources (le courriel décrit une stéatose simple, l'histologie une NASH), la cible de perte de poids révisée à **7-10 % (7,7-11 kg)**, les données anthropométriques (1,77 m / 110 kg / IMC 35,1) et l'analyse d'articulation avec le dossier psychiatrique (**déficit intéroceptif**, absence de perception de la satiété, règle « signal interne absent → structure externe ») |
| [`20260119 Gabriel ROISMAN Conclusion Polysomnographie.md`](ressources/20260119%20Gabriel%20ROISMAN%20Conclusion%20Polysomnographie.md) | Polysomnographie du 29/10/2025, conclusion du 19/01/2026 (Dr Gabriel Roisman, pneumologue-somnologue, centre SomnoGalien, adressée au Dr Célia Fournier, généraliste) : **SAOS sévère, IAH 35/h** (48/h avec MELER), **61 micro-éveils/h**, déficit en sommeil lent profond et paradoxal (7,2 % SP), ronflement 80 % du TST, désaturations 29/h (SpO2 min 86 %), charge hypoxique 61 %min/h, **mouvements périodiques des jambes 31/h**. Épworth 14, ISI 20, Beck 7. Poids à l'examen : **104 kg (IMC 33)**. Conclusion : SAOS sévère, perte de poids souhaitable, prescription de PPC |
| [`20260119 Gabriel ROISMAN Demande de PPC.md`](ressources/20260119%20Gabriel%20ROISMAN%20Demande%20de%20PPC.md) | Prescription de PPC du 19/01/2026 : ResMed AirSense 11 auto (4-16 cm H2O), masque nasal ou narinaire, prestataire Link Sommeil |
| [`20260504 Gabriel ROISMAN PPC.md`](ressources/20260504%20Gabriel%20ROISMAN%20PPC.md) | **Consultation de suivi PPC du 04/05/2026**, de nouveau adressée au seul Dr Fournier : « utilisation très irrégulière », intolérance par **fuites au masque et toux sèche**, ⭐ **IAH résiduel < 6/h** (l'appareil est efficace quand il est porté), mise en place d'un **humidificateur**, pression ramenée à **6-12 cm H2O**, **EPR niveau 2** activé, prise en charge renouvelée, « **je remotive le patient** ». Base de la **v2.4** du rapport |
| [`20260808 Email au Dr Isorni.md`](ressources/20260808%20Email%20au%20Dr%20Isorni.md) | Brouillon d'email prêt à envoyer au Dr Isorni (+ notes internes) : transmission des deux diagnostics qui n'ont pas circulé jusqu'au psychiatre (SAOS sévère, NASH) et 5 questions prioritaires — **alprazolam et SAOS**, **venlafaxine et mouvements périodiques des jambes** (+ ferritine), **bilan hépatique de référence** à la reprise de la venlafaxine, part attribuable au SAOS dans la distractibilité, **paroxétine et prise de poids** (+6 kg en 9 mois). Documente la boucle SAOS → privation de sommeil → dérèglement ghréline/leptine → prise de poids → aggravation SAOS et NASH |
| [`Rapport psychiatrique et psychologique.md`](ressources/Rapport%20psychiatrique%20et%20psychologique.md) | **Document de référence du profil de Xavier.** Rapport de synthèse complet généré par Claude (**v2.4, 09/08/2026**) à partir de toutes les ressources ci-dessus + compléments anamnestiques directs de Xavier + DSM-5 + littérature en ligne : fiche patient détaillée, chronologie 1986-2026, anamnèse, re-cotation indépendante AQ (39/50) et EQ (9/80), analyse critérielle DSM-5 (TSA niveau 1 confirmé ; agoraphobie avec attaques de panique attendues ; phobie sang-injection-accident avec syncopes vasovagales fortement probable ; TAG probable ; trouble panique écarté), aphantasie et shutdowns intégrés au profil, confirmation du diagnostic d'Emeline Saley, **23 enseignements** et recommandations.<br>**v2.1** ajoute le versant somatique : atteinte hépatique confirmée par biopsie, obésité de classe II (IMC 35,1), **§6.5 conduite alimentaire et déficit intéroceptif**, hyperphagie boulimique non retenue (BES à passer), **§10.7 versant somatique** (alimentation et activité physique requalifiées en prescription médicale), et la règle de conception **« signal interne absent → structure externe »** (§9.19).<br>**v2.2** corrige le diagnostic hépatique en **stéato-hépatite non alcoolique (NASH), sans fibrose**, la date du geste au **15/06/2026**, et porte la cible de perte de poids de ≥ 5 % à **7-10 % (7,7-11 kg)**.<br>**v2.3** ⚠️ requalifie le SAOS d'hypothèse en **diagnostic constitué et NON TRAITÉ** (IAH 35/h, PPC prescrite non utilisée) : **§6.6** (SAOS sévère — pourquoi la PPC échoue au vu du profil, boucles SAOS↔poids↔NASH, mouvements périodiques des jambes, alprazolam, défaut de coordination entre six praticiens) et **§10.8** (protocole de désensibilisation à la PPC = exposition graduée) ; §6.3 révisé (traiter avant de conclure sur l'attention), §9.17 et §9.21 révisés, **§9.23 ajouté** (3e instance de la règle §9.19 : l'échec de la PPC était prévisible).<br>**v2.4** ⚠️ corrige un fait de la v2.3 à partir de la consultation du 04/05/2026 : la PPC n'est **pas inutilisée**, elle est **utilisée de façon très irrégulière** ; le SAOS est **insuffisamment traité** et non « non traité » ; ⭐ **IAH résiduel < 6/h sous appareil** (l'efficacité est démontrée, seul le port manque) ; causes d'intolérance documentées (**fuites au masque, toux sèche** — d'où la question **fuite au masque ou à la bouche**, qui commande le choix d'interface) ; réglages actualisés (humidificateur, 6-12 cm H₂O, EPR 2) et prise en charge renouvelée ; **le Dr Roisman sait — c'est le Dr Isorni qui ignore tout**, les deux courriers étant partis au seul Dr Fournier ; « je remotive le patient » versé au §9.23 comme démonstration en conditions réelles que la conduite standard ne suffit pas |

Les questionnaires notés « réponses cochées uniquement » ont été convertis manuellement (lecture visuelle des pages du PDF source, `npm run psy:pdf2md` étant incapable de détecter les cases cochées en couleur) : seule la réponse retenue est conservée, les autres choix possibles ont été omis.

---

## `ressources/originales/` — l'archive source

Les PDF et images bruts dont les Markdown ci-dessus sont issus.

> 🔴 **Ne jamais lire, ouvrir ou utiliser les fichiers de [`ressources/originales/`](ressources/originales/) comme entrée.** Ce dossier existe **uniquement comme archive** de la conversion ; les documents exploitables sont les Markdown à côté.

---

## `scripts/`

**Vide aujourd'hui** — voir [`scripts/README.md`](scripts/README.md).
