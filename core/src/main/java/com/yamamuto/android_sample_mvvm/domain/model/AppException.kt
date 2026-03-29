package com.yamamuto.android_sample_mvvm.domain.model

import com.yamamuto.android_sample_mvvm.ui.Strings

/**
 * アプリ内で使用するエラー種別。
 *
 * ネットワークエラーとサーバーエラーを分類し、UIで適切なメッセージを表示するために使用する。
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    /** オフライン・タイムアウトなどのネットワーク接続エラー。 */
    class Network(cause: Throwable) : AppException(Strings.Error.NETWORK_MESSAGE, cause)

    /** APIがエラーステータスを返した場合のサーバーエラー。 */
    class Server(val code: Int, cause: Throwable) : AppException(Strings.Error.SERVER_ERROR_FORMAT.format(code), cause)

    /** 検索結果が見つからなかった場合のエラー。 */
    class NotFound(query: String) : AppException(Strings.Error.notFound(query))

    /** セッションの有効期限切れエラー。再ログインが必要。 */
    class SessionExpired : AppException(Strings.Error.SESSION_EXPIRED)

    /** アプリの強制アップデートが必要なエラー。 */
    class ForceUpdate(
        val storeUrl: String,
    ) : AppException(Strings.Error.FORCE_UPDATE)

    /** 上記以外の予期しないエラー。 */
    class Unknown(cause: Throwable) : AppException(cause.message ?: Strings.Error.UNKNOWN_ERROR, cause)
}
