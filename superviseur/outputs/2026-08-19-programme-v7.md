---
date: 2026-08-19
porte_sur: programme
version: 7
verdict: publiable
controles: [C1, C2, C3, C4, C5, C6, C7, C8, C9, C10]
---

# Passe de publication — programme v7

**Périmètre :** une seule étape ajoutée, `essai-a-deux`, **le premier `seance-duo` du dispositif**, et le portage du type dans Kokoro (**K6**). ⭐ **C'est tout ce que la v7 change** : les seize autres étapes sont identiques, mot pour mot, à la v6 publiée ce matin. **La bibliothèque n'est pas modifiée** — elle n'a donc pas été relue ici.

**Contrôle central de cette passe : C10.** C'est la première fois qu'un contenu du dispositif est adressé à quelqu'un d'autre que Xavier.

> **Ce rôle supervise Claude, pas Xavier.**

---

## ✅ Verdict : publiable

**Cinq constats, aucun bloquant.** Un porte sur une garde mécanique qui ne s'exécutait plus ; deux sur ce que le retour de Kokoro ne saura pas dire ; deux sur la durée de vie de l'étape ajoutée. **Les trois constats ouverts de la v6 restent ouverts et ne sont pas aggravés par cette publication.**

---

## Constats

| # | Contrôle | Fait vérifié | Où | Gravité |
|---|---|---|---|---|
| **1** | 🔴 **C3 — une garde câblée qui ne s'exécutait plus** | `ProgrammePublieTest` vérifie qu'aucune étape publiée n'est écartée en silence par Kokoro. Il lit `../../inputs/programme.json` **à l'exécution**, un chemin que Gradle ne connaissait pas : la tâche de test était donc déclarée `UP-TO-DATE` dès lors que **seul le programme changeait**. ⭐ **La garde n'a donc pas tourné sur les v5 ni v6** — elle était écrite, exécutée jamais. **Réparé pendant le développement de K6** *(`build.gradle.kts`, `inputs.files(fileTree("../../inputs"))`)* et **prouvé par un échec provoqué** : `entrainement_requis: false` fait désormais tomber le test, sur `essai-a-deux` nommément. ⚠️ **La correction est antérieure à cette passe et n'est pas un acte du superviseur** — elle est nommée ici pour qu'elle laisse une trace. | `companion/android/app/build.gradle.kts` · `app/src/test/.../ProgrammePublieTest.kt` | ⚠️ Corrigé, à ne pas défaire |
| **2** | ⭐ **C3 — le critère de fin de K6 n'est pas observable dans `reponses/`** | `PLAN.md` §2 K6 pose comme critère *« un entraînement joué en entier par Chourouk »*. Kokoro écrit `issue: entrainement` **aussi bien pour un entraînement mené au bout que pour un entraînement arrêté en route** — c'est conforme au contrat *(`PROGRAMME.md` §5 : quatre issues, et il n'y en a pas d'autre)*, et c'est délibéré : un entraînement interrompu ne doit pas ressembler à un arrêt clinique. **Conséquence : le dossier ne pourra pas constater le critère depuis les fichiers.** Il se constatera par la parole de Xavier, en séance. **Ce n'est pas un défaut de l'app, c'est une limite du format** — mais elle doit être écrite avant, pas découverte au moment de fermer le jalon. | `PROGRAMME.md` §5 vs `PLAN.md` §2 K6 | ⚠️ À nommer, non bloquant |
| **3** | **C3 — la mémoire de l'entraînement ne survit ni à une réinstallation ni à un effacement des données** | Kokoro retient localement *(SharedPreferences)* les étapes dont l'entraînement est allé au bout : l'issue n'étant pas dans le nom du fichier, elle ne se relit pas de Drive. **Après réinstallation, Kokoro reproposera l'entraînement en premier.** ⭐ **Le sens de la défaillance est le bon** — reproposer un entraînement déjà fait ne coûte rien, l'inverse coûterait la garde entière. **Aucune correction demandée**, le point est nommé pour ne pas être relu comme un bug plus tard. | `journal/DossierSynchronise.kt`, clé `entrainements_menes` | ⚠️ Veille |
| **4** | ⭐ **C8 — une étape qui fait agir, sans compte rendu de séance qui la décide** | `essai-a-deux` est une **étape qui fait agir** au sens de `PROGRAMME.md` §1 : elle part donc sous `--seance`. **Elle ne vient d'aucune clôture de séance** — elle vient d'une demande directe de Xavier, aujourd'hui, en conversation, pour éprouver le circuit avec Chourouk. **Le fond est fondé** *(c'est lui qui décide, et le type ne peut pas se tester autrement qu'en le jouant)* **mais rien n'en garde la trace côté dossier** : ni `etat.md` §1, ni `seances/`. ⚠️ **C'est la forme exacte du mode de défaillance C8** — une surface que Xavier consulte sans intermédiaire, portant une décision que le dossier ne connaît pas. **`etat.md` doit l'enregistrer au moment de la publication.** | `companion/inputs/programme.json` vs `psy/outputs/dossier/etat.md`, `seances/` | 🔴 À corriger — au moment de publier |
| **5** | **C8 — une étape d'essai n'a pas vocation à rester** | `essai-a-deux` est un **essai d'ingénierie**, pas un acte clinique : son contenu ne fait rien travailler et le dit lui-même *(« Ceci est un essai du dispositif »)*. Rangée en `therapie` / `au_besoin`, elle s'affichera **indéfiniment** sous *quand j'en ai besoin*, à côté du mot-code et de la tension appliquée — **Kokoro montre l'état, il ne décide jamais d'une sortie** *(`PROGRAMME.md` §3)*. ⭐ **Une ligne non clinique qui reste sur un écran qui doit rester lisible est du bruit permanent**, et il n'existe aucun mécanisme qui la retirera tout seul. **Son retrait se décide à la clôture de séance qui suivra le test.** | `companion/inputs/programme.json` · étape `essai-a-deux` | ⚠️ Arbitrage n° 1 |

