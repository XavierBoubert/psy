package io.allonsy.kokoro.ui

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import io.allonsy.kokoro.R

// texte reste null par défaut : la notification de crise n'affiche jamais de ligne de corps.
fun NotificationCompat.Builder.avecIllustration(
    context: Context,
    titre: String,
    texte: String? = null,
): NotificationCompat.Builder =
    setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(vue(context, titre, texte))

// fitXY obligatoire (fondu alpha déjà encodé) ; bandeau très léger, RemoteViews passe par un IPC borné.
private fun vue(context: Context, titre: String, texte: String?): RemoteViews =
    RemoteViews(context.packageName, R.layout.notif_kokoro).apply {
        setTextViewText(R.id.notif_titre, titre)
        setViewVisibility(R.id.notif_texte, if (texte == null) View.GONE else View.VISIBLE)
        if (texte != null) setTextViewText(R.id.notif_texte, texte)
    }
