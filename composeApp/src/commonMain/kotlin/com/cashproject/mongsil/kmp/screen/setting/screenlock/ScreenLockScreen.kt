package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenLockScreen(
    onBack: () -> Unit = {},
    viewModel: ScreenLockSettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
    ) {
        CommonToolbar(
            onBack = onBack,
            title = "화면 잠금",
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ScreenLockInfoCard(
                title = uiState.nativeAvailability.title,
                description = uiState.nativeAvailability.description,
            )

            if (uiState.nativeAvailability.isAvailable) {
                SwitchRow(
                    title = "기기 잠금 사용",
                    description = "기기에 저장된 생체인증, PIN, 패턴, 비밀번호로 앱을 잠급니다.",
                    checked = uiState.isEnabled && uiState.method == ScreenLockMethod.SYSTEM,
                    onCheckedChange = viewModel::updateNativeLockEnabled,
                )
            } else {
                MethodOptionRow(
                    title = "앱 비밀번호",
                    description = "네이티브 잠금을 바로 쓸 수 없어서 앱 전용 비밀번호 잠금을 사용합니다.",
                    selected = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text("앱 비밀번호") },
                    placeholder = { Text("4자리 이상 입력") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                Text(
                    text = if (uiState.hasPassword) {
                        "새 비밀번호를 저장하면 기존 앱 비밀번호를 교체합니다."
                    } else {
                        "먼저 앱 비밀번호를 저장한 뒤 잠금을 켤 수 있습니다."
                    },
                    style = MongsilTheme.typography.caption1,
                    color = MongsilTheme.colorScheme.labelWeak,
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = password.trim().length >= ScreenLockSettingsViewModel.MIN_PASSWORD_LENGTH,
                    onClick = {
                        viewModel.updateAppPassword(password)
                        password = ""
                    },
                ) {
                    Text(if (uiState.hasPassword) "비밀번호 변경" else "비밀번호 저장")
                }

                SwitchRow(
                    title = "앱 비밀번호 잠금 사용",
                    description = "저장된 앱 비밀번호로 잠금 화면을 표시합니다.",
                    checked = uiState.isEnabled && uiState.method == ScreenLockMethod.APP_PASSWORD,
                    onCheckedChange = viewModel::updateAppPasswordEnabled,
                    enabled = uiState.hasPassword,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ScreenLockInfoCard(
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MongsilTheme.colorScheme.card)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        Text(
            text = description,
            style = MongsilTheme.typography.default,
            color = MongsilTheme.colorScheme.labelWeak,
        )
    }
}

@Composable
private fun SwitchRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MongsilTheme.typography.body1Medium,
                color = MongsilTheme.colorScheme.labelStrong,
            )
            Text(
                text = description,
                style = MongsilTheme.typography.caption1,
                color = MongsilTheme.colorScheme.labelWeak,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    }
}

@Composable
private fun MethodOptionRow(
    title: String,
    description: String,
    selected: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MongsilTheme.typography.body1Medium,
                color = MongsilTheme.colorScheme.labelStrong,
            )
            Text(
                text = description,
                style = MongsilTheme.typography.caption1,
                color = MongsilTheme.colorScheme.labelWeak,
            )
        }
        RadioButton(
            selected = selected,
            onClick = {},
        )
    }
}
