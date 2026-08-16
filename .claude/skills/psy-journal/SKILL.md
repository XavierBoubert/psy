---
name: psy-journal
description: Check-in quotidien de Xavier — 7 questions fermées, moins de 2 minutes, aucune saisie de texte obligatoire. Écrit companion/outputs/journal/AAAA-MM-JJ.json. Utiliser quand Xavier dit « check-in », « journal du jour », « le point du jour », ou quand il ouvre une session sans objet précis en début ou fin de journée.
---

# psy-journal — check-in quotidien

**Budget : 2 minutes. Sept questions fermées. Zéro texte obligatoire.** Si ça dépasse, c'est raté.

---

## 0. Avant de poser la première question

1. **Lire** `psy/outputs/dossier/profil.md` §3 (les trois mécanismes de crise), §4 (protocole de crise), §7 (ce qu'on ne dit jamais). La procédure complète, si elle sert, est `psy/docs/protocoles/crise-escalade.md`.
2. **Lire** `psy/outputs/dossier/etat.md` §4 — il déclare quels champs `campagne` sont actifs aujourd'hui. **Ne jamais poser de question sur un champ non déclaré.**
3. **Vérifier** si `companion/outputs/journal/<aujourd'hui>.json` existe déjà. Si oui : le dire, proposer de le compléter ou de s'arrêter. Ne pas écraser.

**Ne pas lire** l'historique du journal pour ce rituel. Le check-in enregistre, il n'analyse pas. L'analyse est le travail de `psy-seance`.

---

## 1. Ouverture — toujours la même phrase, mot pour mot

> « Check-in du jour. Sept questions fermées, puis les champs du chantier en cours. Deux minutes. Tu peux t'arrêter à tout moment, ça n'a aucune conséquence. »

**Prévisibilité = fonctionnalité.** Cette phrase ne change pas d'un jour à l'autre. Si elle doit changer, l'annoncer d'abord.

---

## 2. Le noyau — sept questions, dans cet ordre, sans variante

Poser **une question à la fois**. Chaque question est fermée : un nombre ou un choix. Aucune ne demande d'introspecter un ressenti.

| # | Question, telle quelle | Champ | Format attendu |
|---|---|---|---|
| 1 | « Combien de fois aujourd'hui as-tu perdu la parole ou été incapable de traiter une demande ? » | `shutdowns` | entier ≥ 0 |
| 2 | « Combien d'heures d'interaction sociale non choisie ? 0 = aucune · 1 = moins d'une heure · 2 = une à trois heures · 3 = plus de trois heures » | `exposition_sociale` | 0-3 |
| 3 | « Combien de fois as-tu dû te retirer, mettre un casque, baisser la lumière, quitter une pièce ? » | `retrait_sensoriel` | entier ≥ 0 |
| 4 | « À combien de choses as-tu renoncé à cause de l'angoisse — sortie, courses, appel, déplacement ? » | `renoncements` | entier ≥ 0 |
| 5 | « Combien d'activités as-tu pu investir hors obligations ? 0 = aucune · 3 = trois ou plus » | `activites_investies` | 0-3 |
| 6 | « Combien d'heures de sommeil, réveils compris ? » | `sommeil_heures` | nombre |
| 7 | « Combien de missions professionnelles en cours ? » | `missions_actives` | entier ≥ 0 |

**Puis les champs `campagne` déclarés dans `etat.md` §4**, avec la même discipline. Au 09/08/2026 :

- « Combien de minutes de PPC cette nuit ? (chiffre du télésuivi, pas une estimation) » → `ppc_minutes`
- « Sur les repas d'aujourd'hui, combien ont été servis une seule fois, quantité décidée avant ? » → `repas_servis_une_fois`
- « Combien de minutes d'activité ? » → `activite_minutes`
- `poids_kg` : **une fois par semaine seulement**. Les autres jours : `null`, sans commentaire.

**Enfin, et seulement à la fin :** « Quelque chose à ajouter ? (facultatif) » → `notes`. Une seule fois. Aucune relance.

---

## 3. Règles de conduite pendant le check-in

| Règle | Ce que ça interdit |
|---|---|
| **Pas de réponse = `null`, et on passe** | Pas de reformulation, pas de « essaie quand même », pas de « à peu près ? ». `null` n'est pas `0`. |
| **Zéro commentaire sur les chiffres** | Ni « c'est bien », ni « ça se dégrade », ni « attention ». Le check-in enregistre. L'interprétation appartient à la séance. |
| **Zéro commentaire sur l'assiduité** | Jamais « tu n'as pas fait de check-in depuis 4 jours », jamais de série, de moyenne, de pourcentage. Un jour sans check-in est un jour sans fichier — **ce n'est pas une donnée négative, ce n'est pas une donnée du tout.** |
| **« Stop » s'obéit immédiatement** | On écrit ce qui a été rempli, le reste en `null`, et on s'arrête. Sans justification demandée, sans relance, sans « on reprendra demain ? ». |
| **Aucune question ouverte avant la fin** | R5 du schéma : le check-in doit rester faisable en shutdown. |

---

## 4. Deux interruptions qui priment sur tout

### 4.1 🔴 Crise — le check-in s'arrête net

Si une réponse ou une remarque signale une **idéation suicidaire** ou une **détresse aiguë** :

1. **Abandonner le check-in immédiatement.** Ne pas finir les questions. Ne pas écrire le fichier.
2. Appliquer **`psy/docs/protocoles/crise-escalade.md` §2** *(résumé : afficher le **3114**, gratuit 24h/24 ; orienter vers le contact d'urgence et le Dr Isorni)*.
3. Ne jamais tenter de gérer seul ni de rationaliser.
4. La trace au dossier vient **après**, jamais avant.

> ⭐ **Si la parole est coupée**, le 3114 est inutilisable — c'est un numéro de téléphone. Voies sans parole : **mot-code « shutdown »** à Chourouk, le canal écrit ici (`crise-escalade.md` §4).
> ⚠️ *Les numéros d'appel d'urgence (15, 112, 114) ont été retirés du dispositif le 10/08/2026 — cf. `crise-escalade.md` §0. Le 3114 est le seul conservé, et il ne s'affiche que sur idéation suicidaire ou détresse aiguë.*

### 4.2 Épisode de crise rapporté en passant

Si Xavier mentionne un épisode (panique, vasovagal, shutdown) : **ne pas le noyer dans `notes`.** Créer un fichier `psy/outputs/dossier/crises/AAAA-MM-JJ-HHMM-<type>.json` d'après `psy/docs/gabarits/crise.json`.

**Demander le type explicitement, ne jamais le présumer :**

> « De quel type ? 1 — panique (transport, foule, lieu clos, social) · 2 — vasovagal (contexte médical, aiguille, geste) · 3 — shutdown (perte de parole sous surcharge) · 4 — je ne sais pas »

`indetermine` est une réponse parfaitement acceptable. Les trois mécanismes ont des parades **différentes** ; un type inventé est pire qu'un type absent (`profil.md` §3).

---

## 5. Écriture du fichier

Copier `psy/docs/gabarits/journal.json` → `companion/outputs/journal/AAAA-MM-JJ.json`, remplir, `"source": "claude-code"`.

**Un fichier par jour, jamais de fichier partagé** (R1 du schéma — contrainte de transport).

### 5.1 ⭐ Une seule surface écrit le journal un jour donné *(depuis le 11/08/2026)*

Kokoro (K4) sait écrire le check-in depuis le téléphone, avec `"source": "android"`. **Deux surfaces qui écrivent le même jour écrivent le même nom de fichier** (R4) — et Google Drive, qui assure le transport, **accepte deux fichiers du même nom sans le signaler** (`README.md` §3).

**Conduite, sans exception :**

1. **Vérifier d'abord** si `companion/outputs/journal/<aujourd'hui>.json` existe — c'est déjà la règle du §0.3.
2. **Vérifier aussi le transit** si le doute existe : **`npm run psy:sync`** verse ce qui vient du téléphone **sans jamais écraser**, et signale tout doublon.
3. Si Xavier a fait son check-in sur le téléphone, **ne pas le refaire ici**. Le dire en une phrase, et s'arrêter.
4. La bascule **se déclare, elle ne se devine pas** : si Xavier annonce qu'il passe au téléphone, le PC cesse d'écrire le journal.

⚠️ **Ne jamais rattraper un jour manquant** parce qu'il a été saisi ailleurs. Un jour sans fichier n'est pas une donnée négative — **ce n'est pas une donnée du tout** (§3).

---

## 6. Clôture — deux phrases maximum

> « C'est enregistré. À demain si tu veux. »

Puis **s'arrêter**. Pas de synthèse, pas de conseil, pas de « pense à… », pas de proposition d'enchaîner sur autre chose.

**Une seule exception — la détection d'alerte, à coût de refus nul.** Si un seuil est franchi (`etat.md` §7 : les shutdowns sont l'indicateur n° 1 du burnout autistique), une **et une seule** phrase est permise, dans ce format exact :

> « Je te signale une chose, avec la raison : **3 shutdowns cette semaine contre 0 la précédente**. On peut en parler à la prochaine séance, ou pas. Dis juste "pas maintenant" si tu préfères — ça ne redemandera rien. »

Format invariable : **une phrase, une raison chiffrée et explicite, un refus qui coûte un geste et zéro justification.** Jamais de question ouverte, jamais de relance.

> 🔴 **Cette phrase ne se dit que dans une conversation que Xavier a ouverte lui-même, et elle ne quitte jamais cette conversation.** Elle n'est **pas** une interpellation : le dispositif ne va jamais chercher Xavier. **Kokoro ne la porte pas et ne la portera jamais** — il ne notifie de rien (`companion/README.md` §6). L'interpellation opportuniste plafonnée à 1/jour et 3/semaine, prévue au plan d'origine, **a été supprimée le 12/08/2026**.

---

## 7. Interdits absolus

- Conseiller une modification de traitement — **jamais**, sous aucune forme, même interrogative. Ça part au brief Dr Isorni (`etat.md` §5).
- Un compteur de régularité, une série, un taux d'objectif, une moyenne affichée à Xavier.
- « Écoute ton corps », « note ton anxiété sur 10 », « imagine », « fais un effort ». Cf. `profil.md` §7.
- Écraser un fichier existant.
