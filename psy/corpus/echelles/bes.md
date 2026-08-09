# BES — Binge Eating Scale ⚠️ *instrument incomplet, substitut comportemental disponible*

**Source :** Gormally, J., Black, S., Daston, S., Rardin, D. (1982). *The assessment of binge eating severity among obese persons.* Addictive Behaviors, 7(1), 47-55.
**Version française validée :** Brunault, P. et al. (2016) — traduction et validation française.
**Durée :** 10 minutes. **Identifiant `mesures/` :** `bes`.

---

## 1. 🔴 État de cet instrument — à lire avant tout

**Les 16 items du BES ne sont pas reproduits ici.** Chaque item est un groupe de 3 ou 4 énoncés pondérés dont la formulation exacte porte toute la cotation ; **une restitution approximative produirait un score faux et faussement rassurant** — exactement le résultat le plus dangereux dans ce dossier. Les recherches du 09/08/2026 n'ont pas permis d'obtenir l'instrument intégral en source fiable et librement diffusable.

> ⭐ **Il n'y a rien à acheter.** Le BES n'est **pas un instrument sous licence commerciale** : il n'est distribué par aucun éditeur de tests (contrairement au BDI, à la WAIS ou à la MMPI). C'est une échelle publiée dans un article de revue, libre d'usage en clinique et en recherche. **Le problème n'est pas le prix, c'est l'accès au texte** — la version française validée est parue dans une revue payante, et le PDF n'a pas pu être récupéré automatiquement.

**Ce qu'il faut faire pour le débloquer** — les voies sont classées par coût réel, la première est gratuite et prend cinq minutes :

| # | Voie | Détail |
|---|---|---|
| **1** ⭐ | **L'archive ouverte HAL** — [hal.science/hal-01379543](https://hal.science/hal-01379543) | Dépôt libre de **Brunault, P., Gaillard, P., Machet, L. *et al.* (2016).** *Validation de la version française de la Binge Eating Scale.* **L'Encéphale, 42(5), 426-433.** ⚠️ **À ouvrir dans un navigateur** : le site bloque les récupérations automatisées (protection anti-robot), pas les humains. **Si le PDF y est déposé en texte intégral, l'annexe contient les 16 items en français validé.** |
| **2** | **Écrire à l'auteur** — **Pr Paul Brunault**, CHRU de Tours / Université de Tours | Les auteurs d'échelles envoient leur instrument sur simple demande, c'est l'usage. Délai : quelques jours. |
| **3** | **ResearchGate** — « Request full-text » sur l'article | Même mécanisme, en un clic. |
| **4** | **Le demander au Dr Isorni** le **03/09** — déjà inscrit comme **question 11** du brief | Un psychiatre y a accès en routine. ⚠️ **Mais c'est la voie la plus lente** : elle consomme du temps d'un créneau déjà surchargé, pour un document qu'un site web peut fournir aujourd'hui. |
| **5** | La **Dr Bouarioua** | Le dépistage de l'hyperphagie fait partie du bilan standard d'une NASH. |

**Une fois obtenu :** PDF dans `ressources/originales/`, conversion via `npm run pdf-to-markdown`, items versés dans cette fiche.

> 📌 **Conséquence sur le brief :** si la voie 1 ou 2 aboutit avant le 03/09, **retirer la question 11 du brief.** Un créneau de consultation qui doit couvrir un SAOS sévère, une NASH, une ordonnance de voyage et six questions pharmacologiques n'a pas à être dépensé pour un document téléchargeable.

> ⚠️ **Ce blocage ne bloque pas le chantier alimentaire.** La grille du §4 est utilisable immédiatement, ne dépend d'aucune source externe, et est **mieux adaptée au profil** que le BES lui-même.

---

## 2. Ce que le BES mesure, et pourquoi la question est ouverte

La question à trancher est posée au rapport §6.5 et reste sans réponse : **existe-t-il des épisodes de perte de contrôle** — grande quantité en peu de temps, impossibilité de s'arrêter ? Réponse de Xavier au 08/08/2026 : *« je ne sais pas, je ne perçois pas bien »*.

**Deux diagnostics, deux traitements différents :**

| Hypothèse | Mécanisme | Ce qu'on fait |
|---|---|---|
| **Déficit intéroceptif seul** (hypothèse actuelle) | La satiété n'est pas perçue → l'arrêt du repas n'a aucun signal | **Structure externe** : quantité décidée avant, servie une fois → `protocoles/alimentation-structure-externe.md` |
| **Hyperphagie boulimique** (DSM-5 307.51 / F50.8) | Épisodes de perte de contrôle, avec détresse | **Traitement distinct** : TCC spécifique du trouble ; la structure externe seule ne suffit pas et peut même être contre-productive si elle induit de la restriction |

Les deux peuvent coexister. **C'est pour cela qu'on mesure au lieu de conclure.**

