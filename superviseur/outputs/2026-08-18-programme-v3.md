---
date: 2026-08-18
porte_sur: programme
version: 3
verdict: refuse
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v3

**Périmètre :** `companion/inputs/programme.json` **v3**, les deux fiches de `companion/inputs/bibliotheque/` *(`fiche-chourouk.md`, `ppc-les-paliers.md`)*, lues **contre** la séance du 18/08/2026 et `etat.md` v1.27.

> **Ce rôle supervise Claude, pas Xavier. Aucune ligne ne porte sur son observance, son assiduité ou ses chiffres.**

---

## 🔴 Verdict : refuse

**Un constat bloquant, et il porte sur la fiche écrite aujourd'hui.** Il est peu coûteux à corriger, et il n'a rien à voir avec le contenu clinique du palier — qui tient.

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | 🔴 **C8 — une fiche qui périme toute seule, dans une surface sans intermédiaire** | `ppc-les-paliers.md` porte une section **`## Où tu en es`** qui affirme : *« Première étape : le masque tenu à la main contre le visage »*. **Cette phrase devient fausse au premier passage de palier.** Or une fiche est publiée en **PDF figé** *(`PLAN-DOCUMENTATION.md` §2)* et `psy:publish` ne reconvertit que ce qui a changé : **rien ne force sa réécriture quand le palier bouge.** ⭐ **La position courante est déjà portée par le programme** — la carte `ppc-palier-1`, republiée à chaque séance. **Deux surfaces porteraient la même information, une vivante et une figée**, et c'est la figée que Xavier ouvrira pour comprendre. C'est exactement C8 : une copie qui périme, sur l'écran où plus personne n'est là pour objecter. | `companion/inputs/bibliotheque/ppc-les-paliers.md` §*Où tu en es* vs `programme.json` étape `ppc-palier-1` | 🔴 **Bloquant** |
| **2** | 🔴 **C5 — un arbitrage attribué à Xavier repose sur « Ok »** | La question posée était **fermée à trois branches** : *« Palier d'entrée : 1, 2 ou 3 ? »*, la branche **1** étant la recommandation de Claude. **La réponse a été « Ok ».** Le compte-rendu inscrit pourtant *« Palier d'entrée : 1 — **Qui tranche : Xavier** »* et le frontmatter `palier_atteint: 1`. ⭐ **« Ok » à une question à trois branches n'est pas une sélection, c'est un acquiescement à la recommandation** — c'est-à-dire le contenu de Claude, réétiqueté comme décision de Xavier. **C'est le point 3 du §2 : la déclaration reprise qui devient un fait.** ⚠️ **Une fenêtre de correction explicite a bien été ouverte** *(« si tu voulais 2 ou 3, dis-le maintenant »)* et n'a pas été utilisée : **la ratification est réelle, l'attribution est inflatée.** La correction porte sur le compte-rendu, pas sur le palier. | `seances/2026-08-18-seance.md` § *Décisions* n° 2 et frontmatter | ⚠️ Moyenne |
| **3** | **C2 — fait périmé dans le document réécrit aujourd'hui** | `etat.md` §1, ligne *Étape du PLAN* : *« 🔴 Étape 1 — Axe D, ouverte (protocoles écrits, **exécution suspendue à des démarches réelles**) »*. **Le palier 1 a démarré le jour même**, et la ligne *Palier en cours* du même tableau le dit. **Deux lignes du même tableau se contredisent**, dans la version 1.27 écrite ce jour. | `psy/outputs/dossier/etat.md` §1 | ⚠️ Moyenne |
| **4** | **C2 / C8 — `PLAN.md` non mis à jour par la séance** | `PLAN.md` §1 porte encore `[ ] **Essayer le nouveau masque bouche + nez**` et *« 🔴 Le point qui commande maintenant : reprendre la désensibilisation avec le nouveau masque »* — **tranché aujourd'hui**. §2 Étape 5 porte encore *« K5 — ✅ Moitié PC écrite ; **Kokoro ne lit pas encore** »*, alors que `kokoro/programme/Bibliotheque.kt` lit `programme.json` et que `LecteurPdf.kt` ouvre les documents. **Deux faits périmés dans le document qui sert à décider quoi faire ensuite.** | `PLAN.md` §1 et §2 Étape 5 | Faible — hors surface publiée |
| **5** | **C7 — le compte reste ouvert, mais la passe ne l'aggrave pas** | Troisième signalement depuis le 09/08. ⭐ **Différence de nature à acter :** cette séance n'a pas produit une fiche de doctrine de plus — elle a produit **un `exercice` au minuteur sur le téléphone** et a **retiré deux étapes** du programme. **Deux fiches sur les quinze de `PLAN-DOCUMENTATION.md` sont écrites, et celle d'aujourd'hui est appelée par une étape qui existe.** C'est la première passe où le dispositif retire plus de doctrine qu'il n'en ajoute. | `programme.json` v2 → v3 · `PLAN-DOCUMENTATION.md` §1 | ⚠️ Signalé, non aggravé |

