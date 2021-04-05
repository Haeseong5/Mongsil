package com.cashproject.mongsil.ui.pages.calendar

import PaginationScrollListener
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
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.pages.calendar.day.DayAdapter
import com.cashproject.mongsil.ui.pages.calendar.day.ViewTypeCase
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.RxEventBus
import com.cashproject.mongsil.viewmodel.CalendarViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    override val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    private val dayAdapter by lazy {
        DayAdapter(ViewTypeCase.NORMAL)
    }

    private val click by lazy { ClickUtil(this.lifecycle) }

    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    private val PAGE_START = 1
    private val itemCount = 0
    private var currentPage: Int = PAGE_START
    private val totalPage = 10

    private var isLastPage = false
    private var isLoading = false

    override fun initStartView() {
        initRecyclerView()
        initClickListener()
        viewModel.getCalendarListData(Date(Calendar.getInstance().timeInMillis)) //read firestore -> display saying in RecyclerView
        viewModel.getAllComments() //read room db -> display comment in CalendarView
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

            addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
                override fun loadMoreItems() {
                    Log.d(TAG, "RecyclerView Scrolling: 마지막 아이템이 보이기 시작함!")
                    isLoading = true;
//                    //Increment page index to load the next one
                    currentPage += 1
//                    viewModel.getData() //read firestore -> display saying in RecyclerView
                }

                override fun isLastPage(): Boolean {
                    return this@CalendarFragment.isLastPage
                }

                override fun isLoading(): Boolean {
                    return this@CalendarFragment.isLoading
                }

            })
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
                     * 댓글이 없으면, Firestore 에 해당 날짜의 명언 데이터 요청
                     *  단, 오늘 이후 데이터는 조회 안되도록 하기
                     * 댓글이 있으면, Local DB 데이터 요청
                     */
                    if (it.comments.isEmpty()) {
                        val selectedTimeInMillis = it.calendar.timeInMillis
                        val todayTimeInMillis = Calendar.getInstance().timeInMillis
                        if(selectedTimeInMillis <= todayTimeInMillis)
                            viewModel.getDataByDate(Date(selectedTimeInMillis))
                    } else {
                        findNavController().navigate(
                            R.id.action_pager_to_home,
                            bundleOf("docId" to it.comments[0].docId)
                        )
                    }
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
            dayAdapter.update(it as ArrayList<Saying>)
        })

        //댓글 데이터를 받아와서 CalendarView 에 세팅
        viewModel.commentData.observe(viewLifecycleOwner, Observer {
            binding.customCalendarView.notifyDataChanged(it)
            Log.d(TAG, "++CommentData: ${it[it.size-1]}")
        })

        /**
         * 달력 빨리 눌럿을 때 에러
         * java.lang.IllegalArgumentException: Navigation action/destination com.cashproject.mongsil:id/action_pager_to_home cannot be found from the current destination Destination(com.cashproject.mongsil:id/homeFragment) label=saying class=com.cashproject.mongsil.ui.pages.home.HomeFragment

         */
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

        RxEventBus.toCommentObservable().subscribe{
            if (it) viewModel.getAllComments()
            Log.d(TAG, "++RxEventBus Consume $it") //댓글이 삭제되는 시점에 데이타 수신
        }.addTo(compositeDisposable)
    }

}