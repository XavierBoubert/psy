package io.allonsy.kokoro.monde

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import io.allonsy.kokoro.corps.BALAYAGE_LIGNE_MILLIS
import io.allonsy.kokoro.corps.Balayage
import io.allonsy.kokoro.corps.CorpsKokoro
import io.allonsy.kokoro.corps.Cote
import io.allonsy.kokoro.corps.EPAULE_GAUCHE
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.corps.HAUTEUR_LOCUTEUR
import io.allonsy.kokoro.corps.HAUTEUR_VUE
import io.allonsy.kokoro.corps.PALETTE_CLAIRE
import io.allonsy.kokoro.corps.Passe
import io.allonsy.kokoro.corps.Posture
import io.allonsy.kokoro.corps.REGARD_LECTURE
import io.allonsy.kokoro.corps.RigKokoro
import io.allonsy.kokoro.corps.Vol
import io.allonsy.kokoro.corps.cadrePour
import io.allonsy.kokoro.corps.habitantEnScene
import io.allonsy.kokoro.corps.rigAnime
import io.allonsy.kokoro.ui.Zzz
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * L'habitant — **Kokoro dans le décor**, `PRESENCE.md` §1.1, §1.3 et §2.
 *
 * ⭐ **Il n'est plus dans la liste.** Il vivait en tête de l'écran de thérapie, comme une carte de
 * plus ; il est maintenant peint dans **une couche à lui, entre le décor et le contenu** — l'ordre
 * de peinture du §1.3. 🔴 **C'est cet ordre, et rien d'autre, qui garantit qu'il ne passe jamais
 * devant un texte et qu'aucune ombre ne tombe sur un panneau** : un panneau posé par-dessus le
 * recouvre mécaniquement. Aucune découpe, aucun test à écrire pour ça.
 *
 * ⭐ **Sa place est ancrée au contenu, pas à la dalle.** Chaque écran déclare un [Perchoir] avec
 * [perchoir], en coordonnées de la racine : le cadre suit donc **le défilement de la liste** et
 * **la traversée du monde** sans que personne n'ait à les recalculer. Quand la liste défile, il
 * sort du champ avec elle — 🔴 **il ne se replace pas**, parce qu'un personnage qui se recolle en
 * haut de l'écran est un mouvement qu'on n'a pas demandé.
 *
 * 🔴 **Une seule instance à l'écran** (§1.1) : il y a **un** habitant pour les quatre écrans, et il
 * transite de place en place. Deux Kokoro pendant une traversée seraient deux personnages.
 *
 * 🔴 **Aucune de ces poses ne porte d'information** (§2). Ne pas les reconnaître ne fait rien
 * perdre : aucune action n'est attendue, aucun état du dossier n'y est encodé.
 */

/**
 * §1.4 — **60 dp**, et pas moins : le contour vaut 1,1 % de la hauteur, il tomberait à 1,6 px en
 * 48 dp et le trait se délaverait. C'est la plus petite taille où le cerne reste à 2 px pleins.
 *
 * 🔴 **C'est la hauteur du personnage, pas celle de sa vue** *(corrigé à E12)*. Les deux diffèrent
 * de 11 % — le dessin a des marges — et **mesurer la vue rendait le cerne à 1,8 px** : le chiffre
 * même que §1.4 refuse pour écarter les 48 dp. La vue se déduit du personnage ([CADRE_HABITANT]),
 * jamais l'inverse.
 *
 * ⏳ **À revérifier sur l'appareil.**
 */
val HAUTEUR_HABITANT = 60.dp

/** La vue qui le contient — c'est elle qu'on pose, et c'est elle qu'une bande doit pouvoir loger. */
val CADRE_HABITANT = cadrePour(HAUTEUR_HABITANT)

/**
 * Le transit d'un écran à l'autre (§3) — **et son retard sur le décor.**
 *
 * ⭐ **Le retard est ce qui fait qu'il habite le monde au lieu d'être collé dessus** : le décor
 * part au doigt, lui suit un dixième de seconde plus tard. 🔴 **Aucune apparition, aucune
 * disparition** — il vole d'une place à l'autre, et les deux places glissent déjà avec le monde.
 */
