package com.cashproject.mongsil.kmp.screen.diarywrite

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable
actual fun rememberImagePickerLauncher(
    onImagesPicked: (List<String>) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        val copiedPaths = uris.mapNotNull { copyImageToAppStorage(context, it) }
        if (copiedPaths.isNotEmpty()) {
            onImagesPicked(copiedPaths)
        }
    }

    return {
        launcher.launch("image/*")
    }
}

private fun copyImageToAppStorage(context: Context, uri: Uri): String? {
    return runCatching {
        val imagesDir = File(context.filesDir, "diary_images").apply { mkdirs() }
        val extension = context.contentResolver.getType(uri)
            ?.substringAfterLast('/')
            ?.ifBlank { "jpg" }
            ?: "jpg"
        val file = File(imagesDir, "photo_${System.currentTimeMillis()}.$extension")

        context.contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        } ?: return null

        file.absolutePath
    }.getOrNull()
}
