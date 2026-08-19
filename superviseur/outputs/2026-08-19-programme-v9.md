---
date: 2026-08-19
porte_sur: programme
version: 9
verdict: publiable
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v9

**Périmètre : la bibliothèque, et elle seule.** Douze fiches sont écrites et branchées au programme — les douze lignes de `PLAN.md` §5. **Aucune étape qui fait agir n'est créée, modifiée ni retirée** : `check-in`, `ppc-palier-1`, `email-isorni`, `essai-a-deux` et les quatre écrans de crise sont identiques à la v8, et les quatre bilans aussi. **La publication se fait donc hors séance**, comme le prévoit `PROGRAMME.md` §1.

**Le programme passe de 13 à 25 étapes ; la bibliothèque, de 2 à 14 documents.**

> **Ce rôle supervise Claude, pas Xavier.**

---

## ✅ Verdict : publiable

**Cinq constats, aucun bloquant.** Deux ont donné lieu à une correction **avant** publication, écrite ici et non en silence. Une objection de fond porte sur le volume, pas sur le contenu.

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | 🔴 **C3 — la « ligne discrète en haut » n'existe pas dans Kokoro** | `PROGRAMME.md` §2 dit que Kokoro compare sa version à celle du programme et « affiche une ligne discrète en haut » quand il y a du nouveau. **Le champ `version` n'est lu que par le parseur** *(`programme/Programme.kt:104`)* et n'est référencé par aucune surface : ni `monde/`, ni `ui/`, ni `reglages/`. **La fiche `comment-marche-kokoro` l'affirmait ; elle a été corrigée avant publication** — elle dit maintenant que l'annonce se fait dans la conversation, ce qui est vrai et conforme à l'invariant « annoncer avant de faire ». ⚠️ **Le contrat, lui, décrit toujours une interface inexistante** : c'est `PROGRAMME.md` §2 qu'il faut corriger, ou l'app qu'il faut compléter. **Acte séparé, non bloquant pour cette publication.** | `companion/PROGRAMME.md` §2 · `programme/Programme.kt` | 🔴 Ouvert |
| **2** | **C6 — un chiffre sans source primaire retiré avant publication** | `ppc-desensibilisation.md` §0 porte « la claustrophobie est invoquée par **63 à 84 %** des patients intolérants ». **Aucune référence vérifiable au dossier** — ni dans `docs/references/`, ni dans un courrier. La première version de `ppc-pourquoi-maintenant` le recopiait ; **il a été retiré et remplacé par une formulation sans chiffre.** ⚠️ **Le protocole, lui, le porte toujours** : à sourcer ou à retirer au prochain passage dessus. | `bibliotheque/ppc-pourquoi-maintenant.md` · `protocoles/ppc-desensibilisation.md` §0 | ⚠️ Ouvert sur le protocole |
| **3** | **C3 — `pourquoi-pas-de-score` ne peut pas porter la rubrique prévue** | `PLAN.md` §5 la range en rubrique `bilan`. **Le schéma l'interdit** : `rubrique: bilan` est réservée au type `bilan`, et une fiche rangée là n'aurait aucune place à l'écran *(`schemas/programme.ts`, `PROGRAMME.md` §3)*. **Publiée en `documentation`** — sans conséquence d'affichage, une fiche vivant de toute façon sur l'écran *Documentation* quelle que soit sa rubrique. **Le PLAN disait une chose impossible ; il disparaît avec cette publication.** | `programme.json` v9 · `psy/PLAN.md` §5 | ✅ Résolu |
| **4** | ⚠️ **C8 — deux fiches décrivent des chantiers qui ne sont pas ouverts** | `alimentation-les-quatre-regles` et `activite-sans-impact` décrivent des dispositifs que `etat.md` §1 range en **palier 0**, et que `PLAN.md` §3 conditionne à deux préalables *(passation du BES · feu vert médical)*. **Les deux fiches le disent explicitement et refusent de démarrer quoi que ce soit** — « rien ne commence tant que ce n'est pas ouvert en séance », « tant que ce feu vert n'est pas obtenu, rien ne démarre ». ⚠️ **Le risque n'est pas dans la lecture d'aujourd'hui, il est dans la durée** : ce sont des PDF figés, et si les règles bougent en séance, rien ne force leur réécriture. **C'est le mode de défaillance C8 appliqué à la bibliothèque, déjà constaté sur `ppc-les-paliers`.** | `bibliotheque/` · `etat.md` §1 · `PLAN.md` §3 | ⚠️ Veille |
| **5** | ⭐ **C7 — la bibliothèque est multipliée par sept en une publication** | 2 documents → 14. **C'est le plus gros ajout de doctrine depuis l'ouverture du dispositif**, et il tombe le jour où le check-in quotidien compte **5 relevés sur les 11 jours écoulés** *(`PLAN.md` §6)*. ⭐ **Ce n'est pas un reproche d'assiduité — c'est le contrôle C7 : plus de doctrine produite qu'exécutée.** Voir l'objection de fond. | `bibliotheque/` | ⚠️ Nommé |

