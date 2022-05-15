package io.ak1.paper.ui.screens.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.R
import io.ak1.paper.data.local.AppDatabase
import io.ak1.paper.data.repositories.local.LocalRepository
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.models.Doodle
import io.ak1.paper.models.Image
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import io.ak1.paper.ui.screens.note.note.getEmptyNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */
data class HomeUiState(
    val notes: List<NoteWithDoodleAndImage> = emptyList(),
    val loading: Boolean = false
)


const val DEFAULT = "default"

class HomeViewModel(
    private val db: AppDatabase,
    private val context: Context,
    private val localRepository: LocalRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        // Observe for Notes changes in the repo layer
        viewModelScope.launch {
            notesRepository.observeNotes().collect { notes ->
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun saveCurrentNote(currentNote: NoteWithDoodleAndImage?) {
        viewModelScope.launch {
            localRepository.saveCurrentNote(currentNote ?: getEmptyNote())
        }
    }


    val noteDao = db.noteDao()
    val doodleDao = db.doodleDao()
    val imageDao = db.imageDao()

    private suspend fun getNotesCount() = noteDao.getNotesCountByFolderId(DEFAULT)

    fun getAllNotesByDescription(query: String) = if (query.trim().isEmpty()) MutableLiveData(
        emptyList()
    ) else noteDao.getNotesBySearch(query)


    fun insertDefaultData() {
        viewModelScope.launch {
            val count = getNotesCount()
            if (count == 0) {
                noteDao.insert(
                    Note(
                        description = context.resources.getString(R.string.default_note_text),
                        folderId = DEFAULT
                    )
                )
                noteDao.insert(
                    Note(
                        description = context.resources.getString(R.string.default_note_text2),
                        folderId = DEFAULT
                    )
                )
                noteDao.insert(
                    Note(
                        description = context.resources.getString(R.string.default_note_text3),
                        folderId = DEFAULT
                    )
                )
            }
        }
    }


    fun getNote(it: String?) = it?.let { noteDao.getNoteById(it) }

    fun saveNote(note: Note) = viewModelScope.launch {
        noteDao.insert(note = note)
    }

    fun saveDoodle(vararg doodle: Doodle) = viewModelScope.launch {
        doodleDao.insertAll(doodle = doodle)
    }

    fun saveDoodle(doodles: ArrayList<Doodle>, noteId: String) {
        if (doodles.isNotEmpty()) {
            val newDoodles = doodles.map {
                it.attachedNoteId = noteId
                it
            }.toTypedArray()
            saveDoodle(*newDoodles)
        }
    }

    fun saveImage(vararg image: Image) = viewModelScope.launch {
        imageDao.insertAll(image = image)
    }

    fun saveImage(images: ArrayList<Image>, noteId: String) {
        if (images.isNotEmpty()) {
            val newImages = images.map {
                it.attachedNoteId = noteId
                it
            }.toTypedArray()
            saveImage(*newImages)
        }
    }




}
