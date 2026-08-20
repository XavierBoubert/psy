package io.allonsy.kokoro.programme

enum class Quand(val cle: String) {
    AUJOURDHUI("aujourdhui"),
    AU_BESOIN("au_besoin"),
    SANS_DATE("sans_date"),
}

enum class Rubrique(val cle: String) {
    CRISE("crise"),
    THERAPIE("therapie"),
    BILAN("bilan"),
    DOCUMENTATION("documentation"),
}

// Qui tient le téléphone. Sur une carte portée par l'aidant, Kokoro tient la cadence et ne lui demande jamais de juger.
enum class Porteur(val cle: String) {
    PATIENT("patient"),
    AIDANT("aidant"),
}

// Guidée : une étape à la fois, dans l'ordre, jusqu'à l'issue. Libre : un sommaire, chaque étape se ferme et y revient.
enum class Allure { GUIDEE, LIBRE }

enum class Unite(val cle: String) {
    BRUTE("brute"),
    MINUTES("minutes"),
    HEURES("heures"),
    KILOS("kilos"),
}

data class Choix(val valeur: Double, val libelle: String)

data class Compteur(
    val depart: Double,
    val pas: Double,
    val grandPas: Double,
    val minimum: Double,
    val unite: Unite,
)

sealed interface Saisie {
    data class Fermee(val choix: List<Choix>) : Saisie

    data class Reglee(val compteur: Compteur) : Saisie
}

sealed interface Etape {
    data class Info(val texte: String, val montrable: Boolean = false) : Etape

    data class Question(
        val id: String,
        val enonce: String,
        val precision: String?,
        val saisie: Saisie,
        // Repart de la dernière valeur donnée à cette question, jamais d'un ressenti à retrouver.
        val reprise: Boolean = false,
    ) : Etape

    data class Note(val id: String, val enonce: String, val precision: String?) : Etape

    data class Minuteur(val secondes: Int, val consigne: String?, val pour: Porteur?) : Etape

    data class Checklist(val enonce: String, val lignes: List<String>) : Etape

    data class Confirmation(val libelle: String) : Etape
}

// Une étape qui n'écrit rien ne fait pas rendre la carte : une fiche lue ne renvoie pas plus qu'avant.
val Etape.rend: Boolean
    get() = this is Etape.Question || this is Etape.Note || this is Etape.Minuteur || this is Etape.Confirmation

data class Reperes(
    val id: String,
    val titre: String,
    val rubrique: Rubrique,
    // Nul sur une carte du dossier Bilan, et sur elle seule : sa date appartient au document, pas à l'assiduité de Xavier.
    val quand: Quand?,
    val dureeMinutes: Int?,
)

sealed interface Carte {
    val reperes: Reperes

    data class Panneau(
        override val reperes: Reperes,
        val etapes: List<Etape>,
        val porteur: Porteur = Porteur.PATIENT,
        val allure: Allure = Allure.GUIDEE,
        val signalArret: String? = null,
        val arret: List<String> = emptyList(),
    ) : Carte

    // date non nulle : le document vit dans bilans/, pas dans la bibliothèque — canal distinct, contrôles distincts.
    data class Pdf(override val reperes: Reperes, val document: String, val date: String? = null) : Carte
}

val Carte.id: String get() = reperes.id

val Carte.titre: String get() = reperes.titre

val Carte.rubrique: Rubrique get() = reperes.rubrique

val Carte.quand: Quand? get() = reperes.quand

val Carte.dureeMinutes: Int? get() = reperes.dureeMinutes

data class Programme(val version: Int, val cartes: List<Carte>)

val PROGRAMME_ABSENT = Programme(version = 0, cartes = emptyList())

private val KEBAB = Regex("""^[a-z0-9-]+$""")

private val DATE = Regex("""^\d{4}-\d{2}-\d{2}$""")

private val DERNIER_CRITERE = Regex("""ne sais pas quoi faire""", RegexOption.IGNORE_CASE)

