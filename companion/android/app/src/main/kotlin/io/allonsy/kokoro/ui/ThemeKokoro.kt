package io.allonsy.kokoro.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FOND = Color(0xFF101214)
private val SURFACE = Color(0xFF181B1E)
private val TRAIT = Color(0xFFE6E8EA)
private val TRAIT_SECONDAIRE = Color(0xFF9AA0A6)
private val ACCENT = Color(0xFF7FA8C9)

private val SCHEMA = darkColorScheme(
    primary = ACCENT,
    onPrimary = FOND,
    background = FOND,
    onBackground = TRAIT,
    surface = SURFACE,
    onSurface = TRAIT,
    onSurfaceVariant = TRAIT_SECONDAIRE,
    outline = TRAIT_SECONDAIRE,
)

@Composable
fun ThemeKokoro(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = SCHEMA, content = content)
}
