package io.allonsy.kokoro.corps

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.monde.HAUTEUR_HABITANT
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Trois densités — l'écran de Xavier. C'est là que se comptent les pixels du §1.4. */
private const val DENSITE = 3f

/** La taille que `PRESENCE.md` §1.4 écarte, et la raison pour laquelle elle est écartée. */
private val HAUTEUR_REFUSEE = 48.dp

/**
 * Le locuteur — `PRESENCE.md` §1.1, §1.4 et étape **E12**.
 *
 * ⭐ **Ce qui se vérifie ici sans écran** : que les deux régimes ne peuvent pas être à l'écran
 * ensemble, et que le cadrage est pris **dans le dessin** au lieu d'être réglé à l'œil.
 */
class LocuteurTest {

    private fun contourRendu(hauteurPersonnage: Dp): Float =
        (unitePour(hauteurPersonnage) * EPAISSEUR_CONTOUR).value * DENSITE

    /**
     * 🔴 **Une seule instance à l'écran** (§1.1) — c'est l'invariant que l'étape **E12** doit tenir,
     * *« y compris pendant les 320 ms d'ouverture du panneau »*. Les deux régimes se partagent une
     * seule bascule : il n'existe **aucune** valeur de sortie où les deux sont vrais, et aucune où
     * les deux sont faux — le personnage n'a pas le droit de disparaître entre les deux non plus.
     */
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

    /**
     * 🔴 **Le locuteur n'entre qu'une fois l'habitant entièrement sorti.** Pendant la montée du
     * panneau — qui découvre le bas de l'écran en premier, c'est-à-dire précisément le coin du
     * locuteur — l'habitant est encore dans le champ : entrer là serait le doublon interdit.
     */
    @Test
    fun `le locuteur attend que l'habitant soit dehors`() {
        assertTrue("À l'arrêt, c'est l'habitant qui est là", habitantEnScene(0f))
        assertTrue("En pleine sortie, c'est encore lui", habitantEnScene(0.99f))
        assertTrue("Le locuteur n'entre pas avant la fin", !locuteurEnScene(0.99f))
        assertTrue("Sorti, il laisse la place", locuteurEnScene(1f))
    }

    /**
     * ⭐ **Le cadrage est pris dans le dessin** (§1.1) : la coupe est le centre du ventre, donc elle
     * passe **sous les épaules** — le thorax est entier — et bien au-dessus du sol. La tête, elle,
     * tient tout entière dans le cadre, marge du dessin comprise.
     */
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

    /**
     * 🔴 **Le tableau du §1.4 se referme, et il ne se referme qu'à hauteur de personnage.** C'est
     * l'argument même du document : à 48 dp le cerne tombe à 1,6 px et le trait se délave ; à 60 dp
     * il reste à 2 px pleins ; le locuteur, lui, doit tenir ses 3,5 px pour qu'un **visage** se
     * lise. **Mesurer la vue au lieu du personnage rendait le cerne de l'habitant à 1,8 px** — sous
     * le seuil qui sert à écarter les 48 dp.
     */
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

    /**
     * ⭐ **Ce qui se lit est un visage** (§1.4) : du sommet du crâne à la ligne des épaules, la tête
     * mesure une soixantaine de dp — le chiffre du tableau —, soit près du double de celle de
     * l'habitant.
     */
    @Test
    fun `la tete du locuteur mesure une soixantaine de dp`() {
        val tete = (unitePour(HAUTEUR_LOCUTEUR) * (EPAULE_GAUCHE.y - SOMMET_TETE)).value
        assertEquals("La tête du locuteur", 60f, tete, 3f)

        val habitant = (unitePour(HAUTEUR_HABITANT) * (EPAULE_GAUCHE.y - SOMMET_TETE)).value
        assertTrue("Le locuteur montre un visage, pas une silhouette", tete > 1.7f * habitant)
    }

    /** La vue se déduit du personnage, jamais l'inverse — et elle garde les proportions du dessin. */
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
