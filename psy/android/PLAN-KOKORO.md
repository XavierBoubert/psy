# Plan de construction — Kokoro (心) et la surface web

**Statut :** plan de construction — **v1.8 (12/08/2026)**. ⭐ **Changement de nature acté : Kokoro cesse d'être une app à fonctions et devient le porteur de la thérapie écrite par Claude Psy** (K5). ❌ **L'interpellation (ex-K6) est supprimée** — Kokoro ne notifie de rien.
**Statut précédent :** v1.7 (11/08/2026). Ouvre l'**Étape 5** du `PLAN.md` racine. ✅ **K0, K1 et K2 franchis le 10/08/2026**, K2 **sans réserve** — mot-code envoyé pour de vrai, réception confirmée par Chourouk. ✅ **K3 construit et vérifié sur l'appareil** ; ⏳ **son critère de fin — un bloc en salle d'attente réelle — reste ouvert**. ✅ **K4 franchi le 11/08/2026** — un **vrai check-in** saisi sur le téléphone est arrivé dans `dossier/journal/`, valide au schéma ; 🔴 **le transport passe par Google Drive** *(arbitrage de Xavier, Syncthing écarté)*.
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
| Transport du dossier | ❌ aucun | ✅ **Google Drive** *(K4, 11/08/2026 — ~~Syncthing~~ écarté par arbitrage, [`../SYNCHRO.md`](../SYNCHRO.md) §2.2)* |
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

→ Kokoro **écrit directement des fichiers JSON** dans le dossier de transit désigné *(Google Drive depuis le 11/08/2026 — ~~Syncthing~~ écarté)*, et **ne tient aucun cache persistant** des données cliniques. `"source": "android"` dans chaque fichier écrit (R4 : `journal/` est écrit par une seule surface à la fois — la bascule PC → Android se déclare, elle ne se devine pas).

> 📌 **Une seule exception, et elle est minuscule :** le **jeton local de date** ajouté à K4, qui empêche un second check-in le même jour. Il ne contient qu'une date, aucune réponse, et **sa perte ne perd rien**. À partir de K5 s'y ajoute la **version de programme déjà vue**, de même nature — un entier, pour savoir s'il faut afficher la ligne « le programme a changé ». Ni l'un ni l'autre n'est une donnée clinique.

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

### K1 — ⚡ Le full-screen intent *(spike de faisabilité — arbitrage de Xavier, 10/08/2026)* — ✅ **franchi le 10/08/2026**

**Pourquoi ici, juste après K0 :** c'est **le point techniquement le plus risqué du projet**, et le seul dont l'échec invaliderait une partie de la conception. Depuis Android 14, `USE_FULL_SCREEN_INTENT` est réservé aux apps d'appel et d'alarme ; pour les autres, l'accès est **révoqué par défaut**. Le `PLAN.md` §5 le donnait pour acquis (« ✅ ») — **c'est faux depuis Android 14.** Le lever maintenant, sur un APK vide, coûte une soirée ; le découvrir en K5 coûterait une refonte.

- Déclarer `USE_FULL_SCREEN_INTENT`, vérifier `NotificationManager.canUseFullScreenIntent()`, et écrire l'**écran de guidage** qui envoie Xavier au bon réglage One UI s'il est refusé (Paramètres → Applications → Accès spécial → Notifications plein écran).
- Notification de test en `IMPORTANCE_HIGH` mais **canal muet** : `setSound(null)`, aucune vibration. ⚠️ Un full-screen intent par défaut **sonne** — c'est exactement ce que les hypersensibilités interdisent. **Le rendre silencieux fait partie du jalon, pas d'un réglage ultérieur.**
- **Critère de fin :** téléphone verrouillé et écran éteint, une notification déclenchée à distance allume l'écran et affiche l'Activity de Kokoro, **sans aucun son ni vibration**.

> ⭐ **Ce jalon décide de la suite.** S'il passe, l'interpellation (K6) et le réveil d'écran sont acquis. **S'il ne passe pas sur ce téléphone, Kokoro reste une app qu'on ouvre** — ce qui reste utile, mais doit être su avant de construire dessus, pas après.

**Constat de fin — 10/08/2026.** Galaxy S22 (SM-S901B, Android 16 / SDK 36), APK debug `versionName=K1`. **Trois passages**, dont le dernier conduit par Xavier seul, sans PC.

| Ce qui a été constaté | Comment |
|---|---|
| **État de départ** : téléphone verrouillé, écran éteint | `dumpsys power` → `mWakefulness=Dozing` · `dumpsys display` → `mScreenState=DOZE` · `dumpsys window` → `mDreamingLockscreen=true` |
| **Déclenchement à distance** *(passages 1 et 2)* | `adb shell am broadcast -n io.allonsy.kokoro/.alerte.DeclencheurAlerte` |
| **Déclenchement sans PC** *(passage 3, par Xavier)* | Bouton dans l'app → `AlarmManager.setAndAllowWhileIdle`, 20 s, écran éteint entre-temps |
| **L'écran s'allume seul et Kokoro s'affiche par-dessus le verrouillage** | `mWakefulness=Awake` · `mScreenState=ON` · `topResumedActivity=io.allonsy.kokoro/.alerte.AlerteActivity` · capture d'écran · ✅ **constaté de visu par Xavier** |
| **Aucun son, aucune vibration** | `dumpsys notification` → canal `mImportance=4`, `mSound=null`, `mVibrationEnabled=false`, `mVibrationPattern=null`, `mLights=false` ; notification `sound=null vibrate=null defaults=0` · ✅ **confirmé à l'oreille par Xavier, sonnerie active** |

