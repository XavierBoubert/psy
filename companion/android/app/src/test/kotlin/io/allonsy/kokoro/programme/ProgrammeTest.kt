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

private const val ECRAN =
    """{ "id": "check-in", "titre": "Check-in du jour", "type": "ecran", "rubrique": "therapie",
        "quand": "aujourdhui", "duree_minutes": 2, "ecran": "check-in" }"""

private const val EXERCICE =
    """{ "id": "ppc-palier-1", "titre": "Masque tenu à la main", "type": "exercice", "rubrique": "therapie",
        "quand": "aujourdhui", "duree_minutes": 5, "consigne": "Masque contre le visage, tenu à la main.",
        "minuteur_secondes": 300, "sortie_libre": true }"""

private const val QUESTIONNAIRE =
    """{ "id": "gad7", "titre": "Questionnaire GAD-7", "type": "questionnaire", "rubrique": "therapie",
        "quand": "sans_date", "duree_minutes": 5, "questions": [
          { "id": "q1", "enonce": "Combien de jours cette semaine as-tu quitté le logement ?", "choix": [
              { "valeur": 0, "libelle": "Aucun jour" },
              { "valeur": 3, "libelle": "Presque tous les jours" } ] },
          { "id": "q2", "enonce": "Combien de repas as-tu servis en une seule fois ?", "choix": [
              { "valeur": 0, "libelle": "Aucun" },
              { "valeur": 1, "libelle": "Un" },
              { "valeur": 2, "libelle": "Deux" } ] } ] }"""

private const val DEMARCHE =
    """{ "id": "ppc-releve", "titre": "Demander le relevé", "type": "demarche", "rubrique": "therapie",
        "quand": "sans_date", "detail": "Des chiffres, pas une impression." }"""

private const val BILAN_2024 =
    """{ "id": "evaluation-tsa", "titre": "Évaluation TSA", "type": "bilan", "rubrique": "bilan",
        "date": "2024-04-18", "document": "evaluation-tsa" }"""

private const val BILAN_2026 =
    """{ "id": "vviq-2026-08", "titre": "VVIQ — imagerie mentale", "type": "bilan", "rubrique": "bilan",
        "date": "2026-08-09", "document": "vviq-2026-08" }"""

class ProgrammeTest {

    @Test
    fun `lit les six types portes par Kokoro`() {
        val lu = lireProgramme(programme("$ECRAN, $EXERCICE, $QUESTIONNAIRE, $DEMARCHE, $FICHE_PDF, $BILAN_2026"))

        assertEquals(7, lu.version)
        assertEquals(
            listOf(
                Etape.Ecran::class,
                Etape.Exercice::class,
                Etape.Questionnaire::class,
                Etape.Demarche::class,
                Etape.Fiche::class,
                Etape.Bilan::class,
            ),
            lu.etapes.map { it::class },
        )
    }

    @Test
    fun `le questionnaire porte ses items et leurs choix fermes`() {
        val questionnaire = lireProgramme(programme(QUESTIONNAIRE)).etapes.single() as Etape.Questionnaire

        assertEquals(listOf("q1", "q2"), questionnaire.questions.map { it.id })
        assertEquals(listOf(0, 3), questionnaire.questions.first().choix.map { it.valeur })
        assertEquals(listOf("Aucun", "Un", "Deux"), questionnaire.questions.last().choix.map { it.libelle })
    }

    // Un item perdu produirait un score faux, donc faussement rassurant : le questionnaire tombe entier.
    @Test
    fun `un questionnaire ampute tombe entier plutot que partiellement`() {
        val sansQuestion = """{ "id": "sans-question", "titre": "Sans question", "type": "questionnaire",
            "rubrique": "bilan", "quand": "sans_date", "questions": [] }"""
        val choixUnique = """{ "id": "choix-unique", "titre": "Choix unique", "type": "questionnaire",
            "rubrique": "bilan", "quand": "sans_date", "questions": [
              { "id": "q1", "enonce": "Une question ?", "choix": [ { "valeur": 0, "libelle": "Oui" } ] } ] }"""
        val enonceInterdit = """{ "id": "enonce-interdit", "titre": "Énoncé interdit", "type": "questionnaire",
            "rubrique": "bilan", "quand": "sans_date", "questions": [
              { "id": "q1", "enonce": "Visualise un lieu sûr.", "choix": [
                  { "valeur": 0, "libelle": "Oui" }, { "valeur": 1, "libelle": "Non" } ] },
              { "id": "q2", "enonce": "Une question ?", "choix": [
                  { "valeur": 0, "libelle": "Oui" }, { "valeur": 1, "libelle": "Non" } ] } ] }"""
        val idEnDouble = """{ "id": "id-en-double", "titre": "Id en double", "type": "questionnaire",
            "rubrique": "bilan", "quand": "sans_date", "questions": [
              { "id": "q1", "enonce": "Une question ?", "choix": [
                  { "valeur": 0, "libelle": "Oui" }, { "valeur": 1, "libelle": "Non" } ] },
              { "id": "q1", "enonce": "Une autre ?", "choix": [
                  { "valeur": 0, "libelle": "Oui" }, { "valeur": 1, "libelle": "Non" } ] } ] }"""

        val lu = lireProgramme(programme("$sansQuestion, $choixUnique, $enonceInterdit, $idEnDouble"))

        assertTrue(lu.etapes.isEmpty())
    }

