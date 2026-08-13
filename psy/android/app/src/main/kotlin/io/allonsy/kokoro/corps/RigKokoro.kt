package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset

/**
 * L'état complet du personnage à un instant donné — la seule entrée de [CorpsKokoro].
 *
 * Tout est ici, rien n'est caché dans le dessin : une animation n'est qu'une suite de rigs.
 * Le rig est immuable ; on le fait avancer avec `copy`, ou avec [pose] / [deplace] / [redimensionne].
 *
 * ⭐ **Zéro, partout, c'est le dessin de Xavier.** Aucune ouverture, aucune orbite, aucun regard :
 * un rig par défaut rend `design/retenus/kokoro-corps-v2.svg` au pixel près.
 */
data class RigKokoro(
    val visage: Visage,
    val panneauAllume: Boolean = true,
    /** 0 = expiration, 1 = inspiration. N'anime que le torse (§5). */
    val respiration: Float = 0f,
    /** Degrés depuis la pose dessinée, autour de l'épaule. Positif = le bras s'écarte du corps. */
    val ouvertureBrasGauche: Float = 0f,
    val ouvertureBrasDroit: Float = 0f,
    /** Degrés depuis la pose dessinée, autour de [CENTRE_VENTRE]. Positif = le pied s'écarte. */
    val orbitePiedGauche: Float = 0f,
    val orbitePiedDroit: Float = 0f,
    /** Décalage horizontal des deux yeux, du même côté. Négatif = vers la gauche de l'écran. */
    val regard: Float = 0f,
    /** Translation de la racine, en unités de la vue — le vol. */
    val decalage: Offset = Offset.Zero,
    /** Rotation de la racine, en degrés, autour de [PIVOT_RACINE] — l'inclinaison en vol. */
    val inclinaison: Float = 0f,
    val echelle: Float = 1f,
) {
    /**
     * Rotation appliquée au bras de gauche, en degrés Compose (sens horaire).
     * Le bras pend déjà écarté de [INCLINAISON_REPOS] dans le dessin : une ouverture positive
     * l'écarte davantage, et le pivot n'a pas de butée matérielle — la borne est dans les postures.
     */
    val rotationBrasGauche: Float get() = ouvertureBrasGauche

    val rotationBrasDroit: Float get() = -ouvertureBrasDroit

    val rotationPiedGauche: Float get() = orbitePiedGauche

    val rotationPiedDroit: Float get() = -orbitePiedDroit

    /** Étirement vertical du torse sous l'effet de la respiration. */
    val etirementCorps: Float get() = 1f + AMPLITUDE_HAUTEUR * respiration.coerceIn(0f, 1f)

    /** Rétraction horizontale correspondante. */
    val retractionCorps: Float get() = 1f - AMPLITUDE_LARGEUR * respiration.coerceIn(0f, 1f)

    fun deplace(decalage: Offset, inclinaison: Float = 0f) =
        copy(decalage = decalage, inclinaison = inclinaison)

    fun redimensionne(echelle: Float) = copy(echelle = echelle)

    companion object {
        fun pose(posture: Posture): RigKokoro = with(posture.reglage()) {
            RigKokoro(
                visage = Visage.de(expression),
                panneauAllume = panneauAllume,
                ouvertureBrasGauche = ouvertureBrasGauche,
                ouvertureBrasDroit = ouvertureBrasDroit,
                regard = regard,
                echelle = echelle,
            )
        }
    }
}

const val AMPLITUDE_HAUTEUR = 0.02f
const val AMPLITUDE_LARGEUR = 0.01f
