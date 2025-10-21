package com.example.gameonapp.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Shapes
import androidx.wear.compose.material3.ColorScheme


// 🧱 Manual color scheme
val ActiveOrangeColorScheme = ColorScheme(
    primary = OrangePrimary,
    onPrimary = TextOnDark,
    secondary = OrangeSecondary,
    onSecondary = Color.Black,
    tertiary = OrangePrimary,
    onTertiary = TextOnDark,
    background = BackgroundDarkGray,
    onBackground = TextOnDark,
    surfaceContainer = BackgroundDarkGray,
    onSurface = TextOnDark,
    onSurfaceVariant = TextOnDark,
    outline = OrangeSecondary,
    outlineVariant = OrangePressed,
)


@Composable
fun GameOnAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ActiveOrangeColorScheme,
        shapes = Shapes(),
        content = content
    )
}