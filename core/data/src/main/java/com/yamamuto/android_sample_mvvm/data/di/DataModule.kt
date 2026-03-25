package com.yamamuto.android_sample_mvvm.data.di

import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.repository.PokemonRepositoryImpl
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * データ層の依存関係を定義する Hilt モジュール。
 *
 * DataSource と Repository のインスタンス生成を管理する。
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun providePokemonRemoteDataSource(api: PokeApiService): PokemonRemoteDataSource =
        PokemonRemoteDataSource(api)

    @Provides
    @Singleton
    fun providePokemonRepository(dataSource: PokemonRemoteDataSource): PokemonRepository =
        PokemonRepositoryImpl(dataSource)
}
