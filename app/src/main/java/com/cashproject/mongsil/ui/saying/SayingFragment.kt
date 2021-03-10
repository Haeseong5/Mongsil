package com.cashproject.mongsil.ui.saying

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.Comment
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.emoticon.EmoticonBottomSheetFragment
import com.cashproject.mongsil.ui.main.CommentAdapter
import com.cashproject.mongsil.viewmodel.LockerViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SayingFragment : BaseFragment<FragmentHomeBinding, LockerViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_saying

    override val viewModel: LockerViewModel by viewModels{ viewModelFactory }

    private lateinit var viewModelFactory: ViewModelFactory

    lateinit var mSaying: Saying

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    override fun initStartView() {
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        initRecyclerView()
        initSaying()


        //button click listener
        binding.homeIvBackground.setOnClickListener {
            showBottomListDialog()
        }

        binding.homeIvEmoticon.setOnClickListener {
            showEmoticonBottomSheet()
        }

        binding.homeTvCommentBtn.setOnClickListener {
            if (binding.homeEtComment.text.isNullOrBlank()){
                activity?.showToast("일기를 입력해주세요.")
            }else{
                viewModel.insertComment(
                    Comment(
                        docId = mSaying.docId!!,
                        content = binding.homeEtComment.text.toString(),
                        emotion = 1
                    )

                )
                binding.homeEtComment.text?.clear()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.commentData.observe(viewLifecycleOwner, Observer {
//            commentAdapter.update(it)
            commentAdapter.setItems(it as ArrayList<Comment>)
            d("Comment", it.size.toString())
        })

        //댓글 삽입/삭제 결과 관찰
        viewModel.isCompletable.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.getComments(mSaying.docId!!)
            }
        })
    }

    private fun initSaying(){
        //보관함 or 리스트에서 넘어왔을 경우
        arguments?.let {
            mSaying = Saying(
                docId = it.getString("docId"),
                image = it.getString("image")
            )
            binding.saying = mSaying
            viewModel.getComments(mSaying.docId!!)
        }

        viewModel.getTodayData()
        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            mSaying = it
            viewModel.getComments(mSaying.docId!!)

        })
    }

    private fun initRecyclerView() {
        binding.homeRvComment.apply {
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

        }
    }

    private fun showBottomListDialog() {
        val bottomSheetFragment = SayingBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, "approval")
        bottomSheetFragment.setLikeBtnOnClickListener {
            val saying = LikeSaying(docId = mSaying.docId!!, image = mSaying.image!!)
            addDisposable(
                viewModel.insert(saying).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
//                    update_user_button.isEnabled = true
                        Log.d(TAG, "success insertion")
                    },
                        { error -> Log.e(TAG, "Unable to update username", error) })
            )
        }
    }

}