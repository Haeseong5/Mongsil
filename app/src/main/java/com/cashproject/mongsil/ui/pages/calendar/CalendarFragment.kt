package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.data.db.entity.SayingEntity
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.calendar.day.DayAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.ViewTypeCase
import com.cashproject.mongsil.ui.pages.detail.DetailFragment
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    private val mainViewModel: MainViewModel by activityViewModels()

    private val dayAdapter by lazy {
        DayAdapter(ViewTypeCase.NORMAL)
    }

    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getAllComments()
        Log.d(this.javaClass.name, "this: $this dayAdapter: $dayAdapter")
        initRecyclerView()
        initClickListener()
        mainViewModel.sayingEntityList.observe(viewLifecycleOwner) {
            dayAdapter.update(it as ArrayList<SayingEntity>)
        }

        mainViewModel.commentEntityList.observe(viewLifecycleOwner) {
            binding.customCalendarView.notifyDataChanged(it)
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvCalendarDayList.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            adapter = dayAdapter
        }
    }

    private fun initClickListener() {
        binding.fabCalendarFloatingActionButton.setOnClickListener {
            flag = when (flag) {
                false -> true
                true -> false
            }
            binding.flag = flag
        }

        binding.customCalendarView.setOnDayClickListener {
            click.run {
                DetailFragment.start(
                    fragment = this,
                    argument = DetailFragment.Argument(
                        sayingEntity = mainViewModel.getRandomSaying(it.calendar.time),
                        selectedDate = it.calendar.time
                    )
                )
            }
        }

        dayAdapter.setOnItemClickListener { item, selectedDate ->
            DetailFragment.start(
                fragment = this,
                argument = DetailFragment.Argument(
                    sayingEntity = item,
                    selectedDate = selectedDate
                )
            )
        }
    }
}