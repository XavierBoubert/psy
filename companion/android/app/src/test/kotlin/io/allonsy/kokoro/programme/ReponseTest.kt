package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val LE_18_AOUT = OffsetDateTime.of(2026, 8, 18, 18, 4, 37, 0, ZoneOffset.ofHours(2))

private fun etape(id: String, quand: Quand): Etape.Demarche =
    Etape.Demarche(
        reperes = Reperes(id, "Un titre", Rubrique.THERAPIE, quand, null),
        detail = "Un détail.",
    )

class ReponseTest {

    // psy-sync refuse un fichier dont le nom ne se termine pas par l'étape qu'il déclare.
    @Test
    fun `le nom du fichier suit la convention et se deduit de l'horodatage`() {
        val reponse = reponseDe("ppc-palier-1", Issue.TERMINE, LE_18_AOUT)

        assertEquals("2026-08-18T18:04:37+02:00", reponse.horodatage)
        assertEquals("2026-08-18-1804-ppc-palier-1.json", nomDeLaReponse(reponse))
        assertTrue(Regex("""^\d{4}-\d{2}-\d{2}-\d{4}-[a-z0-9-]+\.json$""").matches(nomDeLaReponse(reponse)))
    }

    @Test
    fun `le contenu porte les cinq champs attendus par psy-sync`() {
        val ecrit = serialiserReponse(reponseDe("ppc-releve", Issue.FAIT, LE_18_AOUT))

        assertTrue(ecrit.contains(""""etape": "ppc-releve""""))
        assertTrue(ecrit.contains(""""horodatage": "2026-08-18T18:04:37+02:00""""))
        assertTrue(ecrit.contains(""""issue": "fait""""))
        assertTrue(ecrit.contains(""""reponses": null"""))
        assertTrue(ecrit.contains(""""source": "android""""))
    }

    @Test
    fun `les quatre issues du contrat portent leur cle`() {
        assertEquals(
            listOf("termine", "arrete_avant_la_fin", "fait", "entrainement"),
            Issue.entries.map { it.cle },
        )
    }
}

class FaitesTest {

    private val faites = Faites(
        jour = "2026-08-18",
        reponses = listOf(
            "2026-08-17-2210-ppc-palier-1.json",
            "2026-08-18-0930-ppc-releve.json",
            "2026-08-18-1804-mot-code.json",
        ),
    )

    // Une étape du jour repart à zéro le lendemain : hier ne se compte pas, et ne se reproche pas non plus.
    @Test
    fun `une etape du jour ne reste pas faite le lendemain`() {
        assertFalse(faites.faite(etape("ppc-palier-1", Quand.AUJOURDHUI)))
    }

    @Test
    fun `une etape du jour rendue aujourd'hui est faite`() {
        val aujourdhui = faites.copy(reponses = faites.reponses + "2026-08-18-1900-ppc-palier-1.json")

        assertTrue(aujourdhui.faite(etape("ppc-palier-1", Quand.AUJOURDHUI)))
    }

    @Test
    fun `une demarche sans date reste faite jusqu'a ce que le psy la retire`() {
        assertTrue(faites.faite(etape("ppc-releve", Quand.SANS_DATE)))
    }

    @Test
    fun `ce qui sert au besoin ne se grise jamais`() {
        assertFalse(faites.faite(etape("mot-code", Quand.AU_BESOIN)))
    }

    @Test
    fun `un identifiant n'est jamais confondu avec un autre qui le prefixe`() {
        assertFalse(faites.faite(etape("ppc-palier", Quand.SANS_DATE)))
        assertFalse(faites.faite(etape("releve", Quand.SANS_DATE)))
    }

    @Test
    fun `un nom hors convention ne rend rien fait`() {
        val bruit = Faites(jour = "2026-08-18", reponses = listOf("ppc-releve.json", "brouillon.txt"))

        assertFalse(bruit.faite(etape("ppc-releve", Quand.SANS_DATE)))
    }
}
