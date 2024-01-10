package com.cashproject.mongsil.ui.dialog.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.repository.model.Poster
import com.cashproject.mongsil.ui.dialog.menu.MenuViewModel.Companion.createViewModelFactory
import com.cashproject.mongsil.ui.theme.MongsilTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import java.util.Date

class MenuBottomSheetDialog(
    private val poster: Poster,
    private val selectedDate: Date,
    private val dismissOnClick: Boolean = false,
) : BottomSheetDialogFragment() {

    private val viewModel: MenuViewModel by viewModels {
        createViewModelFactory(selectedDate, poster)
    }

    private val eventHandler: MenuEventHandler by lazy {
        MenuEventHandler(this, viewModel, dismissOnClick)
    }

    var onSave: () -> Unit = {}
    var onShare: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsState()
                MongsilTheme {
                    MenuScreen(
                        uiState = uiState.value,
                        onUiEvent = viewModel::emitEvent,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect {
                eventHandler.handleEvent(it)
            }
        }
    }
}
