package com.example.gameonapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import com.example.gameonapp.R
import com.example.gameonapp.presentation.components.MainScreenButton
import com.example.gameonapp.presentation.components.MainScreenOutlinedButon
import com.google.android.horologist.compose.layout.responsivePaddingDefaults

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    val listState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    val buttons = listOf(
        Triple(Icons.Outlined.PlayArrow, "Start") { navController.navigate("selectSports") },
        Triple(
            Icons.Outlined.BarChart, "Statistics"
        ) { navController.navigate("selectStatistics") },
        Triple(Icons.Outlined.Settings, "Settings") { navController.navigate("settings") })

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
            )
    ) {
        TransformingLazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = responsivePaddingDefaults()
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.game_on_logo),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(80.dp)
                )
            }

            buttons.forEachIndexed { index, (icon, text, action) ->
                item {
                    if (index == 0) {
                        MainScreenButton(
                            onClickMethod = action,
                            icon = icon,
                            buttonText = text,
                            transformation = SurfaceTransformation(transformationSpec)
                        )
                    } else {
                        MainScreenOutlinedButon(
                            onClickMethod = action,
                            icon = icon,
                            buttonText = text,
                            transformation = SurfaceTransformation(transformationSpec)
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}