# `DOSSIER.md` — le format du dossier clinique

> 🔴 **NORMATIF.** Ce document définit le format du dossier. Claude Code et Kokoro le lisent et l'écrivent. **Aucune surface n'a le droit d'inventer un format.**

**Pourquoi il existe :** le dossier est la **source de vérité unique**. C'est lui — pas les conversations — qui rend le suivi longitudinal possible. Si son format dérive, la mémoire longitudinale se dégrade **en silence** : les tendances deviennent incalculables, les comparaisons faussées, et l'avantage central du dispositif disparaît.

---

## 1. Les six règles invariables

| # | Règle | Raison |
|---|---|---|
| **R1** | **Un fichier par événement.** Jamais de fichier partagé auquel on ajoute des lignes | Deux appareils qui appendent au même fichier produisent un conflit. Un fichier par événement le rend **structurellement impossible**. ⚠️ **Renforcée par le transport retenu** : Google Drive accepte deux fichiers du même nom **sans rien signaler** |
| **R2** | **Append-only.** Un enregistrement daté n'est jamais réécrit ni supprimé. Une correction est un **ajout** | C'est un dossier clinique. L'historique doit rester lisible, **y compris ce qui s'est révélé faux**. Le `git log` est l'audit |
| **R3** | **Le format suit l'auteur.** Ce que **Claude** écrit → Markdown + frontmatter YAML. Ce qu'une **application** écrit → JSON | Chacun son format fiable. Pas de conversion, pas de format bâtard |
| **R4** | **Nommage `AAAA-MM-JJ` en préfixe, toujours** | Le tri lexicographique **est** le tri chronologique. Aucun index à maintenir |
| **R5** | **Aucun champ obligatoire ne demande d'écrire ou de parler.** Tout ce qui est requis est un nombre ou un choix fermé | Contrainte shutdown : le dossier doit rester alimentable quand le canal verbal est coupé |
| **R6** | ⭐ **On cote des comportements observables, pas des ressentis** | Alexithymie probable + déficit intéroceptif. « Note ton anxiété sur 10 » demande d'utiliser une fonction perceptive déficitaire — même erreur que « écoute ta satiété » |

> **R6 est la règle la plus facile à enfreindre sans s'en apercevoir.** À chaque champ ajouté : *« Xavier peut-il répondre en observant ce qu'il a fait, ou doit-il introspecter ce qu'il a ressenti ? »* Si c'est la seconde, le champ est mal conçu.
>
> ⭐ **R6 ne s'applique pas aux échelles validées.** Le journal quotidien reste strictement comportemental ; une échelle est un autre objet — une passation datée, avec un seuil publié, dont la validation psychométrique remplace l'ancre comportementale.

---

## 2. Arborescence

⭐ **Le dossier est réparti sur deux rôles, et la ligne de partage est celle de l'auteur** *(R3, et « aucun fichier n'a deux auteurs »)* : **ce que Claude Psy écrit vit chez le psy, ce que Kokoro écrit vit chez le compagnon.** C'est une répartition de fichiers, **pas** deux dossiers : **c'est une seule mémoire longitudinale, qui se charge en entier.**

```
psy/outputs/dossier/                     ← écrit par Claude Psy
  profil.md        fiche condensée — contexte PERMANENT, rechargé à chaque séance
  etat.md          état COURANT — chantier en cours, traitement, questions ouvertes
  seances/         comptes-rendus de séance ... MD   — AAAA-MM-JJ-seance.md
  crises/          épisodes de crise .......... JSON — AAAA-MM-JJ-HHMM-<type>.json
  mesures/         échelles cotées ............ JSON — AAAA-MM-JJ-<echelle>.json
  briefs/          briefs Dr Isorni ........... MD   — AAAA-MM-JJ-isorni.md

companion/outputs/                       ← écrit par Kokoro, versé par `npm run psy:sync`
  journal/         check-ins quotidiens ....... JSON — AAAA-MM-JJ.json (reconstruit par psy:sync)
  reponses/        ce que Xavier a fait ....... JSON — AAAA-MM-JJ-HHMM-<id>.json

psy/docs/gabarits/                       ← ni l'un ni l'autre : des modèles vierges
                   à copier, jamais à remplir sur place
```

