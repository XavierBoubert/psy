# `companion/` — Kokoro (心), le compagnon du patient

**Le compagnon de Xavier, sur son téléphone.** **C'est la seule surface tournée vers lui** : tout ce qui lui est accessible passe par ici. Il porte **toute la documentation accessible à Xavier** — bilans, questionnaires, thérapies, protocoles. Il suit le contenu de Claude Psy : **il n'invente rien et ne décide rien.**

> 📖 Vue d'ensemble : [`../README.md`](../README.md). Vocabulaire : [`../THESAURUS.md`](../THESAURUS.md).

---

## 1. Ses quatre rôles, dans cet ordre

| Rôle | Ce que ça veut dire concrètement |
|---|---|
| **Protéger** | Écran de crise sur l'écran verrouillé : mot-code à Chourouk, tension appliquée guidée. **En un geste, sans parler, sans déverrouiller** |
| **Accompagner** | Le programme du jour : les exercices, les démarches, les paliers. Ce que Claude Psy a décidé en séance |
| **Éduquer** | La bibliothèque : les protocoles et les fiches, **écrits pour être lus par Xavier** — pas les documents cliniques bruts |
| **Réconforter** | La présence : un personnage qui respire, qui n'attend rien, qui ne reproche rien |

| Il ne fait jamais | |
|---|---|
| **Décider** | Le contenu vient de Claude Psy, publié en séance |
| **Interpréter** | Aucune lecture, aucune hypothèse, aucun conseil de son cru |
| **Calculer une progression** | Aucun historique, aucune courbe, aucun score à l'écran |
| ⭐ **Venir vers Xavier** | **Aucune notification, aucune relance, aucun reproche.** Xavier vient à lui, et y trouve tout |

> ⭐ **Seule exception à la dernière ligne : l'accès crise sur l'écran verrouillé.** C'est une **porte**, pas un rappel : elle ne dit rien, ne demande rien, et n'apparaît pas parce qu'il s'est passé quelque chose.

---

## 2. Ce que Kokoro contient

**Tout ce qui est accessible à Xavier**, groupé en quatre rubriques :

| Rubrique | Contenu | Exemple |
|---|---|---|
| **`crise`** | Ce qui doit être là au pire moment, accessible **depuis l'écran verrouillé** | Mot-code à Chourouk · tension appliquée guidée · phrase pour le soignant |
| **`therapie`** | Les protocoles en cours, les paliers, les exercices, les démarches — et les **séances à deux** | Palier PPC du moment · repas servis une fois · bloc de tension appliquée · ancrage corporel à deux |
| **`bilan`** | Ses bilans et ses questionnaires — **les échelles se passent ici** | Passation GAD-7, TAS-20, CAT-Q… · compte rendu **écrit par Claude Psy**, jamais calculé par l'app. 🔴 **Jamais le PHQ-9** |
| **`documentation`** | La bibliothèque — les fiches écrites pour être lues par lui | Les 13 symptômes de la panique · le kit vol · la fiche pour Chourouk |

> 🔴 **Un bilan dans Kokoro est un texte daté écrit en séance, jamais un graphique que l'app calcule.** Kokoro n'affiche aucune progression, aucun historique, aucun palier atteint. Ce qui satisfait « Xavier a ses bilans dans la main » sans toucher à l'invariant : **c'est le psy qui interprète, pas l'interface.**

---

## 3. La doctrine de construction

**Kotlin natif + Jetpack Compose.** Cible : **Samsung Galaxy / One UI**. App personnelle et sideloadée — aucune contrainte Google Play. C'est le seul morceau du projet qui sort du TypeScript strict imposé par les règles projet : overlay système, foreground service, `showWhenLocked` et full-screen intent sont des APIs natives ; en cross-platform ce sont des ponts fragiles.

**Quatre décisions de construction, prises et closes :**

1. ⭐ **Kokoro n'a pas commencé par le visage, mais par l'écran de crise.** Le mot-code convenu avec Chourouk n'avait aucun porteur : parole coupée, il fallait déverrouiller, ouvrir une messagerie, trouver un contact et écrire — quatre gestes, dont un impossible.
2. **App unique, multi-modules.** Trois apps seraient trois icônes à retrouver au pire moment.
3. **Aucune base de données.** L'app écrit des fichiers JSON — R1/R2/R3 l'imposent, une base dupliquerait la source de vérité.
4. ⭐ **Kokoro n'apprend rien : il lit.** Claude Psy écrit la thérapie, Kokoro l'affiche et renvoie ce que Xavier a fait. **Ajouter une désensibilisation, un exercice, une démarche ou un questionnaire n'est pas un acte de développement, c'est un acte clinique**, fait en séance.

---

## 4. Le personnage

