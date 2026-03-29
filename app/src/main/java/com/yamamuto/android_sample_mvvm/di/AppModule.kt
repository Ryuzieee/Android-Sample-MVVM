package com.yamamuto.android_sample_mvvm.di

import com.yamamuto.android_sample_mvvm.BuildConfig
import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.network.ApiHeaderInterceptor
import com.yamamuto.android_sample_mvvm.network.mock.MockInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val CONTENT_TYPE_JSON = "application/json"

/** ネットワーク層の依存関係を定義する Hilt モジュール。 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiHeaderInterceptor: ApiHeaderInterceptor,
        mockInterceptor: MockInterceptor,
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(apiHeaderInterceptor)
            .apply {
                if (BuildConfig.IS_MOCK) {
                    addInterceptor(mockInterceptor)
                }
            }.addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC },
            ).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json,
    ): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(CONTENT_TYPE_JSON.toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun providePokeApiService(retrofit: Retrofit): PokeApiService {
        return retrofit.create(PokeApiService::class.java)
    }
}
