package com.cashproject.mongsil.kmp.screen.setting.backup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.core.backup.BackupConflictPolicy
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackupRestoreScreen(
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    viewModel: BackupRestoreViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding),
    ) {
        CommonToolbar(
            onBack = onBack,
            title = "백업/복원",
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "충돌 정책: ${uiState.selectedPolicy}",
                style = MongsilTheme.typography.default,
                color = MongsilTheme.colorScheme.labelStrong,
            )

            Button(onClick = { viewModel.selectPolicy(BackupConflictPolicy.Skip) }) {
                Text("Skip 정책")
            }
            Button(onClick = { viewModel.selectPolicy(BackupConflictPolicy.Overwrite) }) {
                Text("Overwrite 정책")
            }
            Button(onClick = { viewModel.selectPolicy(BackupConflictPolicy.Merge) }) {
                Text("Merge 정책")
            }

            Button(onClick = { viewModel.createBackup() }) {
                Text("백업 생성")
            }

            Button(onClick = {
                val location = uiState.lastBackupLocation ?: return@Button
                viewModel.restoreBackup(location)
            }) {
                Text("마지막 백업 복원")
            }

            if (uiState.progressMessage.isNotBlank()) {
                Text(
                    text = uiState.progressMessage,
                    style = MongsilTheme.typography.body1Normal,
                    color = MongsilTheme.colorScheme.labelWeak,
                )
            }

            uiState.lastBackupLocation?.let { location ->
                Text(
                    text = "마지막 백업 위치: $location",
                    style = MongsilTheme.typography.body2Normal,
                    color = MongsilTheme.colorScheme.labelWeak,
                )
            }

            uiState.lastRestoreReport?.let { report ->
                Text(
                    text = "복원 결과 - 가져옴:${report.importedCount}, 병합:${report.mergedCount}, 건너뜀:${report.skippedCount}, 실패:${report.failedCount}",
                    style = MongsilTheme.typography.body2Normal,
                    color = MongsilTheme.colorScheme.labelWeak,
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    style = MongsilTheme.typography.body1Normal,
                    color = MongsilTheme.colorScheme.labelPrimary,
                )
            }
        }
    }
}
