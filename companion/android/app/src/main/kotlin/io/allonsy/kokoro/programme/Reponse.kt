package io.allonsy.kokoro.programme

import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

enum class Issue(val cle: String) {
    TERMINE("termine"),
    ARRETE("arrete_avant_la_fin"),
    FAIT("fait"),
    ENTRAINEMENT("entrainement"),
}

data class Reponse(val etape: String, val horodatage: String, val issue: Issue)

fun reponseDe(etape: String, issue: Issue, quand: OffsetDateTime = OffsetDateTime.now()): Reponse =
    Reponse(
        etape = etape,
        horodatage = quand.truncatedTo(ChronoUnit.SECONDS).toString(),
        issue = issue,
    )

// Le nom se déduit de l'horodatage : psy-sync refuse un fichier dont le nom et le contenu divergent.
fun nomDeLaReponse(reponse: Reponse): String {
    val jour = reponse.horodatage.take(10)
    val minute = reponse.horodatage.substring(11, 13) + reponse.horodatage.substring(14, 16)

    return "$jour-$minute-${reponse.etape}.json"
}

fun serialiserReponse(reponse: Reponse): String =
    """
    {
      "etape": "${reponse.etape}",
      "horodatage": "${reponse.horodatage}",
      "issue": "${reponse.issue.cle}",
      "reponses": null,
      "source": "android"
    }
    """.trimIndent() + "\n"
