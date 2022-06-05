/*
 * Copyright (C) 2022 akshay2211 (Akshay Sharma)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ak1.paper.ui.screens.note.image

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import io.ak1.paper.R
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.utils.clickImage
import io.ak1.paper.ui.utils.saveImage
import org.koin.androidx.compose.inject

/**
 * Created by akshay on 28/05/22
 * https://ak1.io
 */

@Composable
fun PreviewScreen(backPress: () -> Unit) {
    val imageViewModel by inject<ImageViewModel>()
    val uiState by imageViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
    val imageData = remember { mutableStateOf<Uri?>(null) }
    val currentPhotoPath = remember { mutableStateOf("") }
    val imageClickedUri = remember { mutableStateOf<Uri?>(null) }


    val image = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture())
    { result ->
        Log.e("image call", "$result    file://${currentPhotoPath.value}")
        if (result) {
            imageData.value = imageClickedUri.value
            imageViewModel.saveCurrentImageType()
            //imageData.value = Uri.parse("file://${currentPhotoPath.value}")
        } else {
            backPress.invoke()
        }
    }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
    { result ->
        if (result == null) {
            backPress.invoke()
            return@rememberLauncherForActivityResult
        }
        imageViewModel.saveCurrentImageType()
        Log.e("gallery call", "$result")

        imageData.value = result
    }

    LaunchedEffect(uiState) {
        when (uiState.openImageChooser) {
            ImageChooserType.CAMERA -> context.clickImage(currentPhotoPath) {
                Log.e("photo uri", "$it")
                imageClickedUri.value = it
                image.launch(it)
            }
            ImageChooserType.GALLERY -> gallery.launch("image/*")
            else -> {

            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(text = "Add Image", color = MaterialTheme.colors.primary) },
                navigationIcon = {
                    PaperIconButton(id = R.drawable.ic_back) {
                        backPress.invoke()
                    }
                },
                actions = {
                    PaperIconButton(
                        id = R.drawable.ic_check,
                    ) {
                        if (bitmap.value == null) {
                            Log.e("Bitmap", "Is null")
                        }
                        val uri = context.saveImage(bitmap.value, uiState.image.imageId)
                        imageViewModel.save(uri, bitmap.value)
                        backPress.invoke()
                    }
                    PaperIconButton(
                        id = R.drawable.ic_trash,
                    ) {
                    }
                },
                //  backgroundColor = defaultBgColor.value,
                elevation = 0.dp,
                backgroundColor = MaterialTheme.colors.background,
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            ) {
                PaperIconButton(id = R.drawable.ic_crop) {
                }

            }

        }) { paddingValues ->
        imageData.value?.let {
            Image(
                painter = rememberAsyncImagePainter(model = imageData.value, onState = {
                    if (it is AsyncImagePainter.State.Success) {
                        bitmap.value = it.result.drawable.toBitmap()
                    }
                }),
                contentDescription = "Image",
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        }

    }
}
