package com.cashproject.mongsil.ui.pages.diary.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.cashproject.mongsil.base.BaseBottomSheetDialogFragment
import com.cashproject.mongsil.ui.model.Emoticon
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class EmoticonSelectionBottomSheetDialogFragment(
    private val emoticons: List<Emoticon>,
    private val onClickItem: (Emoticon) -> Unit
) : BaseBottomSheetDialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.let {
            val bottomSheet = it as BottomSheetDialog
            bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet.behavior.skipCollapsed = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                EmoticonSelectionBottomSheetContent(
                    emoticons = emoticons,
                    onClick = {
                        onClickItem.invoke(it)
                        dismiss()
                    },
                )
            }
        }
    }
}