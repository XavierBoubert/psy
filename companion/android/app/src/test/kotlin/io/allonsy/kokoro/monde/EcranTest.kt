package io.allonsy.kokoro.monde

import io.allonsy.kokoro.decor.Ancrage
import io.allonsy.kokoro.decor.COUCHES
import org.junit.Assert.assertEquals
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
    fun `l ordre des ecrans est celui de la traversee`() {
        assertEquals(
            listOf(Ecran.THERAPIE, Ecran.DOCUMENTATION, Ecran.BILAN, Ecran.CRISE),
            Ecran.entries,
        )
    }

    /** ⭐ On arrive sur la thérapie, et la crise est sa voisine de gauche. */
    @Test
    fun `la crise est a un seul geste de l entree`() {
        assertEquals(Ecran.THERAPIE, ecranEn(0))
        assertEquals(Ecran.CRISE, ecranEn(-1))
    }

    /** 🔴 Après le dernier écran vient le premier, dans le même sens. */
    @Test
    fun `l anneau boucle dans les deux sens`() {
        assertEquals(Ecran.THERAPIE, ecranEn(4))
        assertEquals(Ecran.DOCUMENTATION, ecranEn(5))
        assertEquals(Ecran.CRISE, ecranEn(-5))
        assertEquals(Ecran.BILAN, ecranEn(-6))
    }

    /** ⭐ Quatre positions peintes, quatre écrans distincts : aucun n'est monté deux fois. */
    @Test
    fun `les positions peintes couvrent chaque ecran une fois`() {
        listOf(-9, -1, 0, 3, 7).forEach { ancre ->
            val ecrans = positionsAutour(ancre).map(::ecranEn)

            assertEquals("ancre $ancre", Ecran.entries.size, ecrans.toSet().size)
        }
    }

    /** ⭐ Les deux positions à l'image sont l'ancre et sa voisine de droite, marge comprise. */
    @Test
    fun `les positions peintes encadrent celles qui sont a l image`() {
        val ancre = ancreDe(2.4f)

        assertEquals(2, ancre)
        assertTrue(positionsAutour(ancre).containsAll(listOf(2, 3)))
        assertEquals(listOf(1, 2, 3, 4), positionsAutour(ancre))
    }

    @Test
    fun `l ancre suit la camera meme en negatif`() {
        assertEquals(-1, ancreDe(-0.2f))
        assertEquals(-3, ancreDe(-2.6f))
        assertEquals(5, ancreDe(5f))
    }

    @Test
    fun `en deca du seuil on revient d ou l on vient`() {
        val court = SEUIL_BASCULE - 0.01f

        assertEquals(0, aterrissage(court, 0f, 0))
    }

    @Test
    fun `au dela du seuil on bascule`() {
        val franc = SEUIL_BASCULE + 0.01f

        assertEquals(1, aterrissage(franc, 0f, 0))
        assertEquals(-1, aterrissage(-franc, 0f, 0))
    }

    /** 🔴 La traversée ne bute nulle part : le dernier écran mène au suivant, pas au premier. */
    @Test
    fun `la position ne se replie jamais`() {
        val franc = SEUIL_BASCULE + 0.01f

        assertEquals(4, aterrissage(3f + franc, 0f, 3))
        assertEquals(Ecran.THERAPIE, ecranEn(4))
    }

    /**
     * ⭐ Un geste vif part plus vite qu'il ne va loin. Sans l'élan, il échouait sur la distance et le
     * monde revenait en arrière alors que le geste était sans ambiguïté — c'était la saccade.
     */
    @Test
    fun `un geste lance bascule meme s il est court`() {
        assertEquals(1, aterrissage(0.04f, VITESSE_BASCULE + 0.1f, 0))
    }

    /** ⭐ Le doigt s'est ravisé avant de se lever : le dernier sens voulu est celui-là. */
    @Test
    fun `un elan qui repart en arriere annule la traversee`() {
        assertEquals(0, aterrissage(0.6f, -VITESSE_BASCULE - 0.1f, 0))
    }

    /** ⭐ On ne saute jamais deux écrans, si lancé soit le geste. */
    @Test
    fun `un geste tres lance n avance que d un ecran`() {
        assertEquals(1, aterrissage(0.9f, 6f, 0))
    }

    @Test
    fun `sans ecart on ne bouge pas`() {
        assertEquals(2, aterrissage(2f, 5f, 2))
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

    /** 🔴 Un décalage négatif découvrirait le ciel sous une couche ancrée en bas. */
    @Test
    fun `aucune couche du bas ne decolle du bord`() {
        COUCHES.filter { it.ancrage == Ancrage.BAS }.forEach {
            assertTrue("décalage = ${it.decalage}", it.decalage >= 0f)
        }
    }
}
