package com.yamamuto.android_sample_mvvm.di

import com.yamamuto.android_sample_mvvm.network.ApiHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * dev / prod フレーバー用の OkHttpClient を提供する。
 *
 * mock フレーバーでは `app/src/mock/` 側の [com.yamamuto.android_sample_mvvm.di.NetworkModule]
 * が代わりに評価されるため、`MockInterceptor` などモックコードは APK にバンドルされない。
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(apiHeaderInterceptor: ApiHeaderInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(apiHeaderInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }
}
