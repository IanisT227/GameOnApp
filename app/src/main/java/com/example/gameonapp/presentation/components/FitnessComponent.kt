package com.example.gameonapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gameonapp.presentation.viewModels.FitnessViewModel
import com.example.gameonapp.utils.PULSE_METER
import com.google.android.horologist.compose.layout.fillMaxRectangle
import kotlin.math.roundToInt

@Composable
fun FitnessComponent(
    modifier: Modifier = Modifier,
    fitnessViewModel: FitnessViewModel,
) {
    val hr by fitnessViewModel.heartRateBpm.collectAsState()
    val calories by fitnessViewModel.calories.collectAsState()
    val timeInSeconds by fitnessViewModel.timeInSeconds.collectAsState()
    val isTimerRunning by fitnessViewModel.isTimerRunning.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog)
        SaveDialog(isVisible = true, onDismiss = { showDialog = false })
    Column(
        modifier = Modifier.fillMaxRectangle(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        FitnessTimerChip(
            modifier = Modifier.fillMaxWidth(),
            timeValue = timeInSeconds,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                value = hr?.roundToInt() ?: 111,
                icon = Icons.Rounded.LocalFireDepartment
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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
                    showDialog = true
                }, buttonImage = Icons.Outlined.Check
            )
        }
    }
}