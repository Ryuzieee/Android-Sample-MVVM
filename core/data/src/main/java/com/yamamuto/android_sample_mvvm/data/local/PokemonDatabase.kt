package com.yamamuto.android_sample_mvvm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yamamuto.android_sample_mvvm.data.local.dao.FavoriteDao
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.FavoriteEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonNameEntity

/** アプリの Room データベース。 */
@Database(
    entities = [PokemonDetailEntity::class, FavoriteEntity::class, PokemonNameEntity::class],
    version = 8,
    exportSchema = false,
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    abstract fun favoriteDao(): FavoriteDao
}
