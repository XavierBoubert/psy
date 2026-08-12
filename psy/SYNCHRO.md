# Synchronisation et sécurité des données

**Statut :** v2.0 — **11/08/2026**. ⚠️ **Le transport PC ↔ Android passe par Google Drive** *(arbitrage de Xavier, 11/08/2026, §2.2)* — **Syncthing est écarté**. Mise en œuvre : Étape 5, jalon K4.

---

## 1. Décision

**Deux mécanismes, deux rôles distincts — ils ne font pas la même chose.**

| Mécanisme | Rôle | Périmètre | Quand |
|---|---|---|---|
| **Dépôt git privé** `github.com/XavierBoubert/psy` | **Historique et sauvegarde.** Traçabilité clinique gratuite : qui a écrit quoi, quand, et retour arrière possible. | Tout le dépôt, `psy/dossier/` compris | ✅ **En place** |
| **Google Drive** | **Transport dans les deux sens** — le contenu vivant entre Claude Psy et Kokoro. Dossier de transit hors dépôt. | `journal/` · `programme.json` *(PC → Android)* · `reponses/` *(Android → PC)*. ⭐ **Rien d'autre du dossier ne transite** | 🔴 **K4** *(11/08/2026)* étendu le **12/08/2026** (§2.3) |
| ~~Syncthing (P2P)~~ | ~~Transport PC ↔ Android~~ | — | ❌ **Écarté le 11/08/2026** |

**Pourquoi pas git comme canal Android :** il n'existe pas de client git confortable sur Android, et une app compagnon qui doit écrire en un geste ne peut pas dépendre d'un `commit`/`push`. Drive pose des fichiers ; c'est ce qu'il faut.

**Pourquoi pas Drive comme archive :** il synchronise, il n'archive pas. Une erreur d'écriture se propage. Le git fournit le filet, et il reste la sauvegarde de référence.

---

## 2. Ce qui est assumé — à dire franchement

### 2.1 GitHub *(arbitrage du 09/08/2026)*

Le PLAN §6, dans sa rédaction d'origine (08/08/2026), posait : « données locales, repo privé + synchro chiffrée. **Rien ne part vers un tiers hors appels à Claude.** » **Versionner le dossier sur GitHub contredit partiellement cette phrase** : GitHub est un tiers, et il héberge des données de santé. Le §6 a été réécrit le 09/08/2026 pour porter l'arbitrage plutôt que le contredire en silence.

C'est un arbitrage rendu par Xavier le 09/08/2026, en connaissance de cause, pour un motif précis : la traçabilité clinique et la sauvegarde hors-machine valent, à ses yeux, le risque résiduel. Le dossier `ressources/xavier/` — qui contient l'intégralité des documents médicaux réels — y est de toute façon déjà versionné depuis l'origine du projet.

**Conditions attachées :**

- Le dépôt **doit rester privé**. À vérifier périodiquement.
- **2FA obligatoire** sur le compte GitHub, avec clé SSH (déjà le cas : le remote est en `git@`).
- **Aucun fork, aucun collaborateur, aucune GitHub Action** ayant accès au contenu du dépôt.

### 2.2 🔴 Google Drive *(arbitrage du 11/08/2026 — second tiers)*

**Xavier a écarté Syncthing au profit de Google Drive le 11/08/2026, après objection argumentée du dispositif et maintien de sa décision.** L'objection est consignée ici parce que la règle du §2.3 l'exige, et parce qu'un arbitrage dont on a effacé le contre-argument n'est plus un arbitrage.

**Ce qui a été opposé, et qui reste vrai :**

