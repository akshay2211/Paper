package io.ak1.paper.ui.screens.note

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
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
import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.screens.note.doodle.DoodleScreen
import io.ak1.paper.ui.screens.note.note.AddMoreScreen
import io.ak1.paper.ui.screens.note.note.NoteScreen
import org.koin.androidx.compose.getViewModel

/**
 * Created by akshay on 23/02/22
 * https://ak1.io
 */

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun NoteContainerScreen(navController: NavController, noteId: String? = null) {
    val homeViewModel = getViewModel<HomeViewModel>()
    var localNote = remember {
        mutableStateOf(homeViewModel.emptyNote)
    }
    var note = homeViewModel.getNote(noteId)?.observeAsState()

    LaunchedEffect(note?.value) {
        note?.value?.let {
            localNote.value = it
        }
    }

    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val innerNavController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = innerNavController,
            startDestination = Destinations.NOTE_ROUTE
        ) {
            composable(Destinations.NOTE_ROUTE) {
                NoteScreen(navController, innerNavController, localNote)
            }
            composable(Destinations.DOODLE_ROUTE) {
                DoodleScreen(innerNavController, true, localNote.value.note.noteId)
            }
            composable(
                "${Destinations.DOODLE_ROUTE}/{${Destinations.NOTE_KEY}}",
                arguments = listOf(navArgument(Destinations.NOTE_KEY) {
                    type = NavType.StringType
                })
            ) {
                val arg = requireNotNull(it.arguments)
                val doodleId = requireNotNull(arg.getString(Destinations.NOTE_KEY))
                DoodleScreen(innerNavController, false, doodleId)
            }
            bottomSheet(
                "${Destinations.INSERT_ROUTE}/{${Destinations.NOTE_KEY}}",
                arguments = listOf(navArgument(Destinations.NOTE_KEY) {
                    type = NavType.StringType
                })
            ) {
                val arg = requireNotNull(it.arguments)
                val noteId = arg.getString(Destinations.NOTE_KEY)

                AddMoreScreen(innerNavController, noteId)
            }
        }
    }

}