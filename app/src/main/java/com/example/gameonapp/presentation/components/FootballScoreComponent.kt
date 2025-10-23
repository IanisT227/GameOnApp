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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import com.example.gameonapp.utils.DECREMENT
import com.example.gameonapp.utils.INCREMENT
import com.example.gameonapp.utils.adjustFootballScore

@Composable
fun FootballScoreComponent(modifier: Modifier = Modifier) {
    var scores by rememberSaveable { mutableStateOf(listOf(0, 0)) }
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
                    adjustFootballScore(
                        operation,
                        { scores = scores.toMutableList().also { scoreList -> scoreList[0] = it } },
                        scores[0]
                    )
                },
                score = scores[0]
            )
            TeamBox(
                teamName = "Away", onClick = { operation ->
                    adjustFootballScore(
                        operation,
                        { scores = scores.toMutableList().also { scoreList -> scoreList[1] = it } },
                        scores[1]
                    )
                },
                score = scores[1]
            )
        }
        Button(
            modifier = Modifier.size(36.dp),
            onClick = { scores = List(scores.size) { 0 } },
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
            teamName, style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        )
        Text(
            score.toString(), style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp,
            )
        )
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
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
            Spacer(modifier = Modifier.width(12.dp))
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
        }
    }
}