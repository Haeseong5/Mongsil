package com.cashproject.mongsil.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialog
import com.cashproject.mongsil.R
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import kotlinx.android.synthetic.main.dialog_progress.view.*


class ProgressDialog(context: Context): Dialog(context) {

    private var view: View
    private var linear: LinearLayout
    private val builder: AlertDialog.Builder by lazy {
        AlertDialog.Builder(context)
    }

    var drawable: AnimationDrawable

    private val handler = Handler(Looper.getMainLooper())

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.dialog_progress, null)
        linear = view.findViewById(R.id.linear)
        drawable = view.iv_loading.background as AnimationDrawable
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun show() {
        if (view.parent != null) (view.parent as ViewGroup).removeView(view)
        executeOnMainLooper {
            if (!isShowing){
                builder.setView(view)
                drawable.start()
                super.show()
            }
        }
    }

    override fun dismiss() {
        executeOnMainLooper {
            drawable.stop()
            super.dismiss()
        }
    }

    fun isProgress(flag: Boolean) {
        if (flag)
            show()
        else
            dismiss()
    }

    private fun executeOnMainLooper(callback: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper())
            callback.invoke()
        else handler.post { callback.invoke() }
    }
}