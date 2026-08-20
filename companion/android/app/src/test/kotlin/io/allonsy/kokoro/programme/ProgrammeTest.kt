package io.allonsy.kokoro.programme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private fun programme(cartes: String): String =
    """{ "version": 12, "publie_le": "2026-08-20", "supervision": "essai", "cartes": [$cartes] }"""

private const val FICHE =
    """{ "id": "jour-de-vol", "titre": "Le jour du vol", "type": "pdf", "rubrique": "therapie",
        "quand": "sans_date", "document": "jour-de-vol" }"""

private const val LECTURE =
    """{ "id": "mot-arret", "titre": "Le signal d'arrêt", "type": "panneau", "rubrique": "crise",
        "quand": "au_besoin", "sortie_libre": true, "etapes": [
          { "type": "info", "texte": "Le mot convenu à froid suffit. Rien à dire de plus.", "montrable": true } ] }"""

private const val EXERCICE =
    """{ "id": "ppc-palier-1", "titre": "Masque tenu à la main", "type": "panneau", "rubrique": "therapie",
        "quand": "aujourdhui", "duree_minutes": 5, "sortie_libre": true, "etapes": [
          { "type": "info", "texte": "Masque contre le visage, tenu à la main." },
          { "type": "minuteur", "secondes": 300 } ] }"""

private const val QUESTIONNAIRE =
    """{ "id": "gad7", "titre": "Questionnaire GAD-7", "type": "panneau", "rubrique": "therapie",
        "quand": "sans_date", "duree_minutes": 5, "sortie_libre": true, "etapes": [
          { "type": "question", "id": "q1", "enonce": "Combien de jours as-tu quitté le logement ?", "choix": [
              { "valeur": 0, "libelle": "Aucun jour" },
              { "valeur": 3, "libelle": "Presque tous les jours" } ] },
          { "type": "question", "id": "q2", "enonce": "Combien de repas servis en une fois ?", "choix": [
              { "valeur": 0, "libelle": "Aucun" },
              { "valeur": 1, "libelle": "Un" },
              { "valeur": 2, "libelle": "Deux" } ] } ] }"""

private const val CHECK_IN =
    """{ "id": "check-in", "titre": "Check-in du jour", "type": "panneau", "rubrique": "therapie",
        "quand": "aujourdhui", "duree_minutes": 2, "sortie_libre": true, "etapes": [
          { "type": "question", "id": "sommeil-heures", "enonce": "Combien d'heures de sommeil ?",
            "compteur": { "depart": 7, "pas": 0.5, "grand_pas": 1, "minimum": 0, "unite": "heures" } },
          { "type": "question", "id": "poids-kg", "enonce": "Poids du jour ?", "reprise": true,
            "precision": "Une fois par semaine. Les autres jours : passer, sans commentaire.",
            "compteur": { "depart": 110, "pas": 0.1, "grand_pas": 1, "minimum": 0, "unite": "kilos" } },
          { "type": "note", "id": "notes", "enonce": "Quelque chose à ajouter ?" } ] }"""

private const val DEMARCHE =
    """{ "id": "ppc-releve", "titre": "Demander le relevé", "type": "panneau", "rubrique": "therapie",
        "quand": "sans_date", "sortie_libre": true, "etapes": [
          { "type": "info", "texte": "Des chiffres, pas une impression." },
          { "type": "confirmation", "libelle": "C'est fait" } ] }"""

private const val BILAN_2024 =
    """{ "id": "evaluation-tsa", "titre": "Évaluation TSA", "type": "pdf", "rubrique": "bilan",
        "date": "2024-04-18", "document": "evaluation-tsa" }"""

private const val BILAN_2026 =
    """{ "id": "vviq-2026-08", "titre": "VVIQ — imagerie mentale", "type": "pdf", "rubrique": "bilan",
        "date": "2026-08-09", "document": "vviq-2026-08" }"""

class ProgrammeTest {

    @Test
    fun `lit les deux types de carte`() {
        val lu = lireProgramme(programme("$EXERCICE, $FICHE, $BILAN_2026"))

        assertEquals(12, lu.version)
        assertEquals(
            listOf(Carte.Panneau::class, Carte.Pdf::class, Carte.Pdf::class),
            lu.cartes.map { it::class },
        )
    }

    @Test
    fun `un type de carte inconnu est ecarte plutot qu'affiche mort`() {
        val inconnu = """{ "id": "ailleurs", "titre": "Ailleurs", "type": "video", "rubrique": "therapie",
            "quand": "aujourdhui" }"""

        assertEquals(listOf("ppc-releve"), lireProgramme(programme("$inconnu, $DEMARCHE")).cartes.map { it.id })
    }

    @Test
    fun `l'exercice devient une info puis un minuteur`() {
        val carte = lireProgramme(programme(EXERCICE)).cartes.single() as Carte.Panneau

        assertEquals(5, carte.dureeMinutes)
        assertEquals(
            listOf(
                Etape.Info("Masque contre le visage, tenu à la main."),
                Etape.Minuteur(secondes = 300, consigne = null, pour = null),
            ),
            carte.etapes,
        )
    }

