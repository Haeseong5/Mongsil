package com.cashproject.mongsil.ui.pages.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.receiver.AlarmReceiver
import com.cashproject.mongsil.util.PreferencesManager
import java.util.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.databinding.FragmentLockerBinding
import com.cashproject.mongsil.databinding.FragmentSettingBinding
import com.cashproject.mongsil.extension.addTo
import com.cashproject.mongsil.model.data.Saying
import com.cashproject.mongsil.ui.dialog.DiaryListBottomSheetFragment
import com.cashproject.mongsil.ui.main.MainFragment
import com.cashproject.mongsil.ui.pages.locker.LockerAdapter
import com.cashproject.mongsil.util.FragmentListener
import com.cashproject.mongsil.util.RxEventBus
import com.cashproject.mongsil.viewmodel.LockerViewModel
import kotlinx.coroutines.launch

import kotlin.collections.ArrayList


class SettingFragment : Fragment(){

    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingBinding.inflate(inflater, container, false)
            .also { binding ->
                binding.fragment = this
                binding.lifecycleOwner = this
    //            progress = ProgressDialogView(requireContext()).apply {
    //                setCancelable(false)
    //                setCanceledOnTouchOutside(false)
    //            }
            this.binding = binding
        }.root
    }

    fun showBottomListDialog() {
        val bottomSheetDiaryListFragment = DiaryListBottomSheetFragment()
        bottomSheetDiaryListFragment.show(childFragmentManager, "approval")
        bottomSheetDiaryListFragment.setCheckBtnOnClickListener { hour, minute ->
            PreferencesManager.hour = hour
            PreferencesManager.minute = minute
            setAlarm(hour, minute)
            activity?.showToast("매일 ${hour}시 ${minute}분에 푸시메세지가 전송됩니다!")
            bottomSheetDiaryListFragment.dismiss()
        }
        bottomSheetDiaryListFragment.setReleaseBtnClickListener {
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
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager

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

    fun showReadyMessage(){
        activity?.showToast("준비중입니다.")
    }

}