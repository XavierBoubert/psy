package io.allonsy.kokoro.monde

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.corps.Expression
import io.allonsy.kokoro.corps.Locuteur
import io.allonsy.kokoro.ui.Croix
import io.allonsy.kokoro.ui.LocalPaletteKokoro
import io.allonsy.kokoro.ui.Teinte
import io.allonsy.kokoro.ui.TypoKokoro

/**
 * Ce que les écrans de bord affichent aujourd'hui.
 *
 * ⚠️ **Le contenu est écrit en dur, et c'est provisoire.** Il reprend fidèlement
 * `companion/inputs/programme.json` v1 — 11 étapes — pour que la matière et le rangement soient
 * regardables sur le téléphone avant que Kokoro sache lire. **K5 remplacera ce fichier par une
 * lecture du dossier synchronisé**, avec le filtrage des sept interdits de
 * `companion/PROGRAMME.md` §7.
 *
 * ⭐ **Les textes vivent dans `strings.xml`, pas ici** : c'est ce fichier que lisent les tests
 * d'invariants. Une phrase écrite en dur dans du Kotlin échapperait au contrôle.
 */

/** Une fonction déjà construite dans Kokoro — les seules valeurs que `type: ecran` peut prendre. */
enum class Fonction { CHECK_IN, MOT_CODE, TENSION, PHRASE }

/** Ce qu'une carte fait quand on la touche. */
sealed interface Ouverture {
    /** Elle ouvre une fonction existante. 🔴 **Un nom d'écran inconnu se refuse**, il ne s'affiche pas. */
    data class Ecran(val fonction: Fonction) : Ouverture

    /** Elle ouvre un détail à lire, en plein écran (`type: demarche`, `type: fiche`). */
    data class Detail(val texte: String) : Ouverture
}

data class Etape(
    val titre: String,
    val ouverture: Ouverture,
    val duree: String? = null,
)

/**
 * Un `quand` et ce qu'il contient. La pancarte porte le `quand`, jamais autre chose.
 *
 * [perchoir] est la bande où l'habitant peut se tenir (`PRESENCE.md` §2) — 🔴 **c'est une place,
 * pas une marque** : rien ne distingue une section qui porte Kokoro d'une section qui ne le porte
 * pas, et il n'y en a jamais qu'une à la fois de toute façon.
 */
data class Section(
    val quand: String,
    val couleur: Teinte,
    val perchoir: Perchoir,
    val etapes: List<Etape>,
)

/**
 * ⏳ **Les deux écrans qui n'ont rien** — la bibliothèque et le bilan restent vides tant que **K5**
 * n'a pas branché la lecture du dossier. C'est ce que l'habitant lit pour s'endormir (§2), et c'est
 * la même vérité que celle qu'affiche leur `CadreVide` : **une seule source, pas deux.**
 */
val ECRANS_VIDES = setOf(Ecran.DOCUMENTATION, Ecran.BILAN)

@Composable
fun sectionsTherapie(): List<Section> {
    val palette = LocalPaletteKokoro.current
    return listOf(
        Section(
            quand = stringResource(R.string.monde_quand_aujourdhui),
            couleur = palette.peche,
            perchoir = Perchoir.AUJOURDHUI,
            etapes = listOf(
                Etape(
                    titre = stringResource(R.string.journal_titre),
                    ouverture = Ouverture.Ecran(Fonction.CHECK_IN),
                    duree = stringResource(R.string.monde_duree_minutes, 2),
                ),
            ),
        ),
        Section(
            quand = stringResource(R.string.monde_quand_sans_date),
            couleur = palette.azur,
            perchoir = Perchoir.SANS_DATE,
            etapes = listOf(
                demarche(R.string.etape_ppc_releve, R.string.etape_ppc_releve_detail),
                demarche(R.string.etape_ppc_origine_fuite, R.string.etape_ppc_origine_fuite_detail),
                demarche(R.string.etape_ppc_interfaces, R.string.etape_ppc_interfaces_detail),
                demarche(R.string.etape_ppc_prise_en_charge, R.string.etape_ppc_prise_en_charge_detail),
                demarche(R.string.etape_ppc_roisman, R.string.etape_ppc_roisman_detail),
                demarche(R.string.etape_ppc_voyage, R.string.etape_ppc_voyage_detail),
                demarche(R.string.etape_email_isorni, R.string.etape_email_isorni_detail),
            ),
        ),
    )
}

