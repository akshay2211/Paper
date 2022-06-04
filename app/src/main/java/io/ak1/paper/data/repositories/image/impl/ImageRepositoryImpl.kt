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
package io.ak1.paper.data.repositories.image.impl

import io.ak1.paper.data.local.ImageDao
import io.ak1.paper.data.repositories.image.ImageRepository
import io.ak1.paper.models.Image

/**
 * Created by akshay on 04/06/22
 * https://ak1.io
 */
class ImageRepositoryImpl(private val imageDao: ImageDao) : ImageRepository {
    override suspend fun create(image: Image) {
        imageDao.insert(image)
    }

    override suspend fun getImageById(imageId: String): Image? {
        return imageDao.getImageById(imageId)
    }

    override suspend fun deleteImageById(imageId: String) {
        imageDao.deleteImage(imageId)
    }

    override suspend fun deleteImageByNoteId(noteId: String) {
        imageDao.deleteImageByNote(noteId)
    }
}