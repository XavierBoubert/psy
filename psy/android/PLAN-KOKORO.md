# Plan de construction — Kokoro (心) et la surface web

**Statut :** plan de construction — **v1.2 (10/08/2026)**. Ouvre l'**Étape 5** du `PLAN.md` racine. ✅ **K0 franchi le 10/08/2026.**
**Portée :** ce document dit **dans quel ordre on construit les applications, et pourquoi cet ordre-là**. Il ne redit ni la conception du personnage (→ [`README.md`](README.md)), ni les décisions d'architecture (→ `../../PLAN.md` §5), ni le format des données (→ [`../dossier/SCHEMA.md`](../dossier/SCHEMA.md), **normatif**).

> **Ce document fait foi sur le séquençage et les critères de fin.** Sur tout le reste, il pointe.

---

## 1. Constat de départ — et il est plus dur que « rien n'est écrit »

Vérifié le 10/08/2026 sur la machine, **au matin** :

| Élément | État initial | État à 12h03 |
|---|---|---|
| `psy/android/` | 1 README. **Zéro ligne de Kotlin.** | ✅ squelette Gradle + `MainActivity` |
| `psy/web/` | 1 README. **Zéro ligne de TypeScript.** | inchangé *(après K1, cf. §7)* |
| JDK | ❌ absent | ✅ **Microsoft OpenJDK 21.0.12** |
| SDK / `adb` | ❌ absent | ✅ **platform 36, build-tools 36.0.0, platform-tools 37.0.1** |
| Syncthing (PC et Android) | ❌ non installé | ❌ non installé *(prévu en K4 — [`../SYNCHRO.md`](../SYNCHRO.md) §4)* |
| Node | ✅ v24.14 | ✅ |

**Conséquence à écrire noir sur blanc :** la première tâche de Kokoro n'est pas de dessiner un visage, c'est **d'installer une chaîne de compilation et de faire tourner un APK sur le téléphone**. Tant que ce jalon n'est pas franchi, tout ce qui est écrit ici est de la littérature — et le dispositif vient précisément de se faire reprocher, en supervision, de produire de la doctrine plus vite que des actes.

> ✅ **C'est fait.** Le 10/08/2026 à 12h03, un APK debug s'est ouvert sur le **Galaxy S22 (SM-S901B, Android 16 / SDK 36)** et a affiché un écran vide. **Kokoro existe comme processus avant d'exister comme personnage** — et ce paragraphe cesse d'être une intention.

---

## 2. La contrainte qui commande tout : le calendrier

| Date | Événement | Effet sur ce plan |
|---|---|---|
| **jeu. 03/09** | Consultation Dr Isorni, 12h30 | **24 jours.** Le brief (29-30/08), l'email et le palier 0 PPC passent **avant** tout développement. |
| **lun. 07/09** | Départ Tunisie, **3 semaines ou plus** | **28 jours.** ⭐ Période déclarée **à haut risque de shutdown** ([`../dossier/etat.md`](../dossier/etat.md) §8) : rupture de routine, domicile-refuge indisponible, avion, foule. |
| **≈ 28/09** | Retour | Reprise des chantiers **un palier plus bas**, décidée à l'avance. |

