@file:OptIn(ExperimentalMaterial3Api::class)

package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import io.ak1.paper.R
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.component.*
import io.ak1.paper.ui.screens.Destinations
import io.ak1.rangvikalp.colorArray
import org.koin.androidx.compose.get

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
val fabShape = RoundedCornerShape(30)

const val darkerToneIndex = 7
const val lighterToneIndex = 1

@Composable
fun HomeScreen(isDark: Boolean, scrollState: LazyListState, navigateTo: (String) -> Unit) {
    val homeViewModel = get<HomeViewModel>()
    val uiState by homeViewModel.uiState.collectAsState()
    HomeScreen(isDark, uiState, scrollState, {
        homeViewModel.saveCurrentNote(it.note.noteId)
        navigateTo(Destinations.NOTE_ROUTE)
    }, {
        homeViewModel.saveCurrentNote()
        navigateTo(Destinations.NOTE_ROUTE)
    }, navigateTo)
}

@Composable
fun HomeScreen(
    isDark: Boolean,
    uiState: HomeUiState,
    scrollState: LazyListState,
    saveNote: (NoteWithDoodleAndImage) -> Unit,
    openNewNote: () -> Unit,
    navigateTo: (String) -> Unit
) {
    val randomInt = get<Int>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val headerColor = colorArray[randomInt][if (isDark) lighterToneIndex else darkerToneIndex]
    val tintColor = colorArray[randomInt][if (isDark) darkerToneIndex else lighterToneIndex]
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displaySmall,
                    color = headerColor,
                    fontFamily = FontFamily(Font(R.font.lavishly_yours_regular))
                )
            }, actions = {
                PaperIconButton(
                    id = R.drawable.ic_search,
                ) { navigateTo(Destinations.SEARCH_ROUTE) }
                PaperIconButton(
                    id = R.drawable.ic_more,
                ) { navigateTo(Destinations.SETTING_ROUTE) }
            }, scrollBehavior = scrollBehavior)
        },
        content = { paddingValues ->
            NotesListComponent(
                headerColor,
                uiState,
                scrollState,
                paddingValues,
                saveNote
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = openNewNote,
                shape = fabShape,
                containerColor = headerColor
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_feather),
                    contentDescription = stringResource(
                        id = R.string.image_desc
                    ),
                    colorFilter = ColorFilter.tint(tintColor)
                )
            }
        })
}

