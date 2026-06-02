package com.example.gameonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.gameonapp.data.local.model.GameScore
import com.example.gameonapp.data.local.model.PadelMatchState
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.data.local.model.TennisMatchState
import com.example.gameonapp.data.local.model.VolleyballScore
import com.example.gameonapp.presentation.theme.ColorScheme
import com.example.gameonapp.utils.GameType
import com.example.gameonapp.utils.toPascalCase

@Composable
fun ScoreColumn(modifier: Modifier = Modifier, gameType: GameType, gameScore: GameScore) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.EmojiEvents,
            contentDescription = gameType.toString()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = gameType.toString().toPascalCase(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        when (gameScore) {
            is SimpleScore -> Text(
                text = "${gameScore.home} - ${gameScore.away}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            is VolleyballScore -> {
                Text(
                    text = gameScore.getFinalScore(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gameScore.getSetScores(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            is TennisMatchState -> {
                Text(
                    text = gameScore.getFinalScore(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(2.dp)
                        .background(color = ColorScheme.primary)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gameScore.getSetScores(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )
                )
            }

            is PadelMatchState -> {
                Text(
                    text = gameScore.getFinalScore(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = gameScore.getSetScores(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            else -> Text("Unknown score")
        }
    }
}