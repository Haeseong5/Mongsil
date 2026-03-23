package com.cashproject.mongsil.kmp.core.backup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.cloud_ios_not_available
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun CloudBackupSection(
    cloudState: CloudBackupState,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onUpload: () -> Unit,
    onDownload: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Text(
        text = stringResource(Res.string.cloud_ios_not_available),
        style = MongsilTheme.typography.caption1,
        color = MongsilTheme.colorScheme.labelWeak,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
    )
}