**Cotation du BES**, pour mémoire quand l'instrument sera versé : 16 items, chacun coté **0 à 3** selon l'énoncé choisi. **Total 0-46.**

| Total | Interprétation |
|---|---|
| **< 17** | Pas d'hyperphagie ou minime |
| **18-26** | Hyperphagie modérée |
| **≥ 27** | Hyperphagie sévère |

---

## 3. ⚠️ Ce que cette échelle ne dit pas

- 🔴 **Le BES interroge massivement des ressentis** : sentiment de perte de contrôle, culpabilité, honte, dégoût de soi après avoir mangé. **C'est précisément la classe de questions à laquelle Xavier ne peut pas répondre de façon fiable** (alexithymie probable, déficit intéroceptif confirmé). La tension avec la règle R6 était déjà actée au PLAN le 09/08/2026, avec l'arbitrage suivant, qui tient : **on le passe quand même, mais un score bas ne clôt pas la question à lui seul.**
- **Il n'est pas diagnostique.** C'est un instrument de dépistage : un score ≥ 27 appelle un entretien clinique, il ne pose pas le diagnostic d'hyperphagie boulimique.
- **Il a été construit sur une population obèse en demande de soin**, ce qui correspond au cas présent — c'est le point favorable de cet instrument ici.
- **Il ne mesure ni la quantité ingérée ni la structure des repas.** Un apport au double d'un adulte, régulier et sans épisode de perte de contrôle, donne un score BES bas. **Le score bas serait exact et n'infirmerait rien du problème** : c'est exactement la situation attendue si l'hypothèse du déficit intéroceptif est la bonne.

---

## 4. ⭐ Substitut comportemental — utilisable immédiatement, sans instrument

Puisque le BES demande d'introspecter et que le journal quotidien ne le peut pas, voici l'ancre comportementale correspondante. **Elle ne remplace pas le BES** — elle mesure autre chose, et cet autre chose est plus fiable chez Xavier.

**Cinq questions fermées, à poser une fois en séance, puis à re-poser après 3 semaines de structure externe :**

| # | Question — fermée, observable | Ce qu'elle discrimine |
|---|---|---|
| 1 | « Est-il arrivé, ce mois-ci, que tu continues à manger **après que le plat servi était fini** — en te resservant, ou en allant chercher autre chose ? Combien de fois ? » | Comportement de resservage. Un comptage, pas un ressenti. |
| 2 | « Est-il arrivé que tu manges **en moins de 30 minutes** une quantité que tu décrirais toi-même comme nettement supérieure à un repas normal ? Combien de fois ? » | Critère **quantité + vitesse** de l'hyperphagie, sans passer par « perte de contrôle ». |
| 3 | « Est-il arrivé que tu manges **seul parce que la quantité te gênait** vis-à-vis de quelqu'un ? » | Critère DSM-5 d'hyperphagie, formulé en comportement observable (manger seul) et non en émotion (honte). |
| 4 | « Est-il arrivé que tu **t'arrêtes de manger alors qu'il restait de la nourriture disponible** ? Combien de fois ? » | ⭐ **La question décisive.** Un arrêt en présence de nourriture disponible suppose un signal d'arrêt. **Zéro sur cette question est plus informatif que n'importe quel score BES** : cela signifie que ce qui arrête le repas est l'épuisement du stock, jamais un signal interne. |
| 5 | « Est-il arrivé que tu manges **sans avoir eu faim au départ** ? Combien de fois ? » | Détection de la faim, versant symétrique de la satiété. |

**Lecture :**
- Beaucoup de « 0 » aux questions 4 et 5 + peu d'épisodes rapides (question 2) → **profil de déficit intéroceptif pur**. La structure externe est le bon traitement, et le BES sera probablement bas — sans que cela n'infirme quoi que ce soit.
- Épisodes rapides fréquents (question 2) + manger seul par gêne (question 3) → **signal d'hyperphagie**, à porter au Dr Isorni **avant** de pousser le chantier alimentaire, la restriction pouvant aggraver un trouble des conduites alimentaires.

**Ces cinq réponses s'écrivent dans `mesures/AAAA-MM-JJ-bes.json`** avec `"version": "grille-comportementale-locale"`, `score: null` et les cinq comptages dans `sous_scores`. Le champ `notes` doit porter que le BES formel n'a pas été passé.

---

## 5. Ce qu'on écrit dans `mesures/`

```json
{
  "date": "2026-08-16",
  "echelle": "bes",
  "version": "grille-comportementale-locale",
  "score": null,
  "score_max": null,
  "seuil": null,
  "sous_scores": {
    "resservages": null,
    "episodes_rapides": null,
    "manger_seul_par_gene": null,
    "arrets_avec_nourriture_disponible": null,
    "manger_sans_faim": null
  },
  "reponses": [],
  "passation": "claude-code",
  "notes": "BES formel non passé — instrument non obtenu au 09/08/2026. Grille comportementale du corpus §4."
}
```
