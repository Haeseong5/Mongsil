package com.cashproject.mongsil.ui.pages.calendar

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.HomeAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.SayingCase
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    override val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private val dayAdapter by lazy {
        HomeAdapter(SayingCase.LIST)
    }

    private val click by lazy { ClickUtil(this.lifecycle) }

    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    override fun initStartView() {
        initRecyclerView()
        initClickListener()
        viewModel.getData() //read firestore -> display saying in RecyclerView
        viewModel.getAllComments() //read room db -> display comment in CalendarView
        observeData()
        observeErrorEvent()
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

                /**
                 * 댓글이 없으면, Firestore SayingData 요청
                 * 댓글이 있으면, Local DB 데이터 요청
                 */
                if (it.comments.isEmpty()) { viewModel.getDataByDate(Date(it.calendar.timeInMillis)) }
                else { findNavController().navigate(R.id.action_pager_to_home, bundleOf("docId" to it.comments[0].docId)) }
            }
        }

        dayAdapter.setOnItemClickListener {
            if (it.docId != "")
                findNavController().navigate(R.id.action_pager_to_home, bundleOf("saying" to it))
            //else 데이터 삭제해버리기?
        }
    }

    private fun observeData() {
        //리사이클러뷰 데이터 세팅
        viewModel.sayingData.observe(viewLifecycleOwner, Observer {
            dayAdapter.setItems(it as ArrayList<Saying>)
        })

        //댓글 데이터를 받아와서 Calendar 에 세팅
        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            binding.customCalendarView.notifyDataChanged(it)
        })

        //날짜 클릭 시 로컬디비에 데이터 없을 때, Firestore 에서 받아와서 이동
        viewModel.sayingDataByDate.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_pager_to_home, bundleOf("saying" to it))
//            isProgress(false)
        })

        viewModel.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("Loading...", it.toString())
                isProgress(it)
            }
            .addTo(compositeDisposable)
    }

    override fun onResume() {
        viewModel.getAllComments()
        super.onResume()
    }

}