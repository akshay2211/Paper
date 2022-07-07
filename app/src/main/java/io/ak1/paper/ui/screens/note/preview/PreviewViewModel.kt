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
package io.ak1.paper.ui.screens.note.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.data.repositories.doodles.DoodlesRepository
import io.ak1.paper.data.repositories.image.ImageRepository
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.models.ClickableUri
import io.ak1.paper.ui.utils.getUriList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by akshay on 06/06/22
 * https://ak1.io
 */

data class PreviewUiState(
    val list: List<ClickableUri> = emptyList<ClickableUri>(),
    val loading: Boolean = false,
    var selection: Int = 0
)

class PreviewViewModel(
    private val localRepository: LocalRepository,
    private val imageRepository: ImageRepository,
    private val doodlesRepository: DoodlesRepository,
    private val notesRepository: NotesRepository,
) : ViewModel() {
    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(PreviewUiState(loading = true))
    val uiState: StateFlow<PreviewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localRepository.currentNote.collect { id ->
                notesRepository.getNoteByFlow(id).collect{note ->
                    note?.let {
                        _uiState.update {
                            it.copy(
                                list = note.getUriList(),
                                selection = localRepository.currentSelectedPosition.value
                            )
                        }
                    }

                }
            }
        }
    }

    fun saveCurrentDoodleId(currentDoodleId: String) {
        viewModelScope.launch {
            localRepository.saveCurrentDoodleId(currentDoodleId)
        }
    }

    fun saveCurrentImageId(currentImageId: String) {
        viewModelScope.launch {
            localRepository.saveCurrentImageId(currentImageId)
        }
    }

    fun deleteImage(imageId: String) {
        viewModelScope.launch {
            imageRepository.deleteImageById(imageId)
        }
    }

    fun deleteDoodle(doodleId: String) {
        viewModelScope.launch {
            doodlesRepository.deleteDoodleById(doodleId)
        }
    }

    fun deleteMedia(isDoodle: Boolean, mediaId: String) {
        if (isDoodle) {
            deleteDoodle(mediaId)
        } else {
            deleteImage(mediaId)
        }
    }


}