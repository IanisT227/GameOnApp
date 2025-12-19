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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import com.example.gameonapp.data.local.model.VolleyballScore
import com.example.gameonapp.data.local.model.VolleyballSet
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.getScoreItemBackground

@Composable
fun VolleyballScoreComponent(gameViewModel: GameViewModel, onGameFinished: (() -> Unit)) {
    val scores: VolleyballScore by gameViewModel.volleyballScores.collectAsState()
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }
    val gameFinished: Boolean by gameViewModel.volleyballGameFinished.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(true) }


    if (gameFinished && showDialog)
        EndGameDialog(
            isVisible = true,
            onDismiss = {showDialog = false},
            onConfirmClick = {
                onGameFinished()
                showDialog = false
            })
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                VolleyTeamComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(selectedTeam),
                    teamName = "Home",
                    teamScore = scores.scoresPerSet[scores.currentSet].pointsHome,
                    teamSetsWon = scores.setsHome,
                    onClick = { selectedTeam = HOME })
                Spacer(modifier = Modifier.width(10.dp))
                VolleyTeamComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(!selectedTeam),
                    teamName = "Away",
                    teamScore = scores.scoresPerSet[scores.currentSet].pointsAway,
                    teamSetsWon = scores.setsAway,
                    onClick = { selectedTeam = AWAY })
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    onClick = { gameViewModel.adjustVolleyballScore(selectedTeam, -1) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    enabled = checkIfEnabled(selectedTeam, scores.scoresPerSet[scores.currentSet])
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = "Decrease"
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    onClick = { gameViewModel.adjustVolleyballScore(selectedTeam, 1) },
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Increase"
                        )
                    }
                }
            }
        }
    }
}

private fun checkIfEnabled(selectedTeam: Boolean, currentSet: VolleyballSet): Boolean {
    return if (selectedTeam == HOME)
        currentSet.pointsHome > 0
    else
        currentSet.pointsAway > 0
}