package com.cashproject.mongsil.kmp.screen.setting.pdfexport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.screen.setting.pdfexport.model.PdfExportUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PdfExportViewModel(
    private val diaryRepository: DiaryRepository,
    private val emoticonRepository: EmoticonRepository,
    private val pdfExportService: PdfExportService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfExportUiState())
    val uiState = _uiState.asStateFlow()

    fun exportPdf() {
        if (_uiState.value.isExporting) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isExporting = true,
                    progress = 0f,
                    progressMessage = "일기를 불러오는 중",
                    errorMessage = null,
                    exportedFile = null,
                )
            }

            runCatching {
                val diaries = diaryRepository.getAllDiaries()
                    .sortedWith(compareBy({ it.year }, { it.month }, { it.day }))

                require(diaries.isNotEmpty()) { "내보낼 일기가 없어요." }
                updateProgress(0.15f, "이모티콘 정보를 준비하는 중")

                val emoticons = emoticonRepository.getEmoticons().getOrDefault(emptyList())
                    .associateBy { it.id.toLong() }

                updateProgress(0.25f, "PDF 데이터를 정리하는 중")

                val entries = diaries.map { diary ->
                    PdfExportEntry(
                        dateLabel = formatDate(diary.year, diary.month, diary.day),
                        emoticonTitle = diary.emoticonId
                            ?.let { emoticons[it]?.title }
                            ?: "기록 없음",
                        emoticonImageUrl = diary.emoticonId
                            ?.let { emoticons[it]?.imageUrl },
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
                        progressMessage = "PDF 생성 완료",
                        exportedFile = file,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isExporting = false,
                        progress = 0f,
                        progressMessage = "PDF 생성",
                        errorMessage = throwable.message ?: "PDF 생성에 실패했어요.",
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
