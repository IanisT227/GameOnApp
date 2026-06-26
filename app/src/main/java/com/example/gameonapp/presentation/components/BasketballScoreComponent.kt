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
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.getScoreItemBackground
import java.util.Locale

@Composable
fun BasketballScoreComponent(gameViewModel: GameViewModel) {
    val scores: SimpleScore by gameViewModel.basketballScore.collectAsState()
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }
    val amounts = listOf(1, 2, 3)
    val isEnabled = if (selectedTeam == HOME) {
        scores.home != 0
    } else {
        scores.away != 0
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TeamScoreComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(selectedTeam),
                    teamScore = scores.home,
                    teamName = "Home",
                    onClick = { selectedTeam = HOME }
                )
                TeamScoreComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(!selectedTeam),
                    teamScore = scores.away,
                    teamName = "Away",
                    onClick = { selectedTeam = AWAY }
                )
            }
            ScoreButtonsRow(valuesList = amounts, onClick = { amount ->
                gameViewModel.adjustBasketballScore(selectedTeam, amount)
            })
            DecreaseScoreButton(
                onClick = { amount ->
                    gameViewModel.adjustBasketballScore(
                        selectedTeam,
                        amount
                    )
                },
                isEnabled = isEnabled
            )
        }
    }
}

@Composable
fun TeamScoreComponent(
    modifier: Modifier = Modifier,
    teamScore: Int,
    teamName: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clickable(enabled = true, onClick = { onClick() })
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = teamName.uppercase(Locale.ROOT),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = teamScore.toString(),
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.ExtraBold
            ),
        )
    }
}

@Composable
fun ScoreButtonsRow(valuesList: List<Int>, onClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        valuesList.forEach { amount ->
            Button(onClick = { onClick(amount) }, shape = CircleShape) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = "+ $amount", style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
fun DecreaseScoreButton(onClick: (Int) -> Unit, isEnabled: Boolean) {
    Button(
        modifier = Modifier.size(42.dp),
        enabled = isEnabled,
        shape = CircleShape,
        onClick = { onClick(-1) },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Outlined.Remove,
                contentDescription = "Decrease"
            )
        }
    }
}



