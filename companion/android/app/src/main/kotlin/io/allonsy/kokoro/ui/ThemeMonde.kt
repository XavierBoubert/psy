package io.allonsy.kokoro.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ThemeMonde(nuit: Boolean, content: @Composable () -> Unit) {
    val palette = if (nuit) KOKORO_NUIT else KOKORO_JOUR
    val schema = if (nuit) {
        darkColorScheme(
            background = palette.panneauBas,
            onBackground = palette.encre,
            surface = palette.panneauHaut,
            onSurface = palette.encre,
            onSurfaceVariant = palette.encreDouce,
            outline = palette.contour,
        )
    } else {
        lightColorScheme(
            background = palette.panneauBas,
            onBackground = palette.encre,
            surface = palette.panneauHaut,
            onSurface = palette.encre,
            onSurfaceVariant = palette.encreDouce,
            outline = palette.contour,
        )
    }

    MaterialTheme(colorScheme = schema) {
        CompositionLocalProvider(
            LocalPaletteKokoro provides palette,
            LocalContentColor provides palette.encre,
            content = content,
        )
    }
}