private const val TRANSIT_MILLIS = 420
private const val RETARD_TRANSIT_MILLIS = 120

/** La flèche de l'arc du transit — il monte et redescend, sans jamais changer de sens net. */
private val ARC_TRANSIT = 26.dp

/** La parution des Zzz. Un fondu, **jamais un battement** (§4.3). */
private const val FONDU_ZZZ_MILLIS = 600

/**
 * La sortie du champ à l'ouverture d'un panneau (§1.1) — **latérale, et par le bord droit.**
 *
 * 🔴 **Il sort, il ne s'efface pas** : §4.2 n'admet que des entrées et des sorties latérales, et
 * une disparition sur place serait un escamotage. La distance parcourue est la largeur de la dalle,
 * donc il est dehors quel que soit le perchoir d'où il part.
 */
fun ecartDeSortie(largeur: Float, sortie: Float): Float = largeur * sortie

/** L'heure à partir de laquelle il montre le check-in (§2). */
const val HEURE_DU_CHECKIN = 18

/**
 * Les points du contenu auxquels l'habitant s'accroche — **un par place du §2**.
 *
 * 🔴 **C'est le contenu qui réserve la place, la couche ne fait que peindre dedans.** Sans réserve,
 * Kokoro passerait derrière le premier panneau et disparaîtrait — conséquence de l'ordre de
 * peinture, pas d'un défaut de placement.
 */
enum class Perchoir { AUJOURDHUI, SANS_DATE, DOCUMENTATION, BILAN, CRISE }

/** Comment il se pose sur son perchoir. */
enum class Cadrage {
    /** À droite de la bande, à côté de la pancarte qui l'occupe. */
    A_DROITE,

    /** Au milieu de la bande que la liste lui réserve au-dessus d'elle. */
    AU_CENTRE,

    /**
     * ⭐ **Accoudé : la ligne des épaules tombe exactement sur le bord haut du cadre.**
     *
     * Le cadre est ici le **bouton** lui-même, pas une bande vide : les bras, tenus à
     * l'horizontale par la posture, reposent donc sur son arête, et **tout ce qui est sous les
     * épaules passe derrière lui**. 🔴 **Rien de tout ça n'est réglé à l'œil** — la hauteur des
     * épaules est une ancre du dessin, et c'est elle qui décide.
     */
    EPAULES_AU_BORD,
}

/**
 * Ce que l'habitant fait sur un écran — **les trois axes du §1.2, et sa place.**
 *
 * [expression] et [balayage] à `null` valent *ce que la posture dit*, jamais *rien*.
 */
data class Place(
    val perchoir: Perchoir,
    val cadrage: Cadrage,
    val posture: Posture,
    val expression: Expression? = null,
    val balayage: Balayage? = null,
    val vol: Vol = Vol.LEVITATION,
    /** La hauteur du **personnage** à cette place (§1.4) — jamais celle de sa vue. */
    val hauteur: Dp = HAUTEUR_HABITANT,
    /** 🔴 Le corps sous le contenu, les bras dessus. **Une seule place s'en sert** : la crise. */
    val deuxPasses: Boolean = false,
    /** À `false`, une liste vide ne l'endort pas — il n'y a pas de liste à cette place. */
    val sommeilPossible: Boolean = true,
) {
    /**
     * Le sommeil d'une liste vide (§2) — 🔴 **la place ne change pas.** Il s'endort là où il était :
     * un personnage qui se déplace parce qu'une liste est vide dirait quelque chose de la liste.
     */
    fun endormi(): Place = copy(
        posture = Posture.Sommeil,
        expression = null,
        balayage = null,
        vol = Vol.SOMMEIL,
    )
}

/**
 * Ce que l'habitant sait du monde, **et rien de plus.**
 *
 * ⭐ **Trois faits, aucun jugement** : l'heure qu'il est, si le check-in du jour est écrit, quelles
 * listes sont vides. 🔴 **Rien ici ne compte les jours, ne mesure une régularité, ni ne retient ce
 * qui n'a pas été fait.**
 *
 * ⭐ **L'heure est lue à l'arrivée**, comme celle du décor : si 18 h passent pendant que le monde
 * est ouvert, il ne bascule pas sous les yeux de Xavier.
 */
