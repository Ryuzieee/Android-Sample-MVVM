package com.yamamuto.android_sample_mvvm.ui.util

import androidx.paging.LoadState
import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.UiState

/**
 * [LoadState] を [UiState] に変換する。
 *
 * Paging の refresh 状態を UiStateContent に渡すときに使う。
 *
 * ```kotlin
 * UiStateContent(
 *     state = pagingItems.loadState.refresh.asUiState { pagingItems },
 *     onRetry = { pagingItems.retry() },
 * ) { items -> ... }
 * ```
 */
fun <T> LoadState.asUiState(onNotLoading: () -> T): UiState<T> = when (this) {
    is LoadState.Loading -> UiState.Loading
    is LoadState.Error -> UiState.Error(
        message = error.message ?: "Unknown error",
        isNetworkError = error is AppException.Network,
    )
    is LoadState.NotLoading -> UiState.Success(onNotLoading())
}
