package com.yamamuto.android_sample_mvvm.data.di

import android.content.Context
import androidx.room.Room
import com.yamamuto.android_sample_mvvm.data.api.PokeApiService
import com.yamamuto.android_sample_mvvm.data.datasource.PokemonRemoteDataSource
import com.yamamuto.android_sample_mvvm.data.local.PokemonDatabase
import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.repository.FavoriteRepositoryImpl
import com.yamamuto.android_sample_mvvm.data.repository.PokemonRepositoryImpl
import com.yamamuto.android_sample_mvvm.domain.repository.FavoriteRepository
import com.yamamuto.android_sample_mvvm.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "pokemon.db"

/** データ層の依存関係を定義する Hilt モジュール。 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): PokemonDatabase {
        return Room
            .databaseBuilder(context, PokemonDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: PokemonDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun providePokemonRemoteDataSource(api: PokeApiService): PokemonRemoteDataSource {
        return PokemonRemoteDataSource(api)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(
        dataSource: PokemonRemoteDataSource,
        dao: PokemonDao,
    ): PokemonRepository {
        return PokemonRepositoryImpl(dataSource, dao)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(dao: FavoriteDao): FavoriteRepository {
        return FavoriteRepositoryImpl(dao)
    }
}
