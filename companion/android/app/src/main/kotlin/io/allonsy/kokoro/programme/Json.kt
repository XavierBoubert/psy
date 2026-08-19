package io.allonsy.kokoro.programme

sealed interface Valeur {
    data class Objet(val champs: Map<String, Valeur>) : Valeur

    data class Liste(val elements: List<Valeur>) : Valeur

    data class Texte(val contenu: String) : Valeur

    data class Nombre(val contenu: Double) : Valeur

    data class Booleen(val contenu: Boolean) : Valeur

    data object Vide : Valeur
}

fun lireJson(source: String): Valeur? {
    val curseur = Curseur(source)
    val valeur = curseur.valeur() ?: return null
    return if (curseur.plusRienApres()) valeur else null
}

fun Valeur.champ(cle: String): Valeur? = (this as? Valeur.Objet)?.champs?.get(cle)

fun Valeur.texte(cle: String): String? = (champ(cle) as? Valeur.Texte)?.contenu

fun Valeur.entier(cle: String): Int? = (champ(cle) as? Valeur.Nombre)?.contenu?.toInt()

fun Valeur.booleen(cle: String): Boolean? = (champ(cle) as? Valeur.Booleen)?.contenu

fun Valeur.elements(cle: String): List<Valeur> = (champ(cle) as? Valeur.Liste)?.elements.orEmpty()

private class Curseur(private val source: String) {
    private var rang = 0

    fun plusRienApres(): Boolean {
        sauterBlancs()
        return rang >= source.length
    }

    fun valeur(): Valeur? {
        sauterBlancs()
        return when (actuel()) {
            null -> null
            '{' -> objet()
            '[' -> liste()
            '"' -> texte()?.let(Valeur::Texte)
            't' -> litteral("true", Valeur.Booleen(true))
            'f' -> litteral("false", Valeur.Booleen(false))
            'n' -> litteral("null", Valeur.Vide)
            else -> nombre()
        }
    }

    private fun objet(): Valeur? {
        rang += 1
        val champs = mutableMapOf<String, Valeur>()
        sauterBlancs()
        if (avaler('}')) return Valeur.Objet(champs)

        while (true) {
            sauterBlancs()
            val cle = texte() ?: return null
            sauterBlancs()
            if (!avaler(':')) return null
            champs[cle] = valeur() ?: return null
            sauterBlancs()
            if (avaler(',')) continue
            return if (avaler('}')) Valeur.Objet(champs) else null
        }
    }

    private fun liste(): Valeur? {
        rang += 1
        val elements = mutableListOf<Valeur>()
        sauterBlancs()
        if (avaler(']')) return Valeur.Liste(elements)

        while (true) {
            elements.add(valeur() ?: return null)
            sauterBlancs()
            if (avaler(',')) continue
            return if (avaler(']')) Valeur.Liste(elements) else null
        }
    }

    private fun texte(): String? {
        if (!avaler('"')) return null
        val assemble = StringBuilder()

        while (true) {
            val caractere = actuel() ?: return null
            rang += 1
            when (caractere) {
                '"' -> return assemble.toString()
                '\\' -> assemble.append(echappe() ?: return null)
                else -> assemble.append(caractere)
            }
        }
    }

    private fun echappe(): Char? {
        val caractere = actuel() ?: return null
        rang += 1
        return when (caractere) {
            '"', '\\', '/' -> caractere
            'b' -> '\b'
            'f' -> '\u000C'
            'n' -> '\n'
            'r' -> '\r'
            't' -> '\t'
            'u' -> unicode()
            else -> null
        }
    }

    private fun unicode(): Char? {
        if (rang + 4 > source.length) return null
        val point = source.substring(rang, rang + 4).toIntOrNull(16) ?: return null
        rang += 4
        return point.toChar()
    }

    private fun nombre(): Valeur? {
        val debut = rang
        while (actuel()?.let { it.isDigit() || it in "-+.eE" } == true) rang += 1
        return source.substring(debut, rang).toDoubleOrNull()?.let(Valeur::Nombre)
    }

    private fun litteral(attendu: String, valeur: Valeur): Valeur? {
        if (!source.startsWith(attendu, rang)) return null
        rang += attendu.length
        return valeur
    }

    private fun sauterBlancs() {
        while (actuel()?.isWhitespace() == true) rang += 1
    }

    private fun avaler(attendu: Char): Boolean {
        if (actuel() != attendu) return false
        rang += 1
        return true
    }

    private fun actuel(): Char? = source.getOrNull(rang)
}
