package io.allonsy.kokoro.monde

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import io.allonsy.kokoro.decor.DECOR_JOUR
import io.allonsy.kokoro.decor.DECOR_NUIT
import io.allonsy.kokoro.decor.PaletteDecor
import io.allonsy.kokoro.reglages.estNuit
import io.allonsy.kokoro.reglages.lireReglages
import io.allonsy.kokoro.reglages.minuteCourante
import io.allonsy.kokoro.ui.ThemeKokoro

/**
 * Le monde — l'écran où Kokoro habite.
 *
 * Il ne porte encore aucune thérapie : c'est le décor et la navigation, rien d'autre. Ce qu'on y
 * posera arrive avec K5 et K6, et se décide en séance.
 *
 * ⭐ **Bord à bord** : le décor passe sous la barre d'état et sous la barre de navigation. Le monde
 * n'a aucun texte à protéger d'une encoche, et un bandeau opaque en haut le couperait en deux.
 *
 * ⭐ **L'heure est lue à l'arrivée, et à ce moment-là seulement** *(14/08/2026)*. Si la nuit tombe
 * pendant que le monde est ouvert, il ne change pas : Xavier retrouvera la nuit à sa prochaine
 * venue. Un décor qui vire sous les yeux serait un mouvement à interpréter, et le dispositif n'en
 * provoque aucun.
 */
class MondeActivity : ComponentActivity() {
    private val palette = mutableStateOf(DECOR_JOUR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        palette.value = paletteDuMoment(this)
        setContent {
            ThemeKokoro {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MondeKokoro(palette = palette.value)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        palette.value = paletteDuMoment(this)
    }
}

private fun paletteDuMoment(context: Context): PaletteDecor =
    when {
        estNuit(lireReglages(context).nuit, minuteCourante()) -> DECOR_NUIT
        else -> DECOR_JOUR
    }
