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
package io.ak1.paper.data.repositories.image

import io.ak1.paper.models.Image

/**
 * Created by akshay on 04/06/22
 * https://ak1.io
 */
interface ImageRepository {
    suspend fun create(image: Image)
    suspend fun getImageById(imageId: String): Image?
    suspend fun deleteImageById(imageId: String)
    suspend fun deleteImageByNoteId(noteId: String)
}