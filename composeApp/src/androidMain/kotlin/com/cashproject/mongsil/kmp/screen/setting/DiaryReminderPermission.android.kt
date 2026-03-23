package com.cashproject.mongsil.kmp.screen.setting

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
actual fun rememberDiaryReminderPermissionRequester(
    onPermissionResult: (Boolean) -> Unit,
    onNavigateToNotificationSetting: () -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    var hasRequestedPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            onPermissionResult(granted)

            if (!granted && hasRequestedPermission && activity != null) {
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    POST_NOTIFICATIONS_PERMISSION,
                )
                if (!shouldShowRationale) {
                    openAppNotificationSettings(context)
                    onNavigateToNotificationSetting()
                }
            }
        },
    )

    return remember(context, activity, permissionLauncher) {
        {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                onPermissionResult(true)
            } else {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS_PERMISSION,
                ) == PackageManager.PERMISSION_GRANTED

                if (granted) {
                    onPermissionResult(true)
                } else {
                    hasRequestedPermission = true
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}

private fun openAppNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(intent)
}

private const val POST_NOTIFICATIONS_PERMISSION = "android.permission.POST_NOTIFICATIONS"

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
