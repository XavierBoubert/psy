---
date: 2026-08-15
perimetre: depuis 2026-08-09 — companion/PRESENCE.md, étapes E12 à E14
constats: 6
bloquants: 2
---

# Supervision — 15/08/2026

**Passe demandée sur un périmètre borné :** l'étape **E13** de [`companion/PRESENCE.md`](../../companion/PRESENCE.md) — *l'entrée du personnage Kokoro dans une surface de crise* —, déclarée **bloquante** par le §7.1 du même document. Le contexte immédiat (E12 livrée le jour même, E14 à venir) est examiné avec, parce qu'il déplace la même règle.

> **Rappel de ce que ce rôle supervise :** Claude, pas Xavier. **Aucune ligne de ce document ne porte sur l'observance, l'assiduité ou les chiffres de Xavier.**

---

## 🔴 Réponse à la question posée

**E13 ne passe pas en l'état.** Trois raisons, aucune n'étant *« un personnage sur un écran de crise est impensable »* :

1. La règle qu'elle dérogerait a été **réaffirmée la veille**, dans le même document, en connaissance de cause — et **E13 n'oppose aucun motif clinique**, seulement un plan.
2. La posture qu'elle installe est décrite avec **le mot que `CORPS.md` §8 interdit nommément**.
3. Elle ne dit pas **laquelle des deux portes de crise** elle vise, et l'appliquer à une seule défait un arbitrage de Xavier du 15/08.

**Le point 1 n'est pas réparable par une correction d'écriture : il appartient à Xavier.** L'écran de crise a été dépouillé sur ses demandes ; le superviseur n'a pas qualité pour le redécorer. **Arbitrage A1.**

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | **C1 — dérogation sans motif opposé** | `CORPS.md` §10 : *« L'écran de crise ne porte aucun personnage. Deux boutons, rien d'autre — **un compagnon décoratif au pire moment est du bruit** »*. Le §10.1 point 3, écrit à l'occasion de la dérogation de la notification, **réaffirme l'interdit dans les mêmes termes** et pose la borne en toutes lettres : *« Ce qui est admis sur une porte ne l'est pas derrière »*. ⭐ **E13 ne cite ni l'un ni l'autre et n'oppose aucun argument clinique** — `PRESENCE.md` §5 se contente d'annoncer « dérogation bornée ». **Une dérogation qui ne répond pas au motif de l'interdit ne l'a pas levé, elle l'a contourné.** | `CORPS.md` §10 et §10.1 pt 3 vs `PRESENCE.md` §5 et §6 E13 | 🔴 **Bloquant** |
| **2** | **C1 — la posture porte un mot interdit** | `PRESENCE.md` §2 décrit `attente` : *« **Affaissé**, bras posés sur le bouton »*. `CORPS.md` §8 point 3 interdit : *« **S'affaisser**, baisser la tête, se recroqueviller, laisser tomber les bras. Aucune posture de découragement, jamais »*. ⚠️ **Le code, lui, ne s'affaisse pas** — `Posture.Attente` ne pose que `OUVERTURE_POSEE = 12°` aux deux bras, sans inclinaison ni tête baissée. **Le document décrit donc une posture que le code ne fait pas et que la doctrine interdit** : c'est le mot qui est faux, ou l'intention. | `PRESENCE.md` §2 vs `CORPS.md` §8.3 ; `corps/Posture.kt` | 🔴 **Bloquant** |
| **3** | **C3 — la cible n'est pas nommée** | Deux surfaces portent l'écran de crise : celle du **monde** (`Ecran.CRISE`, `monde/Bords.kt`) et celle **ouverte hors du monde** (`crise/CriseActivity.kt`), et `INTERFACE.md` §6.2 impose depuis le 15/08 *(demande de Xavier)* qu'elles affichent **le même contenu, à la lettre** — motif écrit : *« deux écrans qui font la même chose et ne se ressemblent pas obligent à vérifier lequel on a sous les yeux, au moment précis où on n'a rien à vérifier »*. ⭐ **`PRESENCE.md` §2 vise la première ; la liste de fichiers de E13 vise la seconde.** Exécutée telle quelle, E13 met un personnage sur une porte et pas sur l'autre. | `PRESENCE.md` §2 vs §6 E13 ; `INTERFACE.md` §6.2 | ⚠️ Moyenne — **bloquante si E13 est exécutée** |
| **4** | **C3 — invariant déclaré, non câblé** | *« Annoncer avant de faire »* est l'une des huit contraintes non négociables (`profil.md` §5, rigidité / intolérance au changement) et `CORPS.md` §8 point 6 l'écrit pour l'apparence. `PRESENCE.md` §7.2 la déclare **due depuis E9** — *« maintenant elle est due : le changement ne vit plus dans l'atelier, il est sur les écrans que Xavier ouvre »*. **Elle n'a pas eu lieu**, et **E12 vient d'étendre le changement au check-in et aux réglages** — deux surfaces que Xavier ouvre sans passer par le monde. | `PRESENCE.md` §7.2 ; `journal/ContenuJournal.kt`, `MainActivity.kt` | 🔴 **Bloquant, et il précède E13** |
| **5** | **C2 — datation en avant** | Trois sections portent la date du **16/08/2026** — `CORPS.md` §10.1, `INTERFACE.md` §7.5 et §7.6 — dans des fichiers dont **tous les commits sont datés du 15/08/2026** (`git log --date=short`). **La datation est l'instrument principal de ce rôle** : elle sert à savoir ce qui a été écrit *avant* un fait qui l'a depuis contredit. Une date en avant d'un jour ne trompe personne aujourd'hui et fausse l'ordre de lecture dans un mois. | `CORPS.md` §10.1 · `INTERFACE.md` §7.5, §7.6 | Faible — mais sur l'outil du contrôle |
| **6** | **C7 — prolifération** | `PRESENCE.md` planifie **14 étapes** d'animation ; **E1 → E12 sont faites en une journée**. `PLAN-KOKORO.md` §9-A — rappelé **à chacun des sept jalons K** dans `etat.md` §7 — pose que *« le développement passe APRÈS le palier 0 PPC et le brief, jamais à leur place »*. Le palier 0 PPC est ouvert depuis le 09/08 ; le brief du Dr Isorni est dû au week-end du **29-30/08** pour une consultation le 03/09. **Le constat n° 4 de la passe du 09/08 n'a pas bougé de nature** — le dispositif produit sa doctrine plus vite qu'il ne l'exécute. | `companion/PRESENCE.md` §6 vs `PLAN-KOKORO.md` §9-A | ⚠️ Moyenne — **structurelle, deuxième signalement** |

