package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset

/**
 * L'état complet du personnage à un instant donné — la seule entrée de [CorpsKokoro].
 *
 * Tout est ici, rien n'est caché dans le dessin : une animation n'est qu'une suite de rigs.
 * Le rig est immuable ; on le fait avancer avec `copy`, ou avec [pose] / [deplace] / [redimensionne].
 */
data class RigKokoro(
    val visage: Visage,
    val panneauAllume: Boolean = true,
    /** 0 = expiration, 1 = inspiration. N'anime que le corps et la plaque (§5). */
    val respiration: Float = 0f,
    /** 0 = le long du corps, 90 = tendu à l'horizontale, et au-delà si on veut. */
    val ouvertureBrasGauche: Float = OUVERTURE_REPOS,
    val ouvertureBrasDroit: Float = OUVERTURE_REPOS,
    /** Décalage horizontal des deux yeux, du même côté. Négatif = vers la gauche de l'écran. */
    val regard: Float = 0f,
    /** Translation de la racine, en unités de la vue — le vol. */
    val decalage: Offset = Offset.Zero,
    /** Rotation de la racine, en degrés, autour de [PIVOT_RACINE] — l'inclinaison en vol. */
    val inclinaison: Float = 0f,
    val echelle: Float = 1f,
) {
    /**
     * Rotation appliquée au bras gauche, en degrés Compose (sens horaire).
     * Le bras pend vers le bas dans son repère local : une ouverture positive l'écarte vers
     * l'extérieur, et le pivot n'a pas de butée.
     */
    val rotationBrasGauche: Float get() = ouvertureBrasGauche

    val rotationBrasDroit: Float get() = -ouvertureBrasDroit

    /** Étirement vertical du corps sous l'effet de la respiration. */
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
