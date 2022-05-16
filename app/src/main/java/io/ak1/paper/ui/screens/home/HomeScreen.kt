package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.component.HomeHeader
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
    }, navigateTo)
}

@OptIn(ExperimentalUnitApi::class)
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
            Spacer(modifier = Modifier.statusBarsPadding())
        },
        content = { paddingValues ->
            NotesListComponent(true, uiState.notes, scrollState, paddingValues, saveNote)
            if (scrollState.firstVisibleItemIndex != 0 || (scrollState.firstVisibleItemIndex==0 && scrollState.firstVisibleItemScrollOffset>422)) {
                Box(
                    modifier = Modifier
                        .height(50.dp).padding(12.dp,0.dp)
                        .fillMaxWidth().background(MaterialTheme.colors.background),
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = TextUnit(20f,
                            TextUnitType.Sp
                        ),
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.BottomStart),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End
                    ){
                        PaperIconButton(
                            id = R.drawable.ic_search,
                        ) { }
                        PaperIconButton(
                            id = R.drawable.ic_more,
                        ) { }
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

