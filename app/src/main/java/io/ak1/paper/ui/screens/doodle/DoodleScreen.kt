package io.ak1.paper.ui.screens.doodle

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.PathWrapper
import io.ak1.drawbox.rememberDrawController
import io.ak1.paper.R
import io.ak1.paper.models.Doodle
import io.ak1.paper.models.Note
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.home.DEFAULT
import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.utils.getEncodedString
import org.koin.java.KoinJavaComponent
import java.lang.reflect.Type


/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
@Composable
fun DoodleScreen(navController: NavHostController, noteId: String? = null) {


    val drawController = rememberDrawController()
    val homeViewModel by KoinJavaComponent.inject<HomeViewModel>(HomeViewModel::class.java)
    val note = remember {
        mutableStateOf<Note?>(null)
    }
    val doodleData = remember {
        mutableStateOf<Doodle?>(null)
    }
    LaunchedEffect(note) {
        noteId?.let { noteId ->
            homeViewModel.getNote(noteId) {
                note.value = it
            }
        }
        // inputService?.showSoftwareKeyboard()
        //focusRequester.requestFocus()
    }
    fun saveAndExit() {
        var newNote = ""
        if (note.value == null) {
            val n = Note(
                description = "",
                doodle = doodleData.value?.rawText?.trim(),
                imageText = doodleData.value?.base64Text?.trim(),
                folderId = DEFAULT
            )
            newNote = n.id
            homeViewModel.saveNote(
                n
            )
        } else if (note.value != null && note.value?.doodle != doodleData.value?.rawText?.trim()) {
            note.value?.apply {
                this.doodle = doodleData.value?.rawText?.trim()
                this.imageText = doodleData.value?.base64Text?.trim()
                this.updatedOn = System.currentTimeMillis()
            }
            newNote = "${note.value?.id}"
            homeViewModel.saveNote(note.value!!)
        }
        navController.popBackStack(Destinations.NOTE_ROUTE, inclusive = true, saveState = true)
        navController.navigate("${Destinations.NOTE_ROUTE}/$newNote")
    }


    Column {
        TopAppBar(
            title = { Text(text = "Doodle", color = MaterialTheme.colors.primary) },
            navigationIcon = {
                PaperIconButton(id = R.drawable.ic_back) {
                    navController.navigateUp()
                }
            },
            actions = {
                PaperIconButton(
                    id = R.drawable.ic_check,
                ) {
                    val base64 = drawController.getDrawBoxBitmap()?.getEncodedString() ?: ""
                    val list = drawController.exportPath()
                    val json = GsonBuilder().create().toJson(list)
                    val doodle = Doodle(json, base64)
                    doodleData.value = doodle
                    saveAndExit()

                }
                PaperIconButton(
                    id = R.drawable.ic_trash,
                ) {
                    navController.navigateUp()
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        )
        DrawBox(
            drawController = drawController,
            Modifier
                .fillMaxSize()
                .weight(1f, true)
        )
    }
}