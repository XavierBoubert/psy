package io.allonsy.kokoro.corps

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/** Cycle complet inspiration + expiration (CORPS.md §5). Il ne change jamais. */
const val RESPIRATION_MILLIS = 4_500

/** Toute transition d'expression ou de posture (invariant §5.6). */
const val TRANSITION_MILLIS = 800

const val CLIGNEMENT_MILLIS = 200
private const val CLIGNEMENT_MORPHING_MILLIS = 80
private const val CLIGNEMENT_ATTENTE_MIN_MILLIS = 20_000L
private const val CLIGNEMENT_ATTENTE_MAX_MILLIS = 45_000L

private const val AMPLITUDE_VERTICALE = 9f
private const val AMPLITUDE_LATERALE = 5f
private const val AMPLITUDE_INCLINAISON = 3f
private const val TRAVERSEE_PORTEE = 46f
private const val TRAVERSEE_MILLIS = 7_000

/** Le déplacement de la racine — c'est le vol. */
enum class Vol { AUCUN, FLOTTEMENT, TRAVERSEE }

data class Deplacement(val decalage: Offset, val inclinaison: Float)

@Composable
fun rigAnime(posture: Posture, vol: Vol = Vol.AUCUN): RigKokoro = with(posture.reglage()) {
    rigAnime(
        expression = expression,
        panneauAllume = panneauAllume,
        ouvertureBrasGauche = ouvertureBrasGauche,
        ouvertureBrasDroit = ouvertureBrasDroit,
        regard = regard,
        echelle = echelle,
        vol = vol,
    )
}

@Composable
fun rigAnime(
    expression: Expression,
    panneauAllume: Boolean = true,
    ouvertureBrasGauche: Float = OUVERTURE_REPOS,
    ouvertureBrasDroit: Float = OUVERTURE_REPOS,
    orbitePiedGauche: Float = 0f,
    orbitePiedDroit: Float = 0f,
    regard: Float = expression.regardParDefaut,
    echelle: Float = 1f,
    vol: Vol = Vol.AUCUN,
): RigKokoro {
    val ferme = clignementAnime(expression, actif = panneauAllume)
    val visage = visageAnime(if (ferme) Expression.CLIGNEMENT else expression)
    val souffle = respirationAnimee()
    val mouvement = deplacementAnime(vol)
    val brasGauche by animateFloatAsState(ouvertureBrasGauche, transition(), label = "bras-gauche")
    val brasDroit by animateFloatAsState(ouvertureBrasDroit, transition(), label = "bras-droit")
    val piedGauche by animateFloatAsState(orbitePiedGauche, transition(), label = "pied-gauche")
    val piedDroit by animateFloatAsState(orbitePiedDroit, transition(), label = "pied-droit")
    val oeillade by animateFloatAsState(regard, transition(), label = "regard")
    val taille by animateFloatAsState(echelle, transition(), label = "echelle")

    return RigKokoro(
        visage = visage,
        panneauAllume = panneauAllume,
        respiration = souffle,
        ouvertureBrasGauche = brasGauche,
        ouvertureBrasDroit = brasDroit,
        orbitePiedGauche = piedGauche,
        orbitePiedDroit = piedDroit,
        regard = oeillade,
        decalage = mouvement.decalage,
        inclinaison = mouvement.inclinaison,
        echelle = taille,
    )
}

private fun transition() = tween<Float>(TRANSITION_MILLIS, easing = FastOutSlowInEasing)

/**
 * Sinusoïde continue, sans temps d'arrêt : la phase avance linéairement et se referme sur elle-même,
 * donc le retour à zéro ne fait pas de saut.
 */
@Composable
private fun respirationAnimee(): Float {
    val phase by rememberInfiniteTransition(label = "respiration").phase(RESPIRATION_MILLIS, "souffle")
    return (sin(phase) + 1f) / 2f
}

/** Rythme irrégulier mais borné — un clignement régulier deviendrait un métronome (§5). */
@Composable
private fun clignementAnime(expression: Expression, actif: Boolean): Boolean {
    var ferme by remember { mutableStateOf(false) }
    LaunchedEffect(expression, actif) {
        ferme = false
        if (!actif || !expression.yeuxOuverts) return@LaunchedEffect
        while (true) {
            delay(Random.nextLong(CLIGNEMENT_ATTENTE_MIN_MILLIS, CLIGNEMENT_ATTENTE_MAX_MILLIS))
            ferme = true
            delay(CLIGNEMENT_MILLIS.toLong())
            ferme = false
        }
    }
    return ferme
}

/** Les formes se déforment l'une vers l'autre — le morphing est dans [MorphingVisage.kt][Contour]. */
@Composable
private fun visageAnime(cible: Expression): Visage {
    var visage by remember { mutableStateOf(Visage.de(cible)) }
    val progression = remember { Animatable(1f) }
    LaunchedEffect(cible) {
        if (visage.vers == cible) return@LaunchedEffect
        val depuis = if (progression.value < 0.5f) visage.depuis else visage.vers
        visage = Visage(depuis, cible, 0f)
        progression.snapTo(0f)
        progression.animateTo(
            targetValue = 1f,
            animationSpec = tween(dureeMorphing(depuis, cible), easing = FastOutSlowInEasing),
        )
    }
    return visage.copy(progression = progression.value)
}

private fun dureeMorphing(depuis: Expression, vers: Expression): Int = when {
    depuis == Expression.CLIGNEMENT || vers == Expression.CLIGNEMENT -> CLIGNEMENT_MORPHING_MILLIS
    else -> TRANSITION_MILLIS
}

@Composable
private fun deplacementAnime(vol: Vol): Deplacement = when (vol) {
    Vol.AUCUN -> Deplacement(Offset.Zero, 0f)
    Vol.FLOTTEMENT -> flottementAnime()
    Vol.TRAVERSEE -> traverseeAnimee()
}

@Composable
private fun flottementAnime(): Deplacement {
    val transition = rememberInfiniteTransition(label = "flottement")
    val hauteur by transition.phase(3_200, "flottement-hauteur")
    val lateral by transition.phase(5_100, "flottement-lateral")
    val bascule by transition.phase(4_300, "flottement-bascule")
    return Deplacement(
        decalage = Offset(
            x = AMPLITUDE_LATERALE * sin(lateral),
            y = -AMPLITUDE_VERTICALE * (sin(hauteur) + 1f) / 2f,
        ),
        inclinaison = AMPLITUDE_INCLINAISON * sin(bascule),
    )
}

@Composable
private fun traverseeAnimee(): Deplacement {
    val transition = rememberInfiniteTransition(label = "traversee")
    val avance by transition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(TRAVERSEE_MILLIS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "traversee-avance",
    )
    val hauteur by transition.phase(2_600, "traversee-hauteur")
    return Deplacement(
        decalage = Offset(
            x = TRAVERSEE_PORTEE * avance,
            y = -AMPLITUDE_VERTICALE * (sin(hauteur) + 1f) / 2f,
        ),
        inclinaison = -AMPLITUDE_INCLINAISON * avance,
    )
}

/** Une phase en radians qui tourne indéfiniment, sans discontinuité au bouclage. */
@Composable
private fun InfiniteTransition.phase(dureeMillis: Int, label: String): State<Float> = animateFloat(
    initialValue = 0f,
    targetValue = 2f * PI.toFloat(),
    animationSpec = infiniteRepeatable(
        animation = tween(dureeMillis, easing = LinearEasing),
        repeatMode = RepeatMode.Restart,
    ),
    label = label,
)
