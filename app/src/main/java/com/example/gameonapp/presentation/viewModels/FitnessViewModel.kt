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
    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories

    private val sensorManager: SensorManager =
        application.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager
    private val heartSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    private var isSensorRegistered = false
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val value = event.values.getOrNull(0) ?: return
        val bpm = value.toDouble()
        if (bpm > 0.0) {
            _heartRateBpm.value = bpm
        }
    }

    fun registerHeartRateSensor() {
        if (isSensorRegistered) return
        heartSensor?.let {
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