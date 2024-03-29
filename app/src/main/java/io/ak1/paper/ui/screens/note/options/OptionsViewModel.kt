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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.ui.screens.note.image.ImageChooserType
import kotlinx.coroutines.launch

/**
 * Created by akshay on 04/06/22
 * https://ak1.io
 */
class OptionsViewModel(private val localRepository: LocalRepository) : ViewModel() {
    fun saveCurrentDoodleId(currentDoodleId: String = "") {
        viewModelScope.launch {
            localRepository.saveCurrentDoodleId(currentDoodleId)
        }
    }

    fun saveCurrentImageId(currentImageId: String = "") {
        viewModelScope.launch {
            localRepository.saveCurrentImageId(currentImageId)
        }
    }

    fun saveCurrentImageType(imageChooserType: ImageChooserType = ImageChooserType.NONE) {
        viewModelScope.launch {
            localRepository.saveCurrentImageType(imageChooserType)
        }
    }
}