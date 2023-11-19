package com.cashproject.mongsil.ui.composables.screen.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

//@HiltViewModel
class CalendarViewModel : ViewModel() {

    val progress = MutableStateFlow<Boolean>(false)
    val finish = MutableSharedFlow<Unit>()
    val error = MutableSharedFlow<Throwable>()

//    fun searchRecord(records: List<WorkoutRecord>): List<LocalDate> {
//        val temp = mutableListOf<LocalDate>()
//        records.forEach {
//            temp.add(timeMillisToLocalDate(it.date.time))
//        }
//        return temp
//    }

    fun timeMillisToLocalDate(timeMillis: Long): LocalDate {
        val instant = Instant.ofEpochMilli(timeMillis)
        return instant.atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun CoroutineScope.launchWithCatching(
        useProgress: Boolean = true,
        action: suspend () -> Unit = {}
    ) {
        launch {
            try {
                if (useProgress) progress.emit(true)
                action.invoke()
            } catch (e: Exception) {
                error.emit(e)
            } finally {
                if (useProgress) progress.emit(false)
            }
        }
    }
}