# `companion/` — Kokoro (心), le compagnon du patient

**Le compagnon de Xavier, sur son téléphone.** Depuis le 14/08/2026, **c'est la seule surface tournée vers lui** : tout ce qui lui est accessible passe par ici *(`PLAN.md` [§5.8](../PLAN.md#58-il-ny-a-quune-surface-14082026))*.

Doctrine complète : [`../PLAN.md` §5](../PLAN.md#5-kokoro--le-compagnon). Vocabulaire : [`../THESAURUS.md`](../THESAURUS.md).

---

## Ses quatre rôles, dans cet ordre

**protéger · accompagner · éduquer · réconforter**

Il porte **toute la documentation accessible à Xavier** : bilans, questionnaires, thérapies, protocoles. Il suit le contenu de Claude Psy — **il n'invente rien et ne décide rien.**

| Il ne fait jamais | |
|---|---|
| **Décider** | Le contenu vient de Claude Psy, publié en séance |
| **Interpréter** | Aucune lecture, aucune hypothèse, aucun conseil de son cru |
| **Calculer une progression** | Aucun historique, aucune courbe, aucun score à l'écran |
| ⭐ **Venir vers Xavier** | **Aucune notification, aucune relance, aucun reproche.** Xavier vient à lui, et y trouve tout |

> ⭐ **Seule exception à la dernière ligne : l'accès crise sur l'écran verrouillé.** C'est une **porte**, pas un rappel.

---

## Carte

| Chemin | Rôle |
|---|---|
| [`CORPS.md`](CORPS.md) | ⭐ **Le corps de Kokoro** — un petit robot kawaii en 2D. Six expressions, cinq postures, deux jeux fermés, aucun sourcil, panneau-visage qui s'éteint. **Le livrable est vectoriel : aucune image du personnage n'entre dans l'APK** |
| [`DECOR.md`](DECOR.md) | ⭐ **Le monde** — cinq écrans en croix, décor peint en quatre couches en parallaxe, verrou portrait, passage en nuit sur plage horaire. 🔴 **Seule dérogation à « aucun bitmap » : quatre WebP** — arbitrage au §2, **à confirmer par Xavier** |
| [`INTERFACE.md`](INTERFACE.md) | Le rendu des étapes du programme et de la bibliothèque à l'écran |
| [`android/`](android/README.md) | **Le code** — Kotlin + Compose, Galaxy S22 / One UI, sideloadée. Construire et déployer : `./kokoro` |
| [`ressources/`](ressources/) | **Ce dont le compagnon a besoin pour exister** : `prompts/` *(recherche graphique)*, `retenus/` *(les planches qui font foi)*, `sorties/` *(non versionné)* |
| [`inputs/`](inputs/) | 🔴 **Ce que Claude Psy lui donne** : `programme.json` + [`bibliotheque/`](inputs/bibliotheque/README.md). **Écrit par le psy seul, publié uniquement à la clôture d'une séance et uniquement après supervision** |
| [`outputs/`](outputs/) | 🔴 **Ce que Kokoro produit** : `journal/` *(check-ins)* + `reponses/` *(ce qui a été fait)*. **Écrit par Kokoro seul** |
| [`scripts/`](scripts/) | `kokoro-image.ts` *(planches via Gemini)* · `kokoro-decoupe.ts` *(détourage du fond magenta)* |

> ⚠️ **`inputs/` et `outputs/` sont du contenu clinique**, malgré leur nom d'interface. Formats normatifs : [`../PLAN.md` §8](../PLAN.md#8-le-programme--format) pour les entrées, [`§7`](../PLAN.md#7-le-dossier--format) pour les sorties. **Aucune surface n'a le droit d'inventer un format.**
>
> 🔴 **`outputs/` est append-only** *(règle R2)*. `npm run sync` n'écrase jamais un fichier existant, et **un doublon Drive ne se supprime jamais sans être lu : c'est une donnée clinique.**

---

## Où en est le travail

✅ **K0 → K4 franchis** : poste de travail · full-screen intent levé · **noyau de crise** *(mot-code envoyé pour de vrai, verrouillé, essai fait à froid)* · **tension appliquée guidée sur repères externes** · **check-in quotidien sur le téléphone**.
🔴 **K5 en cours** — Kokoro lit `programme.json` et `bibliotheque/`, écrit `reponses/`.
🔜 **K6** — la séance à deux. ⏸️ **K7** — la présence *(overlay, le corps, diagnostic One UI)*.

Jalons et points durs : [`../PLAN.md` §5](../PLAN.md#5-kokoro--le-compagnon).

---

## Construire, tester, déployer

```bash
npm run kokoro          # depuis la racine
./kokoro                # depuis companion/android/
```

**Jamais `gradlew` ni `adb` à la main.** Un verdict par étape, et en cas d'échec seulement l'extrait qui l'explique ; le détail reste dans `build/kokoro.log` (`./kokoro journal`). Sous-commandes et motif : [`android/README.md`](android/README.md).

---

## Les invariants qui commandent l'interface

Ils viennent des contraintes de Xavier *(`PLAN.md` [§2](../PLAN.md#2-xavier--les-contraintes-qui-commandent-tout))*, pas d'un goût :

- **Aphantasie** → aucune technique de visualisation. La consigne est **inopérante**, pas difficile.
- **Shutdowns** → toute interface reste utilisable **sans parler ni écrire**.
- **Hypersensibilités (4 canaux)** → pas de son surprise, pas de flash, pas d'animation brusque.
- **Rigidité / routines** → **la prévisibilité est une fonctionnalité** : aucun changement d'interface non annoncé. C'est pourquoi le décor **ne suit jamais le thème sombre du système** et **ne bascule jamais sous les yeux**.
- **Camouflage = moteur de l'anxiété** → zéro exigence de performance, zéro jugement.
- **Réduire les charges, pas « motiver »** → 🔴 **aucun streak, aucun compteur de régularité, aucun reproche d'assiduité.** Vérifié mécaniquement à la publication.
- **Deux mécanismes de crise distincts** → panique *(exposition/respiration)* ≠ vasovagal *(tension appliquée)*. **La mauvaise parade aggrave.**

> 🔴 **Les numéros d'appel d'urgence — 15, 112, 114 — ont été retirés du dispositif le 10/08/2026**, à la demande de Xavier. **Ne jamais les réintroduire, sous aucune forme.** Le **3114** est le seul conservé, et **il ne s'affiche jamais dans Kokoro**.
