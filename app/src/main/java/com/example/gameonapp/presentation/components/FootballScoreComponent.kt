package com.example.gameonapp.presentation.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.DECREMENT
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.INCREMENT
import java.util.Locale

@Composable
fun FootballScoreComponent(modifier: Modifier = Modifier, gameViewModel: GameViewModel) {
    val scores: SimpleScore by gameViewModel.simpleScores.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TeamBox(
                teamName = "Home",
                onClick = { operation ->
                    gameViewModel.adjustFootballScore(HOME, operation)
                },
                score = scores.home
            )
            TeamBox(
                teamName = "Away", onClick = { operation ->
                    gameViewModel.adjustFootballScore(AWAY, operation)
                },
                score = scores.away
            )
        }
        Button(
            modifier = Modifier.size(36.dp),
            onClick = { gameViewModel.resetScore() },
            contentPadding = PaddingValues(0.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.RestartAlt,
                    contentDescription = "Reset",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun TeamBox(
    modifier: Modifier = Modifier,
    teamName: String,
    onClick: (Boolean) -> Unit,
    score: Int,
) {
    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            teamName.uppercase(Locale.ROOT), style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            score.toString(), style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp,
            )
        )
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                onClick = { onClick(DECREMENT) },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                enabled = score != 0
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
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                modifier = Modifier.size(36.dp),
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
}