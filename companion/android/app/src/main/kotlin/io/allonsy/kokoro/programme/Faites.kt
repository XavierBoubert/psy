package io.allonsy.kokoro.programme

private val NOM_DE_REPONSE = Regex("""^(\d{4}-\d{2}-\d{2})-\d{4}-([a-z0-9-]+)\.json$""")

data class Faites(
    val jour: String,
    val reponses: List<String>,
    // L'issue n'est pas dans le nom du fichier : l'entraînement mené se retient localement, il ne se relit pas de Drive.
    val entrainements: Set<String> = emptySet(),
    // Dernière valeur donnée à une question qui se reprend, par identifiant de question.
    val reprises: Map<String, Double> = emptyMap(),
)

val AUCUNE_FAITE = Faites(jour = "", reponses = emptyList())

fun Faites.entrainementMene(carte: Carte): Boolean = carte.id in entrainements

// Une carte tenue par l'aidant se refait : elle ne se coche pas, et un entraînement la dirait faite à tort.
fun Faites.faite(carte: Carte): Boolean = when {
    carte is Carte.Panneau && carte.porteur == Porteur.AIDANT -> false
    else -> when (carte.quand) {
        Quand.AU_BESOIN -> false
        Quand.AUJOURDHUI -> reponses.any { porte(it, carte.id) && it.startsWith(jour) }
        Quand.SANS_DATE -> reponses.any { porte(it, carte.id) }
        null -> false
    }
}

fun nomDeCarte(nom: String): String? = NOM_DE_REPONSE.find(nom)?.groupValues?.get(2)

private fun porte(nom: String, id: String): Boolean = nomDeCarte(nom) == id