> ⚠️ **`companion/outputs/` est du dossier clinique, malgré son emplacement.** Les six règles ci-dessus s'y appliquent entières — **R1 et R2 en particulier**. Un fichier n'y est jamais écrasé ni supprimé, et `npm run psy:sync` refuse de le faire.

**`profil.md` et `etat.md` sont les deux seules exceptions à R2** : ce sont des documents vivants, réécrits. Leur historique est tenu par git, et chacun porte un journal de révisions en pied de page.

**La distinction profil / état — à ne jamais confondre :**

| | `profil.md` | `etat.md` |
|---|---|---|
| Contenu | Ce qui ne change pas | Ce qui change |
| Exemples | TSA niveau 1, aphantasie, les 3 mécanismes de crise, ce qu'on ne dit jamais | Traitement en cours, poids, chantier ouvert, questions en attente |
| Révision | Rare (nouveau diagnostic, nouvelle contrainte) | Hebdomadaire (clôture de séance) |

Les deux se chargent **ensemble**, jamais l'un sans l'autre : le profil dit *qui est Xavier*, l'état dit *où on en est*.

---

## 3. `journal/AAAA-MM-JJ.json` — le check-in quotidien

Un fichier par jour. **Cible : moins de 2 minutes, zéro saisie de texte obligatoire.**

```json
{
  "date": "2026-08-13",
  "source": "android",
  "noyau": {
    "shutdowns": 0,
    "exposition_sociale": 1,
    "retrait_sensoriel": 0,
    "renoncements": 0,
    "activites_investies": 2,
    "sommeil_heures": 6.5,
    "missions_actives": 3
  },
  "campagne": {},
  "notes": null
}
```

| Champ | Type | Question fermée | Justification clinique |
|---|---|---|---|
| `shutdowns` | entier ≥ 0 | « Combien de fois as-tu perdu la parole ou été incapable de traiter une demande ? » | ⭐ **Indicateur n° 1** — la fréquence des pertes de parole est le meilleur indicateur de suivi du burnout autistique |
| `exposition_sociale` | 0-3 | « Combien d'heures d'interaction sociale non choisie ? » 0 = aucune · 1 = < 1 h · 2 = 1-3 h · 3 = > 3 h | Proxy comportemental du **camouflage**, qui prédit anxiété, dépression et épuisement indépendamment des traits autistiques. Mesure l'exposition, pas l'effort ressenti — R6 |
| `retrait_sensoriel` | entier ≥ 0 | « Combien de fois as-tu dû te retirer, mettre un casque, baisser la lumière, quitter une pièce ? » | Charge sensorielle. Comptage d'actions, pas d'inconfort — R6 |
| `renoncements` | entier ≥ 0 | « À combien de choses as-tu renoncé à cause de l'angoisse ? » | Ancre comportementale de l'anxiété : l'**évitement** est le critère D de l'agoraphobie, et il s'observe |
| `activites_investies` | 0-3 | « Combien d'activités as-tu pu investir hors obligations ? » | Ancre comportementale de l'humeur. La **clinophilie** est le marqueur dépressif documenté chez Xavier — on mesure ce marqueur-là, pas « ton moral sur 10 » |
| `sommeil_heures` | nombre ≥ 0 | « Combien d'heures de sommeil, réveils compris ? » | Critère C du TAG — et référence pour juger l'effet de la PPC |
| `missions_actives` | entier ≥ 0 | « Combien de missions professionnelles en cours ? » | **Seule variable d'ajustement disponible** — pas la famille, pas le sommeil |

**Ce que le noyau ne contient délibérément pas :** aucun champ « anxiété /10 », « humeur /10 », « fatigue /10 », « niveau de stress ». **Tous violeraient R6.**

**Campagne** — champs temporaires liés au chantier ouvert, et seulement lui. Quand le chantier se ferme, ses champs sortent : **le journal ne grossit jamais indéfiniment.** Les champs actifs sont déclarés dans `etat.md`.

```json
"campagne": { "ppc_minutes": 0, "repas_servis_une_fois": 3, "activite_minutes": 0, "poids_kg": null }
```

