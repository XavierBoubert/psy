package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val LE_18_AOUT = OffsetDateTime.of(2026, 8, 18, 18, 4, 37, 0, ZoneOffset.ofHours(2))

private fun carte(id: String, quand: Quand): Carte.Panneau =
    Carte.Panneau(
        reperes = Reperes(id, "Un titre", Rubrique.THERAPIE, quand, null),
        etapes = listOf(Etape.Confirmation("C'est fait")),
    )

class ReponseTest {

    // psy-sync refuse un fichier dont le nom ne se termine pas par la carte qu'il déclare.
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

        assertTrue(ecrit.contains(""""carte": "ppc-releve""""))
        assertTrue(ecrit.contains(""""horodatage": "2026-08-18T18:04:37+02:00""""))
        assertTrue(ecrit.contains(""""issue": "fait""""))
        assertTrue(ecrit.contains(""""reponses": null"""))
        assertTrue(ecrit.contains(""""source": "android""""))
    }

    // « Passer » écrit null, jamais 0 : la cotation doit pouvoir distinguer un item passé d'un item coté au plancher.
    @Test
    fun `une passation renvoie ses items dans l'ordre, un item passe valant null`() {
        val items = listOf(
            ReponseItem.Nombre("q1", 2.0),
            ReponseItem.Nombre("q2", null),
            ReponseItem.Nombre("q3", 0.0),
        )
        val ecrit = serialiserReponse(reponseDe("gad7", Issue.TERMINE, LE_18_AOUT, items))

        assertTrue(ecrit.contains(""""carte": "gad7""""))
        assertTrue(ecrit.contains(""""issue": "termine""""))
        assertTrue(ecrit.contains("""{ "question": "q1", "valeur": 2 }"""))
        assertTrue(ecrit.contains("""{ "question": "q2", "valeur": null }"""))
        assertTrue(ecrit.contains("""{ "question": "q3", "valeur": 0 }"""))
    }

    // Un compte s'écrit sans décimale, une mesure la garde : le journal reconstruit au dépôt lit les deux.
    @Test
    fun `une mesure garde sa decimale, un compte n'en prend pas`() {
        val items = listOf(
            ReponseItem.Nombre("sommeil-heures", 7.5),
            ReponseItem.Nombre("shutdowns", 2.0),
            ReponseItem.Texte("notes", "Deux mots.\nUne \"citation\"."),
            ReponseItem.Texte("vide", null),
        )
        val ecrit = serialiserReponse(reponseDe("check-in", Issue.TERMINE, LE_18_AOUT, items))

        assertTrue(ecrit.contains("""{ "question": "sommeil-heures", "valeur": 7.5 }"""))
        assertTrue(ecrit.contains("""{ "question": "shutdowns", "valeur": 2 }"""))
        assertTrue(ecrit.contains("""{ "question": "notes", "texte": "Deux mots.\nUne \"citation\"." }"""))
        assertTrue(ecrit.contains("""{ "question": "vide", "texte": null }"""))
    }

    // Une passation arrêtée au milieu part telle quelle : les items non atteints sont absents, pas null.
    @Test
    fun `une passation arretee au milieu ne porte que les items atteints`() {
        val ecrit = serialiserReponse(
            reponseDe("gad7", Issue.ARRETE, LE_18_AOUT, listOf(ReponseItem.Nombre("q1", 1.0))),
        )

        assertTrue(ecrit.contains(""""issue": "arrete_avant_la_fin""""))
        assertTrue(ecrit.contains("""{ "question": "q1", "valeur": 1 }"""))
        assertFalse(ecrit.contains("q2"))
    }

    // La reprise relit ce que Kokoro a écrit : une note ne se reprend pas, un item passé non plus.
    @Test
    fun `la relecture d'une reponse ne rend que ses valeurs chiffrees`() {
        val items = listOf(
            ReponseItem.Nombre("poids-kg", 108.4),
            ReponseItem.Nombre("passe", null),
            ReponseItem.Texte("notes", "Rien à signaler."),
        )
        val relu = valeursDeLaReponse(serialiserReponse(reponseDe("check-in", Issue.TERMINE, LE_18_AOUT, items)))

        assertEquals(mapOf("poids-kg" to 108.4), relu)
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

    // Une carte du jour repart à zéro le lendemain : hier ne se compte pas, et ne se reproche pas non plus.
    @Test
    fun `une carte du jour ne reste pas faite le lendemain`() {
        assertFalse(faites.faite(carte("ppc-palier-1", Quand.AUJOURDHUI)))
    }

    @Test
    fun `une carte du jour rendue aujourd'hui est faite`() {
        val aujourdhui = faites.copy(reponses = faites.reponses + "2026-08-18-1900-ppc-palier-1.json")

        assertTrue(aujourdhui.faite(carte("ppc-palier-1", Quand.AUJOURDHUI)))
    }

    @Test
    fun `une demarche sans date reste faite jusqu'a ce que le psy la retire`() {
        assertTrue(faites.faite(carte("ppc-releve", Quand.SANS_DATE)))
    }

    @Test
    fun `ce qui sert au besoin ne se grise jamais`() {
        assertFalse(faites.faite(carte("mot-code", Quand.AU_BESOIN)))
    }

    @Test
    fun `un identifiant n'est jamais confondu avec un autre qui le prefixe`() {
        assertFalse(faites.faite(carte("ppc-palier", Quand.SANS_DATE)))
        assertFalse(faites.faite(carte("releve", Quand.SANS_DATE)))
    }

    @Test
    fun `un nom hors convention ne rend rien fait`() {
        val bruit = Faites(jour = "2026-08-18", reponses = listOf("ppc-releve.json", "brouillon.txt"))

        assertFalse(bruit.faite(carte("ppc-releve", Quand.SANS_DATE)))
    }
}
