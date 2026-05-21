package com.example.gameonapp.data.local.model

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val HEIGHT = intPreferencesKey("height")
    val WEIGHT = intPreferencesKey("weight")
    val GENDER = stringPreferencesKey("gender")
}