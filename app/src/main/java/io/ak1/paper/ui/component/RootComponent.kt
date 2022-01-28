package io.ak1.paper.ui.component

import android.view.Window
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.doodle.DoodleScreen
import io.ak1.paper.ui.screens.home.HomeScreen
import io.ak1.paper.ui.screens.note.AddMoreScreen
import io.ak1.paper.ui.screens.note.NoteScreen
import io.ak1.paper.ui.screens.search.SearchScreen
import io.ak1.paper.ui.screens.setting.SettingsScreen
import io.ak1.paper.ui.theme.PaperTheme
import io.ak1.paper.ui.theme.StatusBarConfig
import io.ak1.paper.ui.theme.isSystemInDarkThemeCustom

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */


@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun RootComponent(window: Window) {
    val isDark = isSystemInDarkThemeCustom()
    PaperTheme(isDark) {
        window.StatusBarConfig(isDark)
        Surface(color = MaterialTheme.colors.background) {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberNavController(bottomSheetNavigator)
            ModalBottomSheetLayout(bottomSheetNavigator) {
                val listState = rememberLazyListState()
                NavHost(
                    navController = navController,
                    startDestination = Destinations.HOME_ROUTE
                ) {
                    composable(Destinations.HOME_ROUTE) {
                        HomeScreen(navController, listState)
                    }
                    composable(Destinations.NOTE_ROUTE) {
                        NoteScreen(navController)
                    }
                    composable(Destinations.SEARCH_ROUTE) {
                        SearchScreen(navController, listState)
                    }
                    composable(Destinations.SETTING_ROUTE) {
                        SettingsScreen(navController)
                    }
                    composable(
                        "${Destinations.DOODLE_ROUTE}/{${Destinations.NOTE_KEY}}",
                        arguments = listOf(navArgument(Destinations.NOTE_KEY) {
                            type = NavType.StringType
                        })
                    ) {
                        val arg = requireNotNull(it.arguments)
                        val noteId = arg.getString(Destinations.NOTE_KEY)
                        DoodleScreen(navController, noteId)
                    }
                    composable(
                        "${Destinations.NOTE_ROUTE}/{${Destinations.NOTE_KEY}}",
                        arguments = listOf(navArgument(Destinations.NOTE_KEY) {
                            type = NavType.StringType
                        })
                    ) {
                        val arg = requireNotNull(it.arguments)
                        val noteId = arg.getString(Destinations.NOTE_KEY)
                        NoteScreen(navController, noteId)
                    }

                    bottomSheet(
                        "${Destinations.INSERT_ROUTE}/{${Destinations.NOTE_KEY}}",
                        arguments = listOf(navArgument(Destinations.NOTE_KEY) {
                            type = NavType.StringType
                        })
                    ) {
                        val arg = requireNotNull(it.arguments)
                        val noteId = arg.getString(Destinations.NOTE_KEY)
                        AddMoreScreen(navController, noteId)
                    }
                }
            }
        }
    }
}
