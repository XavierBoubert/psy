package io.allonsy.kokoro.corps

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.sin

/**
 * Le vol et son ombre — `PRESENCE.md` §1.3 et §3.
 *
 * Kokoro ne pose pas les pieds : il lévite sur place, très peu, et **une ombre au sol dit de
 * combien**. Le déplacement de la racine est tout ce qu'il y a ici ; la posture, elle, ne change
 * pas d'un millimètre en vol.
 *
 * 🔴 **Tout ce qui bat ici bat sur l'horloge de la respiration** ([RESPIRATION_MILLIS]) : deux
 * périodes distinctes produiraient un battement lent entre elles, donc un rythme involontaire —
 * quelque chose à décoder, exactement ce que §4 interdit.
 */
enum class Vol { AUCUN, LEVITATION, SOMMEIL, TRAVERSEE }

/** Le déplacement de la racine à un instant donné. */
data class Deplacement(val decalage: Offset, val inclinaison: Float)

/**
 * L'amplitude de la lévitation — **9 % de la hauteur du personnage**, soit ≈ 16 unités.
 *
 * ⭐ **Portée à trois fois son ancienne valeur** *(demande de Xavier, 16/08/2026)* : à 3 % le
 * flottement ne se voyait pas. C'est toujours le même mouvement, seulement plus visible.
 */
const val LEVITATION_AMPLITUDE = 0.09f * HAUTEUR_PERSONNAGE

/**
 * Le quart de période qui sépare la lévitation du souffle (§3) — un quart de tour de phase.
 *
 * Même horloge, mais pas le même instant : le personnage n'atteint pas le haut de son vol au
 * sommet de son inspiration. **Les deux mouvements ne se renforcent jamais.**
 */
const val LEVITATION_DEPHASAGE = PI.toFloat() / 2f

/**
 * La hauteur de vol à un instant de l'horloge, en unités de la vue — **négatif = vers le haut**.
 *
 * 🔴 **Le bas du cycle, c'est la pose dessinée** : il ne descend jamais sous ses pieds, sinon
 * l'ombre passerait devant lui.
 */
fun levitation(phase: Float): Float =
    -LEVITATION_AMPLITUDE * (sin(phase + LEVITATION_DEPHASAGE) + 1f) / 2f

/**
 * Le vol du sommeil — **moitié moins vite, moitié moins haut** (`PRESENCE.md` §3).
 *
 * 🔴 **Ce n'est pas une seconde horloge, c'est la même divisée par deux.** L'horloge du corps fait
 * deux respirations par tour ([HORLOGE_MILLIS]) : une phase divisée par deux s'y referme donc
 * exactement, sans saut au bouclage, et **rien ne peut battre contre le souffle** puisqu'il n'y a
 * toujours qu'une horloge.
 */
fun levitationLente(phase: Float): Float = levitation(phase / 2f) / 2f

/** Portée d'une traversée d'atelier, de part et d'autre du centre. */
private const val TRAVERSEE_PORTEE = 46f

private const val TRAVERSEE_MILLIS = 7_000

/**
 * L'inclinaison suit l'avance, en degrés — 🔴 **elle n'a pas d'horloge à elle.** Une bascule sur sa
 * propre période battrait contre les deux autres ; ici elle n'est qu'une lecture du déplacement.
 */
private const val TRAVERSEE_INCLINAISON = 3f

/**
 * Le déplacement de la racine, **fonction pure de l'horloge** : à phase égale, même image. C'est ce
 * qui rend le vol vérifiable sans le regarder tourner.
 */
fun Vol.deplacement(phase: Float, avance: Float): Deplacement = when (this) {
    Vol.AUCUN -> Deplacement(Offset.Zero, 0f)

    Vol.LEVITATION -> Deplacement(Offset(0f, levitation(phase)), 0f)

    Vol.SOMMEIL -> Deplacement(Offset(0f, levitationLente(phase)), 0f)

    Vol.TRAVERSEE -> Deplacement(
        decalage = Offset(TRAVERSEE_PORTEE * avance, levitation(phase)),
        inclinaison = -TRAVERSEE_INCLINAISON * avance,
    )
}

