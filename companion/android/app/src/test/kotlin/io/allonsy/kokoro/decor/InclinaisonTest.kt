package io.allonsy.kokoro.decor

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * L'inclinaison est ce qui laisse le décor bouger **sans le doigt**. Ce qui est vérifié ici est
 * exactement ce qui rend cette dérogation acceptable : elle est **bornée**, elle est **centrée sur
 * la verticale**, et elle est **réversible** — reposer le téléphone comme il était remet le décor
 * où il était, sans mémoire d'aucun mouvement passé.
 */
class InclinaisonTest {

    /** L'intensité de la pesanteur, en m/s² — la longueur des vecteurs que rend le capteur. */
    private val g = 9.81f

    /** Le téléphone tenu droit devant soi, en portrait : la gravité est le long de l'axe vertical. */
    @Test
    fun `tenu d aplomb, le decor ne bouge pas`() {
        assertEquals(0f, inclinaisonDeLaGravite(x = 0f, y = g, z = 0f), 1e-4f)
    }

    /** Posé à plat sur une table : la gravité sort de l'écran, aucun côté ne penche. */
    @Test
    fun `pose a plat, le decor ne bouge pas`() {
        assertEquals(0f, inclinaisonDeLaGravite(x = 0f, y = 0f, z = g), 1e-4f)
    }

    /**
     * ⭐ **Une fenêtre, pas un niveau à bulle** : pencher le bord droit vers le bas — donc l'axe `x`
     * du téléphone vers le sol, où le capteur rend un `x` négatif — découvre ce qui est à droite,
     * dans le même sens que le doigt qui pousse le monde vers la gauche.
     */
    @Test
    fun `pencher a droite emmene la camera a droite`() {
        assertTrue(inclinaisonDeLaGravite(x = -1f, y = g, z = 0f) > 0f)
        assertTrue(inclinaisonDeLaGravite(x = 1f, y = g, z = 0f) < 0f)
    }

    /**
     * 🔴 **Sans butée, un téléphone retourné emmènerait le décor à l'infini.** Le doigt a un bout —
     * il se lève ; une position, non.
     */
    @Test
    fun `le debattement est borne des deux cotes`() {
        val couche = inclinaisonDeLaGravite(x = -g, y = 0f, z = 0f)
        assertEquals(DEBATTEMENT_INCLINAISON, couche, 1e-4f)
        assertEquals(-DEBATTEMENT_INCLINAISON, inclinaisonDeLaGravite(x = g, y = 0f, z = 0f), 1e-4f)
    }

    /** À l'angle de butée exactement, le débattement est atteint et pas dépassé. */
    @Test
    fun `l angle de butee vaut le debattement entier`() {
        val radians = Math.toRadians(INCLINAISON_MAX_DEGRES.toDouble())
        val x = (-Math.sin(radians) * g).toFloat()
        val y = (Math.cos(radians) * g).toFloat()

        assertEquals(DEBATTEMENT_INCLINAISON, inclinaisonDeLaGravite(x, y, z = 0f), 1e-4f)
    }

    /** À mi-course, la moitié : la réponse est droite, sans zone morte ni accélération. */
    @Test
    fun `la reponse est proportionnelle a l angle`() {
        val radians = Math.toRadians(INCLINAISON_MAX_DEGRES.toDouble() / 2)
        val x = (-Math.sin(radians) * g).toFloat()
        val y = (Math.cos(radians) * g).toFloat()

        assertEquals(DEBATTEMENT_INCLINAISON / 2f, inclinaisonDeLaGravite(x, y, z = 0f), 1e-4f)
    }

    /**
     * ⭐ **Le décor ne garde aucune trace du chemin parcouru.** C'est ce qui distingue une position
     * d'une intégration de gyroscope : passer par une inclinaison extrême puis revenir rend
     * exactement la valeur de départ.
     */
    @Test
    fun `revenir a la meme position rend la meme valeur`() {
        val depart = inclinaisonDeLaGravite(x = -2f, y = g, z = 1f)
        inclinaisonDeLaGravite(x = -g, y = 0f, z = 0f)

        assertEquals(depart, inclinaisonDeLaGravite(x = -2f, y = g, z = 1f), 0f)
    }

    /** En chute libre le capteur ne rend rien d'exploitable ; le décor reste où il est. */
    @Test
    fun `un vecteur nul ne fait rien`() {
        assertEquals(0f, inclinaisonDeLaGravite(x = 0f, y = 0f, z = 0f), 0f)
    }
}
