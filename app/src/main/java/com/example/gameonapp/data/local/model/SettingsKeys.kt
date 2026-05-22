package com.example.gameonapp.data.local.model

import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val HEIGHT = stringPreferencesKey("height")
    val WEIGHT = stringPreferencesKey("weight")
    val GENDER = stringPreferencesKey("gender")
    val UNITS = stringPreferencesKey("units")
    val TIME_FORMAT = stringPreferencesKey("time_format")
}