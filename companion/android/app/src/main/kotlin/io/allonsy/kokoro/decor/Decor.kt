package io.allonsy.kokoro.decor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun Decor(camera: () -> Float, palette: PaletteDecor, modifier: Modifier = Modifier) {
    val images = COUCHES.map { ImageBitmap.imageResource(it.image) }
    val filtre = palette.teinte?.let { ColorFilter.tint(it, BlendMode.Modulate) }

    Canvas(modifier.fillMaxSize().clipToBounds()) {
        val vue = camera()

        drawRect(brush = Brush.verticalGradient(palette.ciel))
        COUCHES.forEachIndexed { rang, couche -> dessinerCouche(couche, images[rang], vue, filtre) }
    }
}

private fun DrawScope.dessinerCouche(
    couche: Couche,
    image: ImageBitmap,
    camera: Float,
    filtre: ColorFilter?,
) {
    val largeur = size.width * couche.largeur
    val hauteur = largeur * image.height / image.width
    val pas = size.width * couche.pas

    val haut = when (couche.ancrage) {
        Ancrage.HAUT -> couche.decalage * size.height
        Ancrage.BAS -> size.height - hauteur + couche.decalage * size.height
    }

    val origine = (size.width - largeur) / 2f - camera * size.width * couche.profondeur
    val premier = floor((-largeur - origine) / pas).toInt() + 1

    generateSequence(premier) { it + 1 }
        .map { rang -> rang to origine + rang * pas }
        .takeWhile { (_, gauche) -> gauche < size.width }
        .forEach { (rang, gauche) ->
            dessinerTuile(image, gauche, haut, largeur, hauteur, couche.enMiroir && enMiroir(rang), filtre)
        }
}

private fun enMiroir(rang: Int): Boolean = (rang % 2 + 2) % 2 == 1

private fun DrawScope.dessinerTuile(
    image: ImageBitmap,
    gauche: Float,
    haut: Float,
    largeur: Float,
    hauteur: Float,
    miroir: Boolean,
    filtre: ColorFilter?,
) {
    withTransform({
        if (miroir) scale(-1f, 1f, Offset(gauche + largeur / 2f, 0f))
    }) {
        drawImage(
            image = image,
            srcOffset = IntOffset(0, 0),
            srcSize = IntSize(image.width, image.height),
            dstOffset = IntOffset(gauche.roundToInt(), haut.roundToInt()),
            dstSize = IntSize(largeur.roundToInt(), hauteur.roundToInt()),
            colorFilter = filtre,
            filterQuality = FilterQuality.High,
        )
    }
}