| Objection | Portée après décision |
|---|---|
| **Drive n'expose pas de racine sélectionnable en arbre de documents (SAF)** | ⚠️ **Fait technique à vérifier sur l'appareil.** S'il se confirme, Kokoro ne peut pas écrire *dans* Drive sans l'API Google — donc `INTERNET`, OAuth et un SDK tiers dans l'app, que `PLAN-KOKORO.md` §8 exclut. **Contournement retenu : Kokoro écrit en local, l'app Drive assure le transport** (§4). |
| **Drive pour desktop ne doit jamais voir `.git`** | Traité par construction : le dossier Drive est **hors dépôt**, et un script copie les fichiers dans le dépôt (§4). |
| **Les conflits Drive sont silencieux** (« fichier (1) ») là où Syncthing les marque `*.sync-conflict-*` | ⚠️ **Non résolu.** Mitigation : le transport ne porte que `journal/`, écrit par **une seule surface à la fois** (R4), ce qui rend le conflit peu probable. À surveiller à chaque ingestion. |
| **Drive n'apporte pas la sauvegarde** — GitHub la fournit déjà | Acté : Drive est un **transport**, rien d'autre. |
| **Un tiers de plus voit des données de santé**, sur un compte grand public non HDS | ⭐ **Réduit, pas supprimé** : seul `journal/` transite (voir ci-dessous). |

**Ce que Drive apporte, et qui a motivé la décision :** Syncthing exige que les deux appareils soient allumés en même temps, et son installation Android passe par F-Droid. Drive n'a ni l'une ni l'autre de ces frictions.

**⭐ Réduction de surface appliquée d'office : seul `journal/` transite.** `profil.md`, `etat.md`, `seances/`, `crises/`, `mesures/`, `briefs/` et `gabarits/` **ne quittent pas le PC**. Le transport n'a besoin que du check-in quotidien, et c'est la seule chose que Kokoro écrit à ce jalon. Ce qui part chez Google se limite donc à des **compteurs de comportements** — nombre de shutdowns, heures de sommeil, minutes de PPC — sans diagnostic, sans nom de praticien, sans compte rendu, sans idéation.

**Conditions attachées à cet arbitrage :**

- Le dossier Drive **n'est jamais partagé** : aucun lien, aucun destinataire, aucun « partagé avec moi ».
- **2FA obligatoire** sur le compte Google.
- **Aucune application tierce** autorisée sur ce Drive.
- Le dossier Drive est un **transit** : les fichiers y arrivent, sont copiés dans le dépôt, et le dépôt fait foi.
- ⚠️ **Aucune extension du périmètre sans nouvel arbitrage tracé ici.** Le jour où une surface voudra faire transiter `seances/` ou `crises/`, c'est une décision nouvelle — pas une continuation de celle-ci.

### 2.3 ⭐ Extension du périmètre — le Drive porte le contenu vivant *(arbitrage du 12/08/2026)*

**Le §2.2 exigeait qu'une extension du périmètre soit un arbitrage neuf, tracé ici. En voici un.**

Décision de Xavier, 12/08/2026 : *« Partons du principe que Google Drive contienne le contenu vivant qui transite entre Claude Psy et Kokoro. »* Le transport cesse d'être à sens unique.

| | Avant *(11/08)* | Après *(12/08)* |
|---|---|---|
| Sens | Android → PC | **Les deux sens** |
| Ce qui transite | `journal/` seul | `journal/` · **`programme.json`** *(PC → Android)* · **`reponses/`** *(Android → PC)* |
| Ce qui reste au PC | profil, état, séances, crises, mesures, briefs, gabarits | **Inchangé — aucun de ces répertoires ne transite** |

**Ce que ça change vraiment, dit franchement :**

- 🔴 **Le contenu qui part chez Google cesse d'être uniquement des compteurs.** `programme.json` porte des **libellés cliniques** — le nom des démarches PPC, les consignes d'exercice, les énoncés de questionnaire. On y lit le chantier en cours, et le nom des praticiens sollicités. C'est plus qu'un compteur de comportements ; c'est moins qu'un compte rendu, un diagnostic ou une idéation, qui eux ne transitent toujours pas.
- ⚠️ **Un questionnaire publié un jour dans Kokoro fera transiter ses réponses**, donc des scores. **Le PHQ-9 reste hors de ce circuit** — c'est le seul instrument porteur d'un déclencheur d'escalade, et il ne se passe qu'en conversation.
- ✅ **Le risque de conflit ne s'aggrave pas** : `programme.json` est écrit par le PC **seul**, `reponses/` par Kokoro **seul**. Aucun fichier n'a deux auteurs — c'est ce qui rend l'ajout tolérable, et c'est une condition de l'arbitrage, pas une observation.

