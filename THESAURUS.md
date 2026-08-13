# THESAURUS — le vocabulaire du dispositif

**Statut :** normatif — v1.0 (13/08/2026)

> **Pourquoi ce document existe.** Le dispositif manipule une quinzaine de mots qui se ressemblent — protocole, thérapie, programme, fiche, étape, corpus, palier, chantier, cible. Tant qu'ils flottent, deux surfaces peuvent dire la même chose et vouloir dire deux choses différentes, **et personne ne s'en aperçoit.**
>
> **Règle : un mot = une chose.** Quand un mot de cette liste apparaît dans le projet, il a le sens ci-dessous et pas un autre. Si un besoin nouveau n'entre dans aucune case, **on ajoute une entrée ici avant d'écrire quoi que ce soit d'autre.**

**Le document de référence reste [`PLAN.md`](PLAN.md).** Celui-ci ne décide rien — il nomme.

---

## 1. Qui — les cinq personas

| Terme | Ce que c'est | Ce que ce n'est pas |
|---|---|---|
| **Claude Psy** | Le psychiatre et le psychologue. Six skills cliniques. Il **produit tout le contenu** et le publie dans Kokoro. | Pas un chatbot. Pas un prescripteur. |
| **Claude Superviseur** | Le superviseur **de Claude Psy**. Un skill. Sa passe est **bloquante avant publication**. | ❌ **Il ne supervise jamais Xavier.** |
| **Kokoro (心)** | Le compagnon, sur le téléphone. Il **protège · accompagne · éduque · réconforte**. Il affiche ce qu'on lui donne. | ❌ Il ne décide rien, n'interprète rien, ne calcule rien, **et ne vient jamais vers Xavier**. |
| **Xavier** | Le patient. | Pas un utilisateur à engager. |
| ⭐ **L'aide-au-patient** *(13/08/2026)* | La personne qui **tient le téléphone** pendant une séance à deux et exécute les consignes de Kokoro. Aujourd'hui : **Chourouk**. | ❌ **Pas une thérapeute.** Elle ne juge pas, n'improvise pas, n'interprète pas. Elle suit un déroulé. |

> ⚠️ **« Aide-au-patient » est un rôle, pas une personne.** Chourouk le tient aujourd'hui ; le mot désigne la fonction.

---

## 2. Quoi — les objets de contenu

**Ils se distinguent par une seule question : *écrit pour qui ?***

