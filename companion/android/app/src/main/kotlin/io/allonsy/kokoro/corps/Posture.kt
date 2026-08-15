package io.allonsy.kokoro.corps

/**
 * Les postures de départ — `companion/CORPS.md` §7.
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

    /**
     * ⭐ Écran de thérapie avant 18 h — `PRESENCE.md` §1.2 : *serein × regard vers la liste × repos*.
     * **Aucun geste** : le corps est celui du dessin, seuls les yeux sont sur la liste.
     */
    data object Pensif : Posture

    /** Au-dessus d'une liste de fiches : les bras sont avancés vers le bas, les yeux sur la liste. */
    data object Lecture : Posture

    /** Au-dessus du bilan : un bras en bas qui va et vient, les yeux baissés vers ce bras. */
    data object Notes : Posture

    /** Écran de crise : bras posés, panneau éteint. 🔴 **L'immobilité est du ressort de l'écran.** */
    data object Attente : Posture

    /** Liste vide : yeux fermés au repos. `sommeil` réutilise `veille` (`PRESENCE.md` §1.2). */
    data object Sommeil : Posture
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

/**
 * 🔴 **La butée basse : le bras ne croise jamais le corps.** À -19,5° il est exactement vertical, et
 * plus bas il passerait devant le ventre. Le garde-fou du haut vaut aussi en bas — les deux sont
 * calculés depuis la pose dessinée, aucun des deux ne se choisit.
 */
const val OUVERTURE_MINIMALE = -INCLINAISON_REPOS

/** Bras ramenés vers l'avant et le bas, à mi-chemin de la verticale — la posture de qui lit. */
const val OUVERTURE_AVANCEE = OUVERTURE_MINIMALE / 2f

/** Bras légèrement écartés : ce qu'ils font quand ils reposent sur quelque chose. */
const val OUVERTURE_POSEE = 12f

const val ECHELLE_RETRAIT = 0.4f

/**
 * Décalage horizontal des yeux, en unités de la vue, quand Kokoro regarde ce qu'il montre.
 *
 * ⭐ **C'est un réglage de posture, plus une expression.** Il vivait dans `de-cote` ; le regard est
 * devenu un axe à part (`PRESENCE.md` §1.2), et c'est ici qu'on le règle.
 */
const val REGARD_DESIGNATION = 5f

/**
 * Abaissement des yeux quand Kokoro regarde ce qui est **sous** lui — une liste, ou son propre bras.
 *
 * ⭐ **C'est le second axe du regard**, et il n'en dit pas plus que le premier : baisser les yeux
 * vers ce qu'on lit n'est pas une information à décoder.
 */
const val REGARD_BAISSE = 4f

data class ReglagePosture(
    val expression: Expression,
    val panneauAllume: Boolean,
    val ouvertureBrasGauche: Float,
    val ouvertureBrasDroit: Float,
    val regard: Float,
    val abaissement: Float,
    /** Le côté du bras qui écrit, ou `null` — **la seule posture qui bouge d'elle-même.** */
    val ecriture: Cote?,
    val echelle: Float,
)

fun Posture.reglage(): ReglagePosture = when (this) {
    Posture.Repos -> reglageDeBase(Expression.SEREIN)

    Posture.Present -> reglageDeBase(Expression.ATTENTIF)

    is Posture.Montre -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = if (cote == Cote.GAUCHE) OUVERTURE_HORIZONTALE else OUVERTURE_REPOS,
        ouvertureBrasDroit = if (cote == Cote.DROITE) OUVERTURE_HORIZONTALE else OUVERTURE_REPOS,
        regard = when (cote) {
            Cote.GAUCHE -> -REGARD_DESIGNATION
            Cote.DROITE -> REGARD_DESIGNATION
        },
    )

    Posture.CoteACote -> reglageDeBase(Expression.SEREIN).copy(panneauAllume = false)

    Posture.Retrait -> reglageDeBase(Expression.SEREIN).copy(
        panneauAllume = false,
        echelle = ECHELLE_RETRAIT,
    )

    Posture.Pensif -> reglageDeBase(Expression.SEREIN).copy(abaissement = REGARD_BAISSE)

    Posture.Lecture -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = OUVERTURE_AVANCEE,
        ouvertureBrasDroit = OUVERTURE_AVANCEE,
        abaissement = REGARD_BAISSE,
    )

    Posture.Notes -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = OUVERTURE_AVANCEE,
        regard = -REGARD_DESIGNATION / 2f,
        abaissement = REGARD_BAISSE,
        ecriture = Cote.GAUCHE,
    )

    Posture.Attente -> reglageDeBase(Expression.SEREIN).copy(
        panneauAllume = false,
        ouvertureBrasGauche = OUVERTURE_POSEE,
        ouvertureBrasDroit = OUVERTURE_POSEE,
    )

    Posture.Sommeil -> reglageDeBase(Expression.VEILLE)
}

/** Le regard part au centre : une posture qui regarde ailleurs le dit, sinon il ne se décale pas. */
private fun reglageDeBase(expression: Expression) = ReglagePosture(
    expression = expression,
    panneauAllume = true,
    ouvertureBrasGauche = OUVERTURE_REPOS,
    ouvertureBrasDroit = OUVERTURE_REPOS,
    regard = 0f,
    abaissement = 0f,
    ecriture = null,
    echelle = 1f,
)
