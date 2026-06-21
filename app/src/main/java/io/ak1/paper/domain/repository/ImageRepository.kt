package io.ak1.paper.domain.repository

import io.ak1.paper.models.Image

interface ImageRepository {
    suspend fun create(image: Image)
    suspend fun getImageById(imageId: String): Image?
    suspend fun deleteImageById(imageId: String)
    suspend fun deleteImageByNoteId(noteId: String)
}
