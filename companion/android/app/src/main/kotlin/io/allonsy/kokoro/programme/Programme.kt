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

enum class Fonction(val cle: String) {
    CHECK_IN("check-in"),
    MOT_CODE("mot-code"),
    TENSION("tension-appliquee"),
    PHRASE("phrase-soignant"),
}

sealed interface Support {
    data class Pdf(val document: String) : Support

    data class Texte(val contenu: String) : Support
}

data class Choix(val valeur: Int, val libelle: String)

data class QuestionFermee(val id: String, val enonce: String, val choix: List<Choix>)

data class Reperes(
    val id: String,
    val titre: String,
    val rubrique: Rubrique,
    val quand: Quand,
    val dureeMinutes: Int?,
)

sealed interface Etape {
    val reperes: Reperes

    data class Ecran(override val reperes: Reperes, val fonction: Fonction) : Etape

    data class Exercice(
        override val reperes: Reperes,
        val consigne: String,
        val minuteurSecondes: Int,
    ) : Etape

    data class Questionnaire(
        override val reperes: Reperes,
        val questions: List<QuestionFermee>,
    ) : Etape

    data class Demarche(override val reperes: Reperes, val detail: String) : Etape

    data class Fiche(override val reperes: Reperes, val support: Support) : Etape
}

val Etape.id: String get() = reperes.id

val Etape.titre: String get() = reperes.titre

val Etape.rubrique: Rubrique get() = reperes.rubrique

val Etape.quand: Quand get() = reperes.quand

val Etape.dureeMinutes: Int? get() = reperes.dureeMinutes

data class Programme(val version: Int, val etapes: List<Etape>)

val PROGRAMME_ABSENT = Programme(version = 0, etapes = emptyList())

private val KEBAB = Regex("""^[a-z0-9-]+$""")

fun lireProgramme(json: String): Programme {
    val racine = lireJson(json) ?: return PROGRAMME_ABSENT

    return Programme(
        version = racine.entier("version") ?: 0,
        etapes = racine.elements("etapes").mapNotNull(::etape),
    )
}

fun Programme.etapesDe(rubrique: Rubrique): List<Etape> =
    etapes.filter { it.rubrique == rubrique && it !is Etape.Fiche }

// PROGRAMME.md §3 : une fiche vit sur Documentation quelle que soit sa rubrique.
fun Programme.fiches(): List<Etape.Fiche> = etapes.filterIsInstance<Etape.Fiche>()

private fun etape(valeur: Valeur): Etape? {
    val reperes = reperes(valeur) ?: return null

    return when (valeur.texte("type")) {
        "ecran" -> fonction(valeur)?.let { Etape.Ecran(reperes, it) }
        "exercice" -> exercice(reperes, valeur)
        "questionnaire" -> questionnaire(reperes, valeur)
        "demarche" -> valeur.permis("detail")?.let { Etape.Demarche(reperes, it) }
        "fiche" -> support(valeur)?.let { Etape.Fiche(reperes, it) }
        else -> null
    }
}

private fun reperes(valeur: Valeur): Reperes? {
    val id = valeur.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val titre = valeur.permis("titre") ?: return null
    val rubrique = Rubrique.entries.firstOrNull { it.cle == valeur.texte("rubrique") } ?: return null
    val quand = Quand.entries.firstOrNull { it.cle == valeur.texte("quand") } ?: return null

    return Reperes(id = id, titre = titre, rubrique = rubrique, quand = quand, dureeMinutes = valeur.entier("duree_minutes"))
}

private fun fonction(valeur: Valeur): Fonction? =
    Fonction.entries.firstOrNull { it.cle == valeur.texte("ecran") }

private fun exercice(reperes: Reperes, valeur: Valeur): Etape.Exercice? {
    val consigne = valeur.permis("consigne") ?: return null
    val secondes = valeur.entier("minuteur_secondes")?.takeIf { it > 0 } ?: return null

    return Etape.Exercice(reperes = reperes, consigne = consigne, minuteurSecondes = secondes)
}

// Un questionnaire amputé d'un item produirait un score faux, donc faussement rassurant : il tombe entier ou pas du tout.
private fun questionnaire(reperes: Reperes, valeur: Valeur): Etape.Questionnaire? {
    val lues = valeur.elements("questions").map(::question)
    if (lues.isEmpty() || lues.any { it == null }) return null

    val questions = lues.filterNotNull()

    return if (questions.distinctBy { it.id }.size == questions.size) {
        Etape.Questionnaire(reperes = reperes, questions = questions)
    } else {
        null
    }
}

private fun question(valeur: Valeur): QuestionFermee? {
    val id = valeur.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val enonce = valeur.permis("enonce") ?: return null
    val lus = valeur.elements("choix").map(::choix)
    if (lus.size < 2 || lus.any { it == null }) return null

    return QuestionFermee(id = id, enonce = enonce, choix = lus.filterNotNull())
}

private fun choix(valeur: Valeur): Choix? {
    val chiffre = valeur.entier("valeur") ?: return null
    val libelle = valeur.permis("libelle") ?: return null

    return Choix(valeur = chiffre, libelle = libelle)
}

private fun support(etape: Valeur): Support? {
    val document = etape.texte("document")
    val texte = etape.texte("texte")

    return when {
        document != null && texte != null -> null
        document != null -> document.takeIf { it.matches(KEBAB) }?.let(Support::Pdf)
        texte != null -> texte.takeIf { it.isNotBlank() && estPermis(it) }?.let(Support::Texte)
        else -> null
    }
}

private fun Valeur.permis(cle: String): String? = texte(cle)?.takeIf { it.isNotBlank() && estPermis(it) }
