package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yamamuto.android_sample_mvvm.ui.util.UiState

/**
 * [UiState] に応じて Idle / Loading / Error / Success を切り替える共通コンポーネント。
 *
 * Success の中身だけ書けばよく、Loading と Error は自動で表示される。
 * Idle 状態を使う場合は [idleContent] を渡す。
 */
@Composable
fun <T> UiStateContent(
    state: UiState<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    idleContent: @Composable () -> Unit = {},
    content: @Composable (T) -> Unit,
) {
    when (state) {
        is UiState.Idle -> idleContent()

        is UiState.Loading -> LoadingIndicator(modifier = modifier)

        is UiState.Error ->
            ErrorContent(
                message = state.message,
                onRetry = onRetry,
                isNetworkError = state.isNetworkError,
                modifier = modifier,
            )

        is UiState.Success -> content(state.data)
    }
}
