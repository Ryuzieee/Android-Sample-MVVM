package com.yamamuto.android_sample_mvvm.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yamamuto.android_sample_mvvm.domain.model.UiState

/**
 * [UiState] に応じて Loading / Error / Success を切り替える共通コンポーネント。
 *
 * Success の中身だけ書けばよく、Loading と Error は自動で表示される。
 *
 * ```kotlin
 * UiStateContent(
 *     state = uiState.contentState,
 *     onRetry = viewModel::retry,
 *     modifier = Modifier.padding(padding),
 * ) { data ->
 *     DetailContent(detail = data)
 * }
 * ```
 */
@Composable
fun <T> UiStateContent(
    state: UiState<T>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    when (state) {
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
