package io.allonsy.kokoro.corps

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import kotlin.math.min

/**
 * Kokoro, dessiné en vectoriel à partir des formes de [Geometrie.kt] — donc du SVG de Xavier.
 *
 * Aucun bitmap, aucune ressource distante, aucune police externe (CORPS.md §9).
 * Le rendu ne décide de rien : il applique le [RigKokoro] qu'on lui donne, pièce par pièce.
 *
 * Le placement d'une pièce est cuit dans sa géométrie une fois pour toutes, puis stroké à épaisseur
 * fixe — c'est la sémantique SVG, et c'est ce qui garde le contour de la tête régulier alors que
 * `head-out` est étiré de façon non uniforme.
 */
@Composable
fun CorpsKokoro(
    rig: RigKokoro,
    modifier: Modifier = Modifier,
    palette: PaletteCorps = PALETTE_SOMBRE,
) {
    Canvas(modifier) {
        val facteur = min(size.width / LARGEUR_VUE, size.height / HAUTEUR_VUE)
        withTransform({
            translate(
                left = (size.width - LARGEUR_VUE * facteur) / 2f,
                top = (size.height - HAUTEUR_VUE * facteur) / 2f,
            )
            scale(facteur, facteur, Offset.Zero)
        }) {
            dessinerKokoro(rig, palette)
        }
    }
}

private fun Transformation.matrice(): Matrix = Matrix().also {
    it.values[Matrix.ScaleX] = a
    it.values[Matrix.SkewY] = b
    it.values[Matrix.SkewX] = c
    it.values[Matrix.ScaleY] = d
    it.values[Matrix.TranslateX] = e
    it.values[Matrix.TranslateY] = f
}

private fun Forme.chemin(): Path = when (this) {
    is Forme.Chemin -> PathParser().parsePathString(donnees).toPath()
    is Forme.Ellipse -> Path().apply { addOval(Rect(-rx, -ry, rx, ry)) }
    is Forme.Segment -> Path().apply {
        moveTo(x1, y1)
        lineTo(x2, y2)
    }

    is Forme.Arc -> Path().apply {
        moveTo(x1, y1)
        quadraticTo(cx, cy, x2, y2)
    }
}

/** Chaque pièce est posée dans la vue une fois pour toutes : le placement ne dépend pas du rig. */
private val PIECES_POSEES: Map<String, Path> by lazy {
    PIECES.associate { piece ->
        piece.nom to piece.forme.chemin().apply { transform(piece.placement.matrice()) }
    }
}

private val TRACES_TRACEES: Map<String, Path> by lazy {
    TRACES.associate { trace -> trace.nom to trace.forme.chemin() }
}

private val Ancre.offset: Offset get() = Offset(x, y)

private fun DrawScope.dessinerKokoro(rig: RigKokoro, palette: PaletteCorps) {
    withTransform({
        translate(rig.decalage.x, rig.decalage.y)
        rotate(rig.inclinaison, PIVOT_RACINE.offset)
        scale(rig.echelle, rig.echelle, PIVOT_RACINE.offset)
    }) {
        dessinerTorse(rig, palette)
        dessinerTete(rig, palette)
        dessinerAutour(PIED_GAUCHE, CENTRE_VENTRE, rig.rotationPiedGauche, palette)
        dessinerAutour(PIED_DROIT, CENTRE_VENTRE, rig.rotationPiedDroit, palette)
        dessinerAutour(BRAS_GAUCHE, EPAULE_GAUCHE, rig.rotationBrasGauche, palette)
        dessinerAutour(BRAS_DROIT, EPAULE_DROITE, rig.rotationBrasDroit, palette)
    }
}

/** Le torse respire autour de sa base ; la ligne du ventre et le 心 sont dessus, donc ils suivent. */
private fun DrawScope.dessinerTorse(rig: RigKokoro, palette: PaletteCorps) {
    withTransform({
        scale(rig.retractionCorps, rig.etirementCorps, PIVOT_RESPIRATION.offset)
    }) {
        dessinerPiece(TORSE, palette)
        dessinerPiece(LIGNE_VENTRE, palette)
        KANJI.forEach { dessinerPiece(it, palette) }
    }
}

