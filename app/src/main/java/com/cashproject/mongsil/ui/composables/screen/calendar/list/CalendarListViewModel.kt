package com.cashproject.mongsil.ui.composables.screen.calendar.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.data.api.PosterApi
import com.cashproject.mongsil.data.repository.mapper.toPosters
import com.cashproject.mongsil.data.service.PosterService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Calendar.MONTH


class CalendarListViewModel(
    private val posterApi: PosterApi = PosterService,
) : ViewModel() {

    val posters: MutableStateFlow<List<SquarePosterUiModel>> = MutableStateFlow(emptyList())

    init {
        getSayingList()
    }

    private fun getSayingList() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance().apply {
                    this.add(MONTH, 1)
                }

                val posterList = posterApi.getAllPosters().toPosters()
                posters.emit(
                    posterList.toUiModel(calendar)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}