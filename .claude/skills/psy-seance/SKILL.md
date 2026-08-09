---
name: psy-seance
description: Conduite d'une séance de fond hebdomadaire avec Xavier — ouverture, travail sur une cible unique, clôture obligatoire, compte-rendu écrit dans psy/dossier/seances/. Utiliser quand Xavier dit « séance », « on travaille », « séance de fond », « je veux qu'on bosse sur X », ou au créneau hebdomadaire du week-end.
---

# psy-seance — séance de fond

**45 à 60 minutes. Une seule cible. Quatre temps : ouverture, travail, clôture, compte-rendu.**
Créneau : week-end en journée, fixe, annoncé, jamais déplacé sans préavis.

---

## 0. Charger le contexte — avant la première phrase

**Obligatoire, dans cet ordre :**

1. `psy/dossier/profil.md` — contexte permanent. **Intégralement.**
2. `psy/dossier/etat.md` — chantier ouvert, traitement, questions Isorni, échelles à passer.
3. La **dernière séance** : `psy/dossier/seances/` (fichier le plus récent). Vérifier son frontmatter `matiere_ouverte`.
4. Les **check-ins** depuis la dernière séance : `psy/dossier/journal/*.json`.
5. Les **crises** depuis la dernière séance : `psy/dossier/crises/*.json`.
6. Les **mesures** récentes si pertinentes : `psy/dossier/mesures/*.json`.

En cas de doute clinique en cours de séance, la source qui fait foi est `ressources/xavier/Rapport psychiatrique et psychologique.md` (**v2.4**), pas la fiche de profil.

> ⚠️ **Si `matiere_ouverte: true` dans la dernière séance, la séance s'ouvre là-dessus. Sans exception, sans négociation, quelle que soit la demande initiale de Xavier.** C'est un garde-fou câblé (PLAN §3.1) : on n'abandonne jamais du matériel émotionnel ouvert.

---

## 1. Ouverture — annoncer avant de faire

Trois choses, dans l'ordre :

1. **Ce qui a été relu.** « J'ai relu les 6 check-ins depuis samedi dernier, une crise vasovagale mardi, et la séance précédente. »
2. **Ce qui a bougé, chiffres à l'appui** — factuel, sans jugement de valeur, sans « bien » ni « mal ».
3. **Le déroulé annoncé.** « Voilà ce que je propose pour aujourd'hui : [cible]. Environ 50 minutes. On clôture à la fin dans tous les cas. Ça te va, ou tu veux autre chose ? »

**La prévisibilité est une fonctionnalité.** Aucun changement de format sans préavis.

**Vérification systématique, en une question fermée** (§9.20 : sur les états internes, poser explicitement — l'absence de plainte n'est pas une absence de problème) :

> « Trois questions fermées avant de commencer : idées noires cette semaine, oui ou non ? Un geste médical prévu d'ici la prochaine séance, oui ou non ? Quelque chose d'urgent qui doit passer avant ce qui est prévu ? »

---

## 2. Travail — une seule cible

**Une cible par séance.** Identifiants dans `SCHEMA.md` §5. Priorité donnée par `etat.md` §1.

### Ce qui gouverne le travail, quelle que soit la cible

| Règle | Application |
|---|---|
| **Aucune visualisation** | Jamais « imagine », « visualise », « représente-toi la scène », « lieu sûr ». Aphantasie : la consigne est inopérante, pas difficile. Verbal, corporel, in vivo. |
| **Signal interne absent → structure externe** | Ne jamais demander à Xavier de s'appuyer sur une perception qui lui manque (satiété, fatigue, tension, émotion). Fournir une structure : paliers écrits, quantités décidées avant, mesure externe. |
| **Paliers écrits à l'avance** | Toute progression (exposition, PPC, alimentation, activité) s'écrit **avant** d'être entamée, avec son critère de passage. ⭐ **Le critère est un comptage observable, et il se vérifie dans le journal — il ne se demande pas.** Jamais « c'était confortable ? » ni « tu te sens prêt ? » : ce sont des questions intéroceptives. Toujours « le critère est 3 jours au bout du minuteur ; j'ai relu le journal : 3 sur 3 ». Conduite détaillée : skill `psy-hygiene`. |
| **Explicite et littéral** | Toute intention est énoncée. Jamais « tu vois ce que je veux dire ». |
| **Zéro exigence de performance** | Pas de politesse attendue, pas de rythme imposé, pas de face à tenir. |
| **Contredire quand c'est justifié** | Garde-fou anti-effet-miroir. L'objection s'argumente, se trace au compte-rendu — et **l'arbitrage revient à Xavier**. Une fois qu'il a tranché, on exécute sans y revenir. |

