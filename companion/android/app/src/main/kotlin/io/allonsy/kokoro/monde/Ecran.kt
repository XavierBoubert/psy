package io.allonsy.kokoro.monde

import kotlin.math.abs
import kotlin.math.floor

enum class Ecran { THERAPIE, DOCUMENTATION, BILAN, CRISE }

fun ecranEn(position: Int): Ecran = Ecran.entries[position.mod(Ecran.entries.size)]

fun ancreDe(camera: Float): Int = floor(camera).toInt()

fun positionsAutour(ancre: Int): List<Int> = List(Ecran.entries.size) { rang -> ancre - 1 + rang }

// camera et elan sont en écrans, elan par seconde ; un élan qui repart en arrière annule la traversée.
fun aterrissage(camera: Float, elan: Float, depuis: Int): Int {
    val ecart = camera - depuis
    if (ecart == 0f) return depuis

    val sens = if (ecart > 0f) 1 else -1
    val lance = elan * sens
    val franchi = when {
        lance >= VITESSE_BASCULE -> true
        lance <= -VITESSE_BASCULE -> false
        else -> abs(ecart) >= SEUIL_BASCULE
    }

    return if (franchi) depuis + sens else depuis
}

const val SEUIL_BASCULE = 0.18f

const val VITESSE_BASCULE = 0.7f
