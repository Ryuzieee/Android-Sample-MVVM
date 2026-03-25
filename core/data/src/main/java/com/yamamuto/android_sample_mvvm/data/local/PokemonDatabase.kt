package com.yamamuto.android_sample_mvvm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yamamuto.android_sample_mvvm.data.local.dao.PokemonDao
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity

/** アプリの Room データベース。 */
@Database(
    entities = [PokemonDetailEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