    // PROGRAMME.md §3 : l'écran Bilan ne porte que des bilans, du mois le plus récent au plus ancien.
    @Test
    fun `les bilans se rangent par date decroissante, hors des etapes qui font agir`() {
        val lu = lireProgramme(programme("$BILAN_2024, $BILAN_2026, $DEMARCHE"))

        assertEquals(listOf("vviq-2026-08", "evaluation-tsa"), lu.bilans().map { it.id })
        assertEquals(listOf("2026-08", "2024-04"), lu.bilans().map { moisDe(it.date) })
        assertEquals(listOf("ppc-releve"), lu.etapesDe(Rubrique.THERAPIE).map { it.id })
        assertTrue(lu.etapesDe(Rubrique.BILAN).isEmpty())
    }

    @Test
    fun `le bilan porte son document et sa date`() {
        val bilan = lireProgramme(programme(BILAN_2026)).bilans().single()

        assertEquals("vviq-2026-08", bilan.document)
        assertEquals("2026-08-09", bilan.date)
        assertNull(bilan.reperes.quand)
    }

    // La rubrique bilan est réservée au type bilan : rangée là, une autre étape n'aurait pas de place à l'écran.
    @Test
    fun `la rubrique bilan et le type bilan ne vont pas l'un sans l'autre`() {
        val questionnaireRange = QUESTIONNAIRE.replace(""""rubrique": "therapie"""", """"rubrique": "bilan"""")
        val bilanAilleurs = BILAN_2026.replace(""""rubrique": "bilan"""", """"rubrique": "therapie"""")

        assertTrue(lireProgramme(programme("$questionnaireRange, $bilanAilleurs")).etapes.isEmpty())
    }

    @Test
    fun `un bilan date, dessine ou partageable autrement est ecarte`() {
        val sansDate = BILAN_2026.replace(""""date": "2026-08-09", """, "")
        val dateFloue = BILAN_2026.replace("2026-08-09", "août 2026")
        val avecQuand = BILAN_2026.replace(""""date":""", """"quand": "sans_date", "date":""")
        val avecTexte = BILAN_2026.replace(""""document":""", """"texte": "Le bilan tient ici.", "document":""")
        val montrable = BILAN_2026.replace(""""document":""", """"montrable": true, "document":""")

        assertTrue(
            lireProgramme(programme("$sansDate, $dateFloue, $avecQuand, $avecTexte, $montrable")).etapes.isEmpty(),
        )
    }

    @Test
    fun `l'exercice porte sa consigne et son minuteur`() {
        val exercice = lireProgramme(programme(EXERCICE)).etapes.single() as Etape.Exercice

        assertEquals(300, exercice.minuteurSecondes)
        assertEquals(5, exercice.dureeMinutes)
        assertEquals("Masque contre le visage, tenu à la main.", exercice.consigne)
    }

    @Test
    fun `un exercice sans minuteur exploitable est ecarte`() {
        val sansMinuteur = """{ "id": "sans-minuteur", "titre": "Sans minuteur", "type": "exercice",
            "rubrique": "therapie", "quand": "aujourdhui", "consigne": "Une consigne." }"""
        val minuteurNul = """{ "id": "minuteur-nul", "titre": "Minuteur nul", "type": "exercice",
            "rubrique": "therapie", "quand": "aujourdhui", "consigne": "Une consigne.", "minuteur_secondes": 0 }"""

        assertTrue(lireProgramme(programme("$sansMinuteur, $minuteurNul")).etapes.isEmpty())
    }

    @Test
    fun `un ecran inconnu est ecarte plutot qu'affiche mort`() {
        val inconnu = """{ "id": "ailleurs", "titre": "Ailleurs", "type": "ecran", "rubrique": "therapie",
            "quand": "aujourdhui", "ecran": "respiration" }"""

        assertEquals(listOf("check-in"), lireProgramme(programme("$inconnu, $ECRAN")).etapes.map { it.id })
    }

    // PROGRAMME.md §7 : le PC refuse tout, Kokoro écarte la seule étape fautive et affiche le reste.
    @Test
    fun `ecarte l'etape dont un texte affiche enfreint un interdit`() {
        val titre = """{ "id": "lieu-sur", "titre": "Visualise un lieu sûr", "type": "fiche",
            "rubrique": "therapie", "quand": "au_besoin", "document": "lieu-sur" }"""
        val consigne = """{ "id": "note-anxiete", "titre": "Un exercice", "type": "exercice",
            "rubrique": "therapie", "quand": "aujourdhui", "consigne": "Note ton anxiété sur 10.",
            "minuteur_secondes": 60 }"""
        val detail = """{ "id": "detente", "titre": "Une démarche", "type": "demarche",
            "rubrique": "therapie", "quand": "sans_date", "detail": "Détends-toi avant d'appeler." }"""

        assertTrue(lireProgramme(programme("$titre, $consigne, $detail")).etapes.isEmpty())
    }

    @Test
    fun `ecarte l'etape dont les reperes sont hors contrat`() {
        val idHorsKebab = """{ "id": "../secrets", "titre": "Ailleurs", "type": "demarche",
            "rubrique": "therapie", "quand": "sans_date", "detail": "Un détail." }"""
        val quandInconnu = """{ "id": "quand-inconnu", "titre": "Quand inconnu", "type": "demarche",
            "rubrique": "therapie", "quand": "demain", "detail": "Un détail." }"""
        val rubriqueInconnue = """{ "id": "rubrique-inconnue", "titre": "Rubrique inconnue", "type": "demarche",
            "rubrique": "sommeil", "quand": "sans_date", "detail": "Un détail." }"""

        assertTrue(lireProgramme(programme("$idHorsKebab, $quandInconnu, $rubriqueInconnue")).etapes.isEmpty())
    }

    @Test
    fun `les deux formes de fiche sont lues et rangees hors des etapes qui font agir`() {
        val lu = lireProgramme(programme("$FICHE_PDF, $FICHE_TEXTE, $DEMARCHE"))

        assertEquals(
            listOf(Support.Pdf("jour-de-vol"), Support.Texte("Le mot convenu à froid suffit. Rien à dire de plus.")),
            lu.fiches().map { it.support },
        )
        assertEquals(listOf("ppc-releve"), lu.etapesDe(Rubrique.THERAPIE).map { it.id })
    }

    @Test
    fun `une fiche a deux supports ou a document hors kebab-case est ecartee`() {
        val deuxSupports = """{ "id": "deux-supports", "titre": "Deux supports", "type": "fiche",
            "rubrique": "documentation", "quand": "au_besoin", "document": "deux-supports", "texte": "Aussi un texte." }"""
        val chemin = """{ "id": "ailleurs", "titre": "Ailleurs", "type": "fiche", "rubrique": "documentation",
            "quand": "au_besoin", "document": "../secrets/dossier" }"""

        assertTrue(lireProgramme(programme("$deuxSupports, $chemin")).fiches().isEmpty())
    }

    // seance-duo n'est pas porté : il tombe dans le cas général plutôt que de casser la lecture.
    @Test
    fun `un type non encore porte n'empeche pas le reste d'apparaitre`() {
        val duo = """{ "id": "stab-ancrage-1", "titre": "Ancrage à deux", "type": "seance-duo",
            "rubrique": "therapie", "quand": "sans_date", "sequence": [] }"""

        assertEquals(listOf("ppc-releve"), lireProgramme(programme("$duo, $DEMARCHE")).etapes.map { it.id })
    }

    @Test
    fun `un programme illisible ne casse rien`() {
        assertEquals(PROGRAMME_ABSENT, lireProgramme("{ \"version\": 7, \"etapes\": ["))
        assertEquals(PROGRAMME_ABSENT, lireProgramme(""))
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
