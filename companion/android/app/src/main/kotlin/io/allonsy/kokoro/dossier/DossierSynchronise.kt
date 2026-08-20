package io.allonsy.kokoro.dossier

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import io.allonsy.kokoro.programme.Reponse
import io.allonsy.kokoro.programme.nomDeCarte
import io.allonsy.kokoro.programme.nomDeLaReponse
import io.allonsy.kokoro.programme.serialiserReponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val FICHIER = "kokoro_dossier"
private const val CLE_ARBRE = "arbre_dossier"
private const val CLE_SOUS_DOSSIER = "sous_dossier_"
private const val CLE_REPONSES_ECRITES = "reponses_ecrites"
private const val CLE_ENTRAINEMENTS = "entrainements_menes"
private const val SOUS_DOSSIER_REPONSES = "reponses"
private const val SOUS_DOSSIER_BIBLIOTHEQUE = "bibliotheque"
private const val SOUS_DOSSIER_BILANS = "bilans"
private const val FICHIER_PROGRAMME = "programme.json"
private const val MIME_JSON = "application/json"

private val FORMAT_JOUR: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun jourCourant(): String = LocalDate.now().format(FORMAT_JOUR)

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
    val prefs = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
    val edition = prefs.edit().putString(CLE_ARBRE, arbre.toString())
    prefs.all.keys.filter { it.startsWith(CLE_SOUS_DOSSIER) }.forEach(edition::remove)
    edition.remove(CLE_REPONSES_ECRITES)
    edition.apply()
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

// Le dernier segment de l'URI est un identifiant opaque sur Google Drive : on demande le nom au fournisseur.
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

fun texteDuProgramme(context: Context): String? {
    val racine = racineDuDossier(context) ?: return null
    rafraichir(context, racine)
    val fichier = enfant(context, racine, FICHIER_PROGRAMME) ?: return null
    return runCatching {
        context.contentResolver.openInputStream(fichier)?.use { it.readBytes().toString(Charsets.UTF_8) }
    }.getOrNull()
}

fun ecrireReponse(context: Context, reponse: Reponse): ResultatEcriture {
    val dossier = sousDossier(context, SOUS_DOSSIER_REPONSES) ?: return ResultatEcriture.DossierAbsent
    val nom = nomDeLaReponse(reponse)

    return runCatching {
        val cree = DocumentsContract.createDocument(context.contentResolver, dossier, MIME_JSON, nom)
            ?: return ResultatEcriture.Echec("création refusée")
        context.contentResolver.openOutputStream(cree, "wt")?.use { flux ->
            flux.write(serialiserReponse(reponse).toByteArray(Charsets.UTF_8))
        } ?: return ResultatEcriture.Echec("écriture refusée")
        renommerSiBesoin(context, cree, nom)
        retenirLaReponse(context, nom)
        ResultatEcriture.Ecrit(nom)
    }.getOrElse { ResultatEcriture.Echec(it.message ?: it::class.java.simpleName) }
}

// 🔴 Ce que Kokoro a écrit, il s'en souvient lui-même : le fichier met le temps qu'il veut à faire l'aller-retour
// par Drive, et une étape faite ne peut pas réapparaître à faire entre-temps.
fun listerReponses(context: Context): List<String> {
    val dossier = sousDossierExistant(context, SOUS_DOSSIER_REPONSES)
    val surDrive = if (dossier == null) {
        emptyList()
    } else {
        rafraichir(context, dossier)
        parcourir(context, dossier) { _, affiche -> affiche }
    }
    return (surDrive + reponsesRetenues(context)).distinct()
}

private fun retenirLaReponse(context: Context, nom: String) {
    val prefs = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
    prefs.edit().putStringSet(CLE_REPONSES_ECRITES, reponsesRetenues(context) + nom).apply()
}

// La dernière réponse rendue par une carte — c'est d'elle que repart une question qui se reprend.
fun derniereReponse(context: Context, carte: String): String? {
    val dossier = sousDossierExistant(context, SOUS_DOSSIER_REPONSES) ?: return null
    val dernier = parcourir(context, dossier) { identifiant, affiche ->
        if (nomDeCarte(affiche) == carte) affiche to documentDansArbre(dossier, identifiant) else null
    }.maxByOrNull { it.first }?.second ?: return null

    return runCatching {
        context.contentResolver.openInputStream(dernier)?.use { it.readBytes().toString(Charsets.UTF_8) }
    }.getOrNull()
}

// L'issue n'est pas dans le nom du fichier : l'entraînement mené se retient ici, il ne se relit pas de Drive.
fun marquerEntrainement(context: Context, carte: String) {
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .edit()
        .putStringSet(CLE_ENTRAINEMENTS, entrainementsMenes(context) + carte)
        .apply()
}

fun entrainementsMenes(context: Context): Set<String> =
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .getStringSet(CLE_ENTRAINEMENTS, emptySet())
        .orEmpty()

private fun reponsesRetenues(context: Context): Set<String> =
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .getStringSet(CLE_REPONSES_ECRITES, emptySet())
        .orEmpty()

fun pdfDeLaBibliotheque(context: Context, document: String): Uri? =
    pdfDuDossier(context, SOUS_DOSSIER_BIBLIOTHEQUE, document)

fun pdfDuBilan(context: Context, document: String): Uri? =
    pdfDuDossier(context, SOUS_DOSSIER_BILANS, document)

private fun pdfDuDossier(context: Context, nom: String, document: String): Uri? {
    val racine = racineDuDossier(context) ?: return null
    rafraichir(context, racine)
    val dossier = enfant(context, racine, nom) ?: return null
    rafraichir(context, dossier)
    return enfant(context, dossier, "$document.pdf")
}

// Drive sert sa liste en cache : sans ça, une fiche publiée reste invisible tant que l'app Drive n'a pas resynchronisé.
private fun rafraichir(context: Context, dossier: Uri) {
    runCatching { context.contentResolver.refresh(dossier, null, null) }
}

private fun racineDuDossier(context: Context): Uri? {
    val arbre = lireDossier(context) ?: return null
    return DocumentsContract.buildDocumentUriUsingTree(
        arbre,
        DocumentsContract.getTreeDocumentId(arbre),
    )
}

// Sans rafraichir, la liste d'enfants servie en cache ne montre pas un dossier tout juste créé.
private fun sousDossierExistant(context: Context, nom: String): Uri? {
    val racine = racineDuDossier(context) ?: return null
    rafraichir(context, racine)
    return enfant(context, racine, nom)
}

// 🔴 Drive accepte deux dossiers du même nom, et les fichiers écrits dans le second n'arrivent jamais
// au dépôt. Deux gardes : la liste rafraîchie, puis le jeton du dossier créé.
private fun sousDossier(context: Context, nom: String): Uri? {
    val existant = sousDossierExistant(context, nom)
    if (existant != null) return existant

    val prefs = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
    val retenu = prefs.getString(CLE_SOUS_DOSSIER + nom, null)
    if (retenu != null) return Uri.parse(retenu)

    val racine = racineDuDossier(context) ?: return null
    val cree = runCatching {
        DocumentsContract.createDocument(
            context.contentResolver,
            racine,
            DocumentsContract.Document.MIME_TYPE_DIR,
            nom,
        )
    }.getOrNull() ?: return null

    prefs.edit().putString(CLE_SOUS_DOSSIER + nom, cree.toString()).apply()
    return cree
}

private fun enfant(context: Context, parent: Uri, nom: String): Uri? =
    parcourir(context, parent) { identifiant, affiche ->
        if (affiche == nom) documentDansArbre(parent, identifiant) else null
    }.firstOrNull()

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
