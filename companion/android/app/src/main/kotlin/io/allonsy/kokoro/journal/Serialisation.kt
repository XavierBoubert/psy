package io.allonsy.kokoro.journal

import java.util.Locale

private const val INDENT = "  "

/**
 * Écrit le JSON du check-in exactement comme `psy/docs/gabarits/journal.json` :
 * mêmes clés, même ordre, deux espaces d'indentation, `notes` en dernier.
 *
 * Sérialiseur écrit à la main plutôt qu'une bibliothèque : le format du dossier est
 * normatif et se vérifie caractère par caractère en test JVM, sans appareil.
 */
fun serialiser(checkin: Checkin): String {
    val lignes = buildList {
        add("{")
        add("$INDENT\"date\": ${chaine(checkin.date)},")
        add("$INDENT\"source\": ${chaine(SOURCE_ANDROID)},")
        addAll(bloc("noyau", Section.NOYAU, checkin))
        addAll(bloc("campagne", Section.CAMPAGNE, checkin))
        add("$INDENT\"notes\": ${checkin.notes?.let { chaine(it) } ?: "null"}")
        add("}")
    }
    return lignes.joinToString("\n") + "\n"
}

private fun bloc(nom: String, section: Section, checkin: Checkin): List<String> {
    val champs = Champ.entries.filter { it.section == section }
    return buildList {
        add("$INDENT\"$nom\": {")
        champs.forEachIndexed { index, champ ->
            val virgule = if (index == champs.lastIndex) "" else ","
            val valeur = nombre(checkin.valeur(champ), champ.decimal)
            add("$INDENT$INDENT\"${champ.cle}\": $valeur$virgule")
        }
        add("$INDENT},")
    }
}

private fun nombre(valeur: Double?, decimal: Boolean): String = when {
    valeur == null -> "null"
    decimal -> String.format(Locale.ROOT, "%.1f", valeur)
    else -> valeur.toLong().toString()
}

private fun chaine(texte: String): String {
    val echappe = texte
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
    return "\"$echappe\""
}

/**
 * Relit une valeur dans un check-in déjà écrit. Sert uniquement à poser le point de
 * départ d'un compteur (missions en cours, poids) — jamais à afficher un historique,
 * une comparaison ou une évolution.
 */
fun relireValeur(json: String, champ: Champ): Double? {
    val motif = Regex("\"${champ.cle}\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)")
    return motif.find(json)?.groupValues?.get(1)?.toDoubleOrNull()
}
