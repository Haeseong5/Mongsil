package com.cashproject.mongsil.kmp.screen.setting.screenlock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.cashproject.mongsil.kmp.designsystem.component.ObserveErrorEffect
import com.cashproject.mongsil.kmp.model.ScreenLockMethod
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.screen_lock_app_password
import mongsil.composeapp.generated.resources.screen_lock_app_password_desc
import mongsil.composeapp.generated.resources.screen_lock_app_password_enable
import mongsil.composeapp.generated.resources.screen_lock_app_password_enable_desc
import mongsil.composeapp.generated.resources.screen_lock_change_password
import mongsil.composeapp.generated.resources.screen_lock_device_lock
import mongsil.composeapp.generated.resources.screen_lock_device_lock_desc
import mongsil.composeapp.generated.resources.screen_lock_password_hint
import mongsil.composeapp.generated.resources.screen_lock_password_replace
import mongsil.composeapp.generated.resources.screen_lock_password_required
import mongsil.composeapp.generated.resources.screen_lock_save_password
import mongsil.composeapp.generated.resources.setting_screen_lock
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenLockScreen(
    onBack: () -> Unit = {},
    viewModel: ScreenLockSettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var password by rememberSaveable { mutableStateOf("") }

    ObserveErrorEffect(viewModel.errorEvent)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        CommonToolbar(
            onBack = onBack,
            title = stringResource(Res.string.setting_screen_lock),
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
                    title = stringResource(Res.string.screen_lock_device_lock),
                    description = stringResource(Res.string.screen_lock_device_lock_desc),
                    checked = uiState.isEnabled && uiState.method == ScreenLockMethod.SYSTEM,
                    onCheckedChange = viewModel::updateNativeLockEnabled,
                )
            } else {
                MethodOptionRow(
                    title = stringResource(Res.string.screen_lock_app_password),
                    description = stringResource(Res.string.screen_lock_app_password_desc),
                    selected = true,
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    label = { Text(stringResource(Res.string.screen_lock_app_password)) },
                    placeholder = { Text(stringResource(Res.string.screen_lock_password_hint)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                )

                Text(
                    text = if (uiState.hasPassword) {
                        stringResource(Res.string.screen_lock_password_replace)
                    } else {
                        stringResource(Res.string.screen_lock_password_required)
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
                    Text(
                        if (uiState.hasPassword) stringResource(Res.string.screen_lock_change_password) else stringResource(
                            Res.string.screen_lock_save_password
                        )
                    )
                }

                SwitchRow(
                    title = stringResource(Res.string.screen_lock_app_password_enable),
                    description = stringResource(Res.string.screen_lock_app_password_enable_desc),
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
