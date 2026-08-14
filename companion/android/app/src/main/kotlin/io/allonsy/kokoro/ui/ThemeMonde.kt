package io.allonsy.kokoro.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Le thème du monde — **panneaux extrudés** (`companion/INTERFACE.md` §4).
 *
 * ⚠️ **Il ne remplace pas [ThemeKokoro], il s'ajoute à côté.** Les surfaces déjà éprouvées sur le
 * téléphone — l'accès crise, la tension appliquée, le check-in, l'écran de contrôle — gardent leur
 * apparence actuelle tant que leur passage à cette matière n'a pas été **annoncé**. 🔴 **La
 * prévisibilité est une fonctionnalité** : aucun changement d'interface ni de format sans annonce,
 * et surtout pas sur un écran de crise qui sert en situation.
 *
 * ⭐ **La nuit est décidée par l'appelant, à l'arrivée** — jamais lue au fil de l'eau, jamais prise
 * au thème système (`companion/DECOR.md` §5, §4.3).
 */
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
