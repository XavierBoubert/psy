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

/**
 * Le thème de Kokoro — **des panneaux extrudés posés sur le paysage** (`companion/INTERFACE.md` §4).
 *
 * ⭐ **Le décor ne se voit plus à travers l'interface : il se voit entre les panneaux.** C'est la
 * différence de fond avec le verre dépoli abandonné le 14/08/2026, et c'est elle qui règle les deux
 * points durs qui l'avaient fait tomber : **P4** — plus aucun flou, donc plus aucune apparence qui
 * dépende d'un réglage Android étranger — et **P5** — les panneaux sont opaques, donc la lisibilité
 * d'un texte ne dépend jamais de ce qui passe derrière.
 *
 * 🔴 **Une surface qui floute a repris l'ancien thème.** Le verre ne se réintroduit pas par morceaux.
 */

/** Un couple *(haut, bas)* — toute couleur de ce thème est un dégradé, jamais un aplat. */
data class Teinte(val haut: Color, val bas: Color)

/**
 * Les couleurs, jour et nuit. **Cinq teintes, et il n'y en aura pas d'autres**
 * (`companion/INTERFACE.md` §4.1).
 *
 * 🔴 **Aucun rouge, nulle part — écran de crise compris.** ⭐ **La palette n'en contient pas** :
 * c'est ce qui rend la règle tenable au lieu de la laisser à la vigilance de celui qui écrit
 * l'écran suivant.
 *
 * ⭐ **Le vert de *Fait* confirme une action ; il n'a pas de contraire.** Il n'existe ni orange
 * *en retard*, ni gris *pas fait*, ni rouge *raté* — **il n'y a pas de retard dans ce dispositif**,
 * donc pas de couleur pour en parler.
 */
data class PaletteKokoro(
    val panneauHaut: Color,
    val panneauBas: Color,
    /** 🔴 **Brun, jamais noir.** Un contour noir bascule le registre du jouet vers l'outil. */
    val contour: Color,
    val reflet: Color,
    val creux: Color,
    val porte: Color,
    val encre: Color,
    val encreDouce: Color,
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

/**
 * La palette du moment. **Elle suit la plage horaire du décor** (`companion/DECOR.md` §5), lue à
 * l'arrivée — **jamais le thème système, jamais sous les yeux**.
 */
val LocalPaletteKokoro = staticCompositionLocalOf { KOKORO_JOUR }

/** Épaisseur du trait de contour. */
val CONTOUR = 4.dp

/** L'épaisseur portée sous le panneau — c'est elle qu'on enfonce à l'appui. */
val EPAISSEUR = 7.dp

val RAYON = 26.dp

private val REFLET = 4.dp
private val CREUX = 7.dp
private val OMBRE_FLOU = 16.dp
private val OMBRE_RETRAIT = 6.dp
private val OMBRE_CHUTE = 8.dp

/**
 * La recette de matière, en six couches — **une seule, déclinée en couleur**
 * (`companion/INTERFACE.md` §4.1). Carte, bouton, bande de titre et pancarte sont le même dessin.
 *
 * De bas en haut : l'**ombre portée** · l'**épaisseur** posée sous le panneau, dans la couleur du
 * contour · le **fond en dégradé** · le **reflet** sur le bord haut · le **creux** sur le bord bas ·
 * le **contour**.
 *
 * ⭐ **Le reflet et le creux sont des bandes calculées, pas des rectangles posés.** Chacune est la
 * différence entre la forme et la même forme décalée : elle épouse donc exactement l'arrondi des
 * coins, y compris sur une pilule dont le rayon vaut la demi-hauteur.
 *
 * ⭐ **L'[enfoncement] descend le panneau et mange son épaisseur d'autant** : à 7 dp le panneau
 * touche le sol, l'ombre se resserre, et la pièce occupe toujours la même place dans la mise en
 * page. **Rien ne bouge autour d'un appui.**
 *
 * @param couleur `null` pour la matière neutre — la carte, le fond de panneau. Une teinte pour un
 *   bouton plein, un ruban, une pancarte.
 * @param creuse inverse la recette : le dégradé remonte, le creux passe en haut et le reflet en bas,
 *   l'épaisseur disparaît. C'est la plaque enfoncée des états vides.
 * @param debordHaut de combien la matière se dessine **au-dessus** de sa boîte, pour qu'une bande de
 *   titre sorte de l'écran par le haut au lieu d'y montrer deux coins arrondis.
 */
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

/**
 * Sur une pièce colorée, le reflet et le creux sont **du blanc et du noir voilés**, et non les
 * teintes du panneau neutre : posées sur de la menthe, celles-ci vireraient au sale.
 */
private fun reflet(palette: PaletteKokoro, couleur: Teinte?): Color =
    if (couleur == null) palette.reflet else Color.White.copy(alpha = 0.42f)

private fun creux(palette: PaletteKokoro, couleur: Teinte?): Color =
    if (couleur == null) palette.creux else Color.Black.copy(alpha = 0.14f)

/**
 * Une bande le long d'un bord, **taillée dans la forme elle-même** : la forme moins la même forme
 * décalée. C'est ce qui lui fait suivre l'arrondi du coin au lieu de le couper au carré.
 */
private fun DrawScope.bande(forme: RoundRect, versLeBas: Boolean, epaisseur: Dp, couleur: Color) {
    val decalage = epaisseur.toPx() * if (versLeBas) 1f else -1f
    val pleine = chemin(forme)
    val decalee = chemin(forme).apply { translate(Offset(0f, decalage)) }
    drawPath(Path.combine(PathOperation.Difference, pleine, decalee), couleur)
}

/**
 * L'ombre portée — la seule chose floue du thème, et **elle est calculée ici, pas par le système**.
 *
 * 🔴 **P4 vise le flou d'arrière-plan, pas le flou d'une ombre** : celui-ci ne dépend d'aucun
 * réglage Android, il ne change donc jamais parce que l'économiseur de batterie s'est allumé, et il
 * ne laisse jamais passer ce qu'il y a derrière.
 */
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

/** La forme d'une pièce, pour les rares fois où il faut la découper et pas seulement la peindre. */
fun formeArrondie(taille: Size, rayon: Float): Path =
    chemin(RoundRect(Rect(Offset.Zero, taille), CornerRadius(rayon)))
