package com.cashproject.mongsil.kmp.screen.setting.pdfexport

class IosPdfExportService : PdfExportService {
    override suspend fun exportPdf(
        entries: List<PdfExportEntry>,
        onProgress: (Float, String) -> Unit,
    ): Result<PdfExportFile> = Result.failure(
        IllegalStateException("iOS PDF 내보내기는 아직 지원되지 않아요.")
    )
}
