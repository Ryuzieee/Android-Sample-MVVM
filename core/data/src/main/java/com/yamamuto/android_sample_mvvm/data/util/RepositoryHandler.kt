package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import retrofit2.HttpException
import java.io.IOException

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
 * ### キャッシュ付き
 * ```
 * repositoryHandler(
 *     forceRefresh = forceRefresh,
 *     local = { dao.getFoo(id)?.takeUnless { it.isExpired() }?.toDomain() },
 *     remote = { dataSource.getFoo(id).toDomain() },
 *     cache = { dao.insertFoo(it.toEntity()) },
 * )
 * ```
 */
suspend fun <T> repositoryHandler(
    forceRefresh: Boolean = false,
    local: (suspend () -> T?)? = null,
    remote: suspend () -> T,
    cache: (suspend (T) -> Unit)? = null,
): Result<T> {
    return try {
        if (!forceRefresh && local != null) {
            val cached = local()
            if (cached != null) {
                return Result.success(cached)
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
