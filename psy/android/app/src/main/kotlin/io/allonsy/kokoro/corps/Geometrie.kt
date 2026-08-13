package io.allonsy.kokoro.corps

/**
 * Géométrie de Kokoro — `psy/android/design/CORPS.md` §2, §4 et §9.
 *
 * Chaque pièce est une donnée de chemin SVG exprimée dans son repère local, l'origine du repère
 * étant son pivot. Le placement dans le personnage est porté par l'ancre, jamais par le chemin :
 * c'est ce qui permet de faire tourner un bras autour de son épaule sans redessiner sa forme.
 *
 * Le même jeu de chemins est repris tel quel dans `design/retenus/kokoro-corps.svg`
 * (surface web desktop, §10) — `CorpsInvariantsTest` vérifie qu'ils ne divergent pas.
 */

const val LARGEUR_VUE = 240f
const val HAUTEUR_VUE = 201f

private const val SOMMET_TETE = 10f
private const val BAS_PIEDS = 191f

const val HAUTEUR_PERSONNAGE = BAS_PIEDS - SOMMET_TETE

/**
 * Épaisseur unique, constante à toutes les tailles (§4).
 *
 * ⚠️ CORPS.md §4 annonce 2,5 % de la hauteur ; mesuré sur la planche retenue
 * (`retenus/kokoro-corps.png`), le trait est à 1,6 %. À 2,5 % le panneau-visage est écrasé et le
 * contraste devient dur — ce que §4 cherche justement à éviter. La planche fait foi, le chiffre
 * de CORPS.md est à corriger.
 */
const val EPAISSEUR_TRAIT = HAUTEUR_PERSONNAGE * 0.016f

data class Ancre(val x: Float, val y: Float)

data class Piece(val nom: String, val chemin: String, val ancre: Ancre)

/** Un tracé de visage : une forme échangée, jamais déformée (§9). */
data class Trace(val nom: String, val chemin: String, val remplie: Boolean, val epaisseur: Float = 1f)

private const val CHEMIN_TETE =
    "M -34 -43 L 34 -43 Q 60 -43 60 -17 L 60 17 Q 60 43 34 43 " +
        "L -34 43 Q -60 43 -60 17 L -60 -17 Q -60 -43 -34 -43 Z"

private const val CHEMIN_PANNEAU =
    "M -29 -28.5 L 29 -28.5 Q 47 -28.5 47 -10.5 L 47 10.5 Q 47 28.5 29 28.5 " +
        "L -29 28.5 Q -47 28.5 -47 10.5 L -47 -10.5 Q -47 -28.5 -29 -28.5 Z"

private const val CHEMIN_CORPS =
    "M -11 -69 L 11 -69 Q 33 -69 33 -47 L 33 -22 Q 33 0 11 0 " +
        "L -11 0 Q -33 0 -33 -22 L -33 -47 Q -33 -69 -11 -69 Z"

private const val CHEMIN_PLAQUE =
    "M -6 -13 L 6 -13 Q 13 -13 13 -6 L 13 6 Q 13 13 6 13 " +
        "L -6 13 Q -13 13 -13 6 L -13 -6 Q -13 -13 -6 -13 Z"

private const val CHEMIN_BRAS =
    "M -8 8 Q -8 0 0 0 Q 8 0 8 8 L 8 46 Q 8 54 0 54 Q -8 54 -8 46 Z"

private const val CHEMIN_PIED =
    "M -1.5 -7.5 L 1.5 -7.5 Q 9 -7.5 9 0 Q 9 7.5 1.5 7.5 " +
        "L -1.5 7.5 Q -9 7.5 -9 0 Q -9 -7.5 -1.5 -7.5 Z"

val TETE = Piece("tete", CHEMIN_TETE, Ancre(120f, 53f))
val PANNEAU = Piece("panneau", CHEMIN_PANNEAU, Ancre(120f, 55f))
val CORPS = Piece("corps", CHEMIN_CORPS, Ancre(120f, 171f))
val PLAQUE = Piece("plaque", CHEMIN_PLAQUE, Ancre(120f, 136f))
val BRAS_GAUCHE = Piece("bras-g", CHEMIN_BRAS, Ancre(76f, 114f))
val BRAS_DROIT = Piece("bras-d", CHEMIN_BRAS, Ancre(164f, 114f))
val PIED_GAUCHE = Piece("pied-g", CHEMIN_PIED, Ancre(110f, 184f))
val PIED_DROIT = Piece("pied-d", CHEMIN_PIED, Ancre(130f, 184f))

val OEIL_GAUCHE = Ancre(100f, 52f)
val OEIL_DROIT = Ancre(140f, 52f)
val BOUCHE = Ancre(120f, 75f)

/** Pivot de la racine : les transformations de vol tournent et redimensionnent autour de ce point. */
val PIVOT_RACINE = Ancre(120f, 100f)

/** Le corps respire autour de sa base : la tête ne bouge pas (§5, §9). */
val PIVOT_RESPIRATION = CORPS.ancre

val PIECES = listOf(TETE, PANNEAU, CORPS, PLAQUE, BRAS_GAUCHE, BRAS_DROIT, PIED_GAUCHE, PIED_DROIT)

val OEIL_OVALE = Trace(
    nom = "oeil-ovale",
    chemin = "M 0 -4.8 Q 3.4 -4.8 3.4 0 Q 3.4 4.8 0 4.8 Q -3.4 4.8 -3.4 0 Q -3.4 -4.8 0 -4.8 Z",
    remplie = true,
)

val OEIL_TRAIT = Trace("oeil-trait", "M -4.4 0 L 4.4 0", remplie = false)

/** Yeux fermés souriants — l'arc monte. */
val OEIL_ARC_HAUT = Trace("oeil-arc-haut", "M -4.8 2 Q 0 -3 4.8 2", remplie = false)

/** Yeux fermés au repos — l'arc descend. */
val OEIL_ARC_BAS = Trace("oeil-arc-bas", "M -4.8 -2 Q 0 3 4.8 -2", remplie = false)

val BOUCHE_TRAIT = Trace("bouche-trait", "M -10 0 L 10 0", remplie = false)

val BOUCHE_BARRE = Trace("bouche-barre", "M -7 0 L 7 0", remplie = false, epaisseur = 1.9f)

/**
 * Convexe vers le haut, et c'est le seul arc de bouche qui existe.
 * Une bouche concave vers le bas n'est pas interdite par discipline : elle n'est pas dessinée (§3).
 */
val BOUCHE_ARC = Trace("bouche-arc", "M -6.5 -2 Q 0 4 6.5 -2", remplie = false)

val BOUCHE_COURTE = Trace("bouche-courte", "M -6 0 L 6 0", remplie = false)

val TRACES = listOf(
    OEIL_OVALE, OEIL_TRAIT, OEIL_ARC_HAUT, OEIL_ARC_BAS,
    BOUCHE_TRAIT, BOUCHE_BARRE, BOUCHE_ARC, BOUCHE_COURTE,
)
