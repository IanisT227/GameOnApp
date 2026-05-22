package com.example.gameonapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gameonapp.data.local.model.Gender
import com.example.gameonapp.data.local.model.SettingsUiState
import com.example.gameonapp.data.local.model.UnitSystem
import com.example.gameonapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = combine(
        repository.height,
        repository.weight,
        repository.gender,
        repository.units,
        repository.timeFormat
    ) { height, weight, gender, units, timeFormat ->
        SettingsUiState(height, weight, gender, units, timeFormat)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun saveHeight(value: String) = viewModelScope.launch {
        repository.saveHeight(value)
    }

    fun saveWeight(value: String) = viewModelScope.launch {
        repository.saveWeight(value)
    }

    fun saveGender(value: Gender) = viewModelScope.launch {
        repository.saveGender(value)
    }

    fun saveUnits(value: String) = viewModelScope.launch {
        val unit = when (value) {
            UnitSystem.Imperial.name -> UnitSystem.Imperial
            else -> UnitSystem.Metric
        }
        repository.saveUnits(unit)
    }

    fun saveTimeFormat(value: String) = viewModelScope.launch {
        repository.saveTimeFormat(value)
    }
}