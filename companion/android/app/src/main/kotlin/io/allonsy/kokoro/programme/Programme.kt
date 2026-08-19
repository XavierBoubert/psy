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

enum class Pour(val cle: String) {
    AIDE("aide"),
    PATIENT("patient"),
}

data class Consigne(val pour: Pour, val consigne: String, val secondes: Int)

data class Choix(val valeur: Int, val libelle: String)

data class QuestionFermee(val id: String, val enonce: String, val choix: List<Choix>)

data class Reperes(
    val id: String,
    val titre: String,
    val rubrique: Rubrique,
    // Nul sur un bilan, et sur lui seul : sa date appartient au document, pas à l'assiduité de Xavier.
    val quand: Quand?,
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

    data class SeanceDuo(
        override val reperes: Reperes,
        val signalArret: String,
        val avant: List<String>,
        val sequence: List<Consigne>,
        val arret: List<String>,
    ) : Etape

    data class Bilan(override val reperes: Reperes, val document: String, val date: String) : Etape
}

val Etape.id: String get() = reperes.id

val Etape.titre: String get() = reperes.titre

val Etape.rubrique: Rubrique get() = reperes.rubrique

val Etape.quand: Quand? get() = reperes.quand

val Etape.dureeMinutes: Int? get() = reperes.dureeMinutes

data class Programme(val version: Int, val etapes: List<Etape>)

val PROGRAMME_ABSENT = Programme(version = 0, etapes = emptyList())

private val KEBAB = Regex("""^[a-z0-9-]+$""")

private val DATE = Regex("""^\d{4}-\d{2}-\d{2}$""")

private val DERNIER_CRITERE = Regex("""ne sais pas quoi faire""", RegexOption.IGNORE_CASE)

fun lireProgramme(json: String): Programme {
    val racine = lireJson(json) ?: return PROGRAMME_ABSENT

    return Programme(
        version = racine.entier("version") ?: 0,
        etapes = racine.elements("etapes").mapNotNull(::etape),
    )
}

fun Programme.etapesDe(rubrique: Rubrique): List<Etape> =
    etapes.filter { it.rubrique == rubrique && it !is Etape.Fiche && it !is Etape.Bilan }

// PROGRAMME.md §3 : une fiche vit sur Documentation quelle que soit sa rubrique.
fun Programme.fiches(): List<Etape.Fiche> = etapes.filterIsInstance<Etape.Fiche>()

fun Programme.bilans(): List<Etape.Bilan> =
    etapes.filterIsInstance<Etape.Bilan>().sortedByDescending { it.date }

fun moisDe(date: String): String = date.take(7)

private fun etape(valeur: Valeur): Etape? {
    val reperes = reperes(valeur) ?: return null
    val type = valeur.texte("type")
    val bilan = type == "bilan"

    // La rubrique bilan est réservée au type bilan : rangée là, une autre étape n'aurait pas de place à l'écran.
    if (bilan != (reperes.rubrique == Rubrique.BILAN)) return null
    if (bilan) return bilan(reperes, valeur)
    if (reperes.quand == null) return null

    return when (type) {
        "ecran" -> fonction(valeur)?.let { Etape.Ecran(reperes, it) }
        "exercice" -> exercice(reperes, valeur)
        "questionnaire" -> questionnaire(reperes, valeur)
        "demarche" -> valeur.permis("detail")?.let { Etape.Demarche(reperes, it) }
        "fiche" -> support(valeur)?.let { Etape.Fiche(reperes, it) }
        "seance-duo" -> seanceDuo(reperes, valeur)
        else -> null
    }
}

private fun reperes(valeur: Valeur): Reperes? {
    val id = valeur.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val titre = valeur.permis("titre") ?: return null
    val rubrique = Rubrique.entries.firstOrNull { it.cle == valeur.texte("rubrique") } ?: return null
    val quand = Quand.entries.firstOrNull { it.cle == valeur.texte("quand") }

    return Reperes(id = id, titre = titre, rubrique = rubrique, quand = quand, dureeMinutes = valeur.entier("duree_minutes"))
}

// Un bilan ne porte jamais de texte affiché ni de partage : Kokoro confie le PDF au lecteur du téléphone.
private fun bilan(reperes: Reperes, valeur: Valeur): Etape.Bilan? {
    if (reperes.quand != null || valeur.champ("texte") != null || valeur.champ("montrable") != null) return null

    val document = valeur.texte("document")?.takeIf { it.matches(KEBAB) } ?: return null
    val date = valeur.texte("date")?.takeIf { it.matches(DATE) } ?: return null

    return Etape.Bilan(reperes = reperes, document = document, date = date)
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

// L'aidant ne peut ni corriger ni improviser : une séance à deux amputée d'un critère d'arrêt tombe entière.
private fun seanceDuo(reperes: Reperes, valeur: Valeur): Etape.SeanceDuo? {
    if (valeur.booleen("entrainement_requis") != true || valeur.booleen("sortie_libre") != true) return null

    val signal = valeur.permis("signal_arret") ?: return null
    val avant = textesPermis(valeur, "avant") ?: return null
    val arret = textesPermis(valeur, "arret")?.takeIf { it.size >= 2 } ?: return null
    if (!DERNIER_CRITERE.containsMatchIn(arret.last())) return null

    val lues = valeur.elements("sequence").map(::consigne)
    if (lues.isEmpty() || lues.any { it == null }) return null

    return Etape.SeanceDuo(
        reperes = reperes,
        signalArret = signal,
        avant = avant,
        sequence = lues.filterNotNull(),
        arret = arret,
    )
}

private fun consigne(valeur: Valeur): Consigne? {
    val pour = Pour.entries.firstOrNull { it.cle == valeur.texte("pour") } ?: return null
    val dite = valeur.permis("consigne") ?: return null
    val secondes = valeur.entier("secondes")?.takeIf { it > 0 } ?: return null

    return Consigne(pour = pour, consigne = dite, secondes = secondes)
}

private fun textesPermis(valeur: Valeur, cle: String): List<String>? {
    val lus = valeur.elements(cle).map { element ->
        (element as? Valeur.Texte)?.contenu?.takeIf { it.isNotBlank() && estPermis(it) }
    }

    return if (lus.any { it == null }) null else lus.filterNotNull()
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
