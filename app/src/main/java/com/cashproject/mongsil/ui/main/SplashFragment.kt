package com.cashproject.mongsil.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.cashproject.mongsil.R
import com.cashproject.mongsil.databinding.FragmentSplashBinding
import com.cashproject.mongsil.extension.findNavControllerSafely
import kotlinx.coroutines.*

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