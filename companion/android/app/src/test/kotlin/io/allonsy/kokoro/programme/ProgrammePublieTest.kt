package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val FICHIER = File("../../inputs/programme.json")

// Une étape qu'un type non porté fait tomber ne dit rien à l'écran : elle disparaît, en silence, après supervision.
class ProgrammePublieTest {

    private val source: String by lazy {
        assertTrue("Programme introuvable : ${FICHIER.absolutePath}", FICHIER.isFile)
        FICHIER.readText()
    }

    @Test
    fun `le programme du depot est lisible`() {
        assertTrue("Le programme n'est pas du JSON complet", lireProgramme(source) != PROGRAMME_ABSENT)
    }

    @Test
    fun `aucune etape publiee n'est ecartee par Kokoro`() {
        val declares = Regex(""""id"\s*:\s*"([a-z0-9-]+)"""").findAll(source).map { it.groupValues[1] }.toList()
        val lues = lireProgramme(source).etapes.map { it.id }

        assertEquals("Étapes publiées que Kokoro n'affiche pas : ${declares - lues.toSet()}", declares, lues)
    }
}
