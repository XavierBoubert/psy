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
 * Le décor en parallaxe, dessiné sous les écrans.
 *
 * [camera] est en écrans, **et sur le seul axe horizontal** : `0` sur l'écran d'entrée, `1` sur son
 * voisin de droite, `-1` sur celui de gauche. **Elle n'est bornée d'aucun côté** — la tuile se
 * répète en miroir, donc il n'y a pas de fin du dessin à atteindre.
 *
 * ⭐ **Le décor ne décide de rien** — il ne connaît ni les écrans, ni les gestes, ni ce qu'il y a
 * dedans. Il ne fait que suivre la caméra qu'on lui donne, et c'est ce qui rend l'anneau gratuit :
 * pour lui, revenir sur le premier écran n'est qu'un écran de plus dans le même sens.
 *
 * 🔴 **Plus de débattement vertical** *(15/08/2026)*. Il disait la profondeur quand la traversée
 * était une croix ; **la caméra n'a plus de composante verticale**, et le glissement vertical est
 * rendu au contenu des écrans. Un décor qui bougerait avec une liste qui défile lui donnerait une
 * profondeur qu'il n'a pas.
 */
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

/**
 * Une couche, répétée latéralement — **de deux façons, et c'est la couche qui décide**
 * *(15/08/2026)*.
 *
 * ⭐ **Une tuile à marges se répète simplement**, en avançant de sa seule partie peinte
 * *(`Couche.pas`)* : les deux marges vides se recouvrent, le dessin reprend exactement là où il
 * s'arrête, et **il n'y a aucun axe de symétrie**. C'est la façon propre, et elle demande un dessin
 * qui laisse ses bords vides.
 *
 * ⭐ **Une tuile bord à bord se répète en miroir** : deux tuiles voisines se touchent par le même
 * bord, donc il n'y a pas de raccord à faire coïncider — il n'y a pas de raccord du tout. 🔴 **Mais
 * elle paie sa continuité en symétrie** : ce qui est coupé par le bord retrouve son reflet et forme
 * un papillon. C'est le seul recours quand le dessin ne peut pas s'arrêter avant le bord — une
 * couche de sol, qui découvrirait le ciel sous elle.
 *
 * Dans les deux cas on peut glisser indéfiniment sans jamais tomber sur la fin du dessin, **et
 * tourner indéfiniment autour de l'anneau**.
 */
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
