package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.gameonapp.data.local.model.TennisScoreValues
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.INCREMENT
import com.example.gameonapp.utils.getScoreItemBackground

@Composable
fun TennisScoreComponent(gameViewModel: GameViewModel, onGameFinished: (() -> Unit)) {
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }
    val tennisMatchState by gameViewModel.tennisMatchState.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (tennisMatchState.matchWinner != null && showDialog)
        {
            EndGameDialog(
                isVisible = true,
                onDismiss = { showDialog = false },
                onConfirmClick = {
                    onGameFinished()
                    showDialog = false
                })
        }
        else
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    TennisTeamComponent(
                        modifier = Modifier
                            .weight(1f)
                            .getScoreItemBackground(isSelected = selectedTeam),
                        onClick = { selectedTeam = HOME },
                        currentScore = tennisMatchState.homeScore.points,
                        name = tennisMatchState.homeScore.display
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TennisTeamComponent(
                        modifier = Modifier
                            .weight(1f)
                            .getScoreItemBackground(isSelected = !selectedTeam),
                        onClick = { selectedTeam = AWAY },
                        currentScore = tennisMatchState.awayScore.points,
                        name = tennisMatchState.awayScore.display
                    )
                }
                TennisTotalScoreComponent(
                    tennisMatchState = tennisMatchState,
                )
                TennisScoringButtonRow(
                    onClick = {
                        if (it == INCREMENT)
                            gameViewModel.addPoint(isHome = selectedTeam)
                        else
                            gameViewModel.removePoint(isHome = selectedTeam)
                    },
                    enabled = if (selectedTeam == HOME) {
                        tennisMatchState.homeScore.points != TennisScoreValues.ZERO
                    } else {
                        tennisMatchState.awayScore.points != TennisScoreValues.ZERO
                    }
                )
            }
        }
    }
}