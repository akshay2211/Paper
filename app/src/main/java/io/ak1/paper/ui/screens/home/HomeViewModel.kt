package io.ak1.paper.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.domain.repository.LocalRepository
import io.ak1.paper.domain.repository.NotesRepository
import io.ak1.paper.models.NoteWithDoodleAndImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DEFAULT = "default"

data class HomeUiState(
    val notes: List<NoteWithDoodleAndImage> = emptyList(),
    val loading: Boolean = true,
) {
    val isEmpty get() = !loading && notes.isEmpty()
}

sealed interface HomeEvent {
    data object OpenNewNote : HomeEvent
    data class OpenExistingNote(val noteId: String) : HomeEvent
}

class HomeViewModel(
    private val localRepository: LocalRepository,
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            notesRepository.observeNotes().collect { notes ->
                _uiState.update { it.copy(notes = notes, loading = false) }
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OpenNewNote -> viewModelScope.launch {
                localRepository.saveCurrentNote()
            }
            is HomeEvent.OpenExistingNote -> viewModelScope.launch {
                localRepository.saveCurrentNote(event.noteId)
            }
        }
    }

    fun getAllNotesByDescription(query: String) = notesRepository.getNotesBySearch(query)
}