> ⭐ **La restriction d'Android 14 n'a pas bloqué sur ce téléphone.** `USE_FULL_SCREEN_INTENT` est **accordée à l'installation** (`granted=true`), l'app op reste en mode `default`, et `canUseFullScreenIntent()` renvoie **vrai** sans aucune manipulation de réglage. **Ce n'est pas une raison de retirer l'écran de guidage** : il reste dans l'app, et il est la seule chose qui sauvera le jalon si une mise à jour One UI change ce comportement — comme elle réinitialise déjà les réglages batterie (K6).
>
> 🔦 **Un réveil d'écran explicite a été nécessaire, et il faut savoir pourquoi.** `setTurnScreenOn(true)` seul laisse l'Always On Display de Samsung s'intercaler : l'écran « s'allume » sans afficher l'app. Le déclencheur prend donc un `SCREEN_BRIGHT_WAKE_LOCK or ACQUIRE_CAUSES_WAKEUP` de 10 s avant de publier la notification — d'où la permission **`WAKE_LOCK`**. L'API est dépréciée depuis longtemps et reste le seul moyen fiable ; c'est ce qu'emploient les réveils.
>
> ⭐ **Comportement découvert et vérifié, à ne pas prendre pour un défaut : quand l'écran est allumé et le téléphone en cours d'utilisation, Android ne lance PAS le full-screen intent** — il le rétrograde en bannière, qu'il faut toucher. Constaté à 13h26 : broadcast reçu, `topResumedActivity` resté sur `MainActivity`. **C'est un comportement voulu du système, et il coïncide avec la doctrine du dispositif** — `PLAN-KOKORO.md` K6 pose déjà que l'interpellation ne prend jamais le plein écran. Autrement dit : **Kokoro ne peut pas saisir l'écran pendant que Xavier s'en sert, même par erreur de programmation.** C'est une garantie, pas une limite.
>
> ⚠️ **Une seule réserve subsiste.** Les trois passages ont eu lieu avec l'app récemment au premier plan et le téléphone `deviceidle=ACTIVE`. **Le déclenchement depuis un processus froid, après plusieurs heures de veille profonde, reste à observer** — c'est la condition réelle de K5/K6, et elle ne se teste qu'en laissant passer une nuit.
>
> 📌 **Détail d'implémentation qui se perdra si on ne l'écrit pas : un canal de notification est immuable une fois créé.** Changer le son ou la vibration dans le code ne change **rien** sur un téléphone où le canal existe déjà. D'où l'identifiant versionné **`kokoro_alerte_v1`** : toute modification des réglages du canal impose de passer à `_v2`, sans quoi la règle « jamais de son » serait violée en silence sur le seul téléphone qui compte.
>
> 🔒 **Le déclencheur à distance n'existe qu'en build de debug** (`app/src/debug/AndroidManifest.xml`, `tools:replace="android:exported"`). En release, le récepteur est fermé.
>
> **Trois permissions apparaissent au manifeste** — `POST_NOTIFICATIONS`, `USE_FULL_SCREEN_INTENT` et `WAKE_LOCK`. L'invariant « aucune permission » de K0 tombe ici, comme prévu : **aucune n'est réseau, aucune n'est capteur, aucune ne touche au micro, il n'y a toujours ni `INTERNET` ni analytics.**

### K2 — 🔴 Le noyau de crise *(le seul jalon avec une date cible : avant le 07/09)* — ✅ **franchi le 10/08/2026**

Un écran, deux actions, aucune saisie de texte, **aucun numéro d'urgence**, aucun réseau de données requis.

| Action | Mécanisme | Source |
|---|---|---|
| **Mot-code à Chourouk** | SMS **pré-rempli** au contact, texte = « shutdown ». ✅ **Canal validé par Chourouk le 10/08/2026** | `../protocoles/fiche-chourouk.md` |
| **Tension appliquée** | Minuteur nu (voir K3) | `tension-appliquee.md` |

- Accessible **depuis l'écran verrouillé** : Activity `setShowWhenLocked(true)` + `setTurnScreenOn(true)` (l'overlay classique passe **sous** le keyguard — ce n'est pas le bon mécanisme ici).
- 🔴 **Aucun numéro d'urgence, sous aucune forme** *(décision du 10/08/2026)*. Ni appel, ni SMS, ni « en cas de besoin ». Le **3114** n'apparaît pas non plus : il appartient à la conduite d'escalade sur idéation suicidaire (`crise-escalade.md` §2), **et un écran d'accueil n'est pas un déclencheur**.
- ✅ **Le mode étranger disparaît** — il n'existait que pour les numéros de substitution. Le mot-code fonctionne en Tunisie, et Chourouk y sera.
- **Critère de fin :** téléphone verrouillé, en un geste, le SMS « shutdown » est composé et prêt à envoyer, **sans réseau data et sans déverrouillage**.

**Constat de fin — 10/08/2026, 15h53.** Galaxy S22 (SM-S901B, Android 16 / SDK 36), APK debug `versionName=K2`. Séquence conduite écran éteint au départ, **verrouillage jamais levé** — `deviceLocked=1` et `isKeyguardShowing=true` vérifiés **à chaque étape**, y compris sur l'écran final.

| Ce qui a été constaté | Comment |
|---|---|
| **État de départ** : écran éteint, téléphone verrouillé | `dumpsys power` → `mWakefulness=Dozing` · `dumpsys display` → `mScreenState=DOZE` · `dumpsys window` → `mDreamingLockscreen=true`, `isKeyguardShowing=true` · `dumpsys trust` → **`deviceLocked=1`** |
| **La notification d'accès est là, sur l'écran verrouillé** | Capture : « Kokoro — mot-code · tension appliquée », au-dessus du verrouillage |
| **Un tap ouvre l'écran de crise par-dessus le verrouillage** | `topResumedActivity=io.allonsy.kokoro/.crise.CriseActivity` · `mKeyguardOccluded=true` · ⭐ **`deviceLocked=1` — aucun déverrouillage, aucun code demandé** |
| **Le SMS est composé et prêt à envoyer** | Écran « À Chourouk / **shutdown** / Envoyer » · `deviceLocked=1` |
| ⭐ **Le SMS part réellement, et Chourouk le reçoit** | **Passage conduit par Xavier seul, sans PC** *(téléphone débranché)* : vrai numéro enregistré, **téléphone verrouillé, envoi depuis la notification**, ✅ **réception confirmée par Chourouk** |
| **La tension appliquée se déroule au compteur** | `CONTRACTE 12 · Cycle 1 sur 5` puis `RELÂCHE 18 · Cycle 1 sur 5`, toujours `deviceLocked=1` |
| **Aucun son, aucune vibration** | Canal `kokoro_acces_v1` : `mImportance=2`, `mSound=null`, `mVibrationEnabled=false`, `mLights=false` ; notification `sound=null vibrate=null defaults=0 flags=ONGOING_EVENT|SILENT` · **aucun `Vibrator`, aucune API audio dans le code, aucune permission audio** |

