package io.allonsy.kokoro.corps

enum class Expression(val oeil: Trace, val bouche: Trace) {
    // Dessin de Xavier tel quel ; reste dans le jeu même si aucune posture ne l'appelle plus.
    NEUTRE(OEIL_OVALE, BOUCHE_TRAIT),
    SEREIN(OEIL_OVALE, BOUCHE_SEMI),
    ATTENTIF(OEIL_OVALE, BOUCHE_BARRE),
    // Jamais utilisée en réaction à une étape non faite.
    CHALEUREUX(OEIL_ARC_HAUT, BOUCHE_ARC),
    // Transition uniquement, jamais un état stable.
    CLIGNEMENT(OEIL_TRAIT, BOUCHE_COURTE),
    VEILLE(OEIL_ARC_BAS, BOUCHE_COURTE),
    PARLE(OEIL_OVALE, BOUCHE_OUVERTE),
    ;

    val yeuxOuverts: Boolean get() = oeil == OEIL_OVALE
}

data class Morphing(
    val depuis: Trace,
    val vers: Trace,
    val progression: Float,
) {
    val stable: Boolean get() = depuis == vers || progression >= 1f

    companion object {
        fun de(trace: Trace) = Morphing(trace, trace, 1f)
    }
}

data class Visage(val oeil: Morphing, val bouche: Morphing) {
    companion object {
        fun de(expression: Expression) =
            Visage(Morphing.de(expression.oeil), Morphing.de(expression.bouche))
    }
}
