package com.cashproject.mongsil.kmp.screen.setting.pdfexport

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import android.provider.MediaStore

class AndroidPdfExportService(
    private val context: Context,
) : PdfExportService {

    override suspend fun exportPdf(
        entries: List<PdfExportEntry>,
        onProgress: (Float, String) -> Unit,
    ): Result<PdfExportFile> = withContext(Dispatchers.IO) {
        val document = PdfDocument()

        runCatching {
            val paint = createTextPaint()
            val boldPaint = createBoldPaint()
            val linePaint = Paint().apply {
                color = android.graphics.Color.LTGRAY
                strokeWidth = 2f
                isAntiAlias = true
            }

            val pageWidth = 595
            val pageHeight = 842
            val margin = 40f
            var pageNumber = 1
            var page = document.startPage(createPageInfo(pageWidth, pageHeight, pageNumber))
            var canvas = page.canvas
            var cursorY = margin

            entries.forEachIndexed { index, entry ->
                val startProgress = 0.25f + (index.toFloat() / entries.size.toFloat()) * 0.65f
                onProgress(startProgress, "${index + 1}/${entries.size} 페이지 내용을 그리는 중")

                val emoticonBitmap = entry.emoticonImageUrl?.let(::loadBitmap)
                val photoBitmap = entry.photoPath?.let(::loadBitmap)
                val sectionHeight = estimateSectionHeight(
                    pageWidth = pageWidth.toFloat(),
                    margin = margin,
                    content = entry.content,
                    emoticonBitmap = emoticonBitmap,
                    photoBitmap = photoBitmap,
                    paint = paint,
                    boldPaint = boldPaint,
                )

                if (cursorY + sectionHeight > pageHeight - margin) {
                    document.finishPage(page)
                    pageNumber += 1
                    page = document.startPage(createPageInfo(pageWidth, pageHeight, pageNumber))
                    canvas = page.canvas
                    cursorY = margin
                }

                if (index > 0) {
                    canvas.drawLine(margin, cursorY, pageWidth - margin, cursorY, linePaint)
                    cursorY += 18f
                }

                cursorY = drawTextLine(canvas, "날짜", entry.dateLabel, margin, cursorY, boldPaint, paint)
                cursorY = drawEmoticonBlock(
                    canvas = canvas,
                    title = entry.emoticonTitle,
                    bitmap = emoticonBitmap,
                    margin = margin,
                    cursorY = cursorY,
                    boldPaint = boldPaint,
                    paint = paint,
                )
                cursorY = drawMultilineText(
                    canvas = canvas,
                    label = "텍스트",
                    content = entry.content,
                    pageWidth = pageWidth.toFloat(),
                    margin = margin,
                    cursorY = cursorY,
                    boldPaint = boldPaint,
                    paint = paint,
                )
                cursorY = drawPhotoBlock(
                    canvas = canvas,
                    photoBitmap = photoBitmap,
                    pageWidth = pageWidth.toFloat(),
                    margin = margin,
                    cursorY = cursorY,
                    boldPaint = boldPaint,
                    paint = paint,
                )
            }

            onProgress(0.95f, "PDF 파일을 저장하는 중")
            document.finishPage(page)

            val fileName = "mongsil_diary_${System.currentTimeMillis()}.pdf"
            val savedLocation = saveToDownloads(document, fileName)

            onProgress(1f, "PDF 생성 완료")
            PdfExportFile(
                name = fileName,
                path = savedLocation,
            )
        }.also {
            document.close()
        }
    }

    private fun createPageInfo(width: Int, height: Int, pageNumber: Int): PdfDocument.PageInfo =
        PdfDocument.PageInfo.Builder(width, height, pageNumber).create()

    private fun createTextPaint(): Paint = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 14f
        isAntiAlias = true
    }

    private fun createBoldPaint(): Paint = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 15f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
    }

    private fun estimateSectionHeight(
        pageWidth: Float,
        margin: Float,
        content: String,
        emoticonBitmap: Bitmap?,
        photoBitmap: Bitmap?,
        paint: Paint,
        boldPaint: Paint,
    ): Float {
        val wrappedLines = wrapText(content, pageWidth - margin * 2, paint)
        val textHeight = 28f + (wrappedLines.size.coerceAtLeast(1) * (paint.textSize + 8f))
        val emoticonHeight = if (emoticonBitmap != null) 88f else 34f
        val photoHeight = if (photoBitmap != null) {
            calculateScaledHeight(photoBitmap, pageWidth - margin * 2, 220f) + 34f
        } else {
            34f
        }

        return 32f + boldPaint.textSize + emoticonHeight + textHeight + photoHeight + 36f
    }

    private fun drawTextLine(
        canvas: android.graphics.Canvas,
        label: String,
        value: String,
        margin: Float,
        cursorY: Float,
        boldPaint: Paint,
        paint: Paint,
    ): Float {
        canvas.drawText("$label  ", margin, cursorY + boldPaint.textSize, boldPaint)
        canvas.drawText(value, margin + 58f, cursorY + paint.textSize, paint)
        return cursorY + 34f
    }

    private fun drawEmoticonBlock(
        canvas: android.graphics.Canvas,
        title: String,
        bitmap: Bitmap?,
        margin: Float,
        cursorY: Float,
        boldPaint: Paint,
        paint: Paint,
    ): Float {
        canvas.drawText("이모티콘", margin, cursorY + boldPaint.textSize, boldPaint)
        canvas.drawText(title, margin + 72f, cursorY + paint.textSize, paint)
        var nextY = cursorY + 28f

        if (bitmap != null) {
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 52, 52, true)
            canvas.drawBitmap(scaledBitmap, margin + 72f, nextY, null)
            nextY += 60f
        }

        return nextY
    }

    private fun drawMultilineText(
        canvas: android.graphics.Canvas,
        label: String,
        content: String,
        pageWidth: Float,
        margin: Float,
        cursorY: Float,
        boldPaint: Paint,
        paint: Paint,
    ): Float {
        canvas.drawText(label, margin, cursorY + boldPaint.textSize, boldPaint)
        var nextY = cursorY + 28f
        val lines = wrapText(content, pageWidth - margin * 2, paint)

        lines.forEach { line ->
            canvas.drawText(line, margin, nextY + paint.textSize, paint)
            nextY += paint.textSize + 8f
        }

        return nextY + 8f
    }

    private fun drawPhotoBlock(
        canvas: android.graphics.Canvas,
        photoBitmap: Bitmap?,
        pageWidth: Float,
        margin: Float,
        cursorY: Float,
        boldPaint: Paint,
        paint: Paint,
    ): Float {
        canvas.drawText("사진", margin, cursorY + boldPaint.textSize, boldPaint)
        if (photoBitmap == null) {
            canvas.drawText("첨부된 사진이 없어요.", margin + 48f, cursorY + paint.textSize, paint)
            return cursorY + 34f
        }

        val targetWidth = pageWidth - margin * 2
        val scaledSize = calculateScaledSize(photoBitmap, targetWidth, 220f)
        val scaledBitmap = Bitmap.createScaledBitmap(
            photoBitmap,
            scaledSize.first.toInt(),
            scaledSize.second.toInt(),
            true,
        )
        val imageTop = cursorY + 18f

        canvas.drawBitmap(scaledBitmap, margin, imageTop, null)
        return imageTop + scaledSize.second + 16f
    }

    private fun calculateScaledHeight(bitmap: Bitmap, targetWidth: Float, maxHeight: Float): Float =
        calculateScaledSize(bitmap, targetWidth, maxHeight).second

    private fun calculateScaledSize(bitmap: Bitmap, targetWidth: Float, maxHeight: Float): Pair<Float, Float> {
        val widthRatio = targetWidth / bitmap.width.toFloat()
        val heightRatio = maxHeight / bitmap.height.toFloat()
        val scale = minOf(widthRatio, heightRatio)

        return Pair(
            bitmap.width * scale,
            bitmap.height * scale,
        )
    }

    private fun wrapText(text: String, maxWidth: Float, paint: Paint): List<String> {
        if (text.isBlank()) return listOf("")

        val result = mutableListOf<String>()
        val paragraphs = text.lines()

        paragraphs.forEach { paragraph ->
            val words = paragraph.split(" ")
            val lineBuilder = StringBuilder()

            words.forEach { word ->
                val candidate = if (lineBuilder.isEmpty()) word else "${lineBuilder} $word"
                if (paint.measureText(candidate) <= maxWidth) {
                    lineBuilder.clear()
                    lineBuilder.append(candidate)
                } else {
                    result += lineBuilder.toString()
                    lineBuilder.clear()
                    lineBuilder.append(word)
                }
            }

            result += lineBuilder.toString()
        }

        return result.filter { it.isNotEmpty() }.ifEmpty { listOf("") }
    }

    private fun loadBitmap(pathOrUrl: String): Bitmap? = runCatching {
        when {
            pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://") -> {
                URL(pathOrUrl).openStream().use(BitmapFactory::decodeStream)
            }

            else -> BitmapFactory.decodeFile(pathOrUrl)
        }
    }.getOrNull()

    private fun saveToDownloads(document: PdfDocument, fileName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = requireNotNull(resolver.insert(collection, values)) {
                "다운로드 폴더에 PDF를 만들 수 없어요."
            }

            runCatching {
                resolver.openOutputStream(uri)?.use(document::writeTo)
                    ?: error("다운로드 폴더에 PDF를 쓸 수 없어요.")
            }.onSuccess {
                values.clear()
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            }.onFailure { throwable ->
                resolver.delete(uri, null, null)
                throw throwable
            }

            return uri.toString()
        }

        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            ?: File(context.filesDir, "downloads")
        downloadDir.mkdirs()
        val file = File(downloadDir, fileName)
        file.outputStream().use(document::writeTo)
        return file.absolutePath
    }
}
