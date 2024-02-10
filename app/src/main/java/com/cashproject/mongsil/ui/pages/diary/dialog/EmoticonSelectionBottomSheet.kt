package com.cashproject.mongsil.ui.pages.diary.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.component.VerticalSpacer
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.theme.primaryBackgroundColor
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle


@Composable
fun EmoticonSelectionBottomSheetContent(
    emoticons: List<Emoticon>,
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (Emoticon) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    var isSmallDevice by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        isSmallDevice = screenWidth <= 360
    })

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(color = primaryBackgroundColor)
    ) {
        VerticalSpacer(dp = 20.dp)
        Text(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = if (isSmallDevice) 6.dp else 16.dp
            ),
            text = "오늘 기분은 어때요?",
            style = primaryTextStyle,
            color = primaryTextColor,
            fontSize = 26.sp
        )

        LazyVerticalGrid(
            state = listState,
            modifier = Modifier.padding(if (isSmallDevice) 6.dp else 16.dp),
            columns = GridCells.Adaptive(90.dp),
            verticalArrangement = Arrangement.spacedBy(if (isSmallDevice) 6.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(if (isSmallDevice) 6.dp else 16.dp),
            content = {
                items(
                    items = emoticons,
                    key = { emoticon ->
                        emoticon.id
                    }
                ) {
                    EmoticonItem(
//                    modifier = Modifier.animateItemPlacement(),
                        emoticon = it,
                        onClick = { onClick.invoke(it) },
                    )
                }
            }
        )
    }
}

@Composable
fun EmoticonItem(
    modifier: Modifier = Modifier,
    emoticon: Emoticon,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(30.dp))
            .background(color = emoticon.backgroundColor)
            .noRippleClickable { onClick.invoke() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.size(60.dp),
                model = emoticon.imageUrl,
                contentDescription = ""
            )
            VerticalSpacer(dp = 4.dp)
            Text(
                text = emoticon.title,
                color = emoticon.textColor,
                textAlign = TextAlign.Center,
                style = primaryTextStyle
            )
        }
    }
}