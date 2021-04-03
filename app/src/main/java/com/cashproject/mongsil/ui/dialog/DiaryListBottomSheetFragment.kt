package com.cashproject.mongsil.ui.dialog

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetTimePickerBinding
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.util.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DiaryListBottomSheetFragment: BottomSheetDialogFragment(){
    lateinit var binding: FragmentBottomSheetTimePickerBinding

    private var listener: ((hour:Int, minute:Int) -> Unit)? = null
    private var negativeListener: (() -> Unit)? = null

    fun setCheckBtnOnClickListener(listener: (hour:Int, minute:Int) -> Unit) {
        this.listener = listener
    }

    fun setReleaseBtnClickListener(listener: () -> Unit) {
        this.negativeListener = listener
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentBottomSheetTimePickerBinding>(
                inflater,
                R.layout.fragment_bottom_sheet_time_picker,
                container,
                false)

        setOnClickListener()
        initTimePicker()


        return binding.root
    }

    private fun initTimePicker(){
        //db 에 저장된 알람 시간이 있으면, 해당 시간으로 time picker 표시
        binding.timePicker.apply {
            if (PreferencesManager.hour != -1 && PreferencesManager.minute != -1){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hour = PreferencesManager.hour
                    minute = PreferencesManager.minute
                    Log.d("TimePicker", minute.toString())
                }else {
                    requireActivity().showToast("지원하지 않는 기능입니다.")
                }
            }
        }
    }

    private fun setOnClickListener(){
        binding.tvTimePickerCheck.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                listener?.invoke(binding.timePicker.hour, binding.timePicker.minute)
            }else {
                requireActivity().showToast("지원하지 않는 기능입니다.")
            }
        }
        binding.tvTimePickerCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}