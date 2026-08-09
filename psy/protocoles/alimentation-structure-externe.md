# Alimentation — structure externe

**Statut :** fiche actionnable — v1.0 (09/08/2026) · **Étape 1 du PLAN**
**Cible dossier :** `alimentation-structure` · **Champs journal :** `campagne.repas_servis_une_fois`, `campagne.poids_kg`
**Source clinique :** rapport v2.4 §6.5 (conduite alimentaire et déficit intéroceptif), **§10.7** (versant somatique), §9.19 (règle centrale).

> **Ce que cette fiche est.** Un **dispositif de décision**, pas un régime. Elle dit *quand* et *combien* se décident, et *qui décide* — pas quoi manger.
> **Ce qu'elle n'est pas.** Une prescription diététique. Le contenu nutritionnel relève de la Dr Bouarioua et d'un diététicien (point à porter au brief, §7).

---

## 0. Le raisonnement en une phrase

Xavier ne perçoit pas la satiété. **Toute consigne qui demande de s'appuyer sur ce signal est structurellement inapplicable** — « écoute ton corps », « arrête-toi quand tu n'as plus faim », « mange en pleine conscience » ne sont pas difficiles chez lui : elles sont **impossibles**, au même titre que « imagine un lieu sûr ».

> **La régulation doit donc être déplacée hors du corps :** la quantité est décidée **avant** le repas, par quelqu'un qui n'a pas faim, et le repas ne consiste plus qu'à exécuter une décision déjà prise.

**Le SAOS aggrave exactement cette fonction-là.** La privation de sommeil dérègle ghréline et leptine — appétit majoré, satiété diminuée. Les deux mécanismes convergent sur le même signal. Fait mesuré : **104 kg (10/2025) → 110 kg (08/2026)**, +6 kg pendant la période de SAOS insuffisamment traité. Ce chantier et celui de la PPC se renforcent mutuellement.

**Corollaire à énoncer clairement quand le sujet vient :** un échec antérieur de perte de poids ne documente aucun manque de volonté. Il documente une consigne inadaptée au profil.

---

## 1. Quand l'utiliser

- Séances de fond dont la cible est `alimentation-structure`.
- Chaque fois qu'une question alimentaire est posée hors séance.

## 2. Quand ne **PAS** l'utiliser

| Situation | Conduite |
|---|---|
| **BES non passée** *(cf. §6)* | Le protocole peut démarrer, **mais** la question de la perte de contrôle reste ouverte : si elle est établie, le traitement indiqué est différent. Ne jamais conclure « ce n'est qu'une question de structure » avant d'avoir tranché. |
| Xavier **saute des repas** ou réduit spontanément en dessous du cadre | 🔴 **Point d'alerte, pas un progrès.** Cf. §5. |
| Journée à shutdowns, surcharge aiguë | On applique la structure **déjà en place**, on n'en ajoute aucune. Un chantier alimentaire ne s'ouvre pas dans une semaine de surcharge. |
| Question de médicament et de poids | **Non-substitution.** → brief Dr Isorni (question paroxétine / +6 kg déjà inscrite). |

---

## 3. Le dispositif — quatre règles, et rien d'autre

| # | Règle | Ce qu'elle remplace |
|---|---|---|
| **R1** | **La quantité est décidée avant le repas**, hors du moment de manger. | Le jugement « à la sensation », qui n'a pas d'organe pour se faire. |
| **R2** | **Servie une fois. Pas de resservage.** Le plat de service ne reste pas sur la table. | L'arrêt spontané, qui ne se déclenche pas. |
| **R3** | **Horaires fixes.** Mêmes créneaux tous les jours, écrits. | La faim comme déclencheur — signal peu fiable ici. |
| **R4** | **Rotation stable.** Un petit ensemble de repas qui reviennent. Aucune injonction à « varier ». | La décision au cas par cas, qui est une charge quotidienne. |

> **R4 est un choix clinique, pas un pis-aller.** La rigidité et les routines sont documentées (§6.1 B2) : la prévisibilité est **une ressource** chez Xavier, pas une monotonie à corriger. Une consigne de variété alimentaire ajouterait une charge décisionnelle quotidienne sans bénéfice.

### La portion de référence

La quantité n'est pas comptée en calories — **aucun jugement calorique n'entre dans ce dispositif**. Elle est fixée par un **contenant unique et invariable** (une assiette donnée, un bol donné), rempli une fois, décidé à froid.

