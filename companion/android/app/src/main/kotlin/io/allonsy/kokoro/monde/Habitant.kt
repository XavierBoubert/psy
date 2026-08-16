package io.allonsy.kokoro.monde

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
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
import androidx.compose.ui.draw.clipToBounds
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
import io.allonsy.kokoro.corps.Balayage
import io.allonsy.kokoro.corps.CorpsKokoro
import io.allonsy.kokoro.corps.Cote
import io.allonsy.kokoro.corps.DESCENTE_DU_BRAS_HORIZONTAL
import io.allonsy.kokoro.corps.EPAULE_GAUCHE
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.corps.HAUTEUR_LOCUTEUR
import io.allonsy.kokoro.corps.HAUTEUR_VUE
import io.allonsy.kokoro.corps.OUVERTURE_BRAS_LEVES
import io.allonsy.kokoro.corps.OUVERTURE_HORIZONTALE
import io.allonsy.kokoro.corps.PALETTE_CLAIRE
import io.allonsy.kokoro.corps.POSE_VOL_DROITE_BRAS_DROIT
import io.allonsy.kokoro.corps.POSE_VOL_DROITE_BRAS_GAUCHE
import io.allonsy.kokoro.corps.POSE_VOL_DROITE_PIED_DROIT
import io.allonsy.kokoro.corps.POSE_VOL_DROITE_PIED_GAUCHE
import io.allonsy.kokoro.corps.POSE_VOL_DROITE_TRONC
import io.allonsy.kokoro.corps.POSE_VOL_GAUCHE_BRAS_DROIT
import io.allonsy.kokoro.corps.POSE_VOL_GAUCHE_BRAS_GAUCHE
import io.allonsy.kokoro.corps.POSE_VOL_GAUCHE_PIED_DROIT
import io.allonsy.kokoro.corps.POSE_VOL_GAUCHE_PIED_GAUCHE
import io.allonsy.kokoro.corps.POSE_VOL_GAUCHE_TRONC
import io.allonsy.kokoro.corps.Passe
import io.allonsy.kokoro.corps.PoseMembre
import io.allonsy.kokoro.corps.PoseTronc
import io.allonsy.kokoro.corps.Posture
import io.allonsy.kokoro.corps.RigKokoro
import io.allonsy.kokoro.corps.SOMMET_TETE
import io.allonsy.kokoro.corps.Vol
import io.allonsy.kokoro.corps.cadrePour
import io.allonsy.kokoro.corps.habitantEnScene
import io.allonsy.kokoro.corps.rigAnime
import io.allonsy.kokoro.ui.Zzz
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sign
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
 * §1.4 — **110 dp**, la même partout, **écran de crise compris** *(demande de Xavier, 16/08/2026)*.
 * Kokoro se tenait à 60 dp dans le décor et à 110 dp accoudé à la crise ; **un seul et même
 * personnage n'a pas deux tailles selon l'écran où il se trouve.** C'est aussi ce qui fait qu'il n'a
 * plus qu'à grossir, jamais à changer de taille de travers, quand il vient se poser sur un panneau
 * ([HAUTEUR_LOCUTEUR][io.allonsy.kokoro.corps.HAUTEUR_LOCUTEUR], plus grand encore).
 *
 * 🔴 **C'est la hauteur du personnage, pas celle de sa vue** *(corrigé à E12)*. La vue se déduit du
 * personnage ([CADRE_HABITANT]), jamais l'inverse.
 *
 * ⏳ **À revérifier sur l'appareil.**
 */
val HAUTEUR_HABITANT = 110.dp

/** La vue qui le contient — c'est elle qu'on pose, et c'est elle qu'une bande doit pouvoir loger. */
val CADRE_HABITANT = cadrePour(HAUTEUR_HABITANT)

