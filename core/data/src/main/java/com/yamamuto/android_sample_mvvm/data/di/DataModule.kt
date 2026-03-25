package com.yamamuto.android_sample_mvvm.data.di

import android.content.Context
import androidx.room.Room
import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.PokemonDatabase
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.repository.PokemonRepositoryImpl
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * データ層の依存関係を定義する Hilt モジュール。
 *
 * Room / DataSource / Repository のインスタンス生成を管理する。
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): PokemonDatabase =
        Room.databaseBuilder(context, PokemonDatabase::class.java, "pokemon.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase): PokemonDao = database.pokemonDao()

    @Provides
    @Singleton
    fun providePokemonRemoteDataSource(api: PokeApiService): PokemonRemoteDataSource = PokemonRemoteDataSource(api)

    @Provides
    @Singleton
    fun providePokemonRepository(
        dataSource: PokemonRemoteDataSource,
        dao: PokemonDao,
    ): PokemonRepository = PokemonRepositoryImpl(dataSource, dao)
}