> ⭐ **Le mot-code a enfin un porteur.** C'était l'objet du jalon et c'est le seul énoncé qui compte : au 09/08, atteindre Chourouk depuis un shutdown demandait quatre gestes dont un impossible (déverrouiller, ouvrir une messagerie, trouver le contact, **écrire**). Il en demande deux, **aucun n'exige d'écrire**, et le verrouillage n'est jamais levé.
>
> 📌 **Précision sur « en un geste », et elle est tenue de bout en bout.** Sur l'écran verrouillé, One UI affiche la notification **repliée** : ses deux boutons d'action n'apparaissent qu'après dépliage. Le tap sur le corps de la notification ouvre donc l'**écran à deux boutons**, et le SMS composé arrive au **deuxième tap**. ✅ **Arbitrage de Xavier, 10/08/2026 : on garde cet écran-là.** Le motif est clinique et il prime sur la lettre du critère — les deux mécanismes restent **symétriques**, chacun étiqueté par son repère observable (« la parole est coupée » · « aiguille, geste médical, sang »), et **rien ne privilégie une parade sur l'autre**. Confondre les deux aggrave (`profil.md` §3) ; économiser un tap au prix de ce risque-là aurait été un mauvais échange. **Le critère est donc tenu en deux taps, et c'est écrit ici plutôt que réputé « un ».**
>
> ✅ **La réserve qui comptait est levée le jour même — le mot-code a été envoyé pour de vrai, et il est arrivé.** La vérification conduite depuis le PC s'était faite sur un **numéro fictif**, et le constat portait alors une réserve entière : *un porteur de mot-code jamais essayé n'est pas un porteur de mot-code.* **Xavier a fait l'essai à froid, seul, téléphone débranché** — vrai numéro enregistré, **téléphone verrouillé**, envoi **depuis la notification**, et ⭐ **Chourouk a confirmé la réception**. **La chaîne est donc vérifiée de bout en bout, dans les conditions réelles d'emploi**, et pas seulement jusqu'au bouton.
>
> ⭐ **Ce que cet essai vaut, au-delà de la technique.** Il a été fait **à froid, en la prévenant** — c'est-à-dire exactement dans les conditions où l'on apprend un geste, et jamais en situation. C'est la même règle que le §3 de [`../protocoles/tension-appliquee.md`](../protocoles/tension-appliquee.md) : *la technique s'apprend quand elle ne sert pas.* **La première fois que Chourouk recevra ce mot ne sera pas la première fois qu'elle le reçoit.**
>
> ⚠️ **Une permission nouvelle, et elle mérite d'être nommée : `SEND_SMS`.** Elle est la conséquence directe du critère « sans déverrouillage » : passer la main à l'application Messages aurait imposé le code PIN, c'est-à-dire exactement ce que le jalon interdit. Kokoro compose donc lui-même et envoie sur pression d'un bouton. **Le message n'est jamais envoyé automatiquement** — la confirmation est un geste délibéré, sur un écran qui affiche en toutes lettres le destinataire et le texte. **Si la permission est refusée, l'app ne casse pas** : elle bascule sur l'application Messages avec le message pré-rempli, **et l'écrit** (« le téléphone demandera le déverrouillage »). **Cinq permissions au total, aucune réseau, aucune capteur, aucune micro — toujours ni `INTERNET` ni analytics.**
>
> 📌 **L'accès se republie au démarrage du téléphone** (`RECEIVE_BOOT_COMPLETED`) et à chaque ouverture de l'app. ⚠️ **Non vérifié : le comportement après un vrai redémarrage** — le test demande de redémarrer le téléphone de Xavier, ce qui n'a pas été fait sans son accord.
>
> 🧪 **Deux invariants du §6 cessent d'être des phrases et deviennent des tests qui échouent.** `InvariantsTextesTest` lit `strings.xml` et interdit : toute consigne de **visualisation**, toute **cotation de ressenti** (R6), tout vocabulaire de **régularité ou de série**, **tout numéro d'appel d'urgence retiré le 10/08**, et les consignes de **relaxation délétères sur un vasovagal**. `MinuteurTensionTest` verrouille le protocole d'Öst tel qu'il est écrit dans la fiche — 15 s / 20 s, 5 cycles, 175 s — pour qu'une retouche d'interface ne puisse pas le déformer en silence. **Ils tournent à chaque compilation.**
>
> 🔴 **Ce que cet écran ne fait pas, et c'est délibéré : il ne pose aucune question de triage.** `crise-escalade.md` §1 impose la question de sécurité **avant** le mécanisme — mais cette conduite appartient au skill `psy-crise`, en conversation. **Un écran d'accueil n'est pas un déclencheur d'escalade** (§8.7) : y poser une question exigerait de répondre, ce que le shutdown interdit, et afficherait le 3114 en permanence — les deux choses que le retrait du 10/08 a précisément corrigées. **L'écran porte des parades, pas un triage.**

### K3 — Tension appliquée guidée — ✅ **construit le 10/08/2026** · ⏳ **critère de fin non tenu : la salle d'attente réelle manque**
- Cycles de contraction guidés selon [`../protocoles/tension-appliquee.md`](../protocoles/tension-appliquee.md) — **jamais selon un protocole reconstruit de mémoire**.
- ⭐ **Déclenchement sur repères externes et au chronomètre**, jamais sur un prodrome : la fiche a corrigé Öst sur ce point précis (déficit intéroceptif). L'app **ne demande jamais** « en as-tu besoin maintenant ? ».
- Guidage **visuel et silencieux** : aucune tonalité, aucune vibration non sollicitée.
- **Critère de fin :** un bloc complet se déroule en salle d'attente réelle, écran verrouillé au départ, sans son.

**Constat — 10/08/2026, 16h40-16h55.** Galaxy S22 (SM-S901B, Android 16 / SDK 36), APK debug `versionName=K3`. Séquence conduite **écran éteint au départ**, `deviceLocked=1` **vérifié à chaque étape**, verrouillage jamais levé.

