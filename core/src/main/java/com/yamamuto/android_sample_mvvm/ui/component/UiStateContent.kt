package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/**
 * [UiState] に応じて Idle / Loading / Error / Success を切り替える共通コンポーネント。
 *
 * 一度 Success でコンテンツが表示された後は、Loading や Error になっても
 * 前回のコンテンツを維持し、エラーはダイアログとしてオーバーレイ表示する。
 * 初回読み込み時（キャッシュなし）は従来通りフルスクリーンの Loading / Error を表示する。
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
            if (!errorDismissed) {
                ErrorDialog(
                    message = state.message,
                    onDismiss = { errorDismissed = true },
                    onRetry = onRetry,
                )
            }
        }

        // 初回 Loading（キャッシュなし）
        state is UiState.Loading -> LoadingIndicator(modifier = modifier)

        // 初回 Error（キャッシュなし）
        state is UiState.Error ->
            ErrorContent(
                message = state.message,
                onRetry = onRetry,
                isNetworkError = state.isNetworkError,
                modifier = modifier,
            )

        // Idle
        else -> idleContent()
    }
}
