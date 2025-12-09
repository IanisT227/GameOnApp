package com.example.gameonapp.presentation

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gameonapp.presentation.screens.ExpandedStatisticsScreen
import com.example.gameonapp.presentation.screens.MainScreen
import com.example.gameonapp.presentation.screens.ScoringScreen
import com.example.gameonapp.presentation.screens.SelectSportsScreen
import com.example.gameonapp.presentation.screens.SelectStatisticsScreen
import com.example.gameonapp.presentation.screens.SettingsScreen
import com.example.gameonapp.presentation.theme.GameOnAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()

    GameOnAppTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "main",
        ) {
            composable("main") { MainScreen(navController) }
            composable("selectSports") {
                SelectSportsScreen(navController)
            }
            composable("selectStatistics") {
                SelectStatisticsScreen(navController)
            }
            composable("settings") {
                SettingsScreen()
            }
            composable(
                route = "scoring/{sportName}",
                arguments = listOf(
                    navArgument("sportName") { type = NavType.StringType }
                )) { backStackEntry ->
                val sportName = backStackEntry.arguments?.getString("sportName") ?: ""
                ScoringScreen(
                    sportName = sportName,
                    navController = navController,
                )
            }
            composable(
                route = "statisticsExpanded/{gameId}",
                arguments = listOf(
                    navArgument("gameId") { type = NavType.LongType }
                )) { backStackEntry ->
                val gameId = backStackEntry.arguments?.getLong("gameId") ?: 0
                ExpandedStatisticsScreen(
                    gameId = gameId,
                    navController = navController
                )
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}