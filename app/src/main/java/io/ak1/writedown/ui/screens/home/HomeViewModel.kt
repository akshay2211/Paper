package io.ak1.writedown.ui.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.writedown.data.local.NoteDao
import io.ak1.writedown.models.Note
import kotlinx.coroutines.launch

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


    fun insertFakeData() {
        viewModelScope.launch {
            repeat(10) {
                Log.e("Data inserted", "repeat $it")
                noteDao.insert(Note("description $it", folderId = DEFAULT))
            }
        }
    }
}