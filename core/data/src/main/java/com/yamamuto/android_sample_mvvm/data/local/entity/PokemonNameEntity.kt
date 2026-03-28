package com.yamamuto.android_sample_mvvm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** ポケモン名の検索用キャッシュ Entity。 */
@Entity(tableName = "pokemon_names")
data class PokemonNameEntity(
    @PrimaryKey val name: String,
)
