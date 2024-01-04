package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.extension.toDate
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.pages.detail.DiaryFragment
import com.cashproject.mongsil.ui.screen.calendar.CalendarScreen
import com.cashproject.mongsil.ui.screen.calendar.CalendarViewModel
import java.time.LocalDate

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    private val viewModel: CalendarViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsState()
                CalendarScreen(
                    uiState = uiState.value,
                    onStartDiary = ::goToDiaryScreen,
                    onClickFloating = {
                        viewModel.changeScreenType()
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
                poster = poster ?: viewModel.getRandomSaying(date),
                selectedDate = date,
                from = "calendar"
            )
        )
    }
}