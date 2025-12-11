package com.example.gameonapp.presentation.viewModels

import android.app.Application
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.utils.PreferencesKeys
import com.example.gameonapp.utils.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FitnessViewModel(private val application: Application) :
    ViewModel(), SensorEventListener {
    private val _heartRateBpm = MutableStateFlow<Double?>(null)
    val heartRateBpm: StateFlow<Double?> = _heartRateBpm
    private val _calories = MutableStateFlow(0.0)
    val calories: StateFlow<Double> = _calories

    private val _timeInSeconds = MutableStateFlow(0)
    val timeInSeconds: StateFlow<Int> = _timeInSeconds

    private val _isTimerRunning = MutableStateFlow(true)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning

    private val _totalBPM = MutableStateFlow(0L)
    val totalBPM: StateFlow<Long> = _totalBPM

    val maxBpmFlow: StateFlow<Int> = application.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.MAX_BPM_KEY] ?: 0 // Provide a default value
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
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

    fun setMaxBpm(value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            application.dataStore.edit { preferences ->
                preferences[PreferencesKeys.MAX_BPM_KEY] = value
            }
        }
    }

    fun getMaxBpm() = maxBpmFlow.value

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
            if ((heartRateBpm.value?.toInt() ?: 0) > maxBpmFlow.value) {
                setMaxBpm(heartRateBpm.value?.toInt() ?: 0)
            }
        }
    }
}

const val TAG = "FitnessViewModel"