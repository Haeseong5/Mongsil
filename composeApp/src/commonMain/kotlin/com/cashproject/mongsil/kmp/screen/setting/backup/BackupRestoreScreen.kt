package com.cashproject.mongsil.kmp.screen.setting.backup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.cashproject.mongsil.kmp.designsystem.component.ObserveErrorEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.core.backup.CloudBackupSection
import com.cashproject.mongsil.kmp.core.backup.CloudBackupState
import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupManifest
import com.cashproject.mongsil.kmp.core.backup.model.BackupOperationResult
import com.cashproject.mongsil.kmp.core.backup.model.RestoreResult
import com.cashproject.mongsil.kmp.core.backup.rememberBackupFileLoader
import com.cashproject.mongsil.kmp.core.backup.rememberBackupFileSaver
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.designsystem.component.rememberSnackbarController
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.backup_create
import mongsil.composeapp.generated.resources.backup_creating
import mongsil.composeapp.generated.resources.backup_restore_title
import mongsil.composeapp.generated.resources.backup_result_summary
import mongsil.composeapp.generated.resources.backup_save_to_file
import mongsil.composeapp.generated.resources.backup_section_desc
import mongsil.composeapp.generated.resources.backup_section_title
import mongsil.composeapp.generated.resources.count_with_unit
import mongsil.composeapp.generated.resources.restore_cancel
import mongsil.composeapp.generated.resources.restore_conflict_title
import mongsil.composeapp.generated.resources.restore_policy_merge
import mongsil.composeapp.generated.resources.restore_policy_merge_desc
import mongsil.composeapp.generated.resources.restore_policy_overwrite
import mongsil.composeapp.generated.resources.restore_policy_overwrite_desc
import mongsil.composeapp.generated.resources.restore_policy_skip
import mongsil.composeapp.generated.resources.restore_policy_skip_desc
import mongsil.composeapp.generated.resources.restore_preview_app_version
import mongsil.composeapp.generated.resources.restore_preview_created_at
import mongsil.composeapp.generated.resources.restore_preview_diary_count
import mongsil.composeapp.generated.resources.restore_preview_platform
import mongsil.composeapp.generated.resources.restore_preview_title
import mongsil.composeapp.generated.resources.restore_restoring
import mongsil.composeapp.generated.resources.restore_result_failed
import mongsil.composeapp.generated.resources.restore_result_imported
import mongsil.composeapp.generated.resources.restore_result_merged
import mongsil.composeapp.generated.resources.restore_result_skipped
import mongsil.composeapp.generated.resources.restore_result_title
import mongsil.composeapp.generated.resources.restore_section_desc
import mongsil.composeapp.generated.resources.restore_section_title
import mongsil.composeapp.generated.resources.restore_select_file
import mongsil.composeapp.generated.resources.restore_start
import mongsil.composeapp.generated.resources.snackbar_backup_created
import mongsil.composeapp.generated.resources.snackbar_backup_failed
import mongsil.composeapp.generated.resources.snackbar_cloud_delete_failed
import mongsil.composeapp.generated.resources.snackbar_cloud_delete_success
import mongsil.composeapp.generated.resources.snackbar_cloud_download_failed
import mongsil.composeapp.generated.resources.snackbar_cloud_download_success
import mongsil.composeapp.generated.resources.snackbar_cloud_upload_failed
import mongsil.composeapp.generated.resources.snackbar_cloud_upload_success
import mongsil.composeapp.generated.resources.snackbar_file_saved
import mongsil.composeapp.generated.resources.snackbar_invalid_file
import mongsil.composeapp.generated.resources.snackbar_restore_completed
import mongsil.composeapp.generated.resources.snackbar_restore_failed
import mongsil.composeapp.generated.resources.snackbar_sign_in_failed
import mongsil.composeapp.generated.resources.snackbar_signed_in
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackupRestoreScreen(
    onBack: () -> Unit = {},
    viewModel: BackupRestoreViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cloudState by viewModel.cloudState.collectAsStateWithLifecycle()
    val snackbarController = rememberSnackbarController()

    ObserveErrorEffect(viewModel.errorEvent)

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { event ->
            val message = resolveSnackbarMessage(event)
            snackbarController.showSnackbar(message)
        }
    }

    val fileSaver = rememberBackupFileSaver(
        onSaved = { viewModel.onFileSaved() }
    )

    val fileLoader = rememberBackupFileLoader(
        onLoaded = { bytes -> viewModel.onBackupFileLoaded(bytes) }
    )

    BackupRestoreContent(
        uiState = uiState,
        cloudState = cloudState,
        onBack = onBack,
        onCreateBackup = { viewModel.createBackup() },
        onSaveToFile = {
            val data = viewModel.getBackupBytes()
            if (data != null) {
                fileSaver(data.bytes, "mongsil_backup.json")
            }
        },
        onLoadFile = fileLoader,
        onSelectPolicy = viewModel::selectPolicy,
        onConfirmRestore = viewModel::confirmRestore,
        onDismissPreview = viewModel::dismissPreview,
        onCloudSignIn = viewModel::onCloudSignInSuccess,
        onCloudSignOut = viewModel::cloudSignOut,
        onCloudUpload = viewModel::uploadToCloud,
        onCloudDownload = viewModel::downloadFromCloud,
        onCloudDelete = viewModel::deleteCloudBackup,
    )
}

