package com.cashproject.mongsil.ui.main

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.base.BaseViewModel
import com.cashproject.mongsil.data.firebase.fcm.PushManager
import com.cashproject.mongsil.repository.repository.PosterRepository
import com.cashproject.mongsil.ui.pages.calendar.CalendarScreenType
import com.cashproject.mongsil.ui.pages.calendar.defaultCalendarScreenType
import com.cashproject.mongsil.ui.pages.diary.model.Poster
import com.cashproject.mongsil.ui.pages.diary.model.toDomain
import com.cashproject.mongsil.ui.pages.diary.model.toPoster
import com.cashproject.mongsil.util.PreferencesManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MainViewModel(
    private val pushManager: PushManager = PushManager(),
    private val posterRepository: PosterRepository = PosterRepository()
) : BaseViewModel() {

    private val _allPosters: MutableStateFlow<List<Poster>> = MutableStateFlow(emptyList())
    val allPosters = _allPosters.asStateFlow()

    val error = MutableSharedFlow<Throwable>()

    val currentPage = MutableStateFlow(1)

    private val _visibleCalendarScreenType: MutableStateFlow<CalendarScreenType> =
        MutableStateFlow(defaultCalendarScreenType)
    val visibleCalendarScreenType: StateFlow<CalendarScreenType> = _visibleCalendarScreenType

    val showPagerTutorialAnim: MutableSharedFlow<Unit> = MutableSharedFlow()

    init {
        initPushNotificationSettings()
        loadAllPosters()
    }

    fun emitPagerTutorialAnimEvent() {
        viewModelScope.launch {
            showPagerTutorialAnim.emit(Unit)
        }
    }

    private fun loadAllPosters() {
        viewModelScope.launch {
            try {
                _allPosters.emit(posterRepository.getAllPosters().toPoster())
            } catch (e: Exception) {
                error.emit(e)
            }
        }
    }

    fun getRandomSaying(date: Date): Poster {
        return posterRepository.getRandomSaying(
            date = date,
            posters = allPosters.value.toDomain()
        ).toPoster()
    }

    private fun initPushNotificationSettings() {
        pushManager.emitPushEvent(PreferencesManager.isEnabledPushNotification)
    }

    fun toggleCalendarScreenType(calendarScreenType: CalendarScreenType) {
        viewModelScope.launch {
            when (calendarScreenType) {
                CalendarScreenType.DEFAULT -> {
                    _visibleCalendarScreenType.emit(CalendarScreenType.LIST)
                }

                CalendarScreenType.LIST -> {
                    _visibleCalendarScreenType.emit(CalendarScreenType.DEFAULT)
                }
            }
        }
    }
}