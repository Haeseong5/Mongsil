package com.cashproject.mongsil.ui.pages.locker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.repository.repository.BookmarkRepository
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.toPoster
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LockerUiState(
    val posters: List<Poster> = emptyList()
)

sealed interface LockerEvent {
    data class Error(val throwable: Throwable) : LockerEvent
}

class LockerViewModel(
    private val bookmarkRepository: BookmarkRepository = BookmarkRepository()
) : ViewModel() {

    private val _uiState: MutableStateFlow<LockerUiState> = MutableStateFlow(
        LockerUiState()
    )
    val uiState: StateFlow<LockerUiState> = _uiState.asStateFlow()

    val event = MutableSharedFlow<LockerEvent>()

    init {
        viewModelScope.launch {
            try {
                bookmarkRepository.loadBookmarkedPosters().collect { posters ->
                    _uiState.update {
                        uiState.value.copy(
                            posters = posters.toPoster()
                        )
                    }
                }
            } catch (e: Exception) {
                emitEvent(LockerEvent.Error(e))
            }
        }
    }


    private fun emitEvent(e: LockerEvent) {
        viewModelScope.launch {
            event.emit(e)
        }
    }

}