package com.cashproject.mongsil.kmp.screen.setting.pdfexport.model

import com.cashproject.mongsil.kmp.screen.setting.pdfexport.PdfExportFile

data class PdfExportUiState(
    val isExporting: Boolean = false,
    val progress: Float = 0f,
    val progressMessage: String = "PDF 생성",
    val exportedFile: PdfExportFile? = null,
    val errorMessage: String? = null,
)
