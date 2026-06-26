package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gameonapp.data.local.model.Gender
import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import com.example.gameonapp.presentation.viewModels.SettingsViewModel
import com.example.gameonapp.utils.PULSE_METER
import kotlin.math.roundToInt

@Composable
fun FitnessComponent(
    onConfirmClick: () -> Unit,
    fitnessViewModel: FitnessViewModel,
    settingsViewModel: SettingsViewModel
) {
    val settings by settingsViewModel.uiState.collectAsState()
    val hr by fitnessViewModel.heartRateBpm.collectAsState()
    val calories by fitnessViewModel.calories.collectAsState()
    val timeInSeconds by fitnessViewModel.timeInSeconds.collectAsState()
    val isTimerRunning by fitnessViewModel.isTimerRunning.collectAsState()

    LaunchedEffect(settings.weight, settings.height, settings.gender) {
        val weightKg = settings.weight.toDoubleOrNull()
        val heightCm = settings.height.toDoubleOrNull()
        val isMale = settings.gender == Gender.Male
        fitnessViewModel.updateUserProfile(weightKg, heightCm, isMale)
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            FitnessTimerChip(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(0.7f),
                timeValue = timeInSeconds,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FitnessChip(
                    modifier = Modifier.weight(1f),
                    value = hr?.roundToInt() ?: 20,
                    icon = Icons.Rounded.MonitorHeart,
                    type = PULSE_METER
                )
                Spacer(modifier = Modifier.width(4.dp))
                FitnessChip(
                    modifier = Modifier.weight(1f),
                    value = calories.roundToInt(),
                    icon = Icons.Rounded.LocalFireDepartment
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(horizontal = 26.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FitnessButton(
                    modifier = Modifier.weight(1f),
                    onClick = { fitnessViewModel.toggleIsTimerRunning() },
                    buttonImage = if (isTimerRunning) Icons.Outlined.Pause else Icons.Outlined.PlayArrow
                )
                Spacer(modifier = Modifier.width(4.dp))
                FitnessButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onConfirmClick()
                    }, buttonImage = Icons.Outlined.Check
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

const val TAG = "FitnessComponent"