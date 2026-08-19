---
date: 2026-08-19
porte_sur: programme
version: 6
verdict: publiable
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v6

**Périmètre :** l'ouverture du canal `bilan` avec ses quatre premiers documents (`vviq-2026-08`, `rapport-psy-2026-08`, `polysomnographie-2026-01`, `evaluation-tsa-2024-04`). ⭐ **C'est tout ce que la v6 change** : le reste du programme est identique à la v4 supervisée le 18/08, seule version publiée à ce jour.

**Contrôle central de ce canal, en remplacement de C9 :** *ce document ne contient rien que Xavier ne sache déjà.*

> **Ce rôle supervise Claude, pas Xavier.**

---

## ✅ Verdict : publiable

**Cinq constats, aucun bloquant.** Trois portent sur des désynchronisations **antérieures à cette publication et non aggravées par elle** ; deux sur la nature exacte de deux des quatre bilans, et se règlent par l'annonce qui accompagne la publication.

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | 🔴 **C3 — une supervision déclarée avant d'exister** | `programme.json` a été versé au dépôt en **v5** avec `supervision: 2026-08-19-programme-v5`. **Ce fichier n'a jamais été écrit** — `superviseur/outputs/` ne contient rien après `2026-08-18-programme-v4.md`. La garde mécanique a tenu : `psy:publish` aurait refusé, et le transit est resté en v4. **Mais le champ a été rempli du nom d'un acte qui n'avait pas eu lieu**, ce qui est exactement la forme que prendrait un contournement s'il en existait un. ⭐ **La seconde garde a tenu elle aussi** : le retrait de `ppc-consultation-roisman` porté par la v5 est une étape **qui fait agir**, et le contrôle hors séance l'a refusé. **L'étape est donc remise au programme, et son retrait attend une clôture de séance.** La v6 ne contient plus rien de la v5. | `companion/inputs/programme.json` *(commit `fc4415c`)* | ⚠️ Procédure |
| **2** | ⭐ **C8 — quatre réponses revenues, aucune versée au dossier** | `companion/outputs/reponses/` porte quatre `issue: fait` du **19/08/2026 à 00h53** : `ppc-voyage`, `ppc-consultation-roisman`, `ppc-prise-en-charge`, `ppc-releve`. **Une seule a été traitée** — `ppc-consultation-roisman`, retirée du programme en v5. Les trois autres ne figurent ni dans `etat.md` §1, ni au journal des mises à jour, ni dans `PLAN.md`. **Le dossier ne sait pas que ces trois démarches sont revenues.** | `companion/outputs/reponses/2026-08-19-0053-*.json` vs `psy/outputs/dossier/etat.md`, `PLAN.md` §1 | 🔴 À corriger |
| **3** | **C8 — deux surfaces qui disent le contraire l'une de l'autre** | Conséquence directe du n° 2. `PLAN.md` §1 porte `[ ] Vérifier la prise en charge du nouveau masque — étape ppc-prise-en-charge` comme **ouverte**, alors que Kokoro l'affiche **faite** depuis le 19/08 à 00h53. Par ailleurs `ppc-releve` reste une démarche `sans_date` à l'écran, alors que `PLAN.md` §1 écrit qu'elle est **à redemander au palier 3**, la machine étant éteinte aux paliers 1 et 2. **Non aggravé par cette publication** : les deux étapes partent inchangées depuis la v4, et Kokoro les grise localement. | `PLAN.md` §1 vs `companion/inputs/programme.json` | ⚠️ À corriger, non bloquant |
| **4** | ⭐ **Contrôle du canal `bilan` — le VVIQ n'est pas un document que Xavier possède déjà** | Les trois autres bilans sont des documents cliniques reçus de tiers. **`vviq-2026-08` est écrit aujourd'hui, par Claude, et Xavier ne l'a jamais lu.** Le contrôle reste satisfait sur le fond — il connaît les seize items qu'il a passés, le score 18/80, le seuil et la conclusion, tous portés à `etat.md` §1.9 et à `profil.md`. **Une réserve précise :** l'observation sur les items 7 et 16, *« les deux items les plus explicitement dynamiques »*, n'existe que dans les `notes` de `mesures/2026-08-09-vviq.json`, écrites par Claude, et **le compte-rendu de séance du 09/08 ne porte aucune trace qu'elle ait été dite à Xavier** — la passation n'y a pas de section *Travail*. Le bilan la présente comme un constat dont **rien n'est tiré**, ce qui est la formulation correcte. **Elle doit être nommée dans l'annonce de publication**, pas découverte dans un PDF. | `companion/inputs/bilans/vviq-2026-08.md` §4 vs `psy/outputs/dossier/seances/2026-08-09-seance.md` | ⚠️ Veille + annonce |
| **5** | ⚠️ **Contrôle du canal `bilan` — le rapport est le seul bilan écrit par le dispositif lui-même** | `rapport-psy-2026-08.md` est une copie **verbatim** du rapport v2.4. L'exemption des sept interdits est motivée dans `bilans/README.md` par le fait qu'un bilan est *un rapport clinique réel* d'un tiers ; **celui-ci est écrit par Claude.** Ce qui part donc sur le téléphone, sans conversation autour, comprend §6.6.e *(alprazolam et SAOS)*, §10.1 *(quatre questions pharmacologiques et une ligne sur l'évaluation périodique du risque suicidaire)*, §6.3 *(TDAH, question ouverte)*. **Le contrôle est satisfait** : le document est dans `patient/ressources/`, Xavier en a fourni lui-même l'anamnèse le 08/08, `profil.md` le désigne comme la source qui fait foi, et son versement est **arbitré au `PLAN.md` §2 K8**. **Aucune de ces sections n'est une recommandation nouvelle du dispositif** — elles partent au brief du Dr Isorni, comme prévu. Le point est nommé pour que l'exemption reste motivée, et non héritée. | `companion/inputs/bilans/rapport-psy-2026-08.md` §6.6.e, §10.1 | ⚠️ Veille |

