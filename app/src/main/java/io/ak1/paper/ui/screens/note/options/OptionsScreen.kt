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
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.ak1.paper.R
import io.ak1.paper.ui.screens.Destinations
import io.ak1.paper.ui.screens.note.note.NoteViewModel
import org.koin.androidx.compose.inject

/**
 * Created by akshay on 15/05/22
 * https://ak1.io
 */
data class Menu(val iconId: Int, val stringId: Int)

@Composable
fun OptionsScreen(navigateTo: (String) -> Unit, backPress: () -> Unit) {
    val noteViewModel by inject<NoteViewModel>()
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
                        backPress.invoke()
                        Handler(Looper.getMainLooper()).postDelayed({
                            noteViewModel.saveCurrentDoodleId("")
                            navigateTo(Destinations.DOODLE_ROUTE)
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