package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import com.example.gameonapp.R
import com.example.gameonapp.data.local.model.UnitSystem
import com.example.gameonapp.presentation.components.ExpandableSection
import com.example.gameonapp.presentation.components.GenderSelector
import com.example.gameonapp.presentation.components.HorizontalDrumRoller
import com.example.gameonapp.presentation.components.SimpleOptionSelector
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.example.gameonapp.presentation.viewModels.SettingsViewModel
import com.example.gameonapp.utils.fetchHeightList
import com.example.gameonapp.utils.fetchWeightList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen() {
    val viewModel = koinViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                    "Settings", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            ExpandableSection(title = "Personal Profile") {
                Text(
                    text = stringResource(id = R.string.height),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDrumRoller(
                    values = fetchHeightList(uiState.units),
                    defaultValue = uiState.height,
                    unit = if (uiState.units == UnitSystem.Metric) "cm" else "ft",
                    onValueConfirmed = { height -> viewModel.saveHeight(height) })
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.weight),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDrumRoller(
                    values = fetchWeightList(uiState.units),
                    defaultValue = uiState.weight,
                    unit = if (uiState.units == UnitSystem.Metric) "kg" else "lb",
                    onValueConfirmed = { weight -> viewModel.saveWeight(weight) })
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.age),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDrumRoller(
                    values = (18..100).map { it.toString() },
                    defaultValue = uiState.age.toString(),
                    unit = "years",
                    onValueConfirmed = { age -> viewModel.saveAge(age.toInt()) })
                Spacer(modifier = Modifier.height(6.dp))
                GenderSelector(
                    defaultGender = uiState.gender,
                    onGenderSelected = { gender -> viewModel.saveGender(gender) })
            }
            ExpandableSection(title = stringResource(id = R.string.display_units)) {
                SimpleOptionSelector(
                    title = stringResource(id = R.string.units),
                    defaultOption = uiState.units.name,
                    entries = listOf(UnitSystem.Metric.name, UnitSystem.Imperial.name),
                    onOptionSelected = { value -> viewModel.saveUnits(value) })
                Spacer(modifier = Modifier.height(4.dp))
                SimpleOptionSelector(
                    title = stringResource(id = R.string.time_format),
                    defaultOption = uiState.timeFormat,
                    entries = listOf("24h", "12h"),
                    onOptionSelected = { value -> viewModel.saveTimeFormat(value) })
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}