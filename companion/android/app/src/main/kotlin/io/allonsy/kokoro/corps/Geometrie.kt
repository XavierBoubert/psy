package io.allonsy.kokoro.corps

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Géométrie de Kokoro — **transcription littérale de `design/retenus/kokoro-corps-v2.svg`**.
 *
 * Rien n'est redessiné ici : chaque pièce porte les données de forme et la transformation telles
 * qu'elles sont écrites dans le SVG de Xavier, caractère pour caractère. Le placement passe par la
 * transformation, jamais par une réécriture du tracé — c'est ce qui rend la vérification possible,
 * et `CorpsInvariantsTest` échoue si un seul chiffre dérive du dessin.
 *
 * Les seules valeurs calculées sont les **pivots** (§9 de `design/CORPS.md`) : le SVG place les
 * pièces, il ne dit pas autour de quoi elles tournent. Chaque pivot est dérivé du dessin, jamais
 * choisi, et la dérivation est écrite à côté.
 */

const val LARGEUR_VUE = 240f
const val HAUTEUR_VUE = 200f

private const val SOMMET_TETE = 11.062f
private const val BAS_PIEDS = 188.938f

const val HAUTEUR_PERSONNAGE = BAS_PIEDS - SOMMET_TETE

/** Épaisseur des contours, telle que déclarée dans le SVG. Soit 1,1 % de la hauteur (§4). */
const val EPAISSEUR_CONTOUR = 2f

/** Épaisseur du kanji dans son propre repère — rendue à 5 × 0,428136 ≈ 2,14 (§4). */
const val EPAISSEUR_KANJI = 5f

/** L'échelle du repère où le SVG écrit la bouche — et un demi-tour, sans effet sur un segment. */
private const val ECHELLE_BOUCHE = 0.965967f

/**
 * Épaisseur du visage, rendue : la bouche du SVG déclare 3,5 dans un repère à l'échelle 0,965967.
 * Les tracés d'expression sont placés par une simple translation, donc ils la portent déjà rendue.
 */
const val EPAISSEUR_VISAGE = 3.5f * ECHELLE_BOUCHE

/** Demi-largeur de la bouche neutre, rendue : 10,753204 × 0,965967. */
const val DEMI_BOUCHE = 10.753204f * ECHELLE_BOUCHE

data class Ancre(val x: Float, val y: Float)

/** Une transformation SVG `matrix(a b c d e f)`. */
data class Transformation(
    val a: Float = 1f,
    val b: Float = 0f,
    val c: Float = 0f,
    val d: Float = 1f,
    val e: Float = 0f,
    val f: Float = 0f,
) {
    /** La composée `parent ∘ this` — ce qu'un groupe SVG applique à ce qu'il contient. */
    fun sous(parent: Transformation) = Transformation(
        a = parent.a * a + parent.c * b,
        b = parent.b * a + parent.d * b,
        c = parent.a * c + parent.c * d,
        d = parent.b * c + parent.d * d,
        e = parent.a * e + parent.c * f + parent.e,
        f = parent.b * e + parent.d * f + parent.f,
    )

    fun applique(point: Ancre) = Ancre(a * point.x + c * point.y + e, b * point.x + d * point.y + f)

    /** L'image de l'origine du repère local — le point que la transformation pose dans la vue. */
    val origine: Ancre get() = Ancre(e, f)

    /**
     * Facteur d'échelle du trait sous une matrice non uniforme, règle SVG : `sqrt(|det|)`.
     * `head-out` est le seul cas ici — il est étiré de 1,2795 en largeur et 1,3307 en hauteur.
     */
    val facteurTrait: Float get() = sqrt(abs(a * d - b * c))
}

fun translation(e: Float, f: Float) = Transformation(e = e, f = f)

/**
 * Ce qui se dessine : le SVG mélange `path`, `ellipse` et `line`, on garde les trois tels quels.
 *
 * [Forme.Arc] est le seul ajout, et il ne concerne que le visage : les expressions dérivées (§3) ne
 * sont pas dans le dessin, et une courbe écrite en paramètres se morphe, là où une chaîne SVG
 * demanderait d'être relue à chaque image ([MorphingVisage.kt][Contour]).
 */
