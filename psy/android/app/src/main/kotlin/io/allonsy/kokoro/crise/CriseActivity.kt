package io.allonsy.kokoro.crise

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.reglages.MOT_CODE
import io.allonsy.kokoro.reglages.Reglages
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.ui.ThemeKokoro

const val EXTRA_ECRAN = "ecran"
const val ECRAN_MOT_CODE = "mot_code"
const val ECRAN_TENSION = "tension"

sealed interface EcranCrise {
    data object Accueil : EcranCrise
    data object MotCode : EcranCrise
    data object Tension : EcranCrise
}

class CriseActivity : ComponentActivity() {
    private val ecran = mutableStateOf<EcranCrise>(EcranCrise.Accueil)
    private val envoi = mutableStateOf(ResultatEnvoi.INACTIF)
    private val reglages = mutableStateOf(Reglages("", ""))

    private val accuseEnvoi = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            envoi.value = when (resultCode) {
                Activity.RESULT_OK -> ResultatEnvoi.ENVOYE
                else -> ResultatEnvoi.ECHEC
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        ContextCompat.registerReceiver(
            this,
            accuseEnvoi,
            IntentFilter(ACTION_MOT_CODE_ENVOYE),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

        reglages.value = lireReglages(this)
        ecran.value = ecranDemande(intent)

        setContent {
            ThemeKokoro {
                ContenuCrise(
                    ecran = ecran.value,
                    reglages = reglages.value,
                    envoi = envoi.value,
                    onAller = { ecran.value = it },
                    onEnvoyer = { envoyer() },
                    onSecours = { startActivity(intentApplicationSms(reglages.value.contactNumero, MOT_CODE)) },
                    onFermer = { finish() },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        reglages.value = lireReglages(this)
        envoi.value = ResultatEnvoi.INACTIF
        ecran.value = ecranDemande(intent)
    }

    override fun onDestroy() {
        unregisterReceiver(accuseEnvoi)
        super.onDestroy()
    }

    private fun envoyer() {
        envoi.value = ResultatEnvoi.EN_COURS
        envoyerMotCode(this, reglages.value.contactNumero, MOT_CODE)
    }
}

private fun ecranDemande(intent: Intent): EcranCrise = when (intent.getStringExtra(EXTRA_ECRAN)) {
    ECRAN_MOT_CODE -> EcranCrise.MotCode
    ECRAN_TENSION -> EcranCrise.Tension
    else -> EcranCrise.Accueil
}
