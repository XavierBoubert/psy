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
/**
 * Ce qu'une passe de peinture dessine — **une seule instance du personnage, peinte en deux fois.**
 *
 * 🔴 **Ce n'est pas un dédoublement** (`CORPS.md` §8 point 8) : les deux passes reçoivent **le même
 * rig, calculé une seule fois**, et se peignent au même endroit. Deux rigs animés séparément
 * dériveraient l'un de l'autre au premier clignement.
 *
 * ⭐ **Elles n'existent que pour l'écran de crise** : le corps passe **sous** le bouton *Mot code*,
 * les bras **dessus**. C'est ce qui fait qu'il est accoudé au bouton au lieu d'être posé devant.
 */
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
            // 🔴 Les pieds ne suivent jamais le souffle (§5, RigKokoro.decalageRespirationHaut) :
            // seule leur pose empruntée (sommeil, vol) s'ajoute à leur orbite normale.
            dessinerMembre(PIED_GAUCHE, CENTRE_VENTRE, rig.rotationPiedGauche, rig.posePiedGauche, palette)
            dessinerMembre(PIED_DROIT, CENTRE_VENTRE, rig.rotationPiedDroit, rig.posePiedDroit, palette)
        }
        if (passe != Passe.CORPS) {
            // ⭐ Les bras suivent le sommet du ventre qui respire (§1.2 de la demande du 16/08/2026) :
            // à la même hauteur du corps qu'au repos, ni plus enfoncés, ni plus sortis.
            withTransform({ translate(0f, rig.decalageRespirationHaut) }) {
                dessinerMembre(BRAS_GAUCHE, EPAULE_GAUCHE, rig.rotationBrasGauche, rig.poseBrasGauche, palette)
                dessinerMembre(BRAS_DROIT, EPAULE_DROITE, rig.rotationBrasDroit, rig.poseBrasDroit, palette)
            }
        }
    }
}

/**
 * L'ombre est peinte **dans la couche du personnage, juste sous lui** (`PRESENCE.md` §1.3) : un
 * panneau posé par-dessus la recouvre mécaniquement. 🔴 **« Pas d'ombre sur l'interface » est une
 * conséquence de l'ordre de peinture — aucune découpe, aucun test.**
 *
 * Elle suit le personnage en `x` **et pas en `y`** : c'est l'écart entre ses pieds et elle qui dit
 * la hauteur de vol. Le flou est un dégradé radial plutôt qu'un `BlurMaskFilter` — même rendu, et
 * rien à déléguer au pilote graphique.
 */
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

/**
 * Le torse respire autour de sa base ; la ligne du ventre et le 心 sont dessus, donc ils suivent.
 * ⭐ **Vers le haut uniquement** (demande de Xavier, 16/08/2026) : plus de rétraction en largeur.
 */
private fun DrawScope.dessinerTorse(rig: RigKokoro, palette: PaletteCorps) {
    // Le corps du vol enveloppe le souffle : le torse respire dans son repère, et c'est l'ensemble
    // qui se resserre — l'ordre du dessin de variante, où le groupe porte tout. Le 心 y a un
    // glissement propre, comme dans le dessin.
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

/**
 * ⭐ **La tête peut pencher, et elle seule** — autour de [PIVOT_TETE], le milieu de la ligne des
 * épaules. La coque, le panneau et le visage tournent **ensemble** : le visage est peint dans le
 * panneau, il ne glisse pas dessus.
 *
 * 🔴 **Une seule posture s'en sert** (`accoude`), et l'angle y est borné. Partout ailleurs
 * l'inclinaison vaut zéro et cette rotation ne fait rien.
 */
private fun DrawScope.dessinerTete(rig: RigKokoro, palette: PaletteCorps) {
    // ⭐ Suit le sommet du ventre qui respire, comme les bras (§1.2 de la demande du 16/08/2026) —
    // avant l'inclinaison d'`accoude`, qui continue de tourner autour de sa propre ancre.
    withTransform({ translate(0f, rig.decalageRespirationHaut) }) {
        withTransform({ rotate(rig.inclinaisonTete, PIVOT_TETE.offset) }) {
            // Coque, panneau et visage portent chacun leur part du vol : c'est leur resserrement
            // séparé qui donne la tête tournée, et le dessin les traite bien comme trois pièces.
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

/**
 * Un membre (bras ou pied), sa rotation normale **puis, par-dessus, une pose empruntée à un autre
 * dessin** — sommeil, vol (`Geometrie.kt`, [PoseMembre]). Identité par défaut : au repos la
 * transformation ne change rien, et le membre est exactement celui du SVG.
 */
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

/**
 * Le visage change de forme, il ne se superpose jamais à lui-même : à aucun instant deux visages ne
 * sont dessinés l'un sur l'autre. Voir [MorphingVisage.kt][Contour].
 */
private fun DrawScope.dessinerVisage(rig: RigKokoro, couleur: Color) {
    val yeux = Offset(rig.regard, rig.abaissement)
    // Les trois tracés ne glissent pas ensemble en vol : les yeux se rapprochent en même temps
    // qu'ils partent du côté du vol, ce qui est tout le trois-quarts du dessin.
    dessinerTrace(rig.visage.oeil, OEIL_GAUCHE, yeux + rig.vol.oeilGauche.decalage, couleur)
    dessinerTrace(rig.visage.oeil, OEIL_DROIT, yeux + rig.vol.oeilDroit.decalage, couleur)
    dessinerTrace(rig.visage.bouche, BOUCHE, rig.vol.bouche.decalage, couleur)
}

/**
 * Une pièce du visage en cours de déformation. 🔴 **Les yeux et la bouche ont chacun la leur** : un
 * clignement déforme les premiers sans toucher à la seconde.
 *
 * Les deux bouts et les formes qui ne changent pas — l'œil reste ovale de `serein` à `attentif` —
 * sont tracés depuis le dessin lui-même : la silhouette échantillonnée n'est qu'une approche, et
 * elle ne sert que pendant le mouvement.
 */
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
