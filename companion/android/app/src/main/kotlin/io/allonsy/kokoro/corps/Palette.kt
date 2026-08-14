package io.allonsy.kokoro.corps

import androidx.compose.ui.graphics.Color

/**
 * Les couleurs du personnage — `design/CORPS.md` §4.
 *
 * ⭐ **La v2 n'a plus d'accent.** La plaque de poitrine céladon a disparu avec la v1 : le 心 est
 * tracé à l'encre, sur le corps, et le dessin est entièrement monochrome. Il n'y a donc plus que
 * trois valeurs à peindre, et aucune n'est un signal.
 */
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

/** Les trois valeurs du SVG de Xavier, telles quelles. */
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
