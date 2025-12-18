package com.example.gameonapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import java.util.Locale

@Composable
fun VolleyTeamComponent(
    modifier: Modifier,
    teamName: String,
    teamScore: Int,
    teamSetsWon: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(enabled = true, onClick = { onClick() })
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    )
    {
        Text(
            teamName.uppercase(Locale.ROOT), style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            teamScore.toString(), style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.ExtraBold,
            )
        )
        SetsWonRow(teamSetsWon)
    }
}

@Composable
fun SetsWonRow(setsWon: Int, totalSets: Int = 3) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSets) { index ->
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = if (index < setsWon) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}