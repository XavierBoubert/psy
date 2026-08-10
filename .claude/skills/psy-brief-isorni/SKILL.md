---
name: psy-brief-isorni
description: Rédige le brief d'une page pour le Dr Isorni avant une consultation — évolution chiffrée tirée du journal, traitement, événements, questions à trancher. Écrit psy/dossier/briefs/AAAA-MM-JJ-isorni.md avec transmis:false ; Xavier relit et décide de transmettre. Utiliser quand Xavier dit « brief », « prépare la consultation », « le point pour Isorni », ou à la séance qui précède une consultation (la prochaine : 03/09/2026, brief à écrire au week-end du 29-30/08).
---

# psy-brief-isorni — brief de consultation

**Une page. Format médecin : dense, factuel, sans interprétation gratuite.**
**Xavier relit et décide de transmettre ou non, à chaque fois.** Le brief s'écrit toujours avec `transmis: false`.

---

## 0. Charger — avant d'écrire une ligne

1. `psy/dossier/profil.md` et `psy/dossier/etat.md` — ensemble.
2. **`etat.md` §5** — les questions ouvertes au Dr Isorni. **C'est le cœur du brief**, pas une annexe.
3. **Tous les `journal/*.json`** depuis le brief précédent (ou depuis le début s'il n'y en a pas) — c'est la source des chiffres.
4. **Tous les `crises/*.json`** de la période.
5. **Tous les `mesures/*.json`** de la période, avec leurs réserves.
6. Les `seances/*.md` de la période — pour les événements et les décisions.
7. Le brief précédent dans `briefs/`, s'il existe — pour la colonne « période précédente ».

En cas de doute clinique, la source qui fait foi est `ressources/xavier/Rapport psychiatrique et psychologique.md` (**v2.4**).

---

## 1. Les chiffres — comment ils se calculent

**Aucun chiffre n'est estimé ni reconstitué de mémoire.** S'il n'est pas dans le journal, il n'entre pas au brief — et **son absence s'écrit** (« non mesuré sur la période »), ce qui est une information à part entière pour le médecin.

| Indicateur | Calcul | Source |
|---|---|---|
| Shutdowns / semaine | somme ÷ nombre de semaines couvertes | `noyau.shutdowns` |
| Renoncements / semaine | idem | `noyau.renoncements` |
| Sommeil (h/nuit) | **médiane**, pas moyenne — les nuits de nourrisson produisent des extrêmes | `noyau.sommeil_heures` |
| Observance PPC (min/nuit) | **médiane** + nombre de nuits avec donnée | `campagne.ppc_minutes` |
| Poids | dernière valeur + delta sur la période | `campagne.poids_kg` |
| Missions actives | valeur en fin de période | `noyau.missions_actives` |

> ⚠️ **Un jour sans check-in est un jour sans fichier — ce n'est pas un zéro.** Ne jamais compter une absence comme une valeur nulle. Écrire le **nombre de jours couverts** à côté de chaque médiane : « 6,2 h/nuit (médiane sur 19 jours renseignés / 25 »).
>
> 🔴 **L'observance PPC se présente comme une donnée clinique, jamais comme une note.** Le télésuivi sert à **ajuster les réglages**, pas à noter le patient (`SCHEMA.md` §8). Écrire « 214 min/nuit en médiane sur 12 nuits renseignées », jamais « 48 % d'observance » ni « objectif non atteint ».

---

## 2. Structure imposée

Copier `psy/dossier/gabarits/brief-isorni.md` → `psy/dossier/briefs/AAAA-MM-JJ-isorni.md`. Les cinq sections sont obligatoires et gardent cet ordre (`SCHEMA.md` §7) :

| Section | Contenu | Piège |
|---|---|---|
| **Évolution chiffrée** | Le tableau du gabarit, période précédente / période couverte / tendance | Pas de commentaire dans le tableau. Les chiffres parlent. |
| **Traitement** | Molécule, palier, **date du changement**, délai écoulé, effets observés, effets indésirables | ⛔ **Aucune proposition de modification.** Voir §4. |
| **Événements** | Crises (type, contexte, parade, issue), événements de vie, gestes médicaux | Les trois mécanismes se nomment séparément — jamais « crises d'angoisse » en bloc. |
| **Questions à trancher** | Reprise de `etat.md` §5, **ordonnées par priorité, pas par numéro** | C'est la section utile. Chaque question porte son « pourquoi maintenant ». |
| **Ce qui n'a pas changé** | Le stable, explicitement | Utile au médecin : distingue le nouveau du chronique. |

---

## 3. 🔴 Les réserves qui accompagnent obligatoirement certains chiffres

**Un chiffre transmis sans sa réserve induit en erreur.** Trois sont câblées à ce jour :

