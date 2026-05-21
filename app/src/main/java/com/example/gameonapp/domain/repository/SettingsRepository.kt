package com.example.gameonapp.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.gameonapp.data.local.model.Gender
import com.example.gameonapp.data.local.model.SettingsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    val height: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.HEIGHT] ?: 170 }

    val weight: Flow<Int> = dataStore.data
        .map { it[SettingsKeys.WEIGHT] ?: 70 }

    val gender: Flow<Gender> = dataStore.data
        .map { prefs ->
            prefs[SettingsKeys.GENDER]
                ?.let { runCatching { Gender.valueOf(it) }.getOrNull() }
                ?: Gender.Male
        }

    suspend fun saveHeight(value: Int) {
        dataStore.edit { it[SettingsKeys.HEIGHT] = value }
    }

    suspend fun saveWeight(value: Int) {
        dataStore.edit { it[SettingsKeys.WEIGHT] = value }
    }

    suspend fun saveGender(value: Gender) {
        dataStore.edit { it[SettingsKeys.GENDER] = value.name }
    }
}