# `corpus/echelles/` — instruments de mesure

**Statut :** créé le 09/08/2026 — **ouverture de l'Étape 2 du PLAN** (Instrumentation du suivi, Axe B).
**Objet :** les instruments que le dispositif fait passer à Xavier. Un fichier par échelle : items, cotation, seuils, ce que l'échelle **ne dit pas**, et provenance.

> Une passation produit **un fichier dans `psy/dossier/mesures/AAAA-MM-JJ-<echelle>.json`**, conforme au `SCHEMA.md` §6. **Les réponses item par item sont conservées, toujours.** Un score seul n'est pas une mesure, c'est un résumé — c'est parce que les réponses brutes de l'AQ et de l'EQ existaient que le rapport a pu les re-coter et démontrer que le « manque d'empathie » de Xavier est un déficit de décodage, pas un désintérêt (rapport §5, §9.1).

---

## 1. ⚠️ Ces échelles interrogent des ressentis. Pourquoi ce n'est pas une violation de R6.

La règle **R6** du schéma du dossier interdit de coter des ressentis : *« on cote des comportements observables »*. Or la TAS-20 demande « je n'arrive pas à trouver les mots qui correspondent à mes sentiments », la VVIQ demande la vivacité d'une image mentale, le PHQ-9 demande de se sentir triste. Toutes, en apparence, enfreignent R6.

**La distinction est réelle et il faut la tenir explicitement :**

| | Journal quotidien | Échelle validée |
|---|---|---|
| Fréquence | tous les jours | une passation, datée |
| Fonction | **piloter** le suivi au jour le jour | **objectiver** une caractéristique, une fois, avec un seuil publié |
| Effet d'une erreur | fausse toute la série longitudinale | un score isolé, comparable à des normes |
| R6 | **s'applique sans exception** | ne s'applique pas — la validation psychométrique remplace l'ancre comportementale |

**Deux règles qui en découlent, non négociables :**

1. **Aucun score d'échelle n'entre dans le journal quotidien.** Le journal reste comportemental (`SCHEMA.md` §3.1). Les échelles vivent dans `mesures/`, et nulle part ailleurs.
2. ⭐ **Chez Xavier, un score bas sur une échelle introspective ne prouve rien.** L'alexithymie et le déficit intéroceptif sont précisément une **difficulté à répondre à ce type de question**. Un score qui minimise (peu d'anxiété déclarée, peu de perte de contrôle alimentaire déclarée) peut refléter le déficit de perception, pas l'absence du phénomène. **Un score élevé est informatif ; un score bas ne clôt aucune question à lui seul.** Cette réserve est écrite dans chaque fiche et doit être reportée dans le brief au Dr Isorni.

---

## 2. État des instruments

| Échelle | Identifiant `mesures/` | Durée | Items disponibles ici | Priorité |
|---|---|---|---|---|
| **VVIQ** | `vviq` | 5 min | ✅ [`vviq.md`](vviq.md) | 🔴 **Haute — conditionne quelles techniques sont utilisables** |
| **TAS-20** | `tas20` | 10 min | ✅ [`tas-20.md`](tas-20.md) | Haute |
| **CAT-Q** | `catq` | 15 min | ✅ [`cat-q.md`](cat-q.md) | Haute |
| **BES** | `bes` | 10 min | ⚠️ **partiel** — cotation et seuils oui, **items non reproductibles librement** → [`bes.md`](bes.md) | Haute *(chantier alimentaire)* |
| **GAD-7 / PHQ-9** | `gad7` / `phq9` | 5 min | ✅ [`gad-7-phq-9.md`](gad-7-phq-9.md) | Routine mensuelle — **baseline avant le 03/09** |
| **MAIA** | `maia` | 10 min | ⚠️ **partiel** — items non obtenus ; **grille comportementale de 5 questions** → [`maia.md`](maia.md) §5 | Haute — ⭐ **adosse la règle §9.19, la plus citée du dispositif et la seule sans mesure** |
| DIVA-5 | `diva5` | 60 min | ⏸️ non versé | 🔴 **bloqué** — après traitement effectif du SAOS, jamais avant |

---

