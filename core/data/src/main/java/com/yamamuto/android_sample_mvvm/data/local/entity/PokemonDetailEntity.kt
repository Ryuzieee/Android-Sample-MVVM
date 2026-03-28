package com.yamamuto.android_sample_mvvm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yamamuto.android_sample_mvvm.data.util.CACHE_DURATION_MS

/** ポケモン詳細のキャッシュ用 Entity。 */
@Entity(tableName = "pokemon_detail")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val baseExperience: Int,
    val types: String,
    val abilities: String,
    val imageUrl: String,
    val stats: String,
    val cachedAt: Long = System.currentTimeMillis(),
) {
    /** キャッシュが期限切れかどうかを返す。 */
    fun isExpired(): Boolean {
        return System.currentTimeMillis() - cachedAt >= CACHE_DURATION_MS
    }
}