sealed interface Forme {
    data class Chemin(val donnees: String) : Forme

    data class Ellipse(val rx: Float, val ry: Float) : Forme

    data class Segment(val x1: Float, val y1: Float, val x2: Float, val y2: Float) : Forme

    /** Un arc quadratique : deux extrémités et un point de contrôle. */
    data class Arc(
        val x1: Float,
        val y1: Float,
        val cx: Float,
        val cy: Float,
        val x2: Float,
        val y2: Float,
    ) : Forme
}

enum class Remplissage { AUCUN, COQUE, PANNEAU, ENCRE }

/**
 * Le SVG n'arrondit que ce qu'il déclare arrondi — seule la bouche porte `stroke-linecap="round"`.
 * Le kanji et la ligne du ventre sont à bouts plats, et c'est ce qui leur donne leur allure de trait
 * posé plutôt que de tube.
 */
enum class Terminaison { PLATE, ARRONDIE }

data class Piece(
    val nom: String,
    val forme: Forme,
    /** Celle de l'élément, telle qu'écrite dans le SVG. */
    val transformation: Transformation,
    /** La composée des groupes qui la contiennent, racine comprise. */
    val groupe: Transformation,
    val remplissage: Remplissage = Remplissage.COQUE,
    /** Déclarée dans le repère local ; zéro = pas de contour. */
    val epaisseur: Float = EPAISSEUR_CONTOUR,
    val terminaison: Terminaison = Terminaison.PLATE,
) {
    val placement: Transformation get() = transformation.sous(groupe)

    /** L'épaisseur telle qu'elle sort à l'écran, une fois le placement appliqué. */
    val epaisseurRendue: Float get() = epaisseur * placement.facteurTrait
}

// ————————————————————————————————————————————————————————————————————————————————————————————
// Les groupes du SVG
// ————————————————————————————————————————————————————————————————————————————————————————————

val RACINE = translation(1.103565f, -2.054931f)
private val GROUPE_TORSE = translation(0.000001f, 0.000001f).sous(RACINE)
private val GROUPE_TETE = translation(0.258172f, 0f).sous(RACINE)
private val GROUPE_KANJI =
    Transformation(0.428136f, 0f, 0f, 0.428136f, 83.106123f, 63.526899f).sous(GROUPE_TORSE)

// ————————————————————————————————————————————————————————————————————————————————————————————
// Les formes, mot pour mot
// ————————————————————————————————————————————————————————————————————————————————————————————

private const val CHEMIN_TORSE =
    "M112.886629,170.782292q-20.049918-2.551807-28.434429-17.49811c-5.483329-20.2847,10.024959" +
        "-47.937531,15.493118-55.775226c3.492093,2.480268,4.151352,2.62677,7.290879,3.280895c10" +
        ".411912,1.470154,10.99792,1.323652,23.695358,0c2.471867-1.070129,3.88548-.912817,6.933" +
        "836-3.510915c8.322183,9.737739,22.601464,44.774865,14.429306,58.324981-4.388321,10.645" +
        "333-19.670276,13.783998-26.102214,15.178375q-6.431938,1.394377-13.305854,0Z"

private const val CHEMIN_LIGNE_VENTRE =
    "M83.696179,148.260198c26.42389,4.11082,46.237509,4.836396,70.913495-.134306"

private const val CHEMIN_KANJI_1 =
    "M105.95734,122.066929c-.499889,3.582538-2.503066,11.451102-3.999112,15.079988"

private const val CHEMIN_KANJI_2 =
    "M111.622749,117.651242l-.083313,21.578547c-.258808,3.166149,2.183134,5.418589,4.499002,5.0" +
        "82206h8.755723c2.137374.085747,4.390459-2.154852,4.491337-4.499002l.833149-4.748946"

