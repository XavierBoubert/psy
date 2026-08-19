---
date: 2026-08-19
porte_sur: programme
version: 8
verdict: publiable
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v8

**Périmètre :** deux choses, et rien d'autre. **Quatre démarches PPC sortent du programme** *(`ppc-prise-en-charge`, `ppc-consultation-roisman`, `ppc-voyage`, `ppc-releve`)* — c'est l'exécution des arbitrages restés ouverts depuis la v6. Et **le panneau de la séance à deux est repris après son premier essai**, ce qui modifie `essai-a-deux` et le contrat lui-même.

**Le programme passe de 17 à 13 étapes.** La bibliothèque et les bilans ne changent pas et n'ont pas été relus ici.

> **Ce rôle supervise Claude, pas Xavier.**

---

## ✅ Verdict : publiable

**Quatre constats, aucun bloquant.** Trois portent sur ce que la reprise du panneau déplace ; un ferme les arbitrages de la v6. ⭐ **Les trois constats ouverts depuis la v6 sont clos par cette publication.**

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | ✅ **C8 — les trois arbitrages ouverts depuis la v6 sont exécutés** | Xavier a tranché le 19/08 : les démarches revenues le 19/08 à 00h53 sont **closes**, et `ppc-releve` sort **sans avoir été faite** — elle n'est exigible qu'au palier 3 *(`PLAN.md` §1)*. Les quatre étapes sont retirées du programme, **dans une publication `--seance`**, ce qui est la seule voie prévue. `PLAN.md` §1 ne porte plus la prise en charge du masque comme ouverte, et `etat.md` v1.30 enregistre la sortie. ⭐ **Le téléphone et le dépôt disent de nouveau la même chose** — c'est la contradiction ouverte ce matin qui se referme. | `programme.json` v8 · `PLAN.md` §1 · `etat.md` v1.30 | ✅ Clos |
| **2** | 🔴 **C10 — le rappel permanent du signal d'arrêt est remplacé, pas supprimé** | La v7 affichait `signal_arret` sous chaque vue et offrait un bouton *« Quand s'arrêter »*. **Xavier a arbitré le 19/08 que les deux encombraient chaque consigne.** Le signal et les trois critères sont désormais **une case à cocher de l'écran *avant*, et le déroulé ne s'ouvre pas tant qu'elle n'est pas cochée.** ⭐ **Ce qui est gagné est vérifiable, ce qui est perdu aussi** : cocher **prouve** que les critères ont été lus, ce qu'un bouton facultatif ne prouvait pas ; en contrepartie, **plus rien ne les rappelle pendant la séquence**, et l'aidant doit s'en souvenir. **Le bouton *On s'arrête* reste sur chaque consigne** — l'acte d'arrêter n'a jamais été à plus d'un tap et ne l'est toujours pas. ⚠️ **C'est un arbitrage de Xavier sur un invariant qu'il a lui-même posé** : il est exécuté, `PROGRAMME.md` §3 est réécrit en conséquence, et **le motif est consigné pour que le suivant ne le défasse pas sans savoir ce qu'il défait.** | `monde/ContenuSeanceDuo.kt` · `companion/PROGRAMME.md` §3 | ⚠️ Arbitré, consigné |
| **3** | ⭐ **C3 — `pour` cesse d'être affiché et devient un champ contrôlé, non rendu** | Les étiquettes *« À faire »* / *« À lire à voix haute, mot pour mot »* disparaissent : elles appartiennent à la consigne. **Conséquence à ne pas laisser implicite : une consigne destinée à Xavier qui ne dit pas « lis à voix haute » ne sera lue par personne.** `essai-a-deux` est corrigée en ce sens *(ses deux consignes `patient` portent désormais la formule)*, et `PROGRAMME.md` §3 en fait une règle d'écriture. **`pour` reste obligatoire et vérifié** au dépôt comme à la lecture — une valeur inconnue écarte toujours l'étape — **mais plus rien à l'écran ne le trahit.** ⚠️ **C'est un piège pour la prochaine `seance-duo` écrite**, et c'est pour ça qu'il est nommé ici plutôt que dans un commentaire de code. | `programme.json` · `companion/PROGRAMME.md` §3 | ⚠️ Veille à l'écriture |
| **4** | ⭐ **C3 — le critère de fin de K6 devient observable, comme arbitré** | Constat n° 2 de la v7 : `reponses/` ne distingue pas un entraînement mené d'un entraînement interrompu. **Xavier a tranché : le constat se lit sur Kokoro, pas en séance.** L'accueil de la séance affiche désormais *« L'entraînement a été mené jusqu'au bout. »* dès que la séquence est allée à sa dernière ligne. ⭐ **Ce n'est ni un compteur ni une progression** — c'est un état, du même registre que *« Déjà fait »* sur une démarche. ⚠️ **Il reste porté par la mémoire locale de l'app** *(constat n° 3 de la v7, inchangé)* : une réinstallation l'efface, et le sens de la défaillance reste le bon. | `monde/ContenuSeanceDuo.kt` · `PLAN.md` §2 K6 | ⚠️ Veille |

---

## Ce qui tient

