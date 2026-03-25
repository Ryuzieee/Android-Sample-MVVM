package com.yamamuto.android_sample_mvvm.di

import com.yamamuto.android_sample_mvvm.BuildConfig
import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.repository.PokemonRepositoryImpl
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
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

/**
 * アプリ全体のスコープ（[SingletonComponent]）で提供する依存関係を定義する Hilt モジュール。
 *
 * ネットワーク層（OkHttp / Retrofit）とデータ層（DataSource / Repository）の
 * インスタンス生成をまとめて管理する。
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC },
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun providePokeApiService(retrofit: Retrofit): PokeApiService = retrofit.create(PokeApiService::class.java)

    @Provides
    @Singleton
    fun providePokemonRemoteDataSource(api: PokeApiService): PokemonRemoteDataSource = PokemonRemoteDataSource(api)

    @Provides
    @Singleton
    fun providePokemonRepository(dataSource: PokemonRemoteDataSource): PokemonRepository = PokemonRepositoryImpl(dataSource)
}
