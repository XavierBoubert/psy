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

private const val LISSAGE = 0.06f

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

fun capteurInclinaisonPresent(context: Context): Boolean =
    capteurDAplomb(context.getSystemService(SensorManager::class.java)) != null

private fun capteurDAplomb(gestionnaire: SensorManager?): Sensor? =
    gestionnaire?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        ?: gestionnaire?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
