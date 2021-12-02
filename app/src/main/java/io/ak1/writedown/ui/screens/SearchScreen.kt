package io.ak1.writedown.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.writedown.R
import io.ak1.writedown.ui.component.NotesListComponent
import io.ak1.writedown.ui.screens.home.HomeViewModel
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
    val description = remember {
        mutableStateOf(TextFieldValue())
    }
    LaunchedEffect(navController) {
        focus.value = true
        inputService?.showSoftwareKeyboard()
        focusRequester.requestFocus()
    }
    val resultList = homeViewModel.getAllNotesByDescription(description.value.text)
        .observeAsState(initial = listOf())
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            val modifier = Modifier.padding(7.dp)
            TextField(
                value = description.value,
                onValueChange = {
                    description.value = it
                },
                placeholder = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(
                            id = R.string.image_desc
                        ),
                        modifier = modifier.clickable {
                            navController.navigateUp()
                        },
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                    )
                },
                trailingIcon = {
                    if (!description.value.text.isNullOrEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_x),
                            contentDescription = stringResource(
                                id = R.string.image_desc
                            ),
                            modifier = modifier.clickable {
                                description.value = TextFieldValue()
                            },
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                        )
                    }
                },
                textStyle = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp)
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
                navController.navigate("${Destinations.NOTE_ROUTE}/${it.id}")
            }
        }
    }
}