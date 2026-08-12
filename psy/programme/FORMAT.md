# Le programme — contrat entre Claude Psy et Kokoro

**Statut :** normatif — v1.0 (12/08/2026)

Claude Psy écrit la thérapie. Kokoro l'affiche. Ce fichier dit **le seul format que les deux comprennent**.

> **Règle unique :** Kokoro n'invente rien et ne décide rien. Il affiche ce qu'on lui donne, et renvoie ce que Xavier a fait.

---

## 1. Le circuit

```
Claude Psy ──écrit── psy/programme/programme.json
                            │
                     npm run publish
                            ▼
                     Drive/programme.json
                            │
                            ▼
                         Kokoro
                            │
                     Drive/reponses/*.json
                            │
                       npm run sync
                            ▼
              psy/dossier/  ──lit── Claude Psy · Superviseur
```

Le **dépôt reste la source de vérité**. Drive n'est qu'un tuyau.

---

## 2. Le fichier

```json
{
  "version": 3,
  "publie_le": "2026-08-12",
  "etapes": [ … ]
}
```

`version` s'incrémente à chaque publication. Kokoro compare avec la version qu'il a déjà :
s'il y a du nouveau, il affiche une ligne discrète en haut — **jamais une notification**.

---

## 3. Une étape

Champs communs, tous obligatoires sauf `duree_minutes` :

| Champ | Valeurs |
|---|---|
| `id` | identifiant stable, `kebab-case`. **Ne change jamais** — c'est lui qui relie une réponse à son étape. |
| `titre` | ce qui s'affiche dans la liste |
| `type` | `ecran` · `exercice` · `questionnaire` · `demarche` · `fiche` |
| `quand` | `aujourdhui` · `au_besoin` · `sans_date` |
| `duree_minutes` | entier, ou absent si la durée n'est pas connue d'avance |

### `ecran` — ouvre une fonction déjà construite dans Kokoro

```json
{ "id": "check-in", "titre": "Check-in du jour", "type": "ecran",
  "quand": "aujourdhui", "duree_minutes": 2, "ecran": "check-in" }
```

Valeurs de `ecran` : `check-in` · `mot-code` · `tension-appliquee` · `phrase-soignant`.
**Kokoro refuse un nom d'écran qu'il ne connaît pas** plutôt que d'afficher une ligne morte.

### `exercice` — un déroulé guidé au minuteur

```json
{ "id": "ppc-p1", "titre": "Masque tenu à la main", "type": "exercice",
  "quand": "aujourdhui", "duree_minutes": 5,
  "consigne": "Masque contre le visage, sans sangles, machine éteinte, pendant une activité neutre.",
  "minuteur_secondes": 300,
  "sortie_libre": true }
```

`sortie_libre: true` affiche « je peux arrêter avant la fin, sans avoir à le justifier ».
**C'est toujours `true`.** Le champ existe pour que ce soit écrit, pas pour être mis à `false`.

### `questionnaire` — des questions fermées, une par écran

```json
{ "id": "gad7", "titre": "Questionnaire GAD-7", "type": "questionnaire",
  "quand": "sans_date", "duree_minutes": 5,
  "questions": [
    { "id": "q1", "enonce": "…", "choix": [
        { "valeur": 0, "libelle": "Jamais" },
        { "valeur": 3, "libelle": "Presque tous les jours" } ] }
  ] }
```

Toute question est un **choix fermé** ou un **compteur**. Aucune saisie de texte obligatoire, jamais.
« Passer » écrit `null` — qui n'est pas `0`.

### `demarche` — une chose à faire dans le monde réel

```json
{ "id": "ppc-releve", "titre": "Demander le relevé de télésuivi", "type": "demarche",
  "quand": "sans_date",
  "detail": "Link Sommeil — heures par nuit, nombre de nuits, fuites, IAH résiduel." }
```

Renvoie `fait` ou rien. **Pas encore fait n'est pas une donnée** : rien ne s'affiche, rien ne se compte.

### `fiche` — un texte à lire ou à montrer

```json
{ "id": "fiche-x", "titre": "…", "type": "fiche", "quand": "au_besoin",
  "texte": "…", "montrable": true }
```

`montrable: true` affiche le texte en plein écran, lisible par quelqu'un d'autre.

---

## 4. Ce que Kokoro renvoie

Un fichier par étape faite, dans `reponses/` sur Drive :
`AAAA-MM-JJ-HHMM-<id>.json`

```json
{ "etape": "ppc-p1", "horodatage": "2026-08-12T18:04:00+02:00",
  "issue": "termine", "reponses": null, "source": "android" }
```

`issue` : `termine` · `arrete_avant_la_fin` · `fait`.
`arrete_avant_la_fin` **n'est pas un échec** et ne se commente nulle part.

---

## 5. 🔴 Les interdits — vérifiés à la publication ET à la lecture

Les tests de Kokoro lisent aujourd'hui les textes de l'app. **Ces textes-ci ne sont plus dans l'app.**
Sans double vérification, tous les garde-fous du dispositif deviennent contournables par du contenu, **en silence**.

La vérification a lieu **deux fois, et les deux réactions diffèrent volontairement** :

- **`npm run publish` refuse la publication entière.** Sur le PC, on peut corriger — donc on corrige, on ne publie pas à moitié.
- **Kokoro écarte la seule étape fautive** et affiche le reste. Sur le téléphone, on ne peut pas corriger — et perdre tout le programme à cause d'une ligne serait pire.

Est refusée toute étape qui contient :

| # | Interdit | Pourquoi |
|---|---|---|
| 1 | « imagine », « visualise », « représente-toi », « lieu sûr » | Aphantasie mesurée — 18/80 |
| 2 | « note … sur 10 », « ton niveau de », « à combien tu te sens » | R6 — on cote des comportements, pas des ressentis |
| 3 | « jour 3 sur », « d'affilée », « série », « régularité », « % de l'objectif » | Zéro streak |
| 4 | Tout numéro d'appel d'urgence, **3114 compris** | Retrait du 10/08 — un écran n'est pas un déclencheur d'escalade |
| 5 | « as-tu besoin », « quand tu sens », « aux premiers signes » | Déclenchement sur repère externe, jamais sur un prodrome |
| 6 | Tout ce qui touche à une dose, une molécule, un traitement | Non-substitution — ça part au brief Dr Isorni |
| 7 | « détends-toi », « respire lentement » sur une étape vasovagale | Délétère sur un vasovagal |

**Une étape refusée ne casse pas Kokoro** : elle n'apparaît pas, le reste s'affiche.

---

## 6. Ce que le programme ne fait jamais

1. **Notifier.** Kokoro ne vient jamais vers Xavier. Xavier vient vers Kokoro.
   *(Seule exception, inchangée : l'accès crise sur l'écran verrouillé — c'est une porte, pas un rappel.)*
2. **Compter d'un jour à l'autre.** Aucun palier atteint, aucun historique, aucune progression à l'écran.
   Les paliers se cotent **en séance**, à partir du journal.
3. **Reprocher.** Une étape non faite disparaît de l'écran le lendemain sans laisser de trace.

---

| Version | Date | Modification |
|---|---|---|
| 1.0 | 12/08/2026 | Création. Kokoro cesse d'être une app à fonctions et devient le porteur de la thérapie écrite par Claude Psy. |
