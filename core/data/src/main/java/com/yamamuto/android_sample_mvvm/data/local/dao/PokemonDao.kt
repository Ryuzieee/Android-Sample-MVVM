package com.yamamuto.android_sample_mvvm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonDetailEntity
import com.yamamuto.android_sample_mvvm.data.local.entity.PokemonEntity

/** ポケモンデータのキャッシュ操作を行う DAO。 */
@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon ORDER BY rowid")
    suspend fun getAllPokemon(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_detail WHERE name = :name")
    suspend fun getPokemonDetail(name: String): PokemonDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(detail: PokemonDetailEntity)

    @Query("DELETE FROM pokemon")
    suspend fun clearAllPokemon()

    @Query("DELETE FROM pokemon_detail")
    suspend fun clearAllDetails()
}
