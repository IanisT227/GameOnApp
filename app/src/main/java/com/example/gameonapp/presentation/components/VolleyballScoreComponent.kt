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
import com.example.gameonapp.data.local.model.VolleyballScore
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.getScoreItemBackground

@Composable
fun VolleyballScoreComponent(gameViewModel: GameViewModel) {
    val scores: VolleyballScore by gameViewModel.volleyballScores.collectAsState()
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }

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

        }
    }
}