package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 스넥바를 제어하는 컨트롤러
 */
class SnackbarController(
    val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    /**
     * 스넥바를 표시합니다.
     *
     * @param message 표시할 메시지
     * @param actionLabel 액션 버튼 라벨 (선택)
     * @param duration 표시 시간
     * @param onAction 액션 버튼 클릭 시 콜백
     */
    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onAction: (() -> Unit)? = null
    ) {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
            
            if (result == SnackbarResult.ActionPerformed && onAction != null) {
                onAction()
            }
        }
    }
}

/**
 * 전역 스넥바 컨트롤러를 제공하는 CompositionLocal
 */
val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("SnackbarController not provided")
}

/**
 * 현재 스넥바 컨트롤러를 가져옵니다.
 */
@Composable
fun rememberSnackbarController(): SnackbarController {
    return LocalSnackbarController.current
}
