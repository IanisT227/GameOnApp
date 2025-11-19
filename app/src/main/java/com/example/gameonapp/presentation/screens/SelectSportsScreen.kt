package com.example.gameonapp.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.SportsTennis
import androidx.compose.material.icons.outlined.SportsVolleyball
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.example.gameonapp.presentation.components.SelectSportChip
import com.example.gameonapp.presentation.theme.backgroundGradient
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.layout.responsivePaddingDefaults

@Composable
fun SelectSportsScreen(navController: NavController) {
    val context = LocalContext.current
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    val sportsList = listOf(
        Pair("Football", Icons.Outlined.SportsSoccer),
        Pair("Tennis", Icons.Outlined.SportsTennis),
        Pair("Padel", Icons.Outlined.SportsTennis),
        Pair("Volleyball", Icons.Outlined.SportsVolleyball),
        Pair("Basketball", Icons.Outlined.SportsBasketball),
    )

    ScreenScaffold(
        scrollState = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = backgroundGradient
            ),
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
                    "Select your sport", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            sportsList.forEach { listItem ->
                item {
                    SelectSportChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec)
                            .padding(vertical = 4.dp),
                        transformation = SurfaceTransformation(transformationSpec),
                        sportName = listItem.first,
                        sportImage = listItem.second,
                        action = {
                            try {
                                navController.navigate("scoring/${listItem.first.lowercase()}")
                            } catch (e: Exception) {
                                Log.e(TAG, e.message ?: "")
                                Toast.makeText(context, "Not available yet", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                }
            }
            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

const val TAG = "SelectSportsScreen"