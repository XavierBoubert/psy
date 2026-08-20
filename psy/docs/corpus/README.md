# `corpus/` — référentiels cliniques indexés

Sources sur lesquelles le dispositif appuie ses affirmations. Règle : **toute affirmation clinique est adossée à une source citable** (`psy/README.md` §1, levier n° 6).

## Les quatre corpus prioritaires

| Priorité | Corpus | Pourquoi celui-ci | État |
|---|---|---|---|
| **1** | **Protocole de tension appliquée (Öst)**, complet | Le plus rentable immédiatement : court, validé, enseignable en 1-2 séances, et à acquérir **à froid** — aucun geste médical n'est programmé, c'est la fenêtre idéale | ✅ **[`tension-appliquee/`](./tension-appliquee/README.md)** — versé le 09/08/2026 → fiche [`protocoles/tension-appliquee.md`](../protocoles/tension-appliquee.md) |
| **2** | **TCC alimentaire + intéroception** | ⭐ C'est le corpus où l'avantage sur un psy généraliste est le plus net : le croisement **TSA × conduite alimentaire × déficit intéroceptif** est peu diffusé en pratique française. Inclut la **BES**. | ✅ **[`alimentation-interoception/`](./alimentation-interoception/README.md)** — indexé le 19/08/2026. ⭐ **Le *regular eating* de CBT-E ne s'appuie déjà pas sur la faim** |
| **3** | **TCC de l'agoraphobie — exposition graduée** | Cible la plus ancienne (23 ans). ⭐ **Sert deux fois** : la désensibilisation à la PPC *est* une exposition graduée (rapport §10.8). Paliers écrits, **in vivo uniquement** (aphantasie). | ✅ **[`agoraphobie-exposition/`](./agoraphobie-exposition/README.md)** — indexé le 19/08/2026. 🔴 **L'exposition agit par violation d'attente, pas par habituation : les SUDS tombent** |
| **4** | **Recommandations HAS** — TSA adulte, troubles anxieux | Standard de soin français. Surtout utile pour argumenter auprès des professionnels et pour les dossiers MDPH. | ✅ **[`has-recommandations/`](./has-recommandations/README.md)** — indexé le 19/08/2026. ⚠️ **Aphantasie, shutdown, camouflage : absents des textes** |

## Déjà disponible

| Source | Emplacement |
|---|---|
| ⭐ **Échelles et instruments de mesure** — VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9, **BES** (version française validée), MAIA-2 : items, cotation, seuils, limites | **[`echelles/`](./echelles/README.md)** — versé le 09/08/2026, Étape 2 |
| DSM-5 intégral + extraits TSA / TDAH / anxio-dépressif | `psy/docs/references/` |
| Littérature citée au rapport (camouflage, alexithymie, aphantasie, shutdowns, burnout autistique, intéroception, MASLD/NASH, SAOS) | `patient/ressources/Rapport psychiatrique et psychologique.md` §11 — liens vérifiés |

## Reporté

| Corpus | Statut |
|---|---|
| Protocole EMDR — ⭐ **stimulation bilatérale conduite par un tiers** (cadence, longueur des séries, critères d'arrêt) | ⏸️ reporté avec l'axe EMDR (`psy/README.md` §6). ⚠️ **Besoin requalifié le 14/08/2026** : l'aidant joue le geste sur une carte qu'elle tient — ce n'est plus un instrument à spécifier, c'est un **geste à scripter** |
| CIM-11 | ❓ optionnel |

## Tranché

| Question | Réponse |
|---|---|
| **ACT / défusion cognitive** — compatible avec l'aphantasie ? | ✅ **[`act-defusion/`](./act-defusion/README.md)** — évalué le 19/08/2026. **Retenue partiellement, en second rideau** : le noyau de la défusion est verbal donc utilisable, ses exercices les plus connus sont visuels donc écartés, et **son versant expérientiel bute sur le déficit intéroceptif, pas sur l'aphantasie**. Cible `tag-ruminations` uniquement, pas avant que le chantier n° 1 soit tenu |

## Convention

> ⚠️ **Indexé ne veut pas dire versé.** Les corpus 2 et 3 portent les sources, le mécanisme et les adaptations obligatoires ; **leurs textes intégraux, sous droits, restent à obtenir** — chaque README dit lesquels et pourquoi. Le corpus 4, lui, est public et téléchargeable.

Un corpus = un sous-répertoire, en Markdown, avec un `README.md` portant : la source exacte, sa date, son statut de validation, et **ce qu'il ne dit pas**. Les PDF bruts vont dans [`../references/originales/`](../references/originales/) et se convertissent avec `npm run psy:pdf2md`, la version Markdown atterrissant dans [`../references/`](../references/). *(Les documents propres à Xavier, eux, vivent dans `patient/ressources/` — ce ne sont pas des référentiels cliniques.)*
