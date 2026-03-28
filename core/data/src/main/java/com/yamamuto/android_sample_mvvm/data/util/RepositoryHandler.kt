package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import retrofit2.HttpException
import java.io.IOException

/**
 * キャッシュデータのラッパー。
 *
 * [cachedAt] を指定すると [CACHE_DURATION_MS] に基づく期限チェックが行われる。
 * 省略した場合は常に有効（期限なし）として扱われる。
 */
data class Cached<T>(val data: T, val cachedAt: Long? = null)

/**
 * Repository メソッドの共通ハンドラ。
 *
 * 例外を [AppException] に変換して [Result] で返す。
 * [local] / [cache] を指定するとキャッシュ付きパターンになる。
 *
 * ### API のみ
 * ```
 * repositoryHandler(
 *     remote = { dataSource.getFoo(id).toDomain() },
 * )
 * ```
 *
 * ### キャッシュ付き（期限あり）
 * ```
 * repositoryHandler(
 *     forceRefresh = forceRefresh,
 *     local = { dao.getFoo(id)?.let { Cached(it.toDomain(), it.cachedAt) } },
 *     remote = { dataSource.getFoo(id).toDomain() },
 *     cache = { dao.insertFoo(it.toEntity()) },
 * )
 * ```
 *
 * ### キャッシュ付き（期限なし）
 * ```
 * repositoryHandler(
 *     local = { dao.getAll().takeIf { it.isNotEmpty() }?.let { Cached(it) } },
 *     remote = { dataSource.getAll() },
 *     cache = { dao.insertAll(it) },
 * )
 * ```
 */
suspend fun <T> repositoryHandler(
    forceRefresh: Boolean = false,
    local: (suspend () -> Cached<T>?)? = null,
    remote: suspend () -> T,
    cache: (suspend (T) -> Unit)? = null,
): Result<T> {
    return try {
        if (!forceRefresh && local != null) {
            val cached = local()
            if (cached != null && !isExpired(cached.cachedAt)) {
                return Result.success(cached.data)
            }
        }
        val result = remote()
        cache?.invoke(result)
        Result.success(result)
    } catch (e: IOException) {
        Result.failure(AppException.Network(e))
    } catch (e: HttpException) {
        Result.failure(AppException.Server(e.code(), e))
    } catch (e: AppException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(AppException.Unknown(e))
    }
}

private fun isExpired(cachedAt: Long?): Boolean {
    if (cachedAt == null) return false
    return System.currentTimeMillis() - cachedAt >= CACHE_DURATION_MS
}
