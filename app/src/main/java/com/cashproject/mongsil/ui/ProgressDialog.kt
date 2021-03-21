package com.cashproject.mongsil.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialog
import com.cashproject.mongsil.R
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import kotlinx.android.synthetic.main.dialog_progress.view.*


class ProgressDialog(context: Context): AppCompatDialog(context) {

    private var view: View
    private var linear: LinearLayout
    private var builder: AlertDialog.Builder
    lateinit var dialog: Dialog
    lateinit var drawable: AnimationDrawable

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.dialog_progress, null)
        linear = view.findViewById(R.id.linear)
        builder = AlertDialog.Builder(context)
        drawable = view.iv_loading.background as AnimationDrawable
    }

    override fun show() {
        if (view.parent != null) (view.parent as ViewGroup).removeView(view)
        if (!isShowing){
            builder.setView(view)
            drawable.start()
            dialog = builder.create().apply {
                window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                setCancelable(true)
                setCanceledOnTouchOutside(false)
                show()
            }
        }
    }

    override fun dismiss() {
        drawable.stop()
        dialog.dismiss()
    }
}