package io.allonsy.kokoro.programme

import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

private const val INDENT = "  "

enum class Issue(val cle: String) {
    TERMINE("termine"),
    ARRETE("arrete_avant_la_fin"),
    FAIT("fait"),
    ENTRAINEMENT("entrainement"),
}

// « Passer » écrit null ; un item jamais atteint reste absent — les deux ne veulent pas dire la même chose à la cotation.
data class ReponseItem(val question: String, val valeur: Int?)

data class Reponse(
    val etape: String,
    val horodatage: String,
    val issue: Issue,
    val items: List<ReponseItem> = emptyList(),
)

fun reponseDe(
    etape: String,
    issue: Issue,
    quand: OffsetDateTime = OffsetDateTime.now(),
    items: List<ReponseItem> = emptyList(),
): Reponse =
    Reponse(
        etape = etape,
        horodatage = quand.truncatedTo(ChronoUnit.SECONDS).toString(),
        issue = issue,
        items = items,
    )

// Le nom se déduit de l'horodatage : psy-sync refuse un fichier dont le nom et le contenu divergent.
fun nomDeLaReponse(reponse: Reponse): String {
    val jour = reponse.horodatage.take(10)
    val minute = reponse.horodatage.substring(11, 13) + reponse.horodatage.substring(14, 16)

    return "$jour-$minute-${reponse.etape}.json"
}

fun serialiserReponse(reponse: Reponse): String =
    listOf(
        "{",
        """$INDENT"etape": "${reponse.etape}",""",
        """$INDENT"horodatage": "${reponse.horodatage}",""",
        """$INDENT"issue": "${reponse.issue.cle}",""",
        """$INDENT"reponses": ${serialiserItems(reponse.items)},""",
        "$INDENT\"source\": \"android\"",
        "}",
    ).joinToString("\n") + "\n"

private fun serialiserItems(items: List<ReponseItem>): String {
    if (items.isEmpty()) return "null"

    val lignes = items.joinToString(",\n") {
        """$INDENT$INDENT{ "question": "${it.question}", "valeur": ${it.valeur ?: "null"} }"""
    }

    return "[\n$lignes\n$INDENT]"
}
