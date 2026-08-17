package io.allonsy.kokoro.ui

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Teinte(val haut: Color, val bas: Color)

data class PaletteKokoro(
    val panneauHaut: Color,
    val panneauBas: Color,
    // Brun, jamais noir : un contour noir bascule le registre du jouet vers l'outil.
    val contour: Color,
    val reflet: Color,
    val creux: Color,
    val porte: Color,
    val encre: Color,
    val encreDouce: Color,
    // Cinq teintes, aucun rouge : aucune couleur ne doit pouvoir se lire comme urgence ou échec.
    val menthe: Teinte,
    val peche: Teinte,
    val lavande: Teinte,
    val azur: Teinte,
    val beurre: Teinte,
)

val KOKORO_JOUR = PaletteKokoro(
    panneauHaut = Color(0xFFFFF9F1),
    panneauBas = Color(0xFFFFE8D5),
    contour = Color(0xFF6E5A54),
    reflet = Color.White.copy(alpha = 0.95f),
    creux = Color(0xFFCEA082).copy(alpha = 0.32f),
    porte = Color(0xFF5C3E2E).copy(alpha = 0.38f),
    encre = Color(0xFF5C463E),
    encreDouce = Color(0xFF9C8378),
    menthe = Teinte(Color(0xFF7FD3B4), Color(0xFF52AE8D)),
    peche = Teinte(Color(0xFFFFAB8E), Color(0xFFE8836A)),
    lavande = Teinte(Color(0xFFBFA8E6), Color(0xFF9A80C7)),
    azur = Teinte(Color(0xFF8CC6EF), Color(0xFF66A3D0)),
    beurre = Teinte(Color(0xFFFFD98F), Color(0xFFE8B75F)),
)

val KOKORO_NUIT = PaletteKokoro(
    panneauHaut = Color(0xFF473E5C),
    panneauBas = Color(0xFF37304A),
    contour = Color(0xFF2C2438),
    reflet = Color.White.copy(alpha = 0.16f),
    creux = Color.Black.copy(alpha = 0.30f),
    porte = Color.Black.copy(alpha = 0.52f),
    encre = Color(0xFFFBF4EC),
    encreDouce = Color(0xFFB4A8C4),
    menthe = Teinte(Color(0xFF5FB79A), Color(0xFF3E8A72)),
    peche = Teinte(Color(0xFFE08C74), Color(0xFFB96852)),
    lavande = Teinte(Color(0xFFA38CCB), Color(0xFF7E67A6)),
    azur = Teinte(Color(0xFF6FA6CD), Color(0xFF4F82A9)),
    beurre = Teinte(Color(0xFFE0BC77), Color(0xFFBC9750)),
)

// Palette élue à l'arrivée selon la plage horaire du décor, jamais au thème système ni en direct.
val LocalPaletteKokoro = staticCompositionLocalOf { KOKORO_JOUR }

val CONTOUR = 4.dp

val EPAISSEUR = 7.dp

val RAYON = 26.dp

private val REFLET = 4.dp
private val CREUX = 7.dp
private val OMBRE_FLOU = 16.dp
private val OMBRE_RETRAIT = 6.dp
private val OMBRE_CHUTE = 8.dp

