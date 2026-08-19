# `bilans/` — les comptes rendus que Xavier possède déjà

Un fichier Markdown par bilan : **`<id>.md`**, où `<id>` est celui que l'étape `bilan` du programme appelle dans son champ `document` (`companion/PROGRAMME.md` §3).

`npm run psy:publish` le convertit en **`bilans/<id>.pdf`** et ne publie que le PDF. **Ce README n'est pas publié.**

---

## 🔴 Pourquoi ce dossier n'est pas la bibliothèque

> **Une fiche de bibliothèque est *écrite pour Xavier* et *lisible par l'aidant*. Un bilan n'est ni l'un ni l'autre : c'est un document qu'il possède déjà, adressé à lui seul.**

**Conséquence mécanique :** les sept familles d'interdits de [`companion/PROGRAMME.md` §7](../../PROGRAMME.md) **ne s'appliquent pas au corps d'un bilan** — un rapport clinique réel nomme un traitement, un diagnostic, un praticien, et c'est sa raison d'être. Elles restent appliquées au **titre** de l'étape, qui, lui, s'affiche dans Kokoro.

**Conséquence de fond :** **C9 ne s'applique pas ici** — rien n'est réécrit, rien n'est dérivé. Le contrôle qui le remplace tient en une question : **ce document ne contient rien que Xavier ne sache déjà.**

## Ce qu'un bilan ne fait jamais

- 🔴 **Il ne se partage pas depuis Kokoro** — aucune fonction de partage n'existe. L'app confie le PDF au lecteur du téléphone ; **le partage est un acte de Xavier dans son lecteur.**
- 🔴 **Il ne porte ni `texte`, ni `montrable`.**
- **Il ne renvoie rien** — `reponses/` ne le connaît pas, et l'écran ne le grise jamais.
- **Il ne dit pas où Xavier en est rendu** — sa `date` est celle du document, pas celle de la publication.

## Comment un bilan entre ici

1. Il se met en forme depuis le Markdown de [`patient/ressources/`](../../../patient/README.md) ou du dossier — 🔴 **jamais depuis un PDF de `originales/`, qui est une archive.**
2. Il passe la **supervision bloquante** ([`superviseur/README.md` §4](../../../superviseur/README.md)).
3. `npm run psy:publish` le convertit et le verse au transit, **à tout moment, hors séance.**
4. **Il s'annonce à Xavier au moment où il est publié**, dans la conversation en cours.
