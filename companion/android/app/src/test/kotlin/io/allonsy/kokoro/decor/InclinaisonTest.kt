package io.allonsy.kokoro.decor

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InclinaisonTest {

    private val g = 9.81f

    @Test
    fun `tenu d aplomb, le decor ne bouge pas`() {
        assertEquals(0f, inclinaisonDeLaGravite(x = 0f, y = g, z = 0f), 1e-4f)
    }

    @Test
    fun `pose a plat, le decor ne bouge pas`() {
        assertEquals(0f, inclinaisonDeLaGravite(x = 0f, y = 0f, z = g), 1e-4f)
    }

    // x négatif (téléphone penché à droite) ouvre la vue à droite, comme un doigt qui pousse le monde à gauche.
    @Test
    fun `pencher a droite emmene la camera a droite`() {
        assertTrue(inclinaisonDeLaGravite(x = -1f, y = g, z = 0f) > 0f)
        assertTrue(inclinaisonDeLaGravite(x = 1f, y = g, z = 0f) < 0f)
    }

    @Test
    fun `le debattement est borne des deux cotes`() {
        val couche = inclinaisonDeLaGravite(x = -g, y = 0f, z = 0f)
        assertEquals(DEBATTEMENT_INCLINAISON, couche, 1e-4f)
        assertEquals(-DEBATTEMENT_INCLINAISON, inclinaisonDeLaGravite(x = g, y = 0f, z = 0f), 1e-4f)
    }

    @Test
    fun `l angle de butee vaut le debattement entier`() {
        val radians = Math.toRadians(INCLINAISON_MAX_DEGRES.toDouble())
        val x = (-Math.sin(radians) * g).toFloat()
        val y = (Math.cos(radians) * g).toFloat()

        assertEquals(DEBATTEMENT_INCLINAISON, inclinaisonDeLaGravite(x, y, z = 0f), 1e-4f)
    }

    @Test
    fun `la reponse est proportionnelle a l angle`() {
        val radians = Math.toRadians(INCLINAISON_MAX_DEGRES.toDouble() / 2)
        val x = (-Math.sin(radians) * g).toFloat()
        val y = (Math.cos(radians) * g).toFloat()

        assertEquals(DEBATTEMENT_INCLINAISON / 2f, inclinaisonDeLaGravite(x, y, z = 0f), 1e-4f)
    }

    // Aucune mémoire de trajectoire : contrairement à un gyroscope intégré, la position ne dépend que de l'angle actuel.
    @Test
    fun `revenir a la meme position rend la meme valeur`() {
        val depart = inclinaisonDeLaGravite(x = -2f, y = g, z = 1f)
        inclinaisonDeLaGravite(x = -g, y = 0f, z = 0f)

        assertEquals(depart, inclinaisonDeLaGravite(x = -2f, y = g, z = 1f), 0f)
    }

    @Test
    fun `un vecteur nul ne fait rien`() {
        assertEquals(0f, inclinaisonDeLaGravite(x = 0f, y = 0f, z = 0f), 0f)
    }
}
