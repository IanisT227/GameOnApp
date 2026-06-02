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
import com.example.gameonapp.data.local.model.RacquetSportsScoreValues
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.INCREMENT
import com.example.gameonapp.utils.getScoreItemBackground

@Composable
fun TennisScoreComponent(gameViewModel: GameViewModel, onGameFinished: (() -> Unit)) {
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }
    val tennisMatchState by gameViewModel.tennisState.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (tennisMatchState.matchState.isMatchFinished() && showDialog)
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
                TennisTotalScoreRow(
                    tennisMatchState = tennisMatchState.matchState,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                )
                {
                    TennisTeamComponent(
                        modifier = Modifier
                            .weight(1f)
                            .getScoreItemBackground(isSelected = selectedTeam),
                        onClick = { selectedTeam = HOME },
                        currentScore = tennisMatchState.matchState.homeScore.points,
                        name = tennisMatchState.matchState.homeScore.display
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TennisTeamComponent(
                        modifier = Modifier
                            .weight(1f)
                            .getScoreItemBackground(isSelected = !selectedTeam),
                        onClick = { selectedTeam = AWAY },
                        currentScore = tennisMatchState.matchState.awayScore.points,
                        name = tennisMatchState.matchState.awayScore.display
                    )
                }

                TennisScoringButtonRow(
                    onClick = {
                        if (it == INCREMENT)
                            gameViewModel.addTennisPoint(isHome = selectedTeam)
                        else
                            gameViewModel.undoTennisPoint()
                    },
                    enabled = if (selectedTeam == HOME) {
                        tennisMatchState.matchState.homeScore.points != RacquetSportsScoreValues.ZERO
                    } else {
                        tennisMatchState.matchState.awayScore.points != RacquetSportsScoreValues.ZERO
                    }
                )
            }
        }
    }
}