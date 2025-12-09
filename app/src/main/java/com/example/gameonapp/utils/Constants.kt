package com.example.gameonapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball

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