**Kokoro (心)** — le mot japonais qui désigne indissociablement le cœur et l'esprit, racine de 心理学 *(shinrigaku)*, « psychologie ». Deux raisons de le trouver juste : en japonais **cœur et esprit ne sont pas séparés**, ce qui convient à un dossier où l'angoisse passe par le ventre et où la satiété ne se sent pas ; et le nom désigne **l'objet du soin, pas une promesse de résultat**.

**Nommé, expressif, muet.** Il communique **par texte uniquement** : une voix qui surgit est une agression sensorielle, tandis que le texte se relit à froid, ne force pas le tempo, et **reste lisible en shutdown** — précisément quand le canal verbal est coupé.

**État de repos — 99 % du temps : il respire, c'est tout.** Micro-animation lente et constante, sans information, zéro charge cognitive, zéro interprétation à faire.

Spécification complète : [`CORPS.md`](CORPS.md) *(le personnage)* et [`DECOR.md`](DECOR.md) *(le monde)*.

---

## 5. Les invariants, traduits en règles vérifiables

Ils viennent des contraintes de Xavier *(détail : [`../patient/README.md`](../patient/README.md))*, pas d'un goût. **Une contrainte de conception qui reste une phrase se perd à l'implémentation** — d'où la colonne de droite, qui est la checklist de revue de chaque écran :

| Invariant | Règle de code vérifiable |
|---|---|
| Jamais de son | L'app ne déclare **aucune** permission audio ; tout canal est `IMPORTANCE_LOW` ou moins |
| Jamais de vibration non sollicitée | Aucun appel `Vibrator` hors d'une action déclenchée par Xavier dans la seconde |
| Transitions lentes et continues | Toute animation d'expression ≥ **800 ms**, easing continu. Aucune apparition instantanée |
| Utilisable sans parler ni écrire | **Tout champ obligatoire est un nombre ou un choix fermé** (R5). Le texte libre est facultatif et jamais bloquant |
| Aucune visualisation | Aucun texte ne contient « imagine », « visualise », « représente-toi ». **Vérifié par test sur les chaînes de l'app et sur le contenu publié** |
| Zéro streak | Aucun compteur de régularité, de série, de pourcentage d'objectif ni de moyenne mobile affichée |
| Aucune cotation de ressenti (R6) | Aucun libellé « note ton X sur 10 ». Toute question a une ancre comportementale |
| Aucun numéro d'urgence | **Vérifié par test**, sur les sources **et** sur le contenu publié |
| Déclenchement sur repère externe | Aucun texte ne dit « aux premiers signes », « quand tu sens », « si tu sens » |
| L'apparence ne change jamais sans annonce | Tout changement visuel entre deux versions est annoncé **avant** installation |

> 🔴 **Les garde-fous câblés en tests seraient contournables par du contenu, en silence** : les tests de Kokoro vérifient les textes de l'app, et le contenu publié n'y est pas.
>
> **Double garde, et les deux réactions diffèrent volontairement :**
> - **`npm run psy:publish` refuse la publication entière.** Sur le PC on peut corriger — donc on corrige, on ne publie pas à moitié.
> - **Kokoro écarte la seule étape fautive** et affiche le reste. Sur le téléphone on ne peut pas corriger, et perdre tout le programme pour une ligne serait pire.

---

## 6. Ce qui n'entrera jamais dans Kokoro

1. Un conseil, une suggestion ou un rappel touchant au **traitement** — même sous forme de question. Ça part au brief.
2. Un **streak**, un compteur de régularité, un pourcentage d'objectif, un « ça fait 4 jours », un historique, une progression calculée.
3. Un **son** ou une **vibration** non demandés.
4. Une consigne de **visualisation**, y compris dans un texte d'aide.
5. Une **expression de tristesse, de déception ou de reproche** sur le visage.
6. Un **service tiers** : pas de cloud, pas d'analytics, pas de crash reporting, pas de police distante.
7. 🔴 **Une notification, un rappel, une relance.** Seule exception : l'accès crise sur l'écran verrouillé — **une porte, pas un rappel**.
8. 🔴 **Un numéro d'urgence, sous quelque forme que ce soit** — appel, SMS, lien, texte d'aide. ⭐ **Y compris le 3114** : il appartient à une conduite d'escalade, pas à une interface. Un écran qui l'affiche en permanence le transforme en décor — et c'est précisément ce qui angoissait sans jamais servir.
9. 🔴 **Le PHQ-9** — seul instrument porteur d'un déclencheur d'escalade.

---

## 7. Points durs Android — à traiter, pas à découvrir

