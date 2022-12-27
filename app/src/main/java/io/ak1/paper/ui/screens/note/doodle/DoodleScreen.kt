@file:OptIn(ExperimentalMaterial3Api::class)

package io.ak1.paper.ui.screens.note.doodle

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ak1.drawbox.*
import io.ak1.paper.R
import io.ak1.paper.models.Doodle
import io.ak1.paper.ui.component.CustomAlertDialog
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.utils.getEncodedString
import io.ak1.paper.ui.utils.saveImage
import io.ak1.rangvikalp.RangVikalp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get


/**
 * Created by akshay on 01/01/22
 * https://ak1.io
 */
private val gsonBuilder = GsonBuilder().create()

@Composable
fun DoodleScreen(backPress: () -> Unit) {
    val doodleViewModel  = get<DoodleViewModel>()
    val uiState by doodleViewModel.uiState.collectAsState()
    val defaultColor = MaterialTheme.colorScheme.surface
    val drawController = rememberDrawController()

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        drawController.changeBgColor(defaultColor)
        uiState.doodle.let {
            if (it.rawText.isNotEmpty()) {
                val payload = try {
                    gsonBuilder.fromJson(
                        it.rawText,
                        DrawBoxPayLoad::class.java
                    )
                } catch (i: JsonSyntaxException) {
                    val objList = object : TypeToken<ArrayList<PathWrapper>>() {}.type
                    DrawBoxPayLoad(defaultColor, gsonBuilder.fromJson(it.rawText, objList))

                }
                drawController.importPath(payload)
            }
        }
    }
    DoodleScreen(
        uiState.doodle, drawController,
        { base64, json, uri ->
            val newDoodle = uiState.doodle.apply {
                this.base64Text = base64
                this.rawText = json
                this.uri = uri.toString()
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
    save: (base64: String, json: String, uri: Uri?) -> Unit,
    delete: () -> Unit,
    backPress: () -> Unit
) {

    val undoCount = remember { mutableStateOf(0) }
    val redoCount = remember { mutableStateOf(0) }
    val defaultTextColor = remember { Animatable(drawController.color) }
    val defaultBgColor = remember { Animatable(drawController.bgColor) }
    var colorIsBg by remember { mutableStateOf(false) }
    var colorBarVisibility by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(drawController) {
        defaultBgColor.snapTo(drawController.bgColor)
        defaultTextColor.snapTo(drawController.color)
    }

    Scaffold(
        containerColor = defaultBgColor.value,
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = "Doodle", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    PaperIconButton(id = R.drawable.ic_back) {
                        backPress.invoke()
                    }
                },
                actions = {
                    PaperIconButton(
                        id = R.drawable.ic_check,
                    ) {
                        if (drawController.exportPath().path.isNotEmpty()) {
                            if (colorBarVisibility) {
                                colorBarVisibility = false
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
               // colors = defaultBgColor.value,
               // ele = 0.dp
            )
        },
        bottomBar = {
            Column(Modifier.background(MaterialTheme.colorScheme.surface.copy(0.1f))) {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    PaperIconButton(id = R.drawable.ic_undo, enabled = undoCount.value != 0) {
                        drawController.unDo()
                    }
                    PaperIconButton(id = R.drawable.ic_redo, enabled = redoCount.value != 0) {
                        drawController.reDo()
                    }
                    Spacer(modifier = Modifier.weight(1f, fill = true))
                    PaperIconButton(
                        id = R.drawable.ic_color,
                        tint = defaultBgColor.value,
                        border = defaultBgColor.value == drawController.bgColor
                    ) {
                        colorBarVisibility = when (colorBarVisibility) {
                            false -> true
                            !colorIsBg -> true
                            else -> false
                        }
                        colorIsBg = true
                        drawController.changeBgColor(defaultBgColor.value)

                    }
                    PaperIconButton(id = R.drawable.ic_text, tint = defaultTextColor.value) {
                        colorBarVisibility = when (colorBarVisibility) {
                            false -> true
                            colorIsBg -> true
                            else -> false
                        }
                        colorIsBg = false
                        drawController.changeColor(defaultTextColor.value)
                    }
                }

                RangVikalp(
                    isVisible = colorBarVisibility,
                    defaultColor = if (colorIsBg) defaultBgColor.value else defaultTextColor.value,
                    colorIntensity = if (colorIsBg) 0 else 7
                ) {
                    coroutine.launch {
                        if (colorIsBg) {
                            defaultBgColor.animateTo(it, animationSpec = tween(1000)) {
                                drawController.changeBgColor(this.value)
                            }

                        } else {
                            defaultTextColor.animateTo(it, animationSpec = tween(1000)) {
                                drawController.changeColor(this.value)
                            }

                        }

                    }
                }
            }
        }) { padding ->
        val modifier = Modifier
            .fillMaxSize()
        Box(modifier.padding(padding)) {
            DrawBox(
                drawController = drawController,
                modifier = modifier,
                backgroundColor = defaultBgColor.value,
                bitmapCallback = { bitmap, error ->
                    val base64 = bitmap?.asAndroidBitmap()?.getEncodedString() ?: ""
                    val uri = context.saveImage(bitmap?.asAndroidBitmap(), doodle.doodleid)
                    val list = drawController.exportPath()
                    val json = gsonBuilder.toJson(list)
                    save.invoke(base64, json, uri)
                }) { undo_count, redo_count ->
                colorBarVisibility = false
                undoCount.value = undo_count
                redoCount.value = redo_count
            }
        }
    }
    BackHandler(enabled = true) {
        if (drawController.exportPath().path.isNotEmpty())
            drawController.saveBitmap()
        else
            backPress.invoke()
    }

}