fun lireProgramme(json: String): Programme {
    val racine = lireJson(json) ?: return PROGRAMME_ABSENT

    return Programme(
        version = racine.entier("version") ?: 0,
        cartes = racine.elements("cartes").mapNotNull(::carte),
    )
}

fun Programme.cartesDe(rubrique: Rubrique): List<Carte> =
    cartes.filter { it.rubrique == rubrique && it !is Carte.Pdf }

// Un PDF vit sur Documentation quelle que soit sa rubrique ; un bilan a son propre écran.
fun Programme.documents(): List<Carte.Pdf> =
    cartes.filterIsInstance<Carte.Pdf>().filter { it.date == null }

fun Programme.bilans(): List<Carte.Pdf> =
    cartes.filterIsInstance<Carte.Pdf>().filter { it.date != null }.sortedByDescending { it.date }

fun moisDe(date: String): String = date.take(7)

private fun carte(valeur: Valeur): Carte? {
    val reperes = reperes(valeur) ?: return null
    val bilan = reperes.rubrique == Rubrique.BILAN

    return when (valeur.texte("type")) {
        "pdf" -> pdf(reperes, valeur, bilan)
        "panneau" -> if (bilan) null else panneau(reperes, valeur)
        else -> null
    }
}

private fun reperes(valeur: Valeur): Reperes? {
    val id = valeur.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val titre = valeur.permis("titre") ?: return null
    val rubrique = Rubrique.entries.firstOrNull { it.cle == valeur.texte("rubrique") } ?: return null
    val quand = Quand.entries.firstOrNull { it.cle == valeur.texte("quand") }

    return Reperes(
        id = id,
        titre = titre,
        rubrique = rubrique,
        quand = quand,
        dureeMinutes = valeur.entier("duree_minutes"),
    )
}

// Une carte rangée au Bilan sans date n'aurait pas de place à l'écran, et disparaîtrait en silence.
private fun pdf(reperes: Reperes, valeur: Valeur, bilan: Boolean): Carte.Pdf? {
    val document = valeur.texte("document")?.takeIf { it.matches(KEBAB) } ?: return null
    val date = valeur.texte("date")?.takeIf { it.matches(DATE) }

    if (bilan != (date != null)) return null
    if (bilan != (reperes.quand == null)) return null

    return Carte.Pdf(reperes = reperes, document = document, date = date)
}

private fun panneau(reperes: Reperes, valeur: Valeur): Carte.Panneau? {
    if (reperes.quand == null) return null
    if (valeur.booleen("sortie_libre") != true) return null

    val porteur = Porteur.entries.firstOrNull { it.cle == valeur.texte("porteur") } ?: Porteur.PATIENT
    val lues = valeur.elements("etapes").map { etape(it, porteur) }
    if (lues.isEmpty() || lues.any { it == null }) return null

    val carte = Carte.Panneau(
        reperes = reperes,
        etapes = lues.filterNotNull(),
        porteur = porteur,
        signalArret = valeur.permis("signal_arret"),
        arret = textesPermis(valeur, "arret").orEmpty(),
    )

    return if (porteur == Porteur.AIDANT && !tenable(carte)) null else carte
}

// L'aidant ne peut ni corriger ni improviser : une carte qu'elle tient tombe entière s'il lui manque de quoi s'arrêter.
private fun tenable(carte: Carte.Panneau): Boolean {
    val minuteurs = carte.etapes.filterIsInstance<Etape.Minuteur>()

    return carte.signalArret != null &&
        carte.arret.size >= 2 &&
        DERNIER_CRITERE.containsMatchIn(carte.arret.last()) &&
        carte.etapes.first() is Etape.Checklist &&
        minuteurs.isNotEmpty() &&
        minuteurs.all { it.pour != null && it.consigne != null }
}

