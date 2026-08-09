# PLAN — Psychologue/Psychiatre virtuel pour Xavier

**Statut :** brainstorming clos — feuille de route arrêtée (v1.0 — 08/08/2026)
**Méthode :** document vivant, enrichi au fil des questions/réponses entre Xavier et Claude (7 tours).
**Base de référence :** `ressources/xavier/Rapport psychiatrique et psychologique.md` (v2.0) + `ressources/xavier/Biopsie hépatique - Dr Bouarioua.md`

---

## 0. Objectif

Construire un dispositif complet — pas un chatbot — qui assure à Xavier :
1. une expertise clinique supérieure à celle d'un psy français généraliste, **spécifiquement sur son profil** ;
2. un suivi psychologique/psychiatrique continu, **en complément** du Dr Isorni (jamais en substitution) ;
3. un programme de thérapies structuré, outillé par des applications sur mesure ;
4. un programme d'hygiène de vie (activité physique, alimentation, sommeil, récupération) ;
5. une présence quotidienne incarnée sur Android — **Kokoro (心)**, un visage numérique visible en permanence.

### Contraintes non négociables issues du profil

| Contrainte | Origine | Conséquence de conception |
|---|---|---|
| **Aphantasie** | §6.4, §9.15 | Aucune technique de visualisation. Tout passe par le **verbal**, le **corporel**, et l'**exposition in vivo**. Une consigne « imagine la scène » est un bug. |
| **Shutdowns** (perte de parole en surcharge) | §9.16, §10.5 | L'interface doit rester utilisable **sans parler ni écrire** : boutons uniques, mot-code, mode « je ne peux plus ». |
| **Empathie cognitive effondrée / affective intacte** | §9.1 | Le psy virtuel doit être **explicite, littéral, sans sous-entendu** — jamais « tu vois ce que je veux dire ». |
| **Camouflage = moteur de l'anxiété** | §9.6 | L'outil ne doit **jamais** exiger de performance sociale ni de gestion de face. Zéro jugement, zéro attente implicite. |
| **Charges à réduire, pas motivation à créer** | §9.13 (Groden : « Positif » 1,50) | Pas de gamification punitive ni de streaks culpabilisants. Le levier est la **réduction de charge**, pas la motivation. |
| **Hypersensibilités 4 canaux** | §6.1 B4 | UI sobre : pas de son surprise, pas de flash, pas d'animation brusque, contraste maîtrisé, palette douce. |
| **Rigidité / routines / intolérance au changement** | §6.1 B2 | L'app ne change **jamais** son interface sans annonce. Prévisibilité = fonctionnalité. |
| **Deux mécanismes de crise distincts** | §9.14 | Parade panique (exposition/respiration) ≠ parade vasovagale (**tension appliquée**). L'app doit demander *lequel* et ne jamais les confondre. |
| **Risque suicidaire à surveiller** | §6.4, §10.1 | Protocole de crise câblé en dur, non contournable, avec le **3114**. |

---

## 1. Axe A — Rendre Claude plus expert que tout psy français

> ✅ **Priorité n° 1 de construction.** Tout le reste en dépend.

### 1.1 Où se situe réellement l'avantage sur un psy humain

Ce n'est pas « avoir lu plus de livres » — c'est structurel, et six leviers se cumulent :

| # | Levier | Pourquoi aucun psy français ne peut l'égaler |
|---|---|---|
| 1 | **Hyper-spécialisation mono-patient** | Un psy a 40 patients ; ce dispositif en a **un**. Il peut connaître le dossier de Xavier mieux que Xavier. |
| 2 | **Mémoire longitudinale parfaite** | Aucun humain ne relit 40 comptes-rendus avant chaque séance. Ici c'est le fonctionnement par défaut. |
| 3 | **Aucun coût de camouflage** ⭐ | **Le levier décisif.** Chez un psy humain, Xavier paie le camouflage *pendant la séance elle-même* (§5.5, §9.6) : il décode un visage, gère sa présentation, surveille comment il est perçu. Une part du bénéfice thérapeutique est mangée par le coût de la relation thérapeutique. Ici : **zéro visage à lire, zéro face à tenir, zéro jugement à anticiper.** Le canal est écrit, littéral, asynchrone. |
| 4 | **Disponibilité au moment utile** | Une crise à 3 h du matin, un shutdown en plein conflit, une salle d'attente avant une biopsie : les moments qui comptent ne tombent jamais pendant le créneau mensuel. |
| 5 | **Sur-mesure sur les angles morts** | Un psy français généraliste connaît mal le TSA adulte niveau 1, confond régulièrement panique et vasovagal, et ignore le plus souvent l'aphantasie — les trois clés de ce dossier. Ici, elles sont **câblées en contrainte de conception**. |
| 6 | **Traçabilité et contre-expertise** | Chaque affirmation adossée à une source citable (DSM-5, HAS, littérature) + une passe de supervision qui challenge les conclusions. |

**Limite honnête, à écrire noir sur blanc dans le dispositif :** ce que le psy virtuel n'a pas, c'est le corps, la prescription, la responsabilité légale et l'alliance thérapeutique humaine. Le Dr Isorni et un psychologue en présentiel restent structurellement irremplaçables sur l'exposition in vivo accompagnée, l'EMDR encadré et la pharmacologie.

### 1.2 Architecture retenue

**Trois surfaces, un seul dossier.** Données **locales, versionnées dans le dépôt privé, transportées par Syncthing P2P** (arbitrage du 09/08/2026 — cf. §6 et `psy/SYNCHRO.md`).

```
psy/
  agent/          note d'aiguillage — les skills vivent dans .claude/skills/psy-*
  dossier/        mémoire longitudinale (SOURCE DE VÉRITÉ, Markdown + JSON)
  corpus/         référentiels cliniques indexés
  protocoles/     protocoles thérapeutiques opérationnels (fiches actionnables)
  web/            outils de séance desktop — TypeScript strict (règles projet)
  android/        app compagnon — Kotlin natif + Compose
  SYNCHRO.md      décisions de synchronisation et de sécurité des données
```

