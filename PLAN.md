# PLAN — ce qui reste à construire

> 📖 **Ce document ne décrit pas le dispositif — il dit ce qui n'est pas encore fait.** Ce que le dispositif *est* se lit dans [`README.md`](README.md) et dans les documents de chaque rôle ; le vocabulaire fait foi dans [`THESAURUS.md`](THESAURUS.md).
>
> ⚠️ **Écrit ne veut pas dire appliqué.** Un protocole rédigé, un skill livré, un format spécifié ne sont pas des actes cliniques — c'est le contrôle **C7** du superviseur.
>
> 🔴 **Une ligne sort d'ici quand elle est faite, et elle en sort entièrement.** Ce qui est acquis vit dans le document de son rôle, jamais ici : **ce fichier ne tient pas l'historique, git le fait.**

---

## 1. Le chantier qui commande le reste

🔴 **Chantier n° 1 : la reprise de la PPC par désensibilisation.** SAOS sévère insuffisamment traité. Fiche : [`ppc-desensibilisation.md`](psy/docs/protocoles/ppc-desensibilisation.md). **Palier 1 en cours depuis le 18/08/2026.**

🔴 **Ce qui commande maintenant : trois jours où le minuteur va au bout.** C'est le critère de passage du palier 1. Il **se compte** sur les `issue: termine` de [`companion/outputs/reponses/`](companion/outputs/reponses/), une séance par fichier — **il ne se demande pas.**

**Ce qui reste au palier 0 — logistique :**

- [ ] **Vérifier la prise en charge du nouveau masque** — étape `ppc-prise-en-charge`, réponse écrite de Link Sommeil
- [ ] **Envoyer l'email au Dr Isorni** — rédigé, non envoyé. **Seul praticien encore dans l'ignorance des deux diagnostics somatiques**
- [ ] **Recueillir une prédiction avant chaque séance de palier** — *ce que tu prédis* puis *ce qui est arrivé*, jamais une cotation. ⭐ **Le seul ingrédient actif de l'exposition qui ne soit pas câblé** → [`agoraphobie-exposition/`](psy/docs/corpus/agoraphobie-exposition/README.md)

⚠️ **`ppc_minutes` ne mesure pas ce chantier-ci et ne peut pas le mesurer.** C'est la donnée du télésuivi, donc **de la machine allumée** : elle reste à 0 par construction aux paliers 1 et 2. **Un 0 n'est pas un constat d'immobilité, c'est ce que le protocole prévoit.** Elle redevient l'indicateur au palier 3 — c'est là que le relevé se redemande à Link Sommeil.

⭐ **Ces démarches sont déjà des étapes du programme.** Kokoro les met sous la main ; **il ne les passe pas — ce sont des appels et des emails.**

**Les deux autres chantiers sont écrits et démarrent après :** alimentation à structure externe, activité physique sans impact *(feu vert médical préalable requis)*. **Un seul chantier progresse à la fois.**

---

## 2. La feuille de route

### Étape 1 — Versant somatique 🔴 *ouverte*

- [ ] 🔴 **Le palier 0 de la PPC** — §1 ci-dessus
- [ ] **Dépister la perte de contrôle alimentaire** — passation du **BES** *(§ Étape 2 ; instrument disponible en version française validée)*. 🔴 **Le chantier alimentaire ne démarre pas avant** : la structure externe est **contre-productive** si une hyperphagie boulimique est en jeu
- [ ] Recueillir : historique pondéral, bilan hépatique de départ, bilan métabolique, **feu vert médical** pour l'activité

### Étape 2 — Les documentations à écrire *ouverte, priorité accélérée*

#### 2.1 Liste des documentations

