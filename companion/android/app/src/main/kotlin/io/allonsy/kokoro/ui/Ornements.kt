package io.allonsy.kokoro.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Les ornements — **du décor pur** (`companion/INTERFACE.md` §4.2).
 *
 * 🔴 **Aucun n'est jamais porteur d'information.** Une étincelle ne dit pas *nouveau*, un cœur ne
 * dit pas *important*, un rivet ne dit rien du tout. **Et aucun ne se pose sur une carte de la
 * liste** : ils vivent sur les bandes de titre, les états vides et l'écran central — les endroits
 * où il n'y a rien à lire de travers.
 *
 * 🔴 **L'écran de crise n'en porte aucun** (§4.5). En crise, la mignonnerie est du bruit.
 */

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

/** Le boulon décoratif des bandes de titre — deux par bande, aux deux bouts. */
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

/** Une étoile à quatre branches, aux flancs pincés — le scintillement du registre. */
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

/** Deux lobes et une pointe. */
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

/**
 * La bannière à bouts crantés du titre d'un écran — deux V taillés dans les côtés courts.
 *
 * Elle n'est pas un ornement : **elle porte le nom de l'écran**, et c'est la seule chose qui dise
 * où l'on est (D11).
 */
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
