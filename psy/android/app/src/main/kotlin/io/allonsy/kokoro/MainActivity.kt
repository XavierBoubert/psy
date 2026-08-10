package io.allonsy.kokoro

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import io.allonsy.kokoro.alerte.creerCanalAlerte
import io.allonsy.kokoro.alerte.programmerAlerteTest
import io.allonsy.kokoro.ui.ThemeKokoro

private const val DELAI_TEST_MILLIS = 20_000L

data class EtatAutorisations(
    val notificationsAutorisees: Boolean,
    val pleinEcranAutorise: Boolean,
)

class MainActivity : ComponentActivity() {
    private val etat = mutableStateOf(EtatAutorisations(false, false))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creerCanalAlerte(this)
        etat.value = lireAutorisations(this)
        setContent {
            ThemeKokoro {
                EcranControle(
                    etat = etat.value,
                    onRelire = { etat.value = lireAutorisations(this) },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        etat.value = lireAutorisations(this)
    }
}

private fun lireAutorisations(context: Context): EtatAutorisations {
    val gestionnaire = context.getSystemService(NotificationManager::class.java)
    val pleinEcran = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        gestionnaire.canUseFullScreenIntent()
    } else {
        true
    }
    return EtatAutorisations(
        notificationsAutorisees = NotificationManagerCompat.from(context).areNotificationsEnabled(),
        pleinEcranAutorise = pleinEcran,
    )
}

private fun ouvrirReglagePleinEcran(context: Context) {
    val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT
    } else {
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    }
    context.startActivity(
        Intent(action, Uri.fromParts("package", context.packageName, null))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )
}

private fun ouvrirReglageNotifications(context: Context) {
    context.startActivity(
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
    )
}

@Composable
private fun EcranControle(etat: EtatAutorisations, onRelire: () -> Unit) {
    val context = LocalContext.current
    val demandeNotifications = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { onRelire() },
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = stringResource(R.string.controle_titre),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.controle_sous_titre),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            LigneEtat(
                libelle = stringResource(R.string.controle_etat_notifications),
                accorde = etat.notificationsAutorisees,
            )
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_plein_ecran),
                accorde = etat.pleinEcranAutorise,
            )

            if (!etat.notificationsAutorisees) {
                OutlinedButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            demandeNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            ouvrirReglageNotifications(context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.controle_action_notifications))
                }
            }

            if (!etat.pleinEcranAutorise) {
                Text(
                    text = stringResource(R.string.controle_guidage_plein_ecran),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedButton(
                    onClick = { ouvrirReglagePleinEcran(context) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.controle_action_plein_ecran))
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Text(
                text = stringResource(R.string.controle_consigne_test),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(
                onClick = { programmerAlerteTest(context, DELAI_TEST_MILLIS) },
                enabled = etat.notificationsAutorisees,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.controle_action_test))
            }
        }
    }
}

@Composable
private fun LigneEtat(libelle: String, accorde: Boolean) {
    val marque = stringResource(
        if (accorde) R.string.controle_accorde else R.string.controle_refuse,
    )
    Text(
        text = "$libelle : $marque",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
}
