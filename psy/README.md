# `psy/` — le dispositif

Réalisation de [`../PLAN.md`](../PLAN.md) — **le document unique du projet**. Toute la doctrine y est ; ce README n'est qu'une carte.
📖 **Le vocabulaire fait foi dans [`../THESAURUS.md`](../THESAURUS.md)** — *un mot, une chose*. `corpus` ≠ `protocole` ≠ `fiche de bibliothèque` ; `chantier` ≠ `cible` ≠ `palier`.

**Étape 0 close (09/08/2026).** Étapes **1** (versant somatique), **2** (instrumentation), **3** (outils de crise) et **5** (Kokoro) sont ouvertes.
🔴 **Jalon en cours : K5 — Kokoro lit le programme et la bibliothèque.**

⚠️ **Écrit ne veut pas dire appliqué.** À ce jour, un seul palier a démarré (tension appliquée) et une seule échelle a été passée (VVIQ).

---

## ⭐ Cinq personas

| Persona | Ce qu'il est | Où il vit |
|---|---|---|
| **Claude Psy** | **Le psychiatre et le psychologue.** Une séance par semaine. Il construit **tout le contenu** et le donne à Kokoro | `.claude/skills/psy-*` (6 skills cliniques) |
| **Claude Superviseur** | **Le superviseur du psy.** Il supervise **Claude, jamais Xavier**. 🔴 **Sa passe est bloquante avant toute publication** | `.claude/skills/psy-superviseur` → [`agent/supervisions/`](agent/supervisions/) |
| **Kokoro (心)** | **Le compagnon du patient.** Il **protège · accompagne · éduque · réconforte**. Il porte toute la documentation accessible à Xavier | [`android/`](android/README.md) |
| ⭐ **L'aide-au-patient** | **La personne qui tient le téléphone** pendant une **séance à deux** et exécute les consignes chronométrées de Kokoro. Aujourd'hui **Chourouk**. 🔴 **Elle n'est pas thérapeute** — elle suit un déroulé, elle ne juge pas | Kokoro, type `seance-duo` |
| **Xavier** | **Le patient.** | — |

> ⭐ **Kokoro ne vient jamais vers Xavier.** Aucune notification, aucune relance, aucun reproche. **Xavier vient à lui, et y trouve tout.** *(Seule exception : l'accès crise sur l'écran verrouillé — une porte, pas un rappel.)*

```
Claude Psy ──programme + bibliothèque──► Kokoro ──journal + réponses──► dossier ──► Claude Psy
     ▲              (après supervision)                                      │
     └──────────────────── Superviseur ◄──────────────────────────────────────┘
```

---

## Carte

