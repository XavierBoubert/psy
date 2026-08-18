package io.allonsy.kokoro.monde

import org.junit.Assert.assertEquals
import org.junit.Test

class MinuteurTest {

    @Test
    fun `le reste decroit a la seconde et s'arrete a zero`() {
        assertEquals(300, resteSecondes(300, 0L))
        assertEquals(300, resteSecondes(300, 999L))
        assertEquals(299, resteSecondes(300, 1000L))
        assertEquals(0, resteSecondes(300, 300_000L))
        assertEquals(0, resteSecondes(300, 400_000L))
    }

    @Test
    fun `un ecoule negatif ne fabrique pas de temps en plus`() {
        assertEquals(300, resteSecondes(300, -5_000L))
    }

    @Test
    fun `le libelle est un temps restant, jamais un compte de seances`() {
        assertEquals("5:00", libelleDuReste(300))
        assertEquals("4:09", libelleDuReste(249))
        assertEquals("0:07", libelleDuReste(7))
        assertEquals("0:00", libelleDuReste(0))
    }
}
