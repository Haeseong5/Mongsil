package com.cashproject.mongsil.kmp.screen.setting.fontstyle.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 8.dp),
        text = text,
        style = MongsilTheme.typography.body1Medium,
        color = MongsilTheme.colorScheme.labelStrong
    )
}

@Preview
@Composable
private fun SettingSectionTitlePreview() {
    MongsilTheme {
        SettingSectionTitle(text = "폰트")
    }
}
