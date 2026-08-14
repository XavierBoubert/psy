package io.allonsy.kokoro.reglages

import java.time.LocalTime

/**
 * La plage sur laquelle le monde est en nuit.
 *
 * ⭐ **Une plage horaire, pas une détection.** Kokoro ne regarde ni la luminosité, ni le thème du
 * système, ni l'usage : il lit deux heures que Xavier a posées lui-même, et qu'il peut désactiver
 * d'un geste. C'est ce qui en fait un changement *prévu* et non un changement *subi*.
 *
 * Les deux bornes sont en minutes depuis minuit — une seule grandeur à comparer, aucun fuseau,
 * aucune date.
 */
data class PlageNuit(
    val active: Boolean,
    val debut: Int,
    val fin: Int,
)

const val MINUTES_PAR_JOUR: Int = 24 * 60

/** 21 h → 6 h. Le défaut se règle et se coupe ; il ne s'impose pas. */
val PLAGE_NUIT_PAR_DEFAUT = PlageNuit(active = true, debut = 21 * 60, fin = 6 * 60)

/**
 * ⭐ **Une plage de nuit passe minuit**, donc elle s'enroule : `21:00 → 06:00` couvre la fin d'une
 * journée et le début de la suivante. Le cas `début == fin` est traité comme **vide** et non comme
 * plein : à réglage ambigu, le décor reste celui du jour — jamais une nuit permanente subie.
 */
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

/** `21:00`, `21h30`, `6` — et `null` dès que ce n'est pas une heure, pour que rien ne s'enregistre. */
fun lireHeure(texte: String): Int? {
    val morceaux = texte.trim().split(':', 'h', 'H', '.')
    if (morceaux.size > 2) return null

    val heures = morceaux[0].trim().toIntOrNull() ?: return null
    val minutes = when (val reste = morceaux.getOrNull(1)?.trim().orEmpty()) {
        "" -> 0
        else -> reste.toIntOrNull() ?: return null
    }
    if (heures !in 0..23 || minutes !in 0..59) return null

    return heures * 60 + minutes
}
