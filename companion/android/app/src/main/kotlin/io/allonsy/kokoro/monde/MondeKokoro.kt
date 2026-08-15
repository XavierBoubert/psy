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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.corps.CorpsKokoro
import io.allonsy.kokoro.corps.HAUTEUR_VUE
import io.allonsy.kokoro.corps.LARGEUR_VUE
import io.allonsy.kokoro.corps.PALETTE_CLAIRE
import io.allonsy.kokoro.corps.Posture
import io.allonsy.kokoro.corps.rigAnime
import io.allonsy.kokoro.decor.Decor
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.ui.Accuse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Raideur du ressort qui pose la caméra une fois le doigt levé.
 *
 * ⭐ **Un ressort, et non une durée fixe.** C'est la seule façon de repartir **à la vitesse du
 * doigt** : une interpolation à durée fixe redémarre à zéro, donc le monde s'arrête une fraction de
 * seconde au lever du doigt avant de repartir — la saccade. Le ressort, lui, continue le geste.
 *
 * Amorti critique : il rejoint sa cible sans jamais osciller. 120 pose la caméra en ~600 ms, le
 * tempo des transitions du corps (`CORPS.md` §5).
 */
private const val RAIDEUR = 120f

/** Plafond de l'élan repris, en écrans par seconde — au-delà, le ressort dépasserait sa cible. */
private const val ELAN_MAX = 6f

/** Hauteur de Kokoro, en fraction de la hauteur de l'écran. */
private const val TAILLE_KOKORO = 0.24f

/**
 * Il se tient **dans** le feuillage, pas au-dessus.
 *
 * ⭐ Le biais est réglé pour que les feuilles du premier plan **lui passent devant les pieds** : sans
 * ce recouvrement, il flotte au-dessus du décor au lieu d'y être posé, et le parallaxe perd ce qu'il
 * venait chercher.
 */
private val PLACE_DE_KOKORO = BiasAlignment(horizontalBias = 0f, verticalBias = 0.62f)

/** La montée d'une étape ouverte. Assez lente pour se voir, assez courte pour ne pas se subir. */
private const val MONTEE_ETAPE_MS = 320

/** De quoi passer sous la roue dentée sans la toucher — elle fait 54 dp plus son épaisseur. */
private val SOUS_LA_ROUE = 92.dp

/**
 * Le monde de Kokoro — cinq écrans, un décor continu, aucun bouton.
 *
 * ⭐ **On y navigue au doigt et le décor suit le doigt**, au lieu d'attendre qu'il se lève : le
 * geste montre son effet pendant qu'on le fait, donc il n'y a rien à apprendre ni à deviner. C'est
 * la seule façon de rendre quatre écrans découvrables sans jamais rien afficher pour les annoncer —
 * et rien ne les annonce, parce que Kokoro ne vient jamais vers Xavier.
 *
 * ⭐ **Le geste ne s'interrompt jamais** *(14/08/2026)*. La caméra est une valeur ordinaire, écrite
 * directement par le doigt — pas une animation à qui l'on demanderait de se déplacer image par
 * image, ce qui coûtait une image de retard à chaque doigt posé. Au lever, un ressort la reprend
 * **à la vitesse qu'elle avait**. Il n'y a donc plus deux mouvements séparés par un arrêt, mais un
 * seul : celui du doigt, prolongé.
 *
 * 🔴 **Le décor ne bouge pas tout seul.** Aucune dérive, aucun nuage qui file, aucune animation
 * d'ambiance : hors la respiration de Kokoro, rien ne se déplace sans que le doigt le déplace
 * (`companion/README.md` §5 — jamais de mouvement à interpréter).
 */
