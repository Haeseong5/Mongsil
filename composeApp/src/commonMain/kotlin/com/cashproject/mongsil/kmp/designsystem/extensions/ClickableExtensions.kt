package com.cashproject.mongsil.kmp.designsystem.extensions

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


fun Modifier.circularRippleClickable(
    radius: Dp = 24.dp,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {
    this.clickable(
        indication = ripple(
            bounded = false,   // ← 핵심! false 면 원형으로 퍼짐
            radius = radius
        ),
        interactionSource = remember { MutableInteractionSource() },
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

@OptIn(ExperimentalTime::class)
fun Modifier.debounceClickable(
    debounceTime: Long = 500L,
    isRipple: Boolean = false,
    onClick: () -> Unit
): Modifier = composed {
    val lastClickTimeRef = remember { mutableLongStateOf(0L) }
    val interactionSource = remember { MutableInteractionSource() }

    this.clickable(
        interactionSource = interactionSource,
        indication = if (isRipple) LocalIndication.current else null
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        if (now - lastClickTimeRef.longValue > debounceTime) {
            lastClickTimeRef.longValue = now
            onClick()
        }
    }
}
