package io.allonsy.kokoro.corps

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * Cycle complet inspiration + expiration (CORPS.md §5). Il ne change jamais.
 *
 * 🔴 **C'est aussi la période de la lévitation** (`PRESENCE.md` §3) : le corps n'a qu'une horloge,
 * et [Vol.kt][levitation] s'y branche par un déphasage. Une seconde période battrait contre
 * celle-ci, et le battement se lirait comme une information.
 */
const val RESPIRATION_MILLIS = 4_500

/** Toute transition d'expression ou de posture (invariant §5.6). */
const val TRANSITION_MILLIS = 800

/** Durée pendant laquelle les yeux restent fermés. */
const val CLIGNEMENT_MILLIS = 200

private const val CLIGNEMENT_MORPHING_MILLIS = 80

/**
 * La cadence du clignement — `PRESENCE.md` §3. **La borne basse évite le papillonnement, la borne
 * haute évite qu'un clignement devienne un événement.** Le tirage est aléatoire entre les deux :
 * 🔴 un clignement régulier deviendrait un métronome, donc une information à décoder.
 */
const val CLIGNEMENT_ATTENTE_MIN_MILLIS = 2_800L
const val CLIGNEMENT_ATTENTE_MAX_MILLIS = 6_500L

/** Amplitude du balayage de lecture : le regard va d'un bord du panneau à l'autre. */
const val REGARD_LECTURE = 4f

/** Une ligne se parcourt lentement, et le retour à la ligne est bref (`PRESENCE.md` §3). */
const val BALAYAGE_LIGNE_MILLIS = 3_000
const val BALAYAGE_RETOUR_MILLIS = 200

/** Un temps d'arrêt à chaque bout : 🔴 aucun mouvement continu dans le champ (§4.3). */
const val BALAYAGE_PAUSE_MILLIS = 600

/** Le geste d'écriture — `PRESENCE.md` §3 : une série d'allers-retours courts, puis plus rien. */
const val ECRITURE_ALLER_RETOUR_MILLIS = 500
const val ECRITURE_GESTE_MILLIS = 4_000
const val ECRITURE_ALLERS_RETOURS = ECRITURE_GESTE_MILLIS / ECRITURE_ALLER_RETOUR_MILLIS

/**
 * 🔴 **Le bras s'arrête bien plus longtemps qu'il ne bouge**, et jamais deux fois pour la même
 * durée. Un geste continu dans le champ serait quelque chose à décoder (§4.3).
 */
const val ECRITURE_ARRET_MIN_MILLIS = 10_000L
const val ECRITURE_ARRET_MAX_MILLIS = 20_000L

/** L'amplitude d'un aller-retour, en degrés d'ouverture — de petits mouvements, rien de plus. */
const val ECRITURE_AMPLITUDE = 6f

@Composable
fun rigAnime(posture: Posture, vol: Vol = Vol.AUCUN): RigKokoro = with(posture.reglage()) {
    rigAnime(
        expression = expression,
        panneauAllume = panneauAllume,
        ouvertureBrasGauche = ouvertureBrasGauche,
        ouvertureBrasDroit = ouvertureBrasDroit,
        regard = regard,
        abaissement = abaissement,
        ecriture = ecriture,
        echelle = echelle,
        vol = vol,
    )
}

