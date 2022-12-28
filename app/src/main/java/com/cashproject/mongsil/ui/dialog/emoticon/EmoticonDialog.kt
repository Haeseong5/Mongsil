package com.cashproject.mongsil.ui.dialog.emoticon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.DialogEmoticonBinding
import com.cashproject.mongsil.domain.model.Emoticon

class EmoticonDialog : DialogFragment() {
    lateinit var binding: DialogEmoticonBinding

    private var btnListener: ((Emoticon) -> Unit)? = null

    fun setEmoticonBtnClickListener(listener: (Emoticon) -> Unit) {
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
        binding = DataBindingUtil.inflate<DialogEmoticonBinding>(
            inflater,
            R.layout.dialog_emoticon,
            container,
            false
        )


        emoticonAdapter = EmoticonAdapter()

        binding.rvEmoticonList.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = emoticonAdapter
        }
        emoticonAdapter.setOnItemClickListener {
            btnListener?.invoke(it) //익명함수 호출
        }
        binding.btnEmoticonCloseButton.setOnClickListener {
            dismiss()
        }
//        binding.btnEmoticonSelectButton.setOnClickListener {
//            dismiss()
//        }

        return binding.root
    }
}