package com.cashproject.mongsil.kmp.screen.setting.pdfexport

import com.cashproject.mongsil.kmp.model.ImageResource

interface PdfExportService {
    suspend fun exportPdf(
        entries: List<PdfExportEntry>,
        onProgress: (Float, String) -> Unit,
    ): Result<PdfExportFile>
}

data class PdfExportEntry(
    val dateLabel: String,
    val emoticonTitle: String,
    val emoticonImage: ImageResource?,
    val content: String,
    val photoPath: String?,
)

data class PdfExportFile(
    val name: String,
    val path: String,
)
