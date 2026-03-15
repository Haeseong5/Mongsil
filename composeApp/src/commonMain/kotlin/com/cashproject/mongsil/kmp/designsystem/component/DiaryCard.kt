package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.core.data.Date
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import com.cashproject.mongsil.kmp.model.ImageResource
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate


@Composable
fun DiaryCard(
    emoticonImage: ImageResource?,
    date: Date,
    content: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .circularRippleClickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MongsilTheme.colorScheme.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (emoticonImage != null) {
                EmoticonImage(
                    image = emoticonImage,
                    contentDescription = "감정 이모티콘",
                    modifier = Modifier.size(100.dp)
                )
            }

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = formatDate(date.year, date.month, date.day),
                style = MongsilTheme.typography.body1Normal,
                color = MongsilTheme.colorScheme.labelWeak
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                text = content,
                style = MongsilTheme.typography.body1Bold,
                color = MongsilTheme.colorScheme.labelStrong,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


fun formatDate(year: Int, month: Int, day: Int): String {
    val date = LocalDate(year, month, day)
    val dayOfWeek = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }

    return "${year}.${month.toString().padStart(2, '0')}.${
        day.toString().padStart(2, '0')
    } $dayOfWeek"
}

@Preview(showBackground = true)
@Composable
private fun DiaryCardPreview() {
    MongsilTheme {
        DiaryCard(
            emoticonImage = null,
            date = Date(2023, 10, 10),
            content = "오늘은 행복한 하루였어요. 내일도 행복한 하루가 되길 바랄게요.",
            onClick = {}
        )
    }
}
