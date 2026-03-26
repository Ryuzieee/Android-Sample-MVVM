package com.yamamuto.android_sample_mvvm.ui.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.UiState
import timber.log.Timber

/**
 * suspend ブロックを実行し、結果を [UiState] にマッピングする。
 *
 * - 成功時: [UiState.Success]
 * - 失敗時: [UiState.Error]（ネットワークエラー判定 + Timber ログ付き）
 *
 * ```kotlin
 * _uiState.value = loadAsUiState { useCase(name) }
 * ```
 */
suspend fun <T> loadAsUiState(block: suspend () -> T): UiState<T> =
    runCatching { block() }.fold(
        onSuccess = { UiState.Success(it) },
        onFailure = { e ->
            Timber.e(e)
            UiState.Error(
                message = e.message ?: "不明なエラーが発生しました",
                isNetworkError = e is AppException.Network,
            )
        },
    )
