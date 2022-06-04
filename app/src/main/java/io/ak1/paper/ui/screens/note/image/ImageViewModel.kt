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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.data.repositories.image.ImageRepository
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.models.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by akshay on 04/06/22
 * https://ak1.io
 */
data class ImageUiState(
    val image: Image = Image("", "", ""),
    val openImageChooser: ImageChooserType = ImageChooserType.NONE,
    val loading: Boolean = false
)

enum class ImageChooserType {
    CAMERA, GALLERY, NONE
}

class ImageViewModel(
    private val imageRepository: ImageRepository,
    private val localRepository: LocalRepository
) :
    ViewModel() {
    fun save(encodedString: String?) {
        _uiState.update { it.copy(image = it.image.copy(imageText = encodedString.toString())) }
        encodedString?.let {
            viewModelScope.launch {
                imageRepository.create(_uiState.value.image)
            }
        }
    }

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(ImageUiState(loading = true))
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    init {
        if (localRepository.currentImageId.value.isNullOrEmpty()) {
            _uiState.update {
                it.copy(image = Image(localRepository.currentNote.value, "", ""))
            }
            _uiState.update { it.copy(openImageChooser = localRepository.currentImageType.value) }
        }
    }

    fun saveCurrentImageType(imageChooserType: ImageChooserType = ImageChooserType.NONE) {
        viewModelScope.launch {
            localRepository.saveCurrentImageType(imageChooserType)
        }
        _uiState.update { it.copy(openImageChooser = imageChooserType) }
    }
}