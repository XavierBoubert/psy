package io.allonsy.kokoro.monde

import android.content.Context
import android.content.Intent
import android.net.Uri

private const val MIME_PDF = "application/pdf"

// Kokoro n'affiche pas de PDF : il le confie au lecteur du téléphone, et le dit avant de le faire.
fun ouvrirLePdf(context: Context, document: Uri): Boolean =
    runCatching {
        context.startActivity(
            Intent(Intent.ACTION_VIEW)
                .setDataAndType(document, MIME_PDF)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }.isSuccess
