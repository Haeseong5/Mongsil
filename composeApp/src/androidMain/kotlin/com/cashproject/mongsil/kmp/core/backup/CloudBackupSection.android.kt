package com.cashproject.mongsil.kmp.core.backup

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.backup_creating
import mongsil.composeapp.generated.resources.cloud_backup_to_drive
import mongsil.composeapp.generated.resources.cloud_delete
import mongsil.composeapp.generated.resources.cloud_deleting
import mongsil.composeapp.generated.resources.cloud_download
import mongsil.composeapp.generated.resources.cloud_downloading
import mongsil.composeapp.generated.resources.cloud_saved_backups
import mongsil.composeapp.generated.resources.cloud_sign_in_google
import mongsil.composeapp.generated.resources.cloud_sign_out
import mongsil.composeapp.generated.resources.cloud_signed_in_format
import mongsil.composeapp.generated.resources.cloud_uploading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
actual fun CloudBackupSection(
    cloudState: CloudBackupState,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onUpload: () -> Unit,
    onDownload: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    val context = LocalContext.current
    val driveService = koinInject<CloudBackupService>() as? GoogleDriveBackupService

    val googleSignInLauncher = rememberGoogleSignInLauncher(
        onResult = { account ->
            if (account != null) {
                driveService?.initWithAccount(account)
                onSignIn()
            }
        },
        onError = { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        },
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = MongsilTheme.colorScheme.labelStrong,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Google Drive",
                style = MongsilTheme.typography.body1Medium,
                color = MongsilTheme.colorScheme.labelStrong,
            )
        }

        if (!cloudState.isSignedIn) {
            SignInButton(onClick = googleSignInLauncher)
        } else {
            SignedInContent(
                cloudState = cloudState,
                onSignOut = onSignOut,
                onUpload = onUpload,
                onDownload = onDownload,
                onDelete = onDelete,
            )
        }

        if (cloudState.isWorking) {
            WorkingIndicator(cloudState.workingType)
        }
    }
}

@Composable
private fun WorkingIndicator(type: CloudWorkingType?) {
    val message = when (type) {
        CloudWorkingType.CREATING_BACKUP -> stringResource(Res.string.backup_creating)
        CloudWorkingType.UPLOADING -> stringResource(Res.string.cloud_uploading)
        CloudWorkingType.DOWNLOADING -> stringResource(Res.string.cloud_downloading)
        CloudWorkingType.DELETING -> stringResource(Res.string.cloud_deleting)
        null -> return
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircularProgressIndicator(
            modifier = Modifier
                .height(16.dp)
                .width(16.dp),
            strokeWidth = 2.dp,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = message,
            style = MongsilTheme.typography.caption1,
            color = MongsilTheme.colorScheme.labelWeak,
        )
    }
}

@Composable
private fun SignInButton(onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Text(
        text = stringResource(Res.string.cloud_sign_in_google),
        style = MongsilTheme.typography.body1Medium,
        color = MongsilTheme.colorScheme.labelStrong,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, MongsilTheme.colorScheme.labelStrong.copy(alpha = 0.3f), shape)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
    )
}

@Composable
private fun SignedInContent(
    cloudState: CloudBackupState,
    onSignOut: () -> Unit,
    onUpload: () -> Unit,
    onDownload: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Text(
        text = stringResource(Res.string.cloud_signed_in_format, cloudState.accountName),
        style = MongsilTheme.typography.caption1,
        color = MongsilTheme.colorScheme.labelWeak,
    )

    CloudActionButton(
        icon = Icons.Default.CloudUpload,
        text = stringResource(Res.string.cloud_backup_to_drive),
        enabled = !cloudState.isWorking,
        onClick = onUpload,
    )

    if (cloudState.backups.isNotEmpty()) {
        Text(
            text = stringResource(Res.string.cloud_saved_backups),
            style = MongsilTheme.typography.body2Medium,
            color = MongsilTheme.colorScheme.labelStrong,
            modifier = Modifier.padding(top = 4.dp),
        )
        cloudState.backups.forEach { backup ->
            BackupListItem(
                backup = backup,
                enabled = !cloudState.isWorking,
                onDownload = { onDownload(backup.fileId) },
                onDelete = { onDelete(backup.fileId) },
            )
        }
    }

    Text(
        text = stringResource(Res.string.cloud_sign_out),
        style = MongsilTheme.typography.caption1,
        color = MongsilTheme.colorScheme.labelWeak,
        modifier = Modifier
            .clickable(enabled = !cloudState.isWorking, onClick = onSignOut)
            .padding(vertical = 4.dp),
    )
}

@Composable
private fun CloudActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, MongsilTheme.colorScheme.labelStrong.copy(alpha = 0.3f), shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) {
                MongsilTheme.colorScheme.labelStrong
            } else {
                MongsilTheme.colorScheme.labelWeak
            },
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MongsilTheme.typography.body1Medium,
            color = if (enabled) {
                MongsilTheme.colorScheme.labelStrong
            } else {
                MongsilTheme.colorScheme.labelWeak
            },
        )
    }
}

@Composable
private fun BackupListItem(
    backup: CloudBackupMetadata,
    enabled: Boolean,
    onDownload: () -> Unit,
    onDelete: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, MongsilTheme.colorScheme.labelWeak.copy(alpha = 0.2f), shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = backup.fileName,
                style = MongsilTheme.typography.body2Medium,
                color = MongsilTheme.colorScheme.labelStrong,
            )
            Text(
                text = backup.createdAt.take(10),
                style = MongsilTheme.typography.caption1,
                color = MongsilTheme.colorScheme.labelWeak,
            )
        }
        Icon(
            imageVector = Icons.Default.CloudDownload,
            contentDescription = stringResource(Res.string.cloud_download),
            tint = MongsilTheme.colorScheme.labelStrong,
            modifier = Modifier
                .clickable(enabled = enabled, onClick = onDownload)
                .padding(8.dp),
        )
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(Res.string.cloud_delete),
            tint = MongsilTheme.colorScheme.fillRed,
            modifier = Modifier
                .clickable(enabled = enabled, onClick = onDelete)
                .padding(8.dp),
        )
    }
}
