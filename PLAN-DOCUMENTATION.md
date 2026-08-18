# PLAN-DOCUMENTATION — les fiches à écrire pour Kokoro

## 1. Les documentations à écrire

| `document` | Rubrique | Source | Vigilance |
|---|---|---|---|
| `panique-13-symptomes` | `crise` | [panique-13-symptomes.md](psy/docs/protocoles/panique-13-symptomes.md) | Interdit n° 5 — pas de déclenchement sur prodrome |
| `vasovagal-ce-qui-se-passe` | `crise` | [tension-appliquee.md](psy/docs/protocoles/tension-appliquee.md) §0, §2 | Interdit n° 7 — jamais « détends-toi », « respire lentement » |
| `tension-appliquee-le-geste` | `crise` | [tension-appliquee.md](psy/docs/protocoles/tension-appliquee.md) §1, §3 | L'écran existe ; la fiche porte le pourquoi, lisible à froid |
| `shutdown-ce-qui-reste-ouvert` | `crise` | [crise-escalade.md](psy/docs/protocoles/crise-escalade.md) §4 | Aucun numéro d'appel, 3114 compris |
| ✅ `fiche-chourouk` *(`montrable: true`)* — **publiée le 18/08/2026** | `crise` | [fiche-chourouk.md](aidant/ressources/fiche-chourouk.md) | 🔴 C10 — des consignes, pas un dossier ; aucune demande de jugement |
| `ppc-pourquoi-maintenant` | `therapie` | [ppc-desensibilisation.md](psy/docs/protocoles/ppc-desensibilisation.md) §0 | Aucun pronostic ; vérifier en séance ce qui a été dit à Xavier |
| `ppc-les-paliers` | `therapie` | [ppc-desensibilisation.md](psy/docs/protocoles/ppc-desensibilisation.md) §4, §5 | Zéro compteur ; sortie libre annoncée |
| `ppc-palier-0` | `therapie` | [ppc-desensibilisation.md](psy/docs/protocoles/ppc-desensibilisation.md) §3 | Accompagne les sept démarches déjà publiées |
| `alimentation-les-quatre-regles` | `therapie` | [alimentation-structure-externe.md](psy/docs/protocoles/alimentation-structure-externe.md) §3 | Interdit n° 2 — aucune cotation de faim ni de satiété |
| `activite-sans-impact` | `therapie` | [activite-physique-sans-impact.md](psy/docs/protocoles/activite-physique-sans-impact.md) §3-§5 | Feu vert médical préalable non obtenu |
| `jour-de-vol` | `therapie` | [jour-de-vol.md](psy/docs/protocoles/jour-de-vol.md) | Ce n'est pas un programme d'exposition — le dire dans la fiche |
| `sejour-tunisie` | `therapie` | [PLAN.md](PLAN.md) §4 | Aucune progression de palier sur place, redescente d'un palier au retour |
| `pourquoi-pas-de-score` | `bilan` | [companion/PROGRAMME.md](companion/PROGRAMME.md) §3 | La cotation est en séance ; Kokoro n'affiche jamais un score |
| `comment-marche-kokoro` | `documentation` | [companion/PROGRAMME.md](companion/PROGRAMME.md) §8 | Ne notifie pas, ne compte pas, ne reproche pas |
| `ou-vont-mes-reponses` | `documentation` | [companion/PROGRAMME.md](companion/PROGRAMME.md) §1 | Le circuit, et qui lit quoi |

## 2. Markdown à la source, PDF à la publication

- La fiche s'écrit et se relit en **Markdown** dans `companion/inputs/bibliotheque/<id>.md` — c'est la version qui passe la supervision et les sept familles d'interdits.
- `npm run psy:publish` la convertit en **`<id>.pdf`** *(via `psy:md2pdf`)* et publie vers Drive **le PDF et `programme.json`**. Le Markdown ne part pas.
- Le champ `document` de l'étape `fiche` reste l'identifiant nu : Kokoro résout `bibliotheque/<document>.pdf`.

## 3. Quand une fiche se publie

- **À tout moment**, séance ou non — une fiche est à portée dès qu'elle est écrite et supervisée. **Xavier n'attend pas la séance suivante pour comprendre ce qui lui arrive.**
- **La supervision reste bloquante**, et sa `version` doit correspondre à celle du programme au moment du `publish`.
- **Chaque publication s'annonce à Xavier** dans la conversation en cours.
- **Ne partent pas hors séance** : les étapes qui font agir — `ecran`, `exercice`, `questionnaire`, `demarche`, `seance-duo`.
