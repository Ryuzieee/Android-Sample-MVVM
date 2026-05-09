package com.yamamuto.android_sample_mvvm.di

import com.yamamuto.android_sample_mvvm.network.ApiHeaderInterceptor
import com.yamamuto.android_sample_mvvm.network.mock.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * mock フレーバー用の OkHttpClient を提供する。
 *
 * MockInterceptor を組み込み、API レスポンスをアプリ内のモックデータで置き換える。
 * このモジュールは mock ソースセットに置かれているため、dev / prod ビルドには
 * MockInterceptor / モックデータ自体が含まれない。
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiHeaderInterceptor: ApiHeaderInterceptor,
        mockInterceptor: MockInterceptor,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(apiHeaderInterceptor)
            .addInterceptor(mockInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }
}
