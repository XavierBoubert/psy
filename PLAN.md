# PLAN — Psychologue/Psychiatre virtuel pour Xavier

**Statut :** brainstorming clos — feuille de route arrêtée (v1.2 — 09/08/2026). **Étape 0 close · Étapes 1, 2 et 3 ouvertes en parallèle** (§7)
**Méthode :** document vivant, enrichi au fil des questions/réponses entre Xavier et Claude (7 tours), puis au fil de la réalisation.
**Base de référence :** `ressources/xavier/Rapport psychiatrique et psychologique.md` (**v2.4**) + `ressources/xavier/Biopsie hépatique.md` + `ressources/xavier/20260119 Gabriel ROISMAN Conclusion Polysomnographie.md`

> ⚠️ **Ce document a été écrit avant les v2.2, v2.3 et v2.4 du rapport. Trois sections sont périmées et conservées telles quelles** — le plan est un **journal de conception**, pas un document courant :
> - **§4.1** — décrit une *stéatose simple sans surveillance particulière*, d'après le seul courriel de la Dr Bouarioua. **L'histologie a tranché autrement (v2.2) : stéato-hépatite non alcoolique (NASH), sans fibrose.**
> - **§4.4** — cible « ≥ 5 %, ≈ 5,5 kg ». **Portée à 7-10 % → 99-102,3 kg** (v2.2), la NASH relevant des seuils hauts.
> - **§4.5** — le SAOS y figure comme une *hypothèse à dépister*. **C'est un diagnostic sévère constitué (IAH 35/h), insuffisamment traité** — PPC prescrite, **utilisée de façon très irrégulière**, IAH résiduel < 6/h sous appareil (v2.3 puis v2.4).
>
> **Ce qui fait foi au quotidien : `psy/dossier/etat.md` et le rapport v2.4.** Seule la feuille de route (§7) est tenue à jour.

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

> *Statuts mis à jour le 09/08/2026. Les skills vivent dans **`.claude/skills/psy-*`**, pas dans `psy/agent/` — cf. la correction notée au §1.2. La table courante fait foi dans `psy/agent/README.md`.*

| Skill | Rôle | Statut |
|---|---|---|
| `psy-seance` | Conduite d'une séance de fond (ouverture, travail, clôture, compte-rendu) | ✅ **écrit** — Étape 0 |
| `psy-journal` | Check-in quotidien à faible coût cognitif | ✅ **écrit** — Étape 0 |
| `psy-crise` | **Triage crise** : panique ? vasovagal ? shutdown ? → oriente vers la bonne parade, jamais la mauvaise | ✅ **écrit** — 09/08/2026 |
| `psy-bilan` | Passation et cotation des échelles (TAS-20, CAT-Q, VVIQ, GAD-7, PHQ-9, DIVA-5) | ✅ **écrit** — 09/08/2026 |
| `psy-brief-isorni` | Brief d'une page avant chaque consultation mensuelle | ✅ **écrit** — 09/08/2026 · **échéance dure : le brief du 03/09 s'écrit au week-end du 29-30/08** |
| `psy-hygiene` | Programme et suivi d'hygiène de vie | ✅ **écrit** — 09/08/2026 |
| `psy-superviseur` | Contre-expertise : challenge les conclusions du thérapeute, détecte l'effet miroir | ✅ **écrit** — 09/08/2026 |

> ⭐ **Les sept rôles existent depuis le 09/08/2026 — la table est close.** Ce qui manquait n'était ni la doctrine ni les fiches : c'était **l'exécutant**. Un protocole de crise sans skill de crise est un document que personne n'ouvre au moment où il sert ; des instruments d'échelle sans skill de passation sont un corpus qu'on ne fait pas passer.
>
> 🔴 **`psy-superviseur` supervise Claude, pas Xavier**, et le risque qu'il traite est structurel : **presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** Sa première passe l'a démontré immédiatement — elle a trouvé, **dans `psy-seance` lui-même**, le critère « on ne passe pas au palier suivant tant que le précédent n'est pas confortable » : mot pour mot la faute R6 que le dispositif se félicitait d'avoir corrigée dans le rapport §10.8, et qui avait survécu à l'audit de cohérence du matin.

### 1.4 Posture retenue : **direct, littéral, clinique**

- Il dit les choses **sans emballage et sans sous-entendu** ; jamais « tu vois ce que je veux dire ».
- Il **ne demande jamais à Xavier de décoder** : toute intention est explicitée.
- Il **peut et doit contredire** Xavier (garde-fou anti-effet-miroir, cf. §6).
- Il **annonce ce qu'il fait** avant de le faire (prévisibilité = fonctionnalité, cf. §0).

### 1.5 Corpus à constituer

