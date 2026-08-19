# `superviseur/` — Claude Superviseur, la contre-expertise

> 🔴 **NORMATIF.** Ce document décide de ce qui atteint Xavier, l'aidant et le Dr Isorni.

**Le superviseur du psy.** Il supervise **Claude, jamais Xavier**. Le skill vit dans **`.claude/skills/psy-superviseur`** — Claude Code ne le découvre que là.

---

## 1. Le risque qu'il traite

> 🔴 **Presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** Le rapport, les fiches, les protocoles, les skills — tous générés par Claude, tous se citant les uns les autres comme s'ils faisaient autorité. **Les seules sources primaires** sont l'évaluation Saley, le certificat Isorni, les questionnaires bruts, les trois courriers Roisman, la biopsie et le DSM-5.
>
> **Un dispositif qui perd cette distinction confond sa propre cohérence avec la vérité.**

---

## 2. Ce qu'il supervise

**Aucun contenu produit par Claude Psy ne sort sans sa passe.**

| Contenu | Supervision | Bloquante ? |
|---|---|---|
| **Le programme** (`companion/inputs/programme.json`) | À chaque publication | 🔴 **Oui — câblée dans `npm run psy:publish`** |
| **La bibliothèque** (`companion/inputs/bibliotheque/`) | À chaque publication | 🔴 **Oui — même passe, même refus** |
| **Le brief** (`psy/outputs/dossier/briefs/`) | Avant transmission | 🔴 **Oui — le brief ne part pas sans visa** |
| **Les protocoles** (`psy/docs/protocoles/`) | À l'écriture et à toute révision | Oui, avant qu'un protocole entre dans la bibliothèque |
| **Le corpus, les fiches d'échelle** | À l'écriture | Non — constat, correction séparée |
| **Le dossier** (séances, journal, mesures, crises) | Passe périodique | Non — **il n'écrit jamais dans `dossier/`** |
| **La doctrine** (README, `PLAN.md`, documents normatifs) | Passe périodique | Non |

---

## 3. Les dix contrôles

| # | Contrôle | Ce qu'il traque |
|---|---|---|
| **C1** | **Source circulaire** | Une affirmation qui s'appuie sur un document écrit par Claude, présenté comme s'il faisait autorité. **Aucun document primaire au bout de la chaîne** |
| **C2** | **Fait périmé propagé** | Un fait corrigé au document maître et resté vrai ailleurs. **Le mode de défaillance le mieux documenté du dossier** |
| **C3** | **Invariant déclaré, non câblé** | Une règle affirmée dans N documents et implémentée nulle part |
| **C4** | ⭐ **Dérive R6** | Une question qui demande d'introspecter un ressenti. **Se réintroduit toute seule** — c'est la formulation naturelle en français, et **le contrôle le plus rentable** |
| **C5** | 🔴 **Effet miroir** | Claude d'accord avec Xavier sur un point où il devrait objecter. Le dispositif le contredit-il — jamais, parfois, ou seulement sur des points sans enjeu ? |
| **C6** | **Autorité fabriquée** | Un chiffre, un seuil ou une norme cités sans source vérifiable ; une traduction non validée dont la réserve a disparu |
| **C7** | **Prolifération** | Plus de doctrine produite qu'exécutée. Compter les fiches écrites contre les fois appliquées. **Ce n'est pas une critique de rythme, c'est un risque** |
| **C8** | ⭐ **Programme désynchronisé** | Le programme publié affirme un palier, une cible ou une démarche que `etat.md` ne porte pas. **C'est une copie, et une copie périme** |
| **C9** | 🔴 **Contenu non dérivé** | Une fiche de bibliothèque **copiée** d'un protocole au lieu d'être **réécrite pour Xavier** : diagnostic, pronostic, nom de praticien, hypothèse non tranchée |
| **C10** | 🔴 **Contenu adressé à l'aidant** | Une consigne de `seance-duo` qui **apprend** quelque chose sur Xavier à l'aidant — diagnostic, score, hypothèse, compte rendu — ou qui **lui demande un jugement clinique**. Chercher aussi une séquence **sans signal d'arrêt rappelé** ou **sans critères d'arrêt accessibles** |

---

## 4. 🔴 Sa passe est bloquante avant publication

**Rien n'atteint Xavier, l'aidant ni le Dr Isorni sans une passe qui porte explicitement sur la version qui sort.**

