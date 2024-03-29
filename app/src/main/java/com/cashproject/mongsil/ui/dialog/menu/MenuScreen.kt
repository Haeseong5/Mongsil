package com.cashproject.mongsil.ui.dialog.menu


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle

@Composable
fun MenuScreen(
    uiState: MenuUiState,
    onUiEvent: (MenuUiEvent) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopEnd),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = uiState.year,
                color = colorResource(id = R.color.colorYellow),
                style = primaryTextStyle.copy(
                    fontSize = 22.sp
                )
            )
            Text(
                text = uiState.monthDay,
                color = colorResource(id = R.color.blackOrWhite),
                style = primaryTextStyle.copy(
                    fontSize = 22.sp
                )
            )
        }
        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            MenuItem(
                modifier = Modifier.noRippleClickable {
                    onUiEvent(MenuUiEvent.Share)
                },
                painter = painterResource(id = R.drawable.ic_share),
                text = "공유"
            )
            MenuItem(
                modifier = Modifier.noRippleClickable {
                    onUiEvent(MenuUiEvent.Scrap)
                },
                painter = painterResource(id = if (uiState.scrapped) R.drawable.ic_like_sel else R.drawable.ic_like),
                text = "스크랩"
            )
            MenuItem(
                modifier = Modifier.noRippleClickable {
                    onUiEvent(MenuUiEvent.Save)
                },
                painter = painterResource(id = R.drawable.ic_download),
                text = "저장"
            )
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(46.dp),
            painter = painter,
            colorFilter = ColorFilter.tint(primaryTextColor),
            contentDescription = null
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = colorResource(id = R.color.blackOrWhite),
            style = primaryTextStyle
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MenuScreen(
        MenuUiState(
            year = "2023",
            monthDay = "1225"
        )
    )
}