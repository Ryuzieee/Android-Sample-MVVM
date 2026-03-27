package com.yamamuto.android_sample_mvvm.ui.util

/** 画面の UI 状態を表す汎用 sealed interface。 */
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>

    data object Loading : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>

    data class Error(
        val message: String,
        val isNetworkError: Boolean = false,
    ) : UiState<Nothing>
}
