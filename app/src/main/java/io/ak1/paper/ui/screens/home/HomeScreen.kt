package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.paper.R
import io.ak1.paper.ui.component.NotesListComponent
import io.ak1.paper.ui.screens.Destinations
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@Composable
fun HomeScreen(navController: NavController, listState: LazyListState) {
    val homeViewModel by inject<HomeViewModel>(HomeViewModel::class.java)
    val resultList = homeViewModel.getAllDefaultNotes().observeAsState(initial = listOf())

    LaunchedEffect(resultList.value) {
        homeViewModel.insertDefaultData()
    }
    val fabShape = RoundedCornerShape(30)
    Scaffold(
        topBar = {},

        content = {
            NotesListComponent(true, resultList, listState, {
                navController.navigate(Destinations.SEARCH_ROUTE)
            }, {
                navController.navigate(Destinations.SETTING_ROUTE)
            }) {
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

