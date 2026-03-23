package com.cashproject.mongsil.kmp.core.backup

import androidx.compose.runtime.Composable

@Composable
expect fun rememberBackupFileSaver(
    onSaved: (String) -> Unit,
): (ByteArray, String) -> Unit

@Composable
expect fun rememberBackupFileLoader(
    onLoaded: (ByteArray) -> Unit,
): () -> Unit