private suspend fun resolveSnackbarMessage(event: BackupSnackbarEvent): String {
    return when (event) {
        is BackupSnackbarEvent.BackupCreated ->
            getString(Res.string.snackbar_backup_created, event.count)

        is BackupSnackbarEvent.BackupFailed ->
            getString(Res.string.snackbar_backup_failed)

        is BackupSnackbarEvent.FileSaved ->
            getString(Res.string.snackbar_file_saved)

        is BackupSnackbarEvent.InvalidFile ->
            getString(Res.string.snackbar_invalid_file)

        is BackupSnackbarEvent.RestoreCompleted ->
            getString(Res.string.snackbar_restore_completed, event.importedCount)

        is BackupSnackbarEvent.RestoreFailed ->
            getString(Res.string.snackbar_restore_failed)

        is BackupSnackbarEvent.SignedIn ->
            getString(Res.string.snackbar_signed_in, event.name)

        is BackupSnackbarEvent.SignInFailed ->
            getString(Res.string.snackbar_sign_in_failed)

        is BackupSnackbarEvent.CloudUploadSuccess ->
            getString(Res.string.snackbar_cloud_upload_success)

        is BackupSnackbarEvent.CloudUploadFailed ->
            getString(Res.string.snackbar_cloud_upload_failed)

        is BackupSnackbarEvent.CloudDownloadSuccess ->
            getString(Res.string.snackbar_cloud_download_success)

        is BackupSnackbarEvent.CloudDownloadFailed ->
            getString(Res.string.snackbar_cloud_download_failed)

        is BackupSnackbarEvent.CloudDeleteSuccess ->
            getString(Res.string.snackbar_cloud_delete_success)

        is BackupSnackbarEvent.CloudDeleteFailed ->
            getString(Res.string.snackbar_cloud_delete_failed)
    }
}

@Composable
private fun BackupRestoreContent(
    uiState: BackupRestoreUiState,
    cloudState: CloudBackupState,
    onBack: () -> Unit,
    onCreateBackup: () -> Unit,
    onSaveToFile: () -> Unit,
    onLoadFile: () -> Unit,
    onSelectPolicy: (BackupConflictPolicy) -> Unit,
    onConfirmRestore: () -> Unit,
    onDismissPreview: () -> Unit,
    onCloudSignIn: () -> Unit,
    onCloudSignOut: () -> Unit,
    onCloudUpload: () -> Unit,
    onCloudDownload: (String) -> Unit,
    onCloudDelete: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        CommonToolbar(
            onBack = onBack,
            title = stringResource(Res.string.backup_restore_title),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cloudState.isAvailable) {
                CloudBackupSection(
                    cloudState = cloudState,
                    onSignIn = onCloudSignIn,
                    onSignOut = onCloudSignOut,
                    onUpload = onCloudUpload,
                    onDownload = onCloudDownload,
                    onDelete = onCloudDelete,
                )

                SectionDivider()
            }

            BackupSection(
                uiState = uiState,
                onCreateBackup = onCreateBackup,
                onSaveToFile = onSaveToFile,
            )

            SectionDivider()

            RestoreSection(
                uiState = uiState,
                onLoadFile = onLoadFile,
                onSelectPolicy = onSelectPolicy,
                onConfirmRestore = onConfirmRestore,
                onDismissPreview = onDismissPreview,
            )

            StatusMessage(uiState.status)
            ResultSummary(uiState.lastResult)
        }
    }
}

