package io.ak1.paper.ui.screens.note.doodle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.domain.repository.DoodlesRepository
import io.ak1.paper.domain.repository.LocalRepository
import io.ak1.paper.domain.repository.NotesRepository
import io.ak1.paper.models.Doodle
import io.ak1.paper.models.Note
import io.ak1.paper.ui.screens.home.DEFAULT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DoodleUiState(
    val doodle: Doodle = Doodle("", "", "", ""),
    val loading: Boolean = false,
)

sealed interface DoodleEvent {
    data class Save(val doodle: Doodle) : DoodleEvent
    data class Delete(val doodle: Doodle) : DoodleEvent
}

class DoodleViewModel(
    private val notesRepository: NotesRepository,
    private val doodlesRepository: DoodlesRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoodleUiState(loading = true))
    val uiState: StateFlow<DoodleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localRepository.currentDoodleId.collect { doodleId ->
                val doodle = doodlesRepository.getDoodleById(doodleId)
                    ?: Doodle(localRepository.currentNote.value, "", "", "")
                _uiState.update { it.copy(doodle = doodle) }
            }
        }
    }

    fun onEvent(event: DoodleEvent) {
        when (event) {
            is DoodleEvent.Save -> viewModelScope.launch {
                val note = notesRepository.getNote(event.doodle.attachedNoteId)
                if (note == null) {
                    val newNote = Note(DEFAULT, "").apply { noteId = event.doodle.attachedNoteId }
                    notesRepository.create(newNote)
                }
                doodlesRepository.create(event.doodle)
            }
            is DoodleEvent.Delete -> viewModelScope.launch {
                doodlesRepository.deleteDoodleById(event.doodle.doodleid)
            }
        }
    }
}
