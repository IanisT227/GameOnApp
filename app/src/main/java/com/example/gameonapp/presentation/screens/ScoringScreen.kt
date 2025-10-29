package com.example.gameonapp.presentation.screens

import android.Manifest
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
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TimeText
import com.example.gameonapp.presentation.components.FitnessComponent
import com.example.gameonapp.presentation.components.FootballScoreComponent
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import com.example.gameonapp.presentation.viewModels.GameViewModel
import com.example.gameonapp.utils.BASKETBALL
import com.example.gameonapp.utils.FITNESS_COMPONENT
import com.example.gameonapp.utils.FOOTBALL
import com.example.gameonapp.utils.PADEL
import com.example.gameonapp.utils.SCORE_COMPONENT
import com.example.gameonapp.utils.TENNIS
import com.example.gameonapp.utils.VOLLEYBALL
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoringScreen(
    modifier: Modifier = Modifier,
    sportName: String,
    navController: NavController,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState { 2 }
    val fitnessViewModel = koinViewModel<FitnessViewModel>()
    val gameViewModel = koinViewModel<GameViewModel>()
    var sensorsPermissionGranted by rememberSaveable { mutableStateOf(false) }
    val permissionToRequest = Manifest.permission.BODY_SENSORS
    val isTimerRunning by fitnessViewModel.isTimerRunning.collectAsState()

    var showExitDialog by rememberSaveable { mutableStateOf(false) }

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

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false // Dismiss dialog, stay on screen
            },
            title = { Text("Unsaved Changes") },
            text = { Text("Are you sure you want to exit? The event will not be saved.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false // Hide dialog
                        navController.popBackStack() // Proceed with navigation back
                    }
                ) {
                    Text("Yes, Exit")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text("No, Stay")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = backgroundGradient
            ),
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
                SCORE_COMPONENT -> GetSportsComponent(sportName = sportName, gameViewModel = gameViewModel)
                FITNESS_COMPONENT -> FitnessComponent(
                    fitnessViewModel = fitnessViewModel,
                    onConfirmClick = { saveGame(gameViewModel, fitnessViewModel) }
                )
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
fun GetSportsComponent(sportName: String, gameViewModel: GameViewModel) {
    when (sportName) {
        FOOTBALL -> FootballScoreComponent(gameViewModel = gameViewModel)
        TENNIS -> Box() {}
        PADEL -> Box() {}
        VOLLEYBALL -> Box() {}
        BASKETBALL -> Box() {}
    }
}

fun saveGame(gameViewModel: GameViewModel, fitnessViewModel: FitnessViewModel) {

}