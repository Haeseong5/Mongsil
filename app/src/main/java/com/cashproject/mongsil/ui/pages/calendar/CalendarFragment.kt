package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.DayAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.ViewTypeCase
import com.cashproject.mongsil.ui.pages.home.detail.DetailFragment
import com.cashproject.mongsil.util.RxEventBus
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    override val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private val dayAdapter by lazy {
        DayAdapter(ViewTypeCase.NORMAL)
    }

    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    override fun initStartView() {
        initRecyclerView()
        initClickListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        observeErrorEvent()
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
                        saying = Saying("", "", "", Date()),
                        selectedDate = it.calendar.time
                    )
                )
            }
        }

        dayAdapter.setOnItemClickListener {
//            DetailFragment.start(
//                fragment = this,
//                saying = Saying("", "", "", Date())
//            )
        }
    }

    private fun observeData() {
        mainActivity?.mainViewModel?.sayingList?.observe(viewLifecycleOwner, {
            dayAdapter.update(it as ArrayList<Saying>)
        })

        mainActivity?.mainViewModel?.commentList?.observe(viewLifecycleOwner, {
            binding.customCalendarView.notifyDataChanged(it)
        })

        viewModel.sayingDataByDate.observe(viewLifecycleOwner, {
//            DetailFragment.start(
//                fragment = this,
//                saying = it
//            )
        })

        viewModel.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("Loading...", it.toString())
                mainActivity?.progressBar?.isProgress(false)
            }
            .addTo(compositeDisposable)

        RxEventBus.toResumedObservable().subscribe {
            if (it) mainActivity?.mainViewModel?.getAllComments()
            Log.d(TAG, "++RxEventBus Consume $it")
        }.addTo(compositeDisposable)
    }

    override fun onResume() {
        super.onResume()
        mainActivity?.mainViewModel?.getAllComments()
    }

    override fun onPause() {
        super.onPause()
        RxEventBus.sendToHome(true)
    }
}