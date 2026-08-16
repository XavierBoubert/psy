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

/**
 * La montée d'une étape ouverte. Assez lente pour se voir, assez courte pour ne pas se subir.
 *
 * ⭐ **C'est aussi le délai que l'habitant attend avant de revenir** (`Habitant.kt`, [sortieAnimee])
 * : il ne rentre dans le champ qu'une fois le panneau redescendu.
 */
const val MONTEE_ETAPE_MS = 320

/**
 * Le monde de Kokoro — quatre écrans en anneau, un décor continu, aucun bouton de navigation.
 *
 * ⭐ **On y navigue au doigt et le décor suit le doigt**, au lieu d'attendre qu'il se lève : le
 * geste montre son effet pendant qu'on le fait, donc il n'y a rien à apprendre ni à deviner. C'est
 * la seule façon de rendre quatre écrans découvrables sans jamais rien afficher pour les annoncer —
 * et rien ne les annonce, parce que Kokoro ne vient jamais vers Xavier.
 *
 * ⭐ **La traversée est horizontale, et elle seule** *(15/08/2026)*. Le glissement vertical
 * n'appartient plus au monde : il est rendu au contenu de chaque écran, qui peut donc défiler. Le
 * doigt tranche de lui-même — un mouvement horizontal déplace le monde, un mouvement vertical
 * déplace la liste — **et aucun des deux ne peut plus rater à cause de l'autre.**
 *
 * ⭐ **Elle ne bute nulle part.** La caméra est un nombre qui court sans borne, et l'écran montré
 * est sa position **modulo quatre** : après la crise revient la thérapie, dans le même sens, sans
 * retour en arrière ni saut. **Rien n'indique un bout parce qu'il n'y en a pas.**
 *
 * ⭐ **Le geste ne s'interrompt jamais** *(14/08/2026)*. La caméra est une valeur ordinaire, écrite
 * directement par le doigt — pas une animation à qui l'on demanderait de se déplacer image par
 * image, ce qui coûtait une image de retard à chaque doigt posé. Au lever, un ressort la reprend
 * **à la vitesse qu'elle avait**. Il n'y a donc plus deux mouvements séparés par un arrêt, mais un
 * seul : celui du doigt, prolongé.
 *
 * 🔴 **Le décor ne bouge pas tout seul.** Aucune dérive, aucun nuage qui file, aucune animation
 * d'ambiance : hors la respiration de Kokoro, rien ne se déplace sans que **la main** le déplace
 * (`companion/README.md` §5 — jamais de mouvement à interpréter).
 *
 * ⭐ **Deux mains, une seule caméra de décor** *(15/08/2026, demande de Xavier)*. Le doigt donne la
 * traversée, l'inclinaison du téléphone donne un débattement court **qui s'y ajoute** — et qui ne
 * touche **que** le décor : le contenu des écrans reste à sa place, sans quoi l'écran courant
 * partirait de travers en tenant le téléphone de biais. **Les deux se coupent séparément**
 * (`Parallaxe`).
 */
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

    /**
     * ⭐ **L'ancre ne change qu'au passage d'un écran**, alors que la caméra change à chaque image.
     * Sans elle, lire la caméra pendant la composition recomposerait les quatre écrans soixante fois
     * par seconde — le décalage, lui, se calcule à la mise en page et ne recompose rien.
     */
    val ancre by remember { derivedStateOf { ancreDe(vue.floatValue) } }

    /**
     * 🔴 **L'alternance des deux régimes** (`PRESENCE.md` §1.1) : un panneau ouvert fait sortir
     * l'habitant du champ, et **le locuteur n'entre qu'une fois qu'il en est sorti.**
     *
     * ⭐ **La bascule est lue par [derivedStateOf], pas la sortie elle-même** : celle-ci change à
     * chaque image pendant 420 ms, et la lire ici recomposerait les quatre écrans autant de fois.
     * Le booléen, lui, ne change que deux fois par ouverture.
     */
    val sortie = sortieAnimee(dehors = ouverte != null)
    val locuteur by remember { derivedStateOf { locuteurEnScene(sortie.value) } }

    /**
     * ⭐ **La seconde passe de peinture** (`PRESENCE.md` §1.3, **E13**) : sur l'écran de crise, le
     * corps de Kokoro passe **sous** le bouton *Mot code* et ses bras **dessus**. L'état est publié
     * par la couche du bas et relu par celle du haut — **un seul rig, un seul point, deux passes.**
     */
    val bras = rememberPasseDesBras()
    val entier = rememberEntierAnime()

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

        /**
         * ⭐ **Ce qui se peint ici, désormais, ne concerne que la crise** *(16/08/2026, demande de
         * Xavier)* : ailleurs, [Habitant] ne fait plus que calculer et publier — c'est
         * [HabitantSurInterface], plus bas dans cette couche, qui peint le personnage entier
         * **par-dessus** le contenu des écrans. Seule la crise garde son corps peint ici, sous le
         * bouton *Mot code* ; ses bras viennent d'une seconde passe, plus bas.
         *
         * ⭐ **Il suit l'écran posé, pas la caméra** : c'est le changement de [position] qui le fait
         * transiter, et il part avec son retard sur le décor.
         */
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

        /**
         * ⭐ **Kokoro flotte devant l'interface, partout sauf à la crise** *(demande de Xavier,
         * 16/08/2026)* : le personnage entier, peint par-dessus le contenu des écrans qu'on vient de
         * poser juste au-dessus. Cette couche ne dessine rien à la crise — [Habitant] n'y publie
         * jamais [EtatEntier].
         */
        HabitantSurInterface(entier = entier)

        /**
         * 🔴 **La seconde passe de l'écran de crise** : ses bras, posés sur l'arête du bouton
         * *Mot code*, peints par-dessus le bouton pendant que le corps reste dessous. Partout
         * ailleurs cette couche ne dessine rien — [HabitantSurInterface], juste au-dessus, porte
         * déjà le personnage entier.
         */
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