private const val CHEMIN_KANJI_3 = "M116.954901,110.902738l5.748725,10.331043"

private const val CHEMIN_KANJI_4 =
    "M129.618758,121.150466c.710724,1.666202,4.080542,8.576902,6.498559,13.580321"

private const val CHEMIN_TETE =
    "M133.483363,24.782435q23.330809,1.275903,26.429438,15.310847c3.098629,14.034944,1.093631,3" +
        "4.08486,1.093631,34.08486s-.546816,17.315837-22.237182,18.409469-23.14854,2.004991-42.4" +
        "69373-.546816Q76.979044,89.488988,76.614502,71.444061Q73.880423,51.94096,78.619494,38.0" +
        "88289t26.793981-13.123583q14.034949-.729086,28.069888-.182271Z"

private const val CHEMIN_PIED =
    "M90.274389,175.680176c-.468918-2.907282.281348-5.908346,4.314029-4.501596c3.845113,1.68809" +
        "8,5.626995,3.094846,13.129655,4.689163c4.03268.375132,5.158079,2.34458,5.251862,4.12646" +
        "2-2.063232,8.721842-5.251862,11.53534-12.660739,9.941025-5.064296-2.438365-8.252927-6.1" +
        "89695-10.034807-14.255054Z"

private const val CHEMIN_BRAS =
    "M56.604635,147.996249c5.362901-27.747221,18.420422-36.957429,18.420422-36.957429c5.518205-" +
        "3.995298,9.746596-1.373695,9.09362,4.54681-3.068008,9.132568-6.761922,27.863805-6.76192" +
        "2,27.863805-.308963,2.495288-.439838,4.151978-.699511,8.394115-1.357187,9.746221-7.4614" +
        "38,11.891665-7.461438,11.891665-3.683833,1.437391-10.026308-.466341-12.00825-5.829248-1" +
        ".755526-3.280931-.582927-9.909721-.582921-9.909718Z"

// ————————————————————————————————————————————————————————————————————————————————————————————
// Les pièces
//
// ⚠️ Les noms Kotlin disent le côté de **l'écran** — c'est ce que veut dire `Cote` partout
// ailleurs. Le SVG, lui, nomme les pièces du côté du **personnage**, qui nous fait face : son
// `arm-right` est donc notre bras de gauche. Le champ `nom` garde l'identifiant du SVG.
// ————————————————————————————————————————————————————————————————————————————————————————————

val TORSE = Piece("body-form", Forme.Chemin(CHEMIN_TORSE), translation(0.000001f, 0f), GROUPE_TORSE)

val LIGNE_VENTRE = Piece(
    nom = "body-line",
    forme = Forme.Chemin(CHEMIN_LIGNE_VENTRE),
    transformation = translation(0.000001f, 0f),
    groupe = GROUPE_TORSE,
    remplissage = Remplissage.AUCUN,
)

private fun traitKanji(nom: String, chemin: String, transformation: Transformation) = Piece(
    nom = nom,
    forme = Forme.Chemin(chemin),
    transformation = transformation,
    groupe = GROUPE_KANJI,
    remplissage = Remplissage.AUCUN,
    epaisseur = EPAISSEUR_KANJI,
)

/** 心 — quatre traits, posés à main levée sur le côté du cœur. Il remplace la plaque de la v1. */
val KANJI = listOf(
    traitKanji("kanji-1", CHEMIN_KANJI_1, translation(0f, 0.000001f)),
    traitKanji("kanji-2", CHEMIN_KANJI_2, translation(0.000001f, 0f)),
    traitKanji("kanji-3", CHEMIN_KANJI_3, translation(0f, 0.000001f)),
    traitKanji("kanji-4", CHEMIN_KANJI_4, Transformation()),
)

/** La coque de la tête : le même tracé que le panneau, étiré autour de lui. */
val TETE = Piece(
    nom = "head-out",
    forme = Forme.Chemin(CHEMIN_TETE),
    transformation = Transformation(1.279505f, 0f, 0f, 1.330691f, -33.148574f, -19.549175f),
    groupe = GROUPE_TETE,
)