fun Modifier.matiere(
    palette: PaletteKokoro,
    couleur: Teinte? = null,
    rayon: Dp = RAYON,
    epaisseur: Dp = EPAISSEUR,
    enfoncement: Dp = 0.dp,
    ombre: Boolean = true,
    creuse: Boolean = false,
    debordHaut: Dp = 0.dp,
    epaisseurReflet: Dp = REFLET,
    epaisseurCreux: Dp = CREUX,
): Modifier = drawBehind {
    val ep = if (creuse) 0f else epaisseur.toPx()
    val enf = enfoncement.toPx().coerceIn(0f, ep)
    val boite = Rect(
        left = 0f,
        top = -debordHaut.toPx() + enf,
        right = size.width,
        bottom = size.height - ep + enf,
    )
    if (boite.width <= 0f || boite.height <= 0f) return@drawBehind

    val r = rayon.toPx().coerceAtMost(minOf(boite.width, boite.height) / 2f)
    val exterieur = RoundRect(boite, CornerRadius(r))
    val trait = CONTOUR.toPx()
    val interieur = RoundRect(boite.deflate(trait), CornerRadius((r - trait).coerceAtLeast(0f)))

    if (ombre && !creuse) {
        ombrePortee(exterieur, palette.porte, chute = ep - enf + OMBRE_CHUTE.toPx())
    }

    if (ep - enf > 0f) {
        translate(top = ep - enf) { drawPath(chemin(exterieur), palette.contour) }
    }

    val fond = couleur ?: Teinte(palette.panneauHaut, palette.panneauBas)
    drawPath(
        path = chemin(exterieur),
        brush = Brush.verticalGradient(
            colors = if (creuse) listOf(fond.bas, fond.haut) else listOf(fond.haut, fond.bas),
            startY = boite.top,
            endY = boite.bottom,
        ),
    )

    val clairEnHaut = if (creuse) palette.creux else reflet(palette, couleur)
    val sombreEnBas = if (creuse) palette.reflet else creux(palette, couleur)
    bande(interieur, versLeBas = true, epaisseur = if (creuse) epaisseurCreux else epaisseurReflet, couleur = clairEnHaut)
    bande(interieur, versLeBas = false, epaisseur = if (creuse) epaisseurReflet else epaisseurCreux, couleur = sombreEnBas)

    drawPath(
        path = chemin(RoundRect(boite.deflate(trait / 2f), CornerRadius((r - trait / 2f).coerceAtLeast(0f)))),
        color = palette.contour,
        style = Stroke(width = trait),
    )
}

// Sur une pièce colorée le reflet et le creux sont du blanc/noir voilés : les teintes du panneau neutre y vireraient au sale.
private fun reflet(palette: PaletteKokoro, couleur: Teinte?): Color =
    if (couleur == null) palette.reflet else Color.White.copy(alpha = 0.42f)

private fun creux(palette: PaletteKokoro, couleur: Teinte?): Color =
    if (couleur == null) palette.creux else Color.Black.copy(alpha = 0.14f)

private fun DrawScope.bande(forme: RoundRect, versLeBas: Boolean, epaisseur: Dp, couleur: Color) {
    val decalage = epaisseur.toPx() * if (versLeBas) 1f else -1f
    val pleine = chemin(forme)
    val decalee = chemin(forme).apply { translate(Offset(0f, decalage)) }
    drawPath(Path.combine(PathOperation.Difference, pleine, decalee), couleur)
}

private fun DrawScope.ombrePortee(forme: RoundRect, couleur: Color, chute: Float) {
    val retrait = OMBRE_RETRAIT.toPx()
    val boite = Rect(forme.left, forme.top, forme.right, forme.bottom).deflate(retrait)
    if (boite.width <= 0f || boite.height <= 0f) return

    drawIntoCanvasFloutee(couleur, OMBRE_FLOU.toPx() / 2f) { toile, peinture ->
        toile.drawRoundRect(
            left = boite.left,
            top = boite.top + chute,
            right = boite.right,
            bottom = boite.bottom + chute,
            radiusX = forme.topLeftCornerRadius.x,
            radiusY = forme.topLeftCornerRadius.y,
            paint = peinture,
        )
    }
}

private fun DrawScope.drawIntoCanvasFloutee(
    couleur: Color,
    flou: Float,
    dessin: (androidx.compose.ui.graphics.Canvas, Paint) -> Unit,
) {
    val peinture = Paint().apply { color = couleur }
    if (flou > 0f) {
        peinture.asFrameworkPaint().maskFilter = BlurMaskFilter(flou, BlurMaskFilter.Blur.NORMAL)
    }
    drawContext.canvas.let { dessin(it, peinture) }
}

private fun chemin(forme: RoundRect): Path = Path().apply { addRoundRect(forme) }

fun formeArrondie(taille: Size, rayon: Float): Path =
    chemin(RoundRect(Rect(Offset.Zero, taille), CornerRadius(rayon)))
