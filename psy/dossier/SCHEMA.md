# Schéma du dossier — mémoire longitudinale

**Statut :** normatif — v1.0 (09/08/2026)
**Portée :** ce document définit le format de `psy/dossier/`. Les trois surfaces (Claude Code, web desktop, Android) le lisent et l'écrivent. **Aucune surface n'a le droit d'inventer un format.**

---

## 0. Pourquoi ce document existe

Le dossier est la **source de vérité unique** du dispositif (PLAN §1.2). C'est lui — pas les conversations — qui rend le suivi longitudinal possible. Si son format dérive, la mémoire longitudinale se dégrade en silence : les tendances deviennent incalculables, les comparaisons faussées, et l'avantage n° 2 du dispositif (« mémoire longitudinale parfaite », PLAN §1.1) disparaît.

---

## 1. Les six règles invariables

| # | Règle | Raison |
|---|---|---|
| **R1** | **Un fichier par événement.** Jamais de fichier partagé auquel on ajoute des lignes. | Syncthing (PC ↔ Android) résout mal les écritures concurrentes sur un même fichier : deux appareils qui appendent produisent un conflit. Un fichier par événement rend le conflit structurellement impossible. |
| **R2** | **Append-only.** Un enregistrement daté n'est jamais réécrit ni supprimé. Une correction est un **ajout**, jamais une réécriture. | C'est un dossier clinique. L'historique doit rester lisible, y compris ce qui s'est révélé faux. Le git log est l'audit. |
| **R3** | **Le format suit l'auteur.** Ce que **Claude** écrit → Markdown + frontmatter YAML. Ce qu'une **application** écrit → JSON. | Claude produit du Markdown de façon fiable et le relit sans parseur. Les apps produisent du JSON de façon fiable. Chacun son format, pas de conversion. |
| **R4** | **Nommage `AAAA-MM-JJ` en préfixe, toujours.** Le tri lexicographique = le tri chronologique. | Un `ls` trié est une chronologie. Aucun index à maintenir. |
| **R5** | **Aucun champ obligatoire ne demande d'écrire ou de parler.** Tout ce qui est requis est un nombre ou un choix fermé. Le texte libre est toujours facultatif. | Contrainte shutdown (PLAN §0) : le dossier doit rester alimentable quand le canal verbal est coupé. |
| **R6** | **On cote des comportements observables, pas des ressentis.** ⭐ | Alexithymie probable (rapport §9.2) + déficit intéroceptif confirmé (§6.5). Demander « note ton anxiété sur 10 » revient à demander d'utiliser une fonction perceptive déficitaire — c'est la même erreur que « écoute ta satiété ». Application directe de la règle §9.19 : **signal interne absent → structure externe.** |

> **R6 est la règle la plus facile à enfreindre sans s'en apercevoir.** Chaque fois qu'un champ est ajouté au dossier, la question à poser est : *« Xavier peut-il répondre en observant ce qu'il a fait, ou doit-il introspecter ce qu'il a ressenti ? »* Si c'est la seconde, le champ est mal conçu — il faut lui trouver une ancre comportementale.

---

## 2. Arborescence

```
psy/dossier/
  SCHEMA.md        ce document — normatif
  profil.md        fiche de profil condensée — contexte PERMANENT, rechargé à chaque séance
  etat.md          état COURANT — chantier en cours, traitement, questions ouvertes
  journal/         check-ins quotidiens ...... JSON  — AAAA-MM-JJ.json
  seances/         comptes-rendus de séance .. MD    — AAAA-MM-JJ-seance.md
  crises/          épisodes de crise ......... JSON  — AAAA-MM-JJ-HHMM-<type>.json
  mesures/         échelles cotées ........... JSON  — AAAA-MM-JJ-<echelle>.json
  briefs/          briefs Dr Isorni .......... MD    — AAAA-MM-JJ-isorni.md
  gabarits/        modèles vierges — à copier, jamais à remplir sur place
```

**`profil.md` et `etat.md` sont les deux seules exceptions à R2** (append-only) : ce sont des documents vivants, réécrits. Leur historique est tenu par git, et chacun porte un journal de révisions en pied de page.

### La distinction profil / état — à ne jamais confondre

| | `profil.md` | `etat.md` |
|---|---|---|
| Contenu | Ce qui ne change pas | Ce qui change |
| Exemples | TSA niveau 1, aphantasie, les 3 mécanismes de crise, ce qu'on ne dit jamais | Traitement en cours, poids, chantier ouvert, questions en attente du Dr Isorni |
| Fréquence de révision | Rare (nouveau diagnostic, nouvelle contrainte) | Hebdomadaire (clôture de séance) |
| Qui le charge | Toute séance, tout check-in, tout brief | Idem |

