package io.allonsy.kokoro.corps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp

/**
 * Le locuteur — **Kokoro dans un panneau plein écran**, `PRESENCE.md` §1.1, §1.4 et étape **E12**.
 *
 * ⭐ **C'est l'autre régime, et il n'y en a que deux.** L'habitant vit dans le décor et porte une
 * **posture** ; le locuteur se tient en bas à gauche d'une bulle de discussion et ne porte qu'une
 * **expression**. 🔴 **Jamais les deux à la fois** (§1.1) : [locuteurEnScene] et [habitantEnScene]
 * sont les deux moitiés d'une même bascule, elles ne peuvent pas être vraies ensemble, et rien
 * d'autre dans le dispositif ne décide de qui est à l'écran.
 *
 * 🔴 **Il est immobile hors expression** (§4.1) : aucun vol, donc **aucune ombre** — elle tomberait
 * sur l'interface —, aucun balayage, aucun geste, aucun transit. **Il respire et il cligne**, parce
 * que ce sont le corps lui-même (`CORPS.md` §5) et non des mouvements ajoutés : E13 devra, sur
 * l'écran de crise, *retirer* le clignement en toutes lettres, et c'est bien la preuve qu'ailleurs
 * il reste.
 *
 * 🔴 **Une carte de liste n'est pas une bulle de discussion** (§1.1). Ce fichier ne sait poser un
 * personnage que dans une page plein écran ; il n'existe aucun moyen d'en accrocher un à une carte.
 *
 * ⭐ **Le panneau lui garde sa bande, faite ou non** : [Locuteur] occupe toujours sa place, même
 * quand [present] est faux. Sans ça, le texte se remettrait en page à son arrivée — un mouvement
 * qu'on n'a pas demandé, pendant qu'on lit.
 */

/**
 * §1.4 — **la hauteur du personnage**, pas celle de sa vue.
 *
 * ⭐ **Portée à 150 dp** *(demande de Xavier, 16/08/2026)* — l'habitant du monde vaut désormais
 * lui-même 110 dp ([io.allonsy.kokoro.monde.HAUTEUR_HABITANT]) : sans cet écart, le personnage
 * n'aurait plus rien à gagner en arrivant sur le panneau. **Ce qui doit se lire est un visage**, donc
 * c'est la tête qui fixe l'échelle, et le reste en découle.
 *
 * ⏳ **À revérifier sur l'appareil.**
 */
val HAUTEUR_LOCUTEUR = 150.dp

/**
 * La coupe du cadrage — **le thorax** (§1.1).
 *
 * ⭐ **Elle passe sous le 心 et au-dessus de la ligne du ventre** : le cœur est entier dans le
 * cadre, et aucun trait du dessin n'est tranché en son milieu. 🔴 **Ce n'est pas une taille de
 * fenêtre choisie à l'œil, c'est un point du dessin** — le centre du ventre, celui-là même autour
 * duquel les pieds tournent.
 *
 * ⚠️ **Le personnage n'est pas coupé au milieu du panneau** : la bande est posée en bas de la page,
 * donc la coupe tombe sur le bord de la dalle. Ce qui manque est **hors de l'écran**, pas effacé.
 */
val COUPE_LOCUTEUR = CENTRE_VENTRE.y

private val UNITE_LOCUTEUR = unitePour(HAUTEUR_LOCUTEUR)

/** La bande qu'une page réserve au locuteur : la vue entière, coupée au thorax. */
val LARGEUR_BANDE_LOCUTEUR = UNITE_LOCUTEUR * LARGEUR_VUE
val HAUTEUR_BANDE_LOCUTEUR = UNITE_LOCUTEUR * COUPE_LOCUTEUR

/**
 * La parution du locuteur, **quand l'habitant vient de sortir du champ**.
 *
 * ⭐ **Il arrive, il ne surgit pas** *(demande de Xavier, 16/08/2026)* — un fondu, un léger glissé
 * depuis le bas et un grossissement : **c'est le Kokoro du monde qui vient se poser dans le coin du
 * panneau**, plus grand qu'il ne l'était dans le décor. Il ne joue qu'à l'ouverture : à la fermeture,
 * **le panneau l'emporte avec lui** en redescendant, donc il n'y a rien à faire disparaître.
 */
private const val PARUTION_LOCUTEUR_MILLIS = 500

/** L'ampleur du grossissement à l'arrivée — il part un peu plus petit, jamais à sa taille finale. */
private const val PARUTION_ECHELLE_DEPART = 0.7f

/** Le glissé qui accompagne l'arrivée — un quart de sa hauteur, jamais plus. */
private const val PARUTION_GLISSE_FRACTION = 4

/**
 * 🔴 **L'alternance des deux régimes, en une seule bascule** (§1.1) — [sortie] vaut 0 quand
 * l'habitant est à sa place et 1 quand il est hors champ.
 *
 * **Le locuteur n'entre que lorsque l'habitant est entièrement sorti**, et pas au moment où le
 * panneau commence à monter : pendant ces 320 ms le panneau ne couvre encore que le bas de l'écran,
 * et le locuteur, qui se tient précisément en bas à gauche, serait le premier visible — avec
 * l'habitant encore posé au-dessus. **Deux Kokoro à l'écran, et c'est exactement ce que §1.1
 * interdit.**
 */
fun locuteurEnScene(sortie: Float): Boolean = sortie >= 1f

fun habitantEnScene(sortie: Float): Boolean = sortie < 1f

/**
 * Le locuteur posé dans une page — **cadré au thorax, à l'échelle du visage.**
 *
 * ⭐ **Le cadre est celui du dessin, pas une découpe choisie** : la vue entière en largeur, coupée
 * au thorax en hauteur. La marge du dessin pose le personnage à peu près à l'aplomb du texte de la
 * page, sans qu'on ait eu à choisir une marge de plus. ⏳ **À juger à l'écran.**
 *
 * 🔴 **Il garde les couleurs du SVG, jour et nuit**, comme l'habitant : le décor change d'heure,
 * lui non. Le repeindre lui donnerait une seconde apparence à décoder.
 */
@Composable
fun Locuteur(expression: Expression, modifier: Modifier = Modifier, present: Boolean = true) {
    Box(
        modifier = modifier
            .size(width = LARGEUR_BANDE_LOCUTEUR, height = HAUTEUR_BANDE_LOCUTEUR)
            .clipToBounds(),
    ) {
        AnimatedVisibility(
            visible = present,
            enter = fadeIn(tween(PARUTION_LOCUTEUR_MILLIS)) +
                scaleIn(
                    initialScale = PARUTION_ECHELLE_DEPART,
                    animationSpec = tween(PARUTION_LOCUTEUR_MILLIS),
                    transformOrigin = TransformOrigin(0f, 1f),
                ) +
                slideInVertically(animationSpec = tween(PARUTION_LOCUTEUR_MILLIS)) {
                    it / PARUTION_GLISSE_FRACTION
                },
            exit = fadeOut(tween(PARUTION_LOCUTEUR_MILLIS)),
        ) {
            CorpsKokoro(
                rig = rigAnime(expression = expression),
                modifier = Modifier.size(cadrePour(HAUTEUR_LOCUTEUR)),
                palette = PALETTE_CLAIRE,
            )
        }
    }
}
