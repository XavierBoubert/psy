package io.allonsy.kokoro.programme

private val NOM_DE_REPONSE = Regex("""^(\d{4}-\d{2}-\d{2})-\d{4}-([a-z0-9-]+)\.json$""")

data class Faites(
    val jour: String,
    val reponses: List<String>,
    // L'issue n'est pas dans le nom du fichier : l'entraînement mené se retient localement, il ne se relit pas de Drive.
    val entrainements: Set<String> = emptySet(),
)

val AUCUNE_FAITE = Faites(jour = "", reponses = emptyList())

fun Faites.entrainementMene(etape: Etape): Boolean = etape.id in entrainements

// Une séance à deux se refait : elle ne se coche pas, et un entraînement joué la dirait faite à tort.
fun Faites.faite(etape: Etape): Boolean = if (etape is Etape.SeanceDuo) false else when (etape.quand) {
    Quand.AU_BESOIN -> false
    Quand.AUJOURDHUI -> reponses.any { porte(it, etape.id) && it.startsWith(jour) }
    Quand.SANS_DATE -> reponses.any { porte(it, etape.id) }
    null -> false
}

private fun porte(nom: String, id: String): Boolean =
    NOM_DE_REPONSE.find(nom)?.groupValues?.get(2) == id
