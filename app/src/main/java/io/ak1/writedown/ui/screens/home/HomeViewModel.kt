package io.ak1.writedown.ui.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.writedown.data.local.NoteDao
import io.ak1.writedown.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by akshay on 27/11/21
 * https://ak1.io
 */

const val DEFAULT = "default"

class HomeViewModel(private val noteDao: NoteDao) : ViewModel() {
    fun getAllDefaultNotes(): LiveData<List<Note>> {

        val list = noteDao.getAllNotesByFolderId(DEFAULT)
        if (list.value?.size == 0) {
            insertFakeData()
        }
        return list
    }

    fun getAllNotesByDescription(query: String) =
        if (query.trim().isNullOrEmpty()) MutableLiveData(emptyList()) else noteDao.getNotesBySearch(query)


    fun insertFakeData() {
        viewModelScope.launch {
            repeat(10) {
                Log.e("Data inserted", "repeat $it")
                noteDao.insert(Note("description $it", folderId = DEFAULT))
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