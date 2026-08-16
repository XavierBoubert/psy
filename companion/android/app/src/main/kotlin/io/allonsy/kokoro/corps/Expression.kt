package io.allonsy.kokoro.corps

/**
 * Les expressions — `companion/CORPS.md` §3, amendé par `companion/PRESENCE.md` §1.2 et §5.
 * **Le jeu est fermé.**
 *
 * Une expression est un jeu de trois tracés : un œil (dessiné deux fois, symétrie stricte) et une
 * bouche. Aucun sourcil n'existe dans le jeu de pièces, donc le reproche est indessinable.
 *
 * ⭐ **Une expression ne porte plus de regard.** Le regard est devenu un axe indépendant, réglé par
 * la posture ([ReglagePosture.regard]) : c'est ce qui permet de regarder une liste sans inventer une
 * forme de visage pour chaque direction. `de-cote` a donc quitté le jeu — elle n'était que `serein`
 * plus un décalage des yeux.
 */
enum class Expression(val oeil: Trace, val bouche: Trace) {
    /** Le dessin de Xavier, tel quel. Reste dans le jeu ; aucune posture ne l'appelle plus. */
    NEUTRE(OEIL_OVALE, BOUCHE_TRAIT),

    /** ⭐ Par défaut : accueil, veille, overlay. Le semi-sourire ne demande rien et ne dit rien. */
    SEREIN(OEIL_OVALE, BOUCHE_SEMI),

    /** Une étape est ouverte, un contenu est affiché. */
    ATTENTIF(OEIL_OVALE, BOUCHE_BARRE),

    /** Une étape est faite. Jamais en réaction à une étape non faite (§8 point 4). */
    CHALEUREUX(OEIL_ARC_HAUT, BOUCHE_ARC),

    /** Transition uniquement, jamais un état stable. */
    CLIGNEMENT(OEIL_TRAIT, BOUCHE_COURTE),

    /** Mode shutdown, écran en veille. */
    VEILLE(OEIL_ARC_BAS, BOUCHE_COURTE),

    /**
     * ⭐ **Le locuteur du panneau, à l'arrivée** *(demande de Xavier, 16/08/2026)* — la bouche
     * entrouverte, comme s'il venait de se poser pour dire ce qui est écrit à côté.
     */
    PARLE(OEIL_OVALE, BOUCHE_OUVERTE),
    ;

    val yeuxOuverts: Boolean get() = oeil == OEIL_OVALE
}

/**
 * Un tracé sortant, un tracé entrant, et l'avancement de l'un vers l'autre.
 * ⭐ **Les formes se déforment l'une vers l'autre** (§9) : à aucun instant deux tracés ne sont
 * dessinés l'un sur l'autre. La déformation est calculée par [Contour].
 */
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

/**
 * Le visage à un instant donné — **les yeux et la bouche se déforment séparément.**
 *
 * 🔴 C'est ce qu'exige le clignement : fermer les yeux 200 ms ne doit pas faire tressaillir la
 * bouche. Tant que les deux axes suivent la même expression ils avancent ensemble ; un clignement
 * ne touche que le premier, et la bouche reste celle de l'expression courante.
 */
data class Visage(val oeil: Morphing, val bouche: Morphing) {
    companion object {
        fun de(expression: Expression) =
            Visage(Morphing.de(expression.oeil), Morphing.de(expression.bouche))
    }
}