private fun DrawScope.dessinerTete(rig: RigKokoro, palette: PaletteCorps) {
    dessinerPiece(TETE, palette)
    dessinerPiece(PANNEAU, palette)
    if (rig.panneauAllume) {
        dessinerVisage(rig, palette.trait)
    }
}

private fun DrawScope.dessinerAutour(
    piece: Piece,
    pivot: Ancre,
    rotation: Float,
    palette: PaletteCorps,
) {
    withTransform({ rotate(rotation, pivot.offset) }) {
        dessinerPiece(piece, palette)
    }
}

private fun DrawScope.dessinerPiece(piece: Piece, palette: PaletteCorps) {
    val forme = PIECES_POSEES.getValue(piece.nom)
    palette.couleur(piece.remplissage)?.let { drawPath(forme, it) }
    if (piece.epaisseur > 0f) {
        drawPath(forme, palette.trait, style = trait(piece.epaisseurRendue, piece.terminaison))
    }
}

/**
 * Le visage change de forme, il ne se superpose jamais à lui-même : à aucun instant deux visages ne
 * sont dessinés l'un sur l'autre. Voir [MorphingVisage.kt][Contour].
 */
private fun DrawScope.dessinerVisage(rig: RigKokoro, couleur: Color) {
    val visage = rig.visage
    val avancement = if (visage.stable) 1f else visage.progression.coerceIn(0f, 1f)
    dessinerTrace(visage.depuis.oeil, visage.vers.oeil, avancement, OEIL_GAUCHE, rig.regard, couleur)
    dessinerTrace(visage.depuis.oeil, visage.vers.oeil, avancement, OEIL_DROIT, rig.regard, couleur)
    dessinerTrace(visage.depuis.bouche, visage.vers.bouche, avancement, BOUCHE, 0f, couleur)
}

/**
 * Une pièce du visage en cours de déformation.
 *
 * Les deux bouts et les formes qui ne changent pas — l'œil reste ovale de `neutre` à `attentif` —
 * sont tracés depuis le dessin lui-même : la silhouette échantillonnée n'est qu'une approche, et
 * elle ne sert que pendant le mouvement.
 */
private fun DrawScope.dessinerTrace(
    depuis: Trace,
    vers: Trace,
    avancement: Float,
    ancre: Ancre,
    decalage: Float,
    couleur: Color,
) {
    withTransform({ translate(ancre.x + decalage, ancre.y) }) {
        when {
            depuis == vers || avancement >= 1f -> dessinerForme(vers, couleur)
            avancement <= 0f -> dessinerForme(depuis, couleur)
            else -> drawPath(polygone(depuis.contour.vers(vers.contour, avancement)), couleur)
        }
    }
}

private fun DrawScope.dessinerForme(trace: Trace, couleur: Color) {
    val forme = TRACES_TRACEES.getValue(trace.nom)
    when {
        trace.epaisseur > 0f ->
            drawPath(forme, couleur, style = trait(trace.epaisseur, TERMINAISON_VISAGE))

        else -> drawPath(forme, couleur)
    }
}

private fun polygone(contour: Contour): Path = Path().apply {
    val depart = contour.points.first()
    moveTo(depart.x, depart.y)
    (1 until contour.points.size).forEach {
        lineTo(contour.points[it].x, contour.points[it].y)
    }
    close()
}

private fun trait(epaisseur: Float, terminaison: Terminaison) = Stroke(
    width = epaisseur,
    cap = when (terminaison) {
        Terminaison.PLATE -> StrokeCap.Butt
        Terminaison.ARRONDIE -> StrokeCap.Round
    },
    join = when (terminaison) {
        Terminaison.PLATE -> StrokeJoin.Miter
        Terminaison.ARRONDIE -> StrokeJoin.Round
    },
)
