package io.allonsy.kokoro.tension

const val SECONDES_CONTRACTION = 15
const val SECONDES_RELACHE = 20
const val NOMBRE_CYCLES = 5
const val SECONDES_CYCLE = SECONDES_CONTRACTION + SECONDES_RELACHE
const val SECONDES_BLOC = SECONDES_CYCLE * NOMBRE_CYCLES

enum class PhaseTension { CONTRACTE, RELACHE, TERMINE }

data class EtatTension(
    val phase: PhaseTension,
    val secondesRestantes: Int,
    val cycle: Int,
)

fun etatTension(secondesEcoulees: Int): EtatTension {
    val ecoulees = secondesEcoulees.coerceAtLeast(0)
    if (ecoulees >= SECONDES_BLOC) {
        return EtatTension(PhaseTension.TERMINE, 0, NOMBRE_CYCLES)
    }
    val cycle = ecoulees / SECONDES_CYCLE + 1
    val position = ecoulees % SECONDES_CYCLE
    if (position < SECONDES_CONTRACTION) {
        return EtatTension(PhaseTension.CONTRACTE, SECONDES_CONTRACTION - position, cycle)
    }
    return EtatTension(PhaseTension.RELACHE, SECONDES_CYCLE - position, cycle)
}
