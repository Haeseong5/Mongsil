package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.core.model.TextSource
import com.cashproject.mongsil.kmp.core.model.asString
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.model.Emoticon
import com.cashproject.mongsil.kmp.model.ImageResource
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.emoticon_01
import mongsil.composeapp.generated.resources.emoticon_bottom_sheet_subtitle
import mongsil.composeapp.generated.resources.emoticon_bottom_sheet_title
import mongsil.composeapp.generated.resources.ic_lock
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmoticonBottomSheet(
    emoticons: List<Emoticon>,
    unlockedPremiumIds: Set<Int>,
    onDismiss: () -> Unit,
    onEmoticonSelected: (Emoticon) -> Unit,
    onPremiumLocked: (Emoticon) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MongsilTheme.colorScheme.card,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        dragHandle = { Header() }
    ) {
        EmoticonSelectionBottomSheetContent(
            emoticons = emoticons,
            unlockedPremiumIds = unlockedPremiumIds,
            onEmoticonSelected = onEmoticonSelected,
            onPremiumLocked = onPremiumLocked,
        )
    }
}

@Composable
private fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 40.dp, height = 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MongsilTheme.colorScheme.fill300)
        )
    }
}

@Composable
fun EmoticonSelectionBottomSheetContent(
    emoticons: List<Emoticon>,
    unlockedPremiumIds: Set<Int> = emptySet(),
    listState: LazyGridState = rememberLazyGridState(),
    onEmoticonSelected: (Emoticon) -> Unit = {},
    onPremiumLocked: (Emoticon) -> Unit = {},
) {
    val isSmallDevice = true
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset = available

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity = available
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 4.dp,
                start = if (isSmallDevice) 20.dp else 24.dp,
                end = if (isSmallDevice) 20.dp else 24.dp
            ),
            text = stringResource(Res.string.emoticon_bottom_sheet_title),
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong
        )

        Text(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                start = if (isSmallDevice) 20.dp else 24.dp,
                end = if (isSmallDevice) 20.dp else 24.dp
            ),
            text = stringResource(Res.string.emoticon_bottom_sheet_subtitle),
            style = MongsilTheme.typography.body2Normal,
            color = MongsilTheme.colorScheme.fill500
        )

        LazyVerticalGrid(
            state = listState,
            modifier = Modifier.nestedScroll(nestedScrollConnection),
            contentPadding = PaddingValues(
                horizontal = if (isSmallDevice) 16.dp else 24.dp,
                vertical = 16.dp
            ),
            columns = GridCells.Adaptive(90.dp),
            verticalArrangement = Arrangement.spacedBy(if (isSmallDevice) 12.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(if (isSmallDevice) 12.dp else 16.dp),
            content = {
                items(items = emoticons, key = { it.id }) { emoticon ->
                    val isLocked = emoticon.isPremium && emoticon.id !in unlockedPremiumIds
                    EmoticonItem(
                        emoticon = emoticon,
                        isLocked = isLocked,
                        onClick = {
                            if (isLocked) onPremiumLocked(emoticon)
                            else onEmoticonSelected(emoticon)
                        },
                    )
                }
            }
        )

        VerticalSpacer(16.dp)
    }
}

@Composable
fun EmoticonItem(
    modifier: Modifier = Modifier,
    emoticon: Emoticon,
    isLocked: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(color = parseHexColor(emoticon.backgroundColor))
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmoticonImage(
                modifier = Modifier.size(40.dp),
                image = emoticon.image,
                contentDescription = emoticon.title.asString()
            )
            VerticalSpacer(4.dp)
            Text(
                text = emoticon.title.asString(),
                color = parseHexColor(emoticon.textColor),
                textAlign = TextAlign.Center,
                style = MongsilTheme.typography.body2Normal
            )
        }

        // 프리미엄 잠금 오버레이
        if (isLocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = "프리미엄 잠금",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}

/**
 * Hex 색상 문자열을 Color 객체로 변환
 * @param hex "#RRGGBB" 또는 "#AARRGGBB" 형식의 문자열
 */
private fun parseHexColor(hex: String): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)

        if (cleanHex.length == 6) {
            // RGB 형식: 알파값 FF 추가
            Color(0xFF000000 or colorInt)
        } else {
            // ARGB 형식
            Color(colorInt)
        }
    } catch (e: Exception) {
        // 파싱 실패 시 기본 색상 반환
        Color.Gray
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    MongsilTheme {
        Header()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmoticonItemPreview() {
    MongsilTheme {
        EmoticonItem(
            emoticon = Emoticon(
                id = 1,
                title = TextSource.Value("행복"),
                image = ImageResource.Local(Res.drawable.emoticon_01),
                textColor = "#333333",
                backgroundColor = "#FFE5E5"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmoticonSelectionBottomSheetContentPreview() {
    MongsilTheme {
        EmoticonSelectionBottomSheetContent(
            emoticons = listOf(
                Emoticon(1, TextSource.Value("행복"), ImageResource.Local(Res.drawable.emoticon_01), "#333333", "#FFE5E5"),
                Emoticon(2, TextSource.Value("슬픔"), ImageResource.Local(Res.drawable.emoticon_01), "#333333", "#E5F0FF"),
                Emoticon(3, TextSource.Value("화남"), ImageResource.Local(Res.drawable.emoticon_01), "#333333", "#FFE5CC"),
                Emoticon(4, TextSource.Value("평온"), ImageResource.Local(Res.drawable.emoticon_01), "#333333", "#E5FFE5"),
            )
        )
    }
}
