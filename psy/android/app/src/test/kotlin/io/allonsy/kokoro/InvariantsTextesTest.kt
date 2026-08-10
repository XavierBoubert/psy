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
)

private val REGULARITE = listOf(
    "série", "d'affilée", "jours de suite", "régularité", "streak",
    "assiduité", "objectif atteint", "moyenne", "ça fait",
)

private val NUMEROS_RETIRES = listOf("112", "114", "3114", "le 15", "samu", "pompiers")

private val RELAXATION_DELETERE = listOf("détends-toi", "detends-toi", "relaxe-toi", "respire lentement")

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

    private fun interdire(motifs: List<String>, raison: String) {
        val trouves = motifs.filter { textes.contains(it) }
        assertTrue("Motifs interdits présents dans strings.xml ($raison) : $trouves", trouves.isEmpty())
    }
}
