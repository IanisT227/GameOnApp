package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.example.gameonapp.presentation.components.SelectSportChip
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.layout.responsivePaddingDefaults

@Composable
fun SelectSportsScreen() {
    val context = LocalContext.current
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()


    ScreenScaffold(
        scrollState = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF333750), Color(0xFF555555)),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
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
            (0..10).forEach { i ->
                item {
                    SelectSportChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec)
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        transformation = SurfaceTransformation(transformationSpec)
                    )
                }
            }

        }
    }
}