---

## Ce qui tient

**Effectivement relu ce jour, ligne à ligne — à ne pas recontrôler sans raison.**

- 🔴 **C9 sur `ppc-les-paliers.md` : la fiche n'est pas une copie.** Comparée phrase à phrase à `protocoles/ppc-desensibilisation.md`. **Aucun diagnostic, aucun pronostic, aucune hypothèse présentée comme un fait, aucune réserve destinée au Dr Isorni.** Le jargon est traduit et non recopié — *otalgie* → « mal à l'oreille », *lésion cutanée* → « marque sur la peau ». Les deux seuls praticiens nommés — **Link Sommeil** et **Dr Roisman** — sont ceux qu'il consulte, et ils le sont là où la fiche lui dit **de ne pas chercher à s'en sortir seul**.
- ⭐ **C4 : aucun critère de passage n'est écrit dans la fiche, et c'est délibéré.** Le critère réel est *« trois jours consécutifs au bout du minuteur »* — l'écrire sur l'écran de Xavier aurait été **un compteur de régularité**, interdit n° 3. La fiche dit à la place *« tu ne montes jamais d'un cran de ta propre initiative »* et renvoie le comptage à la séance, ce qui est la conduite exacte du `ppc-desensibilisation.md` §4. **Le contournement de l'interdit par un synonyme n'a pas eu lieu.**
- **C4 bis : aucune question intéroceptive dans la nouvelle étape.** La `consigne` de `ppc-palier-1` décrit une position et un minuteur ; elle ne demande ni confort, ni ressenti, ni « es-tu prêt ». `sortie_libre: true`.
- 🔴 **C10 sur `fiche-chourouk.md`** *(relue intégralement, publiée ce matin)* **: elle ne lui apprend aucun diagnostic** — le mot *autisme* n'y figure pas, le shutdown est décrit par ce qu'il fait, pas par ce qu'il nomme. **Elle ne lui demande aucun jugement**, et le dit en toutes lettres sur le seul point où la tentation existait : *« ce n'est pas à vous de trancher si c'est grave — ce n'est le rôle de personne dans l'entourage »*. **Aucun score, aucun compte rendu, aucune hypothèse.**
- **Aucun numéro d'appel d'urgence dans les deux fiches ni dans le programme, 3114 compris.** Sur la perte de connaissance, `fiche-chourouk.md` dit *« allongez-le, jambes surélevées »* **avant** le recours médical — l'ordre correct pour un vasovagal, et celui que le retrait des numéros du 10/08 visait précisément à préserver.
- **C6 : aucun chiffre fabriqué.** 5 min / 300 s, et les six paliers, sont repris du tableau §4 de la fiche clinique. **Aucun chiffre de télésuivi n'a été inventé pour combler son absence** — c'est le point où la tentation était réelle, la règle d'entrée dans l'échelle exigeant un relevé qui n'existe pas. Le manque a été traité par un **argument écrit** *(l'interface a changé, donc rien ne se crédite)*, pas par une estimation.
- ⭐ **C5, versant favorable, à porter au compte :** trois objections tracées dans une seule séance, dont **une contre une décision déjà prise par Xavier** *(le masque bouche + nez augmente le contact facial — conservée entière, arbitrage rendu contre elle)*, une **soulevée par Claude contre son propre plan** *(le calendrier des 20 jours)*, et **un refus de publier une fiche** que rien n'obligeait à refuser *(`ppc-pourquoi-maintenant` : le chiffre se dit une fois, une fiche le répète)*. **Le compte du 15/08 continue de s'améliorer.**
- **C3 : l'annonce du changement d'apparence a eu lieu le 18/08 et Xavier l'a validée.** Le constat 4 bloquant de la passe du 15/08 est levé ; `PRESENCE.md` §7 en porte la trace.
- **C8, versant favorable :** les deux étapes retirées — `ppc-origine-fuite`, `ppc-interfaces` — l'ont été **parce que le dossier les dit closes**, pas parce qu'elles gênaient. C'est le sens du contrôle, dans le bon sens.

