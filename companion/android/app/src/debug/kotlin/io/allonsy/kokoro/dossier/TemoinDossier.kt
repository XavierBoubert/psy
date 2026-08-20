package io.allonsy.kokoro.dossier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import io.allonsy.kokoro.programme.Issue
import io.allonsy.kokoro.programme.reponseDe

private const val CARTE_TEMOIN = "temoin"

// Carte volontairement inexistante : une réponse écrite depuis le PC au nom d'une vraie carte serait
// une donnée clinique fabriquée. Ce témoin ne vérifie que le chemin d'écriture vers le dossier.
class TemoinDossier : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("kokoro-temoin", "avant : ${listerReponses(context)}")

        if (intent.getBooleanExtra("lister", false)) return

        Log.i("kokoro-temoin", "${ecrireReponse(context, reponseDe(CARTE_TEMOIN, Issue.TERMINE))}")
        Log.i("kokoro-temoin", "apres : ${listerReponses(context)}")
    }
}
