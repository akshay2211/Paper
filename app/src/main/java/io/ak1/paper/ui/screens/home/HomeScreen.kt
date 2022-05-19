package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.component.*
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
    }, navigateTo)
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    scrollState: LazyListState,
    saveNote: (NoteWithDoodleAndImage) -> Unit,
    openNewNote: () -> Unit,
    navigateTo: (String) -> Unit
) {
    val maxHeightInPX = with(LocalDensity.current) { headerBarExpandedHeight.toPx() }
    val minHeightInPx = with(LocalDensity.current) { headerBarCollapsedHeight.toPx() }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding(),
        topBar = {
            Spacer(modifier = Modifier.statusBarsPadding())
        },
        content = { paddingValues ->
            NotesListComponent(
                true,
                uiState.notes,
                scrollState,
                paddingValues,
                navigateTo,
                saveNote
            )

            if (scrollState.firstVisibleItemIndex != 0 || (scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset > maxHeightInPX - minHeightInPx)) {
                Box(
                    modifier = Modifier
                        .padding(12.dp, 0.dp)
                        .background(MaterialTheme.colors.background)
                ) {
                    HomeHeader {
                        PaperIconButton(
                            id = R.drawable.ic_search,
                        ) { navigateTo(Destinations.SEARCH_ROUTE) }
                        PaperIconButton(
                            id = R.drawable.ic_more,
                        ) { navigateTo(Destinations.SETTING_ROUTE) }
                    }
                }
            }
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

