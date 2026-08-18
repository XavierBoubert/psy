package io.allonsy.kokoro.programme

enum class Quand(val cle: String) {
    AUJOURDHUI("aujourdhui"),
    AU_BESOIN("au_besoin"),
    SANS_DATE("sans_date"),
}

sealed interface Support {
    data class Pdf(val document: String) : Support

    data class Texte(val contenu: String) : Support
}

data class Fiche(
    val id: String,
    val titre: String,
    val quand: Quand,
    val support: Support,
)

data class Bibliotheque(val version: Int, val fiches: List<Fiche>)

val BIBLIOTHEQUE_ABSENTE = Bibliotheque(version = 0, fiches = emptyList())

private val KEBAB = Regex("""^[a-z0-9-]+$""")

fun lireBibliotheque(json: String): Bibliotheque {
    val racine = lireJson(json) ?: return BIBLIOTHEQUE_ABSENTE

    return Bibliotheque(
        version = racine.entier("version") ?: 0,
        fiches = racine.elements("etapes").mapNotNull(::fiche),
    )
}

private fun fiche(etape: Valeur): Fiche? {
    if (etape.texte("type") != "fiche") return null

    val id = etape.texte("id")?.takeIf { it.matches(KEBAB) } ?: return null
    val titre = etape.texte("titre")?.takeIf { it.isNotBlank() && estPermis(it) } ?: return null
    val quand = Quand.entries.firstOrNull { it.cle == etape.texte("quand") } ?: return null
    val support = support(etape) ?: return null

    return Fiche(id = id, titre = titre, quand = quand, support = support)
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
