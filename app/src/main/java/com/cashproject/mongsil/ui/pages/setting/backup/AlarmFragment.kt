package com.cashproject.mongsil.ui.pages.setting.backup

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentAlarmBinding
import com.cashproject.mongsil.databinding.FragmentBackupBinding
import com.cashproject.mongsil.fcm.PushManager
import com.cashproject.mongsil.model.db.backup.DatabaseUtils
import com.cashproject.mongsil.util.ClickUtil
import com.cashproject.mongsil.util.PreferencesManager.isEnabledPushNotification
import com.cashproject.mongsil.util.PreferencesManager.updateEnablePushMessage


class BackupFragment : Fragment() {

    private lateinit var binding: FragmentBackupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBackupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backup.setOnClickListener {
            DatabaseUtils.backupDatabase(requireContext())
        }

        binding.restore.setOnClickListener {
            DatabaseUtils.restoreDatabase(requireContext())
        }
    }

}