1. **PHQ-9** — quatre items (sommeil, fatigue, concentration, ralentissement) sont **directement produits par le SAOS sévère insuffisamment traité** et peuvent porter le score en zone « modérée » sans dépression. **Le score ne part jamais seul.** Le Dr Isorni est précisément le praticien qui ignore encore le diagnostic de SAOS : sans la réserve, le chiffre le trompe.
2. **Toute échelle introspective** — un score élevé est informatif, **un score bas ne clôt aucune question** (alexithymie, déficit intéroceptif). À écrire tel quel.
3. **CAT-Q et VVIQ** — traduction locale non validée / normes anglophones. À mentionner dès que le chiffre sort du dossier.

**S'y ajoute, tant que le SAOS n'est pas effectivement traité :** aucune conclusion attentionnelle, aucune conclusion sur le critère C du TAG. **Le DIVA-5 reste bloqué**, et le brief doit dire pourquoi.

---

## 4. ⛔ Non-substitution — la ligne à ne jamais franchir

**Le brief pose des questions. Il ne propose jamais de réponse pharmacologique.**

| Interdit | Formulation correcte |
|---|---|
| « Il faudrait diminuer l'alprazolam » | « **Alprazolam et SAOS sévère** — limiter, remplacer, ou sans conséquence à la dose employée ? » |
| « La venlafaxine cause ses impatiences » | « **31 mouvements périodiques/h objectivés**, indépendants des apnées, sous venlafaxine reprise le 07/08. Ferritine jamais dosée. » |
| « Le palier semble insuffisant » | « Reprise le 07/08/2026. **27 jours écoulés** au 03/09. Observations sur la période : […]. » |

**Cela vaut aussi sous forme interrogative rhétorique.** « Ne faudrait-il pas envisager… ? » est une proposition déguisée. La forme admise est la **question ouverte au prescripteur**, adossée à un fait daté.

---

## 5. ⏱️ Ce que le brief du 29-30/08 doit obligatoirement porter

**Consultation le jeudi 03/09/2026 à 12h30 — la dernière avant fin septembre** (départ en Tunisie le 07/09, retour ≈ 28/09).

- ✈️ **L'ordonnance de venlafaxine pour un séjour de plus de 3 semaines** — quantité, marge, bagage **cabine**, justificatif. **La venlafaxine ne se manque jamais** : l'arrêt brutal expose à un syndrome de sevrage marqué. **Logistique, jamais posologie.**
- ✈️ **L'alprazolam prévu pour le vol** — déjà prescrit « si besoin », donc son emploi n'est pas une modification de traitement ; **mais le point de vigilance benzodiazépine / SAOS n'a jamais été instruit.** Élément nouveau à donner : **la PPC part en Tunisie** et sera utilisée pendant le séjour. **Arbitrage au Dr Isorni — le dispositif ne se prononce pas.**
- 🔴 **Les deux diagnostics qu'il ignore : SAOS sévère (IAH 35/h) et NASH.** Les deux courriers du Dr Roisman (19/01 et 04/05/2026) sont partis au seul Dr Fournier. **⭐ IAH résiduel < 6/h sous appareil** — l'efficacité est démontrée, seul le port manque.
- ~~**Le 114 par SMS est-il utilisable en shutdown ?**~~ (question 12) — ✅ **caduque le 10/08/2026** : les numéros d'appel d'urgence ont été retirés du dispositif à la demande de Xavier (`crise-escalade.md` §0). **Un créneau de consultation économisé.**
- **Tension appliquée et tension artérielle** (question 13) — avant de démarrer le palier 1.
- **Demander l'échelle BES** (question 11) — une demande de document, pas une question clinique.

> ⚠️ **L'email part avant la consultation** (`ressources/xavier/20260808 Email au Dr Isorni.md`, rédigé, non envoyé au 09/08/2026). **Un créneau de consultation ne suffit pas à découvrir un SAOS sévère, une NASH et six questions en même temps.** Le brief prépare la consultation ; l'email prépare le médecin.

---

## 6. Après l'écriture

1. **Lire le brief à Xavier** — ou lui dire où il est. `transmis: false` **reste faux** tant qu'il n'a pas décidé.
2. **Ne rien transmettre soi-même.** Le dispositif n'écrit à aucun praticien.
3. **Mettre à jour `etat.md`** §5 (questions restantes) et §7 (ce qui vient de changer) — comme à toute clôture de séance.

---

## 7. Interdits absolus

- **Proposer une modification de traitement**, sous quelque forme que ce soit.
- **Transmettre un score sans sa réserve.**
- **Présenter l'observance PPC comme une note, un pourcentage d'objectif ou un taux de régularité.**
- **Compter un jour sans check-in comme un zéro.**
- **Écrire un chiffre absent du dossier** — l'estimer, l'arrondir de mémoire, le reconstituer.
- **Mettre `transmis: true`** sans décision explicite de Xavier.
- **Faire figurer des données sur Chourouk ou les filles** au-delà de ce qui concerne directement Xavier (`SCHEMA.md` §8).
- **Écraser un brief existant** — append-only (R2).
