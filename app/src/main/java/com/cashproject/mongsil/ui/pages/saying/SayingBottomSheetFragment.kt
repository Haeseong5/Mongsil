package com.cashproject.mongsil.ui.pages.saying

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetSayingBinding
import com.cashproject.mongsil.util.DateUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class SayingBottomSheetFragment(private val date: Date): BottomSheetDialogFragment(){
    lateinit var binding: FragmentBottomSheetSayingBinding

    private var likeBtnListener: (() -> Unit)? = null

    fun setLikeBtnOnClickListener(listener: () -> Unit) {
        this.likeBtnListener = listener
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBottomSheetSayingBinding>(
                inflater,
                R.layout.fragment_bottom_sheet_saying,
                container,
                false)

        initDate()
        setOnClickListener()

        return binding.root
    }


    private fun initDate() {
        binding.tvSayingDate.text = DateUtil.dateToString(date)
        binding.tvSayingYear.text = DateUtil.yearToString(date)
    }

    private fun setOnClickListener(){
        binding.ivSayingLock.setOnClickListener {
            likeBtnListener?.invoke()
        }
    }

}