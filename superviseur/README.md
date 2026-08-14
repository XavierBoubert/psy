# `superviseur/` — Claude Superviseur, la contre-expertise

**Le superviseur du psy.** Il supervise **Claude, jamais Xavier**.

Doctrine complète et normative : [`../PLAN.md` §4](../PLAN.md#4-claude-superviseur--le-contrôle). Le skill vit dans **`.claude/skills/psy-superviseur`** — Claude Code ne le découvre que là.

---

## Le risque qu'il traite

> 🔴 **Presque toutes les sources de ce dossier sont écrites par l'instance qui les consomme.** Le rapport, le plan, les fiches, les protocoles, les skills — tous générés par Claude, tous se citant les uns les autres comme s'ils faisaient autorité. **Les seules sources primaires** sont l'évaluation Saley, le certificat Isorni, les questionnaires bruts, les trois courriers Roisman, la biopsie et le DSM-5.
>
> **Un dispositif qui perd cette distinction confond sa propre cohérence avec la vérité.**

---

## Ce qu'il ne fait jamais

| | |
|---|---|
| **Écrire dans `psy/outputs/dossier/`** | Il constate ; **la correction est un acte séparé**, fait par le psy |
| **Modifier ou publier le programme** | Il rend un verdict, il n'agit pas dessus |
| **Noter Xavier** | Il ne le supervise pas. Il ne s'adresse jamais à lui |

---

## Les dix contrôles

| # | Contrôle | Ce qu'il traque |
|---|---|---|
| **C1** | Source circulaire | Une fiche qui cite une fiche comme source d'autorité, sans document primaire au bout |
| **C2** | Fait périmé propagé | Un fait corrigé au document maître et resté vrai ailleurs. **Le mode de défaillance le mieux documenté du dossier** |
| **C3** | Invariant déclaré, non câblé | Une règle affirmée partout et implémentée nulle part |
| **C4** | ⭐ Dérive R6 | Une question qui demande d'introspecter un ressenti. **Se réintroduit toute seule** — c'est la formulation naturelle en français |
| **C5** | 🔴 Effet miroir | Le dispositif contredit-il Xavier — jamais, parfois, ou seulement sur des points sans enjeu ? |
| **C6** | Autorité fabriquée | Un seuil ou une norme cités de mémoire, une traduction non validée dont la réserve a disparu |
| **C7** | Prolifération | Plus de doctrine produite qu'exécutée. Compter les fiches écrites contre les fois appliquées |
| **C8** | ⭐ Programme désynchronisé | `companion/inputs/programme.json` est une **copie** de ce que le dossier a décidé, et une copie périme |
| **C9** | 🔴 Contenu non dérivé | Une fiche de bibliothèque **copiée** d'un protocole au lieu d'être **réécrite pour Xavier** |
| **C10** | 🔴 Contenu adressé à l'aide-au-patient | Une consigne qui **apprend** quelque chose sur Xavier à l'aidant, ou qui **lui demande un jugement clinique** |

---

## 🔴 Sa passe est bloquante avant publication

**Rien n'atteint Xavier ni le Dr Isorni sans une passe qui porte explicitement sur la version qui sort.** Quatre points où une erreur sort du dispositif :

| Sortie | Vers qui | Contrôle |
|---|---|---|
| Le programme publié | Xavier, **sans intermédiaire pour objecter** | `npm run publish` *(mécanique)* **+ supervision bloquante** |
| La bibliothèque publiée | Xavier, idem | Identique — **C9 s'applique ici en premier** |
| Les consignes de séance à deux | **L'aide-au-patient** | Identique, **plus C10** |
| Le brief | Le Dr Isorni | **Supervision bloquante** + Xavier relit et décide de transmettre |

⭐ **Et c'est câblé, pas déclaré.** `programme.json` porte un champ `supervision` **obligatoire** ; `npm run publish` refuse si le fichier manque, si sa version ne correspond pas à celle du programme, ou si son verdict n'est pas `publiable`. **Un refus se corrige, il ne se contourne pas** — aucune option de forçage n'existe, et il ne doit jamais en exister une.

> ⭐ **Ne pas refaire le travail du script.** Visualisation, cotation de ressenti, streak, numéro d'urgence, prodrome, traitement, relaxation sur vasovagal : `npm run publish` les attrape déjà, **il ne fatigue pas et ne s'habitue pas**. Ce que le superviseur regarde est **ce que le script ne peut pas voir** : une étape conforme mot à mot et **fausse cliniquement**.

---

## Carte

| Chemin | Rôle |
|---|---|
| [`outputs/`](outputs/) | 🔴 **Ses sorties — une supervision par fichier**, `AAAA-MM-JJ-<objet>.md`. **Hors `dossier/`**, délibérément : une supervision n'est pas une donnée clinique |
| [`ressources/`](ressources/README.md) | Ce dont il aurait besoin en propre. **Vide aujourd'hui** — voir le README |
| [`scripts/`](scripts/README.md) | **Vide aujourd'hui.** Ce qui est vérifiable mécaniquement est déjà dans `npm run publish` |

**Ce qu'il lit**, et il le lit toujours en entier : le dossier (`psy/outputs/dossier/`), les check-ins et réponses (`companion/outputs/`), les protocoles et le corpus (`psy/docs/`), et **ce que Kokoro affiche aujourd'hui** (`companion/inputs/`) — ce dernier **contre** le dossier, jamais seul.
