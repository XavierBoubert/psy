package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Le morphing du visage — `design/CORPS.md` §9.
 *
 * ⭐ **Une expression ne s'échange plus en fondu : sa forme se déforme vers la suivante.** L'ovale
 * de l'œil s'écrase en trait quand Kokoro cligne, le trait de la bouche se creuse en sourire — il
 * n'y a jamais deux visages superposés à l'écran, il n'y en a qu'un, en train de changer de forme.
 *
 * Le morphing tient à une seule idée : **chaque tracé est réduit à sa silhouette pleine,
 * échantillonnée toujours de la même manière** — bord haut de gauche à droite, bout droit, bord bas
 * de droite à gauche, bout gauche, [POINTS_PAR_QUART] points chacun. Deux tracés quelconques ont
 * donc le même nombre de points, dans le même ordre, et le point d'indice `i` de l'un a le même rôle
 * que celui de l'autre. **La correspondance est construite, pas cherchée** : aucun appariement à
 * l'exécution, aucun réglage, et le même couple de formes donne toujours exactement la même image.
 *
 * Deux conséquences qui comptent :
 * - **Une forme intermédiaire est une combinaison convexe des deux silhouettes.** Tout ce qui
 *   s'écrit comme une inégalité linéaire sur les points se transporte donc aux images du milieu —
 *   en particulier 🔴 **les commissures ne tombent pas davantage pendant la déformation qu'aux deux
 *   bouts** (§3), et `CorpsInvariantsTest` le vérifie couple par couple.
 * - **Rien n'est calculé par image** : les huit silhouettes sont calculées une fois, seule
 *   l'interpolation tourne pendant les 800 ms de la transition.
 *
 * ⚠️ Une silhouette **approche** le tracé — c'est un polygone. Le rendu ne s'en sert donc que
 * pendant une déformation ; à l'arrêt, [CorpsKokoro] trace la forme elle-même, telle que le dessin
 * la donne.
 */

/** Points par quart de silhouette. Le contour complet en porte quatre fois plus. */
const val POINTS_PAR_QUART = 24

const val POINTS_CONTOUR = 4 * POINTS_PAR_QUART

/**
 * La silhouette pleine d'un tracé de visage, dans le repère du tracé.
 *
 * L'ordre des points est la seule chose qui rend le morphing possible : il est décrit ci-dessus et
 * il ne change pas d'une forme à l'autre.
 */
data class Contour(val points: List<Offset>) {
    init {
        require(points.size == POINTS_CONTOUR) {
            "Une silhouette porte $POINTS_CONTOUR points, pas ${points.size}"
        }
    }

    /** Le bord supérieur, de gauche à droite. */
    val bordHaut: List<Offset> get() = points.subList(0, POINTS_PAR_QUART)

    /** Le bord inférieur, remis de gauche à droite pour se comparer à [bordHaut]. */
    val bordBas: List<Offset>
        get() = points.subList(2 * POINTS_PAR_QUART, 3 * POINTS_PAR_QUART).reversed()

    /** La forme intermédiaire entre celle-ci et [cible] : point par point, sans appariement. */
    fun vers(cible: Contour, avancement: Float): Contour = Contour(
        points.mapIndexed { indice, point -> lerp(point, cible.points[indice], avancement) },
    )
}

/** Calculée une fois : un tracé est une constante, sa silhouette aussi. */
val Trace.contour: Contour get() = CONTOURS.getValue(nom)

private val CONTOURS: Map<String, Contour> by lazy {
    TRACES.associate { trace -> trace.nom to trace.silhouette() }
}

/**
 * La ligne centrale d'un tracé et sa tangente, échantillonnées — de quoi épaissir la forme des deux
 * côtés. L'ellipse pleine n'a pas de ligne centrale : c'est un point, et sa silhouette est faite de
 * ses deux bouts arrondis, mis bout à bout.
 */
private data class Squelette(val points: List<Offset>, val tangentes: List<Offset>)

private fun Trace.silhouette(): Contour = when (val forme = forme) {
    is Forme.Ellipse -> contourAutour(squelettePonctuel(), forme.rx).etire(forme.ry / forme.rx)
    is Forme.Segment -> contourAutour(squelette(forme), epaisseur / 2f)
    is Forme.Arc -> contourAutour(squelette(forme), epaisseur / 2f)
    is Forme.Chemin -> error("Le visage ne porte pas de tracé SVG libre : $nom")
}

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

/**
 * La silhouette d'un tracé épaissi de [rayon] de chaque côté, bouts arrondis compris — c'est ce que
 * dessine un trait à terminaison ronde (§4), et le visage est le seul endroit qui en porte.
 */
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

/**
 * Le demi-cercle qui ferme un bout : il part de [depuis], passe par [dehors] et arrive à l'opposé
 * de [depuis]. Les deux extrémités appartiennent déjà aux bords, donc seuls les points intérieurs
 * sont produits — la silhouette ne porte aucun point en double, sauf pour l'ellipse, dont les deux
 * bords sont réduits à un point.
 */
private fun bout(centre: Offset, depuis: Offset, dehors: Offset, rayon: Float): List<Offset> =
    List(POINTS_PAR_QUART) { indice ->
        val angle = PI.toFloat() * (indice + 1f) / (POINTS_PAR_QUART + 1f)
        centre + (depuis * cos(angle) + dehors * sin(angle)) * rayon
    }

private fun Contour.etire(facteurVertical: Float) =
    Contour(points.map { point -> Offset(point.x, point.y * facteurVertical) })

/** [POINTS_PAR_QUART] valeurs réparties de 0 à 1, les deux extrémités comprises. */
private fun <T> echantillons(valeur: (Float) -> T): List<T> =
    List(POINTS_PAR_QUART) { indice -> valeur(indice.toFloat() / (POINTS_PAR_QUART - 1)) }

private val VERS_LA_DROITE = Offset(1f, 0f)

/** La perpendiculaire qui monte quand la tangente va vers la droite *(y croît vers le bas)*. */
private fun Offset.normale() = Offset(y, -x)

private fun Offset.unitaire(): Offset = when (val longueur = getDistance()) {
    0f -> VERS_LA_DROITE
    else -> this / longueur
}