---

## Objections de fond

### O1 — La fiche et le programme ne doivent pas porter la même information

C'est le fond du constat 1, et il vaut au-delà de cette fiche. **Le dispositif vient de se doter de deux surfaces de contenu à durées de vie différentes** : `programme.json`, republié à chaque séance, et la **bibliothèque en PDF**, écrite une fois et convertie une fois.

**La règle qui manque, et qu'il faudra écrire :** *ce qui change au rythme des séances vit dans le programme ; ce qui ne change pas vit dans la bibliothèque.* Une fiche porte **l'échelle, les règles, ce qui n'est pas de l'anxiété, la conduite du voyage** — tout ça est stable. Elle ne porte **jamais où il en est**, parce que ça bouge et qu'elle, non.

⚠️ **Ce n'est pas un détail d'écriture : c'est le mode de défaillance C8 en train de se recréer une deuxième fois**, sur une surface neuve, exactement comme il s'est créé le 12/08 sur le programme.

### O2 — « Ok » est un signal dont ce dispositif va devoir décider quoi faire

Le constat 2 n'est pas un incident isolé, c'est un **motif appelé à se répéter** : Xavier répond court, c'est documenté et légitime *(shutdowns, zéro exigence de performance, refuser doit coûter zéro justification)*. **Un dispositif qui exige une réponse bien formée pour enregistrer un arbitrage lui demanderait une performance** — donc l'inverse de ce qu'il doit faire.

**Mais l'inverse est pire :** compter chaque « ok » comme un arbitrage de Xavier revient à faire signer par lui des décisions qui sont celles de Claude, et **c'est l'effet miroir dans sa forme la plus difficile à voir**, puisque la trace écrite dit le contraire.

**La sortie n'est pas de poser plus de questions** — c'est d'**écrire ce qui s'est réellement passé** : *« recommandé par Claude, ratifié par Xavier sans objection après fenêtre de correction explicite »* est vrai, vérifiable, et ne coûte rien à personne. **Une décision de Claude ratifiée reste une décision de Claude**, et le dossier doit pouvoir le relire dans six mois.

---

## Arbitrages demandés

| # | Question | Recommandation |
|---|---|---|
| **A1** | **La fiche `ppc-les-paliers` cesse-t-elle de nommer le palier courant ?** *(oui / non)* | **Oui.** Garder l'échelle entière, les règles, les critères d'arrêt et le voyage — retirer *« première étape »* comme position courante, et renvoyer à la carte du jour. **C'est le blocage, et c'est la seule chose qui empêche la publication.** |
| **A2** | **Le compte-rendu réétiquette-t-il la décision n° 2 en « recommandé par Claude, ratifié par Xavier » ?** *(oui / non)* | **Oui.** Le palier ne change pas ; seule l'attribution est corrigée. **Ne rien changer serait laisser une décision de Claude porter ta signature.** |
| **A3** | **Écrit-on la règle de partage programme / bibliothèque dans [`companion/PROGRAMME.md`](../../companion/PROGRAMME.md) ?** *(maintenant / plus tard)* | **Maintenant, en deux lignes.** Quinze fiches sont à écrire ; la règle coûte deux lignes aujourd'hui et quinze relectures dans un mois. |

> **Ce document constate. Il ne corrige rien, et il ne publie rien.** La correction est un acte séparé — un audit qui répare ce qu'il trouve ne laisse aucune trace de ce qui n'allait pas.
