package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import com.example.gameonapp.data.local.model.Gender
import com.example.gameonapp.presentation.components.GenderSelector
import com.example.gameonapp.presentation.components.HorizontalDrumRoller
import com.example.gameonapp.presentation.theme.backgroundGradient

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    ScreenScaffold(
        scrollState = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            ListHeader {
                Text(
                    "Settings",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Personal Profile", style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDrumRoller(
                values = (50..250).toList(),
                defaultValue = 170,
                unit = "cm",
                onValueConfirmed = { height -> }
            )
            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDrumRoller(
                values = (20..300).toList(),
                defaultValue = 70,
                unit = "kg",
                onValueConfirmed = { weight -> }
            )
            Spacer(modifier = Modifier.height(6.dp))
            GenderSelector(
                defaultGender = Gender.Female,
                onGenderSelected = { gender -> }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}