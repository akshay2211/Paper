package io.ak1.paper.ui.screens.note.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.domain.repository.DoodlesRepository
import io.ak1.paper.domain.repository.ImageRepository
import io.ak1.paper.domain.repository.LocalRepository
import io.ak1.paper.domain.repository.NotesRepository
import io.ak1.paper.models.ClickableUri
import io.ak1.paper.ui.utils.getUriList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PreviewUiState(
    val list: List<ClickableUri> = emptyList(),
    val loading: Boolean = false,
    var selection: Int = 0,
)

sealed interface PreviewEvent {
    data class SetDoodleId(val id: String) : PreviewEvent
    data class SetImageId(val id: String) : PreviewEvent
    data class DeleteMedia(val isDoodle: Boolean, val mediaId: String) : PreviewEvent
}

class PreviewViewModel(
    private val localRepository: LocalRepository,
    private val imageRepository: ImageRepository,
    private val doodlesRepository: DoodlesRepository,
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreviewUiState(loading = true))
    val uiState: StateFlow<PreviewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            localRepository.currentNote.collect { id ->
                notesRepository.getNoteByFlow(id).collect { note ->
                    note?.let {
                        _uiState.update {
                            it.copy(
                                list = note.getUriList(),
                                selection = localRepository.currentSelectedPosition.value,
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: PreviewEvent) {
        when (event) {
            is PreviewEvent.SetDoodleId -> viewModelScope.launch {
                localRepository.saveCurrentDoodleId(event.id)
            }
            is PreviewEvent.SetImageId -> viewModelScope.launch {
                localRepository.saveCurrentImageId(event.id)
            }
            is PreviewEvent.DeleteMedia -> viewModelScope.launch {
                if (event.isDoodle) doodlesRepository.deleteDoodleById(event.mediaId)
                else imageRepository.deleteImageById(event.mediaId)
            }
        }
    }
}
