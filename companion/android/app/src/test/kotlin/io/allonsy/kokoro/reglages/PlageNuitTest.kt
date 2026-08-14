package io.allonsy.kokoro.reglages

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * La nuit est une plage horaire, et une plage de nuit passe minuit. C'est tout ce qu'il y a à
 * vérifier — mais il faut le vérifier, parce qu'un intervalle qui s'enroule est exactement le genre
 * de calcul qui se trompe d'un jour sans que rien ne le signale.
 */
class PlageNuitTest {

    private val nuit = PlageNuit(active = true, debut = 21 * 60, fin = 6 * 60)

    @Test
    fun `la plage par defaut va de vingt et une heures a six heures`() {
        assertEquals(21 * 60, PLAGE_NUIT_PAR_DEFAUT.debut)
        assertEquals(6 * 60, PLAGE_NUIT_PAR_DEFAUT.fin)
        assertTrue(PLAGE_NUIT_PAR_DEFAUT.active)
    }

    @Test
    fun `une plage qui passe minuit couvre les deux cotes`() {
        assertTrue("22:00", estNuit(nuit, 22 * 60))
        assertTrue("03:00", estNuit(nuit, 3 * 60))
        assertTrue("21:00 — la borne de début est dedans", estNuit(nuit, 21 * 60))
    }

    @Test
    fun `le jour reste le jour`() {
        assertFalse("06:00 — la borne de fin est dehors", estNuit(nuit, 6 * 60))
        assertFalse("12:00", estNuit(nuit, 12 * 60))
        assertFalse("20:59", estNuit(nuit, 21 * 60 - 1))
    }

    @Test
    fun `une plage qui ne passe pas minuit se lit dans l ordre`() {
        val sieste = PlageNuit(active = true, debut = 13 * 60, fin = 15 * 60)

        assertTrue(estNuit(sieste, 14 * 60))
        assertFalse(estNuit(sieste, 12 * 60))
        assertFalse(estNuit(sieste, 16 * 60))
    }

    /** 🔴 Coupée, la nuit n'arrive jamais — c'est la porte de sortie, elle doit être franche. */
    @Test
    fun `une plage coupee ne declenche rien`() {
        val coupee = nuit.copy(active = false)

        assertFalse(estNuit(coupee, 23 * 60))
        assertFalse(estNuit(coupee, 3 * 60))
    }

    /** ⭐ Deux bornes égales sont un réglage ambigu : on reste au jour, jamais en nuit permanente. */
    @Test
    fun `deux bornes egales laissent le jour`() {
        val vide = PlageNuit(active = true, debut = 8 * 60, fin = 8 * 60)

        assertFalse(estNuit(vide, 8 * 60))
        assertFalse(estNuit(vide, 20 * 60))
    }

    @Test
    fun `les heures s ecrivent sur deux chiffres`() {
        assertEquals("21:00", ecrireHeure(21 * 60))
        assertEquals("06:05", ecrireHeure(6 * 60 + 5))
        assertEquals("00:00", ecrireHeure(0))
    }

    @Test
    fun `les heures se lisent dans les formes usuelles`() {
        assertEquals(21 * 60, lireHeure("21:00"))
        assertEquals(21 * 60 + 30, lireHeure("21h30"))
        assertEquals(6 * 60, lireHeure(" 6 "))
        assertEquals(0, lireHeure("0:00"))
    }

    /** Une saisie qui ne se lit pas ne s'enregistre pas : rien n'est corrigé en silence. */
    @Test
    fun `une saisie qui n est pas une heure ne se lit pas`() {
        assertNull(lireHeure(""))
        assertNull(lireHeure("24:00"))
        assertNull(lireHeure("21:75"))
        assertNull(lireHeure("le soir"))
        assertNull(lireHeure("21:00:00"))
    }
}
