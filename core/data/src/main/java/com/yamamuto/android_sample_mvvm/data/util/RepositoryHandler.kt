package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository メソッドの共通ハンドラ。
 *
 * 例外を [AppException] に変換して [Result] で返す。
 * キャッシュの期限チェックは [cachedAt] と [CACHE_DURATION_MS] で handler が一元管理する。
 * [load] と [fetch] は共に [E] を返し、[toModel] で [D] に変換する。
 *
 * ### キャッシュ付き
 * ```
 * repositoryHandler(
 *     forceRefresh = forceRefresh,
 *     load = { dao.getFoo(id) },
 *     fetch = { dataSource.getFoo(id).toEntity() },
 *     toModel = { it.toDomain() },
 *     cachedAt = { it.cachedAt },
 *     save = { dao.insertFoo(it) },
 * )
 * ```
 *
 * ### API のみ
 * ```
 * repositoryHandler(
 *     fetch = { dataSource.getFoo(id) },
 *     toModel = { it.toDomain() },
 * )
 * ```
 *
 * @param D ドメインモデル型（[Result] で返す型）
 * @param E ローカルキャッシュの Entity 型（[load] と [fetch] が共に返す型）
 */
suspend fun <D : Any, E : Any> repositoryHandler(
    forceRefresh: Boolean = false,
    load: suspend () -> E?,
    fetch: suspend () -> E,
    toModel: (E) -> D,
    cachedAt: ((E) -> Long)? = null,
    save: (suspend (E) -> Unit)? = null,
): Result<D> {
    return try {
        if (!forceRefresh) {
            val entity = load()
            if (entity != null && !isExpired(cachedAt?.invoke(entity))) {
                return Result.success(toModel(entity))
            }
        }
        val entity = fetch()
        save?.invoke(entity)
        Result.success(toModel(entity))
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

/** API のみの Repository メソッド用ハンドラ。キャッシュ版に委譲する。 */
suspend fun <D : Any, E : Any> repositoryHandler(
    fetch: suspend () -> E,
    toModel: (E) -> D,
): Result<D> {
    return repositoryHandler(
        load = { null },
        fetch = fetch,
        toModel = toModel,
    )
}

private fun isExpired(cachedAt: Long?): Boolean {
    if (cachedAt == null) return false
    return System.currentTimeMillis() - cachedAt >= CACHE_DURATION_MS
}
