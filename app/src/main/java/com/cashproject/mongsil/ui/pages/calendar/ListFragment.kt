package com.cashproject.mongsil.ui.pages.calendar

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
import com.applandeo.materialcalendarview.EventDay
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentListBinding
import com.cashproject.mongsil.di.Injection
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.SayingAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.SayingCase
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : BaseFragment<FragmentListBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_list

    private lateinit var viewModelFactory: ViewModelFactory

    override val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private val dayAdapter by lazy {
        SayingAdapter(SayingCase.LIST)
    }

    var flag :Boolean = true

    override fun initStartView() {
//        binding.lifecycleOwner = this
        viewModelFactory = Injection.provideViewModelFactory(activity as Context)

        binding.fabCalendarFloatingActionButton.setOnClickListener {
            when(flag){
                false -> {
                    binding.rvCalendarDayList.visibility = View.GONE
                    binding.customCalendarView.visibility = View.VISIBLE
                    binding.fabCalendarFloatingActionButton.setImageResource(R.drawable.ic_list)
                    viewModel.getAllComments()
                    flag = true
                }
                true -> {
                    binding.rvCalendarDayList.visibility = View.VISIBLE
                    binding.customCalendarView.visibility = View.GONE
                    binding.fabCalendarFloatingActionButton.setImageResource(R.drawable.ic_calendar)
                    flag = false
                }
            }
        }

        binding.customCalendarView.setonDayClickListener {
            d("day click", it.toString())
        }

        initDayRecyclerView()
        viewModel.getData()
        viewModel.getAllComments()
        observerData()

    }


    private fun initDayRecyclerView() {
        binding.rvCalendarDayList.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = dayAdapter
        }
        dayAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_pager_to_home,
                bundleOf("image" to it.image, "docId" to it.docId)
            )
        }
    }

    private fun observerData() {
        viewModel.sayingData.observe(viewLifecycleOwner, Observer {
            dayAdapter.setItems(it as ArrayList<Saying>)
            Log.d("observe dATA", it.toString())
        })
        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            binding.customCalendarView.notifyDataChanged(it)
            Log.d(TAG, it.toString())
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllComments()
        Log.d(TAG, "onResume")

    }
}