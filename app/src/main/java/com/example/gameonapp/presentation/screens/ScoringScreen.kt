package com.example.gameonapp.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.TimeText
import androidx.wear.widget.ConfirmationOverlay
import com.example.gameonapp.presentation.components.BasketballScoreComponent
import com.example.gameonapp.presentation.components.ExitGameDialog
import com.example.gameonapp.presentation.components.FitnessComponent
import com.example.gameonapp.presentation.components.FootballScoreComponent
import com.example.gameonapp.presentation.components.PadelScoreComponent
import com.example.gameonapp.presentation.components.TennisScoreComponent
import com.example.gameonapp.presentation.components.VolleyballScoreComponent
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.BASKETBALL
import com.example.gameonapp.utils.FITNESS_COMPONENT
import com.example.gameonapp.utils.FOOTBALL
import com.example.gameonapp.utils.GameType
import com.example.gameonapp.utils.PADEL
import com.example.gameonapp.utils.SCORE_COMPONENT
import com.example.gameonapp.utils.TENNIS
import com.example.gameonapp.utils.VOLLEYBALL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun ScoringScreen(
    sportName: String,
    navController: NavController,
) {
    val pagerState = rememberPagerState { 2 }
    val fitnessViewModel = koinViewModel<FitnessViewModel>()
    val gameViewModel = koinViewModel<GameViewModel>()
    var sensorsPermissionGranted by rememberSaveable { mutableStateOf(false) }
    val permissionToRequest = Manifest.permission.BODY_SENSORS
    val isTimerRunning by fitnessViewModel.isTimerRunning.collectAsState()
    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    var showOverlay by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()



    BackHandler(enabled = true) {
        showExitDialog = true
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        sensorsPermissionGranted = granted
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissionToRequest)
    }

    LaunchedEffect(isTimerRunning, sensorsPermissionGranted) {
        if (sensorsPermissionGranted) {
            fitnessViewModel.registerHeartRateSensor()
        }

        if (isTimerRunning) {
            while (true) {
                delay(1000L)
                fitnessViewModel.increaseTimer()
                fitnessViewModel.increaseTotalBPM()
            }
        }
    }


        ExitGameDialog(
            isVisible = showExitDialog,
            onDismiss = { },
            onConfirmClick = { navController.popBackStack() }
        )


    if (showOverlay) {
        LaunchedEffect(Unit) {
            showConfirmation(context)
            navController.popBackStack()
        }
    }

    ScreenScaffold(
        modifier = Modifier
            .background(
                brush = backgroundGradient
            )
            .fillMaxSize()
    ) {
        TimeText(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp),
            backgroundColor = MaterialTheme.colorScheme.tertiary

        )
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 2.dp)
        ) { page ->
            when (page) {
                SCORE_COMPONENT -> {
                    val isRound = LocalConfiguration.current.isScreenRound
                    val horizontalPadding = if (isRound) 12.dp else 8.dp

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = horizontalPadding)
                    ) {
                        GetSportsComponent(
                            sportName = sportName, gameViewModel = gameViewModel, onGameFinished = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            })
                    }
                }

                FITNESS_COMPONENT -> FitnessComponent(
                    fitnessViewModel = fitnessViewModel, onConfirmClick = {
                        saveGame(gameViewModel, fitnessViewModel, sportName)
                    })
            }
        }

        AnimatedVisibility(
            visible = pagerState.isScrollInProgress,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            HorizontalPageIndicator(
                pagerState = pagerState,
                selectedColor = MaterialTheme.colorScheme.secondary,
                unselectedColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun GetSportsComponent(
    sportName: String,
    gameViewModel: GameViewModel,
    onGameFinished: (() -> Unit)? = null,
) {
    when (sportName) {
        FOOTBALL -> FootballScoreComponent(gameViewModel = gameViewModel)
        BASKETBALL -> BasketballScoreComponent(gameViewModel = gameViewModel)
        TENNIS -> TennisScoreComponent(
            gameViewModel = gameViewModel, onGameFinished = onGameFinished!!
        )

        PADEL -> PadelScoreComponent(
            gameViewModel = gameViewModel, onGameFinished = onGameFinished!!
        )

        VOLLEYBALL -> VolleyballScoreComponent(
            gameViewModel = gameViewModel, onGameFinished = onGameFinished!!
        )
    }
}

fun saveGame(gameViewModel: GameViewModel, fitnessViewModel: FitnessViewModel, sportName: String) {
    val durationSeconds = fitnessViewModel.timeInSeconds.value
    val averageBPM = computeAverageBPM(
        fitnessViewModel.totalBPM.value, fitnessViewModel.timeInSeconds.value.toLong()
    )
    val date = Date()

    // Map string to GameType enum safely
    val gameType = try {
        GameType.valueOf(sportName.uppercase(java.util.Locale.ROOT))
    } catch (e: Exception) {
        GameType.OTHER
    }

    val finishedGame = gameViewModel.buildEndGameEntity(
        durationSeconds = durationSeconds,
        averageBPM = averageBPM,
        date = date,
        gameType = gameType
    )

    gameViewModel.insertGame(finishedGame)
}

fun showConfirmation(context: Context) {
    ConfirmationOverlay().setType(ConfirmationOverlay.SUCCESS_ANIMATION).setDuration(1000)
        .showOn(context as Activity)
}

fun computeAverageBPM(totalBPM: Long, timeInSeconds: Long): Int {
    if (timeInSeconds == 0L) return 0
    return (totalBPM / timeInSeconds).toInt()
}