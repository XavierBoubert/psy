package io.allonsy.kokoro.corps

import androidx.compose.ui.graphics.Color

data class PaletteCorps(
    val fond: Color,
    val trait: Color,
    val remplissage: Color,
    val panneau: Color,
    val accent: Color,
)

val PALETTE_CLAIRE = PaletteCorps(
    fond = Color(0xFFF4F1EA),
    trait = Color(0xFF2B2F33),
    remplissage = Color(0xFFFBF9F5),
    panneau = Color(0xFFE6E2DA),
    accent = Color(0xFF8FA99B),
)

val PALETTE_SOMBRE = PaletteCorps(
    fond = Color(0xFF14171A),
    trait = Color(0xFFD8D4CC),
    remplissage = Color(0xFF1C2024),
    panneau = Color(0xFF22262B),
    accent = Color(0xFF8FA99B),
)