data class Sejour(
    val heure: Int,
    val checkinFait: Boolean,
    val vides: Set<Ecran> = ECRANS_VIDES,
)

/**
 * La place de l'habitant sur un écran, ou `null` quand il n'y est pas — **fonction pure, donc
 * vérifiable sans écran.**
 */
fun place(ecran: Ecran, sejour: Sejour): Place? {
    val place = placeOrdinaire(ecran, sejour) ?: return null
    return if (ecran in sejour.vides && place.sommeilPossible) place.endormi() else place
}

private fun placeOrdinaire(ecran: Ecran, sejour: Sejour): Place? = when (ecran) {
    Ecran.THERAPIE -> when {
        sejour.heure >= HEURE_DU_CHECKIN -> montreLeCheckin(sejour.checkinFait)
        else -> pensifDevantLaListe()
    }

    Ecran.DOCUMENTATION -> Place(
        perchoir = Perchoir.DOCUMENTATION,
        cadrage = Cadrage.AU_CENTRE,
        posture = Posture.Lecture,
        balayage = Balayage(),
    )

    Ecran.BILAN -> Place(
        perchoir = Perchoir.BILAN,
        cadrage = Cadrage.AU_CENTRE,
        posture = Posture.Notes,
    )

    Ecran.CRISE -> veilleSurLaCrise()
}

/**
 * ⭐ **L'écran de crise — *« Kokoro veille sur toi »*** *(arbitrage de Xavier, 16/08/2026)*.
 *
 * 🔴 **C'est une dérogation à `CORPS.md` §10, et elle est entièrement bornée par ce qui suit.** La
 * supervision du 15/08/2026 avait refusé la version précédente — un personnage **sans visage**, que
 * `PRESENCE.md` §2 déclarait lui-même sans information, donc une décoration sur l'écran qui en
 * refuse. **Xavier a tranché en donnant le motif qui manquait, et il change la pose** : ce qu'on
 * veut là n'est pas une présence muette, c'est **un visage bienveillant qui regarde**.
 *
 * **Les quatre bornes :**
 * - 🔴 **Cet écran-ci, et lui seul.** L'écran de crise **ouvert hors du monde** (`CriseActivity`),
 *   celui qui s'impose par-dessus le verrouillage quand ça va déjà mal, **n'en porte aucun** — pas
 *   plus que la tension appliquée ni la phrase pour le soignant. ⭐ **La présence se pose là où on
 *   arrive en traversant, pas là où on tombe.** Un test de sources le verrouille.
 * - 🔴 **Aucun texte n'est ajouté à l'écran.** Les trois boutons ne bougent ni de place, ni de
 *   taille, ni de libellé, ni de couleur : **les deux portes font toujours exactement la même
 *   chose**, et c'est ce que `INTERFACE.md` §6.2 protège.
 * - 🔴 **Il ne vole pas.** C'est la seule place du dispositif dans ce cas : **il est accoudé, pas
 *   posé au sol** — et une ombre portée tomberait sur l'interface.
 * - 🔴 **Il ne s'endort jamais ici.** Il n'y a pas de liste, et veiller est précisément ce qu'il y
 *   fait.
 */
private fun veilleSurLaCrise() = Place(
    perchoir = Perchoir.CRISE,
    cadrage = Cadrage.EPAULES_AU_BORD,
    posture = Posture.Accoude,
    vol = Vol.AUCUN,
    hauteur = HAUTEUR_LOCUTEUR,
    deuxPasses = true,
    sommeilPossible = false,
)

/**
 * Avant 18 h : à côté de *Sans date*, **aucun geste**, le regard qui glisse d'une carte à l'autre.
 *
 * ⭐ **Le balayage est plus lent et plus court que celui de la lecture** : il parcourt une liste et
 * non une ligne, donc il traîne au lieu de courir. C'est le seul mouvement de la posture.
 */
private fun pensifDevantLaListe() = Place(
    perchoir = Perchoir.SANS_DATE,
    cadrage = Cadrage.A_DROITE,
    posture = Posture.Pensif,
    balayage = Balayage(
        amplitude = REGARD_LECTURE / 2f,
        ligneMillis = 2 * BALAYAGE_LIGNE_MILLIS,
    ),
)