| Point | Réalité | Traitement |
|---|---|---|
| **Full-screen intent** | ✅ Levé sur le Galaxy S22 : permission accordée à l'installation, `canUseFullScreenIntent()` vrai sans manipulation. 🔦 Un `WAKE_LOCK` est nécessaire — `setTurnScreenOn` seul laisse l'Always On Display s'intercaler | Écran de guidage conservé — **une mise à jour système peut changer ce comportement** |
| **Canal de notification** | ⚠️ **Un canal est immuable une fois créé** | Identifiant versionné (`kokoro_alerte_v1`) ; toute modification de réglage impose `_v2` |
| **Foreground service** | Depuis Android 14, un `foregroundServiceType` est obligatoire | `specialUse` avec justification. Aucune review : l'app est sideloadée |
| **Notification persistante** | Une notification de service peut sonner | Canal `IMPORTANCE_LOW`, **aucun son, aucune vibration** — règle, pas préférence |
| **Accès aux fichiers** | Le stockage cloisonné empêche d'écrire librement | ✅ **SAF, URI d'arbre persistant.** `MANAGE_EXTERNAL_STORAGE` écarté — il ouvre tout le stockage pour un seul dossier. **Aucune permission au manifeste**, transport interchangeable |
| **Un dossier Drive n'est pas un système de fichiers** | 🔴 Drive **accepte deux fichiers du même nom** et ne le signale pas | Garde **double** : jeton local de date + interrogation du dossier. Côté PC, l'ingestion refuse d'écraser |
| **One UI tue les services** | Deux réglages batterie obligatoires | *Batterie → Limites d'utilisation en arrière-plan → Applications jamais mises en veille* + désactivation de l'optimisation. **Écran de diagnostic dans l'app** — une mise à jour les réinitialise |

---

## 8. Construire, tester, déployer

```bash
npm run companion:kokoro   # depuis la racine
./kokoro                   # depuis companion/android/
```

**Jamais `gradlew` ni `adb` à la main.** Un verdict par étape, et en cas d'échec seulement l'extrait qui l'explique ; le détail reste dans `build/kokoro.log` (`./kokoro journal`). Sous-commandes et motif : [`android/README.md`](android/README.md).

⭐ **Le câble n'est pas nécessaire** *(15/08/2026)* : PC et téléphone sur le même Wi-Fi suffisent, et le lien se renoue tout seul à chaque build. Il se noue une première fois par `./kokoro lien` *(câble branché une fois)* ou `./kokoro appairer <IP:port> <code>` *(sans câble)*.

---

## 9. Carte

| Chemin | Rôle |
|---|---|
| 🔴 [`PROGRAMME.md`](PROGRAMME.md) | **NORMATIF — le format du programme et de la bibliothèque.** Les six types d'étape, les rubriques, les interdits |
| [`CORPS.md`](CORPS.md) | ⭐ **Le corps de Kokoro** — un petit robot kawaii en 2D. Six expressions, cinq postures, deux jeux fermés, aucun sourcil, panneau-visage qui s'éteint. **Le livrable est vectoriel : aucune image du personnage n'entre dans l'APK** |
| [`DECOR.md`](DECOR.md) | ⭐ **Le monde** — **quatre écrans en anneau horizontal, sans bout** *(`INTERFACE.md` §7.7)*, décor peint en quatre couches en parallaxe, verrou portrait, passage en nuit sur plage horaire. 🔴 **Seule dérogation à « aucun bitmap » : quatre WebP** |
| [`INTERFACE.md`](INTERFACE.md) | Le rendu des étapes du programme et de la bibliothèque à l'écran |
| [`android/`](android/README.md) | **Le code** — Kotlin + Compose, Galaxy S22 / One UI, sideloadée |
| [`ressources/`](ressources/) | **Ce dont le compagnon a besoin pour exister** : `prompts/` *(recherche graphique)*, `retenus/` *(les planches qui font foi)*, `sorties/` *(non versionné)* |
| [`inputs/`](inputs/) | 🔴 **Ce que Claude Psy lui donne** : `programme.json` + [`bibliotheque/`](inputs/bibliotheque/README.md). **Écrit par le psy seul, publié uniquement à la clôture d'une séance et uniquement après supervision** |
| [`outputs/`](outputs/) | 🔴 **Ce que Kokoro produit** : `journal/` *(check-ins)* + `reponses/` *(ce qui a été fait)*. **Écrit par Kokoro seul** |
| [`scripts/`](scripts/) | `companion-image.ts` *(planches via Gemini)* · `companion-decoupe.ts` *(détourage du fond magenta)* |

> ⚠️ **`inputs/` et `outputs/` sont du contenu clinique**, malgré leur nom d'interface. Formats normatifs : [`PROGRAMME.md`](PROGRAMME.md) pour les entrées, [`../psy/DOSSIER.md`](../psy/DOSSIER.md) pour les sorties. **Aucune surface n'a le droit d'inventer un format.**
>
> 🔴 **`outputs/` est append-only** *(règle R2)*. `npm run psy:sync` n'écrase jamais un fichier existant, et **un doublon Drive ne se supprime jamais sans être lu : c'est une donnée clinique.**
