package com.example.gameonapp.utils

fun adjustFootballScore(adjustType: Boolean, update: (Int) -> Unit, currentValue: Int) {
    val newValue = if (adjustType == INCREMENT) currentValue.plus(1) else currentValue.minus(1)
    update(newValue)
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}