package io.ak1.paper.ui.screens.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.R
import io.ak1.paper.data.local.AppDatabase
import io.ak1.paper.models.Doodle
import io.ak1.paper.models.Image
import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import kotlinx.coroutines.launch

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

const val DEFAULT = "default"

class HomeViewModel(private val db: AppDatabase, private val context: Context) : ViewModel() {
    val noteDao = db.noteDao()
    val doodleDao = db.doodleDao()
    val imageDao = db.imageDao()
    val emptyNote = NoteWithDoodleAndImage(Note(DEFAULT, ""), ArrayList(), ArrayList())


    fun getAllDefaultNotes() = noteDao.getAllNotesByFolderId(DEFAULT)
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

    fun saveImage(vararg image: Image) = viewModelScope.launch {
        imageDao.insertAll(image = image)
    }


    fun deleteNote(value: Note?) {
        value?.let {
            viewModelScope.launch {
                noteDao.deleteNote(it.noteId)
            }
        }
    }

    fun deleteDoodle(value: Doodle?) {
        value?.let {
            viewModelScope.launch {
                doodleDao.deleteDoodle(value.doodleid)
            }
        }
    }

    fun getDoodle(id: String) = id.let { doodleDao.getDoodleById(it) }
}