---

## Ce qui tient

**Effectivement relu, ligne à ligne.**

**C10 — les sept garde-fous du type, vérifiés un par un sur `essai-a-deux` :**

- **`signal_arret` est présent, non vide, et recopié tel quel** du contrat : *« Xavier fait « non » de la main. On s'arrête, sans rien demander. »* ⭐ **Il n'a pas été réinventé pour l'occasion** — c'est précisément l'invariant du champ *(`PROGRAMME.md` §3)*.
- **Il est rappelé à l'écran en permanence** — vérifié dans le code : `RappelArret` est rendu sous **chacune** des trois vues *(accueil, cases à cocher, séquence)*, et n'est retiré qu'à la vue de fin, quand tout est terminé.
- **Les trois critères d'arrêt sont accessibles en un tap à tout moment**, par le bouton *« Quand s'arrêter »*, **sans quitter la consigne en cours ni interrompre le minuteur**.
- 🔴 **Le dernier critère est bien « tu ne sais pas quoi faire → on s'arrête »** — et cette règle est désormais **vérifiée deux fois** : par `psy-publish` au dépôt, et par Kokoro à la lecture. Une séance dont le dernier critère ne l'est pas est écartée. **Testé.**
- **`entrainement_requis: true`, et l'entraînement est proposé en premier** tant qu'il n'a pas été mené une fois. Il joue **la même séquence**, sans le matériel, et renvoie `issue: entrainement`.
- **Aucune consigne ne demande un jugement à Chourouk.** Les quatre consignes sont des actes : s'asseoir, lire, se taire, lire. **Aucune ne dit « estime », « décide », « rassure-le ».**
- ⭐ **Aucune consigne ne demande une réponse gestuelle de la main.** L'invariant est tenu : le « non » de la main n'apparaît **que** dans `signal_arret` et dans `avant`, où il est **convenu à froid** — jamais sollicité pendant la séquence, où un geste ambigu se lirait comme un arrêt manqué.

**C10 — ce que l'étape n'apprend pas à l'aidant :** aucun diagnostic, aucun score, aucune hypothèse, aucun compte rendu. **Le mot « shutdown » est le seul terme du dossier qui apparaît**, dans le deuxième critère d'arrêt — Chourouk le connaît déjà : c'est le mot-code convenu avec elle depuis le 09/08/2026 *(`etat.md`, v1.4)*, et il est là pour dire **ce qui n'est pas un refus**, pas pour la renseigner sur Xavier.

**Le format, relu contre `PROGRAMME.md` §3 :** `sequence` de quatre consignes, `pour` ∈ {`aide`, `patient`}, `secondes` entiers positifs *(30 · 30 · 45 · 20)*, `avant` de quatre lignes, `arret` de trois, `sortie_libre: true`, `rubrique: therapie`, `quand: au_besoin`. **Le schéma de `psy-publish` accepte le fichier** *(relu contre `ProgrammeSchema`)*, **et Kokoro le lit sans écarter aucune des dix-sept étapes** *(`ProgrammePublieTest`, désormais réellement exécuté)*.

