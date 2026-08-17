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

// Horloge propre, volontairement dissociée de celle du vol (16/08/2026).
const val RESPIRATION_MILLIS = 3_800

// Deux respirations par tour, pour que la lévitation du sommeil (÷2) s'y referme sans saut.
const val HORLOGE_MILLIS = 2 * RESPIRATION_MILLIS

const val TOUR_HORLOGE = 4f * PI.toFloat()

const val TRANSITION_MILLIS = 800

const val CLIGNEMENT_MILLIS = 200

private const val CLIGNEMENT_MORPHING_MILLIS = 80

// Tirage aléatoire borné (PRESENCE.md §3) : un clignement régulier serait un métronome.
const val CLIGNEMENT_ATTENTE_MIN_MILLIS = 2_800L
const val CLIGNEMENT_ATTENTE_MAX_MILLIS = 6_500L

const val REGARD_LECTURE = 4f

const val BALAYAGE_LIGNE_MILLIS = 3_000
const val BALAYAGE_RETOUR_MILLIS = 200

// Pause à chaque bout : aucun mouvement continu dans le champ (§4.3).
const val BALAYAGE_PAUSE_MILLIS = 600

const val ECRITURE_ALLER_RETOUR_MILLIS = 500
const val ECRITURE_GESTE_MILLIS = 4_000
const val ECRITURE_ALLERS_RETOURS = ECRITURE_GESTE_MILLIS / ECRITURE_ALLER_RETOUR_MILLIS

// Arrêt bien plus long que le geste, jamais répété identique — sinon rythme à décoder (§4.3).
const val ECRITURE_ARRET_MIN_MILLIS = 10_000L
const val ECRITURE_ARRET_MAX_MILLIS = 20_000L

const val ECRITURE_AMPLITUDE = 6f

// null veut dire « celui de la posture », jamais « aucun » (PRESENCE.md §1.2).
@Composable
fun rigAnime(
    posture: Posture,
    vol: Vol = Vol.AUCUN,
    expression: Expression? = null,
    balayage: Balayage? = null,
    respire: Boolean = true,
    partDuVol: Float = 0f,
): RigKokoro = with(posture.reglage()) {
    rigAnime(
        expression = expression ?: this.expression,
        respire = respire,
        partDuVol = partDuVol,
        panneauAllume = panneauAllume,
        ouvertureBrasGauche = ouvertureBrasGauche,
        ouvertureBrasDroit = ouvertureBrasDroit,
        regard = regard,
        abaissement = abaissement,
        balayage = balayage,
        ecriture = ecriture,
        inclinaisonTete = inclinaisonTete,
        echelle = echelle,
        sommeil = sommeil,
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
    inclinaisonTete: Float = 0f,
    echelle: Float = 1f,
    sommeil: Boolean = false,
    vol: Vol = Vol.AUCUN,
    // L'amplitude tombe à zéro, jamais l'horloge : la couper figerait le ventre au hasard, avec un saut visible.
    respire: Boolean = true,
    // Remplace la posture, ne s'ajoute pas — un ajout laissait le vol invisible derrière un bras déjà tendu.
    partDuVol: Float = 0f,
): RigKokoro {
    val posture = 1f - partDuVol.coerceIn(0f, 1f)
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
    val teteInclinee by animateFloatAsState(inclinaisonTete, transition(), label = "inclinaison-tete")
    val parcours = balayageAnime(balayage)
    val taille by animateFloatAsState(echelle, transition(), label = "echelle")
    val poseSommeil by animateFloatAsState(if (sommeil) 1f else 0f, transition(), label = "pose-sommeil")
    val ampleur by animateFloatAsState(if (respire) 1f else 0f, transition(), label = "ampleur-souffle")

    return RigKokoro(
        visage = visage,
        panneauAllume = panneauAllume,
        respiration = souffle(respirationAnime()) * ampleur,
        ouvertureBrasGauche = brasGauche * posture + geste.sur(ecriture, Cote.GAUCHE),
        ouvertureBrasDroit = brasDroit * posture + geste.sur(ecriture, Cote.DROITE),
        orbitePiedGauche = piedGauche * posture,
        orbitePiedDroit = piedDroit * posture,
        poseBrasGauche = POSE_SOMMEIL_BRAS_GAUCHE.echelle(poseSommeil * posture).transformation,
        poseBrasDroit = POSE_SOMMEIL_BRAS_DROIT.echelle(poseSommeil * posture).transformation,
        posePiedGauche = POSE_SOMMEIL_PIED_GAUCHE.echelle(poseSommeil * posture).transformation,
        posePiedDroit = POSE_SOMMEIL_PIED_DROIT.echelle(poseSommeil * posture).transformation,
        regard = (oeillade + parcours) * posture,
        abaissement = yeuxBaisses * posture,
        inclinaisonTete = teteInclinee,
        decalage = mouvement.decalage,
        inclinaison = mouvement.inclinaison,
        echelle = taille,
        ombre = vol.ombre(),
    )
}

private fun transition() = tween<Float>(TRANSITION_MILLIS, easing = FastOutSlowInEasing)

// 0 = expiration, 1 = inspiration.
fun souffle(phase: Float): Float = (sin(phase) + 1f) / 2f

fun attenteIrreguliere(
    precedente: Long,
    minimum: Long,
    maximum: Long,
    alea: Random = Random,
): Long = generateSequence { alea.nextLong(minimum, maximum) }.first { it != precedente }

fun attenteClignement(precedente: Long, alea: Random = Random): Long =
    attenteIrreguliere(precedente, CLIGNEMENT_ATTENTE_MIN_MILLIS, CLIGNEMENT_ATTENTE_MAX_MILLIS, alea)

fun attenteEcriture(precedente: Long, alea: Random = Random): Long =
    attenteIrreguliere(precedente, ECRITURE_ARRET_MIN_MILLIS, ECRITURE_ARRET_MAX_MILLIS, alea)

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

@Composable
private fun visageAnime(expression: Expression, yeuxFermes: Boolean): Visage = Visage(
    oeil = morphingAnime(if (yeuxFermes) OEIL_TRAIT else expression.oeil, ::dureeOeil),
    bouche = morphingAnime(expression.bouche),
)

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

private fun dureeOeil(depuis: Trace, vers: Trace): Int = when {
    depuis == OEIL_TRAIT || vers == OEIL_TRAIT -> CLIGNEMENT_MORPHING_MILLIS
    else -> TRANSITION_MILLIS
}

data class Balayage(
    val amplitude: Float = REGARD_LECTURE,
    val ligneMillis: Int = BALAYAGE_LIGNE_MILLIS,
    val retourMillis: Int = BALAYAGE_RETOUR_MILLIS,
    val pauseMillis: Int = BALAYAGE_PAUSE_MILLIS,
)

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

private fun Float.sur(ecriture: Cote?, bras: Cote): Float = if (ecriture == bras) this else 0f

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

@Composable
private fun battementAnime(): Float {
    val phase by rememberInfiniteTransition(label = "battement").animateFloat(
        initialValue = 0f,
        targetValue = TOUR_HORLOGE,
        animationSpec = infiniteRepeatable(
            animation = tween(HORLOGE_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "battement-phase",
    )
    return phase
}

@Composable
private fun respirationAnime(): Float {
    val phase by rememberInfiniteTransition(label = "respiration").animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(RESPIRATION_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "respiration-phase",
    )
    return phase
}
