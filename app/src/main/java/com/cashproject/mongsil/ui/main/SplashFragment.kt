package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.BaseFragment
import com.cashproject.mongsil.databinding.FragmentMainBinding
import com.cashproject.mongsil.databinding.FragmentSettingBinding
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.extension.findNavControllerSafely
import com.cashproject.mongsil.ui.pages.calendar.CalendarFragment
import com.cashproject.mongsil.ui.pages.home.HomeFragment
import com.cashproject.mongsil.ui.pages.setting.SettingFragment
import com.cashproject.mongsil.util.RxEventBus
import com.cashproject.mongsil.ui.dialog.admob.OnBackPressListener
import com.cashproject.mongsil.ui.dialog.admob.TedAdmobDialog
import com.cashproject.mongsil.ui.pages.calendar.CalendarViewModel
import kotlinx.coroutines.*
import org.koin.androidx.scope.compat.ScopeCompat.lifecycleScope

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSplashBinding.inflate(inflater, container, false)
            .also { binding ->
                this.binding = binding
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showMain()
    }

    private fun showMain() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000L)
            findNavControllerSafely()?.navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}