/**
 * À partir de 18 h : à côté du check-in, **le bras tendu vers lui** — il n'a pas de doigt.
 *
 * 🔴 **Le geste et la place ne dépendent jamais de l'état du check-in** (§4.4). Quand il n'est pas
 * fait, la posture, la place et le texte sont **inchangés** ; seule l'expression reste celle de
 * tous les jours. **`chaleureux` réagit à un fait accompli et n'a pas de contraire** : il n'existe
 * aucune expression, aucune place et aucun geste qui dise *tu ne l'as pas fait*.
 *
 * ⚠️ **Il se tient dans la bande de la section, pas contre la carte.** Les cartes prennent toute la
 * largeur et sont peintes **par-dessus lui** (§1.3) : se poser à côté de l'une d'elles reviendrait à
 * se poser derrière. La bande du `quand` est le seul endroit libre de la liste — le bras y désigne
 * la section, donc ce qu'elle contient. **⏳ À juger à l'écran** : c'est le genre de chose qu'un
 * dessin tranche mieux qu'un raisonnement.
 */
private fun montreLeCheckin(checkinFait: Boolean) = Place(
    perchoir = Perchoir.AUJOURDHUI,
    cadrage = Cadrage.A_DROITE,
    posture = Posture.Montre(Cote.GAUCHE),
    expression = if (checkinFait) Expression.CHALEUREUX else null,
)

/**
 * Les cadres que les écrans déclarent, **en coordonnées de la racine**.
 *
 * ⭐ **Non clippées, exprès** : [positionInRoot] continue de compter quand la bande sort de la
 * dalle, alors que des bornes clippées s'écraseraient au bord. C'est ce qui laisse l'habitant
 * sortir du champ avec sa liste au lieu de se coincer contre le bord.
 */
@Stable
class Perchoirs {
    private val cadres = mutableStateMapOf<Perchoir, Rect>()

    fun poser(perchoir: Perchoir, cadre: Rect) {
        if (cadres[perchoir] != cadre) cadres[perchoir] = cadre
    }

    fun cadre(perchoir: Perchoir): Rect? = cadres[perchoir]
}

@Composable
fun rememberPerchoirs(): Perchoirs = remember { Perchoirs() }

/** La bande qu'un écran réserve à l'habitant, déclarée là où elle est posée dans la liste. */
fun Modifier.perchoir(perchoirs: Perchoirs, perchoir: Perchoir): Modifier =
    onGloballyPositioned { perchoirs.poser(perchoir, Rect(it.positionInRoot(), it.size.toSize())) }

/**
 * La couche de l'habitant — **posée entre le décor et le contenu**, et peinte avant lui.
 *
 * [ecran] est l'écran **posé**, pas celui qu'on est en train de traverser : c'est son changement
 * qui déclenche le transit, et le retard sur le décor vient de là.
 *
 * [sortie] est la bascule des deux régimes (`corps/Locuteur.kt`) : à 1 il est hors champ et **il
 * n'est plus composé du tout** — c'est la forme la plus littérale de « une seule instance à
 * l'écran », puisqu'à cet instant précis le locuteur, lui, entre.
 */