| Champ | Type | Justification |
|---|---|---|
| `ppc_minutes` | entier ≥ 0 | ⭐ Donnée **objective, issue du télésuivi de l'appareil**, jamais d'une auto-évaluation — exactement l'instrument qu'appelle un déficit intéroceptif |
| `repas_servis_une_fois` | entier 0-4 | On compte les repas **conformes à la structure**, jamais les calories |
| `activite_minutes` | entier ≥ 0 | Prescription médicale. Sans impact, domicile |
| `poids_kg` | nombre \| null | Hebdomadaire. `null` les autres jours |

**Règles de remplissage :**

- ⭐ **Un jour sans check-in est un jour sans fichier.** Aucun rattrapage rétroactif, aucune relance, aucune trace de manquement. **L'absence de fichier n'est pas une donnée négative — elle n'est pas une donnée du tout**, et un calcul de médiane ne doit jamais la compter comme un zéro.
- Un champ auquel Xavier ne répond pas est écrit `null`. **`null` ≠ `0`.**
- `notes` est **toujours** facultatif et **toujours** en dernier.
- `source` : `"claude-code"` | `"android"`. ⭐ **Deux valeurs, et il n'y en a pas d'autre** — il n'y a qu'une surface tournée vers Xavier.
- ⚠️ **Une seule surface écrit le journal un jour donné.** Vérifier avant d'écrire ; jamais de rattrapage.

> ⚠️ **Interdit dans le journal, en toute circonstance :** compteur de régularité, série, pourcentage d'objectif, moyenne mobile affichée à Xavier, rappel de manquement, jugement calorique. **Un compteur est une charge.**

---

## 4. `reponses/AAAA-MM-JJ-HHMM-<id>.json` — ce que Xavier a fait

Écrit par Kokoro, un fichier par carte rendue. **Format défini dans [`../companion/PROGRAMME.md`](../companion/PROGRAMME.md).**

🔴 **C'est le seul fichier que Kokoro écrit.** Le journal ci-dessus n'en est pas un : `npm run psy:sync` le **reconstruit** à partir des réponses de la carte d'`id` `check-in` — les `id` de question en `kebab-case` deviennent les clés en `snake_case`. **Le format du journal ne bouge pas d'un champ, et Kokoro ne l'interprète jamais.**

> ⭐ **`arrete_avant_la_fin` n'est pas un échec et ne se commente nulle part.** Une carte non faite ne produit aucun fichier — **et ce n'est pas une donnée**.

---

## 5. `crises/AAAA-MM-JJ-HHMM-<type>.json`

> 🔴 **La règle la plus importante du dossier : les trois mécanismes ne se confondent jamais.** Le champ `type` n'a pas de valeur par défaut et ne peut pas être laissé vide.

```json
{
  "horodatage": "2026-08-13T14:32:00+02:00",
  "type": "vasovagal",
  "contexte": "medical",
  "declencheur": "pose de cathéter",
  "duree_minutes": 8,
  "parade_utilisee": "tension_appliquee",
  "parade_efficace": true,
  "perte_de_connaissance": false,
  "source": "claude-code",
  "notes": null
}
```

| Champ | Valeurs | Notes |
|---|---|---|
| `type` | `panique` \| `vasovagal` \| `shutdown` \| `indetermine` | **Obligatoire.** `indetermine` est légitime — mieux vaut « je ne sais pas » qu'un type inventé. Le tri se fait après, à froid |
| `contexte` | `transport` \| `foule` \| `lieu_clos` \| `medical` \| `social` \| `conflit` \| `domicile` \| `autre` | |
| `parade_utilisee` | `tension_appliquee` \| `respiration` \| `retrait_sensoriel` \| `mot_code` \| `sortie_situation` \| `aucune` | `tension_appliquee` **uniquement** pour le vasovagal. `mot_code` uniquement pour le shutdown |
| `perte_de_connaissance` | booléen | **Discriminant capital** : la panique ne fait pratiquement jamais perdre connaissance ; le vasovagal, si. Un `true` sur un épisode typé `panique` **doit** déclencher une révision du typage en séance |

**Escalade :** si l'épisode comporte une idéation suicidaire ou une détresse aiguë, **le fichier s'écrit après le protocole de crise, jamais avant.**

---

## 6. `seances/AAAA-MM-JJ-seance.md`

```markdown
---
date: 2026-08-13
duree_minutes: 52
cible: ppc-desensibilisation
mesures_passees: [vviq]
palier_atteint: 2
programme_publie: 4
supervision: 2026-08-13-programme-v4
prochaine_seance: 2026-08-16
matiere_ouverte: false
---

## Ouverture
## Travail
## Clôture
## Décisions
## Repris à la prochaine séance
```

