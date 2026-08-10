# `psy/` — le dispositif

Implémentation du plan de conception `PLAN.md` (racine).

**Étape 0 — socle minimal : close (09/08/2026).** Trois étapes sont **ouvertes en parallèle**, toutes le 09/08/2026 :
**Étape 1 — Axe D** (prescription médicale) : les trois protocoles sont écrits · **Étape 2 — instrumentation du suivi** : les instruments sont versés, le check-in quotidien démarre · **Étape 3 — outils de crise** : le protocole câblé et la tension appliquée sont écrits.

⚠️ **Écrit ne veut pas dire appliqué.** À ce jour, aucun palier n'a démarré et aucune échelle n'a été passée.

---

## Carte

| Répertoire | Rôle | État |
|---|---|---|
| **`dossier/`** ⭐ | **Mémoire longitudinale — source de vérité unique.** Lue et écrite par les trois surfaces. | ✅ schéma + profil + état |
| `agent/` | Rôles Claude Code + `supervisions/`. **Les skills vivent dans `.claude/skills/`** — cf. `agent/README.md` | ✅ **7/7** — `psy-seance`, `psy-journal`, `psy-crise`, `psy-bilan`, `psy-brief-isorni`, `psy-hygiene`, `psy-superviseur` |
| `corpus/` | Référentiels cliniques indexés | ✅ **échelles** (VVIQ, TAS-20, CAT-Q, GAD-7/PHQ-9 ; BES partiel) + **tension appliquée** (corpus n° 1) · ⏸️ 3 corpus prioritaires restants |
| `protocoles/` | Protocoles thérapeutiques opérationnels (fiches actionnables) | ✅ 7 fiches — **crise/escalade**, **PPC**, tension appliquée, alimentation, activité physique, panique, jour de vol |
| `web/` | Outils de séance desktop — TypeScript strict | ⏸️ aucun outil écrit — le premier attendu est désormais la **formalisation Zod du schéma du dossier**, avec le jalon K3 ([`android/PLAN-KOKORO.md`](android/PLAN-KOKORO.md) §7) |
| `android/` | Kokoro (心) — compagnon permanent, Kotlin + Compose | 🏗️ **Étape 5 ouverte (10/08/2026)** — plan de construction : [`android/PLAN-KOKORO.md`](android/PLAN-KOKORO.md). ⭐ Le premier livrable est l'**écran de crise**, pas le visage |
| `SYNCHRO.md` | Décisions de synchronisation et de sécurité des données | ✅ |

**Critère de répartition entre surfaces, à ne jamais enfreindre :**
> **Ce qui doit être là au moment où ça arrive → Android. Ce qui demande de la surface et du calme → desktop.**

---

## Par où on entre

| Je veux… | Fichier |
|---|---|
| 🔴 **Faire face à une crise, maintenant** | **`protocoles/crise-escalade.md`** — prime sur tout le reste |
| Savoir qui est Xavier avant de lui parler | `dossier/profil.md` |
| Savoir où on en est aujourd'hui | `dossier/etat.md` |
| Écrire ou lire une donnée du dossier | `dossier/SCHEMA.md` *(normatif)* |
| Appliquer un protocole en cours | `protocoles/ppc-desensibilisation.md` *(cible n° 1)* · `protocoles/alimentation-structure-externe.md` · `protocoles/activite-physique-sans-impact.md` |
| Trancher un point clinique | `../ressources/xavier/Rapport psychiatrique et psychologique.md` *(**v2.4**, fait foi)* |
| Comprendre une décision de conception | `../PLAN.md` |

`profil.md` et `etat.md` se chargent **ensemble**, jamais l'un sans l'autre : le premier dit *qui est Xavier*, le second *où on en est*.

---

## Les cinq choses à ne jamais faire

1. **Conseiller une modification de traitement.** Le dispositif complète le Dr Isorni, il ne le remplace pas. Toute question pharmacologique part au brief.
2. **Confondre panique, vasovagal et shutdown.** Trois mécanismes, trois parades ; la mauvaise parade aggrave.
3. **Proposer une technique de visualisation.** Aphantasie — la consigne est inopérante, pas difficile.
4. **Demander de s'appuyer sur un signal interne absent** (satiété, fatigue, tension, émotion). Structure externe, toujours.
5. **Introduire un streak, un compteur de régularité ou un reproche d'assiduité.** Il n'y a rien à motiver ; il y a des charges à réduire.

En cas d'idéation suicidaire ou de détresse aiguë : **3114**, gratuit, 24h/24. **Protocole complet : [`protocoles/crise-escalade.md`](protocoles/crise-escalade.md)** *(`dossier/profil.md` §4 n'en est que le résumé)*.

🔴 **Les numéros d'appel d'urgence — 15, 112, 114 — ont été retirés du dispositif le 10/08/2026**, à la demande de Xavier (motifs : §0 de la fiche). **Le 3114 est le seul conservé, et il ne s'affiche que sur le déclencheur ci-dessus** — jamais en ouverture, jamais « au cas où ».

⭐ **Si la parole est coupée**, le 3114 est inaccessible — c'est un numéro de téléphone. Mot-code « shutdown » à Chourouk, canal écrit : §4 de la fiche.
