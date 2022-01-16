package com.cashproject.mongsil.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

open class SuperFragment : Fragment() {
    val TAG = this.javaClass.simpleName

    fun printLog(message: String) {
        Log.d(TAG, message)
    }

    fun printErrorLog(message: String) {
        Log.e(TAG, message)
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "++onAttach $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "++onCreate $this")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "++onCreateView $this")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "++onViewCreated $this")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "++onStart $this")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "++onResume $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "++onDestroyView $this")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "++onPause $this")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "++onStop $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "++onDetach $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "++onDestroy $this")
    }
}