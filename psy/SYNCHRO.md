# Synchronisation et sécurité des données

**Statut :** décision arrêtée le 09/08/2026 (Étape 0). Installation Syncthing : **à l'Étape 5**, quand Kokoro existera.

---

## 1. Décision

**Deux mécanismes, deux rôles distincts — ils ne font pas la même chose.**

| Mécanisme | Rôle | Périmètre | Quand |
|---|---|---|---|
| **Dépôt git privé** `github.com/XavierBoubert/psy` | **Historique et sauvegarde.** Traçabilité clinique gratuite : qui a écrit quoi, quand, et retour arrière possible. | Tout le dépôt, `psy/dossier/` compris | ✅ **En place** |
| **Syncthing (P2P)** | **Transport PC ↔ Android.** Chiffré TLS de bout en bout, aucun serveur tiers ne stocke les données. | `psy/dossier/` **seulement** | ⏸️ Étape 5 |

**Pourquoi pas git comme canal Android :** il n'existe pas de client git confortable sur Android, et une app compagnon qui doit écrire une crise en un geste depuis l'écran verrouillé ne peut pas dépendre d'un `commit`/`push`. Syncthing pose des fichiers ; c'est exactement ce qu'il faut.

**Pourquoi pas Syncthing seul :** il synchronise, il n'archive pas. Une erreur d'écriture se propage à tous les appareils en quelques secondes. Le git fournit le filet.

---

## 2. Ce qui est assumé — à dire franchement

Le PLAN §6, dans sa rédaction d'origine (08/08/2026), posait : « données locales, repo privé + synchro chiffrée. **Rien ne part vers un tiers hors appels à Claude.** » **Versionner le dossier sur GitHub contredit partiellement cette phrase** : GitHub est un tiers, et il héberge des données de santé. Le §6 a été réécrit le 09/08/2026 pour porter l'arbitrage plutôt que le contredire en silence.

C'est un arbitrage rendu par Xavier le 09/08/2026, en connaissance de cause, pour un motif précis : la traçabilité clinique et la sauvegarde hors-machine valent, à ses yeux, le risque résiduel. Le dossier `ressources/xavier/` — qui contient l'intégralité des documents médicaux réels — y est de toute façon déjà versionné depuis l'origine du projet.

**Conditions attachées à cet arbitrage :**

- Le dépôt **doit rester privé**. À vérifier périodiquement — un passage accidentel en public exposerait un dossier médical complet.
- **2FA obligatoire** sur le compte GitHub, avec clé SSH (déjà le cas : le remote est en `git@`).
- **Aucun fork, aucun collaborateur, aucune GitHub Action** ayant accès au contenu du dépôt.
- Si cet arbitrage est un jour révisé, la sortie existe et elle est propre : chiffrement au repos par `git-crypt` ou `age` sur `psy/dossier/` et `ressources/xavier/`. Contrepartie à connaître : Claude Code ne peut plus rien lire sans déverrouillage préalable, et chaque surface doit gérer la clé.

**Ce qui reste vrai sans réserve.** L'assouplissement porte sur **GitHub et rien d'autre**. Hors du dépôt et hors des appels à Claude, **aucune donnée ne part vers un tiers** : pas de cloud santé, pas de service d'analyse externe, pas de télémétrie, pas de sauvegarde chez un hébergeur. Syncthing est du pair-à-pair — il transporte, il ne dépose rien sur un serveur. Cette ligne ne se renégocie pas au coup par coup : toute proposition d'ajouter un service tiers au dispositif est refusée par défaut et doit faire l'objet d'un arbitrage explicite, tracé ici.

---

## 3. Ce que la synchro impose au format du dossier

Ces contraintes sont **déjà câblées** dans `psy/dossier/SCHEMA.md`. Elles sont rappelées ici parce que c'est la synchro qui les motive.

| Contrainte | Pourquoi |
|---|---|
| **R1 — un fichier par événement, jamais de fichier partagé auquel on ajoute des lignes** | Deux appareils qui appendent au même fichier produisent un conflit Syncthing. Un fichier par événement rend le conflit **structurellement impossible**. |
| **R2 — append-only** | Un fichier jamais réécrit ne peut pas diverger. Les seules exceptions, `profil.md` et `etat.md`, sont écrites **uniquement depuis le PC**. |
| **R4 — nommage `AAAA-MM-JJ` en préfixe** | Deux appareils qui écrivent le même jour écrivent le même nom de fichier — d'où la règle : `journal/` est écrit par **une seule surface à la fois** (`source` le déclare). Les crises portent l'heure dans le nom (`AAAA-MM-JJ-HHMM-<type>.json`), ce qui les rend uniques par construction. |

**Fichiers de conflit.** Si Syncthing en crée un (`*.sync-conflict-*`), il ne faut **jamais** le supprimer sans le lire : c'est une donnée clinique. Procédure : lire les deux versions, fusionner à la main, committer la fusion, supprimer le fichier de conflit.

---

## 4. Procédure d'installation — Étape 5

À exécuter quand l'app Android existera.

**Sur le PC (Windows) :**
1. Installer Syncthing (SyncTrayzor ou le binaire officiel).
2. Partager **`c:\p\psy\psy\dossier`** — et **rien d'autre**. Ni `.git`, ni `node_modules`, ni `ressources/`.
   > ⚠️ Ne **jamais** faire synchroniser `.git` par Syncthing : deux appareils qui écrivent dans un dépôt git en parallèle le corrompent. Le dépôt git vit **uniquement sur le PC**.
3. Dossier ignoré : `.stignore` → `.git`, `*.tmp`, `*.sync-conflict-*`.

**Sur le Samsung Galaxy (One UI) :**
4. Installer **Syncthing-Fork** (F-Droid) — le client officiel a été retiré du Play Store en 2024.
5. Appairer les deux appareils, accepter le partage, cibler un dossier accessible à l'app Kokoro.
6. **Réglages One UI indispensables**, sans lesquels la synchro mourra silencieusement au bout de quelques heures — les mêmes que ceux exigés par Kokoro (PLAN §5) :
   - Paramètres → Batterie → Limites d'utilisation en arrière-plan → **Applications jamais mises en veille** → y ajouter Syncthing **et** Kokoro ;
   - désactiver l'**optimisation de la batterie** pour les deux.
   - ⚠️ Une mise à jour système One UI peut réinitialiser ces réglages. L'écran de diagnostic prévu dans Kokoro doit les vérifier.

**Vérification :**
7. Écrire un fichier témoin sur le PC, vérifier qu'il arrive sur le téléphone, et l'inverse.
8. Vérifier que `.git` n'a **pas** été synchronisé.

---

## 5. Reste à faire

- [ ] Installation Syncthing PC + Android — **Étape 5**
- [ ] `.stignore` à écrire au moment de l'installation
- [ ] Vérifier que le dépôt GitHub est bien privé et que la 2FA est active
- [ ] Décider si une sauvegarde froide hors-ligne (disque chiffré) s'ajoute au dispositif

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 09/08/2026 | Création — Étape 0. Décisions : dossier versionné dans le dépôt privé · Syncthing P2P pour le transport PC ↔ Android. |
