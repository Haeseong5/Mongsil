package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.error_temporary
import org.jetbrains.compose.resources.stringResource

@Composable
fun ObserveErrorEffect(
    errorEvent: SharedFlow<Unit>,
    snackbarController: SnackbarController = rememberSnackbarController(),
) {
    val message = stringResource(Res.string.error_temporary)
    LaunchedEffect(Unit) {
        errorEvent.collect {
            snackbarController.showSnackbar(message)
        }
    }
}
