package io.ak1.paper.ui.screens.note.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.domain.repository.LocalRepository
import io.ak1.paper.domain.repository.NotesRepository
import io.ak1.paper.models.ClickableUri
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.screens.home.DEFAULT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteUiState(
    val note: NoteWithDoodleAndImage = getEmptyNote(),
    val loading: Boolean = false,
)

fun getEmptyNote(id: String? = null) = NoteWithDoodleAndImage(
    Note(DEFAULT, "").apply { if (id != null) noteId = id },
    ArrayList(),
    ArrayList(),
)

sealed interface NoteEvent {
    data class SetSelectedImage(val pos: Int) : NoteEvent
    data class SaveNote(val note: Note) : NoteEvent
    data class DeleteNote(val note: Note) : NoteEvent
    data class SetCurrentMediaList(val uriList: MutableList<ClickableUri>) : NoteEvent
}

class NoteViewModel(
    private val notesRepository: NotesRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState(loading = true))
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localRepository.currentNote.collect { id ->
                notesRepository.getNoteByFlow(id).collect { note ->
                    val newNote = note ?: getEmptyNote(localRepository.currentNote.value)
                    _uiState.update { it.copy(note = newNote) }
                }
            }
        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.SetSelectedImage -> viewModelScope.launch {
                localRepository.saveSelectedPosition(event.pos)
            }
            is NoteEvent.SaveNote -> viewModelScope.launch {
                notesRepository.create(event.note.apply { updatedOn = System.currentTimeMillis() })
            }
            is NoteEvent.DeleteNote -> viewModelScope.launch {
                notesRepository.delete(event.note.noteId)
                localRepository.saveCurrentNote()
            }
            is NoteEvent.SetCurrentMediaList -> viewModelScope.launch {
                localRepository.saveCurrentMediaList(event.uriList)
            }
        }
    }
}
