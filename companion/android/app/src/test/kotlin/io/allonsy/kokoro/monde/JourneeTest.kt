package io.allonsy.kokoro.monde

import io.allonsy.kokoro.programme.Carte
import io.allonsy.kokoro.programme.Choix
import io.allonsy.kokoro.programme.Etape
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.PROGRAMME_ABSENT
import io.allonsy.kokoro.programme.Porteur
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.Quand
import io.allonsy.kokoro.programme.Reperes
import io.allonsy.kokoro.programme.Saisie
import io.allonsy.kokoro.programme.Rubrique
import io.allonsy.kokoro.programme.faite
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val JOUR = "2026-08-20"

private val CHECK_IN = Carte.Panneau(
    reperes = Reperes("check-in", "Le point du jour", Rubrique.THERAPIE, Quand.AUJOURDHUI, 2),
    etapes = listOf(
        Etape.Question(
            id = "shutdowns",
            enonce = "Combien de fois ?",
            precision = null,
            saisie = Saisie.Fermee(listOf(Choix(0.0, "Aucune"), Choix(1.0, "Une"))),
        ),
    ),
)

private val PALIER = Carte.Panneau(
    reperes = Reperes("ppc-palier-1", "Le masque posé", Rubrique.THERAPIE, Quand.AUJOURDHUI, 20),
    etapes = listOf(Etape.Minuteur(secondes = 1_200, consigne = "Une consigne.", pour = null)),
)

private val AU_BESOIN = Carte.Panneau(
    reperes = Reperes("ppc-releve", "Demander le relevé", Rubrique.THERAPIE, Quand.AU_BESOIN, null),
    etapes = listOf(Etape.Confirmation("C'est fait")),
)

private val A_DEUX = Carte.Panneau(
    reperes = Reperes("stab-ancrage", "Ancrage à deux", Rubrique.THERAPIE, Quand.SANS_DATE, 22),
    etapes = listOf(Etape.Minuteur(secondes = 60, consigne = "Une consigne.", pour = Porteur.AIDANT)),
    porteur = Porteur.AIDANT,
)

private val PROGRAMME = Programme(version = 2, cartes = listOf(CHECK_IN, PALIER, AU_BESOIN))

private fun faites(vararg noms: String) = Faites(jour = JOUR, reponses = noms.toList())

class JourneeTest {

    // Le check-in est une carte comme les autres : son état se lit dans ses réponses, plus dans un état à part.
    @Test
    fun `une carte du jour se grise sur sa propre reponse`() {
        val palierFait = faites("$JOUR-0900-ppc-palier-1.json")

        assertTrue(palierFait.faite(PALIER))
        assertFalse(palierFait.faite(CHECK_IN))
        assertTrue(faites("$JOUR-0800-check-in.json").faite(CHECK_IN))
    }

    @Test
    fun `une carte du jour non rendue suffit a laisser la journee ouverte`() {
        assertFalse(toutFaitAujourdhui(PROGRAMME, faites("$JOUR-0900-ppc-palier-1.json")))
        assertTrue(
            toutFaitAujourdhui(
                PROGRAMME,
                faites("$JOUR-0900-ppc-palier-1.json", "$JOUR-0800-check-in.json"),
            ),
        )
    }

    // « Quand j'en ai besoin » et « Sans date » ne se comptent jamais : il n'y a pas d'assiduité à mesurer dessus.
    @Test
    fun `seules les cartes d'aujourd'hui comptent`() {
        val sansJour = Programme(version = 2, cartes = listOf(AU_BESOIN))

        assertTrue(toutFaitAujourdhui(sansJour, faites()))
        assertTrue(toutFaitAujourdhui(PROGRAMME_ABSENT, faites()))
    }

    @Test
    fun `une reponse d'hier ne ferme pas la journee`() {
        assertFalse(toutFaitAujourdhui(PROGRAMME, faites("2026-08-19-0900-ppc-palier-1.json")))
    }

    // Une carte tenue par l'aidant se refait : la cocher ferait passer un déroulé rejoué pour un acquis.
    @Test
    fun `une carte tenue par l'aidant ne se coche jamais`() {
        assertFalse(faites("$JOUR-1000-stab-ancrage.json").faite(A_DEUX))
    }
}
