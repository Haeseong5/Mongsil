package com.cashproject.mongsil.kmp.screen.diarywrite.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.extensions.circularRippleClickable
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.background_color_picker
import mongsil.composeapp.generated.resources.ic_format_align_center
import mongsil.composeapp.generated.resources.ic_format_align_left
import mongsil.composeapp.generated.resources.ic_format_align_right
import mongsil.composeapp.generated.resources.ic_format_color_fill
import mongsil.composeapp.generated.resources.ic_format_color_text
import mongsil.composeapp.generated.resources.ic_imagesmode
import mongsil.composeapp.generated.resources.ic_more_time
import mongsil.composeapp.generated.resources.text_align_toggle
import mongsil.composeapp.generated.resources.text_color_picker
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun BottomToolbar(
    modifier: Modifier = Modifier,
    isSaving: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = Color.Black,
    backgroundColor: Color = Color.Transparent,
    showColorPalette: Boolean = false,
    showBackgroundColorPalette: Boolean = false,
    openImagePicker: () -> Unit = {},
    onClickTime: () -> Unit = {},
    onTextAlignToggle: () -> Unit = {},
    onColorPickerToggle: () -> Unit = {},
    onBackgroundColorPickerToggle: () -> Unit = {},
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
                    .circularRippleClickable { openImagePicker() },
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
                    .circularRippleClickable { onClickTime() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_more_time),
                    contentDescription = "current time"
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(iconBoxSize)
                    .circularRippleClickable { onTextAlignToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(textAlignIcon(textAlign)),
                    contentDescription = stringResource(Res.string.text_align_toggle)
                )
            }

            // 글자 색상 선택 버튼
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(iconBoxSize)
                    .then(
                        if (showColorPalette) Modifier.background(
                            MongsilTheme.colorScheme.labelDisable.copy(alpha = 0.15f),
                            CircleShape
                        ) else Modifier
                    )
                    .circularRippleClickable { onColorPickerToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_format_color_text),
                    contentDescription = stringResource(Res.string.text_color_picker),
                    tint = textColor
                )
            }

            // 배경 색상 선택 버튼
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(iconBoxSize)
                    .then(
                        if (showBackgroundColorPalette) Modifier.background(
                            MongsilTheme.colorScheme.labelDisable.copy(alpha = 0.15f),
                            CircleShape
                        ) else Modifier
                    )
                    .circularRippleClickable { onBackgroundColorPickerToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_format_color_fill),
                    contentDescription = stringResource(Res.string.background_color_picker),
                    tint = if (backgroundColor == Color.Transparent) {
                        MongsilTheme.colorScheme.labelStrong
                    } else {
                        backgroundColor
                    }
                )
            }
        }

        // 자동 저장 인디케이터
        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MongsilTheme.colorScheme.labelWeak
            )
        }
    }
}

private fun textAlignIcon(textAlign: TextAlign) = when (textAlign) {
    TextAlign.Center -> Res.drawable.ic_format_align_center
    TextAlign.End -> Res.drawable.ic_format_align_right
    else -> Res.drawable.ic_format_align_left
}

@Preview(showBackground = true)
@Composable
private fun BottomToolbarIdlePreview() {
    MongsilTheme {
        BottomToolbar(isSaving = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomToolbarSavingPreview() {
    MongsilTheme {
        BottomToolbar(isSaving = true)
    }
}