@Composable
fun rigAnime(
    expression: Expression,
    panneauAllume: Boolean = true,
    ouvertureBrasGauche: Float = OUVERTURE_REPOS,
    ouvertureBrasDroit: Float = OUVERTURE_REPOS,
    orbitePiedGauche: Float = 0f,
    orbitePiedDroit: Float = 0f,
    regard: Float = 0f,
    abaissement: Float = 0f,
    balayage: Balayage? = null,
    ecriture: Cote? = null,
    echelle: Float = 1f,
    vol: Vol = Vol.AUCUN,
): RigKokoro {
    val yeuxFermes = clignementAnime(expression, actif = panneauAllume)
    val visage = visageAnime(expression, yeuxFermes)
    val battement = battementAnime()
    val mouvement = deplacementAnime(vol, battement)
    val geste = ecritureAnimee(actif = ecriture != null)
    val brasGauche by animateFloatAsState(ouvertureBrasGauche, transition(), label = "bras-gauche")
    val brasDroit by animateFloatAsState(ouvertureBrasDroit, transition(), label = "bras-droit")
    val piedGauche by animateFloatAsState(orbitePiedGauche, transition(), label = "pied-gauche")
    val piedDroit by animateFloatAsState(orbitePiedDroit, transition(), label = "pied-droit")
    val oeillade by animateFloatAsState(regard, transition(), label = "regard")
    val yeuxBaisses by animateFloatAsState(abaissement, transition(), label = "abaissement")
    val parcours = balayageAnime(balayage)
    val taille by animateFloatAsState(echelle, transition(), label = "echelle")

    return RigKokoro(
        visage = visage,
        panneauAllume = panneauAllume,
        respiration = souffle(battement),
        ouvertureBrasGauche = brasGauche + geste.sur(ecriture, Cote.GAUCHE),
        ouvertureBrasDroit = brasDroit + geste.sur(ecriture, Cote.DROITE),
        orbitePiedGauche = piedGauche,
        orbitePiedDroit = piedDroit,
        regard = oeillade + parcours,
        abaissement = yeuxBaisses,
        decalage = mouvement.decalage,
        inclinaison = mouvement.inclinaison,
        echelle = taille,
        ombre = vol.ombre(),
    )
}

private fun transition() = tween<Float>(TRANSITION_MILLIS, easing = FastOutSlowInEasing)

/**
 * Le souffle à un instant de l'horloge : 0 = expiration, 1 = inspiration. Sinusoïde continue, sans
 * temps d'arrêt — 🔴 **et sans changement de rythme, jamais** (`PRESENCE.md` §4.6).
 */
fun souffle(phase: Float): Float = (sin(phase) + 1f) / 2f

/**
 * Un intervalle tiré entre deux bornes, 🔴 **jamais celui qui vient de s'écouler.** Deux attentes
 * égales de suite, c'est le début d'un rythme, donc quelque chose à décoder (§5).
 */
fun attenteIrreguliere(
    precedente: Long,
    minimum: Long,
    maximum: Long,
    alea: Random = Random,
): Long = generateSequence { alea.nextLong(minimum, maximum) }.first { it != precedente }

/** L'attente avant le prochain clignement. */
fun attenteClignement(precedente: Long, alea: Random = Random): Long =
    attenteIrreguliere(precedente, CLIGNEMENT_ATTENTE_MIN_MILLIS, CLIGNEMENT_ATTENTE_MAX_MILLIS, alea)

/** L'arrêt entre deux séries d'écriture. */
fun attenteEcriture(precedente: Long, alea: Random = Random): Long =
    attenteIrreguliere(precedente, ECRITURE_ARRET_MIN_MILLIS, ECRITURE_ARRET_MAX_MILLIS, alea)

/** Rythme irrégulier mais borné — un clignement régulier deviendrait un métronome (§5). */
@Composable
private fun clignementAnime(expression: Expression, actif: Boolean): Boolean {
    var ferme by remember { mutableStateOf(false) }
    LaunchedEffect(expression, actif) {
        ferme = false
        if (!actif || !expression.yeuxOuverts) return@LaunchedEffect
        var attente = 0L
        while (true) {
            attente = attenteClignement(attente)
            delay(attente)
            ferme = true
            delay(CLIGNEMENT_MILLIS.toLong())
            ferme = false
        }
    }
    return ferme
}

/**
 * 🔴 **Le clignement n'agit que sur les yeux** : la bouche garde la forme de l'expression courante,
 * et les deux axes se déforment sur leurs propres horloges.
 */
@Composable
private fun visageAnime(expression: Expression, yeuxFermes: Boolean): Visage = Visage(
    oeil = morphingAnime(if (yeuxFermes) OEIL_TRAIT else expression.oeil, ::dureeOeil),
    bouche = morphingAnime(expression.bouche),
)

/** Les formes se déforment l'une vers l'autre — le morphing est dans [MorphingVisage.kt][Contour]. */
@Composable
private fun morphingAnime(
    cible: Trace,
    duree: (Trace, Trace) -> Int = { _, _ -> TRANSITION_MILLIS },
): Morphing {
    var morphing by remember { mutableStateOf(Morphing.de(cible)) }
    val progression = remember { Animatable(1f) }
    LaunchedEffect(cible) {
        if (morphing.vers == cible) return@LaunchedEffect
        val depuis = if (progression.value < 0.5f) morphing.depuis else morphing.vers
        morphing = Morphing(depuis, cible, 0f)
        progression.snapTo(0f)
        progression.animateTo(
            targetValue = 1f,
            animationSpec = tween(duree(depuis, cible), easing = FastOutSlowInEasing),
        )
    }
    return morphing.copy(progression = progression.value)
}

