package io.allonsy.kokoro.tension

const val CYCLES_APRES_GESTE = 3
const val SECONDES_ASSIS_APRES = 300

enum class EtapeSoins { PORTE, FAUTEUIL, PLATEAU, APRES_GESTE }

val SEQUENCE_SOINS: List<EtapeSoins> = listOf(
    EtapeSoins.PORTE,
    EtapeSoins.FAUTEUIL,
    EtapeSoins.PLATEAU,
    EtapeSoins.APRES_GESTE,
)

fun cyclesDe(etape: EtapeSoins): Int? = when (etape) {
    EtapeSoins.PORTE -> NOMBRE_CYCLES
    EtapeSoins.FAUTEUIL -> NOMBRE_CYCLES
    EtapeSoins.PLATEAU -> null
    EtapeSoins.APRES_GESTE -> CYCLES_APRES_GESTE
}

fun etapeAttendue(dernierFait: EtapeSoins?): EtapeSoins = when (dernierFait) {
    null -> SEQUENCE_SOINS.first()
    else -> SEQUENCE_SOINS.getOrNull(SEQUENCE_SOINS.indexOf(dernierFait) + 1) ?: dernierFait
}
