package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun FitnessComponent(
    modifier: Modifier = Modifier,
    timeInSeconds: Long,
    isRunning: Boolean,
    onPause: (Boolean) -> Unit,
    onReset: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = formatTime(timeInSeconds),
            style = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onPause(!isRunning) }) {
            Text(if (isRunning) "Stop" else "Start")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            onReset()
        }) {
            Text("Reset")
        }
    }
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}