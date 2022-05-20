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

import io.ak1.paper.data.local.DoodleDao
import io.ak1.paper.data.local.ImageDao
import io.ak1.paper.data.local.NoteDao
import io.ak1.paper.data.repositories.notes.NotesRepository
import io.ak1.paper.models.Note
import io.ak1.paper.ui.screens.home.DEFAULT

/**
 * Created by akshay on 10/05/22
 * https://ak1.io
 */
class NotesRepositoryImpl(
    private val notesDao: NoteDao,
    private val doodleDao: DoodleDao,
    private val imageDao: ImageDao
) : NotesRepository {

    override suspend fun create(note: Note) {
        notesDao.insert(note = note)
    }

    override suspend fun getNote(noteId: String) = notesDao.getNoteById(noteId)

    override suspend fun delete(noteId: String) {
        notesDao.deleteNote(noteId)
        doodleDao.deleteDoodleByNote(noteId)
        imageDao.deleteImageByNote(noteId)
    }

    override fun observeNotes() = notesDao.getAllNotesByFolderId(DEFAULT)
    override fun getNotesBySearch(query: String) = notesDao.getNotesBySearch(query)

    override fun getNoteByFlow(noteId: String) = notesDao.getNoteByIdByFlow(noteId)
}