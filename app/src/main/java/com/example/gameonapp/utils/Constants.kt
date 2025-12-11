package com.example.gameonapp.utils

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val INCREMENT = true
const val DECREMENT = false
const val SCORE_COMPONENT = 0
const val FITNESS_COMPONENT = 1
const val PULSE_METER = true
const val FOOTBALL = "football"
const val TENNIS = "tennis"
const val PADEL = "padel"
const val BASKETBALL = "basketball"
const val VOLLEYBALL = "volleyball"

const val HOME = true
const val AWAY = false

val sportsList = listOf(
    Pair("Football", Icons.Outlined.SportsSoccer),
    Pair("Volleyball", Icons.Outlined.SportsVolleyball),
    Pair("Basketball", Icons.Outlined.SportsBasketball),
    Pair("Tennis", Icons.Outlined.SportsTennis),
    Pair("Padel", Icons.Outlined.SportsTennis),
)

val Context.dataStore by preferencesDataStore("statistics")

object PreferencesKeys {
    val MAX_BPM_KEY = intPreferencesKey("max_bpm_key")
}
