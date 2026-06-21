package io.ak1.paper.domain.repository

import io.ak1.paper.domain.model.ImageChooserType
import io.ak1.paper.models.ClickableUri
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

interface LocalRepository {
    suspend fun saveCurrentNote(currentNote: String = UUID.randomUUID().toString())
    val currentNote: MutableStateFlow<String>

    suspend fun saveCurrentDoodleId(currentDoodleId: String)
    val currentDoodleId: MutableStateFlow<String>

    suspend fun saveCurrentImageId(currentImageId: String)
    val currentImageId: MutableStateFlow<String>

    suspend fun saveCurrentImageType(imageChooserType: ImageChooserType)
    val currentImageType: MutableStateFlow<ImageChooserType>

    suspend fun saveCurrentMediaList(list: List<ClickableUri>)
    val currentMediaList: MutableStateFlow<List<ClickableUri>>

    suspend fun saveSelectedPosition(position: Int)
    val currentSelectedPosition: MutableStateFlow<Int>
}
