package com.cashproject.mongsil.ui.pages.locker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentLockerBinding
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.receiver.AlarmReceiver
import com.cashproject.mongsil.util.PreferencesManager
import java.util.*
import androidx.lifecycle.Observer
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.dialog.DiaryListBottomSheetFragment
import com.cashproject.mongsil.viewmodel.LockerViewModel

import kotlin.collections.ArrayList


class LockerFragment : BaseFragment<FragmentLockerBinding, LockerViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_locker

    override val viewModel: LockerViewModel by viewModels { viewModelFactory }


    private val lockerAdapter: LockerAdapter by lazy {
        LockerAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)는 프래그먼트가 메뉴 관련 콜백을 수신하려 한다고 시스템에 알립니다.
        setHasOptionsMenu(true)
    }

    override fun initStartView() {
        initToolbar()
        initRecyclerView()
        viewModel.getAllLike()
        observeData()
    }


    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbLocker)

        (activity as AppCompatActivity).actionBar?.title = "보관함"

    }

    private fun initRecyclerView() {
        binding.rvLockerList.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = lockerAdapter
        }

        lockerAdapter.setOnItemClickListener {
            findNavController().navigate(R.id.action_pager_to_home, bundleOf("saying" to it))
        }
    }

    private fun observeData(){
        viewModel.likeData.observe(viewLifecycleOwner, Observer {
            lockerAdapter.update(it as ArrayList<Saying>)
        })
    }

    private fun showBottomListDialog() {
        val bottomSheetDiaryListFragment =
            DiaryListBottomSheetFragment()
        bottomSheetDiaryListFragment.show(childFragmentManager, "approval")
        bottomSheetDiaryListFragment.setCheckBtnOnClickListener { hour, minute ->
            activity?.showToast("매일 ${hour}시 ${minute}분에 푸시메세지가 전송됩니다!")
            PreferencesManager.hour = hour
            PreferencesManager.minute = minute
            setAlarm(hour, minute)
            bottomSheetDiaryListFragment.dismiss()
        }
    }

    private fun setAlarm(hour: Int, minute: Int) {
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager

        val intent =
            Intent(activity, AlarmReceiver::class.java)  // 1. 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
        val pendingIntent = PendingIntent.getBroadcast(     // 2
            activity,
            AlarmReceiver.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT //PendingIntent 객체가 이미 존재할 경우, 기존의 ExtraData 를 모두 삭제
        )

        val calendar: Calendar =
            Calendar.getInstance().apply { // 3. Calendar 객체를 생성하여 알람이 울릴 정확한 시간을 설정합니다.
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

//        alarmManager.setInexactRepeating( //Android는 여러 개의 부정확한 반복 알람을 동기화하고 동시에 실행합니다. 이렇게 하면 배터리 소모를 줄일 수 있습니다.
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                AlarmManager.INTERVAL_DAY,
//                pendingIntent
//        )
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

//        alarmManager.cancel(pendingIntent) //알람 취소
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.locker_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_alarm -> {
                showBottomListDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllLike()
    }
}