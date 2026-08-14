package io.allonsy.kokoro.monde

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import io.allonsy.kokoro.MainActivity
import io.allonsy.kokoro.crise.CriseActivity
import io.allonsy.kokoro.crise.ECRAN_MOT_CODE
import io.allonsy.kokoro.crise.ECRAN_PHRASE
import io.allonsy.kokoro.crise.ECRAN_TENSION
import io.allonsy.kokoro.crise.EXTRA_ECRAN
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.journal.JournalActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        nuit.value = nuitDuMoment(this)
        setContent {
            ThemeMonde(nuit = nuit.value) {
                MondeKokoro(
                    palette = paletteDuMoment(nuit.value),
                    onFonction = { ouvrir(it) },
                    onReglages = { startActivity(Intent(this, MainActivity::class.java)) },
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nuit.value = nuitDuMoment(this)
    }

    /**
     * 🔴 **Le monde ne réimplémente aucune fonction de crise** : il ouvre celles qui existent, et
     * qui ont été éprouvées pour de vrai. **Deux portes, un seul contenu** (§6.2).
     */
    private fun ouvrir(fonction: Fonction) {
        val intent = when (fonction) {
            Fonction.CHECK_IN -> Intent(this, JournalActivity::class.java)
            Fonction.MOT_CODE -> intentCrise(ECRAN_MOT_CODE)
            Fonction.TENSION -> intentCrise(ECRAN_TENSION)
            Fonction.PHRASE -> intentCrise(ECRAN_PHRASE)
        }
        startActivity(intent)
    }

    private fun intentCrise(ecran: String): Intent =
        Intent(this, CriseActivity::class.java).putExtra(EXTRA_ECRAN, ecran)
}

private fun nuitDuMoment(context: Context): Boolean =
    estNuit(lireReglages(context).nuit, minuteCourante())

private fun paletteDuMoment(nuit: Boolean): PaletteDecor = if (nuit) DECOR_NUIT else DECOR_JOUR
