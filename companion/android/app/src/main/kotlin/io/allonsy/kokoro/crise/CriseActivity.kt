package io.allonsy.kokoro.crise

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.R
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.monde.SceneDeCrise
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.Accuse
import io.allonsy.kokoro.ui.LocalPanneauPorte
import io.allonsy.kokoro.ui.ThemeMonde

// Seul le mot-code s'ouvre depuis l'extérieur : la notification ouvre la scène de crise, et tout le reste s'y touche.
const val EXTRA_ECRAN = "ecran"
const val ECRAN_MOT_CODE = "mot_code"
const val EXTRA_ECHEC = "echec"

sealed interface EcranCrise {
    data object Accueil : EcranCrise
    data object MotCode : EcranCrise
    data object Tension : EcranCrise
    data object Phrase : EcranCrise
}

class CriseActivity : ComponentActivity() {
    private val ecran = mutableStateOf<EcranCrise>(EcranCrise.Accueil)
    private val envoi = mutableStateOf(ResultatEnvoi.INACTIF)
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val nuit = mutableStateOf(false)
    private val accuse = mutableStateOf<String?>(null)

    private val accuseEnvoi = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    envoi.value = ResultatEnvoi.ENVOYE
                    accuse.value = getString(R.string.monde_mot_code_envoye, reglages.value.contactNom)
                }
                else -> {
                    envoi.value = ResultatEnvoi.ECHEC
                    accuse.value = null
                    ecran.value = EcranCrise.MotCode
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()

        ContextCompat.registerReceiver(
            this,
            accuseEnvoi,
            IntentFilter(ACTION_MOT_CODE_ENVOYE),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

        relire(intent)

        setContent {
            ThemeMonde(nuit = nuit.value) {
                // Aucune scène ne porte les panneaux ici : ni queue de bulle, ni Kokoro dessous.
                CompositionLocalProvider(LocalPanneauPorte provides false) {
                    BackHandler(enabled = ecran.value != EcranCrise.Accueil) { retour() }

                    Box(modifier = Modifier.fillMaxSize()) {
                        SceneDeCrise(
                            palette = if (nuit.value) DECOR_NUIT else DECOR_JOUR,
                            contactNom = reglages.value.contactNom,
                            envoiEnCours = envoi.value == ResultatEnvoi.EN_COURS,
                            onPorteDeCrise = { ouvrir(it) },
                            onFermer = { finish() },
                        )
                        ContenuCrise(
                            ecran = ecran.value,
                            reglages = reglages.value,
                            envoi = envoi.value,
                            onEnvoyer = { envoyer() },
                            onSecours = { startActivity(intentSecours()) },
                            onFermer = { retour() },
                        )
                        Accuse(
                            texte = accuse.value,
                            onFini = {
                                accuse.value = null
                                if (envoi.value == ResultatEnvoi.EN_COURS) {
                                    envoi.value = ResultatEnvoi.INACTIF
                                }
                            },
                            modifier = Modifier.align(Alignment.BottomCenter),
                        )
                    }
                }
            }
        }
    }

    // Fermer un panneau revient à la scène de crise, jamais à l'écran verrouillé : elle est derrière, comme au monde.
    private fun retour() {
        ecran.value = EcranCrise.Accueil
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        relire(intent)
    }

    private fun relire(depuis: Intent) {
        reglages.value = lireReglages(this)
        nuit.value = estNuit(reglages.value.nuit, minuteCourante())
        envoi.value = when {
            depuis.getBooleanExtra(EXTRA_ECHEC, false) -> ResultatEnvoi.ECHEC
            else -> ResultatEnvoi.INACTIF
        }
        accuse.value = null
        ecran.value = ecranDemande(depuis)
    }

    private fun ouvrir(porte: PorteDeCrise) {
        when (porte) {
            PorteDeCrise.MOT_CODE -> envoyerLeMotCode()
            PorteDeCrise.TENSION -> ecran.value = EcranCrise.Tension
            PorteDeCrise.PHRASE -> ecran.value = EcranCrise.Phrase
        }
    }

    private fun envoyerLeMotCode() {
        if (!tenterMotCode(this, reglages.value)) {
            ecran.value = EcranCrise.MotCode
            return
        }
        envoi.value = ResultatEnvoi.EN_COURS
        accuse.value = getString(R.string.mot_code_en_cours)
    }

    private fun intentSecours(): Intent =
        intentApplicationSms(reglages.value.contactNumero, reglages.value.motCode)

    override fun onDestroy() {
        unregisterReceiver(accuseEnvoi)
        super.onDestroy()
    }

    private fun envoyer() {
        envoi.value = ResultatEnvoi.EN_COURS
        envoyerMotCode(this, reglages.value.contactNumero, reglages.value.motCode)
    }
}

private fun ecranDemande(intent: Intent): EcranCrise = when (intent.getStringExtra(EXTRA_ECRAN)) {
    ECRAN_MOT_CODE -> EcranCrise.MotCode
    else -> EcranCrise.Accueil
}