| `document` | Rubrique | Source | Vigilance |
|---|---|---|---|
| `panique-13-symptomes` | `crise` | [panique-13-symptomes.md](psy/docs/protocoles/panique-13-symptomes.md) | Interdit n° 5 — pas de déclenchement sur prodrome |
| `vasovagal-ce-qui-se-passe` | `crise` | [tension-appliquee.md](psy/docs/protocoles/tension-appliquee.md) §0, §2 | Interdit n° 7 — jamais « détends-toi », « respire lentement » |
| `tension-appliquee-le-geste` | `crise` | [tension-appliquee.md](psy/docs/protocoles/tension-appliquee.md) §1, §3 | L'écran existe ; la fiche porte le pourquoi, lisible à froid |
| `shutdown-ce-qui-reste-ouvert` | `crise` | [crise-escalade.md](psy/docs/protocoles/crise-escalade.md) §4 | Aucun numéro d'appel, 3114 compris |
| ⏱️ `fiche-chourouk` *(`montrable: true`)* — **publiée le 18/08/2026 mais une nouvelle version existe depuis** | `crise` | [fiche-chourouk.md](aidant/ressources/fiche-chourouk.md)
| `ppc-pourquoi-maintenant` | `therapie` | [ppc-desensibilisation.md](psy/docs/protocoles/ppc-desensibilisation.md) §0 | Aucun pronostic ; vérifier en séance ce qui a été dit à Xavier |
| `ppc-les-paliers` | `therapie` | [ppc-desensibilisation.md](psy/docs/protocoles/ppc-desensibilisation.md) §4, §5 | Zéro compteur ; sortie libre annoncée |
| `ppc-palier-0` | `therapie` | [ppc-desensibilisation.md](psy/docs/protocoles/ppc-desensibilisation.md) §3 | Accompagne les sept démarches déjà publiées |
| `alimentation-les-quatre-regles` | `therapie` | [alimentation-structure-externe.md](psy/docs/protocoles/alimentation-structure-externe.md) §3 | Interdit n° 2 — aucune cotation de faim ni de satiété |
| `activite-sans-impact` | `therapie` | [activite-physique-sans-impact.md](psy/docs/protocoles/activite-physique-sans-impact.md) §3-§5 | Feu vert médical préalable non obtenu |
| `jour-de-vol` | `therapie` | [jour-de-vol.md](psy/docs/protocoles/jour-de-vol.md) | Ce n'est pas un programme d'exposition — le dire dans la fiche |
| `sejour-tunisie` | `therapie` | [PLAN.md](PLAN.md) §4 | Aucune progression de palier sur place, redescente d'un palier au retour |
| `pourquoi-pas-de-score` | `bilan` | [companion/PROGRAMME.md](companion/PROGRAMME.md) §3 | La cotation est en séance ; Kokoro n'affiche jamais un score |
| `comment-marche-kokoro` | `documentation` | [companion/PROGRAMME.md](companion/PROGRAMME.md) §8 | Ne notifie pas, ne compte pas, ne reproche pas |
| `ou-vont-mes-reponses` | `documentation` | [companion/PROGRAMME.md](companion/PROGRAMME.md) §1 | Le circuit, et qui lit quoi |

#### 2.2 Markdown à la source, PDF à la publication

- La fiche s'écrit et se relit en **Markdown** dans `companion/inputs/bibliotheque/<id>.md` — c'est la version qui passe la supervision et les sept familles d'interdits.
- `npm run psy:publish` la convertit en **`<id>.pdf`** *(via `psy:md2pdf`)* et publie vers Drive **le PDF et `programme.json`**. Le Markdown ne part pas.
- Le champ `document` de l'étape `fiche` reste l'identifiant nu : Kokoro résout `bibliotheque/<document>.pdf`.

#### 2.3 Quand une fiche se publie

- **À tout moment**, séance ou non — une fiche est à portée dès qu'elle est écrite et supervisée. **Xavier n'attend pas la séance suivante pour comprendre ce qui lui arrive.**
- **La supervision reste bloquante**, et sa `version` doit correspondre à celle du programme au moment du `publish`.
- **Chaque publication s'annonce à Xavier** dans la conversation en cours.
- **Ne partent pas hors séance** : les étapes qui font agir — `ecran`, `exercice`, `questionnaire`, `demarche`, `seance-duo`.

### Étape 3 — Instrumentation du suivi ⏱️ *ouverte*

- [ ] **Premier brief Dr Isorni**, à écrire à la séance du **week-end du 29-30/08**
- [ ] Passer les échelles restantes : **TAS-20** · **CAT-Q** · **GAD-7 / PHQ-9** · **BES**. Plafond 20 min par séance ; **l'échelle n'est jamais la cible de la séance**. ⭐ **En conversation avec Claude Psy** — Kokoro porte le bilan, jamais la passation
- [ ] ✈️ **Sécuriser l'ordonnance de venlafaxine pour le séjour.** **Logistique, pas posologie**

### Étape 4 — Outils de crise 🔴 *ouverte*

- [ ] ⏳ **Critère de fin de la tension appliquée : un bloc en salle d'attente réelle.** Tout le reste est construit et essayé à froid

