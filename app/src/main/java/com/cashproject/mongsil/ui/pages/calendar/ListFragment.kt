package com.cashproject.mongsil.ui.pages.calendar

import android.content.Context
import android.util.Log
import android.util.Log.d
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentListBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.SayingAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.SayingCase
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import kotlin.collections.ArrayList

class ListFragment : BaseFragment<FragmentListBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_list

    override val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private val dayAdapter by lazy {
        SayingAdapter(SayingCase.LIST)
    }

    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    override fun initStartView() {
        initRecyclerView()
        initClickListener()
        viewModel.getData()
        viewModel.getAllComments()
        observeData()
    }


    private fun initRecyclerView() {
        binding.rvCalendarDayList.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = dayAdapter
        }
    }

    private fun initClickListener() {
        binding.fabCalendarFloatingActionButton.setOnClickListener {
            flag = when (flag) {
                false -> true
                true -> {
                    viewModel.getAllComments()
                    false
                }
            }
            binding.flag = flag
        }

        binding.customCalendarView.setOnDayClickListener {
            if (it.comments.isEmpty()) {

            } else {
                findNavController().navigate(
                    R.id.action_pager_to_home,
                    bundleOf("docId" to it.comments[0].docId)
                )
            }
        }

        dayAdapter.setOnItemClickListener {
            findNavController().navigate(R.id.action_pager_to_home, bundleOf("saying" to it))
        }
    }

    private fun observeData() {
        viewModel.sayingData.observe(viewLifecycleOwner, Observer {
            dayAdapter.setItems(it as ArrayList<Saying>)
        })

        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            binding.customCalendarView.notifyDataChanged(it)
        })
    }

    override fun onResume() {
        viewModel.getAllComments()
        super.onResume()
    }

}