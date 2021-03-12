package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.SayingAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.SayingCase
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    override val viewModel: CalendarViewModel by viewModels()

    private val dayAdapter by lazy {
        SayingAdapter(SayingCase.LIST)
    }

    var flag :Boolean = true

    override fun initStartView() {
//        binding.lifecycleOwner = this

        binding.fabCalendarFloatingActionButton.setOnClickListener {
            when(flag){
                false -> {
                    binding.rvCalendarDayList.visibility = View.GONE
                    binding.cvCalendarView.visibility = View.VISIBLE
                    flag = true
                }
                true -> {
                    binding.rvCalendarDayList.visibility = View.VISIBLE
                    binding.cvCalendarView.visibility = View.GONE
                    flag = false
                }
            }
        }

        initDayRecyclerView()

        setIconToCalendar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData()

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
    }


    private fun setIconToCalendar(){
        val events: MutableList<EventDay> = ArrayList()

        val calendar: Calendar = Calendar.getInstance()
        events.add(EventDay(calendar, R.drawable.emoticon_01_happy))
        //or
//        events.add(EventDay(calendar, Drawable()))
        //or if you want to specify event label color
//        events.add(EventDay(calendar, R.drawable.sample_icon, Color.parseColor("#228B22")))

        binding.cvCalendarView.setEvents(events)
    }


}