**Comment on la calibre, puisqu'aucun signal interne ne peut le faire :** par le résultat, pas par la sensation. La pesée hebdomadaire est le retour. **Si le poids n'a pas bougé après 4 semaines pleines de structure tenue, la portion de référence est réduite d'une unité visible** (un demi-bol, une louche) — décidé **en séance, à froid**, jamais à table, jamais un soir de faim.

---

## 4. Mise en place — paliers

On n'installe pas quatre règles sur quatre repas d'un coup. **Un palier = un nombre de repas conformes par jour**, tenu avant de monter.

| Palier | Contenu | Critère de passage *(observable)* |
|---|---|---|
| **0** | Écrire la liste des repas en rotation, les horaires, et choisir le contenant de référence. Rien ne change encore. | La liste existe, elle est au dossier |
| **1** | **Un** repas par jour conforme aux 4 règles — le plus facile à cadrer (souvent le déjeuner en télétravail) | `repas_servis_une_fois ≥ 1` sur **5 jours d'une même semaine** |
| **2** | **Deux** repas par jour conformes | `≥ 2` sur 5 jours d'une même semaine |
| **3** | **Trois** repas par jour conformes | `≥ 3` sur 5 jours d'une même semaine |
| **4** | Structure complète, incluant ce qui se mange **entre** les repas — même règle : décidé avant, sorti une fois, rangé | `= 4` sur 5 jours d'une même semaine, deux semaines de suite |

**Règles de conduite :**
1. **Un repas non conforme n'est pas une faute** — c'est un `repas_servis_une_fois` plus bas ce jour-là, rien de plus. Aucun commentaire, aucun rattrapage, aucune compensation le lendemain.
2. **Deux semaines sans atteindre le critère → on redescend d'un palier.** La redescente s'annonce avant.
3. **Le chantier alimentaire ne démarre pas la même semaine qu'un nouveau palier PPC.** Un changement à la fois (intolérance au changement, §6.1 B2).

---

## 5. Critères d'arrêt et points d'alerte

| Signal | Lecture | Conduite |
|---|---|---|
| **Repas sautés, restriction spontanée** | 🔴 Le dispositif a été détourné en régime. Chez quelqu'un qui ne perçoit ni faim ni satiété, une restriction ne se sent pas venir. | Retour au palier précédent, point en séance |
| **Perte de poids > 1 kg/semaine, soutenue** | Trop rapide | Point en séance, à signaler au brief |
| **Culpabilité, honte, préoccupation corporelle apparues** | Absentes du tableau initial (§6.5.a) — leur apparition change le tableau | Point en séance ; réévaluer la BES |
| **Épisodes de perte de contrôle identifiés** | Change le diagnostic et le traitement | → §6, brief Dr Isorni |
| Malaise, vertiges | Ne pas interpréter ici — venlafaxine en titration, tension à surveiller à IMC 35 | → Dr Isorni / Dr Fournier |

---

## 6. Dépistage de la perte de contrôle — à trancher formellement

**État de la question (rapport §6.5.a) :** l'hyperphagie boulimique (DSM-5 307.51 / F50.8) est **non retenue en l'état** — le tableau est celui d'un apport élevé **continu**, sans épisodes délimités ; ni détresse, ni culpabilité, ni préoccupation corporelle. Mais interrogé sur la perte de contrôle, Xavier répond ne pas savoir — **réponse cohérente avec le déficit intéroceptif lui-même**, donc non informative.

**Ce qui reste à faire : passer la BES** (Binge Eating Scale, Gormally 1982 — 16 items, ~10 min). Passation programmée à l'**Étape 2** avec les autres échelles ; instrument à verser dans `psy/corpus/echelles/`. Résultat à écrire dans `mesures/AAAA-MM-JJ-bes.json`.

> ⚠️ **Tension méthodologique à ne pas masquer.** La BES interroge en partie des ressentis (perte de contrôle, culpabilité) — c'est-à-dire ce que la règle R6 déconseille de coter chez quelqu'un d'alexithymique. **On la passe quand même**, parce qu'elle est l'instrument validé et que son résultat engage un diagnostic différentiel ; mais un score bas **ne clôt pas** la question à lui seul. Il se lit avec les quatre repères comportementaux ci-dessous.

