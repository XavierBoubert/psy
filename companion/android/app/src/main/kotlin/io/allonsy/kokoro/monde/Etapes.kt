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

// Contenu écrit en dur, reprend companion/inputs/programme.json v1 ; K5 le remplacera par une lecture du dossier.
enum class Fonction { CHECK_IN, MOT_CODE, TENSION, PHRASE }

sealed interface Ouverture {
    data class Ecran(val fonction: Fonction) : Ouverture

    data class Detail(val texte: String) : Ouverture
}

data class Etape(
    val titre: String,
    val ouverture: Ouverture,
    val duree: String? = null,
)

data class Section(
    val quand: String,
    val couleur: Teinte,
    val perchoir: Perchoir,
    val etapes: List<Etape>,
)

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

private val BULLE_RAYON = 22.dp
private val BULLE_QUEUE = 20.dp

private const val OPACITE_SCRIM = 0.28f

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
