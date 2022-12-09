package edu.temple.falldetection

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val alpha: Float = 0.8f
        val linearAcceleration = floatArrayOf(.1f)
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){

            // Isolate the force of gravity with the low-pass filter.
            event.values[0] = alpha * event.values[0] + (1 - alpha) * event.values[0]
            event.values[1] = alpha * event.values[1] + (1 - alpha) * event.values[1]
            event.values[2] = alpha * event.values[2] + (1 - alpha) * event.values[2]

            // Remove the gravity contribution with the high-pass filter.
            linearAcceleration[0] = event.values[0] - event.values[0]
            linearAcceleration[1] = event.values[1] - event.values[1]
            linearAcceleration[2] = event.values[2] - event.values[2]

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }
}