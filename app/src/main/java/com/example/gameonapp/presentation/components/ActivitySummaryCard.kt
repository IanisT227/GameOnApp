package com.example.gameonapp.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.SportsScore
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.gameonapp.R
import com.example.gameonapp.utils.formatTime

@Composable
fun ActivitySummaryCard(
    totalCalories: Int,
    totalGames: Int,
    totalTime: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(vertical = 8.dp, horizontal = 6.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.sports_summary),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        DetailsRow(
            image = Icons.Outlined.Timer,
            legendText = stringResource(R.string.total_time),
            valueText = formatTime(totalTime)
        )
        Spacer(modifier = Modifier.height(4.dp))
        DetailsRow(
            image = Icons.Outlined.SportsScore,
            legendText = stringResource(id = R.string.total_games),
            valueText = totalGames.toString()
        )
        Spacer(modifier = Modifier.height(4.dp))
        DetailsRow(
            image = Icons.Outlined.LocalFireDepartment,
            legendText = stringResource(id = R.string.total_calories),
            valueText = totalCalories.toString()
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}