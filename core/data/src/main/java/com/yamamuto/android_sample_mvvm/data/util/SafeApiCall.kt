package com.yamamuto.android_sample_mvvm.data.util

import com.yamamuto.android_sample_mvvm.domain.model.AppException
import retrofit2.HttpException
import java.io.IOException

/**
 * API 呼び出しを [Result] でラップするヘルパー。
 *
 * 例外を [AppException] に変換して [Result.failure] で返す。
 */
inline fun <T> safeApiCall(block: () -> T): Result<T> {
    return try {
        Result.success(block())
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
