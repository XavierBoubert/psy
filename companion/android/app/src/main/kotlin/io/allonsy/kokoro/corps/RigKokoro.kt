package io.allonsy.kokoro.corps

import androidx.compose.ui.geometry.Offset

data class RigKokoro(
    val visage: Visage,
    val panneauAllume: Boolean = true,
    // 0 = expiration, 1 = inspiration ; n'anime que le torse.
    val respiration: Float = 0f,
    // Degrés depuis la pose dessinée, autour de l'épaule ; positif = le bras s'écarte du corps.
    val ouvertureBrasGauche: Float = 0f,
    val ouvertureBrasDroit: Float = 0f,
    // Degrés depuis la pose dessinée, autour de CENTRE_VENTRE ; positif = le pied s'écarte.
    val orbitePiedGauche: Float = 0f,
    val orbitePiedDroit: Float = 0f,
    // Pose empruntée à un autre dessin (sommeil, vol), surimposée à l'ouverture normale ; identité au repos.
    val poseBrasGauche: Transformation = Transformation(),
    val poseBrasDroit: Transformation = Transformation(),
    val posePiedGauche: Transformation = Transformation(),
    val posePiedDroit: Transformation = Transformation(),
    val vol: PoseTronc = PoseTronc(),
    // Négatif = vers la gauche de l'écran.
    val regard: Float = 0f,
    // Positif = les yeux descendent vers ce qui est sous le personnage.
    val abaissement: Float = 0f,
    // Négatif = penche vers la gauche ; zéro partout sauf la posture accoude.
    val inclinaisonTete: Float = 0f,
    val decalage: Offset = Offset.Zero,
    val inclinaison: Float = 0f,
    val echelle: Float = 1f,
    // Suit le personnage en x uniquement ; l'écart en y donne la hauteur de vol.
    val ombre: Ombre? = null,
) {
    // Pas de butée ici : la borne sur l'ouverture est appliquée par les postures.
    val rotationBrasGauche: Float get() = ouvertureBrasGauche

    val rotationBrasDroit: Float get() = -ouvertureBrasDroit

    val rotationPiedGauche: Float get() = orbitePiedGauche

    val rotationPiedDroit: Float get() = -orbitePiedDroit

    val etirementCorps: Float get() = 1f + AMPLITUDE_HAUTEUR * respiration.coerceIn(0f, 1f)

    // Négatif = vers le haut ; les pieds ne le reçoivent jamais, ils ne bougent pas avec le souffle.
    val decalageRespirationHaut: Float
        get() = (EPAULE_GAUCHE.y - PIVOT_RESPIRATION.y) * (etirementCorps - 1f)

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
                abaissement = abaissement,
                inclinaisonTete = inclinaisonTete,
                echelle = echelle,
            )
        }
    }
}

// 0.13 donne environ 6,5 dp de montée de tête, soit les deux tiers des 10 dp de la lévitation.
const val AMPLITUDE_HAUTEUR = 0.13f
