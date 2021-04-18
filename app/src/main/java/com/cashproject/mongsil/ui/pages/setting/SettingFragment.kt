package com.cashproject.mongsil.ui.pages.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.showToast
import com.cashproject.mongsil.receiver.AlarmReceiver
import com.cashproject.mongsil.util.PreferencesManager
import java.util.*
import com.cashproject.mongsil.databinding.FragmentSettingBinding
import com.cashproject.mongsil.ui.dialog.DiaryListBottomSheetFragment


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
            this.binding = binding
        }.root
    }

    fun showReadyMessage(){
        activity?.showToast("준비중입니다.")
    }

    fun startLocker(){
        findNavController().navigate(R.id.action_setting_to_locker)
    }

    fun startAlarm(){
        findNavController().navigate(R.id.action_setting_to_alarm)
    }
}