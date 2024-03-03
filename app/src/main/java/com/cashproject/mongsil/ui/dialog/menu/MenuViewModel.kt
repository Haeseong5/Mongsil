package com.cashproject.mongsil.ui.dialog.menu


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.common.extensions.DateFormat
import com.cashproject.mongsil.common.extensions.toTextFormat
import com.cashproject.mongsil.repository.repository.BookmarkRepository
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.toDomain
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class MenuViewModel(
    private val bookmarkRepository: BookmarkRepository = BookmarkRepository(),
    date: Date,
    private val poster: Poster
) : ViewModel() {
    companion object {
        fun createViewModelFactory(date: Date, poster: Poster): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
                        return MenuViewModel(
                            date = date,
                            poster = poster,
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(
        MenuUiState(
            year = date.toTextFormat(DateFormat.YEAR),
            monthDay = date.toTextFormat(DateFormat.PlainMonthDay)
        )
    )
    val uiState = _uiState.asStateFlow()

    val event = MutableSharedFlow<MenuUiEvent>()

    init {
        viewModelScope.launch {
            try {
                bookmarkRepository.loadBookmarkedPosters().collect {
                    val ids = it.map { poster -> poster.id }

                    _uiState.update {
                        uiState.value.copy(
                            scrapped = ids.contains(poster.id)
                        )
                    }
                }
            } catch (e: Exception) {
                emitEvent(MenuUiEvent.Error(e))
            }
        }
    }

    fun emitEvent(e: MenuUiEvent) {
        viewModelScope.launch {
            event.emit(e)
        }
    }

    fun scrap(
        poster: Poster = this.poster,
        scrapped: Boolean = uiState.value.scrapped
    ) {
        viewModelScope.launch {
            try {
                if (scrapped) {
                    bookmarkRepository.unlike(poster.id)
                } else {
                    bookmarkRepository.bookmark(poster.toDomain())
                }
            } catch (e: Exception) {
                emitEvent(MenuUiEvent.Error(e))
            }
        }
    }
}
