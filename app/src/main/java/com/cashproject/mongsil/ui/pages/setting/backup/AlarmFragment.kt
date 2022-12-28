package com.cashproject.mongsil.ui.pages.setting.backup

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.cashproject.mongsil.databinding.FragmentBackupBinding
import com.cashproject.mongsil.data.db.DatabaseUtils


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