**Conditions attachées** — les mêmes qu'au §2.2, plus une :

- ⭐ **Aucun contenu de `psy/dossier/` ne part par ce canal.** Le programme est **dérivé** du dossier, il n'en est pas un extrait : il porte ce qu'il y a à faire, jamais ce qui a été constaté, mesuré ou diagnostiqué.

### 2.4 La règle qui n'a pas changé

L'assouplissement porte sur **GitHub et Google Drive, et rien d'autre**. Hors de ces deux-là et hors des appels à Claude, **aucune donnée ne part vers un tiers** : pas de cloud santé, pas de service d'analyse externe, pas de télémétrie, pas de sauvegarde chez un hébergeur. **Toute proposition d'ajouter un service tiers au dispositif est refusée par défaut et doit faire l'objet d'un arbitrage explicite, tracé ici** — comme les deux ci-dessus.

**Porte de sortie**, si l'un des deux arbitrages est révisé : chiffrement au repos par `git-crypt` ou `age` sur `psy/dossier/` et `ressources/xavier/`. Contrepartie : Claude Code ne peut plus rien lire sans déverrouillage, et chaque surface doit gérer la clé.

---

## 3. Ce que la synchro impose au format du dossier

Ces contraintes sont **déjà câblées** dans `psy/dossier/SCHEMA.md`. Elles valent pour tout transport, Drive comme Syncthing.

| Contrainte | Pourquoi |
|---|---|
| **R1 — un fichier par événement, jamais de fichier partagé auquel on ajoute des lignes** | Deux appareils qui appendent au même fichier produisent un conflit. Un fichier par événement rend le conflit **structurellement impossible**. |
| **R2 — append-only** | Un fichier jamais réécrit ne peut pas diverger. Les seules exceptions, `profil.md` et `etat.md`, sont écrites **uniquement depuis le PC** — et ne transitent pas. |
| **R4 — nommage `AAAA-MM-JJ` en préfixe** | Deux appareils qui écrivent le même jour écrivent le même nom de fichier — d'où la règle : `journal/` est écrit par **une seule surface à la fois** (`source` le déclare). |

**Fichiers en double.** Drive ne marque pas les conflits : il crée `2026-08-11 (1).json`. ⚠️ **Un fichier de ce nom ne se supprime jamais sans être lu** — c'est une donnée clinique. Procédure : lire les deux versions, fusionner à la main dans le dépôt, committer la fusion. **Le script d'ingestion refuse d'écraser un fichier existant** : c'est la garde côté PC.

---

## 4. Procédure d'installation — K4

