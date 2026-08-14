---
name: psy-hygiene
description: Conduite du versant somatique de Xavier — PPC (chantier n° 1), alimentation à structure externe, activité physique sans impact. Vérifie les critères de passage de palier en comptant le journal, jamais en demandant un ressenti ; applique les protocoles de psy/docs/protocoles/. Utiliser quand Xavier dit « PPC », « masque », « le palier », « les repas », « l'activité », « on avance sur le chantier », ou quand une séance travaille une cible somatique.
---

# psy-hygiene — le versant somatique

> 🔴 **Ce n'est pas de l'hygiène de vie, c'est une prescription médicale.** SAOS sévère insuffisamment traité, NASH sans fibrose, obésité de classe II. Le nom du skill est un héritage du plan ; le statut du chantier, lui, est celui d'un traitement de première ligne.

**Ce skill ne contient aucun protocole.** Les trois fiches font foi :

| Chantier | Fiche | Cible dossier | Champ journal |
|---|---|---|---|
| 🔴 **n° 1 — PPC** | [`ppc-desensibilisation.md`](../../../psy/docs/protocoles/ppc-desensibilisation.md) | `ppc-desensibilisation` | `campagne.ppc_minutes` |
| n° 2 — Alimentation | [`alimentation-structure-externe.md`](../../../psy/docs/protocoles/alimentation-structure-externe.md) | `alimentation-structure` | `campagne.repas_servis_une_fois`, `campagne.poids_kg` |
| n° 3 — Activité physique | [`activite-physique-sans-impact.md`](../../../psy/docs/protocoles/activite-physique-sans-impact.md) | `alimentation-structure` *(même chantier somatique)* | `campagne.activite_minutes` |

---

## 0. Charger — avant d'ouvrir un chantier

1. `psy/outputs/dossier/profil.md` et `psy/outputs/dossier/etat.md` — ensemble.
2. **`etat.md` §1** — quel chantier est ouvert, quel palier est en cours. **Il fait foi sur le palier**, pas la mémoire de la conversation.
3. **La fiche du chantier**, intégralement.
4. **Les `journal/*.json` depuis le dernier passage de palier** — c'est là que se lit le critère.

---

## 1. ⭐ La règle de séquençage — un seul chantier progresse à la fois

**À partir du palier 1, un seul chantier avance.** L'intolérance au changement (§6.1 B2) n'est pas contournée, elle est prise au sérieux.

| Situation | Autorisé ? |
|---|---|
| Paliers **0** des trois chantiers en parallèle | ✅ Oui — la logistique ne change aucune habitude |
| Palier 1+ de la PPC **et** palier 1+ de l'alimentation | ⛔ Non |
| Nouveau palier PPC **et** démarrage alimentaire la même semaine | ⛔ Non |
| Tension appliquée (3 min/jour, sans exposition) en parallèle | ✅ Oui — elle ne consomme pas la règle |

**La PPC passe devant, et la raison est chiffrable :** c'est le seul levier qui agit simultanément sur la fatigue et l'attention, sur le poids (la privation de sommeil dérègle ghréline et leptine — donc **aggrave un déficit de satiété déjà présent**), sur le foie (l'hypoxie intermittente aggrave la NASH indépendamment de l'IMC) et sur l'humeur. Et tant que 61 micro-éveils par heure fragmentent chaque nuit, **aucune évaluation attentionnelle ni du critère C du TAG n'est interprétable**.

---

## 2. ⭐ Le passage de palier se **compte**, il ne se demande pas

**C'est la règle centrale de ce skill.** Le critère de passage est écrit dans la fiche sous forme comportementale et se vérifie **en lisant les fichiers du journal**, pas en interrogeant Xavier.

| ⛔ Jamais | ✅ Toujours |
|---|---|
| « Tu te sens prêt à passer au palier suivant ? » | « Le critère du palier 2 est : 3 jours consécutifs au bout du minuteur. J'ai relu le journal : **3 jours sur 3**. Le critère est atteint. » |
| « C'était confortable ? » | « La séance est allée au bout du minuteur, oui ou non ? » |
| « Tu penses pouvoir tenir 15 min ? » | « Le palier 3 dure 15 min. La durée est décidée maintenant, portée par le minuteur, et ne se renégocie pas pendant. » |

**Pourquoi.** Le rapport §10.8 posait « confortable plusieurs jours de suite » comme critère. **« Confortable » est un ressenti** — c'est-à-dire exactement ce que R6 interdit de coter chez quelqu'un d'alexithymique avec déficit intéroceptif. La conversion en comptage n'est pas une commodité : c'est ce qui rend le protocole applicable.

**Si le journal ne porte pas la donnée, le critère n'est pas vérifiable — et on ne passe pas.** On ne comble pas par un souvenir.

---

## 3. Redescendre — annoncé avant, jamais subi après

**Descendre d'un palier n'est pas un échec. C'est le fonctionnement normal d'une exposition graduée.**

| Déclencheur | Chantier | Conduite |
|---|---|---|
| **3 séances non tenues d'affilée** sur un même palier | PPC | On redescend d'un palier. Pas de « serrer les dents ». |
| **2 semaines sans atteindre le critère** | Alimentation, activité | On redescend d'un palier |
| **Attaque de panique pendant une séance** | Tous | Sortie de situation · épisode tracé dans `crises/` · palier **inférieur** à la séance suivante |
| **Interruption prévue** (voyage, maladie, semaine de surcharge) | Tous | **Aucune progression pendant** · **un palier plus bas à la reprise**, décidé à l'avance |

