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
package io.ak1.paper.data.repositories.doodles.impl

import io.ak1.paper.data.local.DoodleDao
import io.ak1.paper.data.repositories.doodles.DoodlesRepository
import io.ak1.paper.models.Doodle
import kotlinx.coroutines.flow.Flow

/**
 * Created by akshay on 15/05/22
 * https://ak1.io
 */
class DoodlesRepositoryImpl(private val doodleDao: DoodleDao) : DoodlesRepository {
    override suspend fun create(doodle: Doodle) {
        doodleDao.insert(doodle)
    }

    override suspend fun getDoodleById(doodleId: String) = doodleDao.getDoodleById(doodleId)

    override suspend fun deleteDoodleById(doodleId: String) {
        doodleDao.deleteDoodle(doodleId)
    }

    override suspend fun deleteDoodleByNoteId(noteId: String) {
        doodleDao.deleteDoodleByNote(noteId)
    }
}