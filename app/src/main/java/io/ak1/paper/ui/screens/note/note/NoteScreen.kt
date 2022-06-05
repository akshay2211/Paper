package io.ak1.paper.ui.screens.note.note

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import io.ak1.paper.R
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.fabShape
import io.ak1.paper.ui.utils.timeAgoInSeconds
import org.koin.androidx.compose.inject

/**
 * Created by akshay on 23/02/22
 * https://ak1.io
 */

@Composable
fun NoteScreen(navigateTo: (String) -> Unit, backPress: () -> Unit) {
    val noteViewModel by inject<NoteViewModel>()
    val uiState by noteViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val description = remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(uiState) {
        uiState.note.let {
            description.value = TextFieldValue(
                annotatedString = AnnotatedString(it.note.description),
                TextRange(it.note.description.length)
            )
        }
    }
    fun saveAndExit(note: NoteWithDoodleAndImage) {
        if (note.note.description != description.value.text.trim()
        ) {
            note.note.description = description.value.text.trim()
            noteViewModel.saveNote(note.note)
        }
        if (description.value.text.isEmpty() && note.doodleList.isEmpty() && note.imageList.isEmpty()) {
            noteViewModel.deleteNote(note.note)
        }
    }

    NoteScreen(
        uiState, description, { saveAndExit(uiState.note) },
        {   //delete
            noteViewModel.deleteNote(uiState.note.note)
            Toast.makeText(context, R.string.note_removed, Toast.LENGTH_LONG).show()
            backPress.invoke()
        }, {
            noteViewModel.saveCurrentDoodleId(it)
        }, backPress, navigateTo
    )

}

@Composable
fun NoteScreen(
    uiState: NoteUiState,
    description: MutableState<TextFieldValue>,
    save: () -> Unit,
    delete: () -> Unit,
    saveDoodleId: (id: String) -> Unit,
    backPress: () -> Unit,
    navigateTo: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val noteFont = TextStyle(
        fontWeight = FontWeight.Thin,
        color = MaterialTheme.colors.primary,
        fontSize = 20.sp,
        letterSpacing = 0.10.sp
    )

    BackHandler(enabled = true) {
        save.invoke()
        backPress.invoke()
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            NotesTopBar({ backPress.invoke() }, {
                inputService?.hideSoftwareKeyboard()
                save.invoke()
                backPress.invoke()
            }, { setShowDialog(true) })
        },
        bottomBar = {
            NotesBottomBar(uiState.note.note) {
                inputService?.hideSoftwareKeyboard()
                //  focusRequester.freeFocus()
                navigateTo(Destinations.OPTIONS_ROUTE)
            }
        },
        content = { paddingValues ->
            val pv = paddingValues.calculateBottomPadding()
            Column(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, if (pv > 45.dp) pv - 45.dp else pv)
                    .fillMaxSize()
            ) {
                if (uiState.note.doodleList.isNotEmpty()) {
                    LazyRow(modifier = Modifier.padding(5.dp, 15.dp)) {
                        items(uiState.note.doodleList) { doodle ->
                            if (!doodle.uri.isEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = doodle.uri),
                                    contentDescription = "hi",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                        .clip(fabShape)
                                        .border(
                                            0.5.dp,
                                            MaterialTheme.colors.primary,
                                            fabShape
                                        )
                                        .clickable {
                                            saveDoodleId(doodle.doodleid)
                                            navigateTo(Destinations.DOODLE_ROUTE)
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }

                        }
                        items(uiState.note.imageList) { image ->
                            if (!image.uri.isEmpty()) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = image.uri),
                                    contentDescription = "hi",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(5.dp)
                                        .clip(fabShape)
                                        .border(
                                            0.5.dp,
                                            MaterialTheme.colors.primary,
                                            fabShape
                                        )
                                        .clickable {
                                            // saveDoodleId(doodle.doodleid)
                                            //navigateTo(Destinations.IMAGE_ROUTE)
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
                BasicTextField(
                    value = description.value,
                    onValueChange = { tx ->
                        description.value = tx
                    },
                    textStyle = noteFont,
                    cursorBrush = SolidColor(MaterialTheme.colors.primary),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, true)
                        .padding(14.dp, 3.dp, 14.dp, 50.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused && uiState.note.note.description.isEmpty()) {
                                focusRequester.requestFocus()
                            }
                            if (focus.value != focusState.isFocused) {
                                focus.value = focusState.isFocused
                                if (!focusState.isFocused && !showDialog) {
                                    inputService?.hideSoftwareKeyboard()
                                }
                            }
                        }
                )
            }
        })
    CustomAlertDialog(
        titleId = R.string.deletion_confirmation,
        showDialog = showDialog,
        setShowDialog = setShowDialog
    ) { delete.invoke() }
}

@Composable
fun NotesTopBar(backPress: () -> Unit, save: () -> Unit, showDialog: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            PaperIconButton(id = R.drawable.ic_back) {
                backPress.invoke()
            }
        },
        actions = {
            PaperIconButton(
                id = R.drawable.ic_check,
                /*   tint = if (description.value.text.trim()
                           .isEmpty()
                   ) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary*/
            ) {
                save.invoke()
            }
            PaperIconButton(
                id = R.drawable.ic_trash,
                /*tint = if (description.value.text.trim()
                        .isEmpty()
                ) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary*/
            ) {
                showDialog.invoke()
                /*if (note != null) {
                    setShowDialog(true)
                }*/
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )
}


@Composable
fun NotesBottomBar(note: Note, onClick: () -> Unit) {
    Column(
        Modifier
            .navigationBarsPadding()
            .imePadding()
    ) {
        BottomAppBar(
            modifier = Modifier.height(46.dp),
            backgroundColor = MaterialTheme.colors.background,
            contentPadding = PaddingValues(4.dp, 4.dp),
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PaperIconButton(id = R.drawable.ic_feather, onClick = onClick)
                    note.updatedOn.timeAgoInSeconds().let {
                        Text(
                            text = "Last updated $it",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.overline
                        )
                    }

                }
            }
        )
    }
}



