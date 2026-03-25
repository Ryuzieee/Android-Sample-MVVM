package com.yamamuto.android_sample_mvvm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** お気に入りポケモンのキャッシュ用 Entity。 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val savedAt: Long = System.currentTimeMillis(),
)
