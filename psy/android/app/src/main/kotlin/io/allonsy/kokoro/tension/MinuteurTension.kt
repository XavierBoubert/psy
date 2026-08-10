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

fun secondesDuBloc(cycles: Int?): Int? = cycles?.times(SECONDES_CYCLE)

fun etatTension(secondesEcoulees: Int, cycles: Int? = NOMBRE_CYCLES): EtatTension {
    val ecoulees = secondesEcoulees.coerceAtLeast(0)
    val fin = secondesDuBloc(cycles)
    if (cycles != null && fin != null && ecoulees >= fin) {
        return EtatTension(PhaseTension.TERMINE, 0, cycles)
    }
    val cycle = ecoulees / SECONDES_CYCLE + 1
    val position = ecoulees % SECONDES_CYCLE
    if (position < SECONDES_CONTRACTION) {
        return EtatTension(PhaseTension.CONTRACTE, SECONDES_CONTRACTION - position, cycle)
    }
    return EtatTension(PhaseTension.RELACHE, SECONDES_CYCLE - position, cycle)
}

fun fractionPhase(millisEcoulees: Long, cycles: Int? = NOMBRE_CYCLES): Float {
    val ecoulees = millisEcoulees.coerceAtLeast(0L)
    val fin = secondesDuBloc(cycles)?.times(1000L)
    if (fin != null && ecoulees >= fin) return 1f
    val position = (ecoulees % (SECONDES_CYCLE * 1000L)) / 1000f
    if (position < SECONDES_CONTRACTION) {
        return position / SECONDES_CONTRACTION
    }
    return (position - SECONDES_CONTRACTION) / SECONDES_RELACHE
}
