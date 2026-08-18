# PLAN — ce qui reste à construire

**Statut :** v1.

> 📖 **Ce document ne décrit pas le dispositif — il dit ce qui n'est pas encore fait.** Ce que le dispositif *est* se lit dans [`README.md`](README.md) et dans les documents de chaque rôle ; le vocabulaire fait foi dans [`THESAURUS.md`](THESAURUS.md).
>
> ⚠️ **Écrit ne veut pas dire appliqué.** Un protocole rédigé, un skill livré, un format spécifié ne sont pas des actes cliniques. **C'est le contrôle C7 du superviseur**, et il reste ouvert tant que `ppc_minutes` est à 0.

---

## 1. Le chantier qui commande le reste

🔴 **Chantier n° 1 : la reprise de la PPC par désensibilisation.** SAOS sévère **insuffisamment traité** — usage très irrégulier, IAH résiduel < 6/h sous appareil. Fiche : [`psy/docs/protocoles/ppc-desensibilisation.md`](psy/docs/protocoles/ppc-desensibilisation.md).

**Le palier 0 est logistique, et il n'est pas bouclé :**

- [x] **Relevé de télésuivi — vide pour le moment.** Aucun usage à relever : **le relevé ne tranchera rien tant que `ppc_minutes` est à 0.** À redemander à Link Sommeil quand du port sera enregistré
- [x] **Origine de la fuite — la question est court-circuitée** : masque changé pour une interface **bouche + nez**
- [x] **Le nouveau masque bouche + nez est entré au protocole** — palier 1 démarré le **18/08/2026**, exercice publié dans Kokoro
- [ ] **Vérifier la prise en charge du nouveau masque** — étape `ppc-prise-en-charge`, réponse écrite de Link Sommeil
- [ ] Demander une **consultation de reprise** au Dr Roisman
- [ ] **Envoyer l'email au Dr Isorni** — rédigé, non envoyé. **Seul praticien encore dans l'ignorance des deux diagnostics somatiques**

