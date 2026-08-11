package io.allonsy.kokoro.journal

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val ETAT = File("../../dossier/etat.md")

private val NOYAU_DU_SCHEMA = listOf(
    "shutdowns",
    "exposition_sociale",
    "retrait_sensoriel",
    "renoncements",
    "activites_investies",
    "sommeil_heures",
    "missions_actives",
)

private val RESSENTIS = listOf(
    "anxiete", "anxiété", "humeur", "moral", "stress", "fatigue", "douleur",
    "tension_ressentie", "bien_etre", "energie",
)

class QuestionsTest {

    @Test
    fun `le noyau porte les sept champs du schema, dans l ordre`() {
        val noyau = Champ.entries.filter { it.section == Section.NOYAU }.map { it.cle }

        assertEquals(NOYAU_DU_SCHEMA, noyau)
    }

    @Test
    fun `aucun champ ne cote un ressenti`() {
        val fautifs = Champ.entries.filter { champ -> RESSENTIS.any { champ.cle.contains(it) } }

        assertTrue("Règle R6 — on cote des comportements observables : $fautifs", fautifs.isEmpty())
    }

    @Test
    fun `les champs de campagne sont ceux declares dans etat`() {
        assertTrue("État introuvable : ${ETAT.absolutePath}", ETAT.isFile)
        val etat = ETAT.readText()
        val campagne = Champ.entries.filter { it.section == Section.CAMPAGNE }

        val absents = campagne.filterNot { etat.contains("`${it.cle}`") }
        assertTrue("Champs de campagne non déclarés dans etat.md §4 : $absents", absents.isEmpty())
    }

    @Test
    fun `chaque champ a une question, et une seule`() {
        assertEquals(Champ.entries.toList(), QUESTIONS.map { it.champ })
    }

    @Test
    fun `aucune question obligatoire ne demande d ecrire`() {
        QUESTIONS.forEach { question ->
            when (val saisie = question.saisie) {
                is Saisie.Choix -> assertTrue(
                    "Un choix fermé a au moins deux options : ${question.champ}",
                    saisie.options.size >= 2,
                )
                is Saisie.Compteur -> assertTrue(
                    "Un compteur a des pas utilisables : ${question.champ}",
                    saisie.pas > 0.0 && saisie.grandPas >= saisie.pas,
                )
            }
        }
    }

    @Test
    fun `seuls le nombre de missions et le poids se reprennent du dernier checkin`() {
        assertEquals(listOf(Champ.MISSIONS_ACTIVES, Champ.POIDS_KG), CHAMPS_REPRIS)
    }
}
