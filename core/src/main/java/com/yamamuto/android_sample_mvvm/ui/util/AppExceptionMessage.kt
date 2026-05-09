package com.yamamuto.android_sample_mvvm.ui.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import com.yamamuto.android_sample_mvvm.ui.Strings

/**
 * [Throwable] をユーザー向け表示文字列に変換する。
 *
 * AppException は文字列を持たず、UI 層でこの拡張関数を通して表示用の文言を解決する
 * (ドメイン層が UI 文字列に依存しないようにするため)。
 */
fun Throwable.toUserMessage(): String {
    return when (this) {
        is AppException.Network -> Strings.Error.NETWORK_MESSAGE
        is AppException.Server -> Strings.Error.SERVER_ERROR_FORMAT.format(code)
        is AppException.NotFound -> Strings.Error.notFound(query)
        is AppException.SessionExpired -> Strings.Error.SESSION_EXPIRED
        is AppException.ForceUpdate -> Strings.Error.FORCE_UPDATE
        is AppException.Unknown -> message ?: Strings.Error.UNKNOWN_ERROR
        else -> message ?: Strings.Error.UNKNOWN_ERROR
    }
}
