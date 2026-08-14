package io.allonsy.kokoro.alerte

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.R

const val ID_CANAL_ALERTE = "kokoro_alerte_v1"

internal const val ID_NOTIFICATION_ALERTE = 1

private const val CODE_ALERTE = 1
private const val CODE_DECLENCHEUR = 2

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

fun programmerAlerteTest(context: Context, delaiMillis: Long) {
    val declencheur = PendingIntent.getBroadcast(
        context,
        CODE_DECLENCHEUR,
        Intent(context, DeclencheurAlerte::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    context.getSystemService(AlarmManager::class.java).setAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + delaiMillis,
        declencheur,
    )
}

@SuppressLint("MissingPermission")
fun publierAlerteTest(context: Context): Boolean {
    if (!notificationsAutorisees(context)) return false

    creerCanalAlerte(context)

    val pleinEcran = PendingIntent.getActivity(
        context,
        CODE_ALERTE,
        Intent(context, AlerteActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    val notification = NotificationCompat.Builder(context, ID_CANAL_ALERTE)
        .setSmallIcon(R.drawable.ic_kokoro)
        .setContentTitle(context.getString(R.string.alerte_titre))
        .setContentText(context.getString(R.string.alerte_texte))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_REMINDER)
        .setDefaults(0)
        .setSound(null)
        .setVibrate(null)
        .setAutoCancel(true)
        .setContentIntent(pleinEcran)
        .setFullScreenIntent(pleinEcran, true)
        .build()

    NotificationManagerCompat.from(context).notify(ID_NOTIFICATION_ALERTE, notification)
    return true
}

private fun notificationsAutorisees(context: Context): Boolean =
    NotificationManagerCompat.from(context).areNotificationsEnabled() &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
