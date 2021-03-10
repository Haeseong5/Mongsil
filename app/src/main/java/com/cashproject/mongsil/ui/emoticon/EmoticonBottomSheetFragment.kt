package com.cashproject.mongsil.ui.emoticon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetEmoticonBinding
import com.cashproject.mongsil.model.data.Emoticon

class EmoticonBottomSheetFragment : DialogFragment() {
    lateinit var binding: FragmentBottomSheetEmoticonBinding

    private var btnListener: (() -> Unit)? = null

    fun setEmoticonBtnClickListener(listener: () -> Unit) {
        this.btnListener = listener
    }

    lateinit var emoticonAdapter: EmoticonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBottomSheetEmoticonBinding>(
            inflater,
            R.layout.dialog_emoticon,
            container,
            false
        )

        val emoticons = ArrayList<Emoticon>()

        emoticons.add(Emoticon(
            0,
            "행복",
            R.drawable.emoticon_01_happy,
            R.color.emoticon_happy_text,
            R.color.emoticon_happy_background
        ))

        emoticons.add(Emoticon(
            1,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        ))

        emoticons.add(Emoticon(
            2,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        ))

        emoticons.add(Emoticon(
            3,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        ))

        emoticons.add(Emoticon(
            4,
            "기쁨",
            R.drawable.emoticon_02_enjoy,
            R.color.emoticon_enjoy_text,
            R.color.emoticon_enjoy_background
        ))


        emoticons.add(Emoticon(
            5,
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