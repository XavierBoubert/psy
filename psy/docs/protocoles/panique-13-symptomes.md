# Les 13 symptômes de l'attaque de panique — psychoéducation

**Statut :** fiche actionnable — v1.0 (09/08/2026) · **Étape 4, avancée pour le vol du 07/09/2026**
**Cible dossier :** `agoraphobie-exposition`
**Source clinique :** DSM-5 (critères de l'attaque de panique) · rapport v2.4 §6.2.b (agoraphobie), §9.14 (les trois mécanismes), §6.4.

> **Ce que cette fiche est.** Une liste à relire **à froid**, jusqu'à la connaître. Son effet thérapeutique ne vient pas de sa lecture pendant la crise — il vient de l'avoir lue **avant**.
> **Ce qu'elle n'est pas.** Une technique. Elle ne fait rien s'arrêter ; elle empêche d'interpréter une sensation comme une catastrophe.

---

## 0. Pourquoi cette liste change quelque chose

Le moteur du trouble n'est pas l'angoisse : c'est **la peur de la peur**. Une sensation corporelle survient, elle est interprétée comme un signe de danger (crise cardiaque, folie, étouffement, mort), et cette interprétation produit plus de sensations. La boucle s'auto-alimente.

**Savoir qu'une sensation figure sur une liste de treize la sort du registre du danger et la remet dans celui du symptôme connu.** C'est tout ce que fait cette fiche, et c'est beaucoup.

> ⭐ **Le point qui compte le plus pour Xavier : la dépersonnalisation et la déréalisation sont le symptôme n° 11.** Elles figurent au certificat du Dr Isorni, elles sont ce qui l'inquiète le plus, et elles ne sont **ni de la folie, ni un dommage cérébral, ni un trouble dissociatif**. Ce sont des symptômes d'attaque de panique, listés comme les douze autres.

---

## 1. Les 13 symptômes (DSM-5)

Une attaque de panique = **au moins 4** de ces symptômes, montée brutale, pic **en quelques minutes**.

| # | Symptôme |
|---|---|
| 1 | Palpitations, battements de cœur ou accélération du rythme cardiaque |
| 2 | Transpiration |
| 3 | Tremblements ou secousses musculaires |
| 4 | Sensation de souffle coupé ou impression d'étouffement |
| 5 | Sensation d'étranglement |
| 6 | Douleur ou gêne thoracique |
| 7 | Nausée ou gêne abdominale |
| 8 | Sensation de vertige, d'instabilité, de tête vide — **ou impression d'évanouissement** |
| 9 | Frissons ou bouffées de chaleur |
| 10 | Engourdissements ou picotements |
| **11** | ⭐ **Déréalisation** (sentiment d'irréalité) **ou dépersonnalisation** (être détaché de soi-même) |
| 12 | Peur de perdre le contrôle ou de devenir fou |
| 13 | Peur de mourir |

**Trois faits à retenir avec la liste :**
1. **Elle a une fin.** Le pic est atteint en quelques minutes, puis ça redescend. Une attaque de panique ne dure pas des heures — ce qui dure, c'est l'appréhension autour.
2. **Les symptômes 12 et 13 sont des symptômes**, pas des évaluations lucides de la situation. Avoir peur de mourir fait partie du tableau ; ce n'est pas une information sur le danger réel.
3. **Chez Xavier, elles sont *attendues*** — déclenchées par des situations identifiées (transports, foules, lieux clos, magasins), et non surgies de nulle part. **Le trouble panique est écarté.** Ce n'est pas un détail : ça veut dire que les situations sont connues, donc préparables.

---

## 2. 🔴 Le symptôme n° 8 et le piège du dossier

**« Impression d'évanouissement » n'est pas « évanouissement ».** C'est la confusion la plus coûteuse du dossier, et elle doit être défaite explicitement.

| | **Panique** | **Vasovagal** |
|---|---|---|
| Où | Transports, foules, lieux clos, avion | **Contexte médical uniquement** : aiguille, cathéter, geste invasif |
| La tension artérielle | **monte** | **chute** |
| Perte de connaissance | **Pratiquement jamais** — 20 ans de crises, **aucune syncope** | **Oui** — les 3 syncopes documentées, toutes en contexte de soins |
| Parade | Rester, respirer lentement, laisser passer | ⭐ **Tension appliquée** — contracter bras, jambes, tronc par salves de 10-15 s |
| Erreur à ne pas commettre | — | **Lui appliquer une respiration lente : elle abaisse encore la tension** |

> **Ce qu'il faut pouvoir se dire dans un avion :** « J'ai l'impression que je vais tomber. C'est le symptôme n° 8. Il n'y a pas d'aiguille ici, donc ce n'est pas un vasovagal. **En vingt ans, je ne suis jamais tombé en situation d'angoisse.** »
>
> **Et la règle qui en découle : dans un avion, on ne fait pas de tension appliquée.** Elle est faite pour l'autre mécanisme. En vol, la parade est de rester et de laisser redescendre.

---

## 3. Ce qu'on fait pendant — et ce qu'on ne fait pas

| On fait | On ne fait pas |
|---|---|
| **Nommer le symptôme par son numéro.** « C'est le 1 et le 11. » Nommer suffit — ça remet la sensation dans le connu. | **Chercher à faire disparaître la crise.** Vouloir l'arrêter la prolonge. |
| **Expiration plus longue que l'inspiration**, comptée. Inspirer 4, expirer 6. Un rythme externe, pas une sensation à surveiller. | **Respirer profondément et vite** — c'est de l'hyperventilation, ça aggrave les symptômes 4, 8 et 10. |
| **Rester** si la situation le permet. Le pic redescend. | **Se surveiller le pouls, la gorge, la poitrine.** L'auto-surveillance entretient la boucle. |
| Contact avec quelque chose de concret : appui des pieds, mains sur les accoudoirs, température d'une bouteille. | ⛔ **Toute technique de visualisation** — « imagine un endroit calme » est **inopérant** ici, ce n'est pas une question d'effort. |
| Prévenir Chourouk par le **mot-code** si le canal verbal se coupe. | Se forcer à parler pour rassurer les autres. |

---

## 4. Ce qui se trace au dossier

Un épisode → `crises/AAAA-MM-JJ-HHMM-panique.json` (cf. `PLAN.md` §7.5). Champs qui comptent ici : `contexte` (`transport`, `foule`, `lieu_clos`), `parade_utilisee`, `perte_de_connaissance`.

⚠️ **Un `perte_de_connaissance: true` sur un épisode typé `panique` impose de revoir le typage en séance.** C'est le discriminant, il ne se néglige pas.

---

## 5. Ce qu'on ne dit jamais

| Ne pas dire | Dire à la place |
|---|---|
| « Ce n'est rien », « c'est dans la tête » | « C'est réel, c'est physiologique, et c'est répertorié. Ce sont les symptômes 1, 4 et 11. » |
| « Calme-toi », « détends-toi » | « Inspire sur 4, expire sur 6. Je compte avec toi. » |
| « Imagine un endroit agréable » | ⛔ Aphantasie. « Pose tes pieds bien à plat, sens le sol. » |
| « Tu vas t'évanouir ? » | « Tu as l'impression de tomber — c'est le symptôme 8. Ça ne t'est jamais arrivé hors contexte médical. » |
| « Prends un cachet » | **Non-substitution.** « C'est une question pour le Dr Isorni. Elle est au brief. » |

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 09/08/2026 | Création — brique d'Étape 4 avancée pour le vol du 07/09/2026 (kit vol minimal). |
