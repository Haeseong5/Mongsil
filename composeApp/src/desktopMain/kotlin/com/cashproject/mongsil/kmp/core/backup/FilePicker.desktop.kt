package com.cashproject.mongsil.kmp.core.backup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberBackupFileSaver(
    onSaved: (String) -> Unit,
): (ByteArray, String) -> Unit = remember {
    { data, fileName ->
        val dialog = FileDialog(null as Frame?, "백업 파일 저장", FileDialog.SAVE).apply {
            file = fileName
            setFilenameFilter { _, name -> name.endsWith(".json") }
        }
        dialog.isVisible = true
        val dir = dialog.directory
        val name = dialog.file
        if (dir != null && name != null) {
            val file = File(dir, name)
            file.writeBytes(data)
            onSaved(file.absolutePath)
        }
    }
}

@Composable
actual fun rememberBackupFileLoader(
    onLoaded: (ByteArray) -> Unit,
): () -> Unit = remember {
    {
        val dialog = FileDialog(null as Frame?, "백업 파일 선택", FileDialog.LOAD).apply {
            setFilenameFilter { _, name -> name.endsWith(".json") }
        }
        dialog.isVisible = true
        val dir = dialog.directory
        val name = dialog.file
        if (dir != null && name != null) {
            val bytes = File(dir, name).readBytes()
            onLoaded(bytes)
        }
    }
}
