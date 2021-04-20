package com.cashproject.mongsil.ui.pages.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentAlarmBinding
import com.cashproject.mongsil.databinding.FragmentLockerBinding
import com.cashproject.mongsil.databinding.FragmentSettingBinding
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.receiver.AlarmReceiver
import com.cashproject.mongsil.ui.dialog.DiaryListBottomSheetFragment
import com.cashproject.mongsil.ui.pages.locker.LockerAdapter
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.DateUtil
import com.cashproject.mongsil.util.PreferencesManager
import com.cashproject.mongsil.util.PreferencesManager.alarm
import com.cashproject.mongsil.util.PreferencesManager.hour
import com.cashproject.mongsil.util.PreferencesManager.minute
import com.cashproject.mongsil.util.RxEventBus
import com.cashproject.mongsil.viewmodel.LockerViewModel
import java.util.*


class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private val click by lazy { ClickUtil(this.lifecycle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)는 프래그먼트가 메뉴 관련 콜백을 수신하려 한다고 시스템에 알립니다.
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAlarmBinding.inflate(inflater, container, false)
            .also { binding ->
                binding.fragment = this
                binding.lifecycleOwner = this
                this.binding = binding
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        binding.tvAlarmTime.text = DateUtil.timeToString(hour, minute)

        if(alarm)
            binding.ivAlarmSwitch.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_switch_on))
        else
            binding.ivAlarmSwitch.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_switch_off))
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbAlarm)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun showBottomListDialog() {
        val bottomSheetAlarmDialog = DiaryListBottomSheetFragment()
        click.run {
            bottomSheetAlarmDialog.show(childFragmentManager, "approval")
        }
        bottomSheetAlarmDialog.setCheckBtnOnClickListener { hour, minute ->
            PreferencesManager.hour = hour
            PreferencesManager.minute = minute
            alarm = true
            setAlarm(hour, minute)
            binding.tvAlarmTime.text = DateUtil.timeToString(hour, minute)

            activity?.showToast("매일 ${hour}시 ${minute}분에 푸시메세지가 전송됩니다!")
            bottomSheetAlarmDialog.dismiss()
        }
        bottomSheetAlarmDialog.setReleaseBtnClickListener {
            //알람해제
            val intent = Intent(activity, AlarmReceiver::class.java)  // 1. 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
            PendingIntent.getBroadcast(     // 2 PendingIntent가 이미 존재할 경우 cancel 하고 다시 생성
                activity,
                AlarmReceiver.NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT //PendingIntent 객체가 이미 존재할 경우, 기존의 ExtraData 를 모두 삭제
            ).run {
                cancel()
            }
        }
    }

    private fun setAlarm(hour: Int, minute: Int) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(activity, AlarmReceiver::class.java)  // 1. 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
        val pendingIntent = PendingIntent.getBroadcast(     // 2 PendingIntent가 이미 존재할 경우 cancel 하고 다시 생성
            activity,
            AlarmReceiver.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT //PendingIntent 객체가 이미 존재할 경우, 기존의 ExtraData 를 모두 삭제
        )

        val calendar: Calendar =
            Calendar.getInstance().apply { // 3. Calendar 객체를 생성하여 알람이 울릴 정확한 시간을 설정합니다.
//                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onClickSwitch(){
        alarm = !alarm
        if(alarm)
            binding.ivAlarmSwitch.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_switch_on))
        else {
            binding.ivAlarmSwitch.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_switch_off
                )
            )
            //알람해제
            val intent = Intent(activity, AlarmReceiver::class.java)  // 1. 알람 조건이 충족되었을 때, 리시버로 전달될 인텐트를 설정합니다.
            PendingIntent.getBroadcast(     // 2 PendingIntent가 이미 존재할 경우 cancel 하고 다시 생성
                activity,
                AlarmReceiver.NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT //PendingIntent 객체가 이미 존재할 경우, 기존의 ExtraData 를 모두 삭제
            ).run {
                cancel()
            }
        }
    }


}