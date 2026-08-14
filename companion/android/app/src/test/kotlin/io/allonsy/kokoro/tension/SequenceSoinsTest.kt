package io.allonsy.kokoro.tension

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SequenceSoinsTest {

    @Test
    fun `la sequence suit les quatre reperes externes de la fiche, dans l'ordre`() {
        assertEquals(
            listOf(
                EtapeSoins.PORTE,
                EtapeSoins.FAUTEUIL,
                EtapeSoins.PLATEAU,
                EtapeSoins.APRES_GESTE,
            ),
            SEQUENCE_SOINS,
        )
    }

    @Test
    fun `porte et fauteuil declenchent un bloc de cinq cycles`() {
        assertEquals(NOMBRE_CYCLES, cyclesDe(EtapeSoins.PORTE))
        assertEquals(NOMBRE_CYCLES, cyclesDe(EtapeSoins.FAUTEUIL))
    }

    @Test
    fun `voir l'aiguille enchaine les cycles sans terme`() {
        assertNull(cyclesDe(EtapeSoins.PLATEAU))
        assertNull(secondesDuBloc(cyclesDe(EtapeSoins.PLATEAU)))
        assertEquals(
            EtatTension(PhaseTension.CONTRACTE, 15, 30),
            etatTension(SECONDES_CYCLE * 29, cycles = null),
        )
    }

    @Test
    fun `apres le geste, trois cycles puis cinq minutes assis`() {
        assertEquals(3, CYCLES_APRES_GESTE)
        assertEquals(CYCLES_APRES_GESTE, cyclesDe(EtapeSoins.APRES_GESTE))
        assertEquals(105, secondesDuBloc(CYCLES_APRES_GESTE))
        assertEquals(300, SECONDES_ASSIS_APRES)
    }

    @Test
    fun `le repere attendu avance d'un cran et s'arrete au dernier`() {
        assertEquals(EtapeSoins.PORTE, etapeAttendue(null))
        assertEquals(EtapeSoins.FAUTEUIL, etapeAttendue(EtapeSoins.PORTE))
        assertEquals(EtapeSoins.PLATEAU, etapeAttendue(EtapeSoins.FAUTEUIL))
        assertEquals(EtapeSoins.APRES_GESTE, etapeAttendue(EtapeSoins.PLATEAU))
        assertEquals(EtapeSoins.APRES_GESTE, etapeAttendue(EtapeSoins.APRES_GESTE))
    }

    @Test
    fun `chaque etape porte un libelle et un bloc distincts`() {
        val libelles = SEQUENCE_SOINS.map { it.name }
        assertEquals(libelles.size, libelles.toSet().size)
    }
}
