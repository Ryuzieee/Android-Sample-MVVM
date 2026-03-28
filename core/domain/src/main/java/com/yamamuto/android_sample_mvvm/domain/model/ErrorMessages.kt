package com.yamamuto.android_sample_mvvm.domain.model

/** アプリ全体で使用するエラーメッセージ定数。 */
object ErrorMessages {
    const val NETWORK_ERROR = "ネットワークに接続できません"
    const val UNKNOWN_ERROR = "不明なエラーが発生しました"
    const val UNKNOWN_ERROR_EN = "Unknown error"

    fun notFound(query: String): String {
        return "「$query」に一致するポケモンは見つかりませんでした"
    }
}
