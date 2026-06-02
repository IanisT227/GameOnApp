package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gameonapp.data.local.model.VolleyballSet
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.INCREMENT
import com.example.gameonapp.utils.getScoreItemBackground

@Composable
fun VolleyballScoreComponent(gameViewModel: GameViewModel, onGameFinished: (() -> Unit)) {
    val volleyballState: GameViewModel.VolleyballUiState by gameViewModel.volleyballState.collectAsState()
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }

    var showDialog by rememberSaveable { mutableStateOf(true) }


    if (volleyballState.isFinished && showDialog)
        EndGameDialog(
            isVisible = true,
            onDismiss = { },
            onConfirmClick = {
                onGameFinished()
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
                    teamScore = volleyballState.score.scoresPerSet[volleyballState.score.currentSet].pointsHome,
                    teamSetsWon = volleyballState.score.setsHome,
                    onClick = { selectedTeam = HOME })
                Spacer(modifier = Modifier.width(10.dp))
                VolleyTeamComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(!selectedTeam),
                    teamName = "Away",
                    teamScore = volleyballState.score.scoresPerSet[volleyballState.score.currentSet].pointsAway,
                    teamSetsWon = volleyballState.score.setsAway,
                    onClick = { selectedTeam = AWAY })
            }
            Spacer(modifier = Modifier.height(4.dp))
            ScoringRow(onClick = {
                gameViewModel.adjustVolleyballScore(selectedTeam, if (it == INCREMENT) 1 else -1)
            },
                enabled = checkIfEnabled(
                    selectedTeam,
                    volleyballState.score.scoresPerSet[volleyballState.score.currentSet]
                )
            )
        }
    }
}

private fun checkIfEnabled(selectedTeam: Boolean, currentSet: VolleyballSet): Boolean {
    return if (selectedTeam == HOME)
        currentSet.pointsHome > 0
    else
        currentSet.pointsAway > 0
}