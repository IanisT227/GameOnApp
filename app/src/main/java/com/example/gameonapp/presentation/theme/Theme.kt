package com.example.gameonapp.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Shapes
import androidx.wear.compose.material3.ColorScheme


// 🧱 Manual color scheme
val ActiveOrangeColorScheme = ColorScheme(
    primary = BluePrimary,
    onPrimary = TextOnDark,
    secondary = BlueSecondary,
    onSecondary = Color.Black,
    tertiary = BluePressed,
    onTertiary = TextOnDark,
    background = BackgroundDarkGray,
    onBackground = TextOnDark,
    surfaceContainer = BackgroundDarkGray,
    onSurface = TextOnDark,
    onSurfaceVariant = TextOnDark,
    outline = BlueSecondary,
    outlineVariant = BluePressed,
)

val backgroundGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF0B1A33), Color(0xFF101820)),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

@Composable
fun GameOnAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ActiveOrangeColorScheme,
        shapes = Shapes(),
        content = content
    )
}