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
@file:OptIn(ExperimentalSnapperApi::class)

package io.ak1.paper.ui.screens.note.preview

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import io.ak1.paper.R
import io.ak1.paper.ui.component.PaperIconButton
import io.ak1.paper.ui.screens.Destinations
import org.koin.androidx.compose.inject

/**
 * Created by akshay on 05/06/22
 * https://ak1.io
 */

@Composable
fun PreviewScreen(navigateTo: (String) -> Unit, backPress: () -> Unit) {
    val lazyListState = rememberLazyListState()
    val previewViewModel by inject<PreviewViewModel>()
    val uiState by previewViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.selection) {
        lazyListState.scrollToItem(uiState.selection)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapperFlingBehavior(lazyListState),
        ) {
            items(uiState.list) { item ->
                Image(
                    painter = rememberAsyncImagePainter(model = item.uri),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .fillMaxSize(),
                )
            }
        }
        val item = if (uiState.list.isNotEmpty()) {
            uiState.list[lazyListState.firstVisibleItemIndex]
        } else null

        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = { },
            navigationIcon = {
                PaperIconButton(id = R.drawable.ic_back) {
                    backPress.invoke()
                }
            },
            actions = {
                if (item?.isDoodle == true) {
                    PaperIconButton(
                        // id = if (item.isDoodle == true) R.drawable.ic_edit else R.drawable.ic_crop,
                        id = R.drawable.ic_edit
                    ) {
                        item.let {
                            if (it.isDoodle) {
                                previewViewModel.saveCurrentDoodleId(it.id)
                                navigateTo(Destinations.DOODLE_ROUTE)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Image Editing not working",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                previewViewModel.saveCurrentImageId(it.id)
                                navigateTo(Destinations.IMAGE_ROUTE)
                            }
                        }

                    }
                }
                PaperIconButton(
                    id = R.drawable.ic_trash,
                ) {
                    item?.isDoodle?.let {
                        previewViewModel.deleteMedia(it, item.id)
                    }
                    Toast.makeText(context, R.string.media_deleted, Toast.LENGTH_SHORT).show()
                    if (uiState.list.size <= 1) {
                        backPress.invoke()
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background.copy(0.4f),
            elevation = 0.dp
        )
    }

}