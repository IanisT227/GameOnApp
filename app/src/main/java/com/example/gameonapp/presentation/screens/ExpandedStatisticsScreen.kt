package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.composables.icons.lucide.Heart
import com.composables.icons.lucide.Lucide
import com.example.gameonapp.presentation.components.DeleteGameDialog
import com.example.gameonapp.presentation.components.DetailsRow
import com.example.gameonapp.presentation.components.ScoreColumn
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.formatDate
import com.example.gameonapp.utils.formatTime
import com.google.android.horologist.compose.layout.responsivePaddingDefaults
import org.koin.androidx.compose.koinViewModel


@Composable
fun ExpandedStatisticsScreen(gameId: Long, navController: NavController) {
    val gameViewModel = koinViewModel<GameViewModel>()
    val gameData by gameViewModel.gameData.collectAsState()
    val listState = rememberTransformingLazyColumnState()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog)
        DeleteGameDialog(
            isVisible = true,
            onDismiss = { showDialog = false },
            onConfirmClick = {
                gameViewModel.removeGame(gameId)
                showDialog = false
                navController.popBackStack()
            })


    if (gameId != 0L) {
        LaunchedEffect(Unit) {
            gameViewModel.getGameById(gameId)
        }
    }
    ScreenScaffold(
        scrollState = listState, modifier = Modifier
            .fillMaxSize()
            .background(
                brush = backgroundGradient
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TransformingLazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxSize()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                contentPadding = responsivePaddingDefaults(),
            ) {
                item {
                    ListHeader {
                        Text(
                            text = "Game Details",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                }
                item {
                    ScoreColumn(
                        gameType = gameData.gameType,
                        gameScore = gameData.score.toString()
                    )
                }
                item { Spacer(modifier = Modifier.height(6.dp)) }
                item {
                    Column(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                    ) {
                        DetailsRow(
                            image = Icons.Outlined.CalendarMonth,
                            legendText = "Date",
                            valueText = formatDate(gameData.matchDate)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DetailsRow(
                            image = Icons.Outlined.WatchLater,
                            legendText = "Duration",
                            valueText = formatTime(gameData.durationSeconds)
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(6.dp)) }
                item {
                    Column(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                    ) {
                        DetailsRow(
                            image = Icons.Outlined.LocalFireDepartment,
                            legendText = "Calories",
                            valueText = gameData.averageBPM.toString()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DetailsRow(
                            image = Icons.Outlined.MonitorHeart,
                            legendText = "Max. BPM",
                            valueText = gameData.averageBPM.toString()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        DetailsRow(
                            image = Lucide.Heart,
                            legendText = "Avg. BPM",
                            valueText = gameData.averageBPM.toString()
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(6.dp)) }
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    ) {
                        Text(
                            text = "Remove game",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}