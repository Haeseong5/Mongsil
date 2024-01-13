package com.cashproject.mongsil.ui.pages.diary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.DateFormat
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.extension.toTextFormat
import com.cashproject.mongsil.ui.component.HorizontalSpacer
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import com.cashproject.mongsil.ui.theme.textShadow

@Composable
fun DiaryTopLayout(
    modifier: Modifier = Modifier,
    uiState: DiaryUiState = DiaryUiState(),
    onUiEvent: (DiaryUiEvent) -> Unit = {}
) {
    val statusBarHeightDp = getStatusBarHeight()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Black.copy(0.3f),
                        Color.Transparent
                    )
                )
            )
            .padding(top = statusBarHeightDp + 8.dp)
            .height(60.dp)
    ) {
        if (!uiState.isPagerItem) {
            Image(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(30.dp)
                    .align(Alignment.TopStart)
                    .noRippleClickable {
                        onUiEvent.invoke(DiaryUiEvent.Finish)
                    },
                painter = rememberVectorPainter(image = Icons.Outlined.Close),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "info"
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalSpacer(dp = 32.dp)
            Text(
                text = if (Locale.current.language == "ko") uiState.date.toTextFormat(DateFormat.KoreaYearMonthDay) else {
                    uiState.date.toTextFormat(DateFormat.YearMonthDay)
                },
                style = primaryTextStyle.copy(
                    shadow = textShadow,
                    color = Color.White,
                    fontSize = 22.sp
                )
            )
            HorizontalSpacer(dp = 8.dp)
            AsyncImage(
                modifier = Modifier
                    .size(24.dp),
                model = uiState.emoticons.find { it.id == uiState.emoticonId }?.imageUrl ?: "",
                contentDescription = ""
            )
        }

        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(30.dp)
                .align(Alignment.TopEnd)
                .noRippleClickable {
                    onUiEvent.invoke(DiaryUiEvent.ShowMenuBottomSheetDialog(uiState.poster))
                },
            colorFilter = ColorFilter.tint(Color.White),
            painter = rememberVectorPainter(image = Icons.Outlined.MoreVert),
            contentDescription = "info"
        )
    }
}