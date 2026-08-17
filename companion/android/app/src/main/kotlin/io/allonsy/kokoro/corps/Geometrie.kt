package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val LARGEUR_VUE = 240f
const val HAUTEUR_VUE = 200f

const val SOMMET_TETE = 11.062f

const val BAS_PIEDS = 188.938f

const val HAUTEUR_PERSONNAGE = BAS_PIEDS - SOMMET_TETE

// 🔴 hauteurPersonnage est celle du personnage, jamais de la vue (écart de 11 %, PRESENCE.md §1.4)
fun unitePour(hauteurPersonnage: Dp): Dp = hauteurPersonnage / HAUTEUR_PERSONNAGE

fun cadrePour(hauteurPersonnage: Dp): DpSize = with(unitePour(hauteurPersonnage)) {
    DpSize(width = this * LARGEUR_VUE, height = this * HAUTEUR_VUE)
}

const val EPAISSEUR_CONTOUR = 2f

const val EPAISSEUR_KANJI = 5f

private const val ECHELLE_BOUCHE = 0.965967f

const val EPAISSEUR_VISAGE = 3.5f * ECHELLE_BOUCHE

const val DEMI_BOUCHE = 10.753204f * ECHELLE_BOUCHE

data class Ancre(val x: Float, val y: Float)

data class Transformation(
    val a: Float = 1f,
    val b: Float = 0f,
    val c: Float = 0f,
    val d: Float = 1f,
    val e: Float = 0f,
    val f: Float = 0f,
) {
    fun sous(parent: Transformation) = Transformation(
        a = parent.a * a + parent.c * b,
        b = parent.b * a + parent.d * b,
        c = parent.a * c + parent.c * d,
        d = parent.b * c + parent.d * d,
        e = parent.a * e + parent.c * f + parent.e,
        f = parent.b * e + parent.d * f + parent.f,
    )

    fun applique(point: Ancre) = Ancre(a * point.x + c * point.y + e, b * point.x + d * point.y + f)

    // 🔴 exact seulement sans rotation — une rotation y perdrait sa longueur ; voir PoseMembre
    fun versIdentite(t: Float) =
        Transformation(1f + (a - 1f) * t, b * t, c * t, 1f + (d - 1f) * t, e * t, f * t)

    fun miroir() = Transformation(a, -b, -c, d, 2f * AXE * (1f - a) - e, 2f * AXE * b + f)

    val origine: Ancre get() = Ancre(e, f)

    val decalage: Offset get() = Offset(e, f)

    // règle SVG : l'échelle du trait suit sqrt(|det|) de la matrice
    val facteurTrait: Float get() = sqrt(abs(a * d - b * c))
}

fun translation(e: Float, f: Float) = Transformation(e = e, f = f)

sealed interface Forme {
    data class Chemin(val donnees: String) : Forme

    data class Ellipse(val rx: Float, val ry: Float) : Forme