private fun etape(valeur: Valeur, porteur: Porteur): Etape? = when (valeur.texte("type")) {
    "info" -> valeur.permis("texte")?.let {
        Etape.Info(texte = it, montrable = valeur.booleen("montrable") == true)
    }

    "question" -> question(valeur)
    "note" -> note(valeur)
    "minuteur" -> minuteur(valeur, porteur)
    "checklist" -> checklist(valeur)
    "confirmation" -> valeur.permis("libelle")?.let(Etape::Confirmation)
    else -> null
}

// Un item perdu produit un score faux, donc faussement rassurant : une question amputée emporte la carte entière.
private fun question(valeur: Valeur): Etape.Question? {
    val id = valeur.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val enonce = valeur.permis("enonce") ?: return null
    val precision = valeur.permisSiPresent("precision") ?: return null
    val saisie = saisie(valeur) ?: return null

    return Etape.Question(
        id = id,
        enonce = enonce,
        precision = precision.ifEmpty { null },
        saisie = saisie,
        reprise = valeur.booleen("reprise") == true,
    )
}

private fun saisie(valeur: Valeur): Saisie? {
    val compteur = valeur.champ("compteur")
    val lus = valeur.elements("choix").map(::choix)

    if (compteur != null && lus.isNotEmpty()) return null
    if (compteur != null) return compteur(compteur)?.let(Saisie::Reglee)
    if (lus.size < 2 || lus.any { it == null }) return null

    return Saisie.Fermee(lus.filterNotNull())
}

private fun compteur(valeur: Valeur): Compteur? {
    val pas = valeur.nombre("pas")?.takeIf { it > 0.0 } ?: return null
    val grandPas = valeur.nombre("grand_pas")?.takeIf { it >= pas } ?: return null
    val minimum = valeur.nombre("minimum") ?: return null
    val depart = valeur.nombre("depart")?.takeIf { it >= minimum } ?: return null
    val unite = Unite.entries.firstOrNull { it.cle == valeur.texte("unite") } ?: return null

    return Compteur(depart = depart, pas = pas, grandPas = grandPas, minimum = minimum, unite = unite)
}

private fun choix(valeur: Valeur): Choix? {
    val chiffre = valeur.nombre("valeur") ?: return null
    val libelle = valeur.permis("libelle") ?: return null

    return Choix(valeur = chiffre, libelle = libelle)
}

private fun note(valeur: Valeur): Etape.Note? {
    val id = valeur.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val enonce = valeur.permis("enonce") ?: return null
    val precision = valeur.permisSiPresent("precision") ?: return null

    return Etape.Note(id = id, enonce = enonce, precision = precision.ifEmpty { null })
}

private fun minuteur(valeur: Valeur, porteur: Porteur): Etape.Minuteur? {
    val secondes = valeur.entier("secondes")?.takeIf { it > 0 } ?: return null
    val consigne = valeur.permisSiPresent("consigne") ?: return null
    val pour = Porteur.entries.firstOrNull { it.cle == valeur.texte("pour") }

    if (porteur == Porteur.AIDANT && pour == null) return null

    return Etape.Minuteur(secondes = secondes, consigne = consigne.ifEmpty { null }, pour = pour)
}

private fun checklist(valeur: Valeur): Etape.Checklist? {
    val enonce = valeur.permis("enonce") ?: return null
    val lignes = textesPermis(valeur, "lignes")?.takeIf { it.isNotEmpty() } ?: return null

    return Etape.Checklist(enonce = enonce, lignes = lignes)
}

private fun textesPermis(valeur: Valeur, cle: String): List<String>? {
    val lus = valeur.elements(cle).map { element ->
        (element as? Valeur.Texte)?.contenu?.takeIf { it.isNotBlank() && estPermis(it) }
    }

    return if (lus.any { it == null }) null else lus.filterNotNull()
}

private fun Valeur.permis(cle: String): String? = texte(cle)?.takeIf { it.isNotBlank() && estPermis(it) }

// Distingue « champ absent » (permis, rendu vide) de « champ fautif » (null, qui écarte la carte).
private fun Valeur.permisSiPresent(cle: String): String? = if (champ(cle) == null) "" else permis(cle)
