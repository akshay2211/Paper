package io.ak1.paper.ui.screens.note

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.DEFAULT
import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.utils.convert
import io.ak1.paper.ui.utils.timeAgoInSeconds
import org.koin.androidx.compose.getViewModel

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
    val homeViewModel = getViewModel<HomeViewModel>()

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
        //inputService?.showSoftwareKeyboard()
        //focusRequester.requestFocus()
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {},
            navigationIcon = {
                PaperIconButton(id = R.drawable.ic_back) {
                    navController.navigateUp()
                }
            },
            actions = {
                PaperIconButton(
                    id = R.drawable.ic_check,
                    tint = if (description.value.text.trim()
                            .isEmpty()
                    ) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
                ) {
                    navController.navigateUp()
                }
                PaperIconButton(
                    id = R.drawable.ic_trash,
                    tint = if (description.value.text.trim()
                            .isEmpty()
                    ) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
                ) {
                    if (note.value != null) {
                        setShowDialog(true)
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        )
    }, bottomBar = {
        BottomAppBar(
            modifier = Modifier.height(46.dp),
            backgroundColor = MaterialTheme.colors.background,
            contentPadding = PaddingValues(4.dp, 4.dp),
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PaperIconButton(id = R.drawable.ic_feather) {
                        inputService?.hideSoftwareKeyboard()
                        focusRequester.freeFocus()
                        Handler(Looper.getMainLooper()).postDelayed({
                            navController.navigate("${Destinations.INSERT_ROUTE}/${noteId}")
                        }, 100L)
                    }
                    note.value?.updatedOn?.timeAgoInSeconds()?.let {
                        Text(
                            text = "Last updated $it",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.overline
                        )
                    }

                }
            }
        )
    }) {
        if (note.value?.doodle == null) {
            BasicTextField(
                value = description.value,
                onValueChange = { tx ->
                    description.value = tx
                },
                textStyle = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .fillMaxSize()
                    // .fillParentMaxHeight()
                    //   .requiredHeight(200.dp)
                    .padding(14.dp, 3.dp, 14.dp, 50.dp)
                    .focusRequester(focusRequester)
                    //.background(Color.Cyan)
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (note.value?.doodle != null) {
                    note.value?.imageText?.convert()?.asImageBitmap()?.let {
                        item {
                            Image(
                                bitmap = it,
                                contentDescription = "hi",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth()
                            )
                        }
                    }

                }
                item {
                    BasicTextField(
                        value = description.value,
                        onValueChange = { tx ->
                            description.value = tx
                        },
                        textStyle = MaterialTheme.typography.subtitle1,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp, 3.dp, 14.dp, 50.dp)
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
            }
        }


    }

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

@Composable
fun AddMoreScreen(navHostController: NavHostController, noteId: String? = null) {
    LazyColumn {
        items(6) { it ->
            IconButton(
                onClick = {
                    if (it == 0) {
                        navHostController.navigate("${Destinations.DOODLE_ROUTE}/${noteId}")
                    }
                }
            ) {

                Icon(
                    painterResource(id = R.drawable.ic_feather),
                    contentDescription = stringResource(id = R.string.image_desc),
                    tint = MaterialTheme.colors.primary
                )
                Text(
                    text = "Hello",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp, 0.dp, 0.dp, 0.dp)
                )

            }
        }
    }
}
