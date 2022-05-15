package io.ak1.paper.ui.screens.note.doodle

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.DrawController
import io.ak1.drawbox.PathWrapper
import io.ak1.drawbox.rememberDrawController
import io.ak1.paper.R
import io.ak1.paper.models.Doodle
import io.ak1.paper.ui.component.ColorRow
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.theme.colors500
import io.ak1.paper.ui.utils.getEncodedString
import org.koin.androidx.compose.inject


/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
private val gsonBuilder = GsonBuilder().create()

@Composable
fun DoodleScreen(backPress: () -> Unit) {
    val doodleViewModel by inject<DoodleViewModel>()
    val uiState by doodleViewModel.uiState.collectAsState()

    val drawController = rememberDrawController()

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        uiState.doodle.let {
            if (it.rawText.isNotEmpty()) {
                val objList = object : TypeToken<ArrayList<PathWrapper>>() {}.type
                drawController.importPath(gsonBuilder.fromJson(it.rawText, objList))
            }
        }
    }
    DoodleScreen(
        uiState.doodle, drawController,
        { base64, json ->
            val newDoodle = uiState.doodle.apply {
                this.base64Text = base64
                this.rawText = json
            }
            doodleViewModel.saveDoodle(newDoodle)
            backPress.invoke()
        },
        { setShowDialog(true) }, backPress
    )


    val context = LocalContext.current

    CustomAlertDialog(
        titleId = R.string.deletion_confirmation_doodle,
        showDialog = showDialog,
        setShowDialog = setShowDialog
    ) {

        doodleViewModel.deleteDoodle(uiState.doodle)
        backPress.invoke()
        Toast.makeText(context, R.string.doodle_removed, Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun DoodleScreen(
    doodle: Doodle,
    drawController: DrawController,
    save: (base64: String, json: String) -> Unit,
    delete: () -> Unit,
    backPress: () -> Unit
) {

    var undoCount = remember { mutableStateOf(0) }
    val redoCount = remember { mutableStateOf(0) }
    val defaultColor = remember { mutableStateOf(colors500[0]) }
    val colorBarVisibility = remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier
        .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Doodle", color = MaterialTheme.colors.primary) },
                navigationIcon = {
                    PaperIconButton(id = R.drawable.ic_back) {
                        backPress.invoke()
                    }
                },
                actions = {
                    PaperIconButton(
                        id = R.drawable.ic_check,
                    ) {
                        if (drawController.exportPath()
                                .isNotEmpty()
                        ) {
                            if (colorBarVisibility.value) {
                                colorBarVisibility.value = false
                                Handler(Looper.getMainLooper()).postDelayed({
                                    drawController.saveBitmap()
                                }, 500L)
                                return@PaperIconButton
                            }
                            drawController.saveBitmap()
                        } else backPress.invoke()
                    }
                    PaperIconButton(
                        id = R.drawable.ic_trash,
                    ) {
                        if (doodle.rawText.isNotBlank()) delete.invoke() else backPress.invoke()
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
        }) { padding ->

        DrawBox(
            drawController = drawController,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            bitmapCallback = { bitmap, error ->
                val base64 = bitmap?.asAndroidBitmap()?.getEncodedString() ?: ""
                val list = drawController.exportPath()
                val json = gsonBuilder.toJson(list)
                Log.e("before save", "${gsonBuilder.toJson(doodle)}")
                save.invoke(base64, json)
            }) { undo_count, redo_count ->
            colorBarVisibility.value = false
            undoCount.value = undo_count
            redoCount.value = redo_count
        }
    }
    BackHandler(enabled = true) {
        if (drawController.exportPath().isNotEmpty())
            drawController.saveBitmap()
        else
            backPress.invoke()
    }

}
