package com.cashproject.mongsil.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.TextView
import com.cashproject.mongsil.R


class CheckDialog(context : Context) {
    private val dialog = Dialog(context)

    private lateinit var tvMessage : TextView
    private lateinit var btAccept : TextView
    private lateinit var btnCancel : TextView

    private lateinit var listener : AcceptBtnClickListener

    interface AcceptBtnClickListener {
        fun onClicked()
    }

    fun setAcceptBtnOnClickListener(listener: () -> Unit) {
        this.listener = object: AcceptBtnClickListener {
            override fun onClicked() {
                listener()
            }
        }
    }
    fun start(message : String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_check)
        dialog.setCancelable(true)

        tvMessage = dialog.findViewById(R.id.dialog_tv_message)
        tvMessage.text = message

        btAccept = dialog.findViewById(R.id.dialog_bt_accept)
        btAccept.setOnClickListener {
            listener.onClicked()
            dialog.dismiss()
        }

        btnCancel = dialog.findViewById(R.id.dialog_bt_cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}
