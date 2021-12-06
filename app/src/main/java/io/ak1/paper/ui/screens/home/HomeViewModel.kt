package io.ak1.paper.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.R
import io.ak1.paper.data.local.NoteDao
import io.ak1.paper.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

const val DEFAULT = "default"

class HomeViewModel(private val noteDao: NoteDao, private val context: Context) : ViewModel() {

    fun getAllDefaultNotes() = noteDao.getAllNotesByFolderId(DEFAULT)
    private suspend fun getNotesCount() = noteDao.getNotesCountByFolderId(DEFAULT)

    fun getAllNotesByDescription(query: String) =
        if (query.trim()
                .isNullOrEmpty()
        ) MutableLiveData(emptyList()) else noteDao.getNotesBySearch(query)


    fun insertDefaultData() {
        viewModelScope.launch {
            val count = getNotesCount()
            Log.e("count", "$count")
            if (count == 0) {
                noteDao.insert(
                    Note(
                        context.resources.getString(R.string.default_note_text),
                        folderId = DEFAULT
                    )
                )
                noteDao.insert(
                    Note(
                        context.resources.getString(R.string.default_note_text2),
                        folderId = DEFAULT
                    )
                )
                noteDao.insert(
                    Note(
                        context.resources.getString(R.string.default_note_text3),
                        folderId = DEFAULT
                    )
                )
            }
        }
    }

    fun getNote(it: String, callback: (Note) -> Unit) {
        viewModelScope.launch {

            val note = noteDao.getNoteById(it)
            note?.let {
                withContext(Dispatchers.Main) {
                    callback(it)
                }
            }

        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            noteDao.insert(note = note)
        }
    }

    fun deleteNote(value: Note?) {
        value?.let {
            viewModelScope.launch {
                noteDao.deleteNote(it.id)
            }
        }
    }
}