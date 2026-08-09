# `psy/` — le dispositif

Implémentation du plan de conception `PLAN.md` (racine). **Étape 0 — socle minimal : close. Étape 1 — Axe D : ouverte au 09/08/2026, protocoles écrits.**

---

## Carte

| Répertoire | Rôle | État |
|---|---|---|
| **`dossier/`** ⭐ | **Mémoire longitudinale — source de vérité unique.** Lue et écrite par les trois surfaces. | ✅ schéma + profil + état |
| `agent/` | Rôles Claude Code. **Les skills vivent dans `.claude/skills/`** — cf. `agent/README.md` | ✅ `psy-seance`, `psy-journal` |
| `corpus/` | Référentiels cliniques indexés | ⏸️ 4 corpus prioritaires à récupérer |
| `protocoles/` | Protocoles thérapeutiques opérationnels (fiches actionnables) | ✅ 3 fiches — **PPC**, alimentation, activité physique |
| `web/` | Outils de séance desktop — TypeScript strict | ⏸️ Étape 3-4 |
| `android/` | Kokoro (心) — compagnon permanent, Kotlin + Compose | ⏸️ Étape 5 |
| `SYNCHRO.md` | Décisions de synchronisation et de sécurité des données | ✅ |

**Critère de répartition entre surfaces, à ne jamais enfreindre :**
> **Ce qui doit être là au moment où ça arrive → Android. Ce qui demande de la surface et du calme → desktop.**

---

## Par où on entre

| Je veux… | Fichier |
|---|---|
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

En cas d'idéation suicidaire ou de détresse aiguë : **3114**, gratuit, 24h/24. Protocole complet dans `dossier/profil.md` §4.
