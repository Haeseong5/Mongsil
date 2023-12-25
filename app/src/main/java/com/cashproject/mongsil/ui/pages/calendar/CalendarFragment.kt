package com.cashproject.mongsil.ui.pages.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentCalendarBinding
import com.cashproject.mongsil.extension.toDate
import com.cashproject.mongsil.ui.main.MainViewModel
import com.cashproject.mongsil.ui.pages.detail.DiaryFragment
import com.cashproject.mongsil.ui.screen.calendar.CalendarScreen

class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_calendar

    private val mainViewModel: MainViewModel by activityViewModels()

    var flag: Boolean = false //false: CalendarView, true: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CalendarScreen(
                    onStartDiary = {
                        val date = it.toDate()
                        DiaryFragment.start(
                            this@CalendarFragment,
                            argument = DiaryFragment.Argument(
                                poster = mainViewModel.getRandomSaying(date),
                                selectedDate = date,
                                from = "calendar"
                            )
                        )
                    }
                )
            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mainViewModel.getAllComments()
//        Log.d(this.javaClass.name, "this: $this dayAdapter: $dayAdapter")
//        initRecyclerView()
//        initClickListener()
//        mainViewModel.allPosters.observe(viewLifecycleOwner) {
//            dayAdapter.update(it.toLegacy() as ArrayList<SayingEntity>)
//        }
//
//        mainViewModel.commentEntityList.observe(viewLifecycleOwner) {
//            binding.customCalendarView.notifyDataChanged(it)
//        }
//    }
//
//    private fun initRecyclerView() {
//        val linearLayoutManager = LinearLayoutManager(
//            context,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//        binding.rvCalendarDayList.apply {
//            layoutManager = linearLayoutManager
//            setHasFixedSize(true)
//            setItemViewCacheSize(10)
//            adapter = dayAdapter
//        }
//    }
//
//    private fun initClickListener() {
//        binding.fabCalendarFloatingActionButton.setOnClickListener {
//            flag = when (flag) {
//                false -> true
//                true -> false
//            }
//            binding.flag = flag
//        }
//
//        binding.customCalendarView.setOnDayClickListener {
//            click.run {
//                DetailFragment.start(
//                    fragment = this,
//                    argument = DetailFragment.Argument(
//                        sayingEntity = mainViewModel.getRandomSaying(it.calendar.time).toLegacy(),
//                        selectedDate = it.calendar.time
//                    )
//                )
//            }
//        }
//
//        dayAdapter.setOnItemClickListener { item, selectedDate ->
//            DetailFragment.start(
//                fragment = this,
//                argument = DetailFragment.Argument(
//                    sayingEntity = item,
//                    selectedDate = selectedDate
//                )
//            )
//        }
//    }
}