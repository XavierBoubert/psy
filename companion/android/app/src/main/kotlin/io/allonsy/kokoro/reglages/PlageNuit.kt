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

/** Les deux chiffres des heures d'une borne, tels qu'ils se saisissent. */
fun ecrireHeures(minute: Int): String = "%02d".format(minute / 60)

/** Les deux chiffres des minutes d'une borne. */
fun ecrireMinutes(minute: Int): String = "%02d".format(minute % 60)

/**
 * Une borne lue **en deux morceaux séparés**, les heures d'un côté, les minutes de l'autre.
 *
 * ⭐ **Le deux-points a disparu de la saisie** *(15/08/2026)* : le clavier numérique d'Android ne le
 * porte pas, et une heure ne s'écrivait donc pas du tout sans basculer sur le clavier de texte. Deux
 * champs de deux chiffres ferment la question — **il n'y a plus de caractère à trouver.**
 *
 * `null` dès qu'une des deux moitiés ne se lit pas : **rien n'est corrigé en silence.** Des minutes
 * laissées vides valent zéro, parce que taper `21` puis rien est la façon normale d'écrire 21 h.
 */
fun lireBorne(heures: String, minutes: String): Int? {
    val h = heures.trim().toIntOrNull() ?: return null
    val m = minutes.trim().ifBlank { "0" }.toIntOrNull() ?: return null
    if (h !in 0..23 || m !in 0..59) return null

    return h * 60 + m
}
