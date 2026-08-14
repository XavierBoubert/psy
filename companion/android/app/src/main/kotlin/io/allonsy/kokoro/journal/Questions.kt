package io.allonsy.kokoro.journal

import io.allonsy.kokoro.R

enum class Unite { BRUTE, HEURES, MINUTES, KILOS }

sealed interface Saisie {
    /** Choix fermé : toucher une option répond et passe à la suivante. */
    data class Choix(val options: List<Option>) : Saisie

    /** Compteur : deux pas, un point de départ, jamais de saisie au clavier. */
    data class Compteur(
        val depart: Double,
        val pas: Double,
        val grandPas: Double,
        val minimum: Double,
        val unite: Unite,
    ) : Saisie
}

data class Option(val valeur: Double, val libelle: Int)

data class Question(
    val champ: Champ,
    val enonce: Int,
    val saisie: Saisie,
    val precision: Int? = null,
)

/**
 * Les sept questions du noyau, dans l'ordre du SCHEMA §3.1 et avec les énoncés du
 * skill `psy-journal` §2 — mot pour mot. Puis les champs `campagne` déclarés dans
 * `etat.md` §4, et rien d'autre : on ne pose jamais de question sur un champ non déclaré.
 *
 * R5 : aucun champ obligatoire ne demande d'écrire. Tout est compteur ou choix fermé.
 * R6 : chaque question porte une ancre comportementale, aucune ne cote un ressenti.
 */
val QUESTIONS: List<Question> = listOf(
    Question(
        champ = Champ.SHUTDOWNS,
        enonce = R.string.journal_q_shutdowns,
        saisie = Saisie.Compteur(depart = 0.0, pas = 1.0, grandPas = 5.0, minimum = 0.0, unite = Unite.BRUTE),
    ),
    Question(
        champ = Champ.EXPOSITION_SOCIALE,
        enonce = R.string.journal_q_exposition,
        saisie = Saisie.Choix(
            listOf(
                Option(0.0, R.string.journal_exposition_0),
                Option(1.0, R.string.journal_exposition_1),
                Option(2.0, R.string.journal_exposition_2),
                Option(3.0, R.string.journal_exposition_3),
            ),
        ),
    ),
    Question(
        champ = Champ.RETRAIT_SENSORIEL,
        enonce = R.string.journal_q_retrait,
        saisie = Saisie.Compteur(depart = 0.0, pas = 1.0, grandPas = 5.0, minimum = 0.0, unite = Unite.BRUTE),
    ),
    Question(
        champ = Champ.RENONCEMENTS,
        enonce = R.string.journal_q_renoncements,
        saisie = Saisie.Compteur(depart = 0.0, pas = 1.0, grandPas = 5.0, minimum = 0.0, unite = Unite.BRUTE),
    ),
    Question(
        champ = Champ.ACTIVITES_INVESTIES,
        enonce = R.string.journal_q_activites,
        saisie = Saisie.Choix(
            listOf(
                Option(0.0, R.string.journal_activites_0),
                Option(1.0, R.string.journal_activites_1),
                Option(2.0, R.string.journal_activites_2),
                Option(3.0, R.string.journal_activites_3),
            ),
        ),
    ),
    Question(
        champ = Champ.SOMMEIL_HEURES,
        enonce = R.string.journal_q_sommeil,
        saisie = Saisie.Compteur(depart = 7.0, pas = 0.5, grandPas = 1.0, minimum = 0.0, unite = Unite.HEURES),
    ),
    Question(
        champ = Champ.MISSIONS_ACTIVES,
        enonce = R.string.journal_q_missions,
        saisie = Saisie.Compteur(depart = 0.0, pas = 1.0, grandPas = 1.0, minimum = 0.0, unite = Unite.BRUTE),
    ),
    Question(
        champ = Champ.PPC_MINUTES,
        enonce = R.string.journal_q_ppc,
        saisie = Saisie.Compteur(depart = 0.0, pas = 15.0, grandPas = 60.0, minimum = 0.0, unite = Unite.MINUTES),
        precision = R.string.journal_p_ppc,
    ),
    Question(
        champ = Champ.REPAS_SERVIS_UNE_FOIS,
        enonce = R.string.journal_q_repas,
        saisie = Saisie.Choix(
            listOf(
                Option(0.0, R.string.journal_repas_0),
                Option(1.0, R.string.journal_repas_1),
                Option(2.0, R.string.journal_repas_2),
                Option(3.0, R.string.journal_repas_3),
                Option(4.0, R.string.journal_repas_4),
            ),
        ),
    ),
    Question(
        champ = Champ.ACTIVITE_MINUTES,
        enonce = R.string.journal_q_activite,
        saisie = Saisie.Compteur(depart = 0.0, pas = 5.0, grandPas = 15.0, minimum = 0.0, unite = Unite.MINUTES),
    ),
    Question(
        champ = Champ.POIDS_KG,
        enonce = R.string.journal_q_poids,
        saisie = Saisie.Compteur(depart = 110.0, pas = 0.1, grandPas = 1.0, minimum = 0.0, unite = Unite.KILOS),
        precision = R.string.journal_p_poids,
    ),
)

/**
 * Point de départ d'un compteur. Deux champs seulement se reprennent du dernier
 * check-in écrit — le nombre de missions et le poids — parce qu'ils bougent rarement
 * et que les retrouver au compteur chaque jour est une charge inutile. Ce n'est pas
 * un historique : rien n'est affiché, rien n'est comparé, et la valeur reste à confirmer.
 */
val CHAMPS_REPRIS: List<Champ> = listOf(Champ.MISSIONS_ACTIVES, Champ.POIDS_KG)

fun departDe(question: Question, repris: Map<Champ, Double>): Double = when (val saisie = question.saisie) {
    is Saisie.Compteur -> repris[question.champ] ?: saisie.depart
    is Saisie.Choix -> saisie.options.first().valeur
}
