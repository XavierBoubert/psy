package io.allonsy.kokoro.monde

import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Fonction
import io.allonsy.kokoro.programme.PROGRAMME_ABSENT
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.Quand
import io.allonsy.kokoro.programme.Reperes
import io.allonsy.kokoro.programme.Rubrique
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val JOUR = "2026-08-20"

private val CHECK_IN = Etape.Ecran(
    reperes = Reperes("check-in", "Le point du jour", Rubrique.THERAPIE, Quand.AUJOURDHUI, 2),
    fonction = Fonction.CHECK_IN,
)

private val PALIER = Etape.Exercice(
    reperes = Reperes("ppc-palier-1", "Le masque posé", Rubrique.THERAPIE, Quand.AUJOURDHUI, 20),
    consigne = "Une consigne.",
    minuteurSecondes = 1_200,
)

private val AU_BESOIN = Etape.Demarche(
    reperes = Reperes("ppc-releve", "Demander le relevé", Rubrique.THERAPIE, Quand.AU_BESOIN, null),
    detail = "Des chiffres, pas une impression.",
)

private val PROGRAMME = Programme(version = 2, etapes = listOf(CHECK_IN, PALIER, AU_BESOIN))

private fun faites(vararg noms: String) = Faites(jour = JOUR, reponses = noms.toList())

class JourneeTest {

    @Test
    fun `le check in ne se lit pas dans les reponses mais dans le sejour`() {
        val palierFait = faites("$JOUR-0900-ppc-palier-1.json")

        assertFalse(
            "Le check-in n'écrit aucune réponse : sans le séjour, il ne serait jamais compté",
            toutFaitAujourdhui(PROGRAMME, palierFait, checkinFait = false),
        )
        assertTrue(toutFaitAujourdhui(PROGRAMME, palierFait, checkinFait = true))
    }

    // La carte du check-in et le bras de Kokoro lisent la même règle : sans ça, l'un se grise et l'autre pas.
    @Test
    fun `la carte du check in se grise sur le sejour, celle d'une etape sur sa reponse`() {
        val palierFait = faites("$JOUR-0900-ppc-palier-1.json")

        assertTrue(rendue(CHECK_IN, faites(), checkinFait = true))
        assertFalse(rendue(CHECK_IN, palierFait, checkinFait = false))
        assertTrue(rendue(PALIER, palierFait, checkinFait = false))
        assertFalse(rendue(PALIER, faites(), checkinFait = true))
    }

    @Test
    fun `une etape du jour non rendue suffit a laisser la journee ouverte`() {
        assertFalse(toutFaitAujourdhui(PROGRAMME, faites(), checkinFait = true))
    }

    // « Quand j'en ai besoin » et « Sans date » ne se comptent jamais : il n'y a pas d'assiduité à mesurer dessus.
    @Test
    fun `seules les etapes d'aujourd'hui comptent`() {
        val sansJour = Programme(version = 2, etapes = listOf(AU_BESOIN))

        assertTrue(toutFaitAujourdhui(sansJour, faites(), checkinFait = false))
        assertTrue(toutFaitAujourdhui(PROGRAMME_ABSENT, faites(), checkinFait = false))
    }

    @Test
    fun `une reponse d'hier ne ferme pas la journee`() {
        assertFalse(
            toutFaitAujourdhui(PROGRAMME, faites("2026-08-19-0900-ppc-palier-1.json"), checkinFait = true),
        )
    }
}
