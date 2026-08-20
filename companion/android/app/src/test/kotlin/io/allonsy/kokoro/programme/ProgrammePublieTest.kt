package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val FICHIER = File("../../inputs/programme.json")

// Une carte qu'un type non porté fait tomber ne dit rien à l'écran : elle disparaît, en silence, après supervision.
class ProgrammePublieTest {

    private val source: String by lazy {
        assertTrue("Programme introuvable : ${FICHIER.absolutePath}", FICHIER.isFile)
        FICHIER.readText()
    }

    @Test
    fun `le programme du depot est lisible`() {
        assertTrue("Le programme n'est pas du JSON complet", lireProgramme(source) != PROGRAMME_ABSENT)
    }

    // Les ids lus au JSON brut, jamais au mapper : c'est lui qu'on surveille, et une question porte aussi un id.
    @Test
    fun `aucune etape publiee n'est ecartee par Kokoro`() {
        val declares = lireJson(source)?.elements("cartes").orEmpty().mapNotNull { it.texte("id") }
        val lues = lireProgramme(source).cartes.map { it.id }

        assertEquals("Cartes publiées que Kokoro n'affiche pas : ${declares - lues.toSet()}", declares, lues)
    }
}