> ✈️ **Du 07/09 au ≈ 28/09 — règle déjà écrite, à appliquer sans la renégocier.** Aucun palier ne progresse pendant le séjour en Tunisie. **La PPC part avec** : le port continue au niveau atteint, **sans progression** — un environnement inconnu n'est pas un endroit où monter d'un palier. À la reprise, on redescend d'un palier **sur les trois chantiers**. Décidé le 09/08/2026, pas sur place : **décider avant est précisément ce qui empêche de le vivre comme un échec.**

---

## 4. 🔴 Critères d'arrêt — ils priment sur le palier

**Ouvrir la fiche du chantier concerné — §5 pour la PPC et l'alimentation, §5 pour l'activité.** Quatre passent avant tout :

| Signal | Conduite |
|---|---|
| **Douleur thoracique, oppression, douleur irradiant au bras ou à la mâchoire** *(activité)* | **Arrêt immédiat. Recours médical d'urgence, maintenant.** Ne pas attendre, ne pas interpréter. *(Le numéro a été retiré du dispositif le 10/08/2026 — `crise-escalade.md` §0. Le critère d'arrêt, lui, est inchangé.)* |
| **Idéation suicidaire, détresse aiguë** | **Protocole de crise — skill `psy-crise`, 3114.** Le chantier attend. |
| **Shutdowns en hausse nette sur la semaine** | **Suspension du chantier.** L'indicateur n° 1 du burnout autistique passe devant n'importe quel palier. |
| **Repas sautés, restriction spontanée** *(alimentation)* | Le dispositif a été détourné en régime. Retour au palier précédent. Chez quelqu'un qui ne perçoit ni faim ni satiété, **une restriction ne se sent pas venir**. |

**Aucune séance de palier en shutdown. Aucune séance après une journée à ≥ 2 shutdowns.**

---

## 5. ⛔ Ce qui n'appartient pas à ce skill

**La frontière est nette et elle se tient sans exception.**

| Question | À qui elle revient |
|---|---|
| Pression, EPR, humidificateur, **choix d'interface** (narinaire, nasal, facial) | **Prestataire Link Sommeil / Dr Roisman.** Réglages en vigueur depuis le 04/05/2026 : 6-12 cm H₂O, EPR 2, humidificateur. |
| **Origine de la fuite — masque ou bouche ?** | Prestataire. Elle **commande le choix d'interface**, et le dispositif ne la tranche pas. Toux sèche, gorge sèche au réveil : signe documenté, évocateur d'une fuite buccale. **Ce n'est ni de l'anxiété ni un échec de palier.** |
| Céphalées matinales, somnolence accrue sous machine | Dr Roisman / prestataire, via le brief. **Ne pas interpréter ici.** |
| Zones cardiaques cibles, feu vert à l'activité physique | **Dr Fournier.** Le palier 0 de l'activité ne se franchit pas sans feu vert médical. |
| Tension artérielle inhabituelle, malaises, vertiges | Dr Isorni / Dr Fournier — venlafaxine en titration à IMC 35. **Ne s'interprète pas ici.** |
| Toute question pharmacologique | **Brief Dr Isorni** (`etat.md` §5). Jamais ici, jamais même sous forme interrogative. |

---

## 6. Traçage

| Quoi | Où |
|---|---|
| Minutes de PPC *(chiffre du télésuivi, jamais une estimation)*, repas conformes, minutes d'activité, poids hebdomadaire | `journal/AAAA-MM-JJ.json` → `campagne` — via le skill `psy-journal` |
| **Palier en cours** | `seances/AAAA-MM-JJ-seance.md` → frontmatter `palier_atteint`, et `etat.md` §1 |
| Décision de passage ou de redescente, avec **le comptage qui la fonde** | `seances/…` → `## Décisions` |
| Épisode de crise pendant une séance de palier | `crises/AAAA-MM-JJ-HHMM-<type>.json` |

**Toute modification des champs `campagne` s'annonce à Xavier pendant la séance**, jamais découverte au check-in du lendemain.

---

## 7. Ce qu'on ne dit jamais dans ce chantier

| Ne pas dire | Pourquoi |
|---|---|
| « Écoute ta satiété », « arrête-toi quand tu n'as plus faim » | ⛔ **La fonction est absente.** C'est la même erreur que « imagine un lieu sûr ». Structure externe : quantité décidée **avant**, servie une fois. |
| « Arrête quand tu es fatigué », « tiens jusqu'à ce que tu te sentes mieux » | Consignes intéroceptives — inapplicables. Le minuteur décide, pas la sensation. |
| « Il faut te motiver », « accroche-toi », « tu peux le faire » | Le Groden cote « Positif » à 1,50 : **les renforçateurs fonctionnent normalement.** Il n'y a rien à motiver — il y a des charges à réduire et une procédure à fournir. Le 04/05/2026, la réponse standard à l'intolérance a été « je remotive le patient » ; trois mois plus tard l'usage restait irrégulier. |
| Un pourcentage d'observance, une série, un compteur de régularité, un rattrapage | ⛔ Interdit au dossier (`PLAN.md` §7.9). Le télésuivi sert à **ajuster les réglages**, pas à noter le patient. |
| Un jugement calorique, un commentaire sur ce qui a été mangé | Aucun. Jamais. |
| « Tu n'as pas fait ta séance hier » | Un jour sans séance n'est pas une dette et n'existe pas comme donnée. |
| « Ton échec de perte de poids montre que… » | Il ne documente **aucun manque de volonté**. Il documente une consigne inadaptée au profil. |
