package com.cashproject.mongsil.kmp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.toBitmap
import com.cashproject.mongsil.kmp.MainActivity
import com.cashproject.mongsil.kmp.R
import com.cashproject.mongsil.kmp.core.data.DiaryRepository
import com.cashproject.mongsil.kmp.core.data.EmoticonRepository
import com.cashproject.mongsil.kmp.model.ImageResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import org.koin.core.context.GlobalContext
import kotlin.time.ExperimentalTime

class CalendarGlanceWidget : GlanceAppWidget() {

    @OptIn(ExperimentalTime::class)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = kotlin.time.Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

        val (recordedDays, emoticonBitmaps) = runCatching {
            withContext(Dispatchers.IO) {
                val koin = GlobalContext.get()
                val diaryRepo = koin.get<DiaryRepository>()
                val emoticonRepo = koin.get<EmoticonRepository>()

                val diaries = diaryRepo.getDiariesByYearMonth(today.year, today.monthNumber)
                val emoticons = emoticonRepo.getEmoticons()
                    .getOrElse { emptyList() }
                    .associateBy { it.id.toLong() }

                val recorded = diaries.map { it.day }.toSet()
                val bitmaps = mutableMapOf<Int, Bitmap>()
                for (diary in diaries) {
                    val emoticonId = diary.emoticonId ?: continue
                    val image = emoticons[emoticonId]?.image ?: continue
                    val bitmap = loadBitmapFromImageResource(context, image) ?: continue
                    bitmaps[diary.day] = bitmap
                }
                Pair(recorded, bitmaps as Map<Int, Bitmap>)
            }
        }.getOrElse { Pair(emptySet(), emptyMap()) }

        provideContent {
            CalendarWidgetContent(
                today = today,
                recordedDays = recordedDays,
                emoticonBitmaps = emoticonBitmaps,
            )
        }
    }

    private suspend fun loadBitmapFromImageResource(context: Context, resource: ImageResource): Bitmap? =
        when (resource) {
            is ImageResource.Url -> loadBitmapFromUrl(context, resource.url)
            is ImageResource.Local -> runCatching {
                context.assets.open(resource.assetPath).use(android.graphics.BitmapFactory::decodeStream)
            }.getOrNull()
        }

    private suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap? = runCatching {
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false)
            .build()
        (context.imageLoader.execute(request) as? SuccessResult)?.image?.toBitmap()
    }.getOrNull()

}

@SuppressLint("RestrictedApi")
@Composable
private fun CalendarWidgetContent(
    today: LocalDate,
    recordedDays: Set<Int>,
    emoticonBitmaps: Map<Int, Bitmap>,
) {
    val calendarDays = buildCalendarDays(today.year, today.monthNumber)
    val monthTitle = "${today.year}년 ${today.monthNumber}월"
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ImageProvider(R.drawable.widget_background))
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = monthTitle,
            style = TextStyle(
                color = ColorProvider(Color(0xFF1A1A1A)),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = GlanceModifier.fillMaxWidth().padding(bottom = 6.dp),
        )

        Row(modifier = GlanceModifier.fillMaxWidth()) {
            daysOfWeek.forEach { label ->
                Box(
                    modifier = GlanceModifier.defaultWeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF999999)),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }

        Spacer(modifier = GlanceModifier.height(4.dp))

        calendarDays.chunked(7).forEach { week ->
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(vertical = 1.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                week.forEach { day ->
                    if (day != null) {
                        DayCell(
                            day = day,
                            isToday = day == today.dayOfMonth,
                            isRecorded = day in recordedDays,
                            emoticonBitmap = emoticonBitmaps[day],
                            modifier = GlanceModifier.defaultWeight(),
                        )
                    } else {
                        Box(modifier = GlanceModifier.defaultWeight()) {}
                    }
                }
            }
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun DayCell(
    day: Int,
    isToday: Boolean,
    isRecorded: Boolean,
    emoticonBitmap: Bitmap? = null,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val cellModifier = if (isToday) {
            GlanceModifier
                .size(26.dp)
                .background(ImageProvider(R.drawable.widget_today_background))
        } else {
            GlanceModifier.size(26.dp)
        }

        Box(modifier = cellModifier, contentAlignment = Alignment.Center) {
            Text(
                text = day.toString(),
                style = TextStyle(
                    color = ColorProvider(if (isToday) Color.White else Color(0xFF333333)),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                ),
            )
        }

        when {
            emoticonBitmap != null -> {
                Image(
                    provider = ImageProvider(emoticonBitmap),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = GlanceModifier.size(16.dp),
                )
            }

            isRecorded -> {
                Box(
                    modifier = GlanceModifier
                        .size(4.dp)
                        .background(ImageProvider(R.drawable.widget_record_dot))
                ) {}
            }

            else -> {
                Box(modifier = GlanceModifier.size(4.dp)) {}
            }
        }
    }
}

private fun buildCalendarDays(year: Int, month: Int): List<Int?> {
    val firstDayOfMonth = LocalDate(year, month, 1)
    val totalDays = daysInMonth(year, month)
    val startOffset = firstDayOfMonth.dayOfWeek.isoDayNumber % 7

    return buildList {
        repeat(startOffset) { add(null) }
        for (day in 1..totalDays) add(day)
        while (size % 7 != 0) add(null)
    }
}

private fun daysInMonth(year: Int, month: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    else -> 30
}