@Composable
private fun BackupSection(
    uiState: BackupRestoreUiState,
    onCreateBackup: () -> Unit,
    onSaveToFile: () -> Unit,
) {
    SectionTitle(stringResource(Res.string.backup_section_title))

    Text(
        text = stringResource(Res.string.backup_section_desc),
        style = MongsilTheme.typography.body2Medium,
        color = MongsilTheme.colorScheme.labelWeak,
    )

    val isWorking = uiState.status is BackupScreenStatus.Working
    val hasBackupData = uiState.lastResult is BackupOperationResult.Created

    ActionButton(
        text = if (isWorking) {
            stringResource(Res.string.backup_creating)
        } else {
            stringResource(Res.string.backup_create)
        },
        enabled = !isWorking,
        onClick = onCreateBackup,
    )

    if (hasBackupData) {
        ActionButton(
            text = stringResource(Res.string.backup_save_to_file),
            enabled = !isWorking,
            onClick = onSaveToFile,
            isPrimary = false,
        )
    }
}

@Composable
private fun RestoreSection(
    uiState: BackupRestoreUiState,
    onLoadFile: () -> Unit,
    onSelectPolicy: (BackupConflictPolicy) -> Unit,
    onConfirmRestore: () -> Unit,
    onDismissPreview: () -> Unit,
) {
    SectionTitle(stringResource(Res.string.restore_section_title))

    Text(
        text = stringResource(Res.string.restore_section_desc),
        style = MongsilTheme.typography.body2Medium,
        color = MongsilTheme.colorScheme.labelWeak,
    )

    val isWorking = uiState.status is BackupScreenStatus.Working

    ActionButton(
        text = stringResource(Res.string.restore_select_file),
        enabled = !isWorking,
        onClick = onLoadFile,
    )

    uiState.previewManifest?.let { manifest ->
        RestorePreview(manifest)
        ConflictPolicySelector(uiState.selectedPolicy, onSelectPolicy)
        RestoreActions(
            isWorking = isWorking,
            onConfirm = onConfirmRestore,
            onCancel = onDismissPreview,
        )
    }
}

@Composable
private fun RestorePreview(manifest: BackupManifest) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, MongsilTheme.colorScheme.labelWeak.copy(alpha = 0.3f), shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(Res.string.restore_preview_title),
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        InfoRow(
            stringResource(Res.string.restore_preview_diary_count),
            stringResource(Res.string.count_with_unit, manifest.diaryCount),
        )
        InfoRow(
            stringResource(Res.string.restore_preview_created_at),
            manifest.createdAtIso.take(10),
        )
        InfoRow(
            stringResource(Res.string.restore_preview_platform),
            manifest.platformName,
        )
        InfoRow(
            stringResource(Res.string.restore_preview_app_version),
            manifest.appVersion,
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MongsilTheme.typography.caption1,
            color = MongsilTheme.colorScheme.labelWeak,
            modifier = Modifier.width(80.dp),
        )
        Text(
            text = value,
            style = MongsilTheme.typography.caption1,
            color = MongsilTheme.colorScheme.labelStrong,
        )
    }
}

@Composable
private fun ConflictPolicySelector(
    selected: BackupConflictPolicy,
    onSelect: (BackupConflictPolicy) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(Res.string.restore_conflict_title),
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        PolicyOption(
            label = stringResource(Res.string.restore_policy_skip),
            description = stringResource(Res.string.restore_policy_skip_desc),
            selected = selected is BackupConflictPolicy.Skip,
            onClick = { onSelect(BackupConflictPolicy.Skip) },
        )
        PolicyOption(
            label = stringResource(Res.string.restore_policy_overwrite),
            description = stringResource(Res.string.restore_policy_overwrite_desc),
            selected = selected is BackupConflictPolicy.Overwrite,
            onClick = { onSelect(BackupConflictPolicy.Overwrite) },
        )
        PolicyOption(
            label = stringResource(Res.string.restore_policy_merge),
            description = stringResource(Res.string.restore_policy_merge_desc),
            selected = selected is BackupConflictPolicy.MergeAppend,
            onClick = { onSelect(BackupConflictPolicy.MergeAppend) },
        )
    }
}

