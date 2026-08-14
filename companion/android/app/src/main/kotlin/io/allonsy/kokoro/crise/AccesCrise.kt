package io.allonsy.kokoro.crise

import android.Manifest
import android.annotation.SuppressLint
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

const val ID_CANAL_ACCES = "kokoro_acces_v1"

private const val ID_NOTIFICATION_ACCES = 2
private const val CODE_ACCUEIL = 20
private const val CODE_MOT_CODE = 21
private const val CODE_TENSION = 22

fun creerCanalAcces(context: Context) {
    val canal = NotificationChannel(
        ID_CANAL_ACCES,
        context.getString(R.string.canal_acces_nom),
        NotificationManager.IMPORTANCE_LOW,
    ).apply {
        description = context.getString(R.string.canal_acces_description)
        setSound(null, null)
        enableVibration(false)
        vibrationPattern = null
        enableLights(false)
        setShowBadge(false)
        lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
    }
    context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
}

@SuppressLint("MissingPermission")
fun publierAccesCrise(context: Context): Boolean {
    if (!notificationsAutorisees(context)) return false

    creerCanalAcces(context)

    val notification = NotificationCompat.Builder(context, ID_CANAL_ACCES)
        .setSmallIcon(R.drawable.ic_kokoro)
        .setContentTitle(context.getString(R.string.acces_titre))
        .setContentText(context.getString(R.string.acces_texte))
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setCategory(NotificationCompat.CATEGORY_STATUS)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setOngoing(true)
        .setSilent(true)
        .setShowWhen(false)
        .setDefaults(0)
        .setSound(null)
        .setVibrate(null)
        .setContentIntent(intentEcran(context, CODE_ACCUEIL, null))
        .addAction(
            0,
            context.getString(R.string.acces_action_mot_code),
            intentEcran(context, CODE_MOT_CODE, ECRAN_MOT_CODE),
        )
        .addAction(
            0,
            context.getString(R.string.acces_action_tension),
            intentEcran(context, CODE_TENSION, ECRAN_TENSION),
        )
        .build()

    NotificationManagerCompat.from(context).notify(ID_NOTIFICATION_ACCES, notification)
    return true
}

private fun intentEcran(context: Context, code: Int, ecran: String?): PendingIntent {
    val destination = Intent(context, CriseActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        .putExtra(EXTRA_ECRAN, ecran)
    return PendingIntent.getActivity(
        context,
        code,
        destination,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}

private fun notificationsAutorisees(context: Context): Boolean =
    NotificationManagerCompat.from(context).areNotificationsEnabled() &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
