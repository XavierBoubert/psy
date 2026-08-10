package io.allonsy.kokoro.crise

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.allonsy.kokoro.R
import io.allonsy.kokoro.alerte.ID_CANAL_ALERTE
import io.allonsy.kokoro.alerte.creerCanalAlerte

private const val ETIQUETTE_REVEIL = "kokoro:crise-debug"
private const val DUREE_REVEIL_MILLIS = 10_000L
private const val ID_NOTIFICATION_DEBUG = 3
private const val CODE_DEBUG = 30

/**
 * Harnais de vérification, **présent uniquement en build de debug**.
 * Ouvre l'écran de crise par-dessus le verrouillage sans passer par un tap manuel,
 * pour que la chaîne « écran éteint → écran de crise » soit constatable depuis le PC.
 */
class DeclencheurCrise : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        reveillerEcran(context)
        creerCanalAlerte(context)

        val destination = PendingIntent.getActivity(
            context,
            CODE_DEBUG,
            Intent(context, CriseActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(EXTRA_ECRAN, intent.getStringExtra(EXTRA_ECRAN)),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, ID_CANAL_ALERTE)
            .setSmallIcon(R.drawable.ic_kokoro)
            .setContentTitle(context.getString(R.string.acces_titre))
            .setContentText(context.getString(R.string.acces_texte))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setDefaults(0)
            .setSound(null)
            .setVibrate(null)
            .setAutoCancel(true)
            .setContentIntent(destination)
            .setFullScreenIntent(destination, true)
            .build()

        NotificationManagerCompat.from(context).notify(ID_NOTIFICATION_DEBUG, notification)
    }
}

@Suppress("DEPRECATION")
private fun reveillerEcran(context: Context) {
    context.getSystemService(PowerManager::class.java)
        .newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            ETIQUETTE_REVEIL,
        )
        .acquire(DUREE_REVEIL_MILLIS)
}
