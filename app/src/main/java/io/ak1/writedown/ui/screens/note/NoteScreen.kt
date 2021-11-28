package io.ak1.writedown.ui.screens.note

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import io.ak1.writedown.models.Note
import io.ak1.writedown.ui.screens.home.DEFAULT
import io.ak1.writedown.ui.screens.home.HomeViewModel
import org.koin.java.KoinJavaComponent

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
@Composable
fun NoteScreen(navController: NavController, noteId: String? = null) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }
    val homeViewModel by KoinJavaComponent.inject<HomeViewModel>(HomeViewModel::class.java)
    val description = remember {
        mutableStateOf(TextFieldValue())
    }
    val note = remember {
        mutableStateOf<Note?>(null)
    }

    fun saveAndExit() {
        if (note.value == null && description.value.text.trim().isNotEmpty()) {
            homeViewModel.saveNote(
                Note(
                    description = description.value.text.trim(),
                    folderId = DEFAULT
                )
            )
        } else if (note.value != null && note.value?.description != description.value.text.trim()) {
            note.value?.apply {
                this.description = description.value.text
                this.updatedOn = System.currentTimeMillis()
            }
            homeViewModel.saveNote(note.value!!)
        }
        navController.navigateUp()
    }
    LaunchedEffect(note) {
        noteId?.let { noteId ->
            homeViewModel.getNote(noteId) {
                note.value = it
                description.value = TextFieldValue(
                    annotatedString = AnnotatedString(it.description),
                    TextRange(it.description.length)
                )
            }
        }
        inputService?.showSoftwareKeyboard()
        focusRequester.requestFocus()
    }

    TextField(
        value = description.value,
        onValueChange = {
            description.value = it
        },
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focus.value != focusState.isFocused) {
                    focus.value = focusState.isFocused
                    if (!focusState.isFocused) {
                        inputService?.hideSoftwareKeyboard()
                        saveAndExit()
                    }
                }
            },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            focusedIndicatorColor = MaterialTheme.colors.background
        ),
    )


}