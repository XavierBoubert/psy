package io.allonsy.kokoro.alerte

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

private const val ETIQUETTE_REVEIL = "kokoro:alerte"
private const val DUREE_REVEIL_MILLIS = 10_000L

class DeclencheurAlerte : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        reveillerEcran(context)
        publierAlerteTest(context)
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