/**
 * L'ombre portée — `PRESENCE.md` §1.3.
 *
 * Elliptique, floue, très aplatie, posée bas. Trois choses la définissent, et aucune n'est un
 * signal :
 * - 🔴 **elle est solidaire du personnage en `x`, et de rien d'autre** — sinon elle dériverait au
 *   parallaxe ;
 * - 🔴 **elle ne monte pas avec lui** : c'est l'écart entre ses pieds et elle qui dit la hauteur de
 *   vol, et c'est la seule chose qu'elle dise ;
 * - 🔴 **son opacité est une valeur, pas une animation.** Rien ici ne dépend du temps, donc rien ne
 *   peut pulser — une ombre qui pulse serait un second rythme dans le champ (§4.3).
 */
data class Ombre(
    /** L'empreinte au sol : la moitié de la largeur d'épaules du dessin. */
    val demiLargeur: Float = DEMI_LARGEUR_OMBRE,
    val aplatissement: Float = APLATISSEMENT_OMBRE,
    /**
     * L'altitude du sol dans la vue — **un peu sous les pieds tels qu'ils sont dessinés**
     * *(demande de Xavier, 16/08/2026)* : au ras des pieds, il la touchait, et l'effet de vol s'en
     * trouvait cassé. Le décalage l'en éloigne, même posé.
     */
    val sol: Float = BAS_PIEDS + DECALAGE_SOL_OMBRE,
    /** La part pleine avant que le flou ne commence, en fraction du rayon. */
    val noyau: Float = NOYAU_OMBRE,
)

/**
 * L'empreinte au sol se prend sur le dessin — **la largeur d'épaules** —, elle ne se choisit pas :
 * une ombre plus étroite ou plus large que le corps serait une forme de plus à interpréter.
 */
val DEMI_LARGEUR_OMBRE = (EPAULE_DROITE.x - EPAULE_GAUCHE.x) / 2f

/**
 * ⏳ **À itérer sur l'appareil** (§3) : très aplatie parce qu'un sol vu de face n'est pas un disque,
 * discrète parce qu'elle n'a rien à annoncer.
 */
const val APLATISSEMENT_OMBRE = 0.16f
const val NOYAU_OMBRE = 0.45f

/**
 * ⏳ L'écart entre le sol de l'ombre et le bas des pieds dessinés — à ajuster à l'usage.
 * 🔴 **Borné par la vue** : au-delà de ≈ 4,8, l'ellipse la plus aplatie déborderait de [HAUTEUR_VUE]
 * en bas ; `CorpsInvariantsTest` le vérifie.
 */
const val DECALAGE_SOL_OMBRE = 4f

/**
 * ⭐ **L'opacité varie avec la hauteur de vol** *(demande de Xavier, 16/08/2026)* — plus sombre quand
 * Kokoro s'approche du sol, plus transparente quand il s'en éloigne. Ce n'est pas une horloge de
 * plus : elle ne dépend que de [Deplacement.decalage], donc de la hauteur déjà tirée de l'horloge du
 * vol — rien ne peut y pulser tout seul.
 */
const val OPACITE_OMBRE_PROCHE = 0.22f
const val OPACITE_OMBRE_LOINTAINE = 0.08f

/**
 * L'opacité de l'ombre à une hauteur de vol donnée — **0 au sol, 1 au sommet de la lévitation.**
 * `hauteur` est négative en montant ([levitation]) ; on la ramène à une fraction positive avant de
 * l'utiliser, et [LEVITATION_AMPLITUDE] sert de référence même en vol du sommeil, plus bas.
 */
fun Ombre.opaciteA(hauteur: Float): Float {
    val fraction = (-hauteur / LEVITATION_AMPLITUDE).coerceIn(0f, 1f)
    return OPACITE_OMBRE_PROCHE + (OPACITE_OMBRE_LOINTAINE - OPACITE_OMBRE_PROCHE) * fraction
}

/** 🔴 **Le vol porte son ombre** : ce qui ne vole pas n'en a pas, et il n'y a rien à régler. */
fun Vol.ombre(): Ombre? = if (this == Vol.AUCUN) null else Ombre()

/**
 * Le déplacement animé. Seule la traversée a besoin d'un état à elle — un aller-retour lent — ; la
 * lévitation, elle, n'est qu'une lecture de l'horloge du corps qu'on lui passe.
 */
@Composable
fun deplacementAnime(vol: Vol, phase: Float): Deplacement =
    vol.deplacement(phase, avance = if (vol == Vol.TRAVERSEE) avanceAnimee() else 0f)

@Composable
private fun avanceAnimee(): Float {
    val avance by rememberInfiniteTransition(label = "traversee").animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(TRAVERSEE_MILLIS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "traversee-avance",
    )
    return avance
}
