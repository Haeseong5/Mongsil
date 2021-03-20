package com.cashproject.mongsil.ui.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetTimePickerBinding
import com.cashproject.mongsil.util.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DiaryListBottomSheetFragment: BottomSheetDialogFragment(){
    lateinit var binding: FragmentBottomSheetTimePickerBinding

    private var listener: ((hour:Int, minute:Int) -> Unit)? = null

    fun setCheckBtnOnClickListener(listener: (hour:Int, minute:Int) -> Unit) {
        this.listener = listener
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBottomSheetTimePickerBinding>(
                inflater,
                R.layout.fragment_bottom_sheet_time_picker,
                container,
                false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setOnClickListener()
            initTimePicker()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initTimePicker(){
        //db 에 저장된 알람 시간이 있으면, 해당 시간으로 time picker 표시
        binding.timePicker.apply {
            if (PreferencesManager.hour != -1 && PreferencesManager.minute != -1){
                hour = PreferencesManager.hour
                minute = PreferencesManager.minute
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setOnClickListener(){
        binding.tvTimePickerCheck.setOnClickListener {
            listener?.invoke(binding.timePicker.hour, binding.timePicker.minute)
        }
        binding.tvTimePickerCancel.setOnClickListener {
            dismiss()
        }
    }

}