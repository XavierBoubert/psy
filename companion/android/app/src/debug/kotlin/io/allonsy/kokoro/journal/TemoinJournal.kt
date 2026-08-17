package io.allonsy.kokoro.journal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val DATE_TEMOIN = "2000-01-01"

// Date volontairement absurde : une date réelle écrite depuis le PC serait une donnée clinique fabriquée.
class TemoinJournal : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("kokoro-temoin", "avant : ${listerJournal(context)}")

        when {
            intent.getBooleanExtra("lister", false) -> Unit
            intent.getBooleanExtra("lire", false) ->
                Log.i("kokoro-temoin", "contenu :\n${lireDuJournal(context, "$DATE_TEMOIN.json")}")
            intent.getBooleanExtra("nettoyer", false) -> {
                val supprimes = supprimerDuJournal(context, "$DATE_TEMOIN.json")
                Log.i("kokoro-temoin", "témoins supprimés : $supprimes")
                Log.i("kokoro-temoin", "apres : ${listerJournal(context)}")
            }
            else -> {
                Log.i("kokoro-temoin", "${ecrireCheckin(context, Checkin.vide(DATE_TEMOIN))}")
                Log.i("kokoro-temoin", "apres : ${listerJournal(context)}")
            }
        }
    }
}