@Composable
fun Habitant(
    perchoirs: Perchoirs,
    ecran: Ecran,
    sejour: Sejour,
    sortie: State<Float>,
    largeur: Int,
    bras: MutableState<PasseDesBras?>,
    modifier: Modifier = Modifier,
) {
    val avancementSortie = sortie.value
    if (!habitantEnScene(avancementSortie)) {
        EffacerLesBras(bras)
        return
    }

    val transit = transitAnime(ecran)
    val arrivee = place(transit.vers, sejour)
    val depart = place(transit.depuis, sejour)

    /** Ce qu'il porte : la pose d'arrivée, ou celle qu'il emporte hors d'un écran sans place. */
    val tenue = arrivee ?: depart ?: return

    val cadre = cadrePour(tenue.hauteur)
    val taille = with(LocalDensity.current) { Size(cadre.width.toPx(), cadre.height.toPx()) }
    val fleche = with(LocalDensity.current) { ARC_TRANSIT.toPx() }

    val depuis = depart?.let { pointDeLaPlace(perchoirs.cadre(it.perchoir), it.cadrage, taille) }
    val vers = arrivee?.let { pointDeLaPlace(perchoirs.cadre(it.perchoir), it.cadrage, taille) }
    val glisse = Offset(ecartDeSortie(largeur.toFloat(), avancementSortie), 0f)
    val point = when {
        depuis == null || vers == null -> (vers ?: depuis ?: return) + glisse
        else -> lerp(depuis, vers, transit.avancement) -
            Offset(0f, arc(fleche, transit.avancement)) + glisse
    }

    val rig = rigAnime(
        posture = tenue.posture,
        vol = tenue.vol,
        expression = tenue.expression,
        balayage = tenue.balayage,
    )

    /**
     * 🔴 **Le même rig, le même point, le même cadre — publiés, jamais recalculés.** La seconde
     * passe ne refait aucun calcul : deux rigs animés séparément dériveraient l'un de l'autre au
     * premier clignement, et deux transits parallèles feraient voler les bras à côté du corps.
     */
    SideEffect {
        bras.value = if (tenue.deuxPasses) PasseDesBras(rig, point, cadre) else null
    }
    EffacerLesBras(bras)

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset { IntOffset(point.x.roundToInt(), point.y.roundToInt()) }
                .size(cadre),
        ) {
            // 🔴 Il garde les couleurs du SVG, jour et nuit : il n'est pas posé sur le fond de
            // l'application, il est posé dans le décor. Le repeindre avec le ciel lui donnerait une
            // seconde apparence à décoder — le décor change d'heure, lui non.
            CorpsKokoro(
                rig = rig,
                modifier = Modifier.fillMaxSize(),
                palette = PALETTE_CLAIRE,
                passe = if (tenue.deuxPasses) Passe.CORPS else Passe.ENTIER,
            )

            AnimatedVisibility(
                visible = tenue.posture == Posture.Sommeil,
                enter = fadeIn(tween(FONDU_ZZZ_MILLIS)),
                exit = fadeOut(tween(FONDU_ZZZ_MILLIS)),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-8).dp),
            ) {
                Zzz()
            }
        }
    }
}

/**
 * Ce que la seconde passe a besoin de savoir — **rien qu'elle calcule elle-même.**
 *
 * ⭐ **Ce n'est pas un second personnage** (`CORPS.md` §8 point 8) : c'est le même, peint en deux
 * fois pour qu'un bouton puisse passer entre son corps et ses bras.
 */
data class PasseDesBras(val rig: RigKokoro, val point: Offset, val cadre: DpSize)

@Composable
fun rememberPasseDesBras(): MutableState<PasseDesBras?> = remember { mutableStateOf(null) }

/** Quand l'habitant cesse d'être peint, ses bras s'en vont avec lui — jamais une seconde plus tard. */
@Composable
private fun EffacerLesBras(bras: MutableState<PasseDesBras?>) {
    DisposableEffect(Unit) { onDispose { bras.value = null } }
}

/**
 * La seconde passe — **les bras seuls, peints par-dessus le contenu.**
 *
 * 🔴 **C'est la seule chose du personnage qui passe devant l'interface, et elle est bornée à une
 * place** : l'écran de crise, où les bras reposent sur l'arête du bouton *Mot code*. Partout
 * ailleurs cet état vaut `null` et cette couche ne dessine rien — **l'ordre de peinture n'est donc
 * jamais relâché ailleurs** (§1.3).
 */
@Composable
fun BrasDeLHabitant(bras: State<PasseDesBras?>, modifier: Modifier = Modifier) {
    val passe = bras.value ?: return
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset { IntOffset(passe.point.x.roundToInt(), passe.point.y.roundToInt()) }
                .size(passe.cadre),
        ) {
            CorpsKokoro(
                rig = passe.rig,
                modifier = Modifier.fillMaxSize(),
                palette = PALETTE_CLAIRE,
                passe = Passe.BRAS,
            )
        }
    }
}

