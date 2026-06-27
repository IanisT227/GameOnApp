package com.example.gameonapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.MaterialTheme
import com.example.gameonapp.R
import com.example.gameonapp.data.local.model.SimpleScore
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.AWAY
import com.example.gameonapp.utils.DECREMENT
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.INCREMENT
import com.example.gameonapp.utils.getScoreItemBackground
import java.util.Locale

@Composable
fun FootballScoreComponent(gameViewModel: GameViewModel) {
    val scores: SimpleScore by gameViewModel.footballScore.collectAsState()
    var selectedTeam by rememberSaveable { mutableStateOf(HOME) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TeamBox(
                modifier = Modifier
                    .weight(1f)
                    .getScoreItemBackground(selectedTeam),
                teamName = stringResource(id = R.string.home),
                onClick = {
                    selectedTeam = HOME
                },
                score = scores.home
            )
            Spacer(modifier = Modifier.width(6.dp))
            TeamBox(
                modifier = Modifier
                    .weight(1f)
                    .getScoreItemBackground(!selectedTeam),
                teamName = stringResource(id = R.string.away), onClick = {
                    selectedTeam = AWAY
                },
                score = scores.away
            )
        }
        ScoringRow(onClick = {
            gameViewModel.adjustFootballScore(
                selectedTeam,
                if (it == INCREMENT) INCREMENT else DECREMENT
            )
        }, enabled = checkIfEnabled(selectedTeam, scores))
    }
}

@Composable
fun TeamBox(
    modifier: Modifier = Modifier,
    teamName: String,
    onClick: () -> Unit,
    score: Int,
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .clickable(enabled = true, onClick = { onClick() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            teamName.uppercase(Locale.ROOT), style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Text(
            score.toString(), style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp,
            )
        )
    }
}

private fun checkIfEnabled(selectedTeam: Boolean, scores: SimpleScore): Boolean {
    return if (selectedTeam == HOME)
        scores.home > 0
    else
        scores.away > 0
}