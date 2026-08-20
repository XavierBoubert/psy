package io.allonsy.kokoro.monde

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R

// Reprend l'écran de démarrage d'Android trait pour trait : même fond que `windowSplashScreenBackground`,
// même couche avant de l'icône adaptative, cerclée aux mêmes 192 dp pour ses 288 dp d'image.
private val FOND = Color(0xFF101214)
private val CERCLE = 192.dp
private val ICONE = 288.dp
private const val FONDU_MS = 400

@Composable
fun VoileDeDemarrage(visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = EnterTransition.None,
        exit = fadeOut(animationSpec = tween(durationMillis = FONDU_MS)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FOND),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(CERCLE)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.mipmap.ic_lanceur_avant),
                    contentDescription = null,
                    modifier = Modifier.requiredSize(ICONE),
                )
            }
        }
    }
}
