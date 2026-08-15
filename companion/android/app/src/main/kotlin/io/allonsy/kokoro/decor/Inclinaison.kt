package io.allonsy.kokoro.decor

import kotlin.math.atan2
import kotlin.math.hypot

/**
 * L'inclinaison latérale au-delà de laquelle le décor ne va pas plus loin, en degrés.
 *
 * ⭐ **Une butée est indispensable ici, alors qu'il n'y en a aucune sur le doigt.** Le doigt a un
 * bout — il se lève ; un téléphone retourné n'en a pas, et sans butée le décor partirait à l'infini
 * quand on repose l'appareil face contre table.
 *
 * 🔄 **Ramenée de 26° à 18° le 15/08/2026** *(retour de Xavier : le déplacement était trop court)*.
 * 🔴 **C'était la moitié du problème, et la moins visible** : un mouvement de poignet ordinaire vaut
 * huit à dix degrés, donc la course n'était **jamais parcourue** — on n'en voyait qu'un tiers, et
 * augmenter le seul débattement n'aurait corrigé qu'un bout de terrain qu'on n'atteignait pas.
 */
const val INCLINAISON_MAX_DEGRES: Float = 18f

/**
 * Le débattement, en écrans de caméra, atteint à [INCLINAISON_MAX_DEGRES].
 *
 * 🔄 **0,18 → 0,40 le 15/08/2026** *(retour de Xavier)*. En bout de course, le feuillage
 * *(profondeur 0,78)* se déplace désormais de 31 % de la largeur de l'écran et les nuages lointains
 * *(0,14)* de 5,6 %.
 *
 * ⭐ **Ça ne peut toujours pas faire changer d'écran, et pas parce que le nombre est petit** :
 * l'inclinaison n'entre jamais dans la caméra du contenu *(`monde/MondeKokoro.kt`)*. **Elle n'a
 * aucun chemin vers la traversée** — c'est la structure qui l'interdit, pas le réglage.
 */
const val DEBATTEMENT_INCLINAISON: Float = 0.40f

/**
 * Le déplacement de caméra que vaut une position du téléphone, à partir du vecteur de gravité —
 * `x` vers la droite de l'écran, `y` vers son haut, `z` vers l'utilisateur *(repère Android)*.
 *
 * ⭐ **C'est une position, pas un mouvement** *(15/08/2026)*. On ne lit pas la vitesse angulaire du
 * gyroscope, qu'il faudrait intégrer : on lit **où est le bas**, et le décor en découle. Trois
 * conséquences, et ce sont elles qui rendent le réglage compatible avec « le décor ne bouge jamais
 * tout seul » :
 *
 * - 🔴 **Aucune dérive.** Une intégration accumule son erreur et fait glisser le décor à l'arrêt ; un
 *   angle mesuré par rapport à la verticale n'accumule rien du tout.
 * - 🔴 **Aucun recentrage.** Rien ne revient à zéro derrière le dos de Xavier, parce qu'il n'y a
 *   aucune référence à rattraper — le zéro est la verticale, et elle ne bouge pas.
 * - ⭐ **C'est réversible exactement.** Reposer le téléphone comme il était remet le décor où il
 *   était, au pixel près. **Le décor est une fonction de la main, pas une mémoire du geste.**
 *
 * L'appareil est verrouillé en portrait *(manifeste)*, donc l'axe `x` du capteur est toujours la
 * droite de l'écran : il n'y a pas de rotation d'affichage à compenser.
 *
 * ⭐ **Une fenêtre, pas un niveau à bulle** : pencher le téléphone vers la droite découvre ce qui est
 * à droite, dans le même sens que le doigt qui pousse le monde vers la gauche.
 */
fun inclinaisonDeLaGravite(x: Float, y: Float, z: Float): Float {
    val aplomb = hypot(y, z)
    val degres = Math.toDegrees(atan2(x, aplomb).toDouble()).toFloat()

    return -(degres / INCLINAISON_MAX_DEGRES).coerceIn(-1f, 1f) * DEBATTEMENT_INCLINAISON
}
