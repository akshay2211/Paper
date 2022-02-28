package io.ak1.paper.ui.screens.note.doodle

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.PathWrapper
import io.ak1.drawbox.rememberDrawController
import io.ak1.paper.R
import io.ak1.paper.models.Doodle
import io.ak1.paper.ui.component.ColorRow
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.home.HomeViewModel
import io.ak1.paper.ui.theme.colors500
import io.ak1.paper.ui.utils.getEncodedString
import org.koin.androidx.compose.getViewModel


/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
@Composable
fun DoodleScreen(navController: NavHostController, isNewDoodle: Boolean, id: String) {

    val context = LocalContext.current
    val drawController = rememberDrawController()
    val homeViewModel = getViewModel<HomeViewModel>()
    val gsonBuilder = GsonBuilder().create()
    val doodleData = remember { mutableStateOf(Doodle(id, "", "")) }
    var undoCount = remember { mutableStateOf(0) }
    val redoCount = remember { mutableStateOf(0) }
    val defaultColor = remember { mutableStateOf(colors500[0]) }
    val colorBarVisibility = remember { mutableStateOf(true) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    var doodle = homeViewModel.getDoodle(id).observeAsState()

    LaunchedEffect(doodle.value) {
        if (!isNewDoodle) {
            doodle.value?.let {
                doodleData.value = it
                val listOfMyClassObject = object : TypeToken<ArrayList<PathWrapper>>() {}.type
                drawController.importPath(
                    gsonBuilder.fromJson(
                        doodleData.value.rawText,
                        listOfMyClassObject
                    )
                )
            }
        }
    }

    fun saveAndExit() {
        homeViewModel.saveDoodle(doodleData.value)
        navController.navigateUp()
    }
    Scaffold(topBar = {
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
                    if (drawController.exportPath()
                            .isNotEmpty()
                    ) drawController.saveBitmap() else navController.navigateUp()
                }
                if (doodle.value != null)
                    PaperIconButton(
                        id = R.drawable.ic_trash,
                    ) {
                        if (doodleData.value.rawText.isNotBlank() && doodle != null) setShowDialog(
                            true
                        ) else navController.navigateUp()
                    }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        )
    },
        bottomBar = {
            Column {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 0.dp
                ) {
                    PaperIconButton(id = R.drawable.ic_undo, enabled = undoCount.value != 0) {
                        drawController.unDo()
                    }
                    PaperIconButton(id = R.drawable.ic_redo, enabled = redoCount.value != 0) {
                        drawController.reDo()
                    }
                    Spacer(modifier = Modifier.weight(1f, fill = true))
                    PaperIconButton(id = R.drawable.ic_color, tint = defaultColor.value) {
                        colorBarVisibility.value = !colorBarVisibility.value
                    }
                }

                ColorRow(isVisible = colorBarVisibility.value, colors = colors500.apply {
                    // add(0, MaterialTheme.colors.primary)
                }) {
                    defaultColor.value = it
                    drawController.changeColor(it)
                }
            }
        }) {

        DrawBox(
            drawController = drawController,
            modifier = Modifier
                .fillMaxSize(),
            bitmapCallback = { bitmap, error ->
                val base64 = bitmap?.asAndroidBitmap()?.getEncodedString() ?: ""
                val list = drawController.exportPath()
                val json = gsonBuilder.toJson(list)
                doodleData.value.apply {
                    this.rawText = json
                    this.base64Text = base64
                }
                saveAndExit()
            }) { undo_count, redo_count ->
            colorBarVisibility.value = false
            undoCount.value = undo_count
            redoCount.value = redo_count
        }
    }
    CustomAlertDialog(
        titleId = R.string.deletion_confirmation_doodle,
        showDialog = showDialog,
        setShowDialog = setShowDialog
    ) {
        navController.navigateUp()
        homeViewModel.deleteDoodle(doodle.value)
        Toast.makeText(context, R.string.doodle_removed, Toast.LENGTH_LONG).show()
    }
}