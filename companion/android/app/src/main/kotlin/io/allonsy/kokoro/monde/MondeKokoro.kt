package io.allonsy.kokoro.monde

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import io.allonsy.kokoro.corps.locuteurEnScene
import io.allonsy.kokoro.decor.Decor
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.decor.rememberInclinaison
import io.allonsy.kokoro.reglages.PARALLAXE_PAR_DEFAUT
import io.allonsy.kokoro.reglages.Parallaxe
import io.allonsy.kokoro.ui.Accuse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Amorti critique (jamais d'oscillation) : 120 pose la caméra en ~600 ms, au tempo de CORPS.md §5.
private const val RAIDEUR = 120f

private const val ELAN_MAX = 6f

const val MONTEE_ETAPE_MS = 320

@Composable
fun MondeKokoro(
    palette: PaletteDecor,
    contactNom: String,
    sejour: Sejour,
    onFonction: (Fonction) -> Unit,
    onReglages: () -> Unit,
    modifier: Modifier = Modifier,
    parallaxe: Parallaxe = PARALLAXE_PAR_DEFAUT,
    envoiEnCours: Boolean = false,
    accesPerdu: Boolean = false,
    accuse: String? = null,
    onAccuseFini: () -> Unit = {},
    debug: DebugMonde = DebugMonde(),
) {
    var position by remember { mutableIntStateOf(0) }
    val perchoirs = rememberPerchoirs()
    var taille by remember { mutableStateOf(IntSize.Zero) }
    var ouverte by remember { mutableStateOf<Etape?>(null) }
    var affichee by remember { mutableStateOf<Etape?>(null) }
    val vue = remember { mutableFloatStateOf(0f) }
    val pose = remember { mutableStateOf<Job?>(null) }
    val portee = rememberCoroutineScope()
    val inclinaison = rememberInclinaison(actif = parallaxe.actif && parallaxe.inclinaison)

    // derivedStateOf : évite de recomposer les quatre écrans à chaque image.
    val ancre by remember { derivedStateOf { ancreDe(vue.floatValue) } }

    val sortie = sortieAnimee(dehors = ouverte != null)
    val locuteur by remember { derivedStateOf { locuteurEnScene(sortie.value) } }

    val bras = rememberPasseDesBras()
    val entier = rememberEntierAnime()

    BackHandler(enabled = ouverte != null) { ouverte = null }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { taille = it }
            .pointerInput(taille, ouverte != null) {
                if (taille.width == 0) return@pointerInput
                if (ouverte != null) return@pointerInput

                suivreLeDoigt(
                    largeur = taille.width,
                    positionCourante = { position },
                    vueCourante = { vue.floatValue },
                    onSaisie = { pose.value?.cancel() },
                    onGlisse = { vue.floatValue = it },
                    onLever = { depuis, elan ->
                        val arrivee = aterrissage(vue.floatValue, elan, depuis)
                        position = arrivee
                        pose.value = portee.launch {
                            Animatable(vue.floatValue, Float.VectorConverter).animateTo(
                                targetValue = arrivee.toFloat(),
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = RAIDEUR,
                                    visibilityThreshold = souffleDuPixel(taille.width),
                                ),
                                initialVelocity = elan,
                            ) {
                                vue.floatValue = value
                            }
                        }
                    },
                )
            },
    ) {
        Decor(camera = { cameraDuDecor(parallaxe, vue.floatValue, inclinaison.floatValue) }, palette = palette)

        Habitant(
            perchoirs = perchoirs,
            ecran = ecranEn(position),
            sejour = sejour,
            sortie = sortie,
            largeur = taille.width,
            bras = bras,
            entier = entier,
        )

        positionsAutour(ancre).forEach { rang ->
            key(ecranEn(rang)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset {
                            IntOffset(
                                x = ((rang - vue.floatValue) * taille.width).roundToInt(),
                                y = 0,
                            )
                        },
                ) {
                    ContenuEcran(
                        ecran = ecranEn(rang),
                        perchoirs = perchoirs,
                        contactNom = contactNom,
                        envoiEnCours = envoiEnCours,
                        accesPerdu = accesPerdu,
                        onOuvrir = { etape ->
                            affichee = etape
                            ouverte = etape
                        },
                        onFonction = onFonction,
                        onReglages = onReglages,
                        debug = debug,
                    )
                }
            }
        }

        HabitantSurInterface(entier = entier)

        BrasDeLHabitant(bras = bras)

        EtapeOuverte(
            etape = affichee,
            visible = ouverte != null,
            locuteur = locuteur,
            onFermer = { ouverte = null },
        )

        Accuse(
            texte = accuse,
            onFini = onAccuseFini,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

// Parallaxe coupée : le décor se fige à zéro, jamais figé où il était — même image à chaque venue.
private fun cameraDuDecor(parallaxe: Parallaxe, vue: Float, inclinaison: Float): Float =
    if (parallaxe.actif) vue + inclinaison else 0f

// Seuil de ressort exprimé en fraction d'écran : le seuil par défaut de Compose (0.01) laissait un saut visible sur ces unités.
private fun souffleDuPixel(largeur: Int): Float = 0.5f / largeur.coerceAtLeast(1)

@Composable
private fun ContenuEcran(
    ecran: Ecran,
    perchoirs: Perchoirs,
    contactNom: String,
    envoiEnCours: Boolean,
    accesPerdu: Boolean,
    onOuvrir: (Etape) -> Unit,
    onFonction: (Fonction) -> Unit,
    onReglages: () -> Unit,
    debug: DebugMonde,
) {
    when (ecran) {
        Ecran.THERAPIE -> ContenuTherapie(
            perchoirs = perchoirs,
            accesPerdu = accesPerdu,
            onReglages = onReglages,
            onOuvrir = { etape ->
                when (val ouverture = etape.ouverture) {
                    is Ouverture.Ecran -> onFonction(ouverture.fonction)
                    is Ouverture.Detail -> onOuvrir(etape)
                }
            },
            onBasculerAffichage = debug.onBasculerAffichageTherapie,
        )

        Ecran.DOCUMENTATION -> ContenuDocumentation(
            perchoirs = perchoirs,
            videDebug = debug.documentationVide,
            onBasculerVideDebug = debug.onBasculerDocumentationVide,
        )

        Ecran.BILAN -> ContenuBilan(
            perchoirs = perchoirs,
            videDebug = debug.bilanVide,
            onBasculerVideDebug = debug.onBasculerBilanVide,
        )

        Ecran.CRISE -> ContenuCriseDuMonde(
            perchoirs = perchoirs,
            contactNom = contactNom,
            envoiEnCours = envoiEnCours,
            onFonction = onFonction,
        )
    }
}

// Jamais montrées hors build debug : ces bascules ne pilotent rien du dossier.
data class DebugMonde(
    val documentationVide: Boolean = true,
    val bilanVide: Boolean = true,
    val onBasculerAffichageTherapie: (aujourdhui: Boolean) -> Unit = {},
    val onBasculerDocumentationVide: () -> Unit = {},
    val onBasculerBilanVide: () -> Unit = {},
)

@Composable
private fun EtapeOuverte(
    etape: Etape?,
    visible: Boolean,
    locuteur: Boolean,
    onFermer: () -> Unit,
) {
    val detail = (etape?.ouverture as? Ouverture.Detail)?.texte

    AnimatedVisibility(
        visible = visible && detail != null,
        enter = slideInVertically(animationSpec = tween(MONTEE_ETAPE_MS)) { hauteur -> hauteur },
        exit = slideOutVertically(animationSpec = tween(MONTEE_ETAPE_MS)) { hauteur -> hauteur },
    ) {
        // etape vient de affichee, pas de ouverte : ça garde le contenu affiché pendant la descente du panneau.
        if (etape != null && detail != null) {
            PanneauEtape(
                titre = etape.titre,
                detail = detail,
                locuteur = locuteur,
                onFermer = onFermer,
            )
        }
    }
}

private suspend fun PointerInputScope.suivreLeDoigt(
    largeur: Int,
    positionCourante: () -> Int,
    vueCourante: () -> Float,
    onSaisie: () -> Unit,
    onGlisse: (Float) -> Unit,
    onLever: (Int, Float) -> Unit,
) {
    var depuis = 0
    var ancre = 0f
    var cumul = 0f
    val suivi = VelocityTracker()

    detectHorizontalDragGestures(
        onDragStart = {
            onSaisie()
            depuis = positionCourante()
            // Repart d'où la caméra est, pas du centre de l'écran de destination : reprendre le monde en cours de pose ne le fait jamais sauter.
            ancre = vueCourante()
            cumul = 0f
            suivi.resetTracking()
        },
        onDragEnd = { onLever(depuis, elanDe(suivi, largeur)) },
        onDragCancel = { onLever(depuis, 0f) },
        onHorizontalDrag = { changement, ecart ->
            suivi.addPointerInputChange(changement)
            changement.consume()
            cumul += ecart

            onGlisse(ancre - cumul / largeur)
        },
    )
}

// Signe inversé (doigt à droite → caméra à gauche) ; le plafond évite que le ressort dépasse visiblement sa cible.
private fun elanDe(suivi: VelocityTracker, largeur: Int): Float =
    (-suivi.calculateVelocity().x / largeur).coerceIn(-ELAN_MAX, ELAN_MAX)
