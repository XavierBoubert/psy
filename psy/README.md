# `psy/` — Claude Psy, le praticien

Réalisation de [`../PLAN.md`](../PLAN.md) — **le document unique du projet**. Toute la doctrine y est ; ce README n'est qu'une carte.
📖 **Le vocabulaire fait foi dans [`../THESAURUS.md`](../THESAURUS.md)** — *un mot, une chose*. `corpus` ≠ `protocole` ≠ `fiche de bibliothèque` ; `chantier` ≠ `cible` ≠ `palier`.

**Ce qu'il est.** Le psychiatre et le psychologue. Une **séance de fond par semaine**. **Il construit tout le contenu** — protocoles, désensibilisations, bilans, questionnaires, briefs, programme — et le donne à Kokoro.

**Ce qu'il ne fait jamais :** prescrire · conseiller une modification de traitement, même sous forme interrogative · **publier sans supervision** · publier hors séance · venir vers Xavier de lui-même.

**Étape 0 close (09/08/2026).** Étapes **1** (versant somatique), **2** (instrumentation), **3** (outils de crise) et **5** (Kokoro) sont ouvertes.
🔴 **Jalon en cours : K5 — Kokoro lit le programme et la bibliothèque.**

⚠️ **Écrit ne veut pas dire appliqué.** À ce jour, un seul palier a démarré (tension appliquée) et une seule échelle a été passée (VVIQ).

---

## Carte