| Champ | Notes |
|---|---|
| `cible` | **Une seule cible par séance.** Identifiants : `ppc-desensibilisation`, `alimentation-structure`, `agoraphobie-exposition`, `tension-appliquee`, `shutdown-protocole`, `alexithymie-nommage`, `camouflage-pacing`, `tag-ruminations`, `deuil-ainee` |
| `palier_atteint` | Pour les cibles à paliers. `null` sinon |
| `programme_publie` | Version du programme publiée en clôture, ou `null` si rien n'a été publié |
| `supervision` | Fichier de supervision qui a visé cette publication. **Obligatoire si `programme_publie` n'est pas `null`** |
| `matiere_ouverte` | ⚠️ **Doit être `false` en fin de séance.** `true` signifie qu'on a ouvert du matériel émotionnel sans le refermer. Si `true`, la séance suivante s'ouvre là-dessus, **sans exception** |

**Règle de clôture non négociable :** aucune séance ne se termine sur du matériel ouvert. La section `## Clôture` est obligatoire et ne peut pas être vide.

---

## 7. `mesures/AAAA-MM-JJ-<echelle>.json`

Une passation = un fichier. Identifiants : `vviq`, `tas20`, `catq`, `bes`, `gad7`, `phq9`, `diva5`, `epworth`, `isi`, `maia`.

```json
{
  "date": "2026-08-09",
  "echelle": "vviq",
  "version": "VVIQ-16-Zeman",
  "score": 18,
  "score_max": 80,
  "seuil": { "valeur": 32, "sens": "en_dessous", "interpretation": "aphantasie" },
  "sous_scores": null,
  "reponses": [1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1],
  "passation": "claude-code",
  "notes": null
}
```

- `version` porte **l'identification exacte de l'instrument, sens de cotation compris.** Le VVIQ le démontre : Marks (1973) cote à l'envers, la convention dite « de Zeman » cote 1 = aucune image. **Un même score lu avec la mauvaise convention inverse la conclusion.**
- `reponses` conserve **toujours** les réponses item par item, **et en compte autant que l'instrument a d'items**. ⭐ **Un score seul n'est pas une mesure, c'est un résumé** — le rapport n'a pu re-coter l'AQ et l'EQ que parce que les réponses brutes existaient.
- `seuil.sens` : `au_dessus` | `en_dessous`.

---

## 8. `briefs/AAAA-MM-JJ-isorni.md`

Une page, format médecin : dense, factuel, sans interprétation gratuite.

**Frontmatter :** `date`, `consultation_prevue`, `periode_couverte`, `supervise` *(fichier de supervision — obligatoire avant transmission)*, `transmis` *(booléen — **Xavier relit et décide, à chaque fois**)*.

**Structure imposée :** Évolution chiffrée · Effets du traitement · Événements · **Questions à trancher** · Ce qui n'a pas changé.

**Deux règles de calcul :** les chiffres sont **calculés depuis le journal, jamais estimés**, et le **nombre de jours renseignés figure à côté de chaque chiffre**.

---

## 9. Ce que le dossier ne contient jamais

| Interdit | Raison |
|---|---|
| Un conseil de modification de traitement | Non-substitution |
| Un compteur de régularité, une série, un taux d'observance présenté comme une note | Réduire les charges, pas motiver. Le télésuivi PPC sert à **ajuster les réglages**, pas à noter le patient |
| Une échelle introspective sans ancre comportementale | R6 |
| Un champ obligatoire en texte libre | R5 |
| Des données sur Chourouk ou les filles au-delà de ce qui concerne directement Xavier | Elles n'ont pas consenti à un dossier |

---

## 10. Faire évoluer ce format

Ajouter un champ est un acte de conception, pas une commodité. **Trois questions avant tout ajout :**

1. **R6** — répond-on en observant, ou en introspectant ?
2. **R5** — le champ est-il remplissable en shutdown ?
3. **Coût** — qu'est-ce qu'on retire en échange ? Le journal a un budget de 2 minutes, et il est déjà dépensé.

**Toute modification est annoncée à Xavier avant d'être appliquée.**
