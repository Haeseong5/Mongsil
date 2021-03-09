package com.cashproject.mongsil.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.emoticon.EmoticonBottomSheetFragment
import com.cashproject.mongsil.ui.locker.LockerAdapter
import com.cashproject.mongsil.ui.main.CommentAdapter
import com.cashproject.mongsil.viewmodel.LockerViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class HomeFragment : BaseFragment<FragmentHomeBinding, LockerViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_home

    override val viewModel: LockerViewModel by viewModels{ viewModelFactory }

    private lateinit var viewModelFactory: ViewModelFactory

    lateinit var mSaying: Saying

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    override fun initStartView() {
        setFullView()
        initRecyclerView()
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        binding.homeIvBackground.setOnClickListener {
            showBottomListDialog()
        }

        binding.homeIvEmoticon.setOnClickListener {
            showEmoticonBottomSheet()
        }

        binding.homeTvCheck.setOnClickListener {

        }

        arguments?.let {
//            binding.saying = Saying("1", it, Timestamp.now())
            mSaying = Saying(
                docId = it.getString("docId"),
                image = it.getString("image")
            )
            binding.saying = mSaying
        }

        viewModel.getTodayData()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            mSaying = it
            d("today", it.toString())
        })
    }

    private fun showEmoticonBottomSheet() {
        val bottomSheetFragment = EmoticonBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, "approval")
        bottomSheetFragment.setEmoticonBtnClickListener {

        }
    }

    private fun showBottomListDialog() {
        val bottomSheetFragment = HomeBottomSheetFragment()
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