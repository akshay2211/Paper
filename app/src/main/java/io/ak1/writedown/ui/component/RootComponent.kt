package io.ak1.writedown.ui.component

import android.view.Window
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ak1.writedown.ui.screens.Destinations
import io.ak1.writedown.ui.screens.home.HomeScreen
import io.ak1.writedown.ui.theme.WriteDownTheme
import io.ak1.writedown.ui.theme.isSystemInDarkThemeCustom
import io.ak1.writedown.ui.theme.statusBarConfig

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */


@Composable
fun RootComponent(window: Window) {
    val isDark = isSystemInDarkThemeCustom()
    WriteDownTheme {
        window.statusBarConfig(isDark)
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Destinations.HOME_ROUTE
            ) {
                composable(Destinations.HOME_ROUTE) {
                    HomeScreen(navController)
                }
            }
        }
    }
}
