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
 * 🔄 **Sa propre horloge, détachée de la lévitation** *(demande de Xavier, 16/08/2026 : « non calée
 * sur la vitesse du haut/bas pour faire plus réelle »)*. Jusqu'ici le corps n'avait qu'une horloge,
 * partagée avec [Vol.kt][levitation] par un déphasage — c'était voulu, pour qu'aucun battement lent
 * ne naisse entre les deux. **C'est cette dérive-là que Xavier demande** : vues côte à côte, la
 * respiration et le vol stationnaire ne culminent plus ensemble, ce qui les fait lire comme deux
 * mouvements distincts plutôt qu'un seul mécanisme. La période ne change pas ; c'est la source qui
 * se dédouble ([respirationAnime]).
 *
 * 🔴 Le rythme reste fixe : ce que §4.6 interdit est qu'il **varie**, pas qu'il soit réglé une fois.
 */
const val RESPIRATION_MILLIS = 3_800

/**
 * Le tour complet de l'horloge du corps — **deux respirations, et toujours une seule horloge.**
 *
 * ⭐ **C'est le sommeil qui l'impose** (§3 : lévitation à ½ vitesse). Un vol deux fois plus lent tiré
 * d'une horloge qui ne fait qu'un tour de respiration sauterait à chaque bouclage — il serait à
 * mi-course quand l'horloge repasse à zéro. En faisant durer le tour **deux** respirations, la
 * lévitation lente s'y referme exactement, et 🔴 **la respiration, elle, ne change pas de rythme**
 * (§4.6) : elle en fait simplement deux par tour.
 */
const val HORLOGE_MILLIS = 2 * RESPIRATION_MILLIS

/** Un tour d'horloge en radians — deux tours de souffle. */
const val TOUR_HORLOGE = 4f * PI.toFloat()

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

/**
 * La posture animée — 🔴 **les trois axes de `PRESENCE.md` §1.2 restent indépendants.** La posture
 * porte le corps ; l'expression et le balayage du regard se règlent par-dessus, et `null` veut dire
 * *celui de la posture*, jamais *aucun*.
 */
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
    /**
     * Le souffle s'arrête sur l'écran de crise, et là seulement.
     * 🔴 C'est l'amplitude qui tombe, jamais l'horloge : couper la source figerait le ventre là où il
     * en était — un saut, et une taille de corps différente à chaque venue.
     */
    respire: Boolean = true,
    /**
     * La part que le dessin de vol prend sur la posture — 0 la posture seule, 1 le dessin seul.
     *
     * 🔴 Le vol **remplace**, il ne s'ajoute pas. Un bras déjà tendu par `montre` auquel on ajoute la
     * rotation du dessin reste un bras tendu : c'est ce qui faisait que le vol ne se voyait pas. Ce
     * qui s'efface ici, c'est l'ouverture des bras, la pose de sommeil et le regard de la posture ;
     * ce qui prend la place est appliqué par-dessus, dans [io.allonsy.kokoro.monde.Habitant].
     */
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
 * L'horloge du vol — 🔄 **celle de la lévitation et du sommeil, plus seule celle de la respiration**
 * *(16/08/2026 : voir [RESPIRATION_MILLIS])*. La phase avance linéairement et se referme sur
 * elle-même, donc le retour à zéro ne fait pas de saut.
 *
 * ⭐ **Un tour vaut deux respirations** ([HORLOGE_MILLIS]) : c'est ce qui laisse le sommeil ralentir
 * son vol de moitié **sans horloge à lui**, en se contentant de diviser la phase par deux. Le nom
 * garde le tour de respiration comme unité de mesure ; ce n'est plus la respiration elle-même.
 */
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

/**
 * L'horloge de la respiration, **détachée de celle du vol** *(demande de Xavier, 16/08/2026)* — même
 * période ([RESPIRATION_MILLIS]), une source indépendante. Les deux horloges démarrent ensemble à la
 * composition, donc elles dérivent l'une de l'autre plutôt que de sauter : c'est cette dérive lente
 * qui fait qu'un souffle et un vol stationnaire ne se ressemblent plus jamais tout à fait.
 */
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
