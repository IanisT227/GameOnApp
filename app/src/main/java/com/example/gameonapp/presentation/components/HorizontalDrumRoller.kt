package com.example.gameonapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun HorizontalDrumRoller(
    values: List<String>,
    defaultValue: String = values[values.size / 2],
    unit: String = "",
    onValueConfirmed: (String) -> Unit
) {
    var selectedIndex by remember {
        mutableIntStateOf(values.indexOf(defaultValue).takeIf { it >= 0 } ?: 0)
    }

    // Debounce: fire onValueConfirmed 600ms after the user stops dragging
    LaunchedEffect(selectedIndex) {
        delay(400)
        onValueConfirmed(values[selectedIndex])
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()  // ensures Column itself is centered
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()             // stretch to parent so centering works
                .height(40.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        val steps = (-dragAmount / 30f).roundToInt()
                        selectedIndex = (selectedIndex + steps)
                            .coerceIn(0, values.lastIndex)
                    }
                },
            contentAlignment = Alignment.Center  // centers the Row + highlight together
        ) {
            // Selection highlight — sits behind the Row
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 36.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(6.dp)
                    )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center  // Row itself is also centered
            ) {
                for (offset in -2..2) {
                    val itemIdx = selectedIndex + offset
                    val isSelected = offset == 0
                    val alpha = when (kotlin.math.abs(offset)) {
                        0 -> 1f; 1 -> 0.5f; else -> 0.2f
                    }
                    val fontSize = if (isSelected) 20.sp else 14.sp

                    Box(
                        modifier = Modifier.width(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (itemIdx in values.indices) values[itemIdx] else "",
                            fontSize = fontSize,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}