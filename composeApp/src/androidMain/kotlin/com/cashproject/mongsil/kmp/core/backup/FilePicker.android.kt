package com.cashproject.mongsil.kmp.core.backup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
actual fun rememberBackupFileSaver(
    onSaved: (String) -> Unit,
): (ByteArray, String) -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var pendingData: ByteArray? = null

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val data = pendingData ?: return@rememberLauncherForActivityResult
        scope.launch {
            withContext(Dispatchers.IO) {
                context.contentResolver.openOutputStream(uri)?.use { it.write(data) }
            }
            onSaved(uri.toString())
        }
    }

    return { data, fileName ->
        pendingData = data
        launcher.launch(fileName)
    }
}

@Composable
actual fun rememberBackupFileLoader(
    onLoaded: (ByteArray) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            val bytes = withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            }
            if (bytes != null) onLoaded(bytes)
        }
    }

    return {
        launcher.launch(arrayOf("application/json"))
    }
}