Les deux sont chargés **ensemble** en ouverture de séance. Le profil dit *qui est Xavier* ; l'état dit *où on en est*.

---

## 3. `journal/AAAA-MM-JJ.json` — check-in quotidien

Un fichier par jour. Écrit par Claude Code aujourd'hui, par Android à partir de l'Étape 5. **Cible : moins de 2 minutes, zéro saisie de texte obligatoire.**

### 3.1 Noyau — 7 champs, stables dans le temps

```json
{
  "date": "2026-08-09",
  "source": "claude-code",
  "noyau": {
    "shutdowns": 0,
    "exposition_sociale": 1,
    "retrait_sensoriel": 0,
    "renoncements": 0,
    "activites_investies": 2,
    "sommeil_heures": 6.5,
    "missions_actives": 3
  },
  "campagne": {},
  "notes": null
}
```

| Champ | Type | Question posée, à réponse fermée | Justification clinique |
|---|---|---|---|
| `shutdowns` | entier ≥ 0 | « Combien de fois aujourd'hui as-tu perdu la parole ou été incapable de traiter une demande ? » | ⭐ **Indicateur n° 1.** « La fréquence des pertes de parole est le meilleur indicateur de suivi » du burnout autistique (rapport §10.5). |
| `exposition_sociale` | 0-3 | « Combien d'heures d'interaction sociale non choisie ? » 0 = aucune · 1 = < 1 h · 2 = 1-3 h · 3 = > 3 h | Proxy comportemental du **camouflage**, dont l'intensité prédit anxiété, dépression et épuisement indépendamment des traits autistiques (§9.6). Mesure l'exposition, pas l'effort ressenti — R6. |
| `retrait_sensoriel` | entier ≥ 0 | « Combien de fois as-tu dû te retirer, mettre un casque, baisser la lumière, quitter une pièce ? » | Charge sensorielle, catégorie de stress n° 2 au Groden (3,50). Comptage d'actions, pas d'inconfort — R6. |
| `renoncements` | entier ≥ 0 | « À combien de choses as-tu renoncé à cause de l'angoisse (sortie, courses, appel, déplacement) ? » | Ancre comportementale de l'anxiété : l'**évitement** est le critère D de l'agoraphobie (§6.2.b) et il s'observe, contrairement à l'angoisse. |
| `activites_investies` | 0-3 | « Combien d'activités as-tu pu investir hors obligations ? » 0 = aucune · 3 = trois ou plus | Ancre comportementale de l'humeur. La **clinophilie** — « rester au lit sans pouvoir en sortir ni investir une activité » — est le marqueur dépressif documenté chez Xavier (§6.4). On mesure ce marqueur-là, pas « ton moral sur 10 ». |
| `sommeil_heures` | nombre ≥ 0 | « Combien d'heures de sommeil, réveils compris ? » | Critère C du TAG à documenter (§6.2.d) — et référence pour juger l'effet de la PPC. |
| `missions_actives` | entier ≥ 0 | « Combien de missions professionnelles en cours ? » | **Seule variable d'ajustement disponible** — pas la famille, pas le sommeil (§9.17, §10.4). |

**Ce que le noyau ne contient délibérément pas :** aucun champ « anxiété /10 », « humeur /10 », « fatigue /10 », « niveau de stress ». Tous violeraient R6. S'ils manquent un jour, ils seront réintroduits **avec une ancre comportementale**, jamais comme échelle introspective.

### 3.2 Campagne — champs temporaires liés au chantier en cours

Le champ `campagne` porte les mesures **du chantier ouvert**, et seulement lui. Quand le chantier se ferme, ses champs sortent du journal : le journal ne grossit jamais indéfiniment.

Les champs actifs sont déclarés dans `etat.md` § « Campagne en cours ». Au 09/08/2026 (Étape 1 — Axe D) :

```json
"campagne": {
  "ppc_minutes": 0,
  "repas_servis_une_fois": 3,
  "activite_minutes": 0,
  "poids_kg": null
}
```

| Champ | Type | Justification |
|---|---|---|
| `ppc_minutes` | entier ≥ 0 | ⭐ **SAOS sévère insuffisamment traité — le fait le plus important du dossier** (rapport §6.6). Donnée **objective, issue du télésuivi de l'appareil**, pas d'une auto-évaluation : c'est exactement l'instrument qu'appelle un déficit intéroceptif (§10.8). |
| `repas_servis_une_fois` | entier 0-4 | Structure externe alimentaire : quantité décidée **avant**, servie une fois, pas de resservage (§10.7.b). On compte les repas **conformes à la structure**, jamais les calories. |
| `activite_minutes` | entier ≥ 0 | Prescription médicale, pas hygiène de vie (§10.7.c). Sans impact, domicile. |
| `poids_kg` | nombre \| null | Hebdomadaire, pas quotidien. `null` les autres jours. Cible : 7-10 % → 99-102,3 kg (§9.22). |

