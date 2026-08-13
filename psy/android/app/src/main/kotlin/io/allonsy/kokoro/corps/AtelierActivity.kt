package io.allonsy.kokoro.corps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.ui.ThemeKokoro

/**
 * Atelier du corps — écran de mise au point, hors thérapie.
 *
 * Il sert à voir le rig bouger avant que le jalon K7 ne branche Kokoro sur une surface vue par
 * Xavier. Rien de ce qui est ici n'est un comportement décidé : ce sont des poses qu'on inspecte.
 */
class AtelierActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeKokoro { EcranAtelier() }
        }
    }
}

@Composable
private fun EcranAtelier() {
    var posture by remember { mutableStateOf<Posture>(Posture.Repos) }
    var expressionForcee by remember { mutableStateOf<Expression?>(null) }
    var brasForces by remember { mutableStateOf<Float?>(null) }
    var vol by remember { mutableStateOf(Vol.AUCUN) }
    var paletteClaire by remember { mutableStateOf(false) }

    val reglage = posture.reglage()
    val palette = if (paletteClaire) PALETTE_CLAIRE else PALETTE_SOMBRE
    val rig = rigAnime(
        expression = expressionForcee ?: reglage.expression,
        panneauAllume = expressionForcee != null || reglage.panneauAllume,
        ouvertureBrasGauche = brasForces ?: ouvertureEnVol(reglage.ouvertureBrasGauche, vol),
        ouvertureBrasDroit = brasForces ?: ouvertureEnVol(reglage.ouvertureBrasDroit, vol),
        regard = expressionForcee?.regardParDefaut ?: reglage.regard,
        echelle = reglage.echelle,
        vol = vol,
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.corps_titre),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.corps_sous_titre),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(palette.fond),
            ) {
                CorpsKokoro(rig = rig, modifier = Modifier.fillMaxSize(), palette = palette)
            }

            Section(stringResource(R.string.corps_section_posture))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_posture_repos) to Posture.Repos,
                    stringResource(R.string.corps_posture_present) to Posture.Present,
                    stringResource(R.string.corps_posture_montre_gauche) to Posture.Montre(Cote.GAUCHE),
                    stringResource(R.string.corps_posture_montre_droite) to Posture.Montre(Cote.DROITE),
                    stringResource(R.string.corps_posture_cote_a_cote) to Posture.CoteACote,
                    stringResource(R.string.corps_posture_retrait) to Posture.Retrait,
                ),
                selection = posture,
                onChoix = { posture = it },
            )

            Section(stringResource(R.string.corps_section_expression))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_expression_posture) to null,
                    stringResource(R.string.corps_expression_neutre) to Expression.NEUTRE,
                    stringResource(R.string.corps_expression_attentif) to Expression.ATTENTIF,
                    stringResource(R.string.corps_expression_chaleureux) to Expression.CHALEUREUX,
                    stringResource(R.string.corps_expression_clignement) to Expression.CLIGNEMENT,
                    stringResource(R.string.corps_expression_veille) to Expression.VEILLE,
                    stringResource(R.string.corps_expression_de_cote) to Expression.DE_COTE,
                ),
                selection = expressionForcee,
                onChoix = { expressionForcee = it },
            )

            Section(stringResource(R.string.corps_section_bras))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_bras_posture) to null,
                    stringResource(R.string.corps_bras_0) to 0f,
                    stringResource(R.string.corps_bras_45) to 45f,
                    stringResource(R.string.corps_bras_90) to 90f,
                    stringResource(R.string.corps_bras_135) to 135f,
                    stringResource(R.string.corps_bras_180) to 180f,
                ),
                selection = brasForces,
                onChoix = { brasForces = it },
            )

            Section(stringResource(R.string.corps_section_vol))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_vol_aucun) to Vol.AUCUN,
                    stringResource(R.string.corps_vol_flottement) to Vol.FLOTTEMENT,
                    stringResource(R.string.corps_vol_traversee) to Vol.TRAVERSEE,
                ),
                selection = vol,
                onChoix = { vol = it },
            )

            Section(stringResource(R.string.corps_section_palette))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_palette_sombre) to false,
                    stringResource(R.string.corps_palette_claire) to true,
                ),
                selection = paletteClaire,
                onChoix = { paletteClaire = it },
            )
        }
    }
}

private fun ouvertureEnVol(ouverture: Float, vol: Vol): Float = when (vol) {
    Vol.AUCUN -> ouverture
    else -> maxOf(ouverture, OUVERTURE_VOL)
}

@Composable
private fun <T> LigneChoix(options: List<Pair<String, T>>, selection: T, onChoix: (T) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { (libelle, valeur) ->
            when (valeur) {
                selection -> Button(onClick = { onChoix(valeur) }) { Text(libelle) }
                else -> OutlinedButton(onClick = { onChoix(valeur) }) { Text(libelle) }
            }
        }
    }
}

@Composable
private fun Section(libelle: String) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
    Text(
        text = libelle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}
