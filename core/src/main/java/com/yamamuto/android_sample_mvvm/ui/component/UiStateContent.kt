package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yamamuto.android_sample_mvvm.ui.util.AppErrorHandler
import com.yamamuto.android_sample_mvvm.ui.util.ErrorType
import com.yamamuto.android_sample_mvvm.ui.util.LocalAppErrorHandler
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/**
 * [UiState] に応じて Idle / Loading / Error / Success を切り替える共通コンポーネント。
 *
 * 一度 Success でコンテンツが表示された後は、Loading や Error になっても
 * 前回のコンテンツを維持し、エラーはダイアログとしてオーバーレイ表示する。
 * 初回読み込み時（キャッシュなし）は従来通りフルスクリーンの Loading / Error を表示する。
 *
 * セッション切れ・強制アップデートは [LocalAppErrorHandler] 経由で
 * 非閉じ可能なブロッキングダイアログを表示する。
 */
@Composable
fun <T> UiStateContent(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    idleContent: @Composable () -> Unit = {},
    content: @Composable (T) -> Unit,
) {
    var cachedData by remember { mutableStateOf<T?>(null) }
    var errorDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        errorDismissed = false
    }

    if (state is UiState.Success) {
        cachedData = state.data
    } else if (state is UiState.Idle) {
        cachedData = null
    }

    val cached = cachedData

    when {
        // Success → そのまま表示
        state is UiState.Success -> content(state.data)

        // キャッシュありの Loading → コンテンツ維持（pull-to-refresh インジケータに任せる）
        state is UiState.Loading && cached != null -> content(cached)

        // キャッシュありの Error → コンテンツ維持 + エラーダイアログ
        state is UiState.Error && cached != null -> {
            content(cached)
            ErrorOverlay(state = state, onRetry = onRetry, onDismiss = { errorDismissed = true })
        }

        // 初回 Loading（キャッシュなし）
        state is UiState.Loading -> LoadingIndicator(modifier = modifier)

        // 初回 Error（キャッシュなし）→ ブロッキングエラーはダイアログ、それ以外はフルスクリーン
        state is UiState.Error -> {
            if (state.type != ErrorType.General) {
                ErrorOverlay(state = state, onRetry = onRetry, onDismiss = {})
            } else {
                ErrorContent(
                    message = state.message,
                    onRetry = onRetry,
                    isNetworkError = state.isNetworkError,
                    modifier = modifier,
                )
            }
        }

        // Idle
        else -> idleContent()
    }
}

@Composable
private fun ErrorOverlay(
    state: UiState.Error,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    val errorHandler: AppErrorHandler = LocalAppErrorHandler.current

    when (state.type) {
        is ErrorType.SessionExpired -> SessionExpiredDialog(
            onConfirm = errorHandler.onSessionExpired,
        )
        is ErrorType.ForceUpdate -> ForceUpdateDialog(
            onConfirm = { errorHandler.onForceUpdate(state.type.storeUrl) },
        )
        is ErrorType.General -> ErrorDialog(
            message = state.message,
            onDismiss = onDismiss,
            onRetry = onRetry,
        )
    }
}
