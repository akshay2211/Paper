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
package io.ak1.paper.ui.screens.note.doodle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.data.repositories.doodles.DoodlesRepository
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.models.Doodle
import io.ak1.paper.models.Note
import io.ak1.paper.ui.screens.home.DEFAULT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by akshay on 15/05/22
 * https://ak1.io
 */

data class DoodleUiState(
    val doodle: Doodle = Doodle("", "", ""),
    val loading: Boolean = false
)

class DoodleViewModel(
    private val notesRepository: NotesRepository,
    private val doodlesRepository: DoodlesRepository,
    private val localRepository: LocalRepository
) : ViewModel() {
    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(DoodleUiState(loading = true))
    val uiState: StateFlow<DoodleUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            localRepository.currentDoodleId.collect { doodleId ->
                val doodle = doodlesRepository.getDoodleById(doodleId)
                    ?: Doodle(localRepository.currentNote.value, "", "")
                _uiState.update { it.copy(doodle = doodle) }
            }

        }
    }

    fun saveDoodle(doodle: Doodle) {
        viewModelScope.launch {
            val note = notesRepository.getNote(doodle.attachedNoteId)
            if (note == null) {
                val newNote = Note(DEFAULT, "").apply { noteId = doodle.attachedNoteId }
                notesRepository.create(newNote)
            }
            doodlesRepository.create(doodle)
        }
    }

    fun deleteDoodle(doodle: Doodle) {
        viewModelScope.launch {
            doodlesRepository.deleteDoodleById(doodleId = doodle.doodleid)
        }
    }
}