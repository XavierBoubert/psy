# `references/` — la littérature source, convertie

**Les documents externes que Claude Psy lit comme entrées.** Ce sont, avec les documents de [`patient/ressources/`](../../../patient/README.md), **les seules sources primaires du dispositif** — tout le reste est écrit par Claude *(c'est le risque que traite le contrôle **C1**)*.

> 🔴 **Ne jamais lire, ouvrir ou utiliser les fichiers de [`originales/`](originales/) comme entrée.** Ce sous-dossier est **l'archive de la conversion**, rien d'autre. Les documents exploitables sont les Markdown ci-dessous.

---

## Ce qu'il y a

| Fichier | Description |
|---|---|
| [`DSM-5_Manuel-diagnostique-et-statistique-des-troubles-mentaux.md`](DSM-5_Manuel-diagnostique-et-statistique-des-troubles-mentaux.md) | Version Markdown du DSM-5 (Manuel diagnostique et statistique des troubles mentaux, 5e édition), généré à partir du PDF source via le script `pdf-to-markdown` |
| [`DSM-5_TSA.md`](DSM-5_TSA.md) | Extrait du DSM-5 : **trouble du spectre de l'autisme** |
| [`DSM-5_TDAH.md`](DSM-5_TDAH.md) | Extrait du DSM-5 : **déficit de l'attention/hyperactivité** |
| [`DSM-5_Anxio-depressif.md`](DSM-5_Anxio-depressif.md) | Extrait du DSM-5 : **troubles dépressifs et troubles anxieux** (incl. trouble anxieux généralisé) |
| [`Encéphale-postprintHAL-2016-Version francaise Binge Eating Scale.md`](Encéphale-postprintHAL-2016-Version%20francaise%20Binge%20Eating%20Scale.md) | **Brunault et al. (2016), *L'Encéphale* 42(5), 426-433** — validation française de la **Binge Eating Scale** *(postprint HAL, accès ouvert)*. ⭐ **L'Annexe 1 contient les 16 items validés**, énoncés pondérés et clé de cotation. Source de [`../corpus/echelles/bes.md`](../corpus/echelles/bes.md). Porte aussi les données du seuil ≥ 18 : sensibilité 75 %, spécificité 88,4 %, **VPP 37,5 %**, **VPN 97,4 %** |
| [`HAS-2011_TSA-adulte-diagnostic_Recommandations.md`](HAS-2011_TSA-adulte-diagnostic_Recommandations.md) · [`_Argumentaire.md`](HAS-2011_TSA-adulte-diagnostic_Argumentaire.md) | **HAS, juillet 2011** — *Autisme et autres TED : diagnostic et évaluation chez l'adulte.* Le seul texte HAS sur le diagnostic du TSA à l'âge adulte |
| [`HAS-2018_TSA-adulte-interventions_Recommandations.md`](HAS-2018_TSA-adulte-interventions_Recommandations.md) · [`_Argumentaire.md`](HAS-2018_TSA-adulte-interventions_Argumentaire.md) | **HAS / Anesm, mars 2018** — *TSA : interventions et parcours de vie de l'adulte.* ⭐ Porte l'axe **accès aux soins somatiques**, adossement du chantier n° 1 |
| [`HAS-2007_ALD-23-troubles-anxieux_Guide-medecin.md`](HAS-2007_ALD-23-troubles-anxieux_Guide-medecin.md) | **HAS, juin 2007** — Guide médecin ALD n° 23, *Troubles anxieux graves.* TCC en première intention. ⚠️ **Nosographie DSM-IV / CIM-10** |
| [`HAS-2025_ALD-23-troubles-anxieux_Actes-et-prestations.md`](HAS-2025_ALD-23-troubles-anxieux_Actes-et-prestations.md) | **HAS, janvier 2025** — Liste des actes et prestations ALD n° 23, actualisée |
| [`Craske-2014_Maximizing-exposure-therapy_Inhibitory-learning.md`](Craske-2014_Maximizing-exposure-therapy_Inhibitory-learning.md) | **Craske et al. (2014), *Behaviour Research and Therapy* 58, 10-23** — 🔴 **l'apprentissage inhibiteur** : l'exposition agit par violation d'attente, pas par habituation. Source de [`../corpus/agoraphobie-exposition/`](../corpus/agoraphobie-exposition/README.md) *(postprint UCLA, eScholarship)* |
| [`Spain-Happe-2023_Improving-CBT-for-Autistic-Individuals_Delphi.md`](Spain-Happe-2023_Improving-CBT-for-Autistic-Individuals_Delphi.md) | **Spain, … Happé et al. (2023), *J. Rat-Emo Cogn-Behav Ther*** — étude Delphi sur les aménagements de la TCC pour adultes autistes : littéralité, supports écrits, séances structurées *(CC BY)* |
| [`Chernyak-2020_CPAP-Desensitization_MedEdPORTAL.md`](Chernyak-2020_CPAP-Desensitization_MedEdPORTAL.md) | **Chernyak (2020), *MedEdPORTAL*** — désensibilisation à la PPC. ⭐ **Porte le « 63%-84% »** : la claustrophobie, premier frein invoqué par les patients intolérants *(accès ouvert)* |
| [`Garfinkel-2015_Knowing-your-own-heart_Interoception.md`](Garfinkel-2015_Knowing-your-own-heart_Interoception.md) | **Garfinkel et al. (2015), *Biological Psychology* 104, 65-74** — ⭐ **le modèle à trois dimensions de l'intéroception** : précision, sensibilité, conscience. Source de [`../corpus/alimentation-interoception/`](../corpus/alimentation-interoception/README.md) |
| [`King-2024_Aphantasia-and-autism_Mental-imagery-vividness.md`](King-2024_Aphantasia-and-autism_Mental-imagery-vividness.md) | **King, Buxton & Tyndall (2024), *Consciousness and Cognition* 124, 103749** — ⭐ **20 % du groupe autiste sous le seuil d'aphantasie (VVIQ ≤ 32) contre 6,4 % des non-autistes**, 5 % au seuil strict de 16 |
| [`Fairburn-2008_Regular-eating_Table-6.1_CBT-E-handout.md`](Fairburn-2008_Regular-eating_Table-6.1_CBT-E-handout.md) | **Fairburn (2008), Table 6.1** — les règles du *regular eating* **au texte d'origine**, fiche patient officielle CBT-E, libre accès. ⭐ **Aucune règle ne s'appuie sur la faim** : six prises annoncées, jamais plus de quatre heures d'intervalle, savoir toujours quand est la suivante |

---

## Convention

**Un document source ne devient exploitable qu'une fois converti et versé ici**, PDF d'origine gardé dans `originales/`. La conversion se fait avec :

```bash
npm run psy:pdf2md -- <source.pdf> <destination.md>
npm run psy:docx2md -- <source.docx> <destination.md>
```

⚠️ **`pdf-to-markdown` ne détecte pas les cases cochées en couleur.** Un questionnaire rempli se transcrit **à la main**, par lecture visuelle des pages — c'est ce qui a été fait pour les questionnaires de `patient/ressources/`.

> ⭐ **Ce répertoire porte des référentiels, pas des documents sur Xavier.** Un courrier de praticien, un examen, un questionnaire rempli vont dans [`patient/ressources/`](../../../patient/README.md). **Un mot, une chose** — [`THESAURUS.md`](../../../THESAURUS.md).
>
> ⭐ **Et un référentiel indexé pour être appliqué n'est pas une référence brute** : il devient un **corpus**, dans [`../corpus/`](../corpus/README.md), avec sa source exacte, sa date, son statut de validation et **ce qu'il ne dit pas**.
