package com.yamamuto.android_sample_mvvm.domain.util

/** PokeAPI で使用する言語コード定数。 */
object LanguageCodes {
    const val JA = "ja"
    const val JA_HRKT = "ja-hrkt"
    const val EN = "en"

    /** Map から日本語名を JA → JA_HRKT のフォールバック順で取得する。 */
    fun Map<String, String>.japaneseName(fallback: String = ""): String {
        return this[JA] ?: this[JA_HRKT] ?: fallback
    }
}
