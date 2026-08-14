package io.allonsy.kokoro.monde

import androidx.compose.ui.geometry.Offset
import io.allonsy.kokoro.decor.Ancrage
import io.allonsy.kokoro.decor.COUCHES
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Ce que le monde doit tenir, quoi qu'on change ensuite.
 *
 * Ce ne sont pas des tests de rendu — rien ici ne regarde une image. Ce sont les règles de
 * déplacement, qui décident de ce qui arrive quand Xavier pose le doigt, et le fait qu'aucune
 * couche du décor n'en dépasse une autre en profondeur.
 */
class EcranTest {

    @Test
    fun `le centre mene aux quatre bords`() {
        assertEquals(Ecran.GAUCHE, Ecran.CENTRE.versLe(Direction.VERS_LA_GAUCHE))
        assertEquals(Ecran.DROITE, Ecran.CENTRE.versLe(Direction.VERS_LA_DROITE))
        assertEquals(Ecran.HAUT, Ecran.CENTRE.versLe(Direction.VERS_LE_HAUT))
        assertEquals(Ecran.BAS, Ecran.CENTRE.versLe(Direction.VERS_LE_BAS))
    }

    @Test
    fun `depuis un bord, le centre est a un seul geste`() {
        assertEquals(Ecran.CENTRE, Ecran.GAUCHE.versLe(Direction.VERS_LA_DROITE))
        assertEquals(Ecran.CENTRE, Ecran.DROITE.versLe(Direction.VERS_LA_GAUCHE))
        assertEquals(Ecran.CENTRE, Ecran.HAUT.versLe(Direction.VERS_LE_BAS))
        assertEquals(Ecran.CENTRE, Ecran.BAS.versLe(Direction.VERS_LE_HAUT))
    }

    /** 🔴 Le monde est une croix : aucune diagonale, aucun écran à deux gestes du centre. */
    @Test
    fun `aucun bord ne mene a un autre bord`() {
        val bords = listOf(Ecran.GAUCHE, Ecran.DROITE, Ecran.HAUT, Ecran.BAS)

        bords.forEach { bord ->
            Direction.entries
                .filter { bord.versLe(it) != Ecran.CENTRE }
                .forEach { assertNull("$bord vers $it", bord.versLe(it)) }
        }
    }

    @Test
    fun `un geste sans voisin ne deplace rien`() {
        val brut = Offset(Ecran.GAUCHE.camera.x - 0.7f, 0f)

        assertEquals(Ecran.GAUCHE.camera, bornerCamera(brut, Ecran.GAUCHE, Axe.HORIZONTAL))
    }

    @Test
    fun `un geste hors axe ne deplace rien`() {
        val brut = Offset(Ecran.GAUCHE.camera.x, 0.6f)

        assertEquals(Ecran.GAUCHE.camera, bornerCamera(brut, Ecran.GAUCHE, Axe.VERTICAL))
    }

    @Test
    fun `la camera ne depasse jamais l ecran vise`() {
        val brut = Offset(2.4f, 0f)

        assertEquals(Ecran.DROITE.camera, bornerCamera(brut, Ecran.CENTRE, Axe.HORIZONTAL))
    }

    /** ⭐ Reprendre le monde en pleine traversée ne doit pas le faire sauter au premier contact. */
    @Test
    fun `la course d un bord garde le centre a portee`() {
        assertEquals(0f..1f, course(Ecran.DROITE, Axe.HORIZONTAL))
        assertEquals(-1f..1f, course(Ecran.CENTRE, Axe.HORIZONTAL))
        assertEquals(0f..0f, course(Ecran.DROITE, Axe.VERTICAL))
    }

    @Test
    fun `en deca du seuil on revient d ou l on vient`() {
        val court = Offset(SEUIL_BASCULE - 0.01f, 0f)

        assertEquals(Ecran.CENTRE, aterrissage(court, Offset.Zero, Ecran.CENTRE, Axe.HORIZONTAL))
    }

    @Test
    fun `au dela du seuil on bascule`() {
        val franc = Offset(SEUIL_BASCULE + 0.01f, 0f)

        assertEquals(Ecran.DROITE, aterrissage(franc, Offset.Zero, Ecran.CENTRE, Axe.HORIZONTAL))
    }

    /**
     * ⭐ Un geste vif part plus vite qu'il ne va loin. Sans l'élan, il échouait sur la distance et le
     * monde revenait en arrière alors que le geste était sans ambiguïté — c'était la saccade.
     */
    @Test
    fun `un geste lance bascule meme s il est court`() {
        val court = Offset(0.04f, 0f)
        val lance = Offset(VITESSE_BASCULE + 0.1f, 0f)

        assertEquals(Ecran.DROITE, aterrissage(court, lance, Ecran.CENTRE, Axe.HORIZONTAL))
    }

    /** ⭐ Le doigt s'est ravisé avant de se lever : le dernier sens voulu est celui-là. */
    @Test
    fun `un elan qui repart en arriere annule la traversee`() {
        val loin = Offset(0.6f, 0f)
        val retour = Offset(-VITESSE_BASCULE - 0.1f, 0f)

        assertEquals(Ecran.CENTRE, aterrissage(loin, retour, Ecran.CENTRE, Axe.HORIZONTAL))
    }

    @Test
    fun `un elan sans voisin ne mene nulle part`() {
        val lance = Offset(-VITESSE_BASCULE - 1f, 0f)

        assertEquals(Ecran.GAUCHE, aterrissage(Offset(-1.2f, 0f), lance, Ecran.GAUCHE, Axe.HORIZONTAL))
    }

    @Test
    fun `le seuil reste court`() {
        assertTrue("seuil = $SEUIL_BASCULE", SEUIL_BASCULE in 0.05f..0.35f)
        assertTrue("vitesse = $VITESSE_BASCULE", VITESSE_BASCULE in 0.3f..2f)
    }

    /**
     * ⭐ Les couches sont déclarées du loin vers le près, et c'est cet ordre qui décide de l'ordre
     * de peinture. Une profondeur qui ne suit pas ferait passer un nuage devant le feuillage.
     */
    @Test
    fun `les couches du decor vont du loin vers le pres`() {
        val profondeurs = COUCHES.map { it.profondeur }

        assertEquals(profondeurs.sorted(), profondeurs)
        assertTrue("aucune couche ne colle au contenu", profondeurs.all { it > 0f && it < 1f })
    }

    /** Le ciel est en haut et le sol en bas : les couches ancrées en bas sont les plus proches. */
    @Test
    fun `les couches ancrees en bas sont les plus proches`() {
        val ancrages = COUCHES.map { it.ancrage }

        assertEquals(listOf(Ancrage.HAUT, Ancrage.HAUT, Ancrage.BAS, Ancrage.BAS), ancrages)
    }
}
