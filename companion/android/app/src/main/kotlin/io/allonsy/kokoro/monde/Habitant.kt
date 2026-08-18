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

// Hauteur du personnage, pas de sa vue — CADRE_HABITANT s'en déduit via cadrePour().
val HAUTEUR_HABITANT = 110.dp

val CADRE_HABITANT = cadrePour(HAUTEUR_HABITANT)

private const val TRANSIT_MILLIS = 700
private const val RETARD_TRANSIT_MILLIS = 200

private val ARC_TRANSIT = 26.dp

private const val INCLINAISON_RAGDOLL = 10f

// Le padding bas du bandeau (Pieces.kt) : le sommet de la tête arrive ainsi sous la roue dentée, plus sous la bande.
private val MONTEE_DU_PERCHOIR = 18.dp

// Distance crâne→épaules ÷ hauteur de la vue, lue dans le dessin : fait émerger Kokoro exactement de derrière le bouton Mot code.
private val EMERGENCE_CRISE_FRACTION: Float = (EPAULE_GAUCHE.y - SOMMET_TETE) / HAUTEUR_VUE

private const val FONDU_ZZZ_MILLIS = 600

fun ecartDeSortie(largeur: Float, sortie: Float): Float = largeur * sortie

const val HEURE_DU_CHECKIN = 18

enum class Perchoir { AUJOURDHUI, SANS_DATE, DOCUMENTATION, BILAN, CRISE, PLAFOND }

enum class Cadrage {
    A_DROITE,
    AU_CENTRE,

    // Le cadre est ici le bouton lui-même : les épaules tombent sur son arête, tout ce qui est dessous passe derrière.
    EPAULES_AU_BORD,
}

// expression et balayage à null valent « ce que la posture dit », jamais « rien ».
data class Place(
    val perchoir: Perchoir,
    val cadrage: Cadrage,
    val posture: Posture,
    val expression: Expression? = null,
    val balayage: Balayage? = null,
    val vol: Vol = Vol.LEVITATION,
    val hauteur: Dp = HAUTEUR_HABITANT,
    // Corps sous le contenu, bras dessus — seule la crise s'en sert.
    val deuxPasses: Boolean = false,
    // À false, il ne respire pas — seule la crise s'en sert.
    val souffle: Boolean = true,
    // À false, une liste vide ne l'endort pas — il n'y a pas de liste à cette place.
    val sommeilPossible: Boolean = true,
) {
    // La place ne change jamais ici : il s'endort là où il était.
    fun endormi(): Place = copy(
        posture = Posture.Sommeil,
        expression = null,
        balayage = null,
        vol = Vol.SOMMEIL,
    )

    // Sa bande a défilé sous le bandeau : il repose les bras plutôt que de désigner une place sortie du cadre.
    fun auRepos(): Place = copy(posture = Posture.Repos, balayage = null)
}

data class Sejour(
    val heure: Int,
    val checkinFait: Boolean,
    val vides: Set<Ecran> = ECRANS_VIDES,
)

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
        cadrage = Cadrage.A_DROITE,
        posture = Posture.Lecture,
        balayage = Balayage(),
    )

    Ecran.BILAN -> Place(
        perchoir = Perchoir.BILAN,
        cadrage = Cadrage.AU_CENTRE,
        posture = Posture.Floss,
    )

    Ecran.CRISE -> veilleSurLaCrise()
}

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

private fun pensifDevantLaListe() = Place(
    perchoir = Perchoir.SANS_DATE,
    cadrage = Cadrage.A_DROITE,
    posture = Posture.Pensif,
)

private fun montreLeCheckin(checkinFait: Boolean) = Place(
    perchoir = Perchoir.AUJOURDHUI,
    cadrage = Cadrage.A_DROITE,
    posture = Posture.Montre(Cote.GAUCHE),
    expression = if (checkinFait) Expression.CHALEUREUX else null,
)