---

## Ce qui tient

**Effectivement relu, ligne à ligne :**

- **Les quatre étapes `bilan` sont conformes au format** *(`companion/PROGRAMME.md` §3)* : `rubrique: bilan` sur les quatre et sur elles seules, **aucun `quand`**, `date` en `AAAA-MM-JJ` et **celle du document** *(05/04/2024 · 19/01/2026 · 08/08/2026 · 09/08/2026)*, `document` en kebab-case, **ni `texte` ni `montrable` nulle part**. Les quatre `document` appelés existent dans `companion/inputs/bilans/`.
- **Les quatre titres affichés passent les sept familles d'interdits** — vérifié mot à mot, y compris le titre du VVIQ : *« imagerie mentale »* ne déclenche pas le motif `image mentale`, et aucun des quatre ne nomme une molécule, un ressenti coté, une série ou un numéro d'appel.
- **`vviq-2026-08` n'est pas une copie de la fiche `corpus/echelles/vviq.md`.** Le tableau des **trois issues et de leurs conséquences pour le dispositif** (§1 de la fiche) — la partie écrite pour le praticien — **n'est pas repris**. Le piège de cotation de la fiche §4 l'est, mais reformulé pour ce qu'il est ici : une précaution destinée à qui recevrait le chiffre. **La section 6, *ce que ce résultat ne dit pas*, est présente et développée**, ce qui est le point qu'un score plancher rend le plus facile à perdre.
- **`polysomnographie-2026-01` et `evaluation-tsa-2024-04` sont fidèles à leur source.** Chiffres recomptés contre les Markdown de `patient/ressources/` : IAH 35/h, 197 apnées dont 145 obstructives, charge hypoxique 61 %·min/h, SpO₂ minimale 86 %, ronflement 80 % du TST · AQ 39, EQ 9, 7 items ≥ 4 à l'échelle Attwood, ASDI 3/4 · 2/3 · 1/2 · 1/5 · 2/5 · 0/1, Groden 23 items, moyennes par catégorie. **Aucune conclusion n'est ajoutée, aucune n'est retirée.**
- ⭐ **La perte du codage couleur de la conclusion Saley est déclarée dans le bilan lui-même**, au lieu d'être comblée par une reconstitution. C'est le comportement correct : le rapport v2.4 §11 documente déjà cette perte, et reconstituer en silence aurait fabriqué de l'autorité (C6).
- ⭐ **Aucune étape qui fait agir ne change à cette publication.** Les quatre démarches PPC, l'email au Dr Isorni, les étapes de crise *(mot-code, tension appliquée, phrase pour le soignant, fiche Chourouk)*, `check-in`, `ppc-palier-1` et `ppc-les-paliers` sont **identiques, mot pour mot, à la v4 supervisée le 18/08** — vérifié contre `programme.json` du transit. **Le retrait de `ppc-consultation-roisman` est fondé au fond** *(`reponses/2026-08-19-0053-ppc-consultation-roisman.json` porte `issue: fait`)* **mais il ne part pas ici** : il se décide avec Xavier, en clôture de séance. **La bibliothèque n'est pas modifiée** : `fiche-chourouk.md` et `ppc-les-paliers.md` n'ont pas été relus dans cette passe, et ne figurent donc pas ici comme vérifiés.
- **C10 sans objet** — aucun `seance-duo` au programme.
- **C7 — cette publication verse du contenu, elle ne produit pas de doctrine.** Elle ferme le dernier item de K8. **Ce qui commande reste inchangé et n'a pas avancé aujourd'hui : trois séances de masque menées au bout.**

