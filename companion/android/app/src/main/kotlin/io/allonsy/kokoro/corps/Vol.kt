package io.allonsy.kokoro.corps

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.sin

// Bat toujours sur l'horloge de la respiration (RESPIRATION_MILLIS) — jamais une horloge à part, sinon rythme involontaire.
enum class Vol { AUCUN, LEVITATION, SOMMEIL, TRAVERSEE }

data class Deplacement(val decalage: Offset, val inclinaison: Float)

const val LEVITATION_AMPLITUDE = 0.09f * HAUTEUR_PERSONNAGE

// Déphasage d'un quart de tour avec le souffle : les deux mouvements ne se renforcent jamais.
const val LEVITATION_DEPHASAGE = PI.toFloat() / 2f

// négatif = vers le haut, jamais sous la pose dessinée (l'ombre passerait devant).
fun levitation(phase: Float): Float =
    -LEVITATION_AMPLITUDE * (sin(phase + LEVITATION_DEPHASAGE) + 1f) / 2f

// Même horloge divisée par deux, pas une seconde horloge — elle se referme sans saut grâce à HORLOGE_MILLIS.
fun levitationLente(phase: Float): Float = levitation(phase / 2f) / 2f

private const val TRAVERSEE_PORTEE = 46f

private const val TRAVERSEE_MILLIS = 7_000

// Pas d'horloge à elle : une bascule à sa propre période battrait contre les deux autres.
private const val TRAVERSEE_INCLINAISON = 3f

fun Vol.deplacement(phase: Float, avance: Float): Deplacement = when (this) {
    Vol.AUCUN -> Deplacement(Offset.Zero, 0f)

    Vol.LEVITATION -> Deplacement(Offset(0f, levitation(phase)), 0f)

    Vol.SOMMEIL -> Deplacement(Offset(0f, levitationLente(phase)), 0f)

    Vol.TRAVERSEE -> Deplacement(
        decalage = Offset(TRAVERSEE_PORTEE * avance, levitation(phase)),
        inclinaison = -TRAVERSEE_INCLINAISON * avance,
    )
}

data class Ombre(
    val demiLargeur: Float = DEMI_LARGEUR_OMBRE,
    val aplatissement: Float = APLATISSEMENT_OMBRE,
    val sol: Float = BAS_PIEDS + DECALAGE_SOL_OMBRE,
    val noyau: Float = NOYAU_OMBRE,
)

val DEMI_LARGEUR_OMBRE = (EPAULE_DROITE.x - EPAULE_GAUCHE.x) / 2f

const val APLATISSEMENT_OMBRE = 0.16f
const val NOYAU_OMBRE = 0.45f

// Borné à ≈4,8 par HAUTEUR_VUE — au-delà, `CorpsInvariantsTest` échoue.
const val DECALAGE_SOL_OMBRE = 4f

const val OPACITE_OMBRE_PROCHE = 0.22f
const val OPACITE_OMBRE_LOINTAINE = 0.08f

// Référence LEVITATION_AMPLITUDE même en vol du sommeil, pourtant deux fois plus bas — c'est voulu.
fun Ombre.opaciteA(hauteur: Float): Float {
    val fraction = (-hauteur / LEVITATION_AMPLITUDE).coerceIn(0f, 1f)
    return OPACITE_OMBRE_PROCHE + (OPACITE_OMBRE_LOINTAINE - OPACITE_OMBRE_PROCHE) * fraction
}

fun Vol.ombre(): Ombre? = if (this == Vol.AUCUN) null else Ombre()

@Composable
fun deplacementAnime(vol: Vol, phase: Float): Deplacement =
    vol.deplacement(phase, avance = if (vol == Vol.TRAVERSEE) avanceAnimee() else 0f)

@Composable
private fun avanceAnimee(): Float {
    val avance by rememberInfiniteTransition(label = "traversee").animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(TRAVERSEE_MILLIS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "traversee-avance",
    )
    return avance
}
