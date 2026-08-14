# `aidant/` — l'aide-au-patient

**La personne qui tient le téléphone** pendant une **séance à deux**, et qui exécute les consignes chronométrées de Kokoro. Aujourd'hui : **Chourouk**.

> 🔴 **C'est un rôle, pas une personne.** Ce qui est écrit ici vaut pour quiconque le tient, aujourd'hui ou plus tard.
>
> 🔴 **Elle n'est pas thérapeute.** Tout ce qui demande un jugement clinique est **hors de son rôle**. Elle suit un déroulé ; elle ne juge pas, n'interprète pas, ne rassure pas hors script.

Doctrine complète : [`../PLAN.md` §8.3](../PLAN.md#83-une-étape) *(le type d'étape `seance-duo`)* et [§1.4](../PLAN.md#1-la-vision--cinq-personas).

---

## Ce qu'elle ne fait jamais

**Improviser · juger · interpréter · rassurer hors script.**

---

## 🔴 Les trois garde-fous, aucun optionnel

La séance à deux met **une tierce personne dans la boucle**. C'est le seul endroit du dispositif où quelqu'un d'autre que Xavier et Claude agit sur une séance, et il porte trois protections :

| Garde-fou | Ce que c'est |
|---|---|
| **Le signal d'arrêt** | Un **geste convenu à froid**, par lequel Xavier arrête **sans parler**. Rappelé à l'écran **en permanence** — pas au début, pas dans une aide : en permanence |
| **Les critères d'arrêt** | Accessibles **en un tap**. Le dernier est toujours *« tu ne sais pas quoi faire → on s'arrête »* |
| **Le mode entraînement** | **Obligatoire avant la première fois.** On ne découvre pas un déroulé en situation |

⭐ **Les trois sont vérifiés mécaniquement par `npm run publish`** — ils ne dépendent pas de la vigilance de qui écrit la séquence.

---

## 🔴 Ce qu'elle n'apprend jamais — le contrôle C10

> **Rien de ce que l'aide lit ne lui apprend quelque chose sur Xavier qu'il n'a pas décidé de partager** — ni diagnostic, ni score, ni hypothèse, ni compte rendu.
>
> **Elle lit des consignes, pas un dossier.**

Le superviseur le vérifie sur **chaque** séquence, et il cherche deux fautes distinctes :

1. **Une consigne qui lui apprend quelque chose** — un diagnostic non encore dit, un score, une hypothèse formulée comme un fait.
2. **Une consigne qui lui demande de juger** — « estime si ça va », « décide s'il faut continuer », « rassure-le ». ⭐ **Une consigne qui lui demande de juger la met en faute quoi qu'elle fasse.**

⚠️ **Une frontière est nommée d'avance, pour ne pas être découverte en séance** *(14/08/2026)* : **un retraitement EMDR en phase 3 demande de dire ce qui vient entre deux séries** — donc la personne qui tient l'instrument **entend le matériel**. En phases 1 et 2 la question ne se pose pas. En phase 3, c'est une condition de plus, **à trancher avec le Dr Isorni au déverrouillage, et jamais pendant une séance**.

---

## Carte

| Chemin | Rôle |
|---|---|
| [`ressources/fiche-chourouk.md`](ressources/fiche-chourouk.md) | ⭐ **La seule chose écrite qu'elle reçoit** — une explication des shutdowns de Xavier. **Elle ne lui demande rien et ne lui attribue aucun rôle thérapeutique.** ⚠️ Elle porte une note interne **à retirer avant transmission**, et **Xavier relit et décide de la transmettre ou non** |
| [`scripts/`](scripts/README.md) | **Vide aujourd'hui** — voir le README |

**Les déroulés qu'elle exécute ne vivent pas ici** : ce sont des étapes `seance-duo` du programme, écrites par Claude Psy et publiées dans [`../companion/inputs/`](../companion/inputs/). **Elle ne les lit que dans Kokoro**, jamais dans le dépôt.

---

## Ce qui n'existe pas encore

🔜 **K6 — la séance à deux** est le prochain jalon de Kokoro. À ce jour, **aucune séquence `seance-duo` n'est écrite ni publiée**, et le mode entraînement n'a jamais tourné. ⚠️ **Écrit ne veut pas dire appliqué.**
