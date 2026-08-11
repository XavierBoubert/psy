package io.allonsy.kokoro.journal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val DATE_TEMOIN = "2000-01-01"

/**
 * Harnais de vérification, **présent uniquement en build de debug**.
 *
 * Écrit un check-in **témoin** daté du 01/01/2000, tous champs à `null`, pour constater
 * depuis le PC que la chaîne « Kokoro → dossier désigné → transport » fonctionne.
 *
 * ⚠️ La date est volontairement absurde : un check-in daté d'aujourd'hui écrit par le PC
 * serait une **donnée clinique fabriquée**. Le check-in réel appartient à Xavier, et à lui
 * seul — c'est la même règle qu'à K2, où l'envoi réel du mot-code lui revenait.
 */
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
