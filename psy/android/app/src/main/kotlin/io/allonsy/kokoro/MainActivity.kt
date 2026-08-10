package io.allonsy.kokoro

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.alerte.creerCanalAlerte
import io.allonsy.kokoro.alerte.programmerAlerteTest
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.creerCanalAcces
import io.allonsy.kokoro.crise.publierAccesCrise
import io.allonsy.kokoro.reglages.MOT_CODE
import io.allonsy.kokoro.reglages.Reglages
import io.allonsy.kokoro.reglages.ecrireReglages
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.ui.ThemeKokoro

private const val DELAI_TEST_MILLIS = 20_000L

data class EtatAutorisations(
    val notificationsAutorisees: Boolean,
    val pleinEcranAutorise: Boolean,
    val smsAutorise: Boolean,
)

class MainActivity : ComponentActivity() {
    private val autorisations = mutableStateOf(EtatAutorisations(false, false, false))
    private val reglages = mutableStateOf(Reglages("", ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        creerCanalAlerte(this)
        creerCanalAcces(this)
        relire()
        publierAccesCrise(this)
        setContent {
            ThemeKokoro {
                EcranControle(
                    autorisations = autorisations.value,
                    reglages = reglages.value,
                    onRelire = { relire() },
                    onEnregistrer = {
                        ecrireReglages(this, it)
                        relire()
                    },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        relire()
        publierAccesCrise(this)
    }

    private fun relire() {
        autorisations.value = lireAutorisations(this)
        reglages.value = lireReglages(this)
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
        smsAutorise = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) ==
            PackageManager.PERMISSION_GRANTED,
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
private fun EcranControle(
    autorisations: EtatAutorisations,
    reglages: Reglages,
    onRelire: () -> Unit,
    onEnregistrer: (Reglages) -> Unit,
) {
    val context = LocalContext.current
    val demandeNotifications = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { onRelire() },
    )
    val demandeSms = rememberLauncherForActivityResult(
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
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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

            Section(stringResource(R.string.controle_section_contact))
            ChampsContact(reglages = reglages, onEnregistrer = onEnregistrer)
            Text(
                text = stringResource(R.string.controle_mot_code, MOT_CODE),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Section(stringResource(R.string.controle_section_autorisations))
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_notifications),
                accorde = autorisations.notificationsAutorisees,
            )
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_plein_ecran),
                accorde = autorisations.pleinEcranAutorise,
            )
            LigneEtat(
                libelle = stringResource(R.string.controle_etat_sms),
                accorde = autorisations.smsAutorise,
            )

            if (!autorisations.notificationsAutorisees) {
                Action(stringResource(R.string.controle_action_notifications)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        demandeNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        ouvrirReglageNotifications(context)
                    }
                }
            }

            if (!autorisations.pleinEcranAutorise) {
                Explication(stringResource(R.string.controle_guidage_plein_ecran))
                Action(stringResource(R.string.controle_action_plein_ecran)) {
                    ouvrirReglagePleinEcran(context)
                }
            }

            if (!autorisations.smsAutorise) {
                Explication(stringResource(R.string.controle_guidage_sms))
                Action(stringResource(R.string.controle_action_sms)) {
                    demandeSms.launch(Manifest.permission.SEND_SMS)
                }
            }

            Section(stringResource(R.string.controle_section_acces))
            Explication(stringResource(R.string.controle_acces_explication))
            Action(stringResource(R.string.controle_action_acces)) { publierAccesCrise(context) }
            Action(stringResource(R.string.controle_action_ouvrir_crise)) {
                context.startActivity(Intent(context, CriseActivity::class.java))
            }

            Section(stringResource(R.string.controle_section_test))
            Explication(stringResource(R.string.controle_consigne_test))
            Action(
                libelle = stringResource(R.string.controle_action_test),
                actif = autorisations.notificationsAutorisees,
            ) {
                programmerAlerteTest(context, DELAI_TEST_MILLIS)
            }
        }
    }
}

@Composable
private fun ChampsContact(reglages: Reglages, onEnregistrer: (Reglages) -> Unit) {
    var nom by remember(reglages) { mutableStateOf(reglages.contactNom) }
    var numero by remember(reglages) { mutableStateOf(reglages.contactNumero) }

    Text(
        text = when {
            reglages.contactRenseigne -> stringResource(
                R.string.controle_contact_enregistre,
                reglages.contactNom,
                reglages.contactNumero,
            )
            else -> stringResource(R.string.controle_contact_absent)
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
    OutlinedTextField(
        value = nom,
        onValueChange = { nom = it },
        label = { Text(stringResource(R.string.controle_champ_nom)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = numero,
        onValueChange = { numero = it },
        label = { Text(stringResource(R.string.controle_champ_numero)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth(),
    )
    Action(stringResource(R.string.controle_action_enregistrer)) {
        onEnregistrer(Reglages(contactNom = nom, contactNumero = numero))
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

@Composable
private fun Explication(texte: String) {
    Text(
        text = texte,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun Action(libelle: String, actif: Boolean = true, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        enabled = actif,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(libelle)
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
