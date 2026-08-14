package io.allonsy.kokoro.reglages

import android.content.Context

const val MOT_CODE = "shutdown"

private const val FICHIER = "kokoro_reglages"
private const val CLE_NOM = "contact_nom"
private const val CLE_NUMERO = "contact_numero"
private const val CLE_NUIT_ACTIVE = "nuit_active"
private const val CLE_NUIT_DEBUT = "nuit_debut"
private const val CLE_NUIT_FIN = "nuit_fin"
private const val NOM_PAR_DEFAUT = "Chourouk"

data class Reglages(
    val contactNom: String,
    val contactNumero: String,
    val nuit: PlageNuit,
) {
    val contactRenseigne: Boolean get() = contactNumero.isNotBlank()
}

fun lireReglages(context: Context): Reglages {
    val prefs = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
    return Reglages(
        contactNom = prefs.getString(CLE_NOM, NOM_PAR_DEFAUT).orEmpty().ifBlank { NOM_PAR_DEFAUT },
        contactNumero = prefs.getString(CLE_NUMERO, "").orEmpty(),
        nuit = PlageNuit(
            active = prefs.getBoolean(CLE_NUIT_ACTIVE, PLAGE_NUIT_PAR_DEFAUT.active),
            debut = prefs.getInt(CLE_NUIT_DEBUT, PLAGE_NUIT_PAR_DEFAUT.debut),
            fin = prefs.getInt(CLE_NUIT_FIN, PLAGE_NUIT_PAR_DEFAUT.fin),
        ),
    )
}

fun ecrireReglages(context: Context, reglages: Reglages) {
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .edit()
        .putString(CLE_NOM, reglages.contactNom.trim().ifBlank { NOM_PAR_DEFAUT })
        .putString(CLE_NUMERO, reglages.contactNumero.filterNot { it == ' ' })
        .putBoolean(CLE_NUIT_ACTIVE, reglages.nuit.active)
        .putInt(CLE_NUIT_DEBUT, reglages.nuit.debut)
        .putInt(CLE_NUIT_FIN, reglages.nuit.fin)
        .apply()
}