> ⚠️ **Interdit dans le journal, en toute circonstance :** compteur de régularité, série (« streak »), pourcentage d'objectif atteint, moyenne mobile affichée à Xavier, rappel de manquement, jugement calorique. Le Groden cote « Positif » à 1,50 : **il n'y a rien à motiver chez Xavier, il y a des charges à réduire** (§9.13). Un compteur est une charge.

### 3.3 Règles de remplissage

- **Un jour sans check-in est un jour sans fichier.** Aucun rattrapage rétroactif, aucune relance, aucune trace de manquement. L'absence de fichier n'est pas une donnée négative — elle n'est pas une donnée du tout.
- Un champ auquel Xavier ne répond pas est écrit `null`. `null` ≠ `0`.
- `notes` est **toujours** facultatif et **toujours** en dernier. Jamais de question ouverte avant que le noyau soit rempli.
- `source` : `"claude-code"` | `"android"` | `"web"`.

---

## 4. `crises/AAAA-MM-JJ-HHMM-<type>.json`

Un fichier par épisode. Le nom porte le type pour qu'il soit lisible sans ouvrir le fichier.

> 🔴 **La règle la plus importante du dossier : les trois mécanismes ne se confondent jamais.** Panique, vasovagal et shutdown ont des **parades différentes**, et appliquer la mauvaise est délétère (rapport §9.14, PLAN §0). Le champ `type` n'a pas de valeur par défaut et ne peut pas être laissé vide.

```json
{
  "horodatage": "2026-08-09T14:32:00+02:00",
  "type": "vasovagal",
  "contexte": "medical",
  "declencheur": "pose de cathéter",
  "duree_minutes": 8,
  "parade_utilisee": "tension_appliquee",
  "parade_efficace": true,
  "perte_de_connaissance": false,
  "source": "claude-code",
  "notes": null
}
```

| Champ | Valeurs | Notes |
|---|---|---|
| `type` | `panique` \| `vasovagal` \| `shutdown` \| `indetermine` | **Obligatoire.** `indetermine` est une réponse légitime — mieux vaut « je ne sais pas » qu'un type inventé. Le tri se fait après, à froid. |
| `contexte` | `transport` \| `foule` \| `lieu_clos` \| `medical` \| `social` \| `conflit` \| `domicile` \| `autre` | Les cinq premiers recouvrent les situations agoraphobogènes cotées 4-5 au Groden. |
| `parade_utilisee` | `tension_appliquee` \| `respiration` \| `retrait_sensoriel` \| `mot_code` \| `sortie_situation` \| `aucune` | `tension_appliquee` **uniquement** pour le vasovagal. `mot_code` uniquement pour le shutdown. |
| `perte_de_connaissance` | booléen | Discriminant capital : la panique ne fait pratiquement jamais perdre connaissance ; le vasovagal, si (§9.14). Un `true` sur un épisode typé `panique` doit déclencher une révision du typage en séance. |

**Escalade :** si l'épisode comporte une idéation suicidaire ou une détresse aiguë, le fichier est écrit **après** le protocole de crise, jamais avant. Le protocole passe d'abord (**3114**, cf. `profil.md` § Protocole de crise).

---

## 5. `seances/AAAA-MM-JJ-seance.md`

Un fichier par séance de fond (hebdomadaire, week-end en journée — PLAN §2.1). Markdown + frontmatter.

```markdown
---
date: 2026-08-09
duree_minutes: 52
cible: ppc-desensibilisation
mesures_passees: [vviq]
palier_atteint: 2
prochaine_seance: 2026-08-16
matiere_ouverte: false
---

## Ouverture
## Travail
## Clôture
## Décisions
## Repris à la prochaine séance
```

| Champ frontmatter | Notes |
|---|---|
| `cible` | Une seule cible par séance. Identifiants : `ppc-desensibilisation`, `alimentation-structure`, `agoraphobie-exposition`, `tension-appliquee`, `shutdown-protocole`, `alexithymie-nommage`, `camouflage-pacing`, `tag-ruminations`, `deuil-ainee`. |
| `mesures_passees` | Renvoie aux fichiers `mesures/` du même jour. |
| `palier_atteint` | Pour les cibles à paliers (exposition, PPC, alimentation). `null` sinon. |
| `matiere_ouverte` | ⚠️ **Doit être `false` en fin de séance.** `true` signifie qu'on a ouvert du matériel émotionnel sans le refermer — garde-fou câblé du PLAN §3.1. Si `true`, la séance suivante s'ouvre là-dessus, sans exception. |

