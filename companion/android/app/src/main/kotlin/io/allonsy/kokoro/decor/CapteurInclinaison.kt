package io.allonsy.kokoro.decor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect

/**
 * La part de la mesure neuve retenue à chaque relevé.
 *
 * ⭐ **Le lissage n'est pas un confort, c'est l'invariant d'hypersensibilité** *(quatre canaux)*. La
 * main tremble, et un décor collé à la mesure brute vibrerait en permanence. À ~50 relevés par
 * seconde, 0,06 vaut une constante de temps d'environ un tiers de seconde : le décor suit sans
 * retard perceptible, **et il ne peut pas sursauter**.
 */
private const val LISSAGE = 0.06f

/**
 * L'inclinaison du téléphone, en écrans de caméra, prête à s'ajouter à celle du doigt.
 *
 * ⭐ **Elle est lue par [Sensor.TYPE_GRAVITY]**, la direction du bas — pas par le gyroscope, dont la
 * grandeur est une vitesse de rotation qu'il faudrait intégrer. Le motif est écrit sur
 * [inclinaisonDeLaGravite] : **une position ne dérive pas, donc rien ne se recentre tout seul.**
 * L'accéléromètre sert de repli sur un téléphone qui n'aurait pas la gravité en capteur composé ; il
 * mesure la même chose, en un peu plus bruité — et [LISSAGE] s'en occupe.
 *
 * 🔴 **Le capteur n'est branché que tant que le monde est à l'écran, et que si le réglage le
 * demande** : hors de là il n'est même pas enregistré. Un capteur qui continuerait de tourner
 * derrière une application fermée serait une consommation invisible et non demandée.
 *
 * ⭐ **Rien n'est enregistré ni transmis.** La valeur vit le temps d'une image ; il n'y a ni
 * historique, ni fichier, ni réseau — l'app n'a d'ailleurs pas la permission INTERNET.
 *
 * Quand [actif] est faux, l'inclinaison retombe à zéro : le décor ne dépend plus que du doigt.
 */
@Composable
fun rememberInclinaison(actif: Boolean): FloatState {
    val context = LocalContext.current
    val inclinaison = remember { mutableFloatStateOf(0f) }

    LifecycleResumeEffect(actif) {
        val gestionnaire = context.getSystemService(SensorManager::class.java)
        val capteur = if (actif) capteurDAplomb(gestionnaire) else null

        if (capteur == null) {
            inclinaison.floatValue = 0f
            return@LifecycleResumeEffect onPauseOrDispose { }
        }

        val ecoute = object : SensorEventListener {
            override fun onSensorChanged(evenement: SensorEvent) {
                val cible = inclinaisonDeLaGravite(
                    x = evenement.values[0],
                    y = evenement.values[1],
                    z = evenement.values[2],
                )
                inclinaison.floatValue += (cible - inclinaison.floatValue) * LISSAGE
            }

            override fun onAccuracyChanged(capteur: Sensor?, precision: Int) = Unit
        }

        gestionnaire.registerListener(ecoute, capteur, SensorManager.SENSOR_DELAY_GAME)
        onPauseOrDispose { gestionnaire.unregisterListener(ecoute) }
    }

    return inclinaison
}

/**
 * 🔴 **Le réglage se cache si le téléphone ne sait pas répondre** — il n'affiche pas un interrupteur
 * qui ne ferait rien. Un réglage qui semble posé et qui ne l'est pas est pire qu'un réglage absent
 * (`companion/README.md` §5 — rien ne défaille en silence).
 */
fun capteurInclinaisonPresent(context: Context): Boolean =
    capteurDAplomb(context.getSystemService(SensorManager::class.java)) != null

private fun capteurDAplomb(gestionnaire: SensorManager?): Sensor? =
    gestionnaire?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        ?: gestionnaire?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