✅ **La désensibilisation a repris le 18/08/2026, au palier 1** — masque tenu à la main, 5 min en journée *(séance du 18/08 ; l'objection sur le contact facial accru du bouche + nez est conservée entière au compte-rendu)*.

🔴 **Le point qui commande maintenant : le premier `ppc_minutes` non nul.** Tant qu'il n'existe pas, le chantier n'a produit aucune donnée — et **le relevé de télésuivi ne peut rien trancher**, ce qui est la raison pour laquelle il a cessé d'être bloquant.

⭐ **Ces démarches sont déjà des étapes du programme.** Kokoro les met sous la main ; **il ne les passe pas — ce sont des appels et des emails.**

🔴 **`ppc_minutes` est l'indicateur qui tranche.** Tant qu'il est à 0, aucune quantité de doctrine produite ne compte comme un progrès.

**Les deux autres chantiers sont écrits et démarrent après :** alimentation à structure externe, activité physique sans impact *(feu vert médical préalable requis)*. **À partir du palier 1, un seul chantier progresse à la fois.**

---

## 2. La feuille de route

### Étape 1 — Versant somatique 🔴 *ouverte*

- [ ] 🔴 **Le palier 0 de la PPC** — §1 ci-dessus
- [~] **Dépister la perte de contrôle alimentaire** — instrument BES non obtenu ; **grille comportementale de substitution utilisable immédiatement**
- [ ] Recueillir : historique pondéral, bilan hépatique de départ, bilan métabolique, **feu vert médical** pour l'activité

### Étape 2 — Instrumentation du suivi ⏱️ *ouverte*

- [ ] **Premier brief Dr Isorni**, à écrire à la séance du **week-end du 29-30/08**
- [ ] Passer les échelles restantes : **TAS-20** · **CAT-Q + GAD-7/PHQ-9** · **BES** dès obtention. Plafond 20 min par séance ; **l'échelle n'est jamais la cible de la séance**
- [ ] ✈️ **Sécuriser l'ordonnance de venlafaxine pour le séjour.** **Logistique, pas posologie**

### Étape 3 — Outils de crise 🔴 *ouverte*

- [ ] ⏳ **Critère de fin de la tension appliquée : un bloc en salle d'attente réelle.** Tout le reste est construit et essayé à froid

### Étape 4 — TCC de l'agoraphobie

- [ ] Corpus exposition graduée adapté TSA
- [ ] Paliers écrits → **publiés dans Kokoro**

⚠️ **Le kit vol n'est pas un programme d'exposition et n'en tient pas lieu.**

### Étape 5 — Kokoro 🏗️ *ouverte, priorité accélérée*

**✅ K0 → K4, K7 franchis** : poste de travail · full-screen intent levé · **noyau de crise** *(mot-code envoyé pour de vrai, téléphone verrouillé, essai fait à froid)* · **tension appliquée guidée sur repères externes** · **check-in quotidien sur le téléphone** · La présence de Kokoro et l'UI.

| Jalon | Objet | Critère de fin |
|---|---|---|
| 🔴 **K5** *(en cours)* | **Le programme et la bibliothèque** — Kokoro lit `programme.json` + `bibliotheque/`, affiche par rubrique, écrit `reponses/`. ✅ Moitié PC écrite et vérifiée ; ✅ **Kokoro lit `programme.json`, filtre les fiches et confie le PDF au lecteur du téléphone** *(`programme/Bibliotheque.kt`, `monde/LecteurPdf.kt`)* ; ⏳ **reste `reponses/`** — aucune réponse d'étape n'est encore remontée | Une étape publiée depuis le PC apparaît sur le téléphone, est faite par Xavier, et sa réponse revient au dossier — **valide au format, sans intervention manuelle** |
| 🔜 **K6** | **La séance à deux** — type `seance-duo`, **mode entraînement** obligatoire, **signal d'arrêt** rappelé en permanence, **critères d'arrêt à un tap**. ⭐ **Le jalon qui ouvre les thérapies impossibles en solo** | Un entraînement joué en entier par Chourouk, puis une séance réelle menée à son terme **ou arrêtée sur le signal** — l'un et l'autre valent |

- [ ] **Publier les échelles** comme questionnaires, rubrique `bilan` — **jamais le PHQ-9**
- [ ] **Publier les démarches du palier 0 PPC**

### Étape 6 — Stabilisation à deux, puis réouverture de l'EMDR ⏸️

- [x] ✅ **Chourouk accepte le rôle d'aidant** et **le signal d'arrêt est convenu** — les deux préalables à froid sont levés
- [x] ✅ **Le signal d'arrêt est le « non » de la main** — consigné dans [`companion/PROGRAMME.md`](companion/PROGRAMME.md) §3, `signal_arret` obligatoire, recopié tel quel dans chaque `seance-duo`. ⚠️ **Conséquence câblée** : aucune consigne de `sequence` ne demande de réponse gestuelle de la main
- [ ] **Porter le geste côté Chourouk** — il n'existe aujourd'hui que dans le format ; il lui est dit oralement tant que la fiche n'est pas étendue *(voir Transverse)*
- [ ] **Écrire la première séance à deux : la stabilisation non visuelle** *(arbitrage O)* — kit d'auto-apaisement corporel et sensoriel
- [ ] **Écrire la stimulation bilatérale comme `seance-duo`** — ⭐ **l'aidant fait le geste, Kokoro tient la cadence.** Il n'y a **pas d'instrument à développer** : ce qui reste est du contenu clinique. ⚠️ **Point dur de format à traiter d'abord** — `sequence` ne sait pas exprimer une **répétition en séries** *([`companion/PROGRAMME.md`](companion/PROGRAMME.md) §3)*
- [ ] Réouverture de l'EMDR **sous les critères de [`psy/README.md`](psy/README.md) §6**, après avis du Dr Isorni
- [ ] ⚠️ **Trancher avec le Dr Isorni, au déverrouillage de la phase 3 seulement : qui tient l'instrument quand Xavier verbalise du matériel lourd** — c'est **C10**, et la question se pose **avant**, jamais pendant

### Transverse

- [ ] Récupérer et indexer les **3 corpus prioritaires restants** : TCC alimentaire + intéroception · TCC de l'agoraphobie · recommandations HAS. ⭐ **Le deuxième sert deux fois** : la désensibilisation à la PPC *est* une exposition graduée
- [ ] Évaluer **ACT / défusion cognitive** — ❓ **vérifier la compatibilité avec l'aphantasie**
- [ ] Étendre [`aidant/ressources/fiche-chourouk.md`](aidant/ressources/fiche-chourouk.md) au rôle d'**aidant** — aujourd'hui elle n'explique que les shutdowns et **déclare explicitement ne lui attribuer aucun rôle**. 🔴 **Chourouk ayant accepté, ce cadre est périmé et la fiche est à réécrire, pas à compléter** — elle doit porter le **« non » de la main**, le mode entraînement et les critères d'arrêt
- [ ] **Schémas Zod des deux formats normatifs** dans les scripts — `psy-publish` et `psy-sync` valident aujourd'hui **à la main** ce que [`psy/DOSSIER.md`](psy/DOSSIER.md) et [`companion/PROGRAMME.md`](companion/PROGRAMME.md) définissent

---

## 3. Les arbitrages ouverts

| # | Question | Recommandation |
|---|---|---|
| **O** | **Quelle est la première séance à deux ?** | ⭐ **La stabilisation non visuelle** (ancrage corporel et sensoriel). Elle ne touche aucun matériel traumatique, et **c'est la brique qui manque** |

⭐ **Le clinicien du dispositif est Claude Psy** : il délègue **le suivi à Kokoro** et **les séances à deux à Chourouk**. La question du psychologue en présentiel n'est plus un arbitrage ouvert.

---

## 4. ⏱️ Les deux échéances qui structurent le trimestre

| Date | Événement | Conséquences |
|---|---|---|
| **03/09/2026, 12h30** | **Consultation Dr Isorni** | **La dernière avant fin septembre.** Brief à écrire au week-end du 29-30/08, email à envoyer **avant** — un créneau ne suffit pas à découvrir un SAOS sévère, une NASH et six questions à la fois |
| **07/09/2026** | **Départ en Tunisie, 3 semaines ou plus** | ⭐ **Un tiers du trimestre.** Le palier 0 PPC doit être bouclé avant. La PPC part en Tunisie — **port au niveau atteint, sans progression de palier**. ⭐ **Aucun palier ne progresse pendant le séjour, et on redescend d'un palier à la reprise, sur les trois chantiers** — décidé maintenant, pas subi sur place : **décider avant est précisément ce qui empêche de le vivre comme un échec**. ⚠️ **Le voyage est une exposition agoraphobique majeure.** ✈️ **Le 3114 ne fonctionne pas depuis l'étranger.** ✅ **Première période sans mission professionnelle depuis longtemps** : la seule variable d'ajustement du dossier tombe à zéro — **observer si les shutdowns baissent vaudra plus que n'importe quelle échelle** |
