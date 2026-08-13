package io.allonsy.kokoro.corps

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val FICHIER_SVG = File("../design/retenus/kokoro-corps.svg")

/** Vérifications de géométrie : que le rig et le SVG décrivent bien le même personnage. */
class CorpsInvariantsTest {

    private val svg: String by lazy {
        assertTrue("SVG introuvable : ${FICHIER_SVG.absolutePath}", FICHIER_SVG.isFile)
        FICHIER_SVG.readText()
    }

    @Test
    fun `le svg porte exactement les memes chemins que le rig`() {
        val manquants = (PIECES.map { it.nom to it.chemin } + TRACES.map { it.nom to it.chemin })
            .filterNot { (_, chemin) -> svg.contains(chemin) }
            .map { (nom, _) -> nom }
        assertTrue(
            "Chemins absents du SVG — les deux surfaces ont divergé : $manquants",
            manquants.isEmpty(),
        )
    }

    @Test
    fun `la designation regarde du cote qu'elle montre`() {
        val gauche = RigKokoro.pose(Posture.Montre(Cote.GAUCHE))
        val droite = RigKokoro.pose(Posture.Montre(Cote.DROITE))
        assertEquals(OUVERTURE_HORIZONTALE, gauche.rotationBrasGauche, 0f)
        assertTrue("Le regard suit le bras gauche", gauche.regard < 0f)
        assertEquals(-OUVERTURE_HORIZONTALE, droite.rotationBrasDroit, 0f)
        assertTrue("Le regard suit le bras droit", droite.regard > 0f)
    }

    @Test
    fun `la respiration reste dans l'amplitude annoncee`() {
        val expiration = RigKokoro(visage = Visage.de(Expression.NEUTRE), respiration = 0f)
        val inspiration = RigKokoro(visage = Visage.de(Expression.NEUTRE), respiration = 1f)
        assertEquals(1f, expiration.etirementCorps, 0f)
        assertEquals(1f + AMPLITUDE_HAUTEUR, inspiration.etirementCorps, 1e-6f)
        assertEquals(1f - AMPLITUDE_LARGEUR, inspiration.retractionCorps, 1e-6f)
    }
}
