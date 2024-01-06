package com.cashproject.mongsil.ui.pages.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.pages.diary.DiaryFragment
import com.cashproject.mongsil.ui.theme.MongsilTheme
import java.util.Date


class LockerFragment : Fragment() {

    private val viewModel: LockerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsState()
                MongsilTheme {
                    LockerScreen(
                        uiState = uiState.value,
                        onSetting = {
                            goToSettingScreen()
                        },
                        onPoster = {
                            goToDiaryScreen(it)
                        }
                    )
                }
            }
        }
    }

    private fun goToSettingScreen() {
        findNavController().navigate(R.id.action_to_setting)
    }

    private fun goToDiaryScreen(poster: Poster) {
        DiaryFragment.start(
            fragment = this,
            argument = DiaryFragment.Argument(
                poster = poster,
                selectedDate = Date(),
                from = "locker",
            ),
        )
    }
}