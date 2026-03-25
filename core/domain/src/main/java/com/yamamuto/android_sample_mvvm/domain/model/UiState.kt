package com.yamamuto.android_sample_mvvm.domain.model

/**
 * 画面の UI 状態を表す汎用 sealed interface。
 *
 * 30+ 画面に増えても各画面で Loading / Success / Error を繰り返し定義する必要がない。
 * 画面固有の状態が必要な場合は [Success.data] の型パラメータ [T] で表現する。
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>

    data class Error(
        val message: String,
        val isNetworkError: Boolean = false,
    ) : UiState<Nothing>
}
