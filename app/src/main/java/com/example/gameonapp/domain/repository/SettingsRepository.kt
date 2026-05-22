package com.example.gameonapp.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.gameonapp.data.local.model.Gender
import com.example.gameonapp.data.local.model.SettingsKeys
import com.example.gameonapp.data.local.model.UnitSystem
import com.example.gameonapp.utils.defaultHeight
import com.example.gameonapp.utils.defaultWeight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    val height: Flow<String> = dataStore.data.map {
        it[SettingsKeys.HEIGHT] ?: defaultHeight(
            UnitSystem.Metric
        )
    }

    val weight: Flow<String> = dataStore.data.map {
        it[SettingsKeys.WEIGHT] ?: defaultWeight(
            UnitSystem.Metric
        )
    }

    val gender: Flow<Gender> = dataStore.data.map { prefs ->
        prefs[SettingsKeys.GENDER]?.let { runCatching { Gender.valueOf(it) }.getOrNull() }
            ?: Gender.Male
    }

    val units: Flow<UnitSystem> = dataStore.data.map { prefs ->
        prefs[SettingsKeys.UNITS]?.let { runCatching { UnitSystem.valueOf(it) }.getOrNull() }
            ?: UnitSystem.Metric
    }

    val timeFormat: Flow<String> = dataStore.data.map { it[SettingsKeys.TIME_FORMAT] ?: "24h" }

    suspend fun saveHeight(value: String) {
        dataStore.edit { it[SettingsKeys.HEIGHT] = value }
    }

    suspend fun saveWeight(value: String) {
        dataStore.edit { it[SettingsKeys.WEIGHT] = value }
    }

    suspend fun saveGender(value: Gender) {
        dataStore.edit { it[SettingsKeys.GENDER] = value.name }
    }

    suspend fun saveUnits(value: UnitSystem) {
        dataStore.edit { it[SettingsKeys.UNITS] = value.name }
    }

    suspend fun saveTimeFormat(value: String) {
        dataStore.edit { it[SettingsKeys.TIME_FORMAT] = value }
    }
}