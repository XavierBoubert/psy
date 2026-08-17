package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val POINTS_PAR_QUART = 24

const val POINTS_CONTOUR = 4 * POINTS_PAR_QUART

// Points échantillonnés dans un ordre fixe et identique pour tous les tracés : condition du morphing point à point.
data class Contour(val points: List<Offset>) {
    init {
        require(points.size == POINTS_CONTOUR) {
            "Une silhouette porte $POINTS_CONTOUR points, pas ${points.size}"
        }
    }

    val bordHaut: List<Offset> get() = points.subList(0, POINTS_PAR_QUART)

    val bordBas: List<Offset>
        get() = points.subList(2 * POINTS_PAR_QUART, 3 * POINTS_PAR_QUART).reversed()

    fun vers(cible: Contour, avancement: Float): Contour = Contour(
        points.mapIndexed { indice, point -> lerp(point, cible.points[indice], avancement) },
    )
}

val Trace.contour: Contour get() = CONTOURS.getValue(nom)

private val CONTOURS: Map<String, Contour> by lazy {
    TRACES.associate { trace -> trace.nom to trace.silhouette() }
}

private data class Squelette(val points: List<Offset>, val tangentes: List<Offset>)

private fun Trace.silhouette(): Contour = when (val forme = forme) {
    is Forme.Ellipse -> contourAutour(squelettePonctuel(), forme.rx).etire(forme.ry / forme.rx)
    is Forme.Segment -> contourAutour(squelette(forme), epaisseur / 2f)
    is Forme.Arc -> contourAutour(squelette(forme), epaisseur / 2f)
    is Forme.Chemin -> error("Le visage ne porte pas de tracé SVG libre : $nom")
}

// Ellipse : pas de ligne centrale, juste un point — sa silhouette vient de deux bouts arrondis mis bout à bout.
private fun squelettePonctuel() = Squelette(
    points = echantillons { Offset.Zero },
    tangentes = echantillons { VERS_LA_DROITE },
)

private fun squelette(segment: Forme.Segment): Squelette {
    val depart = Offset(segment.x1, segment.y1)
    val arrivee = Offset(segment.x2, segment.y2)
    val tangente = (arrivee - depart).unitaire()
    return Squelette(
        points = echantillons { avance -> lerp(depart, arrivee, avance) },
        tangentes = echantillons { tangente },
    )
}

private fun squelette(arc: Forme.Arc): Squelette {
    val depart = Offset(arc.x1, arc.y1)
    val controle = Offset(arc.cx, arc.cy)
    val arrivee = Offset(arc.x2, arc.y2)
    return Squelette(
        points = echantillons { avance ->
            val reste = 1f - avance
            depart * (reste * reste) + controle * (2f * reste * avance) + arrivee * (avance * avance)
        },
        tangentes = echantillons { avance ->
            ((controle - depart) * (1f - avance) + (arrivee - controle) * avance).unitaire()
        },
    )
}

private fun contourAutour(squelette: Squelette, rayon: Float): Contour {
    val normales = squelette.tangentes.map { it.normale() }
    val haut = squelette.points.mapIndexed { indice, point -> point + normales[indice] * rayon }
    val bas = squelette.points.mapIndexed { indice, point -> point - normales[indice] * rayon }
    return Contour(
        haut +
            bout(squelette.points.last(), normales.last(), squelette.tangentes.last(), rayon) +
            bas.reversed() +
            bout(squelette.points.first(), -normales.first(), -squelette.tangentes.first(), rayon),
    )
}

// Demi-cercle de depuis à son opposé en passant par dehors ; aucun point dupliqué avec les bords adjacents.
private fun bout(centre: Offset, depuis: Offset, dehors: Offset, rayon: Float): List<Offset> =
    List(POINTS_PAR_QUART) { indice ->
        val angle = PI.toFloat() * (indice + 1f) / (POINTS_PAR_QUART + 1f)
        centre + (depuis * cos(angle) + dehors * sin(angle)) * rayon
    }

private fun Contour.etire(facteurVertical: Float) =
    Contour(points.map { point -> Offset(point.x, point.y * facteurVertical) })

private fun <T> echantillons(valeur: (Float) -> T): List<T> =
    List(POINTS_PAR_QUART) { indice -> valeur(indice.toFloat() / (POINTS_PAR_QUART - 1)) }

private val VERS_LA_DROITE = Offset(1f, 0f)

// y croît vers le bas (repère écran) : Offset(y, -x) est donc la perpendiculaire qui monte.
private fun Offset.normale() = Offset(y, -x)

private fun Offset.unitaire(): Offset = when (val longueur = getDistance()) {
    0f -> VERS_LA_DROITE
    else -> this / longueur
}
