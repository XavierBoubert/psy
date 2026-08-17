package io.allonsy.kokoro.corps

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
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

// Les deux passes peignent le même rig, jamais deux rigs distincts (dériveraient au clignement).
enum class Passe { ENTIER, CORPS, BRAS }

@Composable
fun CorpsKokoro(
    rig: RigKokoro,
    modifier: Modifier = Modifier,
    palette: PaletteCorps = PALETTE_SOMBRE,
    passe: Passe = Passe.ENTIER,
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
            dessinerKokoro(rig, palette, passe)
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

private val PIECES_POSEES: Map<String, Path> by lazy {
    PIECES.associate { piece ->
        piece.nom to piece.forme.chemin().apply { transform(piece.placement.matrice()) }
    }
}

private val TRACES_TRACEES: Map<String, Path> by lazy {
    TRACES.associate { trace -> trace.nom to trace.forme.chemin() }
}

private val Ancre.offset: Offset get() = Offset(x, y)

private fun DrawScope.dessinerKokoro(rig: RigKokoro, palette: PaletteCorps, passe: Passe) {
    if (passe != Passe.BRAS) rig.ombre?.let { dessinerOmbre(it, rig, palette.trait) }
    withTransform({
        translate(rig.decalage.x, rig.decalage.y)
        rotate(rig.inclinaison, PIVOT_RACINE.offset)
        scale(rig.echelle, rig.echelle, PIVOT_RACINE.offset)
    }) {
        if (passe != Passe.BRAS) {
            dessinerTorse(rig, palette)
            dessinerTete(rig, palette)
            // Les pieds ne suivent jamais le souffle, contrairement aux bras (decalageRespirationHaut).
            dessinerMembre(PIED_GAUCHE, CENTRE_VENTRE, rig.rotationPiedGauche, rig.posePiedGauche, palette)
            dessinerMembre(PIED_DROIT, CENTRE_VENTRE, rig.rotationPiedDroit, rig.posePiedDroit, palette)
        }
        if (passe != Passe.CORPS) {
            withTransform({ translate(0f, rig.decalageRespirationHaut) }) {
                dessinerMembre(BRAS_GAUCHE, EPAULE_GAUCHE, rig.rotationBrasGauche, rig.poseBrasGauche, palette)
                dessinerMembre(BRAS_DROIT, EPAULE_DROITE, rig.rotationBrasDroit, rig.poseBrasDroit, palette)
            }
        }
    }
}

// L'absence d'ombre sur l'interface vient de l'ordre de peinture (sous le personnage), pas d'un clip.
private fun DrawScope.dessinerOmbre(ombre: Ombre, rig: RigKokoro, encre: Color) {
    val centre = Offset(AXE, ombre.sol)
    val teinte = encre.copy(alpha = ombre.opaciteA(rig.decalage.y))
    withTransform({
        translate(rig.decalage.x, 0f)
        scale(rig.echelle, rig.echelle, PIVOT_RACINE.offset)
        scale(1f, ombre.aplatissement, centre)
    }) {
        drawCircle(
            brush = Brush.radialGradient(
                0f to teinte,
                ombre.noyau to teinte,
                1f to encre.copy(alpha = 0f),
                center = centre,
                radius = ombre.demiLargeur,
            ),
            radius = ombre.demiLargeur,
            center = centre,
        )
    }
}

private fun DrawScope.dessinerTorse(rig: RigKokoro, palette: PaletteCorps) {
    withTransform({ transform(rig.vol.torse.matrice()) }) {
        withTransform({ scale(1f, rig.etirementCorps, PIVOT_RESPIRATION.offset) }) {
            dessinerPiece(TORSE, palette)
            dessinerPiece(LIGNE_VENTRE, palette)
        }
    }
    withTransform({ transform(rig.vol.kanji.matrice()) }) {
        withTransform({ scale(1f, rig.etirementCorps, PIVOT_RESPIRATION.offset) }) {
            KANJI.forEach { dessinerPiece(it, palette) }
        }
    }
}

// L'inclinaison de tête n'est utilisée que par la posture accoude ; ailleurs elle vaut toujours zéro.
private fun DrawScope.dessinerTete(rig: RigKokoro, palette: PaletteCorps) {
    withTransform({ translate(0f, rig.decalageRespirationHaut) }) {
        withTransform({ rotate(rig.inclinaisonTete, PIVOT_TETE.offset) }) {
            withTransform({ transform(rig.vol.coque.matrice()) }) { dessinerPiece(TETE, palette) }
            withTransform({ transform(rig.vol.panneau.matrice()) }) { dessinerPiece(PANNEAU, palette) }
            if (rig.panneauAllume) {
                dessinerVisage(rig, palette.trait)
            }
        }
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

private fun DrawScope.dessinerMembre(
    piece: Piece,
    pivot: Ancre,
    rotation: Float,
    pose: Transformation,
    palette: PaletteCorps,
) {
    withTransform({ transform(pose.matrice()) }) {
        dessinerAutour(piece, pivot, rotation, palette)
    }
}

private fun DrawScope.dessinerPiece(piece: Piece, palette: PaletteCorps) {
    val forme = PIECES_POSEES.getValue(piece.nom)
    palette.couleur(piece.remplissage)?.let { drawPath(forme, it) }
    if (piece.epaisseur > 0f) {
        drawPath(forme, palette.trait, style = trait(piece.epaisseurRendue, piece.terminaison))
    }
}

private fun DrawScope.dessinerVisage(rig: RigKokoro, couleur: Color) {
    val yeux = Offset(rig.regard, rig.abaissement)
    dessinerTrace(rig.visage.oeil, OEIL_GAUCHE, yeux + rig.vol.oeilGauche.decalage, couleur)
    dessinerTrace(rig.visage.oeil, OEIL_DROIT, yeux + rig.vol.oeilDroit.decalage, couleur)
    dessinerTrace(rig.visage.bouche, BOUCHE, rig.vol.bouche.decalage, couleur)
}

private fun DrawScope.dessinerTrace(
    morphing: Morphing,
    ancre: Ancre,
    decalage: Offset,
    couleur: Color,
) {
    val avancement = morphing.progression.coerceIn(0f, 1f)
    withTransform({ translate(ancre.x + decalage.x, ancre.y + decalage.y) }) {
        when {
            morphing.stable -> dessinerForme(morphing.vers, couleur)
            avancement <= 0f -> dessinerForme(morphing.depuis, couleur)
            else -> drawPath(
                polygone(morphing.depuis.contour.vers(morphing.vers.contour, avancement)),
                couleur,
            )
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
