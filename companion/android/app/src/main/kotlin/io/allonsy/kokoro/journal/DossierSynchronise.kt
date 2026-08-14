package io.allonsy.kokoro.journal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract

private const val FICHIER = "kokoro_dossier"
private const val CLE_ARBRE = "arbre_dossier"
private const val CLE_DERNIER_JOUR = "dernier_jour_ecrit"
private const val SOUS_DOSSIER = "journal"
private const val MIME_JSON = "application/json"

/**
 * Accès au dossier synchronisé — arbitrage tranché le 11/08/2026 (`companion/README.md` §7), retenu ici en
 * faveur du **Storage Access Framework** plutôt que de `MANAGE_EXTERNAL_STORAGE` :
 * Xavier désigne une fois le dossier `companion/outputs` partagé par Syncthing, et Kokoro
 * n'obtient de droit que sur celui-là. Aucune permission n'entre au manifeste, et
 * l'app ne peut pas lire le reste du téléphone.
 *
 * Kokoro écrit des fichiers, pas une base : la source de vérité reste le dossier
 * (`companion/README.md` §3 — aucune base de données). Rien n'est mis en cache ici.
 */
fun intentChoisirDossier(): Intent =
    Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        .addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION,
        )

fun enregistrerDossier(context: Context, arbre: Uri) {
    context.contentResolver.takePersistableUriPermission(
        arbre,
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
    )
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .edit()
        .putString(CLE_ARBRE, arbre.toString())
        .apply()
}

fun lireDossier(context: Context): Uri? {
    val enregistre = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .getString(CLE_ARBRE, null) ?: return null
    val arbre = Uri.parse(enregistre)
    val accorde = context.contentResolver.persistedUriPermissions.any {
        it.uri == arbre && it.isWritePermission
    }
    return if (accorde) arbre else null
}

/**
 * Le nom lisible du dossier désigné. ⚠️ Le dernier segment de l'URI ne convient pas :
 * sur Google Drive c'est un identifiant opaque (`acc=2;doc=encoded=L9caT1…`), illisible
 * à l'écran. On demande donc son nom au fournisseur.
 */
fun cheminAffichable(context: Context, arbre: Uri?): String? {
    if (arbre == null) return null
    val racine = DocumentsContract.buildDocumentUriUsingTree(
        arbre,
        DocumentsContract.getTreeDocumentId(arbre),
    )
    val projection = arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
    val nom = runCatching {
        context.contentResolver.query(racine, projection, null, null, null)?.use { curseur ->
            if (curseur.moveToFirst()) curseur.getString(0) else null
        }
    }.getOrNull()
    return nom ?: Uri.decode(arbre.lastPathSegment ?: arbre.toString())
}

sealed interface ResultatEcriture {
    data class Ecrit(val nom: String) : ResultatEcriture
    data object DossierAbsent : ResultatEcriture
    data object DejaEcritAujourdhui : ResultatEcriture
    data class Echec(val cause: String) : ResultatEcriture
}

fun ecrireCheckin(context: Context, checkin: Checkin): ResultatEcriture {
    val journal = dossierJournal(context) ?: return ResultatEcriture.DossierAbsent
    val nom = "${checkin.date}.json"
    if (checkinDuJourExiste(context, checkin.date)) return ResultatEcriture.DejaEcritAujourdhui

    return runCatching {
        val cree = DocumentsContract.createDocument(context.contentResolver, journal, MIME_JSON, nom)
            ?: return ResultatEcriture.Echec("création refusée")
        context.contentResolver.openOutputStream(cree, "wt")?.use { flux ->
            flux.write(serialiser(checkin).toByteArray(Charsets.UTF_8))
        } ?: return ResultatEcriture.Echec("écriture refusée")
        renommerSiBesoin(context, cree, nom)
        marquerJourEcrit(context, checkin.date)
        ResultatEcriture.Ecrit(nom)
    }.getOrElse { ResultatEcriture.Echec(it.message ?: it::class.java.simpleName) }
}

/**
 * ⚠️ **Deux gardes, et il en faut deux.** Le fournisseur Google Drive **accepte deux
 * fichiers du même nom** dans un même dossier, et sa liste d'enfants n'est pas immédiate :
 * constaté le 11/08/2026, deux écritures à 25 s d'intervalle ont produit deux
 * `2000-01-01.json`. Interroger le dossier ne suffit donc pas.
 *
 * Le jeton local tient le verrou d'écriture — ce n'est pas un cache de données cliniques :
 * il ne contient qu'une date, aucune réponse, et sa perte ne perd rien.
 */
fun checkinDuJourExiste(context: Context, date: String): Boolean {
    val jeton = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .getString(CLE_DERNIER_JOUR, null)
    if (jeton == date) return true

    val journal = dossierJournal(context) ?: return false
    return enfant(context, journal, "$date.json") != null
}

