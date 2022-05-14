package io.ak1.paper.ui.screens.note.note

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.ak1.paper.R
import io.ak1.paper.models.Doodle
import io.ak1.paper.models.Image
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.utils.convert
import io.ak1.paper.ui.utils.timeAgoInSeconds
import org.koin.androidx.compose.getViewModel

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
@Composable
fun NoteScreen(
    navController: NavController,
    innerNavController: NavHostController,
    note: MutableState<NoteWithDoodleAndImage>
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    val focus = remember { mutableStateOf(false) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val homeViewModel = getViewModel<HomeViewModel>()

    val noteFont: TextStyle = TextStyle(
        fontWeight = FontWeight.Thin,
        color = MaterialTheme.colors.primary,
        fontSize = 20.sp,
        letterSpacing = 0.10.sp
    )

    val description = remember {
        mutableStateOf(TextFieldValue())
    }
    val doodles = remember {
        mutableStateOf(ArrayList<Doodle>())
    }
    val images = remember {
        mutableStateOf(ArrayList<Image>())
    }
    val localNote = remember {
        mutableStateOf(homeViewModel.emptyNote)
    }

    LaunchedEffect(note.value) {
        note.value.let {
            localNote.value = it
            description.value = TextFieldValue(
                annotatedString = AnnotatedString(it.note.description),
                TextRange(it.note.description.length)
            )
            if (it.doodleList.isNotEmpty()) {
                doodles.value.apply {
                    clear()
                    addAll(it.doodleList)
                }
            }
            if (it.imageList.isNotEmpty()) {
                images.value.apply {
                    clear()
                    addAll(it.imageList)
                }
            }
        }
        //inputService?.showSoftwareKeyboard()
        //focusRequester.requestFocus()
    }


    fun saveAndExit() {
        if (note.value.note.description != description.value.text.trim() || doodles.value.isNotEmpty() || images.value.isNotEmpty()) {
            note.value.note.description = description.value.text.trim()

            homeViewModel.saveDoodle(doodles.value, note.value.note.noteId)
            if (images.value.isNotEmpty()) {
                val newImages = images.value.map {
                    it.attachedNoteId = note.value.note.noteId
                    it
                }.toTypedArray()
                homeViewModel.saveImage(*newImages)
            }
            homeViewModel.saveNote(note.value.note)
        }
        if (note.value.note.description.isEmpty() && doodles.value.isEmpty() && images.value.isEmpty()) {
            homeViewModel.deleteNote(note.value.note)
        }
        navController.navigateUp()
    }



    Scaffold(modifier = Modifier
        .statusBarsPadding(),
        topBar = {
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
                        saveAndExit()
                        // navController.navigateUp()
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
            Column(
                Modifier
                    .navigationBarsPadding()
                    .imePadding()
            ) {

            }
        }) { paddingValues ->
        val paddingBottom = paddingValues.calculateBottomPadding()
        val padding = if (paddingBottom > 46.dp) paddingBottom - 46.dp else paddingBottom
        Column(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, padding)
                .fillMaxSize()
        ) {
            if (note.value.doodleList.isNotEmpty()) {
                LazyRow(modifier = Modifier.padding(5.dp, 15.dp)) {
                    items(note.value.doodleList) { doodle ->
                        doodle.base64Text.convert()?.let {
                            androidx.compose.foundation.Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "hi",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(5.dp)
                                    .clip(CircleShape)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colors.primary,
                                        CircleShape
                                    )
                                    .clickable {
                                        innerNavController.navigate("${Destinations.DOODLE_ROUTE}/${doodle.doodleid}")
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

    CustomAlertDialog(
        titleId = R.string.deletion_confirmation,
        showDialog = showDialog,
        setShowDialog = setShowDialog
    ) {
        navController.navigateUp()
        homeViewModel.deleteNote(note.value.note)
        Toast.makeText(context, R.string.note_removed, Toast.LENGTH_LONG).show()
    }
}

data class Menu(val iconId: Int, val stringId: Int)

@Composable
fun AddMoreScreen(navHostController: NavHostController, nodeId: String? = null) {

    val context = LocalContext.current
    var list = listOf(
        Menu(R.drawable.ic_more, R.string.take_photo),
        Menu(R.drawable.ic_search, R.string.add_image),
        Menu(R.drawable.ic_feather, R.string.add_doodle),
    )
    LazyColumn {
        items(list) { it ->
            IconButton(
                onClick = {
                    if (it.iconId == R.drawable.ic_feather) {
                        navHostController.navigateUp()
                        Handler(Looper.getMainLooper()).postDelayed({
                            navHostController.navigate(Destinations.DOODLE_ROUTE)
                        }, 100L)

                    } else {
                        Toast.makeText(context, "Coming soon", Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Row {

                    Icon(
                        painterResource(id = it.iconId),
                        contentDescription = stringResource(id = it.stringId),
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(10.dp, 3.dp)
                    )
                    Text(
                        text = stringResource(id = it.stringId),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .weight(1f, true)
                            .padding(10.dp, 3.dp)
                    )
                }

            }
        }
    }
}
