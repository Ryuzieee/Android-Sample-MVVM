package com.yamamuto.android_sample_mvvm.ui.util

import androidx.paging.LoadState
import com.yamamuto.android_sample_mvvm.ui.Strings

/**
 * [LoadState] を [UiState] に変換する。
 *
 * Paging の refresh 状態を UiStateContent に渡すときに使う。
 */
fun <T> LoadState.asUiState(onNotLoading: () -> T): UiState<T> {
    return when (this) {
        is LoadState.Loading -> UiState.Loading
        is LoadState.Error ->
            UiState.Error(
                message = error.message ?: Strings.Error.UNKNOWN_ERROR,
                type = error.toErrorType(),
            )
        is LoadState.NotLoading -> UiState.Success(onNotLoading())
    }
}
