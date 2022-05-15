package io.ak1.paper.ui.screens.note.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.screens.home.DEFAULT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */


data class NoteUiState(
    val note: NoteWithDoodleAndImage = getEmptyNote(),
    val loading: Boolean = false
)

fun getEmptyNote() = NoteWithDoodleAndImage(Note(DEFAULT, ""), ArrayList(), ArrayList())

class NoteViewModel(
    private val notesRepository: NotesRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(NoteUiState(loading = true))
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        Log.e("init", "NoteViewModel ${localRepository.currentNote.value}")
        viewModelScope.launch {
            localRepository.currentNote.collect { id ->
                val newNote = notesRepository.getNote(id) ?: getEmptyNote()
                _uiState.update { it.copy(note = newNote) }
            }
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            notesRepository.create(note.apply {
                updatedOn = System.currentTimeMillis()
            })
        }

    }

    fun saveCurrentDoodleId(currentDoodleId: String) {
        viewModelScope.launch {
            localRepository.saveCurrentDoodleId(currentDoodleId)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.delete(note.noteId)
            localRepository.saveCurrentNote("")
        }
    }


}