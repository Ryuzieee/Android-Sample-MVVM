package com.yamamuto.android_sample_mvvm.data.util

/**
 * キャッシュ付き API 呼び出しの共通パターン。
 *
 * [forceRefresh] が false の場合、[fromCache] でキャッシュを確認し、
 * 有効なデータがあればそれを返す。なければ [fromNetwork] で取得して [saveToCache] で保存する。
 */
suspend inline fun <T> cachedApiCall(
    forceRefresh: Boolean = false,
    crossinline fromCache: suspend () -> T?,
    crossinline fromNetwork: suspend () -> T,
    crossinline saveToCache: suspend (T) -> Unit,
): T {
    if (!forceRefresh) {
        fromCache()?.let { return it }
    }
    return fromNetwork().also { saveToCache(it) }
}