---

## Objections de fond

**Une, et elle porte sur le canal, pas sur les quatre documents.**

🔴 **L'exemption des sept interdits sur le corps d'un bilan est motivée par une propriété que le rapport v2.4 n'a pas.** `bilans/README.md` l'argumente ainsi : *« un rapport clinique réel nomme un traitement, un diagnostic, un praticien, et c'est sa raison d'être »*. C'est vrai de la polysomnographie et de l'évaluation Saley. **Ce n'est pas vrai du rapport v2.4, ni du compte rendu VVIQ : ceux-là sont écrits par Claude.** Le canal les traite pourtant à l'identique.

**Ce n'est pas un motif de refus** — le versement des quatre est arbitré au `PLAN.md` §2 K8, et le contrôle de substitution *(rien que Xavier ne sache déjà)* est satisfait pour les quatre. **Mais la règle écrite ne dit pas encore ce qui est réellement appliqué**, et une exemption dont le motif ne couvre plus la moitié des cas se transmet ensuite sans être réexaminée. **Le canal a besoin de sa propre phrase** : elle se rédige une fois, maintenant que le cas existe, plutôt qu'au cinquième bilan.

---

## Arbitrages demandés

| # | Question fermée | Ce qui en dépend |
|---|---|---|
| **1** | **Les trois démarches revenues le 19/08 — `ppc-prise-en-charge`, `ppc-releve`, `ppc-voyage` — sont-elles closes, oui ou non ?** | Constats n° 2 et n° 3. Tant que la réponse n'est pas au dossier, `PLAN.md` et le téléphone se contredisent. ⚠️ **C'est une question sur l'état du dossier, pas sur ce que Xavier a fait ou pas** — le superviseur ne cote pas l'observance |
| **2** | **`ppc-releve` doit-elle rester affichée aujourd'hui, ou revenir au palier 3 ?** | `PLAN.md` §1 dit qu'elle n'est exigible qu'au palier 3. Si la réponse est « au palier 3 », elle sort du programme et y revient plus tard |
| **3** | **Faut-il écrire dans `bilans/README.md` le motif propre aux bilans écrits par le dispositif ?** | Objection de fond. Une phrase, à écrire maintenant que le premier cas existe |