@Composable
fun MondeKokoro(
    palette: PaletteDecor,
    contactNom: String,
    onFonction: (Fonction) -> Unit,
    onReglages: () -> Unit,
    modifier: Modifier = Modifier,
    envoiEnCours: Boolean = false,
    accesPerdu: Boolean = false,
    accuse: String? = null,
    onAccuseFini: () -> Unit = {},
) {
    var ecran by remember { mutableStateOf(Ecran.CENTRE) }
    var taille by remember { mutableStateOf(IntSize.Zero) }
    var ouverte by remember { mutableStateOf<Etape?>(null) }
    var affichee by remember { mutableStateOf<Etape?>(null) }
    val vue = remember { mutableStateOf(Ecran.CENTRE.camera) }
    val pose = remember { mutableStateOf<Job?>(null) }
    val portee = rememberCoroutineScope()

    /**
     * 🔴 **Le bouton *retour* du téléphone ferme le panneau, il ne quitte pas l'application.** Sans
     * ça, le geste système le plus ancré du téléphone faisait disparaître Kokoro d'un coup depuis une
     * étape ouverte — exactement le contraire de la prévisibilité annoncée.
     *
     * ⭐ **Il fait la même chose que le bouton *Fermer***, et rien de plus : le retour ne traverse
     * pas le monde, il ne remonte pas d'écran en écran. **Un seul geste, un seul effet.**
     */
    BackHandler(enabled = ouverte != null) { ouverte = null }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { taille = it }
            .pointerInput(taille, ouverte != null) {
                if (taille.width == 0 || taille.height == 0) return@pointerInput
                if (ouverte != null) return@pointerInput

                suivreLeDoigt(
                    taille = taille,
                    ecranCourant = { ecran },
                    vueCourante = { vue.value },
                    onSaisie = { pose.value?.cancel() },
                    onGlisse = { vue.value = it },
                    onLever = { depuis, axe, elan ->
                        val arrivee = aterrissage(vue.value, elan, depuis, axe)
                        ecran = arrivee
                        pose.value = portee.launch {
                            Animatable(vue.value, Offset.VectorConverter).animateTo(
                                targetValue = arrivee.camera,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = RAIDEUR,
                                    visibilityThreshold = souffleDuPixel(taille),
                                ),
                                initialVelocity = elan,
                            ) {
                                vue.value = value
                            }
                        }
                    },
                )
            },
    ) {
        Decor(camera = { vue.value }, palette = palette)

        Ecran.entries.forEach { habitant ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(
                            x = ((habitant.camera.x - vue.value.x) * taille.width).roundToInt(),
                            y = ((habitant.camera.y - vue.value.y) * taille.height).roundToInt(),
                        )
                    },
            ) {
                ContenuEcran(
                    ecran = habitant,
                    contactNom = contactNom,
                    envoiEnCours = envoiEnCours,
                    accesPerdu = accesPerdu,
                    onOuvrir = { etape ->
                        affichee = etape
                        ouverte = etape
                    },
                    onFonction = onFonction,
                    onReglages = onReglages,
                )
            }
        }

        EtapeOuverte(etape = affichee, visible = ouverte != null, onFermer = { ouverte = null })

        Accuse(
            texte = accuse,
            onFini = onAccuseFini,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

/**
 * Le seuil sous lequel le ressort considère qu'il est arrivé — **une demi-image de large**.
 *
 * 🔴 **C'est la cause du rattrapage d'un ou deux pixels en fin de traversée.** Sans seuil donné, le
 * ressort prend celui de Compose, `0.01`, appliqué à des unités qui valent **un écran entier** :
 * l'animation s'arrêtait donc à un centième d'écran de sa cible — une dizaine de pixels — et la
 * valeur **sautait** sur la cible d'un coup. Exprimé en fraction d'écran, un demi-pixel vaut
 * `0.5 / largeur`, et le saut passe sous la définition de la dalle.
 */
private fun souffleDuPixel(taille: IntSize): Offset = Offset(
    x = 0.5f / taille.width.coerceAtLeast(1),
    y = 0.5f / taille.height.coerceAtLeast(1),
)

/**
 * Ce qu'il y a dans chaque écran — **une rubrique par écran** (`companion/INTERFACE.md` §3).
 *
 * 🔴 **Kokoro garde les couleurs du SVG, jour et nuit** ([PALETTE_CLAIRE]) : il n'est pas posé sur
 * le fond de l'application, il est posé dans le décor. Le repeindre avec le ciel reviendrait à lui
 * donner une deuxième apparence à décoder — le décor change d'heure, lui non.
 */
@Composable
private fun ContenuEcran(
    ecran: Ecran,
    contactNom: String,
    envoiEnCours: Boolean,
    accesPerdu: Boolean,
    onOuvrir: (Etape) -> Unit,
    onFonction: (Fonction) -> Unit,
    onReglages: () -> Unit,
) {
    when (ecran) {
        Ecran.CENTRE -> Box(modifier = Modifier.fillMaxSize()) {
            CorpsKokoro(
                rig = rigAnime(Posture.Repos),
                modifier = Modifier
                    .align(PLACE_DE_KOKORO)
                    .fillMaxHeight(TAILLE_KOKORO)
                    .aspectRatio(LARGEUR_VUE / HAUTEUR_VUE),
                palette = PALETTE_CLAIRE,
            )
            RoueDentee(onClic = onReglages, modifier = Modifier.align(Alignment.TopEnd))
            if (accesPerdu) {
                AvisAcces(
                    onReglages = onReglages,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 24.dp)
                        .padding(top = SOUS_LA_ROUE),
                )
            }
        }

        Ecran.GAUCHE -> ContenuTherapie(
            onOuvrir = { etape ->
                when (val ouverture = etape.ouverture) {
                    is Ouverture.Ecran -> onFonction(ouverture.fonction)
                    is Ouverture.Detail -> onOuvrir(etape)
                }
            },
        )

        Ecran.DROITE -> ContenuDocumentation()
        Ecran.HAUT -> ContenuBilan()
        Ecran.BAS -> ContenuCriseDuMonde(
            contactNom = contactNom,
            envoiEnCours = envoiEnCours,
            onFonction = onFonction,
        )
    }
}

