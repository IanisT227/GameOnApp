package com.example.gameonapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.TimeText
import com.example.gameonapp.presentation.components.FitnessComponent
import com.example.gameonapp.presentation.components.FootballScoreComponent
import com.example.gameonapp.presentation.theme.backgroundGradient
import kotlinx.coroutines.delay

@Composable
fun FootballScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val pagerState = rememberPagerState { 2 }
    var isChronometerRunning by rememberSaveable { mutableStateOf(true) }
    var timeInSeconds by rememberSaveable { mutableStateOf(0L) }

    // Run the chronometer in the background if running
    LaunchedEffect(isChronometerRunning) {
        if (isChronometerRunning) {
            while (true) {
                delay(1000L)
                timeInSeconds++
            }
        }
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
            if (page == 0) {
                FootballScoreComponent()
            } else {
                FitnessComponent(
                    timeInSeconds = timeInSeconds,
                    isRunning = isChronometerRunning,
                    onPause = { isChronometerRunning = it },
                    onReset = { timeInSeconds = 0L })
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