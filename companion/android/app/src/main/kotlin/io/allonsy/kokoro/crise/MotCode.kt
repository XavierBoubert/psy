package io.allonsy.kokoro.crise

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import androidx.core.content.ContextCompat
import io.allonsy.kokoro.reglages.Reglages

const val ACTION_MOT_CODE_ENVOYE = "io.allonsy.kokoro.MOT_CODE_ENVOYE"

private const val CODE_ENVOI = 10

enum class ResultatEnvoi { INACTIF, EN_COURS, ENVOYE, ECHEC }

fun envoiDirectDisponible(context: Context): Boolean =
    ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) ==
        PackageManager.PERMISSION_GRANTED

fun tenterMotCode(context: Context, reglages: Reglages): Boolean {
    if (!reglages.contactRenseigne || !envoiDirectDisponible(context)) return false
    envoyerMotCode(context, reglages.contactNumero, reglages.motCode)
    return true
}

fun envoyerMotCode(context: Context, numero: String, texte: String) {
    val accuse = PendingIntent.getBroadcast(
        context,
        CODE_ENVOI,
        Intent(ACTION_MOT_CODE_ENVOYE).setPackage(context.packageName),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    context.getSystemService(SmsManager::class.java)
        .sendTextMessage(numero, null, texte, accuse, null)
}

fun intentApplicationSms(numero: String, texte: String): Intent =
    Intent(Intent.ACTION_SENDTO, Uri.fromParts("smsto", numero, null))
        .putExtra("sms_body", texte)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
