package io.allonsy.kokoro.corps

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.monde.HAUTEUR_HABITANT
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val DENSITE = 3f

private val HAUTEUR_REFUSEE = 48.dp

class LocuteurTest {

    private fun contourRendu(hauteurPersonnage: Dp): Float =
        (unitePour(hauteurPersonnage) * EPAISSEUR_CONTOUR).value * DENSITE

    @Test
    fun `jamais deux Kokoro a l'ecran, et jamais aucun`() {
        (0..100).map { it / 100f }.forEach { sortie ->
            assertTrue(
                "À $sortie, l'habitant et le locuteur sont tous les deux là",
                !(habitantEnScene(sortie) && locuteurEnScene(sortie)),
            )
            assertTrue(
                "À $sortie, il n'y a plus personne",
                habitantEnScene(sortie) || locuteurEnScene(sortie),
            )
        }
    }

    @Test
    fun `le locuteur attend que l'habitant soit dehors`() {
        assertTrue("À l'arrêt, c'est l'habitant qui est là", habitantEnScene(0f))
        assertTrue("En pleine sortie, c'est encore lui", habitantEnScene(0.99f))
        assertTrue("Le locuteur n'entre pas avant la fin", !locuteurEnScene(0.99f))
        assertTrue("Sorti, il laisse la place", locuteurEnScene(1f))
    }

    @Test
    fun `le cadrage coupe au thorax et garde la tete entiere`() {
        assertEquals("La coupe est le centre du ventre", CENTRE_VENTRE.y, COUPE_LOCUTEUR, 0f)
        assertTrue("La coupe doit passer sous les épaules", COUPE_LOCUTEUR > EPAULE_GAUCHE.y)
        assertTrue("Elle ne descend pas jusqu'au sol", COUPE_LOCUTEUR < BAS_PIEDS)
        assertTrue("Le sommet du crâne est dans le cadre", SOMMET_TETE > 0f)
        assertTrue(
            "La bande est plus courte que le personnage entier : il est coupé",
            HAUTEUR_BANDE_LOCUTEUR < HAUTEUR_LOCUTEUR,
        )
    }

    // Mesurer la vue au lieu du personnage avait fait tomber le cerne de l'habitant à 1,8 px.
    @Test
    fun `les contours rendus sont ceux du tableau`() {
        assertEquals("La taille refusée", 1.6f, contourRendu(HAUTEUR_REFUSEE), 0.05f)
        assertTrue(
            "L'habitant doit garder 2 px pleins : ${contourRendu(HAUTEUR_HABITANT)}",
            contourRendu(HAUTEUR_HABITANT) >= 2f,
        )
        assertTrue(
            "Le locuteur doit garder ses 3,5 px : ${contourRendu(HAUTEUR_LOCUTEUR)}",
            contourRendu(HAUTEUR_LOCUTEUR) >= 3.5f,
        )
        assertTrue(
            "Le contour vaut 1,1 % de la hauteur du personnage",
            EPAISSEUR_CONTOUR / HAUTEUR_PERSONNAGE in 0.0105f..0.0115f,
        )
    }

    @Test
    fun `la tete du locuteur est nettement plus grande que celle de l'habitant`() {
        val tete = (unitePour(HAUTEUR_LOCUTEUR) * (EPAULE_GAUCHE.y - SOMMET_TETE)).value
        val habitant = (unitePour(HAUTEUR_HABITANT) * (EPAULE_GAUCHE.y - SOMMET_TETE)).value
        assertTrue("Le locuteur montre un visage, pas une silhouette", tete > 1.3f * habitant)
    }

    @Test
    fun `le cadre garde les proportions du dessin`() {
        val cadre = cadrePour(HAUTEUR_LOCUTEUR)
        assertEquals(
            "Le rapport de la vue",
            LARGEUR_VUE / HAUTEUR_VUE,
            cadre.width.value / cadre.height.value,
            1e-4f,
        )
        assertTrue("La vue est plus haute que le personnage", cadre.height > HAUTEUR_LOCUTEUR)
        assertEquals(
            "La largeur de la bande est celle de la vue",
            cadre.width.value,
            LARGEUR_BANDE_LOCUTEUR.value,
            1e-4f,
        )
    }
}