/**
 * L'étape ouverte, posée **au-dessus du monde entier** et non dans son écran : elle prend l'écran
 * complet (§3.1), et tant qu'elle est là **la traversée est coupée** — sans quoi le monde
 * continuerait de glisser derrière un panneau qui le cache.
 *
 * ⭐ **Elle monte en 320 ms et s'arrête.** Un panneau qui apparaît d'un coup se lit comme un
 * changement d'application ; un panneau qui rebondit serait l'animation brusque interdite.
 */
@Composable
private fun EtapeOuverte(etape: Etape?, visible: Boolean, onFermer: () -> Unit) {
    val detail = (etape?.ouverture as? Ouverture.Detail)?.texte ?: return
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(animationSpec = tween(MONTEE_ETAPE_MS)) { hauteur -> hauteur },
        exit = slideOutVertically(animationSpec = tween(MONTEE_ETAPE_MS)) { hauteur -> hauteur },
    ) {
        PanneauEtape(titre = etape.titre, detail = detail, onFermer = onFermer)
    }
}

/**
 * Le geste : un axe verrouillé au premier mouvement, puis la caméra collée au doigt.
 *
 * ⭐ **L'axe se verrouille et ne se relâche plus jusqu'au lever du doigt.** Sans ce verrou, un geste
 * un peu oblique — et ils le sont tous — ferait hésiter le monde entre deux écrans pendant qu'on le
 * traverse. Ce serait le seul endroit du dispositif où le résultat d'un geste dépendrait de sa
 * précision.
 *
 * ⭐ **Le geste repart d'où la caméra est**, et non du centre de l'écran de destination : reprendre
 * le monde pendant qu'il se pose ne le fait donc jamais sauter. Un simple appui, lui, ne déclenche
 * rien du tout — il n'y a de geste qu'à partir du seuil de glissement d'Android.
 */
private suspend fun PointerInputScope.suivreLeDoigt(
    taille: IntSize,
    ecranCourant: () -> Ecran,
    vueCourante: () -> Offset,
    onSaisie: () -> Unit,
    onGlisse: (Offset) -> Unit,
    onLever: (Ecran, Axe, Offset) -> Unit,
) {
    var axe: Axe? = null
    var depuis = Ecran.CENTRE
    var ancre = Offset.Zero
    var cumul = Offset.Zero
    val suivi = VelocityTracker()

    detectDragGestures(
        onDragStart = {
            onSaisie()
            axe = null
            depuis = ecranCourant()
            ancre = vueCourante()
            cumul = Offset.Zero
            suivi.resetTracking()
        },
        onDragEnd = {
            val verrou = axe ?: Axe.HORIZONTAL
            onLever(depuis, verrou, elanDe(suivi, taille, verrou))
        },
        onDragCancel = { onLever(depuis, axe ?: Axe.HORIZONTAL, Offset.Zero) },
        onDrag = { changement, ecart ->
            suivi.addPointerInputChange(changement)
            changement.consume()
            cumul += ecart

            val verrou = axe ?: axeDe(ecart).also { axe = it }
            val brut = when (verrou) {
                Axe.HORIZONTAL -> Offset(ancre.x - cumul.x / taille.width, ancre.y)
                Axe.VERTICAL -> Offset(ancre.x, ancre.y - cumul.y / taille.height)
            }

            onGlisse(bornerCamera(brut, depuis, verrou))
        },
    )
}

private fun axeDe(ecart: Offset): Axe =
    if (abs(ecart.x) >= abs(ecart.y)) Axe.HORIZONTAL else Axe.VERTICAL

/**
 * La vitesse du doigt, retournée en vitesse de caméra — en écrans par seconde, sur le seul axe
 * verrouillé.
 *
 * Le signe s'inverse : pousser le doigt vers la droite emmène la caméra vers la gauche. Le plafond
 * n'est pas un confort, c'est ce qui garantit que le ressort ne dépasse pas visiblement sa cible.
 */
private fun elanDe(suivi: VelocityTracker, taille: IntSize, axe: Axe): Offset {
    val mesure = suivi.calculateVelocity()
    val ecrans = when (axe) {
        Axe.HORIZONTAL -> Offset(-mesure.x / taille.width, 0f)
        Axe.VERTICAL -> Offset(0f, -mesure.y / taille.height)
    }

    return Offset(
        x = ecrans.x.coerceIn(-ELAN_MAX, ELAN_MAX),
        y = ecrans.y.coerceIn(-ELAN_MAX, ELAN_MAX),
    )
}
