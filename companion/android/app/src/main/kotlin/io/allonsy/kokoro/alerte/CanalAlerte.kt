package io.allonsy.kokoro.alerte

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import io.allonsy.kokoro.R

// ⚠️ Canal immuable une fois créé : l'identifiant est versionné, le silence et l'importance sont figés.
const val ID_CANAL_ALERTE = "kokoro_alerte_v1"

fun creerCanalAlerte(context: Context) {
    val canal = NotificationChannel(
        ID_CANAL_ALERTE,
        context.getString(R.string.canal_alerte_nom),
        NotificationManager.IMPORTANCE_HIGH,
    ).apply {
        description = context.getString(R.string.canal_alerte_description)
        setSound(null, null)
        enableVibration(false)
        vibrationPattern = null
        enableLights(false)
        setShowBadge(false)
    }
    context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
}
