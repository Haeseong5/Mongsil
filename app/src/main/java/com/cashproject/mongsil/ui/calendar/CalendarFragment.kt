package com.cashproject.mongsil.ui.calendar


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.calendar.day.SayingAdapter
import com.cashproject.mongsil.ui.calendar.day.SayingCase
import com.cashproject.mongsil.ui.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CalendarFragment : BaseFragment<FragmentCalendarBinding, FirebaseViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    override val viewModel: FirebaseViewModel by viewModels()

    private val dayAdapter by lazy {
        SayingAdapter(SayingCase.LIST)
    }

    override fun initStartView() {
        binding.lifecycleOwner = this

        initDayRecyclerView()

        viewModel.getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerData()
    }

    private fun initDayRecyclerView(){
        binding.calendarRvDayList.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        binding.calendarRvDayList.adapter = dayAdapter
    }

    private fun observerData(){
//        Log.d("observe dATA", "it.toString()")

        viewModel.sayingData.observe(viewLifecycleOwner, Observer {
            dayAdapter.setItems(it as ArrayList<Saying>)
            Log.d("observe dATA", it.toString())
        })
    }
}