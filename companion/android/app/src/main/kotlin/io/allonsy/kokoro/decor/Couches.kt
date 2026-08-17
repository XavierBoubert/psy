package io.allonsy.kokoro.decor

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import io.allonsy.kokoro.R

enum class Ancrage { HAUT, BAS }

data class Couche(
    @DrawableRes val image: Int,
    val profondeur: Float,
    val ancrage: Ancrage,
    val largeur: Float,
    val decalage: Float,
    val marge: Float = 0f,
) {
    val enMiroir: Boolean get() = marge <= 0f

    val pas: Float get() = largeur * (1f - 2f * marge)
}

val COUCHES: List<Couche> = listOf(
    Couche(R.drawable.decor_nuages_loin, profondeur = 0.14f, ancrage = Ancrage.HAUT, largeur = 1.40f, decalage = 0.01f),
    Couche(R.drawable.decor_nuages_pres, profondeur = 0.30f, ancrage = Ancrage.HAUT, largeur = 2.40f, decalage = 0.06f, marge = 0.16f),
    Couche(R.drawable.decor_collines, profondeur = 0.52f, ancrage = Ancrage.BAS, largeur = 3.60f, decalage = 0.055f),
    Couche(R.drawable.decor_feuillage, profondeur = 0.78f, ancrage = Ancrage.BAS, largeur = 1.90f, decalage = 0.080f, marge = 0.16f),
)

data class PaletteDecor(
    val ciel: List<Color>,
    val teinte: Color?,
)

val DECOR_JOUR = PaletteDecor(
    ciel = listOf(Color(0xFF1FA9CE), Color(0xFF7FD4E6), Color(0xFFC8ECF2)),
    teinte = null,
)

val DECOR_NUIT = PaletteDecor(
    ciel = listOf(Color(0xFF08202E), Color(0xFF103648), Color(0xFF1A4A63)),
    teinte = Color(0xFF4C7691),
)
