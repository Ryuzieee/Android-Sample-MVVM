package com.yamamuto.android_sample_mvvm.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 全 API リクエストに共通ヘッダーを付与する Interceptor。
 *
 * 認証トークンや API バージョンなど、リクエストごとに必要なヘッダーをここで一元管理する。
 */
@Singleton
class ApiHeaderInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain
                .request()
                .newBuilder()
                // TODO: 認証トークンを付与する（例: TokenProvider から取得）
                // .header("Authorization", "Bearer ${tokenProvider.accessToken}")
                .header("Accept", "application/json")
                .build()
        return chain.proceed(request)
    }
}