*(Correction apportée à la réalisation, 09/08/2026 : Claude Code ne découvre les skills d'un projet que dans `.claude/skills/<nom>/SKILL.md`. Les placer dans `psy/agent/` les aurait rendus invisibles. `psy/agent/README.md` ne conserve que la table des rôles et les invariants communs.)*

Le **dossier** est la pièce maîtresse : source de vérité unique, lue et écrite par les trois surfaces, synchronisée avec le téléphone. C'est lui qui rend le suivi longitudinal possible.

### 1.2.1 Répartition des trois surfaces

> **Critère de répartition, simple et à ne jamais enfreindre :**
> **ce qui doit être là au moment où ça arrive → Android. Ce qui demande de la surface et du calme → desktop.**

| Surface | Rôle | Techno |
|---|---|---|
| **Claude Code (PC)** | Séances conversationnelles, analyse, briefs Isorni, tenue du dossier | Skills Markdown |
| **Web desktop** ✅ *(ajouté au tour 5)* | Outils de séance visuels et interactifs : stimulation bilatérale, passation d'échelles, paliers d'exposition, tableaux de bord | TypeScript strict |
| **Android** | Compagnon permanent, check-in quotidien, **outils de crise**, repas | Kotlin natif + Compose |

**Pourquoi le desktop n'est pas un confort mais une nécessité clinique :** la stimulation bilatérale visuelle exige une **amplitude de mouvement oculaire** suffisante — sur un écran de téléphone elle est dérisoire, sur un écran desktop elle est correcte. De même, les échelles longues (CAT-Q, TAS-20, DIVA-5) et les tableaux de bord d'évolution sont illisibles sur mobile. Le choix ergonomique est aussi le bon choix thérapeutique.

**Ce qui doit rester sur Android, sans discussion possible :** la **tension appliquée** (elle sert en salle d'examen, pas au bureau) et le **bouton shutdown** (il sert en plein conflit). Les deux doivent être accessibles **en un geste, depuis l'écran verrouillé**.

### 1.3 Les rôles (skills) envisagés

| Skill | Rôle | Statut |
|---|---|---|
| `psy-seance` | Conduite d'une séance de fond (ouverture, travail, clôture, compte-rendu) | ❓ à concevoir |
| `psy-crise` | **Triage crise** : panique ? vasovagal ? shutdown ? → oriente vers la bonne parade, jamais la mauvaise | ❓ à concevoir |
| `psy-bilan` | Passation et cotation des échelles (TAS-20, CAT-Q, VVIQ, GAD-7, PHQ-9, DIVA-5) | ❓ à concevoir |
| `psy-brief-isorni` | Brief d'une page avant chaque consultation mensuelle | ❓ à concevoir |
| `psy-journal` | Check-in quotidien à faible coût cognitif | ❓ à concevoir |
| `psy-hygiene` | Programme et suivi d'hygiène de vie | ❓ à concevoir |
| `psy-superviseur` | Contre-expertise : challenge les conclusions du thérapeute, détecte l'effet miroir | ❓ à concevoir |

### 1.4 Posture retenue : **direct, littéral, clinique**

- Il dit les choses **sans emballage et sans sous-entendu** ; jamais « tu vois ce que je veux dire ».
- Il **ne demande jamais à Xavier de décoder** : toute intention est explicitée.
- Il **peut et doit contredire** Xavier (garde-fou anti-effet-miroir, cf. §6).
- Il **annonce ce qu'il fait** avant de le faire (prévisibilité = fonctionnalité, cf. §0).

### 1.5 Corpus à constituer

> ✅ **Les quatre corpus prioritaires sont validés (tour 6).** À récupérer et indexer dans `psy/corpus/`.

| Priorité | Source | Objet |
|---|---|---|
| ✅ **1** | **Protocole tension appliquée (Öst)** — complet | Le plus rentable immédiatement : court, validé, enseignable en 1-2 séances, et à acquérir **à froid** puisque aucun geste médical n'est programmé |
| ✅ **2** | **TCC alimentaire + intéroception** | Nouvelle priorité depuis la stéatose. Structure externe, régulation sans signal de satiété, dépistage de la perte de contrôle (échelle **BES**). ⭐ **C'est le corpus où l'avantage sur un psy généraliste est le plus net** : le croisement TSA × conduite alimentaire × déficit intéroceptif est peu diffusé en pratique française |
| ✅ **3** | **TCC de l'agoraphobie** — exposition graduée | Cible la plus ancienne (23 ans). Paliers écrits, adapté TSA, **in vivo uniquement** (aphantasie) |
| ✅ **4** | **Recommandations HAS** — TSA adulte, troubles anxieux | Standard de soin français. Surtout utile pour argumenter auprès des professionnels et pour les dossiers MDPH |
| — | DSM-5 (intégral + extraits TSA/TDAH/anxio-dépressif) | ✅ déjà dans `ressources/spécialisées/` |
| — | Littérature citée au rapport v2.0 (§11) | ✅ références en main : camouflage, alexithymie, aphantasie, shutdowns, burnout autistique |
| ⏸️ | Protocole EMDR — stimulations bilatérales non visuelles | Reporté avec l'axe EMDR (§3.1) |
| ❓ | ACT / défusion cognitive | À évaluer pour le TAG — vérifier la compatibilité aphantasie |
| ❓ | CIM-11 | Optionnel |

---

## 2. Axe B — Suivi psychologique/psychiatrique continu

> ✅ **Cadence retenue : séance de fond hebdomadaire + check-in quotidien léger.**

### 2.1 Les trois rythmes

| Rythme | Format | Durée | Support |
|---|---|---|---|
| **Quotidien** | Check-in à faible coût cognitif — quelques mesures, pas de journal libre | < 2 min | App Android |
| **Hebdomadaire** | Séance de fond : ouverture / travail / clôture / compte-rendu écrit. ✅ **Créneau : week-end en journée** — fixe, annoncé, jamais déplacé sans préavis | 45-60 min | Claude Code + web desktop |
| **Mensuel** | Brief d'une page pour le Dr Isorni + revue des tendances | 10 min | Claude Code → PDF/Markdown |

### 2.2 Ce qu'on mesure au quotidien

Principe : **le moins d'items possible, chacun justifié cliniquement**. Candidats issus du rapport :

| Mesure | Pourquoi (source) |
|---|---|
| **Nombre de shutdowns / pertes de parole** | « Le meilleur indicateur de suivi » du burnout autistique (§10.5) — indicateur n° 1 |
| Charge de camouflage du jour | Prédit anxiété, dépression et épuisement indépendamment des traits autistiques (§9.6) |
| Charge sensorielle | Catégorie de stress n° 2 au Groden (3,50) |
| Sommeil | Contraint par le nourrisson ; critère C du TAG à documenter (§6.2.d) |
| Nombre de missions actives | Seule variable d'ajustement disponible (§9.17, §10.4) |
| Anxiété / humeur | Vigilance dépressive et suicidaire au long cours (§6.4) |
| Crises (type + contexte) | **Doit distinguer panique / vasovagal / shutdown** — les parades diffèrent (§9.14) |

### 2.3 Échelles jamais passées, à programmer

Toutes recommandées au §10.3 du rapport, aucune administrée à ce jour :

| Échelle | Objet | Priorité rapport | Durée |
|---|---|---|---|
| **VVIQ** | Objectivation de l'aphantasie — **conditionne quelles techniques sont utilisables** | Haute | 5 min |
| **TAS-20** | Alexithymie (« le chaînon manquant du dossier », §9.2) | Haute | 10 min |
| **CAT-Q** | Intensité du camouflage | Haute | 15 min |
| **DIVA-5** | TDAH adulte — à trancher avant tout traitement stimulant | Haute si plainte persiste | 60 min |
| GAD-7 / PHQ-9 | Anxiété et dépression, en routine mensuelle | — | 5 min |

### 2.4 Proactivité : **opportuniste, mais à coût de refus nul**

Décision : le dispositif **peut interpeller Xavier hors horaire**, sur détection (silence prolongé, indicateurs qui se dégradent, journée surchargée, veille d'un geste médical).

> ⚠️ **Tension identifiée avec le profil — et sa résolution.** Le rapport documente une intolérance marquée à l'imprévu (§6.1 B2 ; Groden : changement de superviseur 5/5) et pose la prévisibilité comme fonctionnalité. Une sollicitation opportuniste est, par nature, un imprévu.
>
> **Résolution retenue : rendre le *timing* imprévisible mais la *forme* absolument invariable.** Concrètement —
> - **jamais de son, jamais de vibration, jamais de plein écran** : l'interpellation est une modification silencieuse de l'expression du visage kawaï, rien de plus ;
> - **format strictement identique à chaque fois** : une phrase, une raison explicite (« je te sollicite parce que : 3 shutdowns cette semaine contre 0 la précédente »), jamais de question ouverte ;
> - **refuser coûte un geste et zéro justification** : un bouton « pas maintenant » qui ne redemande rien, ne culpabilise pas, ne relance pas ;
> - **plafond dur** : maximum 1 sollicitation opportuniste par jour, 3 par semaine ;
> - **zéro streak, zéro compteur de régularité, zéro « tu n'as pas ouvert l'app depuis 4 jours »** (§9.13 : réduire les charges, pas motiver).

### 2.5 Détection d'alerte et escalade

Trois niveaux, à câbler explicitement :

| Niveau | Déclencheur | Réponse |
|---|---|---|
| **Veille** | Tendance à la baisse sur 2 semaines | Mention en séance hebdo |
| **Alerte** | Shutdowns en hausse nette, signes de burnout autistique, humeur en chute | Interpellation directe + point à mettre au brief Isorni |
| **Crise** | Idéation suicidaire, détresse aiguë | **Protocole câblé en dur, non contournable** : 3114 affiché, contact d'urgence, aucune tentative de « gérer seul » (cf. §6) |

---

## 3. Axe C — Programme de thérapies + applications

> ✅ **Approche retenue : on commence par la TCC.** L'EMDR est réduit à son instrument (§3.1) ; le retraitement du matériel traumatique est reporté.

Cibles réordonnées au 08/08/2026, après la confirmation de la stéatose hépatique :

| # | Cible | Protocole indiqué | Outil applicatif envisageable |
|---|---|---|---|
| 1 | **Conduite alimentaire** 🔴 *(nouveau, priorité haute)* | **TCC + structure externe** — compenser l'absence de signal de satiété (§4.2). Dépistage de la perte de contrôle. Prescription somatique en jeu. | Aide au cadrage des repas (quantité décidée avant, servie une fois), journal sans jugement calorique |
| 2 | **Phobie sang-injection-accident** (syncopes) | **Tension appliquée (Öst)** — 1 à 2 séances, à acquérir **à froid** (aucun geste programmé : fenêtre idéale) | App de guidage des cycles de contraction 10-15 s, utilisable en salle d'examen |
| 3 | **Agoraphobie / transports** (23 ans) | **TCC** : psychoéducation des 13 symptômes + exposition graduée **in vivo**, paliers écrits | App de paliers d'exposition + suivi anxiété en temps réel + kit sensoriel |
| 4 | **Shutdowns** (couple) | Protocole négocié à froid avec Chourouk | **Bouton shutdown** → envoie le mot-code à Chourouk, coupe les sollicitations |
| 5 | **Alexithymie + intéroception** ⭐ | Identification guidée — **des émotions et des signaux corporels**. Double rendement : même famille de fonctions que la satiété (§4.2) | Aide au nommage émotionnel et corporel (sans visualisation) |
| 6 | **Camouflage / pacing énergie sociale** | Budget d'énergie sociale, récupération planifiée | Compteur de charge sociale, alerte de dette |
| 7 | **TAG / ruminations** | **TCC** (restructuration) ou ACT (défusion) | Journal de soucis, report programmé |
| 8 | **Deuil du lien avec sa fille aînée** | Travail de deuil actif, canal basse intensité | Rappel doux, aide à la rédaction de lettres sans exigence de réponse |
| 9 | **Trauma d'enfance** ⏸️ | EMDR — **reporté**, cf. §3.1 | Instrument de stimulation bilatérale construit dès maintenant |

**Pourquoi cet ordre tient debout :** les cibles 1 à 3 sont toutes des **protocoles TCC comportementaux, à effet mesurable et à faible risque d'ouverture émotionnelle**. Elles construisent exactement la capacité de régulation que la phase de stabilisation de l'EMDR exigerait de toute façon. On ne perd pas de temps : **on fait la phase 2 de l'EMDR sans l'appeler EMDR.**

**Note EMDR :** Xavier a une réceptivité hypnotique élevée documentée (« transe » sur consigne oculaire vers le bas, §9.5) — un atout rare et directement exploitable. C'est aussi ce qui rend le point 3.1 ci-dessous d'autant plus sérieux : une réceptivité élevée signifie que ça *marchera*, y compris dans le mauvais sens.

### 3.1 EMDR — arbitrage rendu : **on commence par la TCC**

> ✅ **Décision du 08/08/2026.** Après objection argumentée (conservée ci-dessous), Xavier arbitre : **on commence par de la TCC.** L'EMDR est ramené à sa **phase 0 — l'instrument seul** : l'app de stimulation bilatérale (point mobile, bips alternés, vibration) est construite et disponible, mais **aucun protocole de retraitement n'est conduit**, ni sur matériel léger ni sur matériel d'enfance.
>
> Le sujet est **suspendu, pas clos** — il sera rouvert quand la stabilisation sera acquise et, idéalement, après avis du Dr Isorni. Les critères de déverrouillage ci-dessous restent la référence pour cette réouverture.

<details>
<summary><b>Objection argumentée du 08/08/2026 (conservée pour mémoire)</b></summary>

La demande initiale était un protocole complet auto-guidé, y compris sur le matériel d'enfance. Objection formulée au titre de la posture directe (§1.4) — **elle ne portait pas sur la capacité de Xavier à conduire un protocole seul, ni sur l'EMDR auto-administré en général, mais sur le calendrier** :

1. **La stabilisation précède le retraitement — c'est la phase 2 du protocole, pas une précaution optionnelle.** On n'ouvre pas de matériel traumatique sans capacité d'auto-apaisement installée et vérifiée. Chez toi, cette phase demande d'abord d'être *réinventée* : le « lieu sûr » standard est une **visualisation**, donc structurellement inopérant (§9.15). Il faut lui substituer un ancrage corporel et sensoriel réel — ce qui reste entièrement à construire.
2. **Tu es dans une fenêtre de surcharge documentée, pas de stabilité.** Le §9.17 la détaille : mariage, naissance, nuits fragmentées, charge sensorielle maximale au domicile — ton refuge historique —, trois missions simultanées, deuil actif du lien avec ton aînée, parcours somatique en cours. Le rapport nomme le risque : **burnout autistique**.
3. **Ton traitement a été repris hier (07/08/2026).** Le §10.1 rappelle qu'il faut 2 à 4 semaines par palier pour juger d'un effet. Ouvrir du matériel traumatique lourd pendant une titration rend ininterprétable ce qui viendra ensuite : une dégradation sera-t-elle la molécule, le retraitement, ou la charge ? Tu perds la lisibilité au moment exact où elle compte le plus.

Le risque concret n'est pas théorique : c'est l'**abréaction sans filet** — une reviviscence qui s'ouvre sans se refermer, sur du matériel lourd (violences multi-sources, foyer d'urgence, harcèlement scolaire, idéation suicidaire adolescente). Chez quelqu'un qui perd la parole sous surcharge, la sécurité manquante est précisément celle-là : **en shutdown, tu ne peux plus demander d'aide.**

**Contre-proposition — même destination, séquençage différent (référence pour la réouverture) :**

| Phase | Contenu | Condition d'entrée |
|---|---|---|
| **0. Instrument** | L'app de stimulation bilatérale (point mobile, bips alternés, vibration) est construite immédiatement et utilisable tout de suite | Aucune |
| **1. Stabilisation** | Construction et test d'un kit d'auto-apaisement **non visuel** (ancrages corporels, sensoriels, verbaux) + tension appliquée + psychoéducation | Immédiat |
| **2. Retraitement léger** | Protocole complet auto-guidé sur **matériel récent et de charge modérée** : conflit, fin de mission, épisode médical | Kit de stabilisation testé et efficace au moins 3 fois |
| **3. Retraitement lourd** | Protocole complet sur le **matériel d'enfance** | **Critères objectifs, chiffrés, définis à l'avance** — pas « quand je me sentirai prêt » |

**Critères de déverrouillage de la phase 3, à figer maintenant** (proposition, à valider) : traitement stabilisé depuis ≥ 6 semaines · shutdowns en baisse ou stables sur 4 semaines consécutives · charge professionnelle plafonnée · phase 2 conduite avec succès au moins 3 fois · sujet évoqué au moins une fois avec le Dr Isorni.

**Garde-fous câblés en dur, quelle que soit la phase :** critères d'arrêt automatique de séance · protocole de clôture obligatoire (jamais de fin sur du matériel ouvert) · plafond de fréquence · escalade vers le protocole de crise (§6) si détresse aiguë · aucune séance en période de shutdown.

</details>

**Question ouverte restante :** une app par outil, ou une app unique multi-modules ?

---

## 4. Axe D — Alimentation, activité physique, sommeil 🔴

> ✅ **Trois points dégradés déclarés le 08/08/2026 :** activité physique quasi nulle · sommeil insuffisant et fragmenté · **apport alimentaire d'environ le double d'un adulte**.

### 4.1 🔴 Cet axe n'est plus de l'hygiène de vie — c'est une prescription médicale

> **Tranché le 08/08/2026 par le courriel de la Dr Leila Bouarioua** (hépato-gastro-entérologue) → `ressources/xavier/Biopsie hépatique - Dr Bouarioua.md`.

La biopsie hépatique de juillet 2026, dont l'indication était inconnue du dossier, est expliquée : **stéatose hépatique liée au surpoids (MASLD), sans fibrose, sans surveillance particulière, avec perte de poids impérative (« absolument », « +++++ ») pour éviter l'aggravation.** Le traitement psychotrope n'est pas en cause.

**Deux lectures à tenir ensemble :**
- **Pronostic favorable** — une stéatose sans fibrose est le stade **le plus réversible**. Le foie n'est pas abîmé, la trajectoire est modifiable.
- **Changement de statut** — l'alimentation et l'activité physique cessent d'être du confort de vie. Elles deviennent le **traitement de première ligne d'une atteinte organique documentée**. Cet axe passe donc devant dans les priorités de construction, à égalité avec l'Axe A.

### 4.2 ⭐ Le déficit intéroceptif — et la règle de conception qui en découle

Confirmé directement par Xavier le 08/08/2026 : **« je ne ressens effectivement pas la satiété »**, pour un apport estimé au double d'un adulte.

L'alexithymie probable (§9.2 du rapport) est un déficit d'identification des **états internes émotionnels**. L'intéroception recouvre la même famille de signaux — faim, satiété, rythme cardiaque, tension. **Ne pas sentir qu'on n'a plus faim est le pendant corporel exact de ne pas sentir quelle émotion on éprouve.** Le déficit intéroceptif est largement documenté dans le TSA.

**Conséquence — la prescription standard est structurellement inapplicable.** « Mangez moins, écoutez votre satiété, arrêtez-vous quand vous n'avez plus faim » demande d'utiliser une fonction perceptive dont Xavier est dépourvu. C'est rigoureusement la même erreur que « imaginez un lieu sûr » chez un aphantasique :

| Fonction absente | Prescription standard inapplicable | Substitution |
|---|---|---|
| Imagerie mentale (aphantasie) | « Imaginez un lieu sûr », exposition en imagination | Verbal, corporel, exposition in vivo |
| **Perception de la satiété** | « Écoutez votre satiété » | **Structure externe** : portions décidées **avant** le repas, servies une fois, horaires fixes |

> **Règle de conception centrale du dispositif, valable bien au-delà de l'alimentation :**
> **quand un signal interne manque, on ne le remplace pas par de la volonté — on le remplace par une structure externe explicite.**
>
> Corollaire à énoncer clairement : un échec antérieur de perte de poids ne documente **aucun manque de volonté**. Il documente une consigne inadaptée au profil.

**Reste à trancher :** existe-t-il des **épisodes de perte de contrôle** (grande quantité en peu de temps, impossibilité de s'arrêter) ? Réponse actuelle : « je ne sais pas / je ne perçois pas bien » — cohérente avec le déficit intéroceptif, et qui impose de **mesurer avant de conclure**. Si oui → **hyperphagie boulimique (DSM-5 307.51 / F50.8)**, diagnostic distinct, traitement distinct.

### 4.3 Principes de conception du programme

Issu du §9.13 du rapport : **« Positif » à 1,50 au Groden — les renforçateurs fonctionnent normalement. Il n'y a rien à motiver ; il y a des charges à réduire et des repères à fournir.**

- **Zéro streak, zéro culpabilisation, zéro compteur de régularité, aucun jugement calorique.**
- **Structure externe plutôt que volonté** (§4.2) — c'est le principe organisateur, pas une astuce.
- **Prévisibilité** : mêmes horaires, mêmes plats en rotation, aucune injonction à « varier ».

### 4.4 Les trois chantiers

**Données anthropométriques (08/08/2026) : 1,77 m · ≈ 110 kg · IMC 35,1 → obésité de classe II.**

> ⭐ **Le premier palier utile n'est pas « perdre 30 kg », c'est « perdre 5,5 kg ».**
>
> Les seuils hépatologiques sont gradués par effet. Recommandations **EASL-EASD-EASO 2024** : **≥ 5 % en cas de surpoids ou d'obésité** (la fourchette 3-5 % souvent citée ne vaut que pour les MASLD de poids normal) · ≥ 7-10 % si stéatohépatite ou fibrose. Même gradation histologique chez Vilar-Gomez et al. (*Gastroenterology*, 2015). Or Xavier n'a **ni stéatohépatite, ni fibrose** — les seuils hauts visent des lésions qu'il n'a pas. **Sa cible est ≥ 5 %, soit ≈ 5,5 kg → 104,5 kg.**
>
> Ce n'est pas de la présentation encourageante, c'est la condition de faisabilité : chez quelqu'un dont le profil exige des paliers écrits, chiffrés et prévisibles — exactement la logique de l'exposition graduée — un objectif global et lointain est structurellement inopérant.

| # | Chantier | Piste retenue | Statut |
|---|---|---|---|
| 1 | **Alimentation** 🔴 | Structure externe : quantités décidées **avant** le repas, servies une fois, pas de resservage. Journal sans jugement calorique. Dépistage de la perte de contrôle (**BES**). **Objectif de première marche : −5,5 kg → 104,5 kg** (≥ 5 %, cible EASL 2024). | ❓ à concevoir — **priorité haute** |
| 2 | **Activité physique** 🔴 | Compatible agoraphobie → **domicile d'abord** (pas de salle, pas de trajet, pas de regard). Sans compétition (désintérêt coté 6/6 à l'échelle Attwood). **À 110 kg : sans impact** — vélo d'appartement, rameur, renforcement ; pas de course ni de saut (contrainte articulaire). Format court, quotidien, invariable, progression écrite à l'avance. Agit sur la stéatose **même à perte de poids modeste**. | ❓ à concevoir — **priorité haute** |
| 3 | **Sommeil** ⚠️ | Deux volets désormais. **(a) Organisationnel** : protéger un créneau de récupération sans sollicitation, roulement explicite avec Chourouk. **(b) Médical** : voir §4.5 — le sommeil fragmenté n'est peut-être pas seulement dû au nourrisson. | ❓ à concevoir |

### 4.5 ⚠️ Hypothèse nouvelle — apnées du sommeil, et ce qu'elle remet en cause

IMC 35,1 + sommeil déclaré insuffisant et fragmenté = indication de dépistage d'un **SAOS** (syndrome d'apnées obstructives du sommeil). L'obésité de classe II en est le principal facteur de risque.

**Ce qui rend l'hypothèse sérieuse, c'est le recouvrement symptomatique** — le SAOS produit fatigue diurne, **troubles de la concentration**, irritabilité et humeur dégradée, tous déjà attribués à autre chose dans le dossier :

| Symptôme | Attribution actuelle | Alternative |
|---|---|---|
| Sommeil fragmenté | Nourrisson | ± SAOS |
| **« Trouble de la concentration, distractibilité »** (certificat Isorni) | 3 hypothèses au rapport §6.3 : TDAH / inattention anxieuse / attention autistique | **± dette de sommeil respiratoire — 4e hypothèse, jamais envisagée** |
| Fatigabilité, irritabilité | Critère C du TAG, à documenter | ± SAOS |
| Dégradation de 2026 | Surcharge de vie + traitement (§9.17) | ± SAOS |

**Deux conséquences directes :**
1. **Le DIVA-5 doit passer après, pas avant.** Une dette de sommeil respiratoire mime l'inattention ; un stimulant prescrit sur un SAOS non traité masque le problème au lieu de le traiter.
2. **Boucle d'aggravation hépatique** : le SAOS aggrave la stéatose par l'hypoxie intermittente, indépendamment de l'IMC. S'il est présent, il entretient exactement ce que la Dr Bouarioua demande de faire régresser.

**Examen :** polygraphie ventilatoire nocturne — simple, à domicile, **et sans aucune aiguille ni geste invasif**, ce qui la rend compatible avec la phobie sang-injection-accident (§6.2.f du rapport).

**Question à porter au Dr Isorni :** la Dr Bouarioua écarte l'imputabilité du traitement **sur le foie** — pas sur **le poids**. Or la **paroxétine**, prise ~1 an jusqu'au 07/08/2026, est l'ISRS le plus associé à une prise de poids. La question exacte est : *« la paroxétine a-t-elle contribué à la prise de poids qui a causé la stéatose ? »* Point favorable : la venlafaxine, reprise le 07/08/2026, a un profil pondéral plus neutre — le changement décidé pour des motifs psychiatriques se trouve aussi favorable métaboliquement.

**Données manquantes à recueillir en priorité :** poids, taille, IMC (aucun chiffre au dossier) ; compte-rendu anatomopathologique ; bilan biologique hépatique de départ ; historique pondéral.

**Déjà acté par ailleurs :** caféine à limiter (panicogène documenté), pas d'automédication alcool, budget de récupération après événements sociaux, plafonnement des missions professionnelles (§10.4-10.5).

---

## 5. Axe E — Kokoro, le visage numérique Android

> ❓ **En discussion.**

### Réponse technique à la question posée (« au-dessus de toutes les autres apps, même le verrouillage ? »)

**Oui, en grande partie — et l'app étant personnelle et sideloadée, aucune contrainte Google Play ne s'applique.**

| Besoin | Mécanisme Android | Faisabilité |
|---|---|---|
| Flotter au-dessus de toutes les apps | `SYSTEM_ALERT_WINDOW` + `TYPE_APPLICATION_OVERLAY` (bulle type Messenger) | ✅ Standard, permission à accorder une fois |
| Rester vivant en permanence | Foreground Service + exemption d'optimisation batterie | ✅ |
| S'afficher **par-dessus l'écran de verrouillage** | Une Activity avec `setShowWhenLocked(true)` + `setTurnScreenOn(true)` — l'overlay classique, lui, passe **sous** le keyguard | ✅ mais autre mécanisme que l'overlay |
| Réveiller l'écran pour une alerte | `full-screen intent` (comme un appel entrant) | ✅ |
| Présence permanente sur l'écran verrouillé | Notification persistante / widget lockscreen | ✅ (variable selon version/constructeur) |
| Remplacer entièrement l'écran d'accueil | App launcher personnalisée | ✅ (option radicale) |

**Le point d'attention réel n'est pas Android, c'est le fabricant.**

> ✅ **Cible retenue : Samsung Galaxy (One UI).** Terrain plutôt favorable — moins hostile que MIUI/HyperOS, bon support des overlays et de `showWhenLocked`. Deux réglages à faire une fois, sans lesquels le compagnon mourra silencieusement au bout de quelques heures :
> - **Paramètres → Batterie → Limites d'utilisation en arrière-plan → Applications jamais mises en veille** → y ajouter l'app ;
> - désactiver l'**optimisation de la batterie** pour l'app (`REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`).
>
> À prévoir dans l'app : un **écran de diagnostic** qui vérifie ces réglages et guide leur activation — sur One UI, une mise à jour système peut les réinitialiser.

### Stack retenue

> ✅ **Kotlin natif + Jetpack Compose.** Le cœur du projet — overlay système, foreground service, `showWhenLocked`, full-screen intent — est constitué d'APIs natives ; en cross-platform ce sont des ponts fragiles, en natif c'est direct. Compose est par ailleurs excellent pour animer un visage.
>
> **Conséquence à assumer :** c'est le seul morceau du projet qui sort du TypeScript strict imposé par les règles projet. Le reste (scripts, éventuel backend, tooling) y reste.

### Le personnage : **Kokoro (心)**

> ✅ **Nom retenu : Kokoro** — le mot japonais qui désigne indissociablement le cœur et l'esprit, racine de 心理学 *(shinrigaku)*, « psychologie ».
>
> Deux raisons de le trouver juste ici : en japonais **cœur et esprit ne sont pas séparés** — pertinent pour un dossier où l'angoisse passe par le ventre depuis le CM2 et où la satiété ne se sent pas ; et le nom désigne **l'objet du soin, pas une promesse de résultat** — aucun jour de mauvaise passe ne le fera sonner faux.

### Design du visage

> ✅ **Personnage nommé, expressif, muet.** Une identité stable, qui exprime des états par le visage et communique **par texte uniquement**.
> ✅ **Registre graphique : trait minimal, ligne claire.** Forme simple, contour fin, deux yeux et une bouche ; peu de surface colorée, contraste maîtrisé. Choisi pour l'hypersensibilité visuelle documentée (§6.1 B4) — et accessoirement trivial à animer proprement en Compose, ce qui sert directement la règle des transitions lentes.

Le choix du muet est cliniquement solide, pas seulement esthétique : une voix qui surgit est une agression sensorielle (hypersensibilité auditive documentée, §6.1 B4) ; le texte se relit à froid, ne force pas le tempo, et reste lisible **en shutdown** — précisément quand le canal verbal est coupé (§9.16).

**Règles de conception non négociables :**

| Règle | Origine |
|---|---|
| Jamais de son, jamais de vibration non sollicitée | Hypersensibilité auditive et tactile (§6.1 B4) |
| Transitions d'expression **lentes et continues** — jamais de changement brusque | Hypersensibilité visuelle ; intolérance à l'imprévu |
| Le visage **n'attend jamais rien** de Xavier : pas d'air déçu, pas de reproche, pas de « ça fait longtemps » | Camouflage = moteur de l'anxiété (§9.6) ; zéro exigence sociale |
| Palette douce, contraste maîtrisé, pas de flash ni d'animation rapide | Hypersensibilité visuelle (lumières intenses, halogènes) |
| L'apparence **ne change jamais sans annonce** — pas de skin surprise, pas d'événement saisonnier | Rigidité / routines (§6.1 B2) |
| Il **explicite toujours pourquoi** il s'exprime | Empathie cognitive : ne jamais demander de décoder (§9.1) |

### État de repos — 99 % du temps

> ✅ **Il respire, c'est tout.** Micro-animation lente et constante, sans information. Présence pure, zéro charge cognitive, zéro risque d'interprétation.

**Nuance retenue pour ne pas perdre un apport clinique.** L'option écartée (« le visage reflète ta charge mesurée ») avait une valeur propre : elle faisait du compagnon un **organe intéroceptif externalisé** — rendre visible un signal interne non perçu, exactement la règle du §4.2. Compromis intégré, qui respecte le choix fait :

- **par défaut, il respire** — aucune information imposée, aucune interprétation à faire ;
- **la charge mesurée est consultable en un tap**, jamais affichée d'elle-même ;
- **jamais de tristesse ni de reproche** dans l'expression, en aucune circonstance — uniquement des niveaux de charge, sans valence morale.

**Questions ouvertes :** nom du personnage (série « psy » en cours de proposition) ; registre graphique précis.

---

## 6. Axe transversal — Sécurité, éthique, données

> ❓ **En discussion.**

- **Non-substitution** : le dispositif complète le Dr Isorni, ne le remplace pas. Jamais de conseil de modification de traitement.
- **Protocole de crise** : idéation suicidaire → escalade immédiate, **3114** affiché, contact d'urgence.
- **Données de santé** : ✅ **dossier versionné dans le dépôt privé `github.com/XavierBoubert/psy`** (arbitrage de Xavier, 09/08/2026) **+ Syncthing P2P** pour le transport PC↔téléphone (chiffré TLS, aucun serveur tiers ne stocke). Détail, conditions et porte de sortie : `psy/SYNCHRO.md`.
  > ⚠️ **Cette décision assouplit sciemment la règle « rien ne part vers un tiers ».** GitHub est un tiers, et il héberge un dossier médical complet. Arbitrage rendu en connaissance de cause, motivé par la traçabilité clinique (le git log est l'audit) et la sauvegarde hors-machine — `ressources/xavier/` y était de toute façon versionné depuis l'origine du projet.
  > **Conditions attachées** : dépôt privé (à revérifier périodiquement) · 2FA + clé SSH · aucun fork, aucun collaborateur, aucune GitHub Action ayant accès au contenu.
  > **Porte de sortie si l'arbitrage est révisé** : chiffrement au repos (`git-crypt` ou `age`) sur `psy/dossier/` et `ressources/xavier/` — contrepartie : Claude Code ne lit plus rien sans déverrouillage, et chaque surface doit gérer la clé.
  > **Ce qui reste vrai sans réserve** : hors GitHub et hors appels à Claude, **aucune donnée ne part vers un tiers** — pas de cloud santé, pas de service d'analyse, pas de télémétrie. Syncthing est du pair-à-pair : il ne dépose rien sur un serveur.
- **Effet miroir** : un psy virtuel toujours d'accord serait nocif. Le dispositif doit pouvoir contredire Xavier — d'où le rôle `psy-superviseur` (§1.3).

### 6.1 Tiers dans la boucle

> ✅ **Retenu : Dr Isorni (briefs mensuels) + Chourouk (protocole shutdown).** Écarté pour l'instant : psychologue en présentiel.

| Tiers | Ce qu'il reçoit | Contrôle |
|---|---|---|
| **Dr Isorni** | Brief d'une page avant chaque consultation mensuelle : évolution chiffrée, effets du traitement, questions ouvertes à trancher (TAG, phobie sang-injection, nosophobie, TDAH, trauma). Format médecin : dense, factuel, sans interprétation gratuite. | Xavier relit et décide de transmettre ou non, à chaque fois |
| **Chourouk** | (1) Le **mot-code shutdown** en temps réel quand Xavier déclenche le bouton ; (2) une **fiche explicative** une fois pour toutes : le silence est neurologique et non relationnel (§9.16), l'empathie affective est intacte (§9.1). | Aucun accès au journal, aux séances, ni aux mesures |

> ⚠️ **Point à ne pas manquer.** Le §10.2 du rapport recommande explicitement un **psychologue en présentiel** (reprise de contact avec Catherine Gazeau) pour ce que le virtuel ne peut structurellement pas faire : EMDR encadré sur le matériel traumatique de l'enfance, exposition in vivo accompagnée, et apprentissage supervisé de la tension appliquée. L'écarter est un choix légitime, mais il **crée un trou** dans les cibles thérapeutiques n° 1, 2 et 3 de l'Axe C. À rediscuter une fois le dispositif en place — noté comme dette assumée, pas comme oubli.

---

## 7. Feuille de route

Séquençage retenu : **Axe A minimal, puis Axe D à fond** (§tour 5). Le foie n'attend pas, mais on évite de piloter à l'aveugle.

### Étape 0 — Socle minimal *(Axe A)* ✅ **en place — 09/08/2026**
- [x] Créer l'arborescence `psy/` (agent, dossier, corpus, protocoles, web, android) → `psy/README.md` + un README par surface
- [x] **Schéma du dossier** : format de la mémoire longitudinale → **`psy/dossier/SCHEMA.md`** (normatif) + 5 gabarits dans `psy/dossier/gabarits/`
- [x] **Fiche de profil condensée** → **`psy/dossier/profil.md`** (permanent) **+ `psy/dossier/etat.md`** (courant). *Le rapport fait désormais 670 lignes en v2.3 ; la fiche en tient 12 sections opérationnelles.*
- [x] Skills `psy-seance` et `psy-journal` → **`.claude/skills/psy-*/SKILL.md`** *(et non `psy/agent/` : Claude Code ne découvre les skills que là — cf. `psy/agent/README.md`)*
- [x] Synchro chiffrée PC↔téléphone → **décision arrêtée** dans `psy/SYNCHRO.md` : **dépôt privé** (historique) **+ Syncthing P2P** (transport PC↔Android). ⏸️ *Installation reportée à l'Étape 5 : il n'y a pas encore d'app avec laquelle synchroniser.*

**Trois décisions de conception prises à cette étape, qui contraignent toute la suite :**
1. ⭐ **On cote des comportements observables, pas des ressentis** (règle R6 du schéma). Alexithymie + déficit intéroceptif : « note ton anxiété sur 10 » demande d'utiliser une fonction déficitaire — c'est la même erreur que « écoute ta satiété ». D'où un journal qui compte des shutdowns, des retraits sensoriels et des renoncements, et **aucune échelle introspective**.
2. **Un fichier par événement, append-only** (R1, R2). Contrainte Syncthing autant que principe de dossier clinique : deux appareils qui écrivent dans un même fichier produisent un conflit ; un fichier par événement le rend structurellement impossible.
3. **Le format suit l'auteur** (R3) : ce que Claude écrit est du Markdown + frontmatter, ce qu'une app écrit est du JSON. Pas de conversion, pas de format bâtard.

⚠️ **Arbitrage assumé, à garder visible :** le dossier est versionné sur GitHub, ce qui contredit partiellement le §6 (« rien ne part vers un tiers »). Décision de Xavier du 09/08/2026, motivée par la traçabilité et la sauvegarde ; conditions et porte de sortie (chiffrement `git-crypt`/`age`) documentées dans `psy/SYNCHRO.md` §2.

### Étape 1 — Axe D, prescription médicale 🔴
- [x] ~~Poids, taille, IMC~~ → **1,77 m · 110 kg · IMC 35,1** (08/08/2026)
- [ ] **Programme alimentaire à structure externe** : quantités décidées avant le repas, servies une fois, horaires fixes, zéro jugement calorique — **objectif de première marche : −5,5 kg → 104,5 kg**
- [ ] **Programme d'activité physique** : domicile, **sans impact** (vélo/rameur/renforcement), format court, quotidien, invariable, progression écrite à l'avance
- [ ] **Dépister la perte de contrôle alimentaire** (échelle **BES**) → départage hyperphagie boulimique / déficit intéroceptif
- [ ] Recueillir : historique pondéral, compte-rendu anatomopathologique, bilan hépatique de départ, bilan métabolique (HbA1c, lipides, tension)
- [ ] ⚠️ **Demander un dépistage du SAOS** (polygraphie ventilatoire nocturne) — §4.5

### Étape 2 — Instrumentation du suivi *(Axe B)*
- [ ] Check-in quotidien (d'abord en web/Claude Code, migré sur Android à l'étape 5)
- [ ] Passer les échelles jamais administrées : **VVIQ**, **TAS-20**, **CAT-Q**, **BES**
- [ ] Premier **brief Dr Isorni** — questions ouvertes du rapport + paroxétine et prise de poids (§4.4) + **dépistage SAOS avant le DIVA-5** (§4.5) + surveillance tensionnelle sous venlafaxine à IMC 35

### Étape 3 — Outils de crise *(Axe C, première brique Android)*
- [ ] Corpus + protocole **tension appliquée (Öst)**, à acquérir **à froid**
- [ ] App tension appliquée — guidage des cycles de contraction, utilisable en salle d'examen
- [ ] **Bouton shutdown** + protocole négocié à froid avec Chourouk + fiche explicative pour elle
- [ ] Protocole de crise câblé (3114, escalade)

### Étape 4 — TCC de l'agoraphobie *(Axe C)*
- [ ] Corpus exposition graduée adapté TSA
- [ ] Paliers écrits + outil web desktop de suivi d'exposition
- [ ] Psychoéducation des 13 symptômes de l'attaque de panique

### Étape 5 — Kokoro *(Axe E)*
- [ ] App compagnon : overlay, foreground service, `showWhenLocked`
- [ ] Écran de diagnostic des réglages batterie One UI
- [ ] Visage à trait minimal + états + transitions lentes
- [ ] Migration du check-in quotidien sur Android

### Étape 6 — Réouverture de l'EMDR ⏸️
- [ ] Instrument de stimulation bilatérale (web desktop) — **peut être construit dès maintenant**
- [ ] Réouverture du protocole **sous les critères du §3.1**, après avis du Dr Isorni

### Transverse
- [ ] **Rapport v2.1** — intégrer stéatose, déficit intéroceptif, conduite alimentaire (liste des points en fin de `Biopsie hépatique - Dr Bouarioua.md`)
- [ ] Récupérer et indexer les 4 corpus prioritaires (§1.5)
- [ ] Rediscuter la **dette assumée** : psychologue en présentiel (§6.1)

---

## 8. Journal du brainstorming

| Date | Décisions prises |
|---|---|
| **09/08/2026** | ✅ **Étape 0 exécutée — le socle existe.** Arborescence `psy/`, **schéma du dossier** (`psy/dossier/SCHEMA.md`, normatif) + 5 gabarits, **fiche de profil condensée** (`profil.md`) **et état courant** (`etat.md`) — la distinction permanent/courant est nouvelle et remplace la « fiche unique » prévue —, skills **`psy-seance`** et **`psy-journal`** dans `.claude/skills/`. ⭐ **Décision de conception structurante : on cote des comportements observables, pas des ressentis** (règle R6) — le journal compte des shutdowns, des retraits sensoriels, des renoncements et des activités investies ; **aucune échelle introspective**, parce que demander « note ton anxiété sur 10 » à quelqu'un d'alexithymique avec déficit intéroceptif est la même erreur que « écoute ta satiété ». ✅ **Données** : dossier **versionné dans le dépôt privé** (traçabilité clinique) — arbitrage assumé qui contredit partiellement le §6, conditions et porte de sortie documentées ; **Syncthing P2P** retenu pour le transport PC↔Android, installation à l'Étape 5. ⚠️ **Correction au plan** : les skills vont dans `.claude/skills/`, pas dans `psy/agent/` (Claude Code ne les découvre que là). |
| 08/08/2026 | **Tour 1 de questions.** ✅ Ordre de construction : **Axe A (cerveau clinique) d'abord**. ✅ Architecture : **hybride Claude Code (PC) + app Android**, synchronisés par le dossier. ✅ Données : **locales, repo privé + synchro chiffrée PC↔téléphone**. ✅ Posture : **directe, littérale, clinique**, avec droit de contredire. Identification du levier décisif face à un psy humain : **l'absence de coût de camouflage** (§1.1). |
| 08/08/2026 | 🔴 **SAOS SÉVÈRE NON TRAITÉ — le fait le plus important du dossier à ce jour.** Polysomnographie du Dr Roisman versée : **IAH 35/h, 61 micro-éveils/h, SP 7,2 %, Épworth 14, ISI 20, MPJ 31/h**. Diagnostiqué le **19/01/2026**, PPC prescrite — **non utilisée**. Jamais transmis au psychiatre (courrier adressé à la généraliste). → Rapport **v2.3** : §6.6 et §10.8 créées, §6.3/§9.17/§9.21 révisés, §9.23 ajouté. **Trois conséquences pour le dispositif :** (1) l'Axe D gagne un levier majeur — la privation de sommeil dérègle ghréline/leptine, donc le SAOS **aggrave le déficit de satiété déjà présent** : boucle SAOS→poids→SAOS documentée par +6 kg en 9 mois ; (2) **l'exposition graduée devient l'outil n° 1** — la désensibilisation à la PPC *est* une exposition graduée, donc le même outil sert au masque et aux transports ; (3) **la coordination inter-praticiens devient une fonction du dispositif** — six médecins, aucune vue d'ensemble. Email au Dr Isorni rédigé (`20260808 Email au Dr Isorni.md`). |
| 08/08/2026 | **Rapport passé en v2.1 + sourçage bibliographique vérifié.** 11 sections du rapport touchées ; §6.5 (conduite alimentaire et déficit intéroceptif) et §10.7 (versant somatique) créées ; enseignements 19 à 22 ajoutés. Recherches en ligne effectuées → **correction d'une imprécision** : la fourchette « 3-5 % » ne vaut que pour les MASLD de **poids normal** ; en obésité, la cible EASL-EASD-EASO 2024 est **≥ 5 %** (le chiffre final, 104,5 kg, est inchangé). Toutes les références v2.1 disposent désormais de liens vérifiés. |
| 08/08/2026 | **Données anthropométriques reçues : 1,77 m · 110 kg · IMC 35,1 (obésité de classe II).** ⭐ Cadrage de l'objectif : Xavier n'ayant **ni stéatohépatite ni fibrose**, le seuil utile est **≥ 5 %, soit ≈ 5,5 kg** — pas les 7-10 % qui visent des lésions qu'il n'a pas. **Première marche : −5,5 kg → 104,5 kg.** ⚠️ **Hypothèse nouvelle : SAOS** (§4.5) — IMC 35 + sommeil fragmenté ; recouvre la fatigue, l'irritabilité et surtout la **« distractibilité »** du certificat Isorni, qui gagne une **4e hypothèse différentielle jamais envisagée** ; à éliminer **avant** le DIVA-5, et aggrave la stéatose par hypoxie intermittente. Examen sans aiguille, donc compatible phobie sang-injection. ⚠️ Bémol ajouté sur la surveillance tensionnelle sous venlafaxine à IMC 35. Activité physique recadrée : **sans impact** à 110 kg. |
| 08/08/2026 | **Tour 7 — clôture du brainstorming.** ✅ Le compagnon s'appelle **Kokoro (心)** — cœur-esprit indissociés, racine de 心理学 « psychologie » ; nomme l'objet du soin, pas une promesse de résultat. ✅ Graphisme : **trait minimal, ligne claire**. ✅ **Feuille de route en 7 étapes** arrêtée (§7). ❓ Poids/taille toujours manquants — bloquant pour l'étape 1. |
| 08/08/2026 | **Tour 6 de questions.** ✅ Corpus prioritaires validés : **tension appliquée (Öst)**, **TCC alimentaire + intéroception**, **TCC agoraphobie**, **HAS**. ✅ Compagnon au repos : **il respire, rien d'autre** — la charge mesurée reste consultable en un tap, jamais imposée (§5). ❓ Nom : registre kawaï écarté au profit d'un **registre « psy »** — nouvelle série à proposer. |
| 08/08/2026 | **Tour 5 de questions.** ✅ Séquençage : **Axe A minimal, puis Axe D à fond**. ✅ Architecture : **trois surfaces** — Claude Code (séances), **web desktop** (outils de séance, TypeScript strict), **app Android compagnon unique** (quotidien + crise). Critère de répartition figé : *ce qui doit être là au moment où ça arrive → Android ; ce qui demande de la surface et du calme → desktop* (§1.2.1). Le desktop est validé **cliniquement** et pas seulement ergonomiquement : la stimulation bilatérale exige une amplitude oculaire impossible sur mobile. ✅ Séance de fond : **week-end en journée**. ✅ Personnage : **nom japonais, registre kawaï assumé**. ❓ Poids/taille toujours manquants. |
| 08/08/2026 | **Tour 4 de questions — tour le plus déterminant à ce jour.** 🔴 **Stéatose hépatique confirmée par biopsie** (courriel Dr Leila Bouarioua, hépato-gastro-entérologue) : liée au surpoids, **sans fibrose**, sans surveillance, **perte de poids impérative** ; psychotropes non imputables. → Source versée : `ressources/xavier/Biopsie hépatique - Dr Bouarioua.md`. **L'Axe D change de statut : prescription médicale, plus hygiène de vie** — passe en priorité haute. ⭐ **Absence de perception de la satiété confirmée directement** → déficit intéroceptif, extension corporelle de l'alexithymie ; d'où la **règle de conception centrale du dispositif : signal interne absent → structure externe, jamais volonté** (§4.2). ✅ EMDR : **arbitrage rendu — on commence par la TCC** ; EMDR réduit à l'instrument, retraitement suspendu (§3.1). ✅ Rapport : **v2.1 après collecte complémentaire**. ❓ Perte de contrôle alimentaire : non tranchée (« je ne perçois pas bien ») → à mesurer. |
| 08/08/2026 | **Tour 3 de questions.** ✅ Hygiène de vie : 3 points dégradés — activité nulle, sommeil fragmenté, **apport alimentaire doublé**. → ⚠️ **Élément clinique nouveau, absent de tout le dossier** : hypothèse principale = déficit intéroceptif (satiété), pendant corporel de l'alexithymie (§4.1) ; ⚠️ **question à poser au médecin** : lien possible avec la biopsie hépatique de juillet 2026, d'indication inconnue (§4.2). ✅ Compagnon : **personnage nommé, expressif, muet** (texte uniquement — lisible en shutdown). ✅ Stack : **Kotlin natif + Compose**. ⚠️ EMDR : protocole complet auto-guidé demandé → **objection argumentée et contre-proposition en 4 phases** avec critères de déverrouillage chiffrés (§3.1) — **arbitrage en attente**. |
| 08/08/2026 | **Tour 2 de questions.** ✅ Cadence : **séance de fond hebdomadaire + check-in quotidien léger**. ✅ Proactivité : **opportuniste** — tension avec l'intolérance à l'imprévu identifiée et résolue par « timing variable, forme invariable, refus à coût nul » (§2.4). ✅ Téléphone : **Samsung Galaxy / One UI** (réglages batterie à câbler, §5). ✅ Tiers : **Dr Isorni** (briefs mensuels) + **Chourouk** (protocole shutdown) ; psychologue en présentiel écarté → **dette assumée** notée au §6.1. |