**En attendant, quatre questions fermées et observables** — à poser telles quelles, jamais reformulées en « comment tu te sens quand tu manges » :

| # | Question | Ce qu'elle repère |
|---|---|---|
| 1 | « Y a-t-il des moments où tu manges **beaucoup en peu de temps** — disons plus d'un repas normal en moins de deux heures ? Oui / non / je ne sais pas » | Épisode délimité (critère A1) |
| 2 | « Est-ce qu'il t'arrive de continuer à manger **alors que le plat est fini** — tu vas en rechercher ? Oui / non » | Comportement de recherche, observable |
| 3 | « Est-ce que tu manges **en cachette**, ou différemment quand quelqu'un est là ? Oui / non » | Critère B4 — observable, pas introspectif |
| 4 | « Après, est-ce que tu **évites** quelque chose que tu aurais fait autrement ? Oui / non » | Retentissement comportemental plutôt que détresse ressentie |

Réponses au compte-rendu de séance, section `## Décisions`. **Un « je ne sais pas » est une réponse recevable et se note tel quel.**

---

## 7. Chiffres et traçage

| Mesure | Valeur | Règle |
|---|---|---|
| Poids de départ | **110 kg** (1,77 m · IMC 35,1) — 08/08/2026 | — |
| Cible du palier en cours | **99–102,3 kg** (−7,7 à −11 kg, soit 7–10 %) | Cible **NASH sans fibrose**, recommandations EASL-EASD-EASO 2024. Paliers suivants définis **seulement** une fois celui-ci atteint. |
| Pesée | **Hebdomadaire** — même jour, même heure, même conditions | `campagne.poids_kg` ; `null` les autres jours. **Jamais de pesée quotidienne** : le bruit de mesure devient une charge sans information. |
| Repas conformes | Quotidien | `campagne.repas_servis_une_fois` (0-4) |

> ⚠️ **Interdit dans ce chantier, sans exception :** compteur de calories · série de jours conformes · pourcentage d'objectif atteint · courbe de poids présentée comme une performance · commentaire sur un écart. Le Groden cote « Positif » à 1,50 : **il n'y a rien à motiver, il y a des charges à réduire** (§9.13). Un compteur est une charge.

**Pourquoi le chiffre est énoncé et pas caché :** une consigne sans chiffre (« perdre du poids +++++ ») laisse imaginer un objectif hors d'atteinte. Un palier d'environ 9 kg, écrit à l'avance, est franchissable — c'est la même logique que les paliers d'exposition (§9.22).

---

## 8. Ce qui manque encore — à porter au brief

| # | Point | Destinataire |
|---|---|---|
| 1 | **Avis diététique** — le contenu nutritionnel n'est du ressort ni de Xavier ni du dispositif. Une consultation compatible avec l'agoraphobie (téléconsultation) est à demander. | Dr Fournier / Dr Bouarioua |
| 2 | Bilan métabolique complet : **HbA1c, lipides, tension** | Dr Fournier |
| 3 | Bilan hépatique **de référence** à la reprise de la venlafaxine | Dr Isorni |
| 4 | **Paroxétine et prise de poids** (+6 kg en 9 mois) — a-t-elle contribué à la prise de poids qui a causé la NASH ? | Dr Isorni |
| 5 | Modalités de **surveillance hépatologique** non invasive à distance (biologie ± élastométrie) | Dr Bouarioua |
| 6 | **Historique pondéral** daté, à reconstituer | Xavier + dossiers médicaux |

---

## 9. Ce qu'on ne dit jamais dans ce chantier

| Ne pas dire | Dire à la place |
|---|---|
| « Écoute ta satiété », « arrête-toi quand tu n'as plus faim », « mange en pleine conscience » | « La quantité, tu l'as décidée avant ? » |
| « Tu as craqué », « c'est un écart » | Rien. Un repas non conforme se compte, il ne se commente pas. |
| « Il faut de la volonté » | « Trois fois dans ton dossier, c'était la consigne qui n'allait pas, pas toi. » |
| « Tu as repris 300 g cette semaine » | La pesée se lit en séance, sur la tendance, jamais semaine par semaine. |
| « Tu devrais manger plus varié » | Rien. La rotation stable est voulue. |

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 09/08/2026 | Création — Étape 1 du PLAN, d'après le rapport v2.3 §6.5, §10.7 et §9.19. |