/**
 * Ce que voit le décor : la traversée du doigt, plus le débattement de l'inclinaison.
 *
 * ⭐ **Elle est lue dans le dessin, pas dans la composition** — comme la caméra du doigt. Une valeur
 * qui change à chaque relevé du capteur recomposerait sinon les quatre écrans cinquante fois par
 * seconde, pour un décor qui n'a besoin que d'être **redessiné**.
 *
 * 🔴 **Parallaxe coupée : le décor se fige à zéro, il ne se fige pas où il était.** Une image fixe
 * doit être la même à chaque venue — sans quoi le réglage rendrait le décor imprévisible au lieu de
 * l'immobiliser.
 */
private fun cameraDuDecor(parallaxe: Parallaxe, vue: Float, inclinaison: Float): Float =
    if (parallaxe.actif) vue + inclinaison else 0f

/**
 * Le seuil sous lequel le ressort considère qu'il est arrivé — **une demi-image de large**.
 *
 * 🔴 **C'est la cause du rattrapage d'un ou deux pixels en fin de traversée.** Sans seuil donné, le
 * ressort prend celui de Compose, `0.01`, appliqué à des unités qui valent **un écran entier** :
 * l'animation s'arrêtait donc à un centième d'écran de sa cible — une dizaine de pixels — et la
 * valeur **sautait** sur la cible d'un coup. Exprimé en fraction d'écran, un demi-pixel vaut
 * `0.5 / largeur`, et le saut passe sous la définition de la dalle.
 */
private fun souffleDuPixel(largeur: Int): Float = 0.5f / largeur.coerceAtLeast(1)

/**
 * Ce qu'il y a dans chaque écran — **une rubrique par écran** (`companion/INTERFACE.md` §3).
 */
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

/**
 * 🧪 Les bascules de test posées sur le monde — **jamais montrées hors build debug**
 * ([io.allonsy.kokoro.BuildConfig.DEBUG]). Elles ne pilotent rien du dossier : elles forcent un
 * affichage pour le comparer à l'écran, avant que **K5** ne branche de vraies données.
 */
data class DebugMonde(
    val documentationVide: Boolean = true,
    val bilanVide: Boolean = true,
    val onBasculerAffichageTherapie: (aujourdhui: Boolean) -> Unit = {},
    val onBasculerDocumentationVide: () -> Unit = {},
    val onBasculerBilanVide: () -> Unit = {},
)

/**
 * L'étape ouverte, posée **au-dessus du monde entier** et non dans son écran : elle prend l'écran
 * complet (§3.1), et tant qu'elle est là **la traversée est coupée** — sans quoi le monde
 * continuerait de glisser derrière un panneau qui le cache.
 *
 * ⭐ **Elle monte en 320 ms et s'arrête.** Un panneau qui apparaît d'un coup se lit comme un
 * changement d'application ; un panneau qui rebondit serait l'animation brusque interdite.
 *
 * 🔴 **[AnimatedVisibility] est composée en permanence, y compris avant la première ouverture**
 * *(15/08/2026)*. Elle ne joue son entrée que sur un **changement** d'état : née visible, elle
 * s'affiche d'un coup. Tant qu'on sortait avant elle faute d'étape à montrer, **la toute première
 * ouverture de la session n'était pas animée** — le panneau apparaissait sec, puis toutes les
 * suivantes montaient normalement. ⚠️ **Le manque était invisible en revue et sautait aux yeux à
 * l'usage**, parce qu'il ne se produit qu'une fois.
 *
 * ⭐ **C'est le contenu qui se garde de l'absence, pas l'animation** : pendant la descente, l'étape
 * est encore là *(`affichee` survit à `ouverte`)*, sinon le panneau se viderait en partant.
 *
 * ⭐ **Le panneau garde toujours sa bande de locuteur, occupée ou non** (**E12**) : [locuteur] ne
 * commande que la parution du personnage, jamais la place. Sans ça le texte se remettrait en page
 * à son arrivée, sous les yeux de qui lit.
 */
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

/**
 * Le geste : la caméra collée au doigt, **sur le seul axe horizontal**.
 *
 * ⭐ **Il n'y a plus d'axe à verrouiller** *(15/08/2026)*. Le monde n'écoute que le glissement
 * horizontal, donc un mouvement vertical ne lui parvient jamais : il va au contenu, qui le prend
 * pour défiler. **Deux gestes, deux destinataires, aucun arbitrage** — et plus aucun geste oblique
 * dont le résultat dépendrait de sa précision.
 *
 * ⭐ **Le geste repart d'où la caméra est**, et non du centre de l'écran de destination : reprendre
 * le monde pendant qu'il se pose ne le fait donc jamais sauter. Un simple appui, lui, ne déclenche
 * rien du tout — il n'y a de geste qu'à partir du seuil de glissement d'Android.
 */
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

/**
 * La vitesse du doigt, retournée en vitesse de caméra — en écrans par seconde.
 *
 * Le signe s'inverse : pousser le doigt vers la droite emmène la caméra vers la gauche. Le plafond
 * n'est pas un confort, c'est ce qui garantit que le ressort ne dépasse pas visiblement sa cible.
 */
private fun elanDe(suivi: VelocityTracker, largeur: Int): Float =
    (-suivi.calculateVelocity().x / largeur).coerceIn(-ELAN_MAX, ELAN_MAX)
