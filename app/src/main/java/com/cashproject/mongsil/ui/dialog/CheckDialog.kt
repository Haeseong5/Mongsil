package com.cashproject.mongsil.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.TextView
import com.cashproject.mongsil.R


class CheckDialog(
    context : Context,
    val accept: () -> Unit,
    val acceptText: String? = null
) {
    private val dialog = Dialog(context)

    private lateinit var tvMessage : TextView
    private lateinit var btAccept : TextView
    private lateinit var btnCancel : TextView

    fun start(message : String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_check)
        dialog.setCancelable(true)

        tvMessage = dialog.findViewById(R.id.dialog_tv_message)
        tvMessage.text = message

        btAccept = dialog.findViewById(R.id.dialog_bt_accept)
        btAccept.text = acceptText ?: "확인"
        btAccept.setOnClickListener {
            accept.invoke()
            dialog.dismiss()
        }

        btnCancel = dialog.findViewById(R.id.dialog_bt_cancel)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}
