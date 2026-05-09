package com.yamamuto.android_sample_mvvm.ui.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import timber.log.Timber

/** [Result] を [UiState] に変換する拡張関数。 */
fun <T> Result<T>.toUiState(): UiState<T> {
    return fold(
        onSuccess = { UiState.Success(it) },
        onFailure = { e ->
            Timber.e(e)
            UiState.Error(type = e.toErrorType())
        },
    )
}

fun Throwable.toErrorType(): ErrorType {
    return when (this) {
        is AppException.Network -> ErrorType.Network
        is AppException.SessionExpired -> ErrorType.SessionExpired
        is AppException.ForceUpdate -> ErrorType.ForceUpdate(storeUrl)
        is AppException.NotFound -> ErrorType.NotFound(query)
        is AppException.Server -> ErrorType.Server(code)
        is AppException.Unknown -> ErrorType.Unknown(cause?.message)
        else -> ErrorType.Unknown(message)
    }
}
