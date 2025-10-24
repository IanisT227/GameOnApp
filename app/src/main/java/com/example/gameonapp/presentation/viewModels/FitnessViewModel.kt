package com.example.gameonapp.presentation.viewModels

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FitnessViewModel(application: Application) : ViewModel(), SensorEventListener {
    private val _heartRateBpm = MutableStateFlow<Double?>(null)
    val heartRateBpm: StateFlow<Double?> = _heartRateBpm

    // Estimated calories burned so far (kcal)
    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories

    private val sensorManager: SensorManager =
        application.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
    private val heartSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    private var isSensorRegistered = false
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        ""
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        // For heart rate, values[0] usually contains bpm
        val value = event.values.getOrNull(0) ?: return
        // Some devices report 0 when not ready — ignore zeros
        val bpm = value.toDouble()
        if (bpm > 0.0) {
            _heartRateBpm.value = bpm
            // optional: feed BPM into calories formula here to refine estimate
        }
    }

    fun registerHeartRateSensor() {
        if (isSensorRegistered) return
        // If heartSensor is null, device has no HR sensor
        heartSensor?.let {
            // Choose a moderate delay to save battery
            // SENSOR_DELAY_NORMAL or SENSOR_DELAY_UI recommended for wearable
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            isSensorRegistered = true
        }
    }

    private fun unregisterHeartRateSensor() {
        if (!isSensorRegistered) return
        sensorManager.unregisterListener(this)
        isSensorRegistered = false
    }

    override fun onCleared() {
        super.onCleared()
        unregisterHeartRateSensor()
    }
}