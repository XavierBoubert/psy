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

// 150 dp, le double de l'habitant (110 dp) : c'est la tête qui doit rester lisible en arrivant.
val HAUTEUR_LOCUTEUR = 150.dp

// Coupe au centre du ventre : le 心 reste entier dans le cadre, aucun trait n'est tranché.
val COUPE_LOCUTEUR = CENTRE_VENTRE.y

private val UNITE_LOCUTEUR = unitePour(HAUTEUR_LOCUTEUR)

val LARGEUR_BANDE_LOCUTEUR = UNITE_LOCUTEUR * LARGEUR_VUE
val HAUTEUR_BANDE_LOCUTEUR = UNITE_LOCUTEUR * COUPE_LOCUTEUR

private const val PARUTION_LOCUTEUR_MILLIS = 500

private const val PARUTION_ECHELLE_DEPART = 0.7f

private const val PARUTION_GLISSE_FRACTION = 4

// Bascule stricte : jamais vrais en même temps, sinon deux Kokoro se superposent à l'écran.
fun locuteurEnScene(sortie: Float): Boolean = sortie >= 1f

fun habitantEnScene(sortie: Float): Boolean = sortie < 1f

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
                // Palette fixe : contrairement à l'habitant, le locuteur ne suit pas le décor jour/nuit.
                palette = PALETTE_CLAIRE,
            )
        }
    }
}
