package com.cashproject.mongsil.kmp.screen.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberDiaryReminderPermissionRequester(
    onPermissionResult: (Boolean) -> Unit,
): () -> Unit = remember {
    { onPermissionResult(true) }
}
