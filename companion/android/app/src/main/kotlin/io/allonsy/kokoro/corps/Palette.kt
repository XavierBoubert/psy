package io.allonsy.kokoro.corps

import androidx.compose.ui.graphics.Color

// ⭐ v2 sans accent : plus de plaque de poitrine, tout est monochrome (companion/README.md §6)
data class PaletteCorps(
    val fond: Color,
    val trait: Color,
    val coque: Color,
    val panneau: Color,
)

fun PaletteCorps.couleur(remplissage: Remplissage): Color? = when (remplissage) {
    Remplissage.AUCUN -> null
    Remplissage.COQUE -> coque
    Remplissage.PANNEAU -> panneau
    Remplissage.ENCRE -> trait
}

val PALETTE_CLAIRE = PaletteCorps(
    fond = Color(0xFFF4F1EA),
    trait = Color(0xFF383838),
    coque = Color(0xFFFAF7F0),
    panneau = Color(0xFFE5DFD4),
)

val PALETTE_SOMBRE = PaletteCorps(
    fond = Color(0xFF14171A),
    trait = Color(0xFFD8D4CC),
    coque = Color(0xFF1C2024),
    panneau = Color(0xFF22262B),
)
