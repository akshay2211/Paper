package io.ak1.paper.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalTextInputService
import androidx.navigation.NavController
import io.ak1.paper.R
import io.ak1.paper.ui.component.NotesListComponent
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.HomeViewModel
import org.koin.java.KoinJavaComponent.inject

/**
 * Created by akshay on 01/12/21
 * https://ak1.io
 */

@Composable
fun SearchScreen(navController: NavController, listState: LazyListState) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(true) }
    val homeViewModel by inject<HomeViewModel>(HomeViewModel::class.java)
    val description = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(navController) {
        focus.value = true
        inputService?.showSoftwareKeyboard()
        focusRequester.requestFocus()
    }
    val resultList = homeViewModel.getAllNotesByDescription(description.value)
        .observeAsState(initial = listOf())
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TextField(
                value = description.value,
                onValueChange = {
                    description.value = it
                },
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.h6
                    )
                },
                leadingIcon = {
                    PaperIconButton(id = R.drawable.ic_back) {
                        navController.navigateUp()
                    }
                },
                trailingIcon = {
                    if (description.value.isNotEmpty()) {
                        PaperIconButton(id = R.drawable.ic_x) {
                            navController.navigateUp()
                        }
                    }
                },
                textStyle = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focus.value != focusState.isFocused) {
                            focus.value = focusState.isFocused
                            if (!focusState.isFocused && focus.value) {
                                inputService?.hideSoftwareKeyboard()
                                navController.navigateUp()
                            }
                        }
                    },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    focusedIndicatorColor = MaterialTheme.colors.background,
                    unfocusedIndicatorColor = MaterialTheme.colors.background
                ),
            )
            NotesListComponent(false, resultList, listState, {}, {}) {
                focus.value = false
                navController.navigate("${Destinations.NOTE_ROUTE}/${it.noteId}")
            }
        }
    }
}