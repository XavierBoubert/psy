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
