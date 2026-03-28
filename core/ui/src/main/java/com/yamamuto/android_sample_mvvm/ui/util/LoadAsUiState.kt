package com.yamamuto.android_sample_mvvm.ui.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.domain.model.ErrorMessages
import timber.log.Timber

/**
 * [Result] を [UiState] に変換する拡張関数。
 *
 * - 成功時: [UiState.Success]
 * - 失敗時: [UiState.Error]（ネットワークエラー判定 + Timber ログ付き）
 */
fun <T> Result<T>.toUiState(): UiState<T> {
    return fold(
        onSuccess = { UiState.Success(it) },
        onFailure = { e ->
            Timber.e(e)
            UiState.Error(
                message = e.message ?: ErrorMessages.UNKNOWN_ERROR,
                isNetworkError = e is AppException.Network,
            )
        },
    )
}
