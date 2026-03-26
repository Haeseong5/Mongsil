package com.cashproject.mongsil.kmp.screen.setting.backup

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.backup.CloudBackupService
import com.cashproject.mongsil.kmp.core.backup.CloudBackupState
import com.cashproject.mongsil.kmp.core.backup.CloudWorkingType
import com.cashproject.mongsil.kmp.core.backup.model.BackupConflictPolicy
import com.cashproject.mongsil.kmp.core.backup.model.BackupOperationResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BackupRestoreViewModel(
    private val createBackupUseCase: CreateBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
    private val validateBackupUseCase: ValidateBackupUseCase,
    private val cloudBackupService: CloudBackupService?,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(BackupRestoreUiState())
    val uiState = _uiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<BackupSnackbarEvent>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    private val _cloudState = MutableStateFlow(
        CloudBackupState(isAvailable = cloudBackupService != null)
    )
    val cloudState = _cloudState.asStateFlow()

    private var lastBackupData: BackupData? = null

    init {
        checkCloudSignInStatus()
    }

    fun createBackup() {
        if (_uiState.value.status is BackupScreenStatus.Working) return

        viewModelScope.launch(exceptionHandler) {
            _uiState.update {
                it.copy(status = BackupScreenStatus.Working(WorkingType.CREATING_BACKUP))
            }

            createBackupUseCase().onSuccess { data ->
                lastBackupData = data
                _uiState.update {
                    it.copy(
                        status = BackupScreenStatus.Success,
                        lastResult = BackupOperationResult.Created(data.diaryCount),
                    )
                }
                _snackbarEvent.emit(BackupSnackbarEvent.BackupCreated(data.diaryCount))
            }.onFailure {
                _uiState.update { it.copy(status = BackupScreenStatus.Error) }
                _snackbarEvent.emit(BackupSnackbarEvent.BackupFailed)
            }
        }
    }

    fun getBackupBytes(): BackupData? = lastBackupData

    fun onBackupFileLoaded(bytes: ByteArray) {
        viewModelScope.launch(exceptionHandler) {
            validateBackupUseCase(bytes).onSuccess { manifest ->
                _uiState.update {
                    it.copy(
                        previewManifest = manifest,
                        pendingRestoreBytes = bytes,
                        status = BackupScreenStatus.Idle,
                        lastResult = null,
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(status = BackupScreenStatus.Error) }
                _snackbarEvent.emit(BackupSnackbarEvent.InvalidFile)
            }
        }
    }

    fun selectPolicy(policy: BackupConflictPolicy) {
        _uiState.update { it.copy(selectedPolicy = policy) }
    }

    fun confirmRestore() {
        val bytes = _uiState.value.pendingRestoreBytes ?: return
        val policy = _uiState.value.selectedPolicy
        if (_uiState.value.status is BackupScreenStatus.Working) return

        viewModelScope.launch(exceptionHandler) {
            _uiState.update {
                it.copy(status = BackupScreenStatus.Working(WorkingType.RESTORING))
            }

            restoreBackupUseCase(bytes, policy).onSuccess { result ->
                _uiState.update {
                    it.copy(
                        status = BackupScreenStatus.Success,
                        lastResult = BackupOperationResult.Restored(result),
                        previewManifest = null,
                        pendingRestoreBytes = null,
                    )
                }
                _snackbarEvent.emit(
                    BackupSnackbarEvent.RestoreCompleted(result.imported)
                )
            }.onFailure {
                _uiState.update { it.copy(status = BackupScreenStatus.Error) }
                _snackbarEvent.emit(BackupSnackbarEvent.RestoreFailed)
            }
        }
    }

    fun dismissPreview() {
        _uiState.update {
            it.copy(
                previewManifest = null,
                pendingRestoreBytes = null,
                status = BackupScreenStatus.Idle,
            )
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(status = BackupScreenStatus.Idle) }
    }

    fun onFileSaved() {
        resetStatus()
        viewModelScope.launch(exceptionHandler) {
            _snackbarEvent.emit(BackupSnackbarEvent.FileSaved)
        }
    }

    // Cloud Backup

    private fun checkCloudSignInStatus() {
        val service = cloudBackupService ?: return
        viewModelScope.launch(exceptionHandler) {
            val signedIn = service.isSignedIn()
            if (signedIn) {
                service.signIn().onSuccess { name ->
                    _cloudState.update {
                        it.copy(isSignedIn = true, accountName = name)
                    }
                    loadCloudBackups()
                }
            }
        }
    }

    fun onCloudSignInSuccess() {
        val service = cloudBackupService ?: return
        viewModelScope.launch(exceptionHandler) {
            service.signIn().onSuccess { name ->
                _cloudState.update {
                    it.copy(isSignedIn = true, accountName = name)
                }
                loadCloudBackups()
                _snackbarEvent.emit(BackupSnackbarEvent.SignedIn(name))
            }.onFailure {
                _snackbarEvent.emit(BackupSnackbarEvent.SignInFailed)
            }
        }
    }

    fun cloudSignOut() {
        val service = cloudBackupService ?: return
        viewModelScope.launch(exceptionHandler) {
            service.signOut()
            _cloudState.update {
                CloudBackupState(isAvailable = true)
            }
        }
    }

    fun uploadToCloud() {
        val service = cloudBackupService ?: return
        if (_cloudState.value.isWorking) return

        viewModelScope.launch(exceptionHandler) {
            _cloudState.update {
                it.copy(isWorking = true, workingType = CloudWorkingType.CREATING_BACKUP)
            }

            createBackupUseCase().onSuccess { data ->
                _cloudState.update {
                    it.copy(workingType = CloudWorkingType.UPLOADING)
                }

                service.upload(data.bytes, CLOUD_BACKUP_FILE_NAME).onSuccess {
                    _cloudState.update { it.copy(isWorking = false, workingType = null) }
                    loadCloudBackups()
                    _snackbarEvent.emit(BackupSnackbarEvent.CloudUploadSuccess)
                }.onFailure {
                    _cloudState.update { it.copy(isWorking = false, workingType = null) }
                    _snackbarEvent.emit(BackupSnackbarEvent.CloudUploadFailed)
                }
            }.onFailure {
                _cloudState.update { it.copy(isWorking = false, workingType = null) }
                _snackbarEvent.emit(BackupSnackbarEvent.BackupFailed)
            }
        }
    }

    fun downloadFromCloud(fileId: String) {
        val service = cloudBackupService ?: return
        if (_cloudState.value.isWorking) return

        viewModelScope.launch(exceptionHandler) {
            _cloudState.update {
                it.copy(isWorking = true, workingType = CloudWorkingType.DOWNLOADING)
            }

            service.download(fileId).onSuccess { bytes ->
                _cloudState.update { it.copy(isWorking = false, workingType = null) }
                onBackupFileLoaded(bytes)
                _snackbarEvent.emit(BackupSnackbarEvent.CloudDownloadSuccess)
            }.onFailure {
                _cloudState.update { it.copy(isWorking = false, workingType = null) }
                _snackbarEvent.emit(BackupSnackbarEvent.CloudDownloadFailed)
            }
        }
    }

    fun deleteCloudBackup(fileId: String) {
        val service = cloudBackupService ?: return
        if (_cloudState.value.isWorking) return

        viewModelScope.launch(exceptionHandler) {
            _cloudState.update {
                it.copy(isWorking = true, workingType = CloudWorkingType.DELETING)
            }

            service.delete(fileId).onSuccess {
                _cloudState.update { it.copy(isWorking = false, workingType = null) }
                loadCloudBackups()
                _snackbarEvent.emit(BackupSnackbarEvent.CloudDeleteSuccess)
            }.onFailure {
                _cloudState.update { it.copy(isWorking = false, workingType = null) }
                _snackbarEvent.emit(BackupSnackbarEvent.CloudDeleteFailed)
            }
        }
    }

    private fun loadCloudBackups() {
        val service = cloudBackupService ?: return
        viewModelScope.launch(exceptionHandler) {
            _cloudState.update {
                it.copy(isWorking = true, workingType = CloudWorkingType.LOADING_BACKUPS)
            }
            service.listBackups().onSuccess { backups ->
                _cloudState.update {
                    it.copy(isWorking = false, workingType = null, backups = backups)
                }
            }.onFailure {
                _cloudState.update { it.copy(isWorking = false, workingType = null) }
            }
        }
    }

    companion object {
        private const val CLOUD_BACKUP_FILE_NAME = "mongsil_backup.json"
    }
}
