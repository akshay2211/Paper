package io.ak1.paper.ui.screens.note

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.screens.home.DEFAULT
import io.ak1.paper.ui.screens.home.HomeViewModel
import org.koin.java.KoinJavaComponent

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
@Composable
fun NoteScreen(navController: NavController, noteId: String? = null) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
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

    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.image_desc),
                        tint = MaterialTheme.colors.primary
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_check),
                        contentDescription = stringResource(id = R.string.image_desc),
                        tint = if (description.value.text.trim()
                                .isEmpty()
                        ) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
                    )
                }
                IconButton(
                    onClick = {
                        if (note.value != null) {
                            setShowDialog(true)
                        }
                    }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_trash),
                        contentDescription = stringResource(id = R.string.image_desc),
                        tint = if (note.value == null) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        )
    },

        content = {
            BasicTextField(
                value = description.value,
                onValueChange = { tx ->
                    description.value = tx
                },
                textStyle = MaterialTheme.typography.h6,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp, 3.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focus.value != focusState.isFocused) {
                            focus.value = focusState.isFocused
                            if (!focusState.isFocused && !showDialog) {
                                inputService?.hideSoftwareKeyboard()
                                saveAndExit()
                            }
                        }
                    }
            )
        }
    )

    CustomAlertDialog(
        titleId = R.string.deletion_confirmation,
        showDialog = showDialog,
        setShowDialog = setShowDialog
    ) {
        navController.navigateUp()
        homeViewModel.deleteNote(note.value)
        Toast.makeText(context, R.string.note_removed, Toast.LENGTH_LONG).show()
    }
}