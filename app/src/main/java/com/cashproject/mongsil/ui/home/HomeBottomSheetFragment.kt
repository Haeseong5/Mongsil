package com.cashproject.mongsil.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetTimePickerBinding
import com.cashproject.mongsil.databinding.FragmentHomeBottomSheetBinding
import com.cashproject.mongsil.util.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeBottomSheetFragment: BottomSheetDialogFragment(){
    lateinit var binding: FragmentHomeBottomSheetBinding

    private var likeBtnListener: (() -> Unit)? = null

    fun setLikeBtnOnClickListener(listener: () -> Unit) {
        this.likeBtnListener = listener
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHomeBottomSheetBinding>(
                inflater,
                R.layout.fragment_home_bottom_sheet,
                container,
                false)

        initTimePicker()
        setOnClickListener()

        return binding.root
    }

    private fun initTimePicker(){
        //db 에 저장된 알람 시간이 있으면, 해당 시간으로 time picker 표시

    }

    private fun setOnClickListener(){
        binding.homeBottomIvLock.setOnClickListener {
            likeBtnListener?.invoke()
        }
    }

}