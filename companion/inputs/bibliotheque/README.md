# `bibliotheque/` — la documentation accessible à Xavier *(et à l'aide-au-patient)*

Un fichier Markdown par document : **`<id>.md`**, où `<id>` est celui que l'étape `fiche` du programme appelle dans son champ `document` (`PLAN.md` §8.3).

Publié tel quel vers Kokoro par `npm run publish`. **Ce README n'est pas publié.**

---

## 🔴 La règle qui vaut plus que toutes les autres ici

> **Une fiche de bibliothèque est *écrite pour Xavier*. Elle n'est jamais *copiée depuis* [`psy/docs/protocoles/`](../../../psy/docs/protocoles/README.md).**

Un protocole clinique porte des **diagnostics**, des **pronostics**, des **noms de praticiens**, des **hypothèses non tranchées** et des **réserves adressées à un professionnel**. Il est écrit pour le praticien.
Une fiche de bibliothèque porte **ce qu'il y a à faire, et pourquoi**.

**Le raccourci est tentant, et c'est ce qui le rend dangereux :** le protocole existe déjà, il est bon, il suffirait de le copier. Il n'est pas écrit pour le même lecteur.

C'est le contrôle **C9** du Superviseur — [`../../../PLAN.md` §4.2](../../../PLAN.md#42-les-dix-contrôles).

## Ce qu'une fiche ne contient jamais

- Un **diagnostic** qui n'a pas encore été dit à Xavier.
- Un **pronostic**.
- Le **nom d'un praticien** qu'il ne consulte pas.
- Une **hypothèse formulée comme un fait**.
- Une **réserve destinée au Dr Isorni**.
- 🔴 **Rien qui apprenne à l'aide-au-patient** un diagnostic, un score, une hypothèse ou un compte rendu — **elle lit des consignes, pas un dossier** (contrôle **C10**). Et **aucune demande de jugement** : « estime si ça va », « décide s'il faut continuer », « rassure-le ».
- 🔴 **Tous les interdits du [`../../../PLAN.md` §8.7](../../../PLAN.md#87--les-interdits)** — visualisation, cotation de ressenti, streak, numéro d'urgence (**3114 compris**), déclenchement sur prodrome, contenu touchant au traitement, relaxation sur un vasovagal. **`npm run publish` lit chaque fiche et refuse la publication entière si l'une d'elles en porte un.**

## Comment une fiche entre ici

1. Elle s'écrit **en séance**, par Claude Psy, et **s'annonce à Xavier pendant la séance**.
2. Elle passe la **supervision bloquante** ([`../../../PLAN.md` §4.3](../../../PLAN.md#43--la-supervision-est-bloquante-avant-publication)).
3. `npm run publish` la copie vers le dossier de transit Drive.

⚠️ **Rien n'entre ici entre deux séances** — un document nouveau sur l'écran de Xavier est un changement d'interface non annoncé.