    @Test
    fun `le questionnaire porte ses items et leurs choix fermes`() {
        val carte = lireProgramme(programme(QUESTIONNAIRE)).cartes.single() as Carte.Panneau
        val questions = carte.etapes.filterIsInstance<Etape.Question>()

        assertEquals(listOf("q1", "q2"), questions.map { it.id })
        assertEquals(listOf(0.0, 3.0), choixDe(questions.first()).map { it.valeur })
        assertEquals(listOf("Aucun", "Un", "Deux"), choixDe(questions.last()).map { it.libelle })
    }

    @Test
    fun `une question au compteur porte son pas, son unite et sa reprise`() {
        val carte = lireProgramme(programme(CHECK_IN)).cartes.single() as Carte.Panneau
        val questions = carte.etapes.filterIsInstance<Etape.Question>()

        assertEquals(
            Compteur(depart = 7.0, pas = 0.5, grandPas = 1.0, minimum = 0.0, unite = Unite.HEURES),
            (questions.first().saisie as Saisie.Reglee).compteur,
        )
        assertTrue(questions.last().reprise)
        assertEquals(Etape.Note("notes", "Quelque chose à ajouter ?", null), carte.etapes.last())
    }

    // Un item perdu produirait un score faux, donc faussement rassurant : la carte tombe entière.
    @Test
    fun `une carte a l'etape amputee tombe entiere plutot que partiellement`() {
        val sansEtape = """{ "id": "sans-etape", "titre": "Sans étape", "type": "panneau",
            "rubrique": "therapie", "quand": "sans_date", "sortie_libre": true, "etapes": [] }"""
        val choixUnique = """{ "id": "choix-unique", "titre": "Choix unique", "type": "panneau",
            "rubrique": "therapie", "quand": "sans_date", "sortie_libre": true, "etapes": [
              { "type": "question", "id": "q1", "enonce": "Une question ?", "choix": [
                  { "valeur": 0, "libelle": "Oui" } ] } ] }"""
        val enonceInterdit = """{ "id": "enonce-interdit", "titre": "Énoncé interdit", "type": "panneau",
            "rubrique": "therapie", "quand": "sans_date", "sortie_libre": true, "etapes": [
              { "type": "question", "id": "q1", "enonce": "Visualise un lieu sûr.", "choix": [
                  { "valeur": 0, "libelle": "Oui" }, { "valeur": 1, "libelle": "Non" } ] } ] }"""
        val compteurEtChoix = """{ "id": "deux-saisies", "titre": "Deux saisies", "type": "panneau",
            "rubrique": "therapie", "quand": "sans_date", "sortie_libre": true, "etapes": [
              { "type": "question", "id": "q1", "enonce": "Une question ?",
                "compteur": { "depart": 0, "pas": 1, "grand_pas": 5, "minimum": 0, "unite": "brute" },
                "choix": [ { "valeur": 0, "libelle": "Oui" }, { "valeur": 1, "libelle": "Non" } ] } ] }"""

        val lu = lireProgramme(programme("$sansEtape, $choixUnique, $enonceInterdit, $compteurEtChoix"))

        assertTrue(lu.cartes.isEmpty())
    }

    // ⭐ Le champ existe pour que ce soit écrit, pas pour être mis à false.
    @Test
    fun `un panneau sans sortie libre ecrite est ecarte`() {
        val sansSortie = EXERCICE.replace(""""sortie_libre": true, """, "")

        assertTrue(lireProgramme(programme(sansSortie)).cartes.isEmpty())
    }

    // PROGRAMME.md §4 : l'écran Bilan ne porte que des bilans, du mois le plus récent au plus ancien.
    @Test
    fun `les bilans se rangent par date decroissante, hors des cartes qui font agir`() {
        val lu = lireProgramme(programme("$BILAN_2024, $BILAN_2026, $DEMARCHE, $FICHE"))

        assertEquals(listOf("vviq-2026-08", "evaluation-tsa"), lu.bilans().map { it.id })
        assertEquals(listOf("2026-08", "2024-04"), lu.bilans().map { moisDe(it.date.orEmpty()) })
        assertEquals(listOf("jour-de-vol"), lu.documents().map { it.id })
        assertEquals(listOf("ppc-releve"), lu.cartesDe(Rubrique.THERAPIE).map { it.id })
        assertTrue(lu.cartesDe(Rubrique.BILAN).isEmpty())
    }

    @Test
    fun `le bilan porte son document et sa date`() {
        val bilan = lireProgramme(programme(BILAN_2026)).bilans().single()

        assertEquals("vviq-2026-08", bilan.document)
        assertEquals("2026-08-09", bilan.date)
        assertNull(bilan.reperes.quand)
    }