> ⚠️ **Le conflit est réel et il faut le nommer.** Développer Kokoro n'est pas un chantier thérapeutique — la règle « un seul chantier à la fois » ne s'y applique pas —, mais il consomme la même ressource que le palier 0 PPC : **le temps de Xavier**. Les démarches PPC (télésuivi, origine de la fuite, essai d'interfaces, consultation de reprise) ne se font pas depuis la Tunisie ; le code, si. **En cas d'arbitrage, la PPC passe devant.**

---

## 3. Trois décisions de construction

### 3.1 ⭐ Kokoro ne commence pas par le visage

Le visage est la partie la plus visible, la plus plaisante à construire, et **la moins urgente**. Le bouton shutdown est l'inverse : invisible, austère, et il manque déjà.

Le mot-code « shutdown » est convenu avec Chourouk depuis le 09/08. La fiche explicative est écrite. **Ce qui n'existe pas, c'est le porteur** : au moment où la parole est coupée, Xavier doit aujourd'hui déverrouiller un téléphone, ouvrir une messagerie, trouver un contact et écrire un mot — quatre gestes de trop, dont un impossible, exactement quand le canal verbal tombe.

**Décision : le premier livrable de Kokoro est un écran de crise, pas un personnage.** Le personnage arrive au jalon **K5**.

> 🔴 **Précision du 10/08/2026, et elle change la nature de cet écran : il ne porte aucun numéro d'urgence.** Les numéros d'appel (15, 112, 114) ont été retirés de tout le dispositif à la demande de Xavier — motifs au [`../protocoles/crise-escalade.md`](../protocoles/crise-escalade.md) §0, dont le principal : ⭐ **une syncope vasovagale ne s'appelle pas, elle s'allonge.** L'écran porte **le mot-code à Chourouk** et **la tension appliquée** — les deux seules choses qui aient jamais servi. Il en sort plus simple, et plus juste cliniquement.

### 3.2 Une app unique, multi-modules — la question §5 est tranchée

Le `PLAN.md` la laissait ouverte en deux endroits (§3.1, §5). Elle était en réalité déjà décidée au §1.2.1 (tour 5) : « app Android compagnon **unique** ». On la ferme ici, avec son argument clinique :

**Trois apps, ce sont trois icônes à retrouver en situation.** En shutdown ou en pré-syncope, chercher la bonne icône est un coût cognitif ajouté au pire moment. Une seule surface, un seul point d'entrée, une seule permission d'overlay à accorder, un seul réglage batterie One UI à protéger.

→ **Modules Gradle internes** (`:core-dossier`, `:feature-crise`, `:feature-tension`, `:feature-journal`, `:feature-presence`), **un seul APK**.

### 3.3 Aucune base de données — l'app écrit des fichiers

`SCHEMA.md` R1/R2/R3 imposent : un fichier par événement, append-only, JSON pour ce qu'écrit une application. **Une base Room dupliquerait la source de vérité et créerait un état à réconcilier.**

→ Kokoro **écrit directement des fichiers JSON** dans le répertoire synchronisé par Syncthing, et **ne tient aucun cache persistant** des données cliniques. `"source": "kokoro"` dans chaque fichier écrit (R4 : `journal/` est écrit par une seule surface à la fois — la bascule PC → Android se déclare, elle ne se devine pas).

---

## 4. Les jalons

Chaque jalon a un **critère de fin vérifiable**. Un jalon n'est pas « fini » parce que le code compile : il l'est quand le critère est constaté sur le téléphone de Xavier.

### K0 — Le poste de travail *(prérequis absolu)* — ✅ **franchi le 10/08/2026**
- JDK 21 · ~~Android Studio~~ · SDK · mode développeur + débogage USB sur le Galaxy · `adb` qui voit le téléphone.
- Squelette Gradle : Kotlin, Compose, `minSdk 31`, `compileSdk` = la plus récente installée, **aucune dépendance réseau, aucune analytics**.
- **Critère de fin :** un APK debug installé par sideload s'ouvre sur le téléphone de Xavier et affiche un écran vide.

**Constat de fin — 10/08/2026, 12h03.** APK installé par `adb install`, `topResumedActivity = io.allonsy.kokoro/.MainActivity`, écran vide affiché. Téléphone : **Galaxy S22 (SM-S901B), Android 16 / SDK 36**.

| | |
|---|---|
| JDK | Microsoft OpenJDK **21.0.12** — `C:\Program Files\Microsoft\jdk-21.0.12.8-hotspot` |
| SDK | `%LOCALAPPDATA%\Android\Sdk` — platform **36**, build-tools **36.0.0**, platform-tools **37.0.1** |
| Variables utilisateur | `JAVA_HOME`, `ANDROID_HOME`, `PATH` posées dans `HKCU\Environment` *(persistantes)* |
| Gradle | **8.14.5** par wrapper · AGP **8.13.2** · Kotlin **2.2.21** · Compose BOM **2026.06.01** |
| Cibles | `minSdk 31` · `compileSdk 36` · `targetSdk 36` · `applicationId io.allonsy.kokoro` |

> ✅ **Android Studio n'a pas été installé** *(arbitrage de Xavier, 10/08/2026)* : outillage en ligne de commande seul, ~600 Mo au lieu de ~4 Go. Le critère de fin ne le demandait pas. **L'IDE reste installable plus tard sans rien casser** — il lirait le même projet Gradle.
>
> ⚠️ **Deux invariants sont vérifiables dès maintenant, et ils le sont** : le manifeste ne déclare **aucune permission** — pas même `INTERNET` — et le module ne dépend d'**aucun** SDK d'analytics ou de crash reporting.
>
> 📌 **Ce qui n'est volontairement pas fait :** aucun module `:feature-*` n'a été créé d'avance. Ils arrivent avec leur jalon. Des coquilles vides seraient exactement la doctrine-sans-acte que la supervision du 09/08 a relevée.

### K1 — ⚡ Le full-screen intent *(spike de faisabilité — arbitrage de Xavier, 10/08/2026)*

**Pourquoi ici, juste après K0 :** c'est **le point techniquement le plus risqué du projet**, et le seul dont l'échec invaliderait une partie de la conception. Depuis Android 14, `USE_FULL_SCREEN_INTENT` est réservé aux apps d'appel et d'alarme ; pour les autres, l'accès est **révoqué par défaut**. Le `PLAN.md` §5 le donnait pour acquis (« ✅ ») — **c'est faux depuis Android 14.** Le lever maintenant, sur un APK vide, coûte une soirée ; le découvrir en K5 coûterait une refonte.

- Déclarer `USE_FULL_SCREEN_INTENT`, vérifier `NotificationManager.canUseFullScreenIntent()`, et écrire l'**écran de guidage** qui envoie Xavier au bon réglage One UI s'il est refusé (Paramètres → Applications → Accès spécial → Notifications plein écran).
- Notification de test en `IMPORTANCE_HIGH` mais **canal muet** : `setSound(null)`, aucune vibration. ⚠️ Un full-screen intent par défaut **sonne** — c'est exactement ce que les hypersensibilités interdisent. **Le rendre silencieux fait partie du jalon, pas d'un réglage ultérieur.**
- **Critère de fin :** téléphone verrouillé et écran éteint, une notification déclenchée à distance allume l'écran et affiche l'Activity de Kokoro, **sans aucun son ni vibration**.

> ⭐ **Ce jalon décide de la suite.** S'il passe, l'interpellation (K6) et le réveil d'écran sont acquis. **S'il ne passe pas sur ce téléphone, Kokoro reste une app qu'on ouvre** — ce qui reste utile, mais doit être su avant de construire dessus, pas après.

### K2 — 🔴 Le noyau de crise *(le seul jalon avec une date cible : avant le 07/09)*

Un écran, deux actions, aucune saisie de texte, **aucun numéro d'urgence**, aucun réseau de données requis.

| Action | Mécanisme | Source |
|---|---|---|
| **Mot-code à Chourouk** | SMS **pré-rempli** au contact, texte = « shutdown ». ✅ **Canal validé par Chourouk le 10/08/2026** | `../protocoles/fiche-chourouk.md` |
| **Tension appliquée** | Minuteur nu (voir K3) | `tension-appliquee.md` |

- Accessible **depuis l'écran verrouillé** : Activity `setShowWhenLocked(true)` + `setTurnScreenOn(true)` (l'overlay classique passe **sous** le keyguard — ce n'est pas le bon mécanisme ici).
- 🔴 **Aucun numéro d'urgence, sous aucune forme** *(décision du 10/08/2026)*. Ni appel, ni SMS, ni « en cas de besoin ». Le **3114** n'apparaît pas non plus : il appartient à la conduite d'escalade sur idéation suicidaire (`crise-escalade.md` §2), **et un écran d'accueil n'est pas un déclencheur**.
- ✅ **Le mode étranger disparaît** — il n'existait que pour les numéros de substitution. Le mot-code fonctionne en Tunisie, et Chourouk y sera.
- **Critère de fin :** téléphone verrouillé, en un geste, le SMS « shutdown » est composé et prêt à envoyer, **sans réseau data et sans déverrouillage**.

### K3 — Tension appliquée guidée
- Cycles de contraction guidés selon [`../protocoles/tension-appliquee.md`](../protocoles/tension-appliquee.md) — **jamais selon un protocole reconstruit de mémoire**.
- ⭐ **Déclenchement sur repères externes et au chronomètre**, jamais sur un prodrome : la fiche a corrigé Öst sur ce point précis (déficit intéroceptif). L'app **ne demande jamais** « en as-tu besoin maintenant ? ».
- Guidage **visuel et silencieux** : aucune tonalité, aucune vibration non sollicitée.
- **Critère de fin :** un bloc complet se déroule en salle d'attente réelle, écran verrouillé au départ, sans son.

### K4 — Check-in quotidien + Syncthing *(migration du check-in sur Android)*
- Les **7 champs du noyau** + les champs `campagne` déclarés dans `etat.md` §4, en choix fermés et compteurs — `SCHEMA.md` §3, **strictement**.
- Écrit `journal/AAAA-MM-JJ.json`, `"source": "kokoro"`.
- Installation Syncthing PC + Syncthing-Fork Android selon [`../SYNCHRO.md`](../SYNCHRO.md) §4 (⚠️ **ne jamais synchroniser `.git`**).
- **Critère de fin :** un check-in saisi sur le téléphone apparaît dans `psy/dossier/journal/` sur le PC, valide au schéma, et le PC cesse d'écrire ce fichier le même jour.

### K5 — La présence *(Kokoro devient Kokoro)*
- Foreground service + overlay `SYSTEM_ALERT_WINDOW` / `TYPE_APPLICATION_OVERLAY`.
- Visage à **trait minimal, ligne claire** ; **état de repos : il respire, c'est tout** — micro-animation lente, sans information.
- Charge mesurée **consultable en un tap**, jamais affichée d'elle-même. **Jamais de tristesse ni de reproche**, en aucune circonstance.
- **Critère de fin :** l'overlay survit 72 h consécutives sans être tué par One UI.

### K6 — Interpellation et diagnostic
- Interpellation opportuniste selon `PLAN.md` §2.4 : **une phrase, une raison chiffrée, refus à coût nul, plafond 1/jour et 3/semaine**, jamais de son ni de plein écran.
- **Écran de diagnostic One UI** : vérifie les deux réglages batterie et guide leur réactivation (une mise à jour système les réinitialise).
- **Critère de fin :** après une mise à jour système simulée (réglages remis à zéro à la main), l'écran de diagnostic les signale.

---

## 5. Points durs Android — à traiter, pas à découvrir

| Point | Réalité | Traitement |
|---|---|---|
| **Full-screen intent** | Depuis Android 14, `USE_FULL_SCREEN_INTENT` est réservé aux apps d'appel/alarme ; pour les autres, l'accès est **révoqué par défaut**. Le `PLAN.md` §5 le donne pour acquis (« ✅ ») — **c'est faux depuis Android 14.** | ⚡ **Traité en K1**, avant tout le reste : c'est le risque le plus élevé du projet. Sideload : Xavier peut l'accorder à la main. À vérifier **sur son téléphone**, pas en théorie. |
| **Foreground service** | Depuis Android 14, un `foregroundServiceType` déclaré est obligatoire. | `specialUse` avec justification. Aucune contrainte de review : l'app est sideloadée. |
| **Notification persistante** | Une notification de service peut sonner. | Canal en `IMPORTANCE_LOW`, **aucun son, aucune vibration** — règle non négociable, pas une préférence. |
| **Accès aux fichiers du dossier** | Le stockage cloisonné empêche d'écrire librement dans un dossier partagé avec Syncthing. | Deux voies : `MANAGE_EXTERNAL_STORAGE` (simple, acceptable en sideload) ou SAF avec URI d'arbre persistant (`takePersistableUriPermission`). **À trancher en K4**, pas avant. |
| **One UI tue les services** | Documenté au `README.md` et à `SYNCHRO.md`. | Les deux réglages batterie se font **avant** K5, sinon le jalon est intestable. |

---

## 6. Les invariants, traduits en règles vérifiables

Une contrainte de conception qui reste une phrase se perd à l'implémentation. Version opérationnelle — **checklist de revue de chaque écran** :

| Invariant | Règle de code vérifiable |
|---|---|
| Jamais de son | L'app ne déclare **aucune** permission audio ; tout canal de notification est `IMPORTANCE_LOW` ou moins. |
| Jamais de vibration non sollicitée | Aucun appel `Vibrator` hors d'une action déclenchée par Xavier dans la seconde. |
| Transitions lentes et continues | Toute animation d'expression ≥ **800 ms**, easing continu. Aucune apparition instantanée d'élément. |
| Utilisable sans parler ni écrire | **Tout champ obligatoire est un nombre ou un choix fermé** (R5). Le texte libre est toujours facultatif et jamais bloquant. |
| Aucune visualisation | Aucun texte d'aide ne contient « imagine », « visualise », « représente-toi ». **Vérifié par un test qui grep les chaînes de l'app.** |
| Zéro streak | Aucun champ de compteur de régularité, de série, de pourcentage d'objectif ni de moyenne mobile affichée. **Vérifié à la revue de chaque écran.** |
| Aucune cotation de ressenti (R6) | Aucun libellé de la forme « note ton X sur 10 ». Toute question a une ancre comportementale. |
| Il explicite pourquoi | Toute interpellation porte sa raison chiffrée dans le même écran. |
| L'apparence ne change jamais sans annonce | Tout changement visuel entre deux versions est annoncé **avant** installation. |

---

## 7. La surface web — ce qu'elle devient dans ce séquençage

**Elle passe après K1, et ce n'est pas un rétrogradage.** Les échelles restantes (TAS-20, CAT-Q, BES, MAIA-2, GAD-7/PHQ-9) se passent les 16/08 et 22-23/08 — **avant** que le moindre outil web puisse exister. Les construire pour ces dates n'est pas tenable ; `psy-bilan` les conduit en conversation, et c'est suffisant.

| Outil | Quand | Pourquoi pas avant |
|---|---|---|
| **Schémas Zod du dossier** | Avec K4 | ⭐ C'est le vrai premier livrable web : **le contrat de données partagé** entre les surfaces. Aujourd'hui `SCHEMA.md` n'est validé par rien — une app qui écrit du JSON invalide le fait en silence. |
| Tableau de bord d'évolution | Après le retour (≈ 10/2026) | Il ne devient lisible qu'avec plusieurs semaines de journal. **Sans compteur de régularité**, jamais. |
| Passation d'échelles | Après le retour | Les passations urgentes sont faites d'ici là. |
| Stimulation bilatérale | Étape 6 | Instrument seul, aucun protocole de retraitement. |

---

## 8. Ce qui n'entrera jamais dans Kokoro

1. Un conseil, une suggestion ou un rappel touchant au **traitement** — même sous forme de question. Ça part au brief.
2. Un **streak**, un compteur de régularité, un pourcentage d'objectif, un « ça fait 4 jours ».
3. Un **son** ou une **vibration** non demandés.
4. Une consigne de **visualisation**, y compris dans un texte d'aide.
5. Une **expression de tristesse, de déception ou de reproche** sur le visage.
6. Un **service tiers** : pas de cloud, pas d'analytics, pas de crash reporting, pas de police de caractères distante. (`SYNCHRO.md` §2 : refusé par défaut.)
7. 🔴 **Un numéro d'urgence, sous quelque forme que ce soit** — appel, SMS, lien, texte d'aide *(décision du 10/08/2026, [`../protocoles/crise-escalade.md`](../protocoles/crise-escalade.md) §0)*. ⭐ **Y compris le 3114 :** il appartient à la conduite d'escalade sur idéation suicidaire, **pas à une interface**. Un écran qui l'affiche en permanence le transforme en décor, et c'est précisément ce qui angoissait sans jamais servir.

---

## 9. Arbitrages qui reviennent à Xavier

| # | Question | Recommandation |
|---|---|---|
| **A** | **K0 → K2 avant le 07/09, ou tout après le retour ?** | ⭐ **Avant.** Le séjour est la période à plus haut risque de shutdown du trimestre, et K2 est devenu **plus petit encore** depuis le retrait des numéros : un écran, deux actions, pas de service, pas de synchro. **Condition ferme : après le palier 0 PPC et le brief, jamais à leur place.** |
| ~~**B**~~ | ~~Canal SMS du mot-code à Chourouk~~ | ✅ **Validé par Chourouk le 10/08/2026.** |
| ~~**C**~~ | ~~Numéros de substitution pour la Tunisie~~ | ✅ **Sans objet le 10/08/2026** — les numéros sont sortis du dispositif, le mode étranger disparaît. |
| **D** | `MANAGE_EXTERNAL_STORAGE` ou SAF | Reportable à K4. |

---

| Version | Date | Modification |
|---|---|---|
| **1.2** | **10/08/2026** | ✅ **K0 franchi — le premier acte exécutable du dispositif sur Android.** JDK 21, SDK Android (platform 36 / build-tools 36 / platform-tools 37.0.1), variables utilisateur posées, squelette Gradle écrit, APK debug installé et ouvert sur le **Galaxy S22 (Android 16)**. §1 devient un avant/après daté, K0 gagne son constat de fin chiffré. ⭐ **Android Studio écarté** *(arbitrage de Xavier)* : outillage CLI seul, l'IDE n'était pas dans le critère de fin. **Aucun module `:feature-*` créé d'avance** — ils arrivent avec leur jalon. |
| 1.1 | 10/08/2026 | ⚡ **Le full-screen intent devient le jalon K1** *(arbitrage de Xavier)*, juste après le poste de travail : c'est le point le plus risqué du projet — le lever sur un APK vide coûte une soirée, le découvrir en K5 coûterait une refonte. Le jalon impose en plus le **canal muet**, un full-screen intent sonnant par défaut. Jalons suivants décalés (K2 crise · K3 tension · K4 check-in · K5 présence · K6 interpellation). 🔴 **L'écran de crise ne porte plus aucun numéro** — retrait acté dans tout le dispositif ; il se réduit au **mot-code** et à la **tension appliquée**, et le **mode étranger disparaît** avec les numéros de substitution. ✅ **Chourouk valide le canal SMS du mot-code** — arbitrage B levé. |
| 1.0 | 10/08/2026 | Création — ouverture de l'Étape 5. ⭐ Trois décisions : **le premier livrable est l'écran de crise, pas le visage** · **app unique multi-modules** (question ouverte du `PLAN.md` §3.1 et §5, tranchée) · **aucune base de données, l'app écrit des fichiers** (R1/R2/R3). Constat vérifié : aucun outillage Android sur la machine — le jalon K0 est l'installation. Nuance apportée au `PLAN.md` §5 : le **full-screen intent** n'est plus acquis par défaut depuis Android 14. |
