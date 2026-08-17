package io.allonsy.kokoro.decor

import kotlin.math.atan2
import kotlin.math.hypot

const val INCLINAISON_MAX_DEGRES: Float = 18f

const val DEBATTEMENT_INCLINAISON: Float = 0.40f

fun inclinaisonDeLaGravite(x: Float, y: Float, z: Float): Float {
    val aplomb = hypot(y, z)
    val degres = Math.toDegrees(atan2(x, aplomb).toDouble()).toFloat()

    return -(degres / INCLINAISON_MAX_DEGRES).coerceIn(-1f, 1f) * DEBATTEMENT_INCLINAISON
}
