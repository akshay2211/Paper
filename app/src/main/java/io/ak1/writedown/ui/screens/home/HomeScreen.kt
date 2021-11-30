package io.ak1.writedown.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.writedown.R
import io.ak1.writedown.ui.component.NotesListComponent
import io.ak1.writedown.ui.screens.Destinations
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

@Composable
fun HomeScreen(navController: NavController,listState : LazyListState) {
    val homeViewModel by inject<HomeViewModel>(HomeViewModel::class.java)
    val resultList = homeViewModel.getAllDefaultNotes().observeAsState(initial = listOf())
    Box(modifier = Modifier.fillMaxSize()) {
        NotesListComponent(resultList, listState,{},{}) {
            navController.navigate("${Destinations.NOTE_ROUTE}/${it.id}")
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(Destinations.NOTE_ROUTE)
            },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_feather),
                contentDescription = stringResource(
                    id = R.string.image_desc
                ),
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}
