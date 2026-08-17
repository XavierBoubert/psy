package io.allonsy.kokoro.monde

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.allonsy.kokoro.MainActivity
import io.allonsy.kokoro.R
import io.allonsy.kokoro.crise.ACTION_MOT_CODE_ENVOYE
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.ECRAN_MOT_CODE
import io.allonsy.kokoro.crise.ECRAN_PHRASE
import io.allonsy.kokoro.crise.ECRAN_TENSION
import io.allonsy.kokoro.crise.EXTRA_ECHEC
import io.allonsy.kokoro.crise.EXTRA_ECRAN
import io.allonsy.kokoro.crise.creerCanalAcces
import io.allonsy.kokoro.crise.publierAccesCrise
import io.allonsy.kokoro.crise.tenterMotCode
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.journal.JournalActivity
import io.allonsy.kokoro.journal.checkinDuJourExiste
import io.allonsy.kokoro.journal.jourCourant
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.ThemeMonde
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

// Ne s'affiche jamais par-dessus le verrouillage : c'est CriseActivity qui porte cette déclaration dans le manifeste.
class MondeActivity : ComponentActivity() {
    private val nuit = mutableStateOf(false)
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val accuse = mutableStateOf<String?>(null)
    private val envoiEnCours = mutableStateOf(false)
    private val accesPerdu = mutableStateOf(false)
    private val sejour = mutableStateOf(Sejour(heure = 0, checkinFait = false))

    private val affichageForce = mutableStateOf<Boolean?>(null)
    private val documentationVide = mutableStateOf(true)
    private val bilanVide = mutableStateOf(true)

    private val accuseEnvoi = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            envoiEnCours.value = false
            when (resultCode) {
                Activity.RESULT_OK -> accuse.value =
                    getString(R.string.monde_mot_code_envoye, reglages.value.contactNom)
                else -> ouvrirMotCode(echec = true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ContextCompat.registerReceiver(
            this,
            accuseEnvoi,
            IntentFilter(ACTION_MOT_CODE_ENVOYE),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        creerCanalAcces(this)
        relire()
        setContent {
            ThemeMonde(nuit = nuit.value) {
                MondeKokoro(
                    palette = paletteDuMoment(nuit.value),
                    contactNom = reglages.value.contactNom,
                    sejour = sejour.value.copy(
                        heure = when (affichageForce.value) {
                            null -> sejour.value.heure
                            true -> HEURE_DU_CHECKIN
                            false -> 0
                        },
                        vides = setOfNotNull(
                            Ecran.DOCUMENTATION.takeIf { documentationVide.value },
                            Ecran.BILAN.takeIf { bilanVide.value },
                        ),
                    ),
                    onFonction = { ouvrir(it) },
                    onReglages = { startActivity(Intent(this, MainActivity::class.java)) },
                    parallaxe = reglages.value.parallaxe,
                    envoiEnCours = envoiEnCours.value,
                    accesPerdu = accesPerdu.value,
                    accuse = accuse.value,
                    onAccuseFini = {
                        accuse.value = null
                        envoiEnCours.value = false
                    },
                    debug = DebugMonde(
                        documentationVide = documentationVide.value,
                        bilanVide = bilanVide.value,
                        onBasculerAffichageTherapie = { affichageForce.value = it },
                        onBasculerDocumentationVide = { documentationVide.value = !documentationVide.value },
                        onBasculerBilanVide = { bilanVide.value = !bilanVide.value },
                    ),
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        relire()
    }

    override fun onDestroy() {
        unregisterReceiver(accuseEnvoi)
        super.onDestroy()
    }

    private fun relire() {
        nuit.value = nuitDuMoment(this)
        reglages.value = lireReglages(this)
        accesPerdu.value = !publierAccesCrise(this)
        relireLeSejour()
    }

    // Lu hors fil principal, sans état intermédiaire affiché : le défaut est « pas fait », et ça ne se voit pas.
    private fun relireLeSejour() {
        sejour.value = sejour.value.copy(heure = LocalTime.now().hour)
        lifecycleScope.launch {
            val fait = withContext(Dispatchers.IO) { checkinDuJourExiste(this@MondeActivity, jourCourant()) }
            sejour.value = sejour.value.copy(checkinFait = fait)
        }
    }

    private fun ouvrir(fonction: Fonction) {
        when (fonction) {
            Fonction.CHECK_IN -> startActivity(Intent(this, JournalActivity::class.java))
            Fonction.MOT_CODE -> envoyerLeMotCode()
            Fonction.TENSION -> startActivity(intentCrise(ECRAN_TENSION))
            Fonction.PHRASE -> startActivity(intentCrise(ECRAN_PHRASE))
        }
    }

    // Accusé et grisage posés avant la réponse du réseau : sans ça, un second tap enverrait le message deux fois.
    private fun envoyerLeMotCode() {
        if (!tenterMotCode(this, reglages.value)) {
            ouvrirMotCode(echec = false)
            return
        }
        envoiEnCours.value = true
        accuse.value = getString(R.string.mot_code_en_cours)
    }

    private fun ouvrirMotCode(echec: Boolean) {
        startActivity(intentCrise(ECRAN_MOT_CODE).putExtra(EXTRA_ECHEC, echec))
    }

    private fun intentCrise(ecran: String): Intent =
        Intent(this, CriseActivity::class.java).putExtra(EXTRA_ECRAN, ecran)
}

private fun nuitDuMoment(context: Context): Boolean =
    estNuit(lireReglages(context).nuit, minuteCourante())

private fun paletteDuMoment(nuit: Boolean): PaletteDecor = if (nuit) DECOR_NUIT else DECOR_JOUR