@Composable
private fun PolicyOption(
    label: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MongsilTheme.colorScheme.labelStrong,
            ),
        )
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(
                text = label,
                style = MongsilTheme.typography.body2Medium,
                color = MongsilTheme.colorScheme.labelStrong,
            )
            Text(
                text = description,
                style = MongsilTheme.typography.caption1,
                color = MongsilTheme.colorScheme.labelWeak,
            )
        }
    }
}

@Composable
private fun RestoreActions(
    isWorking: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ActionButton(
            text = stringResource(Res.string.restore_cancel),
            enabled = !isWorking,
            onClick = onCancel,
            isPrimary = false,
            modifier = Modifier.weight(1f),
        )
        ActionButton(
            text = if (isWorking) {
                stringResource(Res.string.restore_restoring)
            } else {
                stringResource(Res.string.restore_start)
            },
            enabled = !isWorking,
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun StatusMessage(status: BackupScreenStatus) {
    if (status is BackupScreenStatus.Working) {
        val message = when (status.type) {
            WorkingType.CREATING_BACKUP -> stringResource(Res.string.backup_creating)
            WorkingType.RESTORING -> stringResource(Res.string.restore_restoring)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                modifier = Modifier.height(16.dp).width(16.dp),
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
}

@Composable
private fun ResultSummary(result: BackupOperationResult?) {
    when (result) {
        is BackupOperationResult.Created -> {
            ResultCard(stringResource(Res.string.backup_result_summary, result.diaryCount))
        }

        is BackupOperationResult.Restored -> {
            RestoreResultCard(result.result)
        }

        null -> {}
    }
}

@Composable
private fun RestoreResultCard(result: RestoreResult) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MongsilTheme.colorScheme.labelStrong.copy(alpha = 0.05f))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(Res.string.restore_result_title),
            style = MongsilTheme.typography.body1Medium,
            color = MongsilTheme.colorScheme.labelStrong,
        )
        InfoRow(
            stringResource(Res.string.restore_result_imported),
            stringResource(Res.string.count_with_unit, result.imported),
        )
        InfoRow(
            stringResource(Res.string.restore_result_skipped),
            stringResource(Res.string.count_with_unit, result.skipped),
        )
        InfoRow(
            stringResource(Res.string.restore_result_merged),
            stringResource(Res.string.count_with_unit, result.merged),
        )
        if (result.failed > 0) {
            InfoRow(
                stringResource(Res.string.restore_result_failed),
                stringResource(Res.string.count_with_unit, result.failed),
            )
        }
    }
}

@Composable
private fun ResultCard(text: String) {
    val shape = RoundedCornerShape(12.dp)
    Text(
        text = text,
        style = MongsilTheme.typography.body2Medium,
        color = MongsilTheme.colorScheme.labelStrong,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MongsilTheme.colorScheme.labelStrong.copy(alpha = 0.05f))
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ActionButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    isPrimary: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(14.dp)
    val bgColor = if (isPrimary) {
        MongsilTheme.colorScheme.labelStrong
    } else {
        MongsilTheme.colorScheme.background
    }
    val textColor = if (isPrimary) {
        MongsilTheme.colorScheme.background
    } else {
        MongsilTheme.colorScheme.labelStrong
    }
    val borderModifier = if (isPrimary) Modifier else {
        Modifier.border(1.dp, MongsilTheme.colorScheme.labelStrong.copy(alpha = 0.3f), shape)
    }

    Text(
        text = text,
        style = MongsilTheme.typography.body1Medium,
        color = if (enabled) textColor else textColor.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .then(borderModifier)
            .background(if (enabled) bgColor else bgColor.copy(alpha = 0.5f))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp),
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MongsilTheme.typography.body1Medium,
        color = MongsilTheme.colorScheme.labelStrong,
    )
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = MongsilTheme.colorScheme.labelWeak.copy(alpha = 0.2f),
    )
}
