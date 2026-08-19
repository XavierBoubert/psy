package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private fun programme(etapes: String): String =
    """{ "version": 7, "publie_le": "2026-08-19", "supervision": "essai", "etapes": [$etapes] }"""

private const val DUO =
    """{ "id": "essai-a-deux", "titre": "Essai à deux", "type": "seance-duo", "rubrique": "therapie",
        "quand": "au_besoin", "duree_minutes": 3,
        "entrainement_requis": true,
        "signal_arret": "Xavier fait « non » de la main. On s'arrête, sans rien demander.",
        "avant": [ "Le téléphone reste dans tes mains du début à la fin." ],
        "sequence": [
          { "pour": "aide", "consigne": "Assieds-toi en face de lui, à un mètre.", "secondes": 30 },
          { "pour": "patient", "consigne": "Pose les deux pieds à plat.", "secondes": 60 } ],
        "arret": [
          "Il fait le signal d'arrêt → on s'arrête, on ne demande rien.",
          "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser." ],
        "sortie_libre": true }"""

class SeanceDuoTest {

    @Test
    fun `la seance a deux porte son signal, sa sequence et ses criteres d'arret`() {
        val duo = lireProgramme(programme(DUO)).etapes.single() as Etape.SeanceDuo

        assertEquals("essai-a-deux", duo.id)
        assertEquals("Xavier fait « non » de la main. On s'arrête, sans rien demander.", duo.signalArret)
        assertEquals(listOf(Pour.AIDE, Pour.PATIENT), duo.sequence.map { it.pour })
        assertEquals(listOf(30, 60), duo.sequence.map { it.secondes })
        assertEquals(1, duo.avant.size)
        assertEquals(2, duo.arret.size)
    }

    @Test
    fun `la seance a deux se range dans sa rubrique, comme une etape qui fait agir`() {
        val lu = lireProgramme(programme(DUO))

        assertEquals(listOf("essai-a-deux"), lu.etapesDe(Rubrique.THERAPIE).map { it.id })
        assertTrue(lu.fiches().isEmpty())
        assertTrue(lu.bilans().isEmpty())
    }

    // L'aidant ne peut ni corriger ni improviser : ce qui manque ne s'invente pas, l'étape entière tombe.
    @Test
    fun `une seance a deux amputee d'une garde tombe entiere`() {
        val sansSignal = DUO.replace(""""signal_arret": "Xavier fait « non » de la main. On s'arrête, sans rien demander.",""", "")
        val signalVide = DUO.replace("Xavier fait « non » de la main. On s'arrête, sans rien demander.", "  ")
        val sansEntrainement = DUO.replace(""""entrainement_requis": true""", """"entrainement_requis": false""")
        val sansSortie = DUO.replace(""""sortie_libre": true""", """"sortie_libre": false""")
        val sansSequence = DUO.replace(
            """{ "pour": "aide", "consigne": "Assieds-toi en face de lui, à un mètre.", "secondes": 30 },
          { "pour": "patient", "consigne": "Pose les deux pieds à plat.", "secondes": 60 }""",
            "",
        )
        val pourInconnu = DUO.replace(""""pour": "aide"""", """"pour": "soignant"""")
        val sansMinuteur = DUO.replace(""""secondes": 30""", """"secondes": 0""")

        val ecartees = listOf(sansSignal, signalVide, sansEntrainement, sansSortie, sansSequence, pourInconnu, sansMinuteur)

        assertTrue(ecartees.all { lireProgramme(programme(it)).etapes.isEmpty() })
    }

    // Le dernier critère est toujours « tu ne sais pas quoi faire → on s'arrête » : l'aidant n'improvise jamais.
    @Test
    fun `un critere d'arret unique ou sans porte de sortie ecarte la seance`() {
        val critereUnique = DUO.replace(
            """"Il fait le signal d'arrêt → on s'arrête, on ne demande rien.",
          "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser." """,
            """"Tu ne sais pas quoi faire → on s'arrête." """,
        )
        val sansPorteDeSortie = DUO.replace("Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser.", "On continue.")

        assertTrue(lireProgramme(programme(critereUnique)).etapes.isEmpty())
        assertTrue(lireProgramme(programme(sansPorteDeSortie)).etapes.isEmpty())
    }

    // Les sept familles d'interdits se réappliquent à la lecture : Kokoro écarte la seule étape fautive.
    @Test
    fun `une consigne interdite ecarte la seance a deux`() {
        val consigneInterdite = DUO.replace("Pose les deux pieds à plat.", "Visualise un lieu sûr.")
        val avantInterdit = DUO.replace("Le téléphone reste dans tes mains du début à la fin.", "Détends-toi avant de commencer.")

        assertTrue(lireProgramme(programme(consigneInterdite)).etapes.isEmpty())
        assertTrue(lireProgramme(programme(avantInterdit)).etapes.isEmpty())
    }

    // Une séance à deux se refait : elle ne se coche pas, et un entraînement joué la dirait faite à tort.
    @Test
    fun `une seance a deux ne se grise jamais, et son entrainement se retient a part`() {
        val duo = lireProgramme(programme(DUO)).etapes.single()
        val rendue = Faites(
            jour = "2026-08-19",
            reponses = listOf("2026-08-19-2104-essai-a-deux.json"),
            entrainements = setOf("essai-a-deux"),
        )

        assertFalse(rendue.faite(duo))
        assertTrue(rendue.entrainementMene(duo))
        assertFalse(AUCUNE_FAITE.entrainementMene(duo))
    }
}
