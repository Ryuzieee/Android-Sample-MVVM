package com.yamamuto.android_sample_mvvm.network

import com.yamamuto.android_sample_mvvm.domain.model.AppEvent
import com.yamamuto.android_sample_mvvm.domain.model.AppEventBus
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

private const val FORCE_UPDATE_CODE = 426
private const val HEADER_STORE_URL = "X-Store-Url"
private const val DEFAULT_STORE_URL = "https://play.google.com/store"

/**
 * 強制アップデートレスポンス（HTTP 426）を検知する Interceptor。
 *
 * バックエンドが 426 を返した場合、[AppEventBus] に [AppEvent.ForceUpdate] を通知する。
 */
@Singleton
class ForceUpdateInterceptor
    @Inject
    constructor(
        private val appEventBus: AppEventBus,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            if (response.code == FORCE_UPDATE_CODE) {
                val storeUrl = response.header(HEADER_STORE_URL) ?: DEFAULT_STORE_URL
                appEventBus.emit(AppEvent.ForceUpdate(storeUrl))
            }
            return response
        }
    }
