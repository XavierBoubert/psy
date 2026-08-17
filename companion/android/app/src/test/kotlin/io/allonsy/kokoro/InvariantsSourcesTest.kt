package io.allonsy.kokoro

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

private val SOURCES = File("src/main/kotlin")

private val SON_ET_VIBRATION = listOf(
    "Vibrator", "VibrationEffect", "ToneGenerator", "MediaPlayer", "SoundPool",
    "RingtoneManager", "AudioManager", "setSound(RingtoneManager", "performHapticFeedback",
)

private val RESEAU = listOf(
    "HttpURLConnection", "OkHttp", "Retrofit", "java.net.URL(", "WebView",
)

class InvariantsSourcesTest {

    private val sources: List<File> by lazy {
        assertTrue("Sources introuvables : ${SOURCES.absolutePath}", SOURCES.isDirectory)
        SOURCES.walkTopDown().filter { it.isFile && it.extension == "kt" }.toList()
    }

    @Test
    fun `aucune api de son ni de vibration`() {
        interdire(SON_ET_VIBRATION, "jamais de son, jamais de vibration non sollicitée")
    }

    @Test
    fun `aucun appel reseau`() {
        interdire(RESEAU, "aucun service tiers, aucune télémétrie, pas de permission INTERNET")
    }

    // Xavier, 18/08/2026 : la scène de crise du monde est la seule à porter le personnage, et la notification l'affiche
    // telle quelle (CORPS.md §10.2). Aucune surface de crise n'en dessine un elle-même.
    @Test
    fun `aucun personnage dessine par une surface de crise`() {
        val personnages = listOf("CorpsKokoro(", "Locuteur(", "Habitant(", "BrasDeLHabitant(")
        val trouves = sources
            .filter { it.parentFile?.name == "crise" }
            .flatMap { fichier ->
                val contenu = fichier.readText()
                personnages.filter { contenu.contains(it) }.map { "${fichier.name} → $it" }
            }
        assertTrue(
            "Un personnage est dessiné par une surface de crise au lieu de venir de la scène du monde : $trouves",
            trouves.isEmpty(),
        )
    }

    // Tension appliquée, phrase pour le soignant, mot-code : aucun panneau de crise ne porte Kokoro sous lui.
    @Test
    fun `aucun kokoro sous un panneau de crise`() {
        val activite = File(SOURCES, "io/allonsy/kokoro/crise/CriseActivity.kt")
        assertTrue("CriseActivity introuvable : ${activite.absolutePath}", activite.isFile)
        assertTrue(
            "CriseActivity doit poser LocalPanneauPorte à false : un panneau de crise ne porte ni queue ni personnage",
            activite.readText().contains("LocalPanneauPorte provides false"),
        )
    }

    private fun interdire(motifs: List<String>, raison: String) {
        val trouves = sources.flatMap { fichier ->
            val contenu = fichier.readText()
            motifs.filter { contenu.contains(it) }.map { "${fichier.name} → $it" }
        }
        assertTrue("Motifs interdits dans les sources ($raison) : $trouves", trouves.isEmpty())
    }
}
