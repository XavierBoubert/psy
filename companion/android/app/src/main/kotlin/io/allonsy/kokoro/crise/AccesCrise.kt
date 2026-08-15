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
import io.allonsy.kokoro.ui.avecIllustration

/**
 * 🔴 **La notification d'accès est muette** — `companion/INTERFACE.md` §6.2, acté le 14/08/2026,
 * câblé le 15/08/2026.
 *
 * **Le motif est clinique, et il est de Xavier :** *« avec les boutons sur la notification, je lis
 * toute la journée mot code et tension appliquée. Ça n'aide pas mes angoisses. »* ⭐ **C'est le motif
 * du 10/08 appliqué une quatrième fois** — celui qui a fait retirer les numéros d'urgence : **un
 * secours affiché en permanence cesse d'être une porte et devient un rappel permanent du danger.**
 *
 * 🔴 **Il ne reste que l'icône et le mot *Kokoro*.** Plus de boutons d'action, **et plus une ligne de
 * corps** : enlever les boutons en laissant `mot-code · tension appliquée` écrit dessous n'aurait
 * réglé que la moitié du problème. **Rien à lire, rien à relire.**
 *
 * ⭐ **Elle porte une illustration de fond depuis le 16/08/2026** — demande de Xavier,
 * `INTERFACE.md` §7.5. **Elle ne reprend pas une ligne de texte au passage :** [avecIllustration]
 * est appelée **sans `texte`**, et le motif du 15/08 tient toujours — *ce qui pesait, c'était une
 * ligne relue toute la journée, pas une image.* 🔴 **Le jour où une ligne y revient, c'est cette
 * décision-là qu'on défait**, pas un détail de gabarit.
 *
 * 🔴 **Elle ouvre [CriseActivity], et pas le monde** — c'est **le repli écrit d'avance au §6.2**,
 * emprunté le 15/08/2026 après essai sur l'appareil. **Le monde demandait de déverrouiller le
 * téléphone** : il vit dans la tâche du lanceur, et `showWhenLocked` posé à l'exécution arrive après
 * la décision du keyguard. `CriseActivity` le déclare **dans le manifeste**, avec sa propre tâche —
 * c'est ce qui a été éprouvé le 10/08. ⭐ **Rien n'est perdu au passage** : depuis §7.2 les deux
 * portes affichent le même écran ([PortesDeCrise]). **Deux portes, un seul contenu.**
 *
 * ⚠️ **`kokoro_acces_v1` ne change pas d'identifiant** : le nom et la description d'un canal se
 * mettent à jour, son importance et son silence sont figés à la création. En changer l'identifiant
 * rendrait à Android le droit de resonner.
 */

const val ID_CANAL_ACCES = "kokoro_acces_v1"

private const val ID_NOTIFICATION_ACCES = 2
private const val CODE_ACCUEIL = 20

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
        .avecIllustration(context, titre = context.getString(R.string.acces_titre))
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setCategory(NotificationCompat.CATEGORY_STATUS)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setOngoing(true)
        .setSilent(true)
        .setShowWhen(false)
        .setDefaults(0)
        .setSound(null)
        .setVibrate(null)
        .setContentIntent(intentCrise(context))
        .build()

    NotificationManagerCompat.from(context).notify(ID_NOTIFICATION_ACCES, notification)
    return true
}

/**
 * 🔴 **Aucun `EXTRA_ECRAN` : elle ouvre l'accueil de la crise, ses trois boutons**, le même écran que
 * le bord **BAS** du monde. C'est là que le mot-code part d'un appui.
 */
private fun intentCrise(context: Context): PendingIntent {
    val destination = Intent(context, CriseActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    return PendingIntent.getActivity(
        context,
        CODE_ACCUEIL,
        destination,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}

private fun notificationsAutorisees(context: Context): Boolean =
    NotificationManagerCompat.from(context).areNotificationsEnabled() &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
