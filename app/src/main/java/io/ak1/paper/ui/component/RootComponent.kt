package io.ak1.paper.ui.component

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.ak1.paper.R
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.HomeScreen
import io.ak1.paper.ui.screens.note.doodle.DoodleScreen
import io.ak1.paper.ui.screens.note.note.NoteScreen
import io.ak1.paper.ui.screens.note.options.OptionsScreen
import io.ak1.paper.ui.screens.search.SearchScreen
import io.ak1.paper.ui.screens.setting.SettingsScreen
import io.ak1.paper.ui.theme.PaperTheme
import io.ak1.paper.ui.theme.isSystemInDarkThemeCustom

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */


@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun RootComponent() {
    val isDark = isSystemInDarkThemeCustom()
    val systemUiController = rememberSystemUiController()
    val uriHandler = LocalUriHandler.current
    PaperTheme(isDark) {
        val darkIcons = MaterialTheme.colors.isLight
        SideEffect { systemUiController.setSystemBarsColor(Color.Transparent, darkIcons) }
        Surface(color = MaterialTheme.colors.background) {
            val scrollState = rememberLazyListState()
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberNavController(bottomSheetNavigator)
            ModalBottomSheetLayout(bottomSheetNavigator) {
                NavHost(
                    navController = navController,
                    startDestination = Destinations.HOME_ROUTE
                ) {
                    composable(Destinations.HOME_ROUTE) {
                        HomeScreen(scrollState) { navController.navigate(it) }
                    }
                    composable(Destinations.NOTE_ROUTE) {
                        NoteScreen({ navController.navigate(it) })
                        { navController.navigateUp() }
                    }
                    composable(Destinations.SEARCH_ROUTE) { SearchScreen(navController) }
                    composable(Destinations.SETTING_ROUTE) {
                        SettingsScreen({ navController.navigateUp() }) { uri -> uriHandler.openUri(uri) }
                    }
                    composable(Destinations.DOODLE_ROUTE) {
                        DoodleScreen { navController.navigateUp() }
                    }
                    bottomSheet(Destinations.OPTIONS_ROUTE) {
                        OptionsScreen({ navController.navigate(it) })
                        { navController.navigateUp() }
                    }
                }
            }
        }
    }
}
