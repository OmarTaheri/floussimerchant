package com.example.floussi.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

data class GyroscopeData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)

class GyroscopeSensorManager(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var listener: SensorEventListener? = null

    fun startListening(onUpdate: (GyroscopeData) -> Unit) {
        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                    onUpdate(
                        GyroscopeData(
                            x = event.values[0],
                            y = event.values[1],
                            z = event.values[2]
                        )
                    )
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // Not needed for this implementation
            }
        }

        gyroscope?.let {
            sensorManager.registerListener(
                listener,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stopListening() {
        listener?.let { sensorManager.unregisterListener(it) }
        listener = null
    }

    fun isAvailable(): Boolean = gyroscope != null
}

@Composable
fun rememberGyroscopeState(): State<GyroscopeData> {
    val context = LocalContext.current
    val gyroscopeData = remember { mutableStateOf(GyroscopeData()) }
    val sensorManager = remember { GyroscopeSensorManager(context) }

    DisposableEffect(Unit) {
        if (sensorManager.isAvailable()) {
            sensorManager.startListening { data ->
                gyroscopeData.value = data
            }
        }

        onDispose {
            sensorManager.stopListening()
        }
    }

    return gyroscopeData
}
