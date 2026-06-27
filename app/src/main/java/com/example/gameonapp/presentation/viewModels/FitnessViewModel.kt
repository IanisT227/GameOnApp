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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FitnessViewModel(private val application: Application) :
    ViewModel(), SensorEventListener {

    private val _heartRateBpm = MutableStateFlow<Double?>(null)
    val heartRateBpm: StateFlow<Double?> = _heartRateBpm

    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories

    private val _maxBpm = MutableStateFlow(0)
    val maxBpm: StateFlow<Int> = _maxBpm

    private val _timeInSeconds = MutableStateFlow(0)
    val timeInSeconds: StateFlow<Int> = _timeInSeconds

    private val _isTimerRunning = MutableStateFlow(true)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _totalBPM = MutableStateFlow(0L)
    val totalBPM: StateFlow<Long> = _totalBPM

    private var weightKg: Double? = null
    private var heightCm: Double? = null
    private var isMale: Boolean? = null

    fun updateUserProfile(weightKg: Double?, heightCm: Double?, isMale: Boolean?) {
        this.weightKg = weightKg
        this.heightCm = heightCm
        this.isMale = isMale
    }


    private val sensorManager: SensorManager =
        application.getSystemService(SENSOR_SERVICE) as SensorManager
    private val heartSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    private var isSensorRegistered = false

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        // FIX: removed unnecessary Dispatchers.IO coroutine — StateFlow.value
        // assignment is already thread-safe
        val bpm = event?.values?.getOrNull(0)?.toDouble() ?: return
        if (bpm > 0.0) _heartRateBpm.value = bpm
    }

    fun setMaxBpm(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _maxBpm.value = value
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
            _timeInSeconds.update { it + 1 }
            accumulateCalories()
        }
    }

    fun toggleIsTimerRunning() {
        _isTimerRunning.update { !it }
    }

    fun increaseTotalBPM() {
        viewModelScope.launch(Dispatchers.IO) {
            // FIX: capture once to avoid reading the value twice with a potential
            // change in between (race condition)
            val currentBpm = _heartRateBpm.value?.toInt() ?: return@launch
            _totalBPM.update { it + currentBpm }
            if (currentBpm > maxBpm.value) setMaxBpm(currentBpm)
        }
    }

    private fun accumulateCalories() {
        val bpm = _heartRateBpm.value ?: return
        val weight = weightKg ?: return
        val male = isMale ?: return
        val age = 30.0

        val calPerMin = if (male) {
            (-55.0969 + (0.6309 * bpm) + (0.1988 * weight) + (0.2017 * age)) / 4.184
        } else {
            (-20.4022 + (0.4472 * bpm) + (0.1263 * weight) + (0.074 * age)) / 4.184
        }

        // FIX: update{} is atomic, avoids lost updates if called concurrently
        _calories.update { it + (calPerMin / 60.0).coerceAtLeast(0.0) }
    }

}

const val TAG = "FitnessViewModel"