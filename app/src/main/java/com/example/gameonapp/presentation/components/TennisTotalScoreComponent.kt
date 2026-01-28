package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

@Composable
fun TennisTotalScoreComponent(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("6", style = MaterialTheme.typography.bodySmall)
            Text("6", style = MaterialTheme.typography.bodySmall)
        }
        Column {
            Text("6", style = MaterialTheme.typography.bodySmall)
            Text("6", style = MaterialTheme.typography.bodySmall)
        }
        Column {
            Text("6", style = MaterialTheme.typography.bodySmall)
            Text("6", style = MaterialTheme.typography.bodySmall)
        }
    }
}