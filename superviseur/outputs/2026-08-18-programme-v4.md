---
date: 2026-08-18
porte_sur: programme
version: 4
verdict: publiable
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v4

**Passe de reprise après le refus de la v3.** Périmètre : les corrections apportées, et ce qu'elles ont pu casser ailleurs. **Ce qui a été vérifié solide à la v3 n'est pas recontrôlé** — la liste est dans [`2026-08-18-programme-v3.md`](./2026-08-18-programme-v3.md) § *Ce qui tient*.

> **Ce rôle supervise Claude, pas Xavier.**

---

## ✅ Verdict : publiable

Le blocage de la v3 est levé. **Les quatre constats de la v3 sont corrigés, y compris les deux qui n'étaient pas bloquants et qu'aucun arbitrage n'exigeait de traiter.**

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | ✅ **C8 — levé** | `## Où tu en es` → `## Par où on commence, et pourquoi`. La phrase de position courante est remplacée par *« l'échelle démarre à son premier cran »*, qui reste vraie à tous les paliers. ⭐ **Un renvoi explicite a été ajouté** : *« cette fiche ne dit pas où tu en es rendu … l'étape en cours est celle qui s'affiche dans ta liste du jour — c'est elle qui fait foi, jamais ce texte »*. **La fiche désigne elle-même la surface qui la prime**, ce qui est plus fort que de simplement se taire. | `bibliotheque/ppc-les-paliers.md` | ✅ Corrigé |
| **2** | ✅ **C5 — levé** | L'attribution est réécrite **aux deux endroits**, pas seulement dans le tableau : § *Travail* et § *Décisions* n° 2 portent *« recommandé par Claude Psy, ratifié par Xavier »*, avec le mécanisme en toutes lettres — question à trois branches, réponse « Ok », fenêtre de correction ouverte et non utilisée. **Le palier est inchangé** ; seule l'étiquette l'est. | `seances/2026-08-18-seance.md` | ✅ Corrigé |
| **3** | ✅ **C2 — levé** | `etat.md` §1 ne porte plus *« exécution suspendue à des démarches réelles »*. Les deux lignes du tableau disent maintenant la même chose. | `psy/outputs/dossier/etat.md` §1 | ✅ Corrigé |
| **4** | ✅ **C2 / C8 — levé** | `PLAN.md` §1 : la case du nouveau masque est cochée et datée, la prise en charge est **séparée** en une case propre — les deux items étaient confondus sur une seule ligne, ce qui aurait fait disparaître le second en cochant le premier. §2 Étape 5 K5 dit maintenant ce que le code fait *(`Bibliotheque.kt`, `LecteurPdf.kt`)* et ce qui manque *(`reponses/`)*. | `PLAN.md` §1, §2 | ✅ Corrigé |
| **5** | **A3 exécuté — la règle est écrite** | `companion/PROGRAMME.md` §6 porte désormais le partage des deux surfaces, **dérivé de leur durée de vie et non d'un goût**, avec le motif C8 nommé. ⭐ **Elle s'applique aux 13 fiches restantes de `PLAN-DOCUMENTATION.md`, écrite avant qu'elles ne le soient** — c'est-à-dire au seul moment où elle coûte deux lignes. | `companion/PROGRAMME.md` §6 | ✅ Fait |
| **6** | ⚠️ **C7 — point de veille ouvert par la correction elle-même** | `PLAN.md` §1 porte maintenant *« le point qui commande maintenant : le premier `ppc_minutes` non nul »*. **La formulation est juste à cet endroit** — `PLAN.md` mesure ce que le **dispositif** a produit, et il portait déjà *« tant qu'il est à 0, aucune quantité de doctrine produite ne compte comme un progrès »*. 🔴 **Mais elle ne doit jamais migrer vers une surface que Xavier ouvre** : dans Kokoro ou dans une fiche, la même phrase devient un reproche d'assiduité. **Signalé avant que ça n'arrive, pas après.** | `PLAN.md` §1 vs `PROGRAMME.md` §7 | ⚠️ Veille |

---

## Ce qui tient

**Effectivement revérifié après correction :**

- **Les corrections n'ont pas touché au contenu clinique.** Six paliers, 5 min / 300 s, critères d'arrêt, conduite du voyage : identiques à la v3, où ils avaient été comparés au `ppc-desensibilisation.md` §4 et §5.
- **Aucun critère de passage n'est réapparu dans la fiche** à la faveur de la réécriture — c'était le risque de la manœuvre, puisqu'elle rouvrait précisément la section qui parle de progression. **La phrase ajoutée renvoie à la liste du jour, elle ne compte rien.**
- **Les 12 étapes du programme sont inchangées entre v3 et v4** — seuls `version`, `publie_le` et `supervision` ont bougé. **Aucun `id` réutilisé, aucune étape ajoutée en passant sous couvert de correction.**
- **`fiche-chourouk.md` n'a pas été touchée** par cette passe et reste dans l'état validé à la v3.

---

## Objections de fond

**Aucune nouvelle.** L'objection **O1** de la v3 *(les deux surfaces ne portent pas la même information)* est **close par l'écriture de la règle** en `PROGRAMME.md` §6.

L'objection **O2** *(« Ok » comme signal d'arbitrage)* **reste ouverte, et c'est normal** : elle appelle une habitude d'écriture, pas une correction. **Le test sera la prochaine séance** — si un arbitrage court y est de nouveau enregistré comme une décision de Xavier sans que le mécanisme soit écrit, c'est que la correction du 18/08 était ponctuelle et pas apprise. **À recontrôler explicitement à la passe suivante.**

---

## Arbitrages demandés

**Aucun.** Les trois de la v3 ont été rendus et exécutés.

> **Ce document constate. Il ne corrige rien, et il ne publie rien.**
