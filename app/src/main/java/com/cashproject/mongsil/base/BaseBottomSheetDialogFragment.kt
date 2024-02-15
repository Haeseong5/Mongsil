package com.cashproject.mongsil.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val tag = this.javaClass.simpleName

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(tag, "onCreateDialog")

        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }
    }
}