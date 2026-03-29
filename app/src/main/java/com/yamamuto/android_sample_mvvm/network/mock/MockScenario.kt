package com.yamamuto.android_sample_mvvm.network.mock

/**
 * モックレスポンスのシナリオ。
 *
 * [MockInterceptor] が返すレスポンスを切り替えるために使用する。
 * プリセットに加え、[CustomError] で任意の HTTP ステータス・ヘッダー・ボディを指定可能。
 */
sealed interface MockScenario {
    /** 正常系レスポンスを返す。 */
    data object Success : MockScenario

    /** 全 API でネットワークエラー（タイムアウト）をシミュレートする。 */
    data object NetworkError : MockScenario

    /** 任意の HTTP エラーレスポンスを返す。 */
    data class CustomError(
        val code: Int,
        val message: String = "Error",
        val body: String = """{"error": "$message"}""",
        val headers: Map<String, String> = emptyMap(),
    ) : MockScenario

    companion object {
        /** プリセット: 401 Unauthorized（セッション切れ） */
        val SESSION_EXPIRED = CustomError(code = 401, message = "Unauthorized")

        /** プリセット: 426 Upgrade Required（強制アップデート） */
        val FORCE_UPDATE =
            CustomError(
                code = 426,
                message = "Upgrade Required",
                headers = mapOf("X-Store-Url" to "https://play.google.com/store/apps/details?id=com.example.app"),
            )

        /** プリセット: 500 Internal Server Error */
        val SERVER_ERROR = CustomError(code = 500, message = "Internal Server Error")

        /** プリセット: 403 Forbidden */
        val FORBIDDEN = CustomError(code = 403, message = "Forbidden")

        /** プリセット: 404 Not Found */
        val NOT_FOUND = CustomError(code = 404, message = "Not Found")

        /** プリセット: 429 Too Many Requests */
        val RATE_LIMITED = CustomError(code = 429, message = "Too Many Requests")

        /** プリセット: 503 Service Unavailable */
        val MAINTENANCE = CustomError(code = 503, message = "Service Unavailable")

        /** ボトムシートに表示するプリセット一覧。 */
        val presets: List<Pair<String, MockScenario>> =
            listOf(
                "正常系レスポンス" to Success,
                "セッション切れ (401)" to SESSION_EXPIRED,
                "強制アップデート (426)" to FORCE_UPDATE,
                "権限なし (403)" to FORBIDDEN,
                "Not Found (404)" to NOT_FOUND,
                "レート制限 (429)" to RATE_LIMITED,
                "サーバーエラー (500)" to SERVER_ERROR,
                "メンテナンス (503)" to MAINTENANCE,
                "ネットワークエラー" to NetworkError,
            )
    }
}

/** 現在のモックシナリオを保持するホルダー。 */
object MockScenarioHolder {
    @Volatile
    var current: MockScenario = MockScenario.Success
}
