# `android/` — Kokoro (心)

**Kotlin natif + Jetpack Compose.** 🏗️ **Étape 5 ouverte le 10/08/2026.**
Cible : **Samsung Galaxy / One UI**. App personnelle et sideloadée — aucune contrainte Google Play.

> 📐 **Ce README dit ce que Kokoro est. [`PLAN-KOKORO.md`](PLAN-KOKORO.md) dit dans quel ordre on le construit** — jalons **K0 → K6**, critères de fin, points durs Android. ⭐ **Le premier livrable n'est pas le visage, c'est l'écran de crise** — et ⚡ **le full-screen intent se lève en K1**, avant tout le reste.
>
> 🔴 **L'écran de crise ne porte aucun numéro d'urgence** *(10/08/2026)* : le mot-code à Chourouk et la tension appliquée, rien d'autre. Motifs : [`../protocoles/crise-escalade.md`](../protocoles/crise-escalade.md) §0.

*Kokoro (心) : le mot japonais qui désigne indissociablement le cœur et l'esprit, racine de 心理学* (shinrigaku), *« psychologie ». Il nomme l'objet du soin, pas une promesse de résultat.*

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
| Interpellation : **une phrase, une raison chiffrée, un refus à coût nul.** Plafond : 1/jour, 3/semaine | PLAN §2.4 |

## Faisabilité technique

| Besoin | Mécanisme |
|---|---|
| Flotter au-dessus de toutes les apps | `SYSTEM_ALERT_WINDOW` + `TYPE_APPLICATION_OVERLAY` |
| Rester vivant en permanence | Foreground Service + exemption d'optimisation batterie |
| S'afficher **par-dessus l'écran de verrouillage** | Activity avec `setShowWhenLocked(true)` + `setTurnScreenOn(true)` — l'overlay classique passe **sous** le keyguard |
| Réveiller l'écran pour une alerte | `full-screen intent` |

**Le point d'attention réel n'est pas Android, c'est le constructeur.** Deux réglages One UI, sans lesquels le compagnon mourra silencieusement au bout de quelques heures : *Batterie → Limites d'utilisation en arrière-plan → Applications jamais mises en veille* ; et désactivation de l'optimisation de la batterie (`REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`).

→ **Écran de diagnostic obligatoire** dans l'app : il vérifie ces réglages et guide leur activation. Une mise à jour système peut les réinitialiser.

## Données

Lecture/écriture de `psy/dossier/` selon `psy/dossier/SCHEMA.md`. Transport : **Syncthing** — cf. `psy/SYNCHRO.md`.
