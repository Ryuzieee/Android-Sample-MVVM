package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import retrofit2.HttpException
import java.io.IOException

/**
 * API 呼び出しを [AppException] でラップするヘルパー。
 *
 * 新しい Repository で毎回 try-catch を書く必要がなくなる。
 *
 * ```kotlin
 * override suspend fun getItems(): List<Item> = safeApiCall {
 *     dataSource.fetchItems().map { it.toDomain() }
 * }
 * ```
 */
inline fun <T> safeApiCall(block: () -> T): T =
    try {
        block()
    } catch (e: IOException) {
        throw AppException.Network(e)
    } catch (e: HttpException) {
        throw AppException.Server(e.code(), e)
    } catch (e: AppException) {
        throw e
    } catch (e: Exception) {
        throw AppException.Unknown(e)
    }