private fun marquerJourEcrit(context: Context, date: String) {
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .edit()
        .putString(CLE_DERNIER_JOUR, date)
        .apply()
}

/** Suppression d'un fichier du dossier — réservée au nettoyage des témoins de vérification. */
fun supprimerDuJournal(context: Context, nom: String): Int {
    val journal = dossierJournal(context) ?: return 0
    return generateSequence { enfant(context, journal, nom) }
        .takeWhile { runCatching { DocumentsContract.deleteDocument(context.contentResolver, it) }.getOrDefault(false) }
        .count()
}

/**
 * Reprend, dans le dernier check-in écrit, les seules valeurs qui servent de point de
 * départ aux compteurs (`CHAMPS_REPRIS`). Aucun historique n'est constitué, aucune
 * évolution n'est calculée, rien n'est affiché à Xavier.
 */
fun valeursReprises(context: Context, avant: String): Map<Champ, Double> {
    val journal = dossierJournal(context) ?: return emptyMap()
    val dernier = dernierFichierAvant(context, journal, avant) ?: return emptyMap()
    val contenu = runCatching {
        context.contentResolver.openInputStream(dernier)?.use { it.readBytes().toString(Charsets.UTF_8) }
    }.getOrNull() ?: return emptyMap()
    return CHAMPS_REPRIS.mapNotNull { champ ->
        relireValeur(contenu, champ)?.let { champ to it }
    }.toMap()
}

/** Diagnostic : le contenu d'un fichier du journal, tel que le fournisseur le rend. */
fun lireDuJournal(context: Context, nom: String): String? {
    val journal = dossierJournal(context) ?: return null
    val fichier = enfant(context, journal, nom) ?: return null
    return runCatching {
        context.contentResolver.openInputStream(fichier)?.use { it.readBytes().toString(Charsets.UTF_8) }
    }.getOrNull()
}

/** Diagnostic : ce que le fournisseur de documents dit contenir, à l'instant t. */
fun listerJournal(context: Context): List<String> {
    val journal = dossierJournal(context) ?: return emptyList()
    return parcourir(context, journal) { _, affiche -> affiche }
}

private fun dossierJournal(context: Context): Uri? {
    val arbre = lireDossier(context) ?: return null
    val racine = DocumentsContract.buildDocumentUriUsingTree(
        arbre,
        DocumentsContract.getTreeDocumentId(arbre),
    )
    val existant = enfant(context, racine, SOUS_DOSSIER)
    if (existant != null) return existant
    return runCatching {
        DocumentsContract.createDocument(
            context.contentResolver,
            racine,
            DocumentsContract.Document.MIME_TYPE_DIR,
            SOUS_DOSSIER,
        )
    }.getOrNull()
}

private fun enfant(context: Context, parent: Uri, nom: String): Uri? =
    parcourir(context, parent) { identifiant, affiche ->
        if (affiche == nom) documentDansArbre(parent, identifiant) else null
    }.firstOrNull()

private fun dernierFichierAvant(context: Context, journal: Uri, date: String): Uri? =
    parcourir(context, journal) { identifiant, affiche ->
        val jour = affiche.removeSuffix(".json")
        val estJournal = affiche.endsWith(".json") && jour < date
        if (estJournal) jour to documentDansArbre(journal, identifiant) else null
    }.maxByOrNull { it.first }?.second

private fun <T> parcourir(context: Context, parent: Uri, prendre: (String, String) -> T?): List<T> {
    val enfants = DocumentsContract.buildChildDocumentsUriUsingTree(
        parent,
        DocumentsContract.getDocumentId(parent),
    )
    val projection = arrayOf(
        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
    )
    return runCatching {
        context.contentResolver.query(enfants, projection, null, null, null)?.use { curseur ->
            buildList {
                while (curseur.moveToNext()) {
                    prendre(curseur.getString(0), curseur.getString(1))?.let { add(it) }
                }
            }
        }.orEmpty()
    }.getOrDefault(emptyList())
}

private fun documentDansArbre(parent: Uri, identifiant: String): Uri =
    DocumentsContract.buildDocumentUriUsingTree(parent, identifiant)

private fun renommerSiBesoin(context: Context, document: Uri, attendu: String) {
    val projection = arrayOf(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
    val nomReel = runCatching {
        context.contentResolver.query(document, projection, null, null, null)?.use { curseur ->
            if (curseur.moveToFirst()) curseur.getString(0) else null
        }
    }.getOrNull()
    if (nomReel != null && nomReel != attendu) {
        runCatching { DocumentsContract.renameDocument(context.contentResolver, document, attendu) }
    }
}
