package io.allonsy.kokoro.crise

import android.os.SystemClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.allonsy.kokoro.R
import io.allonsy.kokoro.reglages.MOT_CODE
import io.allonsy.kokoro.reglages.Reglages
import io.allonsy.kokoro.tension.NOMBRE_CYCLES
import io.allonsy.kokoro.tension.PhaseTension
import io.allonsy.kokoro.tension.SECONDES_BLOC
import io.allonsy.kokoro.tension.etatTension
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val FORMAT_HEURE: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private const val PERIODE_TICK_MILLIS = 200L

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
                EcranCrise.Tension -> EcranTension(onFermer = onFermer)
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

@Composable
private fun EcranTension(onFermer: () -> Unit) {
    var debut by remember { mutableLongStateOf(0L) }
    var secondes by remember { mutableIntStateOf(0) }
    val demarre = debut > 0L

    LaunchedEffect(debut) {
        if (debut == 0L) return@LaunchedEffect
        while (true) {
            secondes = ((SystemClock.elapsedRealtime() - debut) / 1000L).toInt()
            if (secondes >= SECONDES_BLOC) return@LaunchedEffect
            delay(PERIODE_TICK_MILLIS)
        }
    }

    if (!demarre) {
        Text(
            text = stringResource(R.string.tension_titre),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.tension_geste),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        GrandBouton(
            libelle = stringResource(R.string.tension_action_demarrer),
            repere = stringResource(R.string.tension_repere_demarrer),
            onClick = { debut = SystemClock.elapsedRealtime() },
        )
        Fermer(onFermer)
        return
    }

    val etat = etatTension(secondes)
    Text(
        text = stringResource(
            when (etat.phase) {
                PhaseTension.CONTRACTE -> R.string.tension_phase_contracte
                PhaseTension.RELACHE -> R.string.tension_phase_relache
                PhaseTension.TERMINE -> R.string.tension_phase_termine
            },
        ),
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Text(
        text = stringResource(
            when (etat.phase) {
                PhaseTension.CONTRACTE -> R.string.tension_consigne_contracte
                PhaseTension.RELACHE -> R.string.tension_consigne_relache
                PhaseTension.TERMINE -> R.string.tension_consigne_termine
            },
        ),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    if (etat.phase != PhaseTension.TERMINE) {
        Text(
            text = etat.secondesRestantes.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(R.string.tension_cycle, etat.cycle, NOMBRE_CYCLES),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    OutlinedButton(
        onClick = {
            debut = 0L
            secondes = 0
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            stringResource(
                when (etat.phase) {
                    PhaseTension.TERMINE -> R.string.tension_action_recommencer
                    else -> R.string.tension_action_arreter
                },
            ),
        )
    }
    Fermer(onFermer)
}

@Composable
private fun GrandBouton(libelle: String, repere: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 104.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = libelle,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = repere,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun Fermer(onFermer: () -> Unit) {
    TextButton(onClick = onFermer, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.crise_fermer))
    }
}
