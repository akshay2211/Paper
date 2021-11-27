package io.ak1.writedown.ui.component

import android.view.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import io.ak1.writedown.ui.screens.Destinations
import io.ak1.writedown.ui.screens.home.HomeScreen
import io.ak1.writedown.ui.screens.note.NoteScreen
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
                composable(Destinations.NOTE_ROUTE) {
                    NoteScreen(navController)
                }
                composable(
                    "${Destinations.NOTE_ROUTE}/{${Destinations.NOTE_KEY}}",
                    arguments = listOf(navArgument(Destinations.NOTE_KEY) {
                        type = NavType.StringType
                    })
                ) {
                    val arg = requireNotNull(it.arguments)
                    val noteId = arg.getString(Destinations.NOTE_KEY)
                    NoteScreen(navController,noteId)
                }
            }
        }
    }
}
