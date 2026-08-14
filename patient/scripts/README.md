# `patient/scripts/` — vide aujourd'hui

**Xavier n'exécute rien depuis ce répertoire.** Les deux commandes qui le concernent vivent ailleurs, et c'est volontaire :

| Ce qu'il fait | Commande | Où elle vit |
|---|---|---|
| **Déployer Kokoro sur son téléphone** | `npm run companion:kokoro` *(ou `./kokoro`)* | [`companion/android/kokoro`](../../companion/android/kokoro) — ⭐ **elle existe pour qu'il déploie sans passer par Claude** |
| Convertir un document reçu d'un praticien | `npm run psy:pdf2md` | [`psy/scripts/`](../../psy/scripts/) — c'est le psy qui verse au dossier |

---

## Ce qui aurait vocation à venir ici

Un outil **que Xavier lance pour lui-même**, sans Claude et sans séance. Rien de tel n'existe à ce jour.

> 🔴 **Un script ne remplacera jamais Kokoro comme surface.** Il n'y a **qu'une** surface tournée vers Xavier *([`../../companion/README.md`](../../companion/README.md))* ; ne jamais proposer d'en construire une deuxième, ligne de commande comprise.
>
> ⭐ **Et rien ici ne viendra vers lui.** Aucune notification, aucune relance, aucun rappel programmé.
