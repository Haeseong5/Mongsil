package com.cashproject.mongsil.kmp.screen.setting.pdfexport

import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.pdf_ios_not_supported
import org.jetbrains.compose.resources.getString

class IosPdfExportService : PdfExportService {
    override suspend fun exportPdf(
        entries: List<PdfExportEntry>,
        onProgress: (Float, String) -> Unit,
    ): Result<PdfExportFile> = Result.failure(
        IllegalStateException(getString(Res.string.pdf_ios_not_supported))
    )
}
