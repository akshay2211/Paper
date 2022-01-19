package io.ak1.paper.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.paper.R
import io.ak1.paper.ui.component.Iconsbar
import io.ak1.paper.ui.component.NotesListComponent
import io.ak1.paper.ui.component.PaperIconButton
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
    val result = remember { mutableStateOf("") }
    val selectedItem = remember { mutableStateOf("upload") }
    val fabShape = RoundedCornerShape(30)
    Scaffold(
        topBar = {},

        content = {
            NotesListComponent(true, resultList, listState, {
                navController.navigate(Destinations.SEARCH_ROUTE)
            }, {
                navController.navigate(Destinations.SETTING_ROUTE)
            }) {
                navController.navigate("${Destinations.NOTE_ROUTE}/${it.id}")
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
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,

        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(46.dp),
                cutoutShape = fabShape,
                backgroundColor = MaterialTheme.colors.background,
                contentPadding = PaddingValues(4.dp, 16.dp),
                        content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                    }
                }
            )
        }
    )

}

