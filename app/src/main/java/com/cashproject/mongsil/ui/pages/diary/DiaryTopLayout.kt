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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.extension.DateFormat
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.extension.toTextFormat
import com.cashproject.mongsil.ui.component.HorizontalSpacer
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
                        Color.Black.copy(0.2f),
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
                    .size(28.dp)
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
            Text(
                text = uiState.date.toTextFormat(DateFormat.YearMonthDay),
                style = TextStyle.Default.copy(shadow = textShadow, color = Color.White)
            )
            HorizontalSpacer(dp = 8.dp)
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = uiState.dailyEmoticon.emoticon.icon),
                contentDescription = ""
            )
        }

        Image(
            modifier = Modifier
                .padding(end = 2.dp)
                .size(28.dp)
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