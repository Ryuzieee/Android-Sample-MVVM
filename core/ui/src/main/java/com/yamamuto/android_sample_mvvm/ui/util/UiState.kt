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

/** Success のデータを返す。それ以外は null。 */
fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data

/** Success のデータを変換する。それ以外はそのまま返す。 */
fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Success -> UiState.Success(transform(data))
    is UiState.Error -> this
    is UiState.Loading -> this
    is UiState.Idle -> this
}

val UiState<*>.isLoading: Boolean get() = this is UiState.Loading
val UiState<*>.isSuccess: Boolean get() = this is UiState.Success
val UiState<*>.isError: Boolean get() = this is UiState.Error
