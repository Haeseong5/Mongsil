package com.cashproject.mongsil.kmp.core.backup

import java.io.File

actual class PlatformBackupFileStore : BackupFileStore {

    actual override suspend fun writeBackup(data: ByteArray, targetLocation: String?): String {
        val file = if (targetLocation.isNullOrBlank()) {
            val backupDir = resolveBackupDirectory().also { dir ->
                if (!dir.exists()) dir.mkdirs()
            }
            File(backupDir, "mongsil-backup-${System.currentTimeMillis()}.json")
        } else {
            File(targetLocation)
        }

        file.parentFile?.mkdirs()
        file.writeBytes(data)
        return file.absolutePath
    }

    actual override suspend fun readBackup(location: String): ByteArray {
        val file = File(location)
        require(file.exists() && file.isFile) {
            "백업 파일을 찾을 수 없습니다: $location"
        }
        return file.readBytes()
    }

    private fun resolveBackupDirectory(): File {
        return File(System.getProperty("java.io.tmpdir"), "mongsil-backups")
    }
}
