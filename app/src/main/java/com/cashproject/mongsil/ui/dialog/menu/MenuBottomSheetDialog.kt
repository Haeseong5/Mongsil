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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import java.util.Date

class MenuBottomSheetDialog(
    private val poster: Poster,
    private val selectedDate: Date,
) : BottomSheetDialogFragment() {

    private val viewModel: MenuViewModel by viewModels {
        createViewModelFactory(selectedDate, poster)
    }

    private val eventHandler: MenuEventHandler by lazy {
        MenuEventHandler(this, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState = viewModel.uiState.collectAsState()
                MenuScreen(
                    uiState = uiState.value,
                    onUiEvent = viewModel::emitEvent,
                )
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

    fun shareToSNS() {
//        val bitmap = binding.ivSayingBackgroundImage.drawable as BitmapDrawable
//        val imageUri = getImageUri(requireActivity(), bitmap.bitmap)
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
//        val chooser = Intent.createChooser(intent, "친구에게 공유하기")
//        startActivity(chooser)
    }
}
