package com.cashproject.mongsil.data.db

import android.content.Context
import android.util.Log
import ir.androidexception.roomdatabasebackupandrestore.Backup
import ir.androidexception.roomdatabasebackupandrestore.Restore

/**
 * https://github.com/salehyarahmadi/RoomDatabaseBackupAndRestore?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=8028
 */
object DatabaseUtils {

    const val DB_BACKUP_FOLDER_NAME = "mongsil/"
    const val DB_BACKUP_FILE_NAME = "mongsil_backup.txt"

    private fun getDatabasePath(context: Context): String? {
        return context.getDatabasePath(AppDatabase.DB_FILE_NAME).parent
    }

    fun backupDatabase(context: Context) {
        val path = getDatabasePath(context)
        Log.d("+++++", "file path : $path")
        Backup.Init()
            .database(AppDatabase.getInstance(context))
            .path(path)
            .fileName(DB_BACKUP_FILE_NAME)
            .secretKey("your-secret-key")
            .onWorkFinishListener { success, message ->
                // do anything
                Log.d("+++++", "success: $success message: $message")
            }
            .execute()
    }

    fun restoreDatabase(context: Context) {
        val path = getDatabasePath(context)
        Restore.Init()
            .database(AppDatabase.getInstance(context))
            .backupFilePath("$path/$DB_BACKUP_FILE_NAME")
            .secretKey("your-secret-key")
            .onWorkFinishListener { success, message ->
                Log.d("+++++", "success: $success message: $message")
            }
            .execute()
    }
}