### Étape 5 — TCC de l'agoraphobie

- [ ] **Paliers écrits → publiés dans Kokoro.** 🔴 **Le format est fixé par le corpus** : in vivo uniquement *(aphantasie)*, **aucun SUDS** — une prédiction avant, un constat après. ⚠️ **L'exposition intéroceptive n'entre pas au protocole** : question de sécurité au brief, le vasovagal et le SAOS passent devant

⚠️ **Le kit vol n'est pas un programme d'exposition et n'en tient pas lieu.**

### Étape 6 — Kokoro 🏗️ *ouverte, priorité accélérée*

**Un seul jalon reste ouvert.**

| Jalon | Ce qui manque | Critère de fin |
|---|---|---|
| 🔜 **K6** | **La séance à deux** — type `seance-duo` non porté par l'app. ⭐ **Le jalon qui ouvre les thérapies impossibles en solo** | Un entraînement joué en entier par Chourouk, puis une séance réelle menée à son terme **ou arrêtée sur le signal** — l'un et l'autre valent |

### Étape 7 — Stabilisation à deux, puis réouverture de l'EMDR ⏸️

- [ ] **Transmettre la fiche à Chourouk** — [`fiche-chourouk.md`](aidant/ressources/fiche-chourouk.md) porte le rôle, le « non » de la main, les critères d'arrêt et le mode entraînement. 🔴 **Supervision bloquante, note interne à retirer, et Xavier relit et décide.** Tant qu'elle n'est pas transmise, le geste ne lui est dit qu'oralement
- [ ] **Écrire la première séance à deux : la stabilisation non visuelle** — kit d'auto-apaisement corporel et sensoriel, **sans aucune imagerie**
- [ ] **Écrire la stimulation bilatérale comme `seance-duo`** — ⭐ **l'aidant fait le geste, Kokoro tient la cadence.** Pas d'instrument à développer : ce qui reste est du contenu clinique. ⚠️ **Point dur de format à traiter d'abord** — `sequence` ne sait pas exprimer une **répétition en séries** *([`companion/PROGRAMME.md`](companion/PROGRAMME.md) §3)*
- [ ] **Réouverture de l'EMDR** sous les critères de [`psy/README.md`](psy/README.md) §6, après avis du Dr Isorni
- [ ] ⚠️ **Trancher avec le Dr Isorni, au déverrouillage de la phase 3 seulement : qui tient l'instrument quand Xavier verbalise du matériel lourd** — c'est **C10**, et la question se pose **avant**, jamais pendant

### Transverse

- [ ] **Obtenir les textes intégraux des corpus 2 et 3** — Fairburn (2008), Craske et al. (2014), Barlow & Craske. ⚠️ **Deux références restent à vérifier avant toute citation en brief** ; chaque README dit lesquelles
- [ ] **Verser les trois PDF HAS** dans [`psy/docs/references/originales/`](psy/docs/references/originales/) — publics et gratuits, URL dans [`has-recommandations/`](psy/docs/corpus/has-recommandations/README.md)

---

## 3. ⏱️ Les deux échéances qui structurent le trimestre

| Date | Événement | Conséquences |
|---|---|---|
| **03/09/2026, 12h30** | **Consultation Dr Isorni** | **La dernière avant fin septembre.** Brief à écrire au week-end du 29-30/08, **email à envoyer avant** — un créneau ne suffit pas à découvrir un SAOS sévère, une NASH et six questions à la fois |
| **07/09/2026** | **Départ en Tunisie, 3 semaines ou plus** | ⭐ **Un tiers du trimestre.** Le palier 0 PPC doit être bouclé avant. La PPC part — **portée au niveau atteint, sans progression**. ⭐ **Aucun palier ne progresse pendant le séjour, et on redescend d'un palier à la reprise, sur les trois chantiers** — décidé maintenant, pas subi sur place : **décider avant est précisément ce qui empêche de le vivre comme un échec**. ⚠️ **Le voyage est une exposition agoraphobique majeure, et ce n'est pas une exposition thérapeutique** — ni graduée, ni choisie, sans sortie. ✈️ **Le 3114 ne fonctionne pas depuis l'étranger.** ✅ **Première période sans mission professionnelle depuis longtemps** : la seule variable d'ajustement du dossier tombe à zéro — **observer si les shutdowns baissent vaudra plus que n'importe quelle échelle** |
