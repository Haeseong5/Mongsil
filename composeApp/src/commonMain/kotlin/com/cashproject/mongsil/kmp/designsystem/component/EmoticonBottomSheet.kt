package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.model.Emoticon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmoticonBottomSheet(
    emoticons: List<Emoticon>,
    onDismiss: () -> Unit,
    onEmoticonSelected: (Emoticon) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
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
            onClick = onEmoticonSelected
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
    listState: LazyGridState = rememberLazyGridState(),
    onClick: (Emoticon) -> Unit = {}
) {
    val isSmallDevice = true

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = if (isSmallDevice) 20.dp else 24.dp
            ),
            text = "오늘의 기분을 선택해주세요",
            style = MongsilTheme.typography.heading1,
            color = MongsilTheme.colorScheme.labelStrong
        )

        LazyVerticalGrid(
            state = listState,
            contentPadding = PaddingValues(
                horizontal = if (isSmallDevice) 16.dp else 24.dp,
                vertical = 16.dp
            ),
            columns = GridCells.Adaptive(90.dp),
            verticalArrangement = Arrangement.spacedBy(if (isSmallDevice) 12.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(if (isSmallDevice) 12.dp else 16.dp),
            content = {
                items(
                    items = emoticons,
                    key = { emoticon ->
                        emoticon.id
                    }
                ) {
                    EmoticonItem(
                        emoticon = it,
                        onClick = { onClick.invoke(it) },
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
            AsyncImage(
                modifier = Modifier.size(60.dp),
                model = emoticon.imageUrl,
                contentDescription = emoticon.title
            )
            VerticalSpacer(4.dp)
            Text(
                text = emoticon.title,
                color = parseHexColor(emoticon.textColor),
                textAlign = TextAlign.Center,
                style = MongsilTheme.typography.body2Normal
            )
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
                title = "행복",
                imageUrl = "https://example.com/happy.png",
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
                Emoticon(1, "행복", "url", "#333333", "#FFE5E5"),
                Emoticon(2, "슬픔", "url", "#333333", "#E5F0FF"),
                Emoticon(3, "화남", "url", "#333333", "#FFE5CC"),
                Emoticon(4, "평온", "url", "#333333", "#E5FFE5"),
            )
        )
    }
}
