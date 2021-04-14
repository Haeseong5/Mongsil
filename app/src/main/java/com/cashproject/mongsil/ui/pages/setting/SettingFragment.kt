package com.cashproject.mongsil.ui.pages.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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


class SettingFragment : BaseFragment<FragmentSettingBinding, LockerViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_setting

    override val viewModel: LockerViewModel by viewModels { viewModelFactory } //삭제

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun initStartView() {
    }
}