| Ce qui a été constaté | Comment |
|---|---|
| **État de départ** : écran éteint, verrouillé | `mWakefulness=Dozing` · `mScreenState=DOZE` · `isKeyguardShowing=true` · **`deviceLocked=1`** |
| **L'écran tension s'ouvre par-dessus le verrouillage** | Tap sur la notification de l'écran verrouillé → `topResumedActivity=…/.crise.CriseActivity` · `mKeyguardOccluded=true` · `deviceLocked=1` |
| ⭐ **La séquence de repères de la fiche §2 est à l'écran, dans l'ordre** | *Franchir la porte* (5 cycles) · *S'asseoir dans le fauteuil* (5 cycles) · *Voir le plateau, le garrot, l'aiguille* (**cycles enchaînés, sans terme**) · *Après le geste* (3 cycles, puis 5 minutes assis) |
| **Un bloc complet de 5 cycles va à son terme** | Démarré 16h52, « Bloc terminé — les 5 cycles sont allés au bout » à 16h55 (**175 s**), écran resté allumé, `deviceLocked=1` |
| **Le bloc d'après-geste fait bien 3 cycles, pas 5** | « Les 3 cycles sont allés au bout » (**105 s**), puis bouton **Rester assis — 5 minutes**, décompte `4:55` → `0:00` |
| **La phrase pour le soignant est affichable et montrable** | Écran plein texte, « cet écran peut être montré tel quel » |
| ⭐ **Consulter la phrase n'arrête pas le bloc** | Ouverte au cycle 1, retour au **cycle 3** : le compte a couru pendant la consultation |
| **Aucun son, aucune vibration** | Canaux `kokoro_acces_v1` (`mImportance=2`) et `kokoro_alerte_v1` (`mImportance=4`) : `mSound=null`, `mVibrationEnabled=false`, `mLights=false` ; notifications `sound=null vibrate=null defaults=0` · **aucune API audio, aucun `Vibrator`, aucune permission audio dans le code** |
| **Aucune permission nouvelle** | Cinq permissions, inchangées depuis K2 |

