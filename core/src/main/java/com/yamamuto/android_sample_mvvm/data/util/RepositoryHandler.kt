package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import retrofit2.HttpException
import java.io.IOException

private const val UNAUTHORIZED_CODE = 401
private const val FORCE_UPDATE_CODE = 426
private const val HEADER_STORE_URL = "X-Store-Url"
private const val DEFAULT_STORE_URL = "https://play.google.com/store"

/** キャッシュ付き Repository メソッドの共通ハンドラ。例外を [AppException] に変換して [Result] で返す。 */
suspend fun <D : Any, E : Any, R : Any> handleWithCache(
    forceRefresh: Boolean = false,
    load: suspend () -> E?,
    fetch: suspend () -> R,
    toEntity: (R) -> E,
    toModel: (E) -> D,
    cachedAt: ((E) -> Long)? = null,
    save: (suspend (E) -> Unit)? = null,
): Result<D> {
    return appRunCatching {
        if (!forceRefresh) {
            val entity = load()
            if (entity != null && !isExpired(cachedAt?.invoke(entity))) {
                return@appRunCatching toModel(entity)
            }
        }
        val raw = fetch()
        val entity = toEntity(raw)
        save?.invoke(entity)
        toModel(entity)
    }
}

/** API のみの Repository メソッド用ハンドラ。キャッシュなし。 */
suspend fun <D : Any, R : Any> handleRemote(
    fetch: suspend () -> R,
    toModel: (R) -> D,
): Result<D> {
    return appRunCatching {
        toModel(fetch())
    }
}

/** ローカル DB のみの Repository メソッド用ハンドラ。 */
suspend fun <D : Any, E : Any> handleLocal(
    query: suspend () -> E,
    toModel: (E) -> D,
): Result<D> {
    return try {
        Result.success(toModel(query()))
    } catch (e: Exception) {
        Result.failure(AppException.Unknown(e))
    }
}

private inline fun <D> appRunCatching(block: () -> D): Result<D> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e.toAppException())
    }
}

fun Exception.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is IOException -> AppException.Network(this)
        is HttpException ->
            when (code()) {
                UNAUTHORIZED_CODE -> AppException.SessionExpired()
                FORCE_UPDATE_CODE -> {
                    val storeUrl = response()?.raw()?.header(HEADER_STORE_URL) ?: DEFAULT_STORE_URL
                    AppException.ForceUpdate(storeUrl)
                }
                else -> AppException.Server(code(), this)
            }
        else -> AppException.Unknown(this)
    }
}

private fun isExpired(cachedAt: Long?): Boolean {
    if (cachedAt == null) return false
    return System.currentTimeMillis() - cachedAt >= CACHE_DURATION_MS
}
