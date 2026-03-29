package com.yamamuto.android_sample_mvvm.network

import com.yamamuto.android_sample_mvvm.domain.model.AppEvent
import com.yamamuto.android_sample_mvvm.domain.model.AppEventBus
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val UNAUTHORIZED_CODE = 401

/**
 * セッション切れレスポンス（HTTP 401）を検知する Interceptor。
 *
 * バックエンドが 401 を返した場合、[AppEventBus] に [AppEvent.SessionExpired] を通知する。
 */
@Singleton
class SessionInterceptor
    @Inject
    constructor(
        private val appEventBus: AppEventBus,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            if (response.code == UNAUTHORIZED_CODE) {
                appEventBus.emit(AppEvent.SessionExpired)
            }
            return response
        }
    }
