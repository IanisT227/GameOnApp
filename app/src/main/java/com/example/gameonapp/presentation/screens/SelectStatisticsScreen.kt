package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import com.example.gameonapp.presentation.components.ActivitySummaryCard
import com.example.gameonapp.presentation.components.SportSummaryCard
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.google.android.horologist.compose.layout.responsivePaddingDefaults
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectStatisticsScreen(navController: NavController) {
    val gameViewModel = koinViewModel<GameViewModel>()
    val fitnessViewModel = koinViewModel<FitnessViewModel>()
    val maxBpm by fitnessViewModel.maxBpmFlow.collectAsState()
    val listState = rememberTransformingLazyColumnState()
    val gameHistoryState by gameViewModel.gameHistoryState.collectAsState()
    val transformationSpec = rememberTransformationSpec()

    LaunchedEffect(Unit) {
        gameViewModel.getGamesHistory()
    }
    if (gameHistoryState.gameList.isNotEmpty()) ScreenScaffold(
        scrollState = listState, modifier = Modifier
            .fillMaxSize()
            .background(
                brush = backgroundGradient
            )
    ) {
        TransformingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = responsivePaddingDefaults()
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                ListHeader {
                    Text(
                        "Games History", style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            item {
                ActivitySummaryCard(
                    totalTime = gameHistoryState.totalTime,
                    totalGames = gameHistoryState.gameList.size,
                    maxBpm = maxBpm
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            for (gameEntity in gameHistoryState.gameList) {
                item {
                    SportSummaryCard(
                        modifier = Modifier, contentData = gameEntity, navigateToDetails = {
                            navController.navigate("statisticsExpanded/${gameEntity.gameId}")
                        }, SurfaceTransformation(transformationSpec)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }

    }
}