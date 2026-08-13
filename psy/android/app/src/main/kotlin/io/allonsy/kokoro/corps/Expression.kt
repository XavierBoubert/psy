package io.allonsy.kokoro.corps

/**
 * Les six expressions — `psy/android/design/CORPS.md` §3. **Le jeu est fermé.**
 *
 * Une expression est un jeu de trois formes : un œil (dessiné deux fois, symétrie stricte) et une
 * bouche. Aucun sourcil n'existe dans le jeu de pièces, donc le reproche est indessinable.
 */
enum class Expression(
    val oeil: Trace,
    val bouche: Trace,
    val regardParDefaut: Float,
) {
    /** Par défaut : accueil, veille, overlay. */
    NEUTRE(OEIL_OVALE, BOUCHE_TRAIT, 0f),

    /** Une étape est ouverte, un contenu est affiché. */
    ATTENTIF(OEIL_OVALE, BOUCHE_BARRE, 0f),

    /** Une étape est faite. Jamais en réaction à une étape non faite (§8 point 4). */
    CHALEUREUX(OEIL_ARC_HAUT, BOUCHE_ARC, 0f),

    /** Transition uniquement, jamais un état stable. */
    CLIGNEMENT(OEIL_TRAIT, BOUCHE_COURTE, 0f),

    /** Mode shutdown, écran en veille. */
    VEILLE(OEIL_ARC_BAS, BOUCHE_COURTE, 0f),

    /** Accompagne une désignation : il regarde ce qu'il montre, jamais le lecteur. */
    DE_COTE(OEIL_OVALE, BOUCHE_COURTE, -REGARD_DESIGNATION),
    ;

    val yeuxOuverts: Boolean get() = oeil == OEIL_OVALE
}

/** Décalage horizontal des yeux, en unités de la vue, quand Kokoro regarde ce qu'il montre. */
const val REGARD_DESIGNATION = 5f

/**
 * Une expression sortante, une expression entrante, et l'avancement de l'une vers l'autre.
 * ⭐ **Les formes se déforment l'une vers l'autre** (§9) : à aucun instant deux visages ne sont
 * dessinés l'un sur l'autre. La déformation est calculée par [Contour].
 */
data class Visage(
    val depuis: Expression,
    val vers: Expression,
    val progression: Float,
) {
    val stable: Boolean get() = depuis == vers || progression >= 1f

    companion object {
        fun de(expression: Expression) = Visage(expression, expression, 1f)
    }
}
