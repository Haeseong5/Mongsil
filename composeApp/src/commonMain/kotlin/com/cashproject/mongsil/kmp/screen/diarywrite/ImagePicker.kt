package com.cashproject.mongsil.kmp.screen.diarywrite

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(
    onImagesPicked: (List<String>) -> Unit
): () -> Unit
