package com.cashproject.mongsil.kmp.screen.setting.pdfexport

import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.BaseViewModel
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.model.PdfExportUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.pdf_create
import mongsil.composeapp.generated.resources.pdf_emoticon_none
import mongsil.composeapp.generated.resources.pdf_error_default
import mongsil.composeapp.generated.resources.pdf_error_no_diaries
import mongsil.composeapp.generated.resources.pdf_progress_complete
import mongsil.composeapp.generated.resources.pdf_progress_emoticons
import mongsil.composeapp.generated.resources.pdf_progress_loading
import mongsil.composeapp.generated.resources.pdf_progress_organizing
import org.jetbrains.compose.resources.getString

class PdfExportViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
    private val pdfExportService: PdfExportService,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(PdfExportUiState())
    val uiState = _uiState.asStateFlow()

    fun exportPdf() {
        if (_uiState.value.isExporting) return

        viewModelScope.launch(exceptionHandler) {
            val loadingMsg = getString(Res.string.pdf_progress_loading)
            val emoticonsMsg = getString(Res.string.pdf_progress_emoticons)
            val organizingMsg = getString(Res.string.pdf_progress_organizing)
            val completeMsg = getString(Res.string.pdf_progress_complete)
            val createLabel = getString(Res.string.pdf_create)
            val noDiariesMsg = getString(Res.string.pdf_error_no_diaries)
            val defaultErrorMsg = getString(Res.string.pdf_error_default)
            val noRecordLabel = getString(Res.string.pdf_emoticon_none)

            _uiState.update {
                it.copy(
                    isExporting = true,
                    progress = 0f,
                    progressMessage = loadingMsg,
                    errorMessage = null,
                    exportedFile = null,
                )
            }

            runCatching {
                val diaries = diaryRepository.getAllDiaries()
                    .sortedWith(compareBy({ it.year }, { it.month }, { it.day }))

                require(diaries.isNotEmpty()) { noDiariesMsg }
                updateProgress(0.15f, emoticonsMsg)

                val emoticons = emoticonRepository.getEmoticons()
                    .associateBy { it.id.toLong() }

                updateProgress(0.25f, organizingMsg)

                val entries = diaries.map { diary ->
                    PdfExportEntry(
                        dateLabel = formatDate(diary.year, diary.month, diary.day),
                        emoticonTitle = diary.emoticonId
                            ?.let { emoticons[it]?.title }
                            ?: noRecordLabel,
                        emoticonImage = diary.emoticonId
                            ?.let { emoticons[it]?.image },
                        content = diary.content,
                        photoPath = diary.photoUri,
                    )
                }

                pdfExportService.exportPdf(entries = entries) { progress, message ->
                    updateProgress(progress, message)
                }.getOrThrow()
            }.onSuccess { file ->
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        progress = 1f,
                        progressMessage = completeMsg,
                        exportedFile = file,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        progress = 0f,
                        progressMessage = createLabel,
                        errorMessage = throwable.message ?: defaultErrorMsg,
                    )
                }
            }
        }
    }

    private fun updateProgress(progress: Float, message: String) {
        _uiState.update {
            it.copy(
                progress = progress.coerceIn(0f, 1f),
                progressMessage = message,
            )
        }
    }

    private fun formatDate(year: Int, month: Int, day: Int): String =
        buildString {
            append(year.toString().padStart(4, '0'))
            append('.')
            append(month.toString().padStart(2, '0'))
            append('.')
            append(day.toString().padStart(2, '0'))
        }
}