// Vrai quand la bande visée a défilé au-dessus du bandeau : en vol la pose d'arrivée n'est pas encore la sienne.
fun horsCadre(perchoirs: Perchoirs, plafond: Rect?, arrivee: Place?, avancement: Float): Boolean {
    if (plafond == null || arrivee == null || avancement < 1f) return false
    val cadre = perchoirs.cadre(arrivee.perchoir) ?: return false
    return cadre.bottom <= plafond.bottom
}

// Non clippées, exprès : positionInRoot continue de compter quand la bande sort de la dalle.
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

fun Modifier.perchoir(perchoirs: Perchoirs, perchoir: Perchoir): Modifier =
    onGloballyPositioned { perchoirs.poser(perchoir, Rect(it.positionInRoot(), it.size.toSize())) }

data class EtatEntier(val rig: RigKokoro, val point: Offset, val cadre: DpSize, val endormi: Boolean)

@Composable
fun rememberEntierAnime(): MutableState<EtatEntier?> = remember { mutableStateOf(null) }

@Composable
private fun EffacerEntier(entier: MutableState<EtatEntier?>) {
    DisposableEffect(Unit) { onDispose { entier.value = null } }
}

// ecran est l'écran posé (déclenche le transit) ; deux publications (bras, entier) pour deux couches.
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

    // Plafond posé uniquement par Thérapie (Bords.kt) : ailleurs perchoirs.cadre(PLAFOND) est null, sans effet.
    val plafond = perchoirs.cadre(Perchoir.PLAFOND)
    val posee = arrivee ?: depart ?: return
    val tenue = if (horsCadre(perchoirs, plafond, arrivee, transit.avancement)) posee.auRepos() else posee

    val cadre = cadrePour(tenue.hauteur)
    val taille = with(LocalDensity.current) { Size(cadre.width.toPx(), cadre.height.toPx()) }
    val fleche = with(LocalDensity.current) { ARC_TRANSIT.toPx() }

    val entreeParDerriereLeBouton = transit.vers == Ecran.CRISE && transit.depuis != Ecran.CRISE
    val entree = if (entreeParDerriereLeBouton) transit.avancement else 1f
    val enfoui = if (tenue.deuxPasses) enfouissementDeLaCrise(entree, avancementSortie) else 0f

    val depuis = depart?.let { pointDeLaPlace(perchoirs.cadre(it.perchoir), it.cadrage, taille) }
    val vers = arrivee?.let { pointDeLaPlace(perchoirs.cadre(it.perchoir), it.cadrage, taille) }

    // Accoudé, il ne quitte pas le champ par le côté : il redescend là d'où il est venu, derrière le bouton.
    val retrait = when {
        tenue.deuxPasses -> Offset(0f, taille.height * EMERGENCE_CRISE_FRACTION * enfoui)
        else -> Offset(ecartDeSortie(largeur.toFloat(), avancementSortie), 0f)
    }
    val pointBrut = when {
        depuis == null || vers == null -> (vers ?: depuis ?: return) + retrait
        tenue.deuxPasses -> vers + retrait
        else -> lerp(depuis, vers, transit.avancement) -
            Offset(0f, arc(fleche, transit.avancement)) + retrait
    }
    val montee = with(LocalDensity.current) { MONTEE_DU_PERCHOIR.toPx() }
    val point = if (plafond == null) {
        pointBrut
    } else {
        pointBrut.copy(
            y = pointBrut.y.coerceAtLeast(
                plafond.bottom - montee - taille.height * (SOMMET_TETE / HAUTEUR_VUE),
            ),
        )
    }

    val enVol = transit.avancement < 1f && !tenue.deuxPasses

    // Pose d'arrivée prise dès MONTEE_DU_VOL, cachée sous le vol, pour ne pas rejouer le départ à l'atterrissage.
    val jouee = if (enVol && transit.avancement < MONTEE_DU_VOL) depart ?: tenue else tenue

    val endormi = !enVol && tenue.posture == Posture.Sommeil
    val expressionJouee =
        if (enVol && jouee.posture == Posture.Sommeil) Expression.SEREIN else jouee.expression

    val poseVol = if (tenue.vol == Vol.AUCUN) PosesVol.AUCUNE else poseDeVol(depuis, vers, transit.avancement)
    val partDuVol = if (poseVol === PosesVol.AUCUNE) 0f else enveloppeDuVol(transit.avancement)

    val rigDeBase = rigAnime(
        posture = jouee.posture,
        vol = tenue.vol,
        expression = expressionJouee,
        balayage = jouee.balayage,
        // La place d'arrivée (tenue) décide, jamais la pose jouée : il cesse de souffler en arrivant à la crise.
        respire = tenue.souffle,
        partDuVol = partDuVol,
    )

    val balancement = if (tenue.vol != Vol.AUCUN) inclinaisonDuVol(depuis, vers, transit.avancement) else 0f

    val bouton = perchoirs.cadre(Perchoir.CRISE)
    // Coupe exactement à l'arête du bouton, jamais suspendue à l'épaule courante : rien du bras ne se dessine par-dessus,
    // sa moitié basse passe derrière. Descendre la coupe sous l'arête remontait visuellement la ligne des épaules.
    val coupeDesBras = bouton?.top

    // N'appartient à aucune posture : Posture.Accoude rend toujours l'horizontale.
    val affaissement = if (tenue.deuxPasses) secondeMoitie(1f - enfoui) else 1f
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

    // Même rig, même point, même cadre — publiés, jamais recalculés par l'autre passe.
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

