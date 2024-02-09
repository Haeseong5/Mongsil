package com.cashproject.mongsil.ui.pages.diary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.extension.verticalScrollDisabled
import com.cashproject.mongsil.ui.component.VerticalSpacer
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.theme.primaryBackgroundColor
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import com.cashproject.mongsil.ui.theme.pxToDp
import kotlin.math.max


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmoticonSelectionBottomSheet(
    emoticons: List<Emoticon> = emptyList(),
    listState: LazyGridState = rememberLazyGridState(),
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    onClickItem: (Emoticon) -> Unit = {},
) {
    val isHideAble by remember { derivedStateOf { !listState.isScrollInProgress } }

    ModalBottomSheet(
        modifier = Modifier
            .fillMaxHeight()
            .then(if (isHideAble) Modifier else Modifier.verticalScrollDisabled()),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = primaryBackgroundColor,
        content = {
            EmoticonSelectionBottomSheetContent(
                emoticons = emoticons,
                listState = listState,
                onClick = onClickItem
            )
        })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun EmoticonSelectionBottomSheetContent(
    emoticons: List<Emoticon>,
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (Emoticon) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    var isSmallDevice by remember { mutableStateOf(false) }
    var itemWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = Unit, block = {
        isSmallDevice = screenWidth <= 360
    })

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
        columns = GridCells.Fixed(3),
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
                    modifier = Modifier.animateItemPlacement(),
                    emoticon = it,
                    onClick = { onClick.invoke(it) },
                    size = pxToDp(pixels = itemWidth.toFloat()),
                    itemWidth = { width ->
                        itemWidth = max(width, itemWidth)
                    }
                )
            }
        }
    )
}

@Composable
fun EmoticonItem(
    modifier: Modifier = Modifier,
    emoticon: Emoticon,
    onClick: () -> Unit,
    size: Dp,
    itemWidth: (Int) -> Unit = {}
) {
    Box(
        modifier = modifier
            .onGloballyPositioned {
                itemWidth.invoke(it.size.width)
            }
            .size(size)
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EmoticonSelectionBottomSheetPreview() {
    EmoticonSelectionBottomSheet()
}