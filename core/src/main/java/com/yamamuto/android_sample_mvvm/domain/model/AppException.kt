package com.yamamuto.android_sample_mvvm.domain.model

/**
 * アプリ内で使用するエラー種別。
 *
 * メッセージはドメイン層では持たず、UI 層で [Throwable] から表示文字列に変換する
 * (`core/ui/util/AppExceptionMessage.kt`)。
 */
sealed class AppException(cause: Throwable? = null) : Exception(cause) {
    class Network(cause: Throwable) : AppException(cause)

    class Server(val code: Int, cause: Throwable) : AppException(cause)

    class NotFound(val query: String) : AppException()

    class SessionExpired : AppException()

    class ForceUpdate(val storeUrl: String) : AppException()

    class Unknown(cause: Throwable) : AppException(cause)
}
