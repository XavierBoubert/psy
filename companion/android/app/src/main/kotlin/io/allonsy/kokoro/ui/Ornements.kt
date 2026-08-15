package io.allonsy.kokoro.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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

/**
 * Les ornements — **du décor pur** (`companion/INTERFACE.md` §4.2).
 *
 * 🔴 **Aucun n'est jamais porteur d'information.** Une étincelle ne dit pas *nouveau*, un cœur ne
 * dit pas *important*, un rivet ne dit rien du tout. **Et aucun ne se pose sur une carte de la
 * liste** : ils vivent sur les bandes de titre, les états vides et autour de Kokoro — les endroits
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

/**
 * Les Zzz du sommeil — `PRESENCE.md` §4.5.
 *
 * 🔴 **Ils n'informent de rien que le cadre vide ne dise déjà en toutes lettres.** Le texte de
 * l'état vide reste affiché sous eux : ne pas les voir, ou ne pas les comprendre, ne fait donc rien
 * perdre.
 *
 * 🔴 **Ils ne clignotent pas et ne montent pas.** Un Zzz qui pulse serait un mouvement continu dans
 * le champ (§4.3), donc un rythme à décoder. Ils **paraissent en fondu et se tiennent** — c'est
 * l'appelant qui porte le fondu, parce que c'est lui qui sait quand le sommeil commence.
 *
 * 🔴 **Aucune lettre n'est écrite ici** : ce sont trois tracés, comme le cœur et l'étincelle. Le
 * décor ne porte jamais de texte (**P3**).
 */
@Composable
fun Zzz(modifier: Modifier = Modifier, taille: Dp = 26.dp) {
    val palette = LocalPaletteKokoro.current
    Canvas(modifier.size(taille)) { zzz(palette.contour, TRAIT_ORNEMENT.toPx()) }
}

/** Trois Z qui s'éloignent de la tête en grandissant — l'ordre du plus petit au plus grand. */
private fun DrawScope.zzz(couleur: Color, trait: Float) {
    val poses = listOf(
        Offset(0.00f, 0.72f) to 0.28f,
        Offset(0.28f, 0.36f) to 0.34f,
        Offset(0.56f, 0.00f) to 0.42f,
    )
    poses.forEach { (coin, part) ->
        val cote = part * size.minDimension
        val origine = Offset(coin.x * size.width, coin.y * size.height)
        drawPath(
            path = lettreZ(origine, cote),
            color = couleur,
            style = Stroke(
                width = trait,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )
    }
}

/** Une barre, une diagonale, une barre — le Z se trace, il ne s'écrit pas. */
private fun lettreZ(origine: Offset, cote: Float): Path = Path().apply {
    moveTo(origine.x, origine.y)
    lineTo(origine.x + cote, origine.y)
    lineTo(origine.x, origine.y + cote)
    lineTo(origine.x + cote, origine.y + cote)
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
