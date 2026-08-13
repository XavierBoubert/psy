package io.allonsy.kokoro.corps

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import kotlin.math.min

/**
 * Kokoro, dessiné en vectoriel à partir des chemins de [Geometrie.kt].
 *
 * Aucun bitmap, aucune ressource distante, aucune police externe (CORPS.md §9).
 * Le rendu ne décide de rien : il applique le [RigKokoro] qu'on lui donne, pièce par pièce.
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

private val CHEMINS: Map<String, Path> by lazy {
    (PIECES.map { it.chemin } + TRACES.map { it.chemin })
        .distinct()
        .associateWith { PathParser().parsePathString(it).toPath() }
}

private fun chemin(donnees: String): Path = CHEMINS.getValue(donnees)

private val TRAIT_PLEIN = Stroke(
    width = EPAISSEUR_TRAIT,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round,
)

private val Ancre.offset: Offset get() = Offset(x, y)

private fun DrawScope.dessinerKokoro(rig: RigKokoro, palette: PaletteCorps) {
    withTransform({
        translate(rig.decalage.x, rig.decalage.y)
        rotate(rig.inclinaison, PIVOT_RACINE.offset)
        scale(rig.echelle, rig.echelle, PIVOT_RACINE.offset)
    }) {
        dessinerPiece(PIED_GAUCHE, palette.remplissage, palette.trait)
        dessinerPiece(PIED_DROIT, palette.remplissage, palette.trait)
        dessinerBras(BRAS_GAUCHE, rig.rotationBrasGauche, palette)
        dessinerBras(BRAS_DROIT, rig.rotationBrasDroit, palette)
        dessinerTorse(rig, palette)
        dessinerTete(rig, palette)
    }
}

private fun DrawScope.dessinerPiece(piece: Piece, remplissage: Color, trait: Color) {
    withTransform({ translate(piece.ancre.x, piece.ancre.y) }) {
        tracerForme(chemin(piece.chemin), remplissage, trait)
    }
}

private fun DrawScope.dessinerBras(piece: Piece, rotation: Float, palette: PaletteCorps) {
    withTransform({
        translate(piece.ancre.x, piece.ancre.y)
        rotate(rotation, Offset.Zero)
    }) {
        tracerForme(chemin(piece.chemin), palette.remplissage, palette.trait)
    }
}

private fun DrawScope.dessinerTorse(rig: RigKokoro, palette: PaletteCorps) {
    withTransform({
        scale(rig.retractionCorps, rig.etirementCorps, PIVOT_RESPIRATION.offset)
    }) {
        dessinerPiece(CORPS, palette.remplissage, palette.trait)
        dessinerPiece(PLAQUE, palette.accent, palette.trait)
    }
}

private fun DrawScope.dessinerTete(rig: RigKokoro, palette: PaletteCorps) {
    dessinerPiece(TETE, palette.remplissage, palette.trait)
    dessinerPiece(PANNEAU, palette.panneau, palette.trait)
    if (rig.panneauAllume) {
        dessinerVisage(rig, palette.trait)
    }
}

private fun DrawScope.dessinerVisage(rig: RigKokoro, couleur: Color) {
    if (rig.visage.stable) {
        dessinerExpression(rig.visage.vers, rig.regard, couleur, alpha = 1f)
        return
    }
    val avancement = rig.visage.progression.coerceIn(0f, 1f)
    dessinerExpression(rig.visage.depuis, rig.regard, couleur, alpha = 1f - avancement)
    dessinerExpression(rig.visage.vers, rig.regard, couleur, alpha = avancement)
}

private fun DrawScope.dessinerExpression(
    expression: Expression,
    regard: Float,
    couleur: Color,
    alpha: Float,
) {
    dessinerTrace(expression.oeil, OEIL_GAUCHE, regard, couleur, alpha)
    dessinerTrace(expression.oeil, OEIL_DROIT, regard, couleur, alpha)
    dessinerTrace(expression.bouche, BOUCHE, decalage = 0f, couleur = couleur, alpha = alpha)
}

private fun DrawScope.dessinerTrace(
    trace: Trace,
    ancre: Ancre,
    decalage: Float,
    couleur: Color,
    alpha: Float,
) {
    withTransform({ translate(ancre.x + decalage, ancre.y) }) {
        val forme = chemin(trace.chemin)
        when {
            trace.remplie -> drawPath(forme, couleur, alpha = alpha)
            else -> drawPath(
                path = forme,
                color = couleur,
                alpha = alpha,
                style = Stroke(
                    width = EPAISSEUR_TRAIT * trace.epaisseur,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

private fun DrawScope.tracerForme(forme: Path, remplissage: Color, trait: Color) {
    drawPath(forme, remplissage)
    drawPath(forme, trait, style = TRAIT_PLEIN)
}
