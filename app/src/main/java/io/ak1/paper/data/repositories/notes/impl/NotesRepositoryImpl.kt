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
package io.ak1.paper.data.repositories.notes.impl

import io.ak1.paper.data.local.NoteDao
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.models.Note
import io.ak1.paper.ui.screens.home.DEFAULT
import kotlinx.coroutines.flow.Flow

/**
 * Created by akshay on 10/05/22
 * https://ak1.io
 */
class NotesRepositoryImpl(private val notesDao: NoteDao) : NotesRepository {

    override suspend fun create(note: Note): Note {
        TODO("Not yet implemented")
    }

    override fun getAllNotesByFolderId(folderId: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override fun getNote(noteId: String): Flow<Note> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(noteId: String) {
        TODO("Not yet implemented")
    }

    override fun observeNotes() = notesDao.getAllNotesByFolderId(DEFAULT)
}