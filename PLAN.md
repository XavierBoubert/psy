# PLAN — ce qui reste à construire

**Statut :** v1.

> 📖 **Ce document ne décrit pas le dispositif — il dit ce qui n'est pas encore fait.** Ce que le dispositif *est* se lit dans [`README.md`](README.md) et dans les documents de chaque rôle ; le vocabulaire fait foi dans [`THESAURUS.md`](THESAURUS.md).
>
> ⚠️ **Écrit ne veut pas dire appliqué.** Un protocole rédigé, un skill livré, un format spécifié ne sont pas des actes cliniques. **C'est le contrôle C7 du superviseur** — et il s'est refermé le **18/08/2026**, à la première séance de palier réellement faite.

---

## 1. Le chantier qui commande le reste

🔴 **Chantier n° 1 : la reprise de la PPC par désensibilisation.** SAOS sévère **insuffisamment traité** — usage très irrégulier, IAH résiduel < 6/h sous appareil. Fiche : [`psy/docs/protocoles/ppc-desensibilisation.md`](psy/docs/protocoles/ppc-desensibilisation.md).

**Le palier 0 est logistique, et il n'est pas bouclé :**

- [x] **Relevé de télésuivi — vide pour le moment.** Aucun usage à relever. À redemander à Link Sommeil quand la machine tournera, c'est-à-dire **au palier 3**
- [x] **Origine de la fuite — la question est court-circuitée** : masque changé pour une interface **bouche + nez**
- [x] **Le nouveau masque bouche + nez est entré au protocole** — palier 1 démarré le **18/08/2026**, exercice publié dans Kokoro
- [ ] **Vérifier la prise en charge du nouveau masque** — étape `ppc-prise-en-charge`, réponse écrite de Link Sommeil
- [ ] **Envoyer l'email au Dr Isorni** — rédigé, non envoyé. **Seul praticien encore dans l'ignorance des deux diagnostics somatiques**

