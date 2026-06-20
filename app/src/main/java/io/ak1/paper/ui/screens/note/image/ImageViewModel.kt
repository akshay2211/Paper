package io.ak1.paper.ui.screens.note.image

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.domain.model.ImageChooserType
import io.ak1.paper.domain.repository.ImageRepository
import io.ak1.paper.domain.repository.LocalRepository
import io.ak1.paper.models.Image
import io.ak1.paper.ui.utils.getEncodedString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ImageUiState(
    val image: Image = Image("", "", "", ""),
    val openImageChooser: ImageChooserType = ImageChooserType.NONE,
    val loading: Boolean = false,
)

sealed interface ImageEvent {
    data class ChangeImageType(val type: ImageChooserType) : ImageEvent
    data class Save(val uri: Uri?, val bitmap: Bitmap?) : ImageEvent
}

class ImageViewModel(
    private val imageRepository: ImageRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageUiState(loading = true))
    val uiState: StateFlow<ImageUiState> = _uiState.asStateFlow()

    init {
        if (localRepository.currentImageId.value.isEmpty()) {
            _uiState.update {
                it.copy(image = Image(localRepository.currentNote.value, "", "", ""))
            }
            _uiState.update { it.copy(openImageChooser = localRepository.currentImageType.value) }
        }
    }

    fun onEvent(event: ImageEvent) {
        when (event) {
            is ImageEvent.ChangeImageType -> {
                viewModelScope.launch {
                    localRepository.saveCurrentImageType(event.type)
                }
                _uiState.update { it.copy(openImageChooser = event.type) }
            }
            is ImageEvent.Save -> {
                val encoded = event.bitmap?.getEncodedString()
                _uiState.update {
                    it.copy(
                        image = it.image.copy(
                            imageText = encoded.toString(),
                            uri = event.uri.toString(),
                        ),
                    )
                }
                if (encoded != null) {
                    viewModelScope.launch {
                        imageRepository.create(_uiState.value.image)
                    }
                }
            }
        }
    }
}
