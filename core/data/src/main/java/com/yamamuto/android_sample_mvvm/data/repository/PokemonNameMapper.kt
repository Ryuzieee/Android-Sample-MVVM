package com.yamamuto.android_sample_mvvm.data.repository

/** ポケモン名リストをクエリであいまい検索してフィルタする。 */
internal fun List<String>.toSearchResults(query: String): List<String> {
    return filter { it.contains(query.trim(), ignoreCase = true) }
}
