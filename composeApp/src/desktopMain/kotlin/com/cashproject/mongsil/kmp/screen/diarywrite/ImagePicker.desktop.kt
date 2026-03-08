package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame

@Composable
actual fun rememberImagePickerLauncher(
    onImagesPicked: (List<String>) -> Unit,
): () -> Unit = remember {
    {
        val dialog = FileDialog(null as Frame?, "이미지 선택", FileDialog.LOAD).apply {
            isMultipleMode = true
            setFilenameFilter { _, name ->
                name.lowercase()
                    .let { it.endsWith(".jpg") || it.endsWith(".jpeg") || it.endsWith(".png") }
            }
        }
        dialog.isVisible = true
        val files = dialog.files?.map { it.absolutePath } ?: emptyList()
        if (files.isNotEmpty()) onImagesPicked(files)
    }
}