@Composable
private fun demarche(titre: Int, detail: Int): Etape =
    Etape(titre = stringResource(titre), ouverture = Ouverture.Detail(stringResource(detail)))

/** Le rayon des coins de la bulle, et la taille de la queue qui pointe vers Kokoro. */
private val BULLE_RAYON = 22.dp
private val BULLE_QUEUE = 20.dp

/** L'assombrissement du monde derrière la bulle — de quoi la détacher, jamais l'effacer. */
private const val OPACITE_SCRIM = 0.28f

/**
 * Une étape ouverte — **une bulle de dialogue, comme dans un RPG** *(16/08/2026, demande de
 * Xavier)*, et elle se ferme d'une croix, jamais d'un geste (`companion/INTERFACE.md` §3.1).
 *
 * ⭐ **La croix est en haut à droite, comme sur tous les panneaux** *(15/08/2026, demande de
 * Xavier)*. Le bouton *Fermer* qui était en pied de page obligeait à descendre une fiche longue pour
 * en sortir ; **la sortie ne dépend plus d'où l'on en est dans la lecture.**
 *
 * ⭐ **Aucune poignée de glissement** : elle promettrait un geste qui n'existe pas, et qui entrerait
 * en concurrence avec la traversée du monde.
 *
 * ⏳ **Le bouton *Fait* n'est pas là**, et c'est délibéré : il écrirait dans `reponses/`, ce que
 * Kokoro ne sait pas encore faire (K5). **Un bouton qui n'écrit rien mentirait** — mieux vaut qu'il
 * manque et que ça se voie.
 *
 * ⭐ **C'est une bulle de discussion** (`PRESENCE.md` §1.1), donc elle porte le locuteur en bas à
 * gauche, avec l'expression `parle` — *Kokoro vient de se poser, la bouche entrouverte.*
 *
 * 🔴 **La bulle s'arrête au-dessus de Kokoro, elle ne l'occupe jamais** *(16/08/2026)* : elle n'est
 * qu'un enfant de plus dans la colonne, posé avant la bande du locuteur — **sa hauteur s'arrête donc
 * mécaniquement là où la bande commence**, sans le moindre calcul de position. Une queue pointe vers
 * lui, en bas à gauche de la bulle, et le monde traversé reste visible, assombri, autour d'elle.
 *
 * 🔴 **Le bas de la page n'est plus dans les marges système**, et c'est ce qui coupe le personnage
 * **au bord de la dalle** plutôt qu'en plein panneau : ce qui manque de lui est hors de l'écran,
 * pas effacé. Le haut et les côtés gardent leurs marges.
 */
@Composable
fun PanneauEtape(titre: String, detail: String, locuteur: Boolean, onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current
    val brushBulle = Brush.verticalGradient(listOf(palette.panneauHaut, palette.panneauBas))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.encre.copy(alpha = OPACITE_SCRIM))
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 14.dp),
        ) {
            Croix(onFermer = onFermer, modifier = Modifier.align(Alignment.CenterEnd))
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 4.dp)
                .drawBehind { dessinerBulle(brushBulle) }
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp, bottom = 24.dp),
        ) {
            Text(
                text = titre,
                style = TypoKokoro.titre,
                color = palette.encre,
                modifier = Modifier.padding(bottom = 20.dp),
            )
            Text(text = detail, style = TypoKokoro.lecture, color = palette.encre)
        }
        Locuteur(expression = Expression.PARLE, present = locuteur)
    }
}

/** La bulle : un rectangle arrondi, et une queue qui pointe vers Kokoro, en bas à gauche. */
private fun DrawScope.dessinerBulle(brush: Brush) {
    drawRoundRect(brush = brush, cornerRadius = CornerRadius(BULLE_RAYON.toPx()))

    val queue = BULLE_QUEUE.toPx()
    val chemin = Path().apply {
        moveTo(queue * 0.6f, size.height - queue * 0.3f)
        lineTo(queue * 0.1f, size.height + queue)
        lineTo(queue * 1.8f, size.height - queue * 0.1f)
        close()
    }
    drawPath(chemin, brush = brush)
}
