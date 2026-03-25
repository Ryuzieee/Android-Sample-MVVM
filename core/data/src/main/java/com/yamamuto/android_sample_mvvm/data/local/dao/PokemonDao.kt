package com.yamamuto.android_sample_mvvm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity

/** ポケモンデータのキャッシュ操作を行う DAO。 */
@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_detail WHERE name = :name")
    suspend fun getPokemonDetail(name: String): PokemonDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(detail: PokemonDetailEntity)
}