/**
 * Où poser l'habitant dans le cadre de son perchoir — **pure, donc vérifiable sans écran.**
 *
 * ⭐ **Le cadre est la bande entière, pas la pancarte** : c'est ce qui permet de le poser au bord
 * droit du contenu sans jamais rien connaître de la largeur de la dalle, et donc sans le décoller
 * de son écran quand celui-ci part en traversée.
 */
fun pointDeLaPlace(cadre: Rect?, cadrage: Cadrage, taille: Size): Offset? {
    if (cadre == null) return null
    return Offset(
        x = when (cadrage) {
            Cadrage.A_DROITE -> cadre.right - taille.width
            Cadrage.AU_CENTRE, Cadrage.EPAULES_AU_BORD -> cadre.center.x - taille.width / 2f
        },
        y = when (cadrage) {
            Cadrage.A_DROITE, Cadrage.AU_CENTRE -> cadre.center.y - taille.height / 2f
            Cadrage.EPAULES_AU_BORD -> cadre.top - taille.height * HAUTEUR_EPAULES
        },
    )
}

/**
 * Où tombe la ligne des épaules dans la vue, en fraction de sa hauteur — **lue dans le dessin.**
 *
 * C'est ce qui permet de poser les épaules **exactement** sur l'arête d'un bouton sans connaître ni
 * l'échelle du personnage, ni la taille de l'écran.
 */
val HAUTEUR_EPAULES = EPAULE_GAUCHE.y / HAUTEUR_VUE

/**
 * L'arc du transit — **il monte et il redescend**, et il est nul aux deux bouts. 🔴 Aucun
 * changement de direction net : la dérivée d'un demi-sinus s'annule au sommet.
 */
fun arc(fleche: Float, avancement: Float): Float = fleche * sin(PI.toFloat() * avancement)

/**
 * La sortie du champ, **et le retour qui attend que le panneau soit parti** (§1.1).
 *
 * ⭐ **Les deux sens ne sont pas symétriques, et c'est voulu.** À l'ouverture il part tout de
 * suite : le panneau monte en 320 ms et il doit être dehors avant que le locuteur n'entre. À la
 * fermeture il attend que le panneau ait fini de redescendre — **sans quoi il rentrerait dans le
 * champ pendant que le locuteur en sort encore, et il y aurait deux Kokoro à l'écran.**
 *
 * 🔴 **Le locuteur, lui, n'a rien à attendre en partant** : il est posé sur le panneau, donc le
 * panneau l'emporte.
 */
@Composable
fun sortieAnimee(dehors: Boolean): State<Float> {
    val sortie = remember { Animatable(0f) }
    LaunchedEffect(dehors) {
        sortie.animateTo(
            targetValue = if (dehors) 1f else 0f,
            animationSpec = tween(
                durationMillis = TRANSIT_MILLIS,
                delayMillis = if (dehors) 0 else MONTEE_ETAPE_MS,
                easing = FastOutSlowInEasing,
            ),
        )
    }
    return sortie.asState()
}

/** D'où il vient, où il va, et où il en est. */
data class Transit(val depuis: Ecran, val vers: Ecran, val avancement: Float)

/**
 * Le transit, **en retard sur le décor.** Le monde part au lever du doigt ; l'habitant attend
 * [RETARD_TRANSIT_MILLIS] avant de suivre, puis rejoint sa nouvelle place.
 *
 * ⭐ **Une traversée reprise en cours de vol repart d'où l'on va, pas d'où l'on venait** — même
 * arbitrage que le morphing du visage : au-delà de la mi-course, le point de départ oublié est
 * celui qu'on a déjà quitté.
 */
@Composable
private fun transitAnime(ecran: Ecran): Transit {
    var transit by remember { mutableStateOf(Transit(ecran, ecran, 1f)) }
    val avancement = remember { Animatable(1f) }

    LaunchedEffect(ecran) {
        if (transit.vers == ecran) return@LaunchedEffect
        val depuis = if (avancement.value < 0.5f) transit.depuis else transit.vers
        transit = Transit(depuis, ecran, 0f)
        avancement.snapTo(0f)
        avancement.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = TRANSIT_MILLIS,
                delayMillis = RETARD_TRANSIT_MILLIS,
                easing = FastOutSlowInEasing,
            ),
        )
    }

    return transit.copy(avancement = avancement.value)
}
