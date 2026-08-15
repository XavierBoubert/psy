package io.allonsy.kokoro.ui

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import io.allonsy.kokoro.R

/**
 * ⭐ **L'illustration de fond des notifications de Kokoro** — `notif_kokoro`, **une seule vue**.
 *
 * 🔴 **Aucune vue dépliée, et c'est délibéré** *(16/08/2026, demande de Xavier)*. La notification
 * d'accès n'a **rien de plus** à montrer en grand — pas une ligne, pas un bouton — donc l'ouvrir ne
 * donnait rien. ⭐ **Ne pas poser de `bigContentView` retire aussi le chevron** : la notification
 * cesse d'annoncer un contenu qu'elle n'a pas. **Une affordance qui ne mène à rien est un
 * sous-entendu**, et c'est l'invariant qui a déjà écarté la pastille de §7.4.
 *
 * 🔴 **Le fond d'une notification appartient au système, pas à l'application.** Depuis Android 12,
 * une notification custom est enveloppée dans le gabarit d'Android — entête, marges et fond sont
 * imposés. **Ce qui est peint ici, c'est la zone de contenu.** `DecoratedCustomViewStyle` est
 * déclaré exprès : le système décorerait de toute façon, autant que le rendu soit prévisible.
 *
 * ⭐ **L'intégration passe par le canal alpha de l'image, pas par du code.** Le bandeau est encodé
 * à **60 % d'opacité**, avec un fondu vers le transparent sur les quatre côtés. **Un masque alpha
 * n'existe pas en drawable XML et une `RemoteViews` ne sait pas composer deux couches** — donc
 * c'est cuit dans le WebP. ⚠️ **Le layout doit rester en `fitXY`** : `centerCrop` recadrerait, et
 * jetterait hors du cadre les bords qui adoucissent.
 *
 * 🔴 **`texte` vaut `null` par défaut, et ce défaut est clinique.** La notification d'accès à la
 * crise n'a **aucune ligne de corps** depuis le 15/08/2026 — *« rien à lire, rien à relire »*,
 * `INTERFACE.md` §6.2. **Une ligne s'affiche parce qu'un appelant la demande**, jamais parce qu'un
 * gabarit la prévoit.
 *
 * ⚠️ **Le poids compte** : une `RemoteViews` traverse un IPC borné, et un bitmap trop lourd fait
 * tomber la notification au lieu de l'afficher. Le bandeau est en WebP, 30 ko.
 */
fun NotificationCompat.Builder.avecIllustration(
    context: Context,
    titre: String,
    texte: String? = null,
): NotificationCompat.Builder =
    setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(vue(context, titre, texte))

private fun vue(context: Context, titre: String, texte: String?): RemoteViews =
    RemoteViews(context.packageName, R.layout.notif_kokoro).apply {
        setTextViewText(R.id.notif_titre, titre)
        setViewVisibility(R.id.notif_texte, if (texte == null) View.GONE else View.VISIBLE)
        if (texte != null) setTextViewText(R.id.notif_texte, texte)
    }
