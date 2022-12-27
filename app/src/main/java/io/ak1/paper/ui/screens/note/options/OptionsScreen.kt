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
@file:OptIn(ExperimentalPermissionsApi::class)

package io.ak1.paper.ui.screens.note.options

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import io.ak1.paper.R
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.note.image.ImageChooserType
import org.koin.androidx.compose.get

/**
 * Created by akshay on 15/05/22
 * https://ak1.io
 */
data class Menu(val iconId: Int, val stringId: Int)

const val cameraPermission = Manifest.permission.MANAGE_DOCUMENTS

@Composable
fun OptionsScreen(
    navigateTo: (String) -> Unit,
    backPress: () -> Unit
) {
    val context = LocalContext.current
    val optionsViewModel = get<OptionsViewModel>()

    val cameraPermissionState = rememberPermissionState(cameraPermission) { isGranted: Boolean ->
        if (isGranted) {
            optionsViewModel.saveCurrentImageType(ImageChooserType.CAMERA)
            navigateTo(Destinations.IMAGE_ROUTE)
        }
    }


    val list = mutableListOf(
        Menu(R.drawable.ic_image, R.string.add_image),
        Menu(R.drawable.ic_doodle, R.string.add_doodle)
    ).apply {
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA))
            add(0, Menu(R.drawable.ic_camera, R.string.take_photo))
    }.toList()

    val elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )

    val colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    LazyColumn(
        modifier = Modifier
            .padding(3.dp, 12.dp)
            .navigationBarsPadding()
    ) {
        items(list) { it ->
            Button(
                onClick = {
                    backPress.invoke()
                    optionsViewModel.saveCurrentDoodleId()
                    optionsViewModel.saveCurrentImageId()
                    when (it.iconId) {
                        R.drawable.ic_camera -> {
                            when (cameraPermissionState.status) {
                                // If the camera permission is granted, then show screen with the feature enabled
                                PermissionStatus.Granted -> {
                                    optionsViewModel.saveCurrentImageType(ImageChooserType.CAMERA)
                                    navigateTo(Destinations.IMAGE_ROUTE)
                                }
                                is PermissionStatus.Denied -> {
                                    cameraPermissionState.launchPermissionRequest()
                                }
                                else -> cameraPermissionState.launchPermissionRequest()
                            }
                        }
                        R.drawable.ic_image -> {
                            optionsViewModel.saveCurrentImageType(ImageChooserType.GALLERY)
                            navigateTo(Destinations.IMAGE_ROUTE)
                        }
                        R.drawable.ic_doodle -> navigateTo(Destinations.DOODLE_ROUTE)
                    }
                },
                elevation = elevation,
                colors = colors,
            ) {
                Icon(
                    painterResource(id = it.iconId),
                    contentDescription = stringResource(id = it.stringId),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(10.dp, 3.dp)
                )
                Text(
                    text = stringResource(id = it.stringId),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(10.dp, 3.dp)
                )
            }
        }
    }
}