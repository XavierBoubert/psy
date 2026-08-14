package io.allonsy.kokoro.crise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReceveurDemarrage : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        publierAccesCrise(context)
    }
}
