package com.example.gameonapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

fun adjustFootballScore(adjustType: Boolean, update: (Int) -> Unit, currentValue: Int) {
    val newValue = if (adjustType == INCREMENT) currentValue.plus(1) else currentValue.minus(1)
    update(newValue)
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}

fun fetchIconForGameType(gameType: GameType): ImageVector {
    return when (gameType) {
        GameType.FOOTBALL -> Icons.Outlined.SportsSoccer
        GameType.TENNIS -> Icons.Outlined.SportsTennis
        GameType.BASKETBALL -> Icons.Outlined.SportsBasketball
        GameType.VOLLEYBALL -> Icons.Outlined.SportsVolleyball
        GameType.PADEL -> Icons.Outlined.SportsTennis
        GameType.OTHER -> Icons.Outlined.SportsScore
    }
}

fun String.toPascalCase(): String {
    return this
        .split(Regex("[^a-zA-Z0-9]+"))
        .filter { it.isNotBlank() }
        .joinToString("") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
}

fun formatDate(date: Date): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")
    val localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    return localDate.format(formatter)
}