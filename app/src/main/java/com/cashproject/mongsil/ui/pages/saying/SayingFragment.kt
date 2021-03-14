package com.cashproject.mongsil.ui.pages.saying

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentSayingBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.extension.saveImage
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.emoticon.EmoticonBottomSheetFragment
import com.cashproject.mongsil.viewmodel.SayingViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class SayingFragment : BaseFragment<FragmentSayingBinding, SayingViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_saying

    override val viewModel: SayingViewModel by viewModels{ viewModelFactory }

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mSaying: Saying

    private var mEmoticonId: Int = 0


    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullView()
    }

    override fun initStartView() {
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        initRecyclerView()
        initSaying()

        //button click listener
        binding.ivSayingBackgroundImage.setOnClickListener {
            showBottomListDialog()
        }

        binding.ivSayingEmoticon.setOnClickListener {
            showEmoticonBottomSheet()
        }

        binding.tvSayingCommentBtn.setOnClickListener {
            if (binding.etSayingCommentInput.text.isNullOrBlank()){
                activity?.showToast("일기를 입력해주세요.")
            }else{
                viewModel.insertComment(
                    Comment(
                        docId = mSaying.docId,
                        content = binding.etSayingCommentInput.text.toString(),
                        time = Date(),
                        date = mSaying.date,
                        emotion = mEmoticonId
                    )
                )
                binding.etSayingCommentInput.text?.clear()
            }
        }

        viewModel.commentData.observe(viewLifecycleOwner, Observer {
//            commentAdapter.update(it)
            commentAdapter.update(it as ArrayList<Comment>)
            d("Comment", it.size.toString())
        })

        //댓글 삽입/삭제 결과 관찰
        viewModel.isCompletable.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.getComments(mSaying.docId)
            }
        })
    }

    private fun initSaying(){
        //보관함 or 리스트에서 넘어왔을 경우
        if (arguments != null){
            arguments?.getString("docId")?.let {
                viewModel.getSayingData(it)
                viewModel.getComments(it)
            }
        }else{
            viewModel.getTodayData()
        }

        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            mSaying = it
            viewModel.getComments(mSaying.docId)
        })
    }

    private fun initRecyclerView() {
        binding.rvSayingCommentList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = commentAdapter
        }

        commentAdapter.setOnItemClickListener {
//            activity?.showToast(it.docId.toString())
//            findNavController().navigate(R.id.action_pager_to_home, bundleOf("image" to it.image, "docId" to it.docId))
        }
    }

    private fun showEmoticonBottomSheet() {
        val bottomSheetFragment = EmoticonBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, "approval")
        bottomSheetFragment.setEmoticonBtnClickListener {
            mEmoticonId = it.id
            binding.ivSayingEmoticon.setImageResource(it.icon)
            bottomSheetFragment.dismiss()
        }

    }

    private fun showBottomListDialog() {
        val bottomSheetFragment = SayingBottomSheetFragment(mSaying.date)
        bottomSheetFragment.show(childFragmentManager, "approval")

        bottomSheetFragment.setLikeBtnOnClickListener {
            addDisposable(
                viewModel.like(mSaying).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        bottomSheetFragment.dismiss()
                        Log.d(TAG, "success insertion")
                    },
                        { error -> Log.e(TAG, "Unable to update username", error) })
            )
        }

        bottomSheetFragment.setSaveBtnOnClickListener {
            //bitmap
            val bitmap = binding.ivSayingBackgroundImage.drawable as BitmapDrawable
            val imageUri = bitmap.bitmap.saveImage(requireActivity())
            if (imageUri != null){
                d("imageUri", imageUri.toString())
                activity?.showToast("갤러리에 이미지가 저장되었습니다.")
            }else{
                activity?.showToast("저장 실패")
            }
            bottomSheetFragment.dismiss()
        }
    }

}