**Sur le téléphone :**
1. Kokoro écrit `journal/AAAA-MM-JJ.json` dans un dossier **local** désigné une fois par Xavier (Storage Access Framework — Kokoro n'obtient de droit que sur ce dossier-là, et **aucune permission n'entre au manifeste**).
2. ⚠️ **À vérifier sur l'appareil, et ça décide de la suite** : si le sélecteur de dossier propose Google Drive, le dossier désigné *est* Drive et il n'y a rien d'autre à faire. Sinon, Kokoro propose un envoi vers Drive après l'enregistrement, via le sélecteur système (aucune permission, aucun SDK Google dans l'app — c'est l'application Drive qui fait le réseau).

**Sur le PC :**
3. Google Drive pour ordinateur, dossier Drive `psy-journal` monté en lecture.
4. ⚠️ **Ne jamais faire pointer Drive sur `c:\p\psy`** — le dépôt git ne doit être synchronisé par aucun service. Le dossier Drive reste **hors du dépôt**.
5. Ingestion : `npm run journal-ingest` copie les fichiers du transit vers `psy/dossier/journal/`, **sans jamais écraser** (R2), et signale les doublons Drive.

**Vérification :**
6. Un check-in saisi sur le téléphone arrive dans `psy/dossier/journal/`, valide au schéma.
7. Vérifier que `.git` n'est synchronisé par rien.

---

## 5. Reste à faire

- [x] ~~Vérifier si le sélecteur de dossier Android propose Google Drive~~ → ✅ **oui**, vérifié le 11/08/2026
- [x] ~~Installer Google Drive pour ordinateur, dossier de transit hors dépôt~~ → ✅ `H:\Mon Drive\psy-journal`
- [x] ~~Écrire `scripts/journal-ingest`~~ → ✅ `npm run sync`
- [x] ~~Écrire le script de publication du programme~~ → ✅ `npm run publish` *(12/08/2026)*
- [ ] **Kokoro lit `programme.json` et écrit `reponses/`** — jalon K5
- [ ] Vérifier que le dépôt GitHub est privé et que la 2FA est active — **sur GitHub et sur le compte Google**
- [ ] Vérifier que le dossier Drive n'est partagé avec personne
- [ ] Trancher l'arbitrage E — compte Google du transit *(aujourd'hui `xavier@allons-y.io`, compte de la structure professionnelle)*
- [ ] Décider si une sauvegarde froide hors-ligne (disque chiffré) s'ajoute au dispositif

---

| Version | Date | Modification |
|---|---|---|
| **2.1** | **12/08/2026** | ⭐ **Le Drive porte désormais le contenu vivant, dans les deux sens** *(arbitrage de Xavier — §2.3, tracé comme arbitrage neuf ainsi que le §2.2 l'exigeait)*. S'ajoutent `programme.json` **(PC → Android)** et `reponses/` **(Android → PC)**. 🔴 **Ce qui part chez Google cesse d'être uniquement des compteurs** : le programme porte des **libellés cliniques** — nom des démarches, consignes d'exercice, praticiens sollicités. Il reste **dérivé** du dossier et n'en est pas un extrait : ni profil, ni état, ni séances, ni crises, ni mesures, ni briefs ne transitent. ✅ **Le risque de conflit ne s'aggrave pas** — `programme.json` n'a qu'un auteur (le PC), `reponses/` n'en a qu'un (Kokoro) : **aucun fichier à deux mains**, et c'est une condition de l'arbitrage. ⚠️ **Le PHQ-9 reste hors de ce circuit** : seul instrument porteur d'un déclencheur d'escalade, il ne se passe qu'en conversation. |
| **2.0** | **11/08/2026** | 🔴 **Syncthing est écarté, le transport passe par Google Drive** — arbitrage de Xavier, rendu après objection argumentée du dispositif et maintien de la décision. **L'objection est conservée entière au §2.2**, avec ce qu'elle a de non résolu (conflits silencieux) et ce qu'elle a de motivé (Syncthing exige deux appareils allumés et une installation par F-Droid). ⭐ **Réduction de surface appliquée d'office : seul `journal/` transite** — ni profil, ni état, ni séances, ni crises, ni mesures, ni briefs. Ce qui part chez Google se limite à des compteurs de comportements. 📌 **Drive est un transit hors dépôt** : le dépôt git n'est synchronisé par aucun service, et un script d'ingestion refuse d'écraser (R2). ⚠️ **Un point technique reste à vérifier sur l'appareil** et il commande la mise en œuvre : Drive apparaît-il dans le sélecteur de dossier Android ? |
| 1.0 | 09/08/2026 | Création — Étape 0. Décisions : dossier versionné dans le dépôt privé · Syncthing P2P pour le transport PC ↔ Android. |
