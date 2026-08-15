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
    var piedsForces by remember { mutableStateOf(0f) }
    var regardForce by remember { mutableStateOf<Float?>(null) }
    var balayage by remember { mutableStateOf<Balayage?>(null) }
    var vol by remember { mutableStateOf(Vol.AUCUN) }
    var paletteClaire by remember { mutableStateOf(false) }

    val reglage = posture.reglage()
    val palette = if (paletteClaire) PALETTE_CLAIRE else PALETTE_SOMBRE
    val rig = rigAnime(
        expression = expressionForcee ?: reglage.expression,
        panneauAllume = expressionForcee != null || reglage.panneauAllume,
        ouvertureBrasGauche = brasForces ?: ouvertureEnVol(reglage.ouvertureBrasGauche, vol),
        ouvertureBrasDroit = brasForces ?: ouvertureEnVol(reglage.ouvertureBrasDroit, vol),
        orbitePiedGauche = piedsForces,
        orbitePiedDroit = piedsForces,
        regard = regardForce ?: reglage.regard,
        abaissement = reglage.abaissement,
        balayage = balayage,
        ecriture = if (brasForces == null) reglage.ecriture else null,
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
                    stringResource(R.string.corps_posture_pensif) to Posture.Pensif,
                    stringResource(R.string.corps_posture_lecture) to Posture.Lecture,
                    stringResource(R.string.corps_posture_notes) to Posture.Notes,
                    stringResource(R.string.corps_posture_attente) to Posture.Attente,
                    stringResource(R.string.corps_posture_sommeil) to Posture.Sommeil,
                ),
                selection = posture,
                onChoix = { posture = it },
            )

            Section(stringResource(R.string.corps_section_expression))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_expression_posture) to null,
                    stringResource(R.string.corps_expression_neutre) to Expression.NEUTRE,
                    stringResource(R.string.corps_expression_serein) to Expression.SEREIN,
                    stringResource(R.string.corps_expression_attentif) to Expression.ATTENTIF,
                    stringResource(R.string.corps_expression_chaleureux) to Expression.CHALEUREUX,
                    stringResource(R.string.corps_expression_clignement) to Expression.CLIGNEMENT,
                    stringResource(R.string.corps_expression_veille) to Expression.VEILLE,
                ),
                selection = expressionForcee,
                onChoix = { expressionForcee = it },
            )

            Section(stringResource(R.string.corps_section_regard))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_regard_posture) to null,
                    stringResource(R.string.corps_regard_gauche) to -REGARD_DESIGNATION,
                    stringResource(R.string.corps_regard_centre) to 0f,
                    stringResource(R.string.corps_regard_droite) to REGARD_DESIGNATION,
                ),
                selection = regardForce,
                onChoix = { regardForce = it },
            )

            Section(stringResource(R.string.corps_section_balayage))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_balayage_aucun) to null,
                    stringResource(R.string.corps_balayage_lecture) to Balayage(),
                    stringResource(R.string.corps_balayage_large) to
                        Balayage(amplitude = 2f * REGARD_LECTURE),
                    stringResource(R.string.corps_balayage_lente) to
                        Balayage(ligneMillis = 2 * BALAYAGE_LIGNE_MILLIS),
                ),
                selection = balayage,
                onChoix = { balayage = it },
            )

            Section(stringResource(R.string.corps_section_bras))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_bras_posture) to null,
                    stringResource(R.string.corps_bras_repos) to OUVERTURE_REPOS,
                    stringResource(R.string.corps_bras_mi_chemin) to OUVERTURE_HORIZONTALE / 2f,
                    stringResource(R.string.corps_bras_horizontale) to OUVERTURE_HORIZONTALE,
                    stringResource(R.string.corps_bras_repli) to -INCLINAISON_REPOS,
                ),
                selection = brasForces,
                onChoix = { brasForces = it },
            )

            Section(stringResource(R.string.corps_section_pieds))
            LigneChoix(
                options = listOf(
                    stringResource(R.string.corps_pieds_dessin) to 0f,
                    stringResource(R.string.corps_pieds_serres) to -6f,
                    stringResource(R.string.corps_pieds_ecartes) to 8f,
                ),
                selection = piedsForces,
                onChoix = { piedsForces = it },
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