/**
 * Le transit d'un écran à l'autre (§3) — **et son retard sur le décor.**
 *
 * ⭐ **Le retard est ce qui fait qu'il habite le monde au lieu d'être collé dessus** : le décor
 * part au doigt, lui suit un peu plus tard. 🔴 **Aucune apparition, aucune disparition** — il vole
 * d'une place à l'autre, et les deux places glissent déjà avec le monde.
 *
 * ⭐ **Ralenti à 700 ms** *(demande de Xavier, 16/08/2026)* : à 420 ms le vol se voyait à peine.
 */
private const val TRANSIT_MILLIS = 700
private const val RETARD_TRANSIT_MILLIS = 200

/** La flèche de l'arc du transit — il monte et redescend, sans jamais changer de sens net. */
private val ARC_TRANSIT = 26.dp

/**
 * L'inclinaison du corps pendant le transit — **le ragdoll** *(demande de Xavier, 16/08/2026)* : il
 * penche dans le sens du vol, puis se redresse à l'arrivée, comme un pantin porté par son élan.
 *
 * 🔴 **Jamais à la crise** : `tenue.vol == Vol.AUCUN` y vaut toujours, et c'est ce qui exclut
 * l'accoudé — pencher en étant accoudé au bouton n'aurait aucun sens.
 */
private const val INCLINAISON_RAGDOLL = 10f

/**
 * ⭐ **La part de la vue parcourue en émergeant de derrière le bouton *Mot code*** (demande de
 * Xavier, 16/08/2026) — **elle n'est pas choisie, elle est lue dans le dessin** : c'est la distance
 * du sommet du crâne à la ligne des épaules. Comme la pose `accoude` pose les épaules sur l'arête du
 * bouton, partir de là met le haut du crâne **exactement** au niveau de l'arête, donc entièrement
 * caché. Il sort de derrière le bouton au lieu d'y apparaître déjà à moitié.
 */
private val EMERGENCE_CRISE_FRACTION: Float = (EPAULE_GAUCHE.y - SOMMET_TETE) / HAUTEUR_VUE

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
    /** À `false`, il ne respire pas. Une seule place s'en sert : la crise. */
    val souffle: Boolean = true,
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
 * - 🔴 **Il ne vole pas, et il ne respire pas** *(16/08/2026)*. C'est la seule place du dispositif
 *   dans ces deux cas : **il est accoudé, pas posé au sol** — une ombre portée tomberait sur
 *   l'interface —, et **une fois posé il ne bouge plus du tout.** ⭐ **C'est un écran où l'on arrive
 *   quand ça va mal** : le seul mouvement qu'on y accorde est celui de son arrivée, et il s'arrête
 *   avec elle. *(Le clignement reste : il est l'axe des yeux, pas du corps.)*
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
    souffle = false,
)

/**
 * Avant 18 h : à côté de *Sans date*, **aucun geste et aucune lecture**.
 *
 * 🔄 **Le balayage du regard est retiré** *(demande de Xavier, 16/08/2026 : « ne le fais pas lire
 * dans Thérapie »)*. Il parcourait la liste d'une carte à l'autre ; c'était le seul mouvement de la
 * posture, et **il ne reste que la respiration** — celle du corps, qui n'appartient à aucune posture.
 * ⭐ **Rien n'est perdu** : un balayage ne portait déjà aucune information (§2).
 */