> ⏳ **Le critère de fin n'est pas tenu, et il ne peut pas l'être depuis le PC.** Il dit « **en salle d'attente réelle** ». Ce qui est vérifié ici, c'est que l'outil fonctionne de bout en bout depuis un écran verrouillé, en silence — pas qu'il tienne en situation. **Le jalon reste donc ouvert jusqu'à un vrai passage.** C'est la même forme de réserve que K2, et elle s'était révélée juste : là-bas, seul l'envoi réel avait fait la preuve. **Ici l'occasion ne se commande pas** — elle viendra avec un geste médical programmé, et le palier 4 de la fiche §3 (la séquence répétée **à blanc**) est la répétition qui prépare ce passage sans l'attendre.
>
> ⭐ **Ce que K3 ajoute au minuteur nu de K2, et c'est tout le jalon.** K2 offrait un compteur : *démarrer, 5 cycles, terminé*. Il fallait donc savoir **quand** démarrer — c'est-à-dire s'appuyer sur une perception qui manque. K3 met les **quatre repères extérieurs de la fiche §2 sur l'écran** : on ne touche pas un bouton parce qu'on sent quelque chose, on le touche **parce qu'on vient de franchir une porte**. La règle « signal interne absent → structure externe » cesse d'être appliquée par le texte de la fiche et l'est par l'interface.
>
> 🔴 **Trois choses que cet écran ne fait délibérément pas.** Il ne demande **jamais** « en as-tu besoin ? » — la question exigerait la perception absente, et un test échoue si la formule apparaît dans un texte de l'app. Il **ne compte rien d'un jour à l'autre** : aucun palier, aucune série, aucun historique — les paliers de la fiche §3 se cotent **en séance**, et le journal n'a pas de champ pour ça (fiche §6). Il **ne bloque aucun repère** : les quatre restent touchables dans n'importe quel ordre, seul « repère suivant » guide — un ordre imposé se retournerait contre un déroulé réel qui ne suit pas le script.
>
> 📌 **Deux défauts trouvés en passant sur l'appareil, corrigés le jour même.** Le texte de fin annonçait « les **5** cycles sont allés au bout » sur un bloc de **3** — un chiffre en dur qui **contredisait le protocole affiché juste au-dessus** ; il est désormais paramétré par le bloc réel. Et le contenu passait **sous la barre d'état** sur les écrans longs (`safeDrawingPadding` posé sur la surface de crise et sur l'écran de réglage). Aucun des deux ne se voyait à la compilation : ils se voyaient à l'écran.
>
> 🧪 **Deux tests nouveaux, et une famille d'invariant nouvelle.** `SequenceSoinsTest` verrouille la séquence contre la fiche — quatre repères, dans cet ordre, 5 / 5 / **enchaînés** / 3 cycles, puis 300 s assis : une retouche d'interface ne peut plus la déformer en silence. `InvariantsSourcesTest` lit **les sources Kotlin** et échoue si une API de son, de vibration ou de réseau y apparaît — l'invariant « jamais de son » cessait d'être vérifié dès qu'on sortait de `strings.xml`. Et `InvariantsTextesTest` interdit désormais **le déclenchement sur prodrome** (« as-tu besoin », « quand tu sens », « aux premiers signes ») : c'est l'écart à Öst assumé par la fiche, il est maintenant gardé par un test. **22 tests, tous verts.**
>
> 🔒 **Le harnais de vérification est en debug seul.** `crise.DeclencheurCrise` — qui ouvre l'écran depuis le PC, téléphone verrouillé — vit dans `app/src/debug/kotlin` : la classe **n'existe pas dans le build release**. Il a fallu l'écrire parce que `CriseActivity` n'est pas exportée : `adb shell am start` s'y casse sur une `SecurityException`, ce qui est **la bonne nouvelle** — aucune autre application ne peut ouvrir l'écran de crise.
>
> 📌 **`CriseActivity` est verrouillée en portrait.** Une rotation redémarrait l'Activity et perdait le bloc en cours ; en pré-syncope on s'allonge, et l'auto-rotation aurait choisi ce moment-là. La prévisibilité de l'interface est ici une fonction, pas une préférence.

### K4 — Check-in quotidien + transport du dossier — ✅ **franchi le 11/08/2026**
- Les **7 champs du noyau** + les champs `campagne` déclarés dans `etat.md` §4, en choix fermés et compteurs — `SCHEMA.md` §3, **strictement**.
- Écrit `journal/AAAA-MM-JJ.json`, `"source": "android"`.
- 🔴 **Transport : Google Drive** *(arbitrage de Xavier, 11/08/2026 — Syncthing écarté)*. Objection du dispositif, décision maintenue, **tracée entière dans [`../SYNCHRO.md`](../SYNCHRO.md) §2.2**. ⚠️ **Ne jamais faire synchroniser `.git` par quoi que ce soit** : le dossier Drive est **hors dépôt**, et `npm run journal-ingest` copie vers `psy/dossier/journal/` sans jamais écraser.
- **Critère de fin :** un check-in saisi sur le téléphone apparaît dans `psy/dossier/journal/` sur le PC, valide au schéma, et le PC cesse d'écrire ce fichier le même jour.

**Constat — 11/08/2026, 20h36-20h46.** Galaxy S22 (SM-S901B, Android 16 / SDK 36), APK debug `versionName=K4`.

| Ce qui a été constaté | Comment |
|---|---|
| **L'écran de check-in pose les 11 questions dans l'ordre du skill** | `Question 1 sur 11` · énoncés **mot pour mot** de `psy-journal` §2 · compteurs (−5 / −1 / +1 / +5) et choix fermés, **aucune saisie de texte** hors `notes` facultatif |
| ⭐ **Google Drive est sélectionnable comme dossier d'écriture** | Sélecteur système → Drive `xavier@allons-y.io` → Mon Drive → `psy-journal` → **« UTILISER CE DOSSIER »**, autorisation accordée à Kokoro |
| **Kokoro crée `journal/` et y écrit** | `Ecrit(nom=2000-01-01.json)` — sous-dossier créé par l'app |
| ⭐ **Le contenu écrit est identique au gabarit du dossier** | Relu depuis Drive : mêmes clés, même ordre, `"source": "android"`, tous les champs à `null` |
| ⭐ **Le fichier redescend sur le PC** | `H:\Mon Drive\psy-journal\journal` — dossier créé sur le téléphone, visible sur la machine |
| **Aucune permission nouvelle** | Cinq permissions, inchangées depuis K2. **Ni `INTERNET`, ni SDK Google, ni OAuth dans l'app** |

> ⭐ **Arbitrage D tranché : SAF, pas `MANAGE_EXTERNAL_STORAGE`.** Xavier désigne **un** dossier, Kokoro n'obtient de droit que sur celui-là, et **aucune permission n'entre au manifeste**. C'est ce qui rend le transport interchangeable : Drive aujourd'hui, autre chose demain, sans toucher une ligne du code d'écriture.
>
> ⚠️ **Une objection du dispositif s'est révélée fausse, et il faut l'écrire : Drive *est* sélectionnable en arbre de documents.** Le contournement prévu — écrire en local puis proposer un envoi — **n'a pas été nécessaire**. Kokoro écrit directement dans Drive, sans réseau déclaré : c'est l'application Drive qui transporte.
>
> 🔴 **Une autre objection s'est révélée juste, et pire que prévu : Drive accepte deux fichiers du même nom dans un même dossier.** Constaté : deux écritures à 25 s d'intervalle ont produit **deux `2000-01-01.json`**. Un système de fichiers l'aurait refusé ; Drive non, et il ne signale rien. **Correctif câblé le jour même** : la garde ne repose plus sur l'interrogation du dossier seule, mais **aussi sur un jeton local de date** — qui ne contient qu'une date, aucune réponse, et dont la perte ne perd rien. Vérifié : deuxième écriture **refusée**, un seul fichier. Côté PC, `journal-ingest` **refuse d'écraser** et signale tout nom hors convention.
>
> 📌 **Un défaut d'interface trouvé sur l'appareil, corrigé le jour même** : le dossier désigné s'affichait en identifiant Drive opaque (`acc=2;doc=encoded=L9caT1…`) au lieu de son nom. Il affiche désormais **`psy-journal`**. Comme à K3, il ne se voyait pas à la compilation.
>
> ⭐ **Le critère de fin est tenu le jour même, et par un vrai check-in.** [`../dossier/journal/2026-08-11.json`](../dossier/journal/2026-08-11.json) a été **saisi par Xavier sur le téléphone**, transporté par Drive, versé par `npm run sync` : `"source": "android"`, onze champs, **valide au schéma**. C'est le **deuxième fichier du journal** du dispositif, et le premier qui n'ait pas été écrit depuis le PC.
>
> 📌 **Ce qui a servi de vérification avant lui n'était pas un check-in, et c'est délibéré.** Un check-in écrit par le PC et daté d'aujourd'hui aurait été une **donnée clinique fabriquée** : le témoin portait donc la date absurde du **01/01/2000**, et il a été supprimé après usage. **Le premier vrai check-in appartenait à Xavier**, exactement comme l'envoi réel du mot-code à K2.
>
> ⚠️ **La seconde moitié du critère est une conduite, pas un état** : *le PC cesse d'écrire ce fichier le même jour*. Elle est câblée dans le skill `psy-journal` §5.1 — vérifier avant d'écrire, ne jamais rattraper un jour saisi ailleurs, **la bascule se déclare et ne se devine pas** (R4 : une seule surface à la fois).
>
> 🔒 **`TemoinJournal` est en debug seul** (`app/src/debug/kotlin`) : la classe n'existe pas dans le build release.

**Reste à faire, et ce sont des gestes de Xavier :**
1. Vérifier que le dossier Drive **n'est partagé avec personne** et que la 2FA est active sur le compte Google (`SYNCHRO.md` §2.2).
2. Trancher l'arbitrage **E** ci-dessous — le compte Google du dossier de transit.

### K5 — ⭐ Le programme *(Kokoro porte la thérapie)* — 🔴 **jalon en cours**

**Le changement de nature du projet, et il faut le dire ainsi :** jusqu'ici, chaque besoin clinique nouveau demandait un écran nouveau — donc un jalon, donc du code. **À partir de K5, Kokoro n'apprend plus rien : il lit.** Claude Psy écrit la thérapie dans `psy/programme/programme.json`, Kokoro l'affiche, et renvoie ce que Xavier a fait. Ajouter une démarche, un exercice ou un questionnaire cesse d'être un acte de développement pour devenir un acte clinique, fait en séance.

- **Format :** [`../programme/FORMAT.md`](../programme/FORMAT.md) — **normatif**, partagé par les deux côtés. Cinq types d'étapes : `ecran` · `exercice` · `questionnaire` · `demarche` · `fiche`.
- **Écran d'accueil** : la liste, groupée en *aujourd'hui* · *quand j'en ai besoin* · *sans date*. **Aucun score, aucune progression, aucun historique.**
- **Annonce des changements** : une ligne discrète en haut quand la version publiée a changé — **jamais une notification** *(arbitrage de Xavier, 12/08/2026 : annoncé, non bloquant)*.
- **Écrit** `reponses/AAAA-MM-JJ-HHMM-<id>.json` dans le dossier de transit, versé au dossier par `npm run sync`.
- **Critère de fin :** une étape publiée depuis le PC apparaît sur le téléphone, est faite par Xavier, et sa réponse revient dans `psy/dossier/` — valide au format, sans intervention manuelle.

> 🔴 **Le point dur de ce jalon n'est pas la lecture d'un JSON, c'est un trou de sécurité qu'il ouvre.** Les tests de Kokoro vérifient aujourd'hui les textes de l'app (`InvariantsTextesTest`). **Ces textes-là ne seront plus dans l'app** : visualisation, cotation de ressenti, streak, numéro d'urgence, conseil de traitement redeviendraient possibles, **et en silence**.
> **Double garde, l'une existe déjà :** `npm run publish` **refuse la publication entière** *(écrit et vérifié le 12/08/2026 — les 7 familles d'interdits sont attrapées)* ; **Kokoro écarte la seule étape fautive** et affiche le reste, parce que sur le téléphone on ne peut pas corriger et que perdre tout le programme pour une ligne serait pire.

