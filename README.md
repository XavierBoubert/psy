# Psy — un répertoire par rôle

**Un psychologue/psychiatre virtuel basé sur Claude, conçu pour Xavier.** Un dispositif, pas un chatbot.

> 📐 **Toute la doctrine tient dans [`PLAN.md`](PLAN.md)** — document unique, seul endroit où lire ce que le dispositif est, et seul endroit où le modifier. Les §4, §6, §7 et §8 y sont **normatifs**.
> 📖 **Le vocabulaire fait foi dans [`THESAURUS.md`](THESAURUS.md)** — *un mot, une chose*. `protocole` ≠ `fiche de bibliothèque` ; `chantier` ≠ `cible` ≠ `palier`.

---

## Les cinq rôles, les cinq répertoires

⭐ **Depuis le 14/08/2026, le dépôt s'organise par rôle et non plus par nature de fichier.** Chaque persona a **un** répertoire, et ce qu'il produit, reçoit et documente y tient. Le but est qu'un rôle soit lisible — et un jour extractible — sans démêler les autres.

| Rôle | Répertoire | Ce qu'il est |
|---|---|---|
| **Claude Psy** | [`psy/`](psy/README.md) | **Le psychiatre et le psychologue.** Une séance de fond par semaine. **Il construit tout le contenu** et le donne à Kokoro |
| **Claude Superviseur** | [`superviseur/`](superviseur/README.md) | **Le superviseur du psy.** Il supervise **Claude, jamais Xavier**. 🔴 **Sa passe est bloquante avant toute publication** |
| **Kokoro (心)** | [`companion/`](companion/README.md) | **Le compagnon du patient**, sur son téléphone. La **seule** surface tournée vers Xavier |
| **L'aide-au-patient** | [`aidant/`](aidant/README.md) | **La personne qui tient le téléphone** pendant une séance à deux. Aujourd'hui Chourouk. 🔴 **Elle n'est pas thérapeute** |
| **Xavier** | [`patient/`](patient/README.md) | **Le patient.** Ses documents source — pas un utilisateur à engager |

> ⭐ **Kokoro ne vient jamais vers Xavier.** Aucune notification, aucune relance, aucun reproche. **Xavier vient à lui, et y trouve tout.** *(Seule exception : l'accès crise sur l'écran verrouillé — une porte, pas un rappel.)*

---

## Le circuit du contenu

```
Claude Psy ──── programme + bibliothèque ────► Kokoro ──── journal + réponses ────► dossier
     ▲            (companion/inputs/)                      (companion/outputs/)        │
     │              après supervision                                                  │
     └───────────────────── Superviseur ◄──────────────────────────────────────────────┘
                          (superviseur/outputs/)
```

**Les deux mouvements sont scriptés, jamais faits à la main :**

| Sens | Commande | Ce qui passe |
|---|---|---|
| PC → Kokoro | **`npm run publish`** | `companion/inputs/programme.json` + `companion/inputs/bibliotheque/` |
| Kokoro → PC | **`npm run sync`** | `companion/outputs/journal/` + `companion/outputs/reponses/` |

🔴 **`publish` refuse la publication entière** si un invariant est enfreint ou si la supervision de la version qui sort manque *(`PLAN.md` [§4.3](PLAN.md#43--la-supervision-est-bloquante-avant-publication))*. **Un refus se corrige, il ne se contourne pas** — aucune option de forçage n'existe, et il ne doit jamais en exister une.

---

## La convention de nommage des répertoires

| Nom | Ce qu'il contient | Qui y écrit |
|---|---|---|
| `<role>/` *(racine)* | **Sa documentation** — les markdowns qui disent ce que le rôle est et comment il travaille | Claude, hors séance |
| `<role>/ressources/` | **Ce dont le rôle a besoin pour travailler** — sources, référentiels, planches | Claude, hors séance |
| `<role>/outputs/` | **Ce que le rôle produit** | Le rôle, et lui seul |
| `<role>/inputs/` | **Ce qu'un autre rôle lui donne** | L'autre rôle |
| `<role>/scripts/` | **Les scripts qui servent ce rôle** | — |

> ✅ **Aucun fichier n'a deux auteurs.** C'est une condition de l'arbitrage Drive, pas une observation *(`PLAN.md` [§6.2](PLAN.md#62-le-périmètre))*. La ligne de partage du dossier clinique entre `psy/outputs/` et `companion/outputs/` **est** cette règle : ce que Claude Psy écrit vit chez le psy, ce que Kokoro écrit vit chez le compagnon.
>
> ⚠️ **`companion/outputs/` reste du dossier clinique** malgré son emplacement : les six règles du `PLAN.md` §7.1 s'y appliquent entières, R2 (append-only) comprise.

---

## Les scripts

Tous s'exécutent depuis la racine. Les arguments de chemin sont résolus **par rapport à la racine du projet**, jamais au répertoire courant.

| Commande | Rôle servi | Objet |
|---|---|---|
| `npm run publish` · `npm run programme-publish` | psy → companion | Publie la thérapie et la bibliothèque vers Kokoro. **Validation + supervision bloquantes** |
| `npm run sync` · `npm run contenu-sync` | companion → psy | Verse au dépôt ce que Kokoro a écrit. **N'écrase jamais un fichier existant** |
| `npm run kokoro` | companion | Compile, teste, installe et ouvre Kokoro sur le téléphone. ⭐ **Existe pour que Xavier déploie sans passer par Claude** |
| `npm run image` · `npm run decoupe` | companion | Planches de recherche graphique, et détourage du fond magenta |
| `npm run pdf-to-markdown` · `docx-to-markdown` · `markdown-to-pdf` | psy | Conversion de documents |
| `npm run typecheck` | — | `tsc --noEmit` sur `psy/scripts/` et `companion/scripts/` |

---

## Ce qui ne bouge pas avec la réorganisation

- **Les skills vivent dans `.claude/skills/psy-*`** — Claude Code ne les découvre que là. Ce n'est pas un choix d'organisation, c'est une contrainte de l'outil.
- **`PLAN.md` et `THESAURUS.md` restent à la racine** : ils ne sont à personne, ils sont au dispositif.
- **Les formats normatifs sont inchangés** — §7 et §8 décrivent les mêmes fichiers, à un autre emplacement.

---

## 🔴 Avant toute intervention clinique

Charger **[`psy/outputs/dossier/profil.md`](psy/outputs/dossier/profil.md)** *(contexte permanent)* **et [`psy/outputs/dossier/etat.md`](psy/outputs/dossier/etat.md)** *(état courant)*, **ensemble et jamais l'un sans l'autre**. En cas de doute clinique, la source qui fait foi est le rapport v2.4, pas la fiche.

**Face à une crise, maintenant :** [`psy/docs/protocoles/crise-escalade.md`](psy/docs/protocoles/crise-escalade.md) — prime sur tout le reste.
