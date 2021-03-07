package com.cashproject.mongsil.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.calendar.day.SayingAdapter
import com.cashproject.mongsil.ui.calendar.day.SayingCase
import com.cashproject.mongsil.viewmodel.FirebaseViewModel

class CalendarFragment : BaseFragment<FragmentCalendarBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    override val viewModel: FirebaseViewModel by viewModels()

    private val dayAdapter by lazy {
        SayingAdapter(SayingCase.LIST)
    }

    var flag :Boolean = true

    override fun initStartView() {
//        binding.lifecycleOwner = this

        binding.floatingActionButton.setOnClickListener {
            when(flag){
                false -> {
                    binding.calendarRvDayList.visibility = View.GONE
                    binding.cvCalendar.visibility = View.VISIBLE
                    flag = true
                }
                true -> {
                    binding.calendarRvDayList.visibility = View.VISIBLE
                    binding.cvCalendar.visibility = View.GONE
                    flag = false
                }
            }
        }

        initDayRecyclerView()

        viewModel.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerData()
    }

    private fun initDayRecyclerView() {
        binding.calendarRvDayList.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        binding.calendarRvDayList.adapter = dayAdapter
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
    }

}