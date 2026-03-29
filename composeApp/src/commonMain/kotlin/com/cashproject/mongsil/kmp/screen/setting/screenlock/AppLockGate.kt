package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.app_lock_biometric_reason
import mongsil.composeapp.generated.resources.app_lock_cancelled
import mongsil.composeapp.generated.resources.app_lock_failed
import mongsil.composeapp.generated.resources.app_lock_password_desc
import mongsil.composeapp.generated.resources.app_lock_retry
import mongsil.composeapp.generated.resources.app_lock_system_desc
import mongsil.composeapp.generated.resources.app_lock_title
import mongsil.composeapp.generated.resources.app_lock_wrong_password
import mongsil.composeapp.generated.resources.screen_lock_app_password
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppLockGate(
    state: AppLockUiState,
    nativeAuthenticator: NativeScreenLockAuthenticator,
    onUnlocked: () -> Unit,
) {
    if (!state.shouldShowLockScreen) return

    var password by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf<String?>(null) }
    var authNonce by rememberSaveable { mutableIntStateOf(0) }

    val biometricReason = stringResource(Res.string.app_lock_biometric_reason)
    val failedMessage = stringResource(Res.string.app_lock_failed)
    val cancelledMessage = stringResource(Res.string.app_lock_cancelled)
    val wrongPasswordMessage = stringResource(Res.string.app_lock_wrong_password)

    if (state.method == ScreenLockMethod.SYSTEM) {
        LaunchedEffect(state.method, authNonce) {
            when (val result = nativeAuthenticator.authenticate(biometricReason)) {
                NativeScreenLockResult.Success -> {
                    message = null
                    onUnlocked()
                }
                is NativeScreenLockResult.Failure -> {
                    message = result.message ?: failedMessage
                }
                NativeScreenLockResult.Cancelled -> {
                    message = cancelledMessage
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(Res.string.app_lock_title),
                style = MongsilTheme.typography.headline1,
                color = MongsilTheme.colorScheme.labelStrong,
            )

            Text(
                text = when (state.method) {
                    ScreenLockMethod.SYSTEM -> stringResource(Res.string.app_lock_system_desc)
                    ScreenLockMethod.APP_PASSWORD -> stringResource(Res.string.app_lock_password_desc)
                    ScreenLockMethod.NONE -> ""
                },
                style = MongsilTheme.typography.default,
                color = MongsilTheme.colorScheme.labelWeak,
            )

            if (state.method == ScreenLockMethod.APP_PASSWORD) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text(stringResource(Res.string.screen_lock_app_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val hashedInput = PasswordHasher.hash(password.trim())
                        val isValid = !state.passwordHash.isNullOrBlank() && hashedInput == state.passwordHash
                        if (isValid) {
                            message = null
                            password = ""
                            onUnlocked()
                        } else {
                            message = wrongPasswordMessage
                        }
                    },
                ) {
                    Text(stringResource(Res.string.app_lock_title))
                }
            } else if (state.method == ScreenLockMethod.SYSTEM) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { authNonce += 1 },
                ) {
                    Text(stringResource(Res.string.app_lock_retry))
                }
            }

            message?.let {
                Text(
                    text = it,
                    style = MongsilTheme.typography.caption1,
                    color = MongsilTheme.colorScheme.labelWeak,
                )
            }
        }
    }
}
