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

/**
 * Le repos, c'est le dessin — zéro degré d'écart.
 *
 * La v1 écartait les bras de 8° parce qu'ils étaient dessinés à la verticale. La v2 les dessine
 * déjà écartés : toute ouverture ajoutée par le rig part de là.
 */
const val OUVERTURE_REPOS = 0f

/**
 * Écart du bras par rapport à la verticale dans le dessin : l'axe qui joint le centre du bouchon
 * d'épaule au centre du bouchon bas fait 19,5° avec la verticale.
 */
const val INCLINAISON_REPOS = 19.463f

/**
 * Bras amené à l'horizontale — **et pas plus haut.**
 *
 * 🔴 C'est le garde-fou 1 du §6 : le bras ne dépasse jamais la ligne des épaules. La borne se
 * calcule depuis la pose dessinée, elle ne se choisit pas ; `CorpsInvariantsTest` la vérifie.
 */
const val OUVERTURE_HORIZONTALE = 90f - INCLINAISON_REPOS

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
