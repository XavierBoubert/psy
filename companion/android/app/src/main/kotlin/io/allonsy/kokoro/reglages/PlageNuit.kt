package io.allonsy.kokoro.reglages

import java.time.LocalTime

data class PlageNuit(
    val active: Boolean,
    val debut: Int,
    val fin: Int,
)

const val MINUTES_PAR_JOUR: Int = 24 * 60

val PLAGE_NUIT_PAR_DEFAUT = PlageNuit(active = true, debut = 21 * 60, fin = 6 * 60)

fun estNuit(plage: PlageNuit, minute: Int): Boolean {
    if (!plage.active || plage.debut == plage.fin) return false

    val instant = ((minute % MINUTES_PAR_JOUR) + MINUTES_PAR_JOUR) % MINUTES_PAR_JOUR

    return when {
        plage.debut < plage.fin -> instant >= plage.debut && instant < plage.fin
        else -> instant >= plage.debut || instant < plage.fin
    }
}

fun minuteCourante(): Int = LocalTime.now().let { it.hour * 60 + it.minute }

fun ecrireHeure(minute: Int): String = "%02d:%02d".format(minute / 60, minute % 60)

fun ecrireHeures(minute: Int): String = "%02d".format(minute / 60)

fun ecrireMinutes(minute: Int): String = "%02d".format(minute % 60)

// Minutes vides = 0 : taper seulement les heures est la façon normale d'écrire une heure ronde.
fun lireBorne(heures: String, minutes: String): Int? {
    val h = heures.trim().toIntOrNull() ?: return null
    val m = minutes.trim().ifBlank { "0" }.toIntOrNull() ?: return null
    if (h !in 0..23 || m !in 0..59) return null

    return h * 60 + m
}
