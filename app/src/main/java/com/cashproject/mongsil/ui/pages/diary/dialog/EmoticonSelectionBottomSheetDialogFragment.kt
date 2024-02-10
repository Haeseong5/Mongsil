package com.cashproject.mongsil.ui.pages.diary.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.cashproject.mongsil.R
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.theme.MongsilTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmoticonSelectionBottomSheetDialogFragment(
    private val emoticons: List<Emoticon>,
    private val onClickItem: (Emoticon) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawableResource(R.color.transparent)
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
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
                MongsilTheme {
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
}