/** Le panneau-visage : la seule surface où quelque chose s'affiche, et il s'éteint (§3). */
val PANNEAU = Piece(
    nom = "head-in",
    forme = Forme.Chemin(CHEMIN_TETE),
    transformation = translation(0.000005f, 0.000001f),
    groupe = GROUPE_TETE,
    remplissage = Remplissage.PANNEAU,
)

val PIED_GAUCHE = Piece(
    nom = "foot-right",
    forme = Forme.Chemin(CHEMIN_PIED),
    transformation = Transformation(0.998007f, 0.063107f, -0.063107f, 0.998007f, 9.056586f, -7.210987f),
    groupe = RACINE,
)

val PIED_DROIT = Piece(
    nom = "foot-left",
    forme = Forme.Chemin(CHEMIN_PIED),
    transformation = Transformation(-1f, 0f, 0f, 1f, 238.104569f, 0.633881f),
    groupe = RACINE,
)

val BRAS_GAUCHE = Piece(
    nom = "arm-right",
    forme = Forme.Chemin(CHEMIN_BRAS),
    transformation = translation(-0.040794f, -0.162054f),
    groupe = RACINE,
)

val BRAS_DROIT = Piece(
    nom = "arm-left",
    forme = Forme.Chemin(CHEMIN_BRAS),
    transformation = Transformation(-1f, 0f, 0f, 1f, 237.752042f, -0.162042f),
    groupe = RACINE,
)

/** Dans l'ordre de peinture du SVG : le torse, puis la tête, puis les pieds, puis les bras. */
val PIECES = listOf(TORSE, LIGNE_VENTRE) + KANJI + listOf(
    TETE, PANNEAU, PIED_GAUCHE, PIED_DROIT, BRAS_GAUCHE, BRAS_DROIT,
)

// ————————————————————————————————————————————————————————————————————————————————————————————
// Le visage
// ————————————————————————————————————————————————————————————————————————————————————————————

const val RAYON_OEIL_X = 6.771022f
const val RAYON_OEIL_Y = 8.915179f

private val PLACEMENT_OEIL_GAUCHE = translation(97.919861f, 55.533522f)
private val PLACEMENT_OEIL_DROIT = translation(139.841501f, 55.533522f)
private val PLACEMENT_BOUCHE = Transformation(
    -ECHELLE_BOUCHE, 0f, 0f, -ECHELLE_BOUCHE, 118.964349f, 76.605772f,
)

val OEIL_GAUCHE = PLACEMENT_OEIL_GAUCHE.sous(GROUPE_TETE).origine
val OEIL_DROIT = PLACEMENT_OEIL_DROIT.sous(GROUPE_TETE).origine
val BOUCHE = PLACEMENT_BOUCHE.sous(GROUPE_TETE).origine

/**
 * Un tracé de visage : une forme qui se déforme vers une autre quand l'expression change (§9).
 *
 * ⚠️ Seuls `oeil-ovale` et `bouche-trait` viennent du SVG — Xavier n'a dessiné que l'expression
 * `neutre`. Les six autres tracés en sont **dérivés** : mêmes demi-largeurs, même épaisseur rendue.
 * Ils sont posés par une simple translation, jamais dans le repère de la bouche : celui-ci porte un
 * demi-tour, et un arc convexe vers le haut y sortirait **concave vers le bas** — la seule forme
 * que §3 interdit.
 */
data class Trace(val nom: String, val forme: Forme, val epaisseur: Float = EPAISSEUR_VISAGE)

/** Le visage est le seul endroit du dessin où le SVG arrondit les bouts (§4). */
val TERMINAISON_VISAGE = Terminaison.ARRONDIE

val OEIL_OVALE = Trace("oeil-ovale", Forme.Ellipse(RAYON_OEIL_X, RAYON_OEIL_Y), epaisseur = 0f)

