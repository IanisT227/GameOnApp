package com.example.gameonapp.presentation.viewModels

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FitnessViewModel(application: Application) : ViewModel(), SensorEventListener {
    private val _heartRateBpm = MutableStateFlow<Double?>(null)
    val heartRateBpm: StateFlow<Double?> = _heartRateBpm
    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories

    private val _timeInSeconds = MutableStateFlow(0L)
    val timeInSeconds: StateFlow<Long> = _timeInSeconds

    private val _isTimerRunning = MutableStateFlow(true)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _totalBPM = MutableStateFlow(0L)
    val totalBPM: StateFlow<Long> = _totalBPM

    private val sensorManager: SensorManager =
        application.getSystemService(SENSOR_SERVICE) as SensorManager
    private val heartSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    private var isSensorRegistered = false
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (event == null) return@launch
            val value = event.values.getOrNull(0) ?: return@launch
            val bpm = value.toDouble()
            if (bpm > 0.0) {
                _heartRateBpm.value = bpm
            }
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

    fun increaseTimer() {
        viewModelScope.launch(Dispatchers.IO) {
            _timeInSeconds.value++
        }
    }

    fun toggleIsTimerRunning() {
        _isTimerRunning.value = !_isTimerRunning.value
    }

    fun increaseTotalBPM() {
        viewModelScope.launch(Dispatchers.IO) {
            _totalBPM.value += heartRateBpm.value?.toInt() ?: 0
        }
    }
}

const val TAG = "FitnessViewModel"