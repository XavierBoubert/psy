package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private fun programme(etapes: String): String =
    """{ "version": 7, "publie_le": "2026-08-18", "supervision": "essai", "etapes": [$etapes] }"""

private const val FICHE_PDF =
    """{ "id": "jour-de-vol", "titre": "Le jour du vol", "type": "fiche", "rubrique": "therapie",
        "quand": "sans_date", "document": "jour-de-vol" }"""

private const val FICHE_TEXTE =
    """{ "id": "mot-arret", "titre": "Le signal d'arrêt", "type": "fiche", "rubrique": "crise",
        "quand": "au_besoin", "texte": "Le mot convenu à froid suffit. Rien à dire de plus." }"""

class BibliothequeTest {

    @Test
    fun `lit la version et les deux formes de fiche`() {
        val bibliotheque = lireBibliotheque(programme("$FICHE_PDF, $FICHE_TEXTE"))

        assertEquals(7, bibliotheque.version)
        assertEquals(
            listOf(Support.Pdf("jour-de-vol"), Support.Texte("Le mot convenu à froid suffit. Rien à dire de plus.")),
            bibliotheque.fiches.map { it.support },
        )
        assertEquals(listOf(Quand.SANS_DATE, Quand.AU_BESOIN), bibliotheque.fiches.map { it.quand })
    }

    @Test
    fun `ne retient que les etapes de type fiche`() {
        val etape = """{ "id": "check-in", "titre": "Check-in du jour", "type": "ecran",
            "rubrique": "therapie", "quand": "aujourdhui", "ecran": "check-in" }"""

        assertEquals(listOf("jour-de-vol"), lireBibliotheque(programme("$etape, $FICHE_PDF")).fiches.map { it.id })
    }

    // PROGRAMME.md §7 : le PC refuse tout, Kokoro ecarte la seule fiche fautive et affiche le reste.
    @Test
    fun `ecarte la fiche fautive et garde les autres`() {
        val deuxSupports = """{ "id": "deux-supports", "titre": "Deux supports", "type": "fiche",
            "rubrique": "bilan", "quand": "au_besoin", "document": "deux-supports", "texte": "Aussi un texte." }"""
        val quandInconnu = """{ "id": "quand-inconnu", "titre": "Quand inconnu", "type": "fiche",
            "rubrique": "bilan", "quand": "demain", "document": "quand-inconnu" }"""
        val sansSupport = """{ "id": "sans-support", "titre": "Sans support", "type": "fiche",
            "rubrique": "bilan", "quand": "au_besoin" }"""

        val fiches = lireBibliotheque(programme("$deuxSupports, $quandInconnu, $sansSupport, $FICHE_PDF")).fiches

        assertEquals(listOf("jour-de-vol"), fiches.map { it.id })
    }

    @Test
    fun `ecarte une fiche dont le titre enfreint un interdit`() {
        val visualisation = """{ "id": "lieu-sur", "titre": "Visualise un lieu sûr", "type": "fiche",
            "rubrique": "therapie", "quand": "au_besoin", "document": "lieu-sur" }"""
        val cotation = """{ "id": "note-ton-anxiete", "titre": "Note ton anxiété sur 10", "type": "fiche",
            "rubrique": "bilan", "quand": "aujourdhui", "texte": "Une échelle de 0 à 10." }"""

        assertTrue(lireBibliotheque(programme("$visualisation, $cotation")).fiches.isEmpty())
    }

    @Test
    fun `un document hors kebab-case est ecarte`() {
        val chemin = """{ "id": "ailleurs", "titre": "Ailleurs", "type": "fiche", "rubrique": "bilan",
            "quand": "au_besoin", "document": "../secrets/dossier" }"""

        assertTrue(lireBibliotheque(programme(chemin)).fiches.isEmpty())
    }

    @Test
    fun `un programme illisible ne casse rien`() {
        assertEquals(BIBLIOTHEQUE_ABSENTE, lireBibliotheque("{ \"version\": 7, \"etapes\": ["))
        assertEquals(BIBLIOTHEQUE_ABSENTE, lireBibliotheque(""))
    }
}

class JsonTest {

    @Test
    fun `lit les types du contrat`() {
        val valeur = lireJson("""{ "n": 12, "t": "a\"b\n", "b": true, "v": null, "l": [1, { "x": 2 }] }""")

        assertEquals(12, valeur?.entier("n"))
        assertEquals("a\"b\n", valeur?.texte("t"))
        assertEquals(Valeur.Booleen(true), valeur?.champ("b"))
        assertEquals(Valeur.Vide, valeur?.champ("v"))
        assertEquals(2, valeur?.elements("l")?.get(1)?.entier("x"))
    }

    @Test
    fun `lit un echappement unicode et les blancs`() {
        val valeur = lireJson("\n  {\n\t\"cle\" :  \"\\u00e9t\\u00e9\"\n }\n")

        assertEquals("été", valeur?.texte("cle"))
    }

    @Test
    fun `refuse ce qui n'est pas du json complet`() {
        assertNull(lireJson("""{ "cle": }"""))
        assertNull(lireJson("""{ "cle": 1 } et une suite"""))
        assertNull(lireJson("""["ouvert", 1"""))
    }
}