val OEIL_TRAIT = Trace("oeil-trait", Forme.Segment(-RAYON_OEIL_X, 0f, RAYON_OEIL_X, 0f))

/**
 * Un arc de visage est toujours symétrique autour de l'axe des yeux — la symétrie est écrite dans
 * la construction, elle n'est pas une propriété qu'on espère des chiffres.
 */
private fun arcSymetrique(demiLargeur: Float, extremites: Float, controle: Float) =
    Forme.Arc(-demiLargeur, extremites, 0f, controle, demiLargeur, extremites)

/** Yeux fermés souriants — l'arc monte. */
val OEIL_ARC_HAUT =
    Trace("oeil-arc-haut", arcSymetrique(RAYON_OEIL_X, extremites = 2.6f, controle = -4.2f))

/** Yeux fermés au repos — l'arc descend. */
val OEIL_ARC_BAS =
    Trace("oeil-arc-bas", arcSymetrique(RAYON_OEIL_X, extremites = -2.6f, controle = 4.2f))

val BOUCHE_TRAIT = Trace("bouche-trait", Forme.Segment(-DEMI_BOUCHE, 0f, DEMI_BOUCHE, 0f))

val BOUCHE_BARRE = Trace("bouche-barre", Forme.Segment(-7.3f, 0f, 7.3f, 0f))

/**
 * Le sourire, et c'est le seul arc de bouche qui existe : **les commissures montent, le milieu
 * descend.** Une bouche aux commissures tombantes n'est pas interdite par discipline — elle n'est
 * pas dessinée (§3), et `CorpsInvariantsTest` mesure le milieu de l'arc pour s'en assurer.
 */
val BOUCHE_ARC = Trace("bouche-arc", arcSymetrique(7.6f, extremites = -2.4f, controle = 4.6f))

val BOUCHE_COURTE = Trace("bouche-courte", Forme.Segment(-6.4f, 0f, 6.4f, 0f))

val TRACES = listOf(
    OEIL_OVALE, OEIL_TRAIT, OEIL_ARC_HAUT, OEIL_ARC_BAS,
    BOUCHE_TRAIT, BOUCHE_BARRE, BOUCHE_ARC, BOUCHE_COURTE,
)

// ————————————————————————————————————————————————————————————————————————————————————————————
// Les pivots — les seules valeurs que le SVG ne dit pas, et qu'on en déduit
// ————————————————————————————————————————————————————————————————————————————————————————————

/** Axe de symétrie du personnage, lu sur le miroir des deux bras. */
const val AXE = 119.959189f

/**
 * L'épaule : le milieu de la corde du bouchon arrondi qui termine le bras en haut.
 *
 * Le bras est une forme fermée détachée du corps ; sa seule articulation est ce bouchon, et une
 * rotation autour de son centre ne le déforme pas. Les deux valeurs sont symétriques autour de
 * [AXE] au millième près, ce qui vaut vérification du calcul.
 */
val EPAULE_GAUCHE = Ancre(80.634638f, 111.095240f)
val EPAULE_DROITE = Ancre(159.283740f, 111.095252f)

/**
 * Le centre du ventre — **écrit dans le dessin, pas choisi ici.**
 *
 * `foot-right` porte `matrix(0.998007 0.063107 -0.063107 0.998007 9.056586 -7.210987)`, qui est
 * exactement une rotation de +3,618°. Le point fixe de cette rotation est ce point : Xavier a fait
 * pivoter le pied autour du ventre, et la matrice a gardé la trace du pivot. Les deux pieds en sont
 * à 44,3 et 45,1 unités — même rayon, à la main-levée près.
 */
val CENTRE_VENTRE = Ancre(119.783517f, 137.708595f)

/** Le torse respire autour de sa base : la tête ne bouge pas (§5, §9). */
val PIVOT_RESPIRATION = Ancre(AXE, 169.425f)

/** Pivot de la racine : les transformations de vol tournent et redimensionnent autour de ce point. */
val PIVOT_RACINE = Ancre(AXE, HAUTEUR_VUE / 2f)
