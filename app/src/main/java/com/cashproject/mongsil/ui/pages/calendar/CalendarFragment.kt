package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.calendar.day.DayAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.ViewTypeCase
import com.cashproject.mongsil.ui.pages.detail.DetailFragment
import com.cashproject.mongsil.util.RxEventBus
import io.reactivex.android.schedulers.AndroidSchedulers
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
        mainViewModel.sayingList.observe(viewLifecycleOwner, {
            dayAdapter.update(it as ArrayList<Saying>)
        })

        mainViewModel.commentList.observe(viewLifecycleOwner, {
            binding.customCalendarView.notifyDataChanged(it)
        })
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
                        saying = mainViewModel.getRandomSaying(it.calendar.time),
                        selectedDate = it.calendar.time
                    )
                )
            }
        }

        dayAdapter.setOnItemClickListener { item, selectedDate ->
            DetailFragment.start(
                fragment = this,
                argument = DetailFragment.Argument(
                    saying = item,
                    selectedDate = selectedDate
                )
            )
        }
    }
}