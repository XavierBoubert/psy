# `corpus/` — référentiels cliniques indexés

Sources sur lesquelles le dispositif appuie ses affirmations. Règle : **toute affirmation clinique est adossée à une source citable** (`PLAN.md` §3.1, levier n° 6).

## Les quatre corpus prioritaires — validés, à récupérer

| Priorité | Corpus | Pourquoi celui-ci | État |
|---|---|---|---|
| **1** | **Protocole de tension appliquée (Öst)**, complet | Le plus rentable immédiatement : court, validé, enseignable en 1-2 séances, et à acquérir **à froid** — aucun geste médical n'est programmé, c'est la fenêtre idéale | ✅ **[`tension-appliquee/`](./tension-appliquee/README.md)** — versé le 09/08/2026 → fiche [`protocoles/tension-appliquee.md`](../protocoles/tension-appliquee.md) |
| **2** | **TCC alimentaire + intéroception** | ⭐ C'est le corpus où l'avantage sur un psy généraliste est le plus net : le croisement **TSA × conduite alimentaire × déficit intéroceptif** est peu diffusé en pratique française. Inclut la **BES**. | ⏸️ |
| **3** | **TCC de l'agoraphobie — exposition graduée** | Cible la plus ancienne (23 ans). ⭐ **Sert deux fois** : la désensibilisation à la PPC *est* une exposition graduée (rapport §10.8). Paliers écrits, **in vivo uniquement** (aphantasie). | ⏸️ |
| **4** | **Recommandations HAS** — TSA adulte, troubles anxieux | Standard de soin français. Surtout utile pour argumenter auprès des professionnels et pour les dossiers MDPH. | ⏸️ |

## Déjà disponible

| Source | Emplacement |
|---|---|
| ⭐ **Échelles et instruments de mesure** — VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9 (items, cotation, seuils, limites) + BES partiel | **[`echelles/`](./echelles/README.md)** — versé le 09/08/2026, Étape 2 |
| DSM-5 intégral + extraits TSA / TDAH / anxio-dépressif | `psy/docs/references/` |
| Littérature citée au rapport (camouflage, alexithymie, aphantasie, shutdowns, burnout autistique, intéroception, MASLD/NASH, SAOS) | `patient/ressources/Rapport psychiatrique et psychologique.md` §11 — liens vérifiés |

## Reporté

| Corpus | Statut |
|---|---|
| Protocole EMDR — ⭐ **stimulation bilatérale conduite par un tiers** (cadence, longueur des séries, critères d'arrêt) | ⏸️ reporté avec l'axe EMDR (`PLAN.md` §3.6). ⚠️ **Besoin requalifié le 14/08/2026** : l'aidant joue le geste en `seance-duo` — ce n'est plus un instrument à spécifier, c'est un **geste à scripter** |
| ACT / défusion cognitive | ❓ à évaluer pour le TAG — **vérifier la compatibilité avec l'aphantasie** avant de retenir |
| CIM-11 | ❓ optionnel |

## Convention

Un corpus = un sous-répertoire, en Markdown, avec un `README.md` portant : la source exacte, sa date, son statut de validation, et **ce qu'il ne dit pas**. Les PDF bruts vont dans [`../references/originales/`](../references/originales/) et se convertissent avec `npm run pdf-to-markdown`, la version Markdown atterrissant dans [`../references/`](../references/). *(Les documents propres à Xavier, eux, vivent dans `patient/ressources/` — ce ne sont pas des référentiels cliniques.)*
