package io.allonsy.kokoro.crise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.reglages.MOT_CODE
import io.allonsy.kokoro.reglages.Reglages
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val FORMAT_HEURE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun ContenuCrise(
    ecran: EcranCrise,
    reglages: Reglages,
    envoi: ResultatEnvoi,
    onAller: (EcranCrise) -> Unit,
    onEnvoyer: () -> Unit,
    onSecours: () -> Unit,
    onFermer: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        ) {
            when (ecran) {
                EcranCrise.Accueil -> EcranAccueil(
                    reglages = reglages,
                    onAller = onAller,
                    onFermer = onFermer,
                )
                EcranCrise.MotCode -> EcranMotCode(
                    reglages = reglages,
                    envoi = envoi,
                    onEnvoyer = onEnvoyer,
                    onSecours = onSecours,
                    onFermer = onFermer,
                )
                EcranCrise.Tension -> ContenuTension(onFermer = onFermer)
                EcranCrise.Phrase -> ContenuTension(onFermer = onFermer, ouvrirSurLaPhrase = true)
            }
        }
    }
}

@Composable
private fun EcranAccueil(
    reglages: Reglages,
    onAller: (EcranCrise) -> Unit,
    onFermer: () -> Unit,
) {
    GrandBouton(
        libelle = stringResource(R.string.crise_bouton_mot_code, reglages.contactNom),
        repere = stringResource(R.string.crise_repere_mot_code),
        onClick = { onAller(EcranCrise.MotCode) },
    )
    GrandBouton(
        libelle = stringResource(R.string.crise_bouton_tension),
        repere = stringResource(R.string.crise_repere_tension),
        onClick = { onAller(EcranCrise.Tension) },
    )
    Fermer(onFermer)
}

@Composable
private fun EcranMotCode(
    reglages: Reglages,
    envoi: ResultatEnvoi,
    onEnvoyer: () -> Unit,
    onSecours: () -> Unit,
    onFermer: () -> Unit,
) {
    val context = LocalContext.current
    val direct = remember { envoiDirectDisponible(context) }
    val heure = remember(envoi) { LocalTime.now().format(FORMAT_HEURE) }

    Text(
        text = stringResource(R.string.mot_code_destinataire, reglages.contactNom),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
        text = MOT_CODE,
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onBackground,
    )

    when {
        !reglages.contactRenseigne -> Text(
            text = stringResource(R.string.mot_code_sans_numero),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        envoi == ResultatEnvoi.ENVOYE -> Text(
            text = stringResource(R.string.mot_code_envoye, heure),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        envoi == ResultatEnvoi.EN_COURS -> Text(
            text = stringResource(R.string.mot_code_en_cours),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        direct -> GrandBouton(
            libelle = stringResource(R.string.mot_code_action_envoyer),
            repere = stringResource(R.string.mot_code_repere_envoyer),
            onClick = onEnvoyer,
        )

        else -> GrandBouton(
            libelle = stringResource(R.string.mot_code_action_messages),
            repere = stringResource(R.string.mot_code_repere_messages),
            onClick = onSecours,
        )
    }

    if (envoi == ResultatEnvoi.ECHEC) {
        Text(
            text = stringResource(R.string.mot_code_echec),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedButton(onClick = onSecours, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.mot_code_action_messages))
        }
    }

    Fermer(onFermer)
}
