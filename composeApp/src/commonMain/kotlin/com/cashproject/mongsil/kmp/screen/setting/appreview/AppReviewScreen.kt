package com.cashproject.mongsil.kmp.screen.setting.appreview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.setting_app_review
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppReviewScreen(
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.setting_app_review),
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}