| Répertoire | Rôle | État |
|---|---|---|
| **`dossier/`** ⭐ | **Mémoire longitudinale — source de vérité unique.** Format : **[`../PLAN.md` §7](../PLAN.md#7-le-dossier--format)** *(normatif)* | ✅ profil + état + journal + mesures + séances |
| **`programme/`** ⭐ | **La thérapie telle que Kokoro l'affiche** : `programme.json` + **`bibliotheque/`**. Six types d'étape, dont ⭐ **`seance-duo`** — le déroulé chronométré tenu par l'aide-au-patient. Format : **[`../PLAN.md` §8](../PLAN.md#8-le-programme--format)** *(normatif)* | 🏗️ moitié PC écrite et vérifiée · Kokoro ne lit pas encore |
| `agent/` | `supervisions/` — sorties du Superviseur, **hors `dossier/`** | ✅ |
| `corpus/` | Référentiels cliniques indexés | ✅ **échelles** + **tension appliquée** · ⏸️ 3 corpus prioritaires restants |
| `protocoles/` | Fiches actionnables, **écrites pour le praticien** | ✅ 8 fiches |
| `android/` | **Kokoro (心)** — Kotlin + Compose | ✅ **K0 → K4** · 🔴 **K5 en cours** · 🆕 K6 la séance à deux · ⏸️ K7 la présence |
| `web/` | Outils de séance desktop — TypeScript strict | ⏸️ après K5. Premier livrable : **schémas Zod des §7 et §8** |

**Critère de répartition entre surfaces, à ne jamais enfreindre :**
> **Ce qui doit être là au moment où ça arrive → Kokoro. Ce qui demande de la surface et du calme → desktop.**

⚠️ **`protocoles/` et `programme/bibliotheque/` ne sont pas la même chose et ne doivent jamais l'être.** Un protocole est écrit pour le praticien : il porte des diagnostics, des pronostics, des réserves adressées à un professionnel. **Une fiche de bibliothèque est écrite pour Xavier.** C'est le contrôle **C9** du Superviseur.

---

## Par où on entre

| Je veux… | Fichier |
|---|---|
| 🔴 **Faire face à une crise, maintenant** | **[`protocoles/crise-escalade.md`](protocoles/crise-escalade.md)** — prime sur tout le reste |
| Savoir qui est Xavier avant de lui parler | [`dossier/profil.md`](dossier/profil.md) |
| Savoir où on en est aujourd'hui | [`dossier/etat.md`](dossier/etat.md) |
| Écrire ou lire une donnée du dossier | [`../PLAN.md` §7](../PLAN.md#7-le-dossier--format) *(normatif)* |
| Écrire ou publier le programme | [`../PLAN.md` §8](../PLAN.md#8-le-programme--format) *(normatif)* |
| Savoir ce qui transite par Drive | [`../PLAN.md` §6](../PLAN.md#6-le-contenu--google-drive) *(normatif)* |
| Appliquer un protocole en cours | [`protocoles/ppc-desensibilisation.md`](protocoles/ppc-desensibilisation.md) *(chantier n° 1)* |
| Trancher un point clinique | [`../ressources/xavier/Rapport psychiatrique et psychologique.md`](../ressources/xavier/Rapport%20psychiatrique%20et%20psychologique.md) *(**v2.4**, fait foi)* |
| Comprendre une décision de conception | [`../PLAN.md` §11](../PLAN.md#11-journal-des-décisions) |

`profil.md` et `etat.md` se chargent **ensemble**, jamais l'un sans l'autre : le premier dit *qui est Xavier*, le second *où on en est*.

---

## Les sept choses à ne jamais faire

1. **Conseiller une modification de traitement.** Le dispositif complète le Dr Isorni, il ne le remplace pas. Toute question pharmacologique part au brief.
2. **Confondre panique, vasovagal et shutdown.** Trois mécanismes, trois parades ; **la mauvaise parade aggrave**.
3. **Proposer une technique de visualisation.** Aphantasie mesurée à 18/80 — la consigne est **inopérante**, pas difficile.
4. **Demander de s'appuyer sur un signal interne absent** (satiété, fatigue, tension, émotion). Structure externe, toujours.
5. **Introduire un streak, un compteur de régularité ou un reproche d'assiduité.** Il n'y a rien à motiver ; il y a des charges à réduire.
6. 🔴 **Publier sans supervision.** Ni le programme, ni la bibliothèque, ni le brief. **Un refus se corrige, il ne se contourne pas.**
7. 🔴 **Écrire une consigne qui demande un jugement à l'aide-au-patient**, ou qui lui apprend un diagnostic, un score ou une hypothèse. **Elle lit des consignes, pas un dossier.** Contrôle **C10**.

En cas d'idéation suicidaire ou de détresse aiguë : **3114**, gratuit, 24h/24. **Protocole complet : [`protocoles/crise-escalade.md`](protocoles/crise-escalade.md)** *(`dossier/profil.md` §4 n'en est que le résumé)*.

🔴 **Les numéros d'appel d'urgence — 15, 112, 114 — ont été retirés du dispositif le 10/08/2026**, à la demande de Xavier. **Le 3114 est le seul conservé, et il ne s'affiche que sur ce déclencheur-là** — jamais en ouverture, jamais « au cas où », **jamais dans Kokoro**.

⭐ **Si la parole est coupée**, le 3114 est inaccessible — c'est un numéro de téléphone. Mot-code « shutdown » à Chourouk, canal écrit : §4 de la fiche.
