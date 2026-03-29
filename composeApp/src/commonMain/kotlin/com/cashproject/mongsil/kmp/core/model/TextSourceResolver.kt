package com.cashproject.mongsil.kmp.core.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun TextSource.asString(): String = when (this) {
    is TextSource.Res -> stringResource(stringResource, *args.toTypedArray())
    is TextSource.Value -> text
}

suspend fun TextSource.resolve(): String = when (this) {
    is TextSource.Res -> getString(stringResource, *args.toTypedArray())
    is TextSource.Value -> text
}
