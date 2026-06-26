package com.example.gameonapp.data.local.model

import com.example.gameonapp.utils.defaultHeight
import com.example.gameonapp.utils.defaultWeight

data class SettingsUiState(
    val height: String = defaultHeight(unitSystem = UnitSystem.Metric),
    val weight: String = defaultWeight(unitSystem = UnitSystem.Metric),
    val age: Int = 30,
    val gender: Gender = Gender.Male,
    val units: UnitSystem = UnitSystem.Metric,
    val timeFormat: String = "24h"
)
