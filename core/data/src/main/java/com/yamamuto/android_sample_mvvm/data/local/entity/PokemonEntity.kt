package com.yamamuto.android_sample_mvvm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** ポケモン一覧のキャッシュ用 Entity。 */
@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val name: String,
    val url: String,
    val cachedAt: Long = System.currentTimeMillis(),
)
