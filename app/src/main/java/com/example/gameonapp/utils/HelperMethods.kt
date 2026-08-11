package com.example.gameonapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import com.example.gameonapp.data.local.model.UnitSystem
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

private const val KG_PER_LB = 0.45359237

fun poundsToKg(lb: Double): Double = lb * KG_PER_LB
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

@Composable
fun Modifier.getScoreItemBackground(isSelected: Boolean): Modifier {
    val shape = RoundedCornerShape(20.dp)

    return if (isSelected) {
        this
            .background(MaterialTheme.colorScheme.primary, shape)
    } else {
        this
            .border(2.dp, MaterialTheme.colorScheme.primary, shape)
    }
}

fun fetchHeightList(unitSystem: UnitSystem): List<String> =
    when (unitSystem) {
        UnitSystem.Metric -> (120..250).map { "$it" }
        UnitSystem.Imperial -> {
            (48..98).map { totalInches ->
                val feet = totalInches / 12
                val inches = totalInches % 12
                "$feet′ $inches″"
            }
        }
    }

fun fetchWeightList(unitSystem: UnitSystem): List<String> =
    when (unitSystem) {
        UnitSystem.Metric -> (20..300).map { "$it" }
        UnitSystem.Imperial -> (44..660).map { "$it" }
    }

fun defaultHeight(unitSystem: UnitSystem): String =
    when (unitSystem) {
        UnitSystem.Metric -> "170"
        UnitSystem.Imperial -> "5′ 7″"   // ~170cm
    }

fun defaultWeight(unitSystem: UnitSystem): String =
    when (unitSystem) {
        UnitSystem.Metric -> "70"
        UnitSystem.Imperial -> "154" // ~70kg
    }