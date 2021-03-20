package com.cashproject.mongsil.ui.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentBottomSheetSayingBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.util.DateUtil
import com.cashproject.mongsil.util.PreferencesManager.isVisibilityComment
import com.cashproject.mongsil.viewmodel.SayingViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment(private val saying: Saying) : BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheetSayingBinding

    private val viewModel: SayingViewModel by viewModels { viewModelFactory }
    lateinit var viewModelFactory: ViewModelFactory

    private var likeBtnListener: (() -> Unit)? = null
    private var saveBtnListener: (() -> Unit)? = null
    private var hideCommentBtnListener: (() -> Unit)? = null
    private var shareBtnListener: (() -> Unit)? = null

    var mLike: Boolean = false

    fun setLikeBtnOnClickListener(listener: () -> Unit) {
        this.likeBtnListener = listener
    }

    fun setSaveBtnOnClickListener(listener: () -> Unit) {
        this.saveBtnListener = listener
    }

    fun setHideCommentBtnOnClickListener(listener: () -> Unit) {
        this.hideCommentBtnListener = listener
    }

    fun setShareBtnOnClickListener(listener: () -> Unit) {
        this.shareBtnListener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentBottomSheetSayingBinding>(
            inflater,
            R.layout.fragment_bottom_sheet_saying,
            container,
            false
        )
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)
        initDateIcon()
        setCommentIcon()
        setOnClickListener()
        viewModel.findByDocId(saying.docId)
        observeIsLike()
        return binding.root
    }


    private fun initDateIcon() {
        binding.tvSayingDate.text = DateUtil.dateToString(saying.date)
        binding.tvSayingYear.text = DateUtil.yearToString(saying.date)
    }

    fun setCommentIcon(){
        if (!isVisibilityComment) { // false 라면, 댓글 보이는 상태이고, 댓글을 숨기길 수 있는 아이콘 보이기
            binding.ivSayingHideComment.setImageResource(R.drawable.ic_view_off)
            binding.tvSayingIsHideComment.text = "댓글 숨기기"
            isVisibilityComment = true
        } else {
            binding.ivSayingHideComment.setImageResource(R.drawable.ic_view_on)
            binding.tvSayingIsHideComment.text = "댓글 보이기"
            isVisibilityComment = false
        }
    }

    private fun setOnClickListener() {
        binding.ivSayingLike.setOnClickListener {
            if (mLike) viewModel.unLike(saying.docId)
            else viewModel.like(saying)
            dismiss()
        }

        binding.ivSayingSave.setOnClickListener {
            saveBtnListener?.invoke()
            dismiss()
        }
        binding.ivSayingHideComment.setOnClickListener {
            setCommentIcon()
            hideCommentBtnListener?.invoke()
            dismiss()

        }
        binding.ivSayingShare.setOnClickListener {
            shareBtnListener?.invoke()
            dismiss()

        }
    }

    private fun observeIsLike(){
        viewModel.isLike.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mLike = it
            d("isLike", it.toString())
            if (mLike) binding.ivSayingLike.setImageResource(R.drawable.ic_like_sel)
            else binding.ivSayingLike.setImageResource(R.drawable.ic_like)
        })
    }

}