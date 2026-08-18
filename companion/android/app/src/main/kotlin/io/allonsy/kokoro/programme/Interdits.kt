package io.allonsy.kokoro.programme

import java.text.Normalizer

// Mêmes sept familles que psy-publish.ts : le PC refuse la publication entière, Kokoro écarte la seule fiche fautive.
private val INTERDITS = listOf(
    Regex("""\b(imagine|imaginer|imaginez|visualise|visualiser|represente-toi|image mentale|lieu sur)\b"""),
    Regex("""\bsur 10\b|\bnote (ton|ta)\b|\bton niveau de\b|\bevalue (ton|ta)\b|\ba combien tu te sens\b"""),
    Regex("""\bd'affilee\b|\bconsecutif|\bserie\b|\bregularite\b|\bstreak\b|jour \d+ sur|% de l'objectif"""),
    Regex("""\b3114\b|\bsamu\b|\bpompiers\b|(appel\w*|composer|numero)[^.]{0,24}\b(15|112|114)\b"""),
    Regex("""\bas-tu besoin\b|\bquand tu sens\b|\baux premiers signes\b|\bsi tu sens\b"""),
    Regex("""\bvenlafaxine\b|\balprazolam\b|\bparoxetine\b|\bdose\b|\bposologie\b|\bcachet\b|\bcomprime\b|\bton traitement\b"""),
    Regex("""\bdetends-toi\b|\bdetente\b|\brespire lentement\b|\brespiration lente\b|\brelaxation\b"""),
)

private val MARQUES = Regex("""\p{M}+""")

fun estPermis(texte: String): Boolean {
    val normalise = normalise(texte)
    return INTERDITS.none { it.containsMatchIn(normalise) }
}

private fun normalise(texte: String): String =
    Normalizer.normalize(texte, Normalizer.Form.NFD)
        .replace(MARQUES, "")
        .replace('‘', '\'')
        .replace('’', '\'')
        .lowercase()