**Ce que le script attrape déjà et que je n'ai pas refait :** visualisation, cotation de ressenti, série, numéro d'appel, prodrome, traitement, relaxation vasovagale. **Les sept familles passent au dépôt et à la lecture** — les textes de `seance-duo` sont soumis à `estPermis` côté Kokoro, item par item, ce qui est nouveau et vérifié par test.

**C4 — aucune dérive R6.** L'étape ne demande rien à personne sur un état interne. Elle ne demande **aucune** réponse : elle ne renvoie qu'une issue.

**C2 — aucun fait périmé introduit.** La v7 ne touche à aucune des seize étapes existantes.

**C7 — le dispositif a produit un jalon, et le chantier n° 1 a bougé le même jour.** K6 était le dernier jalon ouvert de Kokoro ; il est porté, pas encore fermé — le critère de fin est un essai réel, avec Chourouk. **Ce qui commande reste `ppc-palier-1` :** deux passages sont revenus au dossier, l'un `arrete_avant_la_fin` le 18/08, l'autre `termine` le 19/08. ⚠️ **Aucun de ces deux chiffres n'est un jugement** — ils disent seulement que le compteur du critère de palier existe et qu'il n'est pas à zéro.

**C9 sans objet** — aucune fiche ajoutée ni modifiée. `fiche-chourouk.md` et `ppc-les-paliers.md` **n'ont pas été relus dans cette passe** et ne figurent donc pas ici comme vérifiés. ⚠️ **`PLAN.md` §2.1 signale toujours qu'une version plus récente de `fiche-chourouk` existe et n'est pas publiée** — inchangé, et il faut le savoir avant de faire jouer une séance à deux.

**Reste ouvert depuis la v6, non aggravé :** les trois démarches revenues le 19/08 à 00h53 *(`ppc-prise-en-charge`, `ppc-releve`, `ppc-voyage`)* ne sont toujours versées nulle part au dossier, et `PLAN.md` §1 continue de porter la prise en charge du masque comme ouverte alors que Kokoro l'affiche faite. **Les trois arbitrages de la v6 sont toujours en attente.**

---

## Objections de fond

**Une, et elle porte sur ce que cette publication rend possible, pas sur son contenu.**

🔴 **Le type `seance-duo` entre en service avant que Chourouk ait reçu quoi que ce soit d'écrit.** `PLAN.md` §7 le pose noir sur blanc : tant que `aidant/ressources/fiche-chourouk.md` n'est pas transmise, **le rôle, le « non » de la main, les critères d'arrêt et le mode entraînement ne lui sont dits qu'oralement**. La version publiée dans Kokoro *(`fiche-chourouk`, `montrable: true`)* **n'est pas la dernière** — `PLAN.md` §2.1 le signale depuis le 18/08.

**Ce n'est pas un motif de refus, et c'est délibérément dit ainsi :** `essai-a-deux` est **exactement** la manière la moins risquée de faire cette première fois. Elle ne travaille rien, elle dure trois minutes, et **elle met le geste d'arrêt sous les yeux de Chourouk avant qu'il serve pour de vrai** — ce qui est le seul apprentissage qui compte ici. ⭐ **Le risque n'est donc pas cet essai : c'est ce qui viendrait après lui sans que la fiche soit passée.** La stabilisation non visuelle *(`PLAN.md` §7)* ne doit pas être la seconde `seance-duo` publiée si Chourouk n'a toujours rien lu.

**Formulé autrement : l'essai est un bon premier pas et un mauvais précédent s'il tient lieu de formation.**

---

## Arbitrages demandés

| # | Question fermée | Ce qui en dépend |
|---|---|---|
| **1** | **`essai-a-deux` sort-elle du programme dès que l'essai est fait, oui ou non ?** | Constat n° 5. Si oui, son retrait se décide à la clôture de la séance suivante ; si non, il faut dire pourquoi une ligne non clinique reste à l'écran |
| **2** | **La fiche à Chourouk part-elle avant la première `seance-duo` clinique, oui ou non ?** | Objection de fond. ⚠️ **Elle demande d'abord une supervision et une relecture de Xavier** *(`PLAN.md` §7)* — donc elle se décide maintenant, pas le jour où la stabilisation sera écrite |
| **3** | **Le critère de fin de K6 se constate-t-il par ta parole en séance, oui ou non ?** | Constat n° 2. `reponses/` ne distinguera pas un entraînement mené d'un entraînement interrompu, et il ne faut pas le découvrir au moment de fermer le jalon |
| **4** | *(reconduit de la v6, sans réponse)* **Les trois démarches revenues le 19/08 sont-elles closes, et `ppc-releve` reste-t-elle affichée aujourd'hui ?** | `PLAN.md` §1 et le téléphone se contredisent depuis ce matin |
