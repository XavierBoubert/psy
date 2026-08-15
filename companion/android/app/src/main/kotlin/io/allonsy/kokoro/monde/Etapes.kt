package io.allonsy.kokoro.monde

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
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

/** Un `quand` et ce qu'il contient. La pancarte porte le `quand`, jamais autre chose. */
data class Section(val quand: String, val couleur: Teinte, val etapes: List<Etape>)

@Composable
fun sectionsTherapie(): List<Section> {
    val palette = LocalPaletteKokoro.current
    return listOf(
        Section(
            quand = stringResource(R.string.monde_quand_aujourdhui),
            couleur = palette.peche,
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

/**
 * Une étape ouverte — **elle prend l'écran entier et se ferme d'une croix, jamais d'un geste**
 * (`companion/INTERFACE.md` §3.1).
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
 */
@Composable
fun PanneauEtape(titre: String, detail: String, onFermer: () -> Unit) {
    val palette = LocalPaletteKokoro.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    Brush.verticalGradient(listOf(palette.panneauHaut, palette.panneauBas)),
                )
            }
            .safeDrawingPadding(),
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp)
                .padding(top = 16.dp, bottom = 28.dp),
        ) {
            Text(
                text = titre,
                style = TypoKokoro.titre,
                color = palette.encre,
                modifier = Modifier.padding(bottom = 20.dp),
            )
            Text(text = detail, style = TypoKokoro.lecture, color = palette.encre)
        }
    }
}
