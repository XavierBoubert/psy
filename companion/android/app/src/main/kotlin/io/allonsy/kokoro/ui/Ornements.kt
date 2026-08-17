package io.allonsy.kokoro.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

private val TRAIT_ORNEMENT = 2.dp

@Composable
fun Etincelle(modifier: Modifier = Modifier, taille: Dp = 16.dp) {
    val palette = LocalPaletteKokoro.current
    Canvas(modifier.size(taille)) {
        val forme = etincelle(size)
        drawPath(forme, palette.contour, style = Stroke(width = TRAIT_ORNEMENT.toPx() * 2f))
        drawPath(forme, palette.beurre.haut)
    }
}

@Composable
fun Coeur(modifier: Modifier = Modifier, taille: Dp = 20.dp) {
    val palette = LocalPaletteKokoro.current
    Canvas(modifier.size(taille, taille * 0.9f)) {
        val forme = coeur(size)
        drawPath(forme, palette.contour, style = Stroke(width = TRAIT_ORNEMENT.toPx() * 2f))
        drawPath(forme, palette.peche.haut)
    }
}

private const val ZZZ_CYCLE_MILLIS = 1_800
private const val ZZZ_DECALAGE = 1f / 3f

@Composable
fun Zzz(modifier: Modifier = Modifier, taille: Dp = 26.dp) {
    val palette = LocalPaletteKokoro.current
    val phase by rememberInfiniteTransition(label = "zzz").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(ZZZ_CYCLE_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "zzz-phase",
    )
    Canvas(modifier.size(taille)) { zzz(palette.contour, TRAIT_ORNEMENT.toPx(), phase) }
}

private fun DrawScope.zzz(couleur: Color, trait: Float, phase: Float) {
    val poses = listOf(
        Offset(0.00f, 0.72f) to 0.28f,
        Offset(0.28f, 0.36f) to 0.34f,
        Offset(0.56f, 0.00f) to 0.42f,
    )
    poses.forEachIndexed { rang, (coin, part) ->
        val cote = part * size.minDimension
        val origine = Offset(coin.x * size.width, coin.y * size.height)
        val decale = ((phase - rang * ZZZ_DECALAGE).mod(1f))
        val alpha = (sin(decale * 2f * PI.toFloat()) + 1f) / 2f
        drawPath(
            path = lettreZ(origine, cote),
            color = couleur.copy(alpha = alpha),
            style = Stroke(
                width = trait,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

private fun lettreZ(origine: Offset, cote: Float): Path = Path().apply {
    moveTo(origine.x, origine.y)
    lineTo(origine.x + cote, origine.y)
    lineTo(origine.x, origine.y + cote)
    lineTo(origine.x + cote, origine.y + cote)
}

@Composable
fun Rivet(modifier: Modifier = Modifier, taille: Dp = 13.dp) {
    val palette = LocalPaletteKokoro.current
    Canvas(modifier.size(taille)) {
        val rayon = size.minDimension / 2f
        val centre = Offset(size.width / 2f, size.height / 2f)
        drawCircle(palette.panneauBas, radius = rayon, center = centre)
        drawCircle(
            color = palette.contour,
            radius = rayon - 1.5.dp.toPx(),
            center = centre,
            style = Stroke(width = 3.dp.toPx()),
        )
    }
}

private fun etincelle(taille: Size): Path {
    val points = listOf(
        0.50f to 0.00f, 0.61f to 0.39f, 1.00f to 0.50f, 0.61f to 0.61f,
        0.50f to 1.00f, 0.39f to 0.61f, 0.00f to 0.50f, 0.39f to 0.39f,
    )
    return Path().apply {
        points.forEachIndexed { rang, (x, y) ->
            val point = Offset(x * taille.width, y * taille.height)
            if (rang == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
        }
        close()
    }
}

private fun coeur(taille: Size): Path {
    val l = taille.width
    val h = taille.height
    return Path().apply {
        moveTo(l / 2f, h)
        cubicTo(l * 0.08f, h * 0.62f, 0f, h * 0.34f, l * 0.14f, h * 0.14f)
        cubicTo(l * 0.30f, -h * 0.06f, l * 0.46f, h * 0.10f, l / 2f, h * 0.26f)
        cubicTo(l * 0.54f, h * 0.10f, l * 0.70f, -h * 0.06f, l * 0.86f, h * 0.14f)
        cubicTo(l, h * 0.34f, l * 0.92f, h * 0.62f, l / 2f, h)
        close()
    }
}

// Porte le titre de l'écran : ce n'est pas un ornement, contrairement au reste de ce fichier.
fun cheminRuban(taille: Size, cran: Float): Path = Path().apply {
    val bord = Rect(Offset.Zero, taille)
    moveTo(bord.left, bord.top)
    lineTo(bord.right, bord.top)
    lineTo(bord.right - cran, bord.center.y)
    lineTo(bord.right, bord.bottom)
    lineTo(bord.left, bord.bottom)
    lineTo(bord.left + cran, bord.center.y)
    close()
}
