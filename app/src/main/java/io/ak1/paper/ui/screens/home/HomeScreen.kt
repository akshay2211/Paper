package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.component.NotesListComponent
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import org.koin.androidx.compose.inject

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
val fabShape = RoundedCornerShape(30)
@Composable
fun HomeScreen(scrollState: LazyListState, navigateTo: (String) -> Unit) {
    val homeViewModel by inject<HomeViewModel>()
    val uiState by homeViewModel.uiState.collectAsState()

    LocalTextInputService.current?.hideSoftwareKeyboard()
    HomeScreen(uiState, scrollState, {
        homeViewModel.saveCurrentNote(it.note.noteId)
        navigateTo(Destinations.NOTE_ROUTE)
    }, {
        homeViewModel.saveCurrentNote()
        navigateTo(Destinations.NOTE_ROUTE)
    },navigateTo)
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    scrollState: LazyListState,
    saveNote: (NoteWithDoodleAndImage) -> Unit,
    openNewNote: () -> Unit,
    navigateTo: (String) -> Unit
) {

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        topBar = {
            TopAppBar(modifier = Modifier.statusBarsPadding(),
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    PaperIconButton(
                        id = R.drawable.ic_search,
                    ) { navigateTo(Destinations.SEARCH_ROUTE) }
                    PaperIconButton(
                        id = R.drawable.ic_more,
                    ) { navigateTo(Destinations.SETTING_ROUTE) }
                },
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            )
        },
        content = { paddingValues ->
            NotesListComponent(true, uiState.notes, scrollState, paddingValues, saveNote)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = openNewNote,
                shape = fabShape,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_feather),
                    contentDescription = stringResource(
                        id = R.string.image_desc
                    ),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        })
}

