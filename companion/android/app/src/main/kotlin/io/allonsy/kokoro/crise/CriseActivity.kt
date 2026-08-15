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
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.R
import io.allonsy.kokoro.journal.JournalActivity
import io.allonsy.kokoro.monde.Fonction
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.Accuse
import io.allonsy.kokoro.ui.ThemeMonde

const val EXTRA_ECRAN = "ecran"
const val ECRAN_MOT_CODE = "mot_code"
const val ECRAN_TENSION = "tension"

/**
 * ⭐ **L'écran s'ouvre en sachant que l'envoi direct vient d'échouer** *(15/08/2026)*. Le mot-code
 * part d'un seul appui ; quand il ne part pas, c'est ici qu'on arrive, et **il faut le dire tout de
 * suite** plutôt que de rejouer l'écran comme si rien ne s'était passé.
 */
const val EXTRA_ECHEC = "echec"

/**
 * ⭐ **La phrase pour le soignant est une porte à part entière** *(15/08/2026)*. Elle n'était
 * atteignable que depuis la tension appliquée ; l'écran **Crise** la propose directement, comme
 * `companion/inputs/programme.json` l'annonce déjà (`ecran: phrase-soignant`).
 */
const val ECRAN_PHRASE = "phrase"

sealed interface EcranCrise {
    data object Accueil : EcranCrise
    data object MotCode : EcranCrise
    data object Tension : EcranCrise
    data object Phrase : EcranCrise
}

/**
 * 🔴 **La deuxième porte de la crise — le même contenu que l'écran **BAS** du monde**
 * (`companion/INTERFACE.md` §6.2). Depuis le 15/08/2026 elle affiche littéralement les mêmes boutons
 * *(demande de Xavier)* : le [PortesDeCrise] du monde, aux mêmes gestes, **mot-code compris — un
 * appui, le message part**.
 *
 * ⚠️ **Elle reste le repli écrit d'avance** : c'est elle qui s'ouvre par-dessus le verrouillage
 * quand le monde n'y arriverait pas, et c'est ici qu'on atterrit quand l'envoi direct échoue.
 */
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
                Box(modifier = Modifier.fillMaxSize()) {
                    ContenuCrise(
                        ecran = ecran.value,
                        reglages = reglages.value,
                        envoi = envoi.value,
                        onFonction = { ouvrir(it) },
                        onEnvoyer = { envoyer() },
                        onSecours = { startActivity(intentSecours()) },
                        onFermer = { finish() },
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

    /**
     * 🔴 **Les mêmes gestes que dans le monde, à la lettre** — c'est la définition de « deux portes,
     * un seul contenu ». Le mot-code part d'un appui, la tension et la phrase ouvrent leur écran.
     */
    private fun ouvrir(fonction: Fonction) {
        when (fonction) {
            Fonction.CHECK_IN -> startActivity(Intent(this, JournalActivity::class.java))
            Fonction.MOT_CODE -> envoyerLeMotCode()
            Fonction.TENSION -> ecran.value = EcranCrise.Tension
            Fonction.PHRASE -> ecran.value = EcranCrise.Phrase
        }
    }

    /**
     * ⭐ **L'accusé paraît à l'appui, avant même la réponse du réseau**, puis il dit que le message
     * est parti. Le SMS met parfois deux secondes à s'acquitter : sans ce premier mot, l'écran
     * resterait figé assez longtemps pour qu'on re-tape. **Et le bouton se grise pendant ce
     * temps-là** — les deux vont ensemble, l'un dit ce qui se passe, l'autre empêche le doublon.
     */
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
    ECRAN_TENSION -> EcranCrise.Tension
    ECRAN_PHRASE -> EcranCrise.Phrase
    else -> EcranCrise.Accueil
}