✅ **La désensibilisation a repris le 18/08/2026, au palier 1** — masque tenu à la main, 5 min en journée *(séance du 18/08 ; l'objection sur le contact facial accru du bouche + nez est conservée entière au compte-rendu)*.

✅ **Première séance de palier réellement faite le 18/08/2026 à 21h24** — `arrete_avant_la_fin`, remontée au dossier dans [`companion/outputs/reponses/`](companion/outputs/reponses/). ⭐ **Le masque a été tenu : c'est ça, la donnée.** Une séance arrêtée avant la fin ne se commente pas et ne se rattrape pas.

🔴 **Ce qui commande maintenant : trois jours consécutifs où le minuteur va au bout** — c'est le critère de passage du palier 1, et il se compte sur les `issue: termine` de `reponses/`, une séance par fichier.

🔴 **`ppc_minutes` ne mesure pas ce chantier-ci et ne peut pas le mesurer.** C'est la donnée du télésuivi, donc **de la machine allumée** — elle reste à 0 par construction aux paliers 1 et 2, où la machine est éteinte. Elle redevient l'indicateur **à partir du palier 3**. ⚠️ **Un `ppc_minutes` à 0 n'est donc plus un constat d'immobilité** : c'est ce que le protocole prévoit à ce stade.

⭐ **Ces démarches sont déjà des étapes du programme.** Kokoro les met sous la main ; **il ne les passe pas — ce sont des appels et des emails.**

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
- [ ] 🔴 **Verser les bilans finalisés dans Kokoro** — jalon **K8**, Étape 5. ⭐ **Les échelles se passent avec Claude Psy, en conversation** *(tranché le 19/08/2026)* : **Kokoro porte le bilan, jamais la passation**
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
| 🔴 **K5** *(en cours)* | **Le programme et la bibliothèque** — Kokoro lit `programme.json` + `bibliotheque/`, affiche par rubrique, écrit `reponses/`. ✅ **Thérapie, Documentation et Bilan sont entièrement lues du dossier** — `ecran`, `exercice` *(minuteur)*, `questionnaire` *(une question par écran, réponses item par item)*, `demarche` *(bouton « c'est fait »)*, `fiche` ; ✅ **le circuit complet a bouclé le 18/08/2026** ; ⏳ **mais le premier aller-retour a demandé un sauvetage à la main** *(voir ci-dessous)*, et **`seance-duo` n'est pas porté** — c'est K6 | Une étape publiée depuis le PC apparaît sur le téléphone, est faite par Xavier, et sa réponse revient au dossier — **valide au format, sans intervention manuelle** |
| 🔴 **K8** *(haute priorité)* | **Les bilans** — type d'étape `bilan`, canal `companion/inputs/bilans/`, écran *Bilan* groupé **par mois**. ⭐ **Xavier passe les échelles avec Claude Psy ; Kokoro porte les bilans finalisés en PDF, consultables et partageables à tout moment** *(tranché le 19/08/2026)* | Les quatre bilans rétrospectifs sont sur le téléphone, ouvrables par le lecteur du téléphone, et partageables depuis lui |
| 🔜 **K6** | **La séance à deux** — type `seance-duo`, **mode entraînement** obligatoire, **signal d'arrêt** rappelé en permanence, **critères d'arrêt à un tap**. ⭐ **Le jalon qui ouvre les thérapies impossibles en solo** | Un entraînement joué en entier par Chourouk, puis une séance réelle menée à son terme **ou arrêtée sur le signal** — l'un et l'autre valent |

> ⚠️ **Le critère de fin de K5 n'est pas encore atteint, et c'est une nuance qui compte.** La première réponse a bien fait le trajet, mais **Drive avait accepté deux dossiers `reponses/`** et le fichier a dû être récupéré à la main dans le second. **Les trois défauts sont corrigés** — une lecture ne crée plus de dossier, la création est doublement gardée, `psy-sync` lit et signale les dossiers en double. **K5 se ferme au premier aller-retour qui se passe de moi.**
>
> 🔴 **La règle qui en sort, et qui vaut au-delà de Kokoro : l'état d'une étape ne dépend jamais d'un aller-retour par Drive.** Kokoro se souvient localement de ce qu'il a écrit — sinon une étape faite réapparaît *à faire* le temps que le fichier remonte, et **Xavier la refait**. *(Constaté en usage réel le 18/08/2026 ; consigné dans [`companion/PROGRAMME.md`](companion/PROGRAMME.md) §5.)*

- [x] ~~**Publier les échelles** comme questionnaires~~ → ✅ **CADUQUE le 19/08/2026.** Xavier passe les échelles **avec Claude Psy**, en conversation — un item introspectif ambigu se désambiguïse à voix haute, et le corpus en signale déjà quatre *(item XVI du BES, trois items du MAIA-2)*. ⭐ **Le type `questionnaire` reste porté et disponible** ; **aucune échelle validée ne part dans Kokoro**
- [x] ✅ **Publier les démarches du palier 0 PPC** — au programme v4, affichées et validables une à une

#### 🔴 K8 — les bilans dans Kokoro

**Trois décisions prises le 19/08/2026. Elles ne se rediscutent pas à l'implémentation.**

1. 🔴 **Un bilan ne passe pas par `bibliotheque/`** — canal distinct `companion/inputs/bilans/<id>.md`. **Raison mécanique :** les sept familles d'interdits de `psy:publish` refusent en bloc un rapport clinique réel *(`venlafaxine`, `posologie`, `agoraphobie`, `apnee`…)*. **Raison de fond :** C9 et C10 protègent une fiche *écrite pour Xavier* et *lisible par l'aidant* — **un bilan n'est ni l'un ni l'autre. C'est un document que Xavier possède déjà, adressé à lui seul.**
2. 🔴 **Un bilan n'est jamais `montrable`, et Kokoro n'offre aucune fonction de partage.** Il confie le PDF au lecteur du téléphone — mécanisme `fiche`/`document` existant, picto « dehors ». ⭐ **Le partage est un acte de Xavier dans son lecteur, pas une fonction du dispositif** : c'est l'artefact le plus sensible de tout le projet.
3. **Un bilan se publie à tout moment, hors séance** — comme la documentation. **Supervision bloquante quand même**, portant explicitement sur : *ce document ne contient rien que Xavier ne sache déjà*. **C'est le contrôle qui remplace C9 sur ce canal.**

**Le travail :**

- [ ] **Format** — type `bilan` dans [`companion/PROGRAMME.md`](companion/PROGRAMME.md) : `document` obligatoire *(jamais `texte`)*, `date` en `AAAA-MM-JJ` **celle du bilan, pas de la publication**, `rubrique: bilan` forcée, `quand` absent. ⚠️ **Un type distinct, et non une `fiche`** : la règle « toute fiche s'affiche sur *Documentation* » a été tranchée le 18/08 pour garder l'écran de crise à trois boutons sans défilement — **on ne la rouvre pas**
- [ ] **Kokoro** — `ContenuBilan` groupe **par mois décroissant** au lieu de `quand`, et route vers `ouvrirLePdf`. ⭐ **La date appartient au document, pas à l'assiduité de Xavier** : ce n'est pas un historique de progression, l'invariant *zéro streak* n'est pas touché
- [ ] **Scripts** — `psy:publish` convertit `bilans/<id>.md` en `bilans/<id>.pdf` par `md2pdf` et retire du transit ce que le programme n'appelle plus, **avec son propre jeu de contrôles** *(les interdits de fiche ne s'y appliquent pas)*. `psy:sync` n'a rien à faire : **un bilan ne renvoie rien**
- [ ] **Verser les quatre bilans rétrospectifs** — hors séance, un par un, sous supervision

| Bilan | Mois | Source | État |
|---|---|---|---|
| **Évaluation TSA** — Emeline Saley | **2024-04** | [`patient/ressources/Evaluation Xavier.md`](patient/ressources/Evaluation%20Xavier.md) | à mettre en forme |
| **Conclusion de polysomnographie** — Dr Roisman | **2026-01** | [`patient/ressources/20260119 … Conclusion Polysomnographie.md`](patient/ressources/20260119%20Gabriel%20ROISMAN%20Conclusion%20Polysomnographie.md) | à mettre en forme |
| **Rapport psychiatrique et psychologique** v2.4 | **2026-08** | [`patient/ressources/Rapport psychiatrique et psychologique.md`](patient/ressources/Rapport%20psychiatrique%20et%20psychologique.md) | à mettre en forme |
| **VVIQ** — passation du 09/08/2026, 18/80 | **2026-08** | [`psy/outputs/dossier/mesures/2026-08-09-vviq.json`](psy/outputs/dossier/mesures/2026-08-09-vviq.json) | 🔴 **aucun compte-rendu n'existe — il est à écrire** |

> ⚠️ **Le VVIQ n'a jamais produit de document.** Seule la passation item par item existe. Le bilan est **à rédiger** : les items, le score, ce que l'échelle établit — l'aphantasie comme donnée mesurée — et **ce qu'elle ne dit pas**.
>
> 🔴 **Les PDF de [`patient/ressources/originales/`](patient/ressources/originales/) ne sont pas la source.** C'est une archive, jamais une entrée : le bilan se compose depuis le Markdown, et `psy:publish` le convertit comme le reste.

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
