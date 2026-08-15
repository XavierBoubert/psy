package io.allonsy.kokoro.monde

import kotlin.math.abs
import kotlin.math.floor

/**
 * Les quatre écrans du monde, **dans l'ordre où on les traverse** — et rien d'autre.
 *
 * ```
 *  ← … ┌──────────┐ ┌───────────────┐ ┌────────┐ ┌───────┐ ┌──────────┐ … →
 *      │ Thérapie │ │ Documentation │ │ Bilan  │ │ Crise │ │ Thérapie │
 *      └──────────┘ └───────────────┘ └────────┘ └───────┘ └──────────┘
 *         entrée                                              (le même)
 * ```
 *
 * ⭐ **Le monde est un anneau, plus une croix** *(15/08/2026)*. Il n'y a plus de haut ni de bas :
 * **tout est horizontal**, donc le glissement vertical est rendu au contenu et **n'importe quel
 * écran peut défiler**. C'est ce qui lève le point dur **P1** — plus aucun contenu n'est logé
 * ailleurs que là où il a du sens.
 *
 * ⭐ **La traversée ne bute jamais.** Après le dernier écran vient le premier, et le décor
 * **continue de glisser dans le même sens** : ce n'est pas un retour en arrière, c'est un tour de
 * plus. Rien ne rebondit, rien ne saute, rien ne dit qu'on est au bout — parce qu'il n'y a pas de
 * bout.
 *
 * ⭐ **La crise est donc à un seul geste de l'entrée**, vers la gauche. Elle est la dernière de
 * l'anneau et la voisine immédiate de la thérapie : **on n'a jamais à traverser le monde pour
 * l'atteindre.**
 */
enum class Ecran { THERAPIE, DOCUMENTATION, BILAN, CRISE }

/**
 * Une **position** est un rang sur la bande infinie ; un **écran** est ce qu'on y trouve. Deux
 * positions distantes de quatre montrent le même écran, et c'est tout ce qui fait l'anneau.
 *
 * 🔴 **La position ne se replie jamais.** Elle continue de croître ou de décroître, et c'est elle
 * qui commande la caméra : la replier remettrait le décor à zéro au passage du dernier écran,
 * c'est-à-dire exactement le saut que l'anneau existe pour éviter.
 */
fun ecranEn(position: Int): Ecran = Ecran.entries[position.mod(Ecran.entries.size)]

/** La position de l'écran qui touche le bord gauche de la dalle. */
fun ancreDe(camera: Float): Int = floor(camera).toInt()

/**
 * Les positions peintes autour de l'ancre — **exactement une par écran**, une de marge de chaque
 * côté des deux qui sont à l'image.
 *
 * ⭐ **Quatre positions pour quatre écrans, donc aucun écran n'est monté deux fois.** C'est ce qui
 * permet de garder l'état de chaque écran — le défilement d'une liste, par exemple — d'un tour à
 * l'autre : ce n'est jamais une copie qui revient, c'est le même.
 */
fun positionsAutour(ancre: Int): List<Int> = List(Ecran.entries.size) { rang -> ancre - 1 + rang }

/**
 * La position où l'on atterrit quand le doigt se lève.
 *
 * Deux façons d'arriver au bout, et il en fallait deux : **la distance** — on a poussé le monde au
 * moins jusqu'à [SEUIL_BASCULE] — ou **l'élan** — on l'a lancé au moins à [VITESSE_BASCULE]. Sur la
 * distance seule, un geste vif et court échouait : le doigt partait plus vite qu'il n'allait loin,
 * et le monde revenait en arrière alors que le geste était sans ambiguïté.
 *
 * ⭐ **Un élan qui repart en arrière annule la traversée**, même si la distance est franchie : le
 * doigt s'est ravisé avant de se lever, et le dernier sens voulu est celui-là.
 *
 * ⭐ **On ne saute jamais deux écrans**, si lancé soit le geste. Un monde qui défile de trois écrans
 * sur un coup de pouce demanderait de retrouver où l'on est ; d'un écran, on le sait sans regarder.
 *
 * [camera] et [elan] sont en écrans, [elan] par seconde.
 */
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

/**
 * Le seuil est court exprès : un geste franc mais bref doit suffire, sans quoi il faudrait traîner
 * le doigt sur un cinquième de l'écran.
 */
const val SEUIL_BASCULE = 0.18f

/** En écrans par seconde. Un geste posé finit sous cette valeur, un geste lancé la dépasse. */
const val VITESSE_BASCULE = 0.7f