---

## Ce qui tient

Vérifié dans le code et dans les tests, ce jour — à ne pas recontrôler sans raison :

- 🔴 **Le verrou de E13 a tenu.** `place(Ecran.CRISE, …)` vaut `null` pour toutes les heures, les deux états du check-in et toutes les listes vides ; `HabitantTest` nomme la raison — *« Kokoro est entré dans une surface de crise sans supervision »*. **Le personnage n'a pas été mis là avant cette passe**, ce qui est exactement le comportement voulu.
- ⭐ **L'extension involontaire n'a pas eu lieu.** Les trois pages de crise (mot-code, tension, phrase) partagent le composable `PageKokoro` avec le check-in et les réglages ; le paramètre `locuteur` y est **`null` par défaut**, et les trois pages ne le renseignent pas. C'est le point précis où *« aucune extension aux autres surfaces de crise »* pouvait tomber par effet de bord — il n'est pas tombé.
- **L'alternance des deux régimes est câblée, pas déclarée.** `locuteurEnScene` / `habitantEnScene` sont deux moitiés d'une même bascule ; un test parcourt les 101 valeurs et refuse à la fois *deux personnages* et *aucun*. L'invariant *« une seule instance à l'écran »* (`CORPS.md` §8.8) cesse d'être une intention.
- **Rien de neuf ne bat.** Le locuteur ne porte ni vol ni ombre ; le corps n'a toujours qu'une horloge (`HORLOGE_MILLIS`), donc aucun second rythme ne peut apparaître dans le champ.
- **C6 — aucun chiffre fabriqué.** Les trois valeurs du §1.4 (60 dp, ≈ 110 dp, contour rendu) sont désormais dérivées du dessin et vérifiées par un test, au lieu d'être recopiées. Un écart de 11 % — la vue mesurée à la place du personnage — a été trouvé et corrigé plutôt que laissé.
- **Aucun numéro d'appel d'urgence n'est réapparu** dans les surfaces touchées ; `InvariantsTextesTest` et `InvariantsSourcesTest` passent (121 tests).

---

## Objections de fond

### O1 — Sur cet écran-là, « ne porte aucune information » **est** la définition de la décoration

`PRESENCE.md` §2 pose lui-même, pour toutes les poses : *« **Aucune de ces poses ne porte d'information.** Ne pas les reconnaître ne fait rien perdre. »* C'est une bonne règle — et sur l'écran de crise elle devient l'argument contraire à E13 : ce qu'on ajouterait est, **de l'aveu du document**, sans information. `CORPS.md` §10 refuse exactement cet objet : *« un compagnon décoratif au pire moment est du bruit »*, et `INTERFACE.md` §4.5 a fait de cet écran **le moins décoré du dispositif**, à dessein — *« en crise, la mignonnerie est du bruit »*.

