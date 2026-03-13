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

    if (state.method == ScreenLockMethod.SYSTEM) {
        LaunchedEffect(state.method, authNonce) {
            when (val result = nativeAuthenticator.authenticate("몽실 잠금을 해제해 주세요.")) {
                NativeScreenLockResult.Success -> {
                    message = null
                    onUnlocked()
                }
                is NativeScreenLockResult.Failure -> {
                    message = result.message ?: "잠금 해제에 실패했습니다. 다시 시도해 주세요."
                }
                NativeScreenLockResult.Cancelled -> {
                    message = "잠금 해제가 취소되었습니다."
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
                text = "잠금 해제",
                style = MongsilTheme.typography.headline1,
                color = MongsilTheme.colorScheme.labelStrong,
            )

            Text(
                text = when (state.method) {
                    ScreenLockMethod.SYSTEM -> "기기에 저장된 인증 방식으로 잠금을 해제해 주세요."
                    ScreenLockMethod.APP_PASSWORD -> "앱 비밀번호를 입력해 잠금을 해제해 주세요."
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
                    label = { Text("앱 비밀번호") },
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
                            message = "비밀번호가 올바르지 않습니다."
                        }
                    },
                ) {
                    Text("잠금 해제")
                }
            } else if (state.method == ScreenLockMethod.SYSTEM) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { authNonce += 1 },
                ) {
                    Text("다시 시도")
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
