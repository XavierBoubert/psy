# État courant

**Statut :** document **vivant** — réécrit à chaque clôture de séance. Contrepartie variable de `profil.md` (permanent).
**Dernière mise à jour :** 09/08/2026 · **Prochaine révision :** à la première séance de fond.

> **Comment lire ce document.** `profil.md` dit *qui est Xavier*. Celui-ci dit *où on en est*. Les deux se chargent ensemble, jamais l'un sans l'autre.

---

## 1. Chantier ouvert

| | |
|---|---|
| **Étape du PLAN** | Étape 0 — socle minimal *(en cours d'achèvement)* → Étape 1 — Axe D 🔴 |
| **Cible thérapeutique n° 1** | 🔴 **Reprise de la PPC par désensibilisation** (exposition graduée, §10.8) — SAOS sévère non traité depuis ~7 mois |
| **Cible n° 2** | Alimentation à structure externe — cible 7-10 % → **99-102,3 kg** |
| **Cible n° 3** | Activité physique — domicile, **sans impact**, format court et invariable |
| **Palier en cours** | Aucun — les paliers PPC et alimentaires restent à écrire |

**Pourquoi la PPC passe devant.** Elle est le seul levier qui agit simultanément sur quatre choses : la fatigue et l'attention, le poids (la privation de sommeil dérègle ghréline et leptine — donc **aggrave un déficit de satiété déjà présent**), le foie (l'hypoxie intermittente aggrave la NASH indépendamment de l'IMC), et l'humeur. Aucune autre cible n'a ce rendement. Et tant que 61 micro-éveils par heure fragmentent chaque nuit, **aucune évaluation attentionnelle ni du critère C du TAG n'est interprétable**.

---

## 2. Traitement en cours

| | |
|---|---|
| **Venlafaxine (IRSN)** | **Reprise le 07/08/2026**, après ~1 an de paroxétine marqué par une régression. En **titration** — délai de 2 à 4 semaines par palier, ne pas conclure trop tôt. **Jamais d'arrêt brutal.** |
| **Alprazolam** | « Si besoin ». ⚠️ Point de vigilance porté au Dr Isorni : benzodiazépine sur SAOS sévère non traité. |
| **PPC ResMed AirSense 11 auto** (4-16 cm H₂O) | Prescrite le 19/01/2026, prestataire Link Sommeil. **Non utilisée.** |

⚠️ **Rappel non contournable :** le dispositif **ne conseille jamais** de modifier un traitement. Toute question pharmacologique part au brief Dr Isorni.

**À surveiller pendant la titration :** tension artérielle aux paliers hauts (obésité de classe II = facteur de risque d'HTA autonome) · apparition ou aggravation d'impatiences dans les jambes (la venlafaxine figure parmi les molécules associées aux mouvements périodiques — effet peu fréquent, mais 31 MPJ/h sont déjà objectivés).

---

## 3. Chiffres de référence

| Mesure | Valeur | Date | Prochaine |
|---|---|---|---|
| Poids | **≈ 110 kg** (IMC 35,1) | 08/08/2026 | Hebdomadaire |
| Poids antérieur | 104 kg (IMC 33) | 29/10/2025 | — *(+6 kg en 9 mois)* |
| Cible hépatique | **99-102,3 kg** (−7,7 à −11 kg) | — | Palier suivant à définir **seulement** une fois celui-ci atteint |
| IAH | 35/h (48/h avec MELER) | 29/10/2025 | Après reprise effective de la PPC |
| Épworth / ISI | 14 / 20 | 19/01/2026 | Après traitement du SAOS |
| Observance PPC | **0 min/nuit** | 09/08/2026 | Quotidien (télésuivi) |
| Missions actives | 3 | 08/08/2026 | Quotidien |

---

## 4. Campagne en cours — champs `campagne` du journal quotidien

Actifs à ce jour (cf. `SCHEMA.md` §3.2). Ils sortiront du journal à la clôture du chantier.

| Champ | Type | Se retire quand |
|---|---|---|
| `ppc_minutes` | entier ≥ 0 | Observance stabilisée et palier 5 tenu |
| `repas_servis_une_fois` | 0-4 | Structure alimentaire devenue routine stable |
| `activite_minutes` | entier ≥ 0 | Idem |
| `poids_kg` | nombre \| null *(hebdomadaire)* | Cible atteinte |

---

## 5. Questions ouvertes — Dr Isorni

Le brouillon d'email existe déjà : `ressources/xavier/20260808 Email au Dr Isorni.md`. **Non envoyé au 09/08/2026.**

| # | Question | Priorité |
|---|---|---|
| 1 | **Alprazolam et SAOS sévère** — limiter, remplacer, ou sans conséquence à la dose employée ? | 🔴 porte sur un traitement en cours |
| 2 | **Venlafaxine et mouvements périodiques des jambes** — 31/h objectivés, indépendants des apnées ; **doser la ferritine** (jamais faite) | 🔴 |
| 3 | **Bilan hépatique de référence** — la GGT était à 2× la normale sous venlafaxine *sans bilan préalable*. La molécule vient d'être reprise : c'est le moment d'établir la valeur de départ | 🔴 |
| 4 | Transmettre le diagnostic de SAOS et **prévenir le Dr Roisman de la non-observance** — un patient qui n'utilise pas sa machine sans le dire sort du circuit de suivi | 🔴 |
| 5 | **Paroxétine et prise de poids** (+6 kg en 9 mois) — a-t-elle contribué à la prise de poids qui a causé la NASH ? | Haute |
| 6 | Confirmation formelle de la **phobie sang-injection-accident** et du **TAG** (critère C) | Moyenne — à réévaluer **après** traitement du SAOS |
| 7 | **Surveillance hépatologique** à clarifier avec la Dr Bouarioua : contrôle non invasif à distance (biologie ± élastométrie) ? | Moyenne |
| 8 | Bilan métabolique complet (HbA1c, lipides, tension) | Moyenne |

**Ordre imposé :** DIVA-5 **après** traitement effectif du SAOS, jamais avant. Un psychostimulant prescrit sur un SAOS non traité masque le trouble au lieu de le corriger.

---

## 6. Échelles à passer — aucune administrée à ce jour

| Échelle | Objet | Durée | Priorité |
|---|---|---|---|
| **VVIQ** | Objectiver l'aphantasie — **conditionne quelles techniques sont utilisables** | 5 min | Haute |
| **BES** | Éliminer formellement une hyperphagie boulimique — deux traitements différents en jeu | 10 min | Haute *(chantier alimentaire)* |
| **TAS-20** | Alexithymie — « le chaînon manquant du dossier » | 10 min | Haute |
| **CAT-Q** | Intensité du camouflage | 15 min | Haute |
| GAD-7 / PHQ-9 | Anxiété, dépression — routine mensuelle | 5 min | À mettre en routine |
| DIVA-5 | TDAH adulte | 60 min | ⏸️ **bloqué** — après traitement du SAOS |

---

## 7. Ce qui vient de changer

| Date | Fait |
|---|---|
| 07/08/2026 | Retour à la **venlafaxine**. Titration en cours — toute dégradation ou amélioration des 4 prochaines semaines est d'interprétation ambiguë. |
| 08/08/2026 | Rapport porté en **v2.3** : le SAOS sévère passe d'hypothèse à **diagnostic constitué et non traité**. |
| 08/08/2026 | **PLAN.md v1.0** — brainstorming clos en 7 tours. |
| 09/08/2026 | **Étape 0 en place** : dossier, schéma, fiche de profil, skills `psy-seance` et `psy-journal`. |

---

## 8. Charges de vie actives — le contexte qu'aucune mesure ne capte

Nourrisson né en 2026 (nuits fragmentées, charge sensorielle maximale **au domicile, son refuge historique**) · 3 missions professionnelles simultanées · **deuil actif du lien avec sa fille aînée** (rompu depuis septembre 2023, deux médiations sans succès) · parcours somatique en cours · titration médicamenteuse.

**Risque nommé : burnout autistique.** Indicateur de suivi n° 1 : **la fréquence des shutdowns**. Seule variable d'ajustement disponible : **la charge professionnelle** — pas la famille, pas le sommeil.

---

## 9. Suspendu, pas clos

| Sujet | Statut |
|---|---|
| **EMDR — retraitement** | ⏸️ Suspendu (arbitrage du 08/08/2026 : on commence par la TCC). Seul l'**instrument** de stimulation bilatérale sera construit. Réouverture sous les critères chiffrés du PLAN §3.1, après avis du Dr Isorni. |
| **Psychologue en présentiel** | Écarté — **dette assumée**, à rediscuter une fois le dispositif en place. |
| **Trauma d'enfance / TSPT** | Jamais évalué. Exploration légitime, pas maintenant. |

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 09/08/2026 | Création — Étape 0 du PLAN. |