**Le contre-argument existe et il faut le dire entier**, parce qu'il est sérieux : la posture `cote-a-cote` (`CORPS.md` §7 n° 4) légitime déjà une **présence muette, panneau éteint**, dont la phrase est *« je reste là pendant que tu fais ça »* — et une présence n'est pas une décoration. **Mais elle est admise *pendant un exercice*, pas sur l'écran d'entrée de la crise**, et la différence n'est pas de degré : sur l'écran d'entrée, il n'y a rien à faire ensemble, il y a trois boutons à trouver vite.

⚠️ **Un fait de dossier qui pèse dans les deux sens, et qu'aucun des deux camps ne peut invoquer seul :** les hypersensibilités portent nommément sur quatre canaux dont le **visuel**, et le profil impose *« pas de flash, pas d'animation brusque »*. E13 y répond par l'immobilité complète — c'est la bonne réponse à cette objection-là. **Elle ne répond pas à l'autre** : un élément de plus à balayer des yeux sur l'écran qu'on ouvre quand tout est déjà trop.

### O2 — L'arbitrage appartient à Xavier, et la supervision n'a pas qualité pour le rendre

L'écran de crise est dans son état actuel **par trois décisions de Xavier**, pas par un choix d'ingénierie : le retrait des numéros d'urgence (10/08), *« les mêmes boutons sur les deux portes »* (15/08), et l'écart assumé du §4.5. **Rendre un personnage à cet écran défait, au moins en partie, le mouvement de ces trois décisions.** Le §3 du présent rôle est explicite : le protocole de crise ne s'affaiblit jamais du fait d'une supervision, et les décisions de Xavier ne se rouvrent pas sans fait nouveau. **Il n'y a ici aucun fait nouveau** — E13 est une proposition du dispositif à lui-même.

### O3 — Effet miroir : le compte s'est amélioré, et il faut le dire

La passe du 09/08 relevait **une seule** objection substantielle tracée. Recensement au 15/08 : l'**EMDR** (08/08) · le **MAIA ne peut pas trancher seul** (09/08) · le **transport par Drive** (11/08, maintenu par Xavier, contre-argument conservé entier) · l'**élargissement du périmètre Drive à la bibliothèque** (12-13/08, arbitré « c'est assumé », contre-argument conservé). ⭐ **Trois de ces quatre portent contre une décision de Xavier et ont survécu à son arbitrage sans être effacées** — c'est la forme utile de la non-complaisance, et elle s'est installée.

**Le miroir a changé de sens, pas disparu.** Ici, le dispositif ne dit pas oui à Xavier : **il s'autorise à lui-même une dérogation que personne ne lui a demandée**, contre une règle qu'il a écrite lui-même et réaffirmée la veille. Le dossier ne trace aucune demande de Xavier concernant un personnage sur l'écran de crise. **C'est le même défaut d'un cran plus loin : une autorité qui se délivre son propre permis.**

---

## Arbitrages demandés

| # | Question | Recommandation |
|---|---|---|
| **A1** | **Kokoro entre-t-il sur l'écran de crise ?** *(oui / non)* | **Non, en l'état.** Le motif de l'interdit — *un compagnon décoratif au pire moment est du bruit* — n'a reçu aucune réponse, et l'écran a été dépouillé sur tes propres demandes. **Si la réponse est oui, elle doit venir de toi et pour un motif écrit**, pas d'un plan d'animation. |
| **A2** | **Si oui : sur les deux portes de crise à la fois ?** *(les deux / celle du monde seulement)* | **Les deux, sans quoi `INTERFACE.md` §6.2 tombe.** Une porte avec personnage et une sans, c'est exactement le *« lequel ai-je sous les yeux ? »* que le partage des boutons a supprimé le 15/08. |
| **A3** | 🔴 **L'annonce du changement d'apparence (table du §5) est-elle faite maintenant, avant la séance du 16/08 ?** *(oui / non)* | **Oui, et avant toute nouvelle pose sur le téléphone.** Le changement est déjà sur trois surfaces que tu ouvres — le monde, le check-in, les réglages. **C'est le constat le plus urgent de cette passe, et il ne dépend pas de E13.** |
| **A4** | **E14 — l'écriture documentaire attend-elle E13 ?** | **Non, sauf pour `CORPS.md` §10, qui dépend de A1.** E14 ne construit rien : il **retire** de la doctrine en double (une seule source par règle), c'est le seul des trois qui *réduit* la surface, et rien n'y dépend de l'écran de crise — hors la ligne du §10 qui le concerne, **qui reste inchangée tant que A1 n'est pas rendu**. |

> **Ce document constate. Il ne corrige rien.** La correction est un acte séparé — un audit qui répare ce qu'il trouve ne laisse aucune trace de ce qui n'allait pas.

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 15/08/2026 | Deuxième supervision. Périmètre borné à `PRESENCE.md` E12→E14. **6 constats, 2 bloquants**, 3 objections de fond, 4 arbitrages. **E13 refusée en l'état** ; le blocage porte sur le §7.1 de `PRESENCE.md`. |