## 3. Plan de passation — la contrainte de calendrier commande l'ordre

**Il reste 4 séances de fond avant la consultation du 03/09/2026** : 09/08, 16/08, 22-23/08, 29-30/08.

| Séance | Échelle | Pourquoi celle-ci, à ce moment |
|---|---|---|
| **09/08** | **VVIQ** (5 min) | ⭐ La seule échelle qui a un effet sur **la conception du dispositif lui-même** : si l'aphantasie est confirmée, toute la famille des techniques par imagerie mentale est définitivement écartée — EMDR en imagination, lieu sûr, exposition imaginée, relaxation guidée par images. C'est aussi la plus courte. |
| **16/08** | **TAS-20** (10 min) | « Le chaînon manquant du dossier » (§9.2). Détermine si le travail de nommage émotionnel est une cible à part entière. |
| **22-23/08** | **CAT-Q** (15 min) + **GAD-7 / PHQ-9** (5 min) | Le CAT-Q chiffre le moteur principal de l'anxiété. Le GAD-7/PHQ-9 fournit la **baseline chiffrée du brief** — passés à 2 semaines de la reprise de la venlafaxine, ils datent le point de départ de la titration. |
| **29-30/08** | **aucune** | Cette séance écrit le **brief Dr Isorni**. On n'ajoute pas de passation à une séance déjà pleine. |
| ⏸️ | **BES** | Dès que l'instrument est obtenu (cf. `bes.md`). En attendant, la **grille comportementale** de `bes.md` §4 est utilisable immédiatement et ne dépend d'aucune source externe. |

**Trois règles de passation :**
- **Une échelle n'est jamais la cible d'une séance.** Elle s'ajoute à la cible du jour et se note dans `mesures_passees` du compte-rendu.
- **Plafond : 20 minutes d'échelles par séance.** Au-delà, la fatigue de passation dégrade la réponse plus qu'elle n'informe.
- **« Stop » s'obéit immédiatement**, comme au check-in. Une passation interrompue s'écrit avec les réponses obtenues et le reste en `null` — elle n'est pas invalide, elle est partielle, et c'est écrit dans `notes`.

---

## 4. 🔴 Sécurité — un item de ce corpus déclenche le protocole de crise

**PHQ-9, item 9** interroge directement l'idéation suicidaire. Toute réponse ≥ 1 à cet item **interrompt la passation** et déclenche [`protocoles/crise-escalade.md`](../../protocoles/crise-escalade.md) §2 : **3114** (gratuit, 24h/24), contact d'urgence, Dr Isorni, 15 si urgence vitale. Le fichier `mesures/` est écrit **après**, jamais avant. Détail : [`gad-7-phq-9.md`](gad-7-phq-9.md) §3.

⭐ **Si la parole est coupée à ce moment-là**, les numéros vocaux sont inutilisables : voies sans parole au §4 de la fiche (**114** par SMS, mot-code « shutdown », canal écrit).

---

## 5. Convention de fiche

Chaque fiche porte, dans cet ordre : **Source exacte et date** · **Ce que l'échelle mesure** · **Passation** · **Items** · **Cotation et seuils** · **⚠️ Ce qu'elle ne dit pas** · **Ce qu'on écrit dans `mesures/`**.

La rubrique « ce qu'elle ne dit pas » est obligatoire (convention de `corpus/README.md`). Une échelle sans ses limites écrites est un chiffre sans son intervalle de confiance.

---

| Version | Date | Modification |
|---|---|---|
| 1.1 | 09/08/2026 | ✅ **VVIQ passé — 18/80, aphantasie objectivée** (`mesures/2026-08-09-vviq.json`), première passation du dossier. **MAIA ajouté** sur arbitrage de Xavier (supervision A4) : items non obtenus, **grille comportementale de substitution versée**. ⭐ **Deuxième instrument de suite dont les items manquent et qui est remplacé par du comportemental** — ce n'est plus un contretemps, c'est un motif : les instruments introspectifs sont à la fois les plus durs à obtenir et les moins adaptés à ce profil. |
| 1.0 | 09/08/2026 | Création — ouverture de l'Étape 2. VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9 versés ; BES partiel. |