> ⭐ **Ce que ce jalon ne fait pas, et ne fera jamais : notifier.** *(Décision de Xavier, 12/08/2026.)* **Tant que Xavier ne vient pas vers Kokoro, Kokoro ne lui signale rien.** Seule exception, inchangée depuis K2 : la notification d'accès crise sur l'écran verrouillé — **c'est une porte, pas un rappel.**

### K6 — La présence *(Kokoro devient Kokoro)* — *ex-K5*
- Foreground service + overlay `SYSTEM_ALERT_WINDOW` / `TYPE_APPLICATION_OVERLAY`.
- Visage à **trait minimal, ligne claire** ; **état de repos : il respire, c'est tout** — micro-animation lente, sans information.
- Charge mesurée **consultable en un tap**, jamais affichée d'elle-même. **Jamais de tristesse ni de reproche**, en aucune circonstance.
- **Écran de diagnostic One UI** *(repris de l'ancien K6)* : vérifie les deux réglages batterie et guide leur réactivation — une mise à jour système les réinitialise, et l'overlay ne survit pas sans eux.
- **Critère de fin :** l'overlay survit 72 h consécutives sans être tué par One UI, et l'écran de diagnostic signale des réglages remis à zéro à la main.

### ~~K6 — Interpellation~~ — ❌ **supprimé le 12/08/2026**

**Motif :** *« tant que Xavier ne vient pas vers Kokoro, Kokoro ne lui notifie de rien »* (décision de Xavier, 12/08/2026). L'interpellation opportuniste du `PLAN.md` §2.4 — une phrase, une raison chiffrée, plafond 1/jour et 3/semaine — **est incompatible avec ce principe**, quelle que soit sa modération.

> 📌 **Ce n'est pas une perte, et le constat de K1 l'avait déjà anticipé sans le savoir.** Android rétrograde un full-screen intent en bannière dès que le téléphone est en cours d'usage : le système empêchait déjà Kokoro d'interrompre Xavier. **La décision du 12/08 ne fait qu'aligner l'intention sur ce que la plateforme garantissait déjà.** L'**écran de diagnostic One UI**, seul élément de l'ancien K6 qui restait nécessaire, est repris dans le nouveau K6.

---

## 5. Points durs Android — à traiter, pas à découvrir

| Point | Réalité | Traitement |
|---|---|---|
| **Full-screen intent** | ✅ **Levé le 10/08/2026 sur le Galaxy S22** : la permission est accordée à l'installation et `canUseFullScreenIntent()` renvoie vrai sans manipulation. La restriction d'Android 14 existe, elle ne s'est pas appliquée ici. | ✅ **Traité en K1.** L'écran de guidage vers le réglage One UI **reste dans l'app** : une mise à jour système peut changer ce comportement, comme elle réinitialise les réglages batterie. |
| **Foreground service** | Depuis Android 14, un `foregroundServiceType` déclaré est obligatoire. | `specialUse` avec justification. Aucune contrainte de review : l'app est sideloadée. |
| **Notification persistante** | Une notification de service peut sonner. | Canal en `IMPORTANCE_LOW`, **aucun son, aucune vibration** — règle non négociable, pas une préférence. |
| **Accès aux fichiers du dossier** | Le stockage cloisonné empêche d'écrire librement dans un dossier partagé. | ✅ **Tranché en K4 (11/08/2026) : SAF, URI d'arbre persistant.** `MANAGE_EXTERNAL_STORAGE` est écarté — il ouvre tout le stockage pour écrire dans un seul dossier. Conséquence : **aucune permission au manifeste**, et le transport est interchangeable sans toucher au code d'écriture. |
| **Un dossier Drive n'est pas un système de fichiers** | 🔴 Drive **accepte deux fichiers du même nom** dans un dossier, et ne le signale pas. | Garde **double** : jeton local de date + interrogation du dossier. Côté PC, `journal-ingest` refuse d'écraser. |
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
7. 🔴 **Une notification, un rappel, une relance** *(décision du 12/08/2026)*. **Kokoro ne vient jamais vers Xavier — Xavier vient vers Kokoro.** Seule exception, inchangée : la notification d'accès crise sur l'écran verrouillé, qui est **une porte, pas un rappel** ; elle ne dit rien, ne demande rien, et n'apparaît pas parce qu'il s'est passé quelque chose.
8. 🔴 **Un numéro d'urgence, sous quelque forme que ce soit** — appel, SMS, lien, texte d'aide *(décision du 10/08/2026, [`../protocoles/crise-escalade.md`](../protocoles/crise-escalade.md) §0)*. ⭐ **Y compris le 3114 :** il appartient à la conduite d'escalade sur idéation suicidaire, **pas à une interface**. Un écran qui l'affiche en permanence le transforme en décor, et c'est précisément ce qui angoissait sans jamais servir.

---

## 9. Arbitrages qui reviennent à Xavier

| # | Question | Recommandation |
|---|---|---|
| **A** | **K0 → K2 avant le 07/09, ou tout après le retour ?** | ⭐ **Avant.** Le séjour est la période à plus haut risque de shutdown du trimestre, et K2 est devenu **plus petit encore** depuis le retrait des numéros : un écran, deux actions, pas de service, pas de synchro. **Condition ferme : après le palier 0 PPC et le brief, jamais à leur place.** |
| ~~**B**~~ | ~~Canal SMS du mot-code à Chourouk~~ | ✅ **Validé par Chourouk le 10/08/2026.** |
| ~~**C**~~ | ~~Numéros de substitution pour la Tunisie~~ | ✅ **Sans objet le 10/08/2026** — les numéros sont sortis du dispositif, le mode étranger disparaît. |
| ~~**D**~~ | ~~`MANAGE_EXTERNAL_STORAGE` ou SAF~~ | ✅ **Tranché le 11/08/2026 : SAF.** Un seul dossier désigné, aucune permission au manifeste. |
| ~~**F**~~ | ~~Le workflow avant ou après la présence ?~~ | ✅ **Tranché le 12/08/2026 : le workflow d'abord.** La présence devient K6. |
| ~~**G**~~ | ~~Un changement de programme doit-il être annoncé ?~~ | ✅ **Tranché le 12/08/2026 : annoncé, non bloquant.** Une ligne discrète en haut, l'écran est déjà à jour. Rien n'arrive en silence, rien n'impose un tap. |
| ~~**H**~~ | ~~`mesures/` et le programme ont-ils le droit de transiter par Drive ?~~ | ✅ **Tranché le 12/08/2026 : oui** — *« Drive contient le contenu vivant »*. Arbitrage neuf tracé au [`../SYNCHRO.md`](../SYNCHRO.md) §2.3, avec ce qu'il élargit : le programme porte des **libellés cliniques**, pas seulement des compteurs. |
| **I** | 🔴 **Le PHQ-9 reste-t-il hors de Kokoro ?** L'item 9 (idéation suicidaire) impose une escalade **3114** — que l'app s'interdit par construction (§8.8). | ⭐ **Oui, hors de Kokoro.** C'est le seul instrument du corpus porteur d'un déclencheur de sécurité ; il se passe en conversation avec `psy-bilan`. Le GAD-7 seul reste publiable. |
| **E** | 🔴 **Compte Google du dossier de transit** — le test a été fait sur **`xavier@allons-y.io`**, l'adresse principale. Le dossier `psy-journal` y a été créé. | ⭐ **À reconsidérer :** c'est le compte de la **structure professionnelle**. Un compte personnel isolerait mieux des données de santé d'un espace lié à l'activité. **Le changement coûte deux taps** — Kokoro → « Désigner le dossier synchronisé ». |

---

| Version | Date | Modification |
|---|---|---|
| **1.8** | **12/08/2026** | ⭐ **K5 change de contenu et le projet change de nature : Kokoro devient le porteur de la thérapie, pas une collection de fonctions.** Claude Psy écrit `psy/programme/programme.json`, Kokoro l'affiche et renvoie les réponses — **ajouter une étape cesse d'être un acte de développement pour devenir un acte clinique**, fait en séance. Format normatif : `../programme/FORMAT.md`, cinq types d'étapes. ✅ **La moitié PC est écrite et vérifiée** : `npm run publish` refuse la publication entière si une étape enfreint un invariant — **testé sur 9 pièges, les 9 attrapés**. 🔴 **Le point dur est nommé : les garde-fous câblés en tests devenaient contournables par du contenu, en silence** — d'où la double garde (refus total côté PC, écartement de la seule étape fautive côté téléphone). ❌ **L'ancien K6 (interpellation) est supprimé** — *« tant que Xavier ne vient pas vers Kokoro, Kokoro ne lui notifie de rien »* (décision de Xavier) ; son écran de diagnostic One UI est repris dans le nouveau K6. ⭐ **La présence devient K6** : Xavier a arbitré le workflow d'abord, le visage ensuite. 📌 **Les changements de programme sont annoncés sans bloquer** (arbitrage du 12/08). 🔗 `SYNCHRO.md` passe en **v2.1** : le Drive porte maintenant le contenu vivant **dans les deux sens**, arbitrage neuf tracé au §2.3. |
| **1.7** | **11/08/2026** | ✅ **K4 franchi le jour même — un vrai check-in saisi sur le téléphone est arrivé au dossier** (`journal/2026-08-11.json`, `"source": "android"`, valide au schéma). **Le check-in quotidien passe sur Android — le check-in quotidien passe sur le téléphone, et le dossier a un transport.** Les **11 champs** (7 du noyau + 4 de campagne) en **compteurs et choix fermés**, énoncés **mot pour mot** du skill `psy-journal`, `"source": "android"`, format **identique au gabarit** du dossier — vérifié en relisant le fichier depuis Drive. 🔴 **Le transport n'est plus Syncthing mais Google Drive** : arbitrage de Xavier, rendu **après objection argumentée et maintien de la décision**, tracé entier dans `SYNCHRO.md` §2.2 avec ce qui reste non résolu. ⭐ **Réduction de surface appliquée d'office : seul `journal/` transite.** ✅ **Arbitrage D tranché : SAF** — un dossier désigné, **aucune permission au manifeste**, transport interchangeable sans toucher au code. ⚠️ **Une objection du dispositif s'est révélée fausse** (Drive *est* sélectionnable en arbre de documents — le contournement prévu n'a pas servi) et 🔴 **une autre s'est révélée pire que prévu** : **Drive accepte deux fichiers du même nom** et n'en dit rien — constaté, **garde doublée le jour même** (jeton local + interrogation du dossier), et `journal-ingest` refuse d'écraser côté PC. 📌 **Un défaut d'interface corrigé sur l'appareil** : le dossier s'affichait en identifiant Drive opaque. ⏳ **Le critère de fin reste ouvert : il exige un check-in réel.** Un check-in fabriqué depuis le PC serait une **donnée clinique fausse** — le témoin porte donc la date du 01/01/2000 et a été supprimé après usage. |
| 1.6 | 10/08/2026 | ✅ **K3 construit — la tension appliquée cesse d'être un minuteur et devient un guidage.** Les **quatre repères externes de la fiche §2** sont à l'écran dans leur ordre (porte · fauteuil · plateau-garrot-aiguille en **cycles enchaînés** · après-geste 3 cycles puis 5 minutes assis), la **phrase pour le soignant** est affichable et **montrable**, les **critères d'arrêt** sont à un tap. Constaté **écran éteint au départ, `deviceLocked=1` à chaque étape** : bloc de 5 cycles mené à son terme (175 s), bloc d'après-geste à 3 cycles (105 s), décompte des 5 minutes, **aucun son ni vibration**, **aucune permission nouvelle**. ⭐ **Ce que le jalon ajoute vraiment : on ne déclenche plus sur une sensation, on déclenche sur un fait extérieur** — la règle §9.19 passe du texte de la fiche à l'interface. ⏳ **Le critère de fin reste ouvert : il exige une salle d'attente réelle**, et ça ne se vérifie pas depuis un PC. 📌 **Deux défauts trouvés sur l'appareil et corrigés le jour même** — « les **5** cycles » affiché sur un bloc de **3**, et le contenu passant sous la barre d'état. 🧪 **Trois gardes nouvelles** : séquence verrouillée contre la fiche, **sources Kotlin** interdites de son / vibration / réseau, et **déclenchement sur prodrome** interdit dans les textes. |
| 1.5 | 10/08/2026 | ⭐ **La réserve de K2 est levée le jour même : le mot-code a été envoyé pour de vrai et Chourouk a confirmé la réception.** Essai conduit par **Xavier seul, téléphone débranché du PC**, **verrouillé**, **depuis la notification** — la chaîne est vérifiée **de bout en bout dans les conditions réelles**, et non plus jusqu'au bouton. **L'essai a été fait à froid, en la prévenant** : la première fois que Chourouk recevra ce mot ne sera pas la première fois qu'elle le reçoit. 📌 **La réserve précédente reste écrite au-dessus plutôt qu'effacée** — elle était juste au moment où elle a été posée. |
| 1.4 | 10/08/2026 | 🔴 **K2 franchi — le mot-code a un porteur, et c'est le premier livrable clinique de Kokoro.** Écran éteint, téléphone verrouillé, **`deviceLocked=1` à chaque étape** : la notification d'accès s'affiche sur l'écran verrouillé, un tap ouvre l'écran de crise **par-dessus le verrouillage**, un second compose « shutdown » à Chourouk, **prêt à envoyer, sans réseau data et sans jamais lever le verrouillage**. La tension appliquée se déroule au compteur (15 s / 20 s, 5 cycles) sur le même écran. 📌 **« En un geste » devient « en deux taps », et c'est un arbitrage de Xavier, pas un renoncement** : One UI replie la notification, et l'écran à deux boutons garde les deux mécanismes **symétriques** — les confondre aggrave. 🔴 **Réserve entière : aucun SMS réel n'a été envoyé** (numéro fictif, effacé). Deux actes restent à Xavier — saisir le vrai numéro, et **faire un envoi d'essai en prévenant Chourouk**. ⚠️ **Permission nouvelle `SEND_SMS`**, conséquence directe du « sans déverrouillage » ; **jamais d'envoi automatique**, et repli documenté sur l'application Messages si elle est refusée. 🧪 **Deux invariants du §6 deviennent des tests qui échouent** — visualisation, cotation de ressenti, régularité, numéros d'urgence, relaxation délétère ; plus le verrouillage du protocole d'Öst. 🔴 **L'écran ne pose aucun triage** : la question de sécurité appartient à `psy-crise`, pas à une interface. |
| 1.3 | 10/08/2026 | ⚡ **K1 franchi — le point le plus risqué du projet est levé.** Téléphone verrouillé et écran éteint (`mWakefulness=Dozing`, `mScreenState=DOZE`), l'écran s'allume seul et Kokoro s'affiche **par-dessus le verrouillage**, **sans son ni vibration** — constaté sur **trois passages**, dont un conduit par Xavier sans PC, ✅ **silence confirmé à l'oreille sonnerie active**. ⭐ **La restriction d'Android 14 ne s'applique pas sur ce Galaxy S22** ; **l'écran de guidage reste néanmoins dans l'app** — une mise à jour One UI peut changer ce comportement. 🔦 **Un `WAKE_LOCK` s'est révélé nécessaire** : `setTurnScreenOn` seul laisse l'Always On Display s'intercaler. ⭐ **Comportement découvert : Android rétrograde le full-screen intent en bannière quand le téléphone est en cours d'usage** — ce n'est pas un défaut, c'est **la garantie système que Kokoro ne peut pas saisir l'écran de Xavier**, et elle rejoint la doctrine de K6. **Conséquence directe : l'interpellation (K6) et le réveil d'écran cessent d'être des paris.** Une seule réserve subsiste — le déclenchement depuis un **processus froid après veille profonde**, qui demande de laisser passer une nuit. Un détail qui se serait perdu : **un canal de notification est immuable**, d'où l'identifiant versionné `kokoro_alerte_v1`. §5 et K6 mis à jour. |
| 1.2 | 10/08/2026 | ✅ **K0 franchi — le premier acte exécutable du dispositif sur Android.** JDK 21, SDK Android (platform 36 / build-tools 36 / platform-tools 37.0.1), variables utilisateur posées, squelette Gradle écrit, APK debug installé et ouvert sur le **Galaxy S22 (Android 16)**. §1 devient un avant/après daté, K0 gagne son constat de fin chiffré. ⭐ **Android Studio écarté** *(arbitrage de Xavier)* : outillage CLI seul, l'IDE n'était pas dans le critère de fin. **Aucun module `:feature-*` créé d'avance** — ils arrivent avec leur jalon. |
| 1.1 | 10/08/2026 | ⚡ **Le full-screen intent devient le jalon K1** *(arbitrage de Xavier)*, juste après le poste de travail : c'est le point le plus risqué du projet — le lever sur un APK vide coûte une soirée, le découvrir en K5 coûterait une refonte. Le jalon impose en plus le **canal muet**, un full-screen intent sonnant par défaut. Jalons suivants décalés (K2 crise · K3 tension · K4 check-in · K5 présence · K6 interpellation). 🔴 **L'écran de crise ne porte plus aucun numéro** — retrait acté dans tout le dispositif ; il se réduit au **mot-code** et à la **tension appliquée**, et le **mode étranger disparaît** avec les numéros de substitution. ✅ **Chourouk valide le canal SMS du mot-code** — arbitrage B levé. |
| 1.0 | 10/08/2026 | Création — ouverture de l'Étape 5. ⭐ Trois décisions : **le premier livrable est l'écran de crise, pas le visage** · **app unique multi-modules** (question ouverte du `PLAN.md` §3.1 et §5, tranchée) · **aucune base de données, l'app écrit des fichiers** (R1/R2/R3). Constat vérifié : aucun outillage Android sur la machine — le jalon K0 est l'installation. Nuance apportée au `PLAN.md` §5 : le **full-screen intent** n'est plus acquis par défaut depuis Android 14. |