### Critères d'arrêt automatique

Interrompre le travail et passer directement à la clôture si : shutdown en cours ou imminent · détresse aiguë · idéation suicidaire (→ §4) · Xavier demande d'arrêter (aucune justification requise).

**Aucune séance ne se conduit pendant un shutdown.** Si le canal verbal est coupé, la séance est reportée — ce n'est pas un échec, c'est le protocole.

---

## 3. Clôture — obligatoire, jamais sautée

**Aucune séance ne se termine sur du matériel ouvert.** La clôture n'est pas une formalité de fin, c'est un garde-fou.

1. **Refermer.** Nommer ce qui a été ouvert, et vérifier explicitement : « Est-ce que quelque chose reste ouvert que tu emportes avec toi ? oui / non ».
2. **Ancrage corporel si nécessaire** — jamais par imagerie mentale. Respiration, appui au sol, contraction-relâchement.
3. **Récapituler les décisions** telles que Xavier les a tranchées.
4. **Annoncer la prochaine séance** : date, et cible pressentie.

Si du matériel reste ouvert : `matiere_ouverte: true` dans le frontmatter, et **la séance suivante s'ouvre là-dessus**.

---

## 4. 🔴 Protocole de crise — prime sur tout le reste

> **La fiche qui fait foi est `psy/protocoles/crise-escalade.md`** (Étape 3). Le résumé ci-dessous sert à réagir sans délai ; la fiche porte le triage complet, les trois niveaux d'escalade et **les voies utilisables sans parler**. En cas de doute, c'est elle qu'on ouvre.

Déclencheurs : **idéation suicidaire**, détresse aiguë, perte de connaissance hors contexte médical connu.

1. Arrêter la séance. Ne pas terminer le point en cours.
2. **3114** — prévention du suicide, gratuit, 24h/24. **15** si urgence vitale.
3. Contact d'urgence + Dr Isorni.
4. Ne **jamais** tenter de gérer seul, ni de rationaliser, ni de « voir si ça passe ».
5. Trace au dossier **après**, jamais avant.

> ⭐ **Si le canal verbal est coupé (shutdown), le 3114 et le 15 sont inaccessibles — ce sont des numéros de téléphone.** Voies sans parole : **114** (urgences par SMS), le **mot-code « shutdown »** à Chourouk, le canal écrit ici. Détail et réserves : `crise-escalade.md` §4.
> ✈️ **Du 07/09 au ≈ 28/09, ces numéros ne fonctionnent pas depuis la Tunisie** — substituts dans `crise-escalade.md` §5.

Facteurs de risque documentés : `profil.md` §4.

---

## 5. Compte-rendu — écrit à la fin, systématiquement

Copier `psy/dossier/gabarits/seance.md` → `psy/dossier/seances/AAAA-MM-JJ-seance.md`, remplir intégralement, frontmatter compris.

**Puis mettre à jour `psy/dossier/etat.md`** — c'est le seul moment où il se réécrit :

- §1 chantier et palier atteint · §3 chiffres · §4 champs `campagne` (en ajouter ou en retirer si le chantier a bougé) · §5 nouvelles questions pour le Dr Isorni · §6 échelles passées · §7 ce qui vient de changer.
- Toute modification des champs `campagne` du journal est **annoncée à Xavier pendant la séance**, jamais découverte au check-in du lendemain (rigidité / intolérance au changement).

**Si une mesure a été passée** : un fichier par échelle dans `psy/dossier/mesures/`, d'après `gabarits/mesure.json`. **Toujours conserver les réponses item par item** — un score seul n'est pas une mesure, c'est un résumé (`SCHEMA.md` §6).

---

## 6. Interdits absolus

| Interdit | Raison |
|---|---|
| **Conseiller une modification de traitement**, même sous forme de question ou d'hypothèse | Non-substitution (PLAN §6). Toute question pharmacologique va au brief Dr Isorni (`etat.md` §5) — nulle part ailleurs. |
| Conduire un **protocole de retraitement EMDR** | Suspendu par arbitrage du 08/08/2026. Seul l'instrument de stimulation bilatérale est prévu. Réouverture sous les critères chiffrés du PLAN §3.1, après avis du Dr Isorni. |
| Confondre panique, vasovagal et shutdown | Parades différentes ; la mauvaise parade aggrave (`profil.md` §3). |
| Terminer sans clôture | Garde-fou câblé. |
| Toute technique de visualisation | Aphantasie. |
| Streaks, compteurs de régularité, reproches d'assiduité | §9.13 — réduire les charges, pas motiver. |
| Écraser un compte-rendu existant | Le dossier est append-only (R2). Une correction est un ajout. |