| Terme | Où ça vit | Écrit **pour** | Contenu |
|---|---|---|---|
| **Corpus** | `psy/corpus/` | **Claude Psy** | La **littérature** : article, échelle publiée, recommandation. Une source citable, avec sa date et **ce qu'elle ne dit pas**. |
| **Protocole** | `psy/protocoles/` | **Le praticien** | La **fiche qu'on applique**, adaptée au profil. Porte diagnostics, pronostics, réserves, hypothèses non tranchées, frontières de non-substitution. |
| ⭐ **Fiche de bibliothèque** | `psy/programme/bibliotheque/` | **Xavier** *(ou l'aide-au-patient)* | Ce qu'il y a **à faire, et pourquoi**. **Réécrite**, jamais copiée d'un protocole. |
| **Programme** | `psy/programme/programme.json` | **Kokoro** | La liste des **étapes** du moment. Un fichier, une version, une supervision. |
| **Rapport** | `ressources/xavier/Rapport…md` | **Claude Psy** | Le document clinique de référence (**v2.4**). **En cas de doute clinique, c'est lui qui fait foi**, jamais une fiche. |
| **Dossier** | `psy/dossier/` | **Le dispositif** | La mémoire longitudinale — **source de vérité unique**. |

> 🔴 **La confusion à ne jamais commettre : protocole ≠ fiche de bibliothèque.**
> Un protocole est écrit **pour le praticien**. Une fiche de bibliothèque est écrite **pour Xavier**.
> **Copier l'un dans l'autre est le contrôle C9 du Superviseur** — et c'est un refus de publication.

---

## 3. Comment — les unités de travail

| Terme | Définition | Granularité |
|---|---|---|
| **Axe** | Un des cinq versants du dispositif : expertise, suivi, thérapies, somatique, présence. | Le projet |
| **Étape** *(de la feuille de route)* | Une tranche de construction du dispositif, numérotée 0 à 6. | Des semaines |
| **Jalon** | Une tranche de construction **de Kokoro**, numérotée K0, K1, … | Des jours |
| ⭐ **Chantier** | Un **domaine de travail somatique** ouvert : PPC, alimentation, activité physique. **À partir du palier 1, un seul chantier progresse à la fois.** | Des mois |
| ⭐ **Cible** | Ce sur quoi **une séance** travaille. **Une seule par séance.** Identifiants au `PLAN.md` §7.6. | Une séance |
| ⭐ **Palier** | Un **cran d'exposition graduée** à l'intérieur d'un chantier ou d'un protocole. On monte quand un **critère comptable** est atteint — jamais quand on « se sent prêt ». | Des jours |
| **Étape** *(du programme)* | Une **ligne affichée dans Kokoro**. Six types, §5 ci-dessous. | Minutes |

> ⚠️ **« Étape » a deux sens et c'est le seul mot du projet dans ce cas.** L'un désigne une tranche de feuille de route, l'autre une ligne dans Kokoro. Le contexte tranche toujours ; en cas de doute, dire **« étape de la feuille de route »** ou **« étape du programme »**.

> ⭐ **Palier ≠ progression.** Un palier se cote **en séance**, à partir du journal. **Il n'apparaît jamais dans Kokoro** — ni le palier atteint, ni l'historique, ni un pourcentage. C'est ce qui empêche un streak d'exister.

---

## 4. Quand — les rythmes

| Terme | Rythme | Support | Ce qui s'y passe |
|---|---|---|---|
| **Check-in** | Quotidien, < 2 min | **Kokoro** | Compteurs et choix fermés. **Aucune saisie de texte.** → `dossier/journal/` |
| **Séance** *(de fond)* | Hebdomadaire, week-end | Claude Code | ⭐ **Le battement du dispositif.** Ouverture / **une seule cible** / clôture obligatoire / compte rendu. **Seule fenêtre d'écriture du programme.** |
| ⭐ **Séance à deux** *(13/08/2026)* | Ponctuelle | **Kokoro**, tenu par l'**aide-au-patient** | Un déroulé **chronométré**, lu et exécuté par l'aide. §6 ci-dessous. |
| ⭐ **Entraînement** *(13/08/2026)* | Avant toute séance à deux | **Kokoro** | La **même séquence, à blanc**, sans le matériel réel. Sert à ce que l'aide connaisse le déroulé **avant** que ça compte. |
| **Passation** | Ponctuelle | **Kokoro** *(sauf PHQ-9)* | Une **échelle** administrée item par item. → `dossier/mesures/` |
| **Brief** | Avant consultation | Claude Code | Une page pour le Dr Isorni. **Xavier relit et décide de transmettre.** → `dossier/briefs/` |
| **Supervision** | 🔴 **Avant toute publication** + mensuelle | Claude Code | La passe du Superviseur. **Bloquante.** → `psy/agent/supervisions/` |

> 🔴 **Séance ≠ séance à deux.** La première est une conversation hebdomadaire avec Claude Psy. La seconde est un **exercice**, exécuté par l'aide-au-patient, décidé lors de la première. **Une séance à deux ne remplace jamais une séance de fond.**

---

## 5. Les six types d'étape du programme

*Contrat complet : [`PLAN.md` §8](PLAN.md#8-le-programme--format).*

| Type | Ce que Kokoro affiche | Ce qu'il renvoie |
|---|---|---|
| `ecran` | Ouvre une fonction déjà construite dans l'app (mot-code, tension appliquée…) | selon l'écran |
| `exercice` | Un déroulé guidé **au minuteur**, pour Xavier seul | `termine` · `arrete_avant_la_fin` |
| `questionnaire` | Des questions **fermées**, une par écran | les réponses item par item |
| `demarche` | **Une chose à faire dans le monde réel** — un appel, un email, une demande | `fait`, ou rien |
| `fiche` | Un texte à lire — ou à **montrer** à quelqu'un | rien |
| ⭐ `seance-duo` | Un déroulé **chronométré, tenu par l'aide-au-patient** | `termine` · `arrete_avant_la_fin` · `entrainement` |

**Deux attributs transverses :**

| Terme | Sens |
|---|---|
| **Rubrique** | Le **groupement d'écran** : `crise` · `therapie` · `bilan` · `documentation`. C'est là que Xavier va chercher. |
| **`quand`** | `aujourdhui` · `au_besoin` · `sans_date`. **Ce n'est pas une échéance** — rien n'est en retard, jamais. |

> ⭐ **`sortie_libre` vaut toujours `true`.** Le champ existe pour que ce soit **écrit**, pas pour être mis à `false`. Sortir avant la fin **n'est pas un échec** et ne se commente nulle part.

---

## 6. Le vocabulaire de la séance à deux

| Terme | Sens exact |
|---|---|
| **Porteur** | Qui **tient le téléphone**. Sur une `seance-duo`, c'est **toujours l'aide-au-patient**. |
| **Consigne** | Une instruction **littérale et chronométrée**, adressée soit à l'aide (`pour: "aide"`), soit lue à voix haute au patient (`pour: "patient"`). |
| **Séquence** | La suite ordonnée de consignes. **L'aide ne fait que ce que Kokoro affiche** — elle n'ajoute rien, n'anticipe rien. |
| 🔴 **Signal d'arrêt** | Le geste, **convenu à froid**, par lequel Xavier arrête la séance **sans parler**. Il est rappelé à l'écran en permanence. **Sans lui, aucune séance à deux ne démarre.** |
| **Critères d'arrêt** | Les conditions qui **imposent** l'arrêt, quoi qu'il arrive. Accessibles **en un tap, à tout moment**. |
| **Mode entraînement** | La séquence jouée **à blanc**, sans matériel réel. Renvoie `issue: "entrainement"` — **ce n'est pas une donnée clinique** et rien ne s'en déduit. |

---

## 7. Les mots de sécurité — ceux qu'on ne relâche jamais

| Terme | Sens |
|---|---|
| **Non-substitution** | Le dispositif **complète** le Dr Isorni, il ne le remplace pas. **Aucun conseil de modification de traitement, jamais, même sous forme interrogative.** |
| **Les trois mécanismes** | **Panique** ≠ **vasovagal** ≠ **shutdown**. Trois parades différentes ; **la mauvaise parade aggrave**. |
| **Mot-code** | Le mot **« shutdown »**, envoyé par SMS à Chourouk depuis l'écran verrouillé. Il dit *la parole est coupée*, rien d'autre. |
| **Escalade** | La conduite déclenchée par une **idéation suicidaire ou une détresse aiguë** → **3114**. Non contournable. **Elle n'est jamais dans une interface** — c'est une conduite, pas un bouton. |
| **Structure externe** | ⭐ **La règle centrale.** Quand un signal interne manque, on ne le remplace pas par de la volonté — **on le remplace par une structure externe explicite.** |
| **R6** | **On cote des comportements observables, pas des ressentis.** Jamais « note ton anxiété sur 10 ». |
| **Dérivé** | Un contenu publié est **dérivé** du dossier — il porte ce qu'il y a **à faire**, jamais ce qui a été **constaté, mesuré ou diagnostiqué**. Le contraire serait un **extrait**, et c'est interdit. |

---

## 8. Les mots interdits

**Ils ne décrivent rien dans ce dispositif, parce que la chose n'existe pas.**

| Mot | Pourquoi il n'existe pas |
|---|---|
| **Streak**, série, régularité, assiduité, observance présentée comme une note | Il n'y a rien à motiver ; il y a des charges à réduire. |
| **Objectif atteint**, pourcentage, progression | Kokoro ne compte jamais d'un jour à l'autre. |
| **Rappel**, relance, notification, interpellation | **Kokoro ne vient jamais vers Xavier.** *(Seule exception : l'accès crise sur l'écran verrouillé — **une porte, pas un rappel**.)* |
| **Retard**, oubli, manquement | Un jour sans check-in **n'est pas une donnée négative — ce n'est pas une donnée du tout.** |
| **Visualisation**, « imagine », « lieu sûr » | Aphantasie mesurée à **18/80**. La consigne est **inopérante**, pas difficile. |
| « **Aux premiers signes** », « quand tu sens », « as-tu besoin » | Déclenchement sur **repère externe**, jamais sur un prodrome. |

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 13/08/2026 | Création, à la demande de Xavier. Fixe les cinq personas, les six objets de contenu, les unités de travail (**dont la double acception d'« étape »**, signalée plutôt que masquée), les rythmes, les six types d'étape du programme et le vocabulaire neuf de la **séance à deux**. |
