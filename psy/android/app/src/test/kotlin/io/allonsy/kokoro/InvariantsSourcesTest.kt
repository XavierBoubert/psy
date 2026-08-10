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

    private fun interdire(motifs: List<String>, raison: String) {
        val trouves = sources.flatMap { fichier ->
            val contenu = fichier.readText()
            motifs.filter { contenu.contains(it) }.map { "${fichier.name} → $it" }
        }
        assertTrue("Motifs interdits dans les sources ($raison) : $trouves", trouves.isEmpty())
    }
}
