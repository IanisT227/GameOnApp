package com.example.gameonapp.utils

fun adjustFootballScore(adjustType: Boolean, update: (Int) -> Unit, currentValue: Int) {
    val newValue = if (adjustType == INCREMENT) currentValue.plus(1) else currentValue.minus(1)
    update(newValue)
}