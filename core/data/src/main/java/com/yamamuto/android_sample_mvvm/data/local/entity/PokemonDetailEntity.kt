package com.yamamuto.android_sample_mvvm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/** ポケモン詳細のキャッシュ用 Entity。 */
@Entity(tableName = "pokemon_detail")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val types: List<String>,
    val abilities: List<AbilityEntry>,
    val imageUrl: String,
    val stats: List<StatEntry>,
    val cachedAt: Long = System.currentTimeMillis(),
)

@Serializable
data class AbilityEntry(
    val name: String,
    val japaneseName: String = "",
    val isHidden: Boolean,
)

@Serializable
data class StatEntry(
    val name: String,
    val value: Int,
)
