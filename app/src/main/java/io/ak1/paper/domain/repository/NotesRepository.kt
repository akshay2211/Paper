package io.ak1.paper.domain.repository

import io.ak1.paper.models.Note
import io.ak1.paper.models.NoteWithDoodleAndImage
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun create(note: Note)
    suspend fun getNote(noteId: String): NoteWithDoodleAndImage?
    suspend fun delete(noteId: String)
    fun observeNotes(): Flow<List<NoteWithDoodleAndImage>>
    fun getNotesBySearch(query: String): Flow<List<NoteWithDoodleAndImage>>
    fun getNoteByFlow(noteId: String): Flow<NoteWithDoodleAndImage?>
}