// Coupé au bas du bouton Mot code : en dessous, il n'y a plus que le décor.
@Composable
private fun CorpsDerriereLeBouton(
    rig: RigKokoro,
    point: Offset,
    cadre: DpSize,
    basDuBouton: Float?,
    modifier: Modifier = Modifier,
) {
    CoucheDuCorps(
        rig = rig,
        point = point,
        cadre = cadre,
        coupe = basDuBouton,
        passe = Passe.CORPS,
        modifier = modifier,
    )
}

@Composable
private fun CoucheDuCorps(
    rig: RigKokoro,
    point: Offset,
    cadre: DpSize,
    coupe: Float?,
    passe: Passe,
    modifier: Modifier = Modifier,
) {
    val hauteur = coupe?.let { with(LocalDensity.current) { it.toDp() } }
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (hauteur == null) Modifier else Modifier.height(hauteur))
                .clipToBounds(),
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(point.x.roundToInt(), point.y.roundToInt()) }
                    // requiredSize et non size : une contrainte ordinaire rétrécirait le personnage au lieu de le couper.
                    .requiredSize(cadre),
            ) {
                CorpsKokoro(
                    rig = rig,
                    modifier = Modifier.fillMaxSize(),
                    palette = PALETTE_CLAIRE,
                    passe = passe,
                )
            }
        }
    }
}

// Même place et même pose qu'à l'écran de crise du monde, sans transit, sans souffle et sans clignement.
data class PoseFigee(
    val rig: RigKokoro,
    val point: Offset,
    val cadre: DpSize,
    val hautDuBouton: Float,
    val basDuBouton: Float,
)

@Composable
fun poseFigeeDeCrise(perchoirs: Perchoirs): PoseFigee? {
    val place = veilleSurLaCrise()
    val cadre = cadrePour(place.hauteur)
    val taille = with(LocalDensity.current) { Size(cadre.width.toPx(), cadre.height.toPx()) }
    val bouton = perchoirs.cadre(place.perchoir) ?: return null
    val point = pointDeLaPlace(bouton, place.cadrage, taille) ?: return null
    return PoseFigee(RigKokoro.pose(place.posture), point, cadre, bouton.top, bouton.bottom)
}

// Deux passes, comme au monde : le corps sous les boutons, les bras dessus.
@Composable
fun CoucheFigee(pose: PoseFigee, passe: Passe, modifier: Modifier = Modifier) {
    CoucheDuCorps(
        rig = pose.rig,
        point = pose.point,
        cadre = pose.cadre,
        coupe = if (passe == Passe.BRAS) pose.hautDuBouton else pose.basDuBouton,
        passe = passe,
        modifier = modifier,
    )
}

