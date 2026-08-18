package io.allonsy.kokoro.corps

sealed interface Posture {
    data object Repos : Posture

    data object Present : Posture

    data class Montre(val cote: Cote) : Posture

    data object CoteACote : Posture

    data object Retrait : Posture

    data object Pensif : Posture

    data object Lecture : Posture

    data object Notes : Posture

    // Écran du bilan : ciseaux de bras alternés, intermittents — jamais un geste continu (§4.3).
    data object Floss : Posture

    // Écran de crise : panneau toujours allumé — visage visible, jamais présence muette.
    data object Accoude : Posture

    data object Sommeil : Posture
}

enum class Cote { GAUCHE, DROITE }

const val OUVERTURE_REPOS = 0f

// Écart du bras à la verticale dans le dessin (bouchon d'épaule → bouchon bas) : 19,5°.
const val INCLINAISON_REPOS = 19.463f

// Garde-fou (§6) : le bras ne dépasse jamais la ligne des épaules ; vérifié par `CorpsInvariantsTest`.
const val OUVERTURE_HORIZONTALE = 90f - INCLINAISON_REPOS

// Butée basse : le bras ne croise jamais le corps (vertical à -19,5°).
const val OUVERTURE_MINIMALE = -INCLINAISON_REPOS

// État de passage uniquement (crise) — jamais une posture tenue, ramené à OUVERTURE_HORIZONTALE ensuite.
const val OUVERTURE_BRAS_LEVES = OUVERTURE_HORIZONTALE + 90f

const val OUVERTURE_AVANCEE = OUVERTURE_MINIMALE / 2f

// Dérivée du dessin (atan2), pas choisie — seule valeur du jeu à dépasser OUVERTURE_HORIZONTALE, volontairement.
const val OUVERTURE_MAIN_AU_MENTON = -139.217f

// Seule dérogation à l'axe non-incliné (companion/README.md §6) — bornée, sinon ce serait une pose à interpréter.
const val INCLINAISON_TETE = -6f

const val INCLINAISON_TETE_MAX = 10f

const val OUVERTURE_POSEE = 12f

const val ECHELLE_RETRAIT = 0.4f

const val REGARD_DESIGNATION = 5f

const val REGARD_BAISSE = 4f

data class ReglagePosture(
    val expression: Expression,
    val panneauAllume: Boolean,
    val ouvertureBrasGauche: Float,
    val ouvertureBrasDroit: Float,
    val regard: Float,
    val abaissement: Float,
    // Seul champ qui bouge de lui-même — tout le reste est statique tant que la posture ne change pas.
    val ecriture: Cote?,
    val inclinaisonTete: Float,
    val echelle: Float,
    val sommeil: Boolean = false,
    val danse: Boolean = false,
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

    // ouvertureBrasGauche porte le bras droit de Kokoro (`arm-right` dans le SVG) — pas une inversion à corriger.
    Posture.Lecture -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = OUVERTURE_MAIN_AU_MENTON,
        ouvertureBrasDroit = OUVERTURE_AVANCEE,
        abaissement = REGARD_BAISSE,
    )

    Posture.Notes -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = OUVERTURE_AVANCEE,
        regard = -REGARD_DESIGNATION / 2f,
        abaissement = REGARD_BAISSE,
        ecriture = Cote.GAUCHE,
    )

    // Position de repos du ciseau ; l'amplitude et le rythme de la danse viennent de danseAnimee (AnimationCorps.kt).
    Posture.Floss -> reglageDeBase(Expression.CHALEUREUX).copy(
        ouvertureBrasGauche = DANSE_CENTRE,
        ouvertureBrasDroit = DANSE_CENTRE,
        danse = true,
    )

    // Regarde droit devant lui : rien ici ne connaît la position de Xavier.
    Posture.Accoude -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = OUVERTURE_HORIZONTALE,
        ouvertureBrasDroit = OUVERTURE_HORIZONTALE,
        inclinaisonTete = INCLINAISON_TETE,
    )

    // Pose lue dans kokoro-corps-v2-sleep.svg (POSE_SOMMEIL_* dans Geometrie.kt).
    Posture.Sommeil -> reglageDeBase(Expression.VEILLE).copy(sommeil = true)
}

private fun reglageDeBase(expression: Expression) = ReglagePosture(
    expression = expression,
    panneauAllume = true,
    ouvertureBrasGauche = OUVERTURE_REPOS,
    ouvertureBrasDroit = OUVERTURE_REPOS,
    regard = 0f,
    abaissement = 0f,
    ecriture = null,
    inclinaisonTete = 0f,
    echelle = 1f,
)