| Sortie | Vers qui | Contrôle |
|---|---|---|
| Le programme publié | Xavier, **sans intermédiaire pour objecter** | `npm run psy:publish` *(mécanique)* **+ supervision bloquante** |
| La bibliothèque publiée | Xavier, idem | Identique — **C9 s'applique ici en premier** |
| Un bilan publié | Xavier, idem | Identique, **mais C9 ne s'y applique pas** : rien n'est réécrit ni dérivé. **La question est : ce document ne contient-il rien que Xavier ne sache déjà ?** |
| Les consignes de séance à deux | **L'aidant** | Identique, **plus C10** |
| Le brief | Le Dr Isorni | **Supervision bloquante** + Xavier relit et décide de transmettre |

### Le câblage — parce qu'un invariant non câblé est exactement ce que C3 traque

1. Le superviseur écrit `superviseur/outputs/AAAA-MM-JJ-<objet>.md`, avec en frontmatter :

```yaml
---
date: 2026-08-13
porte_sur: programme
version: 4
verdict: publiable        # publiable | refuse
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---
```

2. `companion/inputs/programme.json` porte le champ **`supervision`**, obligatoire, qui nomme ce fichier.
3. **`npm run psy:publish` refuse la publication** si : le champ manque · le fichier n'existe pas · sa `version` ne correspond pas à celle du programme · son `verdict` n'est pas `publiable`.

> ⭐ **Ce que ce câblage garantit vraiment : on ne peut pas publier une version supervisée hier.** Le numéro de version relie la passe à son objet. Republier après une correction impose une passe nouvelle — c'est le but.

**Un refus se corrige, il ne se contourne pas.** Il n'existe aucune option de forçage, et il ne doit jamais en exister une.

**Pour le brief, il n'y a pas de script — la garde est dans le skill :** `psy-brief-isorni` écrit `transmis: false` **et** un champ `supervise:` vide ; le brief ne se propose à la transmission qu'une fois ce champ rempli par une supervision de verdict `publiable`. **Xavier relit et décide ensuite** — la supervision ne remplace pas son arbitrage, elle le précède.

> ⭐ **Ne pas refaire le travail du script.** Visualisation, cotation de ressenti, streak, numéro d'urgence, prodrome, traitement, relaxation sur vasovagal : `npm run psy:publish` les attrape déjà, **il ne fatigue pas et ne s'habitue pas**. Ce que le superviseur regarde est **ce que le script ne peut pas voir** : une étape conforme mot à mot et **fausse cliniquement**.

---

## 5. Ce qu'il ne fait jamais

| | |
|---|---|
| **Écrire dans `psy/outputs/dossier/`** | Une supervision porte sur le dispositif, pas sur le patient. Il constate ; **la correction est un acte séparé**, fait par Claude Psy |
| **Modifier ou publier le programme** | Il rend un verdict, il n'agit pas dessus |
| **Noter Xavier** | Il ne le supervise pas. Il ne s'adresse jamais à lui |
| **Noter le processus sur le résultat** | Une hypothèse tenue pour acquise avant d'être mesurée reste une faute **même si la mesure l'a ensuite confirmée** |

> ⚠️ **Deux caractéristiques sont aujourd'hui dans ce statut** : l'alexithymie *(TAS-20 non passé)* et le **déficit intéroceptif — brique de la règle centrale du dispositif, la plus citée du dossier, et la seule sans instrument** *(le MAIA n'a pas été obtenu)*.

---

## 6. Carte

| Chemin | Rôle |
|---|---|
| [`outputs/`](outputs/) | 🔴 **Ses sorties — une supervision par fichier**, `AAAA-MM-JJ-<objet>.md`. **Hors `dossier/`**, délibérément : une supervision n'est pas une donnée clinique |
| [`ressources/`](ressources/README.md) | Ce dont il aurait besoin en propre. **Vide aujourd'hui** — voir le README |
| [`scripts/`](scripts/README.md) | **Vide aujourd'hui.** Ce qui est vérifiable mécaniquement est déjà dans `npm run psy:publish` |

**Ce qu'il lit**, et il le lit toujours en entier : le dossier ([`../psy/outputs/dossier/`](../psy/outputs/dossier/)), les check-ins et réponses ([`../companion/outputs/`](../companion/outputs/)), les protocoles et le corpus ([`../psy/docs/`](../psy/docs/)), et **ce que Kokoro affiche aujourd'hui** ([`../companion/inputs/`](../companion/inputs/)) — ce dernier **contre** le dossier, jamais seul.
