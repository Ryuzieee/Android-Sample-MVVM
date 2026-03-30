package com.yamamuto.android_sample_mvvm.domain.model

import com.yamamuto.android_sample_mvvm.ui.Strings

/** アプリ内で使用するエラー種別。 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Network(cause: Throwable) : AppException(Strings.Error.NETWORK_MESSAGE, cause)

    class Server(val code: Int, cause: Throwable) : AppException(Strings.Error.SERVER_ERROR_FORMAT.format(code), cause)

    class NotFound(query: String) : AppException(Strings.Error.notFound(query))

    class SessionExpired : AppException(Strings.Error.SESSION_EXPIRED)

    class ForceUpdate(
        val storeUrl: String,
    ) : AppException(Strings.Error.FORCE_UPDATE)

    class Unknown(cause: Throwable) : AppException(cause.message ?: Strings.Error.UNKNOWN_ERROR, cause)
}