private fun pensifDevantLaListe() = Place(
    perchoir = Perchoir.SANS_DATE,
    cadrage = Cadrage.A_DROITE,
    posture = Posture.Pensif,
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
 * Ce que la passe entière a besoin de savoir — **rien qu'elle calcule elle-même**, à l'image de
 * [PasseDesBras].
 */
data class EtatEntier(val rig: RigKokoro, val point: Offset, val cadre: DpSize, val endormi: Boolean)

@Composable
fun rememberEntierAnime(): MutableState<EtatEntier?> = remember { mutableStateOf(null) }

/** Quand l'habitant cesse d'être peint, la passe entière s'en va avec lui. */
@Composable
private fun EffacerEntier(entier: MutableState<EtatEntier?>) {
    DisposableEffect(Unit) { onDispose { entier.value = null } }
}

/**
 * La couche de l'habitant — **calculée entre le décor et le contenu, publiée pour être peinte
 * ailleurs.**
 *
 * [ecran] est l'écran **posé**, pas celui qu'on est en train de traverser : c'est son changement
 * qui déclenche le transit, et le retard sur le décor vient de là.
 *
 * [sortie] est la bascule des deux régimes (`corps/Locuteur.kt`) : à 1 il est hors champ et **il
 * n'est plus composé du tout** — c'est la forme la plus littérale de « une seule instance à
 * l'écran », puisqu'à cet instant précis le locuteur, lui, entre.
 *
 * ⭐ **Deux publications, pour deux couches** *(16/08/2026, demande de Xavier)* : [bras] reste la
 * seconde passe de l'écran de crise, peinte par-dessus l'interface au même endroit qu'avant ;
 * [entier] est le personnage tout entier, peint lui aussi par-dessus l'interface **partout
 * ailleurs** par [HabitantSurInterface] — Kokoro flotte devant les cartes au lieu de se glisser
 * dessous. **La crise seule garde le corps sous le contenu** : c'est elle qui ne publie jamais
 * [entier].
 */
@Composable
fun Habitant(
    perchoirs: Perchoirs,
    ecran: Ecran,
    sejour: Sejour,
    sortie: State<Float>,
    largeur: Int,
    bras: MutableState<PasseDesBras?>,
    entier: MutableState<EtatEntier?>,
    modifier: Modifier = Modifier,
) {
    val avancementSortie = sortie.value
    if (!habitantEnScene(avancementSortie)) {
        EffacerLesBras(bras)
        EffacerEntier(entier)
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

    /**
     * ⭐ **L'arrivée à la crise se fait par-derrière le bouton *Mot code*, pas par un côté**
     * (demande de Xavier, 16/08/2026) : Kokoro y est accoudé, jamais en vol (§10.2) — le faire glisser
     * depuis l'écran voisin comme les trois autres transits romprait cette pose avant même qu'elle
     * commence. Il monte donc à la verticale, sur la même horloge et le même relâché que le reste du
     * transit, sans jamais quitter l'aplomb du bouton.
     */
    val entreeParDerriereLeBouton = transit.vers == Ecran.CRISE && transit.depuis != Ecran.CRISE

    val depuis = depart?.let { pointDeLaPlace(perchoirs.cadre(it.perchoir), it.cadrage, taille) }
    val vers = arrivee?.let { pointDeLaPlace(perchoirs.cadre(it.perchoir), it.cadrage, taille) }
    val glisse = Offset(ecartDeSortie(largeur.toFloat(), avancementSortie), 0f)
    val point = when {
        depuis == null || vers == null -> (vers ?: depuis ?: return) + glisse
        entreeParDerriereLeBouton ->
            vers + Offset(0f, taille.height * EMERGENCE_CRISE_FRACTION * (1f - transit.avancement)) + glisse

        else -> lerp(depuis, vers, transit.avancement) -
            Offset(0f, arc(fleche, transit.avancement)) + glisse
    }

    /**
     * ⭐ **Il vole dans la pose qu'il avait, et prend celle d'arrivée en se posant**
     * *(défaut relevé par Xavier le 16/08/2026 : « les animations de vol ne sont pas jouées, c'est
     * l'animation de l'écran d'après qui est jouée dès le départ »)*.
     *
     * ⚠️ **La posture d'arrivée était adoptée au premier instant du transit**, et son morphing de
     * [TRANSITION_MILLIS] couvrait tout le vol — pour la documentation, un bras qui balaie 139° vers
     * le menton. **La pose de vol était bien appliquée, mais noyée dessous.** En tenant la posture de
     * départ jusqu'à l'arrivée, le vol a le champ libre et la nouvelle posture se prend **après**
     * l'atterrissage, ce qui est aussi l'ordre où on la lit.
     *
     * 🔴 **La crise garde la sienne du début à la fin** : elle ne vole pas, elle sort de derrière le
     * bouton, et ses bras ont leur propre arrivée (plus bas).
     */
    val enVol = transit.avancement < 1f && !tenue.deuxPasses

    /**
     * ⭐ **La posture d'arrivée est prise pendant que le vol la cache**, pas en se posant.
     *
     * ⚠️ **Attendre l'atterrissage rejouait la posture de départ** *(défaut relevé par Xavier : « à la
     * fin du vol, il repart sur l'animation de l'écran précédent avant de passer sur celle du nouvel
     * écran »)* : l'enveloppe rendait ses bras à la posture **de départ** en se relâchant, et la
     * nouvelle ne commençait qu'après. En basculant au moment où le dessin de vol prend tout
     * ([MONTEE_DU_VOL]), le changement se joue sous la pose de vol et **il se pose déjà dans la
     * bonne**.
     */
    val jouee = if (enVol && transit.avancement < MONTEE_DU_VOL) depart ?: tenue else tenue

    /**
     * 🔴 **En vol, les yeux sont ouverts et il n'y a pas de Zzz.** On ne dort pas en volant : le
     * sommeil est l'état d'une liste vide **une fois posé**, jamais celui d'un déplacement.
     */
    val endormi = !enVol && tenue.posture == Posture.Sommeil
    val expressionJouee =
        if (enVol && jouee.posture == Posture.Sommeil) Expression.SEREIN else jouee.expression

    /**
     * Bras, pieds, corps, tête et visage empruntent la pose lue dans `kokoro-corps-v2-right.svg`, ou
     * son miroir. 🔴 Jamais accoudé : [Vol.AUCUN] est la crise, et elle ne vole pas.
     *
     * ⚠️ **La pose remplace la posture, elle ne s'y ajoute pas** *(défaut relevé par Xavier :
     * « son bras droit est resté en direction du "aujourd'hui" », « il reste endormi avec juste les
     * pieds qui partent sur le côté »)*. [partDuVol] éteint l'ouverture des bras, la pose de sommeil
     * et le regard de la posture à mesure que le dessin de vol les reprend à son compte.
     */
    val poseVol = if (tenue.vol == Vol.AUCUN) PosesVol.AUCUNE else poseDeVol(depuis, vers, transit.avancement)
    val partDuVol = if (poseVol === PosesVol.AUCUNE) 0f else enveloppeDuVol(transit.avancement)

    val rigDeBase = rigAnime(
        posture = jouee.posture,
        vol = tenue.vol,
        expression = expressionJouee,
        balayage = jouee.balayage,
        // 🔴 La place d'arrivée décide, pas celle qu'il joue : il cesse de souffler en arrivant à la
        // crise, et le reprend dès qu'il en repart.
        respire = tenue.souffle,
        partDuVol = partDuVol,
    )

    /**
     * 🔴 **Le ragdoll** *(demande de Xavier, 16/08/2026)* : le corps penche dans le sens du vol
     * pendant le transit, et se redresse à l'arrivée — jamais à la crise, où il est accoudé, pas en
     * vol ([Vol.AUCUN]).
     */
    val balancement = if (tenue.vol != Vol.AUCUN) inclinaisonDuVol(depuis, vers, transit.avancement) else 0f

    /**
     * 🔴 **La ligne sous laquelle les bras ne se peignent pas — fixe, et posée là où ils finiront.**
     *
     * Les bras sont peints **par-dessus** le bouton pendant que le corps passe **dessous** : sans
     * coupe, on verrait des bras flotter seuls sur l'interface.
     *
     * ⚠️ **Elle a suivi l'arête du bouton, et c'est ce qui rendait l'affaissement invisible** *(défaut
     * relevé par Xavier)* : les bras pivotent aux épaules, et les épaules n'arrivent à l'arête qu'à la
     * fin de la montée — tout le geste se jouait donc **sous** la ligne. En la laissant à sa place
     * d'arrivée, les bras levés franchissent l'arête à mi-montée, **exactement quand ils commencent à
     * descendre**. Rien ne flotte pour autant : à cet instant la tête dépasse déjà de moitié.
     *
     * 🔴 **Fixe veut aussi dire aucune bascule** : plus rien n'apparaît d'un coup à l'arrivée.
     */
    val bouton = perchoirs.cadre(Perchoir.CRISE)
    val coupeDesBras = bouton?.let {
        it.top + taille.height / HAUTEUR_VUE * DESCENTE_DU_BRAS_HORIZONTAL
    }

    /**
     * Il sort les bras à la verticale ([OUVERTURE_BRAS_LEVES]) et les laisse s'affaisser sur l'arête
     * ([OUVERTURE_HORIZONTALE]). **La descente commence à mi-montée et se termine avec elle** : un
     * seul mouvement, celui du corps, dont la seconde moitié replie les bras. [poseDesBras] ne
     * commande plus que la coupe, qui découvre ensuite les avant-bras déjà posés.
     *
     * 🔴 Cette pose n'appartient à aucune posture : `Posture.Accoude` rend toujours l'horizontale.
     */
    val affaissement = if (entreeParDerriereLeBouton) secondeMoitie(transit.avancement) else 1f
    val brasDeLaCrise = OUVERTURE_BRAS_LEVES + (OUVERTURE_HORIZONTALE - OUVERTURE_BRAS_LEVES) * affaissement

    val rig = rigDeBase.copy(
        inclinaison = rigDeBase.inclinaison + balancement,
        ouvertureBrasGauche = if (tenue.deuxPasses) brasDeLaCrise else rigDeBase.ouvertureBrasGauche,
        ouvertureBrasDroit = if (tenue.deuxPasses) brasDeLaCrise else rigDeBase.ouvertureBrasDroit,
        poseBrasGauche = poseVol.brasGauche.transformation.sous(rigDeBase.poseBrasGauche),
        poseBrasDroit = poseVol.brasDroit.transformation.sous(rigDeBase.poseBrasDroit),
        posePiedGauche = poseVol.piedGauche.transformation.sous(rigDeBase.posePiedGauche),
        posePiedDroit = poseVol.piedDroit.transformation.sous(rigDeBase.posePiedDroit),
        vol = poseVol.tronc,
    )

    /**
     * 🔴 **Le même rig, le même point, le même cadre — publiés, jamais recalculés.** Aucune des deux
     * passes ne refait de calcul : deux rigs animés séparément dériveraient l'un de l'autre au
     * premier clignement, et deux transits parallèles feraient voler les bras à côté du corps.
     */
    SideEffect {
        bras.value = if (tenue.deuxPasses) PasseDesBras(rig, point, cadre, coupeDesBras) else null
        entier.value = if (tenue.deuxPasses) {
            null
        } else {
            EtatEntier(rig, point, cadre, endormi = endormi)
        }
    }
    EffacerLesBras(bras)
    EffacerEntier(entier)

    // 🔴 À la crise seulement : le corps peint ici, sous le contenu ; ses bras viennent de
    // [BrasDeLHabitant], peints par-dessus le bouton *Mot code* (§1.3). Partout ailleurs, c'est
    // [HabitantSurInterface] qui peint le personnage entier, au-dessus du contenu.
    if (tenue.deuxPasses) {
        CorpsDerriereLeBouton(
            rig = rig,
            point = point,
            cadre = cadre,
            basDuBouton = bouton?.bottom,
            modifier = modifier,
        )
    }
}

/**
 * Le corps de la crise, **coupé au bas du bouton *Mot code***.
 *
 * ⚠️ **Sans cette coupe, on le voyait dépasser sous le bouton** *(défaut relevé par Xavier le
 * 16/08/2026)* : le bouton fait 88 dp et le cache tant qu'il est à sa place, mais **le personnage
 * part d'un demi-corps plus bas pour émerger** — et cette partie-là ne tombait derrière rien. Le
 * bouton n'occulte que ce qu'il recouvre ; **en dessous, il n'y a plus que le décor.**
 *
 * ⭐ **La coupe ne se voit jamais à l'arrêt** : accoudé, il descend de 66 dp sous l'arête, le bouton
 * en fait 88. **Rien de lui n'atteint jamais cette ligne** quand il est posé.
 *
 * ⚠️ [requiredSize] et non `size` : la boîte de coupe est plus courte que le cadre du personnage, et
 * une contrainte de taille ordinaire **le rétrécirait** au lieu de le couper.
 */
@Composable
private fun CorpsDerriereLeBouton(
    rig: RigKokoro,
    point: Offset,
    cadre: DpSize,
    basDuBouton: Float?,
    modifier: Modifier = Modifier,
) {
    val coupe = basDuBouton?.let { with(LocalDensity.current) { it.toDp() } }
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (coupe == null) Modifier else Modifier.height(coupe))
                .clipToBounds(),
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(point.x.roundToInt(), point.y.roundToInt()) }
                    .requiredSize(cadre),
            ) {
                CorpsKokoro(
                    rig = rig,
                    modifier = Modifier.fillMaxSize(),
                    palette = PALETTE_CLAIRE,
                    passe = Passe.CORPS,
                )
            }
        }
    }
}

