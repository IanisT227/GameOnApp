package com.example.gameonapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.Text
import com.example.gameonapp.data.local.model.RacquetSportsScoreValues
import com.example.gameonapp.utils.DECREMENT
import com.example.gameonapp.utils.INCREMENT

@Composable
fun TennisTeamComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    currentScore: RacquetSportsScoreValues = RacquetSportsScoreValues.ZERO,
    name: String
) {
    Column(
        modifier = modifier
            .clickable(enabled = true, onClick = { onClick() })
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    )
    {
        Text(
           name, style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            currentScore.display, style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.ExtraBold,
            )
        )
    }
}

@Composable
fun TennisScoringButtonRow(
    modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            onClick = { onClick(DECREMENT) },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
            enabled = enabled
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Outlined.Remove,
                    contentDescription = "Decrease"
                )
            }
        }

        Button(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            onClick = { onClick(INCREMENT) },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Increase"
                )
            }
        }
    }
}