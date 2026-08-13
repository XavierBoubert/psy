# `android/` — Kokoro (心)

**Kotlin natif + Jetpack Compose.** Cible : **Samsung Galaxy / One UI**. App personnelle et sideloadée — aucune contrainte Google Play.

> 📐 **Ce README dit ce que Kokoro est. [`../../PLAN.md` §5](../../PLAN.md#5-kokoro--le-compagnon) dit dans quel ordre on le construit** — jalons **K0 → K7**, critères de fin, points durs Android, invariants traduits en règles vérifiables.
>
> ✅ **K0 → K4 franchis.** Poste de travail · **full-screen intent levé** (le point le plus risqué du projet) · **noyau de crise** — ⭐ le mot-code a été envoyé pour de vrai, téléphone verrouillé, et Chourouk a confirmé, l'essai fait **à froid en la prévenant** · **tension appliquée guidée sur quatre repères externes** · **check-in quotidien sur le téléphone**.
>
> 🔴 **K5 en cours — Kokoro porte la thérapie.** Il lit `programme.json` et `bibliotheque/`, et écrit `reponses/`. 🆕 **K6 — la séance à deux** : type `seance-duo`, entraînement, signal d'arrêt, critères d'arrêt à un tap. ⏸️ **K7 — la présence** : overlay, visage, écran de diagnostic One UI.

*Kokoro (心) : le mot japonais qui désigne indissociablement le cœur et l'esprit, racine de 心理学* (shinrigaku), *« psychologie ». Il nomme l'objet du soin, pas une promesse de résultat.*

## ⭐ Ses cinq rôles

| Rôle | Concrètement |
|---|---|
| **Protéger** | Écran de crise sur l'écran verrouillé : mot-code à Chourouk, tension appliquée guidée. En un geste, sans parler, sans déverrouiller |
| **Accompagner** | Le programme du jour — ce que Claude Psy a décidé en séance |
| **Éduquer** | La bibliothèque — les protocoles et les fiches, **écrits pour être lus par Xavier** |
| **Réconforter** | La présence — un visage qui respire, qui n'attend rien, qui ne reproche rien |
| ⭐ **Faire à deux** *(13/08/2026)* | Les thérapies impossibles en solo : Kokoro passe dans les mains de l'**aide-au-patient**, qui suit un déroulé **chronométré**. **Mode entraînement obligatoire** avant la première fois |

> ⭐ **Kokoro ne vient jamais vers Xavier.** Aucune notification, aucune relance, aucun rappel, aucun reproche. **Xavier vient à lui, et y trouve tout.** *(Seule exception : la notification d'accès crise sur l'écran verrouillé — **une porte, pas un rappel** ; elle ne dit rien, ne demande rien, et n'apparaît pas parce qu'il s'est passé quelque chose.)*

> 🔴 **L'écran de crise ne porte aucun numéro d'urgence** *(10/08/2026)* : le mot-code à Chourouk et la tension appliquée, rien d'autre. **3114 compris** — il appartient à une conduite d'escalade, pas à une interface. Motifs : [`../protocoles/crise-escalade.md`](../protocoles/crise-escalade.md) §0.

> C'est le seul morceau du projet qui sort du TypeScript strict imposé par les règles projet — assumé : overlay système, foreground service, `showWhenLocked` et full-screen intent sont des APIs natives ; en cross-platform ce sont des ponts fragiles.

## Ce qui doit être sur Android, sans discussion

**En un geste, depuis l'écran verrouillé :**
- la **tension appliquée** — elle sert en salle d'examen, pas au bureau ;
- le **bouton shutdown** — il sert en plein conflit ; il envoie le mot-code à Chourouk et coupe les sollicitations.

Puis : check-in quotidien, outils de crise, suivi des repas.

## Le personnage

**Nommé, expressif, muet.** Il communique **par texte uniquement** — une voix qui surgit est une agression sensorielle (hypersensibilité auditive), tandis que le texte se relit à froid, ne force pas le tempo et **reste lisible en shutdown**, précisément quand le canal verbal est coupé.

**Registre graphique : trait minimal, ligne claire.** Forme simple, contour fin, deux yeux et une bouche, peu de surface colorée.

**État de repos — 99 % du temps : il respire, c'est tout.** Micro-animation lente et constante, sans information, zéro charge cognitive, zéro interprétation à faire. La charge mesurée reste **consultable en un tap**, jamais affichée d'elle-même.

## Règles non négociables

| Règle | Origine |
|---|---|
| Jamais de son, jamais de vibration non sollicitée | Hypersensibilité auditive et tactile |
| Transitions d'expression **lentes et continues** — jamais de changement brusque | Hypersensibilité visuelle ; intolérance à l'imprévu |
| Le visage **n'attend jamais rien** : pas d'air déçu, pas de reproche, pas de « ça fait longtemps » | Camouflage = moteur de l'anxiété ; zéro exigence sociale |
| **Jamais de tristesse ni de reproche** dans l'expression, en aucune circonstance — des niveaux de charge, sans valence morale | Idem |
| L'apparence **ne change jamais sans annonce** — pas de skin surprise, pas d'événement saisonnier | Rigidité / routines |
| Il **explicite toujours pourquoi** il s'exprime | Empathie cognitive : ne jamais demander de décoder |
| ❌ **Aucune interpellation, aucune notification, aucune relance** *(13/08/2026 — la règle a remplacé l'ancienne « 1/jour, 3/semaine »)* | Kokoro ne vient jamais vers Xavier |

## Faisabilité technique

| Besoin | Mécanisme |
|---|---|
| Flotter au-dessus de toutes les apps | `SYSTEM_ALERT_WINDOW` + `TYPE_APPLICATION_OVERLAY` |
| Rester vivant en permanence | Foreground Service + exemption d'optimisation batterie |
| S'afficher **par-dessus l'écran de verrouillage** | ✅ **vérifié le 10/08/2026** — Activity avec `setShowWhenLocked(true)` + `setTurnScreenOn(true)` ; l'overlay classique passe **sous** le keyguard |
| Réveiller l'écran pour une alerte | ✅ **vérifié le 10/08/2026** — `full-screen intent` sur canal `IMPORTANCE_HIGH` **muet** (`setSound(null, null)`, vibration désactivée) **+ `WAKE_LOCK`**, sans lequel l'Always On Display s'intercale et l'app ne s'affiche pas. ⚠️ **Un canal est immuable une fois créé** : son identifiant est versionné (`kokoro_alerte_v1`), toute modification de ses réglages impose de passer à `_v2` |
| **Ne jamais saisir l'écran pendant que Xavier s'en sert** | ✅ **garanti par Android** — un full-screen intent est automatiquement rétrogradé en bannière dès que l'écran est allumé et le téléphone en usage. Vérifié le 10/08/2026 |

**Le point d'attention réel n'est pas Android, c'est le constructeur.** Deux réglages One UI, sans lesquels le compagnon mourra silencieusement au bout de quelques heures : *Batterie → Limites d'utilisation en arrière-plan → Applications jamais mises en veille* ; et désactivation de l'optimisation de la batterie (`REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`).

→ **Écran de diagnostic obligatoire** dans l'app : il vérifie ces réglages et guide leur activation. Une mise à jour système peut les réinitialiser.

## Données

**Kokoro n'accède jamais à `psy/dossier/`.** Il écrit et lit **un seul dossier de transit**, désigné une fois par Xavier (SAF — aucune permission au manifeste), et c'est le PC qui fait la jonction avec le dossier clinique.

| Sens | Fichiers | Format | Acheminé par |
|---|---|---|---|
| Kokoro **écrit** | `journal/AAAA-MM-JJ.json` · `reponses/AAAA-MM-JJ-HHMM-<id>.json` | [`../../PLAN.md` §7](../../PLAN.md#7-le-dossier--format) *(normatif)* | `npm run sync` |
| Kokoro **lit** | `programme.json` · `bibliotheque/*.md` | [`../../PLAN.md` §8](../../PLAN.md#8-le-programme--format) *(normatif)* | `npm run publish` |

Transport : ~~Syncthing~~ → **Google Drive** depuis le 11/08/2026, étendu aux deux sens le 12/08, **étendu à la bibliothèque le 13/08** — périmètre, objections conservées et conditions : [`../../PLAN.md` §6](../../PLAN.md#6-le-contenu--google-drive).

⭐ **Ni profil, ni état, ni séances, ni crises, ni mesures, ni briefs, ni supervisions ne quittent le PC.** **Le contenu publié est *dérivé*, jamais *extrait*** : il porte ce qu'il y a à faire, jamais ce qui a été constaté, mesuré ou diagnostiqué.

🔴 **Kokoro écarte la seule étape fautive plutôt que de refuser tout le programme** — sur le téléphone on ne peut pas corriger, et perdre tout le programme pour une ligne serait pire. Côté PC, `npm run publish` refuse **la publication entière**. Les deux réactions diffèrent volontairement.

## Construire et installer *(K0 franchi le 10/08/2026)*

Depuis `psy/android/`, téléphone branché et débogage USB autorisé :

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

`local.properties` (non versionné) porte `sdk.dir` — JDK 21, SDK Android platform 36 / build-tools 36 / platform-tools 37.0.1.
**Pas d'Android Studio sur ce poste** — outillage en ligne de commande seul ; l'IDE lirait le même projet Gradle s'il était installé un jour.

## Ce qui n'entrera jamais dans Kokoro

Liste complète et motifs : [`../../PLAN.md` §5.7](../../PLAN.md#57-ce-qui-nentrera-jamais-dans-kokoro). En résumé : conseil touchant au traitement · streak ou historique · son ou vibration non demandés · consigne de visualisation · expression de reproche · service tiers · **notification** · **numéro d'urgence, 3114 compris** · **le PHQ-9**.