/**
 * La seconde couche du personnage — **peinte par-dessus l'interface**, partout sauf à la crise
 * *(demande de Xavier, 16/08/2026)* : Kokoro flotte devant les cartes de la liste plutôt que de se
 * glisser dessous.
 */
@Composable
fun HabitantSurInterface(entier: State<EtatEntier?>, modifier: Modifier = Modifier) {
    val etat = entier.value ?: return
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset { IntOffset(etat.point.x.roundToInt(), etat.point.y.roundToInt()) }
                .size(etat.cadre),
        ) {
            // 🔴 Il garde les couleurs du SVG, jour et nuit : il n'est pas posé sur le fond de
            // l'application, il est posé dans le décor. Le repeindre avec le ciel lui donnerait une
            // seconde apparence à décoder — le décor change d'heure, lui non.
            CorpsKokoro(
                rig = etat.rig,
                modifier = Modifier.fillMaxSize(),
                palette = PALETTE_CLAIRE,
                passe = Passe.ENTIER,
            )

            AnimatedVisibility(
                visible = etat.endormi,
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
 * La seconde moitié d'une course, ramenée à une course entière — 0 jusqu'à mi-parcours, puis 0 → 1.
 * 🔴 Adoucie aux deux bouts : une rampe droite démarrerait d'un coup au milieu, c'est-à-dire
 * l'animation brusque que `CORPS.md` §5 refuse.
 */
fun secondeMoitie(avancement: Float): Float = adouci(((avancement - 0.5f) * 2f).coerceIn(0f, 1f))

/**
 * L'enveloppe du geste de vol : elle monte, elle **tient**, elle se relâche à l'atterrissage.
 *
 * ⚠️ Un demi-sinus rendait le vol invisible — sa pleine valeur ne durait qu'un instant, et Kokoro y
 * traverse l'écran à plus de 2 000 px/s quand il n'est pas encore rentré dans le champ.
 * 🔴 Pas un mouvement continu pour autant (`CORPS.md` §8 point 7) : nulle aux deux bouts, le temps
 * d'un transit, et rien ne la déclenche qu'un doigt. Dérivée nulle aux quatre coins.
 */
fun enveloppeDuVol(avancement: Float): Float = when {
    avancement <= 0f || avancement >= 1f -> 0f
    avancement < MONTEE_DU_VOL -> adouci(avancement / MONTEE_DU_VOL)
    avancement > 1f - RELACHE_DU_VOL -> adouci((1f - avancement) / RELACHE_DU_VOL)
    else -> 1f
}

/** Le geste est pris avant qu'il ne rentre dans le champ, et rendu en se posant. */
private const val MONTEE_DU_VOL = 0.25f
private const val RELACHE_DU_VOL = 0.22f

/** Le lissage de toutes les courses d'ici : dérivée nulle aux deux bouts. */
private fun adouci(t: Float): Float = t * t * (3f - 2f * t)

/** L'inclinaison du ragdoll : il penche dans le sens du vol, puis se redresse à l'arrivée. */
private fun inclinaisonDuVol(depuis: Offset?, vers: Offset?, avancement: Float): Float {
    if (depuis == null || vers == null) return 0f
    val direction = sign(vers.x - depuis.x)
    if (direction == 0f) return 0f
    return INCLINAISON_RAGDOLL * direction * enveloppeDuVol(avancement)
}

/** Le corps entier, dans la pose que le vol vers la droite ou vers la gauche lui donne. */
private data class PosesVol(
    val brasGauche: PoseMembre,
    val brasDroit: PoseMembre,
    val piedGauche: PoseMembre,
    val piedDroit: PoseMembre,
    /** ⭐ Le corps resserré et la tête décalée — la part du dessin que les membres ne portent pas. */
    val tronc: PoseTronc,
) {
    companion object {
        val AUCUNE = PosesVol(PoseMembre(), PoseMembre(), PoseMembre(), PoseMembre(), PoseTronc())
    }
}

/**
 * Le corps entier pendant un transit horizontal : vers la droite il lit `kokoro-corps-v2-right.svg`
 * ([POSE_VOL_DROITE_TRONC] et consorts), vers la gauche son miroir calculé
 * ([io.allonsy.kokoro.corps.PoseMembre.miroir]). Même enveloppe que le ragdoll ([enveloppeDuVol]).
 */
private fun poseDeVol(depuis: Offset?, vers: Offset?, avancement: Float): PosesVol {
    if (depuis == null || vers == null) return PosesVol.AUCUNE
    val direction = sign(vers.x - depuis.x)
    if (direction == 0f) return PosesVol.AUCUNE
    val enveloppe = enveloppeDuVol(avancement)
    return if (direction > 0f) {
        PosesVol(
            brasGauche = POSE_VOL_DROITE_BRAS_GAUCHE.echelle(enveloppe),
            brasDroit = POSE_VOL_DROITE_BRAS_DROIT.echelle(enveloppe),
            piedGauche = POSE_VOL_DROITE_PIED_GAUCHE.echelle(enveloppe),
            piedDroit = POSE_VOL_DROITE_PIED_DROIT.echelle(enveloppe),
            tronc = POSE_VOL_DROITE_TRONC.echelle(enveloppe),
        )
    } else {
        PosesVol(
            brasGauche = POSE_VOL_GAUCHE_BRAS_GAUCHE.echelle(enveloppe),
            brasDroit = POSE_VOL_GAUCHE_BRAS_DROIT.echelle(enveloppe),
            piedGauche = POSE_VOL_GAUCHE_PIED_GAUCHE.echelle(enveloppe),
            piedDroit = POSE_VOL_GAUCHE_PIED_DROIT.echelle(enveloppe),
            tronc = POSE_VOL_GAUCHE_TRONC.echelle(enveloppe),
        )
    }
}

/**
 * Ce que la seconde passe a besoin de savoir — **rien qu'elle calcule elle-même.**
 *
 * ⭐ **Ce n'est pas un second personnage** (`CORPS.md` §8 point 8) : c'est le même, peint en deux
 * fois pour qu'un bouton puisse passer entre son corps et ses bras.
 */
data class PasseDesBras(
    val rig: RigKokoro,
    val point: Offset,
    val cadre: DpSize,
    /**
     * ⭐ **La ligne sous laquelle les bras ne se peignent pas** — l'arête du bouton tant qu'il émerge,
     * puis le bas des bras une fois posés. `null` quand le bouton n'a pas encore été mesuré.
     */
    val coupe: Float? = null,
)

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
    val coupe = passe.coupe?.let { with(LocalDensity.current) { it.toDp() } }
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (coupe == null) Modifier else Modifier.height(coupe))
                .clipToBounds(),
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(passe.point.x.roundToInt(), passe.point.y.roundToInt()) }
                    .requiredSize(passe.cadre),
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
