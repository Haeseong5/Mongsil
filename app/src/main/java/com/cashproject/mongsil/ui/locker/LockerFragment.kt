package com.cashproject.mongsil.ui.locker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentLockerBinding
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.receiver.AlarmReceiver
import com.cashproject.mongsil.util.PreferencesManager
import java.util.*
import kotlin.collections.ArrayList


class LockerFragment : BaseFragment<FragmentLockerBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_locker

    private val lockerAdapter: LockerAdapter by lazy {
        LockerAdapter()
    }

    override fun initStartView() {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar(){
        (activity as AppCompatActivity).setSupportActionBar(binding.lockerToolbar)
        //setHasOptionsMenu(true)는 프래그먼트가 메뉴 관련 콜백을 수신하려 한다고 시스템에 알립니다.
        setHasOptionsMenu(true)
    }

    private fun initRecyclerView(){
        binding.lockerRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = lockerAdapter
        }

        val fakeList = ArrayList<Saying>()
        fakeList.apply {
            add(Saying(1,"https://t2.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/2D4p/image/e_TfCxHtkhP7owdumLgMnRyDiFM.jpeg"))
            add(Saying(2,"https://t2.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/2D4p/image/e_TfCxHtkhP7owdumLgMnRyDiFM.jpeg"))
            add(Saying(3,"https://t2.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/2D4p/image/e_TfCxHtkhP7owdumLgMnRyDiFM.jpeg"))
            add(Saying(4,"https://t2.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/2D4p/image/e_TfCxHtkhP7owdumLgMnRyDiFM.jpeg"))
            add(Saying(5,"https://t2.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/2D4p/image/e_TfCxHtkhP7owdumLgMnRyDiFM.jpeg"))

        }

        lockerAdapter.setItems(fakeList)

        lockerAdapter.setOnItemClickListener {
            activity?.showToast(it.id.toString())
            findNavController().navigate(R.id.action_locker_to_saying,  bundleOf("saying" to it.image))
        }
    }

    private fun showBottomListDialog() {
        val bottomSheetDiaryListFragment = DiaryListBottomSheetFragment()
        bottomSheetDiaryListFragment.show(childFragmentManager, "approval")
        bottomSheetDiaryListFragment.setCheckBtnOnClickListener { hour, minute ->
            activity?.showToast("매일 ${hour}시 ${minute}분에 푸시메세지가 전송됩니다!")
            PreferencesManager.hour = hour
            PreferencesManager.minute = minute
            setAlarm(hour, minute)
            bottomSheetDiaryListFragment.dismiss()
        }
    }

    private fun setAlarm(hour:Int, minute:Int){
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(activity, AlarmReceiver::class.java)  // 1. 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
        val pendingIntent = PendingIntent.getBroadcast(     // 2
                activity, AlarmReceiver.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val repeatInterval: Long = 15 * 60 * 1000   // 15 min
        val calendar: Calendar = Calendar.getInstance().apply { // 3. Calendar 객체를 생성하여 알람이 울릴 정확한 시간을 설정합니다.
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        alarmManager.setInexactRepeating( //Android는 여러 개의 부정확한 반복 알람을 동기화하고 동시에 실행합니다. 이렇게 하면 배터리 소모를 줄일 수 있습니다.
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )
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

}