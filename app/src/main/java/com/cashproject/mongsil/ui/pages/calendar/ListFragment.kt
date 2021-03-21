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
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.SayingAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.SayingCase
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import com.cashproject.mongsil.viewmodel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ListFragment : BaseFragment<FragmentListBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_list

    override val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private val dayAdapter by lazy {
        SayingAdapter(SayingCase.LIST)
    }

    private val click by lazy { ClickUtil(this.lifecycle) }


    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    override fun initStartView() {
        initRecyclerView()
        initClickListener()
        viewModel.getData()
        viewModel.getAllComments()
        observeData()
        observeSubject()
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
            click.run {
                it.calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (it.comments.isEmpty()) { viewModel.getDataByDate(Date(it.calendar.timeInMillis)) }
                else { findNavController().navigate(R.id.action_pager_to_home, bundleOf("docId" to it.comments[0].docId)) }
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

        viewModel.sayingDataByDate.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_pager_to_home, bundleOf("saying" to it))
//            isProgress(false)
        })
    }

    private fun observeSubject(){
        viewModel.errorSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                Log.e("error subject ", it.message.toString())
                activity?.showToast(getString(R.string.error_message))
            }
            .addTo(compositeDisposable)

        viewModel.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                d("Loading...", it.toString())
                isProgress(it)
            }
            .addTo(compositeDisposable)
    }

    override fun onResume() {
        viewModel.getAllComments()
        super.onResume()
    }

}