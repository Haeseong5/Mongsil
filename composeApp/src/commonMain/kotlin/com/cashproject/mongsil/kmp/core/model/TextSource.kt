package com.cashproject.mongsil.kmp.core.model

import org.jetbrains.compose.resources.StringResource

sealed interface TextSource {
    data class Res(
        val stringResource: StringResource,
        val args: List<Any> = emptyList(),
    ) : TextSource

    data class Value(val text: String) : TextSource
}
