package com.cashproject.mongsil.ui.pages.setting.alarm

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentAlarmBinding
import com.cashproject.mongsil.fcm.PushManager
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.PreferencesManager.isEnabledPushNotification
import com.cashproject.mongsil.util.PreferencesManager.updateEnablePushMessage


class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private val click by lazy { ClickUtil(this.lifecycle) }
    private val pushManager = PushManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAlarmBinding.inflate(inflater, container, false)
            .also { binding ->
                binding.fragment = this
                binding.lifecycleOwner = viewLifecycleOwner
                this.binding = binding
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        if (isEnabledPushNotification) {
            binding.ivAlarmSwitch.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_switch_on
                )
            )
        } else {
            binding.ivAlarmSwitch.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_switch_off
                )
            )
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.tbAlarm)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onClickSwitch() {
        val isEnable = updateEnablePushMessage()
        pushManager.emitPushEvent(isEnable)
        updatePushNotificationUI(isEnable)
    }

    private fun updatePushNotificationUI(isEnabled: Boolean) {
        if (isEnabled) {
            binding.ivAlarmSwitch.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_switch_on
                )
            )
        } else {
            binding.ivAlarmSwitch.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_switch_off
                )
            )
        }
    }

}