---

## Ce qui tient

**Les douze fiches ont été relues intégralement, ligne à ligne, après écriture.**

**C9 — aucune n'est une copie.** Vérifié fiche par fiche contre son protocole source :

- **Aucune phrase reprise mot pour mot** d'un fichier de `psy/docs/protocoles/`. Les fiches sont écrites en tutoiement, à la deuxième personne, et réorganisées autour de *ce qu'il y a à faire et pourquoi* — les tableaux de traçage au dossier, les renvois `§x.y`, les critères de cotation et les réserves adressées au prescripteur n'y sont pas.
- **Aucun pronostic.** Le seul énoncé de résultat est un fait mesuré : IAH 35/h sans appareil, < 6/h avec *(courriers Roisman du 19/01 et du 04/05/2026, sources primaires)*.
- **Aucun diagnostic non encore dit.** Les diagnostics évoqués — apnées, atteinte du foie, situations agoraphobiques, épisodes où la parole se coupe — sont tous portés par des documents que Xavier possède. **L'hypothèse d'hyperphagie boulimique n'est jamais nommée** : `alimentation-les-quatre-regles` parle de « moments où manger échappe complètement au contrôle », comme d'un signal d'arrêt, sans en faire un fait ni un diagnostic.
- **Aucun nom de praticien qu'il ne consulte pas.** Cités : Dr Isorni, Dr Roisman, Dr Fournier, Link Sommeil. ⭐ **La consultation du 04/05 est rapportée sans nommer qui a « remotivé le patient »** — le fait clinique est conservé, la mise en cause d'un praticien nommé ne franchit pas la frontière.
- **`shutdown-ce-qui-reste-ouvert` est écrite pour Xavier, `montrable: false`.** Elle ne double pas `fiche-chourouk`, qui est la carte montrée à l'aidant *(`montrable: true`)*. **Deux documents, deux destinataires, aucun contenu de dossier dans le second.**

**Les sept familles d'interdits : `npm run psy:publish` les a passées sur les quatorze fiches et sur les 25 étapes, sans un seul refus** — vérifié en exécution réelle avant l'écriture de cette passe, la publication n'ayant été refusée que sur l'absence de ce fichier.

**C4 — aucune dérive R6.** Aucune fiche ne demande de coter un ressenti, et trois le refusent explicitement en nommant ce qui les remplace : les **repères extérieurs** de la tension appliquée, le **test de la phrase** pour l'intensité d'effort, la **portion décidée à froid** pour les repas. ⭐ **`pourquoi-pas-de-score` est le premier document qui explique à Xavier pourquoi le dispositif refuse de lui montrer un chiffre** — c'était jusqu'ici une règle appliquée sans être dite.

**C2 — les affirmations sur le comportement de l'application ont été vérifiées dans le code**, pas reprises du contrat :

- **Aucun son, aucune vibration** — `CanalAlerte.kt:32-34`, `AccesCrise.kt:30-32`, `ContenuSeanceDuo.kt:75`.
- **Un PDF part au lecteur du téléphone, avec le picto « dehors »** — `LecteurPdf.kt`, `Bords.kt:240-244`.
- **Une démarche faite reste lisible et perd son bouton** — `Etapes.kt:76-90`.
- **Le minuteur vit dans le panneau et c'est dit avant de commencer** — `ContenuExercice.kt:32`.
- **Un questionnaire terminé affiche « C'est enregistré dans le dossier. »**, et rien d'autre — `strings.xml:263`. **Aucun score nulle part.**
- ⚠️ **Une seule affirmation s'est révélée fausse : la ligne de nouveauté.** Constat n° 1, corrigé avant publication.