    data class Segment(val x1: Float, val y1: Float, val x2: Float, val y2: Float) : Forme

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

enum class Terminaison { PLATE, ARRONDIE }

data class Piece(
    val nom: String,
    val forme: Forme,
    val transformation: Transformation,
    val groupe: Transformation,
    val remplissage: Remplissage = Remplissage.COQUE,
    // 0 = pas de contour
    val epaisseur: Float = EPAISSEUR_CONTOUR,
    val terminaison: Terminaison = Terminaison.PLATE,
) {
    val placement: Transformation get() = transformation.sous(groupe)

    val epaisseurRendue: Float get() = epaisseur * placement.facteurTrait
}

val RACINE = translation(1.103565f, -2.054931f)
private val GROUPE_TORSE = translation(0.000001f, 0.000001f).sous(RACINE)
private val GROUPE_TETE = translation(0.258172f, 0f).sous(RACINE)
private val GROUPE_KANJI =
    Transformation(0.428136f, 0f, 0f, 0.428136f, 83.106123f, 63.526899f).sous(GROUPE_TORSE)

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

val KANJI = listOf(
    traitKanji("kanji-1", CHEMIN_KANJI_1, translation(0f, 0.000001f)),
    traitKanji("kanji-2", CHEMIN_KANJI_2, translation(0.000001f, 0f)),
    traitKanji("kanji-3", CHEMIN_KANJI_3, translation(0f, 0.000001f)),
    traitKanji("kanji-4", CHEMIN_KANJI_4, Transformation()),
)

val TETE = Piece(
    nom = "head-out",
    forme = Forme.Chemin(CHEMIN_TETE),
    transformation = Transformation(1.279505f, 0f, 0f, 1.330691f, -33.148574f, -19.549175f),
    groupe = GROUPE_TETE,
)

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

// ⚠️ noms Kotlin = côté écran (voir Cote) ; nom SVG (champ `nom`) = côté personnage, inversés
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

// ordre de peinture du SVG : torse, tête, pieds, bras
val PIECES = listOf(TORSE, LIGNE_VENTRE) + KANJI + listOf(
    TETE, PANNEAU, PIED_GAUCHE, PIED_DROIT, BRAS_GAUCHE, BRAS_DROIT,
)

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

data class Trace(val nom: String, val forme: Forme, val epaisseur: Float = EPAISSEUR_VISAGE)

val TERMINAISON_VISAGE = Terminaison.ARRONDIE

val OEIL_OVALE = Trace("oeil-ovale", Forme.Ellipse(RAYON_OEIL_X, RAYON_OEIL_Y), epaisseur = 0f)

val OEIL_TRAIT = Trace("oeil-trait", Forme.Segment(-RAYON_OEIL_X, 0f, RAYON_OEIL_X, 0f))

// ⚠️ tracés dérivés posés par translation, jamais dans le repère (inversé) de la bouche — sinon arc concave (§3)
private fun arcSymetrique(demiLargeur: Float, extremites: Float, controle: Float) =
    Forme.Arc(-demiLargeur, extremites, 0f, controle, demiLargeur, extremites)

val OEIL_ARC_HAUT =
    Trace("oeil-arc-haut", arcSymetrique(RAYON_OEIL_X, extremites = 2.6f, controle = -4.2f))

val OEIL_ARC_BAS =
    Trace("oeil-arc-bas", arcSymetrique(RAYON_OEIL_X, extremites = -2.6f, controle = 4.2f))

val BOUCHE_TRAIT = Trace("bouche-trait", Forme.Segment(-DEMI_BOUCHE, 0f, DEMI_BOUCHE, 0f))

val BOUCHE_BARRE = Trace("bouche-barre", Forme.Segment(-7.3f, 0f, 7.3f, 0f))

private const val DEMI_SOURIRE = 7.6f
private const val EXTREMITES_SOURIRE = -2.4f
private const val CONTROLE_SOURIRE = 4.6f

// CorpsInvariantsTest vérifie que le milieu de l'arc descend toujours (§3) — aucune bouche tombante
val BOUCHE_ARC =
    Trace("bouche-arc", arcSymetrique(DEMI_SOURIRE, EXTREMITES_SOURIRE, CONTROLE_SOURIRE))

// ⭐ contrairement à chaleureux, ne réagit à rien : expression neutre du quotidien (PRESENCE.md §3)
val BOUCHE_SEMI = Trace(
    nom = "bouche-semi",
    forme = arcSymetrique(
        demiLargeur = (DEMI_BOUCHE + DEMI_SOURIRE) / 2f,
        extremites = EXTREMITES_SOURIRE / 2f,
        controle = CONTROLE_SOURIRE / 2f,
    ),
)

val BOUCHE_COURTE = Trace("bouche-courte", Forme.Segment(-6.4f, 0f, 6.4f, 0f))

val BOUCHE_OUVERTE = Trace("bouche-ouverte", Forme.Ellipse(4.2f, 2.8f), epaisseur = 0f)

val TRACES = listOf(
    OEIL_OVALE, OEIL_TRAIT, OEIL_ARC_HAUT, OEIL_ARC_BAS,
    BOUCHE_TRAIT, BOUCHE_BARRE, BOUCHE_ARC, BOUCHE_SEMI, BOUCHE_COURTE, BOUCHE_OUVERTE,
)

const val AXE = 119.959189f

val EPAULE_GAUCHE = Ancre(80.634638f, 111.095240f)
val EPAULE_DROITE = Ancre(159.283740f, 111.095252f)

// ⭐ pas une main (CORPS.md §2) — sert seulement à dériver un angle (lecture), jamais dessiné
val BOUT_DU_BRAS = Ancre(64.254500f, 158.603500f)

const val BAS_DE_LA_TETE = 90.845f

// 🔴 les bras débordent de 11,5 unités sous le bouton à l'horizontale — coupe le calque des bras pour accoude
const val DESCENTE_DU_BRAS_HORIZONTAL = 11.544f

// point fixe de la rotation portée par la matrice de foot-right dans le SVG, pas une valeur choisie
val CENTRE_VENTRE = Ancre(119.783517f, 137.708595f)

val PIVOT_RESPIRATION = Ancre(AXE, 169.425f)

// 🔴 milieu des deux épaules ; seule la posture accoude l'utilise, la tête ne bouge pas ailleurs (CORPS.md §9)
val PIVOT_TETE = Ancre(AXE, EPAULE_GAUCHE.y)

val PIVOT_RACINE = Ancre(AXE, HAUTEUR_VUE / 2f)

// 🔴 pivots exprimés dans l'espace de la vue (transportés par RACINE) — sans ce transport l'écart atteint 1 unité
data class PoseMembre(
    val angle: Float = 0f,
    val pivot: Ancre = Ancre(AXE, 0f),
    val decalage: Offset = Offset.Zero,
) {
    fun echelle(t: Float): PoseMembre = copy(angle = angle * t, decalage = decalage * t)

    fun miroir(): PoseMembre =
        copy(angle = -angle, pivot = Ancre(2f * AXE - pivot.x, pivot.y), decalage = Offset(-decalage.x, decalage.y))

    // 🔴 composer les matrices, jamais additionner les champs : sin(π·t) ≈ -8,7e-8 à t=1, pas 0 — a mal placé un pivot
    val transformation: Transformation
        get() = translation(decalage.x, decalage.y).sous(rotationAutour(angle, pivot))
}

// 🔴 étirement/glissement, jamais une rotation, sinon la tête sortirait de la vue de face (CORPS.md §2) — VolInvariantsTest revérifie à chaque build
data class PoseTronc(
    val torse: Transformation = Transformation(),
    val kanji: Transformation = Transformation(),
    val coque: Transformation = Transformation(),
    val panneau: Transformation = Transformation(),
    val oeilGauche: Transformation = Transformation(),
    val oeilDroit: Transformation = Transformation(),
    val bouche: Transformation = Transformation(),
) {
    fun echelle(t: Float) = PoseTronc(
        torse.versIdentite(t),
        kanji.versIdentite(t),
        coque.versIdentite(t),
        panneau.versIdentite(t),
        oeilGauche.versIdentite(t),
        oeilDroit.versIdentite(t),
        bouche.versIdentite(t),
    )

    fun miroir() = PoseTronc(
        torse = torse.miroir(),
        kanji = kanji.miroir(),
        coque = coque.miroir(),
        panneau = panneau.miroir(),
        oeilGauche = oeilDroit.miroir(),
        oeilDroit = oeilGauche.miroir(),
        bouche = bouche.miroir(),
    )
}

fun rotationAutour(degres: Float, pivot: Ancre): Transformation {
    val radians = degres * PI.toFloat() / 180f
    val cos = cos(radians)
    val sin = sin(radians)
    return Transformation(
        a = cos,
        b = sin,
        c = -sin,
        d = cos,
        e = pivot.x - cos * pivot.x + sin * pivot.y,
        f = pivot.y - sin * pivot.x - cos * pivot.y,
    )
}

val POSE_SOMMEIL_BRAS_GAUCHE = PoseMembre(angle = -14.047611f, pivot = Ancre(68.477912f, 33.020633f))
val POSE_SOMMEIL_BRAS_DROIT = PoseMembre(angle = 16.723473f, pivot = Ancre(170.388053f, 57.166890f))
val POSE_SOMMEIL_PIED_GAUCHE = PoseMembre(decalage = Offset(3.236246f, -4.118859f))
val POSE_SOMMEIL_PIED_DROIT = PoseMembre(decalage = Offset(-0.882613f, -5.001471f))

val POSE_VOL_DROITE_BRAS_GAUCHE = PoseMembre(angle = 17.178528f, pivot = Ancre(96.150744f, 140.625816f))
val POSE_VOL_DROITE_BRAS_DROIT = PoseMembre(angle = 35.983749f, pivot = Ancre(158.925797f, 96.667499f))
val POSE_VOL_DROITE_PIED_GAUCHE = PoseMembre(angle = 26.486476f, pivot = Ancre(120.348649f, 132.838997f))
val POSE_VOL_DROITE_PIED_DROIT = PoseMembre(angle = 34.941106f, pivot = Ancre(120.121253f, 132.150208f))

val POSE_VOL_DROITE_TRONC = PoseTronc(
    torse = Transformation(0.939725f, 0f, 0f, 1.037005f, 7.240213f, -4.896660f),
    kanji = Transformation(0.939725f, 0f, 0f, 1.037005f, 10.240213f, -4.896660f),
    coque = Transformation(0.871917f, 0f, 0f, 1f, 22.364748f, 0f),
    panneau = Transformation(0.752093f, 0f, 0f, 1f, 50.773043f, 0f),
    oeilGauche = translation(37f, 0f),
    oeilDroit = translation(27f, 0f),
    bouche = translation(34.059429f, 0f),
)

// ⭐ miroir du côté droit, jamais redessiné indépendamment
val POSE_VOL_GAUCHE_BRAS_GAUCHE = POSE_VOL_DROITE_BRAS_DROIT.miroir()
val POSE_VOL_GAUCHE_BRAS_DROIT = POSE_VOL_DROITE_BRAS_GAUCHE.miroir()
val POSE_VOL_GAUCHE_PIED_GAUCHE = POSE_VOL_DROITE_PIED_DROIT.miroir()
val POSE_VOL_GAUCHE_PIED_DROIT = POSE_VOL_DROITE_PIED_GAUCHE.miroir()
val POSE_VOL_GAUCHE_TRONC = POSE_VOL_DROITE_TRONC.miroir()
