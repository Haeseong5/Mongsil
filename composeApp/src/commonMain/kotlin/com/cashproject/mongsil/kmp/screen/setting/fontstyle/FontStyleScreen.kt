package com.cashproject.mongsil.kmp.screen.setting.fontstyle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cashproject.mongsil.kmp.designsystem.Gray300
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.model.FontStyleOption
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FontStyleScreen(
    padding: PaddingValues = PaddingValues(),
    onBack: () -> Unit = {},
    viewModel: FontStyleViewModel = koinViewModel(),
) {
    val selectedFontStyle by viewModel.selectedFontStyle.collectAsStateWithLifecycle()
    val fontScale by viewModel.fontScale.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MongsilTheme.colorScheme.background)
            .padding(padding)
    ) {
        CommonToolbar(
            onBack = onBack,
            title = "글자 스타일"
        )

        SettingSectionTitle(text = "폰트")
        FontOptionItem(
            label = "감자꽃체",
            selected = selectedFontStyle == FontStyleOption.GAMJA_FLOWER,
            onClick = { viewModel.updateFontStyle(FontStyleOption.GAMJA_FLOWER) }
        )

        SettingSectionTitle(text = "글자 크기")
        TextSizeControl(
            fontScale = fontScale,
            onScaleChange = viewModel::updateFontScale
        )
    }
}

@Composable
private fun SettingSectionTitle(text: String) {
    Text(
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 8.dp),
        text = text,
        style = MongsilTheme.typography.body1Medium,
        color = MongsilTheme.colorScheme.labelStrong
    )
}

@Composable
private fun FontOptionItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MongsilTheme.typography.default,
            color = MongsilTheme.colorScheme.labelStrong
        )
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MongsilTheme.colorScheme.labelStrong,
                unselectedColor = Gray300
            )
        )
    }
}

@Composable
private fun TextSizeControl(
    fontScale: Float,
    onScaleChange: (Float) -> Unit,
) {
    val minScale = FontStyleViewModel.MIN_FONT_SCALE
    val maxScale = FontStyleViewModel.MAX_FONT_SCALE
    val displayPercent = (fontScale * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = "현재 크기: ${displayPercent}%",
            style = MongsilTheme.typography.body2Medium,
            color = MongsilTheme.colorScheme.labelWeak
        )

        Slider(
            value = fontScale,
            onValueChange = onScaleChange,
            valueRange = minScale..maxScale,
            steps = 11
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "작게",
                style = MongsilTheme.typography.caption1,
                color = MongsilTheme.colorScheme.labelWeak
            )
            Text(
                text = "미리보기",
                style = MongsilTheme.typography.body1Medium,
                color = MongsilTheme.colorScheme.labelStrong
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "크게",
                style = MongsilTheme.typography.caption1,
                color = MongsilTheme.colorScheme.labelWeak
            )
        }
    }
}
