package com.cashproject.mongsil.ui.pages.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.network.PosterDataSource
import com.cashproject.mongsil.network.retrofit.PosterApi
import com.cashproject.mongsil.repository.mapper.toPosters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar


class CalendarListViewModel(
    private val posterApi: PosterApi = PosterDataSource,
) : ViewModel() {

    val posters: MutableStateFlow<List<SquarePosterUiModel>> = MutableStateFlow(emptyList())

    init {
        getSayingList()
    }

    private fun getSayingList() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()

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