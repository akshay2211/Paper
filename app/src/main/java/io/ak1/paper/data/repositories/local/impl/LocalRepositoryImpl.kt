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
package io.ak1.paper.data.repositories.local.impl

import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.ui.screens.note.image.ImageChooserType
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

/**
 * Created by akshay on 10/05/22
 * https://ak1.io
 */


class LocalRepositoryImpl : LocalRepository {

    override val currentNote = MutableStateFlow("")
    override suspend fun saveCurrentNote(currentNote: String) {
        this.currentNote.emit(currentNote)
    }

    override val currentDoodleId = MutableStateFlow(UUID.randomUUID().toString())
    override suspend fun saveCurrentDoodleId(currentDoodleId: String) {
        this.currentDoodleId.emit(currentDoodleId)
    }


    override val currentImageId = MutableStateFlow(UUID.randomUUID().toString())
    override suspend fun saveCurrentImageId(currentImageId: String) {
        this.currentImageId.emit(currentImageId)
    }

    override val currentImageType = MutableStateFlow(ImageChooserType.NONE)
    override suspend fun saveCurrentImageType(imageChooserType: ImageChooserType) {
        this.currentImageType.emit(imageChooserType)
    }


}