package com.cashproject.mongsil.kmp.screen.setting

import androidx.compose.runtime.Composable

@Composable
expect fun rememberDiaryReminderPermissionRequester(
    onPermissionResult: (Boolean) -> Unit,
    onNavigateToNotificationSetting: () -> Unit = {},
): () -> Unit
