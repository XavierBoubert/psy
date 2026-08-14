package io.allonsy.kokoro.journal

enum class Section { NOYAU, CAMPAGNE }

/**
 * Les champs du check-in, dans l'ordre imposé par `psy/DOSSIER.md` §3.
 * Le noyau est stable dans le temps ; la campagne suit `etat.md` §4 et sortira du
 * journal à la clôture du chantier. Aucune surface n'a le droit d'inventer un format :
 * cet ordre et ces clés sont ceux du schéma, et `ChampsTest` échoue s'ils divergent.
 */
enum class Champ(val cle: String, val section: Section, val decimal: Boolean) {
    SHUTDOWNS("shutdowns", Section.NOYAU, false),
    EXPOSITION_SOCIALE("exposition_sociale", Section.NOYAU, false),
    RETRAIT_SENSORIEL("retrait_sensoriel", Section.NOYAU, false),
    RENONCEMENTS("renoncements", Section.NOYAU, false),
    ACTIVITES_INVESTIES("activites_investies", Section.NOYAU, false),
    SOMMEIL_HEURES("sommeil_heures", Section.NOYAU, true),
    MISSIONS_ACTIVES("missions_actives", Section.NOYAU, false),
    PPC_MINUTES("ppc_minutes", Section.CAMPAGNE, false),
    REPAS_SERVIS_UNE_FOIS("repas_servis_une_fois", Section.CAMPAGNE, false),
    ACTIVITE_MINUTES("activite_minutes", Section.CAMPAGNE, false),
    POIDS_KG("poids_kg", Section.CAMPAGNE, true),
}

const val SOURCE_ANDROID = "android"

/**
 * Un check-in. `null` n'est pas `0` : un champ auquel Xavier n'a pas répondu reste `null`
 * (SCHEMA §3.3). `notes` est toujours facultatif et toujours en dernier.
 */
data class Checkin(
    val date: String,
    val valeurs: Map<Champ, Double?>,
    val notes: String?,
) {
    fun avec(champ: Champ, valeur: Double?): Checkin = copy(valeurs = valeurs + (champ to valeur))

    fun valeur(champ: Champ): Double? = valeurs[champ]

    companion object {
        fun vide(date: String): Checkin = Checkin(
            date = date,
            valeurs = Champ.entries.associateWith { null },
            notes = null,
        )
    }
}
