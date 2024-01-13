package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.extension.toDate
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.calendar.compose.CalendarScreen
import com.cashproject.mongsil.ui.pages.diary.DiaryFragment
import java.time.LocalDate

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    private val calendarViewModel: CalendarViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = calendarViewModel.uiState.collectAsState()
                val currentVisibleCalendarScreenType by mainViewModel.visibleCalendarScreenType.collectAsState()

                CalendarScreen(
                    uiState = uiState.value,
                    onStartDiary = ::goToDiaryScreen,
                    visibleCalendarScreenType = currentVisibleCalendarScreenType,
                    selectedLastPosterIndex = mainViewModel.selectedLastPosterIndex,
                    setSelectedLastPosterIndex = {
                        mainViewModel.selectedLastPosterIndex = it
                    },
                    onClickFloating = {
                        mainViewModel.toggleCalendarScreenType(it)
                    }
                )
            }
        }
    }

    private fun goToDiaryScreen(localDate: LocalDate, poster: Poster?) {
        val date = localDate.toDate()
        DiaryFragment.start(
            this@CalendarFragment,
            argument = DiaryFragment.Argument(
                poster = poster ?: calendarViewModel.getRandomSaying(date),
                selectedDate = date,
                from = "calendar"
            )
        )
    }
}