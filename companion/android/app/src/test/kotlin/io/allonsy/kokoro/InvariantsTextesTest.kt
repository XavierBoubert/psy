package io.allonsy.kokoro

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val FICHIER_TEXTES = File("src/main/res/values/strings.xml")

private val VISUALISATION = listOf(
    "imagine", "imaginer", "visualise", "visualiser",
    "représente-toi", "represente-toi", "lieu sûr", "lieu sur",
    "dans ta tête", "mentalement",
)

private val COTATION_DE_RESSENTI = listOf(
    "sur 10", "sur dix", "note ton", "note ta", "évalue ton", "évalue ta",
    "à combien te sens", "ressens-tu",
    "ton anxiété", "ton humeur", "ton moral", "ton stress", "ta fatigue", "ton niveau de",
)

private val JUGEMENT_OU_RELANCE = listOf(
    "bravo", "félicitations", "c'est bien", "très bien", "tu devrais", "il faut que tu",
    "pense à", "n'oublie pas", "un effort", "essaie quand même",
)

private val REGULARITE = listOf(
    "série", "d'affilée", "jours de suite", "régularité", "streak",
    "assiduité", "objectif atteint", "moyenne", "ça fait",
)

private val NUMEROS_RETIRES = listOf("112", "114", "3114", "le 15", "samu", "pompiers")

private val RELAXATION_DELETERE = listOf("détends-toi", "detends-toi", "relaxe-toi", "respire lentement")

private val DECLENCHEMENT_SUR_PRODROME = listOf(
    "as-tu besoin", "en as-tu besoin", "si tu sens", "quand tu sens", "dès que tu sens",
    "si ça monte", "quand ça monte", "aux premiers signes", "si tu te sens",
)

class InvariantsTextesTest {

    private val textes: String by lazy {
        assertTrue(
            "Fichier de textes introuvable : ${FICHIER_TEXTES.absolutePath}",
            FICHIER_TEXTES.isFile,
        )
        FICHIER_TEXTES.readText().lowercase()
    }

    @Test
    fun `aucune consigne de visualisation`() {
        interdire(VISUALISATION, "aphantasie mesurée à 18 sur 80 — la consigne serait inopérante")
    }

    @Test
    fun `aucune cotation de ressenti`() {
        interdire(COTATION_DE_RESSENTI, "règle R6 — on cote des comportements observables")
    }

    @Test
    fun `aucun compteur de régularité`() {
        interdire(REGULARITE, "zéro streak, zéro compteur de régularité")
    }

    @Test
    fun `aucun numéro d'appel d'urgence`() {
        interdire(NUMEROS_RETIRES, "retirés du dispositif le 10 août 2026, à ne jamais réintroduire")
    }

    @Test
    fun `aucune consigne de relaxation sans support`() {
        interdire(RELAXATION_DELETERE, "délétère sur un vasovagal — la tension chute déjà")
    }

    @Test
    fun `aucun jugement ni relance sur ce qui est saisi`() {
        interdire(
            JUGEMENT_OU_RELANCE,
            "le check-in enregistre, il ne commente pas — l'interprétation appartient à la séance",
        )
    }

    @Test
    fun `aucun declenchement sur un prodrome`() {
        interdire(
            DECLENCHEMENT_SUR_PRODROME,
            "déficit intéroceptif — on déclenche sur un repère externe, jamais sur une sensation",
        )
    }

    private fun interdire(motifs: List<String>, raison: String) {
        val trouves = motifs.filter { textes.contains(it) }
        assertTrue("Motifs interdits présents dans strings.xml ($raison) : $trouves", trouves.isEmpty())
    }
}
