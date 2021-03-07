package com.cashproject.mongsil.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentHomeBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.model.data.LikeSaying
import com.cashproject.mongsil.model.data.Saying
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

    override fun initStartView() {
        setFullView()
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        binding.homeIvBackground.setOnClickListener {
            showBottomListDialog()
        }

        arguments?.let {
//            binding.saying = Saying("1", it, Timestamp.now())
            mSaying = Saying(
                docId = it.getString("docId"),
                image = it.getString("image"))
            binding.saying = mSaying
        }

        viewModel.getTodayData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.todayData.observe(viewLifecycleOwner, Observer {
            binding.saying = it
            mSaying = it
            d("today", it.toString())
        })
    }

    private fun showBottomListDialog() {
        val bottomSheetFragment = HomeBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, "approval")
        bottomSheetFragment.setLikeBtnOnClickListener {
            val saying = LikeSaying(docId = mSaying.docId!!, image = mSaying.image!!)
            addDisposable(viewModel.insert(saying)                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
//                    update_user_button.isEnabled = true
                    Log.d(TAG, "success insertion")
                },
                    { error -> Log.e(TAG, "Unable to update username", error) }))
        }
    }

}