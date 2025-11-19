package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.layout.responsivePaddingDefaults
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpandedStatisticsScreen(modifier: Modifier = Modifier, gameId: Long) {
    val gameViewModel = koinViewModel<GameViewModel>()
    val gameData by gameViewModel.gameData.collectAsState()
    val listState = rememberTransformingLazyColumnState()
    if (gameId != 0L) {
        LaunchedEffect(Unit) {
            gameViewModel.getGameById(gameId)
        }
    }
    ScreenScaffold(
        scrollState = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = backgroundGradient
            )
    ) {
        TransformingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxRectangle()
                .padding(horizontal = 2.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = responsivePaddingDefaults()
        ) {
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(vertical = 30.dp),
                    text = gameData.durationSeconds.toString()
                )
            }


        }

    }
}