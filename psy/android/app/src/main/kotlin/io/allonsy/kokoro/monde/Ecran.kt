package io.allonsy.kokoro.monde

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

/**
 * Les cinq écrans du monde, et rien d'autre.
 *
 * ⭐ **Le monde est une croix, pas une grille.** Du centre on va aux quatre bords ; d'un bord on ne
 * peut que revenir au centre. Il n'y a pas de diagonale, donc **aucun écran n'est à deux gestes**,
 * et depuis n'importe où le centre est à un seul geste. C'est la propriété qui rend le monde
 * mémorisable sans le parcourir : il n'y a rien à retenir d'autre que « on revient toujours ».
 *
 * La position est donnée en écrans — c'est aussi l'unité de la caméra, ce qui évite toute conversion.
 */
enum class Ecran(val camera: Offset) {
    CENTRE(Offset(0f, 0f)),
    GAUCHE(Offset(-1f, 0f)),
    DROITE(Offset(1f, 0f)),
    HAUT(Offset(0f, -1f)),
    BAS(Offset(0f, 1f)),
}

enum class Axe { HORIZONTAL, VERTICAL }

/** La direction d'un geste, une fois l'axe verrouillé. Le vecteur est le déplacement de la caméra. */
enum class Direction(val vecteur: Offset, val axe: Axe) {
    VERS_LA_GAUCHE(Offset(-1f, 0f), Axe.HORIZONTAL),
    VERS_LA_DROITE(Offset(1f, 0f), Axe.HORIZONTAL),
    VERS_LE_HAUT(Offset(0f, -1f), Axe.VERTICAL),
    VERS_LE_BAS(Offset(0f, 1f), Axe.VERTICAL),
}

/** Le voisin atteignable, ou `null` s'il n'y en a pas — auquel cas le geste ne déplace rien. */
fun Ecran.versLe(direction: Direction): Ecran? = when (this) {
    Ecran.CENTRE -> Ecran.entries.first { it.camera == direction.vecteur }
    else -> Ecran.CENTRE.takeIf { camera + direction.vecteur == Ecran.CENTRE.camera }
}

/** La direction d'un écart, sur l'axe déjà verrouillé. `null` tant que l'écart est nul. */
fun directionDe(ecart: Offset, axe: Axe): Direction? {
    val composante = composante(ecart, axe)
    if (composante == 0f) return null

    return Direction.entries.first { it.axe == axe && (it.vecteur.x + it.vecteur.y > 0f) == (composante > 0f) }
}

fun composante(point: Offset, axe: Axe): Float = if (axe == Axe.HORIZONTAL) point.x else point.y

/**
 * La course ouverte depuis [depuis] sur cet axe — l'écran de départ et ses voisins, rien de plus.
 *
 * ⭐ **Elle ne dépend pas du sens du geste, et c'est ce qui la rend sûre.** Une borne calculée
 * direction par direction suppose qu'on parte pile sur un écran ; reprendre le monde en pleine
 * traversée le ferait alors sauter d'un bloc au premier contact. Une course fixe se contente de
 * retenir la caméra là où elle est.
 */
fun course(depuis: Ecran, axe: Axe): ClosedFloatingPointRange<Float> {
    val atteignables = Direction.entries
        .filter { it.axe == axe }
        .mapNotNull { depuis.versLe(it) }
        .plus(depuis)
        .map { composante(it.camera, axe) }

    return atteignables.min()..atteignables.max()
}

/**
 * La caméra ramenée dans la course ouverte.
 *
 * ⭐ **Butée franche, sans élastique.** Quand il n'y a pas de voisin, le décor ne bouge pas d'un
 * pixel plutôt que de céder puis revenir : un mouvement qui part et se rétracte demande à être
 * interprété (« est-ce que ça a marché ? »), et c'est exactement ce que le dispositif ne fait
 * jamais faire à Xavier.
 */
fun bornerCamera(brut: Offset, depuis: Ecran, axe: Axe): Offset {
    val bornes = course(depuis, axe)

    return when (axe) {
        Axe.HORIZONTAL -> Offset(brut.x.coerceIn(bornes), brut.y)
        Axe.VERTICAL -> Offset(brut.x, brut.y.coerceIn(bornes))
    }
}

/**
 * L'écran où l'on atterrit quand le doigt se lève.
 *
 * Deux façons d'arriver au bout, et il en fallait deux : **la distance** — on a poussé le monde au
 * moins jusqu'à [SEUIL_BASCULE] — ou **l'élan** — on l'a lancé au moins à [VITESSE_BASCULE]. Sur la
 * distance seule, un geste vif et court échouait : le doigt partait plus vite qu'il n'allait loin,
 * et le monde revenait en arrière alors que le geste était sans ambiguïté.
 *
 * ⭐ **Un élan qui repart en arrière annule la traversée**, même si la distance est franchie : le
 * doigt s'est ravisé avant de se lever, et le dernier sens voulu est celui-là.
 *
 * [elan] est en écrans par seconde, déjà projeté sur l'axe verrouillé.
 */
fun aterrissage(camera: Offset, elan: Offset, depuis: Ecran, axe: Axe): Ecran {
    val ecart = camera - depuis.camera
    val direction = directionDe(ecart, axe) ?: return depuis
    val voisin = depuis.versLe(direction) ?: return depuis

    val lance = composante(elan, axe) * composante(direction.vecteur, axe)
    val franchi = when {
        lance >= VITESSE_BASCULE -> true
        lance <= -VITESSE_BASCULE -> false
        else -> abs(composante(ecart, axe)) >= SEUIL_BASCULE
    }

    return if (franchi) voisin else depuis
}

/**
 * Le seuil est court exprès : un geste franc mais bref doit suffire, sans quoi il faudrait traîner
 * le doigt sur un cinquième de l'écran.
 */
const val SEUIL_BASCULE = 0.18f

/** En écrans par seconde. Un geste posé finit sous cette valeur, un geste lancé la dépasse. */
const val VITESSE_BASCULE = 0.7f