**C8 — le programme ne contredit pas le dossier.** `jour-de-vol` et `sejour-tunisie` reprennent les décisions datées de `etat.md` §1 : départ le 07/09, aucune progression de palier pendant le séjour, redescente d'un palier au retour sur les trois chantiers, la machine part en bagage cabine. **Aucune fiche ne dit où Xavier en est rendu** — vérifié sur les quatorze : ni palier en cours, ni étape atteinte, ni chiffre d'avancement.

**C10 sans objet** — aucune `seance-duo` n'est créée ni modifiée. `essai-a-deux` est identique à la v8, octet pour octet.

**Le format :** 25 étapes, `id` uniques, aucun `id` réutilisé, les douze `document` appelés existent tous dans `companion/inputs/bibliotheque/`, les quatre bilans sont inchangés. **Le schéma accepte le fichier.**

**Non relu ici :** les quatre bilans *(inchangés depuis leur passe)*, le code de Kokoro au-delà des six points ci-dessus, `fiche-chourouk` et `ppc-les-paliers` *(inchangées, déjà visées)*.

---

## Objections de fond

**Une seule, et elle porte sur le volume, pas sur le contenu.**

⚠️ **Douze fiches d'un coup, c'est plus de lecture que ce qui a été exécuté en dix jours.** Le dispositif a produit, au 19/08 : sept protocoles, cinq corpus, sept skills, une bibliothèque qui devient quatorzaine — contre **une échelle passée, cinq check-in sur onze jours, deux séances de palier 1 sur trois**. **C7 est exactement fait pour repérer ce déséquilibre, et il est là.**

**Ce qui empêche d'en faire un motif de refus, et il faut les deux :**

1. **Xavier l'a demandé explicitement le 19/08** — « mets moi à disposition les documentations qui manquent ». Ce n'est pas une production spontanée du dispositif : c'est une commande, et ce rôle n'a pas à décider à sa place ce qu'il a le droit de lire.
2. ⭐ **Une fiche ne demande rien.** Elle ne s'affiche pas comme une tâche, ne compte pas, ne reproche pas d'être fermée. **Le coût d'une fiche non lue est nul** — c'est ce qui la sépare d'une étape qui fait agir, et c'est précisément pourquoi `PROGRAMME.md` §1 autorise la documentation à partir hors séance.

**Formulé net : le risque n'est pas que Xavier soit submergé — c'est que le dispositif prenne l'écriture pour de l'action.** La contre-mesure n'est pas de publier moins ; c'est que la prochaine séance porte sur ce qui n'avance pas *(le check-in quotidien)*, pas sur ce qui s'écrit bien.

---

## Arbitrages demandés

| # | Question fermée | Ce qui en dépend |
|---|---|---|
| **1** | **Corrige-t-on `PROGRAMME.md` §2 pour supprimer la ligne de nouveauté, ou complète-t-on Kokoro pour qu'elle existe, oui ou non ?** *(constat n° 1)* | Le contrat décrit aujourd'hui une interface qui n'existe pas. **C'est le mode de défaillance C3, sur le document le plus normatif du dispositif.** |
| **2** | *(reconduite de la v8, non traitée)* **Une `seance-duo` clinique peut-elle partir avant que la fiche de rôle soit transmise à Chourouk, oui ou non ?** | Sans objet pour cette publication — aucune séance à deux n'y bouge —, mais l'arbitrage reste ouvert et **bloque la première séance clinique**. |
| **3** | *(reconduite de la v8)* **`essai-a-deux` sort du programme dès que tu déclares l'essai concluant, ou à la prochaine clôture de séance ?** | Une ligne non clinique sur l'écran *quand j'en ai besoin*. **Elle est restée dans la v9 faute de réponse.** |
