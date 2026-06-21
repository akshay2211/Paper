package io.ak1.paper.ui.screens.note.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ak1.paper.domain.model.ImageChooserType
import io.ak1.paper.domain.repository.LocalRepository
import kotlinx.coroutines.launch

sealed interface OptionsEvent {
    data class SetDoodleId(val id: String = "") : OptionsEvent
    data class SetImageId(val id: String = "") : OptionsEvent
    data class SetImageType(val type: ImageChooserType = ImageChooserType.NONE) : OptionsEvent
}

class OptionsViewModel(private val localRepository: LocalRepository) : ViewModel() {

    fun onEvent(event: OptionsEvent) {
        when (event) {
            is OptionsEvent.SetDoodleId -> viewModelScope.launch {
                localRepository.saveCurrentDoodleId(event.id)
            }
            is OptionsEvent.SetImageId -> viewModelScope.launch {
                localRepository.saveCurrentImageId(event.id)
            }
            is OptionsEvent.SetImageType -> viewModelScope.launch {
                localRepository.saveCurrentImageType(event.type)
            }
        }
    }
}