// Peinte par-dessus l'interface, partout sauf à la crise, où le corps reste sous le contenu.
@Composable
fun HabitantSurInterface(entier: State<EtatEntier?>, modifier: Modifier = Modifier) {
    val etat = entier.value ?: return
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset { IntOffset(etat.point.x.roundToInt(), etat.point.y.roundToInt()) }
                .size(etat.cadre),
        ) {
            // Garde les couleurs du SVG jour et nuit : il est posé dans le décor, pas sur le fond de l'app.
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

fun secondeMoitie(avancement: Float): Float = adouci(((avancement - 0.5f) * 2f).coerceIn(0f, 1f))

// 1 = entièrement caché derrière le bouton, 0 = posé dessus. Ouvrir un panneau rejoue l'entrée à l'envers : il
// redescend d'où il est venu et relève les bras, au lieu de glisser hors du champ comme partout ailleurs.
fun enfouissementDeLaCrise(entree: Float, sortie: Float): Float =
    maxOf(1f - entree.coerceIn(0f, 1f), sortie.coerceIn(0f, 1f))

fun enveloppeDuVol(avancement: Float): Float = when {
    avancement <= 0f || avancement >= 1f -> 0f
    avancement < MONTEE_DU_VOL -> adouci(avancement / MONTEE_DU_VOL)
    avancement > 1f - RELACHE_DU_VOL -> adouci((1f - avancement) / RELACHE_DU_VOL)
    else -> 1f
}

private const val MONTEE_DU_VOL = 0.25f
private const val RELACHE_DU_VOL = 0.22f

private fun adouci(t: Float): Float = t * t * (3f - 2f * t)

private fun inclinaisonDuVol(depuis: Offset?, vers: Offset?, avancement: Float): Float {
    if (depuis == null || vers == null) return 0f
    val direction = sign(vers.x - depuis.x)
    if (direction == 0f) return 0f
    return INCLINAISON_RAGDOLL * direction * enveloppeDuVol(avancement)
}

private data class PosesVol(
    val brasGauche: PoseMembre,
    val brasDroit: PoseMembre,
    val piedGauche: PoseMembre,
    val piedDroit: PoseMembre,
    val tronc: PoseTronc,
) {
    companion object {
        val AUCUNE = PosesVol(PoseMembre(), PoseMembre(), PoseMembre(), PoseMembre(), PoseTronc())
    }
}

// Vers la droite : lit kokoro-corps-v2-right.svg. Vers la gauche : son miroir.
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

// Même personnage peint en deux fois, pour qu'un bouton puisse passer entre son corps et ses bras.
data class PasseDesBras(
    val rig: RigKokoro,
    val point: Offset,
    val cadre: DpSize,
    // Arête haute du bouton ; null quand le bouton n'est pas encore mesuré.
    val coupe: Float? = null,
)

@Composable
fun rememberPasseDesBras(): MutableState<PasseDesBras?> = remember { mutableStateOf(null) }

@Composable
private fun EffacerLesBras(bras: MutableState<PasseDesBras?>) {
    DisposableEffect(Unit) { onDispose { bras.value = null } }
}

// Seule chose du personnage peinte devant l'interface, et seulement à l'écran de crise.
@Composable
fun BrasDeLHabitant(bras: State<PasseDesBras?>, modifier: Modifier = Modifier) {
    val passe = bras.value ?: return
    CoucheDuCorps(
        rig = passe.rig,
        point = passe.point,
        cadre = passe.cadre,
        coupe = passe.coupe,
        passe = Passe.BRAS,
        modifier = modifier,
    )
}

// cadre est la bande entière, pas la pancarte : permet de poser au bord droit sans connaître la largeur de la dalle.
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

// Lue dans le dessin : permet de poser les épaules exactement sur l'arête d'un bouton.
val HAUTEUR_EPAULES = EPAULE_GAUCHE.y / HAUTEUR_VUE

fun arc(fleche: Float, avancement: Float): Float = fleche * sin(PI.toFloat() * avancement)

// Asymétrique exprès : à l'ouverture il part tout de suite, à la fermeture il attend le panneau pour ne pas croiser le locuteur.
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

data class Transit(val depuis: Ecran, val vers: Ecran, val avancement: Float)

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
