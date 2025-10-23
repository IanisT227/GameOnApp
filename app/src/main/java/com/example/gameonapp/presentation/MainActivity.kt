/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.gameonapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.gameonapp.presentation.screens.FootballScreen
import com.example.gameonapp.presentation.screens.MainScreen
import com.example.gameonapp.presentation.screens.SelectSportsScreen
import com.example.gameonapp.presentation.screens.SelectStatisticsScreen
import com.example.gameonapp.presentation.screens.SettingsScreen
import com.example.gameonapp.presentation.theme.GameOnAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val navController = rememberSwipeDismissableNavController()
    GameOnAppTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") { MainScreen(navController) }
            composable("selectSports") {
                SelectSportsScreen(navController)
            }
            composable("selectStatistics") {
                SelectStatisticsScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
            composable("football"){
                FootballScreen()
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}