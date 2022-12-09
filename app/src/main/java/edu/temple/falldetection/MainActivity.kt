package edu.temple.falldetection

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private lateinit var xCoordinate: TextView
    private lateinit var yCoordinate: TextView
    private lateinit var zCoordinate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        xCoordinate = findViewById(R.id.x)
        yCoordinate = findViewById(R.id.y)
        zCoordinate = findViewById(R.id.z)

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
        val alpha = 0.8f
        val xValue: Float
        val yValue: Float
        val zValue: Float
//        val linearAcceleration = floatArrayOf(.1f)

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){

            val values = event.values

//            xValue = values[0]
//            yValue = values[1]
//            zValue = values[2]

            // Isolate the force of gravity with the low-pass filter.
            xValue = alpha * values[0] + (1 - alpha) * values[0]
            yValue = alpha * values[1] + (1 - alpha) * values[1]
            zValue = alpha * values[2] + (1 - alpha) * values[2]

            xCoordinate.text = xValue.toString()
            yCoordinate.text = yValue.toString()
            zCoordinate.text = zValue.toString()

            val a = isFalling(xValue, yValue, zValue)
            Log.i("isFalling", a.toString())
        }
    }

    private fun isFalling(x: Float, y: Float, z: Float): Boolean {

        val a = sqrt( x.pow(2) + y.pow(2) + z.pow(2))

        if (a >= 2){
            return false
        }
        Log.i("X:", x.toString())
        Log.i("Y:", y.toString())
        Log.i("Z:", z.toString())
        return true
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}