    // La rubrique bilan est réservée aux cartes du dossier Bilan : rangée là, une autre n'aurait pas de place à l'écran.
    @Test
    fun `la rubrique bilan et la date ne vont pas l'une sans l'autre`() {
        val panneauRange = EXERCICE.replace(""""rubrique": "therapie"""", """"rubrique": "bilan"""")
        val bilanAilleurs = BILAN_2026.replace(""""rubrique": "bilan"""", """"rubrique": "therapie"""")
        val sansDate = BILAN_2026.replace(""""date": "2026-08-09", """, "")
        val dateFloue = BILAN_2026.replace("2026-08-09", "août 2026")
        val avecQuand = BILAN_2026.replace(""""date":""", """"quand": "sans_date", "date":""")

        assertTrue(
            lireProgramme(
                programme("$panneauRange, $bilanAilleurs, $sansDate, $dateFloue, $avecQuand"),
            ).cartes.isEmpty(),
        )
    }

    @Test
    fun `un minuteur sans duree exploitable emporte sa carte`() {
        val sansMinuteur = """{ "id": "sans-minuteur", "titre": "Sans minuteur", "type": "panneau",
            "rubrique": "therapie", "quand": "aujourdhui", "sortie_libre": true, "etapes": [
              { "type": "minuteur" } ] }"""
        val minuteurNul = """{ "id": "minuteur-nul", "titre": "Minuteur nul", "type": "panneau",
            "rubrique": "therapie", "quand": "aujourdhui", "sortie_libre": true, "etapes": [
              { "type": "minuteur", "secondes": 0 } ] }"""

        assertTrue(lireProgramme(programme("$sansMinuteur, $minuteurNul")).cartes.isEmpty())
    }

    // PROGRAMME.md §8 : le PC refuse tout, Kokoro écarte la seule carte fautive et affiche le reste.
    @Test
    fun `ecarte la carte dont un texte affiche enfreint un interdit`() {
        val titre = """{ "id": "lieu-sur", "titre": "Visualise un lieu sûr", "type": "pdf",
            "rubrique": "therapie", "quand": "au_besoin", "document": "lieu-sur" }"""
        val consigne = """{ "id": "note-anxiete", "titre": "Un exercice", "type": "panneau",
            "rubrique": "therapie", "quand": "aujourdhui", "sortie_libre": true, "etapes": [
              { "type": "minuteur", "secondes": 60, "consigne": "Note ton anxiété sur 10." } ] }"""
        val detail = """{ "id": "detente", "titre": "Une démarche", "type": "panneau",
            "rubrique": "therapie", "quand": "sans_date", "sortie_libre": true, "etapes": [
              { "type": "info", "texte": "Détends-toi avant d'appeler." },
              { "type": "confirmation", "libelle": "C'est fait" } ] }"""

        assertTrue(lireProgramme(programme("$titre, $consigne, $detail")).cartes.isEmpty())
    }

    @Test
    fun `ecarte la carte dont les reperes sont hors contrat`() {
        val idHorsKebab = DEMARCHE.replace(""""id": "ppc-releve"""", """"id": "../secrets"""")
        val quandInconnu = DEMARCHE.replace(""""quand": "sans_date"""", """"quand": "demain"""")
        val rubriqueInconnue = DEMARCHE.replace(""""rubrique": "therapie"""", """"rubrique": "sommeil"""")

        assertTrue(lireProgramme(programme("$idHorsKebab, $quandInconnu, $rubriqueInconnue")).cartes.isEmpty())
    }

    // 🔴 Une carte pdf vit sur Documentation quelle que soit sa rubrique ; elle ne fait pas agir.
    @Test
    fun `un document hors kebab-case est ecarte`() {
        val chemin = FICHE.replace(""""document": "jour-de-vol"""", """"document": "../secrets/dossier"""")

        assertTrue(lireProgramme(programme(chemin)).documents().isEmpty())
    }

    @Test
    fun `une info montrable garde sa marque`() {
        val carte = lireProgramme(programme(LECTURE)).cartes.single() as Carte.Panneau

        assertEquals(
            Etape.Info("Le mot convenu à froid suffit. Rien à dire de plus.", montrable = true),
            carte.etapes.single(),
        )
    }

    @Test
    fun `un programme illisible ne casse rien`() {
        assertEquals(PROGRAMME_ABSENT, lireProgramme("{ \"version\": 7, \"cartes\": ["))
        assertEquals(PROGRAMME_ABSENT, lireProgramme(""))
    }
}

private fun choixDe(question: Etape.Question): List<Choix> = (question.saisie as Saisie.Fermee).choix

class JsonTest {

    @Test
    fun `lit les types du contrat`() {
        val valeur = lireJson("""{ "n": 12, "d": 0.5, "t": "a\"b\n", "b": true, "v": null, "l": [1, { "x": 2 }] }""")

        assertEquals(12, valeur?.entier("n"))
        assertEquals(0.5, valeur?.nombre("d"))
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
