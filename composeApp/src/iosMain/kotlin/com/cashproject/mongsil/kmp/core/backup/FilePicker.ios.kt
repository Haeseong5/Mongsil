package com.cashproject.mongsil.kmp.core.backup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeJSON
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberBackupFileSaver(
    onSaved: (String) -> Unit,
): (ByteArray, String) -> Unit = remember {
    { data, fileName ->
        val tempUrl = writeTempFile(data, fileName)
        if (tempUrl != null) {
            val picker = UIDocumentPickerViewController(
                forExportingURLs = listOf(tempUrl)
            )
            presentPicker(picker)
            onSaved(tempUrl.path.orEmpty())
        }
    }
}

@Composable
actual fun rememberBackupFileLoader(
    onLoaded: (ByteArray) -> Unit,
): () -> Unit = remember {
    {
        val picker = UIDocumentPickerViewController(
            forOpeningContentTypes = listOf(UTTypeJSON)
        )
        val delegate = FilePickerDelegate { url ->
            val bytes = readFileAsBytes(url)
            if (bytes != null) onLoaded(bytes)
        }
        picker.delegate = delegate
        presentPicker(picker)
    }
}

private fun presentPicker(picker: UIDocumentPickerViewController) {
    val rootVc = UIApplication.sharedApplication.keyWindow?.rootViewController
    rootVc?.presentViewController(picker, animated = true, completion = null)
}

@OptIn(ExperimentalForeignApi::class)
private fun writeTempFile(data: ByteArray, fileName: String): NSURL? {
    val docDir = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    ) ?: return null

    val fileUrl = docDir.URLByAppendingPathComponent(fileName) ?: return null
    val nsData = data.toNSData()
    return if (nsData.writeToURL(fileUrl, atomically = true)) fileUrl else null
}

@OptIn(ExperimentalForeignApi::class)
private fun readFileAsBytes(url: NSURL): ByteArray? {
    val nsData = NSData.dataWithContentsOfURL(url) ?: return null
    return nsData.toByteArray()
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.create(bytes = pinned.addressOf(0), length = this.size.toULong())
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val bytes = ByteArray(this.length.toInt())
    bytes.usePinned { pinned ->
        memcpy(pinned.addressOf(0), this.bytes, this.length)
    }
    return bytes
}

private class FilePickerDelegate(
    private val onPicked: (NSURL) -> Unit,
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>,
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL ?: return
        onPicked(url)
    }
}
