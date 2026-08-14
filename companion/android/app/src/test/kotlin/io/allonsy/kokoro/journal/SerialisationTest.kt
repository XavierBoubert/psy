package io.allonsy.kokoro.journal

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val GABARIT = File("../../dossier/gabarits/journal.json")

/**
 * Le format du dossier est normatif (`PLAN.md` §7) : aucune surface n'a le droit de
 * l'inventer. Ces tests comparent ce que Kokoro écrit au gabarit du dossier lui-même —
 * si le schéma change sans que l'app suive, la compilation échoue.
 */
class SerialisationTest {

    private val gabarit: String by lazy {
        assertTrue("Gabarit introuvable : ${GABARIT.absolutePath}", GABARIT.isFile)
        GABARIT.readText().replace("\r\n", "\n")
    }

    @Test
    fun `un checkin vide reproduit le gabarit du dossier`() {
        val attendu = gabarit
            .replace("\"date\": \"AAAA-MM-JJ\"", "\"date\": \"2026-08-11\"")
            .replace("\"source\": \"claude-code\"", "\"source\": \"android\"")

        assertEquals(attendu, serialiser(Checkin.vide("2026-08-11")))
    }

    @Test
    fun `un champ sans reponse reste null et jamais zero`() {
        val checkin = Checkin.vide("2026-08-11").avec(Champ.SHUTDOWNS, 0.0)
        val json = serialiser(checkin)

        assertTrue(json.contains("\"shutdowns\": 0,"))
        assertTrue(json.contains("\"renoncements\": null,"))
    }

    @Test
    fun `les nombres decimaux s ecrivent avec un point`() {
        val checkin = Checkin.vide("2026-08-11")
            .avec(Champ.SOMMEIL_HEURES, 6.5)
            .avec(Champ.POIDS_KG, 110.0)
        val json = serialiser(checkin)

        assertTrue(json.contains("\"sommeil_heures\": 6.5,"))
        assertTrue(json.contains("\"poids_kg\": 110.0"))
    }

    @Test
    fun `les entiers ne portent pas de decimale`() {
        val json = serialiser(Checkin.vide("2026-08-11").avec(Champ.PPC_MINUTES, 240.0))

        assertTrue(json.contains("\"ppc_minutes\": 240,"))
    }

    @Test
    fun `une note est echappee et reste en dernier`() {
        val json = serialiser(Checkin.vide("2026-08-11").copy(notes = "un \"guillemet\"\net une ligne"))

        assertTrue(json.contains("\"notes\": \"un \\\"guillemet\\\"\\net une ligne\""))
        assertEquals("\"notes\"", json.trim().lines().dropLast(1).last().trim().substringBefore(":"))
    }

    @Test
    fun `une valeur se relit pour poser le depart d un compteur`() {
        val json = serialiser(Checkin.vide("2026-08-11").avec(Champ.MISSIONS_ACTIVES, 3.0))

        assertEquals(3.0, relireValeur(json, Champ.MISSIONS_ACTIVES)!!, 0.001)
        assertEquals(null, relireValeur(json, Champ.POIDS_KG))
    }
}
