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
import io.allonsy.kokoro.MainActivity
import io.allonsy.kokoro.R
import io.allonsy.kokoro.crise.ACTION_MOT_CODE_ENVOYE
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.ECRAN_MOT_CODE
import io.allonsy.kokoro.crise.ECRAN_PHRASE
import io.allonsy.kokoro.crise.ECRAN_TENSION
import io.allonsy.kokoro.crise.EXTRA_ECHEC
import io.allonsy.kokoro.crise.EXTRA_ECRAN
import io.allonsy.kokoro.crise.envoiDirectDisponible
import io.allonsy.kokoro.crise.envoyerMotCode
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.journal.JournalActivity
import io.allonsy.kokoro.reglages.REGLAGES_INITIAUX
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.ThemeMonde

/**
 * Le monde — l'écran où Kokoro habite, et **l'interface principale de l'app** depuis la v2 de
 * `companion/INTERFACE.md`.
 *
 * ⭐ **Bord à bord** : le décor passe sous la barre d'état et sous la barre de navigation. Les
 * surfaces qui portent du texte, elles, respectent les encoches — **le décor ne porte jamais de
 * texte** (**P3**).
 *
 * ⭐ **L'heure est lue à l'arrivée, et à ce moment-là seulement** *(14/08/2026)*. Si la nuit tombe
 * pendant que le monde est ouvert, il ne change pas : Xavier retrouvera la nuit à sa prochaine
 * venue. Un décor qui vire sous les yeux serait un mouvement à interpréter, et le dispositif n'en
 * provoque aucun. **Le thème de l'interface suit la même heure que le décor** — jamais le thème
 * système.
 */
class MondeActivity : ComponentActivity() {
    private val nuit = mutableStateOf(false)
    private val reglages = mutableStateOf(REGLAGES_INITIAUX)
    private val accuse = mutableStateOf<String?>(null)

    /**
     * 🔴 **Le seul retour que l'envoi direct donne.** Un SMS parti n'affiche rien de lui-même : sans
     * cet accusé, l'écran resterait exactement tel qu'avant l'appui, et **rien ne dirait si le
     * message est parti** — le doute conduirait à re-taper.
     */
    private val accuseEnvoi = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
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
        relire()
        setContent {
            ThemeMonde(nuit = nuit.value) {
                MondeKokoro(
                    palette = paletteDuMoment(nuit.value),
                    onFonction = { ouvrir(it) },
                    onReglages = { startActivity(Intent(this, MainActivity::class.java)) },
                    accuse = accuse.value,
                    onAccuseFini = { accuse.value = null },
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
    }

    /**
     * 🔴 **Le monde ne réimplémente aucune fonction de crise** : il ouvre celles qui existent, et
     * qui ont été éprouvées pour de vrai. **Deux portes, un seul contenu** (§6.2).
     *
     * ⭐ **Sauf le mot-code, qui part d'un seul appui** *(15/08/2026, demande de Xavier)* — voir
     * [envoyerLeMotCode].
     */
    private fun ouvrir(fonction: Fonction) {
        when (fonction) {
            Fonction.CHECK_IN -> startActivity(Intent(this, JournalActivity::class.java))
            Fonction.MOT_CODE -> envoyerLeMotCode()
            Fonction.TENSION -> startActivity(intentCrise(ECRAN_TENSION))
            Fonction.PHRASE -> startActivity(intentCrise(ECRAN_PHRASE))
        }
    }

    /**
     * ⭐ **Un appui, le message part.** Il n'y a plus d'écran de confirmation entre le bouton et
     * l'envoi : **demander « es-tu sûr ? » à quelqu'un qui vient de perdre la parole, c'est lui
     * demander un tap de plus au moment précis où il n'en a plus.** Le geste est déjà volontaire —
     * il faut traverser le monde jusqu'à l'écran du bas pour l'atteindre.
     *
     * 🔴 **Les deux cas où l'envoi direct est impossible gardent l'ancien écran** — pas de numéro
     * enregistré, ou autorisation SMS refusée. Il explique, et il propose l'application Messages.
     * **Un bouton qui n'envoie rien en silence serait pire que l'écran de trop.**
     *
     * ⭐ **L'accusé paraît à l'appui, avant même la réponse du réseau**, puis il dit que le message
     * est parti. Le SMS met parfois deux secondes à s'acquitter : sans ce premier mot, l'écran
     * resterait figé assez longtemps pour qu'on re-tape, **et le message partirait deux fois**.
     */
    private fun envoyerLeMotCode() {
        val etat = reglages.value
        if (!etat.contactRenseigne || !envoiDirectDisponible(this)) {
            ouvrirMotCode(echec = false)
            return
        }
        accuse.value = getString(R.string.mot_code_en_cours)
        envoyerMotCode(this, etat.contactNumero, etat.motCode)
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
