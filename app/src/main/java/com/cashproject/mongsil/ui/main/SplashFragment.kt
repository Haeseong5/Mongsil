package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cashproject.mongsil.R
import com.cashproject.mongsil.base.SuperFragment
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.extension.findNavControllerSafely
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*
import java.lang.Exception

class SplashFragment : SuperFragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var callback: OnBackPressedCallback

    //Splash 화면에서 이탈 한 후, 돌아왔을 때 MainFragment 로 navigate() 가 되지 않는 이슈가 있음. 원인 확인 필요
    private var isLoaded: Boolean = false

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
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000L)
            isLoaded = true
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLoaded) {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
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