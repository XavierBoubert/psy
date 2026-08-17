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

    // Xavier, 16/08/2026 : aucun personnage sur les écrans de crise hors de l'écran du monde (CORPS.md §10).
    @Test
    fun `aucun personnage dans les surfaces de crise hors du monde`() {
        val personnages = listOf("CorpsKokoro(", "Locuteur(", "Habitant(", "BrasDeLHabitant(")
        val trouves = sources
            .filter { it.parentFile?.name == "crise" }
            .flatMap { fichier ->
                val contenu = fichier.readText()
                personnages.filter { contenu.contains(it) }.map { "${fichier.name} → $it" }
            }
        assertTrue(
            "Un personnage est entré dans une surface de crise hors du monde : $trouves",
            trouves.isEmpty(),
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
