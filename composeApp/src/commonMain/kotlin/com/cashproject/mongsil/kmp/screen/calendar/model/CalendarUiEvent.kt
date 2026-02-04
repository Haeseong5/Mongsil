package com.cashproject.mongsil.kmp.screen.calendar.model

sealed interface CalendarUiEvent {
    data class ShowAndHideYearMonthPicker(val isShow: Boolean) : CalendarUiEvent
    
    data class OnYearMonthPickerSelected(val year: Int, val month: Int) : CalendarUiEvent
}