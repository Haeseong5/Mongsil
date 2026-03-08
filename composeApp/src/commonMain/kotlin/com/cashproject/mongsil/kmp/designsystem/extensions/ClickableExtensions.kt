package com.cashproject.mongsil.kmp.designsystem.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


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