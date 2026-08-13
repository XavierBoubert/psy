package io.allonsy.kokoro.corps

/**
 * Les postures de départ — `psy/android/design/CORPS.md` §7.
 *
 * Ce sont des raccourcis vers des réglages de rig, pas un catalogue arrêté : on en ajoute autant
 * qu'on veut tant qu'on cherche ce que Kokoro va faire.
 */
sealed interface Posture {
    /** Par défaut, 99 % du temps. Seul cas sans texte à côté — parce qu'il ne dit rien. */
    data object Repos : Posture

    /** Une étape ou une fiche est ouverte. */
    data object Present : Posture

    /** Un bras tendu latéralement. Le texte à côté dit toujours ce qui est montré (§6). */
    data class Montre(val cote: Cote) : Posture

    /** Pendant un exercice : panneau éteint, zéro visage à lire au moment le plus chargé. */
    data object CoteACote : Posture

    /** Mode shutdown : réduit, en bord d'écran, panneau éteint. */
    data object Retrait : Posture
}

enum class Cote { GAUCHE, DROITE }

/** Bras le long du corps, très légèrement écartés. */
const val OUVERTURE_REPOS = 8f

/** Bras tendu à l'horizontale. Repère, pas borne : le pivot tourne librement. */
const val OUVERTURE_HORIZONTALE = 90f

/** Bras entrouverts, utilisé par l'atelier quand le rig est en vol. */
const val OUVERTURE_VOL = 40f

const val ECHELLE_RETRAIT = 0.4f

data class ReglagePosture(
    val expression: Expression,
    val panneauAllume: Boolean,
    val ouvertureBrasGauche: Float,
    val ouvertureBrasDroit: Float,
    val regard: Float,
    val echelle: Float,
)

fun Posture.reglage(): ReglagePosture = when (this) {
    Posture.Repos -> reglageDeBase(Expression.NEUTRE)

    Posture.Present -> reglageDeBase(Expression.ATTENTIF)

    is Posture.Montre -> reglageDeBase(Expression.DE_COTE).copy(
        ouvertureBrasGauche = if (cote == Cote.GAUCHE) OUVERTURE_HORIZONTALE else OUVERTURE_REPOS,
        ouvertureBrasDroit = if (cote == Cote.DROITE) OUVERTURE_HORIZONTALE else OUVERTURE_REPOS,
        regard = when (cote) {
            Cote.GAUCHE -> -REGARD_DESIGNATION
            Cote.DROITE -> REGARD_DESIGNATION
        },
    )

    Posture.CoteACote -> reglageDeBase(Expression.NEUTRE).copy(panneauAllume = false)

    Posture.Retrait -> reglageDeBase(Expression.NEUTRE).copy(
        panneauAllume = false,
        echelle = ECHELLE_RETRAIT,
    )
}

private fun reglageDeBase(expression: Expression) = ReglagePosture(
    expression = expression,
    panneauAllume = true,
    ouvertureBrasGauche = OUVERTURE_REPOS,
    ouvertureBrasDroit = OUVERTURE_REPOS,
    regard = expression.regardParDefaut,
    echelle = 1f,
)