**Effectivement relu, ligne à ligne.**

**Les quatre retraits :** aucune des quatre étapes retirées n'est référencée ailleurs dans le programme, et **aucun `id` n'est réutilisé**. Les réponses déjà revenues *(`reponses/2026-08-19-0053-*.json`)* restent au dossier — **retirer une étape n'efface pas ce qui a été fait**. `email-isorni` reste la seule démarche affichée, et elle est toujours ouverte au `PLAN.md` §1.

**C10 — les garde-fous du type, revérifiés sur la v8 :**

- **`signal_arret` est toujours présent, non vide, et recopié tel quel** — et il est **affiché**, en tête de la case des critères. Il n'est pas devenu un champ mort.
- **Les trois critères d'arrêt sont lus avant d'entrer**, et le bouton *On commence* reste inactif tant que toutes les cases ne sont pas cochées — **vérifié dans le code** : `tout = coches.size == etape.avant.size + 1`, la case des critères étant comptée.
- 🔴 **Le dernier critère reste « tu ne sais pas quoi faire → on s'arrête »**, vérifié deux fois — au dépôt et à la lecture. Une séance qui ne le porte pas est toujours écartée. **Testé.**
- **`entrainement_requis: true`**, l'entraînement reste proposé en premier tant qu'il n'a pas été mené.
- **Aucune consigne ne demande un jugement à l'aidant**, et **aucune ne demande de réponse gestuelle de la main** : le « non » de la main n'apparaît que dans `signal_arret` et dans `avant`, convenu à froid.
- **Aucun diagnostic, score, hypothèse ou compte rendu** dans les textes adressés à Chourouk.

**Le contrat suit le code, et non l'inverse.** `PROGRAMME.md` §3 est réécrit sur les trois points qui ont bougé — `signal_arret`, `sequence`, `arret` — **y compris son exemple JSON**, dont la ligne *« Relis les critères d'arrêt — bouton en bas »* décrivait un bouton qui n'existe plus. ⭐ **Un contrat qui décrit une interface disparue est exactement le mode de défaillance C3**, et il se serait propagé à la première `seance-duo` clinique.

**Le format, relu :** 13 étapes, `id` uniques, `essai-a-deux` conforme *(trois lignes `avant`, quatre consignes, trois critères, `sortie_libre: true`)*. **Le schéma de `psy-publish` accepte le fichier**, et **Kokoro lit les treize étapes sans en écarter aucune** *(`ProgrammePublieTest`, réellement exécuté depuis la réparation de ce matin)*. **182 tests passent.**

**C4 — aucune dérive R6.** Rien dans la v8 ne demande d'introspecter un état interne.

**C2 — aucun fait périmé introduit.** Les étapes conservées sont identiques à la v7.

**C7 — cette publication retire plus qu'elle n'ajoute.** Quatre lignes quittent l'écran de Xavier, une seule reste à jouer. ⭐ **C'est le premier allègement du programme depuis son ouverture** — et une reprise d'interface faite **après un essai réel**, pas avant.

**C9 sans objet** — aucune fiche modifiée. ⚠️ **`fiche-chourouk` reste dans sa version publiée du 18/08 alors qu'une plus récente existe** *(`PLAN.md` §2.1)* : inchangé, et toujours à savoir avant de faire jouer une séance à deux.

---

## Objections de fond

**Une, et c'est la même qu'à la v7 — elle n'a pas été traitée, elle a été datée.**

🔴 **Chourouk n'a toujours rien reçu d'écrit, et la v8 rend le déroulé plus dépendant d'elle, pas moins.** Les critères d'arrêt ne sont plus sous ses yeux pendant la séquence : ils sont **lus une fois, cochés, puis rangés**. ⭐ **Cela déplace la charge de l'écran vers sa mémoire** — ce qui est tenable sur un essai de trois minutes, et beaucoup moins sur une stabilisation de vingt-deux.

**Ce n'est pas un motif de refus :** la case cochée est une garantie que le bouton n'apportait pas, l'essai est court, et **Xavier a répondu « oui » à la transmission de la fiche, dans une autre session.** ⭐ **La fiche cesse donc d'être une bonne pratique et devient une dépendance de conception** : c'est elle qui portera, hors écran, ce que l'écran ne rappelle plus.

**Formulé net : la v8 est bonne pour l'essai, et elle ne doit pas partir en clinique avant la fiche.**

---

## Arbitrages demandés

| # | Question fermée | Ce qui en dépend |
|---|---|---|
| **1** | **Une `seance-duo` clinique peut-elle partir avant que la fiche à Chourouk soit transmise, oui ou non ?** | Objection de fond. ⭐ **La question a changé de nature depuis la v7** : les critères ne sont plus rappelés pendant la séquence, donc la fiche porte désormais quelque chose que l'écran ne porte plus |
| **2** | *(reconduite de la v7)* **`essai-a-deux` sort du programme quand tu valides l'essai comme concluant** — **est-ce à la prochaine clôture de séance, ou dès que tu le dis ?** | Une ligne non clinique sur l'écran *quand j'en ai besoin*. La règle veut une clôture de séance ; il faut savoir si tu veux l'attendre |
