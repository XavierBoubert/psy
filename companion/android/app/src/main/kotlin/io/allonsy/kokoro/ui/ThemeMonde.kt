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
 * ⭐ **C'est désormais le thème de tout ce que Xavier voit** *(15/08/2026)* : le monde, les écrans de
 * crise, le check-in et les réglages. Le passage des quatre surfaces déjà éprouvées **a été demandé
 * par Xavier lui-même** — 🔴 **la prévisibilité est une fonctionnalité, et c'est la seule chose qui
 * autorisait à toucher à un écran qui sert en situation.**
 *
 * ⚠️ **[ThemeKokoro] survit pour les deux outils de mise au point** — l'alerte K1 et l'atelier du
 * corps. Ils ne sont pas des surfaces de soin : ils n'ont rien à gagner à changer de peau.
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
