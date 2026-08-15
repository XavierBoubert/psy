package io.allonsy.kokoro.reglages

import android.content.Context

/**
 * Le message convenu, tel qu'il part **si personne n'en a convenu d'un autre**.
 *
 * ⭐ **Il se règle désormais dans l'application** *(15/08/2026, demande de Xavier)*. Ça ne le rend pas
 * moins convenu : **un mot-code se décide à froid avec la personne qui le reçoit**, et le changer
 * dans Kokoro ne prévient personne. Le réglage sert à ce que le dispositif suive un accord qui a
 * bougé, pas à improviser un message en situation.
 */
const val MOT_CODE_PAR_DEFAUT = "shutdown"

const val NOM_PAR_DEFAUT = "Chourouk"

private const val FICHIER = "kokoro_reglages"
private const val CLE_NOM = "contact_nom"
private const val CLE_NUMERO = "contact_numero"
private const val CLE_MOT_CODE = "mot_code"
private const val CLE_NUIT_ACTIVE = "nuit_active"
private const val CLE_NUIT_DEBUT = "nuit_debut"
private const val CLE_NUIT_FIN = "nuit_fin"
private const val CLE_PARALLAXE_ACTIVE = "parallaxe_active"
private const val CLE_PARALLAXE_INCLINAISON = "parallaxe_inclinaison"

data class Reglages(
    val contactNom: String,
    val contactNumero: String,
    val nuit: PlageNuit,
    val motCode: String = MOT_CODE_PAR_DEFAUT,
    val parallaxe: Parallaxe = PARALLAXE_PAR_DEFAUT,
) {
    val contactRenseigne: Boolean get() = contactNumero.isNotBlank()
}

/**
 * Ce que valent les réglages avant la première lecture du disque.
 *
 * ⭐ **Le destinataire y porte déjà son prénom** : un écran de crise qui s'ouvre en affichant
 * *Mot-code à* suivi d'un blanc, le temps d'une lecture de fichier, est un écran qui hésite au pire
 * moment.
 */
val REGLAGES_INITIAUX = Reglages(
    contactNom = NOM_PAR_DEFAUT,
    contactNumero = "",
    nuit = PLAGE_NUIT_PAR_DEFAUT,
)

fun lireReglages(context: Context): Reglages {
    val prefs = context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
    return Reglages(
        contactNom = prefs.getString(CLE_NOM, NOM_PAR_DEFAUT).orEmpty().ifBlank { NOM_PAR_DEFAUT },
        contactNumero = prefs.getString(CLE_NUMERO, "").orEmpty(),
        motCode = prefs.getString(CLE_MOT_CODE, MOT_CODE_PAR_DEFAUT)
            .orEmpty()
            .ifBlank { MOT_CODE_PAR_DEFAUT },
        nuit = PlageNuit(
            active = prefs.getBoolean(CLE_NUIT_ACTIVE, PLAGE_NUIT_PAR_DEFAUT.active),
            debut = prefs.getInt(CLE_NUIT_DEBUT, PLAGE_NUIT_PAR_DEFAUT.debut),
            fin = prefs.getInt(CLE_NUIT_FIN, PLAGE_NUIT_PAR_DEFAUT.fin),
        ),
        parallaxe = Parallaxe(
            actif = prefs.getBoolean(CLE_PARALLAXE_ACTIVE, PARALLAXE_PAR_DEFAUT.actif),
            inclinaison = prefs.getBoolean(
                CLE_PARALLAXE_INCLINAISON,
                PARALLAXE_PAR_DEFAUT.inclinaison,
            ),
        ),
    )
}

fun ecrireReglages(context: Context, reglages: Reglages) {
    context.getSharedPreferences(FICHIER, Context.MODE_PRIVATE)
        .edit()
        .putString(CLE_NOM, reglages.contactNom.trim().ifBlank { NOM_PAR_DEFAUT })
        .putString(CLE_NUMERO, reglages.contactNumero.filterNot { it == ' ' })
        .putString(CLE_MOT_CODE, reglages.motCode.trim().ifBlank { MOT_CODE_PAR_DEFAUT })
        .putBoolean(CLE_NUIT_ACTIVE, reglages.nuit.active)
        .putInt(CLE_NUIT_DEBUT, reglages.nuit.debut)
        .putInt(CLE_NUIT_FIN, reglages.nuit.fin)
        .putBoolean(CLE_PARALLAXE_ACTIVE, reglages.parallaxe.actif)
        .putBoolean(CLE_PARALLAXE_INCLINAISON, reglages.parallaxe.inclinaison)
        .apply()
}
