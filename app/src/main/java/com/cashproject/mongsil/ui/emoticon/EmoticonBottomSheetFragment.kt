package com.cashproject.mongsil.ui.emoticon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetEmoticonBinding
import com.cashproject.mongsil.databinding.FragmentBottomSheetTimePickerBinding
import com.cashproject.mongsil.databinding.FragmentHomeBottomSheetBinding
import com.cashproject.mongsil.model.data.Emoticon
import com.cashproject.mongsil.util.PreferencesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmoticonBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheetEmoticonBinding

    private var likeBtnListener: (() -> Unit)? = null

    fun setLikeBtnOnClickListener(listener: () -> Unit) {
        this.likeBtnListener = listener
    }

    lateinit var emoticonAdapter: EmoticonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBottomSheetEmoticonBinding>(
            inflater,
            R.layout.fragment_bottom_sheet_emoticon,
            container,
            false
        )

        val emoticons = ArrayList<Emoticon>()

        emoticons.add(Emoticon(
            "행복",
            R.drawable.emoticon_01_happy,
            R.color.emoticon_happy_text,
            R.color.emoticon_happy_background
        ))

        emoticons.add(Emoticon(
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        ))

        emoticonAdapter = EmoticonAdapter(
            emoticons
        )

        binding.emoticonRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = emoticonAdapter
        }

        setOnClickListener()

        return binding.root
    }


    private fun setOnClickListener() {
//        binding.homeBottomIvLock.setOnClickListener {
//            likeBtnListener?.invoke()
//        }
    }

}