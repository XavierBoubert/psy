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
import io.allonsy.kokoro.corps.Passe
import io.allonsy.kokoro.corps.locuteurEnScene
import io.allonsy.kokoro.crise.ContenuPhrase
import io.allonsy.kokoro.crise.ContenuTension
import io.allonsy.kokoro.crise.PorteDeCrise
import io.allonsy.kokoro.decor.Decor
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.decor.rememberInclinaison
import io.allonsy.kokoro.programme.AUCUNE_FAITE
import io.allonsy.kokoro.programme.Carte
import io.allonsy.kokoro.programme.Faites
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.programme.PROGRAMME_ABSENT
import io.allonsy.kokoro.programme.Programme
import io.allonsy.kokoro.programme.ReponseItem
import io.allonsy.kokoro.programme.entrainementMene
import io.allonsy.kokoro.programme.faite
import io.allonsy.kokoro.reglages.EtatAutorisations
import io.allonsy.kokoro.reglages.PARALLAXE_PAR_DEFAUT
import io.allonsy.kokoro.reglages.PanneauReglages
import io.allonsy.kokoro.reglages.Parallaxe
import io.allonsy.kokoro.reglages.Reglages
import io.allonsy.kokoro.ui.Accuse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Amorti critique (jamais d'oscillation) : 120 pose la caméra en ~600 ms, au tempo de companion/README.md §6.
private const val RAIDEUR = 120f

private const val ELAN_MAX = 6f

const val MONTEE_ETAPE_MS = 320

@Composable
fun MondeKokoro(
    palette: PaletteDecor,
    contactNom: String,
    sejour: Sejour,
    onPorteDeCrise: (PorteDeCrise) -> Unit,
    donneesReglages: DonneesReglages,
    modifier: Modifier = Modifier,
    programme: Programme = PROGRAMME_ABSENT,
    faites: Faites = AUCUNE_FAITE,
    onRendu: (String, Issue, List<ReponseItem>) -> Unit = { _, _, _ -> },
    onEntrainement: (String) -> Unit = {},
    onPdf: (Carte.Pdf) -> Unit = {},
    parallaxe: Parallaxe = PARALLAXE_PAR_DEFAUT,
    envoiEnCours: Boolean = false,
    accesPerdu: Boolean = false,
    accuse: String? = null,
    onAccuseFini: () -> Unit = {},
) {
    var position by remember { mutableIntStateOf(0) }
    val perchoirs = rememberPerchoirs()
    var taille by remember { mutableStateOf(IntSize.Zero) }
    var ouverte by remember { mutableStateOf<Contexte?>(null) }
    var affichee by remember { mutableStateOf<Contexte?>(null) }
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

    val sejourDuJour = remember(sejour, programme, faites) {
        sejour.copy(toutFait = toutFaitAujourdhui(programme, faites))
    }

    BackHandler(enabled = ouverte != null) { ouverte = null }

    val ouvrirPanneau: (Contexte) -> Unit = { contexte ->
        affichee = contexte
        ouverte = contexte
    }

    // Tension et phrase ne quittent pas le monde : elles s'ouvrent dans le panneau, comme les réglages.
    val agir: (PorteDeCrise) -> Unit = { porte ->
        when (porte) {
            PorteDeCrise.TENSION -> ouvrirPanneau(Contexte.Tension)
            PorteDeCrise.PHRASE -> ouvrirPanneau(Contexte.Phrase)
            PorteDeCrise.MOT_CODE -> onPorteDeCrise(porte)
        }
    }

    // Kokoro n'interprète pas une carte : il ouvre son panneau, ou confie son PDF au lecteur du téléphone.
    val ouvrirLaCarte: (Carte) -> Unit = { carte ->
        when (carte) {
            is Carte.Panneau -> ouvrirPanneau(Contexte.Panneau(carte))
            is Carte.Pdf -> onPdf(carte)
        }
    }

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
            sejour = sejourDuJour,
            sortie = sortie,
            largeur = taille.width,
            bras = bras,
            entier = entier,
        )

        // Dalle pas encore mesurée : les quatre écrans se poseraient tous en x = 0, et le dernier peint (Bilan) couvrirait Thérapie.
        val voisins = if (taille.width == 0) emptyList() else positionsAutour(ancre)

        voisins.forEach { rang ->
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
                        programme = programme,
                        faites = faites,
                        onCarte = ouvrirLaCarte,
                        onPorteDeCrise = agir,
                        onReglages = { ouvrirPanneau(Contexte.Reglages) },
                        fige = ouverte != null,
                    )
                }
            }
        }

        HabitantSurInterface(entier = entier)

        BrasDeLHabitant(bras = bras)

        PanneauOuvert(
            contexte = affichee,
            visible = ouverte != null,
            locuteur = locuteur,
            faites = faites,
            donneesReglages = donneesReglages,
            onRendu = onRendu,
            onEntrainement = onEntrainement,
            onFermer = { ouverte = null },
        )

        Accuse(
            texte = accuse,
            onFini = onAccuseFini,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

// L'écran de crise du monde, immobile : même décor sans parallaxe, même Kokoro sans animation, mêmes trois boutons.
// C'est ce que la notification ouvre — il n'y a là ni monde à traverser, ni transit à jouer.
@Composable
fun SceneDeCrise(
    palette: PaletteDecor,
    contactNom: String,
    envoiEnCours: Boolean,
    onPorteDeCrise: (PorteDeCrise) -> Unit,
    onFermer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val perchoirs = rememberPerchoirs()
    val pose = poseFigeeDeCrise(perchoirs)

    Box(modifier = modifier.fillMaxSize()) {
        Decor(camera = { 0f }, palette = palette)

        if (pose != null) CoucheFigee(pose = pose, passe = Passe.CORPS)

        ContenuCriseDuMonde(
            perchoirs = perchoirs,
            contactNom = contactNom,
            envoiEnCours = envoiEnCours,
            onFonction = onPorteDeCrise,
            onFermer = onFermer,
        )

        if (pose != null) CoucheFigee(pose = pose, passe = Passe.BRAS)
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
    programme: Programme,
    faites: Faites,
    onCarte: (Carte) -> Unit,
    onPorteDeCrise: (PorteDeCrise) -> Unit,
    onReglages: () -> Unit,
    fige: Boolean,
) {
    when (ecran) {
        Ecran.THERAPIE -> ContenuTherapie(
            perchoirs = perchoirs,
            programme = programme,
            faites = faites,
            accesPerdu = accesPerdu,
            fige = fige,
            onReglages = onReglages,
            onOuvrir = onCarte,
        )

        Ecran.DOCUMENTATION -> ContenuDocumentation(
            perchoirs = perchoirs,
            programme = programme,
            onDocument = onCarte,
            fige = fige,
        )

        Ecran.BILAN -> ContenuBilan(
            perchoirs = perchoirs,
            programme = programme,
            onBilan = onCarte,
            fige = fige,
        )

        Ecran.CRISE -> ContenuCriseDuMonde(
            perchoirs = perchoirs,
            contactNom = contactNom,
            envoiEnCours = envoiEnCours,
            onFonction = onPorteDeCrise,
        )
    }
}

// État et actions du panneau réglages — porté par MondeActivity, plus par une Activity à part.
data class DonneesReglages(
    val autorisations: EtatAutorisations,
    val reglages: Reglages,
    val dossier: String?,
    val onRelire: () -> Unit,
    val onEnregistrer: (Reglages) -> Unit,
    val onChoisirDossier: () -> Unit,
)

@Composable
private fun PanneauOuvert(
    contexte: Contexte?,
    visible: Boolean,
    locuteur: Boolean,
    faites: Faites,
    donneesReglages: DonneesReglages,
    onRendu: (String, Issue, List<ReponseItem>) -> Unit,
    onEntrainement: (String) -> Unit,
    onFermer: () -> Unit,
) {
    // Attend locuteur, pas seulement visible : sinon le panneau glisse avant que Kokoro n'ait fini son vol (700 ms).
    AnimatedVisibility(
        visible = visible && locuteur && contexte != null,
        enter = slideInVertically(animationSpec = tween(MONTEE_ETAPE_MS)) { hauteur -> hauteur },
        exit = slideOutVertically(animationSpec = tween(MONTEE_ETAPE_MS)) { hauteur -> hauteur },
    ) {
        // contexte vient de affichee, pas de ouverte : ça garde le contenu affiché pendant la descente du panneau.
        when (contexte) {
            is Contexte.Panneau -> PanneauCarte(
                carte = contexte.carte,
                faite = faites.faite(contexte.carte),
                entraine = faites.entrainementMene(contexte.carte),
                reprises = faites.reprises,
                onRendu = { issue, items -> onRendu(contexte.carte.reperes.id, issue, items) },
                onEntrainementMene = { onEntrainement(contexte.carte.reperes.id) },
                onFermer = onFermer,
            )

            Contexte.Reglages -> PanneauReglages(
                autorisations = donneesReglages.autorisations,
                reglages = donneesReglages.reglages,
                dossier = donneesReglages.dossier,
                onRelire = donneesReglages.onRelire,
                onEnregistrer = donneesReglages.onEnregistrer,
                onChoisirDossier = donneesReglages.onChoisirDossier,
                onFermer = onFermer,
            )

            Contexte.Tension -> ContenuTension(onFermer = onFermer)

            Contexte.Phrase -> ContenuPhrase(onFermer = onFermer)

            null -> Unit
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
