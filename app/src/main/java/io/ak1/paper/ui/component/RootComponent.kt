package io.ak1.paper.ui.component

import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.HomeScreen
import io.ak1.paper.ui.screens.note.NoteContainerScreen
import io.ak1.paper.ui.screens.search.SearchScreen
import io.ak1.paper.ui.screens.setting.SettingsScreen
import io.ak1.paper.ui.theme.PaperTheme
import io.ak1.paper.ui.theme.isSystemInDarkThemeCustom

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */


@Composable
fun RootComponent() {
    val isDark = isSystemInDarkThemeCustom()
    val systemUiController = rememberSystemUiController()
    PaperTheme(isDark) {
        val darkIcons = MaterialTheme.colors.isLight
        SideEffect { systemUiController.setSystemBarsColor(Color.Transparent, darkIcons) }
        Surface(color = MaterialTheme.colors.background) {
            val navController = rememberNavController()
            val scrollState = rememberLazyListState()
            NavHost(
                navController = navController,
                startDestination = Destinations.HOME_ROUTE
            ) {
                Log.e("track ", "RootComponent")
                composable(Destinations.HOME_ROUTE) {
                    HomeScreen(scrollState) { navController.navigate(it) }
                }
                composable(Destinations.NOTE_ROUTE) {
                    NoteContainerScreen({ navController.navigate(it) })
                    { navController.navigateUp() }
                }
                composable(Destinations.SEARCH_ROUTE) { SearchScreen(navController) }
                composable(Destinations.SETTING_ROUTE) { SettingsScreen(navController) }
            }
        }
    }
}
