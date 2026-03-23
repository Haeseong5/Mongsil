package com.cashproject.mongsil.kmp.screen.setting.backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.backup.BackupConflictPolicy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BackupRestoreViewModel(
    private val createBackupUseCase: CreateBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BackupRestoreUiState())
    val uiState = _uiState.asStateFlow()

    fun selectPolicy(policy: BackupConflictPolicy) {
        _uiState.update { it.copy(selectedPolicy = policy) }
    }

    fun createBackup(targetLocation: String? = null) {
        if (_uiState.value.isWorking) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isWorking = true,
                    progressMessage = "백업 파일을 생성하는 중",
                    errorMessage = null,
                )
            }

            createBackupUseCase(targetLocation)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isWorking = false,
                            progressMessage = "백업 완료",
                            lastBackupLocation = result.backupLocation,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isWorking = false,
                            progressMessage = "백업",
                            errorMessage = throwable.message ?: "백업에 실패했어요.",
                        )
                    }
                }
        }
    }

    fun restoreBackup(backupLocation: String) {
        if (_uiState.value.isWorking) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isWorking = true,
                    progressMessage = "백업 파일을 복원하는 중",
                    errorMessage = null,
                )
            }

            restoreBackupUseCase(
                backupLocation = backupLocation,
                conflictPolicy = _uiState.value.selectedPolicy,
            ).onSuccess { report ->
                _uiState.update {
                    it.copy(
                        isWorking = false,
                        progressMessage = "복원 완료",
                        lastRestoreReport = report,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isWorking = false,
                        progressMessage = "복원",
                        errorMessage = throwable.message ?: "복원에 실패했어요.",
                    )
                }
            }
        }
    }
}