> ✅ **Les quatre corpus prioritaires sont validés (tour 6).** À récupérer et indexer dans `psy/corpus/`.
>
> **État au 09/08/2026 : 1 des 4 est versé** — le n° 1 (tension appliquée d'Öst) → `psy/corpus/tension-appliquee/`. Les corpus 2, 3 et 4 restent à récupérer. S'y ajoute, non prévu à cette table, le corpus des **échelles** (`psy/corpus/echelles/`, Étape 2).

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

> **État au 09/08/2026** *(Étape 2)* **: les instruments existent désormais** — `psy/corpus/echelles/` porte VVIQ, TAS-20, CAT-Q et GAD-7/PHQ-9 complets (items, cotation, seuils, limites), plus un BES partiel doublé d'une grille comportementale de substitution. **Aucune n'a encore été passée.** Le plan de passation daté fait foi dans `psy/corpus/echelles/README.md` §3, pas dans la table ci-dessous.

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

> **Tranché le 08/08/2026 par le courriel de la Dr Leila Bouarioua** (hépato-gastro-entérologue) → `ressources/xavier/Biopsie hépatique.md` *(fichier fusionné le 08/08/2026 avec le compte-rendu anatomopathologique ; c'est ce dernier qui a corrigé le diagnostic en NASH — cf. l'avertissement en tête de document)*.

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

> ✅ **Conception arrêtée au tour 7** (nom, graphisme, stack, état de repos, règles de conception). **Rien n'est construit** — l'app est l'Étape 5, et aucune ligne de Kotlin n'existe.

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

**Questions tranchées depuis** *(mise à jour du 09/08/2026 — cette ligne listait encore comme ouvertes deux décisions prises plus haut dans la même section)* **:** le nom est **Kokoro (心)**, le registre graphique est le **trait minimal, ligne claire**.

**Questions refermées depuis** *(10/08/2026)* **:** ✅ **app unique multi-modules** — tranché à l'ouverture de l'Étape 5 (`psy/android/PLAN-KOKORO.md` §3.2) : trois apps seraient trois icônes à retrouver au pire moment. ✅ **Écran de crise** — 🔴 **il ne porte aucun numéro d'urgence.** Les numéros d'appel (15, 112, 114) ont été retirés du dispositif le 10/08/2026 à la demande de Xavier ; l'écran porte le **mot-code à Chourouk** (canal SMS validé par elle) et l'accès à la **tension appliquée**. Motifs : `psy/protocoles/crise-escalade.md` §0.

---

## 6. Axe transversal — Sécurité, éthique, données

> ✅ **Tranché le 09/08/2026** — arbitrage des données de santé rendu (dépôt privé + Syncthing P2P), protocole de crise écrit comme fiche (`psy/protocoles/crise-escalade.md`), tiers dans la boucle arrêtés (§6.1). Reste ouvert : le rôle `psy-superviseur` (garde-fou anti-effet-miroir), non planifié à ce jour.

- **Non-substitution** : le dispositif complète le Dr Isorni, ne le remplace pas. Jamais de conseil de modification de traitement.
- **Protocole de crise** : idéation suicidaire → escalade immédiate, **3114** affiché, contact d'urgence. ✅ **Écrit comme fiche actionnable le 09/08/2026 → `psy/protocoles/crise-escalade.md`** (triage en 3 questions, sécurité avant mécanisme, ⭐ **114 par SMS** — seule voie utilisable en shutdown —, numéros de substitution pour la Tunisie).
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
- [x] **Fiche de profil condensée** → **`psy/dossier/profil.md`** (permanent) **+ `psy/dossier/etat.md`** (courant). *Le rapport fait 693 lignes en v2.4 ; la fiche en tient 12 sections opérationnelles.*
- [x] Skills `psy-seance` et `psy-journal` → **`.claude/skills/psy-*/SKILL.md`** *(et non `psy/agent/` : Claude Code ne découvre les skills que là — cf. `psy/agent/README.md`)*
- [x] Synchro chiffrée PC↔téléphone → **décision arrêtée** dans `psy/SYNCHRO.md` : **dépôt privé** (historique) **+ Syncthing P2P** (transport PC↔Android). ⏸️ *Installation reportée à l'Étape 5 : il n'y a pas encore d'app avec laquelle synchroniser.*

**Trois décisions de conception prises à cette étape, qui contraignent toute la suite :**
1. ⭐ **On cote des comportements observables, pas des ressentis** (règle R6 du schéma). Alexithymie + déficit intéroceptif : « note ton anxiété sur 10 » demande d'utiliser une fonction déficitaire — c'est la même erreur que « écoute ta satiété ». D'où un journal qui compte des shutdowns, des retraits sensoriels et des renoncements, et **aucune échelle introspective**.
2. **Un fichier par événement, append-only** (R1, R2). Contrainte Syncthing autant que principe de dossier clinique : deux appareils qui écrivent dans un même fichier produisent un conflit ; un fichier par événement le rend structurellement impossible.
3. **Le format suit l'auteur** (R3) : ce que Claude écrit est du Markdown + frontmatter, ce qu'une app écrit est du JSON. Pas de conversion, pas de format bâtard.

⚠️ **Arbitrage assumé, à garder visible :** le dossier est versionné sur GitHub, ce qui contredit partiellement le §6 (« rien ne part vers un tiers »). Décision de Xavier du 09/08/2026, motivée par la traçabilité et la sauvegarde ; conditions et porte de sortie (chiffrement `git-crypt`/`age`) documentées dans `psy/SYNCHRO.md` §2.

### Étape 1 — Axe D, prescription médicale 🔴 *(ouverte le 09/08/2026 — protocoles écrits)*

> ⚠️ **Étape révisée le 09/08/2026.** Sa rédaction initiale datait du rapport v2.0 : elle demandait un *dépistage* du SAOS (depuis lors **diagnostiqué sévère et non traité**) et visait −5,5 kg (cible **révisée à 7,7-11 kg** en v2.2, l'histologie montrant une NASH et non une stéatose simple). Deux conséquences : **la PPC devient la cible n° 1 de l'étape**, et l'objectif pondéral est corrigé.

- [x] ~~Poids, taille, IMC~~ → **1,77 m · 110 kg · IMC 35,1** (08/08/2026)
- [x] ~~Demander un dépistage du SAOS~~ → **sans objet : SAOS sévère diagnostiqué le 19/01/2026** (IAH 35/h), PPC prescrite et **utilisée de façon très irrégulière** *(corrigé le 09/08/2026 d'après la consultation du 04/05 — v2.4 ; ⭐ IAH résiduel < 6/h sous appareil)*
- [x] 🔴 **Protocole de reprise de la PPC par désensibilisation** → `psy/protocoles/ppc-desensibilisation.md` — palier 0 logistique + 6 paliers d'exposition, critères de passage comportementaux, matériel et réglages à exiger
- [x] **Programme alimentaire à structure externe** → `psy/protocoles/alimentation-structure-externe.md` — quantités décidées avant le repas, servies une fois, horaires fixes, rotation stable, zéro jugement calorique — **cible : −7,7 à −11 kg → 99-102,3 kg** (7-10 %, NASH sans fibrose)
- [x] **Programme d'activité physique** → `psy/protocoles/activite-physique-sans-impact.md` — domicile, sans impact, 5 paliers de 5 à 20 min, deux variantes matériel, feu vert médical préalable
- [x] **Skill `psy-hygiene`** → `.claude/skills/psy-hygiene/` (09/08/2026) — conduite des trois chantiers. ⭐ **Le passage de palier se compte dans le journal, il ne se demande pas** : « tu te sens prêt ? » est une question intéroceptive, « 3 jours sur 3 au bout du minuteur » est un fait vérifiable. Porte aussi la frontière de non-substitution du chantier — **réglages, interface et origine de la fuite appartiennent au prestataire et au Dr Roisman**, pas au dispositif
- [ ] **Exécuter le palier 0 de la PPC** *(réécrit le 09/08/2026 après la consultation du 04/05/2026)* : récupérer le **relevé de télésuivi**, faire trancher l'**origine de la fuite (masque ou bouche ?)** — elle commande le choix d'interface —, essayer plusieurs interfaces, vérifier l'état de la prise en charge, demander une **consultation de reprise** au Dr Roisman, et **informer le Dr Isorni**, seul praticien encore dans l'ignorance
- [~] **Dépister la perte de contrôle alimentaire** (échelle **BES**) → départage hyperphagie boulimique / déficit intéroceptif. ⚠️ **Instrument non obtenu au 09/08/2026** — les 16 items pondérés ne sont pas librement diffusés et une restitution approximative produirait un score faux. Fiche `psy/corpus/echelles/bes.md` : cotation, seuils, trois voies d'obtention (Dr Isorni le 03/09, Dr Bouarioua, article de Brunault et al. 2016) **et une grille comportementale de 5 questions utilisable immédiatement**, qui ne dépend d'aucune source externe et convient mieux au profil que le BES lui-même
- [ ] Recueillir : historique pondéral, bilan hépatique de départ, bilan métabolique (HbA1c, lipides, tension), **feu vert médical** pour l'activité physique
- [ ] **Envoyer l'email au Dr Isorni** (`ressources/xavier/20260808 Email au Dr Isorni.md`, rédigé, non envoyé) — c'est lui qui débloque les questions pharmacologiques du chantier

> ⭐ **Ce que l'écriture des protocoles a produit et qui n'était pas prévu :** le rapport §10.8 posait « confortable plusieurs jours de suite » comme critère de passage des paliers PPC. **« Confortable » est un ressenti** — c'est-à-dire précisément ce que la règle R6 du schéma du dossier interdit de coter chez quelqu'un d'alexithymique avec déficit intéroceptif. Les critères ont donc été convertis en **comptages comportementaux** (« 3 jours consécutifs où le minuteur est allé au bout sans retrait »), et un **palier intermédiaire** a été inséré entre « assis éveillé » et « sieste ». C'est la première fois que le socle de l'Étape 0 corrige une recommandation du rapport : le dispositif commence à fonctionner comme un dispositif, pas comme un classeur.

### Étape 2 — Instrumentation du suivi *(Axe B)* ⏱️ **ouverte le 09/08/2026 — sous contrainte de calendrier**

> **Deux dates fixent l'échéancier** *(09/08/2026)* : **consultation Dr Isorni le jeudi 03/09/2026 à 12h30**, puis **départ en vacances en Tunisie le 07/09/2026**. Il reste **25 jours utiles** et **4 séances de fond** (16, 22-23, 29-30/08 — plus celle du 09/08).

- [x] **Verser les instruments de mesure** → **`psy/corpus/echelles/`** (09/08/2026) : VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9 complets — items, cotation, seuils, limites — et BES partiel. **Sans instrument, « passer les échelles » n'était pas exécutable.** Inclut un **plan de passation daté** sur les 4 séances restantes et le câblage du **protocole de crise sur l'item 9 du PHQ-9**
- [x] **Skills `psy-bilan` et `psy-brief-isorni`** → `.claude/skills/` (09/08/2026) — **les instruments et le gabarit existaient, l'exécutant non.** `psy-bilan` : passation item par item, cotation recopiée depuis la fiche à chaque fois, ⛔ **interdiction de restituer un instrument de mémoire** (la règle qui a bloqué le BES), item 9 du PHQ-9 câblé sur le protocole de crise. `psy-brief-isorni` : chiffres **calculés depuis le journal et jamais estimés**, un jour sans check-in n'est jamais compté comme un zéro, réserves obligatoires, **aucune proposition pharmacologique même interrogative**
- [x] 🔴 **Démarrer le check-in quotidien** (Claude Code, migré sur Android à l'étape 5) — ✅ **démarré le 09/08/2026**, `dossier/journal/2026-08-09.json`. **24 jours utiles restent avant le 03/09.** Le brief ne tirera plus ses chiffres d'un répertoire vide — à condition que la cadence tienne, et **un jour manqué ne se rattrape ni ne se commente**
- [ ] **Premier brief Dr Isorni**, à écrire à la séance du **week-end du 29-30/08** — questions ouvertes du rapport + alprazolam et SAOS + ferritine + bilan hépatique de référence + paroxétine et prise de poids + surveillance tensionnelle sous venlafaxine à IMC 35. **Le DIVA-5 reste bloqué tant que le SAOS n'est pas effectivement traité**
- [ ] **Envoyer l'email au Dr Isorni avant la consultation** — un créneau ne suffit pas à découvrir un SAOS sévère, une NASH et six questions à la fois
- [ ] Passer les échelles jamais administrées, **dans l'ordre imposé par `psy/corpus/echelles/README.md` §3** : **VVIQ** (09/08) · **TAS-20** (16/08) · **CAT-Q + GAD-7/PHQ-9** (22-23/08) · **BES** dès obtention de l'instrument. La séance du 29-30/08 n'en passe aucune — elle écrit le brief. Plafond : 20 min d'échelles par séance, l'échelle n'est jamais la cible de la séance
- [ ] ✈️ **Sécuriser l'ordonnance de venlafaxine pour le séjour** à la consultation du 03/09 — elle tombe 4 jours avant le départ. **Logistique, pas posologie.**

### Étape 3 — Outils de crise *(Axe C, première brique Android)* 🔴 *(ouverte le 09/08/2026 — les deux protocoles écrivables sont écrits)*
- [x] Corpus + protocole **tension appliquée (Öst)**, à acquérir **à froid** → `psy/corpus/tension-appliquee/` + `psy/protocoles/tension-appliquee.md` — 4 paliers d'acquisition, ⭐ **déclenchement sur repères externes et au chronomètre** au lieu des prodromes (le protocole d'origine suppose une intéroception intacte), phrase écrite d'avance pour le soignant
- [x] **Protocole de crise câblé (3114, escalade)** → `psy/protocoles/crise-escalade.md` — triage en 3 questions fermées, **sécurité avant mécanisme**, les 3 niveaux veille/alerte/crise, ⭐ ~~**le 114 par SMS** et les numéros de substitution pour le séjour en Tunisie~~ → **révisé en v1.1 le 10/08/2026 : les numéros d'appel d'urgence (15, 112, 114) sont retirés du dispositif** à la demande de Xavier ; le **3114 est conservé** sur la seule idéation suicidaire. Effet de bord favorable : **plus de liste de numéros à préparer pour la Tunisie** — le protocole devient identique ici et là-bas
- [x] **Skill `psy-crise`** → `.claude/skills/psy-crise/` (09/08/2026) — exécute `crise-escalade.md` sans le résumer. ⭐ **Il porte la seule exception au premier invariant du dispositif : les numéros s'affichent AVANT le chargement de `profil.md` et `etat.md`.** Un contexte chargé n'a jamais aidé personne pendant les trente premières secondes d'une crise. Ajoute un **mode sans parole** opérationnel : bascule sur des choix numérotés (« 1 = ça va · 2 = ça ne va pas · 3 = j'ai besoin d'aide maintenant »), parce qu'**un chiffre est produisible en shutdown, une phrase non**
- [→] ~~App tension appliquée~~ → **déplacée en Étape 5 (Android)** le 09/08/2026, arbitrage de Xavier sur le constat 3 de la supervision. §1.2.1 est catégorique : elle sert **en salle d'examen**, pas au bureau, et doit être accessible **en un geste depuis l'écran verrouillé**. Un guidage desktop aurait été inutilisable exactement là où il sert
- [x] **Protocole shutdown négocié à froid avec Chourouk — fait le 09/08/2026. Mot-code : « shutdown ».** *(Version minimale, dans `psy/protocoles/jour-de-vol.md` §4.)* ✅ **La fiche explicative pour Chourouk est écrite le 09/08/2026** → `psy/protocoles/fiche-chourouk.md` (arbitrage de Xavier sur le constat 2 de la supervision : elle était déclarée dans quatre documents et n'existait nulle part). Reste le **bouton** Android (Étape 5)
- [x] **Commencer le palier 1 de la tension appliquée** — ✅ **démarré le 09/08/2026** (jour 1 : 5 cycles sur 5). 3 min/jour, sans exposition, sans changement d'habitude ; ne consomme pas la règle « un seul chantier à la fois », la PPC restant le chantier n° 1. Critère de passage au palier 2 : **4 blocs complets sur 7 jours**, coté en séance. ⚠️ **Un écart de doctrine relevé à l'exécution** : `etat.md` §5 faisait de la question 13 (tension appliquée / TA) un préalable bloquant, quand `protocoles/tension-appliquee.md` §5 dit explicitement l'inverse. La fiche l'emporte, `etat.md` est corrigé — **mais le fait qui compte est resté entier et part au brief : la tension artérielle de Xavier n'a jamais été mesurée.** Elle n'est pas « contrôlée », elle est **inconnue**

### Étape 4 — TCC de l'agoraphobie *(Axe C)*
- [ ] Corpus exposition graduée adapté TSA
- [ ] Paliers écrits + outil web desktop de suivi d'exposition
- [x] **Psychoéducation des 13 symptômes** de l'attaque de panique → `psy/protocoles/panique-13-symptomes.md` — **avancée le 09/08/2026** pour le vol du 07/09
- [x] **Kit vol** → `psy/protocoles/jour-de-vol.md` — séquence écrite, kit sensoriel, protocole shutdown minimal avec Chourouk. ⚠️ **Ce n'est pas un programme d'exposition** et ça n'en tient pas lieu

### Étape 5 — Kokoro *(Axe E)* 🏗️ *(ouverte le 10/08/2026 — plan de construction écrit)*

> **Le séquençage et les critères de fin font foi dans `psy/android/PLAN-KOKORO.md`** (jalons K0 → K5), pas dans la liste ci-dessous. ⭐ **Décision structurante prise à l'ouverture : le premier livrable de Kokoro est l'écran de crise, pas le visage** — le mot-code avec Chourouk existe depuis le 09/08 et n'a aucun porteur, alors que la période 07/09-28/09 est déclarée à haut risque de shutdown. Deux autres décisions : **app unique multi-modules** (la question « une app par outil ou une app unique ? », ouverte au §3.1 et au §5, est tranchée) et **aucune base de données** — l'app écrit des fichiers JSON, R1/R2/R3 du schéma l'imposent. ⚠️ **Constat vérifié le 10/08 : aucun outillage Android n'est installé sur la machine** — le jalon K0 est l'installation de la chaîne de compilation, et une nuance est apportée au §5 ci-dessus (le **full-screen intent** n'est plus accordé par défaut depuis Android 14).

- [ ] App compagnon : overlay, foreground service, `showWhenLocked`
- [ ] Écran de diagnostic des réglages batterie One UI
- [ ] Visage à trait minimal + états + transitions lentes
- [ ] Migration du check-in quotidien sur Android
- [ ] **App tension appliquée** — guidage des cycles de contraction, **utilisable en salle d'examen** *(déplacée depuis l'Étape 3 le 09/08/2026 — cf. §1.2.1 : elle doit être accessible en un geste depuis l'écran verrouillé, comme le bouton shutdown)*
- [ ] **Écran de crise** — 🔴 **sans aucun numéro d'urgence** *(révisé le 10/08/2026)*. Il porte le **mot-code « shutdown » à Chourouk** (SMS, canal validé par elle le 10/08) et l'accès à la **tension appliquée** — les deux seules choses qui aient jamais servi. ⭐ **Le motif est clinique : une syncope vasovagale ne s'appelle pas, elle s'allonge** ; offrir un appel à la place de la bonne parade était une erreur d'orientation. Le 3114 subsiste dans la seule conduite d'escalade sur idéation suicidaire, jamais sur un écran

### Étape 6 — Réouverture de l'EMDR ⏸️
- [ ] Instrument de stimulation bilatérale (web desktop) — **peut être construit dès maintenant**
- [ ] Réouverture du protocole **sous les critères du §3.1**, après avis du Dr Isorni

### Transverse
- [x] ~~**Rapport v2.1** — intégrer stéatose, déficit intéroceptif, conduite alimentaire~~ → **fait, et dépassé : le rapport est en v2.4** (v2.1 versant somatique · v2.2 NASH et cible 7-10 % · v2.3 SAOS sévère · v2.4 observance PPC réelle)
- [x] **Skill `psy-superviseur`** → `.claude/skills/psy-superviseur/` (09/08/2026) — **le garde-fou anti-effet-miroir du §6 cesse d'être une intention.** 7 contrôles, sortie dans `psy/agent/supervisions/` (**hors `dossier/`** : une supervision porte sur le dispositif, pas sur le patient — donc aucune modification du `SCHEMA.md` n'est requise). Première passe versée : `supervisions/2026-08-09-supervision.md`
- [x] **Arbitrages A2, A3, A4 de la supervision du 09/08 — rendus et exécutés le jour même.** A2 → `protocoles/fiche-chourouk.md` écrite **avant le départ**, le séjour étant la période à plus haut risque de shutdown du trimestre · A3 → l'app de tension appliquée **passe en Étape 5 (Android)**, §1.2.1 étant catégorique · A4 → `corpus/echelles/maia.md` ouverte, ⚠️ **items non obtenus, grille comportementale de substitution versée**
- [ ] Récupérer et indexer les 4 corpus prioritaires (§1.5)
- [ ] Rediscuter la **dette assumée** : psychologue en présentiel (§6.1)

---

## 8. Journal du brainstorming

| Date | Décisions prises |
|---|---|
| **10/08/2026** | 🔴 **Les numéros d'appel d'urgence sortent du dispositif — décision de Xavier, et elle corrige une faute de conception.** 15, 112 et **114** sont retirés des 22 fichiers vivants qui les portaient ; **le 3114 est conservé**, sur le seul déclencheur de l'idéation suicidaire, **jamais affiché en ouverture**. ⭐ **Le motif principal est clinique, et il est de la même famille que la règle §9.19 : une syncope vasovagale ne s'appelle pas, elle s'allonge.** Le dispositif proposait un appel là où la parade est la **tension appliquée** — c'était une **erreur d'orientation présentée comme une sécurité supplémentaire**. S'y ajoutent deux faits que Xavier apporte et que rien dans le dossier ne contredisait : **aucun de ces numéros n'a jamais servi**, et leur affichage permanent était **anxiogène** sur un profil TAG — un dispositif d'urgence omniprésent entretient ce qu'il prétend couvrir. 📌 **Point de méthode : la demande a été instruite avant d'être exécutée**, parce qu'elle était ambiguë sur un point qui compte — sa justification portait sur le vasovagal, mais sa formulation couvrait aussi le câblage de l'idéation suicidaire (item 9 du PHQ-9, escalade de niveau 3). Arbitrage de Xavier : **retirer les numéros d'appel, garder le 3114 sur son déclencheur.** ✅ **Trois effets de bord favorables** : la **question 12 du brief Isorni devient caduque** — deuxième créneau de consultation économisé après la question 11 · le protocole de crise devient **identique en France et en Tunisie**, il n'y a plus de liste de numéros à préparer avant le 07/09 · l'écran de crise de Kokoro se simplifie. ⚠️ **Ce qui n'a pas bougé, et qui devait ne pas bouger : l'escalade reste non contournable, et la question de sécurité reste posée en premier.** Ce qui a été retiré, ce sont des numéros — pas une conduite à tenir. |
| **10/08/2026** | 🏗️ **Étape 5 ouverte — le plan de construction des applications est écrit** (`psy/android/PLAN-KOKORO.md`, jalons K0 → K6). ⭐ **Décision structurante : le premier livrable de Kokoro n'est pas le visage, c'est le noyau de crise.** Le mot-code « shutdown » est convenu avec Chourouk depuis le 09/08 et **n'a aucun porteur** : parole coupée, il faut aujourd'hui déverrouiller, ouvrir une messagerie, trouver un contact et écrire — quatre gestes, dont un impossible, et la période 07/09-28/09 est déclarée à haut risque de shutdown. **Deux autres questions ouvertes du plan sont refermées** : **app unique multi-modules** (§3.1 et §5 la laissaient ouverte, §1.2.1 l'avait déjà tranchée de fait — trois apps sont trois icônes à retrouver au pire moment) et **aucune base de données** — l'app écrit des fichiers JSON, R1/R2/R3 du schéma l'imposent, une base dupliquerait la source de vérité. ✅ **Arbitrage de Xavier : le full-screen intent devient le jalon K1**, juste après l'installation du poste de travail — c'est le point techniquement le plus risqué du projet, donc celui qu'il faut lever avant de construire dessus. ⚠️ **Deux faits vérifiés sur la machine, et le second corrige le §5 de ce document** : **aucun outillage Android n'est installé** (ni JDK, ni SDK, ni `adb` — K0 est l'installation, pas une formalité) ; et le **full-screen intent n'est plus accordé par défaut depuis Android 14**, alors que le §5 le donnait pour acquis (« ✅ »). ✅ **Chourouk valide le canal SMS du mot-code** — le seul point du plan applicatif qui dépendait d'un tiers. |
| **09/08/2026** | 🪞 **`psy-superviseur` écrit — et il trouve une faute dans le dispositif en première passe.** Le §6 déclarait depuis le début qu'« un psy virtuel toujours d'accord serait nocif » et posait le rôle comme garde-fou ; il est resté « non planifié » pendant que le dispositif produisait dix-neuf documents doctrinaux. ⭐ **Le risque qu'il traite est structurel et vaut d'être nommé une fois pour toutes : presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** Le rapport, le PLAN, les fiches, les protocoles, les sept skills — tous générés par Claude, tous se citant les uns les autres comme s'ils faisaient autorité. Les seules sources primaires sont l'évaluation Saley, le certificat Isorni, les questionnaires bruts, les trois courriers Roisman, la biopsie et le DSM-5. **Un dispositif qui perd cette distinction confond sa propre cohérence avec la vérité.** ✅ **Décision de conception : la supervision n'écrit pas dans `psy/dossier/`** mais dans `psy/agent/supervisions/` — elle porte sur le dispositif, pas sur le patient ; corollaire utile, aucune modification du schéma normatif n'est requise pour ouvrir le rôle. 🔴 **Première passe, quatre constats, dont un bloquant et il est ironique : `psy-seance` instruisait « on ne passe pas au palier suivant tant que le précédent n'est pas confortable ».** C'est **mot pour mot** la faute R6 que le dispositif se félicitait d'avoir corrigée dans le rapport §10.8 le matin même. Elle a survécu à l'écriture des trois protocoles (qui l'ont corrigée chez eux), à l'audit de cohérence, et à l'écriture de `psy-hygiene` (qui l'interdit explicitement) — **dans le skill qui conduit toute séance de fond, donc celui qui décide effectivement des passages de palier.** Corrigé après constat, jamais en silence. ⚠️ **Trois autres constats** : la **fiche explicative pour Chourouk** est déclarée dans quatre documents et n'existe nulle part (même forme que le défaut du protocole de crise) · l'**app de tension appliquée** est en Étape 3 sans surface alors que §1.2.1 impose Android « sans discussion possible » · ⭐ **prolifération — 19 documents doctrinaux contre 1 acte exécuté** (le VVIQ) : `journal/` 0, `seances/` 0, `briefs/` 0, aucun palier entamé. **Ce n'est pas une critique de rythme, c'est un risque daté** : le brief du 29-30/08 tirera ses chiffres d'un répertoire vide, et la consultation du 03/09 est la dernière avant fin septembre. 📌 **Objection de fond versée : l'aphantasie a été tenue pour acquise pendant deux jours avant d'être mesurée** — le VVIQ l'a confirmée, mais un superviseur n'a pas le droit de noter le processus sur le résultat. **Deux caractéristiques sont aujourd'hui dans ce même statut** : l'alexithymie (TAS-20 le 16/08) et le **déficit intéroceptif — brique de la règle §9.19, la plus citée du dossier, et la seule sans instrument** (le MAIA est nommé deux fois, versé zéro). |
| **09/08/2026** | ⚙️ **Les quatre rôles manquants sont écrits — le dispositif cesse d'être un classeur de fiches.** `psy-crise`, `psy-bilan`, `psy-brief-isorni`, `psy-hygiene` rejoignent `psy-seance` et `psy-journal` dans `.claude/skills/`. **Constat de départ, et il vaut d'être nommé : les trois étapes ouvertes avaient produit des protocoles, des instruments et des gabarits — et aucun exécutant.** Un protocole de crise sans skill de crise est un document que personne n'ouvre au moment où il sert ; des échelles versées sans skill de passation sont un corpus qu'on ne fait pas passer ; un gabarit de brief sans skill de brief est un tableau vide à quinze jours d'une consultation qui ne se représentera pas avant fin septembre. ⭐ **Quatre décisions de conception prises à l'écriture, aucune n'était dans les fiches.** (1) 🔴 **`psy-crise` porte la seule exception au premier invariant du dispositif** — « charger `profil.md` + `etat.md` avant d'agir » vaut partout **sauf en crise**, où les numéros s'affichent d'abord. Lire deux fiches prend du temps, et le temps est exactement ce qui manque ; un contexte chargé n'a jamais aidé personne pendant les trente premières secondes. L'exception est **écrite**, pas déduite. Le skill ajoute par ailleurs un **mode sans parole opérationnel** que la fiche décrivait sans l'outiller : bascule sur des choix numérotés — **un chiffre est produisible en shutdown, une phrase non.** (2) ⭐ **`psy-hygiene` compte le critère de passage au lieu de le demander.** « Tu te sens prêt à monter d'un palier ? » est une question intéroceptive posée à quelqu'un dont l'intéroception est déficitaire — cinquième instance de la règle §9.19, et elle serait passée inaperçue puisque la fiche PPC avait déjà converti ses critères en comptages : **rien n'empêchait le skill de reposer la question en clair au moment de trancher.** Le critère se vérifie dans les `journal/*.json`, et **si la donnée manque, on ne passe pas** — on ne comble pas par un souvenir. (3) **`psy-bilan` interdit de restituer un instrument de mémoire** — la règle qui avait bloqué le BES le matin même devient une règle de conduite générale, pas une exception ponctuelle : un item mal restitué produit un score faux, donc **faussement rassurant**, le pire résultat possible ici. (4) **`psy-brief-isorni` interdit de compter un jour sans check-in comme un zéro.** Le schéma disait déjà « un jour sans check-in est un jour sans fichier, ce n'est pas une donnée négative » ; **au moment de calculer une médiane, c'est précisément l'erreur qu'on commet sans y penser** — d'où l'obligation d'écrire le nombre de jours renseignés à côté de chaque chiffre. S'y ajoute la frontière de non-substitution rendue opérationnelle : le brief **pose** les questions, il n'y répond jamais, **et « ne faudrait-il pas envisager… ? » est une proposition déguisée.** ✅ **Reste `psy-superviseur`** — le garde-fou anti-effet-miroir, et **le seul rôle dont l'absence ne se voit pas, par construction.** |
| **09/08/2026** | 🔧 **Audit de cohérence du dispositif — aucun fait clinique modifié, dix-sept incohérences corrigées.** ⭐ **La plus grave, et elle était invisible : le protocole de crise avait deux domiciles, et toutes les surfaces pointaient vers le mauvais.** L'Étape 3 avait écrit `protocoles/crise-escalade.md` — triage, escalade, et surtout la découverte que **tous les numéros d'urgence français exigent de parler**, avec le **114 par SMS** en parade. Mais `psy-seance`, `psy-journal`, le corpus des échelles, le protocole PPC et la carte `psy/README.md` renvoyaient tous encore aux huit lignes de `profil.md` §4, **qui n'offrent que le 3114 et le 15 — deux numéros vocaux.** Autrement dit : le dispositif avait identifié que son protocole d'urgence était inutilisable en shutdown, avait écrit la parade, **et continuait à servir l'ancienne version à toutes ses surfaces.** Un défaut de câblage, pas de conception — mais il portait exactement sur le cas que la fiche avait été écrite pour couvrir. `profil.md` §4 est désormais déclaré **résumé**, la fiche fait foi, et les deux portent la mention des voies sans parole. ⚠️ **Deuxième défaut de même nature :** `psy-seance` instruisait de trancher tout doute clinique sur le rapport **v2.3** — la version que la v2.4 corrige précisément sur l'observance de la PPC. Une séance conduite ce jour-là aurait pu déclarer la PPC « non utilisée » devant Xavier, ce qui est faux et ce qui aurait sapé le chantier n° 1. **Corrigé.** ✅ Reste : trois fichiers annonçaient « Étape 1 ouverte » quand trois étapes le sont ; `SCHEMA.md` passe en **v1.1** (son exemple VVIQ portait `"VVIQ-2"` avec un `score_max` de 80, alors que le VVIQ-2 compte 32 items et plafonne à 160 — écart signalé à l'Étape 2, annoncé, **désormais appliqué** conformément au §9) ; deux renvois pointaient vers `Biopsie hépatique - Dr Bouarioua.md`, fichier fusionné le 08/08 et donc inexistant ; l'avertissement d'en-tête déclarait §4.4 et §4.5 périmées **en oubliant §4.1**, qui décrit encore une stéatose simple là où l'histologie dit NASH ; §1.3, §5 et §6 portaient des statuts « à concevoir » ou « en discussion » sur des points tranchés, §5 listant même comme « questions ouvertes » le nom et le graphisme **décidés vingt lignes plus haut**. 📌 **Choix de méthode assumé :** les entrées de ce journal et les tables de version des fiches **n'ont pas été réécrites**, y compris quand elles citent des faits corrigés depuis — R2 du schéma du dossier, l'historique reste lisible, **y compris ce qui s'est révélé faux**. Seuls les **pointeurs vivants** ont été corrigés. |
| **09/08/2026** | 🔴 **Étape 3 ouverte — les deux protocoles de crise écrivables sont écrits.** (1) **`crise-escalade.md`** : le protocole de crise était déclaré « câblé en dur, non contournable » dans six documents et **n'existait nulle part comme fiche** — il vivait en huit lignes de `profil.md` §4. Il a désormais un **triage en trois questions fermées**, dont la première décision de conception est que **la question de sécurité passe avant la question du mécanisme** : typer un épisode pendant qu'une idéation court, c'est faire de la nosologie au lieu de porter secours. ⭐ **Découverte non prévue au plan, et c'est la plus importante de l'étape : tous les numéros d'urgence français sont des numéros de téléphone.** Le 3114 et le 15 demandent de **parler** — or le shutdown coupe précisément le canal verbal. **Le moment où Xavier a le plus besoin d'aide est celui où le dispositif d'aide standard lui est structurellement inaccessible.** Trois voies sans parole sont versées, dont le **114 (urgences par SMS)**, avec sa réserve honnête : il est officiellement destiné aux personnes sourdes ou aphasiques, l'usage en shutdown est défendable mais doit être **vérifié auprès du Dr Isorni** plutôt que découvert en situation. **Conséquence câblée pour Kokoro (Étape 5) : l'écran de crise doit offrir le SMS pré-rempli au même rang que l'appel** — un écran qui n'offre que des appels est inutilisable exactement quand il sert. ✈️ Ajout : le 3114, le 15 et le 114 **ne fonctionnent pas depuis la Tunisie** — numéros de substitution à préparer avant le 07/09. (2) **`tension-appliquee.md`** + corpus priorité n° 1 versé (Öst & Sterner 1987, Öst *et al.* 1991). ⭐ **Quatrième instance de la règle §9.19, et elle était invisible :** le protocole d'Öst prescrit de déclencher la tension « dès les premiers signes » — pâleur, sueur froide, nausée — c'est-à-dire **sur la détection d'une chute de tension artérielle, un signal interne**, chez quelqu'un dont le déficit intéroceptif est confirmé. Appliqué tel quel, il aurait échoué, et l'échec aurait été lu comme un manque d'application. **Remplacé par un déclenchement sur repères externes et au chronomètre** — franchir la porte, s'asseoir, voir le plateau —, avec la règle explicite : *on ne se demande jamais « est-ce que j'en ai besoin là ? »*, puisque répondre exigerait la perception qui manque. ✅ **Séquençage préservé** : l'acquisition à froid est 3 min/jour sans exposition ni changement d'habitude — elle **ne consomme pas** la règle « un seul chantier à la fois », la PPC reste le chantier n° 1, et le palier « vrai geste médical » est explicitement hors fiche. ⚠️ **Point de sécurité versé au brief** : la contraction élève transitoirement la tension artérielle, et une vigilance tensionnelle est déjà notée sous venlafaxine à IMC 35. |
| **09/08/2026** | 🔴 **Étape 2 ouverte — les instruments de mesure sont versés** (`psy/corpus/echelles/`). Constat de départ : `psy/dossier/journal/` était **vide** et `psy/corpus/` ne contenait qu'un README — **rien de l'instrumentation n'existait**, alors que l'item « passer les échelles » supposait des instruments qui n'avaient jamais été récupérés. Versés complets : **VVIQ**, **TAS-20**, **CAT-Q**, **GAD-7/PHQ-9** — items, cotation, seuils, et pour chacun la rubrique obligatoire « ce qu'elle ne dit pas ». ⭐ **Décision de conception : R6 ne s'applique pas aux échelles validées, et il faut le dire explicitement.** Le journal quotidien reste strictement comportemental ; une échelle est un autre objet — une passation datée, avec un seuil publié, dont la validation psychométrique remplace l'ancre comportementale. **Corollaire non négociable, écrit dans chaque fiche : chez Xavier, un score élevé est informatif, un score bas ne clôt aucune question** — l'alexithymie et le déficit intéroceptif sont précisément une difficulté à répondre à ce type de question. ⚠️ **Trois points durs rencontrés et tranchés.** (1) **Le BES n'a pas pu être obtenu** : ses 16 items pondérés ne sont pas librement diffusés et une restitution approximative aurait produit un score faux — donc faussement rassurant, le pire résultat possible ici. Refus d'inventer ; à la place, une **grille comportementale de 5 questions** utilisable immédiatement, dont la question décisive — *« combien de fois t'es-tu arrêté de manger alors qu'il restait de la nourriture disponible ? »* — est **plus informative qu'un score BES** : un zéro y démontre que ce qui arrête le repas est l'épuisement du stock et jamais un signal interne. (2) 🔴 **L'item 9 du PHQ-9 interroge l'idéation suicidaire** : conduite câblée — il se pose **en dernier**, toute réponse ≥ 1 **interrompt la passation** et déclenche le 3114, le fichier `mesures/` s'écrit après. (3) ⭐ **Le PHQ-9 n'est pas interprétable comme une mesure de l'humeur chez Xavier aujourd'hui** : quatre de ses neuf items (sommeil, fatigue, concentration, ralentissement) sont **directement produits par un SAOS sévère insuffisamment traité** et peuvent à eux seuls porter le score en zone « modérée » sans dépression. **La réserve doit figurer au brief** — sans quoi le chiffre induira en erreur le seul praticien qui ignore encore le diagnostic. 📌 **Écart signalé, non corrigé :** l'exemple du `SCHEMA.md` §6 porte `"VVIQ-2"` avec un `score_max` de 80, alors que le VVIQ-2 compte 32 items et plafonne à 160 ; la version retenue est le **VVIQ 16 items /80**. Le schéma étant normatif, sa correction s'annonce avant de s'appliquer. |
| **09/08/2026** | ✅ **Trois décisions de Xavier, qui débloquent le kit vol.** (1) ⭐ **Le mot-code shutdown est convenu avec Chourouk : « shutdown ».** C'est la première brique d'Étape 3 réellement en place, et la plus rentable du dossier — elle n'a coûté qu'une conversation et elle est utilisable en aéroport, en conflit, partout. Restent le bouton Android (Étape 5) et la fiche explicative pour Chourouk. (2) **La PPC part en Tunisie** : le séjour cesse d'être trois semaines perdues pour le chantier n° 1 — le port continue au niveau atteint, **sans progression de palier**, un environnement inconnu n'étant pas un endroit où monter d'un palier d'exposition. (3) **Alprazolam prévu pour le vol** — molécule **déjà prescrite « si besoin »**, donc son emploi n'est pas une modification de traitement ; mais le **point de vigilance benzodiazépine / SAOS sévère n'a jamais été instruit**, et il porte sur ce médicament précisément. Question 10 du brief, arbitrage au Dr Isorni le 03/09 — **avec un élément nouveau à lui donner : la PPC sera utilisée pendant le séjour**. Le dispositif ne se prononce pas. |
| **09/08/2026** | ⏱️ **Deux dates entrent au dossier et fixent l'échéancier** : **consultation Dr Isorni le jeudi 03/09/2026 à 12h30**, **départ en vacances en Tunisie le 07/09/2026**. **Quatre conséquences.** (1) ⭐ **Le check-in quotidien devient urgent** : 25 jours de données observées avant la consultation, ou un brief de souvenirs — c'est tout l'écart entre le dispositif et ce qui existait avant. (2) **Le brief s'écrit à la séance du 29-30/08**, et **l'email part avant la consultation** : un créneau ne suffit pas à découvrir un SAOS sévère, une NASH et six questions simultanément. (3) **Le palier 0 de la PPC doit être bouclé avant le départ** — ce sont des appels, ils ne se passent pas depuis la Tunisie ; s'y ajoute la question du transport de l'appareil (cabine, tension, humidificateur). (4) ⭐ **La pause des vacances est décidée maintenant, pas subie sur place** : aucun palier ne progresse pendant le séjour, et **on redescend d'un palier à la reprise, sur les trois chantiers** — règle écrite à l'avance, parce que décider avant est précisément ce qui empêche de le vivre comme un échec. ⚠️ **Deux points de vigilance** : la consultation tombe **4 jours avant le départ**, donc c'est la fenêtre pour sécuriser l'ordonnance de venlafaxine du séjour (logistique, jamais posologie) ; et **le voyage est une exposition agoraphobique majeure** — avion, aéroport, foule, lieu clos. ✅ **Arbitrage rendu : kit vol minimal**, deux briques d'Étape 4 avancées (`panique-13-symptomes.md`, `jour-de-vol.md`) — **et pas de programme d'exposition**, qui entrerait en concurrence directe avec le chantier PPC en violant la règle « un changement à la fois ». ⚠️ **Durée du séjour : 3 semaines ou plus** — ce n'est pas une parenthèse, c'est un tiers du trimestre : la consultation du 03/09 est **la dernière avant fin septembre**, la question du transport de la PPC cesse d'être secondaire (3 semaines sans appareil = 3 semaines de boucle sommeil→poids→foie), et le séjour est **la première période sans mission professionnelle depuis longtemps** — la seule variable d'ajustement du dossier tombe à zéro : **observer si les shutdowns baissent vaudra plus que n'importe quelle échelle.** |
| **09/08/2026** | 🔴 **Consultation Roisman du 04/05/2026 versée → rapport v2.4.** Document arrivé après l'écriture des protocoles, et qui **corrige trois faits** posés en v2.3 : (1) la PPC n'est pas inutilisée, elle est **utilisée de façon très irrégulière** ; (2) le Dr Roisman **sait** — il a revu Xavier, documenté les causes (**fuites au masque, toux sèche**), installé l'humidificateur, resserré la pression à 6-12, activé l'**EPR 2**, renouvelé la prise en charge, et « **remotivé le patient** » ; (3) ⭐ **IAH résiduel < 6/h sous appareil** — l'efficacité est démontrée sur ses propres nuits, la question de l'utilité est close. **Trois conséquences pour le dispositif :** le SAOS est requalifié d'« non traité » en « **insuffisamment traité** » partout ; le **palier 0 du protocole PPC est entièrement réécrit** (l'inconnue n'est plus la machine mais **l'origine de la fuite** — masque ou bouche —, qui commande le choix d'interface, et dont une des parades, le masque facial, **augmenterait** le contact facial) ; une **règle d'entrée dans l'échelle** est ajoutée, puisqu'il a déjà porté le masque la nuit. ⚠️ **Le trou de coordination n'est pas où on le croyait** : il n'y a rien à révéler au pneumologue — **les deux courriers sont partis au seul Dr Fournier, et c'est le psychiatre qui ignore tout**. ⭐ **Confirmation clinique de la thèse du §9.23, en conditions réelles** : devant l'intolérance, la réponse standard a été de remotiver ; trois mois plus tard l'usage reste irrégulier. On ne remotive pas quelqu'un dont les renforçateurs fonctionnent — on lui donne une procédure. |
| **09/08/2026** | 🔴 **Étape 1 ouverte — les trois protocoles de l'Axe D sont écrits** : `ppc-desensibilisation.md`, `alimentation-structure-externe.md`, `activite-physique-sans-impact.md`. ⚠️ **L'Étape 1 du plan a dû être révisée avant d'être exécutée** : sa rédaction datait de la v2.0 du rapport et demandait encore un *dépistage* du SAOS (diagnostiqué depuis) avec une cible de −5,5 kg (portée à **−7,7/−11 kg** en v2.2). **La PPC devient la cible n° 1 de l'étape.** ⭐ **Premier cas où le socle corrige le rapport** : le critère de passage des paliers PPC du §10.8 (« confortable plusieurs jours de suite ») viole la règle R6 — « confortable » est un ressenti. Converti en comptage comportemental, plus un **palier intermédiaire ajouté** (allongé éveillé) entre « assis » et « sieste », le saut étant trop grand à 5/5 de peur des lieux clos. ✅ **Règle de séquençage posée** : les paliers 0 (logistique, sans changement d'habitude) peuvent courir en parallèle ; à partir du palier 1, **un seul chantier progresse à la fois**. ✅ **Deux points de méthode assumés** : la BES interroge des ressentis (tension avec R6) — on la passe quand même, mais un score bas ne clôt pas la question à lui seul ; l'intensité d'effort est repérée par le **test de la phrase** (comportemental) et non par une échelle d'effort perçu. |
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