/** Fermer et rouvrir les yeux est bref ; changer d'expression ne l'est jamais (§5). */
private fun dureeOeil(depuis: Trace, vers: Trace): Int = when {
    depuis == OEIL_TRAIT || vers == OEIL_TRAIT -> CLIGNEMENT_MORPHING_MILLIS
    else -> TRANSITION_MILLIS
}

/**
 * Le balayage de lecture — `PRESENCE.md` §3. Un parcours lent d'un bord à l'autre, un retour bref,
 * un temps d'arrêt à chaque bout.
 *
 * ⭐ **Il ne porte aucune information** : c'est un mouvement d'yeux, pas un indicateur. Ne pas le
 * remarquer ne fait rien perdre.
 */
data class Balayage(
    val amplitude: Float = REGARD_LECTURE,
    val ligneMillis: Int = BALAYAGE_LIGNE_MILLIS,
    val retourMillis: Int = BALAYAGE_RETOUR_MILLIS,
    val pauseMillis: Int = BALAYAGE_PAUSE_MILLIS,
)

/**
 * Le bras qui écrit — 🔴 **il s'arrête complètement entre deux séries.** L'arrêt est trois fois plus
 * long que le geste au minimum, et le bras revient exactement à sa position de posture : il n'y a
 * pas de position « en train d'écrire » qui traînerait à l'arrêt.
 */
@Composable
private fun ecritureAnimee(actif: Boolean): Float {
    val ecart = remember { Animatable(0f) }
    LaunchedEffect(actif) {
        if (!actif) {
            ecart.animateTo(0f, transition())
            return@LaunchedEffect
        }
        var arret = 0L
        while (true) {
            arret = attenteEcriture(arret)
            delay(arret)
            repeat(ECRITURE_ALLERS_RETOURS) {
                ecart.animateTo(ECRITURE_AMPLITUDE, allerRetour())
                ecart.animateTo(0f, allerRetour())
            }
        }
    }
    return ecart.value
}

private fun allerRetour() =
    tween<Float>(ECRITURE_ALLER_RETOUR_MILLIS / 2, easing = FastOutSlowInEasing)

/** Le geste ne s'ajoute qu'au bras qui écrit ; l'autre reste exactement sur sa posture. */
private fun Float.sur(ecriture: Cote?, bras: Cote): Float = if (ecriture == bras) this else 0f

/**
 * Le décalage que le balayage ajoute au regard de la posture — **zéro quand il n'y en a pas**, et il
 * y revient en douceur : le regard ne se recentre pas d'un coup quand la lecture s'arrête.
 */
@Composable
private fun balayageAnime(balayage: Balayage?): Float {
    val decalage = remember { Animatable(0f) }
    LaunchedEffect(balayage) {
        if (balayage == null) {
            decalage.animateTo(0f, transition())
            return@LaunchedEffect
        }
        decalage.animateTo(-balayage.amplitude, transition())
        while (true) {
            decalage.animateTo(
                targetValue = balayage.amplitude,
                animationSpec = tween(balayage.ligneMillis, easing = LinearEasing),
            )
            delay(balayage.pauseMillis.toLong())
            decalage.animateTo(
                targetValue = -balayage.amplitude,
                animationSpec = tween(balayage.retourMillis, easing = FastOutSlowInEasing),
            )
            delay(balayage.pauseMillis.toLong())
        }
    }
    return decalage.value
}

/**
 * L'horloge du corps — 🔴 **une seule pour tout ce qui bat.**
 *
 * La respiration et la lévitation en sortent l'une et l'autre (`PRESENCE.md` §3) : deux horloges
 * produiraient un battement lent entre elles, donc une information involontaire. La phase avance
 * linéairement et se referme sur elle-même, donc le retour à zéro ne fait pas de saut.
 */
@Composable
private fun battementAnime(): Float {
    val phase by rememberInfiniteTransition(label = "battement").animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(RESPIRATION_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "battement-phase",
    )
    return phase
}
