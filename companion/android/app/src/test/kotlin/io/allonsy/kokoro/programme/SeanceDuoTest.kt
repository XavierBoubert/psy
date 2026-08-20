package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private fun programme(cartes: String): String =
    """{ "version": 12, "publie_le": "2026-08-20", "supervision": "essai", "cartes": [$cartes] }"""

private const val DUO =
    """{ "id": "essai-a-deux", "titre": "Essai à deux", "type": "panneau", "rubrique": "therapie",
        "quand": "au_besoin", "duree_minutes": 3, "porteur": "aidant", "sortie_libre": true,
        "signal_arret": "Xavier fait « non » de la main. On s'arrête, sans rien demander.",
        "arret": [
          "Il fait le signal d'arrêt → on s'arrête, on ne demande rien.",
          "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser." ],
        "etapes": [
          { "type": "checklist", "enonce": "À vérifier avant d'entrer dans le déroulé.",
            "lignes": [ "Le téléphone reste dans tes mains du début à la fin." ] },
          { "type": "minuteur", "pour": "aidant", "consigne": "Assieds-toi en face de lui, à un mètre.", "secondes": 30 },
          { "type": "minuteur", "pour": "patient", "consigne": "Pose les deux pieds à plat.", "secondes": 60 } ] }"""

private fun duo(source: String = DUO): Carte.Panneau =
    lireProgramme(programme(source)).cartes.single() as Carte.Panneau

class SeanceDuoTest {

    @Test
    fun `la carte tenue par l'aidant porte son signal, sa sequence et ses criteres d'arret`() {
        val carte = duo()

        assertEquals("essai-a-deux", carte.id)
        assertEquals(Porteur.AIDANT, carte.porteur)
        assertEquals("Xavier fait « non » de la main. On s'arrête, sans rien demander.", carte.signalArret)
        assertEquals(2, carte.arret.size)
        assertEquals(
            listOf(Porteur.AIDANT, Porteur.PATIENT),
            carte.etapes.filterIsInstance<Etape.Minuteur>().map { it.pour },
        )
        assertEquals(listOf(30, 60), carte.etapes.filterIsInstance<Etape.Minuteur>().map { it.secondes })
        assertEquals(1, (carte.etapes.first() as Etape.Checklist).lignes.size)
    }

    @Test
    fun `la carte a deux se range dans sa rubrique, comme une carte qui fait agir`() {
        val lu = lireProgramme(programme(DUO))

        assertEquals(listOf("essai-a-deux"), lu.cartesDe(Rubrique.THERAPIE).map { it.id })
        assertTrue(lu.documents().isEmpty())
        assertTrue(lu.bilans().isEmpty())
    }

    // L'aidant ne peut ni corriger ni improviser : ce qui manque ne s'invente pas, la carte entière tombe.
    @Test
    fun `une carte a deux amputee d'une garde tombe entiere`() {
        val sansSignal = DUO.replace(
            """"signal_arret": "Xavier fait « non » de la main. On s'arrête, sans rien demander.",""",
            "",
        )
        val signalVide = DUO.replace("Xavier fait « non » de la main. On s'arrête, sans rien demander.", "  ")
        val sansSortie = DUO.replace(""""sortie_libre": true""", """"sortie_libre": false""")
        val sansChecklist = DUO.replace(
            """{ "type": "checklist", "enonce": "À vérifier avant d'entrer dans le déroulé.",
            "lignes": [ "Le téléphone reste dans tes mains du début à la fin." ] },""",
            "",
        )
        val pourInconnu = DUO.replace(""""pour": "aidant"""", """"pour": "soignant"""")
        val sansPour = DUO.replace(""""pour": "aidant", """, "")
        val sansConsigne = DUO.replace(""""consigne": "Assieds-toi en face de lui, à un mètre.", """, "")
        val sansMinuteur = DUO.replace(""""secondes": 30""", """"secondes": 0""")

        val ecartees = listOf(
            sansSignal, signalVide, sansSortie, sansChecklist,
            pourInconnu, sansPour, sansConsigne, sansMinuteur,
        )

        assertTrue(ecartees.all { lireProgramme(programme(it)).cartes.isEmpty() })
    }

    // Le dernier critère est toujours « tu ne sais pas quoi faire → on s'arrête » : l'aidant n'improvise jamais.
    @Test
    fun `un critere d'arret unique ou sans porte de sortie ecarte la carte`() {
        val critereUnique = DUO.replace(
            """"Il fait le signal d'arrêt → on s'arrête, on ne demande rien.",
          "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser." """,
            """"Tu ne sais pas quoi faire → on s'arrête." """,
        )
        val sansPorteDeSortie = DUO.replace(
            "Tu ne sais pas quoi faire → on s'arrête. Ne jamais improviser.",
            "On continue.",
        )

        assertTrue(lireProgramme(programme(critereUnique)).cartes.isEmpty())
        assertTrue(lireProgramme(programme(sansPorteDeSortie)).cartes.isEmpty())
    }

    // Les sept familles d'interdits se réappliquent à la lecture : Kokoro écarte la seule carte fautive.
    @Test
    fun `une consigne interdite ecarte la carte a deux`() {
        val consigneInterdite = DUO.replace("Pose les deux pieds à plat.", "Visualise un lieu sûr.")
        val avantInterdit = DUO.replace(
            "Le téléphone reste dans tes mains du début à la fin.",
            "Détends-toi avant de commencer.",
        )

        assertTrue(lireProgramme(programme(consigneInterdite)).cartes.isEmpty())
        assertTrue(lireProgramme(programme(avantInterdit)).cartes.isEmpty())
    }

    // Une séance à deux se refait : elle ne se coche pas, et un entraînement joué la dirait faite à tort.
    @Test
    fun `une carte a deux ne se grise jamais, et son entrainement se retient a part`() {
        val carte = duo()
        val rendue = Faites(
            jour = "2026-08-19",
            reponses = listOf("2026-08-19-2104-essai-a-deux.json"),
            entrainements = setOf("essai-a-deux"),
        )

        assertFalse(rendue.faite(carte))
        assertTrue(rendue.entrainementMene(carte))
        assertFalse(AUCUNE_FAITE.entrainementMene(carte))
    }
}
