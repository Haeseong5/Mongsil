package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.runtime.Composable

@Composable
actual fun rememberImagePickerLauncher(
    onImagesPicked: (List<String>) -> Unit
): () -> Unit = {
    // TODO: iOS 이미지 피커 연동
}
