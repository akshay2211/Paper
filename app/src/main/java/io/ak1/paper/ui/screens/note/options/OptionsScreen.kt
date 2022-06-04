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
package io.ak1.paper.ui.screens.note.options

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.note.image.ImageChooserType
import org.koin.androidx.compose.inject

/**
 * Created by akshay on 15/05/22
 * https://ak1.io
 */
data class Menu(val iconId: Int, val stringId: Int)


@Composable
fun OptionsScreen(navigateTo: (String) -> Unit, backPress: () -> Unit) {
    val optionsViewModel by inject<OptionsViewModel>()


    val list = listOf(
        Menu(R.drawable.ic_camera, R.string.take_photo),
        Menu(R.drawable.ic_image, R.string.add_image),
        Menu(R.drawable.ic_doodle, R.string.add_doodle),
    )

    val elevation = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )


    val colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    LazyColumn(modifier = Modifier.padding(3.dp, 12.dp)) {
        items(list) { it ->
            Button(
                onClick = {
                    backPress.invoke()
                    optionsViewModel.saveCurrentDoodleId()
                    optionsViewModel.saveCurrentImageId()
                    Handler(Looper.getMainLooper()).postDelayed({
                        when (it.iconId) {
                            R.drawable.ic_camera -> {
                                optionsViewModel.saveCurrentImageType(ImageChooserType.CAMERA)
                                navigateTo(Destinations.IMAGE_ROUTE)
                            }
                            R.drawable.ic_image -> {
                                optionsViewModel.saveCurrentImageType(ImageChooserType.GALLERY)
                                navigateTo(Destinations.IMAGE_ROUTE)
                            }
                            R.drawable.ic_doodle -> navigateTo(Destinations.DOODLE_ROUTE)
                        }
                    }, 100L)
                },
                elevation = elevation,
                colors = colors,
            ) {
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