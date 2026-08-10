package io.allonsy.kokoro.tension

import org.junit.Assert.assertEquals
import org.junit.Test

class MinuteurTensionTest {

    @Test
    fun `le bloc suit le protocole 15 sur 20, cinq cycles`() {
        assertEquals(15, SECONDES_CONTRACTION)
        assertEquals(20, SECONDES_RELACHE)
        assertEquals(5, NOMBRE_CYCLES)
        assertEquals(175, SECONDES_BLOC)
    }

    @Test
    fun `la contraction ouvre chaque cycle et dure quinze secondes`() {
        assertEquals(EtatTension(PhaseTension.CONTRACTE, 15, 1), etatTension(0))
        assertEquals(EtatTension(PhaseTension.CONTRACTE, 1, 1), etatTension(14))
        assertEquals(EtatTension(PhaseTension.CONTRACTE, 15, 2), etatTension(35))
        assertEquals(EtatTension(PhaseTension.CONTRACTE, 15, 5), etatTension(140))
    }

    @Test
    fun `le relache suit la contraction et dure vingt secondes`() {
        assertEquals(EtatTension(PhaseTension.RELACHE, 20, 1), etatTension(15))
        assertEquals(EtatTension(PhaseTension.RELACHE, 1, 1), etatTension(34))
        assertEquals(EtatTension(PhaseTension.RELACHE, 1, 5), etatTension(174))
    }

    @Test
    fun `le bloc se termine apres le cinquieme relache`() {
        assertEquals(EtatTension(PhaseTension.TERMINE, 0, 5), etatTension(175))
        assertEquals(EtatTension(PhaseTension.TERMINE, 0, 5), etatTension(10_000))
    }

    @Test
    fun `un temps negatif est traite comme le depart`() {
        assertEquals(etatTension(0), etatTension(-3))
    }

    @Test
    fun `la barre remplit chaque phase de zero a un, sans saut`() {
        assertEquals(0f, fractionPhase(0L), 0.001f)
        assertEquals(0.5f, fractionPhase(7_500L), 0.001f)
        assertEquals(0f, fractionPhase(15_000L), 0.001f)
        assertEquals(0.5f, fractionPhase(25_000L), 0.001f)
        assertEquals(0f, fractionPhase(35_000L), 0.001f)
        assertEquals(1f, fractionPhase(175_000L), 0.001f)
    }

    @Test
    fun `un bloc enchaine ne se termine jamais de lui-meme`() {
        assertEquals(PhaseTension.CONTRACTE, etatTension(175, cycles = null).phase)
        assertEquals(PhaseTension.RELACHE, etatTension(10_000, cycles = null).phase)
    }

    @Test
    fun `chaque seconde du bloc appartient a un cycle compris entre un et cinq`() {
        (0 until SECONDES_BLOC).forEach { seconde ->
            val etat = etatTension(seconde)
            assert(etat.cycle in 1..NOMBRE_CYCLES) { "cycle hors bornes à $seconde s : ${etat.cycle}" }
            assert(etat.secondesRestantes in 1..SECONDES_RELACHE) {
                "décompte hors bornes à $seconde s : ${etat.secondesRestantes}"
            }
        }
    }
}
