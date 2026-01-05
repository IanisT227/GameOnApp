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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.HOME
import com.example.gameonapp.utils.getScoreItemBackground

@Composable
fun TennisScoreComponent(gameViewModel: GameViewModel) {
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
            )
            {
                TennisTeamComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(isSelected = selectedTeam),
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(10.dp))
                TennisTeamComponent(
                    modifier = Modifier
                        .weight(1f)
                        .getScoreItemBackground(isSelected = !selectedTeam),
                    onClick = {}
                )
            }
        }
    }
}