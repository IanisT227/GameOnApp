package com.example.gameonapp.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.AppCard
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.Text
import com.example.gameonapp.data.local.model.GameEntity

@Composable
fun WorkoutSummaryCard(
    modifier: Modifier,
    contentData: GameEntity,
    navigateToDetails: () -> Unit,
) {
    AppCard(
        modifier = modifier,
        onClick = { navigateToDetails() },
        appImage = {
            Icon(Icons.Filled.FitnessCenter, contentDescription = null)
        },
        appName = { Text(contentData.gameType.toString()) },
        title = { Text(contentData.gameType.toString()) },
        time = { Text(contentData.durationSeconds.toString()) },
        content = {}
    )
}