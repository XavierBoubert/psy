---
date: 2026-08-09
perimetre: tout
constats: 4
bloquants: 1
---

# Supervision — 09/08/2026

**Première passe du rôle `psy-superviseur`, déclenchée sur son propre critère** : ≥ 3 fiches ou skills produits dans la journée (il y en a eu neuf). Périmètre : l'ensemble du dispositif.

> **Rappel de ce que ce rôle supervise :** Claude, pas Xavier. Aucune ligne de ce document ne porte sur l'observance, l'assiduité ou les chiffres de Xavier.

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | **C4 — dérive R6** | `psy-seance` instruit : « **On ne passe pas au palier suivant tant que le précédent n'est pas confortable.** » ⭐ **C'est mot pour mot la faute que le dispositif se félicite d'avoir corrigée dans le rapport §10.8** — « confortable » est un ressenti, donc incotable chez quelqu'un d'alexithymique à déficit intéroceptif confirmé. Écrite à l'Étape 0, elle a survécu à l'écriture des protocoles (qui l'ont corrigée chez eux), à l'audit de cohérence du matin, et à l'écriture de `psy-hygiene` (qui l'interdit explicitement). | [`psy-seance/SKILL.md`](../../../.claude/skills/psy-seance/SKILL.md) §2, ligne 56 | 🔴 **Bloquant** |
| **2** | **C3 — invariant déclaré, non câblé** | La **fiche explicative pour Chourouk** est déclarée dans **quatre documents** (`profil.md` §10, `SCHEMA.md` §8, `protocoles/README.md`, `PLAN.md` §6.1) comme l'une des deux seules choses qu'elle reçoit — et elle **n'existe nulle part**. Même forme exacte que le défaut du protocole de crise trouvé le matin même : déclaré partout, écrit nulle part. | `psy/protocoles/` — aucun fichier | ⚠️ Moyenne |
| **3** | **C1 — contradiction de surface** | L'Étape 3 porte « **App tension appliquée** » comme livrable, sans surface. Or `PLAN.md` §1.2.1 pose, « **sans discussion possible** », que la tension appliquée reste sur **Android** et doit être accessible **en un geste depuis l'écran verrouillé** — et Android est l'**Étape 5**. Le livrable est donc soit mal étagé, soit mal affecté. Un guidage de cycles construit sur le desktop serait inutilisable là où il sert : en salle d'examen. | `PLAN.md` §7 Étape 3 vs §1.2.1 | ⚠️ Moyenne |
| **4** | **C7 — prolifération** | **19 documents doctrinaux** (7 protocoles · 5 fiches d'échelles · 7 skills) contre **1 acte exécuté** (VVIQ). `journal/` : 0 fichier. `seances/` : 0. `crises/` : 0. `briefs/` : 0. Aucun palier entamé sur aucun des trois chantiers. **Le dispositif produit sa doctrine environ vingt fois plus vite qu'il ne l'applique.** | `psy/dossier/` | ⚠️ Moyenne — **structurelle** |

---

## Ce qui tient

Vérifié, et solide — à ne pas recontrôler à la prochaine passe sans raison :

- **C2 — faits périmés.** Les occurrences de « rapport v2.3 » subsistantes sont **toutes** dans des tables de version, c'est-à-dire de l'historique que R2 interdit de réécrire. Aucun pointeur vivant ne renvoie à une version périmée. La correction v2.3 → v2.4 du matin a bien pris.
- **`ppc-desensibilisation.md` est cohérent avec lui-même** : l'en-tête annonce v1.1, le corps signale les deux passages réécrits en v1.1, et la table de version porte les deux lignes. C'est le contrôle qui échoue le plus souvent ailleurs.
- **C6 — autorité fabriquée : comportement correct et constant.** Le BES a été **refusé** plutôt qu'approximé. Les réserves de traduction (CAT-Q) et de normes (VVIQ) sont présentes dans les fiches **et** reportées dans `psy-bilan` et `psy-brief-isorni`. Le VVIQ passé ce jour porte sa réserve dans `notes`.
- **La cotation du VVIQ n'a pas été inversée** — piège réel de cet instrument, seuil appliqué dans le bon sens, `version: "VVIQ-16-Zeman"` conforme.
- **Aucun skill n'écrit dans `psy/dossier/` hors des formats du `SCHEMA.md`.**

---

## Objections de fond

### O1 — Le dispositif a tenu l'aphantasie pour acquise avant de la mesurer, et il a eu de la chance

Le VVIQ du 09/08 confirme (18/80, seuil 32). **Ce n'est pas une excuse.** Pendant deux jours, une contrainte de conception lourde — aucune visualisation, EMDR réduit à son instrument, « lieu sûr » déclaré inopérant — a reposé sur une **déclaration reprise trois fois**, jusqu'à devenir un fait par répétition. Le résultat est tombé du bon côté ; **un superviseur n'a pas le droit de noter le processus sur le résultat.**

**Ce qu'il faut en tirer, concrètement :** deux autres caractéristiques structurantes sont aujourd'hui dans le même statut — *déclarées, non mesurées, déjà traitées comme acquises* : l'**alexithymie** (TAS-20 prévue le 16/08) et le **déficit intéroceptif**, qui est la brique de la règle centrale du dispositif (§9.19) et pour lequel **aucun instrument n'est versé** — le MAIA est nommé dans `tas-20.md` §5 et dans `SCHEMA.md` §6, et n'existe pas. **La règle la plus citée du dossier n'a pas d'instrument.**

### O2 — C7 n'est pas une critique de rythme, c'est un risque clinique daté

Il reste **25 jours** avant la consultation du 03/09 et **29** avant le départ. Le brief du 29-30/08 tirera ses chiffres de `journal/`, qui contient **zéro fichier**. À ce jour, **le brief le mieux outillé du dossier n'aurait rien à mettre dans son tableau** — et la consultation est la dernière avant fin septembre. Chaque jour sans check-in retire une ligne définitivement : un jour sans fichier ne se rattrape pas, par conception (`SCHEMA.md` §3.3), et c'est la bonne conception.

### O3 — Effet miroir : une seule objection substantielle en deux jours

Recensement des désaccords **argumentés et datés** : **une** — l'EMDR, le 08/08/2026. Sur deux jours qui ont produit dix-neuf documents, une soixantaine de décisions de conception et quatre arbitrages de Xavier.

**Lecture honnête, dans les deux sens :** la plupart de ces décisions étaient techniques et n'appelaient pas de contradiction, et le dispositif a plusieurs fois corrigé **ses propres sources** (le §10.8 du rapport, le protocole d'Öst, l'exemple du `SCHEMA.md`) — ce qui est une forme réelle de non-complaisance. **Mais corriger un document est plus facile que contredire la personne qui décide.** Le ratio est à surveiller, pas à conclure. À recompter à la prochaine passe.

---

## Arbitrages demandés

| # | Question | Recommandation |
|---|---|---|
| **A1** | Corriger `psy-seance` §2 — remplacer le critère « confortable » par le critère comportemental ? | **Oui.** C'est le skill qui conduit toute séance de fond, donc celui qui décide effectivement des passages de palier. Le laisser en l'état annule la correction faite partout ailleurs. |
| **A2** | Écrire la fiche explicative pour Chourouk maintenant, ou après le retour de Tunisie ? | **Maintenant.** Le mot-code est déjà convenu et le séjour est une période à haut risque de shutdown, hors du domicile-refuge, avec un nourrisson. C'est le moment où elle sert le plus. |
| **A3** | L'app de tension appliquée : Android (Étape 5) ou desktop (Étape 3) ? | **Android**, et donc **déplacer le livrable en Étape 5** — sauf décision contraire assumée. §1.2.1 est catégorique et la raison est clinique, pas ergonomique. |
| **A4** | Ouvrir un instrument d'intéroception (MAIA) pour adosser la règle §9.19 ? | À trancher. **Non urgent, mais à ne pas oublier** — c'est la règle la plus citée du dispositif et la seule sans mesure. |

> **Ce document constate. Il ne corrige rien.** La correction est un acte séparé — un audit qui répare ce qu'il trouve ne laisse aucune trace de ce qui n'allait pas.

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 09/08/2026 | Première supervision. 4 constats (1 bloquant), 3 objections de fond, 4 arbitrages demandés. |
