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

/**
 * Débattement vertical d'une couche à profondeur 1, en fraction de la hauteur d'écran.
 *
 * ⭐ Il est volontairement **beaucoup plus court que le débattement horizontal**, qui vaut un écran
 * entier. Latéralement, la tuile se répète : on peut glisser autant qu'on veut sans jamais trouver
 * de bord. Verticalement il n'y a pas de répétition possible — le ciel est en haut et le sol en bas,
 * les échanger n'aurait aucun sens. Le déplacement vertical dit donc la profondeur sans défaire la
 * composition.
 */
private const val DEBATTEMENT_VERTICAL = 0.10f

/** Le ciel glisse lui aussi, très peu : assez pour que le haut s'assombrisse quand on monte. */
private const val DEBATTEMENT_CIEL = 0.10f

/** Hauteur de la tranche du bas recopiée sous une couche ancrée en bas, en pixels d'image. */
private const val TRANCHE_PROLONGEE = 6

/**
 * Le décor en parallaxe, dessiné sous les écrans.
 *
 * [camera] est en écrans : `(0, 0)` au centre, `(-1, 0)` sur l'écran de gauche, `(0, 1)` sur celui
 * du bas. Le décor ne décide de rien — il ne connaît ni les écrans, ni les gestes, ni ce qu'il y a
 * dedans. Il ne fait que suivre la caméra qu'on lui donne.
 */
@Composable
fun Decor(camera: () -> Offset, palette: PaletteDecor, modifier: Modifier = Modifier) {
    val images = COUCHES.map { ImageBitmap.imageResource(it.image) }
    val filtre = palette.teinte?.let { ColorFilter.tint(it, BlendMode.Modulate) }

    Canvas(modifier.fillMaxSize().clipToBounds()) {
        val vue = camera()

        dessinerCiel(vue, palette)
        COUCHES.forEachIndexed { rang, couche -> dessinerCouche(couche, images[rang], vue, filtre) }
    }
}

private fun DrawScope.dessinerCiel(camera: Offset, palette: PaletteDecor) {
    val marge = size.height * DEBATTEMENT_CIEL
    val haut = -marge - camera.y * marge

    drawRect(
        brush = Brush.verticalGradient(
            colors = palette.ciel,
            startY = haut,
            endY = haut + size.height + 2f * marge,
        ),
    )
}

/**
 * Une couche, répétée latéralement en miroir.
 *
 * ⭐ **Le miroir est ce qui rend la répétition invisible** : deux tuiles voisines se touchent par le
 * même bord, donc il n'y a pas de raccord à faire coïncider — il n'y a pas de raccord du tout. C'est
 * ce qui permet de glisser d'un écran à l'autre sans jamais tomber sur la fin du dessin.
 */
private fun DrawScope.dessinerCouche(
    couche: Couche,
    image: ImageBitmap,
    camera: Offset,
    filtre: ColorFilter?,
) {
    val largeur = size.width * couche.largeur
    val hauteur = largeur * image.height / image.width
    val glissement = -camera.y * size.height * DEBATTEMENT_VERTICAL * couche.profondeur

    val haut = when (couche.ancrage) {
        Ancrage.HAUT -> couche.decalage * size.height + glissement
        Ancrage.BAS -> size.height - hauteur + couche.decalage * size.height + glissement
    }

    val origine = (size.width - largeur) / 2f - camera.x * size.width * couche.profondeur
    val premier = floor(-origine / largeur).toInt()

    generateSequence(premier) { it + 1 }
        .map { rang -> rang to origine + rang * largeur }
        .takeWhile { (_, gauche) -> gauche < size.width }
        .forEach { (rang, gauche) ->
            dessinerTuile(couche, image, gauche, haut, largeur, hauteur, enMiroir(rang), filtre)
        }
}

private fun enMiroir(rang: Int): Boolean = (rang % 2 + 2) % 2 == 1

private fun DrawScope.dessinerTuile(
    couche: Couche,
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
        poser(image, IntOffset(0, 0), IntSize(image.width, image.height), gauche, haut, largeur, hauteur, filtre)

        val bas = haut + hauteur
        if (couche.ancrage == Ancrage.BAS && bas < size.height) {
            poser(
                image = image,
                depuis = IntOffset(0, image.height - TRANCHE_PROLONGEE),
                taille = IntSize(image.width, TRANCHE_PROLONGEE),
                gauche = gauche,
                haut = bas,
                largeur = largeur,
                hauteur = size.height - bas,
                filtre = filtre,
            )
        }
    }
}

/**
 * La tranche du bas, étirée sous la couche quand la caméra descend.
 *
 * Sans elle, monter le feuillage découvrirait le ciel **sous** lui. En prolongeant chaque colonne
 * par sa propre couleur de bas, la continuation n'a pas de raccord à cacher : c'est la même colonne.
 */
private fun DrawScope.poser(
    image: ImageBitmap,
    depuis: IntOffset,
    taille: IntSize,
    gauche: Float,
    haut: Float,
    largeur: Float,
    hauteur: Float,
    filtre: ColorFilter?,
) {
    drawImage(
        image = image,
        srcOffset = depuis,
        srcSize = taille,
        dstOffset = IntOffset(gauche.roundToInt(), haut.roundToInt()),
        dstSize = IntSize(largeur.roundToInt(), hauteur.roundToInt()),
        colorFilter = filtre,
        filterQuality = FilterQuality.High,
    )
}
