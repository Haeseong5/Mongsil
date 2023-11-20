package com.cashproject.mongsil.ui.composables.extensions

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun getActivity() = LocalContext.current as ComponentActivity

/**
 * Composable끼리 ViewModel 공유하는 ViewModel
 * reference : https://sungbin.land/composable%EB%81%BC%EB%A6%AC-viewmodel-%EA%B3%B5%EC%9C%A0%ED%95%98%EA%B8%B0-32ef53b24e8c
 */
@Composable
inline fun <reified VM : ViewModel> composableActivityViewModel(
    key: String? = null,
    factory: ViewModelProvider.Factory? = null
): VM = viewModel(
    VM::class.java,
    getActivity(),
    key,
    factory
)