package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import io.ak1.paper.R
import io.ak1.paper.ui.component.CollapsibleTopBar
import io.ak1.paper.ui.component.CollapsibleTopBarState
import io.ak1.paper.ui.component.NotesListComponent
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel by inject<HomeViewModel>(HomeViewModel::class.java)
    val resultList = homeViewModel.getAllDefaultNotes().observeAsState(initial = listOf())
    val scrollState = rememberLazyListState()
    LaunchedEffect(resultList.value) {
        homeViewModel.insertDefaultData()
    }
    var currentState = remember { mutableStateOf(CollapsibleTopBarState.Expanded) }

    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        if (scrollState.firstVisibleItemIndex == 0) {
            currentState.value = CollapsibleTopBarState.Expanded
        } else if (currentState.value == CollapsibleTopBarState.Expanded) {
            currentState.value = CollapsibleTopBarState.Collapsed
        }
    }

    val fabShape = RoundedCornerShape(30)
    Scaffold(
        topBar = {
            CollapsibleTopBar(collapsibleTopBarState = currentState.value) {
                PaperIconButton(id = R.drawable.ic_search, onClick = {
                    navController.navigate(Destinations.SEARCH_ROUTE)
                })
                PaperIconButton(id = R.drawable.ic_more, onClick = {
                    navController.navigate(Destinations.SETTING_ROUTE)
                })
            }
        },

        content = {
            NotesListComponent(true, resultList, scrollState) {
                navController.navigate("${Destinations.NOTE_ROUTE}/${it.noteId}")
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Destinations.NOTE_ROUTE)
                },
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