**Règle de clôture (non négociable) :** aucune séance ne se termine sur du matériel ouvert. La section `## Clôture` est obligatoire et ne peut pas être vide.

---

## 6. `mesures/AAAA-MM-JJ-<echelle>.json`

Une passation = un fichier. Identifiants d'échelle : `vviq`, `tas20`, `catq`, `bes`, `gad7`, `phq9`, `diva5`, `epworth`, `isi`, `maia`.

```json
{
  "date": "2026-08-09",
  "echelle": "vviq",
  "version": "VVIQ-16-Zeman",
  "score": 16,
  "score_max": 80,
  "seuil": { "valeur": 32, "sens": "en_dessous", "interpretation": "aphantasie" },
  "sous_scores": null,
  "reponses": [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
  "passation": "web",
  "notes": null
}
```

- `version` porte **l'identification exacte de l'instrument, sens de cotation compris**. Le VVIQ en est la démonstration : la version originale de Marks (1973) cote à l'envers (1 = image parfaitement claire), la convention moderne dite « de Zeman » cote 1 = aucune image. **Un même score lu avec la mauvaise convention inverse la conclusion.** D'où `"VVIQ-16-Zeman"`, et non `"VVIQ"` ni `"VVIQ-2"` — le VVIQ-2 (Marks, 1995) est un autre instrument, à 32 items et 160 points. Fiche : `corpus/echelles/vviq.md`.
- `reponses` conserve **toujours** les réponses item par item, **et en compte autant que l'instrument a d'items** — 16 pour le VVIQ ci-dessus. Le rapport v2.0 a pu re-coter l'AQ et l'EQ de façon indépendante uniquement parce que les réponses brutes existaient (§5) — c'est ce qui a permis de démontrer que le « manque d'empathie » de Xavier est un déficit de décodage et non d'intérêt pour autrui (§9.1). **Un score seul n'est pas une mesure, c'est un résumé.**
- `seuil.sens` : `au_dessus` | `en_dessous` — quel côté du seuil est cliniquement positif dépend de l'échelle.

---

## 7. `briefs/AAAA-MM-JJ-isorni.md`

Une page, format médecin : dense, factuel, sans interprétation gratuite (PLAN §6.1). Frontmatter : `date`, `consultation_prevue`, `periode_couverte`, `transmis` (booléen — **Xavier relit et décide de transmettre ou non, à chaque fois**).

Structure imposée : Évolution chiffrée · Effets du traitement · Événements · **Questions à trancher** · Ce qui n'a pas changé.

---

## 8. Ce que le dossier ne contient jamais

| Interdit | Raison |
|---|---|
| Un conseil de modification de traitement | Non-substitution (PLAN §6). Le dispositif complète le Dr Isorni, il ne prescrit pas. |
| Un compteur de régularité, une série, un taux d'observance présenté comme une note | §9.13 — réduire les charges, pas motiver. Le télésuivi PPC sert à **ajuster les réglages**, pas à noter le patient (§10.8). |
| Une échelle introspective sans ancre comportementale | R6. |
| Un champ obligatoire en texte libre | R5 — inutilisable en shutdown. |
| Des données concernant Chourouk ou les filles au-delà de ce qui concerne directement Xavier | Elles n'ont pas consenti à un dossier. Chourouk reçoit le mot-code et une fiche explicative, rien d'autre (PLAN §6.1). |

---

## 9. Évolution de ce schéma

Ajouter un champ est un acte de conception, pas une commodité. Trois questions avant tout ajout :

1. **R6** — répond-on en observant, ou en introspectant ?
2. **R5** — le champ est-il remplissable en shutdown ?
3. **Coût** — qu'est-ce qu'on retire en échange ? Le journal a un budget de 2 minutes ; il est déjà dépensé.

Toute modification de ce document est annoncée à Xavier **avant** d'être appliquée (prévisibilité = fonctionnalité, PLAN §0), et consignée ci-dessous.

| Version | Date | Modification |
|---|---|---|
| 1.1 | 09/08/2026 | **Correction de l'exemple §6, annoncée puis appliquée** (audit de cohérence). L'exemple portait `"version": "VVIQ-2"` avec `score_max: 80` — incohérent avec lui-même, le VVIQ-2 comptant 32 items et plafonnant à 160 ; et `reponses` n'en listait que 4 pour une échelle qui en compte 16, ce qui contredisait la règle « réponses item par item, toujours ». Corrigé en `"VVIQ-16-Zeman"` et 16 réponses. Écart repéré et signalé à l'ouverture de l'Étape 2 (`corpus/echelles/vviq.md` §6). **Aucune règle ne change** : seul l'exemple était faux. |
| 1.0 | 09/08/2026 | Création — Étape 0 du PLAN. |
