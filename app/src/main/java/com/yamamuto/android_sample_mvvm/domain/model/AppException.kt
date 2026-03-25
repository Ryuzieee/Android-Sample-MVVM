package com.yamamuto.android_sample_mvvm.domain.model

/**
 * アプリ内で使用するエラー種別。
 *
 * ネットワークエラーとサーバーエラーを分類し、UIで適切なメッセージを表示するために使用する。
 */
sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    /** オフライン・タイムアウトなどのネットワーク接続エラー。 */
    class Network(cause: Throwable) : AppException("ネットワークに接続できません", cause)

    /** APIがエラーステータスを返した場合のサーバーエラー。 */
    class Server(val code: Int, cause: Throwable) : AppException("サーバーエラー ($code)", cause)

    /** 上記以外の予期しないエラー。 */
    class Unknown(cause: Throwable) : AppException(cause.message ?: "不明なエラーが発生しました", cause)
}
