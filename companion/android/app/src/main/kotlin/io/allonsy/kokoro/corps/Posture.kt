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
     *
     * **Aucun geste** : le corps est celui du dessin, les yeux sont baissés vers la liste. 🔄 **Et
     * ils ne la parcourent plus** *(demande de Xavier, 16/08/2026 : il ne lit pas dans Thérapie)* —
     * le balayage est retiré côté place ([io.allonsy.kokoro.monde.place]), **il ne reste que la
     * respiration**.
     */
    data object Pensif : Posture

    /** Au-dessus d'une liste de fiches : les bras sont avancés vers le bas, les yeux sur la liste. */
    data object Lecture : Posture

    /** Au-dessus du bilan : un bras en bas qui va et vient, les yeux baissés vers ce bras. */
    data object Notes : Posture

    /**
     * ⭐ **Écran de crise : accoudé sur le bouton comme sur un muret** — *« Kokoro veille sur toi »*
     * *(arbitrage de Xavier, 16/08/2026)*.
     *
     * Les deux bras à l'horizontale reposent sur l'arête du bouton, le corps passe derrière, la
     * tête dépasse au-dessus et **penche légèrement**. 🔴 **Le panneau reste allumé, et c'est tout
     * l'objet** : ce qu'on veut là, c'est un visage bienveillant qui regarde — pas une présence
     * muette. **C'est la seule posture du jeu qui incline la tête, et la seule qui ne vole pas.**
     */
    data object Accoude : Posture

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

/**
 * 🔴 **La butée basse : le bras ne croise jamais le corps.** À -19,5° il est exactement vertical, et
 * plus bas il passerait devant le ventre. Le garde-fou du haut vaut aussi en bas — les deux sont
 * calculés depuis la pose dessinée, aucun des deux ne se choisit.
 */
const val OUVERTURE_MINIMALE = -INCLINAISON_REPOS

/**
 * Les bras levés de l'arrivée à la crise — *« les bras pointent vers le haut, puis s'affaissent sur
 * le bouton »*. **À la verticale, donc un quart de tour au-dessus de l'horizontale**, calculé depuis
 * elle et non choisi.
 *
 * ⚠️ **45° ne se voyait pas, et c'est de la géométrie** : le bouton cache tout ce qui passe sous son
 * arête, les bras pivotent aux épaules, et les épaules n'atteignent l'arête qu'à la toute fin de la
 * montée. Un bras à 45° ne dépasse alors jamais l'arête — il s'affaissait **derrière le bouton**. À
 * la verticale il la franchit dès la mi-montée, exactement quand l'affaissement commence.
 *
 * ⚠️ **C'est un état de passage, jamais une pose tenue** : il ne dure que le temps de sortir de
 * derrière le bouton, et [io.allonsy.kokoro.monde.Habitant] le ramène à [OUVERTURE_HORIZONTALE],
 * qui est la pose arbitrée. **Aucune posture du jeu ne le porte.**
 */
const val OUVERTURE_BRAS_LEVES = OUVERTURE_HORIZONTALE + 90f

/** Bras ramenés vers l'avant et le bas, à mi-chemin de la verticale — la posture de qui lit. */
const val OUVERTURE_AVANCEE = OUVERTURE_MINIMALE / 2f

/**
 * ⭐ **Le bras droit de `lecture`, main contre le menton** *(demande de Xavier, 16/08/2026 —
 * « plus basse, sur le menton plutôt »)* : la rotation, autour de [EPAULE_GAUCHE], qui amène le bout
 * du bras (le bouchon du bas, loin de l'épaule) sous le visage. Dérivée du dessin, pas choisie :
 * `atan2` de la cible moins `atan2` du bout de bras, tous deux depuis l'épaule.
 *
 * ⭐ **La cible est le menton, plus la bouche** : le bouchon arrive à `y ≈ 85,8`, entre la bouche
 * *(74,6)* et le bas de la coque *(90,8)*. À la bouche il fallait -151,66°, et la main montait sur
 * le visage.
 *
 * ⚠️ **Elle dépasse la ligne des épaules** (§6 garde-fou 1, [OUVERTURE_HORIZONTALE]) — la seule
 * posture du jeu qui le fasse. La borne protège un geste tourné vers Xavier ; celui-ci se tourne
 * vers Kokoro lui-même, jamais vers le lecteur, donc il n'a pas le sens que la borne exclut. **Elle
 * ne s'applique qu'ici** : `OUVERTURE_HORIZONTALE` continue de border la désignation sans changement.
 */
const val OUVERTURE_MAIN_AU_MENTON = -139.217f

/**
 * ⭐ **L'inclinaison de la tête d'`accoude`** — négatif : elle penche vers la gauche de l'écran.
 *
 * 🔴 **Bornée, et petite.** Une tête qui penche est ce qui distingue *veiller sur quelqu'un* de
 * *fixer quelqu'un* ; au-delà d'une dizaine de degrés elle devient une pose à interpréter, et
 * `CORPS.md` §2 pose que le personnage est vu de face, sans axe incliné. **C'est la seule
 * dérogation, et elle ne porte que sur la tête.** ⏳ **À juger à l'écran.**
 */
const val INCLINAISON_TETE = -6f

/** La borne de l'inclinaison de tête — au-delà, ce n'est plus une nuance, c'est une pose. */
const val INCLINAISON_TETE_MAX = 10f

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
    val inclinaisonTete: Float,
    val echelle: Float,
    /** Vrai pour `Posture.Sommeil` seul — c'est ce qui amène bras et pieds à leur pose de sommeil. */
    val sommeil: Boolean = false,
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

    /**
     * ⭐ **Le bras droit main contre le menton** *(demande de Xavier, 16/08/2026)* : seul
     * `ouvertureBrasGauche` change — c'est lui qui porte le bras droit de Kokoro, `arm-right` dans
     * le SVG ([Geometrie.kt][BRAS_GAUCHE]). Le bras gauche garde sa posture de lecture inchangée.
     */
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

    /**
     * ⭐ **Les deux bras exactement à l'horizontale, et ce n'est pas une valeur choisie** : c'est la
     * ligne des épaules, celle où le bord haut du bouton vient passer. **Les bras reposent dessus,
     * le corps est derrière.** Le regard reste au centre — 🔴 **il regarde devant lui, il ne suit
     * personne** : rien dans le code ne connaît la position de Xavier.
     */
    Posture.Accoude -> reglageDeBase(Expression.SEREIN).copy(
        ouvertureBrasGauche = OUVERTURE_HORIZONTALE,
        ouvertureBrasDroit = OUVERTURE_HORIZONTALE,
        inclinaisonTete = INCLINAISON_TETE,
    )

    /**
     * ⭐ **Bras et pieds à la pose du sommeil** *(demande de Xavier, 16/08/2026)*, lue dans
     * `retenus/kokoro-corps-v2-sleep.svg` ([Geometrie.kt][POSE_SOMMEIL_BRAS_GAUCHE]) — `sommeil`
     * anime le passage de la pose de repos à celle-ci, dans [rigAnime].
     */
    Posture.Sommeil -> reglageDeBase(Expression.VEILLE).copy(sommeil = true)
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
    inclinaisonTete = 0f,
    echelle = 1f,
)
