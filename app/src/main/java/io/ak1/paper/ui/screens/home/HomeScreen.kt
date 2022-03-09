package io.ak1.paper.ui.screens.home

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.ak1.paper.R
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
    var currentState = remember { mutableStateOf(BoxState.Collapsed) }
    val transition = updateTransition(currentState, label = "box")
    val height = transition.animateDp(label = "box") { state ->
        if (BoxState.Expanded == state.value) 200.dp else 60.dp
    }
    val size = transition.animateInt(label = "box") { state ->
        if (BoxState.Expanded == state.value) 48 else 20
    }
    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        if (scrollState.firstVisibleItemIndex == 0) {
            currentState.value = BoxState.Expanded
        } else if (currentState.value == BoxState.Expanded) {
            currentState.value = BoxState.Collapsed
        }
    }

    val fabShape = RoundedCornerShape(30)
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .height(height.value)
            ) {

                TopAppBar(
                    title = {

                    },
                    actions = {
                        PaperIconButton(id = R.drawable.ic_search, onClick = {
                            navController.navigate(Destinations.SEARCH_ROUTE)
                        })
                        PaperIconButton(id = R.drawable.ic_more, onClick = {
                            navController.navigate(Destinations.SETTING_ROUTE)
                        })
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.h3,
                    fontSize = size.value.sp,
                    modifier = Modifier
                        .padding(18.dp)
                        .align(Alignment.TopStart)

                )
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

