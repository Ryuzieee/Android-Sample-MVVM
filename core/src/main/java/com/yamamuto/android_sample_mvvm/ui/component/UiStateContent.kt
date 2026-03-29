package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yamamuto.android_sample_mvvm.ui.util.ErrorType
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/**
 * [UiState] に応じて Idle / Loading / Error / Success を切り替える共通コンポーネント。
 *
 * 一度 Success でコンテンツが表示された後は、Loading や Error になっても
 * 前回のコンテンツを維持し、エラーはダイアログとしてオーバーレイ表示する。
 * 初回読み込み時（キャッシュなし）は従来通りフルスクリーンの Loading / Error を表示する。
 *
 * セッション切れ・強制アップデートは閉じられないブロッキングダイアログを表示する。
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
        state is UiState.Success -> content(state.data)

        state is UiState.Loading && cached != null -> content(cached)

        state is UiState.Error && cached != null -> {
            content(cached)
            if (!errorDismissed) {
                ErrorOverlay(state = state, onRetry = onRetry, onDismiss = { errorDismissed = true })
            }
        }

        state is UiState.Loading -> LoadingIndicator(modifier = modifier)

        state is UiState.Error -> {
            when (state.type) {
                is ErrorType.General, is ErrorType.Network ->
                    ErrorContent(
                        message = state.message,
                        onRetry = onRetry,
                        errorType = state.type,
                        modifier = modifier,
                    )
                else -> ErrorOverlay(state = state, onRetry = onRetry, onDismiss = {})
            }
        }

        else -> idleContent()
    }
}

@Composable
private fun ErrorOverlay(
    state: UiState.Error,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    when (state.type) {
        is ErrorType.SessionExpired -> SessionExpiredDialog()
        is ErrorType.ForceUpdate -> ForceUpdateDialog(storeUrl = state.type.storeUrl)
        is ErrorType.General, is ErrorType.Network -> ErrorDialog(
            message = state.message,
            onDismiss = onDismiss,
            onRetry = onRetry,
        )
    }
}