| Chemin | Rôle | État |
|---|---|---|
| [`docs/protocoles/`](docs/protocoles/README.md) | **Fiches actionnables, écrites pour le praticien** — réserves, hypothèses, frontières de non-substitution | ✅ 7 fiches *(la 8ᵉ est passée chez l'aidant)* |
| [`docs/corpus/`](docs/corpus/README.md) | **Référentiels cliniques indexés** — `echelles/`, `tension-appliquee/` | ✅ échelles + tension appliquée · ⏸️ 3 corpus prioritaires restants |
| [`docs/references/`](docs/references/README.md) | **Littérature source convertie** — DSM-5, validation française de la BES. `originales/` garde les PDF | ✅ |
| [`docs/gabarits/`](docs/gabarits/) | **Modèles vierges** — à copier, jamais à remplir sur place | ✅ 5 gabarits |
| **[`outputs/dossier/`](outputs/dossier/)** ⭐ | 🔴 **Mémoire longitudinale — source de vérité.** `profil.md`, `etat.md`, `seances/`, `crises/`, `mesures/`, `briefs/`. Format : **[`../PLAN.md` §7](../PLAN.md#7-le-dossier--format)** *(normatif)* | ✅ |
| [`scripts/`](scripts/) | `programme-publish.ts` · `contenu-sync.ts` · les trois convertisseurs de documents | ✅ |

> ⭐ **Le dossier clinique est réparti sur deux rôles, et la ligne de partage est celle de l'auteur.** Ce que Claude Psy écrit est ici ; **les check-ins et les réponses, écrits par Kokoro, vivent dans [`../companion/outputs/`](../companion/outputs/)**. C'est **une seule mémoire longitudinale**, qui se charge en entier — voir [`../PLAN.md` §7.2](../PLAN.md#72-arborescence).
>
> ⭐ **Le programme qu'il écrit ne vit pas ici non plus** : il est **donné** à Kokoro, donc il vit dans [`../companion/inputs/`](../companion/inputs/). Écrire dedans est un acte de séance, jamais un acte de passage.

---

## Le circuit, et les deux commandes qui le tiennent

```
Claude Psy ──── programme + bibliothèque ────► Kokoro ──── journal + réponses ────► dossier
     ▲            (companion/inputs/)                      (companion/outputs/)        │
     │              après supervision                                                  │
     └───────────────────── Superviseur ◄──────────────────────────────────────────────┘
```

| Quand | Commande | Ce qu'elle fait |
|---|---|---|
| **À l'ouverture de séance** | `npm run sync` | Verse au dépôt ce que Kokoro a écrit. **N'écrase jamais un fichier existant** |
| **À la clôture de séance** | `npm run publish` | Publie la thérapie et la bibliothèque. 🔴 **Refuse tout si un invariant est enfreint ou si la supervision manque** |

⭐ **`npm run publish` est la seule fenêtre d'écriture du programme.** Entre deux séances, **l'écran de Xavier ne change pas** — c'est la prévisibilité, pas une limitation technique.

---

## Les six skills

Elles vivent dans **`.claude/skills/psy-*`** — Claude Code ne les découvre que là.

| Skill | Rôle |
|---|---|
| `psy-seance` | Séance de fond hebdomadaire — ouverture / une seule cible / clôture obligatoire → `outputs/dossier/seances/`. ⭐ **Battement hebdomadaire du dispositif** |
| `psy-journal` | Check-in quotidien — 7 questions fermées, < 2 min → `../companion/outputs/journal/` |
| `psy-crise` | **Triage de crise** — sécurité avant mécanisme. ⭐ **Seule exception au chargement de contexte : la question de sécurité se pose avant la lecture du dossier** |
| `psy-bilan` | Passation et cotation d'une échelle → `outputs/dossier/mesures/`. Items lus dans `docs/corpus/echelles/`, **jamais restitués de mémoire** |
| `psy-brief-isorni` | Brief d'une page avant consultation → `outputs/dossier/briefs/`, `transmis: false`. **Aucune proposition pharmacologique** |
| `psy-hygiene` | Versant somatique (PPC, alimentation, activité) — ⭐ **le passage de palier se compte dans le journal, il ne se demande pas** |

**Invariants de tout skill** : charger `profil.md` + `etat.md` avant d'agir · **non-substitution** · protocole de crise câblé · aucune visualisation · utilisable sans parler ni écrire · zéro streak · annoncer avant de faire.

---

## Par où on entre

| Je veux… | Fichier |
|---|---|
| 🔴 **Faire face à une crise, maintenant** | **[`docs/protocoles/crise-escalade.md`](docs/protocoles/crise-escalade.md)** — prime sur tout le reste |
| Savoir qui est Xavier avant de lui parler | [`outputs/dossier/profil.md`](outputs/dossier/profil.md) |
| Savoir où on en est aujourd'hui | [`outputs/dossier/etat.md`](outputs/dossier/etat.md) |
| Écrire ou lire une donnée du dossier | [`../PLAN.md` §7](../PLAN.md#7-le-dossier--format) *(normatif)* |
| Écrire ou publier le programme | [`../PLAN.md` §8](../PLAN.md#8-le-programme--format) *(normatif)* |
| Savoir ce qui transite par Drive | [`../PLAN.md` §6](../PLAN.md#6-le-contenu--google-drive) *(normatif)* |
| Appliquer un protocole en cours | [`docs/protocoles/ppc-desensibilisation.md`](docs/protocoles/ppc-desensibilisation.md) *(chantier n° 1)* |
| Trancher un point clinique | [`../patient/ressources/Rapport psychiatrique et psychologique.md`](../patient/ressources/Rapport%20psychiatrique%20et%20psychologique.md) *(**v2.4**, fait foi)* |
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

⚠️ **[`docs/protocoles/`](docs/protocoles/README.md) et [`../companion/inputs/bibliotheque/`](../companion/inputs/bibliotheque/README.md) ne sont pas la même chose et ne doivent jamais l'être.** Un protocole est écrit pour le praticien : il porte des diagnostics, des pronostics, des réserves adressées à un professionnel. **Une fiche de bibliothèque est écrite pour Xavier.** C'est le contrôle **C9** du Superviseur.

En cas d'idéation suicidaire ou de détresse aiguë : **3114**, gratuit, 24h/24. **Protocole complet : [`docs/protocoles/crise-escalade.md`](docs/protocoles/crise-escalade.md)** *(`outputs/dossier/profil.md` §4 n'en est que le résumé)*.

🔴 **Les numéros d'appel d'urgence — 15, 112, 114 — ont été retirés du dispositif le 10/08/2026**, à la demande de Xavier. **Le 3114 est le seul conservé, et il ne s'affiche que sur ce déclencheur-là** — jamais en ouverture, jamais « au cas où », **jamais dans Kokoro**.

⭐ **Si la parole est coupée**, le 3114 est inaccessible — c'est un numéro de téléphone. Mot-code « shutdown » à Chourouk, canal écrit : §4 de la fiche.
