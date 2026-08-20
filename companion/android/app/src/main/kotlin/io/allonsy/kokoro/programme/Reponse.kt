package io.allonsy.kokoro.programme

import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

private const val INDENT = "  "

enum class Issue(val cle: String) {
    TERMINE("termine"),
    ARRETE("arrete_avant_la_fin"),
    FAIT("fait"),
    ENTRAINEMENT("entrainement"),
}

// « Passer » écrit null ; un item jamais atteint reste absent — les deux ne veulent pas dire la même chose à la cotation.
sealed interface ReponseItem {
    val question: String

    data class Nombre(override val question: String, val valeur: Double?) : ReponseItem

    data class Texte(override val question: String, val texte: String?) : ReponseItem
}

data class Reponse(
    val carte: String,
    val horodatage: String,
    val issue: Issue,
    val items: List<ReponseItem> = emptyList(),
)

fun reponseDe(
    carte: String,
    issue: Issue,
    quand: OffsetDateTime = OffsetDateTime.now(),
    items: List<ReponseItem> = emptyList(),
): Reponse =
    Reponse(
        carte = carte,
        horodatage = quand.truncatedTo(ChronoUnit.SECONDS).toString(),
        issue = issue,
        items = items,
    )

// Le nom se déduit de l'horodatage : psy-sync refuse un fichier dont le nom et le contenu divergent.
fun nomDeLaReponse(reponse: Reponse): String {
    val jour = reponse.horodatage.take(10)
    val minute = reponse.horodatage.substring(11, 13) + reponse.horodatage.substring(14, 16)

    return "$jour-$minute-${reponse.carte}.json"
}

fun serialiserReponse(reponse: Reponse): String =
    listOf(
        "{",
        """$INDENT"carte": "${reponse.carte}",""",
        """$INDENT"horodatage": "${reponse.horodatage}",""",
        """$INDENT"issue": "${reponse.issue.cle}",""",
        """$INDENT"reponses": ${serialiserItems(reponse.items)},""",
        "$INDENT\"source\": \"android\"",
        "}",
    ).joinToString("\n") + "\n"

private fun serialiserItems(items: List<ReponseItem>): String {
    if (items.isEmpty()) return "null"

    val lignes = items.joinToString(",\n") { "$INDENT$INDENT${serialiserItem(it)}" }

    return "[\n$lignes\n$INDENT]"
}

private fun serialiserItem(item: ReponseItem): String = when (item) {
    is ReponseItem.Nombre -> """{ "question": ${chaine(item.question)}, "valeur": ${nombre(item.valeur)} }"""
    is ReponseItem.Texte ->
        """{ "question": ${chaine(item.question)}, "texte": ${item.texte?.let(::chaine) ?: "null"} }"""
}

// Un entier s'écrit sans décimale : le journal reconstruit au dépôt distingue un compte d'une mesure.
fun nombre(valeur: Double?): String = when {
    valeur == null -> "null"
    valeur == Math.floor(valeur) && !valeur.isInfinite() -> valeur.toLong().toString()
    else -> String.format(Locale.ROOT, "%.1f", valeur)
}

// Relue depuis le dossier : seules les valeurs chiffrées servent, une note ne se reprend jamais.
fun valeursDeLaReponse(json: String): Map<String, Double> {
    val racine = lireJson(json) ?: return emptyMap()

    return racine.elements("reponses").mapNotNull { item ->
        val question = item.texte("question") ?: return@mapNotNull null
        item.nombre("valeur")?.let { question to it }
    }.toMap()
}

fun chaine(texte: String): String {
    val echappe = texte
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")

    return "\"$echappe\""
}
