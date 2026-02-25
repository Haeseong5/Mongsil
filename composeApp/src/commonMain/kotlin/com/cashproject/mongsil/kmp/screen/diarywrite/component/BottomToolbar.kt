package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_check
import mongsil.composeapp.generated.resources.ic_imagesmode
import mongsil.composeapp.generated.resources.ic_more_time
import org.jetbrains.compose.resources.painterResource


@Composable
fun BottomToolbar(
    modifier: Modifier = Modifier,
    canSave: Boolean,
    isLoading: Boolean,
    onSaveClick: () -> Unit = {},
    openImagePicker: () -> Unit = {},
    onClickTime: () -> Unit = {}
) {
    val iconBoxSize = 34.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 아이콘들
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(iconBoxSize)
                    .clickable { openImagePicker.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_imagesmode),
                    contentDescription = "image"
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(iconBoxSize)
                    .clickable { onClickTime.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_more_time),
                    contentDescription = "current time"
                )
            }
        }

        // 저장 버튼
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(iconBoxSize)
                .clickable(enabled = canSave && !isLoading, onClick = onSaveClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = "current time"
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BottomToolbarCanSavePreview() {
    MongsilTheme {
        BottomToolbar(
            onSaveClick = {},
            canSave = true,
            isLoading = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomToolbarCannotSavePreview() {
    MongsilTheme {
        BottomToolbar(
            onSaveClick = {},
            canSave = false,
            isLoading = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomToolbarLoadingPreview() {
    MongsilTheme {
        BottomToolbar(
            onSaveClick = {},
            canSave = true,
            isLoading = true
        )
    }
}

// endregion
