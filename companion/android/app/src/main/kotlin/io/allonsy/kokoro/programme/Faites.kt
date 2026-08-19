package io.allonsy.kokoro.programme

private val NOM_DE_REPONSE = Regex("""^(\d{4}-\d{2}-\d{2})-\d{4}-([a-z0-9-]+)\.json$""")

data class Faites(val jour: String, val reponses: List<String>)

val AUCUNE_FAITE = Faites(jour = "", reponses = emptyList())

// Une étape du jour repart à zéro le lendemain ; une démarche sans date reste faite jusqu'à ce que le psy la retire ;
// ce qui sert au besoin ne se grise jamais — c'est disponible, pas à faire. Un bilan (sans quand) ne renvoie rien.
fun Faites.faite(etape: Etape): Boolean = when (etape.quand) {
    Quand.AU_BESOIN -> false
    Quand.AUJOURDHUI -> reponses.any { porte(it, etape.id) && it.startsWith(jour) }
    Quand.SANS_DATE -> reponses.any { porte(it, etape.id) }
    null -> false
}

private fun porte(nom: String, id: String): Boolean =
    NOM_DE_REPONSE.find(nom)?.groupValues?.get(2) == id
