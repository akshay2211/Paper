package io.ak1.paper.domain.repository

import io.ak1.paper.models.Doodle

interface DoodlesRepository {
    suspend fun create(doodle: Doodle)
    suspend fun getDoodleById(doodleId: String): Doodle?
    suspend fun deleteDoodleById(doodleId: String)
    suspend fun deleteDoodleByNoteId(noteId: String)
}
