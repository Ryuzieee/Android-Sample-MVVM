package com.yamamuto.android_sample_mvvm.ui.util

/** 画面の UI 状態を表す汎用 sealed interface。 */
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>

    data object Loading : UiState<Nothing>

    data class Success<T>(val data: T) : UiState<T>

    data class Error(
        val message: String,
        val isNetworkError: Boolean = false,
        val type: ErrorType = ErrorType.General,
    ) : UiState<Nothing>
}

/** [UiState.Error] の種別。セッション切れ・強制アップデートはブロッキングダイアログで表示する。 */
sealed interface ErrorType {
    data object General : ErrorType
    data object SessionExpired : ErrorType
    data class ForceUpdate(val storeUrl: String) : ErrorType
}

/** Success のデータを返す。それ以外は null。 */
fun <T> UiState<T>.getOrNull(): T? {
    return (this as? UiState.Success)?.data
}
