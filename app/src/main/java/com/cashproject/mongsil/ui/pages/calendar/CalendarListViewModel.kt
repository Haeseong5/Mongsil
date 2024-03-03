package com.cashproject.mongsil.ui.pages.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.repository.repository.PosterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar


class CalendarListViewModel(
    private val posterRepository: PosterRepository = PosterRepository()
) : ViewModel() {

    val posters: MutableStateFlow<List<SquarePosterUiModel>> = MutableStateFlow(emptyList())

    init {
        getSayingList()
    }

    private fun getSayingList() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val posterList = posterRepository.getAllPosters()
                posters.emit(
                    posterList.toUiModel(calendar)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}