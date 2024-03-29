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
package io.ak1.paper.data.repositories.local

import io.ak1.paper.models.ClickableUri
import io.ak1.paper.ui.screens.note.image.ImageChooserType
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

/**
 * Created by akshay on 14/05/22
 * https://ak1.io
 */
interface LocalRepository {
    suspend fun saveCurrentNote(currentNote: String = UUID.randomUUID().toString())
    val currentNote: MutableStateFlow<String>

    suspend fun saveCurrentDoodleId(currentDoodleId: String)
    val currentDoodleId: MutableStateFlow<String>

    suspend fun saveCurrentImageId(currentImageId: String)
    val currentImageId: MutableStateFlow<String>

    suspend fun saveCurrentImageType(imageChooserType: ImageChooserType)
    val currentImageType: MutableStateFlow<ImageChooserType>

    suspend fun saveCurrentMediaList(list: List<ClickableUri>)
    val currentMediaList: MutableStateFlow<List<ClickableUri>>

    suspend fun saveSelectedPosition(position: Int)
    val currentSelectedPosition: MutableStateFlow<Int>
}